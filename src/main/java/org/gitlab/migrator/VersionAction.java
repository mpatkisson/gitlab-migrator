package org.gitlab.migrator;

import org.gitlab.migrator.Action;

/**
 * Displays simple help information.
 */
public class VersionAction extends Action {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public void run(String[] args) {
        System.out.println("0.2.0");
    }

}
