package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jabref.gui.sharelatex.ShareLatexProjectViewModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class ShareLatexManager {

    private final SharelatexConnector connector = new SharelatexConnector();
    private final List<ShareLatexProjectViewModel> projects = new ArrayList<>();

    public String login(String server, String username, String password) {
        return connector.connectToServer(server, username, password);
    }

    public List<ShareLatexProjectViewModel> getProjects() {
        try {
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

                        ShareLatexProjectViewModel model = new ShareLatexProjectViewModel(id, name, owner, lastUpdated);
                        projects.add(model);
                        //TODO: How do I pass this projectLIst to the other view?
                    }

                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return projects;
    }
}
