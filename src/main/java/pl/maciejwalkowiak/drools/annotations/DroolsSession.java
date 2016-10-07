package pl.maciejwalkowiak.drools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

/**
 * Used to inject Drools {@link KieSession}, {@link StatelessKieSession}
 *  or {@link pl.maciejwalkowiak.drools.DroolsSession} depending on what type 
 *  annotated property is
 *
 * @author Maciej Walkowiak
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DroolsSession {
    
    /**
     * Should it be a stateless session
     * @return true if Stateless session
     */
    boolean stateless() default false;
}
