package com.mxi.mx.core.services.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvIetmKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.services.inventory.batch.BatchService;
import com.mxi.mx.core.services.inventory.batch.DefaultBatchService;
import com.mxi.mx.core.table.inv.InvIetm;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.org.OrgHr;


public class DefaultBatchServiceTest {

   private static final String IETM_URL1 = "http://google.ca";
   private static final String IETM_URL2 = "http://google2.ca";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private HumanResourceKey iUser;
   private int iUserId;
   private BatchService iBatchInventoryService = new DefaultBatchService();


   /**
    * Tests cloning Batch Inventory with no IETM assigned
    */
   @Test
   public void cloneBatchInventory_withoutIETM() throws MxException, TriggerException {

      PartNoKey lPartNo = Domain.createPart( aPartNo -> {
         aPartNo.setCode( "TESTPART" );
         aPartNo.setInventoryClass( RefInvClassKey.BATCH );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
         aPartNo.setShortDescription( "TESTPART" );
         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      LocationKey lDockLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      InventoryKey lOriginalBatchInventory = Domain.createBatchInventory( aBatch -> {
         aBatch.setBatchNumber( "BN123" );
         aBatch.setBinQt( 2 );
         aBatch.setPartNumber( lPartNo );
         aBatch.setLocation( lDockLocationKey );
         aBatch.setCondition( RefInvCondKey.RFI );
      } );

      // Split / Clone the batch inventory into two single bin qty batches
      InventoryKey lNewBatchInventory =
            iBatchInventoryService.copyBatch( lOriginalBatchInventory, 1, iUser, false );

      // Ensure we have a new inventory from splitting the batch
      assertTrue( InvInvTable.findByPrimaryKey( lNewBatchInventory ).exists() );

   }


   /**
    * Tests that if batch inventory has IETMs assigned, that all of them are available when the
    * batch inventory is cloned
    */
   @Test
   public void cloneBatchInventory_withIETM() throws MxException, TriggerException {

      PartNoKey lPartNo = Domain.createPart( aPartNo -> {
         aPartNo.setCode( "TESTPART" );
         aPartNo.setInventoryClass( RefInvClassKey.BATCH );
         aPartNo.setPartStatus( RefPartStatusKey.ACTV );
         aPartNo.setShortDescription( "TESTPART" );
         aPartNo.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      LocationKey lDockLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.DOCK );
      } );

      InventoryKey lOriginalBatchInventory = Domain.createBatchInventory( aBatch -> {
         aBatch.setBatchNumber( "BN123" );
         aBatch.setBinQt( 2 );
         aBatch.setPartNumber( lPartNo );
         aBatch.setLocation( lDockLocationKey );
         aBatch.setCondition( RefInvCondKey.RFI );
      } );

      // Add an IETM to the inventory
      InvIetm lOriginalIETM1 = InvIetm.create( lOriginalBatchInventory );
      lOriginalIETM1.setIetmSdesc( "Test IETM" );
      lOriginalIETM1.setIetmLdesc( "Test IETM" );
      lOriginalIETM1.setIetmNote( "Test Note" );
      lOriginalIETM1.setCmdlineParmLdesc( IETM_URL1 );
      lOriginalIETM1.insert();

      // Add a Second IETM to the inventory
      InvIetm lOriginalIETM2 = InvIetm.create( lOriginalBatchInventory );
      lOriginalIETM2.setIetmSdesc( "Test IETM2" );
      lOriginalIETM2.setIetmLdesc( "Test IETM2" );
      lOriginalIETM2.setIetmNote( "Test Note2" );
      lOriginalIETM2.setCmdlineParmLdesc( IETM_URL2 );
      lOriginalIETM2.insert();

      // Split / Clone the batch inventory into two single bin qty batches
      InventoryKey lNewBatchInventory =
            iBatchInventoryService.copyBatch( lOriginalBatchInventory, 1, iUser, false );

      // Ensure we have a new inventory from splitting the batch
      assertTrue( InvInvTable.findByPrimaryKey( lNewBatchInventory ).exists() );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lNewBatchInventory, "aInventoryDbId", "aInventoryId" );

      // Ensure all IETM's exist on the new inventory
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.inventory.GetIetmForInventory", lArgs );

      // Validate the first IETM is linked to the split Batch
      assertTrue( lQs.next() );
      InvIetm lCurrentIetm = InvIetm.findByPrimaryKey(
            lQs.getKey( InvIetmKey.class, "inv_no_db_id", "inv_no_id", "inv_no_ietm_id" ) );
      assertTrue( lCurrentIetm.exists() );
      assertEquals( lCurrentIetm.getIetmCmdlineParmLdesc(), IETM_URL1 );

      // Validate the second IETM is linked to the split batch
      assertTrue( lQs.next() );
      lCurrentIetm = InvIetm.findByPrimaryKey(
            lQs.getKey( InvIetmKey.class, "inv_no_db_id", "inv_no_id", "inv_no_ietm_id" ) );
      assertTrue( lCurrentIetm.exists() );
      assertEquals( lCurrentIetm.getIetmCmdlineParmLdesc(), IETM_URL2 );

   }


   @Before
   public void setup() {

      iUser = new HumanResourceDomainBuilder().build();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iUser ) );
      iUserId = OrgHr.findByPrimaryKey( iUser ).getUserId();

   }


   @After
   public void teardown() {
      SecurityIdentificationUtils.setInstance( null );
      UserParameters.setInstance( iUserId, "SECURED_RESOURCE", null );
   }
}
