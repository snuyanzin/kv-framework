package org.boudnik.catalog;

import org.boudnik.framework.OBJ;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

/**
 * @author Alexandre_Boudnik
 * @since 03/12/18 15:55
 */
public class Entry implements OBJ<String>, Function<Entry, Entry > {
    String uri;

    public Entry(String uri) {
        this.uri = uri;
    }

    public Entry(URI uri) {
        this(uri.toString());
    }

    public Entry(URL url) {
        this(url.toExternalForm());
    }

    public Set<URI> uris() {
        return new HashSet<>();
    }

    @Override
    public String getKey() {
        return uri;
    }

    @Override
    public Entry apply(Entry entry) {
        return new Entry(this.uri + "|" + entry.uri);
    }

    //    public Entry concat(Entry other) {
//
//    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        Entry x = new Entry("x");
        Entry y = new Entry("y");
        Function<Entry, Entry> compose = x.compose(y);
        Set<URI> a = compose.andThen(new Function<Entry, Set<URI>>() {
            @Override
            public Set<URI> apply(Entry entry) {
                return null;
            }
        }).apply(new Entry("a"));
        System.out.println("compose = " + compose);
    }
}
