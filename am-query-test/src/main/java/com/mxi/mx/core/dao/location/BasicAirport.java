package com.mxi.mx.core.dao.location;

import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 *
 * A very simple airport for testing very simple supply chain scenarios
 *
 */
public class BasicAirport {

   /*
    * Maintenance is done at Line Maintenance, North Hanger, South Hanger.
    *
    * Line Store, NorthWarehouse, South Warehouse are preferred locations for Line Maintenance,
    * North Hanger, and South Hanger respectively.
    *
    * Line Store is an ignored location. Main Warehouse is a non-preferred location.
    */
   public static final String LINE_MAINTENANCE = "LINE MAINTENANCE";
   public static final String NORTH_HANGER = "NORTH HANGER";
   public static final String SOUTH_HANGER = "SOUTH HANGER";

   public static final String LINE_STORE = "LINE STORE";
   public static final String LINE_BIN_STORE = "LINE BIN STORE";
   public static final String NORTH_WAREHOUSE = "NORTH WAREHOUSE";
   public static final String SOUTH_WAREHOUSE = "SOUTH WAREHOUSE";
   public static final String MAIN_WAREHOUSE = "MAIN WAREHOUSE";

   // A supply location
   final public LocationKey iAirportLocation;

   final public LocationKey iLineMaintenance;
   final public LocationKey iNorthHangar;
   final public LocationKey iSouthHangar;

   final public LocationKey iLineStore;
   final public LocationKey iLineStoreBin;

   final public LocationKey iNorthWarehouse;
   final public LocationKey iSouthWarehouse;
   final public LocationKey iMainWarehouse;


   private BasicAirport(LocationKey aAirportLocation, LocationKey aLineMaintenance,
         LocationKey aNorthHangar, LocationKey aSouthHangar, LocationKey aLineStore,
         LocationKey aLineStoreBin, LocationKey aNorthWarehouse, LocationKey aSouthWarehouse,
         LocationKey aMainWarehouse) {
      iAirportLocation = aAirportLocation;
      iLineMaintenance = aLineMaintenance;
      iNorthHangar = aNorthHangar;
      iSouthHangar = aSouthHangar;
      iLineStore = aLineStore;
      iLineStoreBin = aLineStoreBin;
      iNorthWarehouse = aNorthWarehouse;
      iSouthWarehouse = aSouthWarehouse;
      iMainWarehouse = aMainWarehouse;
   }


   public static class Builder {

      private final String iAirportCode;


      public Builder() {
         this( "APC" );
      }


      private Builder(String aAirportCode) {
         iAirportCode = aAirportCode;
      }


      public Builder airportCode( String aAirportCode ) {
         return new Builder( aAirportCode );
      }


      public BasicAirport build() {

         LocationKey lAirportLocation = new LocationDomainBuilder().withCode( iAirportCode )
               .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

         LocationKey lLineStore = new LocationDomainBuilder().withCode( LINE_STORE )
               .withType( RefLocTypeKey.SRVSTORE ).withParent( lAirportLocation )
               .withSupplyLocation( lAirportLocation ).isIgnoredLocation().build();

         LocationKey lLineStoreBin =
               new LocationDomainBuilder().withCode( LINE_BIN_STORE ).withType( RefLocTypeKey.BIN )
                     .withParent( lLineStore ).withSupplyLocation( lAirportLocation ).build();

         LocationKey lNorthWarehouse =
               new LocationDomainBuilder().withCode( NORTH_WAREHOUSE ).withType( RefLocTypeKey.SRVSTORE )
                     .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();
         LocationKey lSouthWarehouse =
               new LocationDomainBuilder().withCode( SOUTH_WAREHOUSE ).withType( RefLocTypeKey.SRVSTORE )
                     .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

         LocationKey lLineMaintenance =
               new LocationDomainBuilder().withCode( LINE_MAINTENANCE ).withType( RefLocTypeKey.LINE )
                     .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation )
                     .withPrefLocations( lLineStore, lNorthWarehouse ).build();
         LocationKey lNorthHangar = new LocationDomainBuilder().withCode( NORTH_HANGER )
               .withType( RefLocTypeKey.HGR ).withParent( lAirportLocation )
               .withSupplyLocation( lAirportLocation ).withPrefLocations( lNorthWarehouse ).build();
         LocationKey lSouthHangar = new LocationDomainBuilder().withCode( SOUTH_HANGER )
               .withType( RefLocTypeKey.HGR ).withParent( lAirportLocation )
               .withSupplyLocation( lAirportLocation ).withPrefLocations( lSouthWarehouse ).build();
         LocationKey lMainWarehouse =
               new LocationDomainBuilder().withCode( MAIN_WAREHOUSE ).withType( RefLocTypeKey.SRVSTORE )
                     .withParent( lAirportLocation ).withSupplyLocation( lAirportLocation ).build();

         return new BasicAirport( lAirportLocation, lLineMaintenance, lNorthHangar, lSouthHangar,
               lLineStore, lLineStoreBin, lNorthWarehouse, lSouthWarehouse, lMainWarehouse );
      }
   }
}
