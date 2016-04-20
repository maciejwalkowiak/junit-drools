package pl.maciejwalkowiak.drools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.internal.runtime.StatelessKnowledgeSession;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

@RunWith(DroolsJUnitRunner.class)
@DroolsFiles(value = "helloworld.drl", location = "drl/")
public class RunnerBasedTest {

    @DroolsSession
    StatelessKnowledgeSession session;

    @Test
    public void should_set_discount() {
        Purchase purchase = new Purchase(new Customer(17));

        session.execute(purchase);

        assertTrue(purchase.getTicket().isDiscount());
    }

    @Test
    public void should_not_set_discount() {
        Purchase purchase = new Purchase(new Customer(22));

        session.execute(purchase);

        assertFalse(purchase.getTicket().isDiscount());
    }
}