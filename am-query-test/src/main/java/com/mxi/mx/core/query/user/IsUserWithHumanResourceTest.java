
package com.mxi.mx.core.query.user;

import static org.junit.Assert.assertEquals;

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


/**
 * Tests IsUserWithHumanResource.qrx
 *
 * @author okamenskova
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class IsUserWithHumanResourceTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            IsUserWithHumanResourceTest.class );
   }


   private static final int I_USER_WITH_HR_KEY = 1;
   private static final int I_USER_NO_HR_KEY = 2;


   /**
    * Tests the retrieval of HR shifts.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testIsUserWithHumanResource() throws Exception {

      DataSet lDataSet = this.execute( I_USER_WITH_HR_KEY );
      assertEquals( 1, lDataSet.getRowCount() );

      lDataSet = this.execute( I_USER_NO_HR_KEY );
      assertEquals( 0, lDataSet.getRowCount() );
   }


   /**
    * This method executes the query in IsUserWithHumanResource.qrx
    *
    * @param aUserId
    *           the user id
    *
    * @return The dataset after execution.
    */
   private DataSet execute( int aUserId ) {
      DataSetArgument lDataSetArgument = new DataSetArgument();

      lDataSetArgument.add( "aUserId", aUserId );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );
   }
}
