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

import javax.media.opengl.*; 
import hmi.graphics.opengl.GLRenderContext;
import hmi.graphics.opengl.GLRenderObject;

/**
 * A simple capsule object, centered at (0,0,0) rendered using direct mode OpenGL Primary for debugging purposes, optimized for flexibility, not
 * rendering speed. [DR]: I did not really understand what I was doing, but I adapted some of the code of Job's SphereGeometry. Textures on this
 * capsule are surely going to be wrong.
 * 
 * @author Herwin van Welbergen
 * @author Job Zwiers
 * @author Dennis Reidsma
 */
public class CapsuleGeometry implements GLRenderObject
{
    public static final int NUM_SLICES = 8;
    public static final int NUM_STACKS = 12;
    private float cylinderHeight;
    private float height;
    private float radius;
    private int numSlices = NUM_SLICES;
    private int numStacks = NUM_STACKS;
    private float drho, dtheta;
    private float ds, dt;

    private int sphereList;

    /**
     * Constructor
     * 
     * @param h height of the cylindrical part
     * @param r radius
     */
    public CapsuleGeometry(float r, float h, int slices, int stacks)
    {
        numSlices = 2 * slices;
        numStacks = 2 * stacks;
        height = h;
        radius = r;
        // Herwin: why?
        // if (height < 2* radius) height = 2*radius;
        cylinderHeight = height; // it seems that odecapsule length does NOT include the caps?
        drho = (float) (Math.PI) / numStacks;
        dtheta = 2.0f * (float) (Math.PI) / numSlices;
        ds = 1.0f / numSlices;
        dt = 1.0f / numStacks;
    }

    @Override
    public void glInit(GLRenderContext glc)
    {
        GL2 gl = glc.gl2;
        sphereList = gl.glGenLists(1);
        gl.glNewList(sphereList, GL2.GL_COMPILE);
        render(gl);
        gl.glEndList();
    }

    @Override
    public void glRender(GLRenderContext glc)
    {
        GL2 gl = glc.gl2;
        // gl.glCallList(sphereList);
        render(gl);
    }

    private void render(GL2 gl)
    {
       
        float t = 1.0f;
        float s = 0.0f;
        for (int i = 0; i < numStacks; i++)
        {
            float rho = i * drho;
            float srho = (float) (Math.sin(rho));
            float crho = (float) (Math.cos(rho));
            float srhodrho = (float) (Math.sin(rho + drho));
            float crhodrho = (float) (Math.cos(rho + drho));
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            s = 0.0f;
            for (int j = 0; j <= numSlices / 2; j++)
            {
                float theta = (j == numSlices) ? 0.0f : j * dtheta;
                float stheta = (float) (-Math.sin(theta));
                float ctheta = (float) (Math.cos(theta));

                float x = ctheta * srho;
                float y = stheta * srho;
                float z = crho;

                gl.glTexCoord2f(s, t);
                gl.glNormal3f(x, y - cylinderHeight / 2f, z);
                gl.glVertex3f(x * radius, y * radius - cylinderHeight / 2f, z * radius);

                x = ctheta * srhodrho;
                y = stheta * srhodrho;
                z = crhodrho;
                gl.glTexCoord2f(s, t - dt);
                s += ds;
                gl.glNormal3f(x, y - cylinderHeight / 2f, z);
                gl.glVertex3f(x * radius, y * radius - cylinderHeight / 2f, z * radius);
            }
            gl.glEnd();
            t -= dt;
        }
        // and now render cylinder...
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
        for (int i = 0; i <= 2 * numStacks; i++)
        {
            float rho = i * drho;
            float x = (float) (Math.sin(rho));
            float y = -cylinderHeight / 2;
            float z = (float) (Math.cos(rho));

            gl.glNormal3f(x, y, z);
            gl.glVertex3f(x * radius, y, z * radius);

            y = cylinderHeight / 2;

            gl.glNormal3f(x, y, z);
            gl.glVertex3f(x * radius, y, z * radius);
        }
        gl.glEnd();
        // and other half of the capsule end
        for (int i = 0; i < numStacks; i++)
        {
            float rho = i * drho;
            float srho = (float) (Math.sin(rho));
            float crho = (float) (Math.cos(rho));
            float srhodrho = (float) (Math.sin(rho + drho));
            float crhodrho = (float) (Math.cos(rho + drho));
            gl.glBegin(GL.GL_TRIANGLE_STRIP);
            s = 0.0f;
            for (int j = numSlices / 2; j <= numSlices; j++)
            {
                float theta = (j == numSlices) ? 0.0f : j * dtheta;
                float stheta = (float) (-Math.sin(theta));
                float ctheta = (float) (Math.cos(theta));

                float x = ctheta * srho;
                float y = stheta * srho;
                float z = crho;

                gl.glTexCoord2f(s, t);
                gl.glNormal3f(x, y + cylinderHeight / 2f, z);
                gl.glVertex3f(x * radius, y * radius + cylinderHeight / 2f, z * radius);

                x = ctheta * srhodrho;
                y = stheta * srhodrho;
                z = crhodrho;
                gl.glTexCoord2f(s, t - dt);
                s += ds;
                gl.glNormal3f(x, y + cylinderHeight / 2f, z);
                gl.glVertex3f(x * radius, y * radius + cylinderHeight / 2f, z * radius);
            }
            gl.glEnd();
            t -= dt;
        }
    }
}
