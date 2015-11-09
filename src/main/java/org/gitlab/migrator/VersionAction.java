package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;

/**
 * Displays simple help information.
 */
public class VersionAction extends Action {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        System.out.println("0.2.0");
    }

}
