
package com.mxi.mx.web.query.maint;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ReqsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   /** The maintenance program key */
   public static final MaintPrgmKey MAINT_PRGM_KEY = new MaintPrgmKey( 100, 100 );


   /**
    * Tests that the query returns two requirements, REQ 1 is applicable and REQ 2 is not applicable
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();
      lResult.addSort( "maint_task_key", true );

      // asserts that 2 rows are returned
      assertEquals( 2, lResult.getRowCount() );

      // assert REQ 1 is applicable
      lResult.first();

      MxAssert.assertEquals( true, lResult.getBoolean( "applicable" ) );
      MxAssert.assertEquals( "REQ TEST 1 (Req test 1 name)",
            lResult.getString( "maint_task_cd_name" ) );
      MxAssert.assertNull( lResult.getString( "group_cd" ) );
      MxAssert.assertEquals( "A320", lResult.getString( "config_slot_cd" ) );
      MxAssert.assertEquals( "REQ", lResult.getString( "class_subclass" ) );
      MxAssert.assertEquals( 1, lResult.getInt( "issue_ord" ) );
      MxAssert.assertNull( lResult.getString( "prev_revision_ord" ) );
      MxAssert.assertEquals( 1, lResult.getInt( "revision_ord" ) );
      MxAssert.assertEquals( 2, lResult.getInt( "latest_revision_ord" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "in_revision_bool" ) );

      // assert REQ 2 is not applicable
      lResult.next();

      MxAssert.assertEquals( false, lResult.getBoolean( "applicable" ) );
      MxAssert.assertEquals( "REQ TEST 2 (Req test 2 name)",
            lResult.getString( "maint_task_cd_name" ) );
      MxAssert.assertNull( lResult.getString( "group_cd" ) );
      MxAssert.assertEquals( "A320", lResult.getString( "config_slot_cd" ) );
      MxAssert.assertEquals( "REQ", lResult.getString( "class_subclass" ) );
      MxAssert.assertEquals( 1, lResult.getInt( "issue_ord" ) );
      MxAssert.assertNull( lResult.getString( "prev_revision_ord" ) );
      MxAssert.assertEquals( 1, lResult.getInt( "revision_ord" ) );
      MxAssert.assertNull( lResult.getString( "latest_revision_ord" ) );
      MxAssert.assertEquals( true, lResult.getBoolean( "in_revision_bool" ) );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), ReqsTest.class,
            ReqsData.getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      String lNullValue = null;

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( MAINT_PRGM_KEY, "aMaintPrgmDbId", "aMaintPrgmId" );
      lArgs.add( "aApplicable", false );
      lArgs.add( "aGroupCode", lNullValue );
      lArgs.add( "aBomItem", lNullValue );
      lArgs.add( "aNewerRev", false );

      lArgs.addWhereIn( "ref_task_class.class_mode_cd",
            new String[] { RefTaskClassKey.REQ_CLASS_MODE_CD, RefTaskClassKey.REF_CLASS_MODE_CD } );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
