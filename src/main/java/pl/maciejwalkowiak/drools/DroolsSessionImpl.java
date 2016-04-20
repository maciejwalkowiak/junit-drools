package pl.maciejwalkowiak.drools;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.kie.internal.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Can be injected instead of {@link StatefulSession} in test classes to improve readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class DroolsSessionImpl implements DroolsSession {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsSessionImpl.class);

//    private StatefulKnowledgeSession statefulSession;
	private StatelessKnowledgeSession session;

//    public DroolsSessionImpl(StatefulKnowledgeSession statefulSession) {
//        this.statefulSession = statefulSession;
//    }
	public DroolsSessionImpl(StatelessKnowledgeSession session) {
		this.session = session;
	}
	
	
    @Override
    public void fire(String ruleName) {
        LOG.debug("Firing rule: {}", ruleName);

        this.session.execute(new RuleNameEqualsAgendaFilter(ruleName));
    }

    @Override
    public void execute(Object object) {
        this.session.execute(object);
    }

    @Override
    public StatelessKnowledgeSession getSession() {
        return session;
    }
}
