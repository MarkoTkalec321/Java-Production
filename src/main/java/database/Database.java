package database;

import com.example.tkalec7.HelloApplication;
import hr.java.production.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Database {

    public static Boolean activeConnectionWithDatabase = false;

    public static String token = "token";

    public static final int NUMBER_OF_THREADS_2 = 2;
    public static final int NUMBER_OF_THREADS_1 = 1;

    public synchronized static Connection connectToDatabase() throws SQLException, IOException {

        Properties configuration = new Properties();
        configuration.load(new FileReader("properties/database.properties"));
        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        Connection connection = DriverManager.getConnection
                (databaseURL, databaseUsername, databasePassword);

        return connection;
    }

    public static void disconnectFromDatabase(Connection connection) throws SQLException {
        connection.close();
    }

    public static List<Client> getAllClients() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Client> clients = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet clientResultSet = sqlStatement.executeQuery("SELECT * FROM CLIENT;");

        while(clientResultSet.next()) {

            Client newClient = getClientFromResultSet(clientResultSet);
            clients.add(newClient);
        }

        Database.disconnectFromDatabase(connection);

        return clients;
    }

    public static List<Category> getAllCategories() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Category> categories = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet categoryResultSet = sqlStatement.executeQuery("SELECT * FROM CATEGORY;");

        while(categoryResultSet.next()) {

            Category newCategory = getCategoryFromResultSet(categoryResultSet);
            categories.add(newCategory);
        }

        Database.disconnectFromDatabase(connection);

        return categories;

    }

    public static List<Item> getAllItems() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Item> items = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet itemResultSet = sqlStatement.executeQuery("SELECT * FROM ITEM;");

        while(itemResultSet.next()) {

            Item newItem = getItemFromResultSet(itemResultSet);
            items.add(newItem);
        }

        Database.disconnectFromDatabase(connection);

        return items;
    }

    public static List<Address> getAllAddresses() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Address> addresses = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet addressResultSet = sqlStatement.executeQuery("SELECT * FROM ADDRESS;");

        while(addressResultSet.next()) {

            Address newAddress = getAddressFromResultSet(addressResultSet);
            addresses.add(newAddress);
        }

        Database.disconnectFromDatabase(connection);

        return addresses;
    }

    public static List<Factory> getAllFactories() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Factory> factories = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet factoryResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY;");

        while(factoryResultSet.next()){

            Factory newFactory = getFactoriesFromResultSet(factoryResultSet);
            factories.add(newFactory);
        }

        Database.disconnectFromDatabase(connection);

        return factories;
    }

    public static List<Store> getAllStores() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Store> stores = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet storeResultSet = sqlStatement.executeQuery("SELECT * FROM STORE;");

        while(storeResultSet.next()) {

            Store newStore = getStoreFromResultSet(storeResultSet);
            stores.add(newStore);
        }

        Database.disconnectFromDatabase(connection);

        return stores;
    }

    public static List<Item> getItemsFromFactory() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Item> items = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet itemResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY_ITEM FI, ITEM I WHERE FI.FACTORY_ID = 3 AND FI.ITEM_ID = I.ID;");

        while(itemResultSet.next()) {
            Item newItem = getItemFromResultSet(itemResultSet);
            items.add(newItem);
        }

        Database.disconnectFromDatabase(connection);

        return items;
    }

    public static List<Item> getItemsFromStore() throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        List<Item> items = new ArrayList<>();

        Statement sqlStatement = connection.createStatement();

        ResultSet itemResultSet = sqlStatement.executeQuery("SELECT * FROM STORE_ITEM SI, ITEM I WHERE SI.STORE_ID = 3 AND SI.ITEM_ID = I.ID;");

        while(itemResultSet.next()) {
            Item newItem = getItemFromResultSet(itemResultSet);
            items.add(newItem);
        }

        Database.disconnectFromDatabase(connection);

        return items;
    }

    public static void getSingleCategory(List<Category> categoryList) throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        PreparedStatement singleItem = connection.prepareStatement("SELECT * FROM CATEGORY WHERE ID = ?;");

        //System.out.println("id " + categoryList.get(0).getId().toString());
        singleItem.setString(1, categoryList.get(0).getId().toString());

        ResultSet resultSet = singleItem.executeQuery();

        resultSet.next();

        String item = resultSet.getString("NAME");
        //System.out.println(item);

        Database.disconnectFromDatabase(connection);

    }

    public static void getSingleItem(List<Item> itemList) throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        PreparedStatement singleItem = connection.prepareStatement("SELECT * FROM ITEM WHERE ID = ?;");

        //System.out.println("id " + itemList.get(0).getId().toString());
        singleItem.setString(1, itemList.get(0).getId().toString());

        ResultSet resultSet = singleItem.executeQuery();

        resultSet.next();

        String item = resultSet.getString("NAME");
        //System.out.println(item);

        Database.disconnectFromDatabase(connection);

    }

    public static void getSingleFactory(List<Factory> factoryList) throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        PreparedStatement singleItem = connection.prepareStatement("SELECT * FROM FACTORY WHERE ID = ?;");

        //System.out.println("id " + factoryList.get(0).getId().toString());
        singleItem.setString(1, factoryList.get(0).getId().toString());

        ResultSet resultSet = singleItem.executeQuery();

        resultSet.next();

        String item = resultSet.getString("NAME");
        //System.out.println(item);

        Database.disconnectFromDatabase(connection);
    }

    public static void getSingleStore(List<Store> storeList) throws SQLException, IOException {

        Connection connection = Database.connectToDatabase();

        PreparedStatement singleItem = connection.prepareStatement("SELECT * FROM STORE WHERE ID = ?;");

        //System.out.println("id " + storeList.get(0).getId().toString());
        singleItem.setString(1, storeList.get(0).getId().toString());

        ResultSet resultSet = singleItem.executeQuery();

        resultSet.next();

        String item = resultSet.getString("NAME");
        //System.out.println(item);

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewCategory(String enteredCategoryName, String enteredCategoryDescription) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES(?, ?)");

        stmt.setString(1, enteredCategoryName);
        stmt.setString(2, enteredCategoryDescription);

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewItem(long id, long categoryId, String enteredName, String width, String height, String length,
                                     String productionCost, String sellingPrice) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO ITEM (CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST," +
                "SELLING_PRICE) VALUES(?, ?, ?, ?, ?, ?, ?);");

        stmt.setString(1, String.valueOf(categoryId));
        stmt.setString(2, enteredName);
        stmt.setString(3, width);
        stmt.setString(4, height);
        stmt.setString(5, length);
        stmt.setString(6, productionCost);
        stmt.setString(7, sellingPrice);

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewAddress(Address address) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO ADDRESS (STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES(?, ?, ?, ?);");

        stmt.setString(1, address.getStreet());
        stmt.setString(2, address.getHouseNumber());
        stmt.setString(3, address.getCity());
        stmt.setString(4, address.getPostalCode());

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewFactory(String name, Address id) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO FACTORY (NAME, ADDRESS_ID) VALUES(?, ?);");

        System.out.println(name + " " + id.getId());
        stmt.setString(1, name);
        stmt.setString(2, id.getId().toString());

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewStore(String name, String address) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO STORE (NAME, WEB_ADDRESS) VALUES(?, ?);");

        stmt.setString(1, name);
        stmt.setString(2, address);

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewFactoryItem(Factory factory, Item item) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO FACTORY_ITEM (FACTORY_ID, ITEM_ID) VALUES(?, ?);");

        //System.out.println("id :" + factoryId + " " + itemId);
        //stmt.setString(1, String.valueOf(factoryId));
        //stmt.setString(2, String.valueOf(itemId));

        stmt.setString(1, factory.getId().toString());
        stmt.setString(2, item.getId().toString());

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static int checkFactoryItemDuplicates(Factory factory, Item item) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("SELECT FACTORY_ID, ITEM_ID FROM FACTORY_ITEM");

        ResultSet resultSet = stmt.executeQuery();

        int flag = 0;
        while(resultSet.next()) {

            String factoryId =  resultSet.getString("FACTORY_ID");
            String itemId =  resultSet.getString("ITEM_ID");
            if(factory.getId().toString().compareTo(factoryId) == 0 && item.getId().toString().compareTo(itemId) == 0) {
                System.out.println("Duplicate found!");
                flag = 2;
                break;
            }
        }

        return flag;
    }

    public static void insertNewStoreItem(Store store, Item item) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO STORE_ITEM (STORE_ID, ITEM_ID) VALUES(?, ?);");

        stmt.setString(1, store.getId().toString());
        stmt.setString(2, item.getId().toString());

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void insertNewClient(String firstName, String lastName, String dateOfBirth, long addressId) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO CLIENT (FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, ADDRESS_ID) VALUES(?, ?, ?, ?);");

        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, dateOfBirth);
        stmt.setString(4, String.valueOf(addressId));

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static void editExistingClient(String firstName, String lastName, String dateOfBirth, long addressId, long clientId) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("UPDATE CLIENT SET FIRST_NAME = ?, LAST_NAME = ?, DATE_OF_BIRTH = ?, ADDRESS_ID = ? WHERE ID = " + clientId);

        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, dateOfBirth);
        stmt.setString(4, String.valueOf(addressId));

        stmt.executeUpdate();

        Database.disconnectFromDatabase(connection);
    }

    public static int checkStoreItemDuplicates(Store store, Item item) throws SQLException, IOException {
        Connection connection = Database.connectToDatabase();

        PreparedStatement stmt = connection.prepareStatement("SELECT STORE_ID, ITEM_ID FROM STORE_ITEM");

        ResultSet resultSet = stmt.executeQuery();

        int flag = 0;
        while(resultSet.next()) {

            String factoryId =  resultSet.getString("STORE_ID");
            String itemId =  resultSet.getString("ITEM_ID");
            if(store.getId().toString().compareTo(factoryId) == 0 && item.getId().toString().compareTo(itemId) == 0) {
                System.out.println("Duplicate found!");
                flag = 2;
                break;
            }
        }

        return flag;
    }

    private static Client getClientFromResultSet(ResultSet clientResultSet) throws SQLException {

        long clientId = clientResultSet.getLong("ID");
        String clientFirstName = clientResultSet.getString("FIRST_NAME");
        String clientLastName = clientResultSet.getString("LAST_NAME");
        String clientDateOfBirth = clientResultSet.getString("DATE_OF_BIRTH");
        Long clientAddressId = clientResultSet.getLong("ADDRESS_ID");
        //Date date = clientResultSet.getDate("aaa");
        Address clientAddress = null;

        for(Address address : HelloApplication.addressList) {
            if(address.getId().compareTo(clientAddressId) == 0) {
                clientAddress = address;
                System.out.println(clientAddress.getStreet());
            }
        }

        return new Client(clientFirstName, clientId, clientLastName, clientDateOfBirth, clientAddress);
    }

    private static Category getCategoryFromResultSet(ResultSet categoryResultSet) throws SQLException {

        long categoryId = categoryResultSet.getLong("ID");
        String categoryName = categoryResultSet.getString("NAME");
        String categoryDescription = categoryResultSet.getString("DESCRIPTION");
        //Category newCategory = new Category(categoryName, categoryId, categoryDescription);

        return new Category(categoryName, categoryId, categoryDescription);
    }

    private static Item getItemFromResultSet(ResultSet itemResultSet) throws SQLException {

        long itemId = itemResultSet.getLong("ID");
        long categoryId = itemResultSet.getLong("CATEGORY_ID");
        String itemName = itemResultSet.getString("NAME");
        BigDecimal itemWidth = itemResultSet.getBigDecimal("WIDTH");
        BigDecimal itemHeight = itemResultSet.getBigDecimal("HEIGHT");
        BigDecimal itemLength = itemResultSet.getBigDecimal("LENGTH");
        BigDecimal itemProductionCost = itemResultSet.getBigDecimal("PRODUCTION_COST");
        BigDecimal itemSellingPrice = itemResultSet.getBigDecimal("SELLING_PRICE");

        return new Item(itemId, categoryId, itemName, itemWidth, itemHeight, itemLength,
                itemProductionCost, itemSellingPrice);

    }

    private static Address getAddressFromResultSet(ResultSet addressResultSet) throws SQLException {

        long addressId = addressResultSet.getLong("ID");
        String addressStreet = addressResultSet.getString("STREET");
        String addressHouseNumber = addressResultSet.getString("HOUSE_NUMBER");
        String addressCity = addressResultSet.getString("CITY");
        String addressPostalCode = addressResultSet.getString("POSTAL_CODE");

       /* Optional<Cities> temp = Optional.empty();
        for(Cities city : Cities.values()){
            if(city.getCityName().toLowerCase().compareTo(addressCity.toLowerCase()) == 0){
                temp = Optional.of(city);
                //System.out.println("ime: " + city.getCityName());
            }
        }*/

        Address address = new BuilderPattern().id(addressId).street(addressStreet).house(addressHouseNumber).city(addressCity).code(addressPostalCode).build();



        return address;
    }

    private static Factory getFactoriesFromResultSet(ResultSet factoryResultSet) throws SQLException {

        long factoryId = factoryResultSet.getLong("ID");
        String factoryName = factoryResultSet.getString("NAME");
        long addressId = factoryResultSet.getLong("ADDRESS_ID");

        return new Factory(factoryId, factoryName, addressId);
    }

    private static Store getStoreFromResultSet(ResultSet storeResultSet) throws SQLException {

        long storeId = storeResultSet.getLong("ID");
        String storeName = storeResultSet.getString("NAME");
        String storeWebAddress = storeResultSet.getString("WEB_ADDRESS");

        return new Store(storeId, storeName, storeWebAddress);
    }

}
