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
import java.util.ArrayList;
import java.util.List;

/** 
 * Provides a self-contained description of a Collada effect.
 * @author Job Zwiers
 */
public class Effect extends ColladaElement {
 
   // attributes: id, name, inherited from ColladaElement
 
   // child elements:
   private Asset asset;
   private ArrayList<Annotate> annotateList = new ArrayList<Annotate>();
   private ArrayList<ColladaImage> imageList= new ArrayList<ColladaImage>(); // images no longer allowed in Collada 1.5
   private ArrayList<Newparam> newparamList= new ArrayList<Newparam>();
   private ArrayList<ProfileCOMMON> profileCommonList= new ArrayList<ProfileCOMMON>();
   private ArrayList<ProfileCG> profileCGList= new ArrayList<ProfileCG>();
   private ArrayList<ProfileGLSL> profileGLSLList= new ArrayList<ProfileGLSL>();
   private ArrayList<Extra> extras = new ArrayList<Extra>();
   
   public Effect() {
      super();
   }
   
   public Effect(Collada collada, XMLTokenizer tokenizer) throws IOException {
      super(collada);    
      readXML(tokenizer); 
   }
   
   public List<Newparam> getNewparamList() { return newparamList; }
   
   public List<ProfileCOMMON> getProfileCOMMONList() { return profileCommonList; }
   public List<ProfileGLSL> getProfileGLSLList() { return profileGLSLList; }
   
 
   @Override
   public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt) {
      appendXMLStructure(buf, fmt, asset);
      appendXMLStructureList(buf, fmt, annotateList);
      appendXMLStructureList(buf, fmt, imageList);
      appendXMLStructureList(buf, fmt, newparamList);
      appendXMLStructureList(buf, fmt, profileCommonList);
      appendXMLStructureList(buf, fmt, profileCGList);
      appendXMLStructureList(buf, fmt, profileGLSLList);
      appendXMLStructureList(buf, fmt, extras);
      return buf;  
   }

   @Override
   public void decodeContent(XMLTokenizer tokenizer) throws IOException {
      while (tokenizer.atSTag()) {
         String tag = tokenizer.getTagName();
         if (tag.equals(Asset.xmlTag()))  {                
                 asset = new Asset(getCollada(), tokenizer);  
         } else if (tag.equals(Annotate.xmlTag()))  {                
                 annotateList.add(new Annotate(getCollada(), tokenizer)); 
         } else if (tag.equals(ColladaImage.xmlTag()))  {                
                 imageList.add(new ColladaImage(getCollada(), tokenizer)); 
         } else if (tag.equals(Newparam.xmlTag()))  {                
                 newparamList.add(new Newparam(getCollada(), tokenizer));  
         } else if (tag.equals(ProfileCOMMON.xmlTag()))  {                
                 profileCommonList.add(new ProfileCOMMON(getCollada(), tokenizer));  
         } else if (tag.equals(ProfileCG.xmlTag()))  {                
                 profileCGList.add(new ProfileCG(getCollada(), tokenizer));  
         } else if (tag.equals(ProfileGLSL.xmlTag()))  {                
                 profileGLSLList.add(new ProfileGLSL(getCollada(), tokenizer));
         } else if (tag.equals(Extra.xmlTag()))  {                
                 extras.add(new Extra(getCollada(), tokenizer)); 
         } else {         
            getCollada().warning(tokenizer.getErrorMessage("Effect: skip : " + tokenizer.getTagName()));
            tokenizer.skipTag();
         }
      }  
      addColladaNode(asset); 
      addColladaNodes(annotateList);
      addColladaNodes(imageList); 
      addColladaNodes(newparamList); 
      addColladaNodes(profileCommonList); 
      addColladaNodes(profileCGList); 
      addColladaNodes(profileGLSLList); 
      addColladaNodes(extras);          
   }

   /*
    * The XML Stag for XML encoding
    */
   private static final String XMLTAG = "effect";
 
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
