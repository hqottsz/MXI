package com.mxi.am.domain;

/**
 * Domain class for manufacturer
 *
 */
public class Manufacturer {

   private String iCode;
   private String iName;


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }

}
