package pl.maciejwalkowiak.drools;

import org.drools.compiler.compiler.DroolsError;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.compiler.compiler.PackageBuilderErrors;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.StatefulSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Initializes Drools knowledge base and {@link StatefulSession} and injects them to test class
 *
 * @author Maciej Walkowiak
 */
public class DroolsInjector {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsInjector.class);

    public void initDrools(Object testClass) throws Exception {
        if (testClass == null) {
            throw new IllegalArgumentException("Test class cannot be null");
        }

        LOG.info("Initializing Drools objects for test class: {}", testClass.getClass());

        DroolsAnnotationProcessor annotationProcessor = new DroolsAnnotationProcessor(testClass);
        DroolsFiles droolsFiles = annotationProcessor.getDroolsFiles();

        DroolsSession droolsSession = 
          initKnowledgeBase(droolsFiles.location(), droolsFiles.dsl(), Arrays.asList(droolsFiles.value()));

        annotationProcessor.setDroolsSession(droolsSession);
    }

    private DroolsSession initKnowledgeBase(String droolsLocation, String dsl, Iterable<String> fileNames) throws Exception {

        PackageBuilder builder = new PackageBuilder();

        if(dsl == null || dsl.equals("")) {
            LOG.info("Initializing knowledge base for drl files located in {} with names: {}", droolsLocation, fileNames);
            for (String fileName : fileNames) {
                builder.addPackageFromDrl(loadDroolFile(droolsLocation, fileName));
            }
        } else {
            LOG.info("Initializing knowledge base for drl files located in {} with dsl {}  with names: {}", droolsLocation, dsl, fileNames);
            for (String fileName : fileNames) {
                builder.addPackageFromDrl(loadDroolFile(droolsLocation, fileName),
                                          loadDroolFile(droolsLocation, dsl) );
            }
        }
        PackageBuilderErrors errors = builder.getErrors();

        // Make sure that there are no errors in knowledge base
        if (errors.getErrors().length > 0) {
            LOG.error("Errors during loading DRL files");

            for (DroolsError error : errors.getErrors()) {
                LOG.error("Error: {}", error.getMessage());
            }

            throw new IllegalStateException("There are errors in DRL files");
        }

        RuleBase ruleBase  = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage(builder.getPackage());

        StatefulSession session = ruleBase.newStatefulSession(false);

        return new DroolsSessionImpl(session);
    }

    private InputStreamReader loadDroolFile(String droolsLocation, String filename) {
        InputStream stream = getClass().getResourceAsStream(droolsLocation + filename);

        if (stream == null) {
            throw new IllegalArgumentException("File not found in location: " + droolsLocation + filename);
        }
        return new InputStreamReader(stream);
    }
}
