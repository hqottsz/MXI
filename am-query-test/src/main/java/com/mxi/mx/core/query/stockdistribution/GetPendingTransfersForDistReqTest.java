package com.mxi.mx.core.query.stockdistribution;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryTransferBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.TransferKey;


/**
 * This test class ensures that GetPendingTransfersForDistReq.qrx query returns correct data.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetPendingTransfersForDistReqTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensures that query returns only pending transfers associated to the stock distribution
    * request.
    */
   @Test
   public void testPendingTrasnfersForDistReq() {

      // create pending transfer
      TransferKey lPendXfer =
            new InventoryTransferBuilder().withStatus( RefEventStatusKey.LXPEND ).build();

      // create completed transfer
      TransferKey lCompletedXfer =
            new InventoryTransferBuilder().withStatus( RefEventStatusKey.LXCMPLT ).build();

      // create cancelled transfer
      TransferKey lCancelledXfer =
            new InventoryTransferBuilder().withStatus( RefEventStatusKey.LXCANCEL ).build();

      // create a stock distribution request
      StockDistReqKey lStockDistReq = Domain.createStockDistReq( pojo -> {
         pojo.setNeededQuantity( new Float( 5.0 ) );
         pojo.setStatus( RefStockDistReqStatusKey.INPROGRESS );
         pojo.setQtyUnit( RefQtyUnitKey.EA );
      } );

      // link all the transfers and the stock distribution request to mimic picked items
      Domain.createStockDistReqPickedItem( pojo -> {
         pojo.setStockDistReq( lStockDistReq );
         pojo.setTransferKey( lPendXfer );
      } );

      Domain.createStockDistReqPickedItem( pojo -> {
         pojo.setStockDistReq( lStockDistReq );
         pojo.setTransferKey( lCompletedXfer );
      } );

      Domain.createStockDistReqPickedItem( pojo -> {
         pojo.setStockDistReq( lStockDistReq );
         pojo.setTransferKey( lCancelledXfer );
      } );

      // execute query
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lStockDistReq, "aDistReqId", "aDistReqDbId" );
      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );

      // assert only 1 row is returned for pending transfer
      assertEquals( 1, lQs.getRowCount() );

      // assert data for the returned record
      lQs.next();
      assertEquals( lPendXfer, lQs.getKey( TransferKey.class, "xfer_key" ) );
   }

}
