package com.example.tkalec7;

import database.Database;
import files.FilesUtils;
import hr.java.production.main.Main;
import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import hr.java.production.threads.CategoriesControllerThread;
import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CategoriesController {
    private static final int NUMBER_OF_CATEGORIES = 4;//3
    private static final int NUMBER_OF_ITEMS = 5;//5
    private static final int NUMBER_OF_FACTORIES = 2;//2
    private static final int NUMBER_OF_STORES = 2;//2
    private static final Integer NUMBER_OF_LINES_CATEGORIES = 3;
    private static final Integer NUMBER_OF_LINES_ITEMS = 9;
    private static final Integer NUMBER_OF_LINES_ADDRESSES = 4;
    private static final Integer NUMBER_OF_LINES_FACTORIES = 4;
    private static final Integer NUMBER_OF_LINES_STORES = 4;
    private static final Integer NUMBER_OF_ADDRESSES = 3;
    private static final Integer NUMBER_OF_FOODSTORES = 2;
    private static final Integer NUMBER_OF_TechStores = 2;
    private static Integer realChosenFactoryArticlesNumbers;

    public static Category[] makeObjectFromFileCategories(Category[] categories, String datCat) {
        Category[] pom;
        try (BufferedReader in = new BufferedReader(new FileReader(datCat))) {
            String line;
            Integer counterOfLines = 1;
            int br = 0;

            String id = "id";
            String name = "name";
            String description;


            while ((line = in.readLine()) != null) {
                if (counterOfLines % NUMBER_OF_LINES_CATEGORIES == 1) {
                    id = line;
                } else if (counterOfLines % NUMBER_OF_LINES_CATEGORIES == 2) {
                    name = line;
                } else if (counterOfLines % NUMBER_OF_LINES_CATEGORIES == 0) {
                    description = line;
                    Category datCategory = new Category(name, Long.parseLong(id), description);
                    categories[br] = datCategory;
                    br++;
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }
        pom = categories;

        return pom;
    }

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TextField categoryDescriptionTextField;

    @FXML
    private TableView<Category> categoryTableView;

    @FXML
    private TableColumn<Category, String> nameTableColumn;

    @FXML
    private TableColumn<Category, String> idTableColumn;

    @FXML
    private TableColumn<Category, String> descriptionTableColumn;

    //private static Category[] categories = new Category[NUMBER_OF_CATEGORIES];
    private static List<Item> items = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();

    @FXML
    public void initialize() throws InterruptedException {

        /*for(Category category : AddNewCategoryController.categories){
            System.out.println("Iz itemsSerialized: " + category.getName() + " " + category.getDescription() + " " + category.getId());
        }*/

        /*for (Category category : HelloApplication.categoryList) {
            System.out.println(category.getName());
        }*/


        /*categories.clear();
        try{
            FilesUtils.getDeserialization(categories, "dat/categoriesSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }*/


       /* nameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        idTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });

        descriptionTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });


        //ObservableList<Category> categoryObservable = FXCollections.observableList(Arrays.stream(categories).toList());
        categoryTableView.setItems(FXCollections.observableList(HelloApplication.categoryList));*/

        CategoriesControllerThread firstThread = new CategoriesControllerThread(categoryTableView, nameTableColumn, idTableColumn,
                descriptionTableColumn, "initialize", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread1);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

    }

    @FXML
    protected void onSearchButtonClick() throws InterruptedException {

        String enteredCategoryName = categoryNameTextField.getText();
        String enteredCategoryDescription = categoryDescriptionTextField.getText();


        /*List<Category> filteredList = new ArrayList<>(HelloApplication.categoryList);


        filteredList = filteredList.stream()
                .filter(n -> n.getName().toLowerCase().contains(enteredCategoryName.toLowerCase()))
                .collect(Collectors.toList());

        filteredList = filteredList.stream()
                .filter(n -> n.getDescription().toLowerCase().contains(enteredCategoryDescription.toLowerCase()))
                .collect(Collectors.toList());


        categoryTableView.setItems(FXCollections.observableList(filteredList));*/

        CategoriesControllerThread secondThread = new CategoriesControllerThread("gumb", Database.token, enteredCategoryName,
                enteredCategoryDescription, categoryTableView);

        Thread thread1 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread1);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }

    /*@FXML
    public void onDeleteButtonClick(){

        String name = categoryTableView.getSelectionModel().getSelectedItem().getName();
        //System.out.println("name = " + name);
        for(Category category : categories){
            if(category.getName().compareTo(name) == 0){
                categories.remove(category);
                break;
            }
        }

        try{
            FilesUtils.getSerialization(categories, "dat/categoriesSerialized.dat");
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void onEditButtonClick(){
        String name = categoryTableView.getSelectionModel().getSelectedItem().getName();
        for(Category category : categories){
            if(category.getName().compareTo(name) == 0){
                //category = new Category("jaja", category.getId() , "hehe");
                category.setName("jaja");
                category.setDescription("haha");
                break;
            }
        }

        try{
            FilesUtils.getSerialization(categories, "dat/categoriesSerialized.dat");
        } catch (IOException e){
            e.printStackTrace();
        }

    }*/
}