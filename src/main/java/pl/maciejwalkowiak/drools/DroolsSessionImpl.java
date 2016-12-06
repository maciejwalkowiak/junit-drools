package pl.maciejwalkowiak.drools;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Can be injected instead of {@link StatefulKnowledgeSession} in test classes to improve readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class DroolsSessionImpl implements DroolsSession {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsSessionImpl.class);

	private StatefulKnowledgeSession session;

	public DroolsSessionImpl(StatefulKnowledgeSession session) {
		this.session = session;
	}
	
	
    @Override
    public void fire(String ruleName) {
        LOG.debug("Firing rule: {}", ruleName);

        this.session.insert(new RuleNameEqualsAgendaFilter(ruleName));
        this.session.fireAllRules();
    }

    @Override
    public void execute(Object object) {
	    this.session.insert(object);
        this.session.fireAllRules();
    }

    @Override
    public StatefulKnowledgeSession getSession() {
        return session;
    }
}
