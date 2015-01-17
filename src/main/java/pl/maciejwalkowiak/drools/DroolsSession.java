package pl.maciejwalkowiak.drools;

import org.kie.internal.runtime.StatefulKnowledgeSession;

/**
 * Simplified interface of Drools {@link StatefulSession}
 *
 * @author Maciej Walkowiak
 */
public interface DroolsSession {
    void fire(String ruleName);

    void fireAllRules();

    void insert(Object object);

    StatefulKnowledgeSession getStatefulSession();
}