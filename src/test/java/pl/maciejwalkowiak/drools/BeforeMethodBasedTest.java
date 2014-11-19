package pl.maciejwalkowiak.drools;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

@DroolsFiles(value = "helloworld.drl", location = "/drl/")
public class BeforeMethodBasedTest {

    @DroolsSession KieSession session;

    @Before
    public void initDrools() throws Exception {
        new DroolsInjector().initDrools(this);
    }

    @Test
    public void should_set_discount() {
        Purchase purchase = new Purchase(new Customer(17));

        session.insert(purchase);
        session.fireAllRules();

        assertTrue(purchase.getTicket().isDiscount());
    }
}
