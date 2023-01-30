
package hr.java.production.sort;
import hr.java.production.model.Client;
import java.util.Comparator;

/**
 *Sorts clients based on their age.
 */
public class ClientSorter implements Comparator<Client> {

    @Override
    public int compare(Client c1, Client c2) {

        if (c1.getDateOfBirth().compareTo(c2.getDateOfBirth()) > 0) {
            return 1;
        } else if (c1.getDateOfBirth().compareTo(c2.getDateOfBirth()) < 0) {
            return -1;
        } else {
            return c1.getName().compareTo(c2.getName());
        }

    }
}
