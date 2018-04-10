package org.boudnik.framework.pocs;

import org.junit.Before;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Alexandre_Boudnik
 * @since 04/10/18 12:46
 */
public class IntrospectorTest {

    private BeanInfo beanInfo;

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class Bean<T> {
        private T foo;

        public T getFoo() {
            return foo;
        }

        public void setFoo(T foo) {
            this.foo = foo;
        }
    }

    private Bean<String> b1 = new Bean<>();
    private Bean<String> b2 = new Bean<>();
    private Bean<String> b3 = new Bean<>();

    @Before
    public void setUp() throws IntrospectionException {
        b1.setFoo("b1");
        b2.setFoo("b2");
        b3.setFoo("b1");
        beanInfo = Introspector.getBeanInfo(Bean.class);
    }

    @Test
    public void same() throws InvocationTargetException, IllegalAccessException {
        assertTrue(org.boudnik.framework.Introspector.isEquals(beanInfo, b1, b3));
    }

    @Test
    public void reflection() throws InvocationTargetException, IllegalAccessException {
        assertTrue(org.boudnik.framework.Introspector.isEquals(beanInfo, b1, b1));
    }

    @Test
    public void different() throws InvocationTargetException, IllegalAccessException {
        assertFalse(org.boudnik.framework.Introspector.isEquals(beanInfo, b1, b2));
    }

    @Test
    public void set() throws InvocationTargetException, IllegalAccessException {
        org.boudnik.framework.Introspector.set(beanInfo, b1, b2);
        assertTrue(org.boudnik.framework.Introspector.isEquals(beanInfo, b1, b2));
    }

}
