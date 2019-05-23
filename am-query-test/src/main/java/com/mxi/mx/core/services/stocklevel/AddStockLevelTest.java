
package com.mxi.mx.core.services.stocklevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefStockLowActionKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.location.NotSupplyLocationException;
import com.mxi.mx.core.services.location.NotWarehouseLocationException;
import com.mxi.mx.core.services.order.exception.PartWithFinancialTypeExpenseException;
import com.mxi.mx.core.table.inv.InvLocStockTable;


/**
 * This class tests the {@link StockLevelService.add} method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AddStockLevelTest {

   private StockLevelService iStockLevelService;

   private LocationKey iSupplyLocation;
   private LocationKey iWarehouseLocation;
   private OwnerKey iOwner;
   private FncAccountKey iInvAssetAccount;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the happy path. Supply stock level should be created successfully.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAddingSupplyStockLevel() throws Exception {

      boolean SUPPLY_STOCK_LEVEL = true;
      boolean IGNOREO_WNER = true;

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      InvLocStockKey lStockLevelKey = iStockLevelService.add( lStockNo, iSupplyLocation, iOwner,
            SUPPLY_STOCK_LEVEL, IGNOREO_WNER );

      assertStockLevel( lStockLevelKey, IGNOREO_WNER );
   }


   /**
    * Tests the happy path. Warehouse stock level should be created successfully.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAddingWarehouseStockLevel() throws Exception {

      boolean SUPPLY_STOCK_LEVEL = false;
      boolean IGNOREO_WNER = true;

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      InvLocStockKey lStockLevelKey = iStockLevelService.add( lStockNo, iWarehouseLocation, iOwner,
            SUPPLY_STOCK_LEVEL, IGNOREO_WNER );

      assertStockLevel( lStockLevelKey, IGNOREO_WNER );
   }


   /**
    * Tests the case where the stock level being created already exists.
    * StockLevelAlreadyExistsException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = StockLevelAlreadyExistsException.class )
   public void testAlreadyExistingStockLevel() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      iStockLevelService.add( lStockNo, iSupplyLocation, iOwner, true, false );

      // attempt to create the same stock level again
      iStockLevelService.add( lStockNo, iSupplyLocation, iOwner, true, false );

   }


   /**
    * Tests the case where the chosen location for the stock level is not a supply location.
    * NotSupplyLocationException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = NotSupplyLocationException.class )
   public void testLocationNotSupplyLocation() throws Exception {

      LocationKey lDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withCode( "dock" ).withSupplyLocation( iSupplyLocation ).build();

      NotSupplyLocationException.validate( lDockLocation );

   }


   /**
    * Tests the case where the chosen location for the stock level is not a warehose location.
    * NotWarehouseLocationException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = NotWarehouseLocationException.class )
   public void testLocationNotWarehouseLocation() throws Exception {

      LocationKey lDockLocation = new LocationDomainBuilder().withType( RefLocTypeKey.DOCK )
            .withCode( "dock" ).withSupplyLocation( iSupplyLocation ).build();

      NotWarehouseLocationException.validate( lDockLocation );

   }


   /**
    * Tests the case where the part for the stock has financial type EXPENSE and the stock level is
    * for a non-local owner. PartWithFinancialTypeExpenseException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testPartWithFinancialTypeExpense() throws Exception {

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();

      OwnerKey lNonLocalOwner = new OwnerDomainBuilder().isNonLocal().build();

      FncAccountKey lExpenseAccount = new AccountBuilder().withType( RefAccountTypeKey.EXPENSE )
            .withCode( "EXPENSE" ).build();

      new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).withRepairBool( true )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.EXPENSE )
            .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( new BigDecimal( 100.0 ) )
            .withTotalValue( BigDecimal.ONE ).withAssetAccount( lExpenseAccount )
            .withStock( lStockNo ).build();

      try {
         // attempt to create a stock level with a non-local owner whose stock has an EXPENSE
         // financed part.
         iStockLevelService.add( lStockNo, iSupplyLocation, lNonLocalOwner, true, false );
         fail( "expecting an exception since the part has financial type EXPENSE" );
      } catch ( PartWithFinancialTypeExpenseException e ) {
         //
      }

   }


   @Before
   public void loadData() throws Exception {

      iOwner = new OwnerDomainBuilder().isDefault().build();

      iInvAssetAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      iSupplyLocation = createNewLocation( RefLocTypeKey.AIRPORT, "supply", true );
      iWarehouseLocation = createNewLocation( RefLocTypeKey.SRVSTORE, "warehouse", false );
      iStockLevelService = new StockLevelService();

   }


   /**
    * create Location
    *
    * @param lRefLocTypeKey
    * @param lLocCd
    * @param lIsSupplyLocation
    * @return
    */

   private LocationKey createNewLocation( RefLocTypeKey lRefLocTypeKey, String lLocCd,
         Boolean lIsSupplyLocation ) {
      return Domain.createLocation( aLocation -> {
         aLocation.setCode( lLocCd );
         aLocation.setType( lRefLocTypeKey );
         aLocation.setIsSupplyLocation( lIsSupplyLocation );
      } );
   }


   /**
    *
    * Create a part for the given stock.
    *
    * @param aStock
    *           the stock
    */
   private void createPartForStock( StockNoKey aStock ) {

      new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).withRepairBool( true )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( new BigDecimal( 100.0 ) )
            .withTotalValue( BigDecimal.ONE ).withAssetAccount( iInvAssetAccount )
            .withStock( aStock ).build();
   }


   /**
    * Assert stock level is created as expected
    *
    * @param lStockLevelKey
    * @param lIgnoreOwner
    */
   private void assertStockLevel( InvLocStockKey lStockLevelKey, boolean lIgnoreOwner ) {
      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( lStockLevelKey );
      assertEquals( RefStockLowActionKey.MANUAL, lStockLevel.getStockLowAction() );
      assertEquals( new Double( 0 ), lStockLevel.getReorderQt() );
      assertEquals( new Double( 0 ), lStockLevel.getMinReorderQt() );
      assertEquals( new Double( 0 ), lStockLevel.getMaxQt() );
      assertEquals( new Double( 0 ), lStockLevel.getWeightFactorQt() );
      assertEquals( null, lStockLevel.getBatchSize() );
      assertEquals( lIgnoreOwner, lStockLevel.getIgnoreOwnerBool() );
   }

}
