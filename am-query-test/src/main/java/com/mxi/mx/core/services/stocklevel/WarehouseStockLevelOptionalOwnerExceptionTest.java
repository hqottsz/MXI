package com.mxi.mx.core.services.stocklevel;

import org.junit.Assert;
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
import com.mxi.mx.core.table.inv.InvOwnerTable;


/**
 * Test for the WarehouseStockLevelOptionalOwnerException
 * 
 * @author spatlk
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class WarehouseStockLevelOptionalOwnerExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private LocationKey iLocationWithOwner;
   private LocationKey iLocationWithoutOwner;
   private OwnerKey iOwnerDefault;
   private static OwnerKey iOwner;
   private StockNoKey iStockKey;

   private String STOCK_CD = "STOCKCD";
   private String LOC_CD_WITHOUT_OWNER = "ParentSrvstore/StorewithoutOwner";
   private String LOC_CD_WITH_OWNER = "ParentSrvstore/StorewithOwner";
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
      iLocationWithOwner = Domain.createLocation( aLocation -> {
         aLocation.setCode( LOC_CD_WITH_OWNER );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
      } );
      // create a location
      iLocationWithoutOwner = Domain.createLocation( aLocation -> {
         aLocation.setCode( LOC_CD_WITHOUT_OWNER );
         aLocation.setType( RefLocTypeKey.SRVSTORE );
      } );

      // create an owner

      iOwnerDefault = Domain.createOwner( aOwner -> {
         aOwner.setCode( "DEFALUTOWNER" );
         aOwner.setDefaultBool( true );
         aOwner.setLocalBool( true );
      } );

      // create an owner
      iOwner = Domain.createOwner( aOwner -> {
         aOwner.setCode( "ACE" );
         aOwner.setDefaultBool( false );
         aOwner.setLocalBool( true );
      } );

      iStockKey = new StockBuilder().withStockCode( STOCK_CD ).build();

      // create a new stock level at a warehouse (srvstore), non-supply location without and owner
      new StockLevelBuilder( iLocationWithoutOwner, iStockKey, iOwnerDefault )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withIgnoreOwnerBool( true )
            .withStockLowAction( STOCK_LOW_ACTION ).build();
      // create a new stock level at a warehouse (srvstore), non-supply location
      // with owner
      new StockLevelBuilder( iLocationWithOwner, iStockKey, iOwner )
            .withWeightFactorQt( STAITION_WEIGHT_FACTOR )
            .withAllocationPercentage( ALLOCATION_PERCENTAGE ).withMinReorderLevel( SAFETY_LEVEL )
            .withReorderQt( RESTOCK_QT ).withMaxLevel( MAX_LEVEL )
            .withBatchSize( REORDER_SHIPPING_QT ).withStockLowAction( STOCK_LOW_ACTION )
            .withIgnoreOwnerBool( false ).build();
   }


   /**
    *
    * GIVEN a warehouse stock level with ignore owner bool true, WHEN a specific owner is set to
    * above particular warehouse stock level , THEN exceptions should be raised
    *
    * @throws WarehouseStockLevelOptionalOwnerException
    *            This exception validates and ensures that the optional and specific owner setting
    *            of warehouse stock level
    */
   @Test( expected = WarehouseStockLevelOptionalOwnerException.class )
   public void testWarehouseStockLevelOptionalOwnerExceptionWithoutOwner()
         throws WarehouseStockLevelOptionalOwnerException {
      String lOwnercd = InvOwnerTable.findByPrimaryKey( iOwner ).getOwnerCd();
      WarehouseStockLevelOptionalOwnerException.validate( iStockKey, iLocationWithoutOwner,
            STOCK_CD, LOC_CD_WITHOUT_OWNER, lOwnercd );
      Assert.fail( "WarehouseStockLevelOptionalOwnerException" );
   }


   /**
    *
    * GIVEN a warehouse stock level with ignore owner bool false, WHEN a default owner is set to
    * above particular warehouse stock level which is owner code is added while creating it , THEN
    * exceptions should be raised
    *
    * @throws WarehouseStockLevelOptionalOwnerException
    *            This exception validates and ensures that the optional and specific owner setting
    *            of warehouse stock level
    */
   @Test( expected = WarehouseStockLevelOptionalOwnerException.class )
   public void testWarehouseStockLevelOptionalOwnerExceptionWithOwner()
         throws WarehouseStockLevelOptionalOwnerException {
      String lOwnercd = null;
      WarehouseStockLevelOptionalOwnerException.validate( iStockKey, iLocationWithOwner, STOCK_CD,
            LOC_CD_WITH_OWNER, lOwnercd );
      Assert.fail( "WarehouseStockLevelOptionalOwnerException" );
   }


   /**
    *
    * GIVEN a warehouse stock level with ignore owner bool true, WHEN a null is set to owner to
    * above particular warehouse stock level while creating it , THEN no exceptions should be raised
    *
    * @throws WarehouseStockLevelOptionalOwnerException
    *            This exception validates and ensures that the optional and specific owner setting
    *            of warehouse stock level
    */
   @Test( expected = Test.None.class )
   public void testWarehouseStockLevelOptionalOwnerExceptionDefaultOwnerNotChange()
         throws WarehouseStockLevelOptionalOwnerException {
      String lOwnercd = null;
      WarehouseStockLevelOptionalOwnerException.validate( iStockKey, iLocationWithoutOwner,
            STOCK_CD, LOC_CD_WITHOUT_OWNER, lOwnercd );

   }


   /**
    *
    * GIVEN a warehouse stock level with ignore owner bool false, WHEN a same specific is set to
    * above particular warehouse stock level while creating it , THEN no exceptions should be raised
    *
    * @throws WarehouseStockLevelOptionalOwnerException
    *            This exception validates and ensures that the optional and specific owner setting
    *            of warehouse stock level
    */
   @Test( expected = Test.None.class )
   public void testWarehouseStockLevelOptionalOwnerExceptionSpecificOwnerNotChange()
         throws WarehouseStockLevelOptionalOwnerException {
      String lOwnercd = InvOwnerTable.findByPrimaryKey( iOwner ).getOwnerCd();
      WarehouseStockLevelOptionalOwnerException.validate( iStockKey, iLocationWithOwner, STOCK_CD,
            LOC_CD_WITH_OWNER, lOwnercd );

   }
}
