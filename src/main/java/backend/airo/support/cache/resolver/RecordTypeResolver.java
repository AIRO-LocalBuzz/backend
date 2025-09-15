package backend.airo.support.cache.resolver;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

public class RecordTypeResolver extends ObjectMapper.DefaultTypeResolverBuilder {
    public RecordTypeResolver(ObjectMapper.DefaultTyping t, PolymorphicTypeValidator ptv) {
        super(t, ptv);
    }

    public boolean useForType(JavaType t) {
        boolean isRecord = t.getRawClass().isRecord();
        boolean superResult = super.useForType(t);

        if (isRecord) {
            return true;
        }
        return superResult;
    }
}
