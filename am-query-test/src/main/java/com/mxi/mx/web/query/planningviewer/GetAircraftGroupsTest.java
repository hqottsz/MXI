package com.mxi.mx.web.query.planningviewer;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;


/**
 * Test the GetAircraftGroups query used by the planning viewer
 *
 */
public class GetAircraftGroupsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftGroupsTest.class );
   }


   /**
    * Test Case: Get all aircraft groups
    */
   @Test
   public void testGetGroup() {

      // ARRANGE
      DataSetArgument lArgs = new DataSetArgument();

      // ACT
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertEquals( "Number of retrieved rows", 2, lResult.getRowCount() );
      lResult.next();
      assertEquals( "Group name", "A320", lResult.getString( "name" ) );
      lResult.next();
      assertEquals( "Group name", "B737", lResult.getString( "name" ) );
   }
}
