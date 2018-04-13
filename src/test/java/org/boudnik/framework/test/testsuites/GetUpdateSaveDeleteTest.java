package org.boudnik.framework.test.testsuites;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.junit.Assert;
import org.junit.Test;

public class GetUpdateSaveDeleteTest {

    private static final String NEW_VALUE = "New Value";

    @Test
    public void testGetUpdateSaveDeleteCommit() {
        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetUpdateSaveDeleteCommit").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx()) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveDeleteCommit");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
            entry.setValue(NEW_VALUE);
            entry.save();
            entry.delete();
            tx.commit();
            entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveDeleteCommit");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUpdateSaveDeleteRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetUpdateSaveDeleteRollback").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx()) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveDeleteRollback");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
            entry.setValue(NEW_VALUE);
            entry.save();
            entry.delete();
            tx.rollback();
            entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveDeleteRollback");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
