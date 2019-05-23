package com.mxi.mx.web.query.stockdistribution;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockDistReqStatusKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class StockDistRequestDetailsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private QuerySet iQuerySet;
   private LocationKey iMainWarehouse;
   private LocationKey iLineStore;
   private StockNoKey iStockNo;
   private OwnerKey iOwner;

   private static final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
   private static final String STOCK_CODE = "SKCD";
   private static final String STOCK_NAME = "SKNAME";
   private static final Float NEEDED_QTY = new Float( 5.0 );


   @Before
   public void loadData() throws Exception {
      // create main warehouse
      iMainWarehouse = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "mainwarehouse" );
         location.setName( "mainwarehouse" );
         location.setIsSupplyLocation( true );
      } );

      // create line store and make main warehouse as it's supplier
      iLineStore = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.SRVSTORE );
         location.setCode( "linestore" );
         location.setName( "linestore" );
         location.setHubLocation( iMainWarehouse );
      } );

      // create stock
      iStockNo = new StockBuilder().withStockCode( STOCK_CODE ).withStockName( STOCK_NAME )
            .withInvClass( RefInvClassKey.BATCH ).withQtyUnitKey( QTY_UNIT ).build();

      // create default owner
      iOwner = new OwnerDomainBuilder().isDefault().build();
   }


   /**
    * Test to make sure the query returns correct data.
    */
   @Test
   public void testQuery() {

      // create the stock distribution request
      StockDistReqKey lStockDistReq = Domain.createStockDistReq( req -> {
         req.setNeededLocation( iLineStore );
         req.setStockNo( iStockNo );
         req.setOwner( iOwner );
         req.setNeededQuantity( new Float( NEEDED_QTY ) );
         req.setStatus( RefStockDistReqStatusKey.OPEN );
         req.setSupplierLocation( iMainWarehouse );
      } );

      // now execute query to find out details for stock distribution request
      execute( lStockDistReq );

      assertEquals( 1, iQuerySet.getRowCount() );

      // assert data for the returned record
      iQuerySet.next();

      assertEquals( iStockNo, iQuerySet.getKey( StockNoKey.class, "stock_no_key" ) );
      assertEquals( "SKCD (SKNAME)", iQuerySet.getString( "stock_cd_name" ) );
      assertEquals( NEEDED_QTY.doubleValue(), iQuerySet.getDouble( "needed_qty" ), 0 );
      assertEquals( QTY_UNIT, iQuerySet.getString( "std_qty_unit_cd" ) );
      assertEquals( 0, iQuerySet.getInt( "std_decimal_places_qt" ) );
      assertEquals( iLineStore, iQuerySet.getKey( LocationKey.class, "needed_loc" ) );
      assertEquals( iMainWarehouse, iQuerySet.getKey( LocationKey.class, "supplier_loc" ) );
      assertEquals( "linestore", iQuerySet.getString( "needed_loc_cd" ) );
      assertEquals( "linestore (linestore)", iQuerySet.getString( "needed_loc_name" ) );
      assertEquals( "mainwarehouse", iQuerySet.getString( "supplier_loc_cd" ) );
      assertEquals( "mainwarehouse (mainwarehouse)", iQuerySet.getString( "supplier_loc_name" ) );
      assertEquals( RefInvClassKey.BATCH,
            iQuerySet.getKey( RefInvClassKey.class, "inv_class_key" ) );
      assertEquals( "BATCH (Batch)", iQuerySet.getString( "inv_class_cd_sdesc" ) );
      assertEquals( RefStockDistReqStatusKey.OPEN,
            iQuerySet.getKey( RefStockDistReqStatusKey.class, "status" ) );
      assertEquals( "OPEN", iQuerySet.getString( "status_cd" ) );
      assertEquals( iOwner, iQuerySet.getKey( OwnerKey.class, "owner_key" ) );
      assertEquals( iMainWarehouse, iQuerySet.getKey( LocationKey.class, "supply_loc_key" ) );
      assertEquals( null, iQuerySet.getString( "user_id" ) );
      assertEquals( null, iQuerySet.getString( "user_first_last_name" ) );
   }


   private void execute( StockDistReqKey aStockDistReqKey ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( aStockDistReqKey, "aStockDistReqId", "aStockDistReqDbId" );
      iQuerySet = QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }

}
