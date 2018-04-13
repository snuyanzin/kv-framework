package org.boudnik.framework.test;

import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class TxAsLambdaExamples {
    public void testTx() {
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() -> {
            new TestEntry("http://localhost/1").save();
            new TestEntry("http://localhost/2").save();
        })) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            TestEntry entry2 = tx.get(TestEntry.class, "http://localhost/2");
            Assert.assertNotNull(entry);
            Assert.assertNotNull(entry2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCommit() {
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() -> {
                new TestEntry("http://localhost/1").save();
                new TestEntry("http://localhost/2").save();
            })) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            TestEntry entry2 = tx.get(TestEntry.class, "http://localhost/2");
            Assert.assertNotNull(entry);
            Assert.assertNotNull(entry2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testBegin() {
        try (Transaction tx = Transaction.instance().withCacheName(TestEntry.class).tx(() -> {
            new TestEntry("http://localhost/1").save();
            new TestEntry("http://localhost/2").save();
        })) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            TestEntry entry2 = tx.get(TestEntry.class, "http://localhost/2");
            Assert.assertNotNull(entry);
            Assert.assertNotNull(entry2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
