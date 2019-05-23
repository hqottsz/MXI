
package com.mxi.mx.web.query.user;

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
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AssignedRolesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), AssignedRolesTest.class );
   }


   /** DOCUMENT ME! */
   public static final int ROLE_ID_1 = 10;

   /** DOCUMENT ME! */
   public static final String ROLE_CD_1 = "Role1";

   /** DOCUMENT ME! */
   public static final String ROLE_NAME_1 = "RoleName1";

   /** DOCUMENT ME! */
   public static final int ROLE_ID_2 = 20;

   /** DOCUMENT ME! */
   public static final String ROLE_CD_2 = "Role2";

   /** DOCUMENT ME! */
   public static final String ROLE_NAME_2 = "RoleName2";

   /** DOCUMENT ME! */
   public static final int ROLE_ID_3 = 30;

   /** DOCUMENT ME! */
   public static final String ROLE_CD_3 = "Role3";

   /** DOCUMENT ME! */
   public static final String ROLE_NAME_3 = "RoleName3";

   /** DOCUMENT ME! */
   public static final int USER_ID_1 = 110;

   /** DOCUMENT ME! */
   public static final int USER_ID_2 = 120;

   /** DOCUMENT ME! */
   public static final int ROLE_ORDER_1 = 1;

   /** DOCUMENT ME! */
   public static final int ROLE_ORDER_2 = 2;

   /** DOCUMENT ME! */
   public static final int ROLE_ORDER_3 = 3;

   /** DOCUMENT ME! */
   public static final int UTL_ID = 1000;


   /**
    * Tests that the query returns the proper data when there are two users, each assigned to two
    * roles, with one role overlapping.
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testAssignedRolesTwoUsers() throws Exception {

      //
      // Test User #1.
      //

      // Execute the query to retrieve data for user #1
      DataSet lDs = execute( new UserKey( USER_ID_1 ) );

      // Ensure two rows were returned
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // Ensure the first row's data is correct
      MxAssert.assertEquals( "ROLE_ID", ROLE_ID_1, lDs.getIntAt( 1, "ROLE_ID" ) );
      MxAssert.assertEquals( "ROLE_NAME", ROLE_NAME_1, lDs.getStringAt( 1, "ROLE_NAME" ) );
      MxAssert.assertEquals( "ROLE_ORDER", ROLE_ORDER_1, lDs.getIntAt( 1, "ROLE_ORDER" ) );

      // Ensure the second row's data is correct
      MxAssert.assertEquals( "ROLE_ID", ROLE_ID_2, lDs.getIntAt( 2, "ROLE_ID" ) );
      MxAssert.assertEquals( "ROLE_NAME", ROLE_NAME_2, lDs.getStringAt( 2, "ROLE_NAME" ) );
      MxAssert.assertEquals( "ROLE_ORDER", ROLE_ORDER_2, lDs.getIntAt( 2, "ROLE_ORDER" ) );

      //
      // Test User #2.
      //

      // Execute the query to retrieve data for user #1
      lDs = execute( new UserKey( USER_ID_2 ) );

      // Ensure two rows were returned
      MxAssert.assertEquals( "Number of retrieved rows", 2, lDs.getRowCount() );

      // Ensure the first row's data is correct
      MxAssert.assertEquals( "ROLE_ID", ROLE_ID_2, lDs.getIntAt( 1, "ROLE_ID" ) );
      MxAssert.assertEquals( "ROLE_NAME", ROLE_NAME_2, lDs.getStringAt( 1, "ROLE_NAME" ) );
      MxAssert.assertEquals( "ROLE_ORDER", ROLE_ORDER_2, lDs.getIntAt( 1, "ROLE_ORDER" ) );

      // Ensure the second row's data is correct
      MxAssert.assertEquals( "ROLE_ID", ROLE_ID_3, lDs.getIntAt( 2, "ROLE_ID" ) );
      MxAssert.assertEquals( "ROLE_NAME", ROLE_NAME_3, lDs.getStringAt( 2, "ROLE_NAME" ) );
      MxAssert.assertEquals( "ROLE_ORDER", ROLE_ORDER_3, lDs.getIntAt( 2, "ROLE_ORDER" ) );
   }


   /**
    * Execute the query.
    *
    * @param aUser
    *           user key.
    *
    * @return dataSet result.
    */
   private DataSet execute( UserKey aUser ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", aUser.getId() );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
