package com.mxi.am.domain;

import com.mxi.mx.core.key.AuthorityKey;


/**
 * DOCUMENT_ME
 *
 */
public class OrganizationAuthority {

   private AuthorityKey authorityKey;

   private String authorityCode;
   private String authorityName;


   public AuthorityKey getAuthorityKey() {
      return authorityKey;
   }


   public void setAuthorityKey( AuthorityKey authorityKey ) {
      this.authorityKey = authorityKey;
   }


   public String getAuthorityName() {
      return authorityName;
   }


   public void setAuthorityName( String authorityName ) {
      this.authorityName = authorityName;
   }


   public String getAuthorityCode() {
      return authorityCode;
   }


   public void setAuthorityCode( String authorityCode ) {
      this.authorityCode = authorityCode;
   }

}
