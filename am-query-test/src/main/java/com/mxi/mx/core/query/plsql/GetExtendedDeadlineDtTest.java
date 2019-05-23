
package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * This test ensures that the PL/SQL function used to determine the date of an extension to a
 * deadline works correctly.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetExtendedDeadlineDtTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * This test runs a query that executed the pl/sql function with different deviations and has the
    * expected values built into the results. If all rows report pass, the test is successful.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetExtendedDeadlineDt() throws Exception {
      QuerySet lResults = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.core.query.plsql.GetExtendedDeadlineDtTest" );

      while ( lResults.next() ) {
         String lTestResult = lResults.getString( "result" );

         if ( !lTestResult.equals( "PASS" ) ) {
            fail( lTestResult );
         }
      }
   }

}
