package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.junit.Assert;

public class GetDeleteSuite extends GridCommonAbstractTest {

    public void testGetDeleteCommit() {
        Store.instance().create(MutableTestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new MutableTestEntry("testGetDeleteCommit").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Store.instance().begin()) {
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

        Store.instance().create(MutableTestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new MutableTestEntry("testGetDeleteRollback").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Store.instance().begin()) {
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
