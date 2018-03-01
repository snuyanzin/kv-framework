package org.boudnik.framework;

import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * @author Alexandre_Boudnik
 * @since 11/10/17 15:55
 */
public class Store {
    private static final ThreadLocal<Store> stores = ThreadLocal.withInitial(Store::new);

    private final Transaction transaction;

    private Store() {
        transaction = new Transaction(Ignition.getOrStart(new IgniteConfiguration()));
    }

    public static Store instance() {
        return stores.get();
    }

    public Transaction begin() {
        return transaction.begin();
    }

    public void create(Class clazz) {
        Ignition.ignite().getOrCreateCache(clazz.getName());
    }

    Transaction transaction() {
        return transaction.tx();
    }
}
