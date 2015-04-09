package org.seamless.test.javadoc;

import org.seamless.javadoc.EasyDoclet;
import org.testng.annotations.Test;

import java.io.File;

/**
 * @author Sebastian Roth
 */
public class EasyDocletTest {

    @Test
    public void invokeDoclet() {
        // Should not raise any exception.
        final com.sun.javadoc.RootDoc rootDoc = new EasyDoclet(
                "en_US",
                new File[]{new File("src/test/java")},
                new String[]{"org.seamless.test.javadoc"},
                new File[0]
        ).getRootDoc();
    }
}
