
package com.mxi.mx.core.services.order;

import static org.junit.Assert.assertEquals;

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
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.table.InjectionOverrideRule;
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
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.org.OrgHrTable;
import com.mxi.mx.core.table.po.PoHeaderTable;
import com.mxi.mx.core.table.req.ReqPartTable;


/**
 * This class includes test scenarios for re-issuing an order that is based on ad-hoc part requests
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ReissueOrderTest {

   private FncAccountKey iAccount;

   private LocationKey iDockLocation;

   private GlobalParametersStub iGlobalLogicParams;

   private GlobalParametersStub iGlobalSecureResourceParams;

   private HumanResourceKey iHr;

   private LocationKey iSupplyLocation;

   private String iUserCode = "9999999";

   private VendorKey iVendor;

   private OrderService iOrderService;

   private final String iNotes = "Re-issuing a PO";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Re-issue a purchase order that is based on an ad-hoc part request. The part request is in
    * INSPREQ status and an inventory has been received but not inspected as serviceable yet.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testReIssueOrderFilledByInspectionRequiredPartRequest() throws Exception {

      // build part
      final PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "BATCH_PART_1" ).withShortDescription( "BATCH Part for testing" )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).build();

      // build a part request
      PartRequestKey lPartRequest =
            new PartRequestBuilder().withType( RefReqTypeKey.ADHOC ).withRequestedQuantity( 5.0 )
                  .isNeededAt( iSupplyLocation ).withStatus( RefEventStatusKey.PRINSPREQ ).build();

      // build purchase order
      final PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POAUTH )
            .withVendor( iVendor ).withIssueDate( new Date() ).shippingTo( iDockLocation )
            .withOrderType( RefPoTypeKey.PURCHASE ).withRevisionNumber( 2 ).build();

      // build purchase order line
      final PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.PURCHASE ).withOrderQuantity( new BigDecimal( 5.0 ) )
            .promisedBy( new Date() ).forPart( lPartNoKey ).withAccount( iAccount )
            .forPartRequest( lPartRequest ).build();

      // pre-conditions
      // part request status is INSPREQ
      assertEquals( RefEventStatusKey.PRINSPREQ,
            EvtEventTable.findByPrimaryKey( lPartRequest ).getEventStatus() );

      // purchase order status is POAUTH, but was previously issued
      assertEquals( RefEventStatusKey.POAUTH,
            EvtEventTable.findByPrimaryKey( lOrder ).getEventStatus() );
      assertEquals( 2, PoHeaderTable.findByPrimaryKey( lOrder ).getRevisionNo() );

      // order line is filled by part request
      assertEquals( lOrderLine,
            ReqPartTable.findByPrimaryKey( lPartRequest ).getPurchaseOrderLine() );

      // call service class to re-issue the purchase order
      iOrderService.issue( lOrder, iNotes, iHr );

      // assert part request status remain as INSPREQ
      assertEquals( RefEventStatusKey.PRINSPREQ,
            EvtEventTable.findByPrimaryKey( lPartRequest ).getEventStatus() );

      // purchase order status is ISSUE, and was previously issued
      assertEquals( RefEventStatusKey.POISSUED,
            EvtEventTable.findByPrimaryKey( lOrder ).getEventStatus() );
      assertEquals( 3, PoHeaderTable.findByPrimaryKey( lOrder ).getRevisionNo() );
   }


   /**
    * Re-issue a purchase order that is based on an ad-hoc part request. The part request is in QUAR
    * status since an inventory has been received and quarantined
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testReIssueOrderFilledByQuarantinePartRequest() throws Exception {

      // build part
      final PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "BATCH_PART_2" ).withShortDescription( "BATCH Part for testing" )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).build();

      // build a part request
      PartRequestKey lPartRequest =
            new PartRequestBuilder().withType( RefReqTypeKey.ADHOC ).withRequestedQuantity( 5.0 )
                  .isNeededAt( iSupplyLocation ).withStatus( RefEventStatusKey.PRQUAR ).build();

      // build purchase order
      final PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POAUTH )
            .withVendor( iVendor ).withIssueDate( new Date() ).shippingTo( iDockLocation )
            .withOrderType( RefPoTypeKey.PURCHASE ).withRevisionNumber( 2 ).build();

      // build purchase order line
      final PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.PURCHASE ).withOrderQuantity( new BigDecimal( 5.0 ) )
            .promisedBy( new Date() ).forPart( lPartNoKey ).withAccount( iAccount )
            .forPartRequest( lPartRequest ).build();

      // pre-conditions
      // part request status is QUAR
      assertEquals( EvtEventTable.findByPrimaryKey( lPartRequest ).getEventStatus(),
            RefEventStatusKey.PRQUAR );

      // purchase order status is POAUTH, but was previously issued
      assertEquals( RefEventStatusKey.POAUTH,
            EvtEventTable.findByPrimaryKey( lOrder ).getEventStatus() );
      assertEquals( 2, PoHeaderTable.findByPrimaryKey( lOrder ).getRevisionNo() );

      // order line is filled by part request
      assertEquals( lOrderLine,
            ReqPartTable.findByPrimaryKey( lPartRequest ).getPurchaseOrderLine() );

      // call service class to re-issue the purchase order
      iOrderService.issue( lOrder, iNotes, iHr );

      // assert part request status remain as QUAR
      assertEquals( RefEventStatusKey.PRQUAR,
            EvtEventTable.findByPrimaryKey( lPartRequest ).getEventStatus() );

      // purchase order status is ISSUE, and was previously issued
      assertEquals( RefEventStatusKey.POISSUED,
            EvtEventTable.findByPrimaryKey( lOrder ).getEventStatus() );
      assertEquals( 3, PoHeaderTable.findByPrimaryKey( lOrder ).getRevisionNo() );
   }


   /**
    * Re-issue a purchase order that is based on an ad-hoc part request. The part request is
    * completed since an inventory has been received and inspected as serviceable.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testReIssueOrderFilledByInspectedPartRequest() throws Exception {

      // build part
      final PartNoKey lPartNoKey = new PartNoBuilder().withUnitType( RefQtyUnitKey.EA )
            .withOemPartNo( "BATCH_PART_3" ).withShortDescription( "BATCH Part for testing" )
            .withInventoryClass( RefInvClassKey.BATCH )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withStatus( RefPartStatusKey.ACTV )
            .withRepairBool( false ).withAssetAccount( iAccount ).build();

      // build a part request
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.ADHOC )
            .withRequestedQuantity( 5.0 ).isNeededAt( iSupplyLocation )
            .withStatus( RefEventStatusKey.PRCOMPLETE ).asHistorical().build();

      // build purchase order
      final PurchaseOrderKey lOrder = new OrderBuilder().withStatus( RefEventStatusKey.POAUTH )
            .withVendor( iVendor ).withIssueDate( new Date() ).shippingTo( iDockLocation )
            .withOrderType( RefPoTypeKey.PURCHASE ).withRevisionNumber( 2 ).build();

      // build purchase order line
      final PurchaseOrderLineKey lOrderLine = new OrderLineBuilder( lOrder )
            .withUnitType( RefQtyUnitKey.EA ).withUnitPrice( BigDecimal.TEN )
            .withLineType( RefPoLineTypeKey.PURCHASE ).withOrderQuantity( new BigDecimal( 5.0 ) )
            .promisedBy( new Date() ).forPart( lPartNoKey ).withAccount( iAccount )
            .forPartRequest( lPartRequest ).build();

      // pre-conditions
      // part request status is COMPLETE
      assertEquals( RefEventStatusKey.PRCOMPLETE,
            EvtEventTable.findByPrimaryKey( lPartRequest ).getEventStatus() );

      // part request status is an historic event
      assertEquals( true,
            EvtEventTable.findByPrimaryKey( lPartRequest.getEventKey() ).getHistBool() );

      // purchase order status is POAUTH, but was previously issued
      assertEquals( RefEventStatusKey.POAUTH,
            EvtEventTable.findByPrimaryKey( lOrder ).getEventStatus() );
      assertEquals( 2, PoHeaderTable.findByPrimaryKey( lOrder ).getRevisionNo() );

      // order line is filled by part request
      assertEquals( lOrderLine,
            ReqPartTable.findByPrimaryKey( lPartRequest ).getPurchaseOrderLine() );

      // call service class to re-issue the purchase order
      iOrderService.issue( lOrder, iNotes, iHr );

      // assert part request status remain as COMPLETE
      assertEquals( RefEventStatusKey.PRCOMPLETE,
            EvtEventTable.findByPrimaryKey( lPartRequest ).getEventStatus() );

      // purchase order status is ISSUE, and was previously issued
      assertEquals( RefEventStatusKey.POISSUED,
            EvtEventTable.findByPrimaryKey( lOrder ).getEventStatus() );
      assertEquals( 3, PoHeaderTable.findByPrimaryKey( lOrder ).getRevisionNo() );
   }


   /**
    * Set up the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void loadData() throws Exception {
      iOrderService = new OrderService();
      // create human resource
      iHr = new HumanResourceDomainBuilder().build();

      // create a supply location
      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // create a dock at the supply location
      iDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withSupplyLocation( iSupplyLocation ).build();

      // build finance account
      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      // build org vendor
      iVendor = new VendorBuilder().withCode( "TESTVENDOR" ).build();

      // build org hr with generic user
      OrgHrTable lOrgHrTable = OrgHrTable.create();
      lOrgHrTable.setUserId( new UserKey( iUserCode ) );
      lOrgHrTable.insert();
   }

}
