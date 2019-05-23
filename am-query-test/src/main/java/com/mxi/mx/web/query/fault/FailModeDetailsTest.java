package com.mxi.mx.web.query.fault;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Tests the query com.mxi.mx.query.fault.FailModeDetails
 */

public final class FailModeDetailsTest {

   private static final String FAIL_MODE_CD_1 = "[44185]";
   private static final String FAIL_MODE_CD_2 = "[44186]";
   private static final String FAIL_MODE_NAME_1 =
         "Loss of the FIRE TEST (LOOP B) Discrete Input from the Engine 2 FDU";
   private static final String FAIL_MODE_NAME_2 =
         "Loss of the INOP LOOP A Discrete from the Engine 1 FDU";
   private static final String MINOR = "MINOR";
   private static final String MEL = "MEL";

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), FailModeDetailsTest.class,
            "FailModeDetailsTest.xml" );
   }


   /**
    * Tests the query with a task which has ETOPS Significant checked
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void excuteQuery_taskIsETOPS() throws Exception {

      DataSet lDs = executeQuery( 5000000, 1 );

      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      lDs.next();
      assertEquals( "fail_mode_cd", FAIL_MODE_CD_1, lDs.getString( "fail_mode_cd" ) );
      assertEquals( "fail_mode_name", FAIL_MODE_NAME_1, lDs.getString( "fail_mode_name" ) );
      assertEquals( "fail_mode_sdesc", FAIL_MODE_CD_1 + " (" + FAIL_MODE_NAME_1 + ")",
            lDs.getString( "fail_mode_sdesc" ) );
      assertEquals( "fail_defer_cd", "ACODE", lDs.getString( "fail_defer_cd" ) );
      assertEquals( "defer_ref_sdesc", "some defer info A", lDs.getString( "defer_ref_sdesc" ) );
      assertEquals( "op_restriction_ldesc", "some restriction A",
            lDs.getString( "op_restriction_ldesc" ) );
      assertEquals( "sev_type_cd", MINOR, lDs.getString( "sev_type_cd" ) );
      assertEquals( "fail_sev_cd", MINOR, lDs.getString( "fail_sev_cd" ) );
      assertEquals( "fail_type_cd", "A", lDs.getString( "fail_type_cd" ) );
      assertEquals( "fail_mode_ldesc", "something related to this fail mode A",
            lDs.getString( "fail_mode_ldesc" ) );
      assertEquals( "etops", true, lDs.getBoolean( "etops" ) );
   }


   /**
    * Tests the query with a task which has ETOPS Significant unchecked
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void excuteQuery_taskIsNotETOPS() throws Exception {

      DataSet lDs = executeQuery( 5000000, 2 );

      assertEquals( "Number of retrieved rows", 1, lDs.getRowCount() );
      lDs.next();
      assertEquals( "fail_mode_cd", FAIL_MODE_CD_2, lDs.getString( "fail_mode_cd" ) );
      assertEquals( "fail_mode_name", FAIL_MODE_NAME_2, lDs.getString( "fail_mode_name" ) );
      assertEquals( "fail_mode_sdesc", FAIL_MODE_CD_2 + " (" + FAIL_MODE_NAME_2 + ")",
            lDs.getString( "fail_mode_sdesc" ) );
      assertEquals( "fail_defer_cd", "BCODE", lDs.getString( "fail_defer_cd" ) );
      assertEquals( "defer_ref_sdesc", "some defer info B", lDs.getString( "defer_ref_sdesc" ) );
      assertEquals( "op_restriction_ldesc", "some restriction B",
            lDs.getString( "op_restriction_ldesc" ) );
      assertEquals( "sev_type_cd", MEL, lDs.getString( "sev_type_cd" ) );
      assertEquals( "fail_sev_cd", MEL, lDs.getString( "fail_sev_cd" ) );
      assertEquals( "fail_type_cd", "B", lDs.getString( "fail_type_cd" ) );
      assertEquals( "fail_mode_ldesc", "something related to this fail mode B",
            lDs.getString( "fail_mode_ldesc" ) );
      assertEquals( "etops", false, lDs.getBoolean( "etops" ) );
   }


   private DataSet executeQuery( int aFailModeDbId, int aFailModeId ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aFailModeDbId", aFailModeDbId );
      lArgs.add( "aFailModeId", aFailModeId );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
