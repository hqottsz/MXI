package com.mxi.mx.core.services.stocklevel.dataservices;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.dataservices.transferobject.BulkLoadElementTO;
import com.mxi.mx.core.table.inv.InvLocStockTable;


/**
 * Ensures that all {@link BulkLoadWarehouseStockLevelService} methods work as expected
 *
 */
public class BulkLoadWarehouseStockLevelServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   LocationKey iLocation;
   OwnerKey iOwner;
   StockNoKey iStock;


   @Before
   public void setUp() throws Exception {

      // create a location
      iLocation = Domain.createLocation( aLocation -> {
         aLocation.setCode( "ParentSrvstore/Store" );

      } );

      // create an owner
      iOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( "OTA" );

      } );

      // create a stock
      iStock = new StockBuilder().withStockCode( "STOCK1" ).build();
   }


   /*
    * Given a stock level with stock code, owner code, location code, safety level, restock level,
    * max level and stock low action WHEN data is added to the database, THEN data is inserted
    * correctly.
    */
   @Test
   public void testStockLevelDataAddedToDatabaseHappyPath() {
      // add stock level data to the transfer object
      BulkLoadElementTO lBulkLoadElementTO = new BulkLoadElementTO();
      lBulkLoadElementTO.setC0( "STOCK1" );
      lBulkLoadElementTO.setC1( "ParentSrvstore/Store" );
      lBulkLoadElementTO.setC2( "OTA" );
      lBulkLoadElementTO.setC3( "5.4999" );
      lBulkLoadElementTO.setC4( "8.5000" );
      lBulkLoadElementTO.setC5( "50.7890" );
      lBulkLoadElementTO.setC6( "MANUAL" );

      // insert data into inv_loc_stock table
      BulkLoadWarehouseStockLevelService lBulkLoadWarehouseStockLevelService =
            new BulkLoadWarehouseStockLevelService();
      lBulkLoadWarehouseStockLevelService.init( lBulkLoadElementTO );

      lBulkLoadWarehouseStockLevelService.processRow( lBulkLoadElementTO );

      InvLocStockKey lLocStockKey = new InvLocStockKey( iLocation, iStock, iOwner );
      InvLocStockTable lInvLocStockTable = InvLocStockTable.findByPrimaryKey( lLocStockKey );

      // assert csv column values with their corresponding table column values
      assertEquals( 5.0, lInvLocStockTable.getMinReorderQt(), 0 );
      assertEquals( 9.0, lInvLocStockTable.getReorderQt(), 0 );
      assertEquals( 51.0, lInvLocStockTable.getMaxQt(), 0 );
      assertEquals( "MANUAL", lInvLocStockTable.getStockLowAction().getCd() );

   }

}
