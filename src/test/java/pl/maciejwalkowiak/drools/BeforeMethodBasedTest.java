package pl.maciejwalkowiak.drools;

import org.junit.Before;
import org.junit.Test;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

import static org.junit.Assert.assertTrue;

@DroolsFiles(value = "helloworld.drl", location = "drl/")
public class BeforeMethodBasedTest {
    @DroolsSession
    StatefulKnowledgeSession session;

    @Before
    public void initDrools() throws Exception {
        new DroolsInjector().initDrools(this);
    }

    @Test
    public void should_set_discount() {
        Purchase purchase = new Purchase(new Customer(17));

        session.insert(purchase);
        session.fireAllRules();

        System.out.println("isDiscount?: " + purchase.getTicket().isDiscount());
        
        assertTrue(purchase.getTicket().isDiscount());
    }
}
