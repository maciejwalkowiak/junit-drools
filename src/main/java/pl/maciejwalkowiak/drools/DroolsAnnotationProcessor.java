package pl.maciejwalkowiak.drools;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

import java.lang.reflect.Field;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maciejwalkowiak.drools.impl.StatefulDroolsSession;
import pl.maciejwalkowiak.drools.impl.StatelessDroolsSession;

/**
 * Processes test class annotations
 *
 * @author Maciej Walkowiak
 */
public class DroolsAnnotationProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(DroolsAnnotationProcessor.class);
    
    private final Object testClass;

    public DroolsAnnotationProcessor(Object testClass) {
        this.testClass = testClass;
    }

    /**
     * Get drools files annotation
     * @return DroolsFiles
     */
    public DroolsFiles getDroolsFiles() {
        DroolsFiles droolsFiles = testClass.getClass().getAnnotation(DroolsFiles.class);

        if (droolsFiles == null) {
            throw new IllegalStateException("DroolsFiles annotation not set");
        }

        return droolsFiles;
    }

    /**
     * Set a session for each annotated field
     * @param container KieContainer
     */
    public void setDroolsSession(KieContainer container) {
        for (Field field : testClass.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(DroolsSession.class)) {
                Object session = getSessionToSet(container, field);

                try {
                    field.set(testClass, session);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    /**
     * Dispose annotated stateful sessions
     * @throws Exception problems accessing fields
     */
    public void disposeSession() throws Exception {
        if (testClass == null) {
            return;
        }
        for (Field field : testClass.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(pl.maciejwalkowiak.drools.annotations.DroolsSession.class)) {
                if (field.getType().equals(pl.maciejwalkowiak.drools.DroolsSession.class)) {
                    pl.maciejwalkowiak.drools.annotations.DroolsSession annotation = field.getAnnotation(pl.maciejwalkowiak.drools.annotations.DroolsSession.class); 
                    if (!annotation.stateless()) {
                        pl.maciejwalkowiak.drools.DroolsSession session = (pl.maciejwalkowiak.drools.DroolsSession)field.get(testClass);
                        if (session!=null && session.getSession()!=null) {
                            LOG.debug("Automatic session disposal");
                            session.getSession().dispose();
                        }
                    }
                }
                //else if (field.getType().equals(KieSession.class)) {
                else if (field.getType().isAssignableFrom(KieSession.class)) {
                    KieSession session = (KieSession)field.get(testClass);
                    if (session!=null) {
                        LOG.debug("Automatic session disposal");
                        session.dispose();
                    }
                }
            }
        }
    }
    
    /**
     * Get the appropriate session for the specified field
     * @param container KieContainer from which the session is created
     * @param field the current field
     * @return the session
     */
    private Object getSessionToSet(KieContainer container, Field field) {
        Object toSet;
        
        if (field.getType().equals(pl.maciejwalkowiak.drools.DroolsSession.class)) {
            DroolsSession annotation = field.getAnnotation(DroolsSession.class); 
            if (annotation.stateless()) {
                toSet = new StatelessDroolsSession(getStatelessSession(container));
            }
            else {
                toSet = new StatefulDroolsSession(getStatefulSession(container));
            }
        } else if (field.getType().equals(StatelessKieSession.class)) {
            toSet = getStatelessSession(container);
        } else {
            toSet = getStatefulSession(container);
        }
        return toSet;
    }
    
    /**
     * Build a new stateless session
     * @param container KieContainer
     * @return KieSession a stateless session
     */
    private KieSession getStatefulSession(KieContainer container) {
        return container.newKieSession();
    }
    
    /**
     * Build a new stateful session
     * @param container KieContainer
     * @return StatelessKieSession a stateful session
     */
    private StatelessKieSession getStatelessSession(KieContainer container) {
        return container.newStatelessKieSession();
    }
}
