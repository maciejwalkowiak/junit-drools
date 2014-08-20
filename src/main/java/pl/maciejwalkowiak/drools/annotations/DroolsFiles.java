package pl.maciejwalkowiak.drools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables loading DRL files to knowledge base
 *
 * @author Maciej Walkowiak
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DroolsFiles {

    /**
     * DRL file names
     * @return
     */
    String[] value();

    /**
     * DSL file names
     * @return
     */
    String dsl() default "";

    /**
     * DRL files location relative to src/test/resources or src/main/resources
     * @return
     */
    String location();
}
