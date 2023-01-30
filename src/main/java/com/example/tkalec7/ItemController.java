package com.example.tkalec7;

import database.Database;
import hr.java.production.model.*;
import hr.java.production.threads.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ItemController {

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
    public static List<Item> makeObjectFromFileItems(Category[] categories, String datItem) {
        List<Item> list = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(datItem))) {
            String line;
            Integer counterOfLines = 1;
            int br = 0;

            String id = "id";
            String name = "name";
            String width;
            String height;
            String length;
            String productionCost;
            String sellingPrice;
            String discount = "discountAmount";

            Optional<Category> pom = Optional.empty();
            Category pom1 = null;
            BigDecimal widthPom = new BigDecimal(0);
            BigDecimal heightPom = new BigDecimal(0);
            BigDecimal lengthPom = new BigDecimal(0);
            BigDecimal productionCostPom = new BigDecimal(0);
            BigDecimal sellingPricePom = new BigDecimal(0);
            BigDecimal discountAmount = new BigDecimal(0);
            Discount discount1 = new Discount(discountAmount);

            while ((line = in.readLine()) != null) {
                if (counterOfLines == 21) {
                    System.out.println();
                }
                if (counterOfLines % NUMBER_OF_LINES_ITEMS == 1) {

                    id = line;
                    //System.out.println("id " + id);

                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 2) {
                    name = line;
                    //System.out.println("name :" + name);
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 3) {
                    //System.out.println();
                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].getName().compareTo(line) == 0) {
                            pom = Optional.of(categories[i]);
                            System.out.println("cat " + pom.get().getName());
                            //pom1 = categories[i];
                        }
                    }
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 4) {
                    width = line;
                    widthPom = new BigDecimal(width);
                    //System.out.println("widthpom :" + widthPom);
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 5) {
                    height = line;
                    heightPom = new BigDecimal(height);
                    //System.out.println("heightPom " + heightPom);
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 6) {
                    length = line;
                    lengthPom = new BigDecimal(length);
                    //System.out.println("lengthPom " + lengthPom);
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 7) {
                    productionCost = line;
                    productionCostPom = new BigDecimal(productionCost);
                    //System.out.println("productionCostPom " + productionCostPom);
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 8) {
                    sellingPrice = line;
                    sellingPricePom = new BigDecimal(sellingPrice);
                    //System.out.println("sellingPricePom " + sellingPricePom);
                } else if (counterOfLines % NUMBER_OF_LINES_ITEMS == 0) {
                    discount = line;
                    //System.out.println("discount " + discount);
                    discountAmount = new BigDecimal(discount);
                    discount1 = new Discount(discountAmount);

                    if (pom.isPresent()) {
                        Item datItem1 = new Item(name, Long.parseLong(id), pom.get(), widthPom, heightPom, lengthPom, productionCostPom, sellingPricePom, discount1);
                        list.add(datItem1);
                    }
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return list;
    }
    public static Factory[] makeObjectFromFileFactories(Address[] addresses, List<Item> items, String datFact) {
        Factory[] factories = new Factory[NUMBER_OF_FACTORIES];
        try (BufferedReader in = new BufferedReader(new FileReader(datFact))) {
            String line;
            Integer counterOfLines = 1;
            int br = 0;

            String id = "id";
            String name = "name";
            String IdAdd;
            Integer intId = 0;
            String ids;


            while ((line = in.readLine()) != null) {
                if (counterOfLines % NUMBER_OF_LINES_FACTORIES == 1) {
                    id = line;
                    //System.out.println("id: " + id);
                } else if (counterOfLines % NUMBER_OF_LINES_FACTORIES == 2) {
                    name = line;
                    //System.out.println("name: " + name);
                } else if (counterOfLines % NUMBER_OF_LINES_FACTORIES == 3) {
                    IdAdd = line;
                    intId = Integer.parseInt(IdAdd);
                    //System.out.println("intId: " + intId);

                } else if (counterOfLines % NUMBER_OF_LINES_FACTORIES == 0) {
                    ids = line;
                    String[] numbers = ids.split(",");
                    Set<Item> newSet = new HashSet<>();

                    for (int i = 0; i < numbers.length; i++) {
                        Integer pom = Integer.parseInt(numbers[i]);
                        newSet.add(items.get(pom - 1));

                    }
                    Factory factory = new Factory(name, Integer.parseInt(id), addresses[intId - 1], newSet);
                    factories[br] = factory;
                    br++;
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return factories;
    }
    public static Store[] makeObjectFromFileStores(Address[] addresses, List<Item> items, String datStor) {
        Store[] stores = new Store[NUMBER_OF_STORES];
        try (BufferedReader in = new BufferedReader(new FileReader(datStor))) {
            String line;
            Integer counterOfLines = 1;
            int br = 0;

            String id = "id";
            String name = "name";
            String IdAdd;
            Integer intId = 0;
            String ids;

            while ((line = in.readLine()) != null) {
                if (counterOfLines % NUMBER_OF_LINES_STORES == 1) {
                    id = line;
                    //System.out.println("id: " + id);
                } else if (counterOfLines % NUMBER_OF_LINES_STORES == 2) {
                    name = line;
                    //System.out.println("name: " + name);
                } else if (counterOfLines % NUMBER_OF_LINES_STORES == 3) {
                    IdAdd = line;
                    intId = Integer.parseInt(IdAdd);
                    //System.out.println("intId: " + intId);
                } else if (counterOfLines % NUMBER_OF_LINES_STORES == 0) {
                    ids = line;
                    String[] numbers = ids.split(",");
                    Set<Item> newSet = new HashSet<>();

                    for (int i = 0; i < numbers.length; i++) {
                        //System.out.println("brojevi: " + numbers[i]);
                        Integer pom = Integer.parseInt(numbers[i]);
                        newSet.add(items.get(pom - 1));

                    }
                    Store store = new Store(name, Integer.parseInt(id), addresses[intId - 1].getStreet(), newSet);
                    //System.out.println("br " + br);
                    stores[br] = store;
                    br++;
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return stores;
    }
   /* public static Address[] makeObjectFromFileAddresses(String datAdd) {
        Address[] addresses = new Address[NUMBER_OF_ADDRESSES];
        try (BufferedReader in = new BufferedReader(new FileReader(datAdd))) {
            String line;
            Integer counterOfLines = 1;

            String street = "street";
            String houseNumber = "houseNumber";
            String city = "city";
            String postalCode = "postalCode";
            Cities city1 = Cities.CITY_ZAGREB;
            Address address;

            int i = 0;

            while ((line = in.readLine()) != null) {
                if (counterOfLines % NUMBER_OF_LINES_ADDRESSES == 1) {
                    street = line;
                    //System.out.println("street: " + street);
                } else if (counterOfLines % NUMBER_OF_LINES_ADDRESSES == 2) {
                    houseNumber = line;
                    //System.out.println("housenumber: " + houseNumber);
                } else if (counterOfLines % NUMBER_OF_LINES_ADDRESSES == 3) {
                    city = line;
                    for (Cities cities : Cities.values()) {
                        if (city.compareTo(cities.getCityName()) == 0) {
                            city1 = cities;
                            //System.out.println("city1: " + city1.getCityName());
                            break;
                        }
                    }
                } else if (counterOfLines % NUMBER_OF_LINES_ADDRESSES == 0) {
                    postalCode = line;
                    address = new BuilderPattern().street(street).house(houseNumber).city(city1).code(city1).build();
                    //pom = Optional.of(categories[i]);

                    addresses[i] = address;
                    i++;
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return addresses;
    }*/


    @FXML
    private TextField itemNameTextField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TableView<Item> itemTableView;

    @FXML
    private TableColumn<Item, String> nameTableColumn;

    @FXML
    private TableColumn<Item, String> idTableColumn;

    @FXML
    private TableColumn<Item, String> categoryTableColumn;

    @FXML
    private TableColumn<Item, String> widthTableColumn;

    @FXML
    private TableColumn<Item, String> heightTableColumn;

    @FXML
    private TableColumn<Item, String> lengthTableColumn;

    @FXML
    private TableColumn<Item, String> productionCostTableColumn;

    @FXML
    private TableColumn<Item, String> sellingPriceTableColumn;


    //public static Category[] categories = new Category[NUMBER_OF_CATEGORIES];
    //public static List<Item> items = new ArrayList<>();
    //public static List<Category> categories = new ArrayList<>();
    private Timeline clock;

    @FXML
    public void initialize() throws SQLException, IOException, InterruptedException {

        /*items.clear();
        try {
            FilesUtils.getDeserialization(items, "dat/itemsSerialized.dat");
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        categories.clear();
        try{
            FilesUtils.getDeserialization(categories, "dat/categoriesSerialized.dat");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }*/


        /*List<String> options1 = new ArrayList<>();
        for(Category category : HelloApplication.categoryList){
            options1.add(category.getName());
        }

        ObservableList<String> options = FXCollections.observableList(options1);
        categoryComboBox.setItems(options);*/


        /*nameTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        idTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getId().toString());
        });


        *//*categoryTableColumn.setCellValueFactory(cellData -> {
            String temp = null;
            for(Category category : HelloApplication.categoryList){
                if(category.getId().compareTo(cellData.getValue().getCategoryId()) == 0){
                    temp = category.getName();
                }
            }
            return new SimpleStringProperty(temp);
        });*//*

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


        itemTableView.setItems(FXCollections.observableList(HelloApplication.itemList));*/



        /*SearchItemInitializeCategories searchItemInitializeCategories = new SearchItemInitializeCategories(categoryComboBox, categoryTableColumn);

        ItemControllerThread itemTable = new ItemControllerThread(
                categoryComboBox ,itemTableView,
                nameTableColumn, idTableColumn, categoryTableColumn,
                widthTableColumn, heightTableColumn, lengthTableColumn,
                productionCostTableColumn, sellingPriceTableColumn, "items");

        Thread thread1 = new Thread(searchItemInitializeCategories);
        thread1.start();

        Thread thread2 = new Thread(searchItemInitialize);
        thread2.start();*/


        ItemControllerThread firstThread = new ItemControllerThread(
                categoryComboBox ,itemTableView,
                nameTableColumn, idTableColumn, categoryTableColumn,
                widthTableColumn, heightTableColumn, lengthTableColumn,
                productionCostTableColumn, sellingPriceTableColumn, "items", Database.token);

        ItemControllerThread secondThread = new ItemControllerThread(
                categoryComboBox ,itemTableView,
                nameTableColumn, idTableColumn, categoryTableColumn,
                widthTableColumn, heightTableColumn, lengthTableColumn,
                productionCostTableColumn, sellingPriceTableColumn, "categories", Database.token);

        Thread thread1 = new Thread(firstThread, "firstThread");
        Thread thread2 = new Thread(secondThread, "secondThread");

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_2);

        executorService.execute(thread1);
        executorService.execute(thread2);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);


        Platform.runLater(new SortingItemsThread(HelloApplication.itemList));

        SortingItemsThread sortingItemsThread = new SortingItemsThread(HelloApplication.itemList);
        Thread thread3 = new Thread(sortingItemsThread);
        thread3.start();

        /*FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("itemSearch.fxml"));
        Scene scene = null;*/
        clock = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                HelloApplication.getStage().setTitle("Item with the highest price: " + HelloApplication.itemList.get(0).getName());

            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    @FXML
    protected void onSearchButtonClick() throws InterruptedException {

        clock.stop();

        ExecutorService executorService = Executors.newFixedThreadPool(Database.NUMBER_OF_THREADS_1);

        ItemControllerThread thirdThread = new ItemControllerThread(itemNameTextField, categoryComboBox, itemTableView, "SearchButton", Database.token);

        Thread thread3 = new Thread(thirdThread, "thirdThread");

        executorService.execute(thread3);

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

        /*Thread thread = new Thread(searchItemButtonClickThread);
        thread.start();*/

    }
}
