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
 * Skeleton JUnit test
 */

package hmi.animation;

import static hmi.testutil.math.Quat4fTestUtil.assertQuat4fEquals;
import static org.junit.Assert.assertTrue;
import hmi.math.Quat4f;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for hmi.animation.Skeleton
 */
public class SkeletonTest {
    
    VJoint vj0;
    VJoint vj00;
    VJoint vj01;
    VJoint vj010;
    VJoint vj011;
    VJoint vj012;
    VJoint vj0110;
    List<String> sidsInorder;
    List<String> sidsSubset;
    List<String> sidsOutOfOrder;
    private static final float PRECISION = 0.0001f;
   
    public Skeleton createSkeleton(String id)
    {
        Skeleton skel = new Skeleton(id);
        skel.addRoot(vj0);
        return skel;
      
    }
   
    @Before
    public void init()
    {
       sidsInorder = Arrays.asList(new String[] {"root", "lhip", "rhip", "rknee0", "rknee1", "foot", "rknee2"}); 
       sidsOutOfOrder = Arrays.asList(new String[] {"root", "rhip",  "lhip", "rknee0", "rknee1", "rknee2", "foot"}); 
       sidsSubset = Arrays.asList(new String[] {"root", "lhip", "rhip"}); 
       vj0 = new VJoint("vj0", "root");
       vj00 = new VJoint("vj00", "lhip" );
       vj01 = new VJoint("vj01", "rhip");
       vj010 = new VJoint("vj010", "rknee0");
       vj011 = new VJoint("vj011", "rknee1");
       vj012 = new VJoint("vj012", "rknee2");
       vj0110 = new VJoint("vj0110", "rfoot");    
       
       /*
        *                       vj0[root]
        *                       /      \
        *               vj00[lhip]      vj01[rhip]
        *                             /      |         \
        *                vj010[rknee0]   vj011[rknee1]  vj012[rknee2]          
        *                                    |
        *                                vj0110[foot]
        */
    
       vj0.addChild(vj00);        
       vj0.addChild(vj01);        
                                 
       vj01.addChild(vj010);      
       vj01.addChild(vj011);      
       vj01.addChild(vj012);      
                                 
       vj011.addChild(vj0110);    
       initTranslations();
       initRotations();
       initScales();
    }
    
    
    public void initTranslations()
    {
      
       float[] t0    = new float[]{0.0f,   1.0f,  0.0f};
       float[] t00   = new float[]{-0.1f, 1.2f,  0.3f}; 
       float[] t01   = new float[]{0.1f,  1.2f,  0.3f}; 
       float[] t010  = new float[]{0.10f, 0.5f,  0.010f};
       float[] t011  = new float[]{0.15f, 0.5f,  0.011f};
       float[] t012  = new float[]{0.2f,  0.5f,  0.012f};
       float[] t0110 = new float[]{0.17f, 0.27f, 0.37f};
         
       vj0.setTranslation(t0);  
       vj00.setTranslation(t00);
       vj01.setTranslation(t01);
       
       vj010.setTranslation(t010);
       vj011.setTranslation(t011);
       vj012.setTranslation(t012);
       vj0110.setTranslation(t0110);
    }
    
    
    public void initRotations()
    {
       /* not unit quaternions, but ok for the test */
       float[] q0    = new float[]{0.5f,   0.5f,  0.5f, 0.5f};
       float[] q00   = new float[]{0.7f,   0.1f,  0.2f, 0.3f}; 
       float[] q01   = new float[]{0.6f,   0.2f,  0.2f, 0.3f};  
       float[] q010  = new float[]{0.5f,   0.3f,  0.2f, 0.3f}; 
       float[] q011  = new float[]{0.4f,   0.4f,  0.2f, 0.3f}; 
       float[] q012  = new float[]{0.3f,   0.5f,  0.2f, 0.3f}; 
       float[] q0110 = new float[]{0.2f,   0.6f,  0.2f, 0.3f}; 
         
       vj0.setRotation(q0);  
       vj00.setRotation(q00);
       vj01.setRotation(q01);
       
       vj010.setRotation(q010);
       vj011.setRotation(q011);
       vj012.setRotation(q012);
       vj0110.setRotation(q0110);
    }
    
    
    public void initScales()
    {
       float[] s0    = new float[]{1.0f,   2.0f,  3.0f};
       float[] s00   = new float[]{0.4f, 0.4f,  0.4f}; 
       float[] s01   = new float[]{100f,  100f,  100f}; 
       vj0.setScale(s0);  
       vj00.setScale(s00);
       vj01.setScale(s01);
    }
    
    
    public SkeletonTest() {
    }

    @Before
    public void setUp()  { // common initialization, executed for every test.
    }

    @After
    public void tearDown() {
    }

    @Test
    public void basics() {       
        Skeleton skel1 = new Skeleton((String)null);
        assertTrue(skel1.getId() == "");
        Skeleton skel2 = new Skeleton("skel2");
        assertTrue(skel2.getId() == "skel2"); // ok, since id is interned
        
        VJoint root2 = new VJoint("Skel2-Root", "root");
        skel2.addRoot(root2);
        assertTrue(skel2.getRoot() == root2);
        assertTrue(skel2.getRoot().getId() == "Skel2-Root");
        assertTrue(skel2.getRoot().getSid() == "root");
 
        
    } 
    
    private void assertIdentityRotation(VJoint vj)
    {
        float q[]=Quat4f.getQuat4f();
        vj.getRotation(q);
        assertQuat4fEquals(Quat4f.getIdentity(),q, PRECISION);
    }
    
    @Test
    public void testSetNeutralPose()
    {
        Skeleton skel = new Skeleton("",vj0);
        skel.setNeutralPose();
        assertIdentityRotation(vj0);
        assertIdentityRotation(vj00);
        assertIdentityRotation(vj01);
        assertIdentityRotation(vj010);
        assertIdentityRotation(vj012);
        assertIdentityRotation(vj0110);        
    }

  
    @Test
    public void testXMLBasics() throws IOException 
    {
        Skeleton skel = new Skeleton("skel1");
        String encoded = skel.toXMLString();
        System.out.println("Skeleton encoded:\n" + encoded);
        XMLTokenizer tokenizer = new XMLTokenizer(encoded);
        Skeleton skelDecoded = new Skeleton(tokenizer);
        String decoded = skelDecoded.toXMLString();
        System.out.println("Skeleton decoded:\n" + decoded );
        assertTrue(skelDecoded.getId().equals("skel1"));
        assertTrue(skelDecoded.getId() =="skel1");
    }
    
    @Test
    public void testXML1() throws IOException 
    {
        Skeleton skel = createSkeleton("testSkel");
        skel.addRoot(vj0);
        assertTrue(skel.getRoot() == vj0);
        //skel.setEncoding("TRS");
        
        String encoded = skel.toXMLString();
        System.out.println("Skeleton encoded:\n" + encoded);
        XMLTokenizer tokenizer = new XMLTokenizer(encoded);
        Skeleton skelDecoded = new Skeleton(tokenizer);
        String decoded = skelDecoded.toXMLString();
        System.out.println("Skeleton decoded:" + decoded + " id:" + skelDecoded.getId());
        assertTrue(skelDecoded.getId().equals("testSkel"));
        assertTrue(skelDecoded.getId() =="testSkel");
        assertTrue(skelDecoded.getRoot() != null);
        assertTrue(skelDecoded.getRoot().getSid() == vj0.getSid());
  
    }
  
    @Test
    public void testXML2() throws IOException 
    {
        Skeleton skel = createSkeleton("testSkel");
        
        skel.setJointSids(sidsSubset);
        skel.addRoot(vj0);
        assertTrue(skel.getRoot() == vj0);
       // skel.setEncoding("TRS");
        
        String encoded = skel.toXMLString();
        System.out.println("Skeleton encoded:\n" + encoded);
        XMLTokenizer tokenizer = new XMLTokenizer(encoded);
        Skeleton skelDecoded = new Skeleton(tokenizer);
        String decoded = skelDecoded.toXMLString();
        System.out.println("Skeleton decoded:" + decoded + " id:" + skelDecoded.getId());
        assertTrue(skelDecoded.getId().equals("testSkel"));
        assertTrue(skelDecoded.getId() =="testSkel");
        assertTrue(skelDecoded.getRoot() != null);
        assertTrue(skelDecoded.getRoot().getSid() == vj0.getSid());
  
    }
  
  
  
}
