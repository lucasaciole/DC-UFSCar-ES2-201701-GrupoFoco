package org.jabref.model.sharelatex;

public class ShareLatexProject {

    private final String projectId;
    private final String projectTitle;
    private final String owner;
    private final String lastUpdated;

    public ShareLatexProject(String projectId, String projectTitle, String owner, String lastUpdated) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.lastUpdated = lastUpdated;
        this.owner = owner;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getOwner() {
        return owner;
    }
}
