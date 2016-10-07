package pl.maciejwalkowiak.drools.impl;

import java.util.Collection;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maciejwalkowiak.drools.DroolsSession;

/**
 * Can be injected instead of {@link KieSession} in test classes to improve 
 *  readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class StatefulDroolsSession implements DroolsSession {
    private static final Logger LOG = LoggerFactory.getLogger(StatefulDroolsSession.class);

    private final KieSession statefulSession;

    public StatefulDroolsSession(KieSession statefulSession) {
        this.statefulSession = statefulSession;
    }

    @Override
    public void fire(String ruleName) {
        LOG.debug("Firing rule: {}", ruleName);

        this.statefulSession.fireAllRules(new RuleNameEqualsAgendaFilter(ruleName));
    }

    @Override
    public void fireAllRules() {
        this.statefulSession.fireAllRules();
    }

    @Override
    public void insert(Object object) {
        this.statefulSession.insert(object);
    }

    @Override
    public void execute(Object object) {
        this.insert(object);
        this.fireAllRules();
    }

    @Override
    public void execute(Collection collection) {
        for (Object object : collection) {
            this.insert(object);
        }
        this.fireAllRules();
    }

    @Override
    public KieSession getSession() {
        return statefulSession;
    }

    @Override
    public StatelessKieSession getStatelessSession() {
        throw new UnsupportedOperationException("This is a stateful session.");
    }
}