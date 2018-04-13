package org.boudnik.framework.test.testsuites;

import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;
import org.junit.Test;

public class CreateDeleteTest {

    @Test
    public void testCreateDeleteCommit() {

        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() -> {
            TestEntry te = new TestEntry("testCreateDeleteCommit");
            te.save();
            te.delete();
        })) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx()) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateDeleteCommit");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDeleteRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx())
        {
            TestEntry te = new TestEntry("testCreateDeleteRollback");
            te.save();
            te.delete();
            tx.rollback();
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx()) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateDeleteRollback");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
