module se.myhappyplants {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires org.postgresql.jdbc;
    requires jbcrypt;
    requires io.javalin;
    requires com.google.gson;
    requires annotations;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires org.json;


    //exports se.myhappyplants.client.controller;
    exports se.myhappyplants.client.model;
    exports se.myhappyplants.client.view;
    exports se.myhappyplants.shared to com.google.gson;
    opens se.myhappyplants.shared to com.google.gson;

    opens se.myhappyplants.client.controller to javafx.fxml;
    exports se.myhappyplants.server.repositories;
    exports se.myhappyplants.server.addplantsutility;
}