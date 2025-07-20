package backend.airo.domain;

import java.util.Collection;

public interface AggregateSupport<T, ID> {

    T save(T aggregate);

    Collection<T> saveAll(Collection<T> aggregates);

    T findById(ID id);
}