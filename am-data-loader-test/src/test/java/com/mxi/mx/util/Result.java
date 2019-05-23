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

import java.util.HashMap;
import java.util.Map;


/**
 * Result is the objected used to record the script results. The boolean iSuccess will retain the
 * success or failure of the script while the iValues map will contain key-value pairs of the any
 * data that needs to be returned to the rest of the script. <i>Eg: "barcode" = "I0002DDD"</i>
 *
 * @author Kazi Masudul Alam
 */
public class Result {

   // A message from the executing layer. It can be success message, failure or error message
   private String iMessage;

   // Operation status. Successful or failed operation
   private boolean iSuccess;

   // A <Key, Value> like collection which should be used to ensure data passing among scripts
   private Map<String, String> iValues;


   /**
    * Creates a new {@linkplain Result} object.
    */
   public Result() {
      iSuccess = false;
      iMessage = "";
      iValues = new HashMap<String, String>();
   }


   /**
    * Set result to failed state
    */
   public void failed() {
      iSuccess = false;
   }


   public String getMessage() {
      return iMessage;
   }


   public String getValue( String aKey ) {
      return iValues.get( aKey );
   }


   public Map<String, String> getValues() {
      return iValues;
   }


   public boolean isSuccess() {
      return iSuccess;
   }


   /**
    * Adds a key|value pair to the result set
    *
    * @param aKey
    *           The key to access the value
    * @param aValue
    *           The value accessed by the key
    */
   public void put( String aKey, String aValue ) {
      iValues.put( aKey, aValue );
   }


   public void setMessage( String aMessage ) {
      iMessage = aMessage;
   }


   /**
    * Set result to successful state
    */
   public void success() {
      iSuccess = true;
   }
}
