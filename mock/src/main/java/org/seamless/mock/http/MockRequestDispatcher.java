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
package org.seamless.mock.http;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mock implementation of the {@link javax.servlet.RequestDispatcher} interface.
 * <p/>
 * <p>Used for testing the web framework; typically not necessary for
 * testing application controllers.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
public class MockRequestDispatcher implements RequestDispatcher
{

    final private Logger log = Logger.getLogger(MockRequestDispatcher.class.getName());

   private final String url;


   /**
    * Create a new MockRequestDispatcher for the given URL.
    *
    * @param url the URL to dispatch to.
    */
   public MockRequestDispatcher(String url)
   {
      this.url = url;
   }


   public void forward(ServletRequest request, ServletResponse response)
   {
      if (response.isCommitted())
      {
         throw new IllegalStateException("Cannot perform forward - response is already committed");
      }
      getMockHttpServletResponse(response).setForwardedUrl(this.url);
      if (log.isLoggable(Level.FINE))
      {
         log.fine("MockRequestDispatcher: forwarding to URL [" + this.url + "]");
      }
   }

   public void include(ServletRequest request, ServletResponse response)
   {
      getMockHttpServletResponse(response).setIncludedUrl(this.url);
      if (log.isLoggable(Level.FINE))
      {
         log.fine("MockRequestDispatcher: including URL [" + this.url + "]");
      }
   }

   /**
    * Obtain the underlying EnhancedMockHttpServletResponse,
    * unwrapping {@link javax.servlet.http.HttpServletResponseWrapper} decorators if necessary.
    */
   protected MockHttpServletResponse getMockHttpServletResponse(ServletResponse response)
   {
      if (response instanceof MockHttpServletResponse)
      {
         return (MockHttpServletResponse) response;
      }
      if (response instanceof HttpServletResponseWrapper)
      {
         return getMockHttpServletResponse(((HttpServletResponseWrapper) response).getResponse());
      }
      throw new IllegalArgumentException("MockRequestDispatcher requires MockHttpServletResponse");
	}

}