package com.mxi.mx.core.services.stocklevel;

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
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;


/**
 * Test the StockLevelAlreadyExistWithLocalOwnerException class
 *
 * @author spatlk
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class StockLevelAlreadyExistWithLocalOwnerExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private LocationKey iLocation;

   private OwnerKey iOwnerNonLocal;
   private static OwnerKey iOwnerLocal1;
   private static OwnerKey iOwnerLocal2;
   private StockNoKey iStockKey;

   private String STOCK_CD = "STOCKCD";
   private String LOC_CD = "ParentSrvstore/StorewithOwner";
   private static final RefStockLowActionKey STOCK_LOW_ACTION = RefStockLowActionKey.MANUAL;
   private static final double SAFETY_LEVEL = 5.0;
   private static final double RESTOCK_QT = 10.0;
   private static final double MAX_LEVEL = 100.0;
   private static final double STATION_WEIGHT_FACTOR = 5.0;
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
      iOwnerLocal1 = Domain.createOwner( aOwner -> {
         aOwner.setCode( "LOCAL1" );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( true );
      } );

      // create an local owner with different code
      iOwnerLocal2 = Domain.createOwner( aOwner -> {
         aOwner.setCode( "LOCAL2" );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( true );
      } );

      iStockKey = new StockBuilder().withStockCode( STOCK_CD ).build();

      // create a warehouse stock level with a local owner
      new StockLevelBuilder( iLocation, iStockKey, iOwnerLocal1 )
            .withWeightFactorQt( STATION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION )
            .withIgnoreOwnerBool( false ).build();

   }


   /**
    *
    * GIVEN a warehouse stock level with local owner , WHEN a another local owner is set to above
    * particular warehouse stock level , THEN exceptions should be raised
    *
    * @throws StockLevelAlreadyExistWithLocalOwnerException
    *            This exception class raise when user try to create a stock level with same
    *            stock/location combination along with multiple local owners.
    */
   @Test( expected = StockLevelAlreadyExistWithLocalOwnerException.class )
   public void testStockLevelAlreadyExistWithLocalOwnerExceptionLocalOwner()
         throws StockLevelAlreadyExistWithLocalOwnerException {

      StockLevelAlreadyExistWithLocalOwnerException.validate( iLocation, iStockKey, iOwnerLocal2 );
   }


   /**
    *
    * GIVEN a warehouse stock level with a local owner, WHEN a non local owner is set to owner to
    * above particular warehouse stock level while creating it , THEN no exceptions should be raised
    *
    * @throws StockLevelAlreadyExistWithLocalOwnerException
    *            This exception class raise when user try to create a stock level with same
    *            stock/location combination along with multiple local owners.
    */
   @Test( expected = Test.None.class )
   public void testStockLevelAlreadyExistWithLocalOwnerExceptionNonLocalOwner()
         throws StockLevelAlreadyExistWithLocalOwnerException {
      StockLevelAlreadyExistWithLocalOwnerException.validate( iLocation, iStockKey,
            iOwnerNonLocal );

   }

}
