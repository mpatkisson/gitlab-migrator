package org.gitlab.migrator;

/**
 * Displays simple help information.
 */
public class HelpAction extends Action {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void run(String[] args) {
        System.out.println("usage: java -jar gitlab-migrator.jar [COMMAND]");
        System.out.println();
        System.out.println("    version        - Displays the current version of this executable.");
        System.out.println("    help           - Displays this help message.");
        System.out.println();
    }

}
