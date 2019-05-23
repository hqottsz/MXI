
package com.mxi.mx.web.query.exception;

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
 * This class runs a unit test on the LabourRequirementAlreadyCompleteException query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class LabourRequirementAlreadyCompleteExceptionTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            LabourRequirementAlreadyCompleteExceptionTest.class );
   }


   /** The LABOUR_STAGE_CD */
   private static final String LABOUR_STAGE_CD = "ACTV";

   /** The ACTUAL_START_GDT */
   private static final String ACTUAL_START_GDT = "10-Jul-2009 03:31:13";

   /** The ACTUAL_END_GDT */
   private static final String ACTUAL_END_GDT = "11-Jul-2009 03:31:13";

   /** The DOC_SIGN_KEY */
   private static final String DOC_SIGN_KEY = "4650:100027";


   /**
    * Execute LabourRequirementAlreadyCompleteException.qrx
    */
   @Test
   public void testLabourRequirementAlreadyCompleteException() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aSchedLabourDbId", 4650 );
      lArgs.add( "aSchedLabourId", 100027 );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Get the first row.
      lResult.next();

      // Verify the contents of the first row returned.
      testRow( lResult );

      // Get the next row.
      lResult.next();

      // Verify the contents of the second row returned.
      testSecondRow( lResult );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    */
   private void testRow( DataSet aResult ) {

      // Determine if the following are returned :
      // evt_labour.labour_stage_cd
      MxAssert.assertEquals( LABOUR_STAGE_CD, aResult.getString( "labour_stage_cd" ) );

      // evt_labour.actual_start_gdt
      MxAssert.assertEquals( ACTUAL_START_GDT, aResult.getString( "actual_start_dt" ) );

      // evt_labour.actual_end_gdt
      MxAssert.assertEquals( ACTUAL_END_GDT, aResult.getString( "actual_end_dt" ) );

      // esig_doc_sign.sign_db_id || ':' || esig_doc_sign.sign_id
      MxAssert.assertEquals( DOC_SIGN_KEY, aResult.getString( "doc_sign_key" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }


   /**
    * Verify the contents of the second row returned. There is no data in the <esig_doc_sign> and
    * <esig_doc> pertaining to this row. This is to test the "Outer Join == Null" case.
    *
    * @param aResult
    *           the dataset
    */
   private void testSecondRow( DataSet aResult ) {

      // Determine if the following are returned :
      // evt_labour.labour_stage_cd
      MxAssert.assertEquals( LABOUR_STAGE_CD, aResult.getString( "labour_stage_cd" ) );

      // evt_labour.actual_start_gdt
      MxAssert.assertEquals( ACTUAL_START_GDT, aResult.getString( "actual_start_dt" ) );

      // evt_labour.actual_end_gdt
      MxAssert.assertEquals( ACTUAL_END_GDT, aResult.getString( "actual_end_dt" ) );

      // esig_doc_sign.sign_db_id || ':' || esig_doc_sign.sign_id
      MxAssert.assertEquals( DOC_SIGN_KEY, aResult.getString( "doc_sign_key" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
