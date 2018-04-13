package org.boudnik.framework.test.testsuites;

import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class CreateSaveDeleteTest {

    public void testCommitDeleteCommit() {
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() ->
                new TestEntry("testCommitDeleteCommit").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx()) {

            TestEntry entry = tx.get(TestEntry.class, "testCommitDeleteCommit");
            Assert.assertNotNull(entry);
            entry.delete();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx()) {
            TestEntry entry = tx.get(TestEntry.class, "testCommitDeleteCommit");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCommitDeleteRollback() {
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() ->
                new TestEntry("testCommitDeleteRollback").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx()) {
            TestEntry entry = tx.get(TestEntry.class, "testCommitDeleteRollback");
            Assert.assertNotNull(entry);
            entry.delete();
            tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx()) {
            TestEntry entry = tx.get(TestEntry.class, "testCommitDeleteRollback");
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
