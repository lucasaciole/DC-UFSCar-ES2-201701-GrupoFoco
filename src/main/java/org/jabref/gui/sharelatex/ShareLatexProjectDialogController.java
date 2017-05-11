package org.jabref.gui.sharelatex;

import org.jabref.gui.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ShareLatexProjectDialogController extends AbstractController<ShareLatexProjectDialogViewModel> {

    @FXML TableColumn colTitle;
    @FXML TableColumn colOwner;
    @FXML TableColumn colLastModified;
    @FXML TableView tblProjects;



    @FXML
    private void initialize() {
        viewModel = new ShareLatexProjectDialogViewModel();
    }

}
