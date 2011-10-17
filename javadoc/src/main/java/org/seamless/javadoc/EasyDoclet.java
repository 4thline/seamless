/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.javadoc;

import com.sun.javadoc.RootDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import com.sun.tools.javadoc.ModifierFilter;
import com.sun.tools.javadoc.PublicMessager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Alternative and easy to use bootstrap/start class for working with Javadoc.
 * <p>
 * Requires the <tt>com.sun.tools.javadoc.PublicMessager</tt> override to be in the
 * classpath!
 * </p>
 *
 * @author Christian Bauer
 */
public class EasyDoclet {

    final private Logger log = Logger.getLogger(EasyDoclet.class.getName());

    final private String[] packageNames;
    final private File[] fileNames;
    final private RootDoc rootDoc;

    public EasyDoclet(File sourceDirectory, Collection<String> packageNames) {
        this("", sourceDirectory, packageNames.toArray(new String[packageNames.size()]), new File[0]);
    }

    public EasyDoclet(String locale, File sourceDirectory, Collection<String> packageNames) {
        this(locale, sourceDirectory, packageNames.toArray(new String[packageNames.size()]), new File[0]);
    }

    public EasyDoclet(File sourceDirectory, String... packageNames) {
        this("", sourceDirectory, packageNames, new File[0]);
    }

    public EasyDoclet(String locale, File sourceDirectory, String... packageNames) {
        this(locale, sourceDirectory, packageNames, new File[0]);
    }

    public EasyDoclet(File sourceDirectory, File... fileNames) {
        this("", sourceDirectory, new String[0], fileNames);
    }

    public EasyDoclet(String locale, File sourceDirectory, File... fileNames) {
        this(locale, sourceDirectory, new String[0], fileNames);
    }

    public EasyDoclet(File sourceDirectory, String[] packageNames, File[] fileNames) {
        this("", sourceDirectory, packageNames, fileNames);
    }

    public EasyDoclet(String locale, File sourceDirectory, String[] packageNames, File[] fileNames) {
        this(locale, new File[] {sourceDirectory}, packageNames, fileNames);
    }

    public EasyDoclet(String locale, File[] sourceDirectories, String[] packageNames, File[] fileNames) {
        this.packageNames = packageNames;
        this.fileNames = fileNames;

        Context context = new Context();
        Options compOpts = Options.instance(context);

        StringBuilder sb = new StringBuilder();
        for (File sourceDirectory : sourceDirectories) {
            sb.append(sourceDirectory.getAbsolutePath());
            sb.append(File.pathSeparatorChar);
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length()-1);

        log.fine("Using source path: " + sb.toString());
        compOpts.put("-sourcepath", sb.toString());

        ListBuffer<String> javaNames = new ListBuffer();
        for (File fileName : fileNames) {
            log.fine("Adding file to documentation path: " + fileName.getAbsolutePath());
            javaNames.append(fileName.getPath());
        }

        ListBuffer<String> subPackages = new ListBuffer();
        for (String packageName : packageNames) {
            log.fine("Adding sub-packages to documentation path: " + packageName);
            subPackages.append(packageName);
        }

        new PublicMessager(
                context,
                getApplicationName(),
                new PrintWriter(new LogWriter(Level.SEVERE), true),
                new PrintWriter(new LogWriter(Level.WARNING), true),
                new PrintWriter(new LogWriter(Level.FINE), true)
        );

        JavadocTool javadocTool = JavadocTool.make0(context);

        try {
            rootDoc = javadocTool.getRootDocImpl(
                    locale,
                    null,
                    new ModifierFilter(ModifierFilter.ALL_ACCESS),
                    javaNames.toList(),
                    new ListBuffer<String[]>().toList(),
                    false,
                    subPackages.toList(),
                    new ListBuffer<String>().toList(),
                    false,
                    false,
                    false);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        if (log.isLoggable(Level.FINEST)) {
            for (ClassDoc classDoc : getRootDoc().classes()) {
                log.finest("Parsed Javadoc class source: " + classDoc.position() + " with inline tags: " + classDoc.inlineTags().length );
            }
        }
    }

    public String[] getPackageNames() {
        return packageNames;
    }

    public File[] getFileNames() {
        return fileNames;
    }

    public RootDoc getRootDoc() {
        return rootDoc;
    }

    // Logging abstraction! Hurray!
    protected class LogWriter extends Writer {

        Level level;

        public LogWriter(Level level) {
            this.level = level;
        }

        public void write(char[] chars, int offset, int length) throws IOException {
            String s = new String(Arrays.copyOf(chars, length));
            if (!s.equals("\n") && !s.equals("\r") && !s.equals(" "))
                log.log(level, s);
        }

        public void flush() throws IOException {}
        public void close() throws IOException {}
    }

    protected String getApplicationName() {
        return getClass().getSimpleName() + " Application";
    }

}
