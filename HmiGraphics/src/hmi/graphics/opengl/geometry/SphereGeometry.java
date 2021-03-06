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
 

package hmi.graphics.opengl.geometry;


import hmi.graphics.opengl.GLRenderContext;
import hmi.graphics.opengl.GLRenderObject;
import javax.media.opengl.*; 

/**
 * A simple Sphere object, rendered using direct mode OpenGL
 * @author Job Zwiers
 */
public class SphereGeometry implements GLRenderObject {

  private float radius = 5.0f;
  private int numSlices = 32;
  private int numStacks = 16;
  private float drho, dtheta;
  private float ds, dt;
  
  private int sphereList;
  
   /**
    * Create a new Sphere object
    */
   public SphereGeometry(float radius, int numSlices, int numStacks) {
      this.radius = radius;
      this.numSlices = numSlices;
      this.numStacks = numStacks;
      drho = (float)(3.141592653589) / (float) numStacks;
      dtheta = 2.0f * (float)(3.141592653589) / (float) numSlices;
      ds = 1.0f / (float) numSlices;
      dt = 1.0f / (float) numStacks;
   }
 
 
   public void glInit(GLRenderContext glc) {
      sphereList = glc.gl2.glGenLists(1);
      glc.gl2.glNewList(sphereList, GL2.GL_COMPILE);
         render(glc);
      glc.gl2.glEndList();
   }
      
     
   public void glRender(GLRenderContext glc) {
      //gl.glCallList(sphereList);  
      render(glc);
   } 
      
   private void render(GLRenderContext glc) {
      GL2 gl = glc.gl2;
      float t = 1.0f;   
      float s = 0.0f;   
      for (int i = 0; i < numStacks; i++) {
         float rho =  (float)i * drho;
         float srho = (float)(Math.sin(rho));
         float crho = (float)(Math.cos(rho));
         float srhodrho = (float)(Math.sin(rho + drho));
         float crhodrho = (float)(Math.cos(rho + drho));
         gl.glBegin(GL.GL_TRIANGLE_STRIP);
            s = 0.0f;
            for (int j = 0; j <= numSlices; j++) {
               float theta = (j == numSlices) ? 0.0f : j * dtheta;
               float stheta = (float)(-Math.sin(theta));
               float ctheta = (float)(Math.cos(theta));
             
               float x = stheta * srho;
               float y = ctheta * srho;
               float z = crho;
                  
               gl.glTexCoord2f(s, t);
               gl.glNormal3f(x, y, z);
               gl.glVertex3f(x * radius, y * radius, z * radius);
             
               x = stheta * srhodrho;
               y = ctheta * srhodrho;
               z = crhodrho;
               gl.glTexCoord2f(s, t - dt);
               s += ds;
               gl.glNormal3f(x, y, z);
               gl.glVertex3f(x * radius, y * radius, z * radius);
            }
         gl.glEnd(); 
         t -= dt;
      }      
   }
 
 
 
}
