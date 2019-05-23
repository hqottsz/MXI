
package com.mxi.mx.core.services.stocklevel.workitemgenerator;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.PositiveValueException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvKitMapKey;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.materials.stockdistribution.domain.StockDistributionRequestService;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.inventory.MxMoveInventoryWarning;
import com.mxi.mx.core.services.inventory.condition.DefaultConditionService;
import com.mxi.mx.core.services.inventory.phys.BinInventoryService;
import com.mxi.mx.core.services.inventory.phys.PhysicalInventoryService;
import com.mxi.mx.core.services.inventory.pick.PickedInventoryService;
import com.mxi.mx.core.services.kit.pick.PickLineTO;
import com.mxi.mx.core.services.kit.pick.PickListTO;
import com.mxi.mx.core.services.part.PartNoService;
import com.mxi.mx.core.services.stocklevel.StockLevelService;
import com.mxi.mx.core.services.stocklevel.StockLevelTO;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelCheckWorkItem;
import com.mxi.mx.core.services.stocklevel.WarehouseStockLevelWorkItemGenerator;
import com.mxi.mx.core.services.transfer.CreateTransferTO;
import com.mxi.mx.core.services.transfer.TransferService;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvKitMapTable;
import com.mxi.mx.core.table.inv.InvLocStockTable;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * This class tests the {@link WarehouseStockLevelWorkItemGenerator}.
 *
 * @author Libin Cai
 * @since September 4, 2018
 */
public abstract class WarehouseStockLevelWorkItemGeneratorCommonTestCases {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final String WAREHOUSE_NAME = "Warehouse";

   // variables to be set by extending test classes
   private RefInvClassKey iInvClass;
   protected double iBinQty;

   protected OwnerKey iOwner;
   protected InventoryKey iInventory;
   protected LocationKey iWarehouse;

   private StockNoKey iStockNo;
   private PartNoKey iPart;
   private InventoryKey iKitInventory;

   private PartNoService partNoService;


   @Before
   public void setUp() {
      this.partNoService = new PartNoService();
   }


   public void loadData( RefInvClassKey aInvClass, double aBinQty ) throws Exception {

      iInvClass = aInvClass;
      iBinQty = aBinQty;

      LocationKey lAirport = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.AIRPORT );
         location.setCode( "Airport" );
         location.setIsSupplyLocation( true );
      } );

      iWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( WAREHOUSE_NAME );
         location.setSupplyLocation( lAirport );
      } );

      Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.USSTG );
         location.setCode( "Unserviceable Staging" );
         location.setSupplyLocation( lAirport );
      } );

      iOwner = new OwnerDomainBuilder().isDefault().build();

      // When creating stock level work item, we even don't need to have stock level. It is needed
      // when checking stock level.

      iStockNo = createStockNo( iInvClass );
      iPart = Domain.createPart( part -> {
         part.setCode( "partno" );
         part.setInventoryClass( iInvClass );
         part.setQtyUnitKey( QTY_UNIT );
         part.setStockNoKey( iStockNo );
      } );

      iInventory = createInventory( iPart, iWarehouse, iInvClass, iBinQty );

      UserKey lUserKey = new UserKey( 1234 );
      Domain.createHumanResource( hr -> hr.setUser( lUserKey ) );
      SecurityIdentificationUtils
            .setInstance( new SecurityIdentificationStub( lUserKey, "username" ) );
   }


   @After
   public void tearDown() {
      SecurityIdentificationUtils.setInstance( null );
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_MarkInvAsInspected_THEN_WorkItemGenerated()
         throws Exception {

      changeToRFI();
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_MarkInvAsInspected_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      changeToRFI();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_ChangeToNonRFI_THEN_WorkItemGenerated() throws Exception {

      changeToNonRFI();
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_ChangeToNonRFI_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      changeToNonRFI();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_PredrawInv_THEN_WorkItemGenerated() throws Exception {

      predrawInv();
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_PredrawInv_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      predrawInv();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_MarkInvAsINSPREQ_THEN_WorkItemGenerated()
         throws Exception {

      markAsInspectionRequired();
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_MarkInvAsINSPREQ_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      markAsInspectionRequired();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_MarkAsNotFound_THEN_WorkItemGenerated() throws Exception {

      setNotFound();
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_MarkAsNotFound_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      setNotFound();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_CondemnInv_THEN_WorkItemGenerated() throws Exception {

      condemInv();
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_CondemnInv_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      condemInv();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_CompleteTransfer_THEN_WorkItemGenerated()
         throws Exception {

      LocationKey lDestination = createTransferAndComplete();
      assertThatInventoryMovedAndWorkItemGenerated( lDestination );
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_CompleteTransfer_THEN_NoWorkItemGenerated()
         throws Exception {

      createKitInvWithContent();
      createTransferAndComplete();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void
         test_GIVEN_InvInSubWarehouseAndInStock_WHEN_CompleteTransfer_THEN_TwoWorkItemsGenerated()
               throws Exception {

      moveInvToSubLoc();
      LocationKey lDestination = createTransferAndComplete();
      assertThatInventoryMovedAndWorkItemGenerated( lDestination );
   }


   @Test
   public void
         test_GIVEN_KitContentInvInSubWarehouseAndInStock_WHEN_CompleteTransfer_THEN_NoWorkItemsGenerated()
               throws Exception {

      createKitInvWithContent();
      moveInvToSubLoc();
      createTransferAndComplete();
      assertThatNoWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_RemoveFromKit_THEN_WorkItemGenerated()
         throws Exception {

      // GIVEN a kit content inventory
      createKitInvWithContent();

      // WHEN remove the inventory from kit
      removeFromKit();

      // THEN one work item generated for check stock level
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_InvInStock_WHEN_AssignToKit_THEN_WorkItemGenerated() throws Exception {

      // GIVEN a kit inventory without content
      createKitInv();

      // WHEN assign the inventory to kit
      assignInventoryToKit();

      // THEN one work item generated for check stock level
      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_KitContentInvInStock_WHEN_PickItForStockDistReq_THEN_KitContentWarning()
         throws Exception {

      final String DISTREQ_BARCODE = "SR48U000255T";

      // GIVEN a kit content inventory and stock dist request
      createKitInvWithContent();
      Domain.createStockDistReq( pojo -> {
         pojo.setStockNo( iStockNo );
         pojo.setNeededQuantity( new Float( 12.0 ) );
         pojo.setRequestId( DISTREQ_BARCODE );
      } );

      // WHEN pick the inventory to stock dist request
      List<MxMoveInventoryWarning> lWarnings = new StockDistributionRequestService()
            .validatePickedItem( DISTREQ_BARCODE, iBinQty, iInventory );

      // THEN warning that the inventory is included in kit
      assertInventoryInKitError( lWarnings );

   }


   @Test
   public void test_GIVEN_PartAndStock_WHEN_AssignPartToStock_THEN_WorkItemGenerated()
         throws Exception {

      // Create stock level. It is used for checkStockLevels method to get location
      new StockLevelBuilder( iWarehouse, iStockNo, iOwner ).build();

      // Remove the stock no to prepare the data for assigning
      EqpPartNoTable lPart = EqpPartNoTable.findByPrimaryKey( iPart );
      lPart.setStockNumber( null );
      lPart.update();

      // Assign stock no, so work item is generated to check stock high
      partNoService.setStockNumber( iPart, iStockNo, null );

      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_PartAndStock_WHEN_UnassignPartFromStock_THEN_WorkItemGenerated()
         throws Exception {

      // Create stock level. It is used for checkStockLevels method to get location
      new StockLevelBuilder( iWarehouse, iStockNo, iOwner ).build();

      partNoService.setStockNumber( iPart, null, null );

      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_PartAndStock_WHEN_ChangeStockLevelReorderQuantity_THEN_WorkItemGenerated()
         throws Exception {

      double lReorderLevel = 5.0;
      Double lNewReorderLevel = lReorderLevel + 2.0;

      // Create stock level. It is used for checkStockLevels method to get location
      InvLocStockKey lStockLevel =
            new StockLevelBuilder( iWarehouse, iStockNo, iOwner ).withReorderQt( lReorderLevel )
                  .withStockLowAction( RefStockLowActionKey.DISTREQ ).build();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setMinReorderQt( 1.0 );
      lStockLevelTO.setReorderLevelQt( lNewReorderLevel, "" );

      new StockLevelService().set( lStockLevel, lStockLevelTO );

      // Assert the reorder qty is changed
      InvLocStockTable lStockLevelTable = InvLocStockTable.findByPrimaryKey( lStockLevel );
      Assert.assertEquals( lNewReorderLevel, lStockLevelTable.getReorderQt() );

      assertThatOneWorkItemGenerated();
   }


   @Test
   public void test_GIVEN_StockLevel_WHEN_ChangeActionFromMANUAtoDISTREQ_THEN_WorkItemGenerated()
         throws Exception {

      final double REORDER_QTY = 5.0;

      // Create a supplier to warehouse, otherwise we could not change the stock low action to
      // DISTREQ
      LocationKey lSupplier = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "subloc" );
      } );
      InvLocTable lInvLoc = InvLocTable.findByPrimaryKey( iWarehouse );
      lInvLoc.setHubLocation( lSupplier );
      lInvLoc.update();

      InvLocStockKey lStockLevel = new StockLevelBuilder( iWarehouse, iStockNo, iOwner )
            .withReorderQt( REORDER_QTY ).withStockLowAction( RefStockLowActionKey.MANUAL ).build();

      RefStockLowActionKey lNewLowAction = RefStockLowActionKey.DISTREQ;

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setMinReorderQt( 1.0 );
      lStockLevelTO.setReorderLevelQt( REORDER_QTY );
      lStockLevelTO.setLowAction( lNewLowAction.toString() );

      new StockLevelService().set( lStockLevel, lStockLevelTO );

      // Assert the stock low action is changed
      InvLocStockTable lStockLevelTable = InvLocStockTable.findByPrimaryKey( lStockLevel );
      Assert.assertEquals( lNewLowAction, lStockLevelTable.getStockLowAction() );

      assertThatOneWorkItemGenerated();
   }


   private void changeToRFI() throws MxException, TriggerException {

      new DefaultConditionService().markAsInspected( iInventory, iBinQty, null, null, true, null );
   }


   private void changeToNonRFI() throws MxException, TriggerException {

      new DefaultConditionService().markAsInspected( iInventory, iBinQty, null, null, false, null );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws MxException
    * @throws TriggerException
    */
   private void predrawInv() throws MxException, TriggerException {
      LocationKey lNeedeLoc = Domain.createLocation( loc -> {
         loc.setCode( "needed_loc" );
         loc.setIsSupplyLocation( true );
      } );
      Domain.createLocation( loc -> {
         loc.setCode( "predraw_loc" );
         loc.setType( RefLocTypeKey.PREDRAW );
         loc.setSupplyLocation( lNeedeLoc );
      } );

      PartRequestKey lPartReq = Domain.createPartRequest( partReq -> {
         partReq.reservedInventory( iInventory );
         partReq.requestedQuantity( iBinQty );
         partReq.neededAt( lNeedeLoc );
      } );

      TransferService.preDrawInventory( iInventory, lPartReq, iBinQty, 0, HumanResourceKey.ADMIN );
   }


   private void markAsInspectionRequired() throws MxException, TriggerException {

      InventoryServiceFactory.getInstance().getConditionService()
            .markAsInspectionRequired( iInventory, null, null, null, false );
   }


   /**
    * Assert that Inventory Is Included in Kit error
    *
    * @param aWarnings
    */
   private void assertInventoryInKitError( List<MxMoveInventoryWarning> aWarnings ) {
      Assert.assertEquals( 1, aWarnings.size() );
      Assert.assertEquals( "core.msg.ERROR_INVENTORY_IS_INCLUDED_IN_KIT",
            aWarnings.get( 0 ).getMsgFrameKey() );
   }


   private void moveInvToSubLoc() throws MandatoryArgumentException, StringTooLongException,
         PositiveValueException, MxException, TriggerException {

      LocationKey lSubWarehouse = Domain.createLocation( location -> {
         location.setCode( "SUB_WAH" );
         location.setParent( iWarehouse );
      } );

      // Change inventory location to warehouse sub-location
      InvInvTable lInvInvTable = InvInvTable.findByPrimaryKey( iInventory );
      lInvInvTable.setLocation( lSubWarehouse );
      lInvInvTable.update();
   }


   private LocationKey createTransferAndComplete() throws MandatoryArgumentException,
         StringTooLongException, PositiveValueException, MxException, TriggerException {

      String lDestinationLocCd = "DEST_LOC";

      LocationKey lDestination = Domain.createLocation( location -> {
         location.setCode( lDestinationLocCd );
         location.setType( RefLocTypeKey.SRVSTORE );
      } );

      // Create a transfer
      CreateTransferTO lTO = new CreateTransferTO();
      lTO.setTransferToCd( lDestinationLocCd );
      lTO.setInventory( iInventory );
      lTO.setQuantity( iBinQty );
      TransferKey lTransfer = TransferService.create( lTO );

      // Completed to trigger creating stock level work item
      TransferService.complete( lTransfer, null, null, true, true, false, null );

      return lDestination;
   }


   private void condemInv() throws MxException, TriggerException {
      InventoryServiceFactory.getInstance().getConditionService().condemn( iInventory, null, null,
            null );
   }


   private void setNotFound() {
      new PhysicalInventoryService( iInventory ).setNotFoundBool( true, null );
   }


   protected void createKitInvWithContent() {

      createKitInv();

      InvKitMapKey lInvKitMapKey = new InvKitMapKey( iKitInventory, iInventory );
      InvKitMapTable lInvKitMapTable = InvKitMapTable.create( lInvKitMapKey );
      lInvKitMapTable.insert();

   }


   private void createKitInv() {

      // Create kit part
      PartNoKey lKitPart = Domain.createPart();

      // Assign a content part to the Kit part
      PartGroupKey lPartGroup = Domain.createPartGroup( partGroup -> partGroup.addPart( iPart ) );
      new KitBuilder( lKitPart ).withContent( iPart, lPartGroup ).build();

      // create kit inventory WITHOUT content inventory
      iKitInventory = Domain.createKitInventory( kitInventory -> {
         kitInventory.setSerialNo( "KIT_SERIAL_NO" );
         kitInventory.setPartNo( lKitPart );
         kitInventory.setLocation( iWarehouse );
         kitInventory.setOwner( iOwner );
      } );

   }


   /*
    * Assign iInventory to kit iKitInventory
    */
   private void assignInventoryToKit() throws MxException, TriggerException {

      ManufacturerKey lManufacture = Domain.createManufacturer();

      EqpPartNoTable lPartTable = EqpPartNoTable.findByPrimaryKey( iPart );
      lPartTable.setEqpManufact( lManufacture );
      lPartTable.update();

      PickListTO iTO = new PickListTO( iKitInventory );

      PickLineTO lPickedItem;
      if ( RefInvClassKey.BATCH.equals( iInvClass ) ) {
         lPickedItem = new PickLineTO( iKitInventory, iInventory, iBinQty, null );
         lPickedItem.setIsSerialize( false );
      } else {
         lPickedItem = new PickLineTO( iKitInventory, "partno", lManufacture.getCd(), "sn", 1.0,
               iWarehouse, null );
         lPickedItem.setIsSerialize( true );
      }
      lPickedItem.setCondition( RefInvCondKey.RFI.getCd() );
      iTO.addPickedItem( lPickedItem );

      new PickedInventoryService().pickItemsForKit( iTO, null );
   }


   /**
    * Remove content inventory from kit inventory
    *
    * @throws MxException
    * @throws TriggerException
    */
   private void removeFromKit() throws MxException, TriggerException {

      // create a transfer TO for removing inventory from kit
      CreateTransferTO lCreateTransferTO = new CreateTransferTO();
      lCreateTransferTO.setRemoveInvFromKit( true );
      lCreateTransferTO.setKitInv( iKitInventory );
      lCreateTransferTO.setInventory( iInventory );
      lCreateTransferTO.setTransferToCd( WAREHOUSE_NAME );
      lCreateTransferTO.setQuantity( iBinQty );
      lCreateTransferTO.setRelatedReq( null );

      // create transfer with inventory removed from kit
      TransferService.create( lCreateTransferTO );
   }


   private void assertThatInventoryMovedAndWorkItemGenerated( LocationKey aExpectedLocation ) {

      assertThatTwoWorkItemsGenerated( aExpectedLocation );
   }


   private StockNoKey createStockNo( RefInvClassKey aInventoryClass ) {

      return new StockBuilder().withInvClass( aInventoryClass ).withQtyUnitKey( QTY_UNIT ).build();
   }


   private InventoryKey createInventory( PartNoKey aPart, LocationKey aLocation,
         RefInvClassKey aInvClass, Double aBinQty ) {

      return new InventoryBuilder().withPartNo( aPart ).withSerialNo( "sn" ).withClass( aInvClass )
            .atLocation( aLocation ).withBinQt( aBinQty ).withCondition( RefInvCondKey.RFI )
            .withOwner( iOwner ).build();
   }


   protected void assertThatOneWorkItemGenerated() {

      QuerySet lQs = assertGeneratateWorkItemNumber( 1 );

      lQs.next();
      Assert.assertEquals( iStockNo.toString() + ":" + iWarehouse.toString() + ":[NULL]",
            lQs.getString( "data" ) );
   }


   /**
    * This is only called by complete transfer test cases. When completing transfer, we need to
    * check stock level for both from and to locations. One is for stock low while another is for
    * stock high.
    *
    * @param aDestination
    *           the destination location
    */
   private void assertThatTwoWorkItemsGenerated( LocationKey aDestination ) {

      QuerySet lQs = assertGeneratateWorkItemNumber( 2 );

      boolean lFirstWorkItemsAsserted = false;
      boolean lSecondWorkItemsAsserted = false;

      while ( lQs.next() ) {

         if ( isCorrectWorkItem( lQs, iWarehouse ) ) {

            lFirstWorkItemsAsserted = true;

         } else if ( isCorrectWorkItem( lQs, aDestination ) ) {

            lSecondWorkItemsAsserted = true;
         }
      }

      if ( !lFirstWorkItemsAsserted || !lSecondWorkItemsAsserted ) {
         Assert.fail( "At least one work item was not generated correctly." );
      }
   }


   private QuerySet assertGeneratateWorkItemNumber( int aExpectedNumber ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WarehouseStockLevelCheckWorkItem.WORK_TYPE );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      // Assert a new work item is generated with expected data
      Assert.assertEquals( aExpectedNumber, lQs.getRowCount() );
      return lQs;
   }


   private boolean isCorrectWorkItem( QuerySet aQs, LocationKey aLocation ) {

      return ( iStockNo.toString() + ":" + aLocation.toString() + ":[NULL]" )
            .equals( aQs.getString( "data" ) );
   }


   protected void assertThatNoWorkItemGenerated() {
      assertGeneratateWorkItemNumber( 0 );
   }


   protected void setBinQuantity( double aNewBinQty ) throws MxException, TriggerException {

      new BinInventoryService( iInventory ).setBinQuantity( aNewBinQty, false, false, null, null,
            null, null );
   }

}
