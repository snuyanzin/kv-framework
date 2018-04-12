package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class CreateSaveSuite extends GridCommonAbstractTest {

    public void testCreateSaveCommit() {

        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new TestEntry("testCreateSaveCommit").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateSaveCommit");
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCreateSaveRollback() {

        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new TestEntry("testCreateSaveRollback").save();
            tx.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "testCreateSaveRollback");
            Assert.assertNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
