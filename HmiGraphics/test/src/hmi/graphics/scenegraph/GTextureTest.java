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
/*
 * GTexture JUnit test
 */

package hmi.graphics.scenegraph;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import hmi.util.*;
import java.io.*;
import hmi.xml.*;

/**
 * JUnit test for hmi.graphics.scenegraph.GTexture
 */
public class GTextureTest {
    
    public GTextureTest() {
    }

    @Before
    public void setUp()  { // common initialization, executed for every test.
    }

    @After
    public void tearDown() {
    }

    @Test
    public void basics() {       
       GTexture gtex = new GTexture();
       assertTrue(gtex.getImageFileName() == "");
    } 

    @Test
    public void xmlTest0() throws IOException {       
       Resources res = new Resources("scenegraph");
       Reader reader = res.getReader("gtexture0.xml");
       XMLTokenizer tokenizer = new XMLTokenizer(reader);
       try {
          GTexture gtex1 = new GTexture(tokenizer);
          assertTrue(false); // we expect an XMLScanException because of the missing imageFileName attribute
       } catch (XMLScanException e) {
          // we expect to arrive here
       }
    } 
  
    @Test
    public void xmlTest1() throws IOException {       
       Resources res = new Resources("scenegraph");
       Reader reader = res.getReader("gtexture1.xml");
       XMLTokenizer tokenizer = new XMLTokenizer(reader);
       GTexture gtex1 = new GTexture(tokenizer);
       String  encoding = gtex1.toXMLString();
       //System.out.println("gtex1:\n" + encoding);
       XMLTokenizer tokenizer2 = new XMLTokenizer(encoding);
       GTexture gtex2 = new GTexture(tokenizer2);
       //System.out.println("gtexture decoded:\n" + gtex2);
       assertTrue(gtex2.getImageFileName() == "visage_2008_low.jpg");
    } 
    
    @Test
    public void xmlTest2() throws IOException {       
       Resources res = new Resources("scenegraph");
       Reader reader = res.getReader("gtexture2.xml");
       XMLTokenizer tokenizer = new XMLTokenizer(reader);
       GTexture gtex1 = new GTexture(tokenizer);
       String  encoding = gtex1.toXMLString();
      // System.out.println("gtex1:\n" + encoding);
       XMLTokenizer tokenizer2 = new XMLTokenizer(encoding);
       GTexture gtex2 = new GTexture(tokenizer2);
      // System.out.println("gtexture decoded:\n" + gtex2);
       assertTrue(gtex2.getImageFileName() == "visage_2008_low.jpg");
       assertTrue(gtex2.getWrapS().equals("CLAMP_TO_EDGE"));
       assertTrue(gtex2.getWrapT().equals("REPEAT"));
       assertTrue(gtex2.getWrapR().equals("REPEAT")); // default value
       assertTrue(gtex2.getRepeatS() == 2.0f);
       assertTrue(gtex2.getRepeatT() == 3.0f);
       assertTrue(gtex2.getRepeatR() == 1.0f);
       assertTrue(gtex2.getOffsetS() == 0.5f);
       assertTrue(gtex2.getOffsetT() == 0.0f);
       assertTrue(gtex2.getOffsetR() == 0.0f);
    } 
  
  
    @Test
    public void binaryTest() throws IOException {
       Resources res = new Resources("scenegraph");
       Reader reader = res.getReader("gtexture2.xml");
       XMLTokenizer tokenizer = new XMLTokenizer(reader);
       GTexture gtex1 = new GTexture(tokenizer);
    
       String tmpdir = System.getProperty("java.io.tmpdir");
     
    //   System.out.println("userdir=" + userdir + " tmpdir=" + tmpdir);
       DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(tmpdir+"/gtexbintest.dat")));
       gtex1.writeBinary(dataOut);
       dataOut.close();
       DataInputStream dataIn = new DataInputStream(new BufferedInputStream(new FileInputStream(tmpdir+"/gtexbintest.dat")));
       GTexture gtex2 = new GTexture();
       gtex2.readBinary(dataIn);
       dataIn.close();
       
     
       //System.out.println("binaryDecoded: " + gtex2);
       assertTrue(gtex2.getImageFileName() == "visage_2008_low.jpg");
       assertTrue(gtex2.getWrapS().equals("CLAMP_TO_EDGE"));
       assertTrue(gtex2.getWrapT().equals("REPEAT"));
       assertTrue(gtex2.getWrapR().equals("REPEAT")); // default value
       assertTrue(gtex2.getRepeatS() == 2.0f);
       assertTrue(gtex2.getRepeatT() == 3.0f);
       assertTrue(gtex2.getRepeatR() == 1.0f);
       assertTrue(gtex2.getOffsetS() == 0.5f);
       assertTrue(gtex2.getOffsetT() == 0.0f);
       assertTrue(gtex2.getOffsetR() == 0.0f);
       
       
    }
  
}
