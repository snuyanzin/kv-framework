package org.boudnik.framework.test.testsuites;

import junit.framework.TestSuite;

public class KVTestSuiteManager extends TestSuite {
    public static TestSuite suite() {
        TestSuite suite = new TestSuite("KV test suite");
        suite.addTestSuite(CreateSaveSuite.class);
        suite.addTestSuite(CreateSaveDeleteSuite.class);
        suite.addTestSuite(CreateDeleteSuite.class);
        suite.addTestSuite(GetDeleteSuite.class);
        suite.addTestSuite(GetUpdateSaveDeleteSuite.class);
        suite.addTestSuite(GetUpdateSaveSuite.class);
        return suite;
    }
}
