package pl.maciejwalkowiak.drools;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.KieResources;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

/**
 * Initializes Drools knowledge base and {@link org.kie.api.runtime.KieContainer} and injects them to test class
 *
 * @author Maciej Walkowiak
 */
public class DroolsInjector {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsInjector.class);

    private KieServices kieServices = KieServices.Factory.get();

    public void initDrools(Object testClass) throws Exception {
        if (testClass == null) {
            throw new IllegalArgumentException("Test class cannot be null");
        }
        LOG.info("Initializing Drools objects for test class: {}", testClass.getClass());

        DroolsAnnotationProcessor annotationProcessor = new DroolsAnnotationProcessor(testClass);
        DroolsFiles droolsFiles = annotationProcessor.getDroolsFiles();
        List<String> droolsList = new LinkedList<String>(Arrays.asList(droolsFiles.value()));
        DroolsSession droolsSession = initKnowledgeBase(droolsFiles.location(), droolsFiles.dsl(), droolsList);
        annotationProcessor.setDroolsSession(droolsSession);
    }

    private DroolsSession initKnowledgeBase(String droolsLocation, String dsl, List<String> fileNames) throws Exception {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        KieResources kieResources = kieServices.getResources();

        droolsLocation = (droolsLocation.endsWith("/"))   ? droolsLocation : droolsLocation + "/";
        droolsLocation = (droolsLocation.startsWith("/")) ? droolsLocation : "/" + droolsLocation;

        if(dsl == null || dsl.equals("")) {
            LOG.info("Initializing knowledge base for drl files located in {} with names: {}", droolsLocation, fileNames);
        } else {
            LOG.info("Initializing knowledge base for drl files located in {} with dsl {} with names: {}", droolsLocation, dsl, fileNames);
            fileNames.add(dsl);
        }

        for (String filename : fileNames) {
            InputStream stream = getClass().getResourceAsStream(droolsLocation + filename);
            if (stream == null) {
                throw new IllegalArgumentException("File not found in location: " + droolsLocation + filename);
            }
            kieFileSystem.write("src/main/resources" + droolsLocation + filename, kieResources.newInputStreamResource(stream));
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            LOG.error(kieBuilder.getResults().toString());
            throw new IllegalStateException("There are errors in DRL files");
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId());
        return new DroolsSessionImpl(kieContainer.newKieSession());
    }
}