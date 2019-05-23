package com.mxi.am.domain;

import com.mxi.mx.core.key.AssemblyKey;


/**
 * Domain class for Panels
 *
 */
public class Panel {

   private String iCode;
   private String iDescription;
   private AssemblyKey iParentAssembly;


   public String getDescription() {
      return iDescription;
   }


   public void setDescription( String aDescription ) {
      iDescription = aDescription;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public AssemblyKey getParentAssembly() {
      return iParentAssembly;
   }


   public void setParentAssembly( AssemblyKey aParentAssembly ) {
      iParentAssembly = aParentAssembly;
   }

}
