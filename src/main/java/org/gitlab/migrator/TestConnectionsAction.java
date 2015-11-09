package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;

/**
 * Tests the connection to the source Gitlab server.
 */
public class TestConnectionsAction extends Action {

    @Override
    public String getName() {
        return "test-conns";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        try {
            source.getProjects();
            log.info("Connected to source");
            dest.getProjects();
            log.info("Connected to destination");
        } catch (Throwable t) {
            log.info("Unable to connect to either source or destination.");
            log.info("You may wish to test these connections individually to see which one failed.");
            log.debug(App.EXCEPT_MSG, t);
        }
    }

}
