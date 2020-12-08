package cilek.controller;

import cilek.ProductManagement;
import cilek.view.ViewFactory;

public class BaseController {

    protected ProductManagement stationaryManagement;
    protected ViewFactory viewFactory;
    private String fxmlName;

    public BaseController(ProductManagement stationaryManagement, ViewFactory viewFactory, String fxmlName) {
        this.stationaryManagement = stationaryManagement;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName(){
        return fxmlName;
    }
}
