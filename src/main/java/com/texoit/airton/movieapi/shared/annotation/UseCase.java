package com.texoit.airton.movieapi.shared.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * Annotation para marcar classes como Use Cases na arquitetura limpa.
 * Automaticamente registra a classe como um componente Spring.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface UseCase {

    @AliasFor(annotation = Component.class)
    String value() default "";
}