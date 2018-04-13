package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class CreateSaveSuite extends GridCommonAbstractTest {

    public void testCreateSaveCommit() {

        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() ->
                new TestEntry("testCreateSaveCommit").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class)) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateSaveCommit");
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCreateSaveRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class)) {
            new TestEntry("testCreateSaveRollback").save();
            tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class)) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateSaveRollback");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
