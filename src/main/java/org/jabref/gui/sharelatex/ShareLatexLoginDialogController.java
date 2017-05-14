package org.jabref.gui.sharelatex;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.jabref.gui.AbstractController;

public class ShareLatexLoginDialogController extends AbstractController<ShareLatexLoginDialogViewModel> {

    @FXML private Button btnCancel;
    @FXML private Button btnLogin;
    @FXML private TextField tbAddress;
    @FXML private TextField tbUsername;
    @FXML private PasswordField pfPassword;

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
        System.out.println(tbAddress.getText());
        System.out.println(tbUsername.getText());
        System.out.println(pfPassword.getText());
    }
}
