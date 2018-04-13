package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class GetDeleteSuite extends GridCommonAbstractTest {

    public void testGetDeleteCommit() {
        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
                new MutableTestEntry("testGetDeleteCommit").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class)) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetDeleteCommit");
            Assert.assertNotNull(entry);
            entry.delete();
            tx.commit();
            entry = tx.get(MutableTestEntry.class, "testGetDeleteCommit");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetDeleteRollback() {

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class).tx(() ->
            new MutableTestEntry("testGetDeleteRollback").save()
        )) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Transaction.instance().withCacheName(MutableTestEntry.class)) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetDeleteRollback");
            Assert.assertNotNull(entry);
            entry.delete();
            tx.rollback();
            entry = tx.get(MutableTestEntry.class, "testGetDeleteRollback");
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
