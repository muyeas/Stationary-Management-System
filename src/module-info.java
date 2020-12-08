module Stationery {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;


    opens cilek;
    opens cilek.controller;
    opens cilek.view;
    opens cilek.model;

}