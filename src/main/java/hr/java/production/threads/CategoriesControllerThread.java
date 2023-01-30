package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesControllerThread implements Runnable{


    private TableView<Category> categoryTableView;
    private TableColumn<Category, String> nameTableColumn;
    private TableColumn<Category, String> idTableColumn;
    private TableColumn<Category, String> descriptionTableColumn;
    private String type;
    private final String token;
    public static List<Category> categories = new ArrayList<>();

    private String enteredCategoryName;
    private String enteredCategoryDescription;

    public CategoriesControllerThread(TableView<Category> categoryTableView,
                                      TableColumn<Category, String> nameTableColumn,
                                      TableColumn<Category, String> idTableColumn,
                                      TableColumn<Category, String> descriptionTableColumn,
                                      String type, String token) {
        this.categoryTableView = categoryTableView;
        this.nameTableColumn = nameTableColumn;
        this.idTableColumn = idTableColumn;
        this.descriptionTableColumn = descriptionTableColumn;
        this.type = type;
        this.token = token;
    }

    public CategoriesControllerThread(String type, String token, String enteredCategoryName, String enteredCategoryDescription, TableView<Category> categoryTableView) {
        this.type = type;
        this.token = token;
        this.enteredCategoryName = enteredCategoryName;
        this.enteredCategoryDescription = enteredCategoryDescription;
        this.categoryTableView = categoryTableView;
    }

    @Override
    public void run() {
        while (Database.activeConnectionWithDatabase == true) {
            System.out.println("KAJJJ");
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

        if(type.compareTo("initialize") == 0) {
            nameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });

            idTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getId().toString());
            });

            descriptionTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getDescription());
            });


            //ObservableList<Category> categoryObservable = FXCollections.observableList(Arrays.stream(categories).toList());
            categoryTableView.setItems(FXCollections.observableList(HelloApplication.categoryList));
        }

        if(type.compareTo("gumb") == 0) {
            List<Category> filteredList = new ArrayList<>(HelloApplication.categoryList);


            filteredList = filteredList.stream()
                    .filter(n -> n.getName().toLowerCase().contains(enteredCategoryName.toLowerCase()))
                    .collect(Collectors.toList());

            filteredList = filteredList.stream()
                    .filter(n -> n.getDescription().toLowerCase().contains(enteredCategoryDescription.toLowerCase()))
                    .collect(Collectors.toList());


            categoryTableView.setItems(FXCollections.observableList(filteredList));
        }

        Database.activeConnectionWithDatabase = false;

        synchronized (this.token) {
            this.token.notifyAll();
        }
    }
}
