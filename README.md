##junit-drools##

[![Build Status](https://drone.io/github.com/maciejwalkowiak/junit-drools/status.png)](https://drone.io/github.com/maciejwalkowiak/junit-drools/latest)

Tiny little library that helps you to unit test your Drools based business rules. Few simple annotations making Drools unit tests clean an easily maintainable.


### Preface ###

Testing Drools rules can be annoying. Framework itself does not contain any helper classes or JUnit integrations that saves us from writing lots of boilerplate code in each test unit class. 

Example of Drools unit test taken from [Drools JBoss Rules 5.X Developer's Guide](https://code.google.com/p/droolsbook) - this it **NOT** how we want to write unit tests:

    public class ValidationTest {
      static StatelessKnowledgeSession session;
      
      @BeforeClass
      public static void setUpClass() throws Exception {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        builder.add(ResourceFactory.newClassPathResource("validation.drl"), ResourceType.DRL);
        if (builder.hasErrors()) {
          throw new RuntimeException(builder.getErrors().toString());
        }
        
        KnowledgeBaseConfiguration configuration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        configuration.setOption(SequentialOption.YES);
    
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(configuration);
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
        
        session = knowledgeBase.newStatelessKnowledgeSession();
      }
      
      ....
    }


**junit-drools**'s goal is to fix it and make testing Drools easy.

I am not very experienced with Drools so the library actually does what was needed in project I was working with. You are welcome to add your improvements by sending pull requests.

### Installation ###

1. Clone git repository: `git clone https://github.com/maciejwalkowiak/junit-drools.git`
2. Build & install: `mvn clean install`
3. Add repository and dependency to your pom.xml:

```
    <repository>
        <id>maciejwalkowiak.pl</id>
        <url>https://github.com/maciejwalkowiak/maven-repo/raw/releases/</url>
    </repository>
```
        
```
    <dependency>
        <groupId>pl.maciejwalkowiak</groupId>
        <artifactId>junit-drools</artifactId>
        <version>1.0</version>
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
- **@DroolsFiles** - set location of drl files (can be one or multiple) - drl files have to be on class path - **@DroolsFiles#dsl is an optional DSL used for building the drl files - **@DroolsFiles#location** is relative to ```src/test/resources``` or ```src/main/resources```
- **@DroolsSession** - autoinjects Drools session to your test before execution

Find full example with drl file in [src/test](https://github.com/maciejwalkowiak/junit-drools/tree/master/src/test) directory of the project

In case you don't want to use DroolsJUnitRunner, for example because you already want to use Mockito or Spring runner you can initialize Drools objects in @Before method:

    @DroolsFiles(value = "helloworld.drl", location = "/drl/")
    public class BeforeMethodBasedTest {
        @DroolsSession
        StatefulSession session;
    
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


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/maciejwalkowiak/junit-drools/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

