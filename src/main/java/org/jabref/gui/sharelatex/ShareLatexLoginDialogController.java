package org.jabref.gui.sharelatex;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import org.jabref.gui.AbstractController;

public class ShareLatexLoginDialogController extends AbstractController<ShareLatexLoginDialogViewModel> {

    @FXML private Button btnCancel;
    @FXML private Button btnLogin;



    @FXML
    private void initialize() {
        viewModel = new ShareLatexLoginDialogViewModel();
    }

    @FXML
    private void closeDialog() {
        getStage().close();
    }

    @FXML
    private void signIn() {
        System.out.println("sign in pressed");
    }
}
