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

/** 
 * Generic Collada color specifications, like <color> 0.2 0.5 0.7 1.0 </color>
 * @author Job Zwiers
 */
public class CommonColor extends ColladaFloatVector {

   // attribute vec inherited from ColladaFloatVector; it will contain the rgba color
   
   public CommonColor() {
      super();
      setVec(new float[] {0.0f, 0.0f, 0.0f, 1.0f});
   }
   
   public CommonColor(Collada collada, XMLTokenizer tokenizer) throws IOException {
      this();
      readXML(tokenizer); 
   }
 
   /*
    * The XML Stag for XML encoding
    */
   private static final String XMLTAG = "color";
 
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
