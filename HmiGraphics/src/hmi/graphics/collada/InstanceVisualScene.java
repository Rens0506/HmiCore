/*******************************************************************************
 * Copyright (C) 2009 Human Media Interaction, University of Twente, the Netherlands
 * 
 * This file is part of the Elckerlyc BML realizer.
 * 
 * Elckerlyc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Elckerlyc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Elckerlyc.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/

package hmi.graphics.collada;

import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

/** 
 * @author Job Zwiers 
 */
public class InstanceVisualScene extends ColladaElement {
 
   // attributes:
   private String url;
   
   
   public InstanceVisualScene() {
      super();
   }
   
   public InstanceVisualScene(Collada collada, XMLTokenizer tokenizer) throws IOException {
      super(collada);      
      readXML(tokenizer); 
   }
 
   /**
    * returns the url
    */
   public String getURL() {
      return url;
   }
    
   /**
    * returns false, to denote that Params are encoded by means of empty XML elements
    */
   @Override
   public boolean hasContent() { return false; }

   /**
    * appends a String of attributes to buf.
    */
   @Override
   public StringBuilder appendAttributes(StringBuilder buf) {
      super.appendAttributes(buf);
      appendAttribute(buf, "url", url);
      return buf;
   }



   /**
    * decodes the XML attributes
    */
   @Override
   public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer) {      
      url = getRequiredAttribute("url", attrMap, tokenizer);
      super.decodeAttributes(attrMap, tokenizer);
   }
 
   /*
    * The XML Stag for XML encoding
    */
   private static final String XMLTAG = "instance_visual_scene";
 
   /**
    * The XML Stag for XML encoding
    */
   public static String xmlTag() { return XMLTAG; }
 
   /**
    * returns the XML Stag for XML encoding
    */
   @Override
   public String getXMLTag() {
      return XMLTAG;
   }

}
