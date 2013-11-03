##junit-drools##

**Under development**

Tiny little library that helps you to unit test your Drools based business rules.


### Preface ###

Testing Drools rules can be annoying. Framework itself does not contain any helper classes or JUnit integrations that saves us from writing lots of boilerplate code in each test unit class. **junit-drools**'s goal is to fix it and make testing Drools easy.

I am not very experienced with Drools so the library actually does what was needed in project I was working with. You are welcome to add your improvements by sending pull requests.

### Installation ###

1. Clone git repository: `git clone https://github.com/maciejwalkowiak/junit-drools.git`
2. Build & install: `mvn clean install`
3. Add dependency to your pom.xml:
        
```
    <dependency>
        <groupId>pl.maciejwalkowiak</groupId>
        <artifactId>junit-drools</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>
```
    
### Example unit test ###

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
    
### How-tos ###

- drl files have to be on class path - **@DroolsFiles#location** is relative to ```src/test/resources``` or ```src/main/resources```
