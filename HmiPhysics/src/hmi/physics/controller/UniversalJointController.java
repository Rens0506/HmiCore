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
package hmi.physics.controller;

import hmi.physics.PhysicalHumanoid;
import hmi.physics.PhysicalJoint;
import hmi.util.StringUtil;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * A PD-controller that guides a ball joint to a certain desired angle and (optionally) desired avelocity
 * 
 * for each angle: torque = ks * (desiredangle-currentangle) + ds * (desiredavelocity - currentavelocity) with ks the spring gain, ds the damper gain
 * 
 * Note that this controller requires a high physical simulation rate (3 ms or less)
 * 
 * @author Wei
 */
public class UniversalJointController implements PhysicalController
{
    private Logger logger = LoggerFactory.getLogger(UniversalJointController.class.getName());

    private PhysicalJoint joint;
    private String jointId;
    private float anglex;
    private float angley;

    private float avelx;
    private float avely;

    private float aprevx;
    private float aprevy;

    private float ksx = 1f;
    private float dsx = 2f;
    private float ksy = 1;
    private float dsy = 2f;

    private final static float DEFAULT_KSX = 1;
    private final static float DEFAULT_DSX = 2;

    private final static float DEFAULT_KSY = 1;
    private final static float DEFAULT_DSY = 2;
    
    private int axisX = 0, axisY = 1;

    private final Set<String> desJointIDs = ImmutableSet.of();

    private int firstRun = 0;
    private PhysicalHumanoid pHuman;

    /**
     * Constructor
     * Note that x and y  are just the 1st and 2nd axis as set up in the physical joint, not necessarily the x and y axis.
     * @param j physical joint that is controlled
     * @param ax desired x angle
     * @param ay desired y angle
     * @param avx desired x avelocity
     * @param avy desired y avelocity
     * @param kx spring gain x
     * @param ky spring gain y
     * @param dx damper gain x
     * @param dy damper gain y
     * @param axis_X x axis index
     * @param axis_Y y axis index      
     */
    public UniversalJointController(PhysicalJoint j, float ax, float ay, float avx, float avy, float kx, float ky, float dx, float dy,
    		int axis_X, int axis_Y)
    {
        joint = j;
        if (joint != null)
        {
            jointId = j.getName();
        }

        anglex = -ax;
        angley = -ay;
        
        avelx = avx;
        avely = avy;
       

        ksx = kx;
        dsx = dx;
        ksy = ky;
        dsy = dy;
        
        axisX = axis_X;
        axisY = axis_Y;
        
        reset();
    }

    /**
     * Constructor, sets up a PD-controller with 0 desired velocity Spring and damper gains are set up to dampen a shoulder joint that loosely hangs
     * down
     * 
     * @param j controlled physical joint
     * @param ax desired x angle
     * @param ay desired y angle
     */
    public UniversalJointController(PhysicalJoint j, float ax, float ay, int axis_X, int axis_Y)
    {
        this(j, ax, ay, 0, 0, DEFAULT_KSX, DEFAULT_KSY, DEFAULT_DSX, DEFAULT_DSY, axis_X, axis_Y);
    }

    public UniversalJointController()
    {
        this(null, 0, 0, 0, 1);
    }

    /**
     * Sets new spring values
     */
    public void setSprings(float kx, float ky)
    {
        ksx = kx;
        ksy = ky;
    }
    
    /**
     * Sets new spring values
     */
    public void setDampers(float dx, float dy)
    {
        dsx = dx;
        dsy = dy;
    }

    @Override
    public void reset()
    {
        aprevx = 0;
        aprevy = 0;
        
        firstRun = 0;
        logger.debug("universal controller reset");
    }

    @Override
    public void update(double timeDiff)
    {
        float ax = joint.getAngle(axisX);
        float ay = joint.getAngle(axisY);

        float dax = 0;
        float day = 0;

        if (firstRun == 0)  //XXX Ode specific hack: skip first run, 'cause a is still invalid
        {
            firstRun = 1;
            logger.debug("First run of universal controller (x,y)={},{}", new Object[] { ax, ay});
            return;
        }
        if(firstRun == 2)
        {
            aprevx = ax;
            aprevy = ay;         
        }
        
        dax = (ax - aprevx) / (float) timeDiff;
        day = (ay - aprevy) / (float) timeDiff;
        /*
         * logger.debug("dax {}",dax); logger.debug("ax {}",ax); logger.debug("aprevx {}",ax); logger.debug("dsx {}",dsx);
         */
        switch (axisX)
        {
        case 0:
            joint.addTorque((anglex - ax) * ksx + (avelx - dax) * dsx, 0, 0);
            break;
        case 1:
            joint.addTorque(0, (anglex - ax) * ksx + (avelx - dax) * dsx, 0);
            break;
        case 2:
            joint.addTorque(0, 0, (anglex - ax) * ksx + (avelx - dax) * dsx);
            break;
        default:
            throw new RuntimeException("Invalid axis number:"+axisX);
        }
        
        switch (axisY)
        {
        case 0:
            joint.addTorque((angley - ay) * ksy + (avely - day) * dsy, 0, 0);
            break;
        case 1:
            joint.addTorque(0,(angley - ay) * ksy + (avely - day) * dsy, 0);
            break;
        case 2:
            joint.addTorque(0, 0, (angley - ay) * ksy + (avely - day) * dsy);
            break;
        default:
            throw new RuntimeException("Invalid axis number:"+axisY);
        }
        
        aprevx = ax;
        aprevy = ay;
    }

    @Override
    public Set<String> getRequiredJointIDs()
    {
        return ImmutableSet.of(jointId);        
    }

    @Override
    public void setPhysicalHumanoid(PhysicalHumanoid ph)
    {
        joint = ph.getJoint(jointId);
        pHuman = ph;
    }

    @Override
    public UniversalJointController copy(PhysicalHumanoid ph)
    {
        UniversalJointController bjc = new UniversalJointController(joint, -anglex, -angley, avelx, avely, ksx, ksy, dsx, dsy, axisX, axisY);
        bjc.setPhysicalHumanoid(ph);
        try
        {
            bjc.setParameterValue("joint", jointId);
        }
        catch (ControllerParameterException e)
        {
            throw new AssertionError(e); // can't happen
        }
        return bjc;
    }

    @Override
    public String getParameterValue(String name) throws ControllerParameterNotFoundException
    {
        if (name.equals("joint"))
            return jointId;
        else if (name.equals("axisx")) return "" + axisX;
        else if (name.equals("axisy")) return "" + axisY;
        return ""+getFloatParameterValue(name);
    }

    @Override
    public float getFloatParameterValue(String name) throws ControllerParameterNotFoundException
    {
        if (name.equals("anglex"))
            return -anglex;
        if (name.equals("angley"))
            return -angley;
        if (name.equals("ksx"))
            return ksx;
        if (name.equals("ksy"))
            return ksy;
        if (name.equals("dsx"))
            return dsx;
        if (name.equals("dsy"))
            return dsy;
        if (name.equals("axisx"))
            return axisX;
        if (name.equals("axisy"))
            return axisY;
        
        throw new ControllerParameterNotFoundException(name);
    }

    @Override
    public void setParameterValue(String name, String value) throws ControllerParameterException
    {
        if (name.equals("joint"))
        {
            jointId = value;
            if (pHuman != null)
                joint = pHuman.getJoint(jointId);
        }
        else if (StringUtil.isNumeric(value))
        {
            setParameterValue(name,Float.parseFloat(value));
        }
        else
        {
            throw new ControllerParameterException("Invalid parameter setting "+name+"="+value);
        }
    }

    @Override
    public void setParameterValue(String name, float value) throws ControllerParameterException
    {
        if (name.equals("anglex"))
            anglex = -value;
        else if (name.equals("angley"))
            angley = -value;
        else if (name.equals("ksx"))
            ksx = value;
        else if (name.equals("ksy"))
            ksy = value;
        else if (name.equals("dsx"))
            dsx = value;
        else if (name.equals("dsy"))
            dsy = value;
        else if (name.equals("axisx"))
            axisX = (int)value;
        else if (name.equals("axisy"))
            axisY = (int)value;
        else
            throw new ControllerParameterNotFoundException(name);
    }

    @Override
    public Set<String> getDesiredJointIDs()
    {
        return desJointIDs;
    }

    @Override
    public Set<String> getJoints()
    {
        return getRequiredJointIDs();
    }
}
