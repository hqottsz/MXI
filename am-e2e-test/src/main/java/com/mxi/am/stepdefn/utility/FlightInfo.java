package com.mxi.am.stepdefn.utility;

import java.util.Date;

import com.mxi.xml.xsd.core.flights.flight.x20.FlightsDocument.Flights.Flight.FlightAttributes.CapabilityRequirements;


public class FlightInfo {

   private String iRegCd = "";
   private String iArrivalLoc = "";
   private String iDeptLoc = "";
   private String iExtKey = "";
   private String iFltName = "";
   private String iMasterFltNo = "";
   private String barCode = "";
   private Date iSchedDeptTime;
   private Date iSchedArrivalTime;
   private Date iActualDeptTime;
   private Date iActualArrivalTime;
   private Date iTakeOffTime;
   private CapabilityRequirements iCapabilityRequirements;
   private Date iDownTime;


   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "FlightInfo [iRegCd=" + iRegCd + ", iArrivalLoc=" + iArrivalLoc + ", iDeptLoc="
            + iDeptLoc + ", iExtKey=" + iExtKey + ", iFltName=" + iFltName + ", iFltNo="
            + iMasterFltNo + ", barCode=" + barCode + ", iSchedDeptTime=" + iSchedDeptTime
            + ", iSchedArrivalTime=" + iSchedArrivalTime + ", iActualDeptTime=" + iActualDeptTime
            + ", iActualArrivalTime=" + iActualArrivalTime + ", iTakeOffTime=" + iTakeOffTime
            + "iCapabilityRequirements = (" + iCapabilityRequirements + ") ]";
   }


   /**
    * Returns the value of the takeOffTime property.
    *
    * @return the value of the takeOffTime property
    */
   public Date getTakeOffTime() {
      return iTakeOffTime;
   }


   /**
    * Sets a new value for the takeOffTime property.
    *
    * @param aTakeOffTime
    *           the new value for the takeOffTime property
    */
   public void setTakeOffTime( Date aTakeOffTime ) {
      iTakeOffTime = aTakeOffTime;
   }


   /**
    * Returns the value of the actualArrivalTime property.
    *
    * @return the value of the actualArrivalTime property
    */
   public Date getActualArrivalTime() {
      return iActualArrivalTime;
   }


   /**
    * Returns the value of the extKey property.
    *
    * @return the value of the extKey property
    */
   public String getExtKey() {
      return iExtKey;
   }


   /**
    * Sets a new value for the extKey property.
    *
    * @param aExtKey
    *           the new value for the extKey property
    */
   public void setExtKey( String aExtKey ) {
      iExtKey = aExtKey;
   }


   /**
    * Returns the value of the fltName property.
    *
    * @return the value of the fltName property
    */
   public String getFltName() {
      return iFltName;
   }


   /**
    * Sets a new value for the fltName property.
    *
    * @param aFltName
    *           the new value for the fltName property
    */
   public void setFltName( String aFltName ) {
      iFltName = aFltName;
   }


   /**
    * Returns the value of the fltNo property.
    *
    * @return the value of the fltNo property
    */
   public String getMasterFltNo() {
      return iMasterFltNo;
   }


   /**
    * Sets a new value for the fltNo property.
    *
    * @param aMasterFltNo
    *           the new value for the fltNo property
    */
   public void setMasterFltNo( String aMasterFltNo ) {
      iMasterFltNo = aMasterFltNo;
   }


   /**
    * Returns the value of the barCode property.
    *
    * @return the value of the barCode property
    */
   public String getBarCode() {
      return barCode;
   }


   /**
    * Sets a new value for the barCode property.
    *
    * @param aBarCode
    *           the new value for the barCode property
    */
   public void setBarCode( String aBarCode ) {
      barCode = aBarCode;
   }


   /**
    * Returns the value of the schedDeptTime property.
    *
    * @return the value of the schedDeptTime property
    */
   public Date getSchedDeptTime() {
      return iSchedDeptTime;
   }


   /**
    * Sets a new value for the schedDeptTime property.
    *
    * @param aSchedDeptTime
    *           the new value for the schedDeptTime property
    */
   public void setSchedDeptTime( Date aSchedDeptTime ) {
      iSchedDeptTime = aSchedDeptTime;
   }


   /**
    * Returns the value of the schedArrivalTime property.
    *
    * @return the value of the schedArrivalTime property
    */
   public Date getSchedArrivalTime() {
      return iSchedArrivalTime;
   }


   /**
    * Sets a new value for the schedArrivalTime property.
    *
    * @param aSchedArrivalTime
    *           the new value for the schedArrivalTime property
    */
   public void setSchedArrivalTime( Date aSchedArrivalTime ) {
      iSchedArrivalTime = aSchedArrivalTime;
   }


   /**
    * Returns the value of the actualDeptTime property.
    *
    * @return the value of the actualDeptTime property
    */
   public Date getActualDeptTime() {
      return iActualDeptTime;
   }


   /**
    * Sets a new value for the actualDeptTime property.
    *
    * @param aActualDeptTime
    *           the new value for the actualDeptTime property
    */
   public void setActualDeptTime( Date aActualDeptTime ) {
      iActualDeptTime = aActualDeptTime;
   }


   /**
    * Returns the value of the takeOffTime property.
    *
    * @return the value of the takeOffTime property
    */
   public Date getTimeOfTakeOff() {
      return iTakeOffTime;
   }


   /**
    * Sets a new value for the takeOffTime property.
    *
    * @param aTakeOffTime
    *           the new value for the takeOffTime property
    */
   public void setTimeOfTakeOff( Date aTakeOffTime ) {
      iTakeOffTime = aTakeOffTime;
   }


   /**
    * Returns the value of the arrivalLoc property.
    *
    * @return the value of the arrivalLoc property
    */
   public String getArrivalLoc() {
      return iArrivalLoc;
   }


   /**
    * Sets a new value for the arrivalLoc property.
    *
    * @param aArrivalLoc
    *           the new value for the arrivalLoc property
    */
   public void setArrivalLoc( String aArrivalLoc ) {
      iArrivalLoc = aArrivalLoc;
   }


   /**
    * Returns the value of the deptLoc property.
    *
    * @return the value of the deptLoc property
    */
   public String getDeptLoc() {
      return iDeptLoc;
   }


   /**
    * Sets a new value for the deptLoc property.
    *
    * @param aDeptLoc
    *           the new value for the deptLoc property
    */
   public void setDeptLoc( String aDeptLoc ) {
      iDeptLoc = aDeptLoc;
   }


   /**
    * Returns the value of the regCd property.
    *
    * @return the value of the regCd property
    */
   public String getRegCd() {
      return iRegCd;
   }


   /**
    * Sets a new value for the regCd property.
    *
    * @param aRegCd
    *           the new value for the regCd property
    */
   public void setRegCd( String aRegCd ) {
      iRegCd = aRegCd;
   }


   /**
    * Returns the value of the Capability Requirements.
    *
    * @return the value of the Capability Requirement
    */
   public CapabilityRequirements getCapabilityRequirements() {
      return iCapabilityRequirements;
   }


   /**
    * Sets a new value for the Capability Requirements.
    *
    * @param aCapabilityRequirements
    *           the new value for the Capability Requirements.
    */
   public void setCapabilityRequirements( CapabilityRequirements aCapabilityRequirements ) {
      iCapabilityRequirements = aCapabilityRequirements;
   }


   /**
    * Sets a new value for Actual Arrival Time
    *
    * @param Date
    */
   public void setActualArrivalTime( Date aDate ) {
      iActualArrivalTime = aDate;
   }


   /**
    * Get Down Time
    *
    * @return
    */
   public Date getDownTime() {
      return iDownTime;
   }


   /**
    * Get Down Time
    *
    * @return
    */
   public void setDownTime( Date aDownTime ) {
      iDownTime = aDownTime;
   }

}
