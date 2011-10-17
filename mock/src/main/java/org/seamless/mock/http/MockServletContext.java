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

/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, version 2 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.seamless.mock.http;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockServletContext implements ServletContext
{

   private Map<String, String> initParameters = new HashMap<String, String>();
   private Map<String, Object> attributes = new HashMap<String, Object>();
   
   private File webappRoot;
   private File webInfRoot;
   private File webInfClassesRoot;
   
   public MockServletContext()
   {
      try
      {
         URL webxml = getClass().getResource("/WEB-INF/web.xml");
         if (webxml != null)
         {
            webInfRoot = new File(webxml.toURI()).getParentFile();
            if (webInfRoot != null)
            {
               webInfClassesRoot = new File(webInfRoot.getParentFile().getPath() + "/classes");
               webappRoot = webInfRoot.getParentFile();
            }
            // call processing of context parameters
            processContextParameters(webxml);
         }
         else
         {
            webappRoot = new File(getClass().getResource("/.").toURI());
         }
      }
      catch (URISyntaxException e)
      {
          throw new RuntimeException(e);
      }
   }
   
   private void processContextParameters(URL webXML)
   {

       // TODO
/*
      try
      {
         Element root = XML.getRootElementSafely(webXML.openStream());         
         for (Element element : (List<Element>) root.elements("context-param"))
         {
            getInitParameters().put(element.elementText("param-name"), element.elementText("param-value"));
         }
      }
      catch (IOException e) 
      {
         throw new RuntimeException("Error parsing web.xml", e);
      }
      catch (DocumentException e)
      {
         throw new RuntimeException("Error parsing web.xml", e);
      }
*/
   }
   
   public Map<String, String> getInitParameters()
   {
      return initParameters;
   }
   
   public Map<String, Object> getAttributes()
   {
      return attributes;
   }
   
   public ServletContext getContext(String name)
   {
      return this;
   }
   
   public int getMajorVersion()
   {
      return 2;
   }
   
   public int getMinorVersion()
   {
      return 4;
   }

   public String getMimeType(String arg0)
   {
      return null;
   }
   
   public Set getResourcePaths(String name)
   {
      Enumeration<URL> enumeration = null;
      try
      {
         enumeration = getClass().getClassLoader().getResources("WEB-INF");
      }
      catch (IOException e)
      {
         throw new RuntimeException("Error finding webroot.", e);
      }
      Set<String> result = new HashSet<String>();
      while (enumeration.hasMoreElements())
      {
         URL url = enumeration.nextElement();
         File rootFile = new File(url.getPath()).getParentFile();
         File newFile = new File(rootFile.getPath() + name);
         File[] files = newFile.listFiles();
         if (files != null)
         {
            addPaths(result, files, rootFile.getPath());
         }
      }
      return result;
   }
   
   private static void addPaths(Set<String> result, File[] files, String rootPath)
   {
      for (File file : files)
      {
         String filePath = file.getPath().substring(rootPath.length()).replace('\\', '/');
         if (file.isDirectory())
         {
            result.add(filePath + "/");
         }
         else
         {
            result.add(filePath);
         }
      }
   }

   /**
    * Get the URL for a particular resource that is relative to the web app root
    * directory.
    * 
    * @param name The name of the resource to get
    * @return The resource, or null if resource not found
    * @throws MalformedURLException If the URL is invalid
    */
   public URL getResource(String name) throws MalformedURLException
   {
      File file = getFile(name, webappRoot);
      
      if (file == null)
      {
         file = getFile(name, webInfRoot);
      }
      
      if (file == null)
      {
         file = getFile(name, webInfClassesRoot);
      }
      
      if (file != null)
      {
         return file.toURI().toURL();
      }
      else
      {
         return null;
      }
   }

   private static File getFile(String name, File root)
   {
      if (root == null)
      {
         return null;
      }
      
      if (name.startsWith("/"))
      {
         name = name.substring(1);
      }
      
      File f = new File(root, name);
      if (!f.exists())
      {
         return null;
      }
      else
      {
         return f;
      }
   }

   public InputStream getResourceAsStream(String name)
   {
      return getClass().getResourceAsStream(name);
   }

   public RequestDispatcher getRequestDispatcher(String url)
   {
      throw new UnsupportedOperationException();
   }

   public RequestDispatcher getNamedDispatcher(String name)
   {
      throw new UnsupportedOperationException();
   }

   public Servlet getServlet(String name) throws ServletException
   {
      throw new UnsupportedOperationException();
   }

   public Enumeration getServlets()
   {
      return null;
   }

   public Enumeration getServletNames()
   {
      return null;
   }

   public void log(String msg)
   {

   }

   public void log(Exception ex, String msg)
   {

   }

   public void log(String msg, Throwable ex)
   {

   }

   public String getRealPath(String relativePath)
   {
      if (webappRoot != null)
      {
         return webappRoot.getAbsolutePath() + relativePath;
      }
      else
      {
         return relativePath;
      }
   }

   public String getServerInfo()
   {
      return null;
   }

   public String getInitParameter(String param)
   {
      return initParameters.get(param);
   }

   public Enumeration getInitParameterNames()
   {
      return new IteratorEnumeration(initParameters.keySet().iterator());
   }

   public Object getAttribute(String att)
   {
      return attributes.get(att);
   }

   public Enumeration getAttributeNames()
   {
      return new IteratorEnumeration(attributes.keySet().iterator());
   }

   public void setAttribute(String att, Object value)
   {
      if (value == null)
      {
         attributes.remove(value);
      }
      else
      {
         attributes.put(att, value);
      }
   }

   public void removeAttribute(String att)
   {
      attributes.remove(att);
   }

   public String getServletContextName()
   {
      return "Mock";
   }

   public String getContextPath()
   {
      return null;
   }

}
