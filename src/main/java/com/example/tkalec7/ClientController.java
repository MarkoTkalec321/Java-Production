package com.example.tkalec7;

import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.Category;
import hr.java.production.model.Client;
import hr.java.production.sort.ClientSorter;
import hr.java.production.threads.ClientControllerThread;
import hr.java.production.threads.ClientSorterThread;
import hr.java.production.threads.SortingItemsThread;
import hr.java.production.threads.StoreControllerThread;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Contains TextFields used for filtering clients and table to show all clients from database.
 */
public class ClientController {

    @FXML
    private TextField firstNameTextfield;

    @FXML
    private TextField lastNameTextfield;

    @FXML
    private TextField dateOfBirthTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, String> firstNameTableColumn;

    @FXML
    private TableColumn<Client, String> lastNameTableColumn;

    @FXML
    private TableColumn<Client, String> dateTableColumn;

    @FXML
    private TableColumn<Client, String> addressNameTableColumn;

    @FXML
    private ContextMenu contextMenu;


    @FXML
    private MenuItem contextMenuItem;

    public static long clientId = 0;

    private Timeline clock;

    /**
     * Shows this screen when client controller screen is pressed.
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    public void initialize() throws SQLException, IOException, InterruptedException {

        /*firstNameTableColumn.setCellValueFactory(cellData -> {
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

        clientTableView.setItems(FXCollections.observableList(Database.getAllClients()));*/



        ClientControllerThread firstThread = new ClientControllerThread(clientTableView, firstNameTableColumn,
                lastNameTableColumn, dateTableColumn,
                addressNameTableColumn, "client", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread1);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

        Platform.runLater(new ClientSorterThread(HelloApplication.clientList));

        ClientSorterThread clientSorterthread = new ClientSorterThread(HelloApplication.clientList);
        Thread thread = new Thread(clientSorterthread);
        thread.start();

        clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){

            /**
             * Switches between the youngest and the oldest client every 5 seconds.
             * @param event
             */
            @Override
            public void handle(ActionEvent event) {

                HelloApplication.getStage().setTitle("The youngest client: " + HelloApplication.clientList.get(0).getName());

                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                int size = HelloApplication.clientList.size();
                HelloApplication.getStage().setTitle("The oldest client: " + HelloApplication.clientList.get(size - 1).getName());
            }
        }), new KeyFrame(Duration.seconds(5)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


    }

    /**
     * Searches a client based on filters.
     * @throws InterruptedException
     */
    @FXML
    public void clientSearchButton() throws InterruptedException {

        /*String enteredFirstName = firstNameTextfield.getText();
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

        clientTableView.setItems(FXCollections.observableList(filteredList));*/


        ClientControllerThread secondThread = new ClientControllerThread(firstNameTextfield,
                lastNameTextfield, dateOfBirthTextField,
                addressTextField, clientTableView,
                firstNameTableColumn, lastNameTableColumn,
                dateTableColumn, addressNameTableColumn,
                "button", Database.token);

        Thread thread2 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        executorService.execute(thread2);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);


    }

    /**
     * This method is called when certain row is right clicked to edit a client.
     */
    @FXML
    public void editClientButton() {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNewClient.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();


        if((clientTableView.getSelectionModel().getSelectedItem()) != null){
            System.out.println(clientTableView.getSelectionModel().getSelectedItem().getId());
            clientId = clientTableView.getSelectionModel().getSelectedItem().getId();
        }


    }

}
