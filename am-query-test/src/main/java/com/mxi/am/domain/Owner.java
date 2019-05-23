package com.mxi.am.domain;

/**
 * Domain class for Owner
 *
 */
public class Owner {

   private String code;
   private String name;
   private boolean localBool = false;
   private boolean defaultBool = false;


   public String getCode() {
      return code;
   }


   public void setCode( String code ) {
      this.code = code;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }


   public boolean isLocalBool() {
      return localBool;
   }


   public void setLocalBool( boolean localBool ) {
      this.localBool = localBool;
   }


   public boolean isDefaultBool() {
      return defaultBool;
   }


   public void setDefaultBool( boolean defaultBool ) {
      this.defaultBool = defaultBool;
   }

}
