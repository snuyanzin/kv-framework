package org.boudnik.framework.test;

import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;
import org.junit.Test;

public class ObjSaveTest {
    @Test
    public void checkSeveralEntriesWithDifferentKeys() {
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() -> {
            new TestEntry("http://localhost/1").save("checkSeveralEntriesWithDifferentKeys");
            new TestEntry("http://localhost/1").save();
        })) {
            System.out.println("tx = " + tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class)) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            Assert.assertNotNull(entry);
            entry = tx.get(TestEntry.class, "checkSeveralEntriesWithDifferentKeys");
            Assert.assertNotNull(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
