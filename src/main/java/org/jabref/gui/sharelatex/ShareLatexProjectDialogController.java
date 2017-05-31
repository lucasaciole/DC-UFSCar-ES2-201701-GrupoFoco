package org.jabref.gui.sharelatex;

import java.io.IOException;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import org.jabref.gui.AbstractController;
import org.jabref.gui.StateManager;
import org.jabref.logic.sharelatex.ShareLatexManager;

public class ShareLatexProjectDialogController extends AbstractController<ShareLatexProjectDialogViewModel> {

    @FXML private TableColumn<ShareLatexProjectViewModel, Boolean> colActive;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colTitle;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colOwner;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colLastModified;
    @FXML private TableView<ShareLatexProjectViewModel> tblProjects;
    @Inject private ShareLatexManager manager;
    @Inject private StateManager stateManager;

    @FXML
    private void initialize() {
        viewModel = new ShareLatexProjectDialogViewModel(stateManager);
        try {
            viewModel.addProjects(manager.getProjects());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tblProjects.setEditable(true);
        colActive.setEditable(true);

        colActive.setCellFactory(CheckBoxTableCell.forTableColumn(colActive));

        colActive.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
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

        viewModel.projectsProperty().filtered(x -> x.isActive())
                .forEach(item -> System.out.println(item.getProjectTitle()));
        String projectId = "";

        stateManager.getActiveDatabase()
                .ifPresent(database -> manager.uploadLibrary(projectId, database));
    }

    @FXML
    private void cancelAndClose() {
        getStage().close();
    }
}
