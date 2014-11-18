package pl.maciejwalkowiak.drools;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Can be injected instead of {@link org.kie.api.runtime.KieSession} in test classes to improve readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class DroolsSessionImpl implements DroolsSession {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsSessionImpl.class);

    private KieSession kieSession;

    private List<FactHandle> handles;

    public DroolsSessionImpl(KieSession session) {
        this.kieSession = session;
        this.handles = new ArrayList<FactHandle>();
    }

    public void fire(String ruleName) {
        LOG.debug("Firing rule: {}", ruleName);
        this.kieSession.fireAllRules(new RuleNameEqualsAgendaFilter(ruleName));
    }

    public void fireAllRules() {
        this.kieSession.fireAllRules();
    }

    public void insert(Object object) {
        handles.add(this.kieSession.insert(object));
    }

    public void retract() {
        for (FactHandle handle : handles) {
            kieSession.delete(handle);
            handles.remove(handle);
        }
    }

    public KieSession getStatefulSession() {
        return kieSession;
    }
}
