
package com.mxi.mx.common.unittest.ejb.dao.statement;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.db.qrx.Predicate;
import com.mxi.am.db.qrx.QuerySetColumnPredicate;
import com.mxi.am.db.qrx.QuerySetRowSelector;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.common.ejb.dao.QueryBatchStatement;
import com.mxi.mx.core.common.ejb.dao.statement.QueryStatement;
import com.mxi.mx.core.common.ejb.dao.statement.UpdateStatementBuilder;
import com.mxi.mx.core.common.services.dao.MxBatchDataAccess;
import com.mxi.mx.core.key.InventoryKey;


@RunWith( BlockJUnit4ClassRunner.class )
public final class UpdateStatementBuilderTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /**
    * Ensures that an exception is thrown when both update and where columns contain the same
    * argument
    */
   @Test( expected = IllegalArgumentException.class )
   public void testIllegalArgumentException_ColumnConflict() {
      new UpdateStatementBuilder().withTable( InventoryKey.TABLE_NAME )
            .withUpdateColumns( "inv_cond_cd" ).withWhereColumns( "inv_cond_cd" ).build();
   }


   /**
    * Ensures that an exception is thrown when we do not provide a table
    */
   @Test( expected = IllegalArgumentException.class )
   public void testIllegalArgumentException_NoTable() {
      new UpdateStatementBuilder().withUpdateColumns( "inv_cond_cd" ).build();
   }


   /**
    * Ensures that an exception is thrown when we do not provide the columns to update
    */
   @Test( expected = IllegalArgumentException.class )
   public void testIllegalArgumentException_NoUpdateColumns() {
      new UpdateStatementBuilder().withTable( InventoryKey.TABLE_NAME ).build();
   }


   /**
    * Ensures we can update only specific rows
    */
   @Test
   public void testUpdate_MultipleWhereColumns() {
      try {
         QueryStatement lQueryStatement = new UpdateStatementBuilder()
               .withTable( InventoryKey.TABLE_NAME ).withUpdateColumns( "inv_cond_cd" )
               .withWhereColumns( "inv_no_db_id", "inv_no_id" ).build();

         // Test Pre-condition
         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME,
               null, "inv_no_db_id", "inv_no_id", "inv_cond_cd" );

         Assert.assertEquals( 3, lQuerySet.getRowCount() );
         QuerySetRowSelector.select( lQuerySet, withInvNoId( 1 ) );
         assertInventory( lQuerySet, 4650, 1, "ONE" );

         QuerySetRowSelector.select( lQuerySet, withInvNoId( 2 ) );
         assertInventory( lQuerySet, 4650, 2, "TWO" );

         QuerySetRowSelector.select( lQuerySet, withInvNoId( 3 ) );
         assertInventory( lQuerySet, 4651, 3, "THREE" );

         // Run Statement
         QueryBatchStatement lBatchStatement =
               MxBatchDataAccess.getInstance().getBatchStatement( lQueryStatement );
         lBatchStatement.setInt( "inv_no_db_id", 4650 );
         lBatchStatement.setInt( "inv_no_id", 1 );
         lBatchStatement.setString( "inv_cond_cd", "Test" );
         lBatchStatement.addBatch();
         MxBatchDataAccess.getInstance().sendBatch();

         // Test Post-condition
         lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME, null,
               "inv_no_db_id", "inv_no_id", "inv_cond_cd" );

         Assert.assertEquals( 3, lQuerySet.getRowCount() );
         QuerySetRowSelector.select( lQuerySet, withInvNoId( 1 ) );
         assertInventory( lQuerySet, 4650, 1, "Test" );

         QuerySetRowSelector.select( lQuerySet, withInvNoId( 2 ) );
         assertInventory( lQuerySet, 4650, 2, "TWO" );

         QuerySetRowSelector.select( lQuerySet, withInvNoId( 3 ) );
         assertInventory( lQuerySet, 4651, 3, "THREE" );
      } finally {
         MxBatchDataAccess.getInstance().close();
      }
   }


   private Predicate<QuerySet> withInvNoId( int aId ) {
      return new QuerySetColumnPredicate( BigDecimal.valueOf( aId ), "INV_NO_ID" );
   }


   /**
    * Ensures that we can update all the table
    */
   @Test
   public void testUpdate_NoWhereColumns() {
      try {
         QueryStatement lQueryStatement = new UpdateStatementBuilder()
               .withTable( InventoryKey.TABLE_NAME ).withUpdateColumns( "inv_cond_cd" ).build();

         // Test Pre-condition
         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME,
               null, "inv_no_db_id", "inv_no_id", "inv_cond_cd" );

         Assert.assertEquals( 3, lQuerySet.getRowCount() );
         QuerySetRowSelector.select( lQuerySet, withInvNoId( 1 ) );
         assertInventory( lQuerySet, 4650, 1, "ONE" );

         QuerySetRowSelector.select( lQuerySet, withInvNoId( 2 ) );
         assertInventory( lQuerySet, 4650, 2, "TWO" );

         QuerySetRowSelector.select( lQuerySet, withInvNoId( 3 ) );
         assertInventory( lQuerySet, 4651, 3, "THREE" );

         // Run Statement
         QueryBatchStatement lBatchStatement =
               MxBatchDataAccess.getInstance().getBatchStatement( lQueryStatement );
         lBatchStatement.setString( "inv_cond_cd", "Test" );
         lBatchStatement.addBatch();
         MxBatchDataAccess.getInstance().sendBatch();

         // Test Post-condition
         lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME, null,
               "inv_no_db_id", "inv_no_id", "inv_cond_cd" );

         Assert.assertEquals( 3, lQuerySet.getRowCount() );
         QuerySetRowSelector.select( lQuerySet, withInvNoId( 1 ) );
         assertInventory( lQuerySet, 4650, 1, "Test" );
         QuerySetRowSelector.select( lQuerySet, withInvNoId( 2 ) );
         assertInventory( lQuerySet, 4650, 2, "Test" );
         QuerySetRowSelector.select( lQuerySet, withInvNoId( 3 ) );
         assertInventory( lQuerySet, 4651, 3, "Test" );
      } finally {
         MxBatchDataAccess.getInstance().close();
      }
   }


   /**
    * Ensures the query row matches what is expected
    *
    * @param aQuerySet
    *           the query set
    * @param aInvNoDbId
    *           the expected inv_no_db_id
    * @param aInvNoId
    *           the expected inv_no_id
    * @param aInvCondCd
    *           the expected inv_cond_cd
    */
   private void assertInventory( QuerySet aQuerySet, int aInvNoDbId, int aInvNoId,
         String aInvCondCd ) {
      Assert.assertEquals( aInvNoDbId, aQuerySet.getInt( "inv_no_db_id" ) );
      Assert.assertEquals( aInvNoId, aQuerySet.getInt( "inv_no_id" ) );
      Assert.assertEquals( aInvCondCd, aQuerySet.getString( "inv_cond_cd" ) );
   }
}
