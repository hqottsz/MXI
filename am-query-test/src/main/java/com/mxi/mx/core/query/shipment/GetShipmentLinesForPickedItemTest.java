package com.mxi.mx.core.query.shipment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * This test ensures that GetShipmentLinesForPickedItem.qrx retrieves shipment lines as expected.
 * Expectation: Retrieve only non MPI generated (normal) lines only if the picked item match
 * directly, Retrieve MPI generated shipment lines with stock alternates of the picked part
 *
 * @author DuHeLK
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetShipmentLinesForPickedItemTest {

   PartNoKey iPart;
   ShipmentKey iShipment;
   ShipmentKey iOtherShipment;
   private final String MP_KEY_SDESC = "MP001";

   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {

      // Create Stock
      StockNoKey lStock = new StockBuilder().build();

      // Create stock alternates
      iPart = new PartNoBuilder().withStock( lStock ).build();

      PartNoKey lPartStockAlternate = new PartNoBuilder().withStock( lStock ).build();

      // Create another part
      PartNoKey lPartNotInStock = new PartNoBuilder().build();

      // Create Shipment
      iShipment = new ShipmentDomainBuilder().build();
      new ShipmentLineBuilder( iShipment ).forPart( iPart ).build();
      new ShipmentLineBuilder( iShipment ).forPart( lPartStockAlternate ).build();
      new ShipmentLineBuilder( iShipment ).forPart( lPartStockAlternate )
            .withMpKeySdesc( MP_KEY_SDESC ).build();
      new ShipmentLineBuilder( iShipment ).forPart( lPartNotInStock ).build();
      new ShipmentLineBuilder( iShipment ).forPart( lPartNotInStock ).withMpKeySdesc( MP_KEY_SDESC )
            .build();

      // Create another Shipment
      iOtherShipment = new ShipmentDomainBuilder().build();
      new ShipmentLineBuilder( iOtherShipment ).forPart( iPart ).build();
      new ShipmentLineBuilder( iOtherShipment ).forPart( lPartStockAlternate ).build();
      new ShipmentLineBuilder( iOtherShipment ).forPart( lPartNotInStock ).build();

   }


   /**
    * This method verify that shipment lines with the exact picked part and MPI generated shipment
    * lines with stock alternate of the picked part are retrieved.
    *
    * Given Shipment with 5 shipment lines, 1.line with exact matching part, 2.normal line with a
    * stock alternate part, 3.MPI generated line with a stock alternate part, 4.normal line with a
    * part not in the same stock, 5.MPI generated line with a part not in the same stock Query
    * should fetch only 2 rows, line 1 and 3
    *
    */
   @Test
   public void testGetShipmentLinesForStockAlternates() {

      QuerySet lResults = executeQuery( iPart, iShipment );
      assertEquals( "Verify MPI generated shipment lines with stock alternates : ", 2,
            lResults.getRowCount() );
   }


   /**
    * This method verify that only shipment lines with the exact picked part are retrieved, when the
    * shipment does not contain any MPI generated lines.
    *
    * Given Shipment with 3 shipment lines, 1.line with exact matching part, 2.line with a stock
    * alternate of the picked part, 3. line with a different part not in the same stock. Query
    * should return only 1 row, line 1.
    *
    */
   @Test
   public void testGetShipmentLinesForDirectMatch() {

      QuerySet lResults = executeQuery( iPart, iOtherShipment );
      assertEquals( "Verify direct match shipment lines : ", 1, lResults.getRowCount() );
   }


   public QuerySet executeQuery( PartNoKey aPartNoKey, ShipmentKey aShipmentKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aShipmentKey, "aShipmentDbId", "aShipmentId" );
      lArgs.add( aPartNoKey, "aPickedPartDbId", "aPickedPartId" );

      return QueryExecutor.executeQuery( iConnection.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
