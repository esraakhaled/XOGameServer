/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Raiaan
 */
public class CustomPopup {
    public static void display(short state) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Text message = new Text("Do you really want to Stop the server?");
        Button sureButton = new Button("close the server");
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
             stage.close();
        });
        sureButton.setOnAction(e -> {
             stage.close();
             GameServer.close();
             if(state == 1)
                 Platform.exit();
        });
        GridPane layout = new GridPane();
        GridPane subLayout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        subLayout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5); 
        layout.setHgap(5); 
        subLayout.setVgap(5);
        subLayout.setHgap(5);
        layout.add(message, 0,0);
        subLayout.add(cancelButton, 0,0);
        subLayout.add(sureButton, 1,0);
        layout.add(subLayout,0,1);
        Scene scene = new Scene(layout, 250, 90);          
        stage.setTitle("closing...");
        stage.setScene(scene);
        stage.showAndWait();
    }
    public static void databaseError(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Text message = new Text("oops sorry something went wrong while connecting to database");
        
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5); 
        layout.setHgap(5); 
        
        layout.add(message, 0,0);
        Scene scene = new Scene(layout, 250, 90);          
        stage.setTitle("database not connected...");
        stage.setScene(scene);
        stage.showAndWait();
}
}
