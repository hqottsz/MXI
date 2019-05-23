package com.mxi.am.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


public final class Flight {

   private InventoryKey iAircraft;
   private LocationKey iArrivalLocation;
   private LocationKey iDepartureLocation;
   private String iName;
   private Date iScheduledArrivalDate;
   private Date iScheduledDepartureDate;
   private Date iArrivalDate;
   private Date iDepartureDate;
   private String iExternalKey;
   private boolean iHistorical;
   private FlightLegStatus iStatus;

   private UsageAdjustmentId iUsageRecord;
   private List<UsageInfo> iUsages = new ArrayList<UsageInfo>();


   Flight() {
   }


   public LocationKey getArrivalLocation() {
      return iArrivalLocation;
   }


   public void setArrivalLocation( LocationKey aArrivalLocation ) {
      iArrivalLocation = aArrivalLocation;
   }


   public LocationKey getDepartureLocation() {
      return iDepartureLocation;
   }


   public void setDepartureLocation( LocationKey aDepartureLocation ) {
      iDepartureLocation = aDepartureLocation;
   }


   public String getName() {
      return iName;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public Date getScheduledArrivalDate() {
      return iScheduledArrivalDate;
   }


   public void setScheduledArrivalDate( Date aScheduledArrivalDate ) {
      iScheduledArrivalDate = aScheduledArrivalDate;
   }


   public Date getScheduledDepartureDate() {
      return iScheduledDepartureDate;
   }


   public void setScheduledDepartureDate( Date aScheduledDepartureDate ) {
      iScheduledDepartureDate = aScheduledDepartureDate;
   }


   public void setArrivalDate( Date aArrivalDate ) {
      iArrivalDate = aArrivalDate;
   }


   public Date getArrivalDate() {
      return iArrivalDate;
   }


   public void setDepartureDate( Date aDepartureDate ) {
      iDepartureDate = aDepartureDate;
   }


   public Date getDepartureDate() {
      return iDepartureDate;
   }


   public void setAircraft( InventoryKey aAircraft ) {
      iAircraft = aAircraft;
   }


   public InventoryKey getAircraft() {
      return iAircraft;
   }


   public void setExternalKey( String aExternalKey ) {
      iExternalKey = aExternalKey;
   }


   public String getExternalKey() {
      return iExternalKey;
   }


   public void setHistorical( boolean aHistorical ) {
      iHistorical = aHistorical;
   }


   public boolean isHistorical() {
      return iHistorical;
   }


   public void setUsageRecord( UsageAdjustmentId aUsageRecord ) {
      iUsageRecord = aUsageRecord;
   }


   public UsageAdjustmentId getUsageRecord() {
      return iUsageRecord;
   }


   public void addUsage( InventoryKey aInventory, DataTypeKey aType, BigDecimal aValue,
         BigDecimal aTsn ) {
      iUsages.add( new UsageInfo( aInventory, aType, aValue, aTsn ) );
   }


   public void addUsage( InventoryKey aInventory, DataTypeKey aType, BigDecimal aValue ) {
      addUsage( aInventory, aType, aValue, null );
   }


   public List<UsageInfo> getUsages() {
      return iUsages;
   }


   public FlightLegStatus getStatus() {
      return iStatus;
   }


   public void setStatus( FlightLegStatus aStatus ) {
      iStatus = aStatus;
   }


   public class UsageInfo {

      private InventoryKey iInventory;
      private DataTypeKey iType;
      private BigDecimal iValue;
      private BigDecimal iTsn;


      public UsageInfo(InventoryKey aInventory, DataTypeKey aType, BigDecimal aValue,
            BigDecimal aTsn) {
         iInventory = aInventory;
         iType = aType;
         iValue = aValue;
         iTsn = aTsn;
      }


      public InventoryKey getInventory() {
         return iInventory;
      }


      public DataTypeKey getType() {
         return iType;
      }


      public BigDecimal getValue() {
         return iValue;
      }


      public BigDecimal getTsn() {
         return iTsn;
      }

   }
}
