package pl.maciejwalkowiak.drools;

import pl.maciejwalkowiak.drools.model.Customer;
import pl.maciejwalkowiak.drools.model.Purchase;
import org.junit.Test;
import org.junit.Before;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;
import static org.junit.Assert.assertTrue;
import org.kie.api.runtime.KieSession;

@DroolsFiles(value = "helloworld.drl", location = "/drl/")
public class BeforeMethodBasedTest {
    @DroolsSession
    KieSession session;

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
