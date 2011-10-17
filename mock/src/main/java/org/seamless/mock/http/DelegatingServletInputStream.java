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


import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Delegating implementation of {@link javax.servlet.ServletInputStream}.
 * <p/>
 * <p>Used by {@link MockHttpServletRequest}; typically not directly
 * used for testing application controllers.
 *
 * @author Juergen Hoeller
 * @see MockHttpServletRequest
 */
public class DelegatingServletInputStream extends ServletInputStream
{

   private final InputStream sourceStream;


   /**
    * Create a DelegatingServletInputStream for the given source stream.
    *
    * @param sourceStream the source stream (never <code>null</code>)
    */
   public DelegatingServletInputStream(InputStream sourceStream)
   {
      this.sourceStream = sourceStream;
   }

   /**
    * Return the underlying source stream (never <code>null</code>).
    */
   public final InputStream getSourceStream()
   {
      return this.sourceStream;
   }


   public int read() throws IOException
   {
      return this.sourceStream.read();
   }

   public void close() throws IOException
   {
      super.close();
      this.sourceStream.close();
	}

}