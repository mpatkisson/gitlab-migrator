package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMilestone;

import java.util.List;

/**
 * Migrates project milestones from source to destination.
 */
public class MilestonesAction extends Action {

    @Override
    public String getName() {
        return "milestones";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        try {
            int sourceId = App.getSourceProjectId();
            int destId = App.getDestinationProjectId();
            List<GitlabMilestone> milestones = source.getMilestones(sourceId);
            for (GitlabMilestone milestone : milestones) {
                GitlabMilestone created = dest.createMilestone(destId, milestone);
                if (milestone.getState().equals("closed")) {
                    dest.updateMilestone(created, "close");
                }
                log.debug("Milestone '" + milestone.getTitle() + "' created on destination");
            }
            log.info("milestones migrated to destination.");
        } catch (Throwable t) {
            log.info("Unable to migrate milestones");
            log.debug(App.EXCEPT_MSG, t);
        }
    }

}
