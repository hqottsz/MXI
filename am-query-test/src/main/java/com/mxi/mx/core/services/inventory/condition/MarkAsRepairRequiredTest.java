package com.mxi.mx.core.services.inventory.condition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.services.inventory.exception.MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException;
import com.mxi.mx.core.table.inv.InvInvTable.FinanceStatusCd;


/**
 * * This class tests the
 * {@link DefaultBatchConditionService#markAsRepairRequired(InventoryKey, double, RefStageReasonKey, String, HumanResourceKey)}
 * and
 * {@link DefaultConditionService#markAsRepairRequired(InventoryKey, RefStageReasonKey, String, HumanResourceKey, boolean)}
 * methods.
 *
 * @author ydai
 */
public final class MarkAsRepairRequiredTest {

   private PartNoKey iBatchPart;
   private PartNoKey iSerPart;
   private LocationKey iUSSTGLocation;
   private HumanResourceKey iHr;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Test when mark a Batch inventory which is just received from a repair order(finance status
    * code is RCVD), MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException will be thrown.
    *
    * @throws Exception
    */
   @Test
   public void testExceptionWhenMarkBatchInvReceivedFromROAsRepairRequired() throws Exception {

      InventoryKey lBatchRCVDInventory = createInventoryWithOrder( RefPoTypeKey.REPAIR, iBatchPart,
            RefInvClassKey.BATCH, FinanceStatusCd.RCVD );

      try {

         markBatchInventoryAsRepairRequired( lBatchRCVDInventory );

         // If this line executes, the exception wasn't thrown
         fail( "Expected MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException" );

      } catch ( MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException aException ) {

         assertEquals( "core.err.31649", aException.getMessageKey() );
      }
   }


   /**
    * Test when mark a Batch inventory which is just received from a purchase order(finance status
    * code is NEW), MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException will be thrown.
    *
    * @throws Exception
    */
   @Test
   public void testExceptionWhenMarkBatchInvReceivedFromNonRepairOrderAsRepairRequired()
         throws Exception {

      InventoryKey lBatchNEWInventory = createInventoryWithOrder( RefPoTypeKey.PURCHASE, iBatchPart,
            RefInvClassKey.BATCH, FinanceStatusCd.NEW );

      try {

         markBatchInventoryAsRepairRequired( lBatchNEWInventory );

         // If this line executes, the exception wasn't thrown
         fail( "Expected MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException" );

      } catch ( MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException aException ) {

         assertEquals( "core.err.31649", aException.getMessageKey() );
      }
   }


   /**
    * Test when mark a Batch inventory which has already been inspected(finance status code is
    * INSP), MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException will not be thrown.
    *
    * @throws Exception
    */
   @Test
   public void testNoExceptionWhenMarkBatchInspectedInventoryAsRepairRequired() throws Exception {

      InventoryKey lBatchINSPInventory = createInventoryWithOrder( RefPoTypeKey.PURCHASE,
            iBatchPart, RefInvClassKey.BATCH, FinanceStatusCd.INSP );

      try {

         // inspect the received inventory as serviceable
         markBatchInventoryAsRepairRequired( lBatchINSPInventory );

      } catch ( MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException aException ) {

         // If this line executes, the exception wasn't thrown
         fail( "MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException is not expected" );
      }
   }


   /**
    * Test when mark a Serialized inventory which is just received from a repair order(finance
    * status code is RCVD), MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException will be
    * thrown.
    *
    * @throws Exception
    */
   @Test
   public void testExceptionWhenMarkSerInvReceivedFromROAsRepairRequired() throws Exception {

      InventoryKey lSerRCVDInventory = createInventoryWithOrder( RefPoTypeKey.REPAIR, iSerPart,
            RefInvClassKey.SER, FinanceStatusCd.RCVD );

      try {

         // inspect the received inventory as serviceable
         markSerInventoryAsRepairRequired( lSerRCVDInventory );

         // If this line executes, the exception wasn't thrown
         fail( "Expected MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException" );

      } catch ( MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException aException ) {

         assertEquals( "core.err.31649", aException.getMessageKey() );
      }
   }


   /**
    * Test when mark a Serialized inventory which is just received from a purchase order(finance
    * status code is NEW), MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException will be
    * thrown.
    *
    * @throws Exception
    */
   @Test
   public void testExceptionWhenMarkSerInvReceivedFromNonRepairOrderAsRepairRequired()
         throws Exception {

      InventoryKey lSerNEWInventory = createInventoryWithOrder( RefPoTypeKey.PURCHASE, iSerPart,
            RefInvClassKey.SER, FinanceStatusCd.NEW );

      try {

         // inspect the received inventory as serviceable
         markSerInventoryAsRepairRequired( lSerNEWInventory );

         // If this line executes, the exception wasn't thrown
         fail( "Expected MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException" );

      } catch ( MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException aException ) {

         assertEquals( "core.err.31649", aException.getMessageKey() );
      }
   }


   /**
    * Test when mark a Serialized inventory which has already been inspected(finance status code is
    * INSP), MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException will not be thrown.
    *
    * @throws Exception
    */
   @Test
   public void testNoExceptionWhenMarkSerInspectedInventoryAsRepairRequired() throws Exception {

      InventoryKey lSerINSPInventory = createInventoryWithOrder( RefPoTypeKey.PURCHASE, iSerPart,
            RefInvClassKey.SER, FinanceStatusCd.INSP );

      try {

         // inspect the received inventory as serviceable
         markSerInventoryAsRepairRequired( lSerINSPInventory );

      } catch ( MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException aException ) {

         // If this line executes, the exception wasn't thrown
         fail( "MarkAsRepairRequiredWhileCanBeInspectedAsUnserviceableException is not expected" );
      }
   }


   /**
    * Set the batch inventory properties and call mark inventory as repair required logic.
    *
    * @param aInventory
    *           the received inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   private void markBatchInventoryAsRepairRequired( InventoryKey aInventory ) throws Exception {

      new DefaultBatchConditionService().markAsRepairRequired( aInventory, 1.0, null, null, iHr );
   }


   /**
    * Set the serialized inventory properties and call mark inventory as repair required logic.
    *
    * @param aInventory
    *           the received inventory
    *
    * @throws Exception
    *            if an error occurs
    */
   private void markSerInventoryAsRepairRequired( InventoryKey aInventory ) throws Exception {

      new DefaultConditionService().markAsRepairRequired( aInventory, null, null, null, false );
   }


   /**
    * Create the inventory for Mark as Repair Required
    *
    * @param aRefPoTypeKey
    *           the order type
    * @param aPart
    *           the part
    * @param aRefInvClassKey
    *           the inventory class
    * @param aFinanceStatusCd
    *           the finance status code
    *
    * @return the inventory
    */
   private InventoryKey createInventoryWithOrder( RefPoTypeKey aRefPoTypeKey, PartNoKey aPart,
         RefInvClassKey aRefInvClassKey, FinanceStatusCd aFinanceStatusCd ) {

      PurchaseOrderKey lOrder = new OrderBuilder().withOrderType( aRefPoTypeKey ).build();
      PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder ).build();

      return new InventoryBuilder().withPartNo( aPart ).withClass( aRefInvClassKey )
            .withOrderLine( lOrderLine ).withCondition( RefInvCondKey.INSPREQ ).withBinQt( 1.0 )
            .withFinanceStatusCd( aFinanceStatusCd ).atLocation( iUSSTGLocation ).build();
   }


   @Before
   public void loadData() throws Exception {

      // create hr with user
      iHr = new HumanResourceDomainBuilder().withUserId( 999 ).build();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      // create unserviceable staging location
      iUSSTGLocation = new LocationDomainBuilder().withType( RefLocTypeKey.USSTG ).build();

      // create a batch part
      iBatchPart = new PartNoBuilder().withStatus( RefPartStatusKey.ACTV )
            .withInventoryClass( RefInvClassKey.BATCH ).build();

      // create a serialized part
      iSerPart = new PartNoBuilder().withStatus( RefPartStatusKey.ACTV )
            .withInventoryClass( RefInvClassKey.SER ).build();

   }
}
