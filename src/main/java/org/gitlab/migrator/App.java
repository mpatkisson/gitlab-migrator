package org.gitlab.migrator;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Entry point into the CLI
 */
public class App
{
    private final String DEFAULT_ACTION = "help";
    private String[] args;
    private Map<String, Action> actions;

    public static final String EXCEPT_MSG = "Runtime exception logged";

    /**
     * Creates a new instance.
     * @param args Command line arguments
     */
    public App(String[] args) throws IllegalAccessException, InstantiationException {
        actions = new HashMap<String, Action>();
        Reflections reflections = new Reflections("org.gitlab.migrator");
        Set<Class<? extends Action>> types = reflections.getSubTypesOf(Action.class);
        for (Class<? extends Action> type : types) {
            Action action = type.newInstance();
            actions.put(action.getName(), action);
        }
        this.args = args;
    }

    /**
     * Executes the appropriate action.
     */
    public void run() {
        String command = getCommand();
        Action action = getAction(command);
        action.run(args);
    }

    /**
     * Gets the command from the command line arguments.
     * @return The CLI command as a String.
     */
    public String getCommand() {
        String command;
        if (args == null || args.length == 0) {
            command = DEFAULT_ACTION;
        } else {
            command = args[0];
        }
        return command;
    }

    /**
     * Gets the action associated with the CLI command entered by the user.
     * @param command The CLI command entered by the user.
     * @return The action to be executed.  The default action will be returned if no command was found.
     */
    public Action getAction(String command) {
        Action action = actions.get(command);
        if (action == null) {
            action = actions.get(DEFAULT_ACTION);
        }
        return action;
    }

    /**
     * The main method run by the executable jar
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(App.class);
        try {
            App app = new App(args);
            app.run();
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            log.info(EXCEPT_MSG);
        }
    }
}
