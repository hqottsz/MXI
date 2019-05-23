package com.mxi.mx.web.query.po;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.OrderLineBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPoLineTypeKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * Query test for AlternateStockPartsByPOLine.qrx, which is used to populate the dropdown with stock
 * alternates in the Line Description column of the Edit PO Line page for MPI generated purchase
 * orders
 *
 */
public class AlternateStockPartsByPOLineTest {

   private static final String STOCK1_NAME = "Stck1";
   private static final String STOCK2_NAME = "Stck2";
   private static final String PART1_IN_STOCK1 = "P1_Stck1";
   private static final String PART2_IN_STOCK1 = "P2_Stck1";
   private static final String PART3_IN_STOCK2 = "P3_Stck2";

   private static StockNoKey iStock1 = null;
   private static StockNoKey iStock2 = null;
   private static PartNoKey iPart1 = null;
   private static PartNoKey iPart2 = null;
   private static PartNoKey iPart3 = null;
   private static PurchaseOrderKey iPO = null;
   private static PartRequestKey iPartRequest = null;
   private static PurchaseOrderLineKey iPOLineWithoutPR = null;
   private static PurchaseOrderLineKey iPOLineWithPR = null;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {

      // build stock
      iStock1 = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).withStockName( STOCK1_NAME )
            .build();
      iStock2 = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).withStockName( STOCK2_NAME )
            .build();

      // build parts
      iPart1 = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( PART1_IN_STOCK1 ).withStock( iStock1 ).build();
      iPart2 = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( PART2_IN_STOCK1 ).withStock( iStock1 ).build();
      iPart3 = new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( PART3_IN_STOCK2 ).withStock( iStock2 ).build();

      // build a Purchase Order
      iPO = new OrderBuilder().withOrderType( RefPoTypeKey.PURCHASE ).build();

      // build an order line for Part1
      iPOLineWithoutPR = new OrderLineBuilder( iPO ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( iPart1 ).build();

      // build an order line for Part3
      iPOLineWithPR = new OrderLineBuilder( iPO ).withLineType( RefPoLineTypeKey.PURCHASE )
            .forPart( iPart3 ).build();

      // build a new part request for the PO line for Part3
      iPartRequest = new PartRequestBuilder().forPurchaseLine( iPOLineWithPR ).build();

   }


   /**
    * Tests whether the AlternateStockPartByPOLine query retrieves alternate parts from the same
    * stock as the part in the purchase order line when there are no associated Part Requests
    */
   @Test
   public void testAlternatePartsInStockWithoutAttachedPR() throws Exception {

      QuerySet lQs = getAlternateStockByPOLine( iPOLineWithoutPR );

      // This PO line is for Part1, in Stock1. There should be two rows returned since Part2 is also
      // in the same stock.
      Assert.assertEquals( "Number of alternate parts in stock", 2, lQs.getRowCount() );
   }


   /**
    * Tests whether the AlternateStockPartsByPOLine query retrieves alternate parts from the same
    * stock as the part in the purchase order line when there is an associated Part Request
    */
   @Test
   public void testAlternatePartsInStockWithAttachedPR() throws Exception {

      QuerySet lQs = getAlternateStockByPOLine( iPOLineWithPR );

      // This PO line is for Part3 in Stock2. Only one row should be returned since there are no
      // other parts in Stock2.
      Assert.assertEquals( "Number of alternate parts in stock", 1, lQs.getRowCount() );

      lQs.next();
      // Assert that the line for Part3 is retrieved
      Assert.assertEquals( "part_no_key of the PO line", iPart3,
            lQs.getKey( PartNoKey.class, "part_no_key" ) );
   }


   private QuerySet getAlternateStockByPOLine( PurchaseOrderLineKey aPOLineKey ) {

      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPOLineKey, "aPoDbId", "aPoId", "aPoLineId" );

      return QueryExecutor.executeQuery( getClass(), lArgs );
   }

}
