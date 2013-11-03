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

### Usage ###

**junit-drools** provides **DroolsJUnitRunner** class that handles most boilerplate code you need to write to set up knowledge base and Drools session. 
    
Lets consider following example:    

    @RunWith(DroolsJUnitRunner.class)
    @DroolsFiles(value = "helloworld.drl", location = "/drl/")
    public class AppTest {
    
        @DroolsSession
        StatefulSession session;
    
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

- **@RunWith(DroolsJUnitRunner)** - inits JUnit runner for testing drools rules
- **@DroolsFiles** - set location of drl files (can be one or multiple) - drl files have to be on class path - **@DroolsFiles#location** is relative to ```src/test/resources``` or ```src/main/resources```
- **@DroolsSession** - autoinjects Drools session to your test before execution

Find full example with drl file in [src/test](https://github.com/maciejwalkowiak/junit-drools/tree/master/src/test) directory of the project
