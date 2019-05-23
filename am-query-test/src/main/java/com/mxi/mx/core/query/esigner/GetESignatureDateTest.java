
package com.mxi.mx.core.query.esigner;

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
 * This class runs a unit test on the GetESignatureDate query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetESignatureDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetESignatureDateTest.class );
   }


   /** The SIGNATURE_DATE */
   private static final String SIGNATURE_DATE = "10-Jul-2009 03:31:12";


   /**
    * Execute GetESignatureDate.qrx
    */
   @Test
   public void testGetESignatureDate() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aTaskDbId", 4650 );
      lArgs.add( "aTaskId", 100027 );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Get the first row.
      lResult.next();

      // Verify the contents of the row returned.
      testRow( lResult );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    */
   private void testRow( DataSet aResult ) {

      // Determine if the following are returned :
      // esig_doc_sign.sig_dt
      MxAssert.assertEquals( SIGNATURE_DATE, aResult.getString( "signature_dt" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
