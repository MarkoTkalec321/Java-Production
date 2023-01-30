package hr.java.production.main;

import hr.java.production.enums.Cities;
import hr.java.production.exception.Anomaly;
import hr.java.production.exception.Uncommon;
import hr.java.production.genericsi.FoodStore;
import hr.java.production.genericsi.TechnicalStore;
import hr.java.production.model.*;
import hr.java.production.sort.ProductionSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Runs the application and writes down information into a log file.
 */

public class Main {

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


    /**
     * Makes a Category type of array.
     *
     * @param input Scanner object for category inputs.
     *              //@param categoryName Name of the category.
     *              //@param i Counter of the loop.
     * @return Returns a Category type array.
     */
    public static Category makeCategories(Scanner input, String[] categoryNames, int i) throws Uncommon {


        Category categories;
        boolean loop = false;
        String categoryName = "ImeKategorije";

        do {
            System.out.println("Unesite ime " + (i + 1) + ". kategorije: ");
            categoryName = input.nextLine();
            logger.info("Name of category: " + categoryName);


            for (int j = 0; j < NUMBER_OF_CATEGORIES; j++) {
                Optional<String> checked = Optional.ofNullable(categoryNames[j]);
                if (checked.isPresent()) {
                    if (categoryNames[j].compareTo(categoryName) == 0) {
                        throw new Uncommon("Ime kategorije već postoji, unesite drugo ime za kategoriju!");
                    }
                }
            }

            categoryNames[i] = categoryName;
            loop = false;
        }
        while (loop);


        System.out.println("Unesite opis " + (i + 1) + ". kategorije: ");
        String categoryDescription = input.nextLine();


        categories = new Category(categoryName, i, categoryDescription);


        return categories;
    }

    /**
     * Makes an Item type of array.
     *
     * @param input      Scanner object for item inputs.
     * @param categories Category type array.
     * @return Returns an Item type array.
     */
    public static List<Item> makeItems(Scanner input, Category[] categories) {

        System.out.println("Unesite " + NUMBER_OF_ITEMS + " artikala: ");

        //Item[] items = new Item[NUMBER_OF_ITEMS];
        List<Item> items = new ArrayList<>(NUMBER_OF_ITEMS);

        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {

            BigDecimal discountAmount = new BigDecimal("-1");
            BigDecimal hundred = new BigDecimal("100");
            boolean loop = false;

            do {
                loop = false;
                try {
                    System.out.println("Unesite iznos popusta u postocima: ");
                    discountAmount = input.nextBigDecimal();
                    logger.info("Percentage number: " + discountAmount);

                    if (discountAmount.compareTo(hundred) > 0 || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
                        System.out.println("Broj mora biti u rasponu od 0 do 100!");
                        logger.error("Number is not between 0 and 100!");
                        input.nextLine();
                        loop = true;
                    }

                } catch (InputMismatchException ex1) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", ex1);
                    input.nextLine();
                    loop = true;
                }


            }
            while (loop);

            Discount discount = new Discount(discountAmount);
            Integer article = -1;

            do {
                try {
                    loop = false;
                    System.out.println("Unesite broj (1) ako je artikl hrana, (2) za laptop ili (3) za ostale artikle: ");
                    article = input.nextInt();
                    logger.info("Article choice: " + article);

                    if (article < 1 || article > 3) {
                        System.out.println("Broj mora biti 1, 2 ili 3!");
                        logger.error("Number is not 1, 2 or 3!");
                        input.nextLine();
                        loop = true;
                    }
                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }

            } while (loop);

            input.nextLine();

            System.out.println("Unesite ime " + (i + 1) + ". artikla: ");
            String itemName = input.nextLine();
            logger.info("Article name: " + itemName);

            Integer chosenCategoryIndex = -1;

            do {
                loop = false;
                try {
                    System.out.println("Unesite broj kategorije " + (i + 1) + ". artikla: ");
                    chosenCategoryIndex = input.nextInt();
                    logger.info("Number of article category." + chosenCategoryIndex);

                    if (chosenCategoryIndex < 1 || chosenCategoryIndex > NUMBER_OF_CATEGORIES) {
                        System.out.println("Neispravan unos, molimo pokušajte ponovno!");
                        logger.error("Input is not range!");
                        input.nextLine();
                        loop = true;
                    }

                    System.out.print("Odabir >> " + (chosenCategoryIndex));
                    System.out.println();

                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }
            }
            while (loop);

            Category category = categories[chosenCategoryIndex - 1];

            BigDecimal itemWidth = new BigDecimal("-1");
            BigDecimal itemHeight = new BigDecimal("-1");
            BigDecimal itemLength = new BigDecimal("-1");
            BigDecimal itemProductionCost = new BigDecimal("-1");
            BigDecimal itemSellingPrice = new BigDecimal("-1");


            do {
                loop = false;
                try {
                    System.out.println("Unesite širinu " + (i + 1) + ". artikla: ");
                    itemWidth = input.nextBigDecimal();
                    logger.info("Item width: ");

                    if (itemWidth.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Unesite pozitivan broj!");
                        logger.error("Input is not a positive!");
                        input.nextLine();
                        loop = true;
                    }

                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }
            }
            while (loop);


            do {
                loop = false;
                try {
                    System.out.println("Unesite visinu " + (i + 1) + ". artikla: ");
                    itemHeight = input.nextBigDecimal();
                    logger.info("Item height: ");

                    if (itemHeight.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Unesite pozitivan broj!");
                        logger.error("Input is not a positive!");
                        input.nextLine();
                        loop = true;
                    }
                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }
            }
            while (loop);

            do {
                loop = false;
                try {
                    System.out.println("Unesite duljinu " + (i + 1) + ". artikla: ");
                    itemLength = input.nextBigDecimal();
                    logger.info("Item length: ");

                    if (itemLength.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Unesite pozitivan broj!");
                        logger.error("Input is not a positive!");
                        input.nextLine();
                        loop = true;
                    }
                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }
            }
            while (loop);

            do {
                loop = false;
                try {
                    System.out.println("Unesite cijenu proizvodnje " + (i + 1) + ". artikla: ");
                    itemProductionCost = input.nextBigDecimal();
                    logger.info("Item production cost: ");

                    if (itemProductionCost.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Unesite pozitivan broj!");
                        logger.error("Input is not a positive!");
                        input.nextLine();
                        loop = true;
                    }
                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }
            }
            while (loop);

            do {
                loop = false;
                try {
                    System.out.println("Unesite prodajnu cijenu " + (i + 1) + ". artikla: ");
                    itemSellingPrice = input.nextBigDecimal();
                    logger.info("Item selling price: ");

                    if (itemSellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Unesite pozitivan broj!");
                        logger.error("Input is not a positive!");
                        input.nextLine();
                        loop = true;
                    }
                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;
                }
            }
            while (loop);

            Integer choice = -1;

            if (article == 1) {
                do {
                    loop = false;
                    try {
                        System.out.println("Unesite broj (1) ako želite namirnicu edible item " +
                                "ili (2) ako želite namirnicu edible item 1");
                        choice = input.nextInt();
                        logger.info("Item choice: ");

                        if (choice < 1 || choice > 2) {
                            System.out.println("Broj mora biti 1 ili 2!");
                            logger.error("Input is not in range!");
                            input.nextLine();
                            loop = true;
                        }
                    /*if(article == 1 && choice == 3){
                        System.out.println("Morate izabrati 1 ili 2!");
                        logger.error("Input is not 1 or 2!");
                        input.nextLine();
                        loop = true;
                    }*/
                    /*if((article == 2 && choice == 1) || (article == 2 && choice == 2)){
                        System.out.println("Morate izabrati 3!");
                        logger.error("Input is not 3!");
                        input.nextLine();
                        loop = true;
                    }*/
                    } catch (InputMismatchException inp) {
                        System.out.println("Morate unijeti brojčane vrijednosti!");
                        logger.error("Input is not a number!", inp);
                        input.nextLine();
                        loop = true;
                    }
                }
                while (loop);
            }


            if (choice == 1) {

                EdibleItem edibleItem = new EdibleItem(itemName, i, category, itemWidth, itemHeight,
                        itemLength, itemProductionCost, itemSellingPrice, discount);

                Integer kk = edibleItem.calculateKilocalories();
                BigDecimal price = edibleItem.calculatePrice();


                edibleItem.setSellingPrice(price);

                System.out.println("Broj kilokalorija: " + kk);
                System.out.println("Cijena namirnice po kilogramu je: " + price);

                items.add(i, edibleItem);
                items.get(i).setSellingPrice(price);
            } else if (choice == 2) {

                EdibleItem1 edibleItem1 = new EdibleItem1(itemName, i, category, itemWidth, itemHeight,
                        itemLength, itemProductionCost, itemSellingPrice, discount);

                Integer kk = edibleItem1.calculateKilocalories();
                BigDecimal price = edibleItem1.calculatePrice();

                edibleItem1.setSellingPrice(price);

                System.out.println("Broj kilokalorija: " + kk);
                System.out.println("Cijena namirnice po kilogramu je: " + price);

                //items[i] = edibleItem1;
                //items[i].setSellingPrice(price);

                items.add(i, edibleItem1);
                items.get(i).setSellingPrice(price);

            }

            if (article == 2) {

                Laptop laptop = new Laptop(itemName, i, category, itemWidth, itemHeight, itemLength, itemProductionCost,
                        itemSellingPrice, discount);

                System.out.println("Unesite trajanje garantnog roka u mjesecima: ");
                Integer war = input.nextInt();
                logger.info("Warranty: ");
                laptop.setWarranty(war);

                System.out.println("garancija: " + war);
                items.add(i, laptop);

            } else if (article == 3) {
                items.add(i, new Item(itemName, i, category, itemWidth, itemHeight, itemLength, itemProductionCost,
                        itemSellingPrice, discount));
            }

            input.nextLine();

        }

        return items;
    }

    /**
     * Inputs how many items a factory has and checks if those values are in range.
     *
     * @param input Scanner type input.
     * @param test  Int type of array.
     * @param i     Counter from a loop.
     * @return Int Array as a return.
     */
    public static int[] inputFactoryArticlesNumber(Scanner input, int[] test, int i) {

        boolean loop = false;
        Integer chosenFactoryArticlesNumber = -10;

        do {
            loop = false;
            try {
                System.out.println("Unesite broj koliko artikala ima " + (i + 1) + ". tvornica: ");
                chosenFactoryArticlesNumber = input.nextInt();
                logger.info("Number of items");

                if (chosenFactoryArticlesNumber > NUMBER_OF_ITEMS || chosenFactoryArticlesNumber < 1) {
                    System.out.println("Broj mora biti pozitivan i manji od " + (NUMBER_OF_ITEMS + 1));
                    logger.error("Input is not a positive!");
                    input.nextLine();
                    loop = true;
                }
                test[i] = chosenFactoryArticlesNumber;
                System.out.println("Odabir>>>" + test[i]);

            } catch (InputMismatchException inp) {
                System.out.println("Morate unijeti brojčane vrijednosti!");
                logger.error("Input is not a number!", inp);
                input.nextLine();
                loop = true;
            }
        }
        while (loop);

        return test;
    }

    /**
     * Makes a Factory type of array.
     *
     * @param input                        Scanner object for factory inputs.
     * @param items                        Item type array.
     * @param chosenFactoryArticlesNumbers Int array that holds chosen numbers.
     * @param factoryName                  Name of the factory.
     * @param i                            Counter of outer loop.
     * @param j                            Counter of inner loop.
     * @param items1                       Other Item type array.
     *                                     //@param factories Factory type of array.
     *                                     //@param num Input of the chosen item.
     * @param loop                         Saves the boolean value.
     * @param chosenItemsArray             Array that stores numbers of chosen items.
     * @return Returns a Factory type array.
     */
    public static Factory makeFactories(Scanner input, List<Item> items, int[] chosenFactoryArticlesNumbers, String factoryName, int i, int j,
                                        List<Item> items1, Boolean loop, Integer[] chosenItemsArray, Address address)
            throws Anomaly, RuntimeException {

        //Address address = new BuilderPattern().street("street").house("house").city("city").code("code").build();
        Factory factories;
        int num = -1;
        do {

            if (j == chosenFactoryArticlesNumbers[i] - 1) {
                loop = false;
            }
            System.out.println();
            for (int k = 0; k < NUMBER_OF_ITEMS; k++) {
                System.out.print(items.get(k).getName() + " ");
            }


            System.out.println();
            System.out.println("Unesite broj željenog artikla " + (i + 1) + ". tvornica: ");
            num = input.nextInt();
            logger.info("Number of chosen item");
            System.out.println(">>>Uneseni broj je: " + num);

            if (num > NUMBER_OF_ITEMS || num < 1) {
                System.out.println("Broj mora biti pozitivan i manji od " + (NUMBER_OF_ITEMS + 1));
                logger.error("Input is not a positive!");
                input.nextLine();
                loop = true;
            }

            for (int k = 0; k < chosenFactoryArticlesNumbers[i]; k++) {
                Optional<Integer> checked = Optional.ofNullable(chosenItemsArray[k]);
                if (checked.isPresent()) {
                    if (chosenItemsArray[k] == num - 1) {
                        loop = true;
                        throw new Anomaly("Artikl je već odabran, unesite jedan od ostalih artikala!");
                    }
                }
            }

            chosenItemsArray[j] = num - 1;

        }
        while (loop);

        items1.add(j, items.get(num - 1));
        Set<Item> pom = new HashSet<>(items1);
        factories = new Factory(factoryName, i, address, pom);

        input.nextLine();

        return factories;
    }


    /**
     * Makes a Factory type of array.
     *
     * @param input                     Scanner object for factory inputs.
     * @param items                     Item type array.
     * @param chosenStoreArticleNumbers Int array that holds chosen numbers of stores.
     * @param storeName                 Name of the store.
     * @param i                         Counter of outer loop.
     * @param j                         Counter of inner loop.
     * @param items1                    Other Item type array.
     *                                  //@param stores Store type of array.
     * @param loop                      Saves the boolean value.
     * @param chosenItemsArray          Array that stores numbers of chosen items.
     * @param webAddress                String type that contains web address of the store.
     * @return Returns a Factory type array.
     */
    public static Store makeStores(Scanner input, List<Item> items, int[] chosenStoreArticleNumbers, String storeName, int i, int j,
                                   List<Item> items1, Boolean loop, Integer[] chosenItemsArray, String webAddress)
            throws Anomaly, RuntimeException {

        Store stores;
        int num = -1;
        do {

            if (j == chosenStoreArticleNumbers[i] - 1) {
                loop = false;
            }

            System.out.println();
            for (int k = 0; k < NUMBER_OF_ITEMS; k++) {
                System.out.println(items.get(k).getName() + " ");
            }

            System.out.println();
            System.out.println("Unesite broj željenog artikla " + (i + 1) + ". dućana: ");
            num = input.nextInt();
            logger.info("Number of chosen item");
            System.out.println(">>>Uneseni broj je: " + num);

            if (num > NUMBER_OF_ITEMS || num < 1) {
                System.out.println("Broj mora biti pozitivan i manji od " + (NUMBER_OF_ITEMS + 1));
                logger.error("Input is not a positive!");
                input.nextLine();
                loop = true;
            }

            for (int k = 0; k < chosenStoreArticleNumbers[i]; k++) {
                Optional<Integer> checked = Optional.ofNullable(chosenItemsArray[k]);
                if (checked.isPresent()) {
                    if (chosenItemsArray[k] == num - 1) {
                        loop = true;
                        throw new Anomaly("Artikl je već odabran, unesite jedan od ostalih artikala!");
                    }
                }
            }

            chosenItemsArray[j] = num - 1;


        }
        while (loop);
        items1.add(j, items.get(num - 1));
        Set<Item> pom = new HashSet<>(items1);

        stores = new Store(storeName, i, webAddress, pom);

        //input.nextLine();

        return stores;
    }

    /**
     * Inputs how many items a store has and checks if those values are in range.
     *
     * @param input Scanner type input.
     *              //@param chosenStoreArticlesNumber Integer type of variable.
     * @param test1 Int type of array.
     * @param i     Counter from a loop.
     * @return Int array as a return.
     */
    public static int[] inputStoreArticlesNumber(Scanner input, int[] test1, int i) {

        boolean loop = false;
        Integer chosenStoreArticlesNumber = -10;

        do {
            loop = false;
            try {
                System.out.println("Unesite broj koliko artikala ima " + (i + 1) + ". dućan: ");
                chosenStoreArticlesNumber = input.nextInt();
                logger.info("Number of items");

                if (chosenStoreArticlesNumber > NUMBER_OF_ITEMS || chosenStoreArticlesNumber < 1) {
                    System.out.println("Broj mora biti pozitivan i manji od " + (NUMBER_OF_ITEMS + 1));
                    logger.error("Input is not a positive!");
                    input.nextLine();
                    loop = true;
                }
                test1[i] = chosenStoreArticlesNumber;

            } catch (InputMismatchException inp) {
                System.out.println("Morate unijeti brojčane vrijednosti!");
                logger.error("Input is not a number!", inp);
                input.nextLine();
                loop = true;
            }
        }
        while (loop);

        return test1;
    }

    public static FoodStore addFoodStoreItems(Scanner input, List<Item> items) {

        System.out.println("Unesite ime za dućan sa hranom: ");
        String name = input.nextLine();

        System.out.println("Unesite web adresu za dućan sa hranom: ");
        String webAddress = input.nextLine();


        Set<Item> edibleItemList = new HashSet<>();
        long id = -1; //potencijalna greska
        for (Item item : items) {
            if (item instanceof Edible) {
                edibleItemList.add((item));
                id = item.getId();
            }
        }

        return new FoodStore(name, id, webAddress, edibleItemList);
    }

    public static TechnicalStore addTechnicalStoreItems(Scanner input, List<Item> items) {

        System.out.println("Unesite ime za dućan sa tehnikom: ");
        String name = input.nextLine();

        System.out.println("Unesite web adresu za dućan sa tehnikom: ");
        String webAddress = input.nextLine();

        Set<Item> technicalItemList = new HashSet<>();
        long id = -1;
        for (Item item : items) {
            if (item instanceof Technical) {
                technicalItemList.add(item);
                id = item.getId();
            }
        }

        return new TechnicalStore(name, id, webAddress, technicalItemList);
    }

    public static Category[] makeObjectFromFileCategories(Category[] categories, String datCat) {
        Category[] pom = new Category[NUMBER_OF_CATEGORIES];
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
                    //System.out.println("datCat: " + datCategory.getName() + " " + datCategory.getId() + " " + datCategory.getDescription());
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

    /*public static Address[] makeObjectFromFileAddresses(String datAdd) {
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

    public static FoodStore[] makeObjectFromFileFoodStore(Address[] addresses, List<Item> items, String dat) {
        FoodStore[] foodStores = new FoodStore[NUMBER_OF_FOODSTORES];
        try (BufferedReader in = new BufferedReader(new FileReader(dat))) {
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
                    FoodStore<Edible> foodStore = new FoodStore(name, Integer.parseInt(id), addresses[intId - 1].getStreet(), newSet);
                    //System.out.println("br " + br);
                    foodStores[br] = foodStore;
                    br++;
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return foodStores;
    }

    public static TechnicalStore[] makeObjectFromFileTechnicalStore(Address[] addresses, List<Item> items, String dat) {
        TechnicalStore[] technicalStores = new TechnicalStore[NUMBER_OF_TechStores];
        try (BufferedReader in = new BufferedReader(new FileReader(dat))) {
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
                    TechnicalStore<Technical> technicalStore = new TechnicalStore<>(name, Integer.parseInt(id), addresses[intId - 1].getStreet(), newSet);
                    //System.out.println("br " + br);
                    technicalStores[br] = technicalStore;
                    br++;
                }
                counterOfLines++;
            }

        } catch (IOException e) {
            System.err.println(e);
        }

        return technicalStores;
    }

    public static void sortItemsByVolumeLambda(List<Item> items, Store[] stores) {
        List<Item> allStoreItems = new ArrayList<>();

        for (Store store : stores) {
            allStoreItems.addAll(store.getItems());
        }

        List<Item> collectedStoreItems = allStoreItems.stream()
                .distinct()
                .sorted((s1, s2) -> {
                    if (s1.getLength().multiply(s1.getHeight().multiply(s1.getWidth()))
                            .compareTo(s2.getLength().multiply(s2.getHeight().multiply(s2.getWidth()))) > 0) {
                        return 1;
                    } else if (s1.getLength().multiply(s1.getHeight().multiply(s1.getWidth()))
                            .compareTo(s2.getLength().multiply(s2.getHeight().multiply(s2.getWidth()))) < 0) {
                        return -1;
                    } else {
                        return 0;
                    }

                })
                .collect(Collectors.toList());

        System.out.println("Ispis sortiranih itema po volumenu iz dućana: ");
        collectedStoreItems.forEach(h -> System.out.println(h.getName()));
    }

    public static void sortItemsByVolumeNoLambda(List<Item> items, Store[] stores) {
        List<Item> allStoreItems = new ArrayList<>();

        for (Store store : stores) {
            for (Item items1 : store.getItems()) {
                if (!(items1 instanceof Technical) && !(items1 instanceof Edible)) {
                    allStoreItems.addAll(Collections.singleton(items1));
                }
            }
        }
        List<Item> allStoreItems1 = new ArrayList<>(new HashSet<>(allStoreItems));
        List<Item> allStoreItems2 = new ArrayList<>();
        List<BigDecimal> dimensions = new ArrayList<>();

        for (int i = 0; i < allStoreItems1.size(); i++) {
            dimensions.add(allStoreItems1.get(i).getDimensions());
        }
        Collections.sort(dimensions);

        for (int j = 0; j < dimensions.size(); j++) {
            for (int i = 0; i < allStoreItems1.size(); i++) {
                if (dimensions.get(j).compareTo(allStoreItems1.get(i).getDimensions()) == 0) {
                    allStoreItems2.add(allStoreItems1.get(i));
                }
            }
        }

        //allStoreItems2.forEach(s -> System.out.println("sort bez lambdi: " + s.getName()));
    }

    public static void sortAvgItemPriceAboveAvgVolumeLambda(List<Item> items) {
        Double avgVolume = items.stream()
                .mapToDouble(item -> (item.getWidth().multiply(item.getLength().multiply(item.getHeight()))).doubleValue())
                .reduce(0, Double::sum);

        //System.out.println("avgVolume ispis: " + avgVolume);

        BigDecimal numOfItems = new BigDecimal(NUMBER_OF_ITEMS);
        BigDecimal avgVolume1 = BigDecimal.valueOf(avgVolume);

        avgVolume1 = avgVolume1.divide(numOfItems, 2, RoundingMode.CEILING);
        System.out.println("Prosječni volumen artikla: " + avgVolume1);


        BigDecimal finalSumItemVolume = avgVolume1;
        OptionalDouble avgPrice = items.stream()
                .filter(item -> (item.getWidth().multiply(item.getLength().multiply(item.getHeight()))).compareTo(finalSumItemVolume) > 0) //oni koji imaju nadprosjecni volumen
                .mapToDouble(item -> item.getSellingPrice().doubleValue()) //dobivam njihovu cijenu
                .average();


        if (avgPrice.isPresent()) {
            System.out.println("Prosječna cijena artikala koji imaju nadprosječan volumen: " + avgPrice.getAsDouble());
        }
    }

    public static void sortAvgItemPriceAboveAvgVolumeNoLambda(List<Item> items) {
        BigDecimal sum = new BigDecimal("0");
        for (Item item : items) {
            sum = sum.add(item.getDimensions());
        }

        System.out.println("sum bez lmb: " + sum);

        BigDecimal numOfItems = new BigDecimal(NUMBER_OF_ITEMS);
        BigDecimal avgVolume = sum.divide(numOfItems, 2, RoundingMode.CEILING);
        System.out.println("Prosječni volumen artikla bez lmb: " + avgVolume);

        List<Item> chosenItemList = new ArrayList<>();

        for (Item item : items) {
            if (item.getDimensions().compareTo(avgVolume) > 0) {
                chosenItemList.add(item);
            }
        }

        //chosenItemList.forEach(s -> System.out.println("chosen item list: " + s.getName()));

        BigDecimal sum_of_prices = new BigDecimal("0");
        BigDecimal numOfChosenItems = new BigDecimal(chosenItemList.size());
        BigDecimal zero = new BigDecimal("0");
        for (Item item : chosenItemList) {
            sum_of_prices = sum_of_prices.add(item.getSellingPrice());
        }
        if (avgVolume.compareTo(sum_of_prices) > 0 && numOfChosenItems.compareTo(zero) > 0) {
            //System.out.println("Num of chosen items: " + numOfChosenItems);
            System.out.println("Prosječna cijena artikala koji imaju natprosječni volumen bez lamdbe: " +
                    sum_of_prices.divide(numOfChosenItems, 2, RoundingMode.CEILING));
        } else {
            System.out.println("Error!");
        }
    }

    public static void sortStoresByAboveAvgItemsLambda(List<Store> allStores) {


        /*for(int i = 0; i < allStores.size(); i++)
            System.out.println("Svi ducani print: " + allStores.get(i).getName());*/

        Double itemsInStores = allStores.stream()
                .mapToDouble(item -> item.getItems().size())
                .reduce(0, Double::sum);

        //System.out.println("Items in stores: " + itemsInStores);

        BigDecimal numOfStores = new BigDecimal(allStores.size());
        BigDecimal avgItemInStore = new BigDecimal(itemsInStores);

        avgItemInStore = avgItemInStore.divide(numOfStores, 2, RoundingMode.CEILING);
        System.out.println("Prosječni broj artikla u dućanima: " + avgItemInStore);

        double finalAvgItemInStore = avgItemInStore.doubleValue();
        List<Store> storesWithAboveAvgItems = allStores.stream()
                .filter(item -> item.getItems().size() > finalAvgItemInStore)
                .collect(Collectors.toList());

        /*for(Store stores2 : storesWithAboveAvgItems) {
            System.out.println("Dućani koji imaju više nego prosječno artikala od svih dućana: " + stores2.getName());
        }*/
        storesWithAboveAvgItems.forEach(h -> System.out.println("Dućani koji imaju više nego prosječno artikala od svih dućana: " + h.getName()));
    }

    public static void sortStoresByAboveAvgItemsNoLambda(List<Store> allStores) {
        BigDecimal numOfItemsInStores = new BigDecimal("0");
        for (Store store : allStores) {
            numOfItemsInStores = numOfItemsInStores.add(BigDecimal.valueOf(store.getItems().size()));
        }
        BigDecimal numOfStores = new BigDecimal(allStores.size());
        BigDecimal avgItemInStore = numOfItemsInStores.divide(numOfStores, 2, RoundingMode.CEILING);
        System.out.println("Prosječni broj artikla u dućanima: " + avgItemInStore);

        List<Store> finalAvgItemInStore = new ArrayList<>();
        for (Store store : allStores) {
            if (BigDecimal.valueOf(store.getItems().size()).compareTo(avgItemInStore) > 0) {
                finalAvgItemInStore.add(store);
            }
        }

        finalAvgItemInStore.forEach(h -> System.out.println("Sve trgovine koje imaju natprosječan broj artikala bez lambdi: " + h.getName()));
    }

    /**
     * Finds factory with the biggest item volumes.
     *
     * @param factories Factory type array.
     */
    public static void findFactoryArticleVolume(Factory[] factories) {

        //int idx = 0;
        //int jdx = 0;

        BigDecimal biggestVolume = new BigDecimal("0");


        Optional<Factory> fac = Optional.empty();
        Optional<Item> ite = Optional.empty();
        //Class<? extends Factory[]> test = factories.getClass();
        /*for(int i = 0; i < factories.length; i++){
            for(int j = 0; j < factories[i].getItems().size(); j++){

                BigDecimal volume = factories[i].getItems()[j].getWidth().multiply(factories[i].getItems()[j].getHeight()).multiply(factories[i].getItems()[j].getLength());

                if(volume.compareTo(biggestVolume) >= 0){
                    biggestVolume = volume;
                    idx = i;
                    jdx = j;
                }
            }
        }*/
        for (Factory factory : factories) {
            //System.out.println(factory.getName());
            for (Item items : factory.getItems()) {
                BigDecimal volume = items.getWidth().multiply(items.getHeight()).multiply(items.getLength());

                if (volume.compareTo(biggestVolume) >= 0) {
                    biggestVolume = volume;
                    fac = Optional.of(factory);
                    ite = Optional.of(items);
                }

            }
        }

        if (fac.isPresent()) {
            System.out.println("Tvornica koja ima artikl s najvećim volumenom je: "
                    + fac.get().getName() + " " + ite.get().getName());
        }
    }

    /**
     * Finds a store with the cheapest item.
     *
     * @param stores Store type of array.
     */
    public static void findStoreArticleCheapest(Store[] stores) {


        BigDecimal cheapestPrice = new BigDecimal("1000000");

        Optional<Store> sto = Optional.empty();
        Optional<Item> ite = Optional.empty();

        /*int idx = 0;
        int jdx = 0;

        BigDecimal cheapestPrice = new BigDecimal("1000000");

        for(int i = 0; i < stores.length; i++){
            for(int j = 0; j < stores[i].getItems().length; j++){

                BigDecimal price = stores[i].getItems()[j].getSellingPrice();

                if(price.compareTo(cheapestPrice) < 0){
                    cheapestPrice = price;
                    idx = i;
                    jdx = j;
                }
            }
        }*/
        for (Store store : stores) {
            for (Item items : store.getItems()) {
                BigDecimal price = items.getSellingPrice();

                if (price.compareTo(cheapestPrice) < 0) {
                    cheapestPrice = price;
                    sto = Optional.of(store);
                    ite = Optional.of(items);
                }
            }
        }

        if (sto.isPresent()) {
            System.out.println("Dućan koji ima najjeftiniji artikl je : " +
                    sto.get().getName() + " " + ite.get().getName());
        }
    }

    /**
     * Calculates an edible item with the highest value of kilocalories
     * and calculates the most expensive price according to its weight.
     *
     * @param items Item type of array.
     */
    public static void findHighestKiloCaloriesAndPrice(List<Item> items) {

        Integer biggestKilocalories = 0;
        int idx = 0;

        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {

            //System.out.println(items[i].getName());
            if (items.get(i) instanceof EdibleItem edibleItem) {
                if (edibleItem.calculateKilocalories().compareTo(biggestKilocalories) > 0) {
                    biggestKilocalories = edibleItem.calculateKilocalories();
                    idx = i;
                }
            } else if (items.get(i) instanceof EdibleItem1 edibleItem1) {
                if (edibleItem1.calculateKilocalories().compareTo(biggestKilocalories) > 0) {
                    biggestKilocalories = edibleItem1.calculateKilocalories();
                    idx = i;
                }
            }
        }
        if (biggestKilocalories != 0) {
            System.out.println("Namirnica " + items.get(idx).getName() + " ima najveću vrijednost kilokalorija: " + biggestKilocalories);
        }

        BigDecimal highestPrice = new BigDecimal("0");
        int idx1 = 0;

        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            if (items.get(i) instanceof EdibleItem edibleItem) {
                if (edibleItem.getSellingPrice().compareTo(highestPrice) > 0) {
                    highestPrice = edibleItem.getSellingPrice();
                    idx1 = i;
                }
            } else if (items.get(i) instanceof EdibleItem1 edibleItem1) {
                if (edibleItem1.getSellingPrice().compareTo(highestPrice) > 0) {
                    highestPrice = edibleItem1.getSellingPrice();
                    idx1 = i;
                }
            }
        }
        System.out.println("Namirnica " + items.get(idx1).getName() + " ima najveću ukupnu cijenu s obzirom na težinu: " + highestPrice);

    }

    /**
     * Outputs a laptop with the shortest warranty.
     *
     * @param items Item type of array.
     */
    public static void findShortestWarranty(List<Item> items) {
        Integer shortestWarranty = 10000;
        int idx = 0;

        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            if (items.get(i) instanceof Laptop laptop) {
                if (laptop.getWarranty().compareTo(shortestWarranty) < 0) {
                    shortestWarranty = laptop.getWarranty();
                    idx = i;
                }
            }
        }
        if (shortestWarranty != 10000) {
            System.out.println("Laptop " + items.get(idx).getName() + " ima najkraći rok garancije: " + shortestWarranty + " mjeseci.");
        }
    }

    public static void printfOfMostExpensiveItemPerCategories(List<Item> items, Category[] categories) {

        items.sort(new ProductionSorter().reversed());

        Map<Category, List<Item>> categoryItemMap = new HashMap<>();
        //punjenje mape
        for (Item item : items) {
            if (categoryItemMap.containsKey(item.getCategory())) {
                List<Item> itemList = categoryItemMap.get(item.getCategory());
                itemList.add(item);
                categoryItemMap.put(item.getCategory(), itemList);
            } else {
                List<Item> newItemList = new ArrayList<>();
                newItemList.add(item);
                categoryItemMap.put(item.getCategory(), newItemList);
            }
        }

        for (Category category : categories) {
            Optional<List<Item>> checked = Optional.ofNullable(categoryItemMap.get(category));
            if (checked.isPresent()) {
                int listSize = categoryItemMap.get(category).size();
                System.out.println("Najskuplji artikl kategorije " + categoryItemMap.get(category).get(0).getCategory().getName() + " ima cijenu: " +
                        "" + categoryItemMap.get(category).get(0).getSellingPrice() + ".");
                System.out.println("Najjeftiniji artikl kategorije " + categoryItemMap.get(category).get(0).getCategory().getName() + " ima cijenu: " +
                        "" + categoryItemMap.get(category).get(listSize - 1).getSellingPrice() + ".");
            }
        }
    }

    public static void printfOfCheapestAndMostExpensiveItemFromEdibleAndTechnical(List<Item> items) {
        List<Item> technicalList = new ArrayList<>(0);
        List<Item> edibleList = new ArrayList<>(0);
        for (Item item : items) {
            if (item instanceof Technical) {
                technicalList.add(item);
            }
            if (item instanceof Edible) {
                edibleList.add(item);
            }
        }
        technicalList.sort(new ProductionSorter().reversed());
        edibleList.sort(new ProductionSorter().reversed());


        System.out.println();
        if (technicalList.size() != 0) {
            System.out.println();
            System.out.println("Najskuplji artikl Techical sučelja " + technicalList.get(0).getName() + " ima cijenu: " +
                    technicalList.get(0).getSellingPrice());
            System.out.println("Najjeftiniji artikl Techical sučelja " + technicalList.get(technicalList.size() - 1).getName() + " ima cijenu: " +
                    technicalList.get(technicalList.size() - 1).getSellingPrice());
        }
        if (edibleList.size() != 0) {
            System.out.println("Najskuplji artikl Edible sučelja " + edibleList.get(0).getName() + " ima cijenu: " +
                    edibleList.get(0).getSellingPrice());
            System.out.println("Najjeftiniji artikl Edible sučelja " + edibleList.get(edibleList.size() - 1).getName() + " ima cijenu: " +
                    edibleList.get(edibleList.size() - 1).getSellingPrice());
        }
    }

    /**
     * Outputs items.
     *
     * @param items Item type of array.
     */
    public static void printOfItems(List<Item> items) {
        System.out.println();

        for (Item item : items) {
            System.out.println(item.getName() + " " + item.getCategory().getName() + " " + item.getWidth() + " " +
                    item.getHeight() + " " + item.getLength() + " " + item.getProductionCost() + " " +
                    item.getSellingPrice());
        }
    }

    /**
     * Outputs addresses of factories.
     */
    public static void printOfAddresses(Address[] address) {

        //System.out.println(addressess.getStreet() + " " + addressess.getHouseNumber() + " " + address.getCity() + " " + address.getPostalCode());
        for (int i = 0; i < NUMBER_OF_FACTORIES; i++) {
            System.out.println(address[i].getStreet() + " " + address[i].getHouseNumber() + " " + address[i].getCity() + " " + address[i].getPostalCode());
        }
        System.out.println();
    }

    /**
     * Outputs factories.
     *
     * @param factories Factory type of array.
     */
    public static void printfOfFactories(Factory[] factories) {
        System.out.println();

        /*for (Factory factory : factories) {
            System.out.println(factory.getName());
            for (int j = 0; j < factory.getItems().size(); j++) {
                System.out.println(factory.getItems()[j].getName());
            }
        }*/
        for (Factory factory : factories) {
            System.out.println(factory.getName());

            for (Item items : factory.getItems()) {
                System.out.println(items.getName());
            }
        }

    }

    /**
     * Outputs of stores.
     *
     * @param stores Store type of array.
     */
    public static void printfOfStores(Store[] stores) {

        /*for (Store store : stores) {
            System.out.println(store.getName());
            for (int j = 0; j < store.getItems().length; j++) {
                System.out.println(store.getItems()[j].getName());
            }
        }*/

        for (Store store : stores) {
            System.out.println(store.getName());
            for (Item items : store.getItems()) {
                System.out.println(items.getName());
            }
        }

        System.out.println();
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Starts the application from here
     *
     * @param args takes string parameters
     */

    public static void main(String[] args) {


        logger.info("Example log from {}", Main.class.getSimpleName());

        //CATEGORIES-----------------------------------------------------------------

        Scanner input = new Scanner(System.in);

        List<Item> items;
        Category[] categories = new Category[NUMBER_OF_CATEGORIES];
        Category[] pom = new Category[NUMBER_OF_CATEGORIES];
        //System.out.println("Unesite " + NUMBER_OF_CATEGORIES + " kategorije artikala: ");
        String[] categoryNames = new String[NUMBER_OF_CATEGORIES];
        boolean loop = false;

        /*for (int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            do {
                try {
                    categories[i] = makeCategories(input, categoryNames, i);
                    loop = false;
                } catch (Uncommon uncommon) {
                    System.out.println(uncommon.getMessage());
                    logger.error("Name of the category already exists!", uncommon);
                    loop = true;
                }
            }
            while (loop);
        }*/

        //String datCat = "dat/categories.txt";

        categories = makeObjectFromFileCategories(categories, "dat/categories.txt");
        for (int i = 0; i < categories.length; i++) {
            System.out.println("Categories: " + categories[i]);
        }

        /*for(int i = 0; i < NUMBER_OF_CATEGORIES; i++) {
            System.out.println("datCat: " + categories[i].getName() + " " + categories[i].getId() + " " + categories[i].getDescription());
        }*/


        System.out.println();
        items = makeObjectFromFileItems(categories, "dat/items.txt");

        for (int i = 0; i < items.size(); i++) {
            System.out.println("Items name: " + items.get(i).getCategory().getName());
        }

        List<Laptop> laptops = new ArrayList<>();
        List<EdibleItem> edibleItems = new ArrayList<>();
        List<EdibleItem1> edibleItems1 = new ArrayList<>();
        List<Item> usualItems = new ArrayList<>();


        for (Item item : items) {
            if (item.getCategory().getName().compareTo("Technical equipment") == 0) {
                Laptop laptop = new Laptop(item.getName(), item.getId(), item.getCategory(), item.getWidth(), item.getHeight(), item.getLength(),
                        item.getProductionCost(), item.getSellingPrice(), item.getDiscountAmount());
                laptops.add(laptop);
            } else if (item.getCategory().getName().compareTo("Food") == 0) {
                EdibleItem edibleItem = new EdibleItem(item.getName(), item.getId(), item.getCategory(), item.getWidth(), item.getHeight(), item.getLength(),
                        item.getProductionCost(), item.getSellingPrice(), item.getDiscountAmount());
                edibleItems.add(edibleItem);
            } else if (item.getCategory().getName().compareTo("Food1") == 0) {
                EdibleItem1 edibleItem1 = new EdibleItem1(item.getName(), item.getId(), item.getCategory(), item.getWidth(), item.getHeight(), item.getLength(),
                        item.getProductionCost(), item.getSellingPrice(), item.getDiscountAmount());
                edibleItems1.add(edibleItem1);
            } else {
                usualItems.add(item);
            }
        }
        for (Laptop laptop : laptops) {
            System.out.println("Ime artikla iz kategorije laptop: " + laptop.getName());
        }
        for (EdibleItem edibleItem : edibleItems) {
            System.out.println("Ime artikla iz kategorije food: " + edibleItem.getName());
        }
        for (EdibleItem1 edibleItem : edibleItems1) {
            System.out.println("Ime artikla iz kategorije food1: " + edibleItem.getName());
        }
        usualItems.forEach(item -> System.out.println("Ime artikla bez kategorije: " + item.getName()));

        /*for(Item item : items) {
            System.out.println("itemi: " + item.getId() + item.getName() + item.getWidth() + item.getHeight() + item.getLength() +
                    item.getProductionCost() + item.getSellingPrice() + item.getDiscountAmount());
        }*/


        //items = makeItems(input, categories);


        System.out.println();

        //printOfItems(items);

        //FACTORIES-----------------------------------------------------------

        int[] realChosenFactoryArticlesNumbers = new int[NUMBER_OF_FACTORIES];
        int[] test = new int[NUMBER_OF_FACTORIES];
        String factoryName = "factoryName";
        Factory[] factories = new Factory[NUMBER_OF_FACTORIES];
        Address[] addresses = new Address[NUMBER_OF_FACTORIES];
        Boolean loop1 = false;
        int cityNumber = -1;

        /*for (int i = 0; i < NUMBER_OF_FACTORIES; i++) {
            realChosenFactoryArticlesNumbers = (inputFactoryArticlesNumber(input, test, i));
        }

        System.out.println("Unesite " + NUMBER_OF_FACTORIES + " tvornice: ");*/

        //List<Item> items1 = new ArrayList<>();
        System.out.println();
        //addresses = makeObjectFromFileAddresses("dat/addresses.txt");
        for (int i = 0; i < addresses.length; i++) {
            System.out.println("Addresses: " + addresses[i].getCity());
        }

        System.out.println();
        factories = makeObjectFromFileFactories(addresses, items, "dat/factories.txt");
        for (int i = 0; i < factories.length; i++) {
            System.out.println("Factories: " + factories[i]);
        }


        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/serialization.dat"))) {
            for (Factory factory : factories) {
                if (factory.getItems().size() >= 5) {
                    System.out.println("Ime serijaliziranih tvornica i njihovih veličina: " + factory.getName() + " " + factory.getItems().size());
                    out.writeObject(factory);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }


        /*for (int i = 0; i < NUMBER_OF_FACTORIES; i++) {

            List<Item> items1 = new ArrayList<>(1);

            int j = 0;
            Integer[] chosenItemsArray = new Integer[realChosenFactoryArticlesNumbers[i]];

            do {
                System.out.println("Unesite broj grada u kojem se nalazi " + (i + 1) + " tvornica: ");
                System.out.println("(1) Zagreb, (2) Vukovar, (3) Đakovo");
                cityNumber = input.nextInt();

                loop1 = false;

                if (cityNumber < 0 || cityNumber > 4) {
                    loop1 = true;
                }
            }
            while (loop1);


            Cities citiesTest = Cities.CITY_ZAGREB;
            Address address = new BuilderPattern().street("default").house("default").city(citiesTest).code(citiesTest).build();

            switch (cityNumber) {
                case 1 -> {
                    System.out.println("Odabrani grad je Zagreb!");
                    Cities cities = Cities.CITY_ZAGREB;
                    address = new BuilderPattern().street("street").house("house").city(cities).code(cities).build();
                    addresses[i] = address;
                }
                case 2 -> {
                    System.out.println("Odabrani grad je Vukovar!");
                    Cities cities = Cities.CITY_VUKOVAR;
                    address = new BuilderPattern().street("street").house("house").city(cities).code(cities).build();
                    addresses[i] = address;
                }
                case 3 -> {
                    System.out.println("Odabrani grad je Đakovo!");
                    Cities cities = Cities.CITY_DJAKOVO;
                    address = new BuilderPattern().street("street").house("house").city(cities).code(cities).build();
                    addresses[i] = address;
                }
                default -> {
                    System.out.println("Some error");
                }
            }


            *//*Address address = new BuilderPattern().street("street").house("house").city(cities).code(cities).build();
            addresses[i] = address;*//*


            do {
                try {
                    for (; j < realChosenFactoryArticlesNumbers[i]; ++j) {
                        factories[i] = makeFactories(input, items, realChosenFactoryArticlesNumbers,
                                factoryName, i, j, items1, loop, chosenItemsArray, address);
                    }

                    loop = false;

                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;

                } catch (Anomaly anomaly) {
                    System.out.println(anomaly.getMessage());
                    logger.error("Item has already been selected", anomaly);
                    loop = true;
                }
            }
            while (loop);

        }*/


        //printfOfFactories(factories);
        //printOfAddresses(addresses);

        //STORES----------------------------------------------------------------------------------------

        int[] realChosenStoreArticlesNumbers = new int[NUMBER_OF_STORES];
        int[] test1 = new int[NUMBER_OF_STORES];
        //String storeName = "storeName";
        //String storeWebAddress = "storeWebAddress";
        Store[] stores = new Store[NUMBER_OF_STORES];

        /*for (int i = 0; i < NUMBER_OF_STORES; i++) {
            realChosenStoreArticlesNumbers = (inputStoreArticlesNumber(input, test1, i));
        }*/

        //System.out.println("Unesite " + NUMBER_OF_STORES + " dućana: ");

        System.out.println();
        stores = makeObjectFromFileStores(addresses, items, "dat/stores.txt");

        for (int i = 0; i < stores.length; i++) {
            System.out.println("Stores: " + stores[i]);
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/serialization.dat"))) {
            for (Store store : stores) {
                if (store.getItems().size() >= 5) {
                    System.out.println("Ime serijaliziranih dućana i veličina: " + store.getName() + " " + store.getItems().size());
                    out.writeObject(store);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }


        /*for (int i = 0; i < NUMBER_OF_STORES; i++) {

            input.nextLine();
            List<Item> items2 = new ArrayList<>(1);
            System.out.println("Unesite ime " + (i + 1) + ". dućana: ");
            String storeName = input.nextLine();
            logger.info("Store name: " + storeName);
            //input.nextLine();

            System.out.println("Unesite naziv web adrese " + (i + 1) + ". dućana: ");
            String storeWebAddress = input.nextLine();
            logger.info("Web address: " + storeWebAddress);
            //input.nextLine();

            int j = 0;
            Integer[] chosenItemsArray1 = new Integer[realChosenStoreArticlesNumbers[i]];

            do {
                try {
                    for (; j < realChosenStoreArticlesNumbers[i]; ++j) {
                        stores[i] = makeStores(input, items, realChosenStoreArticlesNumbers, storeName, i, j,
                                items2, loop, chosenItemsArray1, storeWebAddress);
                    }

                    loop = false;

                } catch (InputMismatchException inp) {
                    System.out.println("Morate unijeti brojčane vrijednosti!");
                    logger.error("Input is not a number!", inp);
                    input.nextLine();
                    loop = true;

                } catch (Anomaly anomaly) {
                    System.out.println(anomaly.getMessage());
                    logger.error("Item has already been selected", anomaly);
                    loop = true;
                }
            }
            while (loop);
        }

        input.nextLine();

         */


        //FoodStore and Technical Store------------------------------------------------------------------

        FoodStore<Edible>[] foodStore = makeObjectFromFileFoodStore(addresses, items, "dat/foodstores.txt");
        for (FoodStore foodStore1 : foodStore) {
            System.out.println("Ime dućana s hranom: " + foodStore1.getName());
        }

        //FoodStore<Edible> foodStore = addFoodStoreItems(input, items);
        //System.out.println("Foodstore ime: " + foodStore.getName());


        TechnicalStore<Technical>[] technicalStore = makeObjectFromFileTechnicalStore(addresses, items, "dat/technicalstores.txt");
        for (TechnicalStore technicalStore1 : technicalStore) {
            System.out.println("Ime dućana s tehnikom: " + technicalStore1.getName());
        }
        //TechnicalStore<Technical> technicalStore = addTechnicalStoreItems(input, items);
        //System.out.println("TechnicalStore ime: " + foodStore.getName());


        //Sorting food store and technical store items by item volume------------------------------------

        Instant sequentialStart = Instant.now();
        sortItemsByVolumeLambda(items, stores);
        Instant sequentialEnd = Instant.now();

        System.out.println();
        System.out.println("Ukupno vrijeme potrebno za sortiranje lambdama po volumenu artikla: " +
                Duration.between(sequentialStart, sequentialEnd).toMillis());
        System.out.println();

        //bez lambdi

        sequentialStart = Instant.now();
        sortItemsByVolumeNoLambda(items, stores);
        sequentialEnd = Instant.now();

        System.out.println();
        System.out.println("Ukupno vrijeme potrebno za sortiranje bez lambdi po volumenu artikla: " +
                Duration.between(sequentialStart, sequentialEnd).toMillis());
        System.out.println();

        //List<Item> allStoreItems = new ArrayList<>();
        //allStoreItems.addAll(foodStore.getItems());
        //allStoreItems.addAll(technicalStore.getItems());

        //Average price of all items who have volume that's above average

        sequentialStart = Instant.now();
        sortAvgItemPriceAboveAvgVolumeLambda(items);
        sequentialEnd = Instant.now();

        System.out.println();
        System.out.println("Ukupno vrijeme potrebno za sortiranje lambdama po cijeni artikla koji imaju nadprosječni volumen: " +
                Duration.between(sequentialStart, sequentialEnd).toMillis());
        System.out.println();


        //bez lambdi

        sequentialStart = Instant.now();
        sortAvgItemPriceAboveAvgVolumeNoLambda(items);
        sequentialEnd = Instant.now();

        System.out.println();
        System.out.println("Ukupno vrijeme potrebno za sortiranje bez lambdi po cijeni artikla koji imaju nadprosječni volumen: " +
                Duration.between(sequentialStart, sequentialEnd).toMillis());
        System.out.println();


        //Print of all stores that have above average number of items

        List<Store> allStores = new ArrayList<>(Arrays.asList(stores));
        allStores.addAll(Arrays.asList(foodStore));
        allStores.addAll(Arrays.asList(technicalStore));

        allStores.forEach(h -> System.out.println("Popis svih dućana: " + h.getName()));

        sequentialStart = Instant.now();
        sortStoresByAboveAvgItemsLambda(allStores);
        sequentialEnd = Instant.now();


        System.out.println();
        System.out.println("Ukupno vrijeme potrebno za sortiranje lambdama po dućanima koji imaju nadprosječan broj artikla: " +
                Duration.between(sequentialStart, sequentialEnd).toMillis());
        System.out.println();


        //bez lambdi

        sequentialStart = Instant.now();
        sortStoresByAboveAvgItemsNoLambda(allStores);
        sequentialEnd = Instant.now();

        System.out.println();
        System.out.println("Ukupno vrijeme potrebno za sortiranje bez lambdi po dućanima koji imaju nadprosječan broj artikla: " +
                Duration.between(sequentialStart, sequentialEnd).toMillis());

        System.out.println();


        //Filtriranje po artiklima ovisno o popustu


        BigDecimal zero = new BigDecimal("0");

        Optional<Item> discountItems = items.stream()
                .filter(i -> i.getDiscountAmount().discountAmount().compareTo(zero) > 0)
                .findAny();

        if (discountItems.isPresent()) {
            System.out.println("Postoje artikli koji imaju popust veću od 0!");
        } else {
            System.out.println("Nema o koji imaju popust veći od 0!");
        }

        //Ispisati broj artikala u svakoj od trgovina koristeći toString u toku

        System.out.println("Broj artikla u dućanima: ");
        for (Store store : allStores) {
            System.out.print(store.getName() + " ");
        }
        System.out.println();
        (allStores).stream()
                .map(item -> (item.getItems().size()))
                .forEach(System.out::println);


        printfOfMostExpensiveItemPerCategories(items, categories);
        printfOfCheapestAndMostExpensiveItemFromEdibleAndTechnical(items);

        findFactoryArticleVolume(factories);
        findStoreArticleCheapest(stores);
        findHighestKiloCaloriesAndPrice(items);
        findShortestWarranty(items);

        //printOfItems(items);
        //printOfAddresses(addresses);
        //printfOfFactories(factories);
        //printfOfStores(stores);

    }
}
