package com.mxi.am.domain;

/**
 * Domain entity for a user
 *
 */
public class User {

   private String iAltId;
   private String iFirstName;
   private String iLastName;
   private String iUsername;
   private Integer iUserId;
   private String iMiddleName;
   private String iEmailId;


   public String getMiddleName() {
      return iMiddleName;
   }


   public void setMiddleName( String iMiddleName ) {
      this.iMiddleName = iMiddleName;
   }


   public String getEmailId() {
      return iEmailId;
   }


   public void setEmailId( String iEmailId ) {
      this.iEmailId = iEmailId;
   }


   public String getAltId() {
      return iAltId;
   }


   public void setAltId( String aAltId ) {
      iAltId = aAltId;
   }


   public Integer getUserId() {
      return iUserId;
   }


   public void setUserId( Integer aUserId ) {
      iUserId = aUserId;
   }


   public String getUsername() {
      return iUsername;
   }


   public void setUsername( String aUsername ) {
      iUsername = aUsername;
   }


   public String getLastName() {
      return iLastName;
   }


   public void setLastName( String aLastName ) {
      iLastName = aLastName;
   }


   public String getFirstName() {
      return iFirstName;
   }


   public void setFirstName( String aFirstName ) {
      iFirstName = aFirstName;
   }

}
