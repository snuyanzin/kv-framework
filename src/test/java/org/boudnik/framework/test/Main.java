package org.boudnik.framework.test;

import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexandre_Boudnik
 * @since 02/13/2018
 */
public class Main {

    @Test
    public void main() {
        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            tx.commit( () -> new TestEntry("http://localhost/1").save(""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            System.out.println("entry = " + entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mt() {
        Store.instance().create(TestEntry.class);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try (Transaction tx = Store.instance().begin()) {
                System.out.println("tx = " + tx);
                tx.commit(() -> new TestEntry("http://localhost/1").save(""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try (Transaction tx = Store.instance().begin()) {
                System.out.println("tx = " + tx);
                tx.commit(() -> new TestEntry("http://localhost/1").save(""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }
}
