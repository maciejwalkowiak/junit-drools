package pl.maciejwalkowiak.drools;

import org.drools.StatefulSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(DroolsJUnitRunner.class)
@DroolsFiles(value = "helloworld.drl", location = "/drl/")
public class AppTest {

    @DroolsSession
    pl.maciejwalkowiak.drools.DroolsSession session;

    @DroolsSession
    StatefulSession statefulSession;

    @Test
    public void testApp() {
        session.insert(new App());
        session.fire("Hello world");

        assertTrue(true);
        assertNotNull(session);
        assertNotNull(statefulSession);
        assertEquals(session.getStatefulSession(), statefulSession);
    }

    @Test
    public void foo() {

    }
}