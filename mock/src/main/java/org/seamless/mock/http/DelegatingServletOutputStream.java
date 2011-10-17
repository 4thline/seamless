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


import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Delegating implementation of {@link javax.servlet.ServletOutputStream}.
 * <p/>
 * <p>Used by {@link MockHttpServletResponse}; typically not directly
 * used for testing application controllers.
 *
 * @author Juergen Hoeller
 * @see MockHttpServletResponse
 */
public class DelegatingServletOutputStream extends ServletOutputStream
{

   private final OutputStream targetStream;


   /**
    * Create a DelegatingServletOutputStream for the given target stream.
    *
    * @param targetStream the target stream (never <code>null</code>)
    */
   public DelegatingServletOutputStream(OutputStream targetStream)
   {
      this.targetStream = targetStream;
   }

   /**
    * Return the underlying target stream (never <code>null</code>).
    */
   public final OutputStream getTargetStream()
   {
      return this.targetStream;
   }


   public void write(int b) throws IOException
   {
      this.targetStream.write(b);
   }

   public void flush() throws IOException
   {
      super.flush();
      this.targetStream.flush();
   }

   public void close() throws IOException
   {
      super.close();
      this.targetStream.close();
   }

}