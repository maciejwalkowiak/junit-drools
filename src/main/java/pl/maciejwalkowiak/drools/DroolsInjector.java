package pl.maciejwalkowiak.drools;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

/**
 * Initializes Drools knowledge base and {@link StatefulSession} and injects them to test class
 *
 * @author Maciej Walkowiak
 */
public class DroolsInjector {
    private static Logger LOG = LoggerFactory.getLogger(DroolsInjector.class);
    private static boolean logEnabled = true;

    public void initDrools(Object testClass, boolean disableLog) throws Exception {
        logEnabled = !disableLog;
        initDrools(testClass);
    }

    public void initDrools(Object testClass) throws Exception {
        if (testClass == null) {
            throw new IllegalArgumentException("Test class cannot be null");
        }

        if (logEnabled)
            LOG.info("Initializing Drools objects for test class: {}", testClass.getClass());

        DroolsAnnotationProcessor annotationProcessor = new DroolsAnnotationProcessor(testClass);
        DroolsFiles droolsFiles = annotationProcessor.getDroolsFiles();

        DroolsSession droolsSession = 
          initKnowledgeBase(droolsFiles.location(), droolsFiles.dsl(), Arrays.asList(droolsFiles.value()));

        annotationProcessor.setDroolsSession(droolsSession);
    }

    private DroolsSession initKnowledgeBase(String droolsLocation, String dsl, Iterable<String> fileNames) throws Exception {
    	
    	KieServices kieServices = KieServices.Factory.get();
    	KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    	
        if(dsl == null || dsl.equals("")) {
            if (logEnabled)
                LOG.info("Initializing knowledge base for drl files located in {} with names: {}", droolsLocation, fileNames);
            for (String fileName : fileNames) {
            	kieFileSystem.write(
            		ResourceFactory.newFileResource( loadDroolFileAsFile(droolsLocation, fileName) ));
            }
        } else {
            if (logEnabled)
                LOG.info("Initializing knowledge base for drl files located in {} with dsl {}  with names: {}", droolsLocation, dsl, fileNames);
            for (String fileName : fileNames) {
            	kieFileSystem
            		.write(
            			ResourceFactory.newFileResource( loadDroolFileAsFile(droolsLocation, fileName)) )
            		.write(
            			ResourceFactory.newFileResource( loadDroolFileAsFile(droolsLocation, dsl)) );
            }
        }
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();

        // Make sure that there are no errors in knowledge base
    	if (kieBuilder.getResults().hasMessages(Level.ERROR)) {
            if (logEnabled) {
                LOG.error("Errors during loading DRL files");

                for (Message error : kieBuilder.getResults().getMessages(Level.ERROR)) {
                    LOG.error("Error: {}", error);
                }
            }

            throw new IllegalStateException("There are errors in DRL files");
    	}

    	KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    	StatefulKnowledgeSession kieSession = (StatefulKnowledgeSession) kieContainer.newKieSession();
        
        return new DroolsSessionImpl(kieSession);
    }
    
    private File loadDroolFileAsFile(String droolsLocation, String filename) {
        URL resource = getClass().getResource(droolsLocation + filename);
        if (resource == null) {
            throw new IllegalArgumentException("File not found in location: " + droolsLocation + filename);
        }
        
        File file = null;
        try {
			file = new File(resource.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return file;
    }
}