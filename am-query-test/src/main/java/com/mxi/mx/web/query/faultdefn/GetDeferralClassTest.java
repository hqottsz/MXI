
package com.mxi.mx.web.query.faultdefn;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;


public class GetDeferralClassTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   private static final AssemblyKey ASSEMBLY_KEY_1 = new AssemblyKey( "4650:good" );
   private static final AssemblyKey ASSEMBLY_KEY_2 = new AssemblyKey( "4650:invalid" );
   private static final RefFailureSeverityKey FAULT_SEVERITY_KEY_1 =
         new RefFailureSeverityKey( "0:MEL" );
   private static final RefFailureSeverityKey FAULT_SEVERITY_KEY_2 =
         new RefFailureSeverityKey( "0:CDL" );
   private static final String DEFERRAL_REFERENCE_DESC_LEGACY = "legacy";
   private static final String DEFERRAL_REFERENCE_DESC_NEW = "new";


   @BeforeClass
   public static void loadData() {
      SqlLoader.load( sDatabaseConnectionRule.getConnection(), GetDeferralClassTest.class,
            "GetDeferralClass.sql" );
   }


   @Test
   public void execute_moreThanOneResult() {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.assmbl_db_id", "fail_defer_ref.assmbl_cd" },
               ASSEMBLY_KEY_1 );

         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.fail_sev_db_id", "fail_defer_ref.fail_sev_cd" },
               FAULT_SEVERITY_KEY_1 );

         lArgs.addWhereEquals( "LOWER(fail_defer_ref.defer_ref_sdesc)",
               DEFERRAL_REFERENCE_DESC_LEGACY );
      }

      DataSet lDataSet = executeQuery( lArgs );

      assertEquals( 2, lDataSet.getRowCount() );

      lDataSet.next();
      assertEquals( "0:MEL A", lDataSet.getString( "deferral_class" ) );
      assertEquals( "MEL A", lDataSet.getString( "fail_defer_cd" ) );
      assertEquals( "legacy", lDataSet.getString( "deferral_type" ) );

      lDataSet.next();
      assertEquals( "0:MEL A", lDataSet.getString( "deferral_class" ) );
      assertEquals( "MEL A", lDataSet.getString( "fail_defer_cd" ) );
      assertEquals( "legacy", lDataSet.getString( "deferral_type" ) );
   }


   @Test
   public void execute_singleResult() {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.assmbl_db_id", "fail_defer_ref.assmbl_cd" },
               ASSEMBLY_KEY_1 );

         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.fail_sev_db_id", "fail_defer_ref.fail_sev_cd" },
               FAULT_SEVERITY_KEY_1 );

         lArgs.addWhereEquals( "LOWER(fail_defer_ref.defer_ref_sdesc)",
               DEFERRAL_REFERENCE_DESC_NEW );
      }

      DataSet lDataSet = executeQuery( lArgs );
      lDataSet.next();

      assertEquals( 1, lDataSet.getRowCount() );
      assertEquals( "0:MEL A", lDataSet.getString( "deferral_class" ) );
      assertEquals( "MEL A", lDataSet.getString( "fail_defer_cd" ) );
      assertEquals( "new", lDataSet.getString( "deferral_type" ) );
   }


   @Test
   public void execute_noResult() {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.assmbl_db_id", "fail_defer_ref.assmbl_cd" },
               ASSEMBLY_KEY_2 );

         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.fail_sev_db_id", "fail_defer_ref.fail_sev_cd" },
               FAULT_SEVERITY_KEY_1 );

         lArgs.addWhereEquals( "LOWER(fail_defer_ref.defer_ref_sdesc)",
               DEFERRAL_REFERENCE_DESC_LEGACY );
      }

      DataSet lDataSet = executeQuery( lArgs );

      assertEquals( 0, lDataSet.getRowCount() );
   }


   @Test
   public void execute_noResult_differentSeverity() {
      DataSetArgument lArgs = new DataSetArgument();
      {
         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.assmbl_db_id", "fail_defer_ref.assmbl_cd" },
               ASSEMBLY_KEY_1 );

         lArgs.addWhereEquals(
               new String[] { "fail_defer_ref.fail_sev_db_id", "fail_defer_ref.fail_sev_cd" },
               FAULT_SEVERITY_KEY_2 );

         lArgs.addWhereEquals( "LOWER(fail_defer_ref.defer_ref_sdesc)",
               DEFERRAL_REFERENCE_DESC_LEGACY );
      }

      DataSet lDataSet = executeQuery( lArgs );

      assertEquals( 0, lDataSet.getRowCount() );
   }


   private DataSet executeQuery( DataSetArgument aDataSetArgument ) {
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), aDataSetArgument );
   }
}
