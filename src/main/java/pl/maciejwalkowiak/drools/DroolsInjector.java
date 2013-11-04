package pl.maciejwalkowiak.drools;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.DroolsError;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

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
        DroolsAnnotationProcessor annotationProcessor = new DroolsAnnotationProcessor(testClass);
        DroolsFiles droolsFiles = annotationProcessor.getDroolsFiles();

        DroolsSession droolsSession = initKnowledgeBase(droolsFiles.location(), Arrays.asList(droolsFiles.value()));

        annotationProcessor.setDroolsSession(droolsSession);
    }

    public DroolsSession initKnowledgeBase(String droolsLocation, Iterable<String> fileNames) throws Exception {
        PackageBuilder builder = new PackageBuilder();

        for (String fileName : fileNames) {
            builder.addPackageFromDrl(loadDroolFile(droolsLocation, fileName));
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
        return new InputStreamReader(getClass().getResourceAsStream(droolsLocation + filename));
    }
}
