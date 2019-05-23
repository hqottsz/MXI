package com.mxi.mx.core.services.inventory.condition;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.core.key.InvKitMapKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.services.inventory.config.InstalledInventoryException;
import com.mxi.mx.core.services.inventory.exception.InspectAsUnserviceableWithInvalidPoTypeException;
import com.mxi.mx.core.services.inventory.exception.InspectAsUnserviceableWithNonReceivedInventoryException;
import com.mxi.mx.core.services.inventory.exception.InvalidMethodForInventoryException;
import com.mxi.mx.core.services.inventory.phys.InspectInventoryTO;
import com.mxi.mx.core.services.inventory.phys.exception.CannotInspectKitAsUnserviceableException;
import com.mxi.mx.core.services.inventory.phys.exception.InvalidConditionException;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.inv.InvKitMapTable;


/**
 * This class tests {@link DefaultConditionService().inspectInventory for the exceptions
 *
 * @author kTandon
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InspectAsUnserviceableValidationTest extends AbstractConditionServiceTestCase {

   private LocationKey iDockLocation;
   private LocationKey iVendorLocation;
   private LocationKey iLocation;
   private InspectInventoryTO iInspectInventoryTO;
   private LocationKey iUSSTGLocation;
   private InventoryKey iInventory;


   /**
    * test when the inventory is associated with borrow order, inspect as unserviceable logic throws
    * an exception
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testBorrowOrderCannotInspectAsUnserviceableException() throws Exception {

      createInventory( RefPoTypeKey.BORROW, InvInvTable.FinanceStatusCd.NEW, iLocation );
      inspectAndAssertException( InspectAsUnserviceableWithInvalidPoTypeException.class );

   }


   /**
    * test when the inventory is associated with consign order, inspect as unserviceable logic
    * throws an exception
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testConsignOrderCannotInspectAsUnserviceableException() throws Exception {

      createInventory( RefPoTypeKey.CONSIGN, InvInvTable.FinanceStatusCd.NEW, iLocation );
      inspectAndAssertException( InspectAsUnserviceableWithInvalidPoTypeException.class );

   }


   /**
    * test when the inventory is associated with consign exchange order, inspect as unserviceable
    * logic throws an exception
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testConsignExOrderCannotInspectAsUnserviceableException() throws Exception {

      createInventory( RefPoTypeKey.CSGNXCHG, InvInvTable.FinanceStatusCd.NEW, iLocation );
      inspectAndAssertException( InspectAsUnserviceableWithInvalidPoTypeException.class );

   }


   /**
    * test when inspect as unserviceable for the repair order throws an exception when inventory is
    * not received
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInspectAsUserviceableWithNonReceivedInventoryExceptionForRepairOrder()
         throws Exception {

      createInventory( RefPoTypeKey.REPAIR, InvInvTable.FinanceStatusCd.INSP, iLocation );
      inspectAndAssertException( InspectAsUnserviceableWithNonReceivedInventoryException.class );

   }


   /**
    * test when inspect as unserviceable for other than repair order throws an exception when
    * inventory is not received
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInspectAsUserviceableWithNonReceivedInventoryExceptionForNonRepairOrder()
         throws Exception {

      createInventory( RefPoTypeKey.PURCHASE, InvInvTable.FinanceStatusCd.INSP, iLocation );
      inspectAndAssertException( InspectAsUnserviceableWithNonReceivedInventoryException.class );

   }


   /**
    * test when the inventory is at vendor location, inspect as unserviceable logic throws an
    * exception
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInventoryAtVendorLocationCannotInspectAsUnserviceable() throws Exception {

      createInventory( RefPoTypeKey.PURCHASE, InvInvTable.FinanceStatusCd.RCVD, iVendorLocation );
      inspectAndAssertException( InspectAsUnserviceableWithNonReceivedInventoryException.class );

   }


   /**
    * Tests to ensure that {@link CannotInspectKitAsUnserviceableException} is thrown.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testCannotInspectKitAsUnserviceableException() throws Exception {

      iInventory = new InventoryBuilder().withClass( RefInvClassKey.KIT ).build();
      InventoryKey lInventory = new InventoryBuilder().withClass( RefInvClassKey.SER ).build();

      // create entry in InvKitMapTable
      InvKitMapKey lInvKitMapKey = new InvKitMapKey( iInventory, lInventory );
      InvKitMapTable lMapping = InvKitMapTable.create( lInvKitMapKey );
      lMapping.insert();

      inspectAndAssertException( CannotInspectKitAsUnserviceableException.class );
   }


   /**
    * Tests to ensure that {@link InvalidMethodForInventoryException} is thrown.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testExceptionForSystemInventory() throws Exception {

      iInventory = new InventoryBuilder().withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW )
            .withClass( RefInvClassKey.SYS ).atLocation( iLocation ).build();

      inspectAndAssertException( InvalidMethodForInventoryException.class );
   }


   /**
    * Tests to ensure that {@link InstalledInventoryException} is thrown.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testExceptionForInstalledInventory() throws Exception {

      InventoryKey lAcftInventory =
            new InventoryBuilder().withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW )
                  .withClass( RefInvClassKey.ACFT ).build();
      iInventory = new InventoryBuilder().withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW )
            .withParentInventory( lAcftInventory ).atLocation( iLocation ).build();

      inspectAndAssertException( InstalledInventoryException.class );

   }


   /**
    * Tests to ensure that {@link InvalidConditionException} is thrown.
    *
    * @throws Exception
    *            if an unexpected error occurs
    */
   @Test
   public void testInvalidInventoryConditionException() throws Exception {

      iInventory = new InventoryBuilder().withFinanceStatusCd( InvInvTable.FinanceStatusCd.NEW )
            .atLocation( iUSSTGLocation ).withCondition( RefInvCondKey.REPREQ ).build();

      inspectAndAssertException( InvalidConditionException.class );

   }


   /**
    * Inspect inventory and make sure the expected exception is thrown.
    *
    * @param aExpectedException
    *           The expected exception class
    * @throws Exception
    *            if an error occurs
    */
   private void inspectAndAssertException( Class<?> aExpectedException ) throws Exception {

      String lExceptionName = aExpectedException.getSimpleName();
      try {
         new DefaultConditionServiceStub().inspectInventory( iInventory, iInspectInventoryTO, iHr );
         Assert.fail( "Expected " + lExceptionName );
      } catch ( MxException lException ) {
         assertTrue( lException.toString().indexOf( lExceptionName ) > -1 );
      }
   }


   /**
    * Create order line.
    *
    * @param aOrderType
    *           the order type
    * @return the created order line
    */
   private PurchaseOrderLineKey createOrderLine( RefPoTypeKey aOrderType ) {

      return new OrderLineBuilder( new OrderBuilder().withOrderType( aOrderType ).build() ).build();
   }


   /**
    * Create an inventory.
    *
    * @param aOrderType
    *           the order line
    * @param aFinanceStatusCd
    *           the finance status code
    * @param aLocation
    *           the location
    * @return the created inventory
    */
   private void createInventory( RefPoTypeKey aPoType, InvInvTable.FinanceStatusCd aFinanceStatusCd,
         LocationKey aLocation ) {

      PurchaseOrderLineKey lOrderLine = createOrderLine( aPoType );

      iInventory = new InventoryBuilder().withFinanceStatusCd( aFinanceStatusCd )
            .atLocation( aLocation ).withCondition( RefInvCondKey.INSPREQ )
            .withOrderLine( lOrderLine ).isIssued().build();
   }


   @Override
   @Before
   public void loadData() throws Exception {

      super.loadData();

      // change the mode to inspect as UnServiceable
      iInspectInventoryTO = new InspectInventoryTO();
      iInspectInventoryTO.setMode( "MODE_INSPECT_AS_UNSERVICEABLE" );

      iLocation = new LocationDomainBuilder().build();

      // create a vendor location for inventory
      iVendorLocation = new LocationDomainBuilder().withType( RefLocTypeKey.VENDOR ).build();

      // create a dock location for inventory
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK ).build();

      // create unserviceable staging location
      iUSSTGLocation = new LocationDomainBuilder().withType( RefLocTypeKey.USSTG )
            .withSupplyLocation( iDockLocation ).build();
   }

}
