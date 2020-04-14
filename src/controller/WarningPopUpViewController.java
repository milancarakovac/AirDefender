package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class WarningPopUpViewController{
    @FXML TextArea textArea;

    public void initialize(){
        textArea.setText("");
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}
