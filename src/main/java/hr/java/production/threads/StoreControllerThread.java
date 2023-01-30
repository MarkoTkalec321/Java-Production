package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Store;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoreControllerThread implements Runnable{

    private TextField storeNameTextField;
    private TableView<Store> storeTableView;
    private TableColumn<Store, String> nameTableColumn;
    private TableColumn<Store, String> idTableColumn;
    private TableColumn<Store, String> addressTableColumn;
    private String type;
    private final String token;

    public StoreControllerThread(TableView<Store> storeTableView, TableColumn<Store, String> nameTableColumn, TableColumn<Store, String> idTableColumn, TableColumn<Store, String> addressTableColumn, String type, String token) {
        this.storeTableView = storeTableView;
        this.nameTableColumn = nameTableColumn;
        this.idTableColumn = idTableColumn;
        this.addressTableColumn = addressTableColumn;
        this.type = type;
        this.token = token;
    }

    public StoreControllerThread(TextField storeNameTextField, TableView<Store> storeTableView, TableColumn<Store, String> nameTableColumn, TableColumn<Store, String> idTableColumn, TableColumn<Store, String> addressTableColumn, String type, String token) {
        this.storeNameTextField = storeNameTextField;
        this.storeTableView = storeTableView;
        this.nameTableColumn = nameTableColumn;
        this.idTableColumn = idTableColumn;
        this.addressTableColumn = addressTableColumn;
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

        if(type.compareTo("store") == 0) {
            nameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });

            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });

            addressTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getWebAddress());
            });


            ObservableList<Store> storeObservableList = FXCollections.observableList(HelloApplication.storeList);
            storeTableView.setItems(storeObservableList);
        }

        if(type.compareTo("button") == 0) {
            String enteredItemName = storeNameTextField.getText();

            List<Store> filteredList = new ArrayList<>(HelloApplication.storeList);

            if(Optional.of(enteredItemName).isEmpty() == false) {
                filteredList = filteredList.stream()
                        .filter(n -> n.getName().contains(enteredItemName))
                        .collect(Collectors.toList());
            }

            storeTableView.setItems(FXCollections.observableList(filteredList));
        }

        Database.activeConnectionWithDatabase = false;

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
