
package com.mxi.mx.web.query.tag;

import java.io.InputStream;


/**
 * this method provides the data for TagDetailsTest
 *
 * @author okamenskova
 */
public class TagDetailsData {

   /**
    * Returns the value of the data file property.
    *
    * @return the value of the data file property.
    */
   public InputStream getDataFileInputStream() {
      return getClass().getResourceAsStream( "TagDetailsData.xml" );
   }
}
