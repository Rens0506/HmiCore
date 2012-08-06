/*******************************************************************************
 * 
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
/*
 
 */

package hmi.util;

import javax.swing.JOptionPane;

/**
 *
 * @author zwiers
 */
public final class CheckVersion {
   
   /*
    * disable creation of CheckVersion Objects
    */
   private CheckVersion() {};
   
   
   /**
    * Checks whether the current specification version meets the specified required version;
    * if not, a RuntimeException is thrown.
    * No check is performed when manifest info is not available.
    */
   public static void requireVersion(String packageName, String requiredVersion) {
      Package pack = Package.getPackage(packageName);      
      String specificationVersion = pack.getSpecificationVersion();
      if ( specificationVersion == null) return; // no check possible, so assume ok
      if (isCompatibleWith(requiredVersion, specificationVersion)) return;
      String msg = "Package " + packageName + " Version " + pack.getSpecificationVersion() 
        + " does not meet the required version " + requiredVersion;
      JOptionPane.showMessageDialog(null, msg, "Package Info", JOptionPane.PLAIN_MESSAGE);
      throw new RuntimeException(msg); 
   } 
    
   /**
    * Returns the specification version from the Manifest.mf file, if available,
    * or else an empty String.
    */ 
   public static String getVersion(String packageName) {
       Package pack = Package.getPackage(packageName);
      String result = pack.getSpecificationVersion();
      return (result==null) ? "" : result;
   } 
   
   
   /**
    * checks whether the package specification version is compatible with a certain desired version.
    * &quot;isCompatibleWith(desiredversion)&quot; return true iff the desiredVersion is smaller or equal
    * than the package specification version, where "smaller than" is determined by the lexicographic
    * order on major, minor, and micro version numbers. 
    * (Missing numbers are considered to be 0). For instance, when the package specification version
    * would be 2.1, then some examples of compatible desired versions are: 1, 1.0, 1.6, 2.0.4,  2.1, 2.1.0,
    * whereas desired versions like 2.2, 3.0, or 2.1.1 would not be compatible.
    */
   public static boolean isCompatibleWith(String desiredVersion, String specificationVersion) {
       //String specificationVersion = pack.getSpecificationVersion();
       if (specificationVersion == null) return true; // no spec version available, so assume ok
       String[] desiredNums = desiredVersion.split("[.]");
       String[] specificationNums= specificationVersion.split("[.]");  
       int desired, specified;     
       try {
          for (int vn=0; vn<desiredNums.length; vn++) {
             //System.out.println("  desired num " + vn + ": " + desiredNums[vn] + " specification num: " + specificationNums[vn]);
             desired = Integer.valueOf(desiredNums[vn]);
             if (vn<specificationNums.length) {
               specified = Integer.valueOf(specificationNums[vn]);
             } else {
                specified = 0;
             }
         
             if ( desired < specified ) return true;
             if ( desired > specified ) return false;              
          }
          return true;
       } catch (NumberFormatException e) {
           System.out.println("CheckVersion.isCompatibelWith method: illegal version numbers: " + desiredVersion + " / " + specificationVersion);
           return false;
       }
   }
   
 
   
}
