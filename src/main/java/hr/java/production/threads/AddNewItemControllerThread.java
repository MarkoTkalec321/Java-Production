package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import files.FilesUtils;
import hr.java.production.model.Category;
import hr.java.production.model.Discount;
import hr.java.production.model.Item;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class AddNewItemControllerThread implements Runnable{


    private ComboBox<String> categoryComboBox;

    private String enteredItemName;
    private String enteredComboBox;
    private String enteredItemWidth;
    private String enteredItemHeight;
    private String enteredItemLength;
    private String enteredItemProductionCost;
    private String enteredItemSellingPrice;
    private long categoryId;
    private String type;
    private final String token;

    public AddNewItemControllerThread(String enteredItemName, String enteredComboBox,
                                      String enteredItemWidth, String enteredItemHeight,
                                      String enteredItemLength, String enteredItemProductionCost,
                                      String enteredItemSellingPrice, long categoryId, String type, String token) {

        this.enteredItemName = enteredItemName;
        this.enteredComboBox = enteredComboBox;
        this.enteredItemWidth = enteredItemWidth;
        this.enteredItemHeight = enteredItemHeight;
        this.enteredItemLength = enteredItemLength;
        this.enteredItemProductionCost = enteredItemProductionCost;
        this.enteredItemSellingPrice = enteredItemSellingPrice;
        this.categoryId = categoryId;
        this.type = type;
        this.token = token;

    }

    public static List<Item> items = new ArrayList<>();

    public AddNewItemControllerThread(ComboBox<String> categoryComboBox, String type, String token) {
        this.categoryComboBox = categoryComboBox;
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
                //System.out.println("category name: " + category.getName());
                options1.add(category.getName());
            }

            ObservableList<String> options = FXCollections.observableList(options1);
            categoryComboBox.setItems(options);
        }

        if(type.compareTo("SaveButton") == 0) {
            System.out.println("combo " + enteredComboBox);

            try {
                Database.insertNewItem(HelloApplication.itemList.size() + 1, categoryId, enteredItemName,
                        enteredItemWidth, enteredItemHeight, enteredItemLength, enteredItemProductionCost, enteredItemSellingPrice);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

        Database.activeConnectionWithDatabase = false;

        //System.out.println("Dretve: " + Thread.currentThread().getName());

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
