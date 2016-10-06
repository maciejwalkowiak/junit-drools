package pl.maciejwalkowiak.drools.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.kie.api.command.Command;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import pl.maciejwalkowiak.drools.DroolsSession;

/**
 * Can be injected instead of {@link StatelessKieSession} in test classes to 
 *  improve readability in case if only basic methods are used
 *
 * @author Maciej Walkowiak
 */
public class StatelessDroolsSession implements DroolsSession {
    private final StatelessKieSession statelessSession;

    private final List<Command> commands;
    
    public StatelessDroolsSession(StatelessKieSession statefulSession) {
        this.statelessSession = statefulSession;
        this.commands = new ArrayList<Command>();
    }

    @Override
    public void fire(String ruleName) {
        // TODO is it right?
        throw new UnsupportedOperationException("Cannot fire a single rule within a stateless session.");
    }

    @Override
    public void fireAllRules() {
        this.executeCommands();
    }

    @Override
    public void insert(Object object) {
        this.commands.add(CommandFactory.newInsert(object));
    }

    @Override
    public void execute(Object object) {
        this.commands.add(CommandFactory.newInsert(object));
        this.executeCommands();
    }

    @Override
    public void execute(Collection iterable) {
        this.commands.add(CommandFactory.newInsertElements(iterable));
        this.executeCommands();
    }

    @Override
    public KieSession getSession() {
        throw new UnsupportedOperationException("This is a stateless session.");
    }

    @Override
    public StatelessKieSession getStatelessSession() {
        return this.statelessSession;
    }
    
    private void executeCommands() {
        this.statelessSession.execute(CommandFactory.newBatchExecution(this.commands));
    }
}