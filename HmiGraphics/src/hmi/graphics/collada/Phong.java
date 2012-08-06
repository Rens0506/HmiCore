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
 * Phong shader
 * @author Job Zwiers
 */
public class Phong extends FixedFunctionShader {
 
   
   
   public Phong() {
      super(ShaderType.Phong);
   }
   
   public Phong(Collada collada) {
      super(collada, ShaderType.Phong); 
   }
   
   public Phong(Collada collada, XMLTokenizer tokenizer) throws IOException {
      super(collada, ShaderType.Phong); 
      readXML(tokenizer);       
   }
  
   /*
    * The XML Stag for XML encoding
    */
   private static final String XMLTAG = "phong";
 
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
