package hr.java.production.threads;

import hr.java.production.model.Client;
import hr.java.production.model.Item;
import hr.java.production.sort.ClientSorter;
import hr.java.production.sort.ProductionSorter;

import java.util.List;

/**
 * Uses threads to sort list of clients.
 */
public class ClientSorterThread implements Runnable{
    private List<Client> clientList;

    /**
     * Takes in list of clients.
     * @param clientList
     */
    public ClientSorterThread(List<Client> clientList) {
        this.clientList = clientList;
    }

    /**
     * This method is called with Platform.runLater.
     */
    @Override
    public synchronized void run(){

        clientList.sort(new ClientSorter().reversed());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
