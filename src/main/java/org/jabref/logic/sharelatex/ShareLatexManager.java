package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jabref.JabRefExecutorService;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.sharelatex.ShareLatexProject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class ShareLatexManager {

    private final SharelatexConnector connector = new SharelatexConnector();
    private final List<ShareLatexProject> projects = new ArrayList<>();

    public String login(String server, String username, String password) throws IOException {
        return connector.connectToServer(server, username, password);
    }

    public List<ShareLatexProject> getProjects() throws IOException {

        connector.getProjects().ifPresent(jsonResponse -> {
            if (jsonResponse.has("projects")) {
                JsonArray projectArray = jsonResponse.get("projects").getAsJsonArray();
                System.out.println(projectArray);
                for (JsonElement elem : projectArray) {

                    String id = elem.getAsJsonObject().get("id").getAsString();
                    String name = elem.getAsJsonObject().get("name").getAsString();
                    String lastUpdated = elem.getAsJsonObject().get("lastUpdated").getAsString();
                    String owner = elem.getAsJsonObject().get("owner_ref").getAsString();
                    System.out.println("ID " + id);
                    System.out.println("Name " + name);
                    System.out.println("LastUpdated " + lastUpdated);
                    System.out.println("Owner" + owner);

                    ShareLatexProject project = new ShareLatexProject(id, name, owner, lastUpdated);
                    projects.add(project);
                    //TODO: How do I pass this projectLIst to the other view?
                }

            }
        });
        return projects;
    }

    public void uploadLibrary(String projectId, BibDatabaseContext database) {

        //TODO: Not yet implemented
        if (database.getDatabasePath().isPresent()) {

            // connector.uploadFile(projectId, database.getDatabasePath().get());

        }
    }

    public void startWebSocketHandler(String projectID, BibDatabaseContext database) {
        JabRefExecutorService.INSTANCE.executeAndWait(() -> {

            connector.startWebsocketListener();
        });
    }
}
