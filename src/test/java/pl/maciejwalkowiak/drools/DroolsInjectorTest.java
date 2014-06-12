package pl.maciejwalkowiak.drools;

import org.junit.Test;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;

public class DroolsInjectorTest {
    @Test
    public void should_throw_exception_for_null_class() throws Exception {
        catchException(new DroolsInjector()).initDrools(null);

        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("null");
        System.out.println(caughtException());
    }

    @Test
    public void should_throw_exception_for_file_not_found() throws Exception {
        NotExistingFileTestClass testClass = new NotExistingFileTestClass();

        catchException(new DroolsInjector()).initDrools(testClass);

        assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("not found");
        System.out.println(caughtException());
    }

    @Test
    public void foo() throws Exception {
        RulesWithErrorsTestClass testClass = new RulesWithErrorsTestClass();

        catchException(new DroolsInjector()).initDrools(testClass);        
        assertThat(caughtException()).isInstanceOf(IllegalStateException.class).hasMessageContaining("errors in DRL files");
        assertThat(caughtException()).hasMessageContaining("mismatched input");
        System.out.println(caughtException());
        
    }

    @DroolsFiles(value = "foo.drl", location = "/")
    private static class NotExistingFileTestClass {

    }

    @DroolsFiles(value = "rules-with-errors.drl", location = "/drl/")
    private static class RulesWithErrorsTestClass {

    }
}
