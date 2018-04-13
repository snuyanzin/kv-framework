package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.junit.Assert;

public class GetUpdateSaveSuite extends GridCommonAbstractTest {

    private static final String NEW_VALUE = "New Value";

    public void testGetUpdateSaveCommit() {

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetUpdateSaveCommit").save())) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class)) {
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

    public void testGetUpdateSaveRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetUpdateSaveRollback").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class)) {
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
