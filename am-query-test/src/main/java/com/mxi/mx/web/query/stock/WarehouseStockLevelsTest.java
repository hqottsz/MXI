package com.mxi.mx.web.query.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * Query test for WarehouseStockLevels.qrx
 *
 * @auth spatlk
 * @created February 14,2019
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class WarehouseStockLevelsTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String STOCK_CD = "STOCKCD";
   private static final String LOC_CD = "LAX";
   private static final String OWNER_CD = "OWNERCD";
   private static final double MIN_QT = 2.0;
   private static final double REORDER_QT = 2.0;
   private static final double MAX_QT = 10.0;
   private static final RefStockLowActionKey STOCK_LOW_ACTION = RefStockLowActionKey.MANUAL;

   private static StockNoKey iStockKey;
   private static LocationKey iLocKey;
   private static OwnerKey iOwnerKey;


   @Before
   public void setupData() {

      // create location key
      iLocKey = Domain.createLocation( location -> {
         location.setCode( LOC_CD );
         location.setType( RefLocTypeKey.SRVSTORE );
      } );

      // create owner key
      iOwnerKey = Domain.createOwner( owner -> {
         owner.setCode( OWNER_CD );
      } );

      // create stock key
      iStockKey = new StockBuilder().withStockCode( STOCK_CD ).build();

      // create warehouse location
      new StockLevelBuilder( iLocKey, iStockKey, iOwnerKey ).withStockLowAction( STOCK_LOW_ACTION )
            .withMinReorderLevel( MIN_QT ).withReorderQt( REORDER_QT ).withMaxLevel( MAX_QT )
            .build();
   }


   /**
    *
    * GIVEN a warehouse stock level with a location code and owner code, WHEN the
    * WarehouseStockLevels query is executed, THEN the stock level should be retrieved.
    *
    */
   @Test
   public void testWarehouseStockLevels() {

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.stock.WarehouseStockLevels" );

      lQs.next();

      assertEquals( STOCK_CD, lQs.getString( "stock_no_cd" ) );
      assertEquals( LOC_CD, lQs.getString( "loc_cd" ) );
      assertEquals( OWNER_CD, lQs.getString( "owner_cd" ) );
      assertEquals( MIN_QT, lQs.getFloat( "min_reorder_qt" ), 0 );
      assertEquals( REORDER_QT, lQs.getFloat( "reorder_qt" ), 0 );
      assertEquals( MAX_QT, lQs.getFloat( "max_qt" ), 0 );
      assertEquals( STOCK_LOW_ACTION, lQs.getString( "stock_low_actn_cd" ) );
   }

}
