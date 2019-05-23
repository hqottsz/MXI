
package com.mxi.mx.web.query.usage;

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
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class DataValuesByDataTypeTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), DataValuesByDataTypeTest.class );
   }


   public static final DataTypeKey DATATYPE_KEY = new DataTypeKey( 4650, 1003 );


   /**
    * Tests that the query returns the data in the proper order
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testOrder() throws Exception {

      // Execute the query
      DataSet lDs = execute( DATATYPE_KEY );

      // sorting must be case insensitive and alphabetical
      MxAssert.assertEquals( "Ordering", "10", lDs.getStringAt( 1, "data_value_cd" ) );
      MxAssert.assertEquals( "Ordering", "1000", lDs.getStringAt( 2, "data_value_cd" ) );
      MxAssert.assertEquals( "Ordering", "200", lDs.getStringAt( 3, "data_value_cd" ) );
      MxAssert.assertEquals( "Ordering", "300", lDs.getStringAt( 4, "data_value_cd" ) );
      MxAssert.assertEquals( "Ordering", "aab", lDs.getStringAt( 5, "data_value_cd" ) );
      MxAssert.assertEquals( "Ordering", "AAC", lDs.getStringAt( 6, "data_value_cd" ) );
      MxAssert.assertEquals( "Ordering", "xyz", lDs.getStringAt( 7, "data_value_cd" ) );
   }


   /**
    * Execute the query.
    *
    * @param aDataTypeKey
    *           data type key.
    *
    * @return dataSet result.
    */
   private DataSet execute( DataTypeKey aDataTypeKey ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aDataTypeKey, "aDataTypeDbId", "aDataTypeId" );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
