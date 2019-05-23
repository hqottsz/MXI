package com.mxi.am.domain;

import com.mxi.mx.core.key.RefFailureSeverityKey;


public class FailureSeverity {

   private RefFailureSeverityKey id;
   private String name;


   public RefFailureSeverityKey getId() {
      return id;
   }


   public void setId( RefFailureSeverityKey id ) {
      this.id = id;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }

}
