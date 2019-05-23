package com.mxi.mx.core.services.inventory.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.order.validator.RepairOrderValidator;


/**
 *
 * This class tests the RepairOrderValidator class functionality
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class RepairOrderValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private InventoryKey iInventoryKey_1 = null;
   private InventoryKey iInventoryKey_2 = null;
   private InventoryKey iInventoryKey_3 = null;
   private InventoryKey[] iInventoryKeys = null;
   private PartNoKey iPartNoKey_1 = null;
   private LocationKey iRepairLocationKey_1 = null;
   private LocationKey iRepairLocationKey_2 = null;
   private InventoryKey iInventoryKey_4 = null;
   private PartNoKey iPartNoKey_2 = null;
   private TaskKey iWorkPackageKey_1 = null;
   private PurchaseOrderKey iRepairOrder_1 = null;
   private String RepairLocation_1 = "ABQ";
   private String RepairLocation_2 = "ATL";


   @Before
   public void setup() {

      iPartNoKey_1 = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      iRepairLocationKey_1 = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( RepairLocation_1 );
         aLocation.setType( RefLocTypeKey.USSTG );
      } );

      iRepairLocationKey_2 = Domain.createLocation( aLocation -> {
         aLocation.setIsSupplyLocation( true );
         aLocation.setCode( RepairLocation_2 );
         aLocation.setType( RefLocTypeKey.USSTG );
      } );

      iInventoryKey_1 = Domain.createSerializedInventory( aSerInv -> {
         aSerInv.setLocation( iRepairLocationKey_1 );
         aSerInv.setCondition( RefInvCondKey.REPREQ );
         aSerInv.setPartNumber( iPartNoKey_1 );
         aSerInv.setInvNoSdesc( "default description_1" );
         aSerInv.setLockedBoolean( true );

      } );

      // Inventory is not in REPREQ condition
      iInventoryKey_2 = Domain.createSerializedInventory( aSerInv -> {
         aSerInv.setLocation( iRepairLocationKey_2 );
         aSerInv.setCondition( RefInvCondKey.INSPREQ );
         aSerInv.setPartNumber( iPartNoKey_1 );
         aSerInv.setInvNoSdesc( "default description_2" );

      } );

      // Inventory is AIRCRAFT
      iInventoryKey_3 = Domain.createAircraft( aAircraft -> {
         aAircraft.setLocation( iRepairLocationKey_2 );
         aAircraft.setCondition( RefInvCondKey.INSPREQ );

      } );

      // Inventory with active RO
      // Inventory with scheduled work package
      iInventoryKey_4 = Domain.createSerializedInventory( aInventory -> {
         aInventory.setPartNumber( iPartNoKey_2 );
         aInventory.setInvNoSdesc( "default description_4" );
         aInventory.setLocation( iRepairLocationKey_1 );
      } );

      iPartNoKey_2 = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
      } );

      final Date lDate = new Date();

      iWorkPackageKey_1 = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAircraft( iInventoryKey_4 );
         aWorkPackage.setStatus( RefEventStatusKey.ACTV );
         aWorkPackage.setLocation( iRepairLocationKey_1 );
         aWorkPackage.setScheduledStartDate( lDate );
         aWorkPackage.setScheduledEndDate( DateUtils.addDays( lDate, 5 ) );

      } );

      iRepairOrder_1 = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
         aOrder.status( RefEventStatusKey.ACTV );
      } );;

      Domain.createPurchaseOrderLine( aLine -> {
         aLine.task( iWorkPackageKey_1 );
         aLine.orderKey( iRepairOrder_1 );
         aLine.lineType( RefPoLineTypeKey.REPAIR );
      } );

   }


   /**
    *
    * GIVEN a REPREQ inventory, a INSPREQ inventory, a Aircraft Inventory and a inventory with
    * Active RO and Scheduled WP in multiple locations WHEN call RepairOrderValidator THEN return
    * error messages.
    *
    */
   @Test
   public void testRepairOrderValidator() throws MxException, TriggerException {
      iInventoryKeys = new InventoryKey[] { iInventoryKey_1, iInventoryKey_2, iInventoryKey_3,
            iInventoryKey_4, };

      try {
         RepairOrderValidator lValidator = new RepairOrderValidator( iInventoryKeys );
         lValidator.validate();
         fail( "Expected RepairOrderValidationException" );
      } catch ( ValidationException e ) {
         MxMessage[] lActualValidationErrors = e.getErrorWarningMessages();

         // Check message titles
         for ( int i = 0; i < lActualValidationErrors.length; i++ ) {
            assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_title",
                  lActualValidationErrors[i].getMsgTitleKey() );
         }

         // Check error messages
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_NOT_IN_SAME_SUPPLY_LOC_message",
               lActualValidationErrors[0].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_IS_AIRCRAFT_OR_KIT_message",
               lActualValidationErrors[1].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_LOCKED_message",
               lActualValidationErrors[2].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_NOT_REPREQ_message",
               lActualValidationErrors[3].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_NOT_REPREQ_message",
               lActualValidationErrors[4].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_NOT_REPREQ_message",
               lActualValidationErrors[5].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_ASSIGN_TO_RO_message",
               lActualValidationErrors[6].getMsgFrameKey() );
         assertEquals( "core.msg.INV_CANNOT_REPAIR_RETURN_ASSIGN_SCHEDULED_WP_message",
               lActualValidationErrors[7].getMsgFrameKey() );

         // Check messages parameter values
         assertEquals( "default description",
               lActualValidationErrors[1].getParameter( 0 ).getValue() );
         assertEquals( "default description_1",
               lActualValidationErrors[2].getParameter( 0 ).getValue() );
         assertEquals( "default description_2",
               lActualValidationErrors[3].getParameter( 0 ).getValue() );
         assertEquals( "default description",
               lActualValidationErrors[4].getParameter( 0 ).getValue() );
         assertEquals( "default description_4",
               lActualValidationErrors[5].getParameter( 0 ).getValue() );
         assertEquals( "default description_4",
               lActualValidationErrors[6].getParameter( 0 ).getValue() );
         assertEquals( "default description_4",
               lActualValidationErrors[7].getParameter( 0 ).getValue() );
      }

   }
}
