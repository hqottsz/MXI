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
 * Test the GetAircraft query used by the planning viewer
 *
 */
public class GetAircraftTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetAircraftTest.class );
   }


   /**
    * Test Case: Get all aircrafts
    */
   @Test
   public void testGetAllAircraft() {

      // ARRANGE
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", 1 );

      // ACT
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertEquals( "Number of retrieved rows", 4, lResult.getRowCount() );
   }


   /**
    * Test Case: Get aircraft with specified assembly code and group name
    */
   @Test
   public void testGetAircraftWithAssemblyCodeAndGroupName() {

      // ARRANGE
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", 1 );
      lArgs.addWhereIn( "WHERE_CLAUSE_ASSEMBLY", "INV_INV.ASSMBL_CD", new String[] { "A320" } );
      lArgs.addWhereIn( "WHERE_CLAUSE_GROUP", "ACFT_GROUP.NAME", new String[] { "Plan-1" } );

      // ACT
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertEquals( "Number of retrieved rows", 2, lResult.getRowCount() );
   }

}
