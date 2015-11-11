package org.gitlab.migrator;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Migrates project labels from source to destination.
 */
public class IssuesAction extends MilestonesAction {

    protected Map<Integer, GitlabIssue> issuesMap = new HashMap<>();

    @Override
    public String getName() {
        return "issues";
    }

    @Override
    public void run(String[] args, GitlabAPI source, GitlabAPI dest) {
        super.run(args, source, dest);
        try {
            int sourceId = App.getSourceProjectId();
            GitlabProject sourceProject = source.getProject(sourceId);
            int destId = App.getDestinationProjectId();
            List<GitlabIssue> issues = source.getIssues(sourceProject);
            issues.sort((i1, i2) -> Integer.compare(i1.getId(), i2.getId()));
            for (GitlabIssue issue : issues) {
                GitlabUser assignee = issue.getAssignee();
                GitlabMilestone sourceMilestone = issue.getMilestone();
                GitlabMilestone destMilestone = null;
                int milestoneId = 0;
                if (sourceMilestone != null) {
                    destMilestone = milestonesMap.get(sourceMilestone.getId());
                    milestoneId = destMilestone.getId();
                }
                String[] labels = issue.getLabels();
                String csLabels = String.join(",", labels);
                String description = issue.getDescription();
                String title = issue.getTitle();
                GitlabIssue created = dest.createIssue(destId, 5, milestoneId, csLabels, description, title);
                if (issue.getState().equals("closed")) {
                    created = dest.editIssue(created, GitlabIssue.Action.CLOSE);
                }
                saveComments(issue, created, source, dest);
                issuesMap.put(issue.getId(), created);
                log.debug("Issue '" + issue.getId() + "' created on destination");
            }
            log.info("Issues migrated to destination.");
        } catch (Throwable t) {
            log.info("Unable to migrate issues");
            log.debug(App.EXCEPT_MSG, t);
        }
    }

    private void saveComments(
            GitlabIssue origin,
            GitlabIssue transferred,
            GitlabAPI source,
            GitlabAPI dest)
            throws IOException {
        List<GitlabNote> notes = source.getNotes(origin);
        notes.sort((n1, n2) -> Integer.compare(n1.getId(), n2.getId()));
        for (GitlabNote note : notes) {
            dest.createNote(transferred, note.getBody());
        }
    }

}
