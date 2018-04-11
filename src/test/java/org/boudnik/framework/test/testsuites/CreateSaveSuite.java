package org.boudnik.framework.test.testsuites;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class CreateSaveSuite extends GridCommonAbstractTest {

    public void testCreateSave() {

        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new TestEntry("http://localhost/1").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            System.out.println(entry);
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
