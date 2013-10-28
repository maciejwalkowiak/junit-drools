package pl.maciejwalkowiak.drools;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

import java.io.InputStreamReader;
import java.util.Arrays;

public class DroolsInjector {
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
            throw new IllegalStateException();
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
