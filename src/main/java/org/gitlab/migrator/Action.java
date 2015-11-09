package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An action to be performed by the application.
 */
public abstract class Action {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Gets the name of the action.
     * @return The name of the action.
     */
    public abstract String getName();

    /**
     * Performs the application action.
     * @param args Command line arguments.
     */
    public abstract void run(String[] args, GitlabAPI source, GitlabAPI dest);

}
