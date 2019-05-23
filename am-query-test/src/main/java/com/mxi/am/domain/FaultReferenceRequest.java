package com.mxi.am.domain;

import java.util.Date;

import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.HumanResourceKey;


public class FaultReferenceRequest {

   private FaultKey iFaultKey;
   private FaultReferenceKey iFaultReferenceKey;
   private String iRequestStatus;
   private HumanResourceKey iHumanResourceKey;
   private Date iDateRequested;
   private Date iDateResolved;


   // Sets the fault reference request to the fault's current reference's request if it exists
   public void setReferenceRequestByFaultsCurrentRequest( FaultKey faultKey ) {
      this.iFaultKey = faultKey;
   }


   public FaultKey getReferenceRequestByFaultsCurrentRequest() {
      return this.iFaultKey;
   }


   public void setReferenceRequestByReferenceKey( FaultReferenceKey faultReferenceKey ) {
      this.iFaultReferenceKey = faultReferenceKey;
   }


   public FaultReferenceKey getReferenceRequestByReferenceKey() {
      return this.iFaultReferenceKey;
   }


   public void setRequestStatus( String requestStatus ) {
      this.iRequestStatus = requestStatus;
   }


   public String getRequestStatus() {
      return this.iRequestStatus;
   }


   public void setHumanResourceKey( HumanResourceKey humanResourceKey ) {
      this.iHumanResourceKey = humanResourceKey;
   }


   public HumanResourceKey getHumanResourceKey() {
      return this.iHumanResourceKey;
   }


   public void setDateRequested( Date dateRequested ) {
      this.iDateRequested = dateRequested;
   }


   public Date getDateRequested() {
      return this.iDateRequested;
   }


   public void setDateResolved( Date dateResolved ) {
      this.iDateResolved = dateResolved;
   }


   public Date getDateResolved() {
      return this.iDateResolved;
   }

}
