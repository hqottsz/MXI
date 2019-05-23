package com.mxi.mx.web.query.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * Query test for GetLocalOwner.qrx
 *
 * @author spatlk
 */
public class GetLocalOwnerCodesTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private LocationKey iLocation;

   private OwnerKey iOwnerNonLocal;
   private static OwnerKey iOwnerLocal;
   private StockNoKey iStockKey;

   private String STOCK_CD = "STOCKCD";
   private String LOC_CD = "ParentSrvstore/StorewithOwner";
   private String OWNER_CD_LOCAL = "LOCALOWNER";
   private static final RefStockLowActionKey STOCK_LOW_ACTION = RefStockLowActionKey.MANUAL;
   private static final double SAFETY_LEVEL = 5.0;
   private static final double RESTOCK_QT = 10.0;
   private static final double MAX_LEVEL = 100.0;
   private static final double STAITION_WEIGHT_FACTOR = 5.0;
   private static final double ALLOCATION_PERCENTAGE = 12.0;
   private static final double REORDER_SHIPPING_QT = 15.0;


   @Before
   public void setUp() throws Exception {

      // create a location
      iLocation = Domain.createLocation( aLocation -> {
         aLocation.setCode( LOC_CD );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
      } );

      // create an non owner

      iOwnerNonLocal = Domain.createOwner( aOwner -> {
         aOwner.setCode( "NONLOCAL" );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( false );
      } );

      // create an local owner
      iOwnerLocal = Domain.createOwner( aOwner -> {
         aOwner.setCode( OWNER_CD_LOCAL );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( true );
      } );

      iStockKey = new StockBuilder().withStockCode( STOCK_CD ).build();

      // create a warehouse stock level with a local owner
      new StockLevelBuilder( iLocation, iStockKey, iOwnerLocal )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION )
            .withIgnoreOwnerBool( false ).build();

      // create a warehouse stock level with a non local owner
      new StockLevelBuilder( iLocation, iStockKey, iOwnerNonLocal )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION )
            .withIgnoreOwnerBool( false ).build();

   }


   /**
    *
    * GIVEN a warehouse stock level with a location key , stock key , WHEN the GetLocalOwnerCodes
    * query is executed, THEN the owner code should be retrieved.
    *
    */
   @Test
   public void testGetLocalOwnerCodes() {

      // Use a query to find a duplicate a stock level for the owner at the supply location
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( iStockKey, new String[] { "aStockNoDbId", "aStockNoId" } );

      QuerySet lDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.owner.GetLocalOwnerCodes", lArgs );

      if ( lDs.next() ) {

         assertEquals( OWNER_CD_LOCAL, lDs.getString( "localOwnerCode" ) );
         assertEquals( 1, lDs.getRowCount() );
      }

   }

}
