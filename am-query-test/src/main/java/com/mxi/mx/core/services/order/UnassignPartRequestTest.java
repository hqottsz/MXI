
package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.exception.InvalidPartRequestAssignmentException;
import com.mxi.mx.core.services.order.exception.OrderIsHistoricException;
import com.mxi.mx.core.services.order.exception.OrderIsReceivedException;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This class test the OrderService.assignPartRequest method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UnassignPartRequestTest {

   private FncAccountKey iAccount;
   private LocationKey iDockLocation;
   private LocationKey iSupplyLocation;
   private HumanResourceKey iHr;
   private PartNoKey iPartNoKey;
   private VendorKey iVendor;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    *
    * Testing that a part request cannot be unassigned from an historic PO.
    *
    * @throws Exception
    */
   @Test( expected = OrderIsHistoricException.class )
   public void testUnassignPartRequestFromHistoricPO() throws Exception {

      // create an closed PO
      PurchaseOrderKey lOrder =
            new OrderBuilder().withStatus( RefEventStatusKey.POCLOSED ).withVendor( iVendor )
                  .asHistoric().withIssueDate( new Date() ).shippingTo( iDockLocation ).build();
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      PartRequestKey lPartRequest = createOnOrderPartRequest( lOrderLine );

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );
      // should throw OrderIsHistoricException
   }


   /**
    *
    * Testing that a part request can be unassigned from an issued PO.
    *
    * @throws Exception
    */
   @Test
   public void testUnassignPartRequestFromIssuedPO() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createPurchaseOrder( RefEventStatusKey.POISSUED );
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      PartRequestKey lPartRequest = createOnOrderPartRequest( lOrderLine );

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );

      // assert part request is open
      assertEquals( RefEventStatusKey.PROPEN,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
   }


   /**
    *
    * Testing that a part request can be unassigned from a partially received PO.
    *
    * @throws Exception
    */
   @Test
   public void testUnassignPartRequestFromPartialPO() throws Exception {

      // create an partial PO
      PurchaseOrderKey lOrder = createPurchaseOrder( RefEventStatusKey.POPARTIAL );
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      PartRequestKey lPartRequest = createOnOrderPartRequest( lOrderLine );

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );

      // assert part request is open
      assertEquals( RefEventStatusKey.PROPEN,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
   }


   /**
    *
    * Testing that a part request cannot be unassigned from a received PO.
    *
    * @throws Exception
    */
   @Test( expected = OrderIsReceivedException.class )
   public void testUnassignPartRequestFromReceivedPO() throws Exception {

      // create an received PO
      PurchaseOrderKey lOrder = createPurchaseOrder( RefEventStatusKey.PORECEIVED );
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      PartRequestKey lPartRequest = createOnOrderPartRequest( lOrderLine );

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );
      // should throw OrderIsReceivedException
   }


   /**
    *
    * Testing that cancelled part request can be unassigned from a PO and that it will keep its
    * CANCEL status after being unassigned.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testUnassignedCancelledPartRequestRemainsCancelled() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createPurchaseOrder( RefEventStatusKey.POISSUED );
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      PartRequestKey lPartRequest =
            new PartRequestBuilder().withStatus( RefEventStatusKey.PRCANCEL ).asHistorical()
                  .forPurchaseLine( lOrderLine ).forPurchasePart( iPartNoKey )
                  .forSpecifiedPart( iPartNoKey ).withType( RefReqTypeKey.TASK )
                  .withRequestedQuantity( 1.0 ).isNeededAt( iSupplyLocation ).build();

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );

      // assert part request is still cancelled
      assertEquals( RefEventStatusKey.PRCANCEL,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
   }


   /**
    *
    * Testing that unassigning a part request clears its ETA date.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testUnassignPartRequestClearsETADate() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createPurchaseOrder( RefEventStatusKey.POISSUED );
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      // create an on-order part request with an ETA date
      PartRequestKey lPartRequest = new PartRequestBuilder()
            .withStatus( RefEventStatusKey.PRONORDER ).withEstimatedArrivalDate( new Date() )
            .forPurchaseLine( lOrderLine ).forPurchasePart( iPartNoKey )
            .forSpecifiedPart( iPartNoKey ).withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 1.0 ).isNeededAt( iSupplyLocation ).build();

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );

      // assert part request is open and has a null ETA date
      assertEquals( RefEventStatusKey.PROPEN,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getEventStatusCd() );
      assertNull( ReqPartTable.findByPrimaryKey( lPartRequest ).getEstArrivalDt() );
   }


   /**
    *
    * Testing that a part request cannot be unassigned from a historic PO.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test( expected = OrderIsHistoricException.class )
   public void testCannotUnassignPartRequestFromHistoricPO() throws Exception {

      // create a historic PO
      PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POCLOSED )
            .asHistoric().withVendor( iVendor ).withIssueDate( new Date() )
            .shippingTo( iDockLocation ).build();
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      PartRequestKey lPartRequest = createOnOrderPartRequest( lOrderLine );

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );
      // should throw OrderIsHistoricException

   }


   /**
    *
    * Testing that a part request cannot be unassigned from a PO to which it is not assigned.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test( expected = InvalidPartRequestAssignmentException.class )
   public void testCannotUnassignPartRequestFromPOIfNotAlreadyAssigned() throws Exception {

      // create an issued PO
      PurchaseOrderKey lOrder = createPurchaseOrder( RefEventStatusKey.POISSUED );
      PurchaseOrderLineKey lOrderLine = createPurchaseOrderLine( lOrder, 10.0 );

      // create a part request that is not assigned to the PO
      PartRequestKey lPartRequest = new PartRequestBuilder()
            .withStatus( RefEventStatusKey.PRONORDER ).forPurchasePart( iPartNoKey )
            .forSpecifiedPart( iPartNoKey ).withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 1.0 ).isNeededAt( iSupplyLocation ).build();

      // unassign part request from PO
      new OrderService().unassignPartRequest( lOrder, new PartRequestKey[] { lPartRequest }, iHr );
      // should throw InvalidPartRequestAssignmentException

   }


   /**
    * Set up the test case data.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void loadData() throws Exception {
      iHr = new HumanResourceDomainBuilder().build();

      // create a supply location
      iSupplyLocation =
            new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // build finance account
      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // create bom part
      PartGroupKey lPartGroup = new PartGroupDomainBuilder( "MYGROUP" ).build();

      // build part
      iPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).withOemPartNo( "123" )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).isAlternateIn( lPartGroup )
            .build();

   }


   /**
    * Create an on-order part request for the given purchase order line.
    *
    * @return part request key
    */
   private PartRequestKey createOnOrderPartRequest( PurchaseOrderLineKey aOrderLine ) {
      return new PartRequestBuilder().withStatus( RefEventStatusKey.PRONORDER )
            .forPurchaseLine( aOrderLine ).forPurchasePart( iPartNoKey )
            .forSpecifiedPart( iPartNoKey ).withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 1.0 ).isNeededAt( iSupplyLocation ).build();
   }


   /**
    * Create a purchase order.
    *
    * @param aStatus
    * @return purchase order key
    */
   private PurchaseOrderKey createPurchaseOrder( RefEventStatusKey aStatus ) {
      return new OrderBuilder().withStatus( aStatus ).withVendor( iVendor )
            .withIssueDate( new Date() ).shippingTo( iDockLocation ).build();
   }


   /**
    * Create a line for the given purchase order.
    *
    * @param aOrder
    * @param aOrderedQuantity
    *
    * @return purchase order line key
    */
   private PurchaseOrderLineKey createPurchaseOrderLine( PurchaseOrderKey aOrder,
         double aOrderedQuantity ) {
      return new OrderLineBuilder( aOrder ).withUnitType( RefQtyUnitKey.EA )
            .withUnitPrice( BigDecimal.TEN ).withLineType( RefPoLineTypeKey.PURCHASE )
            .withOrderQuantity( new BigDecimal( aOrderedQuantity ) ).promisedBy( new Date() )
            .forPart( iPartNoKey ).withAccount( iAccount ).build();
   }

}
