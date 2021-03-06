/*******************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2015 University of Twente
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/

package hmi.graphics.collada;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

/** 
 * Storage of graphical representations, like raster data.
 * @author Job Zwiers
 */
public class InitFrom extends ColladaElement {
 
   private String imageRef; // ref to image (for init_from in surface, or  file name (for init_from in image)
   private int mip = 0;     // default miplevel, for surface only
   private int slice = 0;   // default slice, used for 3D textures for surface only
   private String face = null;// default: "POSITIVE_X"; used for cube maps for surface only
  
 
   public InitFrom() {
      super();
   }
   
   public InitFrom(Collada collada, XMLTokenizer tokenizer) throws IOException {
      super(collada);
      readXML(tokenizer); 
   }
 
   public String getImageId() {
      return imageRef;
   }
 
   public String getImageFile() {
      return imageRef;
   }
   
   public int getMip() {
      return mip;
   }
   
   public int getSlice() {
      return slice;
   }
 
   public String getFace() {
      return face;
   }
       
   /**
    * appends the XML content
    */
   @Override
   public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt) {  
      appendNewLine(buf);
      appendSpaces(buf, fmt);
      appendTab(buf);
      buf.append(imageRef);
      return buf;
   }

   /**
    * decodes the XML content
    */
   @Override
   public void decodeContent(XMLTokenizer tokenizer) throws IOException {
      imageRef = tokenizer.takeCharData().trim();
   }
   
   /**
    * appends a String of attributes to buf.
    */
@Override
   public StringBuilder appendAttributes(StringBuilder buf) {
      super.appendAttributes(buf);
      if (mip > 0) appendAttribute(buf, "mip", mip);
      if (slice > 0)  appendAttribute(buf, "slice", slice);
      if (face != null) appendAttribute(buf, "face", face);  
      return buf;
   }


   /**
    * decodes the XML attributes
    */
   @Override
   public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer) {
      mip = getOptionalIntAttribute("height", attrMap, 0);
      slice = getOptionalIntAttribute("width", attrMap, 0);
      face = getOptionalAttribute("face", attrMap);
      super.decodeAttributes(attrMap, tokenizer);
   }

   /*
    * The XML Stag for XML encoding
    */
   private static final String XMLTAG = "init_from";
 
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
