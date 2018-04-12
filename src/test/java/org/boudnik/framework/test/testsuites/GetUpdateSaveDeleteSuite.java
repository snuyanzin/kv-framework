package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.junit.Assert;

public class GetUpdateSaveDeleteSuite extends GridCommonAbstractTest {

    private static final String NEW_VALUE = "New Value";

    public void testGetUpdateSaveDeleteCommit() {
        Store.instance().create(MutableTestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new MutableTestEntry("testGetUpdateSaveDeleteCommit").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Store.instance().begin()) {
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

    public void testGetUpdateSaveDeleteRollback() {

        Store.instance().create(MutableTestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new MutableTestEntry("testGetUpdateSaveDeleteRollback").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Store.instance().begin()) {
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
