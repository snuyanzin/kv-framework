package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class CreateDeleteSuite extends GridCommonAbstractTest {

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
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class)) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateDeleteCommit");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCreateDeleteRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() -> {
            TestEntry te = new TestEntry("testCreateDeleteRollback");
            te.save();
            te.delete();
            throw new RuntimeException("RollbackException");
        })) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class)) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateDeleteRollback");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
