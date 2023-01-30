package hr.java.production.threads;

import com.example.tkalec7.ClientController;
import com.example.tkalec7.HelloApplication;
import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class uses synchronized threads to do certain tasks.
 */
public class AddNewClientControllerThread implements Runnable{

    private String enteredFirstName;
    private String enteredLastName;
    private String enteredDate;

    private TextField streetNameTextField;
    private TextField houseNumberTextField;
    private ComboBox<String> cityComboBox;
    private long listSize;
    private long clientId;
    private Address address;

    private String type;
    private final String token;


    /**
     * Takes in address, type and token.
     * @param address
     * @param type
     * @param token
     */
    public AddNewClientControllerThread(Address address, String type, String token) {
        this.address = address;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in enteredFirstName, enteredLastName, enteredDate, listSize, type, token.
     * @param enteredFirstName
     * @param enteredLastName
     * @param enteredDate
     * @param listSize
     * @param type
     * @param token
     */
    public AddNewClientControllerThread(String enteredFirstName, String enteredLastName, String enteredDate, long listSize, String type, String token) {
        this.enteredFirstName = enteredFirstName;
        this.enteredLastName = enteredLastName;
        this.enteredDate = enteredDate;
        this.listSize = listSize;
        this.type = type;
        this.token = token;
    }

    /**
     * Takes in enteredFirstName, enteredLastName, enteredDate, listSize, clientId, type, token.
     * @param enteredFirstName
     * @param enteredLastName
     * @param enteredDate
     * @param listSize
     * @param clientId
     * @param type
     * @param token
     */
    public AddNewClientControllerThread(String enteredFirstName, String enteredLastName, String enteredDate, long listSize, long clientId, String type, String token) {
        this.enteredFirstName = enteredFirstName;
        this.enteredLastName = enteredLastName;
        this.enteredDate = enteredDate;
        this.listSize = listSize;
        this.clientId = clientId;
        this.type = type;
        this.token = token;
    }

    /**
     * This method is called with executorService.
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

        if(type.compareTo("address") == 0) {
            try {
                Database.insertNewAddress(address);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.compareTo("insert") == 0) {
            try {
                Database.insertNewClient(enteredFirstName, enteredLastName, enteredDate, HelloApplication.addressList.size() + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(type.compareTo("edit") == 0) {
            try {
                Database.editExistingClient(enteredFirstName, enteredLastName, enteredDate, HelloApplication.addressList.size() + 1, ClientController.clientId);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
