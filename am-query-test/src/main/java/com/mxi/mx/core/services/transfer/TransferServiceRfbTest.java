package com.mxi.mx.core.services.transfer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.table.inv.InvInvTable;


/**
 * This class test transfer services for RFB (ready for build) condition
 *
 * @author CSarLK
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TransferServiceRfbTest {

   private static final String USERNAME_TESTUSER = "testuser";
   private static final int USERID_TESTUSER = 999;
   private static final String AIRPORT = "ATL";
   private static final String SRVSTG_LOCATION = "SRVSTG_LOC";
   private static final String USSTG_LOCATION = "USSTG_LOC";
   private static final String PART_GROUP = "PART_GROUP_001";
   private static final String PART_NO = "PART_001";
   private static final String SERIAL_NO = "SN001";
   private static final String MANUFACTURER = "MANU_001";
   private static final String EXPENSE_ACCOUNT = "001";
   private static final String PARAMETER_NAME = "ENABLE_READY_FOR_BUILD";

   private static LocationKey iAirport = null;
   private static LocationKey iSrvstgLoc = null;
   private static LocationKey iUsstgLoc = null;
   private static PartNoKey iPartNo = null;
   private static PartGroupKey iPartGroup = null;
   private static ManufacturerKey iManufacturer = null;
   private static InventoryKey iInventory1 = null;
   private static InventoryKey iInventory2 = null;
   private static HumanResourceKey iHr;
   private static OwnerKey iOwner = null;

   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule();
   @ClassRule
   public static FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @ClassRule
   public static OperateAsUserRule iOperateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @BeforeClass
   public static void setup() {

      // create a human resource
      iHr = new HumanResourceDomainBuilder().withUsername( USERNAME_TESTUSER )
            .withUserId( USERID_TESTUSER ).build();

      // create account
      FncAccountKey lExpenceAccount = new AccountBuilder().withCode( EXPENSE_ACCOUNT )
            .withType( RefAccountTypeKey.EXPENSE ).build();

      // create manufacturer
      iManufacturer = new ManufacturerKey( 0, MANUFACTURER );

      // create owner
      iOwner = new OwnerBuilder().build();

      // create part group
      iPartGroup = new PartGroupDomainBuilder( PART_GROUP ).build();

      // location setup
      locationSetup();

      // create tracked part
      createTrackedPart();

      // create RFB (Ready For Build) Inventory part at Serviceable Staging
      // this inventory is used when config parameter ENABLE_READY_FOR_BUILD is true
      iInventory1 = createTrackedInventory();

      // create RFB (Ready For Build) Inventory part at Serviceable Staging
      // this inventory is used when config parameter ENABLE_READY_FOR_BUILD is false
      iInventory2 = createTrackedInventory();

   }


   /**
    * Create tracked inventory using tracked part for use in RFB related tests
    *
    */
   private static InventoryKey createTrackedInventory() {
      InventoryKey lInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aRfbInventoryBuilder ) {
                  aRfbInventoryBuilder.setPartNumber( iPartNo );
                  aRfbInventoryBuilder.setCondition( RefInvCondKey.RFI );
                  aRfbInventoryBuilder.setLocation( iSrvstgLoc );
                  aRfbInventoryBuilder.setSerialNumber( SERIAL_NO );
                  aRfbInventoryBuilder.setComplete( false );
                  aRfbInventoryBuilder.setIssued( true );
                  aRfbInventoryBuilder.setOwner( iOwner );
               }
            } );
      return lInventory;
   }


   /**
    * Creates tracked part
    *
    */
   private static void createTrackedPart() {
      // create tracked part Part
      iPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withOemPartNo( PART_NO ).withStatus( RefPartStatusKey.ACTV )
            .isAlternateIn( iPartGroup ).manufacturedBy( iManufacturer )
            .withFinancialType( RefFinanceTypeKey.EXPENSE ).withAverageUnitPrice( BigDecimal.ONE )
            .withTotalQuantity( BigDecimal.ONE ).withTotalValue( BigDecimal.ONE ).build();
   }


   /**
    * Creates location setup for all the tests
    *
    */
   private static void locationSetup() {
      // create airport location
      iAirport = createLocation( AIRPORT, RefLocTypeKey.AIRPORT );
      // create serviceable staging location
      iSrvstgLoc = createSubLocation( SRVSTG_LOCATION, RefLocTypeKey.SRVSTG, iAirport );
      // create unserviceable staging location
      iUsstgLoc = createSubLocation( USSTG_LOCATION, RefLocTypeKey.USSTG, iAirport );
   }


   /**
    * Creates location
    *
    * @param aLocationCd
    *           Location name
    *
    * @param aLocType
    *           location type
    */
   private static LocationKey createLocation( String aLocationCd, RefLocTypeKey aLocType ) {
      return new LocationDomainBuilder().withCode( aLocationCd ).withType( aLocType ).isSupplyLocation()
            .build();

   }


   /**
    * Creates sub location
    *
    * @param aLocationCd
    *           Location name
    *
    * @param aLocType
    *           location type
    *
    * @param aSupplyLocation
    *           supply location for the location being created
    */
   private static LocationKey createSubLocation( String aLocationCd, RefLocTypeKey aLocType,
         LocationKey aSupplyLocation ) {
      return new LocationDomainBuilder().withCode( aLocationCd ).withType( aLocType )
            .withSupplyLocation( aSupplyLocation ).build();

   }


   /**
    * Set the global configuration parameter value
    *
    * @param aValue
    *           boolean value for the configuration parameter
    *
    */
   private void setReadyForBuildConfigParam( boolean aValue ) {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( PARAMETER_NAME, aValue );
      GlobalParameters.setInstance( lConfigParms );
   }


   /**
    * perform the turn in operation of a inventory given
    *
    * @param aInventory
    *           Inventory key for the inventory to be turned in
    *
    * @param aLocation
    *           Inventory turn in location
    *
    * @param aInvCondition
    *           Inventory condition of the inventory to be turned in
    *
    */

   private void turnIn( InventoryKey aInventory, LocationKey aLocation,
         RefInvCondKey aInvCondition ) throws Exception {

      TurnInTO lTurnInTO = new TurnInTO();
      lTurnInTO.setInventory( aInventory );
      lTurnInTO.setTurnInQuantity( 1.0, "" );
      lTurnInTO.setCondition( aInvCondition, "" );
      lTurnInTO.setLocation( aLocation, "" );
      lTurnInTO.setCreditAccount( EXPENSE_ACCOUNT, "" );
      lTurnInTO.setSerialNo( SERIAL_NO, "" );
      lTurnInTO.setPartNo( PART_NO, "" );
      lTurnInTO.setManufacturer( MANUFACTURER, "" );

      new TransferService().turnIn( lTurnInTO, iHr );
   }


   /**
    * Test if an inventory in RFB (Ready For Build) condition is turned in at Unservieable Location
    * Above is valid only if configuration parameter ENABLE_READY_FOR_BUILD is set to true
    *
    * With above parameter enabled, sometimes inventories which are either in inventory class TRK or
    * ASSY but are incomplete, can be turned in as RFI. Then those inventories should be turned in
    * at USSTG location
    */
   @Test
   public void testRfbInventoryTurnInAtUsstgLocationWhenConfigParamEnabled() throws Exception {

      // change config parameter
      setReadyForBuildConfigParam( true );
      turnIn( iInventory1, iAirport, RefInvCondKey.RFI );

      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( iInventory1 );
      assertEquals( iUsstgLoc, lInvTable.getLocation() );
      assertEquals( RefInvCondKey.RFI, lInvTable.getInvCondCd() );
      assertEquals( false, lInvTable.isCompleteBool() );
   }


   /**
    * Test if an inventory in RFB (Ready For Build) condition is turned in at Serviceable Location
    * Above is valid only if configuration parameter ENABLE_READY_FOR_BUILD is set to false
    *
    * with above parameter disabled, inventories which are either in inventory class TRK or ASSY but
    * are incomplete, can be turned in as RFI. Then those inventories should be turned in at SRVSTG
    * location
    */
   @Test
   public void testRfbInventoryTurnInAtSrvstgLocationWhenConfigParamDisabled() throws Exception {

      // change config parameter
      setReadyForBuildConfigParam( false );
      turnIn( iInventory2, iAirport, RefInvCondKey.RFI );

      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( iInventory2 );
      assertEquals( iSrvstgLoc, lInvTable.getLocation() );
      assertEquals( RefInvCondKey.RFI, lInvTable.getInvCondCd() );
      assertEquals( false, lInvTable.isCompleteBool() );
   }


   /**
    * Tests that no putaway transfers are created for RFB inventory ( inventory is RFB when it has
    * RFI condition but complete_bool is false for TRK/ASSY type)
    *
    * @throws Exception
    */
   @Test
   public void testNoPutAwayGeneratedForRFBInventory() throws Exception {
      // set config parameter value to true
      setReadyForBuildConfigParam( true );

      // verify inventory created in the setup is RFI but incomplete
      InvInvTable lInvTable = InvInvTable.findByPrimaryKey( iInventory1 );
      assertEquals( RefInvCondKey.RFI, lInvTable.getInvCondCd() );
      assertEquals( false, lInvTable.isCompleteBool() );

      // Call the service object to create put away for RFB inventory
      TransferKey lTransfer =
            TransferService.createPutAway( iInventory1, "ABQ/STORE/BIN", 1.0, null, false );

      // Assert that no transfer was created
      assertEquals( null, lTransfer );
   }


   @After
   public void tearDown() {
      GlobalParameters.setInstance( ParmTypeEnum.LOGIC.name(), null );
   }
}
