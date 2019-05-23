
package com.mxi.mx.web.query.dropdown;

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
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the query com.mxi.mx.web.query.dropdown.CalendarParametersForDeadlines.qrx
 *
 * @author bparekh
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CalendarParametersForDeadlinesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            CalendarParametersForDeadlinesTest.class );
   }


   /**
    * Tests the retrieval of calendar parameters.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testCalendarParametersForDeadlines() throws Exception {

      int lIsFault = 0;

      DataSet lDataSet = this.execute( lIsFault );

      lDataSet.next();
      testRow( lDataSet, "CDY" );
      lDataSet.next();
      testRow( lDataSet, "CHR" );
   }


   /**
    * Assert the database rows.
    *
    * @param aDataSet
    *           The DataSet that contains the records.
    * @param aDataTypeCode
    *           The data type code.
    */
   private void testRow( DataSet aDataSet, String aDataTypeCode ) {

      MxAssert.assertEquals( "data_type_cd", aDataTypeCode, aDataSet.getString( "data_type_cd" ) );
   }


   /**
    * This method executes the query in CalendarParametersForDeadlines.qrx
    *
    * @param aIsFault
    *           Indicates whether it is a fault or not.
    *
    * @return The dataset after execution.
    */
   private DataSet execute( int aIsFault ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( "aIsFault", aIsFault );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
