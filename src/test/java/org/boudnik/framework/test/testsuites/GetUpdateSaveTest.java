package org.boudnik.framework.test.testsuites;

import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.junit.Assert;
import org.junit.Test;

public class GetUpdateSaveTest {

    private static final String NEW_VALUE = "New Value";

    @Test
    public void testGetUpdateSaveCommit() {

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetUpdateSaveCommit").save())) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx()) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveCommit");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
            entry.setValue(NEW_VALUE);
            entry.save();
            tx.commit();
            entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveCommit");
            Assert.assertEquals(NEW_VALUE, entry.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUpdateSaveRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetUpdateSaveRollback").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx()) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveRollback");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
            entry.setValue(NEW_VALUE);
            entry.save();
            tx.rollback();
            entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveRollback");
            Assert.assertNull(entry.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
