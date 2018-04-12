package org.boudnik.framework.test.core;

import org.boudnik.framework.OBJ;

/**
 * @author Alexandre_Boudnik
 * @since 03/01/18 14:20
 */
public class TestEntry implements OBJ<String> {
    private final String url;

    public TestEntry(String url) {
        this.url = url;
    }

    @Override
    public String getKey() {
        return url;
    }
}
