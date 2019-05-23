package com.mxi.mx.web.jsp.controller.todolist;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;


/**
 * This test class tests the {@link UnserviceableStagingTabHelper} class.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UnserviceableStagingTabHelperTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final OrgKey ORG_KEY = new OrgKey( 4650, 10001 );
   private static final String CONFIG_PARM = "ENABLE_READY_FOR_BUILD";

   private InventoryKey iInventoryAtUSSTGLocation;
   private InventoryKey iInventoryAtDockLocation;
   private InventoryKey iInventoryInConditionRFB;
   private InventoryKey iToolAtUSSTGLocation;
   private InventoryKey iToolAtDockLocation;

   private UnserviceableStagingTabHelper iHelper;


   @Before
   public void setUp() {

      LocationKey lSupplyLocation =
            new LocationDomainBuilder().withCode( "YYZ" ).isSupplyLocation().build();
      LocationKey lUSSTGLocation = new LocationDomainBuilder().withCode( "YYZ/USSTG" )
            .withType( RefLocTypeKey.USSTG ).withSupplyLocation( lSupplyLocation ).build();
      LocationKey lDockLocation = new LocationDomainBuilder().withCode( "YYZ/DOCK" )
            .withType( RefLocTypeKey.DOCK ).withSupplyLocation( lSupplyLocation ).build();

      OwnerKey lLocalOwner = new OwnerDomainBuilder().isDefault().build();

      iInventoryAtUSSTGLocation = new InventoryBuilder().atLocation( lUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      iInventoryAtDockLocation = new InventoryBuilder().atLocation( lDockLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      // Incomplete inventory in condition RFB - TRK or ASSY inventory with a inv_cond_cd of RFI are
      // shown as RFB in operator. Since there is no RFB condition, we have to use RFI inventory for
      // this test case
      iInventoryInConditionRFB = new InventoryBuilder().atLocation( lUSSTGLocation )
            .withComplete( false ).withClass( RefInvClassKey.TRK )
            .withCondition( RefInvCondKey.RFI ).withOwner( lLocalOwner ).build();

      // tool (with part use of TOOLS) at USSTG location
      PartNoKey lTool = new PartNoBuilder().isTool().build();
      iToolAtUSSTGLocation = new InventoryBuilder().withPartNo( lTool ).atLocation( lUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      // tool (with TSE assembly) at Dock location
      PartNoKey lToolByTSEAssembly = new PartNoBuilder().build();
      new PartGroupDomainBuilder( "TSEGROUP" ).withPartNo( lToolByTSEAssembly ).isToolGroup()
            .build();
      iToolAtDockLocation =
            new InventoryBuilder().withPartNo( lToolByTSEAssembly ).atLocation( lDockLocation )
                  .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      HumanResourceKey lHr =
            new HumanResourceDomainBuilder().withSupplyLocation( lSupplyLocation ).build();

      iHelper = new UnserviceableStagingTabHelper( ORG_KEY, lHr );

      // READY_FOR_BUILD is set to false by default, and set to true in the specific test cases
      // where RFB functionality is being tested
      setReadyForBuildConfigParam( false );

   }


   /**
    *
    * Test retrieving inventory only.
    */
   @Test
   public void testGetInventory() {

      boolean lShowInventory = true;
      boolean lShowTools = false;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      // ENABLE_READY_FOR_BUILD config parameter is false in this test case, so
      // iInventoryInConditionRFB should not be returned.
      assertInventoryReturned( lUnserviceableItems,
            Arrays.asList( iInventoryAtUSSTGLocation, iInventoryAtDockLocation ) );
   }


   /**
    *
    * Test retrieving tools only.
    */
   @Test
   public void testGetTools() {

      boolean lShowInventory = false;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems,
            Arrays.asList( iToolAtUSSTGLocation, iToolAtDockLocation ) );

   }


   /**
    *
    * Test retrieving inventory and tools.
    */
   @Test
   public void testGetInventoryAndTools() {

      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      // ENABLE_READY_FOR_BUILD config parameter is false in this test case, so
      // iInventoryInConditionRFB should not be returned.
      assertInventoryReturned( lUnserviceableItems, Arrays.asList( iInventoryAtUSSTGLocation,
            iInventoryAtDockLocation, iToolAtUSSTGLocation, iToolAtDockLocation ) );

   }


   /**
    *
    * Test filtering for items at unserviceable staging location.
    */
   @Test
   public void testShowOnlyUSSTGLocation() {

      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = false;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems,
            Arrays.asList( iInventoryAtUSSTGLocation, iToolAtUSSTGLocation ) );
   }


   /**
    *
    * Test filtering for items at other locations (where location type is not unserviceable
    * staging).
    */
   @Test
   public void testShowOnlyOtherLocation() {

      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = false;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems,
            Arrays.asList( iInventoryAtDockLocation, iToolAtDockLocation ) );
   }


   /**
    *
    * Test filtering for inventory at unserviceable staging location.
    */
   @Test
   public void testShowInventoryAtUSSTGLocation() {

      boolean lShowInventory = true;
      boolean lShowTools = false;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = false;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, iInventoryAtUSSTGLocation );
   }


   /**
    *
    * Test filtering for inventory at other locations (where location type is not unserviceable
    * staging).
    */
   @Test
   public void testShowInventoryAtOtherLocation() {

      boolean lShowInventory = true;
      boolean lShowTools = false;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = false;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, iInventoryAtDockLocation );
   }


   /**
    *
    * Test filtering for tools at unserviceable staging location.
    */
   @Test
   public void testShowToolsAtUSSTGLocation() {

      boolean lShowInventory = false;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = false;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, iToolAtUSSTGLocation );
   }


   /**
    *
    * Test filtering for tools at other locations (where location type is not unserviceable
    * staging).
    */
   @Test
   public void testShowToolsAtOtherLocation() {

      boolean lShowInventory = false;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = false;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, iToolAtDockLocation );
   }


   /**
    *
    * Test filtering for items at other locations (where location type is not unserviceable staging)
    * when there are only tools, no inventory.
    */
   @Test
   public void testShowOnlyOtherLocationWhenThereAreToolsButNoInventory() {

      LocationKey lSupplyLocation =
            new LocationDomainBuilder().withCode( "ATL" ).isSupplyLocation().build();
      LocationKey lUSSTGLocation = new LocationDomainBuilder().withCode( "ATL/USSTG" )
            .withType( RefLocTypeKey.USSTG ).withSupplyLocation( lSupplyLocation ).build();
      LocationKey lBinLocation = new LocationDomainBuilder().withCode( "ATL/BIN" )
            .withType( RefLocTypeKey.BIN ).withSupplyLocation( lSupplyLocation ).build();

      OwnerKey lLocalOwner = new OwnerDomainBuilder().isDefault().build();

      PartNoKey lTool = new PartNoBuilder().isTool().build();

      // tool at USSTG location
      new InventoryBuilder().withPartNo( lTool ).atLocation( lUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      // tool at BIN location
      InventoryKey lToolAtBinLocation =
            new InventoryBuilder().withPartNo( lTool ).atLocation( lBinLocation )
                  .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      HumanResourceKey lHr =
            new HumanResourceDomainBuilder().withSupplyLocation( lSupplyLocation ).build();

      UnserviceableStagingTabHelper lHelper = new UnserviceableStagingTabHelper( ORG_KEY, lHr );

      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = false;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = lHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, lToolAtBinLocation );
   }


   /**
    *
    * Test filtering for items at unserviceable staging location when there are only inventory, no
    * tools.
    */
   @Test
   public void testShowOnlyUSSTGLocationWhenThereIsInventoryButNoTools() {

      LocationKey lSupplyLocation =
            new LocationDomainBuilder().withCode( "ATL" ).isSupplyLocation().build();
      LocationKey lUSSTGLocation = new LocationDomainBuilder().withCode( "ATL/USSTG" )
            .withType( RefLocTypeKey.USSTG ).withSupplyLocation( lSupplyLocation ).build();
      LocationKey lDockLocation = new LocationDomainBuilder().withCode( "ATL/DOCK" )
            .withType( RefLocTypeKey.DOCK ).withSupplyLocation( lSupplyLocation ).build();

      OwnerKey lLocalOwner = new OwnerDomainBuilder().isDefault().build();

      // inventory at USSTG location
      InventoryKey lInventoryAtUSSTGLocation = new InventoryBuilder().atLocation( lUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( lLocalOwner ).build();

      // inventory at dock location
      new InventoryBuilder().atLocation( lDockLocation ).withCondition( RefInvCondKey.REPREQ )
            .withOwner( lLocalOwner ).build();

      HumanResourceKey lHr =
            new HumanResourceDomainBuilder().withSupplyLocation( lSupplyLocation ).build();

      UnserviceableStagingTabHelper lHelper = new UnserviceableStagingTabHelper( ORG_KEY, lHr );

      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = false;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      DataSet lUnserviceableItems = lHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, lInventoryAtUSSTGLocation );
   }


   /**
    *
    * Test filtering in the repair staging tab when all checkboxes are selected and config parameter
    * ENABLE_READY_FOR_BUILD is selected
    *
    */
   public void testShowRepreqAndRfbInventoryWhenConfigParmIsEnabled() {
      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = true;

      setReadyForBuildConfigParam( true );

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems,
            Arrays.asList( iInventoryAtUSSTGLocation, iInventoryAtDockLocation,
                  iToolAtUSSTGLocation, iToolAtDockLocation, iInventoryInConditionRFB ) );

   }


   /**
    *
    * Test filtering in the repair staging tab when all checkboxes except RFB are selected and
    * config parameter ENABLE_READY_FOR_BUILD is selected
    *
    */
   public void testShowRepreqInventoryAndToolsWhenConfigParmIsEnabled() {
      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = true;
      boolean lShowRfb = false;

      setReadyForBuildConfigParam( true );

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, Arrays.asList( iInventoryAtUSSTGLocation,
            iInventoryAtDockLocation, iToolAtUSSTGLocation, iToolAtDockLocation ) );

   }


   /**
    *
    * Test filtering in the repair staging tab when all checkboxes except Repreq are selected and
    * config parameter ENABLE_READY_FOR_BUILD is selected
    *
    */
   public void testShowRfbInventoryWhenConfigParmIsEnabled() {
      boolean lShowInventory = true;
      boolean lShowTools = true;
      String lPartType = null;
      boolean lShowOnlyVendorOwnedParts = false;
      boolean lShowUSSTGLocations = true;
      boolean lShowOtherLocations = true;
      boolean lShowRepreq = false;
      boolean lShowRfb = true;

      setReadyForBuildConfigParam( true );

      DataSet lUnserviceableItems = iHelper.getFilteredDataSet( lShowInventory, lShowTools,
            lPartType, lShowOnlyVendorOwnedParts, lShowUSSTGLocations, lShowOtherLocations,
            lShowRepreq, lShowRfb );

      assertInventoryReturned( lUnserviceableItems, Arrays.asList( iInventoryInConditionRFB ) );

   }


   /**
    * Assert that the given inventory item was returned in the data set.
    *
    * @param aRetrievedInventory
    *           the data set
    * @param aExpectedInventory
    *           the expected inventory item
    */
   private void assertInventoryReturned( DataSet aRetrievedInventory,
         InventoryKey aExpectedInventory ) {

      assertEquals( "Number of retrieved items", 1, aRetrievedInventory.getRowCount() );

      aRetrievedInventory.next();
      InventoryKey lActualInventory =
            aRetrievedInventory.getKey( InventoryKey.class, "inventory_key" );

      assertEquals( aExpectedInventory, lActualInventory );
   }


   /**
    * Assert that the given inventory items were returned in the data set.
    *
    * @param aRetrievedInventory
    *           the data set
    * @param aExpectedInventory
    *           the list of expected inventory items
    */
   private void assertInventoryReturned( DataSet aRetrievedInventory,
         List<InventoryKey> aExpectedInventory ) {

      assertEquals( "Number of retrieved items", aExpectedInventory.size(),
            aRetrievedInventory.getRowCount() );

      List<InventoryKey> lActualInventoryItems = new ArrayList<InventoryKey>();
      while ( aRetrievedInventory.next() ) {
         lActualInventoryItems
               .add( aRetrievedInventory.getKey( InventoryKey.class, "inventory_key" ) );
      }

      assertThat( lActualInventoryItems, containsInAnyOrder( aExpectedInventory.toArray() ) );
   }


   /**
    *
    * Sets the value for the ENABLE_READY_FOR_BUILD config parameters
    *
    * @param aValue
    *           the value that should be set
    */
   private void setReadyForBuildConfigParam( boolean aValue ) {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( CONFIG_PARM, aValue );
      GlobalParameters.setInstance( lConfigParms );
   }
}
