package hr.java.production.threads;

import hr.java.production.model.Item;
import hr.java.production.sort.ProductionSorter;

import java.util.List;

public class SortingItemsThread implements Runnable{

    private List<Item> itemList;

    public SortingItemsThread(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public synchronized void run(){

        itemList.sort(new ProductionSorter().reversed());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
