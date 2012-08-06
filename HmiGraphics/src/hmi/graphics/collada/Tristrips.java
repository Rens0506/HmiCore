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

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;

/** 
 * Declares the binding of geometric primitives and vertex attributes for a mesh element.
 * @author Job Zwiers
 */
public class Tristrips extends PrimitiveMeshElement {
    
   private ArrayList<P> plist = new ArrayList<P>();
    
   /**
    * Default constructor
    */     
   public Tristrips() {
      super();
      setMeshType(Mesh.MeshType.Tristrips);
   }

   /**
    * Constructor used to create a PolyList Object from XML
    */      
   public Tristrips(Collada collada, XMLTokenizer tokenizer) throws IOException {
      super(collada); 
      readXML(tokenizer); 
      setMeshType(Mesh.MeshType.Tristrips);
   }
 
   @Override
   public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt) {
      appendXMLStructureList(buf, fmt, getInputs());
      appendXMLStructureList(buf, fmt, plist);
      appendXMLStructureList(buf, fmt, getExtras());
      return buf;  
   }

   @Override
   public void decodeContent(XMLTokenizer tokenizer) throws IOException {
      while (tokenizer.atSTag()) {
         String tag = tokenizer.getTagName();
         if (tag.equals(Input.xmlTag()))  {   
             Input inp = new Input(getCollada(), tokenizer);         
             getInputs().add(inp);   
             if (inp.getOffset() > getMaxOffset()) setMaxOffset(inp.getOffset()); 
         } else if (tag.equals(Extra.xmlTag()))  {                
             getExtras().add(new Extra(getCollada(), tokenizer));   
         } else if (tag.equals(P.xmlTag()))  {                
             plist.add(new P(getCollada(), tokenizer));  
         } else {         
            getCollada().warning(tokenizer.getErrorMessage("Tristrips: skip : " + tokenizer.getTagName()));
            tokenizer.skipTag();
         }
      }    
      addColladaNodes(getInputs());
      addColladaNodes(plist);  
      addColladaNodes(getExtras());      
   }
 
 
   /*
    * The XML Stag for XML encoding
    */
   private static final String XMLTAG = "tristrips";
 
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
