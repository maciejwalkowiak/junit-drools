package pl.maciejwalkowiak.drools;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

/**
 * Initializes Drools knowledge base and injects sessions to test class
 *
 * @author Maciej Walkowiak
 */
public class DroolsInjector {

    private static final Logger LOG = LoggerFactory.getLogger(DroolsInjector.class);

    private DroolsAnnotationProcessor annotationProcessor;
    
    public void initDrools(Object testClass) throws Exception {
        if (testClass == null) {
           throw new IllegalArgumentException("Test class cannot be null");
        }

        LOG.info("Initializing Drools objects for test class: {}", testClass.getClass());

        annotationProcessor = new DroolsAnnotationProcessor(testClass);
        DroolsFiles droolsFiles = annotationProcessor.getDroolsFiles();

        KieContainer container
                = initKnowledgeBase(droolsFiles.location(), droolsFiles.dsl(), Arrays.asList(droolsFiles.value()));

        annotationProcessor.setDroolsSession(container);
    }
    
    public void cleanupDrools() throws Exception {
        annotationProcessor.disposeSession();
    }
    
    private KieContainer initKnowledgeBase(String droolsLocation, String dsl, Iterable<String> fileNames) throws Exception {

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        if (dsl == null || dsl.equals("")) {
            LOG.info("Initializing knowledge base for drl files located in {} with names: {}", droolsLocation, fileNames);
            for (String fileName : fileNames) {
                kieFileSystem.write(
                        ResourceFactory.newFileResource(loadDroolFileAsFile(droolsLocation, fileName)));
            }
        } else {
            LOG.info("Initializing knowledge base for drl files located in {} with dsl {}  with names: {}", droolsLocation, dsl, fileNames);
            for (String fileName : fileNames) {
                kieFileSystem
                        .write(
                                ResourceFactory.newFileResource(loadDroolFileAsFile(droolsLocation, fileName)))
                        .write(
                                ResourceFactory.newFileResource(loadDroolFileAsFile(droolsLocation, dsl)));
            }
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();

        // Make sure that there are no errors in knowledge base
        if (kieBuilder.getResults().hasMessages(Level.ERROR)) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Errors during loading DRL files");

                for (Message error : kieBuilder.getResults().getMessages(Level.ERROR)) {
                    LOG.error("Error: {}", error);
                }
            }

            throw new IllegalStateException("There are errors in DRL files");
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    	
        return kieContainer;
    }

    private File loadDroolFileAsFile(String droolsLocation, String filename) {
        // Remove any leading / or \
        if (droolsLocation != null && droolsLocation.matches("^[/\\\\]+.*")) {
            droolsLocation = droolsLocation.replaceFirst("^[/\\\\]+", "");
        }
        
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(droolsLocation + filename);
        
        if (resource == null) {
            throw new IllegalArgumentException("File not found in location: " + droolsLocation + filename);
        }

        File file = new File(resource.getFile());

        return file;
    }
}
