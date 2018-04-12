package org.boudnik.framework.test;

import org.boudnik.framework.Store;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;
import org.junit.Test;

public class ObjSaveTest {
    @Test
    public void checkSeveralEntriesWithDifferentKeys() {
        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            new TestEntry("http://localhost/1").save("checkSeveralEntriesWithDifferentKeys");
            new TestEntry("http://localhost/1").save();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Store.instance().begin()) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            Assert.assertNotNull(entry);
            entry = tx.get(TestEntry.class, "checkSeveralEntriesWithDifferentKeys");
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
