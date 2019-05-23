package com.mxi.mx.core.maint.plan.baselineloader.plsql;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.mxi.mx.util.BaselineLoaderTest;
import com.mxi.mx.util.ScriptRunner;
import com.mxi.mx.util.TableUtil;
import com.mxi.mx.util.TestConstants;
import com.mxi.mx.util.ValidationAndImport;


/**
 * This test suite contains test cases on validation of populate_jic_repl script .
 *
 * @author ALICIA QIAN
 */
public class populateJICRepl extends BaselineLoaderTest {

   @Rule
   public TestName testName = new TestName();
   ValidationAndImport ivalidationandimport;
   public String iNOREQ = "REQ";


   /**
    * Clean up after each individual test
    *
    */
   @After
   @Override
   public void after() {
      try {
         clearBaselineLoaderTables();
      } catch ( SQLException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         Assert.assertTrue( "Clean up tables failed.", false );
      }
      super.after();
   }


   /**
    * Setup before executing each individual test
    *
    * @throws Exception
    */
   @Before
   @Override
   public void before() throws Exception {

      super.before();
      clearBaselineLoaderTables();

   }


   /**
    * This test is to verify OPER-15950: the populate_jic_repl.sql script does not populate the
    * REQ_ACTION_CDs resulting in validation errors c_jic_step. NOREQ is default value as action cd.
    *
    *
    */

   @Test
   public void testOPER15950_ACFT_NOREQ_REPL_SCRIPT() {

      System.out.println(
            "=======Starting:  " + testName.getMethodName() + " import========================" );

      // // clean up c_jic_part table
      // String lsql = "delete from c_jic_part";
      // executeSQL( lsql );

      // run script file
      String lfile = TestConstants.BUILD_RESOURCES_MAIN_LIB_PLSQL_BL + "\\populate_jic_repl.sql";
      ScriptRunner lrunner = new ScriptRunner( getConnection(), lfile );
      lrunner.runScript();

      // verify c_jic_part table has exported with default action cd as "ASREQ".
      int lRowCount = getCount( TableUtil.C_JIC_PART );
      String lquery = "select count(*) as \"COUNT\"  from " + TableUtil.C_JIC_PART
            + " where REQ_ACTION_CD='" + iNOREQ + "'";
      int lActionCd = getIntValueFromQuery( lquery, "COUNT" );

      Assert.assertTrue( "Check all the records' action cd should be ASREQ as default",
            lRowCount == lActionCd );

   }


   /**
    * This test is to verify OPER-15978: the populate_jic_repl.sql script produces duplicates in the
    * C_JIC_PART staging table
    *
    *
    */
   @Ignore
   @Test
   public void testOPER15978VALIDATION() {

      System.out.println( "=======Starting:  " + testName.getMethodName()
            + " Validation========================" );

      // run script file
      String lfile = TestConstants.BUILD_RESOURCES_MAIN_LIB_PLSQL_BL + "\\populate_jic_repl.sql";
      ScriptRunner lrunner = new ScriptRunner( getConnection(), lfile );
      lrunner.runScript();

      // run validation
      Assert.assertTrue( runValidationAndImport( true, true ) == 1 );

   }


   // ===============================================================================================
   /**
    * This function is to implement interface ValidationAndImport
    *
    * @param: blnOnlyValidation
    * @param: allornone
    *
    * @return: return code of Int
    *
    */
   public int runValidationAndImport( boolean ablnOnlyValidation, boolean allornone ) {
      int lrtValue = 0;
      ivalidationandimport = new ValidationAndImport() {

         @Override
         public int runValidation( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallJICPART;

            try {

               lPrepareCallJICPART = getConnection()
                     .prepareCall( "BEGIN  jic_import.validate_jic(on_retcode =>?); END;" );

               lPrepareCallJICPART.registerOutParameter( 1, Types.INTEGER );
               lPrepareCallJICPART.execute();
               commit();
               lReturn = lPrepareCallJICPART.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;

         }


         @Override
         public int runImport( boolean allornone ) {
            int lReturn = 0;
            CallableStatement lPrepareCallKIT;

            try {

               lPrepareCallKIT = getConnection()
                     .prepareCall( "BEGIN jic_import.import_jic(on_retcode =>?); END;" );

               lPrepareCallKIT.registerOutParameter( 1, Types.INTEGER );

               lPrepareCallKIT.execute();
               commit();
               lReturn = lPrepareCallKIT.getInt( 1 );
            } catch ( SQLException e ) {
               e.printStackTrace();
            }
            return lReturn;
         }

      };

      lrtValue = ablnOnlyValidation ? ivalidationandimport.runValidation( allornone )
            : ivalidationandimport.runImport( allornone );

      return lrtValue;
   }

}
