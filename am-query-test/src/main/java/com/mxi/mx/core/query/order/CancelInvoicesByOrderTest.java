package com.mxi.mx.core.query.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InvoiceLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;


/**
 * This class is used to test the com.mxi.mx.core.query.order.CancelInvoicesByOrder.qrx query.
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CancelInvoicesByOrderTest {

   private static final String INVOICE_NUMBER = "INV-TEST-01";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private PurchaseOrderKey purchaseOrderKey;

   private PurchaseInvoiceKey purchaseInvoiceKey;


   @Before
   public void setup() {

      // Add a repair order
      purchaseOrderKey = Domain.createPurchaseOrder( order -> {
         order.orderType( RefPoTypeKey.REPAIR );
      } );

      // Add repair order lines
      PurchaseOrderLineKey purchaseOrderLineKey1 = Domain.createPurchaseOrderLine( orderLine -> {
         orderLine.orderKey( purchaseOrderKey );
      } );
      PurchaseOrderLineKey purchaseOrderLineKey2 = Domain.createPurchaseOrderLine( orderLine -> {
         orderLine.orderKey( purchaseOrderKey );
      } );

      // Add an invoice
      purchaseInvoiceKey = Domain.createInvoice( invoice -> {
         invoice.setInvoiceNumber( INVOICE_NUMBER );
         invoice.setInvoiceStatus( RefEventStatusKey.ACTV );
      } );

      // Add invoice lines linked with repair order lines
      new InvoiceLineBuilder( purchaseInvoiceKey ).mapToOrderLine( purchaseOrderLineKey1 ).build();
      new InvoiceLineBuilder( purchaseInvoiceKey ).mapToOrderLine( purchaseOrderLineKey2 ).build();

   }


   /**
    *
    * Test if the status of the invoice is changed to CANCEL after the query execution.
    *
    */
   @Test
   public void testCancelInvoices() {

      // Execute the query
      execute( purchaseOrderKey );

      // Retrieve the event of invoice and assert the status
      DataSetArgument roLineArgs = new DataSetArgument();
      roLineArgs.add( purchaseInvoiceKey, "event_db_id", "event_id" );
      QuerySet querySet =
            QuerySetFactory.getInstance().executeQueryTable( "evt_event", roLineArgs );
      if ( querySet.first() ) {
         assertEquals( RefEventStatusKey.CANCEL, querySet.getString( "event_status_cd" ) );
      } else {
         assertFalse( "The invoice cannot be found", true );
      }
   }


   /**
    * Executes the query.
    *
    * @param aKey
    *           Order key.
    *
    * @return {@link DataSet}
    */
   private void execute( PurchaseOrderKey aKey ) {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "aPoDbId", "aPoId" );

      // Execute the query
      QueryExecutor.executeUpdate( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
