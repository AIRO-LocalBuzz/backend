package backend.airo.support.cache;

import io.micrometer.core.instrument.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

public class MeteredCacheManager implements CacheManager {
    private final CacheManager delegate;
    private final MeterRegistry registry;
    private final Map<String, Cache> caches = new ConcurrentHashMap<>();

    public MeteredCacheManager(CacheManager delegate, MeterRegistry registry) {
        this.delegate = delegate;
        this.registry = registry;
    }

    @Override public Cache getCache(@NotNull String name) {
        return caches.computeIfAbsent(name, n -> {
            Cache raw = delegate.getCache(n);
            return raw == null ? null : new MeteredCache(raw, registry, n);
        });
    }
    @NotNull
    @Override public Collection<String> getCacheNames() { return delegate.getCacheNames(); }

    static class MeteredCache implements Cache {
        private final Cache delegate;
        private final Counter hit, miss, puts;
        private final Timer loadTimer;
        MeteredCache(Cache d, MeterRegistry r, String name) {
            this.delegate = d;
            this.hit  = r.counter("cache.gets", "name", name, "result", "hit");
            this.miss = r.counter("cache.gets", "name", name, "result", "miss");
            this.puts = r.counter("cache.puts", "name", name);
            this.loadTimer = r.timer("cache.load", "name", name);
        }
        @NotNull
        @Override public String getName() { return delegate.getName(); }
        @NotNull
        @Override public Object getNativeCache() { return delegate.getNativeCache(); }

        @Override public ValueWrapper get(Object key) {
            ValueWrapper v = delegate.get(key);
            if (v == null) miss.increment(); else hit.increment();
            return v;
        }
        @Override public <T> T get(Object key, Class<T> type) {
            T v = delegate.get(key, type);
            if (v == null) miss.increment(); else hit.increment();
            return v;
        }
        @Override public <T> T get(@NotNull Object key, @NotNull Callable<T> valueLoader) {
            ValueWrapper v = delegate.get(key);
            if (v != null) { hit.increment(); return (T) v.get(); }
            miss.increment();
            return loadTimer.record(() -> {
                try { return delegate.get(key, valueLoader); }
                catch (Exception e) { throw new ValueRetrievalException(key, valueLoader, e); }
            });
        }
        @Override public void put(Object key, Object value) { delegate.put(key, value); puts.increment(); }
        @Override public ValueWrapper putIfAbsent(Object key, Object value) { return delegate.putIfAbsent(key, value); }
        @Override public void evict(Object key) { delegate.evict(key); }
        @Override public void clear() { delegate.clear(); }
    }
}
