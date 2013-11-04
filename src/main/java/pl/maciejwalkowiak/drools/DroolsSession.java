package pl.maciejwalkowiak.drools;

import org.drools.StatefulSession;

/**
 * Simplified interface of Drools {@link StatefulSession}
 *
 * @author Maciej Walkowiak
 */
public interface DroolsSession {
    void fire(String ruleName);

    void fireAllRules();

    void insert(Object object);

    StatefulSession getStatefulSession();
}
