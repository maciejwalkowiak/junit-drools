package pl.maciejwalkowiak.drools;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * JUnit runner that does whole magic using {@link DroolsInjector}
 *
 * @author Maciej Walkowiak
 */
public class DroolsJUnitRunner extends BlockJUnit4ClassRunner {

    public DroolsJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        Object testObject = super.createTest();
        new DroolsInjector().initDrools(testObject);

        return testObject;
    }
}
