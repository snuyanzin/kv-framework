package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.MutableTestEntry;
import org.junit.Assert;

public class GetUpdateSaveSuite extends GridCommonAbstractTest {

    private static final String NEW_VALUE = "New Value";

    public void testGetUpdateSaveCommit() {

        Store.instance().create(MutableTestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new MutableTestEntry("testGetUpdateSaveCommit").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Store.instance().begin()) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveCommit");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
            entry.setValue(NEW_VALUE);
            entry.save();
            tx.commit();
            entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveCommit");
            Assert.assertEquals(NEW_VALUE, entry.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testGetUpdateSaveRollback() {

        Store.instance().create(MutableTestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new MutableTestEntry("testGetUpdateSaveRollback").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Transaction tx = Store.instance().begin()) {
            MutableTestEntry entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveRollback");
            Assert.assertNotNull(entry);
            Assert.assertNull(entry.getValue());
            entry.setValue(NEW_VALUE);
            entry.save();
            tx.rollback();
            entry = tx.get(MutableTestEntry.class, "testGetUpdateSaveRollback");
            Assert.assertNull(entry.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
