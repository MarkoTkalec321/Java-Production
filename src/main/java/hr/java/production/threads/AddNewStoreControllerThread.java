package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Item;
import hr.java.production.model.Store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddNewStoreControllerThread implements Runnable{


    private TextField storeNameTextField;
    private TextField storeAddressTextField;
    private ChoiceBox<String> storeChoiceBox;
    private String enteredStoreName;
    private String enteredStoreWebAddress;
    private Store store;
    private Item item;
    private String type;
    private final String token;

    public AddNewStoreControllerThread(Store store, Item item, String type, String token) {
        this.store = store;
        this.item = item;
        this.type = type;
        this.token = token;
    }

    public AddNewStoreControllerThread(String enteredStoreName, String enteredStoreWebAddress, String type, String token) {
        this.enteredStoreName = enteredStoreName;
        this.enteredStoreWebAddress = enteredStoreWebAddress;
        this.type = type;
        this.token = token;
    }

    public AddNewStoreControllerThread(ChoiceBox<String> storeChoiceBox, String type, String token) {
        this.storeChoiceBox = storeChoiceBox;
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

        if(type.compareTo("items") == 0) {
            List<String> options1 = new ArrayList<>();
            for(Item item : HelloApplication.itemList){
                options1.add(item.getName());
            }

            ObservableList<String> cursors = FXCollections.observableArrayList(options1);
            storeChoiceBox.setItems(cursors);
        }

        if(type.compareTo("button1") == 0) {
            try {
                Database.insertNewStore(enteredStoreName, enteredStoreWebAddress);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

        if(type.compareTo("button2") == 0) {
            try {
                Database.insertNewStoreItem(store, item);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Database.activeConnectionWithDatabase = false;

        synchronized (this.token) {
            this.token.notifyAll();
        }

    }
}
