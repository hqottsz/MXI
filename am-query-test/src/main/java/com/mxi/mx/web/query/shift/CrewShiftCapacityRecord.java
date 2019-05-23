package com.mxi.mx.web.query.shift;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;


/**
 * DOCUMENT_ME
 *
 */
public class CrewShiftCapacityRecord {

   Map<String, Object> iFields = new HashMap<>();


   /**
    * Returns the value of the totalCapacity property.
    *
    * @return the value of the totalCapacity property
    */
   public Double getTotalCapacity() {
      return ( Double ) iFields.get( "TotalCapacity" );
   }


   /**
    * Sets a new value for the totalCapacity property.
    *
    * @param aTotalCapacity
    *           the new value for the totalCapacity property
    */
   public CrewShiftCapacityRecord withTotalCapacity( Double aTotalCapacity ) {
      BigDecimal lRound = new BigDecimal( aTotalCapacity );
      setField( "TotalCapacity", lRound.setScale( 2, BigDecimal.ROUND_DOWN ) );
      return this;
   }


   /**
    * Returns the value of the availCapacity property.
    *
    * @return the value of the availCapacity property
    */
   public Double getAvailCapacity() {
      return ( Double ) iFields.get( "AvailCapacity" );
   }


   /**
    * Sets a new value for the availCapacity property.
    *
    * @param aAvailCapacity
    *           the new value for the availCapacity property
    */
   public CrewShiftCapacityRecord withAvailCapacity( Double aAvailCapacity ) {
      BigDecimal lRound = new BigDecimal( aAvailCapacity );
      setField( "AvailCapacity", lRound.setScale( 2, BigDecimal.ROUND_DOWN ) );
      return this;
   }


   /**
    * Returns the value of the OrgCrewShiftPlanKey property.
    *
    * @return the value of the OrgCrewShiftPlanKey property
    */
   public OrgCrewShiftPlanKey getOrgCrewShiftPlanKey() {
      return ( OrgCrewShiftPlanKey ) iFields.get( "OrgCrewShiftPlanKey" );
   }


   /**
    * Sets a new value for the crewKey property.
    *
    * @param aCrewKey
    *           the new value for the crewKey property
    */
   public CrewShiftCapacityRecord withCrewKey( DepartmentKey aCrewKey ) {
      setField( "CrewKey", aCrewKey );
      return this;
   }


   /**
    * Sets a new value for the OrgCrewShiftPlanKey property.
    *
    * @param aOrgCrewShiftPlanKey
    *           the new value for the OrgCrewShiftPlanKey property
    */
   public CrewShiftCapacityRecord
         withOrgCrewShiftPlanKey( OrgCrewShiftPlanKey aOrgCrewShiftPlanKey ) {

      setField( "OrgCrewShiftPlanKey", aOrgCrewShiftPlanKey );
      setField( "CrewKey", new DepartmentKey( aOrgCrewShiftPlanKey.getCrewDbId(),
            aOrgCrewShiftPlanKey.getCrewId() ) );

      return this;
   }


   /**
    * Returns the value of the deptCd property.
    *
    * @return the value of the deptCd property
    */
   public String getDeptCd() {
      return ( String ) iFields.get( "DeptCd" );
   }


   /**
    * Sets a new value for the deptCd property.
    *
    * @param aDeptCd
    *           the new value for the deptCd property
    */
   public CrewShiftCapacityRecord withDeptCd( String aDeptCd ) {
      setField( "DeptCd", aDeptCd );
      return this;
   }


   /**
    * Returns the value of the shiftCd property.
    *
    * @return the value of the shiftCdproperty
    */
   public String getShiftCd() {
      return ( String ) iFields.get( "ShiftCd" );
   }


   /**
    * Sets a new value for the shiftCd property.
    *
    * @param aShiftCd
    *           the new value for the shiftCd property
    */
   public CrewShiftCapacityRecord withShiftCd( String aShiftCd ) {
      setField( "ShiftCd", aShiftCd );
      return this;
   }


   /**
    * Returns the value of the dayDt property.
    *
    * @return the value of the dayDt property
    */
   public Date getDayDt() {
      return ( Date ) iFields.get( "DayDt" );
   }


   /**
    * Sets a new value for the dayDt property.
    *
    * @param aDayDt
    *           the new value for the dayDt property
    */
   public CrewShiftCapacityRecord withDayDt( Date aDayDt ) {
      setField( "DayDt", aDayDt );
      return this;
   }


   /**
    * Returns the value of the startHour property.
    *
    * @return the value of the startHour property
    */
   public Double getStartHour() {
      return ( Double ) iFields.get( "StartHour" );
   }


   /**
    * Sets a new value for the startHour property.
    *
    * @param aStartHour
    *           the new value for the startHour property
    */
   public CrewShiftCapacityRecord withStartHour( Double aStartHour ) {
      setField( "StartHour", aStartHour );
      return this;
   }


   /**
    * Returns the value of the endHour property.
    *
    * @return the value of the endHour property
    */
   public Double getEndHour() {
      return ( Double ) iFields.get( "EndHour" );
   }


   /**
    * Sets a new value for the endHour property.
    *
    * @param aEndHour
    *           the new value for the endHour property
    */
   public CrewShiftCapacityRecord withEndHour( Double aEndHour ) {
      setField( "EndHour", aEndHour );
      return this;
   }


   /**
    * DOCUMENT_ME
    *
    * @param aKey
    * @param aValue
    */
   private void setField( String aKey, Object aValue ) {
      if ( aValue == null ) {
         iFields.remove( aKey );
      } else {
         iFields.put( aKey, aValue );
      }

   }


   public CrewShiftCapacityRecord() {

   }


   public CrewShiftCapacityRecord(DataSet aDataSet) {
      withTotalCapacity( aDataSet.getDouble( "TOTAL_CAPACITY" ) );
      withAvailCapacity( aDataSet.getDouble( "AVAIL_CAPACITY" ) );
      withOrgCrewShiftPlanKey( aDataSet.getKey( OrgCrewShiftPlanKey.class, "CREW_SHIFT_KEY" ) );
      withDeptCd( aDataSet.getString( "CREW_CD" ) );
      withShiftCd( aDataSet.getString( "SHIFT_CD" ) );
      withDayDt( aDataSet.getDate( "SHIFT_DAY" ) );
      withStartHour( aDataSet.getDouble( "START_HOUR" ) );
      withEndHour( aDataSet.getDouble( "END_HOUR" ) );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return iFields.toString();
   }


   /**
    *
    * Verifies that every field set on this record is the same on aResults record. To compare only a
    * subset of fields, set only the expected fields on the object doing the comparing.
    *
    * @param aResults
    *           CrewShiftCapacityRecord to compare with.
    */
   public void assertEquals( CrewShiftCapacityRecord aResults ) {
      for ( Entry<String, Object> lFieldToCheck : iFields.entrySet() ) {
         try {
            Assert.assertEquals( lFieldToCheck.getValue(),
                  aResults.getField( lFieldToCheck.getKey() ) );
         } catch ( AssertionError ae ) {
            String aeMessage = ae.getMessage() + " for field " + lFieldToCheck.getKey();
            if ( aResults.getDayDt() != null ) {
               aeMessage += " on Crew Shift Day " + aResults.getDayDt();
            }
            throw new AssertionError( aeMessage, ae );
         }
      }
   }


   /**
    * Return the named field
    *
    * @param aFieldName
    *           name of the field
    * @return
    */
   private Object getField( String aFieldName ) {
      return iFields.get( aFieldName );
   }

}
