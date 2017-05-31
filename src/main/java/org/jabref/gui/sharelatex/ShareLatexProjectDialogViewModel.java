package org.jabref.gui.sharelatex;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import org.jabref.gui.AbstractViewModel;
import org.jabref.gui.StateManager;
import org.jabref.model.sharelatex.ShareLatexProject;

public class ShareLatexProjectDialogViewModel extends AbstractViewModel {

    private final SimpleListProperty<ShareLatexProjectViewModel> projects = new SimpleListProperty<>(
            FXCollections.observableArrayList());

    public ShareLatexProjectDialogViewModel(StateManager stateManager) {
        //todo currently unused
    }

    public void addProjects(List<ShareLatexProject> projectsToAdd) {
        this.projects.addAll(projectsToAdd.stream().map(ShareLatexProjectViewModel::new).collect(Collectors.toList()));
    }

    public SimpleListProperty<ShareLatexProjectViewModel> projectsProperty() {
        return this.projects;
    }

}
