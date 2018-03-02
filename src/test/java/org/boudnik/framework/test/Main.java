package org.boudnik.framework.test;

import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.junit.Test;

/**
 * @author Alexandre_Boudnik
 * @since 02/13/2018
 */
public class Main {
    @Test
    public void main() {
        Store store = Store.instance();
        store.create(TestEntry.class);
        try (Transaction tx = store.begin()) {
            new TestEntry("http://localhost/1").save("");
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = store.begin()) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            System.out.println("entry = " + entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
