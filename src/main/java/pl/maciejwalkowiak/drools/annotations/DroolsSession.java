package pl.maciejwalkowiak.drools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to inject Drools {@link org.kie.api.runtime.KieSession}
 * or {@link pl.maciejwalkowiak.drools.DroolsSession} depending on what type annotated property is
 *
 * @author Maciej Walkowiak
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DroolsSession {
}
