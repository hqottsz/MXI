
package com.mxi.mx.core.services.part.classchanger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.EventBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.EqpKitPartGroupKey;
import com.mxi.mx.core.key.EqpPartNoLogKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefLogActionKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.RefShipmentTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.RefVendorTypeKey;
import com.mxi.mx.core.key.RefXferTypeKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.CoreLicenseStub.FeatureSet;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.part.KitUtils;
import com.mxi.mx.core.services.part.MultiplePartsAssignedToStockNumberException;
import com.mxi.mx.core.services.part.PartNoService;
import com.mxi.mx.core.services.part.PendingReturnToVendorShipmentsException;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;
import com.mxi.mx.core.services.part.classchange.BatchToSerializedChanger;
import com.mxi.mx.core.services.shipment.ShipmentLineService;
import com.mxi.mx.core.services.shipment.ShipmentService;
import com.mxi.mx.core.services.stocklevel.StockServiceFactory;
import com.mxi.mx.core.services.transfer.TransferService;
import com.mxi.mx.core.table.eqp.EqpKitPartGroupTable;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.table.eqp.EqpStockNoTable;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvLocStockTable;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.table.ship.ShipShipmentLineTable;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNo;
import com.mxi.mx.core.unittest.table.eqp.EqpPartNoLog;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.evt.EvtInv;


/**
 * This tests the BatchToSerializedChanger class
 *
 * @author Yiyi Dai
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class BatchToSerializedChangerTest {

   private static final String WING_FLAP = "wing_flap";
   private HumanResourceKey iHr;
   private PartNoKey iBatchPart;
   private PartGroupKey iBatchPartGroup;
   private FncAccountKey iExpenseAccount;
   private StockNoKey iStock;
   private LocationKey iDockLocation;
   private LocationKey iQuarLocation;
   private LocationKey iVendorLocation;
   private OwnerKey iOwner;
   private static final double QUAR_QTY = 3.0;
   private static final double RFI_QTY = 2.0;
   private static final double LOCKED_QTY = 2.0;
   private static final Double INITIAL_QTY = 10.00;
   private final double EXPECTED_QTY = 2.00;
   private static final String USERNAME = "testuser";
   private static final int USER_ID = 1992;
   private static final String SERIAL_NO_OEM = "ABC";
   private final RefQtyUnitKey LITRE = new RefQtyUnitKey( 0, "L" );
   private final BigDecimal PART_VALUE = new BigDecimal( 10 );
   private final double MAX_QT = 20.00;
   private final double MIN_QT = 5.00;
   private final double EXPECTED_MAX_QT = 4.00;
   private final double EXPECTED_MIN_QT = 1.00;
   private final double CONVERSION_FACTOR = 5;
   private EqpPartNoTable iEqpPartNo;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This scenario tests when convert a part from Batch to Serialized, the Ad-hoc part request for
    * this part with request quantity more than one will be split to part requests with quantity 1
    * for each, and the number of the part requests will be the same as request quantity.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testAdhocSerPartRequestSplitWhenQtyMoreThanOne()
         throws MxException, TriggerException {

      // create an Ad-hoc part request with request quantity 3
      PartRequestKey lAdhocPartRequest =
            new PartRequestBuilder().withType( RefReqTypeKey.ADHOC ).forPurchasePart( iBatchPart )
                  .forPartGroup( iBatchPartGroup ).requestedBy( iHr ).requiredBy( new Date() )
                  .isNeededAt( iDockLocation ).withIssueAccount( iExpenseAccount )
                  .withRequestedQuantity( 3.0 ).withQuantityUnit( RefQtyUnitKey.EA ).build();
      String lReqMasterId = ReqPartTable.findByPrimaryKey( lAdhocPartRequest ).getReqMasterId();

      changeInvClassFromBatchToSer( 1.0 );

      /** ASSERTION **/
      // get all part requests after split
      DataSetArgument lArgs = RefReqTypeKey.ADHOC.getPKWhereArg();
      lArgs.add( iBatchPart, "po_part_no_db_id", "po_part_no_id" );
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( PartRequestKey.TABLE_NAME, lArgs );

      // Assert there are 3 part requests now
      assertEquals( 3, lQs.getRowCount() );
      while ( lQs.next() ) {

         // Assert the original part request's quantity has been reduced to 1
         if ( lAdhocPartRequest
               .equals( lQs.getKey( PartRequestKey.class, "req_part_db_id", "req_part_id" ) ) ) {
            assertEquals(
                  "The Ad-hoc Serialized part request's request quantity is not 1 which is wrong",
                  1.0, lQs.getDouble( "req_qt" ), 0 );
         } else {

            // Assert the two new part requests have the same properties as the original one
            assertEquals( "This serialized part request quantity is not 1, which is wrong", 1.0,
                  lQs.getDouble( "req_qt" ), 0 );
            assertEquals( "The requested by is not the same as where it is split from", iHr,
                  lQs.getKey( HumanResourceKey.class, "req_hr_db_id", "req_hr_id" ) );
            assertEquals( "The needed by location is not the same as where it is split from",
                  iDockLocation, lQs.getKey( LocationKey.class, "req_loc_db_id", "req_loc_id" ) );
            assertEquals( "The issue to account is not the same as where it is split from",
                  iExpenseAccount,
                  lQs.getKey( FncAccountKey.class, "issue_account_db_id", "issue_account_id" ) );
            assertEquals( "The part request note is wrong",
                  i18n.get( "core.msg.REQ_CREATE_DUE_TO_SPLIT", lReqMasterId ),
                  lQs.getString( "req_note" ) );
         }
      }
   }


   /**
    *
    * GIVEN a batch part which belongs to a stock with no other parts and a unit of measure which is
    * not EA, WHEN the part is converted to SER, THEN the standard unit of measure of the part, and
    * stock should change to EA (each).
    *
    * @throws TriggerException
    *            if an error occurs
    * @throws MxException
    *            if an error occurs
    *
    */
   @Test
   public void testUnitOfMeasureUpdatedToEachWhenConvertingFromBatchToSer()
         throws MxException, TriggerException {

      changeInvClassFromBatchToSer( 1.0 );

      // Assert that the inventory class and uom of the part have changed
      assertEquals( "inventory class", RefInvClassKey.SER,
            EqpPartNoTable.findByPrimaryKey( iBatchPart ).getInvClass() );
      assertEquals( "unit of measure of the part", RefQtyUnitKey.EA,
            EqpPartNoTable.findByPrimaryKey( iBatchPart ).getQtyUnit() );

      // Assert that the uom of the stock has changed
      assertEquals( "unit of measure of the stock", RefQtyUnitKey.EA,
            EqpStockNoTable.findByPrimaryKey( iStock ).getQtyUnit() );

   }


   /**
    *
    * GIVEN a batch part with a total quantity value, WHEN the part is converted to SER with a
    * conversion factor, THEN the total quantity should be adjusted for the conversion factor
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testPartQuantityUpdatedWhenConvertingFromBatchToSerWithAConversionFactor()
         throws MxException, TriggerException {

      // Total qt = 6.0, conversion factor = 2.0, expected value = 6.0/2.0 = 3.0
      assertTotalQuantityAdjustedForConversion( 6.0, 2.0, 3.0 );

      // Total qt = 6.0, conversion factor = 0.5, expected value = 6.0/0.5 = 12.0
      assertTotalQuantityAdjustedForConversion( 6.0, 0.5, 12.0 );

      // Total qt = 0.4, conversion factor = 2.0, expected value => 0.4/2.0 = 0.1 rounded to 0.0
      assertTotalQuantityAdjustedForConversion( 0.4, 2.0, 0.0 );

      // Total qt = 0.5, conversion factor = 1.0, expected value => 0.5/1.0 = 0.5 rounded to 0.0
      assertTotalQuantityAdjustedForConversion( 0.5, 1.0, 0.0 );

      // Total qt = 1.6, conversion factor = 2.0, expected value => 1.6/2.0 = 0.8 rounded to 1.0
      assertTotalQuantityAdjustedForConversion( 1.6, 2.0, 1.0 );
   }


   /**
    *
    * GIVEN a batch part with a stock and a stock level, WHEN the part is converted to SER with a
    * conversion factor, THEN the total quantities in global stock and the stock levels should be
    * adjusted for the conversion factor
    *
    * @throws TriggerException
    *            if an error occurs
    * @throws MxException
    *            if an error occurs
    *
    */
   @Test
   public void testStockLevelsUpdatedWhenConvertingFromBatchToSer()
         throws MxException, TriggerException {

      // add a stock level to the existing stock (iStock)
      new StockLevelBuilder( iDockLocation, iStock, iOwner ).withMinReorderLevel( 10.0 )
            .withReorderQt( 20.0 ).withMaxLevel( 80.0 ).withBatchSize( 2.0 )
            .withStockLowAction( RefStockLowActionKey.MANUAL ).build();

      // convert the part to serialized
      changeInvClassFromBatchToSer( 2.0 );

      // retrieve the stock and ensure that the quantities have been adjusted for the conversion
      // factor. The existing quantities in iStock are; GlobalReorderLevel -> 10.0, BatchSize ->
      // 20.0, GlobalSafetyLevel -> 30.0
      EqpStockNoTable lStockTable = EqpStockNoTable.findByPrimaryKey( iStock );

      // conversion -> expected_value = original_value/2.0
      assertEquals( "Global reorder qty", new Double( 5 ), lStockTable.getGlobalReorderQt() );
      assertEquals( "Batch size", new Double( 10 ), lStockTable.getBatchSize() );
      assertEquals( "Global safety level", new Double( 15 ), lStockTable.getSafetyLevelQt() );

      // retrieve the stock level
      InvLocStockKey[] lStockLevelKeys =
            StockServiceFactory.getInstance().getStockLevelService().getStockLevelKeys( iStock );
      InvLocStockTable lStockLevelTable = InvLocStockTable.findByPrimaryKey( lStockLevelKeys[0] );

      // ensure that the quantities in the stock level have been divided by the conversion factor
      // conversion -> expected_value = original_value/2.0
      assertEquals( "Minimum reorder level", new Double( 5 ), lStockLevelTable.getMinReorderQt() );
      assertEquals( "Reorder Level", new Double( 10 ), lStockLevelTable.getReorderQt() );
      assertEquals( "Maximum level", new Double( 40 ), lStockLevelTable.getMaxQt() );
      assertEquals( "Reorder Quantity", new Double( 1 ), lStockLevelTable.getBatchSize() );
   }


   /**
    *
    * GIVEN a batch part that belongs to a stock which has other parts, WHEN inventory class is
    * changed to serialized, THEN a MultiplePartsAssignedToStockNumberException should be triggered
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test( expected = MultiplePartsAssignedToStockNumberException.class )
   public void testCannotConvertInvClassWhenMultiplePartsExistInStock()
         throws MxException, TriggerException {

      // build a new batch part and assign it to the same stock as iBatchPart
      new PartNoBuilder().withStock( iStock ).withInventoryClass( RefInvClassKey.BATCH ).build();

      changeInvClassFromBatchToSer( 2.0 );
      Assert.fail( "Conversion should not be allowed if multiple parts exist in the stock" );
   }


   /**
    *
    * GIVEN a batch part with a pending transfer, WHEN the part is converted to serialized, THEN the
    * transfer should be split and the a total quantity of inventory adjusted by the conversion
    * factor should be transferred
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testPendingTransferQuantitiesConverted() throws MxException, TriggerException {
      // create RFI inventory for a batch part at a dock location
      InventoryKey lRfiInventory = new InventoryBuilder().withClass( RefInvClassKey.BATCH )
            .withPartNo( iBatchPart ).atLocation( iDockLocation ).withBinQt( RFI_QTY )
            .withCondition( RefInvCondKey.RFI ).withOwner( iOwner ).build();

      // create a pending stock transfer
      new InventoryTransferBuilder()
            .withDestination( Domain.createLocation( aLocationConfiguration -> {
               aLocationConfiguration.setType( RefLocTypeKey.DOCK );
               aLocationConfiguration.setIsSupplyLocation( true );
               aLocationConfiguration.setCode( "Dock2" );
               aLocationConfiguration.setParent( iDockLocation );
            } ) ).withInventory( lRfiInventory ).withQuantity( new BigDecimal( RFI_QTY ) )
            .withType( RefXferTypeKey.STKTRN ).withEventType( RefEventTypeKey.LX )
            .withStatus( RefEventStatusKey.LXPEND ).withInitEvent( new EventBuilder()
                  .withStatus( RefEventStatusKey.LXPEND ).withType( RefEventTypeKey.LX ).build() )
            .build();

      // change inventory class with a conversion factor of 0.5
      changeInvClassFromBatchToSer( 0.5 );

      // fetch all inventory after splitting
      InventoryKey[] lAllInventory = PartNoService.getInventory( iBatchPart );

      // assert that the transferred inventory has been split into 4
      assertEquals( "split inventory", 4, lAllInventory.length );

      // assert that the split inventories are assigned to transfers
      for ( InventoryKey lInv : lAllInventory ) {
         assertNotNull( TransferService.getPendingTransfers( lInv, RefXferTypeKey.STKTRN ) );
      }

   }


   /**
    *
    * GIVEN a pending shipment for a batch part, WHEN the batch part is converted to a serialized
    * part, THEN the shipment lines should be split into a corresponding number based on the
    * conversion factor
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   @Test
   public void testPendingStockTransferShipmentQuantitiesConverted()
         throws MxException, TriggerException {
      // create a shipment
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iDockLocation )
            .toLocation( Domain.createLocation( aLocationConfiguration -> {
               aLocationConfiguration.setType( RefLocTypeKey.DOCK );
               aLocationConfiguration.setCode( "ExtDock" );
            } ) ).withHistoric( false ).withType( RefShipmentTypeKey.STKTRN ).build();

      // add a shipment line for the batch inventory to the shipment
      new ShipmentLineBuilder( lShipment ).withExpectedQuantity( RFI_QTY ).forPart( iBatchPart )
            .build();

      // change the inventory class of the batch part with a conversion factor of 0.5
      changeInvClassFromBatchToSer( 0.5 );

      // assert the number of lines in the shipment has been adjusted (3 new shipment lines created)
      assertEquals( "shipment lines", 4, ShipmentService.getAllShipmentLines( lShipment ).length );
   }


   /**
    *
    * GIVEN a batch part with a pending return to vendor shipment against it, WHEN the batch part is
    * converted to a serialized part, THEN an error should be returned stopping the process
    *
    *
    * @throws TriggerException
    *            if an error occurs
    * @throws MxException
    *            if an error occurs
    *
    */
   @Test
   public void testPendingReturnToVendorShipmentsException() throws MxException, TriggerException {

      // create a vendor location
      LocationKey lVendorLoc = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.VENDOR );
         aLocationConfiguration.setCode( "Vendor" );
      } );

      // create a purchase vendor
      VendorKey lVendor = new VendorBuilder().withVendorType( RefVendorTypeKey.PURCHASE )
            .atLocation( lVendorLoc ).build();

      // create a purchase order for iBatchPart
      PurchaseOrderKey lPo =
            new OrderBuilder().withVendor( lVendor ).withOrderType( RefPoTypeKey.PURCHASE )
                  .withStatus( RefEventStatusKey.PORECEIVED ).build();

      // create a PO line key
      PurchaseOrderLineKey lPOLine = Domain.createPurchaseOrderLine( aPoLine -> {
         aPoLine.orderKey( lPo );
         aPoLine.lineType( RefPoLineTypeKey.PURCHASE );
         aPoLine.orderQuantity( new BigDecimal( 4 ) );
         aPoLine.part( iBatchPart );
      } );

      // create inventory in condition INSPREQ
      InventoryKey lInspreqInv = new InventoryBuilder().withClass( RefInvClassKey.BATCH )
            .withPartNo( iBatchPart ).atLocation( iDockLocation ).withBinQt( 4.0 )
            .withCondition( RefInvCondKey.INSPREQ ).withOwner( iOwner ).withOrderLine( lPOLine )
            .build();

      // create a Return to Vendor shipment
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iDockLocation )
            .toLocation( lVendorLoc ).withHistoric( false ).withType( RefShipmentTypeKey.RTNVEN )
            .withOrder( lPo ).build();

      // add a shipment line for the batch inventory to the shipment
      new ShipmentLineBuilder( lShipment ).forInventory( lInspreqInv ).withExpectedQuantity( 4.0 )
            .forOrderLine( lPOLine ).build();

      // change the inventory class of the batch part with a conversion factor of 0.5
      try {
         changeInvClassFromBatchToSer( 0.5 );
         Assert.fail( "PendingReturnToVendorShipmentsException expected!" );
      } catch ( PendingReturnToVendorShipmentsException e ) {
         // expected PendingReturnToVendorShipmentsException
      }
   }


   /**
    *
    * GIVEN a batch part with inventory in conditions RFI and QUAR, WHEN the part is converted to
    * serialized, THEN the original inventory conditions and expected quantities should be preserved
    *
    * @throws TriggerException
    *            if an error occurs
    * @throws MxException
    *            if an error occurs
    *
    */
   @Test
   public void testInvConditionPreservedWhenConvertingFromBatchToSer()
         throws MxException, TriggerException {
      double lQuarCount = 0.0;
      // create 2 inv in condition RFI
      new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withPartNo( iBatchPart )
            .atLocation( iDockLocation ).withBinQt( RFI_QTY ).withCondition( RefInvCondKey.RFI )
            .withOwner( iOwner ).build();

      // create 3 inv in condition QUAR
      new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withPartNo( iBatchPart )
            .atLocation( iQuarLocation ).withBinQt( QUAR_QTY ).withCondition( RefInvCondKey.QUAR )
            .withOwner( iOwner ).build();

      changeInvClassFromBatchToSer( 1.0 );

      // get all inventory for the converted part
      InventoryKey[] lAllInventory = PartNoService.getInventory( iBatchPart );

      // count the number of inventory in condition QUAR
      for ( InventoryKey lInv : lAllInventory ) {
         lQuarCount =
               ( InvInvTable.findByPrimaryKey( lInv ).getInvCond().equals( RefInvCondKey.QUAR ) )
                     ? lQuarCount + 1 : lQuarCount;
      }

      // assert the batch was split into 5
      assertEquals( "All inventory", 5, lAllInventory.length );

      // assert that 3 of the newly created inventory items are quarantined
      assertEquals( "Quar inventory", QUAR_QTY, lQuarCount, 0 );
   }


   /**
    *
    * GIVEN a batch part with X items of locked inventory, WHEN the part is converted to serialized,
    * THEN the same quantity (X) of serialized inventory should be locked
    *
    * @throws TriggerException
    *            if an error occurs
    * @throws MxException
    *            if an error occurs
    *
    */
   @Test
   public void testLockedBatchInvConvertedToLockedSerInvWhenConvertingFromBatchToSer()
         throws MxException, TriggerException {
      double lLockedCount = 0.0;

      // create archived and locked inventory
      new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withPartNo( iBatchPart )
            .atLocation( iDockLocation ).withBinQt( LOCKED_QTY )
            .withCondition( RefInvCondKey.ARCHIVE ).withOwner( iOwner ).isLocked().build();

      changeInvClassFromBatchToSer( 1.0 );

      InventoryKey[] lAllInventory = PartNoService.getInventory( iBatchPart );

      // count the number of locked inventory
      for ( InventoryKey lInv : lAllInventory ) {
         lLockedCount = ( InvInvTable.findByPrimaryKey( lInv ).isLocked() ) ? lLockedCount + 1
               : lLockedCount;
      }

      // assert that the correct number of newly created inventory is locked
      assertEquals( "Locked inventory", LOCKED_QTY, lLockedCount, 0 );
   }


   /**
    * GIVEN a batch part WHEN the inventory class of the part is changed from BATCH to SER THEN
    * tests the audit message log for changing the unit of measure code.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testAuditLogforUoMChange() throws MxException, TriggerException {

      // fetch the original unit of measure of the part.
      String lOrigQtyUnitCd = getUomOfPart( iBatchPart );

      changeInvClassFromBatchToSer( CONVERSION_FACTOR );

      // fetch the new unit of measure of the part.
      String lNewQtyUnitCd = getUomOfPart( iBatchPart );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iBatchPart.getPKWhereArg() );

      QuerySet lDs = QuerySetFactory.getInstance().executeQueryTable( "EQP_PART_NO_LOG", lArgs );

      if ( lDs.last() ) {
         EqpPartNoLog lEqpPartNoLogTable = new EqpPartNoLog( lDs.getKey( EqpPartNoLogKey.class,
               new String[] { "PART_NO_DB_ID", "PART_NO_ID", "PART_NO_LOG_ID" } ) );

         String lAuditMessage = MessageFormat.format( i18n.get( "core.msg.CHANGE_PART_NO_UOM" ),
               new Object[] { lOrigQtyUnitCd, lNewQtyUnitCd, CONVERSION_FACTOR, lOrigQtyUnitCd } );

         lEqpPartNoLogTable.assertExist();
         lEqpPartNoLogTable.assertSystemNote( lAuditMessage );
         lEqpPartNoLogTable.assertLogAction( RefLogActionKey.CHANGEINVCLASS );
         lEqpPartNoLogTable.assertLogDate( new Date() );
         lEqpPartNoLogTable.assertLogReason( null );
         lEqpPartNoLogTable.assertUserNote( null );
      }
   }


   /**
    * GIVEN a batch part WHEN the inventory class of the part is changed from BATCH to SER THEN
    * tests the generated serial numbers of new inventories are unique.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testGenerateUniqueSerialNosWhenConvertingFromBatchToSer()
         throws MxException, TriggerException {

      // create an inventory
      new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withPartNo( iBatchPart )
            .atLocation( iDockLocation ).withSerialNo( SERIAL_NO_OEM ).withBinQt( RFI_QTY )
            .withOwner( iOwner ).build();

      // perform the batch to serialized conversion
      changeInvClassFromBatchToSer( 1.0 );

      // get all inventory for the converted part
      InventoryKey[] lAllInventory = PartNoService.getInventory( iBatchPart );

      // assert that the number of created serialized inventories are equivalent to the old batch
      // quantity
      assertEquals( "No of inventories are created", RFI_QTY, lAllInventory.length, 0 );

      // assert that the generated serial numbers are unique
      for ( InventoryKey lInv : lAllInventory ) {
         InvInvTable lInvInv = InvInvTable.findByPrimaryKey( lInv );
         assertNotEquals( "Generated serial numbers are unique", SERIAL_NO_OEM,
               lInvInv.getSerialNoOem() );
      }

      // fetch the new serial number information for the original batch inventory
      InvInvTable lInvInvTab = InvInvTable.findByPrimaryKey( lAllInventory[0] );
      String lNewSerialNoOem = lInvInvTab.getSerialNoOem();

      // assert that a new event log has been generated for changing the serial number
      // information of the original inventory
      String lNote = i18n.get( "core.msg.INV_SER_NO_CHANGE", SERIAL_NO_OEM, lNewSerialNoOem );

      List<EventKey> lLatestEvents = EvtInv.getLatestEventsForInv( lAllInventory[0], 2 );
      EventKey lLatestEvent = lLatestEvents.get( 0 );
      EvtEventUtil lEvtEvent = new EvtEventUtil( lLatestEvent );
      lEvtEvent.assertEventSdesc( lNote );

      // assert that the inventory class has been changed
      lNote = i18n.get( "core.msg.CHANGE_INV_CLASS", RefInvClassKey.BATCH.getCd(),
            RefInvClassKey.SER.getCd() );

      lLatestEvent = lLatestEvents.get( 1 );
      lEvtEvent = new EvtEventUtil( lLatestEvent );
      lEvtEvent.assertEventSdesc( lNote );
   }


   /**
    * GIVEN a batch part inventory and a shipment for the batch inventory WHEN the inventory class
    * of the part is changed from BATCH to SER THEN tests the serial number information is updated
    * for the original shipment line
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testModifySerialNoInfoInOriginalShipmentLineWhenConvertingFromBatchToSer()
         throws MxException, TriggerException {

      // create a shipment for the batch inventory
      ShipmentKey lShipment = new ShipmentDomainBuilder().fromLocation( iDockLocation )
            .toLocation( iVendorLocation ).build();

      // create a shipment line for the shipment
      new ShipmentLineBuilder( lShipment ).forPart( iBatchPart ).withExpectedQuantity( RFI_QTY )
            .build();

      // fetch the original unit of measure of thle part.
      String lOrigQtyUnitCd = getUomOfPart( iBatchPart );

      // perform the batch to serialized conversion
      changeInvClassFromBatchToSer( 1.0 );

      // get all the shipment lines after batch to serialized conversion
      List<ShipmentLineKey> lShipmentLineList = ShipmentLineService.getShipmentLine( lShipment );

      // fetch the new unit of measure of the part.

      String lNewQtyUnitCd = getUomOfPart( iBatchPart );

      // create history note after changing UoM
      String lMessage = i18n.get( "core.msg.SHIPMENT_LINE_UOM_CHANGE", iEqpPartNo.getPartNoOEM(),
            lOrigQtyUnitCd, lNewQtyUnitCd, 1.0, lOrigQtyUnitCd );

      EventKey lEventKey = new EventKey( lShipment.getDbId(), lShipment.getId() );

      StageKey lStageKey = new StageKey( lEventKey, 1 );
      EvtStageTable lEvtStage = EvtStageTable.findByPrimaryKey( lStageKey );

      // assert entered actual history note and expected history note are same
      assertEquals( "History note is created", lMessage, lEvtStage.getStageNote() );

      // assert that the number of created shipment lines are equivalent to the old batch
      // quantity
      assertEquals( "No of shipment lines are created", RFI_QTY, lShipmentLineList.size(), 0 );

      // assert that the generated serial numbers are unique
      for ( ShipmentLineKey lShipmentLine : lShipmentLineList ) {
         ShipShipmentLineTable lShipShipmentLine =
               ShipShipmentLineTable.findByPrimaryKey( lShipmentLine );
         assertNotEquals( "Serial number on the original shipment line has been modified",
               SERIAL_NO_OEM, lShipShipmentLine.getSerialNoOem() );
      }
   }


   @Test
   public void testBinLevelUpdateBatchToSerialized() throws MxException, TriggerException {
      // create a serialize part
      iBatchPart = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( RefQtyUnitKey.EA )
            .withTotalQuantity( PART_VALUE ).build();
      LocationKey lLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.BIN );
      } );
      // Create the bin level
      BinLevelTO lBinLevelTO = new BinLevelTO();
      lBinLevelTO.setMaxQt( MAX_QT, "Max" );
      lBinLevelTO.setMinQt( MIN_QT, "Min" );
      new BinLevelService().add( iBatchPart, lLocationKey, lBinLevelTO );

      // call service class to change part to Serialize.
      changeInvClassFromBatchToSer( CONVERSION_FACTOR );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iBatchPart.getPKWhereArg() );
      lArgs.add( lLocationKey.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "INV_LOC_BIN", lArgs );
      while ( lQs.next() ) {
         assertEquals( EXPECTED_MAX_QT, lQs.getDouble( "max_qt" ), 0 );
         assertEquals( EXPECTED_MIN_QT, lQs.getDouble( "min_qt" ), 0 );
      }

   }


   @Test
   public void testHistoricShipmentPreserveUnitOfMeasureBatchToSerialized()
         throws MxException, TriggerException, SQLException {

      // Unit of measure of the batch Part created
      RefQtyUnitKey lQtyUnitKey = LITRE;

      ShipmentKey lShipmentKey = Domain.createShipment( aShipment -> {
         aShipment.setHistorical( true );
      } );
      ShipmentLineKey lShipmentLineKey = Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.part( iBatchPart );
         aShipmentLine.shipmentKey( lShipmentKey );
      } );

      // call service class to change part to Serialize.
      changeInvClassFromBatchToSer( CONVERSION_FACTOR );
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lShipmentLineKey.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE", lArgs );
      while ( lQs.next() ) {
         assertTrue( "Unit of measure not updated",
               lQtyUnitKey.getCd().equals( lQs.getString( "qty_unit_cd" ) ) );
      }
   }


   @Test
   public void testNonHistoricShipmentNotPreserveUnitOfMeasureBatchToSerialized()
         throws MxException, TriggerException, SQLException {

      ShipmentKey lShipmentKey = Domain.createShipment( aShipment -> {
         aShipment.setHistorical( false );
      } );
      ShipmentLineKey lShipmentLineKey = Domain.createShipmentLine( aShipmentLine -> {
         aShipmentLine.part( iBatchPart );
         aShipmentLine.shipmentKey( lShipmentKey );
         aShipmentLine.expectedQuantity( 2.00 );
      } );

      // call service class to change part to Serialize.
      changeInvClassFromBatchToSer( CONVERSION_FACTOR );
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lShipmentLineKey.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "SHIP_SHIPMENT_LINE", lArgs );
      while ( lQs.next() ) {
         assertTrue( "Unit of measure not updated",
               RefQtyUnitKey.EA.getCd().equals( lQs.getString( "qty_unit_cd" ) ) );
      }
   }


   @Test
   public void testScrappedAndArchivedInventoryNotArchived() throws MxException, TriggerException {
      // create scrapped inventory with quantity less than 0.5

      InventoryKey lScrappedInventory = createBatchInventory( RefInvCondKey.SCRAP, 0.3 );

      InventoryKey lArchivedInventory = createBatchInventory( RefInvCondKey.ARCHIVE, 0.3 );

      changeInvClassFromBatchToSer( 1.0 );

      assertEquals( RefInvCondKey.SCRAP,
            InvInvTable.findByPrimaryKey( lScrappedInventory ).getInvCond() );

      assertEquals( RefInvCondKey.ARCHIVE,
            InvInvTable.findByPrimaryKey( lArchivedInventory ).getInvCond() );
   }


   @Test
   public void testUpdateStockRequests() throws MxException, TriggerException {
      PartRequestKey lPartRequest = Domain.createPartRequest( aPartRequest -> {
         aPartRequest.type( RefReqTypeKey.STOCK );
         aPartRequest.purchasePart( iBatchPart );
         aPartRequest.requestedQuantity( INITIAL_QTY );
      } );

      // call service class to change part to Serialize.
      changeInvClassFromBatchToSer( CONVERSION_FACTOR );
      assertEquals( EXPECTED_QTY, ReqPartTable.findByPrimaryKey( lPartRequest ).getReqQt(), 0 );
   }


   @Test
   public void testUpdateKitQuantity() throws MxException, TriggerException {
      PartNoKey aKitPart = Domain.createPart( lKitPart -> {
         lKitPart.setInventoryClass( RefInvClassKey.KIT );
      } );

      new KitBuilder( aKitPart ).withContent( iBatchPart, iBatchPartGroup )
            .withKitQuantity( INITIAL_QTY ).build();

      // call service class to change part to Serialize.
      changeInvClassFromBatchToSer( CONVERSION_FACTOR );

      QuerySet lQs = KitUtils.getKitPartGroupByPartNo( iBatchPart );
      while ( lQs.next() ) {
         Double lActualQuantity = EqpKitPartGroupTable
               .findByPrimaryKey( new EqpKitPartGroupKey( lQs.getInt( "eqp_kit_part_group_db_id" ),
                     lQs.getInt( "eqp_kit_part_group_id" ) ) )
               .getKitQty();
         assertEquals( EXPECTED_QTY, lActualQuantity, 0 );
      }

   }


   @Before
   public void setUp() {
      MxCoreLicense.setValidator( new CoreLicenseStub( FeatureSet.NONE ) );
   }


   @After
   public void tearDown() {
      MxCoreLicense.setValidator( null );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {

      // Create a user and a human resource
      iHr = Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser( aUser -> {
         aUser.setUsername( USERNAME );
         aUser.setUserId( USER_ID );
      } ) ) );

      // Setup security and mandatory boolean values for the inventory creation process
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );
      UserParametersStub iUserParametersStub =
            new UserParametersStub( USER_ID, "SECURED_RESOURCE" );
      iUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            true );
      UserParameters.setInstance( USER_ID, "SECURED_RESOURCE", iUserParametersStub );

      // Create an owner
      iOwner = Domain.createOwner();

      // create a dock location
      iDockLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.DOCK );
         aLocationConfiguration.setIsSupplyLocation( true );
         aLocationConfiguration.setCode( "Dock1" );
      } );

      // create a quarantine location
      iQuarLocation = Domain.createLocation( aLocationConfiguration -> {
         aLocationConfiguration.setType( RefLocTypeKey.QUAR );
         aLocationConfiguration.setCode( "Dock1/Quar" );
      } );

      // create a stock
      iStock = new StockBuilder().withInvClass( RefInvClassKey.BATCH )
            .withGlobalReorderQuantity( 10.0 ).withBatchSize( 20.0 ).withSafetyLevelQt( 30.0 )
            .build();

      // Create a batch part
      iBatchPart = new PartNoBuilder().withOemPartNo( WING_FLAP ).withUnitType( LITRE )
            .withStock( iStock ).withInventoryClass( RefInvClassKey.BATCH )
            .withTotalQuantity( new BigDecimal( 6 ) ).build();

      // create a part group for the part
      iBatchPartGroup = new PartGroupDomainBuilder( "partgptest" )
            .withInventoryClass( RefInvClassKey.BATCH ).withPartNo( iBatchPart ).build();

      // create an Expense account
      iExpenseAccount = new AccountBuilder().withCode( "accounttest" )
            .withType( RefAccountTypeKey.EXPENSE ).build();
   }


   /**
    *
    * Builds a batch part with a given quantity, converts it by the conversion factor and asserts
    * that the expected total quantity is created as a result
    *
    * @param aTotalQt
    *           the total quantity of the part
    * @param aConversionFactor
    *           the conversion factor
    * @param aExpectedQt
    *           the expected quantity after conversion
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   private void assertTotalQuantityAdjustedForConversion( double aTotalQt, double aConversionFactor,
         double aExpectedQt ) throws MxException, TriggerException {
      PartNoKey lBatchPart =
            new PartNoBuilder().withUnitType( LITRE ).withInventoryClass( RefInvClassKey.BATCH )
                  .withTotalQuantity( new BigDecimal( aTotalQt ) ).build();

      new BatchToSerializedChanger( lBatchPart, iHr, aConversionFactor ).change();

      assertEquals( "Total quantity", new BigDecimal( aExpectedQt ),
            EqpPartNo.findByPrimaryKey( lBatchPart ).getTotalQt() );
   }


   /**
    * GIVEN a batch part WHEN the inventory class of the part is changed from BATCH to SER THEN
    * tests the history note for changing the unit of measure code of the original part request.
    *
    * @throws MxException
    * @throws TriggerException
    */
   @Test
   public void testPartRequestHistoryNoteforUoMChange() throws MxException, TriggerException {

      // create STOCK part request
      PartRequestKey iPartReqKeyStock = Domain.createPartRequest( aPartReqKey -> {
         aPartReqKey.purchasePart( iBatchPart );
         aPartReqKey.type( RefReqTypeKey.STOCK );
         aPartReqKey.status( RefEventStatusKey.PRCOMPLETE );
      } );

      // create ADHOC part request
      PartRequestKey iPartReqKeyAdhoc = Domain.createPartRequest( aPartReqKey -> {
         aPartReqKey.purchasePart( iBatchPart );
         aPartReqKey.type( RefReqTypeKey.ADHOC );
         aPartReqKey.status( RefEventStatusKey.PRCOMPLETE );
      } );

      // call service class to change part to SER.
      changeInvClassFromBatchToSer( CONVERSION_FACTOR );

      // get STOCK part request note
      EventKey lEventKeyStock =
            new EventKey( iPartReqKeyStock.getDbId(), iPartReqKeyStock.getId() );
      StageKey lStageKeyStock = new StageKey( lEventKeyStock, 1 );
      EvtStageTable lEvtStageStock = EvtStageTable.findByPrimaryKey( lStageKeyStock );
      String lStageNoteStock = lEvtStageStock.getStageNote();

      // get ADHOC part request note
      EventKey lEventKeyAdhoc =
            new EventKey( iPartReqKeyAdhoc.getDbId(), iPartReqKeyAdhoc.getId() );
      StageKey lStageKeyAdhoc = new StageKey( lEventKeyAdhoc, 3 );
      EvtStageTable lEvtStageAdhoc = EvtStageTable.findByPrimaryKey( lStageKeyAdhoc );
      String lStageNoteAdhoc = lEvtStageAdhoc.getStageNote();

      EqpPartNoTable lEqpPartNoTable = EqpPartNoTable.findByPrimaryKey( iBatchPart );

      String lHistory = i18n.get( "core.msg.CHANGE_PART_REQ_UOM", lEqpPartNoTable.getPartNoOEM(),
            LITRE.getCd(), RefQtyUnitKey.EA.getCd(), CONVERSION_FACTOR, LITRE.getCd() );

      // assert that history note for STOCK part request added
      assertTrue( lHistory.equals( lStageNoteStock ) );

      // assert that history note for ADHOC part request added
      assertTrue( lHistory.equals( lStageNoteAdhoc ) );
   }


   /**
    * Initializes and calls the BatchToSerializedChanger with a conversion factor
    *
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   private void changeInvClassFromBatchToSer( double aConversionFactor )
         throws MxException, TriggerException {
      new BatchToSerializedChanger( iBatchPart, iHr, aConversionFactor ).change();
   }


   /**
    * Fetch unit of measure of the part
    *
    * @param lPartNo
    *           primary key of an event
    * @throws MxException
    *            if an error occurs
    * @throws TriggerException
    *            if an error occurs
    */
   private String getUomOfPart( PartNoKey lPartNo ) throws MxException, TriggerException {
      iEqpPartNo = EqpPartNoTable.findByPrimaryKey( lPartNo );
      String QtyUnitCd = iEqpPartNo.getQtyUnitCd();
      return QtyUnitCd;
   }


   private InventoryKey createBatchInventory( RefInvCondKey aInvCond, double aBinQt ) {
      return new InventoryBuilder().withClass( RefInvClassKey.BATCH ).withPartNo( iBatchPart )
            .atLocation( iDockLocation ).withBinQt( aBinQt ).withCondition( aInvCond )
            .withOwner( iOwner ).isLocked().build();
   }
}
