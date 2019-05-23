package com.mxi.am.domain.builder;

import java.util.ArrayList;
import java.util.List;

import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.InvLocPrefMapKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.table.inv.InvLocPrefMapTable;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * Builds a <code>inv_loc</code> object
 *
 * @deprecated - Please use {@linkplain LocationBuilder}
 */
@Deprecated
public class LocationDomainBuilder implements DomainBuilder<LocationKey> {

   private String iCode;
   private LocationKey iHubLocation;
   private boolean iIsSupplyLocation;
   private boolean iIsIgnoredLocation;
   private RefLocTypeKey iLocationType;
   private String iName;
   private ShiftKey iOvernightShift;
   private LocationKey iParent;
   private LocationKey iSupplyLocation;
   private TimeZoneKey iTimeZone;
   private boolean iIsDefaultDock = false;

   private List<LocationKey> iPrefLocationKeys = new ArrayList<>();


   /**
    * {@inheritDoc}
    */
   @Override
   public LocationKey build() {
      LocationKey lLocationKey = generatePrimaryKey();
      InvLocTable lInvLoc = InvLocTable.create( lLocationKey );

      lInvLoc.setLocType( iLocationType );
      lInvLoc.setLocCd( iCode );
      lInvLoc.setLocName( iName );
      lInvLoc.setOvernightShift( iOvernightShift );
      lInvLoc.setTimeZoneCd( iTimeZone );
      lInvLoc.setHubLocation( iHubLocation );
      lInvLoc.setNHLocation( iParent );

      if ( iIsSupplyLocation ) {
         lInvLoc.setSupplyLoc( lLocationKey );
         lInvLoc.setSupplyBool( true );
      } else {
         lInvLoc.setSupplyLoc( iSupplyLocation );
         if ( iIsIgnoredLocation ) {
            lInvLoc.setNoAutoRsrvBool( true );
         }
      }

      lInvLoc.setDefaultDock( iIsDefaultDock );
      lInvLoc.insert();

      // set preferred locations if any for the location
      int lOrder = 1;
      for ( LocationKey lPrefLocationKey : iPrefLocationKeys ) {
         InvLocPrefMapKey lInvLocPrefMapKey =
               new InvLocPrefMapKey( lLocationKey, lPrefLocationKey );

         InvLocPrefMapTable lInvLocPrefMapTable = InvLocPrefMapTable.create( lInvLocPrefMapKey );
         lInvLocPrefMapTable.setPriorityOrder( lOrder++ );
         lInvLocPrefMapTable.insert();
      }

      return lLocationKey;
   }


   /**
    * Sets the default dock flag
    *
    * @param aValue
    *           The value to set
    *
    * @return The builder
    */
   public LocationDomainBuilder isDefaultDock( boolean aValue ) {
      iIsDefaultDock = aValue;
      return this;
   }


   public LocationDomainBuilder isSupplyLocation() {
      iIsSupplyLocation = true;

      return this;
   }


   public LocationDomainBuilder isIgnoredLocation() {
      iIsIgnoredLocation = true;

      return this;
   }


   /**
    * Sets the location code.
    *
    * @param aCode
    *           The code
    *
    * @return The builder
    */
   public LocationDomainBuilder withCode( String aCode ) {
      iCode = aCode;

      return this;
   }


   /**
    * Sets the hub location
    *
    * @param aHubLocation
    *           The hub location
    *
    * @return The builder
    */
   public LocationDomainBuilder withHubLocation( LocationKey aHubLocation ) {
      iHubLocation = aHubLocation;

      return this;
   }


   /**
    *
    * Set preferred locations
    *
    * @param aLocationKeys
    * @return
    */
   public LocationDomainBuilder withPrefLocations( LocationKey... aLocationKeys ) {

      for ( LocationKey lPrefLoc : aLocationKeys ) {
         iPrefLocationKeys.add( lPrefLoc );
      }

      return this;
   }


   /**
    * Sets the location name.
    *
    * @param aName
    *           The name
    *
    * @return The builder
    */
   public LocationDomainBuilder withName( String aName ) {
      iName = aName;

      return this;
   }


   /**
    * Sets the overnight shift.
    *
    * @param aShift
    *           The shift.
    *
    * @return The builder.
    */
   public LocationDomainBuilder withOvernightShift( ShiftKey aShift ) {
      iOvernightShift = aShift;

      return this;
   }


   /**
    * Sets the location code.
    *
    * @param aParent
    *           The code
    *
    * @return The builder
    */
   public LocationDomainBuilder withParent( LocationKey aParent ) {
      iParent = aParent;

      return this;
   }


   /**
    * Sets the supply location
    *
    * @param aSupplyLocation
    *           The supply location
    *
    * @return The builder
    */
   public LocationDomainBuilder withSupplyLocation( LocationKey aSupplyLocation ) {
      iSupplyLocation = aSupplyLocation;

      return this;
   }


   /**
    * Sets the time zone.
    *
    * @param aTimeZone
    *
    * @return The builder.
    */
   public LocationDomainBuilder withTimeZone( TimeZoneKey aTimeZone ) {
      iTimeZone = aTimeZone;

      return this;
   }


   /**
    * Sets the location type.
    *
    * @param aLocationType
    *           The type
    *
    * @return The builder
    */
   public LocationDomainBuilder withType( RefLocTypeKey aLocationType ) {
      iLocationType = aLocationType;

      return this;
   }


   /**
    * Generates a new Location primary key
    *
    * @return New LocationKey
    */
   public static LocationKey generatePrimaryKey() {
      try {

         // Get the next id from the sequence
         int lLocationDbId = Table.Util.getDatabaseId();
         int lNextLocationId =
               EjbFactory.getInstance().createSequenceGenerator().nextValue( "INV_LOC_ID" );

         // Instantiate the table primary key as a proper key
         return new LocationKey( lLocationDbId, lNextLocationId );
      } catch ( Exception lException ) {
         throw new MxRuntimeException( "Could not generate Location id", lException );
      }
   }

}
