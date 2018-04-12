package org.boudnik.framework;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandre_Boudnik
 * @since 11/15/2017
 */
public class Transaction implements AutoCloseable {
    private final Map<Class<? extends OBJ>, Map<Object, OBJ>> scope = new HashMap<>();
    private final Map<OBJ, BinaryObject> mementos = new HashMap<>();
    private final Ignite ignite;

    Transaction(Ignite ignite) {
        this.ignite = ignite;
    }

    public void commit(Transactionable transactionable) {
        transactionable.commit();
        commit();
    }

    public void commit() {
        try {
            walk(this::doCommit);
            ignite.transactions().tx().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }
    }

    public void rollback() {
        try {
            walk(this::doRollback);
            org.apache.ignite.transactions.Transaction tx = ignite.transactions().tx();
            if (tx != null)
                tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }
    }

    private void clear() {
        scope.clear();
        mementos.clear();
    }

    private void doCommit(Class<? extends OBJ> clazz, Map.Entry<Object, OBJ> entry) {
        IgniteCache<Object, BinaryObject> cache = cache(clazz);
        Object key = entry.getKey();
        OBJ obj = entry.getValue();
        if (OBJ.TOMBSTONE == obj)
            cache.remove(key);
        else {
            BinaryObject current = ignite.binary().toBinary(obj);
            BinaryObject memento = mementos.get(obj);
            if (memento != null && !current.equals(memento)) {
                obj.onCommit(current, memento);
            }
            cache.put(key, current);
        }
    }

    private void doRollback(@SuppressWarnings("unused") Class<? extends OBJ> clazz, @SuppressWarnings("unused") Map.Entry<Object, OBJ> entry) {
/*
        for (@SuppressWarnings("unused") Map.Entry<OBJ, BinaryObject> memento : mementos.entrySet()) {
            BinaryObject binary = memento.getValue();
            try {
                Map<String, PropertyDescriptor> pds = new HashMap<>();
                for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors())
                    pds.put(pd.getName(), pd);
                for (String field : binary.type().fieldNames()) {
                    pds.get(field).setValue(field, binary.field(field));
                }
                for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors())
                    pd.setValue(pd.getName(), binary.field(pd.getName()));
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
*/
    }

    private void walk(Worker worker) {
        for (Map.Entry<Class<? extends OBJ>, Map<Object, OBJ>> byClass : scope.entrySet()) {
            for (Map.Entry<Object, OBJ> entry : byClass.getValue().entrySet()) {
                worker.accept(byClass.getKey(), entry);
            }
        }
    }

    @NotNull
    private Map<Object, OBJ> getMap(Class<? extends OBJ> clazz) {
        return scope.computeIfAbsent(clazz, k -> new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public <K, V extends OBJ> V get(Class<V> clazz, K identity) {
        Map<Object, OBJ> map = getMap(clazz);
        V obj = (V) map.get(identity);
        if (obj == null) {
            if (map.containsKey(identity))
                return null;
            else {
                BinaryObject binaryObject = cache(clazz).<K, BinaryObject>withKeepBinary().get(identity);
                if (binaryObject == null)
                    return null;
                V v = binaryObject.deserialize();
                v.setKey(identity);
                map.put(identity, v);
                mementos.put(v, binaryObject);
                return v;
            }
        } else
            return obj;
    }

    void save(OBJ obj) {
        save(obj, obj.getKey());
    }

    void save(OBJ obj, Object key) {
        getMap(obj.getClass()).put(key, obj);
    }

    void delete(OBJ obj) {
        getMap(obj.getClass()).put(obj.getKey(), OBJ.TOMBSTONE);
    }

    void revert(OBJ obj) {
//        unSave(obj);
        //todo
        cache(obj.getClass()).remove(obj.getKey());
    }

    private IgniteCache<Object, BinaryObject> cache(Class<? extends OBJ> clazz) {
        return ignite.cache(clazz.getName());
    }

    @Override
    public void close() throws Exception {
        if (ignite.transactions().tx() != null)
            rollback();
    }

    <K, V extends OBJ<K>> boolean isDeleted(V reference) {
        return OBJ.TOMBSTONE == getMap(reference.getClass()).get(reference.getKey());
    }

    Transaction begin() {
        ignite.transactions().txStart();
        return this;
    }

    Transaction tx(Transactionable transactionable) {
        ignite.transactions().txStart();
        transactionable.commit();
        commit();
        return this;
    }

    Transaction tx() {
        if (ignite.transactions().tx() == null)
            throw new NoTransactionException();
        return this;
    }

    @FunctionalInterface
    interface Worker {
        /**
         * Performs this operation on the given arguments.
         *
         * @param c     class
         * @param entry (key -> value)
         */
//        <K, V>
        void accept(Class<? extends OBJ> c, Map.Entry<Object, OBJ> entry);

    }
}
