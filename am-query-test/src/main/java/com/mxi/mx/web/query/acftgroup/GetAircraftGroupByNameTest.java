
package com.mxi.mx.web.query.acftgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.services.acftgroup.DuplicateAircraftGroupNameException;


/**
 * This class runs a unit test on the GetAircraftGroupByName query file.
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAircraftGroupByNameTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftGroupByNameTest.class );
   }


   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Test Case : creating an aircraft group with an existing name
    *
    * @throws Exception
    */
   @Test
   public void testCreateExistingName() throws Exception {

      // SETUP
      String lName = "Plan-1";

      // ACT
      Integer lResult = getAircraftGroupId( lName );

      try {
         DuplicateAircraftGroupNameException.validate( lName, 2003, "CREATE", lResult );
         fail( "Expected DuplicateAircraftGroupNameException" );
      } catch ( DuplicateAircraftGroupNameException e ) {
         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.32421", e.getMessageKey() );
      }
   }


   /**
    * Test Case : creating an aircraft group with a non existing name
    *
    * @throws Exception
    */
   @Test
   public void testCreateNonExistingName() throws Exception {

      // SETUP
      String lName = "Plan-3";

      // ACT
      Integer lResult = getAircraftGroupId( lName );

      try {
         DuplicateAircraftGroupNameException.validate( lName, 2003, "CREATE", lResult );

      } catch ( DuplicateAircraftGroupNameException e ) {
         lResult = null;
      }
      // ASSERT
      assertNotNull( lResult );
   }


   /**
    * Test Case : editing an aircraft group to have an existing name
    *
    * @throws Exception
    */
   @Test
   public void testEditToExistingName() throws Exception {

      // SETUP
      String lName = "Plan-2";

      // ACT
      Integer lResult = getAircraftGroupId( lName );

      try {
         DuplicateAircraftGroupNameException.validate( lName, 2003, "CREATE", lResult );
         fail( "Expected DuplicateAircraftGroupNameException" );
      } catch ( DuplicateAircraftGroupNameException e ) {
         // Verify that the exception was thrown and contains the expected message.
         assertEquals( "core.err.32421", e.getMessageKey() );
      }
   }


   /**
    *
    * Used to get the Group Id given the group name
    *
    * @param aName
    *           The name of the aircraft group
    * @return Group Id
    */
   private Integer getAircraftGroupId( String aName ) {

      DataSetArgument lArgs = new DataSetArgument();
      Integer lGroupId = 0;
      lArgs.add( "aName", aName );
      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
      if ( lDs.next() ) {
         lGroupId = lDs.getInteger( "ID" );
      }
      return lGroupId;

   }
}
