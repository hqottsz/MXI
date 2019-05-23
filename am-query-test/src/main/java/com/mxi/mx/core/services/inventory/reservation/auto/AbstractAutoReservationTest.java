package com.mxi.mx.core.services.inventory.reservation.auto;

import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartUtils.addPartToPartGroup;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartUtils.createPart;
import static com.mxi.mx.core.services.inventory.reservation.auto.CreatePartUtils.createPartGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.common.trigger.TriggerFactoryStub;
import com.mxi.mx.core.key.InvLocPrefMapKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.services.inventory.reservation.AutoReservationRequestHandlerFake;
import com.mxi.mx.core.services.inventory.reservation.InventoryReservationService;
import com.mxi.mx.core.table.inv.InvLocPrefMapTable;


/**
 * Abstract class which stores the common test behaviour for all autoreservation related unittests
 *
 */
public abstract class AbstractAutoReservationTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   protected static final RefEventStatusKey PROPEN = RefEventStatusKey.PROPEN;
   protected static final RefEventStatusKey PRREMOTE = RefEventStatusKey.PRREMOTE;
   protected static final RefEventStatusKey PRAVAIL = RefEventStatusKey.PRAVAIL;
   protected static final RefEventStatusKey PRINSPREQ = RefEventStatusKey.PRINSPREQ;

   protected static final RefInvCondKey RFI = RefInvCondKey.RFI;

   protected static final RefInvClassKey BATCH = RefInvClassKey.BATCH;
   protected static final RefInvClassKey SERIALIZED = RefInvClassKey.SER;

   public static OwnerKey iLocalOwner;
   public static OwnerKey iVendorOwner;

   protected PartNoKey iPartA;
   protected PartNoKey iPartAlternate;
   protected PartGroupKey iPartGroupA;

   // A Supply location which is a hub
   protected LocationKey iHubLocation;
   // A location under the hub supply location
   protected LocationKey iHubSrvStgLocation;
   // A supply location which is not a hub
   protected LocationKey iAirportLocation;
   // A location under the non-hub supply location
   protected LocationKey iLineLocation;
   protected LocationKey iPreferredStoreLocation;
   protected LocationKey iSecondPreferredStoreLocation;
   protected LocationKey iNonPreferredStoreLocation;


   /** Data: Auto Reservation Contexts */
   public static enum AutoReservationRequestMode {
      PART_NO, PART_GROUP
   };


   @DataPoints
   public static AutoReservationRequestMode[] iPartContexts = AutoReservationRequestMode.values();

   /** Data: Inventory classes */
   @DataPoints
   public static final RefInvClassKey[] iInventoryClasses =
         new RefInvClassKey[] { SERIALIZED, BATCH };

   /** Data: Part Request Priorities */
   @DataPoints
   public static final RefReqPriorityKey[] iPriorities =
         new RefReqPriorityKey[] { RefReqPriorityKey.NORMAL, RefReqPriorityKey.AOG };


   public static enum SupplyLocationMode {
      HUB, HUBSRVSTG, PREFSRVSTORE, SRVSTORE
   }

   public static enum DemandLocationMode {
      AIRPORT, LINE
   }


   /** Data: Locations */
   @DataPoints
   public static final SupplyLocationMode[] iSupplyLocationModes = SupplyLocationMode.values();
   @DataPoints
   public static final DemandLocationMode[] iDemandLocationModes = DemandLocationMode.values();

   /** Data: Supply or demand amounts */
   @DataPoints
   public static final Double[] iAmountOutOf10 = { 1.0, 3.0, 7.0 };


   /**
    * Return a date that is current date + aDaysAfterCurrentDate
    *
    * @param aDaysAfterCurrentDate
    *           days added to current date
    *
    * @return a date within the auto-reservation range
    */
   public static Date getDate( int aDaysAfterCurrentDate ) {
      return DateUtils.addDays( new Date(), aDaysAfterCurrentDate );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {

      // Hub locations should not have their hub set
      iHubLocation = new LocationDomainBuilder().withCode( SupplyLocationMode.HUB.name() )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      iHubSrvStgLocation = new LocationDomainBuilder().withCode( SupplyLocationMode.HUBSRVSTG.name() )
            .withType( RefLocTypeKey.SRVSTG ).withParent( iHubLocation )
            .withSupplyLocation( iHubLocation ).build();

      iAirportLocation = new LocationDomainBuilder().withCode( DemandLocationMode.AIRPORT.name() )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().withHubLocation( iHubLocation )
            .build();

      // The preferred warehouse location
      iPreferredStoreLocation = new LocationDomainBuilder()
            .withCode( SupplyLocationMode.PREFSRVSTORE.name() ).withType( RefLocTypeKey.SRVSTORE )
            .withParent( iAirportLocation ).withSupplyLocation( iAirportLocation ).build();

      // Only supply locations have their hub set
      iLineLocation = new LocationDomainBuilder().withCode( DemandLocationMode.LINE.name() )
            .withType( RefLocTypeKey.LINE ).withParent( iAirportLocation )
            .withSupplyLocation( iAirportLocation ).withPrefLocations( iPreferredStoreLocation )
            .build();

      // A non-preferred warehouse location
      iNonPreferredStoreLocation = new LocationDomainBuilder()
            .withCode( SupplyLocationMode.SRVSTORE.name() ).withType( RefLocTypeKey.SRVSTORE )
            .withParent( iAirportLocation ).withSupplyLocation( iAirportLocation ).build();

      iLocalOwner = new OwnerDomainBuilder().isDefault().build();
      iVendorOwner = new OwnerDomainBuilder().isDefault().isNonLocal().build();

      // Inject the am-query-testable AutoReservationRequestHandlerFake
      Map<String, Object> lTriggerMap = new HashMap<String, Object>();
      lTriggerMap.put( "MX_IN_AUTO_RESERVATION", new AutoReservationRequestHandlerFake() );
      TriggerFactory.setInstance( new TriggerFactoryStub( lTriggerMap ) );
   }


   /**
    * add second preferred store location for Line maintenance location
    *
    */
   protected void addSecondPreferredStoreLocation() {
      // create second preferred location
      iSecondPreferredStoreLocation =
            new LocationDomainBuilder().withCode( "PREFSRVSTORE2" ).withType( RefLocTypeKey.SRVSTORE )
                  .withParent( iAirportLocation ).withSupplyLocation( iAirportLocation ).build();

      // add the the second preferred store location for Line maintenance location
      InvLocPrefMapKey lInvLocPrefMapKey =
            new InvLocPrefMapKey( iLineLocation, iSecondPreferredStoreLocation );
      InvLocPrefMapTable lInvLocPrefMapTable = InvLocPrefMapTable.create( lInvLocPrefMapKey );
      lInvLocPrefMapTable.setPriorityOrder( 2 );
      lInvLocPrefMapTable.insert();
   }


   /**
    *
    * Set up a part group and parts in the scope of this class
    *
    */
   protected void setUpTheoryParts( RefInvClassKey aInvClassKey ) {

      iPartA = createPart( "part A", aInvClassKey );
      iPartAlternate = createPart( "part Alternate", aInvClassKey );
      iPartGroupA = createPartGroup( "group A", aInvClassKey );
      addPartToPartGroup( iPartA, iPartGroupA, true );
      addPartToPartGroup( iPartAlternate, iPartGroupA, false );
   }


   /**
    * Calls the auto-reservation service method for either a part group or a part number depending
    * on the given context.
    *
    * @param aContext
    *           the context (part group or part number)
    * @param aService
    *           the auto reservation service
    * @param aPartGroup
    *           the part group
    * @param aPartNo
    *           the part number
    * @param aLocation
    *           the location
    * @throws TriggerException
    */
   protected void callAutoReservationLogic( LocationKey aLocation,
         AutoReservationRequestMode aMode ) throws TriggerException {
      switch ( aMode ) {
         case PART_NO:
            InventoryReservationService.autoReserveInventory( iPartA, aLocation );
            break;
         case PART_GROUP:
            InventoryReservationService.autoReserveInventory( iPartGroupA, aLocation );
            break;
      }
   }
}
