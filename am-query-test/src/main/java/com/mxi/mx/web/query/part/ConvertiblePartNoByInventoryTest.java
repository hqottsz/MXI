
package com.mxi.mx.web.query.part;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Ensures that the query returns all convertible parts for an inventory
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ConvertiblePartNoByInventoryTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ConvertiblePartNoByInventoryTest.class );
   }


   private static final InventoryKey INV = new InventoryKey( 4650, 1 );
   private static final InventoryKey INV_WITH_NO_CONFIG_SLOT = new InventoryKey( 4650, 2 );
   private static final InventoryKey INV_WITH_NO_PART_GROUP = new InventoryKey( 4650, 3 );
   private static final PartNoKey INV_PART = new PartNoKey( 4650, 1 );
   private static final PartNoKey ALTERNATE_PART = new PartNoKey( 4650, 2 );
   private static final PartNoKey ALTERNATE_CONFIG_SLOT_PART = new PartNoKey( 4650, 3 );
   private static final PartNoKey DIFFERENT_FINANCIAL_CLASS_PART = new PartNoKey( 4650, 4 );
   private static final PartNoKey DIFFERENT_INVENTORY_CLASS_PART = new PartNoKey( 4650, 5 );
   private static final PartNoKey UNRELATED_PART = new PartNoKey( 4650, 6 );
   private static final PartNoKey INV_WITH_NO_PART_GROUP_PART = new PartNoKey( 4650, 7 );


   /**
    * Ensure that an non-convertible part is not returned
    */
   @Test
   public void testDoesNotReturnAlternateConfigSlotPartNo() {
      assertThat( convertibleParts( INV ), not( hasItem( ALTERNATE_CONFIG_SLOT_PART ) ) );
   }


   /**
    * Ensure that an non-convertible part is not returned
    */
   @Test
   public void testDoesNotReturnDifferentFinancialClassPartNo() {
      assertThat( convertibleParts( INV ), not( hasItem( DIFFERENT_FINANCIAL_CLASS_PART ) ) );
   }


   /**
    * Ensure that an non-convertible part is not returned
    */
   @Test
   public void testDoesNotReturnDifferentInventoryClassPartNo() {
      assertThat( convertibleParts( INV ), not( hasItem( DIFFERENT_INVENTORY_CLASS_PART ) ) );
   }


   /**
    * Ensure that an non-convertible part is not returned
    */
   @Test
   public void testDoesNotReturnUnrelatedPartNo() {
      assertThat( convertibleParts( INV ), not( hasItem( UNRELATED_PART ) ) );
   }


   /**
    * Ensure that an non-convertible part is not returned
    */
   @Test
   public void testReturnAlternateConfigSlotPartNoForInventoryWithNoConfigSlot() {
      assertThat( convertibleParts( INV_WITH_NO_CONFIG_SLOT ),
            hasItems( INV_PART, ALTERNATE_PART, ALTERNATE_CONFIG_SLOT_PART ) );
   }


   /**
    * Ensure that an alternate part is returned
    */
   @Test
   public void testReturnsAlternatePartNo() {
      assertThat( convertibleParts( INV ), hasItem( ALTERNATE_PART ) );
   }


   /**
    * Ensure that the inventory's part is returned
    */
   @Test
   public void testReturnsInventoryPartNo() {
      assertThat( convertibleParts( INV ), hasItem( INV_PART ) );
   }


   /**
    * Ensure that the inventory's part is returned even when the part is not assigned to any part
    * groups
    */
   @Test
   public void testReturnsInventoryPartNo_NoPartGroup() {
      assertThat( convertibleParts( INV_WITH_NO_PART_GROUP ),
            hasItem( INV_WITH_NO_PART_GROUP_PART ) );
   }


   /**
    * Retreives all parts that the inventory can be converted to
    *
    * @param aInventory
    *           the inventory
    *
    * @return the convertible parts
    */
   private Set<PartNoKey> convertibleParts( InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      QuerySet lQs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      Set<PartNoKey> lConvertibleParts = new HashSet<PartNoKey>();
      while ( lQs.next() ) {
         lConvertibleParts.add( lQs.getKey( PartNoKey.class, "part_no_key" ) );
      }

      return lConvertibleParts;
   }
}
