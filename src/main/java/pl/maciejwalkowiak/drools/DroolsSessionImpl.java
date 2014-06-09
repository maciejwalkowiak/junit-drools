package pl.maciejwalkowiak.drools;

import org.drools.core.StatefulSession;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Can be injected instead of {@link StatefulSession} in test classes to improve readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class DroolsSessionImpl implements DroolsSession {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsSessionImpl.class);

    private StatefulSession statefulSession;

    public DroolsSessionImpl(StatefulSession statefulSession) {
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
    public StatefulSession getStatefulSession() {
        return statefulSession;
    }
}
