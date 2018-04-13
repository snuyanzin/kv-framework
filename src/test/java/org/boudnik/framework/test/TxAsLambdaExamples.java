package org.boudnik.framework.test;

import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.boudnik.framework.Transaction;
import org.boudnik.framework.test.core.TestEntry;
import org.junit.Assert;

public class TxAsLambdaExamples extends GridCommonAbstractTest {
    public void testTx() {
        try (Transaction tx = Store.instance().tx(() -> {
            new TestEntry("http://localhost/1").save();
            new TestEntry("http://localhost/2").save();
        }, TestEntry.class)) {
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            TestEntry entry2 = tx.get(TestEntry.class, "http://localhost/2");
            Assert.assertNotNull(entry);
            Assert.assertNotNull(entry2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCommit() {
        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin()) {
            tx.commit(() -> {
                new TestEntry("http://localhost/1").save();
                new TestEntry("http://localhost/2").save();
            });
            TestEntry entry = tx.get(TestEntry.class, "http://localhost/1");
            TestEntry entry2 = tx.get(TestEntry.class, "http://localhost/2");
            Assert.assertNotNull(entry);
            Assert.assertNotNull(entry2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testBegin() {
        Store.instance().create(TestEntry.class);
        try (Transaction tx = Store.instance().begin(() -> {
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
