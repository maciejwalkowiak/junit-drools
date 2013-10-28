package pl.maciejwalkowiak.drools;

import org.drools.StatefulSession;

public interface DroolsSession {
    void fire(String ruleName);

    void fireAllRules();

    void insert(Object object);

    StatefulSession getStatefulSession();
}
