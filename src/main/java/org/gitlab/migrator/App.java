package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Entry point into the CLI
 */
public class App
{
    public static final String EXCEPT_MSG = "Runtime exception logged";
    public static final Properties config = new Properties();

    private static final Logger log = LoggerFactory.getLogger(App.class);
    private final String DEFAULT_ACTION = "help";
    private String[] args;
    private Map<String, Action> actions;
    private GitlabAPI sourceGitlabServer = null;
    private GitlabAPI destGitlabServer = null;

    /**
     * Loads configuration from file system.
     */
    static {
        try (FileInputStream stream = new FileInputStream("application.properties")) {
            config.load(stream);
        } catch (Exception e) {
            log.debug(EXCEPT_MSG, e);
        }
    }

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

        sourceGitlabServer = GitlabAPI.connect(getSourceUrl(), getSourceToken());
        sourceGitlabServer.ignoreCertificateErrors(ignoreSourceCertErrors());
        destGitlabServer = GitlabAPI.connect(getDestinationUrl(), getDestinationToken());
        destGitlabServer.ignoreCertificateErrors(ignoreDestinationCertErrors());
    }

    /**
     * Gets the URL of the source Gitlab server.
     * @return
     */
    public static String getSourceUrl() {
        return config.getProperty("source.url");
    }

    /**
     * Gets the API token for the source Gitlab server.
     * @return
     */
    public static String getSourceToken() {
        return config.getProperty("source.token");
    }

    /**
     * Determines if certificate errors should be ignored on the source
     * Gitlab server.
     * @return True if cert errors should be ignored.
     */
    public static boolean ignoreSourceCertErrors() {
        String ignore = config.getProperty("source.ignoreCertErrors", "false");
        return Boolean.parseBoolean(ignore);
    }

    /**
     * Gets the ID of the source project.
     * @return
     */
    public static int getSourceProjectId() {
        String id = config.getProperty("source.project.id");
        return Integer.parseInt(id);
    }

    /**
     * Gets the URL of the destination Gitlab server.
     * @return
     */
    public static String getDestinationUrl() {
        return config.getProperty("destination.url");
    }

    /**
     * Gets the API token for the destination Gitlab server.
     * @return
     */
    public static String getDestinationToken() {
        return config.getProperty("destination.token");
    }

    /**
     * Determines if certificate errors should be ignored on the destination
     * Gitlab server.
     * @return True if cert errors should be ignored.
     */
    public static boolean ignoreDestinationCertErrors() {
        String ignore =
                config.getProperty("destination.ignoreCertErrors", "false");
        return Boolean.parseBoolean(ignore);
    }

    /**
     * Gets the ID of the destination project.
     * @return
     */
    public static int getDestinationProjectId() {
        String id = config.getProperty("destination.project.id");
        return Integer.parseInt(id);
    }

    /**
     * Executes the appropriate action.
     */
    public void run() {
        String command = getCommand();
        Action action = getAction(command);
        action.run(args, sourceGitlabServer, destGitlabServer);
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
