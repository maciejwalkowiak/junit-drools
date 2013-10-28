package pl.maciejwalkowiak.drools;

import org.drools.StatefulSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

import static org.junit.Assert.assertNotNull;

@RunWith(DroolsJUnitRunner.class)
@DroolsFiles(value = "helloworld.drl", location = "/drl/")
public class AppTest {

    @DroolsSession
    StatefulSession session;

    @Test
    public void testApp() {
        session.insert(new App());
        session.fireAllRules();

        assertNotNull(session);
    }
}