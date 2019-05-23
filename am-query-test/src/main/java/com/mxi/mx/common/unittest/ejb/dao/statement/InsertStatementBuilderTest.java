
package com.mxi.mx.common.unittest.ejb.dao.statement;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.common.ejb.dao.QueryBatchStatement;
import com.mxi.mx.core.common.ejb.dao.statement.InsertStatementBuilder;
import com.mxi.mx.core.common.ejb.dao.statement.QueryStatement;
import com.mxi.mx.core.common.services.dao.MxBatchDataAccess;
import com.mxi.mx.core.key.InventoryKey;


/**
 * Ensures that the insert query builder works as expected
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InsertStatementBuilderTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Ensures an exception is thrown when no table is provided
    */
   @Test( expected = IllegalArgumentException.class )
   public void testIllegalArgumentException_NoTable() {
      new InsertStatementBuilder().withColumns( "inv_no_db_id", "inv_no_id" ).build();
   }


   /**
    * Ensures an exception is thrown when no value columns are provided
    */
   @Test( expected = IllegalArgumentException.class )
   public void testIllegalArgumentException_NoValues() {
      new InsertStatementBuilder().withTable( "inv_inv" ).build();
   }


   /**
    * Ensures that the query builder works as expected
    */
   @Test
   public void testInsert_MultipleColumns() {

      try {
         QueryStatement lQueryStatement = new InsertStatementBuilder().withTable( "inv_inv" )
               .withColumns( "inv_no_db_id", "inv_no_id" ).build();

         QuerySet lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME,
               null, "inv_no_db_id" );
         Assert.assertEquals( 0, lQuerySet.getRowCount() );

         QueryBatchStatement lBatchStatement =
               MxBatchDataAccess.getInstance().getBatchStatement( lQueryStatement );
         lBatchStatement.setInt( "inv_no_db_id", 4650 );
         lBatchStatement.setInt( "inv_no_id", 1 );
         lBatchStatement.addBatch();
         MxBatchDataAccess.getInstance().sendBatch();

         lQuerySet = QuerySetFactory.getInstance().executeQuery( InventoryKey.TABLE_NAME, null,
               "inv_no_db_id", "inv_no_id" );
         Assert.assertEquals( 1, lQuerySet.getRowCount() );

         Assert.assertTrue( lQuerySet.next() );
         Assert.assertEquals( 4650, lQuerySet.getInt( "inv_no_db_id" ) );
         Assert.assertEquals( 1, lQuerySet.getInt( "inv_no_id" ) );
      } finally {
         MxBatchDataAccess.getInstance().close();
      }
   }
}
