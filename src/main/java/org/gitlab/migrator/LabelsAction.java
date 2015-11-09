package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabLabel;

import java.util.List;

/**
 * Migrates project labels from source to destination.
 */
public class LabelsAction extends Action {

    @Override
    public String getName() {
        return "labels";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        try {
            int sourceId = App.getSourceProjectId();
            int destId = App.getDestinationProjectId();
            List<GitlabLabel> labels = source.getLabels(sourceId);
            for (GitlabLabel label : labels) {
                dest.createLabel(destId, label);
                log.debug("Label '" + label.getName() + "' created on destination");
            }
            log.info("Labels migrated to destination.");
        } catch (Throwable t) {
            log.info("Unable to migrate labels");
            log.debug(App.EXCEPT_MSG, t);
        }
    }

}
