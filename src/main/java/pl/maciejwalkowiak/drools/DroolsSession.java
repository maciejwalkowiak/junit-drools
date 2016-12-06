package pl.maciejwalkowiak.drools;

import org.kie.internal.runtime.StatefulKnowledgeSession;

/**
 * Simplified interface of Drools {@link StatefulKnowledgeSession}
 *
 * @author Maciej Walkowiak
 */
public interface DroolsSession {
    void fire(String ruleName);

    void execute(Object object);

    StatefulKnowledgeSession getSession();
}
