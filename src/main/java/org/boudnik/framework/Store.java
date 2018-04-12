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

    public Transaction begin(Transactionable transactionable) {
        Transaction tx = transaction.begin();
        transactionable.commit();
        tx.commit();
        return tx;
    }

    public Transaction tx(Transactionable transactionable) {
        return transaction.tx(transactionable);
    }

    public void create(Class clazz) {
        Ignition.ignite().getOrCreateCache(clazz.getName());
    }

    public Transaction tx(Transactionable transactionable, Class clazz, Class ... classes) {
        Ignition.ignite().getOrCreateCache(clazz.getName());
        for(Class cl: classes) {
            Ignition.ignite().getOrCreateCache(cl.getName());
        }
        return tx(transactionable);
    }

    public Transaction tx(Transactionable transactionable, Class clazz) {
        Ignition.ignite().getOrCreateCache(clazz.getName());
        return tx(transactionable);
    }

    Transaction transaction() {
        return transaction.tx();
    }
}
