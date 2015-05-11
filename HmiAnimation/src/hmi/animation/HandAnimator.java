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
package hmi.animation;

import hmi.math.Quat4f;
import hmi.math.Vec3f;
import hmi.neurophysics.Hand;

import java.util.HashMap;
import java.util.Map;

/**
 * Handmodel that models the constraints on the hand
 * @author Herwin
 *
 */
public class HandAnimator
{
    Map<String, VJoint> leftHandJoints = new HashMap<String,VJoint>();
    Map<String, VJoint> rightHandJoints = new HashMap<String,VJoint>();
    private final float [] thumbFlexionAxisLeft;
    private final float [] thumbFlexionAxisRight;
    
    /**
     * Armandia flexion axis left :(1, -1, -1), right:(1, 1, 1)
     *  
     */
    public HandAnimator(VJoint human, float [] thumbFlexionAxisLeft, float [] thumbFlexionAxisRight)
    {
        this.thumbFlexionAxisLeft = Vec3f.getVec3f(thumbFlexionAxisLeft);
        this.thumbFlexionAxisRight = Vec3f.getVec3f(thumbFlexionAxisRight);
        for(String joint:Hanim.LEFTHAND_JOINTS)
        {
            leftHandJoints.put(joint,human.getPartBySid(joint));
        }
        
        for(String joint:Hanim.RIGHTHAND_JOINTS)
        {
            rightHandJoints.put(joint,human.getPartBySid(joint));
        }
    }
    
    public static double getMinimumZRotation(String jointId)
    {
        switch (jointId)
        {
        case Hanim.r_pinky1:
        case Hanim.r_ring1:
        case Hanim.r_middle1:
        case Hanim.r_index1:
        case Hanim.r_thumb2:
            return Hand.getMinimumFingerFlexionMCP();
        case Hanim.l_pinky1:
        case Hanim.l_ring1:
        case Hanim.l_middle1:
        case Hanim.l_index1:
        case Hanim.l_thumb2:
            return -Hand.getMinimumFingerFlexionMCP();
        case Hanim.r_pinky2:
        case Hanim.r_ring2:
        case Hanim.r_middle2:
        case Hanim.r_index2:
            return Hand.getMinimumFingerFlexionPIP();
        case Hanim.l_pinky2:
        case Hanim.l_ring2:
        case Hanim.l_middle2:
        case Hanim.l_index2:
            return -Hand.getMinimumFingerFlexionPIP();
        case Hanim.r_pinky3:
        case Hanim.r_ring3:
        case Hanim.r_middle3:
        case Hanim.r_index3:
        case Hanim.r_thumb3:
            return Hand.getMinimumFingerFlexionDIP();
        case Hanim.l_pinky3:
        case Hanim.l_ring3:
        case Hanim.l_middle3:
        case Hanim.l_index3:
        case Hanim.l_thumb3:
            return -Hand.getMinimumFingerFlexionDIP();
        default:
            throw new IllegalArgumentException(jointId + " is not a valid Hanim hand joint.");
        }
    }
    
    public static double getMaximumZRotation(String jointId)
    {
        switch (jointId)
        {
        case Hanim.r_pinky1:
        case Hanim.r_ring1:
        case Hanim.r_middle1:
        case Hanim.r_index1:
            return Hand.getMaximumFingerFlexionMCP();
        case Hanim.l_pinky1:
        case Hanim.l_ring1:
        case Hanim.l_middle1:
        case Hanim.l_index1:
            return -Hand.getMaximumFingerFlexionMCP();
        case Hanim.r_pinky2:
        case Hanim.r_ring2:
        case Hanim.r_middle2:
        case Hanim.r_index2:
            return Hand.getMaximumFingerFlexionPIP();
        case Hanim.l_pinky2:
        case Hanim.l_ring2:
        case Hanim.l_middle2:
        case Hanim.l_index2:
            return -Hand.getMaximumFingerFlexionPIP();
        case Hanim.r_pinky3:
        case Hanim.r_ring3:
        case Hanim.r_middle3:
        case Hanim.r_index3:
        case Hanim.r_thumb3:
            return Hand.getMaximumFingerFlexionDIP();
        case Hanim.l_pinky3:
        case Hanim.l_ring3:
        case Hanim.l_middle3:
        case Hanim.l_index3:
        case Hanim.l_thumb3: 
            return -Hand.getMaximumFingerFlexionDIP();
        default:
            throw new IllegalArgumentException(jointId + " is not a valid Hanim hand joint.");
        }
    }
    
    public void setHandDOFRight(HandDOF handDoF)
    {
        for(String joint: new String[]{"r_index","r_middle","r_ring","r_pinky"})
        {
            float qAbduction[]=Quat4f.getQuat4fFromAxisAngle(1, 0, 0, (float)handDoF.getMCPAbduction(joint));
            float qFlexion[]=Quat4f.getQuat4fFromAxisAngle(0, 0, 1, (float)handDoF.getMCPFlexion(joint));
            float q[]=Quat4f.getQuat4f(qFlexion);
            Quat4f.mul(q, qAbduction);
            rightHandJoints.get(joint+1).setRotation(q);            
            rightHandJoints.get(joint+2).setRotation(Quat4f.getQuat4fFromAxisAngle(0, 0, 1, (float)handDoF.getPIPFlexion(joint)));
            rightHandJoints.get(joint+3).setRotation(Quat4f.getQuat4fFromAxisAngle(0, 0, 1, (float)Hand.getDIPRotation(handDoF.getPIPFlexion(joint))));
        }
        rightHandJoints.get(Hanim.r_thumb3).setRotation(Quat4f.getQuat4fFromAxisAngle(thumbFlexionAxisRight, (float)handDoF.IPThumbFlexion));
        rightHandJoints.get(Hanim.r_thumb2).setRotation(Quat4f.getQuat4fFromAxisAngle(thumbFlexionAxisRight, (float)handDoF.MCPThumbFlexion));
        
        float qAbduction[]=Quat4f.getQuat4fFromAxisAngle(1, 0, 0, (float)handDoF.TMCAbduction);
        float qFlexion[]=Quat4f.getQuat4fFromAxisAngle(0, 1, 0, (float)handDoF.TMCFlexion);
        float q[]=Quat4f.getQuat4f(qFlexion);
        Quat4f.mul(q, qAbduction);
        rightHandJoints.get(Hanim.r_thumb1).setRotation(q);
    }
    
    public void setHandDOFLeft(HandDOF handDoF)
    {
        for(String joint: new String[]{"l_index","l_middle","l_ring","l_pinky"})
        {
            float qAbduction[]=Quat4f.getQuat4fFromAxisAngle(1, 0, 0, (float)handDoF.getMCPAbduction(joint));
            float qFlexion[]=Quat4f.getQuat4fFromAxisAngle(0, 0, -1, (float)handDoF.getMCPFlexion(joint));
            float q[]=Quat4f.getQuat4f(qFlexion);
            Quat4f.mul(q, qAbduction);
            leftHandJoints.get(joint+1).setRotation(q);            
            leftHandJoints.get(joint+2).setRotation(Quat4f.getQuat4fFromAxisAngle(0, 0, -1, (float)handDoF.getPIPFlexion(joint)));
            leftHandJoints.get(joint+3).setRotation(Quat4f.getQuat4fFromAxisAngle(0, 0, -1, (float)Hand.getDIPRotation(handDoF.getPIPFlexion(joint))));
        }
        leftHandJoints.get(Hanim.l_thumb3).setRotation(Quat4f.getQuat4fFromAxisAngle(thumbFlexionAxisLeft, (float)handDoF.IPThumbFlexion));
        leftHandJoints.get(Hanim.l_thumb2).setRotation(Quat4f.getQuat4fFromAxisAngle(thumbFlexionAxisLeft, (float)handDoF.MCPThumbFlexion));
        
        float qAbduction[]=Quat4f.getQuat4fFromAxisAngle(1, 0, 0, (float)handDoF.TMCAbduction);
        float qFlexion[]=Quat4f.getQuat4fFromAxisAngle(0, -1, 0, (float)handDoF.TMCFlexion);
        float q[]=Quat4f.getQuat4f(qFlexion);
        Quat4f.mul(q, qAbduction);
        leftHandJoints.get(Hanim.l_thumb1).setRotation(q);
    }
    
}
