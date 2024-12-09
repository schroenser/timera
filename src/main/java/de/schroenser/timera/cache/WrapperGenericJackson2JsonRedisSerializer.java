package de.schroenser.timera.cache;

import java.lang.reflect.Modifier;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.databind.ObjectMapper;

class WrapperGenericJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer
{
    public WrapperGenericJackson2JsonRedisSerializer(ObjectMapper mapper)
    {
        super(mapper);
    }

    @Override
    public byte[] serialize(Object value) throws SerializationException
    {
        var tempValue = value;

        if (value != null)
        {
            if (Modifier.isFinal(value.getClass()
                .getModifiers()))
            {
                tempValue = new Wrapper(value);
            }
        }

        return super.serialize(tempValue);
    }

    @Override
    public Object deserialize(byte[] source) throws SerializationException
    {
        var value = super.deserialize(source);

        if (value instanceof Wrapper)
        {
            return ((Wrapper) value).getValue();
        }
        else
        {
            return value;
        }
    }
}
