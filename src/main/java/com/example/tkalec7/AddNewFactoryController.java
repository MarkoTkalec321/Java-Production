package com.example.tkalec7;

import database.Database;
import files.FilesUtils;
import hr.java.production.enums.Cities;
import hr.java.production.main.Main;
import hr.java.production.model.*;
import hr.java.production.threads.AddNewFactoryControllerThread;
import hr.java.production.threads.AddNewItemControllerThread;
import hr.java.production.threads.ItemControllerThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class AddNewFactoryController {

    @FXML
    private TextField factoryNameTextField;

    @FXML
    private TextField streetNameTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private ChoiceBox<String> choiceBox;



    public static List<Item> items = new ArrayList<>();
    public static List<Address> addresses = new ArrayList<>();
    public static List<Factory> factories = new ArrayList<>();
    public static List<Item> itemList = new ArrayList<>();
    public static List<Address> addressList = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(AddNewFactoryController.class);

    @FXML
    public void initialize() throws InterruptedException {

        /*itemList.clear();

        addresses.clear();
        //logger.info("Test");

        try{
            FilesUtils.getDeserialization(addresses, "dat/addressesSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }


        items.clear();
        try{
            FilesUtils.getDeserialization(items, "dat/itemsSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }


        factories.clear();
        try{
            FilesUtils.getDeserialization(factories, "dat/factoriesSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }*/


        /*List<String> options2 = new ArrayList<>();
        for(Cities city : Cities.values()){
            //System.out.println("category name: " + category.getName());
            System.out.println(city.getCityName());
            options2.add(city.getCityName());
        }

        ObservableList<String> options = FXCollections.observableList(options2);
        cityComboBox.setItems(options);*/

        /*for(Address address : HelloApplication.addressList) {
            System.out.println(address.getCity());
        }*/


        /*Set<String> set = new HashSet<>();
        for(Address address : HelloApplication.addressList) {
            set.add(address.getCity());
            //options2.add(address.getCity());
        }
        List<String> options2 = new ArrayList<>(set);
        ObservableList<String> options = FXCollections.observableList(options2);
        cityComboBox.setItems(options);

        List<String> options1 = new ArrayList<>();
        for(Item item : HelloApplication.itemList){
            //System.out.println("category name: " + category.getName());
            options1.add(item.getName());
        }

        ObservableList<String> cursors = FXCollections.observableArrayList(options1);
        choiceBox.setItems(cursors);*/

        AddNewFactoryControllerThread firstThread = new AddNewFactoryControllerThread(cityComboBox, "addresses", Database.token);

        AddNewFactoryControllerThread secondThread = new AddNewFactoryControllerThread(choiceBox, "items", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");
        Thread thread2 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_2);

        executorService.execute(thread1);
        executorService.execute(thread2);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }

    @FXML
    public void onSaveButtonClick() throws SQLException, IOException {

        String enteredFactoryName = factoryNameTextField.getText();
        String enteredStreetName = streetNameTextField.getText();
        String enteredHouseNumber = houseNumberTextField.getText();
        Optional<String> enteredCity = Optional.ofNullable(cityComboBox.getValue());
        Optional<String> enteredChoiceBox = Optional.ofNullable(choiceBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if(enteredFactoryName.isEmpty()){
            s.add("Factory name must not be empty!");
        }

        if(enteredStreetName.isEmpty()){
            s.add("Street name must not be empty!");
        }

        if(enteredHouseNumber.isEmpty()){
            s.add("House number must not be empty!");
        }

        if(enteredCity.isEmpty()){
            s.add("City must not be empty!");
        }

        if(enteredChoiceBox.isEmpty()){
            s.add("Item selection must not be empty!");
        }

        if(s.toString().isEmpty()){

            if(enteredChoiceBox.isPresent() && enteredCity.isPresent()) {

               /* for(Cities city : Cities.values()){

                    if(enteredCity.get().toLowerCase().compareTo(city.getCityName().toLowerCase()) == 0){

                        Address address = new BuilderPattern().street(enteredStreetName).house(enteredHouseNumber).city(city).code(city).build();
                        Set<Item> set = new HashSet<>(itemList);
                        factories.add(new Factory(enteredFactoryName, factories.size() + 1, address, set));

                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Factory added successfully");
                        alert1.setHeaderText("Factory added successfully");
                        alert1.setContentText("Factory'" + enteredFactoryName + "' was saved successfully.");
                        alert1.showAndWait();
                        break;

                    }
                }*/


                for(Address address : HelloApplication.addressList){
                    if(enteredCity.get().toLowerCase().compareTo(address.getCity().toLowerCase()) == 0){

                        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

                        AddNewFactoryControllerThread fourthThread = new AddNewFactoryControllerThread(enteredFactoryName, address, "button1", Database.token);

                        Thread thread4 = new Thread(fourthThread, "fourthThread");

                        executorService.execute(thread4);

                        executorService.shutdown();

                        //Database.insertNewFactory(enteredFactoryName, address);

                        address = new BuilderPattern().street(enteredStreetName).house(enteredHouseNumber).city(address.getCity()).code(address.getPostalCode()).build();

                        ExecutorService executorService1 = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

                        AddNewFactoryControllerThread fifthThread = new AddNewFactoryControllerThread(address, "button3", Database.token);

                        Thread thread5 = new Thread(fifthThread, "fifthThread");

                        executorService1.execute(thread5);

                        executorService1.shutdown();

                        //Database.insertNewAddress(address);

                        break;
                    }

                }
            }

            /*try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/factoriesSerialized.dat"))) {

                out.writeObject(factories);

            } catch (IOException ex) {
                System.err.println(ex);
            }*/

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/factorySerialized.dat"))) {

            out.writeObject(factories);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    @FXML
    public void onSaveItemButtonClick() throws SQLException, IOException {

        String enteredFactoryName = factoryNameTextField.getText();
        String enteredStreetName = streetNameTextField.getText();
        String enteredHouseNumber = houseNumberTextField.getText();
        Optional<String> enteredCity = Optional.ofNullable(cityComboBox.getValue());
        Optional<String> enteredChoiceBox = Optional.ofNullable(choiceBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if(enteredFactoryName.isEmpty()){
            s.add("Factory name must not be empty!");
        }

        if(enteredStreetName.isEmpty()){
            s.add("Street name must not be empty!");
        }

        if(enteredHouseNumber.isEmpty()){
            s.add("House number must not be empty!");
        }

        if(enteredCity.isEmpty()){
            s.add("City must not be empty!");
        }

        if(enteredChoiceBox.isEmpty()){
            s.add("Item selection must not be empty!");
        }

        if(s.toString().isEmpty()){

            if(enteredChoiceBox.isPresent() && enteredCity.isPresent()) {

                /*for(Item item : items){
                    if(item.getName().compareTo(enteredChoiceBox.get()) == 0){
                        System.out.println("addani item: " + item.getName());
                        itemList.add(item);
                        break;
                    }
                }*/

                int flag = 0;
                outerloop:
                for(Factory factory : HelloApplication.factoryList) {
                    for (Item item : HelloApplication.itemList) {
                        if (item.getName().compareTo(enteredChoiceBox.get()) == 0) {
                            if (factory.getName().toLowerCase().compareTo(enteredFactoryName.toLowerCase()) == 0){


                                flag = Database.checkFactoryItemDuplicates(factory, item);
                                if(flag == 2){
                                    break outerloop;
                                }

                                ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

                                AddNewFactoryControllerThread thirdThread = new AddNewFactoryControllerThread(factory, item, "button2", Database.token);

                                Thread thread3 = new Thread(thirdThread, "thirdThread");

                                executorService.execute(thread3);

                                executorService.shutdown();

                                //Database.insertNewFactoryItem(factory, item);

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
                    alert.setHeaderText("There are duplicates in the database!\nPlease try again.");
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
