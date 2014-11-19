package pl.maciejwalkowiak.drools;

import pl.maciejwalkowiak.drools.annotations.DroolsFiles;
import pl.maciejwalkowiak.drools.annotations.DroolsSession;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Processes test class annotations
 *
 * @author Maciej Walkowiak
 */
public class DroolsAnnotationProcessor {
    private Object testClass;

    public DroolsAnnotationProcessor(Object testClass) {
        this.testClass = testClass;
    }

    public DroolsFiles getDroolsFiles() {
        DroolsFiles droolsFiles = testClass.getClass().getAnnotation(DroolsFiles.class);

        if (droolsFiles == null) {
            throw new IllegalStateException("DroolsFiles annotation not set");
        }
        return droolsFiles;
    }

    List<Field> getAllFields() {
        List<Field> allFields = new LinkedList<Field>();
        Class<?> klass = testClass.getClass();
        while (klass != null) {
            allFields.addAll(Arrays.asList(klass.getDeclaredFields()));
            klass = klass.getSuperclass();
        }
        return allFields;
    }

    public void setDroolsSession(pl.maciejwalkowiak.drools.DroolsSession droolsSession) {
        for (Field field : getAllFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DroolsSession.class)) {
                Object value = getValueToSet(droolsSession, field);
                try {
                    field.set(testClass, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Object getValueToSet(pl.maciejwalkowiak.drools.DroolsSession droolsSession, Field field) {
        Object toSet;
        if (field.getType().equals(pl.maciejwalkowiak.drools.DroolsSession.class)) {
            toSet = droolsSession;
        } else {
            toSet = droolsSession.getStatefulSession();
        }
        return toSet;
    }
}
