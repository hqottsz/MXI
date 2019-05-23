package com.mxi.am.domain;

import com.mxi.mx.core.key.IetmDefinitionKey;


public class Attachment {

   private IetmDefinitionKey iIetmDefnKey;
   private String iName;
   private byte[] iAttachedBlob;


   public byte[] getAttachedBlob() {
      return iAttachedBlob;
   }


   public void setAttachedBlob( byte[] aAttachedBlob ) {
      iAttachedBlob = aAttachedBlob;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public IetmDefinitionKey getIetmKey() {
      return iIetmDefnKey;
   }


   public void setIetmKey( IetmDefinitionKey aIetmKey ) {
      iIetmDefnKey = aIetmKey;
   }

}
