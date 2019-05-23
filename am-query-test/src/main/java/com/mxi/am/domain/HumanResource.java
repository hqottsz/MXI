package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.UserKey;


/**
 * Domain entity for human resource used for query testing.
 *
 */
public class HumanResource {

   private String iAltId;
   private UserKey iUser;
   private OrgKey iOrganization;
   private Boolean iAllAuthority;
   private Boolean iDefaultOrg;
   private String iUsername;
   private String iHrCode;
   private List<LocationKey> iSupplyLocations = new ArrayList<LocationKey>();
   private List<AuthorityKey> iAuthorities = new ArrayList<AuthorityKey>();


   public String getAltId() {
      return iAltId;
   }


   public void setAltId( String aAltId ) {
      iAltId = aAltId;
   }


   public UserKey getUser() {
      return iUser;
   }


   public void setUser( UserKey aUser ) {
      iUser = aUser;
   }


   public OrgKey getOrganization() {
      return iOrganization;
   }


   public void setOrganization( OrgKey aOrganization ) {
      iOrganization = aOrganization;
   }


   public Optional<Boolean> hasAllAuthority() {
      return Optional.ofNullable( iAllAuthority );
   }


   public void setAllAuthority( Boolean aAllAuthority ) {
      iAllAuthority = aAllAuthority;
   }


   public Boolean isDefaultOrg() {
      return iDefaultOrg;
   }


   public void setDefaultOrg( Boolean aDefaultOrg ) {
      iDefaultOrg = aDefaultOrg;
   }


   public void setUsername( String aUsername ) {
      iUsername = aUsername;
   }


   public String getUsername() {
      return iUsername;
   }


   public List<LocationKey> getSupplyLocations() {
      return iSupplyLocations;
   }


   public void setSupplyLocations( LocationKey aSupplyLocation ) {
      iSupplyLocations.add( aSupplyLocation );
   }


   public List<AuthorityKey> getAuthorities() {
      return iAuthorities;
   }


   public void addAuthority( AuthorityKey aAuthorityKey ) {
      iAuthorities.add( aAuthorityKey );
   }


   public String getHrCode() {
      return iHrCode;
   }


   public void setHrCode( String iHrCode ) {
      this.iHrCode = iHrCode;
   }
}
