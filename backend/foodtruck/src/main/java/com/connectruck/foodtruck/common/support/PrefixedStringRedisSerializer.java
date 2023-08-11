package com.connectruck.foodtruck.common.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class PrefixedStringRedisSerializer extends StringRedisSerializer {

    private final String prefix;

    public PrefixedStringRedisSerializer(@Value("${connectruck.redis.key-prefix}") final String prefix) {
        this.prefix = prefix + ":";
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return super.serialize(prefix + string);
    }
}
