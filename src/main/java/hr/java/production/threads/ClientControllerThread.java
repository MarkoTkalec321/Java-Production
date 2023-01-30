package hr.java.production.threads;

import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class used with threads to show data from database.
 */
public class ClientControllerThread implements Runnable{

    private TextField firstNameTextfield;
    private TextField lastNameTextfield;
    private TextField dateOfBirthTextField;
    private TextField addressTextField;

    private TableView<Client> clientTableView;
    private TableColumn<Client, String> firstNameTableColumn;
    private TableColumn<Client, String> lastNameTableColumn;
    private TableColumn<Client, String> dateTableColumn;
    private TableColumn<Client, String> addressNameTableColumn;
    private String type;
    private final String token;

    /**
     * Takes in clientTableView, firstNameTableColumn, lastNameTableColumn, dateTableColumn, addressNameTableColumn, type, token.
     * @param clientTableView
     * @param firstNameTableColumn
     * @param lastNameTableColumn
     * @param dateTableColumn
     * @param addressNameTableColumn
     * @param type
     * @param token
     */
    public ClientControllerThread(TableView<Client> clientTableView,
                                  TableColumn<Client, String> firstNameTableColumn,
                                  TableColumn<Client, String> lastNameTableColumn,
                                  TableColumn<Client, String> dateTableColumn,
                                  TableColumn<Client, String> addressNameTableColumn,
                                  String type, String token) {

        this.clientTableView = clientTableView;
        this.firstNameTableColumn = firstNameTableColumn;
        this.lastNameTableColumn = lastNameTableColumn;
        this.dateTableColumn = dateTableColumn;
        this.addressNameTableColumn = addressNameTableColumn;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in firstNameTextfield, firstNameTextfield, dateOfBirthTextField, addressTextField,
     * clientTableView, firstNameTableColumn, lastNameTableColumn, dateTableColumn, addressNameTableColumn,
     * type, token.
     * @param firstNameTextfield
     * @param lastNameTextfield
     * @param dateOfBirthTextField
     * @param addressTextField
     * @param clientTableView
     * @param firstNameTableColumn
     * @param lastNameTableColumn
     * @param dateTableColumn
     * @param addressNameTableColumn
     * @param type
     * @param token
     */
    public ClientControllerThread(TextField firstNameTextfield,
                                  TextField lastNameTextfield,
                                  TextField dateOfBirthTextField,
                                  TextField addressTextField,
                                  TableView<Client> clientTableView,
                                  TableColumn<Client, String> firstNameTableColumn,
                                  TableColumn<Client, String> lastNameTableColumn,
                                  TableColumn<Client, String> dateTableColumn,
                                  TableColumn<Client, String> addressNameTableColumn,
                                  String type, String token) {

        this.firstNameTextfield = firstNameTextfield;
        this.lastNameTextfield = lastNameTextfield;
        this.dateOfBirthTextField = dateOfBirthTextField;
        this.addressTextField = addressTextField;
        this.clientTableView = clientTableView;
        this.firstNameTableColumn = firstNameTableColumn;
        this.lastNameTableColumn = lastNameTableColumn;
        this.dateTableColumn = dateTableColumn;
        this.addressNameTableColumn = addressNameTableColumn;
        this.type = type;
        this.token = token;
    }

    /**
     * This method is used for synchronizing threads with token.
     */
    @Override
    public void run() {
        while (Database.activeConnectionWithDatabase == true) {
            try {
                System.out.println("Ime dretve: " + Thread.currentThread().getName());
                synchronized (this.token) {
                    this.token.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Database.activeConnectionWithDatabase = true;

        if(type.compareTo("client") == 0){
            firstNameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getName());
            });

            lastNameTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getLastName());
            });

            dateTableColumn.setCellValueFactory(cellData -> {
                return new SimpleStringProperty(cellData.getValue().getDateOfBirth());
            });

            addressNameTableColumn.setCellValueFactory(cellData -> {
                String temp = null;
                for(Address address : HelloApplication.addressList){
                    if(address.getId().compareTo(cellData.getValue().getId()) == 0){
                        temp = address.getCity() +  " " + address.getPostalCode() + " " + address.getStreet();
                        break;
                    }
                }
                return new SimpleStringProperty(temp);
            });

            try {
                clientTableView.setItems(FXCollections.observableList(Database.getAllClients()));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.compareTo("button") == 0) {
            String enteredFirstName = firstNameTextfield.getText();
            String enteredLastName = lastNameTextfield.getText();
            String enteredDate = dateOfBirthTextField.getText();
            String enteredAddress = addressTextField.getText();

            List<Client> filteredList = new ArrayList<>(HelloApplication.clientList);


            filteredList = filteredList.stream()
                    .filter(n -> n.getName().toLowerCase().contains(enteredFirstName.toLowerCase()))
                    .collect(Collectors.toList());

            filteredList = filteredList.stream()
                    .filter(n -> n.getLastName().toLowerCase().contains(enteredLastName.toLowerCase()))
                    .collect(Collectors.toList());

            filteredList = filteredList.stream()
                    .filter(n -> n.getDateOfBirth().toLowerCase().contains(enteredDate.toLowerCase()))
                    .collect(Collectors.toList());

            filteredList = filteredList.stream()
                    .filter(n -> n.getAddress().getStreet().toLowerCase().contains(enteredAddress.toLowerCase()) ||
                            n.getAddress().getCity().toLowerCase().contains(enteredAddress.toLowerCase()) ||
                            n.getAddress().getPostalCode().toLowerCase().contains(enteredAddress.toLowerCase()))
                    //.filter(n -> n.get
                    .collect(Collectors.toList());

            clientTableView.setItems(FXCollections.observableList(filteredList));
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
