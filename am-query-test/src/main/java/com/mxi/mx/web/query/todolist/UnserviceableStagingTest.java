package com.mxi.mx.web.query.todolist;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Location;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.PartRequirement.PartRemovalRequest;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementBuilder;
import com.mxi.am.domain.builder.PartVendorBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.EqpPartVendorExchgKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OrgVendorPoTypeKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefVendorStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskRmvdPartKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.eqp.EqpPartVendorExchgTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgVendorPoTypeTable;


/**
 * This test class tests the UnserviceableStaging query which is used to populate the inventory and
 * tools on the Unserviceable Staging Tab.E
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class UnserviceableStagingTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final OrgKey ORG_KEY = new OrgKey( 4650, 10001 );
   private static String PARAMETER_NAME = "ENABLE_READY_FOR_BUILD";
   private static String INVENTORY_KEY = "inventory_key";
   private static String WO_KEY = "wo_key";
   private static String ORDER_KEY = "order_key";
   private static String ORDER_NUMBER = "order_number";
   private static String VENDOR_OR_SHOP_KEY = "vendor_or_shop_key";
   private static String VENDOR_OR_SHOP_CD = "vendor_or_shop_cd";
   private static String VENDOR_OR_SHOP_TYPE = "vendor_or_shop_type";

   private static String REMOVAL_WO_KEY = "removal_wo_key";
   private static String REMOVAL_TASK_KEY = "removal_task_key";
   private static String REMOVAL_REASON = "removal_reason";

   private HumanResourceKey iHumanResource;
   private LocationKey iSupplyLocation;
   private LocationKey iUSSTGLocation;
   private LocationKey iLineLocation;
   private OwnerKey iLocalOwner;
   private PartNoKey iTool;


   @Before
   public void setUp() {

      iSupplyLocation = Domain.createLocation( aSupplyLocation -> {
         aSupplyLocation.setCode( "YYZ" );
         aSupplyLocation.setIsSupplyLocation( true );
      } );

      iUSSTGLocation = Domain.createLocation( aUSSTGLocation -> {
         aUSSTGLocation.setCode( "YYZ/USSTG" );
         aUSSTGLocation.setType( RefLocTypeKey.USSTG );
         aUSSTGLocation.setSupplyLocation( iSupplyLocation );
      } );

      iLineLocation = Domain.createLocation( aLineLocationn -> {
         aLineLocationn.setCode( "YYZ/LINE" );
         aLineLocationn.setType( RefLocTypeKey.LINE );
         aLineLocationn.setSupplyLocation( iSupplyLocation );
      } );

      iLocalOwner = Domain.createOwner();

      iHumanResource = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setSupplyLocations( iSupplyLocation );
      } );

      iTool = Domain.createPart( aTool -> aTool.setTool( true ) );

   }


   /**
    * Execute the query.
    *
    * @return the data set returned by the query
    */
   public DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( ORG_KEY, "aOrgDbId", "aOrgId" );
      lArgs.add( iHumanResource, "aHrDbId", "aHrId" );
      lArgs.add( "aShowInventory", true );
      lArgs.add( "aShowTools", true );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   @Test
   public void testInventoryAndToolsAreReturned() {

      InventoryKey lInventory = createInventoryWithCondition( RefInvCondKey.REPREQ );

      // create a tool with part use of TOOLS
      PartNoKey lTool = new PartNoBuilder().isTool().build();
      InventoryKey lTool1 = new InventoryBuilder().withPartNo( lTool ).atLocation( iUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();

      // create a tool with a default part group that also belongs to a tool group (in the TSE
      // assembly)
      PartNoKey lToolByTSEAssembly = new PartNoBuilder().withDefaultPartGroup().build();
      new PartGroupDomainBuilder( "TSEGROUP" ).withPartNo( lToolByTSEAssembly ).isToolGroup()
            .build();
      InventoryKey lTool2 =
            new InventoryBuilder().withPartNo( lToolByTSEAssembly ).atLocation( iUSSTGLocation )
                  .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();

      DataSet lUnserviceableItems = execute();

      assertItemsReturned( lUnserviceableItems, Arrays.asList( lInventory, lTool1, lTool2 ) );
   }


   @Test
   public void testOnlyREPREQInventoryIsReturned() {

      InventoryKey lInventoryREPREQ = createInventoryWithCondition( RefInvCondKey.REPREQ );

      // RFI inventory
      createInventoryWithCondition( RefInvCondKey.RFI );

      // INSPREQ inventory
      createInventoryWithCondition( RefInvCondKey.INSPREQ );

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lInventoryREPREQ, lUnserviceableInventoryItems );
   }


   @Test
   public void testRepairOrderAndRemovalInfoRetrieved() {
      String VENDOR_CODE = "TEST VENDOR";
      String ORDER_DESC = "REPAIR ORDER";
      String TASK_NAME = "REMOVAL TASK";

      // GIVEN - prepare test data
      // part no and REPREQ inventory
      PartNoKey lPartNoKey = new PartNoBuilder().build();
      InventoryKey lInventoryREPREQ =
            new InventoryBuilder().withPartNo( lPartNoKey ).atLocation( iUSSTGLocation )
                  .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();

      // work package on REPREQ inventory at unserviceable staging location
      TaskKey lWorkPackage = new TaskBuilder().atLocation( iUSSTGLocation )
            .onInventory( lInventoryREPREQ ).withTaskClass( RefTaskClassKey.RO ).build();

      // vendor, vendor PO type, part repair vendor
      VendorKey lVendorKey = new VendorBuilder().withCode( VENDOR_CODE ).build();

      // assign an approved, preferred vendor to the part
      new PartVendorBuilder( lVendorKey, lPartNoKey ).isPreferred( true )
            .withVendorStatus( RefVendorStatusKey.APPROVED ).build();

      OrgVendorPoTypeKey lOrgVendorPoTypeKey =
            new OrgVendorPoTypeKey( ORG_KEY, lVendorKey, RefPoTypeKey.REPAIR );
      OrgVendorPoTypeTable lOrgVendorPoTypeTable =
            OrgVendorPoTypeTable.create( lOrgVendorPoTypeKey );
      lOrgVendorPoTypeTable.setVendorStatus( RefVendorStatusKey.APPROVED );
      lOrgVendorPoTypeTable.insert();

      // add part repair vendor
      addPartRepairVendor( lPartNoKey, lVendorKey, RefVendorStatusKey.APPROVED );

      // add repair order
      PurchaseOrderKey lRepairOrder = new OrderBuilder().withDescription( ORDER_DESC )
            .withOrderType( RefPoTypeKey.REPAIR ).withVendor( lVendorKey ).build();

      // add order line
      new OrderLineBuilder( lRepairOrder ).forTask( lWorkPackage ).withOwner( iLocalOwner )
            .withLineType( RefPoLineTypeKey.REPAIR ).withOrderQuantity( BigDecimal.ONE )
            .forPart( lPartNoKey ).build();

      // ACFT part no, ACFT inventory
      PartNoKey lAcftPartNoKey =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.ACFT ).build();
      InventoryKey lAcftInventory = new InventoryBuilder().withPartNo( lAcftPartNoKey ).build();

      // work package on the aircraft
      TaskKey lAcftWorkPackage = new TaskBuilder().onInventory( lAcftInventory ).build();

      // removal task
      TaskKey lRemoveTask = new TaskBuilder().withName( TASK_NAME )
            .withTaskClass( RefTaskClassKey.ADHOC ).withParentTask( lAcftWorkPackage ).build();

      // part requirement to remove the REPREQ inventory
      PartRequirement lPartRequirement = new PartRequirement();

      PartRemovalRequest lPartRemovalRequest = lPartRequirement.new PartRemovalRequest();
      lPartRemovalRequest.withInventory( lInventoryREPREQ ).withPartNo( lPartNoKey );

      lPartRequirement.setTaskKey( lRemoveTask );
      lPartRequirement.setPartRemovalRequest( lPartRemovalRequest );

      TaskPartKey lPartRequirementKey = PartRequirementBuilder.build( lPartRequirement );

      // removed part
      TaskRmvdPartKey lRmvdPart = new TaskRmvdPartKey( lPartRequirementKey, 1 );

      // REPREQ inventory updated with the removed part
      InvInvTable lInventoryTable = InvInvTable.findByPrimaryKey( lInventoryREPREQ );
      lInventoryTable.setTaskRmvdPart( lRmvdPart );
      lInventoryTable.update();

      // WHEN - execute query
      DataSet lUnserviceableInventoryItems = execute();

      // THEN - assertions
      assertItemReturnedWithDetailedInfo( lInventoryREPREQ, lWorkPackage, lRepairOrder, ORDER_DESC,
            lVendorKey, VENDOR_CODE, RefPoTypeKey.REPAIR.getCd(), lAcftWorkPackage, lRemoveTask,
            RefRemoveReasonKey.IMSCHD, lUnserviceableInventoryItems );
   }


   @Test
   public void testExchangeOrderAndVendorInfoRetrieved() {
      String VENDOR_CODE = "TEST VENDOR";
      String ORDER_DESC = "EXCHANGE ORDER";

      // GIVEN - prepare test data
      // part no and REPREQ inventory
      PartNoKey lPartNoKey = new PartNoBuilder().build();
      InventoryKey lInventoryREPREQ =
            new InventoryBuilder().withPartNo( lPartNoKey ).atLocation( iUSSTGLocation )
                  .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();

      // vendor, vendor PO type, part exchange vendor
      VendorKey lVendorKey = new VendorBuilder().withCode( VENDOR_CODE ).build();

      // assign an approved, preferred vendor to the part
      new PartVendorBuilder( lVendorKey, lPartNoKey ).isPreferred( true )
            .withVendorStatus( RefVendorStatusKey.APPROVED ).build();

      OrgVendorPoTypeKey lOrgVendorPoTypeKey =
            new OrgVendorPoTypeKey( ORG_KEY, lVendorKey, RefPoTypeKey.EXCHANGE );
      OrgVendorPoTypeTable lOrgVendorPoTypeTable =
            OrgVendorPoTypeTable.create( lOrgVendorPoTypeKey );
      lOrgVendorPoTypeTable.setVendorStatus( RefVendorStatusKey.APPROVED );
      lOrgVendorPoTypeTable.insert();

      EqpPartVendorExchgKey lEqpPartVendorExchKey =
            new EqpPartVendorExchgKey( lPartNoKey, lVendorKey );
      EqpPartVendorExchgTable lEqpPartVendorRepTable = EqpPartVendorExchgTable.create();
      lEqpPartVendorRepTable.setVendorStatus( RefVendorStatusKey.APPROVED );
      lEqpPartVendorRepTable.insert( lEqpPartVendorExchKey );

      // exchange order, order line
      PurchaseOrderKey lExchangeOrder = new OrderBuilder().withDescription( ORDER_DESC )
            .withOrderType( RefPoTypeKey.EXCHANGE ).withVendor( lVendorKey )
            .withAuthStatus( RefPoAuthLvlStatusKey.APPROVED ).build();

      PurchaseOrderLineKey lExchangeOrderLine = new OrderLineBuilder( lExchangeOrder )
            .withOwner( iLocalOwner ).withLineType( RefPoLineTypeKey.EXCHANGE )
            .withOrderQuantity( BigDecimal.ONE ).forPart( lPartNoKey ).build();

      // add return inventory mapping for exchange order
      addReturnInventoryForExchange( lExchangeOrderLine, lInventoryREPREQ );

      // WHEN - execute query
      DataSet lUnserviceableInventoryItems = execute();

      // THEN - assertions
      assertItemReturnedWithDetailedInfo( lInventoryREPREQ, null, lExchangeOrder, ORDER_DESC,
            lVendorKey, VENDOR_CODE, RefPoTypeKey.EXCHANGE.getCd(), null, null, null,
            lUnserviceableInventoryItems );

   }


   /*
    * When Repair Shop locations are defined for the part, then REPREQ inventory should be returned
    * with SHOP info
    */
   @Test
   public void testRepairShopLocationInfoRetrieved() {

      String SHOP_CODE = "SHOPLOC";

      // GIVEN - prepare test data
      // part no and REPREQ inventory
      PartNoKey lPartNoKey = new PartNoBuilder().build();
      InventoryKey lInventoryREPREQ =
            new InventoryBuilder().withPartNo( lPartNoKey ).atLocation( iUSSTGLocation )
                  .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();

      // create shop location for part repair
      Location lShopLocation = new Location();
      lShopLocation.setCode( SHOP_CODE );
      lShopLocation.setType( new RefLocTypeKey( 0, "SHOP" ) );
      lShopLocation.setSupplyLocation( iSupplyLocation );
      LocationKey lShop = LocationBuilder.build( lShopLocation );
      addShopLocationForPartRepair( lPartNoKey, lShop );

      // WHEN - execute query
      DataSet lUnserviceableInventoryItems = execute();

      // THEN - assertions
      assertEquals( "item is returned", 1, lUnserviceableInventoryItems.getRowCount() );
      lUnserviceableInventoryItems.next();
      assertEquals( lInventoryREPREQ,
            lUnserviceableInventoryItems.getKey( InventoryKey.class, INVENTORY_KEY ) );
      assertEquals( lShop,
            lUnserviceableInventoryItems.getKey( LocationKey.class, VENDOR_OR_SHOP_KEY ) );
      assertEquals( SHOP_CODE, lUnserviceableInventoryItems.getString( VENDOR_OR_SHOP_CD ) );
      assertEquals( "SHOP", lUnserviceableInventoryItems.getString( VENDOR_OR_SHOP_TYPE ) );
   }


   /* When config param is disabled only REPREQ inventory should be returned, not RFB inventory */
   @Test
   public void testOnlyREPREQInventoryIsReturnedWhenRFBDisabled() {

      // Disable the config param
      setReadyForBuildConfigParam( false );

      // REPREQ inventory
      InventoryKey lInventoryREPREQ = createInventoryWithCondition( RefInvCondKey.REPREQ );

      // RFB inventory
      createRFBInventory();

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lInventoryREPREQ, lUnserviceableInventoryItems );
   }


   /*
    * When config param is enabled both REPREQ and RFB parts should be returned. RFB parts are RFI
    * parts with complete_bool false and Inventory class TRK
    */
   @Test
   public void testBothREPREQRFBInventoryReturnedWhenRFBEnabled() {

      // Enable the config param
      setReadyForBuildConfigParam( true );

      InventoryKey lInventoryREPREQ = createInventoryWithCondition( RefInvCondKey.REPREQ );

      // RFI inventory with complete false and inventory class SER
      new InventoryBuilder().atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.RFI )
            .withComplete( false ).withClass( RefInvClassKey.SER ).withOwner( iLocalOwner ).build();

      // RFI inventory with complete true and inventory class TRK
      new InventoryBuilder().atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.RFI )
            .withComplete( true ).withClass( RefInvClassKey.TRK ).withOwner( iLocalOwner ).build();

      // RFB inventory
      InventoryKey lInventoryRFB = createRFBInventory();

      DataSet lUnserviceableInventoryItems = execute();

      assertItemsReturned( lUnserviceableInventoryItems,
            Arrays.asList( lInventoryRFB, lInventoryREPREQ ) );
   }


   /*
    * When config param is enabled. Only RFB inventory should be returned, NOT tools with RFB equal
    * conditions
    */
   @Test
   public void testOnlyRFBInventoryReturnedAndNotToolsWhenRFBEnabled() {

      // Enable the config param
      setReadyForBuildConfigParam( true );

      // RFB inventory
      InventoryKey lInventoryRFB = createRFBInventory();

      // RFB tool
      createRFBTool();

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lInventoryRFB, lUnserviceableInventoryItems );
   }


   @Test
   public void testBatchInventoryWithZeroBinQuantityIsNotReturned() {

      new InventoryBuilder().atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.REPREQ )
            .withOwner( iLocalOwner ).withClass( RefInvClassKey.BATCH ).withBinQt( 0 ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals( "batch tool with no bin quantity is not returned", 0,
            lUnserviceableTools.getRowCount() );

   }


   @Test
   public void testSubComponenetInventoryIsNotReturned() {

      InventoryKey lEngine = createInventoryWithCondition( RefInvCondKey.REPREQ );

      // an unserviceable inventory which is a component of the engine
      new InventoryBuilder().atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.REPREQ )
            .withOwner( iLocalOwner ).withParentInventory( lEngine ).build();

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lEngine, lUnserviceableInventoryItems );
   }


   @Test
   public void testConsignmentReturnInventoryIsNotReturned() {

      OwnerKey lCosignmentOwner = new OwnerDomainBuilder().isNonLocal().build();

      new InventoryBuilder().atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.REPREQ )
            .withOwner( lCosignmentOwner ).withOwnershipType( RefOwnerTypeKey.CSGNRTRN ).build();

      DataSet lUnserviceableInventoryItems = execute();

      assertEquals( "inventory marked as consignment return is not returned", 0,
            lUnserviceableInventoryItems.getRowCount() );
   }


   @Test
   public void testInventoryAtUSSTGLocationIsReturned() {

      InventoryKey lInventoryAtUSSTGLocation =
            createUnserviceableInventoryAtLocation( iUSSTGLocation );

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lInventoryAtUSSTGLocation, lUnserviceableInventoryItems );
   }


   @Test
   public void testInventoryAtDockLocationIsReturned() {

      Location lDock = new Location();
      lDock.setCode( "YYZ/DOCK" );
      lDock.setType( RefLocTypeKey.DOCK );
      lDock.setSupplyLocation( iSupplyLocation );
      LocationKey lDockLocation = LocationBuilder.build( lDock );

      InventoryKey lInventoryAtDockLocation =
            createUnserviceableInventoryAtLocation( lDockLocation );

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lInventoryAtDockLocation, lUnserviceableInventoryItems );
   }


   @Test
   public void testInventoryAtLocationsOtherThanUSSTGAndDockIsNotReturned() {

      Location lBin = new Location();
      lBin.setCode( "YYZ/BIN" );
      lBin.setType( RefLocTypeKey.BIN );
      lBin.setSupplyLocation( iSupplyLocation );
      LocationKey lBinLocation = LocationBuilder.build( lBin );

      Location lStore = new Location();
      lStore.setCode( "YYZ/STORE" );
      lStore.setType( RefLocTypeKey.STORE );
      lStore.setSupplyLocation( iSupplyLocation );
      LocationKey lStoreLocation = LocationBuilder.build( lStore );

      Location lServiceableStaging = new Location();
      lServiceableStaging.setCode( "YYZ/SRVSTG" );
      lServiceableStaging.setType( RefLocTypeKey.SRVSTG );
      lServiceableStaging.setSupplyLocation( iSupplyLocation );
      LocationKey lServiceableStagingLocation = LocationBuilder.build( lServiceableStaging );

      createUnserviceableInventoryAtLocation( lBinLocation );
      createUnserviceableInventoryAtLocation( lStoreLocation );
      createUnserviceableInventoryAtLocation( lServiceableStagingLocation );

      DataSet lUnserviceableInventoryItems = execute();

      assertEquals( "inventory at locations other than USSTG or DOCK are not returned", 0,
            lUnserviceableInventoryItems.getRowCount() );
   }


   @Test
   public void testInventoryInKitIsReturned() {

      InventoryKey lKit = new InventoryBuilder().atLocation( iUSSTGLocation )
            .withCondition( RefInvCondKey.INSPREQ ).withOwner( iLocalOwner )
            .withClass( RefInvClassKey.KIT ).build();

      InventoryKey lInventoryInKit = new InventoryBuilder().atLocation( iUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).isInKit( lKit ).build();

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lInventoryInKit, lUnserviceableInventoryItems );
      assertEquals( lKit,
            lUnserviceableInventoryItems.getKey( InventoryKey.class, "kit_inv_key" ) );
   }


   @Test
   public void testOnlyREPREQToolsAreReturned() {

      InventoryKey lToolREPREQ = createToolWithCondition( RefInvCondKey.REPREQ );

      // RFI tool
      createToolWithCondition( RefInvCondKey.RFI );

      // INSPREQ tool
      createToolWithCondition( RefInvCondKey.INSPREQ );

      DataSet lUnserviceableTools = execute();

      assertItemReturned( lToolREPREQ, lUnserviceableTools );
   }


   @Test
   public void testBatchToolWithZeroBinQuantityIsNotReturned() {

      new InventoryBuilder().withPartNo( iTool ).atLocation( iUSSTGLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner )
            .withClass( RefInvClassKey.BATCH ).withBinQt( 0 ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals( "batch tool with no bin quantity is not returned", 0,
            lUnserviceableTools.getRowCount() );

   }


   @Test
   public void testToolsAtAllLocationsAreReturned() {

      InventoryKey lToolAtUSSTGLocation = createUnserviceableToolAtLocation( iUSSTGLocation );

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      Location lDock = new Location();
      lDock.setCode( "YYZ/DOCK" );
      lDock.setType( RefLocTypeKey.DOCK );
      lDock.setSupplyLocation( iSupplyLocation );
      LocationKey lDockLocation = LocationBuilder.build( lDock );
      InventoryKey lToolAtDockLocation = createUnserviceableToolAtLocation( lDockLocation );

      Location lBin = new Location();
      lBin.setCode( "YYZ/BIN" );
      lBin.setType( RefLocTypeKey.BIN );
      lBin.setSupplyLocation( iSupplyLocation );
      LocationKey lBinLocation = LocationBuilder.build( lBin );
      InventoryKey lToolAtBinLocation = createUnserviceableToolAtLocation( lBinLocation );

      Location lServiceableStaging = new Location();
      lServiceableStaging.setCode( "YYZ/SRVSTG" );
      lServiceableStaging.setType( RefLocTypeKey.SRVSTG );
      lServiceableStaging.setSupplyLocation( iSupplyLocation );
      LocationKey lServiceableStagingLocation = LocationBuilder.build( lServiceableStaging );
      InventoryKey lToolAtServiceableStagingLocation =
            createUnserviceableToolAtLocation( lServiceableStagingLocation );

      DataSet lUnserviceableTools = execute();

      assertItemsReturned( lUnserviceableTools,
            Arrays.asList( lToolAtUSSTGLocation, lToolAtLineLocation, lToolAtDockLocation,
                  lToolAtBinLocation, lToolAtServiceableStagingLocation ) );

   }


   @Test
   public void testToolInKitIsReturned() {

      InventoryKey lKit = new InventoryBuilder().atLocation( iUSSTGLocation )
            .withCondition( RefInvCondKey.INSPREQ ).withOwner( iLocalOwner )
            .withClass( RefInvClassKey.KIT ).build();

      InventoryKey lToolInKit = new InventoryBuilder().withPartNo( iTool )
            .atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.REPREQ )
            .withOwner( iLocalOwner ).isInKit( lKit ).build();

      DataSet lUnserviceableInventoryItems = execute();

      assertItemReturned( lToolInKit, lUnserviceableInventoryItems );
      assertEquals( lKit,
            lUnserviceableInventoryItems.getKey( InventoryKey.class, "kit_inv_key" ) );
   }


   @Test
   public void testToolAtSameLocationAsCommittedWorkPackageIsNotReturned() {

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      new TaskBuilder().withTaskClass( RefTaskClassKey.CHECK ).onInventory( lToolAtLineLocation )
            .atLocation( iLineLocation ).withStatus( RefEventStatusKey.COMMIT ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals( "tool at the same location as its committed work package is not returned", 0,
            lUnserviceableTools.getRowCount() );
   }


   @Test
   public void testToolAtSameLocationAsScheduledWorkPackageIsNotReturned() {

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      new TaskBuilder().withTaskClass( RefTaskClassKey.CHECK ).onInventory( lToolAtLineLocation )
            .atLocation( iLineLocation ).withStatus( RefEventStatusKey.ACTV )
            .withScheduledStart( new Date() ).withScheduledEnd( new Date() ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals(
            "tool at the same location as its active scheduled work package is not returned", 0,
            lUnserviceableTools.getRowCount() );

   }


   @Test
   public void testToolAtSameLocationAsNonScheduledWorkPackageIsReturned() {

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      new TaskBuilder().withTaskClass( RefTaskClassKey.CHECK ).onInventory( lToolAtLineLocation )
            .atLocation( iLineLocation ).withStatus( RefEventStatusKey.ACTV ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals( "tool at the same location as its non-scheduled work package is returned", 1,
            lUnserviceableTools.getRowCount() );

   }


   @Test
   public void testToolAtSameLocationAsCommittedRepairOrderIsNotReturned() {

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      new TaskBuilder().withTaskClass( RefTaskClassKey.RO ).onInventory( lToolAtLineLocation )
            .atLocation( iLineLocation ).withStatus( RefEventStatusKey.COMMIT ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals( "tool at the same location as its committed work package is not returned", 0,
            lUnserviceableTools.getRowCount() );
   }


   @Test
   public void testToolAtSameLocationAsScheduledRepairOrderIsNotReturned() {

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      new TaskBuilder().withTaskClass( RefTaskClassKey.RO ).onInventory( lToolAtLineLocation )
            .atLocation( iLineLocation ).withStatus( RefEventStatusKey.ACTV )
            .withScheduledStart( new Date() ).withScheduledEnd( new Date() ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals(
            "tool at the same location as its active scheduled work package is not returned", 0,
            lUnserviceableTools.getRowCount() );

   }


   @Test
   public void testToolAtSameLocationAsNonScheduledRepairOrderIsReturned() {

      InventoryKey lToolAtLineLocation = createUnserviceableToolAtLocation( iLineLocation );

      new TaskBuilder().withTaskClass( RefTaskClassKey.RO ).onInventory( lToolAtLineLocation )
            .atLocation( iLineLocation ).withStatus( RefEventStatusKey.ACTV ).build();

      DataSet lUnserviceableTools = execute();

      assertEquals( "tool at the same location as its non-scheduled work package is returned", 1,
            lUnserviceableTools.getRowCount() );

   }


   @Test
   public void testItemsOutsideUserAssignedSupplyLocationsAreNotReturned() {

      // create a new user who doesn't have access to the supply location
      iHumanResource = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );

      createUnserviceableInventoryAtLocation( iUSSTGLocation );
      createUnserviceableToolAtLocation( iUSSTGLocation );

      DataSet lUnserviceablItems = execute();

      assertEquals(
            "items at a supply location outside of the user's assigned supply locations are not returned",
            0, lUnserviceablItems.getRowCount() );

   }


   /**
    * Create an unserviceable inventory with a given condition.
    *
    * @param aInventoryCondition
    * @return the new inventory key
    */
   private InventoryKey createInventoryWithCondition( RefInvCondKey aInventoryCondition ) {
      return new InventoryBuilder().atLocation( iUSSTGLocation )
            .withCondition( aInventoryCondition ).withOwner( iLocalOwner ).build();
   }


   /**
    * Assert that the given items were returned by the query.
    *
    * @param aRetrievedItems
    *           the data set
    * @param aExpectedItems
    *           the list of expected inventory items
    */
   private void assertItemsReturned( DataSet aRetrievedItems, List<InventoryKey> aExpectedItems ) {

      assertEquals( "items are returned", aExpectedItems.size(), aRetrievedItems.getRowCount() );

      List<InventoryKey> lActualItems = new ArrayList<InventoryKey>();
      while ( aRetrievedItems.next() ) {
         lActualItems.add( aRetrievedItems.getKey( InventoryKey.class, INVENTORY_KEY ) );
      }

      assertThat( lActualItems, containsInAnyOrder( aExpectedItems.toArray() ) );
   }


   /**
    * Assert that the given item was returned by the query.
    *
    * @param aExpectedItem
    * @param aActualItem
    */
   private void assertItemReturned( InventoryKey aExpectedItem, DataSet aActualItem ) {
      assertEquals( "item is returned", 1, aActualItem.getRowCount() );
      aActualItem.next();
      assertEquals( aExpectedItem, aActualItem.getKey( InventoryKey.class, INVENTORY_KEY ) );
   }


   /* Add part repair vendor mapping */
   private void addPartRepairVendor( PartNoKey aPart, VendorKey aVendor,
         RefVendorStatusKey aVendorStatus ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aVendor, "vendor_db_id", "vendor_id" );
      lArgs.add( aPart, "part_no_db_id", "part_no_id" );
      lArgs.add( aVendorStatus, "vendor_status_db_id", "vendor_status_cd" );
      MxDataAccess.getInstance().executeInsert( "eqp_part_vendor_rep", lArgs );
   }


   private void assertItemReturnedWithDetailedInfo( InventoryKey aExpectedInv, TaskKey aWorkPackage,
         PurchaseOrderKey aOrder, String aOrderDesc, VendorKey lVendorKey, String aVendorCd,
         String aPoType, TaskKey aAcftWorkPackage, TaskKey aRemoveTask,
         RefRemoveReasonKey aRemoveReason, DataSet aActualItem ) {

      assertEquals( "item is returned", 1, aActualItem.getRowCount() );

      aActualItem.next();
      // for inventory
      assertEquals( aExpectedInv, aActualItem.getKey( InventoryKey.class, INVENTORY_KEY ) );

      // for order
      assertEquals( aWorkPackage, aActualItem.getKey( TaskKey.class, WO_KEY ) );
      assertEquals( aOrder, aActualItem.getKey( PurchaseOrderKey.class, ORDER_KEY ) );
      assertEquals( aOrderDesc, aActualItem.getString( ORDER_NUMBER ) );

      // for vendor
      assertEquals( lVendorKey, aActualItem.getKey( VendorKey.class, VENDOR_OR_SHOP_KEY ) );
      assertEquals( aVendorCd, aActualItem.getString( VENDOR_OR_SHOP_CD ) );
      assertEquals( aPoType, aActualItem.getString( VENDOR_OR_SHOP_TYPE ) );

      // for removed part
      assertEquals( aAcftWorkPackage, aActualItem.getKey( TaskKey.class, REMOVAL_WO_KEY ) );
      assertEquals( aRemoveTask, aActualItem.getKey( TaskKey.class, REMOVAL_TASK_KEY ) );
      assertEquals( aRemoveReason, aActualItem.getString( REMOVAL_REASON ) );

   }


   /* Add return inventory mapping for exchange order line */
   private void addReturnInventoryForExchange( PurchaseOrderLineKey aPoLine,
         InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPoLine, "po_db_id", "po_id", "po_line_id" );
      lArgs.add( aInventory, "inv_no_db_id", "inv_no_id" );
      MxDataAccess.getInstance().executeInsert( "po_line_return_map", lArgs );
   }


   /* Aadd part repair shop location mapping */
   private void addShopLocationForPartRepair( PartNoKey aPart, LocationKey aShop ) {

      // assign part to this shop location for repair
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aShop, "loc_db_id", "loc_id" );
      lArgs.add( aPart, "part_no_db_id", "part_no_id" );
      MxDataAccess.getInstance().executeInsert( "inv_loc_repair", lArgs );

   }


   /**
    * Set the global config param value
    *
    */
   private void setReadyForBuildConfigParam( boolean aValue ) {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      lConfigParms.setBoolean( PARAMETER_NAME, aValue );
      GlobalParameters.setInstance( lConfigParms );
   }


   /**
    * Create a RFB inventory
    *
    * @return the new inventory key
    */
   private InventoryKey createRFBInventory() {
      return new InventoryBuilder().atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.RFI )
            .withComplete( false ).withClass( RefInvClassKey.TRK ).withOwner( iLocalOwner ).build();
   }


   /**
    * Create a RFB tool
    *
    * @return the new inventory key
    */
   private InventoryKey createRFBTool() {
      return new InventoryBuilder().withPartNo( iTool ).atLocation( iUSSTGLocation )
            .withCondition( RefInvCondKey.RFI ).withComplete( false )
            .withClass( RefInvClassKey.TRK ).withOwner( iLocalOwner ).build();
   }


   /**
    * Create an unserviceable inventory at the given location.
    *
    * @param aInventoryLocation
    * @return the new inventory key
    */
   private InventoryKey createUnserviceableInventoryAtLocation( LocationKey aInventoryLocation ) {
      return new InventoryBuilder().atLocation( aInventoryLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();
   }


   /**
    * Create an unserviceable tool with a given condition.
    *
    * @param aInventoryCondition
    * @return the new inventory key
    */
   private InventoryKey createToolWithCondition( RefInvCondKey aInventoryCondition ) {
      return new InventoryBuilder().withPartNo( iTool ).atLocation( iUSSTGLocation )
            .withCondition( aInventoryCondition ).withOwner( iLocalOwner ).build();
   }


   /**
    * Create an unserviceable tool at the given location.
    *
    * @param aInventoryLocation
    * @return the new inventory key
    */
   private InventoryKey createUnserviceableToolAtLocation( LocationKey aInventoryLocation ) {
      return new InventoryBuilder().withPartNo( iTool ).atLocation( aInventoryLocation )
            .withCondition( RefInvCondKey.REPREQ ).withOwner( iLocalOwner ).build();
   }

}
