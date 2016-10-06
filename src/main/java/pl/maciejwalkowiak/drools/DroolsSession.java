package pl.maciejwalkowiak.drools;

import java.util.Collection;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

/**
 * Simplified interface of Drools session
 *
 * @author Maciej Walkowiak
 */
public interface DroolsSession {
    void fire(String ruleName);

    void fireAllRules();

    void insert(Object object);

    void execute(Object object);
    void execute(Collection collection);
    
    KieSession getSession();
    
    StatelessKieSession getStatelessSession();
}