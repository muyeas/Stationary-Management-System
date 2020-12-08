package cilek.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginAccountInfo {

    private String username;
    private String password;
    private DatabaseConnection connection = new DatabaseConnection();

    public LoginAccountInfo(String username , String password) {
        this.username = username;
        this.password = password;
    }
    public boolean checkDatabase() throws SQLException {
        String query = "SELECT * FROM loginAccount WHERE username =? AND password =? ";
        PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return true;
        }
        else{
            return false;
        }
    }
}
