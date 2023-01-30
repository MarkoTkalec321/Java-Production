package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Category;
import hr.java.production.model.Item;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemControllerThread implements Runnable{

    private int idx = 0;

    private TextField itemNameTextField;
    private ComboBox<String> categoryComboBox;
    private TableView<Item> itemTableView;
    private TableColumn<Item, String> nameTableColumn;
    private TableColumn<Item, String> idTableColumn;
    private TableColumn<Item, String> categoryTableColumn;
    private TableColumn<Item, String> widthTableColumn;
    private TableColumn<Item, String> heightTableColumn;
    private TableColumn<Item, String> lengthTableColumn;
    private TableColumn<Item, String> productionCostTableColumn;
    private TableColumn<Item, String> sellingPriceTableColumn;
    private String type;
    private final String token;

    public ItemControllerThread(ComboBox<String> categoryComboBox, TableView<Item> itemTableView,
                                TableColumn<Item, String> nameTableColumn, TableColumn<Item, String> idTableColumn, TableColumn<Item, String> categoryTableColumn,
                                TableColumn<Item, String> widthTableColumn, TableColumn<Item, String> heightTableColumn, TableColumn<Item, String> lengthTableColumn,
                                TableColumn<Item, String> productionCostTableColumn, TableColumn<Item, String> sellingPriceTableColumn, String type, String token) {

        this.categoryComboBox = categoryComboBox;
        this.itemTableView = itemTableView;
        this.nameTableColumn = nameTableColumn;
        this.idTableColumn = idTableColumn;
        this.categoryTableColumn = categoryTableColumn;
        this.widthTableColumn = widthTableColumn;
        this.heightTableColumn = heightTableColumn;
        this.lengthTableColumn = lengthTableColumn;
        this.productionCostTableColumn = productionCostTableColumn;
        this.sellingPriceTableColumn = sellingPriceTableColumn;
        this.type = type;
        this.token = token;

    }

    public ItemControllerThread(TextField itemNameTextField, ComboBox<String> categoryComboBox, TableView<Item> itemTableView, String type, String token) {
        this.itemNameTextField = itemNameTextField;
        this.categoryComboBox = categoryComboBox;
        this.itemTableView = itemTableView;
        this.type = type;
        this.token = token;
    }

    @Override
    public void run() {
        while (Database.activeConnectionWithDatabase == true) {
            try {
                System.out.println("Ime dretve: " + Thread.currentThread().getName());
                //wait();
                synchronized (this.token) {
                    this.token.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Database.activeConnectionWithDatabase = true;

        if(type.compareTo("categories") == 0) {
            List<String> options1 = new ArrayList<>();
            for(Category category : HelloApplication.categoryList){
                options1.add(category.getName());
            }

            ObservableList<String> options = FXCollections.observableList(options1);
            categoryComboBox.setItems(options);

            categoryTableColumn.setCellValueFactory(cellData -> {
                String temp = null;
                for(Category category : HelloApplication.categoryList){
                    if(category.getId().compareTo(cellData.getValue().getCategoryId()) == 0){
                        temp = category.getName();
                    }
                }
                return new SimpleStringProperty(temp);
            });
        }

        if(type.compareTo("items") == 0) {
            nameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });

            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });


            widthTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getWidth().toString());
            });

            heightTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getHeight().toString());
            });

            lengthTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getLength().toString());
            });

            productionCostTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getProductionCost().toString());
            });

            sellingPriceTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getSellingPrice().toString());
            });

            itemTableView.setItems(FXCollections.observableList(HelloApplication.itemList));
        }

        if(type.compareTo("SearchButton") == 0) {

            String enteredItemName = itemNameTextField.getText();
            Optional<String> enteredComboBox = Optional.ofNullable(categoryComboBox.getValue());

            List<Item> filteredList = new ArrayList<>();
            //try {
            filteredList = new ArrayList<>(HelloApplication.itemList);
        /*} catch (SQLException | IOException e) {
            e.printStackTrace();
        }*/

            if(Optional.of(enteredItemName).isEmpty() == false) {
                filteredList = filteredList.stream()
                        .filter(n -> n.getName().toLowerCase().contains(enteredItemName.toLowerCase()))
                        .collect(Collectors.toList());
            }

            Integer categoryId = null;
            if(enteredComboBox.isPresent()) {
                try {
                    for (Category category : Database.getAllCategories()) {

                        if(enteredComboBox.get().compareTo(category.getName()) == 0){

                            categoryId = Math.toIntExact(category.getId());
                            break;
                        }
                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }

            if(enteredComboBox.isPresent()){
                Integer finalCategoryId = categoryId;
                filteredList = filteredList.stream()
                        .filter(n -> ( (String.valueOf(n.getCategoryId()))).compareTo(String.valueOf(finalCategoryId)) == 0)
                        .collect(Collectors.toList());
            }

            itemTableView.setItems(FXCollections.observableList(filteredList));
        }

        idx++;

        Database.activeConnectionWithDatabase = false;

        //System.out.println("Dretva broj #" + idx);

        //System.out.println("notify");
        //System.out.println("Dretve: " + Thread.currentThread().getName());

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
