
package com.mxi.mx.web.query.inventory;

import static com.mxi.mx.testing.matchers.MxMatchers.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.eqp.EqpBomPart;


/**
 * Ensures that <code>InventorySearch</code> query works
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InventorySearchTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private static final ConfigSlotKey CONFIG_SLOT = new ConfigSlotKey( 4650, "TEST", 0 );
   private static final PartGroupKey PART_GROUP = new PartGroupKey( 4650, 1 );
   private static final InventoryKey INV_ON_PART_GROUP = new InventoryKey( 4650, 1 );
   private static final InventoryKey INV_ON_DIFF_PART_GROUP = new InventoryKey( 4650, 2 );
   private static final InventoryKey INV_ON_DIFF_CONFIG_SLOT = new InventoryKey( 4650, 3 );
   private static final InventoryKey INCOMPAT_INV_ON_DIFF_PART_GROUP = new InventoryKey( 4650, 4 );
   private static final InventoryKey INCOMPAT_INV_ON_DIFF_CONFIG_SLOT = new InventoryKey( 4650, 5 );

   private static final HumanResourceKey TEST_USER = new HumanResourceKey( 4650, 1 );


   /**
    * Ensure all compatible parts on a different config slot for the part group are returned
    */
   @Test
   public void testCompatiblePartGroupInventoryForDifferentConfigSlotReturned() {
      assertThat( inventoryCompatibleTo( PART_GROUP ), hasItem( INV_ON_DIFF_CONFIG_SLOT ) );
   }


   /**
    * Ensure all compatible parts on a different part group for the part group are returned
    */
   @Test
   public void testCompatiblePartGroupInventoryForDifferentPartGroupReturned() {
      assertThat( inventoryCompatibleTo( PART_GROUP ), hasItem( INV_ON_DIFF_PART_GROUP ) );
   }


   /**
    * Ensure all compatible parts for the part group are returned
    */
   @Test
   public void testCompatiblePartGroupInventoryReturned() {
      assertThat( inventoryCompatibleTo( PART_GROUP ), hasItem( INV_ON_PART_GROUP ) );
   }


   /**
    * Ensure all incompatiable parts on a different config slot for the part group are not returned
    */
   @Test
   public void testIncompatiblePartGroupInventoryForDifferentConfigSlotNotReturned() {
      assertThat( inventoryCompatibleTo( PART_GROUP ),
            not( hasItem( INCOMPAT_INV_ON_DIFF_CONFIG_SLOT ) ) );
   }


   /**
    * Ensure all incompatiable parts on a different part group for the part group are not returned
    */
   @Test
   public void testIncompatiblePartGroupInventoryForDifferentPartGroupNotReturned() {
      assertThat( inventoryCompatibleTo( PART_GROUP ),
            not( hasItem( INCOMPAT_INV_ON_DIFF_PART_GROUP ) ) );
   }


   /**
    * Provides a dataset arguments with the default values
    *
    * @return the dataset arguments
    */
   private DataSetArgument getDefaultArguments() {
      DataSetArgument lArgs = new DataSetArgument();
      Calendar cal = Calendar.getInstance();
      cal.add( Calendar.DATE, -30000 );
      Date lRecAfterDt = cal.getTime();

      lArgs.add( TEST_USER, "aHrDbId", "aHrId" );
      lArgs.add( "aPartName", ( String ) null );
      lArgs.add( "aOemPartNo", ( String ) null );
      lArgs.add( "aOemSerialNo", ( String ) null );
      lArgs.add( "aIcn", ( String ) null );
      lArgs.add( "aLocation", ( String ) null );
      lArgs.add( "aReceivedBefore", new Date() );
      lArgs.add( "aReceivedAfter", lRecAfterDt );
      lArgs.add( "aHighestInventory", ( String ) null );
      lArgs.add( "aBarcode", ( String ) null );
      lArgs.add( "aAssembly", ( String ) null );
      lArgs.add( "aBomItem", ( String ) null );
      lArgs.add( "aBomPart", ( String ) null );
      lArgs.add( "aInstalled", "INSTALLED_UNINSTALLED" );
      lArgs.add( "aOwnerCd", ( String ) null );
      lArgs.add( "aInvClass", ( String ) null );
      lArgs.add( "aInvCond", ( String ) null );
      lArgs.add( "aFinClass", ( String ) null );
      lArgs.add( null, "aStockNoDbId", "aStockNoId" );

      lArgs.add( "aIncludeSublocations", true );
      lArgs.add( "aIncludeArchived", true );
      lArgs.add( "aIncludeScrapped", true );
      lArgs.add( "aIncludeIssued", true );
      lArgs.add( "aIncludeZeroQty", true );
      lArgs.add( "aRstatCd", 0 );
      lArgs.add( "aMaxResults", 100 );

      return lArgs;
   }


   /**
    * Returns inventory on the part group specified
    *
    * @param aPartGroup
    *           the part group
    *
    * @return the inventory
    */
   private Set<InventoryKey> inventoryCompatibleTo( PartGroupKey aPartGroup ) {
      DataSetArgument lArgs = getDefaultArguments();
      lArgs.add( "aAssembly", CONFIG_SLOT.getCd() + "%" );
      lArgs.add( "aBomItem", EqpAssmblBom.findByPrimaryKey( CONFIG_SLOT ).getAssemblBomCd() + "%" );
      lArgs.add( "aBomPart", EqpBomPart.findByPrimaryKey( aPartGroup ).getBomPartCd() + "%" );

      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      Set<InventoryKey> lInventoryOnPartGroup = new HashSet<InventoryKey>();
      while ( lDs.next() ) {

         InventoryKey lInventory = lDs.getKey( InventoryKey.class, "inventory_key" );
         if ( lInventoryOnPartGroup.contains( lInventory ) ) {
            fail( "Should only contain one inventory once." );
         }

         lInventoryOnPartGroup.add( lInventory );
      }

      return lInventoryOnPartGroup;
   }
}
