package pl.maciejwalkowiak.drools;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;

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

    	KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    	   
        if(dsl == null || dsl.equals("")) {
            LOG.info("Initializing knowledge base for drl files located in {} with names: {}", droolsLocation, fileNames);
            for(String f : fileNames) {
            	kbuilder.add( ResourceFactory.newClassPathResource(droolsLocation + f), ResourceType.DRL );
            }
        } else {
            LOG.info("Initializing knowledge base for drl files located in {} with dsl {}  with names: {}", droolsLocation, dsl, fileNames);

        	kbuilder.add( ResourceFactory.newClassPathResource(droolsLocation + dsl), ResourceType.DRL );
        	
        }
        KnowledgeBuilderErrors errors = kbuilder.getErrors();

        // Make sure that there are no errors in knowledge base
        for (KnowledgeBuilderError e : errors) {
            LOG.error("Errors during loading DRL files");

            LOG.error("Error: {}", e.getMessage());

            throw new IllegalStateException("There are errors in DRL files");
        }

        StatelessKnowledgeSession session = kbuilder.newKnowledgeBase().newStatelessKnowledgeSession();

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
