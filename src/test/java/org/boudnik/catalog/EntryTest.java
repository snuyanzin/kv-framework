package org.boudnik.catalog;

import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * @author Alexandre_Boudnik
 * @since 03/12/18 15:56
 */
public class EntryTest {

    @Test
    public void main() throws MalformedURLException {
        try (Transaction tx = Store.instance().begin()){
            Entry entry = new Entry("http://localhost/1");
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}