package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class CreateDeleteSuite extends GridCommonAbstractTest {

    public void testCreateDeleteCommit() {

        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            TestEntry te = new TestEntry("testCreateDeleteCommit");
            te.save();
            te.delete();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateDeleteCommit");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCreateDeleteRollback() {

        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            TestEntry te = new TestEntry("testCreateDeleteRollback");
            te.save();
            te.delete();
            tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateDeleteRollback");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
