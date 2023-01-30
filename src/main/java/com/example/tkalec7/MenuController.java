package com.example.tkalec7;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MenuController {

    public void showCategorySearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("categorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showItemSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("itemSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showFactorySearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FactorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showStoreSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StoreSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showAddItemScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewItemScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showAddCategoryScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewCategoryScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showAddFactoryScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewFactoryScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }

    public void showAddStoreScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewStoreScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    public void showClientSearchScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("clientSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
    public void showAddClientScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewClient.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setTitle("Hello!");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
}
