/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 * 
 * Copyright 2000-2015 Mxi Technologies, Ltd. All Rights Reserved.
 * 
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

package com.mxi.mx.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


/**
 * ScriptUtil is library that contains common functions necessary to build test cases. It also
 * includes any specialized functionality that simplifies the scripting process in Maintenix.
 *
 * @author Andrew Bruce
 */

public class PropertyLoader {

   /**
    * Creates a new {@linkplain Properties} object.
    */
   private PropertyLoader() {

   }


   /**
    * Loads the Properties from actuals loader configuration file
    *
    * @return The Properties loaded
    *
    * @throws Exception
    *            If an error occurs opening or closing the file input stream
    */
   public static Properties getActualsLoaderProperties() throws Exception {
      InputStream lInput = null;

      try {

         lInput =
               new FileInputStream( "build//resources//test//test.data.loader.config.properties" );

         // load a properties file
         Properties lProperties = new Properties();
         lProperties.load( lInput );

         return lProperties;
      } finally {
         if ( lInput != null ) {

            lInput.close();
         }
      }
   }
}
