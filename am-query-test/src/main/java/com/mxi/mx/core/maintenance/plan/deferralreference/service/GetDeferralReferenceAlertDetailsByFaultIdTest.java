
package com.mxi.mx.core.maintenance.plan.deferralreference.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.FormatUtil;


/**
 * Query Tests for GetDeferralReferenceAlertDetailsByFaultId.qrx
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetDeferralReferenceAlertDetailsByFaultIdTest {

   private enum COLUMN {
      FAIL_DEFER_REF_DB_ID, FAIL_DEFER_REF_ID, CORR_TASK_DB_ID, CORR_TASK_ID
   }


   // Test Data
   private static final UUID MEL_DEFERRED_FAULT =
         FormatUtil.stringToUuid( "ABCDEF1234567890ABCDEF1234567890" );
   private static final String DEFERRAL_REFERENCE_DB_ID = "4650";
   private static final String DEFERRAL_REFERENCE_ID = "50";
   private static final String CORR_TASK_DB_ID = "4650";
   private static final String CORR_TASK_ID = "100";
   private static final UUID NON_MEL_DEFERRED_FAULT =
         FormatUtil.stringToUuid( "1234567890ABCDEF1234567890ABCDEF" );

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetDeferralReferenceAlertDetailsByFaultIdTest.class );
   }


   @Test
   public void itRetrievesAlertDetailsForMELDeferredFault() {
      QuerySet lQs = executeQuery( MEL_DEFERRED_FAULT );

      assertTrue( lQs.first() );
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( 4, lQs.getColumnNames().length );

      assertEquals( lQs.getString( COLUMN.FAIL_DEFER_REF_DB_ID.name() ), DEFERRAL_REFERENCE_DB_ID );
      assertEquals( lQs.getString( COLUMN.FAIL_DEFER_REF_ID.name() ), DEFERRAL_REFERENCE_ID );
      assertEquals( lQs.getString( COLUMN.CORR_TASK_DB_ID.name() ), CORR_TASK_DB_ID );
      assertEquals( lQs.getString( COLUMN.CORR_TASK_ID.name() ), CORR_TASK_ID );
   }


   @Test
   public void itDoesNotRetrieveAlertDetailsForNonMELDeferredFault() {
      QuerySet lQs = executeQuery( NON_MEL_DEFERRED_FAULT );
      assertFalse( lQs.first() );
   }


   /**
    * Utility method to execute the query under test against the provided fault id.
    *
    * @param aFaultId
    * @return
    */
   private QuerySet executeQuery( UUID aFaultId ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aFaultId", aFaultId );

      return QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.maintenance.plan.deferralreference.service.GetDeferralReferenceAlertDetailsByFaultId",
            lArgs );
   }

}
