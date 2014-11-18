package pl.maciejwalkowiak.drools;

import org.kie.api.runtime.KieSession;

/**
 * Simplified interface of Drools {@link KieSession}
 *
 * @author Maciej Walkowiak
 */
public interface DroolsSession {

    void fire(String ruleName);

    void fireAllRules();

    void insert(Object object);

    void retract();

    KieSession getStatefulSession();
}
