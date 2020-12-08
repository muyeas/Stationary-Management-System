package cilek.controller.services;

import cilek.ProductManagement;
import cilek.model.LoginAccountInfo;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;

import java.sql.SQLException;

public class LoginService {
    LoginAccountInfo loginAccountInfo;
    ProductManagement stationaryManagement;

    public LoginService(LoginAccountInfo loginAccountInfo,ProductManagement stationaryManagement)  {
        this.loginAccountInfo = loginAccountInfo;
        this.stationaryManagement = stationaryManagement;
    }
    public boolean loginAction() throws SQLException {
        if(loginAccountInfo.checkDatabase()){
            return true;
        }
        else{
            stationaryManagement.showAlert(Alert.AlertType.ERROR,"Giriş başarısız");
            return false;
        }
    }

    }


