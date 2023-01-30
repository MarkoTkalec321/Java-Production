package com.example.tkalec7;

import database.Database;
import files.FilesUtils;
import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import hr.java.production.threads.AddNewItemControllerThread;
import hr.java.production.threads.ItemControllerThread;
import hr.java.production.threads.SortingItemsThread;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleBinaryOperator;

public class AddNewItemController {

    @FXML
    private TextField itemNameTextField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextField itemWidthTextField;

    @FXML
    private TextField itemHeightTextField;

    @FXML
    private TextField itemLengthTextField;

    @FXML
    private TextField itemProductionCostTextField;

    @FXML
    private TextField itemSellingPriceTextField;

    public static List<Item> items = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    @FXML
    public void initialize() throws InterruptedException {

        /*items.clear();

        try {
            FilesUtils.getDeserialization(items, "dat/itemsSerialized.dat");
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        categories.clear();

        try {
            FilesUtils.getDeserialization(categories, "dat/categoriesSerialized.dat");
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }*/


        AddNewItemControllerThread firstThread = new AddNewItemControllerThread(categoryComboBox, "categories", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread1);

        executorService.shutdown();

        executorService.awaitTermination(100, TimeUnit.SECONDS);

        /*List<String> options1 = new ArrayList<>();
        for(Category category : HelloApplication.categoryList){
            //System.out.println("category name: " + category.getName());
            options1.add(category.getName());
        }

        ObservableList<String> options = FXCollections.observableList(options1);
        categoryComboBox.setItems(options);*/
    }

    @FXML
    public void onSaveButtonClick() throws SQLException, IOException, InterruptedException {



        String enteredItemName = itemNameTextField.getText();
        Optional<String> enteredComboBox = Optional.ofNullable(categoryComboBox.getValue());
        String enteredItemWidth = itemWidthTextField.getText();
        String enteredItemHeight = itemHeightTextField.getText();
        String enteredItemLength = itemLengthTextField.getText();
        String enteredItemProductionCost = itemProductionCostTextField.getText();
        String enteredItemSellingPrice = itemSellingPriceTextField.getText();


        StringJoiner s = new StringJoiner("\n");

        if(enteredItemName.isEmpty()){
            s.add("Item name must not be empty!");
        }

        if(enteredComboBox.isEmpty()){
            s.add("Category must not be empty!");
        }

        if(enteredItemWidth.isEmpty()){
            s.add("Item width must not be empty!");
        }
        else {
            try{
                Double width = Double.parseDouble(enteredItemWidth);
            }
            catch (NumberFormatException e){
                s.add("Item width must be in decimal format!");
            }
        }

        if(enteredItemHeight.isEmpty()){
            s.add("Item height must not be empty!");
        }
        else {
            try{
                Double height = Double.parseDouble(enteredItemHeight);
            }
            catch (NumberFormatException e){
                s.add("Item height must be in decimal format!");
            }
        }

        if(enteredItemLength.isEmpty()){
            s.add("Item length must not be empty!");
        }
        else {
            try{
                Double length = Double.parseDouble(enteredItemLength);
            }
            catch (NumberFormatException e){
                s.add("Item length must be in decimal format!");
            }
        }

        if(enteredItemProductionCost.isEmpty()){
            s.add("Item production cost must not be empty!");
        }
        else {
            try{
                Double productionCost = Double.parseDouble(enteredItemProductionCost);
            }
            catch (NumberFormatException e){
                s.add("Item production cost must be in decimal format!");
            }
        }

        if(enteredItemSellingPrice.isEmpty()){
            s.add("Item selling price must not be empty!");
        }
        else {
            try{
                Double sellingPrice = Double.parseDouble(enteredItemSellingPrice);
            }
            catch (NumberFormatException e){
                s.add("Item selling price must be in decimal format!");
            }
        }
        if(s.toString().isEmpty()){

            if(enteredComboBox.isPresent()) {

                for(Category category : HelloApplication.categoryList) {
                    if (enteredComboBox.get().toLowerCase().compareTo(category.getName().toLowerCase()) == 0) {
                        BigDecimal decimal = BigDecimal.valueOf(Math.random());
                        Random random = new Random();
                        int upper = 99;
                        int int_random = random.nextInt(upper);
                        BigDecimal discount = decimal.add(BigDecimal.valueOf(int_random));
                        Discount discount1 = new Discount(discount);

                        //System.out.println("category name: " + category.getName());
                        HelloApplication.unsortedItemList.add(new Item(enteredItemName, items.size() + 1, category, BigDecimal.valueOf(Double.parseDouble((enteredItemWidth))), BigDecimal.valueOf(Double.parseDouble(enteredItemHeight)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemLength)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemProductionCost)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemSellingPrice)),
                                discount1));

                        HelloApplication.itemList.add(new Item(HelloApplication.itemList.size() + 1, category.getId(), enteredItemName,
                                BigDecimal.valueOf(Double.parseDouble(enteredItemWidth)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemHeight)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemLength)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemProductionCost)),
                                BigDecimal.valueOf(Double.parseDouble(enteredItemSellingPrice))));


                        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

                        //System.out.println("combo2 " + categoryComboBox);
                        AddNewItemControllerThread secondThread = new AddNewItemControllerThread(enteredItemName, enteredComboBox.get(),
                                enteredItemWidth, enteredItemHeight,
                                enteredItemLength, enteredItemProductionCost,
                                enteredItemSellingPrice, category.getId(), "SaveButton", Database.token);


                        Thread thread2 = new Thread(secondThread, "thirdThread");

                        executorService.execute(thread2);

                        executorService.shutdown();
                        //executorService.awaitTermination(100, TimeUnit.SECONDS);

                        /*Database.insertNewItem(HelloApplication.itemList.size() + 1, category.getId(), enteredItemName,
                                enteredItemWidth, enteredItemHeight, enteredItemLength, enteredItemProductionCost, enteredItemSellingPrice);*/

                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Item added successfully");
                        alert1.setHeaderText("Item added successfully");
                        alert1.setContentText("The Item '" + enteredItemName + "' was saved successfully.");
                        alert1.showAndWait();
                        break;
                    }
                }

            }


            try {
                FilesUtils.getSerialization(items, "dat/itemsSerialized.dat");
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


}
