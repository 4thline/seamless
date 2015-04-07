/*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
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

package org.seamless.cdi.weld;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.environment.se.discovery.AbstractWeldSEDeployment;
import org.jboss.weld.environment.se.discovery.ImmutableBeanDeploymentArchive;
import org.jboss.weld.environment.se.discovery.url.ClasspathScanningException;
import org.jboss.weld.resources.spi.ResourceLoader;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Alternative deployment scanner for Weld SE packaging.
 * <p>
 * This <code>Deployment</code> knows how to handle One-JAR packaged applications with
 * nested JAR files (http://one-jar.sourceforge.net). It searches for
 * <code>META-INF/beans.xml</code> marker files in directories and JAR files.
 * </p>
 *
 * @author Christian Bauer
 */
public class SeamlessWeldSEDeployment extends AbstractWeldSEDeployment {

    final private static Logger log = Logger.getLogger(SeamlessWeldSEDeployment.class.getName());

    protected final BeanDeploymentArchive beanDeploymentArchive;

    public SeamlessWeldSEDeployment(ResourceLoader resourceLoader, Bootstrap bootstrap) {
        super(bootstrap);
        this.beanDeploymentArchive = createBeanDeploymentArchive(resourceLoader);
    }

    public List<BeanDeploymentArchive> getBeanDeploymentArchives() {
        return Collections.singletonList(beanDeploymentArchive);
    }

    public BeanDeploymentArchive loadBeanDeploymentArchive(Class<?> beanClass) {
        return beanDeploymentArchive;
    }

    protected BeanDeploymentArchive createBeanDeploymentArchive(ResourceLoader resourceLoader) {
        List<String> discoveredClasses = new ArrayList<String>();
        List<URL> discoveredBeansXmlUrls = new ArrayList<URL>();

        discoverResources(resourceLoader, discoveredClasses, discoveredBeansXmlUrls);

        if (log.isLoggable(Level.FINEST)) {
            for (String discoveredClass : discoveredClasses) {
                log.finest("Discovered class: " + discoveredClass);
            }
            for (URL discoveredBeansXmlUrl : discoveredBeansXmlUrls) {
                log.finest("Discovered beans.xml URL: " + discoveredBeansXmlUrl);
            }
        }

        BeanDeploymentArchive archive =
            new ImmutableBeanDeploymentArchive(
                "classpath",
                discoveredClasses,
                parseBeanXmlUrls(discoveredBeansXmlUrls)
            );

        archive.getServices().add(ResourceLoader.class, resourceLoader);

        return archive;
    }

    protected void discoverResources(ResourceLoader resourceLoader,
                                     List<String> discoveredClasses,
                                     List<URL> discoveredBeanXmlUrls) {

        URL oneJar;
        if ((oneJar = resourceLoader.getResource("OneJar.class")) != null) {
            log.info("Scanning One-JAR archive for nested JARs: " + oneJar);
            String oneJarPath = getDecodedPath(oneJar);
            File archiveFile = new File(URI.create(oneJarPath.substring(0, oneJarPath.indexOf("!"))));
            discoverResourcesInArchive(archiveFile, discoveredClasses, discoveredBeanXmlUrls);

        } else {

            Collection<URL> resources = resourceLoader.getResources(BEANS_XML);
            for (URL resourceURL : resources) {
                log.info("Found beans.xml marker in: " + resourceURL);
                String resourcePath = getDecodedPath(resourceURL);

                if ("file".equals(resourceURL.getProtocol())) {

                    File resourceDirectory =
                        new File(resourcePath.substring(0, resourcePath.length() - BEANS_XML.length()));
                    discoverClassesInDirectory(resourceDirectory.toURI(), resourceDirectory, discoveredClasses);
                    discoveredBeanXmlUrls.add(resourceURL);

                } else if ("jar".equals(resourceURL.getProtocol())) {

                    File archiveFile =
                        new File(URI.create(resourcePath.substring(0, resourcePath.indexOf("!")).replace(" ", "%20")));
                    discoverResourcesInArchive(archiveFile, discoveredClasses, discoveredBeanXmlUrls);

                } else {
                    throw new ClasspathScanningException(
                        "Don't know how to scan: " + resourceURL
                    );
                }
            }
        }
    }

    protected void discoverResourcesInArchive(File archiveFile,
                                              List<String> discoveredClasses,
                                              List<URL> discoveredBeanXmlUrls) {
        log.fine("Scanning archive file for resource: " + archiveFile);

        if (!archiveFile.exists())
            throw new ClasspathScanningException("Can't scan archive file: " + archiveFile);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(archiveFile);

            String archivePath = archiveFile.getPath();

            // Special handling of Windows paths
            archivePath = archivePath.replaceAll("\\\\", "/");
            if (!archivePath.startsWith("/"))
                archivePath = "/" + archivePath;

            discoverResourcesInArchive(
                archivePath,
                inputStream,
                discoveredClasses,
                discoveredBeanXmlUrls
            );
        } catch (Exception ex) {
            throw new RuntimeException("Error scanning archive: " + archiveFile, ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException cEx) {
                    // Ignore
                }
            }
        }
    }

    protected void discoverResourcesInArchive(String path,
                                              InputStream inputStream,
                                              List<String> discoveredClasses,
                                              List<URL> discoveredBeanXmlUrls) throws Exception {

        log.fine("Checking entries of archive: " + path);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry;
        List<String> classesInArchive = new ArrayList<String>();
        boolean archiveHasBeansXml = false;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            String entryPath = path + "!/" + entry.getName();

            if (entry.getName().endsWith(".class")) {
                classesInArchive.add(toClassName(entry.getName()));
            } else if (entry.getName().endsWith(BEANS_XML)) {
                archiveHasBeansXml = true;
                discoveredBeanXmlUrls.add(URI.create("jar:file:" + entryPath.replace(" ", "%20")).toURL());
            } else if (isArchive(entry.getName())) {
                discoverResourcesInArchive(entryPath, zipInputStream, discoveredClasses, discoveredBeanXmlUrls);
            }

            zipInputStream.closeEntry();
        }
        if (archiveHasBeansXml)
            discoveredClasses.addAll(classesInArchive);
    }

    protected void discoverClassesInDirectory(URI root, File directory, List<String> discoveredClasses) {
        log.fine("Discovering classes in directory: " + directory);
        if (directory == null || !directory.exists()) {
            log.severe("Directory not accessible: " + directory);
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            log.fine("No files in directory: " + directory);
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                discoverClassesInDirectory(root, file, discoveredClasses);
            } else if (file.getName().endsWith(".class")) {
                String filePath = root.relativize(file.toURI()).toString();
                discoveredClasses.add(toClassName(filePath));
            }
        }
    }

    protected String getDecodedPath(URL url) {
        try {
            return URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String toClassName(String path) {
        // Turn the file path into a fully qualified class name
        return path
                .substring(0, path.lastIndexOf(".class"))
                .replace('\\', '.').replace('/', '.');
    }

    protected boolean isArchive(String name) {
        return name.endsWith(".jar")
            || name.endsWith(".zip")
            || name.endsWith(".war")
            || name.endsWith(".ear");
    }

    /**
     * Uses the <code>SeamlessBeansXmlParser</code> by default.
     */
    protected BeansXml parseBeanXmlUrls(List<URL> beanXmlUrls) {
        return new SeamlessBeansXmlParser().parse(beanXmlUrls);
    }

}
