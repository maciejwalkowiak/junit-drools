##junit-drools##

JUnit + JBoss Drools integration

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
        public void should_set_drools_session() {
            assertNotNull(session);
            session.insert(new App());
            session.fireAllRules();
        }
    }