package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;

/**
 * Displays simple help information.
 */
public class HelpAction extends Action {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        System.out.println("usage: java -jar gitlab-migrator.jar [COMMAND]");
        System.out.println();
        System.out.println("    labels         - Migrates labels from source to destination.");
        System.out.println("    test-conns     - Determines if you can connect to both the source and destination Gitlab servers.");
        System.out.println("    test-source    - Determines if you can connect to the source Gitlab server.");
        System.out.println("    test-dest      - Determines if you can connect to the destination Gitlab server.");
        System.out.println("    version        - Displays the current version of this executable.");
        System.out.println("    help           - Displays this help message.");
        System.out.println();
    }

}
