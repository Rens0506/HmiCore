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

package hmi.graphics.util;

import hmi.animation.Hanim;
import hmi.graphics.collada.Collada;
import hmi.graphics.collada.scenegraph.ColladaTranslator;
import hmi.graphics.opengl.scenegraph.GLScene;
import hmi.graphics.opengl.scenegraph.ScenegraphTranslator;
import hmi.graphics.opengl.scenegraph.VGLNode;
import hmi.graphics.scenegraph.GNode;
import hmi.graphics.scenegraph.GScene;
import hmi.graphics.scenegraph.Skeletons;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IO for graphics.
 * 
 * @author Job Zwiers
 */
public final class SceneIO
{

    /* Disable creation of SceneIO objects */
    private SceneIO()
    {
    }

    // public enum Postprocess { ArmandiaHAnim, BlueguyHAnim, NONE};

    public static final String ARMANDIA = "ARMANDIA";
    public static final String SETHANIM = "SETHANIM";
    public static final String BLUEGUY = "BLUEGUY";
    public static final String NONE = "NONE";

    private static boolean throwExceptions = false;

    /**
     * Sets the status of &quot;throwExceptions&quot;
     * When false (the default) IOExceptions are caught and logged, and null results are returned.
     * When true, IOExceptions are logged and re-thrown as RuntimeExceptions
     */
    public static void setThrowExceptions(boolean te)
    {
        throwExceptions = te;
    }

    /**
     * Reads a VGLNode from the specified file, within the specified resource directory
     */
    public static VGLNode readVGLNode(String resourceDir, String fileName)
    {
        return readVGLNode(resourceDir, fileName, null);
    }

    /**
     * Reads a VGLNode from the specified file, within the specified resource directory
     * postProcess can be one of the predefined processing modes, for setting HAnim poses.
     */
    public static VGLNode readVGLNode(String resourceDir, String fileName, String postProcess)
    {
        VGLNode result = new VGLNode(fileName);
        GLScene glScene = readGLScene(resourceDir, fileName, postProcess);
        if (glScene != null)
        {
            result.addGLShapeList(glScene.getGLShapeList());

        }
        return result;
    }

    /**
     * Reads a GLScene from the specified file, within the specified resource directory.
     * No postprocessing is performed.
     */
    public static GLScene readGLScene(String resourceDir, String fileName)
    {
        return readGLScene(resourceDir, fileName, NONE);
    }

    /**
     * Reads a GLScene from the specified file, within the specified resource directory.
     * postProcess can be one of the predefined processing modes, for setting HAnim poses.
     */
    public static GLScene readGLScene(String resourceDir, String fileName, String postProcess)
    {
        GScene gscene = readGScene(resourceDir, fileName, postProcess, true);
        if (gscene == null) return new GLScene("Empty");
        GLScene glScene = ScenegraphTranslator.fromGSceneToGLScene(gscene);
        return glScene;
    }

    public static GScene readGScene(String resourceDir, String fileName, String postProcess)
    {
        return readGScene(resourceDir, fileName, postProcess, true);
    }

    /**
     * Reads a GScene from the specified file, within the specified resource directory.
     * postProcess can be one of the predefined processing modes, for setting HAnim poses.
     */
    public static GScene readGScene(String resourceDir, String fileName, String postProcess, boolean adjustBindPoses)

    {
        String resDir = (resourceDir == null || resourceDir.equals("")) ? "" : resourceDir.replace('\\', '/') + "/";
        String file = resDir + fileName;
        return readGScene(file, postProcess, adjustBindPoses);
    }

    public static GScene readGScene(String file, String postProcess)
    {
        return readGScene(file, postProcess, true);
    }

    /**
     * Reads a GScene from the specified file.
     * postProcess can be one of the predefined processing modes, for setting HAnim poses.
     * The file type, derived from the postfix, determines whether to read a Collada file
     * (.dae or .DAE) or a binaray file (.bin)
     */
    public static GScene readGScene(String file, String postProcess, boolean adjustBindPoses)
    {
        GScene gscene = null;
        try
        {
            if (file.endsWith(".dae") || file.endsWith(".DAE"))
            {
                Collada col = null;
                if (file.startsWith("file:"))
                {
                    col = Collada.forURL(file);
                }
                else
                {
                    col = Collada.forResource(file);
                }
                if (col == null) throw new RuntimeException("SceneIO.readGScene: null Collada input");
                gscene = ColladaTranslator.colladaToGSkinnedMeshScene(col, adjustBindPoses);

            }
            else if (file.endsWith(".bin"))
            {
                InputStream inps = null;
                if (file.startsWith("file:"))
                {
                    URL url = new URL(file);
                    inps = url.openStream();
                }
                else
                {
                    inps = hmi.graphics.util.SceneIO.class.getClassLoader().getResourceAsStream(file);
                }

                if (inps == null) throw new RuntimeException("SceneIO.readGScene: null binary input");
                DataInputStream dataIn = new DataInputStream(new BufferedInputStream(inps));
                // DataInputStream dataIn = new DataInputStream(inps);
                gscene = new GScene("");
                gscene.readBinary(dataIn);
                dataIn.close();
                gscene.collectSkinnedMeshes();
                gscene.resolveSkinnedMeshJoints(); // resolve skeleton roots and skeleton joints
            }
            else
            {
                logger.error("readGScene: unknow file format : " + file);
                return null;
            }
        }
        catch (Exception ioe)
        {
            logger.error("SceneIO.readGScene: " + ioe);
            ioe.printStackTrace();
            if (throwExceptions)
            {
                throw new RuntimeException(ioe.getMessage());
            }
            else
            {
                return null;
            }
        }

        if (postProcess == null || postProcess.equals("") || postProcess.equals(NONE))
        {
            // ok, no postprocessing/no HAnim
        }
        else if (postProcess.equals(ARMANDIA))
        {
            gscene.setSkeletonHAnimPoses();
        }
        else if (postProcess.equals(SETHANIM))
        {
            gscene.setSkeletonHAnimPoses();
        }
        else if (postProcess.equals(BLUEGUY))
        {
            GNode humanRootGnode = gscene.getPartBySid(Hanim.HumanoidRoot);
            Skeletons.processHAnim(humanRootGnode, Hanim.HumanoidRoot);
        }
        else
        {
            logger.error("readGScene, unknown postprocessing mode: " + postProcess);
        }

        gscene.calculateVJointMatrices(); // needed
        return gscene;
    }

    /**
     * Writes a GScene to file, where the file format is determined by the postfix.
     * For the time being, only .bin files are allowed
     */
    public static void writeGScene(String fileName, GScene gscene)
    {
        if (fileName.endsWith(".bin"))
        {
            File outFile = new File(fileName);
            try
            {
                DataOutputStream outps = new DataOutputStream(new FileOutputStream(outFile));
                gscene.writeBinary(outps);
                outps.close();
            }
            catch (Exception e)
            {
                logger.error("SceneIO.writeGScene: " + e);
                return;
            }

        }
        else
        {
            logger.error("writeGScene: unsupported file format: " + fileName);
        }
    }

    private static Logger logger = LoggerFactory.getLogger("hmi.graphics.scenegraph");

}
