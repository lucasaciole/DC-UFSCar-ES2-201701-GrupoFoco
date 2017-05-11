package org.jabref.gui.sharelatex;

import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import org.jabref.gui.AbstractDialogView;
import org.jabref.gui.FXDialog;

public class ShareLatexProjectDialogView extends AbstractDialogView {



    @Override
    public void show() {

        FXDialog shareLatexLoginDialog = new FXDialog(AlertType.INFORMATION, "Login to sharelatex");
        shareLatexLoginDialog.setDialogPane((DialogPane) this.getView());
        shareLatexLoginDialog.setResizable(true);
        shareLatexLoginDialog.show();
    }

}
