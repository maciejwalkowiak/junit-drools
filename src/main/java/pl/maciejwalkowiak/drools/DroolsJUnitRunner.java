package pl.maciejwalkowiak.drools;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * JUnit runner that does whole magic using {@link DroolsInjector}
 *
 * @author Maciej Walkowiak
 */
public class DroolsJUnitRunner extends BlockJUnit4ClassRunner {

    private final DroolsInjector droolsInjector;
    
    public DroolsJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        droolsInjector = new DroolsInjector();
    }

    @Override
    protected Object createTest() throws Exception {
        Object testObject = super.createTest();
        droolsInjector.initDrools(testObject);
        return testObject;
    }

    /**
     * For each test, end up by cleaning up drools stuff
     * {@inheritDoc}
     */
    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        final Statement statement = super.methodBlock(method);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                // About to run
                try {
                    statement.evaluate();
                } finally {
                    // Done running
                    droolsInjector.cleanupDrools();
                }
            }
        };
    }
}
