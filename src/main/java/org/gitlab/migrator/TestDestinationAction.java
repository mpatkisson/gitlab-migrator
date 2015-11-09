package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;

/**
 * Tests the connection to the source Gitlab server.
 */
public class TestDestinationAction extends Action {

    @Override
    public String getName() {
        return "test-dest";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        try {
            dest.getProjects();
            log.info("Connected to destination");
        } catch (Throwable t) {
            log.info("Unable to connect to Gitlab destination");
            log.debug(App.EXCEPT_MSG, t);
        }
    }

}
