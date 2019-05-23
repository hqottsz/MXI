
package com.mxi.mx.core.services.stocklevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AccountBuilder;
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
import com.mxi.mx.core.table.inv.InvLocStockTable;


/**
 * This class tests the {@link StockLevelService.remove} method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RemoveStockLevelTest {

   private StockLevelService iStockLevelService;

   private LocationKey iSupplyLocation;
   private LocationKey iSupplyLocation2;
   private LocationKey iWarehouseLocation;
   private LocationKey iWarehouseLocation2;
   private OwnerKey iOwner;
   private OwnerKey iOwner2;
   private FncAccountKey iInvAssetAccount;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests that attempting to remove a supply stock level with allocation percentage greater than 0
    * and more than one stock level associated to that stock results in an exception.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = StockAllocationPctNotZeroException.class )
   public void testRemovingSupplyStockLevelNoneZeroAllocation() throws Exception {

      boolean SUPPLY_STOCK_LEVEL = true;
      boolean IGNORE_OWNER = true;

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      InvLocStockKey lStockLevelKey = iStockLevelService.add( lStockNo, iSupplyLocation, iOwner,
            SUPPLY_STOCK_LEVEL, IGNORE_OWNER );

      InvLocStockKey lStockLevelKey2 = iStockLevelService.add( lStockNo, iSupplyLocation2, iOwner,
            SUPPLY_STOCK_LEVEL, IGNORE_OWNER );

      assertStockLevel( lStockLevelKey, IGNORE_OWNER );
      assertStockLevel( lStockLevelKey2, IGNORE_OWNER );

      iStockLevelService.remove( lStockLevelKey, new Double( 1 ) );
   }


   /**
    * Tests that attempting to remove a supply stock level with default method remove(key) is
    * successful.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testRemovingSupplyStockLevelNullAllocation() throws Exception {

      boolean SUPPLY_STOCK_LEVEL = true;
      boolean IGNORE_OWNER = true;

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      InvLocStockKey lStockLevelKey = iStockLevelService.add( lStockNo, iSupplyLocation, iOwner,
            SUPPLY_STOCK_LEVEL, IGNORE_OWNER );

      InvLocStockKey lStockLevelKey2 = iStockLevelService.add( lStockNo, iSupplyLocation2, iOwner,
            SUPPLY_STOCK_LEVEL, IGNORE_OWNER );

      assertStockLevel( lStockLevelKey, IGNORE_OWNER );
      assertStockLevel( lStockLevelKey2, IGNORE_OWNER );

      iStockLevelService.remove( lStockLevelKey );
      iStockLevelService.remove( lStockLevelKey2 );

      assertStockLevelRemoved( lStockLevelKey );
      assertStockLevelRemoved( lStockLevelKey2 );
   }


   /**
    * Tests that removing Warehouse stock levels does not throw any
    * StockAllocationPctNotZeroExceptions.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testRemovingWarehouseStockLevel() throws Exception {

      boolean SUPPLY_STOCK_LEVEL = false;
      boolean IGNORE_OWNER = true;

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      InvLocStockKey lStockLevelKey = iStockLevelService.add( lStockNo, iWarehouseLocation, iOwner,
            SUPPLY_STOCK_LEVEL, IGNORE_OWNER );
      InvLocStockKey lStockLevelKey2 = iStockLevelService.add( lStockNo, iWarehouseLocation2,
            iOwner, SUPPLY_STOCK_LEVEL, IGNORE_OWNER );

      assertStockLevel( lStockLevelKey, IGNORE_OWNER );
      assertStockLevel( lStockLevelKey2, IGNORE_OWNER );

      iStockLevelService.remove( lStockLevelKey, null );
      iStockLevelService.remove( lStockLevelKey2, null );

      assertStockLevelRemoved( lStockLevelKey );
      assertStockLevelRemoved( lStockLevelKey2 );
   }


   /**
    * Tests that removing Warehouse stock levels with same location and stock number can be removed
    * with no errors.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testRemoveWarehouseStockLevelSameStockTwoOwners() throws Exception {

      boolean SUPPLY_STOCK_LEVEL = false;
      boolean IGNORE_OWNER = false;

      StockNoKey lStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).build();
      createPartForStock( lStockNo );

      InvLocStockKey lStockLevelKey = iStockLevelService.add( lStockNo, iWarehouseLocation, iOwner,
            SUPPLY_STOCK_LEVEL, IGNORE_OWNER );
      // Set up the same stock level with a different owner.
      InvLocStockKey lStockLevelKey2 = iStockLevelService.add( lStockNo, iWarehouseLocation,
            iOwner2, SUPPLY_STOCK_LEVEL, IGNORE_OWNER );

      assertStockLevel( lStockLevelKey, IGNORE_OWNER );
      assertStockLevel( lStockLevelKey2, IGNORE_OWNER );

      iStockLevelService.remove( lStockLevelKey, null );
      iStockLevelService.remove( lStockLevelKey2, null );

      assertStockLevelRemoved( lStockLevelKey );
      assertStockLevelRemoved( lStockLevelKey2 );
   }


   @Before
   public void loadData() throws Exception {

      iOwner = new OwnerDomainBuilder().isDefault().build();
      iOwner2 = new OwnerDomainBuilder().isNonLocal().build();

      iInvAssetAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      iSupplyLocation = createNewLocation( RefLocTypeKey.AIRPORT, "supply", true );
      iSupplyLocation2 = createNewLocation( RefLocTypeKey.AIRPORT, "supply2", true );
      iWarehouseLocation = createNewLocation( RefLocTypeKey.SRVSTORE, "warehouse", false );
      iWarehouseLocation2 = createNewLocation( RefLocTypeKey.SRVSTORE, "warehouse2", false );

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
   private void assertStockLevel( InvLocStockKey aStockLevelKey, boolean aIgnoreOwner ) {
      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( aStockLevelKey );
      assertEquals( RefStockLowActionKey.MANUAL, lStockLevel.getStockLowAction() );
      assertEquals( new Double( 0 ), lStockLevel.getReorderQt() );
      assertEquals( new Double( 0 ), lStockLevel.getMinReorderQt() );
      assertEquals( new Double( 0 ), lStockLevel.getMaxQt() );
      assertEquals( new Double( 0 ), lStockLevel.getWeightFactorQt() );
      assertEquals( null, lStockLevel.getBatchSize() );
      assertEquals( aIgnoreOwner, lStockLevel.getIgnoreOwnerBool() );
   }


   /**
    * Assert stock level is created as expected
    *
    * @param lStockLevelKey
    * @param lIgnoreOwner
    */
   private void assertStockLevelRemoved( InvLocStockKey aStockLevelKey ) {
      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( aStockLevelKey );
      assertFalse( lStockLevel.exists() );
   }
}
