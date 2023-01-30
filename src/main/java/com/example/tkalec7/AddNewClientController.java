package com.example.tkalec7;

import database.Database;
import hr.java.production.model.Address;
import hr.java.production.model.BuilderPattern;
import hr.java.production.model.Client;
import hr.java.production.threads.AddNewClientControllerThread;
import hr.java.production.threads.AddNewFactoryControllerThread;
import hr.java.production.threads.AddNewStoreControllerThread;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *Controller used to add new clients through javaFX.
 */
public class AddNewClientController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField dateOfBirthTextField;

    @FXML
    private TextField streetNameTextField;

    @FXML
    private TextField houseNumberTextField;

    @FXML
    private ComboBox<String> cityComboBox;

    private Timeline clock;

    /**
     * This method appears when Add new clients screen is shown.
     */
    @FXML
    public void initialize() {

        clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {

            /**
             * Fills the ComboBox element with addresses, checks if clientId exists and if it does,
             * fills up used data for specific client into TextFields.
             * @param event
             */
            @Override
            public void handle(ActionEvent event) {

                Set<String> set = new HashSet<>();
                for(Address address : HelloApplication.addressList) {
                    set.add(address.getCity());
                }

                List<String> options2 = new ArrayList<>(set);
                ObservableList<String> options = FXCollections.observableList(options2);
                cityComboBox.setItems(options);

                if (ClientController.clientId != 0) {
                    for (Client client : HelloApplication.clientList) {
                        if (client.getId() == ClientController.clientId) {
                            firstNameTextField.setText(client.getName());
                            lastNameTextField.setText(client.getLastName());
                            dateOfBirthTextField.setText(client.getDateOfBirth());
                            streetNameTextField.setText(client.getAddress().getStreet());
                            houseNumberTextField.setText(client.getAddress().getHouseNumber());
                            cityComboBox.setValue(client.getAddress().getCity());
                            break;
                        }
                    }
                }


            }
        }), new KeyFrame(Duration.seconds(1)));
        //clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }


    /**
     * Method that calls when it is clicked on save client button.
     * @throws InterruptedException
     */
    @FXML
    public void onClickSaveClient() throws InterruptedException {

        String enteredFirstName = firstNameTextField.getText();
        String enteredLastName = lastNameTextField.getText();
        String enteredDate = dateOfBirthTextField.getText();
        String enteredStreetName = streetNameTextField.getText();
        String enteredHouseNumber = houseNumberTextField.getText();
        Optional<String> enteredCity = Optional.ofNullable(cityComboBox.getValue());

        StringJoiner s = new StringJoiner("\n");

        if (enteredFirstName.isEmpty()) {
            s.add("Client first name must not be empty!");
        }

        if (enteredLastName.isEmpty()) {
            s.add("Client last name must not be empty!");
        }

        if (enteredDate.isEmpty()) {
            s.add("Client date of birth must not be empty!");
        }
        else {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                df.setLenient(false);
                df.parse(enteredDate);

            } catch (ParseException e){
                s.add("Client date of birth is not in correct form!");
            }
        }

        if (enteredStreetName.isEmpty()) {
            s.add("Street name must not be empty!");
        }

        if (enteredHouseNumber.isEmpty()) {
            s.add("House number must not be empty!");
        }

        if (enteredCity.isEmpty()) {
            s.add("City must not be empty!");
        }

        if (s.toString().isEmpty()) {
            if (enteredCity.isPresent()) {
                for (Address address : HelloApplication.addressList) {
                    if (enteredCity.get().toLowerCase().compareTo(address.getCity().toLowerCase()) == 0) {

                        address = new BuilderPattern()
                                .street(enteredStreetName)
                                .house(enteredHouseNumber)
                                .city(address.getCity())
                                .code(address.getPostalCode())
                                .build();

                        //Database.insertNewAddress(address);

                        if(ClientController.clientId == 0) {

                            ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_2);

                            AddNewClientControllerThread firstThread = new AddNewClientControllerThread(address, "address", Database.token);
                            AddNewClientControllerThread secondThread = new AddNewClientControllerThread(enteredFirstName, enteredLastName,
                                    enteredDate, HelloApplication.addressList.size() + 1, "insert", Database.token);

                            Thread thread1 = new Thread(firstThread, "firstThread");
                            Thread thread2 = new Thread(secondThread, "secondThread");

                            executorService.execute(thread1);
                            executorService.execute(thread2);

                            executorService.shutdown();
                            executorService.awaitTermination(100, TimeUnit.SECONDS);

                            //Database.insertNewAddress(address);
                            //Database.insertNewClient(enteredFirstName, enteredLastName, enteredDate, HelloApplication.addressList.size() + 1);

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Client added successfully");
                            alert.setHeaderText("Client added successfully");
                            alert.setContentText("Client '" + enteredFirstName + "' was saved successfully.");
                            alert.showAndWait();

                            break;
                        }
                        else{
                            System.out.println("client id " + ClientController.clientId);

                            ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_2);

                            AddNewClientControllerThread thirdThread = new AddNewClientControllerThread(address, "address", Database.token);
                            AddNewClientControllerThread fourthThread = new AddNewClientControllerThread(enteredFirstName, enteredLastName,
                                    enteredDate, HelloApplication.addressList.size() + 1, ClientController.clientId, "edit", Database.token);

                            Thread thread3 = new Thread(thirdThread, "thirdThread");
                            Thread thread4 = new Thread(fourthThread, "fourthThread");

                            executorService.execute(thread3);
                            executorService.execute(thread4);

                            executorService.shutdown();
                            executorService.awaitTermination(100, TimeUnit.SECONDS);

                            //Database.insertNewAddress(address);
                            //Database.editExistingClient(enteredFirstName, enteredLastName, enteredDate, HelloApplication.addressList.size() + 1, ClientController.clientId);

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Client edited successfully");
                            alert.setHeaderText("Client edited successfully");
                            alert.setContentText("Client '" + enteredFirstName + "' was edited successfully.");
                            alert.showAndWait();

                            break;
                        }
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Errors");
            alert.setHeaderText("There was one or more errors");
            alert.setContentText(s.toString());
            alert.showAndWait();
        }
    }
}
