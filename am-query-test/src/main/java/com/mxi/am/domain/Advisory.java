package com.mxi.am.domain;

import com.mxi.mx.core.key.RefTaskAdvisoryTypeKey;


/**
 * Domain entity for Advisory
 *
 */
public class Advisory {

   private String iNotes;
   private RefTaskAdvisoryTypeKey iType;


   public String getNotes() {
      return iNotes;
   }


   public void setNotes( String aNotes ) {
      iNotes = aNotes;
   }


   public RefTaskAdvisoryTypeKey getType() {
      return iType;
   }


   public void setType( RefTaskAdvisoryTypeKey aType ) {
      iType = aType;
   }

}
