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
package hmi.physics.ode;


import hmi.physics.Mass;
import hmi.physics.AbstractMassTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.odejava.Odejava;

public class OdeMassTest extends AbstractMassTest
{
    @Override
    public Mass createMass()
    {
        OdeMass m = new OdeMass();
        return m;
    }
    
    @Before
    public void setUp() throws Exception
    {
        Odejava.init();
    }

    @After
    public void tearDown()
    {
        Odejava.close();
    }
    
    @Override
    @Test
    public void testaddMass()
    {
        super.testaddMass();
    }
    
    @Override
    @Test
    public void testScale()
    {
        super.testScale();
    }
    
    @Override
    @Test
    public void testRotate()
    {
        super.testRotate();
    }
}
