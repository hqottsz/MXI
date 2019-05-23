package com.mxi.mx.core.services.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvKitMapKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefOwnerTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.license.CoreLicenseStub;
import com.mxi.mx.core.license.CoreLicenseStub.FeatureSet;
import com.mxi.mx.core.license.MxCoreLicense;
import com.mxi.mx.core.services.inventory.phys.BinInventoryService;
import com.mxi.mx.core.services.inventory.pick.PickedItem;
import com.mxi.mx.core.services.req.IssueInventoryService;
import com.mxi.mx.core.services.req.PickIssueTO;
import com.mxi.mx.core.services.transfer.CreateTransferTO;
import com.mxi.mx.core.services.transfer.TransferService;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvKitMapTable;


public class KitBatchMappingTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final RefInvCondKey INV_COND_RFI = RefInvCondKey.RFI;

   private static final RefOwnerTypeKey OWNER_TYPE_LOCAL = RefOwnerTypeKey.LOCAL;

   private static final String BATCH_NUMBER = "123";

   private static final String KIT_SERIAL_NO = "KIT SERIAL NO";

   private static final String LOC_1 = "LOC_1";

   private HumanResourceKey hr;

   private InventoryKey batchInv;

   private InventoryKey kitInv;

   private LocationKey location;

   private OwnerKey owner;

   private PartNoKey batchPartNo;

   private PartNoKey kitPartNo;

   private Integer currentUserId;


   /**
    * Tests kit and its batch content mapping when the batch quantity is adjusted
    **/
   @Test
   public void testKit_AdjustBatchInvQuantity() throws Exception {

      // Asserts that there's a link between the kit and the batch inventory
      InvKitMapKey invKitMapKey = new InvKitMapKey( kitInv, batchInv );

      InvKitMapTable invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertTrue( invKitMapTable.exists() );

      // Asserts the bin quantity is 1.0
      InvInvTable invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 1 ), invInvTable.getBinQt() );

      /*
       * Test case 1: Adjust the bin quantity greater than 0
       *
       * Result: The link between the kit and the batch inventory remains intact
       */
      new BinInventoryService( batchInv ).setBinQuantity( 5.0, true, true, null, null, null, hr );

      // Asserts the bin quantity is 5.0
      invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 5 ), invInvTable.getBinQt() );

      // Asserts that the link remains intact
      invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertTrue( invKitMapTable.exists() );

      /*
       * Test case 2: Adjust the bin quantity to 0
       *
       * Result: The link between the kit and the batch inventory is removed
       */
      new BinInventoryService( batchInv ).setBinQuantity( 0.0, true, true, null, null, null, hr );

      // Asserts the bin quantity is 0.0
      invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 0 ), invInvTable.getBinQt() );

      // Asserts that the link is removed
      invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertFalse( invKitMapTable.exists() );
   }


   /**
    * Tests the association between a kit and its batch content when the batch inventory is removed
    * from the kit and it is merged into another batch at a different location
    **/
   @Test
   public void testKit_RemoveAndMergeBatchToDiffLocation() throws Exception {

      final String LOC_2 = "LOC2";

      // DATA SETUP: Create a new location
      LocationKey newLocation = Domain.createLocation( loc -> {
         loc.setCode( LOC_2 );
      } );

      // DATA SETUP: Create another batch inventory at a different location
      InventoryKey newBatchInv = Domain.createBatchInventory( batchInventory -> {
         batchInventory.setBinQt( 3.0 );
         batchInventory.setOwner( owner );
         batchInventory.setPartNumber( batchPartNo );
         batchInventory.setLocation( newLocation );
         batchInventory.setCondition( INV_COND_RFI );
         batchInventory.setOwnershipType( OWNER_TYPE_LOCAL );
         batchInventory.setBatchNumber( BATCH_NUMBER );
      } );

      // Asserts the batch inventory quantity in kit is 1.0
      InvInvTable invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 1 ), invInvTable.getBinQt() );

      // Asserts the new batch inventory quantity 3.0
      invInvTable = InvInvTable.findByPrimaryKey( newBatchInv );
      assertEquals( new Double( 3 ), invInvTable.getBinQt() );

      // Asserts that the link between kit and batch exists
      InvKitMapKey invKitMapKey = new InvKitMapKey( kitInv, batchInv );

      InvKitMapTable invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertTrue( invKitMapTable.exists() );

      // Remove the batch inventory from kit
      CreateTransferTO lCreateTransferTO = new CreateTransferTO();
      lCreateTransferTO.setKitInv( kitInv );
      lCreateTransferTO.setInventory( batchInv );
      lCreateTransferTO.setTransferToCd( LOC_2 );
      lCreateTransferTO.setQuantity( 1.0 );
      lCreateTransferTO.setRemoveInvFromKit( true );

      TransferService.create( lCreateTransferTO );

      // Asserts the batch inventory quantity in kit is now 0.0
      invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 0 ), invInvTable.getBinQt() );
      assertEquals( location, invInvTable.getLocation() );

      // Asserts the new batch inventory quantity increases to 4.0
      invInvTable = InvInvTable.findByPrimaryKey( newBatchInv );
      assertEquals( new Double( 4 ), invInvTable.getBinQt() );
      assertEquals( newLocation, invInvTable.getLocation() );

      // Asserts that the link is removed
      invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertFalse( invKitMapTable.exists() );
   }


   /**
    * Tests the association between a kit and its batch content when the batch inventory is removed
    * from the kit and there's no other matching batch
    **/
   @Test
   public void testKit_RemoveBatchAtTheSameLoc() throws Exception {

      // Asserts the batch inventory quantity in kit is 1.0
      InvInvTable invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 1 ), invInvTable.getBinQt() );

      // Asserts that the link between kit and batch exists
      InvKitMapKey invKitMapKey = new InvKitMapKey( kitInv, batchInv );

      InvKitMapTable invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertTrue( invKitMapTable.exists() );

      // Remove the batch inventory from kit without changing the batch's location
      CreateTransferTO lCreateTransferTO = new CreateTransferTO();
      lCreateTransferTO.setKitInv( kitInv );
      lCreateTransferTO.setInventory( batchInv );
      lCreateTransferTO.setTransferToCd( LOC_1 );
      lCreateTransferTO.setQuantity( 1.0 );
      lCreateTransferTO.setRemoveInvFromKit( true );

      TransferService.create( lCreateTransferTO );

      // Asserts the batch inventory quantity in kit is 1.0
      invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertEquals( new Double( 1 ), invInvTable.getBinQt() );
      assertEquals( location, invInvTable.getLocation() );

      // Asserts that the link remains
      invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertFalse( invKitMapTable.exists() );
   }


   /**
    * Tests the kit and its batch content link when the kit is issued to a part request
    **/
   @Test
   public void testKit_IssueKitToPartRequest() throws Exception {

      // Asserts that there's a link between the kit and the batch inventory
      InvKitMapKey invKitMapKey = new InvKitMapKey( kitInv, batchInv );

      InvKitMapTable invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertTrue( invKitMapTable.exists() );

      // Asserts that the batch inventory is not issued
      InvInvTable invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertFalse( invInvTable.isIssuedBool() );
      assertEquals( new Double( 1.0 ), invInvTable.getBinQt() );

      // Asserts that the kit inventory is not issued
      invInvTable = InvInvTable.findByPrimaryKey( kitInv );
      assertFalse( invInvTable.isIssuedBool() );

      // DATA SETUP: Create a part request
      PartRequestKey partRequest = Domain.createPartRequest( partReq -> {
         partReq.reservedInventory( kitInv );
         partReq.requestedQuantity( 1.0 );
         partReq.neededAt( location );
         partReq.status( RefEventStatusKey.PRAVAIL );
      } );

      PickedItem pickedItem =
            new PickedItem( kitPartNo, KIT_SERIAL_NO, INV_COND_RFI.getCd(), 1.0, location, null );
      PickIssueTO pickIssueTO = new PickIssueTO( partRequest );
      pickIssueTO.addPickedItem( pickedItem );

      IssueInventoryService.issueInventory( pickIssueTO, hr );

      // Asserts that the kit inventory is issued
      invInvTable = InvInvTable.findByPrimaryKey( kitInv );
      assertTrue( invInvTable.isIssuedBool() );

      // Asserts that batch inventory is not marked as issued and the quantity is 0
      invInvTable = InvInvTable.findByPrimaryKey( batchInv );
      assertFalse( invInvTable.isIssuedBool() );
      assertEquals( new Double( 0 ), invInvTable.getBinQt() );

      // Asserts that the link is removed
      invKitMapTable = InvKitMapTable.findByPrimaryKey( invKitMapKey );
      assertFalse( invKitMapTable.exists() );
   }


   @Before
   public void setup() {

      MxCoreLicense.setValidator( new CoreLicenseStub( FeatureSet.NONE ) );

      // DATA SETUP: Create a human resource key for the current user
      currentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();

      DataSetArgument args = new DataSetArgument();
      args.add( "user_id", currentUserId );

      QuerySet qs = QuerySetFactory.getInstance().executeQuery( HumanResourceKey.TABLE_NAME, args,
            new String[] { "hr_db_id", "hr_id" } );

      if ( qs.next() ) {
         hr = qs.getKey( HumanResourceKey.class, "hr_db_id", "hr_id" );

      } else {
         hr = Domain.createHumanResource(
               hrBuilder -> hrBuilder.setUser( new UserKey( currentUserId ) ) );
      }

      // DATA SETUP: Create owner
      owner = Domain.createOwner();

      // DATA SETUP: Create location
      location = Domain.createLocation( loc -> {
         loc.setCode( LOC_1 );
         loc.setIsSupplyLocation( true );
      } );

      // DATA SETUP: Create manufacture
      ManufacturerKey manufacturer =
            Domain.createManufacturer( manufact -> manufact.setCode( "MANUFACT" ) );

      // DATA SETUP: Create a batch part
      batchPartNo = Domain.createPart( part -> {
         part.setInventoryClass( RefInvClassKey.BATCH );
         part.setManufacturer( manufacturer );
         part.setCode( "BATCH PART NAME" );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      // DATA SETUP: Create a batch inventory
      batchInv = Domain.createBatchInventory( batchInventory -> {
         batchInventory.setBinQt( 1.0 );
         batchInventory.setPartNumber( batchPartNo );
         batchInventory.setOwner( owner );
         batchInventory.setLocation( location );
         batchInventory.setCondition( INV_COND_RFI );
         batchInventory.setOwnershipType( OWNER_TYPE_LOCAL );
         batchInventory.setBatchNumber( BATCH_NUMBER );
      } );

      // DATA SETUP: Create a kit part
      kitPartNo = Domain.createPart( part -> {
         part.setInventoryClass( RefInvClassKey.KIT );
         part.setManufacturer( manufacturer );
         part.setCode( "KIT PART NAME" );
         part.setQtyUnitKey( RefQtyUnitKey.EA );
         part.setShortDescription( "Kit part description" );
      } );

      // DATA SETUP: Create a kit inventory
      kitInv = Domain.createKitInventory( kitInventory -> {
         kitInventory.addKitContentInventory( batchInv );
         kitInventory.setSerialNo( KIT_SERIAL_NO );
         kitInventory.setPartNo( kitPartNo );
         kitInventory.setLocation( location );
         kitInventory.setOwner( owner );
      } );

      UserParametersStub userParametersStub = new UserParametersStub( currentUserId, "LOGIC" );
      userParametersStub.setBoolean( "ALLOW_AUTO_COMPLETION", true );
      UserParameters.setInstance( currentUserId, "LOGIC", userParametersStub );
   }


   @After
   public void tearDown() {

      MxCoreLicense.setValidator( null );

      UserParameters.setInstance( currentUserId, "LOGIC", null );
   }

}
