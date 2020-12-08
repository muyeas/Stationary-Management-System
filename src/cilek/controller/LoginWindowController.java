package cilek.controller;

import cilek.ProductManagement;
import cilek.controller.services.LoginService;
import cilek.model.LoginAccountInfo;
import cilek.view.ViewFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginWindowController extends BaseController {


    public LoginWindowController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        super(stationaryManagement, viewFactory, fxmlName);
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void loginButtonAction() throws SQLException {
        LoginAccountInfo loginAccountInfo = new LoginAccountInfo(usernameField.getText(),passwordField.getText());
        LoginService loginService = new LoginService(loginAccountInfo,stationaryManagement);
        if(loginService.loginAction()){
            viewFactory.closeStage((Stage) usernameField.getScene().getWindow());
            viewFactory.showMainWindow();
        }
        else{
            return;
        }

    }

}
