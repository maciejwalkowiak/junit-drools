package pl.maciejwalkowiak.drools;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

@RunWith(DroolsJUnitRunner.class)
@DroolsFiles(value = "helloworld.drl", location = "/drl/")
public class RunnerBasedTest {

    @DroolsSession KieSession session;

    @Test
    public void should_set_discount() {
        Purchase purchase = new Purchase(new Customer(17));

        session.insert(purchase);
        session.fireAllRules();

        assertTrue(purchase.getTicket().isDiscount());
    }

    @Test
    public void should_not_set_discount() {
        Purchase purchase = new Purchase(new Customer(22));

        session.insert(purchase);
        session.fireAllRules();

        assertFalse(purchase.getTicket().isDiscount());
    }
}