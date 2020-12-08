package cilek.view;

import cilek.ProductManagement;
import cilek.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ViewFactory {

    private ProductManagement stationaryManagement;
    private List<Stage> activeStages;


    public ViewFactory(ProductManagement stationaryManagement) {
        this.stationaryManagement = stationaryManagement;
        activeStages = new LinkedList<>();
    }

    public void showMainWindow(){
        BaseController controller = new MainWindowController(stationaryManagement,this,"MainWindow.fxml");
        initializeStage(controller);

    }

    public void showStockWindow(){
        BaseController controller = new StockWindowController(stationaryManagement,this,"StockWindow.fxml");
        initializeStage(controller);

    }

    public void showAddStockWindow(){
        BaseController controller = new AddStockWindowController(stationaryManagement,this,"AddStockWindow.fxml");
        initializeStage(controller);

    }
    public void showLoginWindow(){
        BaseController controller = new LoginWindowController(stationaryManagement,this,"LoginWindow.fxml");
        initializeStage(controller);

    }
    public void showSalesInformationWindow(){
        BaseController controller = new SalesInformationWindowController(stationaryManagement,this,"SalesInformationWindow.fxml");
        initializeStage(controller);

    }
    public void showOrderDetailsWindow(){
        BaseController controller = new OrderDetailsWindowController(stationaryManagement,this,"OrderDetailsWindow.fxml");
        initializeStage(controller);

    }
    public void showAllStockWindow(){
        BaseController controller = new AllStockWindowController(stationaryManagement,this,"AllStockWindow.fxml");
        initializeStage(controller);

    }

    private void initializeStage(BaseController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);
        Parent parent;
        try{
            parent = fxmlLoader.load();
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();

        stage.setMaximized(true);
        Image image = new Image(getClass().getResourceAsStream("LOGO.png"));
        stage.getIcons().add(image);
        stage.setScene(scene);

        stage.show();
        activeStages.add(stage);
        updateTheme();
    }
    public void updateTheme(){
        for(Stage stage: activeStages){
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            String css = this.getClass().getResource("theme.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("Çilek Kırtasiye");

        }
    }
    public void closeStage(Stage closeToStage){
        closeToStage.close();
        activeStages.remove(closeToStage);
    }

}
