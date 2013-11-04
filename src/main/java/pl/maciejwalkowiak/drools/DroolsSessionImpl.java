package pl.maciejwalkowiak.drools;

import org.drools.StatefulSession;
import org.drools.base.RuleNameEqualsAgendaFilter;

/**
 * Can be injected instead of {@link StatefulSession} in test classes to improve readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class DroolsSessionImpl implements DroolsSession {
    private StatefulSession statefulSession;

    public DroolsSessionImpl(StatefulSession statefulSession) {
        this.statefulSession = statefulSession;
    }

    @Override
    public void fire(String ruleName) {
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
