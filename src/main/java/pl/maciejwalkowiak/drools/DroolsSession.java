package pl.maciejwalkowiak.drools;

import org.kie.internal.runtime.StatelessKnowledgeSession;

/**
 * Simplified interface of Drools {@link StatefulSession}
 *
 * @author Maciej Walkowiak
 */
public interface DroolsSession {
    void fire(String ruleName);

    void execute(Object object);

    StatelessKnowledgeSession getSession();
}
