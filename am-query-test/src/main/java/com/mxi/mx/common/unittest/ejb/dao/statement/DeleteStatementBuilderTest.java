
package com.mxi.mx.common.unittest.ejb.dao.statement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.common.ejb.dao.QueryBatchStatement;
import com.mxi.mx.core.common.ejb.dao.statement.DeleteStatementBuilder;
import com.mxi.mx.core.common.ejb.dao.statement.QueryStatement;
import com.mxi.mx.core.common.services.dao.MxBatchDataAccess;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Ensures that the {@link DeleteStatementBuilder} works as expected
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DeleteStatementBuilderTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   /**
    * Ensure that we can build a statement with 1 column
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDeleteMultipleColumn() throws Exception {
      try {
         QueryStatement lQueryStatement = new DeleteStatementBuilder()
               .withColumns( "inv_no_db_id", "inv_no_id" ).withTable( "inv_inv" ).build();

         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME,
               null, "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 3, lQuerySet.getRowCount() );

         QueryBatchStatement lBatchStatement =
               MxBatchDataAccess.getInstance().getBatchStatement( lQueryStatement );
         lBatchStatement.setInt( "inv_no_db_id", 4650 );
         lBatchStatement.setInt( "inv_no_id", 1 );
         lBatchStatement.addBatch();
         MxBatchDataAccess.getInstance().sendBatch();

         lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME, null,
               "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 2, lQuerySet.getRowCount() );
      } finally {
         MxBatchDataAccess.getInstance().close();
      }
   }


   /**
    * Ensure that we can build a statement with no columns
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDeleteNoColumn() throws Exception {
      try {
         QueryStatement lQueryStatement =
               new DeleteStatementBuilder().withTable( "inv_inv" ).build();

         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME,
               null, "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 3, lQuerySet.getRowCount() );

         QueryBatchStatement lBatchStatement =
               MxBatchDataAccess.getInstance().getBatchStatement( lQueryStatement );
         lBatchStatement.addBatch();
         MxBatchDataAccess.getInstance().sendBatch();

         lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME, null,
               "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 0, lQuerySet.getRowCount() );
      } finally {
         MxBatchDataAccess.getInstance().close();
      }
   }


   /**
    * Ensure that we can build a statement with 1 column
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDeleteOneColumn() throws Exception {
      try {
         QueryStatement lQueryStatement = new DeleteStatementBuilder().withColumns( "inv_no_db_id" )
               .withTable( "inv_inv" ).build();

         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME,
               null, "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 3, lQuerySet.getRowCount() );

         QueryBatchStatement lBatchStatement =
               MxBatchDataAccess.getInstance().getBatchStatement( lQueryStatement );
         lBatchStatement.setInt( "inv_no_db_id", 4650 );
         lBatchStatement.addBatch();
         MxBatchDataAccess.getInstance().sendBatch();

         lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME, null,
               "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 1, lQuerySet.getRowCount() );
      } finally {
         MxBatchDataAccess.getInstance().close();
      }
   }


   /**
    * Ensure that an {@link IllegalArgumentException} is thrown when trying to build a DELETE query
    * with no table
    */
   @Test( expected = IllegalArgumentException.class )
   public void testIllegalArgumentException_NoTable() {
      new DeleteStatementBuilder().build();
   }
}
