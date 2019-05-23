
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
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;


/**
 * Ensures <code>PartNoByBomItem</code> query functions properly
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartNoByBomItemTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), PartNoByBomItemTest.class );
   }


   private static final ConfigSlotKey CONFIG_SLOT = new ConfigSlotKey( 4650, "TEST", 1 );
   private static final InventoryKey INVENTORY = new InventoryKey( 4650, 1 );
   private static final PartNoKey INV_PART = new PartNoKey( 4650, 1 );
   private static final PartNoKey PART_ON_INV_PART_GROUP = new PartNoKey( 4650, 2 );
   private static final PartNoKey PART_NOT_ON_INV_PART_GROUP = new PartNoKey( 4650, 3 );
   private static final PartNoKey UNRELATED_PART = new PartNoKey( 4650, 4 );
   private static final PartNoKey DIFF_INV_CLASS_PART = new PartNoKey( 4650, 5 );
   private static final PartNoKey DIFF_FNC_CLASS_PART = new PartNoKey( 4650, 6 );


   /**
    * Ensure the parts that have different financial class are not returned when inventory specified
    */
   @Test
   public void testDoNotIncludeDifferentFncClassWhenInventorySpecified() {
      assertThat( partsFor( CONFIG_SLOT, INVENTORY ), not( hasItem( DIFF_FNC_CLASS_PART ) ) );
   }


   /**
    * Ensure the parts that have different inventory class are not returned when inventory specified
    */
   @Test
   public void testDoNotIncludeDifferentInvClassWhenInventorySpecified() {
      assertThat( partsFor( CONFIG_SLOT, INVENTORY ), not( hasItem( DIFF_INV_CLASS_PART ) ) );
   }


   /**
    * Ensure the parts that do not belong to the same part group are not returned when inventory
    * specified
    */
   @Test
   public void testDoNotIncludeDifferentPartGroupWhenInventorySpecified() {
      assertThat( partsFor( CONFIG_SLOT, INVENTORY ),
            not( hasItem( PART_NOT_ON_INV_PART_GROUP ) ) );
   }


   /**
    * Ensure the parts that do not belong to the same config slot are not returned
    */
   @Test
   public void testDoNotIncludeUnrelatedParts() {
      assertThat( partsFor( CONFIG_SLOT ), not( hasItem( UNRELATED_PART ) ) );
   }


   /**
    * Ensure that all parts on the config slot are returned when inventory is not specified
    */
   @Test
   public void testIncludeAllPartsOnBomItemWhenNoInventorySpecified() {
      assertThat( partsFor( CONFIG_SLOT ),
            hasItems( INV_PART, PART_ON_INV_PART_GROUP, PART_NOT_ON_INV_PART_GROUP ) );
   }


   /**
    * Ensure the parts that belong to the same part group are returned when inventory specified
    */
   @Test
   public void testIncludeSamePartGroupWhenInventorySpecified() {
      assertThat( partsFor( CONFIG_SLOT, INVENTORY ),
            hasItems( INV_PART, PART_ON_INV_PART_GROUP ) );
   }


   /**
    * Returns all parts applicable for the config slot
    *
    * @param aConfigSlot
    *           the config slot
    *
    * @return the parts
    */
   private Set<PartNoKey> partsFor( ConfigSlotKey aConfigSlot ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigSlot, "aAssmblDbId", "aAssmblCd", "aAssmblBomId" );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      Set<PartNoKey> lParts = new HashSet<PartNoKey>();
      while ( lDs.next() ) {
         lParts.add( lDs.getKey( PartNoKey.class, "part_no_key" ) );
      }

      return lParts;
   }


   /**
    * Returns all parts applicable to the same part group as the specified inventory
    *
    * @param aConfigSlot
    *           the config slot
    * @param aInventory
    *           the inventory
    *
    * @return the parts
    */
   private Set<PartNoKey> partsFor( ConfigSlotKey aConfigSlot, InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigSlot, "aAssmblDbId", "aAssmblCd", "aAssmblBomId" );
      lArgs.add( aInventory, "aInvDbId", "aInvId" );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      Set<PartNoKey> lParts = new HashSet<PartNoKey>();
      while ( lDs.next() ) {
         lParts.add( lDs.getKey( PartNoKey.class, "part_no_key" ) );
      }

      return lParts;
   }
}
