package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;

import java.io.IOException;
import java.net.URL;

/**
 * Tests the connection to the source Gitlab server.
 */
public class TestSourceAction extends Action {

    @Override
    public String getName() {
        return "test-source";
    }

    @Override
    public void run(String[] args, GitlabAPI source) {
        try {
            source.getAllProjects();
            log.info("Connected to source");
        } catch (Throwable t) {
            log.info("Unable to connect to Gitlab source");
            log.debug(App.EXCEPT_MSG, t);
        }
    }

}
