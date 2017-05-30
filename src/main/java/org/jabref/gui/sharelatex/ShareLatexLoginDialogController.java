package org.jabref.gui.sharelatex;

import java.io.IOException;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.jabref.gui.AbstractController;
import org.jabref.gui.FXDialog;
import org.jabref.logic.sharelatex.ShareLatexManager;

public class ShareLatexLoginDialogController extends AbstractController<ShareLatexLoginDialogViewModel> {

    @FXML private TextField tbAddress;
    @FXML private TextField tbUsername;
    @FXML private PasswordField pfPassword;
    @Inject private ShareLatexManager manager;

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

        String result;
        try {
            result = manager.login(tbAddress.getText(), tbUsername.getText(), pfPassword.getText());
            if (result.contains("incorrect")) {
                FXDialog dlg = new FXDialog(AlertType.ERROR);
                dlg.setContentText("Your email or password is incorrect. Please try again");
                dlg.showAndWait();
            } else {
                ShareLatexProjectDialogView dlgprojects = new ShareLatexProjectDialogView();
                dlgprojects.show();
            }
        } catch (IOException e) {
            FXDialog dlg = new FXDialog(AlertType.ERROR);
            dlg.setContentText(e.getMessage());
            dlg.showAndWait();

        }

    }
}
