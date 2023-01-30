package com.example.tkalec7;

import database.Database;
import files.FilesUtils;
import hr.java.production.model.*;
import hr.java.production.threads.AddNewFactoryControllerThread;
import hr.java.production.threads.AddNewStoreControllerThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddNewStoreController {


    @FXML
    private TextField storeNameTextField;

    @FXML
    private TextField storeAddressTextField;

    @FXML
    private ChoiceBox<String> storeChoiceBox;

    public static List<Item> items = new ArrayList<>();
    public static List<Item> itemList = new ArrayList<>();
    public static List<Store> stores = new ArrayList<>();

    @FXML
    public void initialize() throws InterruptedException {

        /*itemList.clear();

        items.clear();

        try{
            FilesUtils.getDeserialization(items, "dat/itemsSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        stores.clear();

        try{
            FilesUtils.getDeserialization(stores, "dat/storesSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }*/


        /*List<String> options1 = new ArrayList<>();
        for(Item item : HelloApplication.itemList){
            options1.add(item.getName());
        }


        ObservableList<String> cursors = FXCollections.observableArrayList(options1);
        storeChoiceBox.setItems(cursors);*/

        AddNewStoreControllerThread firstThread = new AddNewStoreControllerThread(storeChoiceBox, "items", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");


        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread1);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }

    @FXML
    public void onSaveButtonClick() throws SQLException, IOException {

        String enteredStoreName = storeNameTextField.getText();
        String enteredStoreWebAddress = storeAddressTextField.getText();
        Optional<String> enteredChoiceBox = Optional.ofNullable(storeChoiceBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if(enteredStoreName.isEmpty()){
            s.add("Store name must not be empty!");
        }

        if(enteredStoreWebAddress.isEmpty()){
            s.add("Store web address must not be empty!");
        }

        if(enteredChoiceBox.isEmpty()){
            s.add("Item selection must not be empty!");
        }

        if(s.toString().isEmpty()){

            if(enteredChoiceBox.isPresent()) {

                /*Set<Item> set = new HashSet<>(itemList);
                stores.add(new Store(enteredStoreName, stores.size() + 1, enteredStoreWebAddress, set));*/


                HelloApplication.storeList.add(new Store(HelloApplication.storeList.size() + 1, enteredStoreName, enteredStoreWebAddress));

                ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

                AddNewStoreControllerThread secondThread = new AddNewStoreControllerThread(enteredStoreName, enteredStoreWebAddress, "button1", Database.token);

                Thread thread2 = new Thread(secondThread, "secondThread");

                executorService.execute(thread2);

                executorService.shutdown();
                //Database.insertNewStore(enteredStoreName, enteredStoreWebAddress);

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Store added successfully");
                alert1.setHeaderText("Store added successfully");
                alert1.setContentText("Store'" + enteredStoreName + "' was saved successfully.");
                alert1.showAndWait();

            }

           /* try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/storesSerialized.dat"))) {

                out.writeObject(stores);

            } catch (IOException ex) {
                System.err.println(ex);
            }*/

            try {
                FilesUtils.getSerialization(stores, "dat/storesSerialized.dat");
            } catch (IOException ex){
                System.err.println(ex);
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }
    }

    @FXML
    public void onSaveItemButtonClick() throws SQLException, IOException {

        String enteredStoreName = storeNameTextField.getText();
        String enteredStoreWebAddress = storeAddressTextField.getText();
        Optional<String> enteredChoiceBox = Optional.ofNullable(storeChoiceBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if(enteredStoreName.isEmpty()){
            s.add("Store name must not be empty!");
        }

        if(enteredStoreWebAddress.isEmpty()){
            s.add("Store web address must not be empty!");
        }

        if(enteredChoiceBox.isEmpty()){
            s.add("Item selection must not be empty!");
        }

        if(s.toString().isEmpty()){

            if(enteredChoiceBox.isPresent()) {


                int flag = 0;
                outerloop:
                for(Store store : HelloApplication.storeList) {
                    for (Item item : HelloApplication.itemList) {
                        if (item.getName().compareTo(enteredChoiceBox.get()) == 0) {
                            if (store.getName().toLowerCase().compareTo(enteredStoreName.toLowerCase()) == 0){

                                flag = Database.checkStoreItemDuplicates(store, item);
                                if(flag == 2){
                                    break outerloop;
                                }

                                ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

                                AddNewStoreControllerThread thirdThread = new AddNewStoreControllerThread(store, item, "button2", Database.token);

                                Thread thread3 = new Thread(thirdThread, "thirdThread");

                                executorService.execute(thread3);

                                executorService.shutdown();
                                //Database.insertNewStoreItem(store, item);
                                flag = 1;
                                break outerloop;
                            }
                        }
                    }
                }

                if(flag == 1) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Item added successfully");
                    alert1.setHeaderText("Item added successfully");
                    alert1.setContentText("The Item '" + enteredChoiceBox.get() + "' was saved successfully.");
                    alert1.showAndWait();
                }
                else if(flag == 2){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Errors");
                    alert.setHeaderText("There is no store in database with this name!\nPlease try again.");
                    alert.setContentText(s.toString());
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Errors");
                    alert.setHeaderText("There is no store in database with this name!\nPlease try again.");
                    alert.setContentText(s.toString());
                    alert.showAndWait();
                }

            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }
    }
}
