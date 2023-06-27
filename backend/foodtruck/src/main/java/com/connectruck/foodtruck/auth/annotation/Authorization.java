package com.connectruck.foodtruck.auth.annotation;

import com.connectruck.foodtruck.user.domain.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Authentication
public @interface Authorization {

    Role value();
}
