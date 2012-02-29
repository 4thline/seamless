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

package org.seamless.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class XmlPullParserUtils {

	final private static Logger log = Logger.getLogger(XmlPullParserUtils.class.getName());
	
	static XmlPullParserFactory xmlPullParserFactory;

	static {
		try {
			xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
		} catch (XmlPullParserException e) {
			log.severe("cannot create XmlPullParserFactory instance: " + e);
		}
	}

	static public XmlPullParser createParser(String xml) throws XmlPullParserException {
		XmlPullParser xpp = createParser();
		InputStream is;
		try {
			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new XmlPullParserException("UTF-8: unsupported encoding");
		}
		xpp.setInput(is, "UTF-8");
		return xpp;
	}
	
	static public XmlPullParser createParser() throws XmlPullParserException {
		if(xmlPullParserFactory == null) throw new XmlPullParserException("no XML Pull parser factory");
		return  xmlPullParserFactory.newPullParser();
	}
	
	static public XmlSerializer createSerializer() throws XmlPullParserException {
		if(xmlPullParserFactory == null) throw new XmlPullParserException("no XML Pull parser factory");
		return  xmlPullParserFactory.newSerializer();
	}
	
	static public void setSerializerIndentation(XmlSerializer serializer, int indent) {
		
		if(indent > 0) {
			try {
				serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",  true);
				//serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", StringUtils.repeat(' ', indent));
			} catch(Exception e) {
				log.warning("error setting feature of XmlSerializer: " + e);
			}

		}
	}
	
	public static void skipTag(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {
		int	event;
		do {
			event = xpp.next();
		} while(event != XmlPullParser.END_DOCUMENT && (event != XmlPullParser.END_TAG || !xpp.getName().equals(tag)));
	}
	
	static public void searchTag(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {
		
		int event;
		while((event = xpp.next()) != XmlPullParser.END_DOCUMENT) {
			if(event == XmlPullParser.START_TAG  && !xpp.getName().equals(tag)) return ;
		}
		
		throw new IOException(String.format("tag '%s' not found", tag));
	}


	public static void serializeIfNotNullOrEmpty(XmlSerializer serializer, String ns, String tag, String value) throws Exception {
		
		if(isNullOrEmpty(value)) return ; 

		serializer.startTag(ns, tag);
		serializer.text(value);
		serializer.endTag(ns, tag);
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static void serializeIfNotEqual(XmlSerializer serializer, String ns, String tag, Object value, Object forbiddenValue) throws Exception {
		if(value == null || value.equals(forbiddenValue)) return ;
		
		serializer.startTag(ns, tag);
		serializer.text(value.toString());
		serializer.endTag(ns, tag);
	}
	
	public static  String fixXMLEntities(String xml) {
		
    	StringBuilder fixedXml = new StringBuilder(xml.length());
    	
    	boolean isFixed = false;
    	
    	for(int i = 0; i < xml.length() ; i++) {
    		
    		char c = xml.charAt(i);
    		if(c == '&') {
    			// will not detect all possibly valid entities but should be sufficient for the purpose
    			String sub = xml.substring(i, Math.min(i+10, xml.length()));
    			if(!sub.startsWith("&#") && !sub.startsWith("&lt;") && !sub.startsWith("&gt;") && !sub.startsWith("&amp;") &&
    			   !sub.startsWith("&apos;") && !sub.startsWith("&quot;")) {
    				isFixed = true;
    				fixedXml.append("&amp;");	
    			} else {
    				fixedXml.append(c);
    			}
    		} else {
    			fixedXml.append(c);
    		}
    	}

    	if(isFixed) {
    		log.warning("fixed badly encoded entities in XML");
    	}
    	
    	return fixedXml.toString();
    }

	

}
