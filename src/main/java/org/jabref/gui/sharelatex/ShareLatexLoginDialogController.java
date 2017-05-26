package org.jabref.gui.sharelatex;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.jabref.gui.AbstractController;
import org.jabref.logic.sharelatex.ShareLatexManager;

public class ShareLatexLoginDialogController extends AbstractController<ShareLatexLoginDialogViewModel> {


    @FXML private Button btnCancel;
    @FXML private Button btnLogin;
    @FXML private TextField tbAddress;
    @FXML private TextField tbUsername;
    @FXML private PasswordField pfPassword;
    @Inject ShareLatexManager manager;

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


        String result = manager.login(tbAddress.getText(), tbUsername.getText(), pfPassword.getText());
        System.out.println(result);

        ShareLatexProjectDialogView dlgprojects = new ShareLatexProjectDialogView();
        dlgprojects.show();

    }
}
