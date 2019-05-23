package com.mxi.am.domain;

import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;


/**
 * Domain object for an organization
 *
 */
public class Organization {

   private String iAltId;
   private OrgKey iOrgKey;
   private String iCode;
   private String iName;
   private RefOrgTypeKey iType;


   public String getAltId() {
      return iAltId;
   }


   public void setAltId( String aAltId ) {
      iAltId = aAltId;
   }


   public OrgKey getOrgKey() {
      return iOrgKey;
   }


   public void setOrgKey( OrgKey aOrgKey ) {
      iOrgKey = aOrgKey;
   }


   public String getCode() {
      return iCode;
   }


   public void setCode( String aCode ) {
      iCode = aCode;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public RefOrgTypeKey getType() {
      return iType;
   }


   public void setType( RefOrgTypeKey aType ) {
      iType = aType;
   }

}
