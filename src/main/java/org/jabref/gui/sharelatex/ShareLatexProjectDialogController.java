package org.jabref.gui.sharelatex;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.jabref.gui.AbstractController;
import org.jabref.logic.sharelatex.ShareLatexManager;

public class ShareLatexProjectDialogController extends AbstractController<ShareLatexProjectDialogViewModel> {

    @FXML private TableColumn<ShareLatexProjectViewModel, Boolean> colActive;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colTitle;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colOwner;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colLastModified;
    @FXML private TableView<ShareLatexProjectViewModel> tblProjects;
    @Inject private ShareLatexManager manager;

    @FXML
    private void initialize() {
        viewModel = new ShareLatexProjectDialogViewModel();
        viewModel.addProjects(manager.getProjects());

        colActive.setCellValueFactory(cellData -> cellData.getValue().isActive());
        colTitle.setCellValueFactory(cellData -> cellData.getValue().getProjectTitle());
        colOwner.setCellValueFactory(cellData -> cellData.getValue().getOwner());
        colLastModified.setCellValueFactory(cellData -> cellData.getValue().getLastUpdated());
        setBindings();

    }

    private void setBindings() {
        tblProjects.itemsProperty().bindBidirectional(viewModel.projectsProperty());
    }

    @FXML
    private void synchronizeLibrary() {
        //todo
    }

}
