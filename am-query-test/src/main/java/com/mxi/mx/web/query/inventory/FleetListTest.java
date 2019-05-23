
package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Tests the query com.mxi.mx.web.query.inventory.FleetList
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class FleetListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), FleetListTest.class );
   }


   /**
    * Tests the proper amount of assemblies are returned when selecting one group and one assembly
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryIncludeOneAssemblyAndOneGroup() throws Exception {

      // Set up the data
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", "4650" );
      lArgs.add( "aHrId", "1" );
      lArgs.add( "aNoGroupsExist", "0" );
      lArgs.addWhereIn( "WHERE_CLAUSE_ASSEMBLIES", "INV_INV.ASSMBL_CD", new String[] { "A320" } );
      lArgs.addWhereIn( "WHERE_CLAUSE_GROUPS", "ACFT_GROUP_ASSIGNMENT.GROUP_ID",
            new String[] { "2001" } );

      // Execute Query
      QuerySet lFleetListDs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertNotNull( lFleetListDs );
      assertEquals( "Number of retrieved rows", 2, lFleetListDs.getRowCount() );

   }


   /**
    * Tests the proper amount of rows are returned when selecting one group and multiple assemblies
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryIncludeMultiAssemblyAndOneGroup() throws Exception {

      // Set up the data
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", "4650" );
      lArgs.add( "aHrId", "1" );
      lArgs.add( "aNoGroupsExist", "0" );
      lArgs.addWhereIn( "WHERE_CLAUSE_ASSEMBLIES", "INV_INV.ASSMBL_CD",
            new String[] { "A320", "747" } );
      lArgs.addWhereIn( "WHERE_CLAUSE_GROUPS", "ACFT_GROUP_ASSIGNMENT.GROUP_ID",
            new String[] { "2001" } );

      // Execute the query
      QuerySet lFleetListDs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertNotNull( lFleetListDs );
      assertEquals( "Number of retrieved rows", 3, lFleetListDs.getRowCount() );
   }


   /**
    * Tests the proper amount of rows are returned when selecting multiple groups and assemblies
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryIncludeMultiAssemblyAndMultiGroup() throws Exception {

      // Set up the data
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", "4650" );
      lArgs.add( "aHrId", "1" );
      lArgs.add( "aNoGroupsExist", "0" );
      lArgs.addWhereIn( "WHERE_CLAUSE_ASSEMBLIES", "INV_INV.ASSMBL_CD",
            new String[] { "A320", "747" } );
      lArgs.addWhereIn( "WHERE_CLAUSE_GROUPS", "ACFT_GROUP_ASSIGNMENT.GROUP_ID",
            new String[] { "2001", "2002" } );

      // Execute the query
      QuerySet lFleetListDs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertNotNull( lFleetListDs );
      assertEquals( "Number of retrieved rows", 3, lFleetListDs.getRowCount() );

   }


   /**
    * Tests the proper amount of rows are returned when selecting no groups
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryIncludeNoGroups() throws Exception {

      // Set up the data
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", "4650" );
      lArgs.add( "aHrId", "1" );
      lArgs.add( "aNoGroupsExist", "1" );
      lArgs.addWhereIn( "WHERE_CLAUSE_ASSEMBLIES", "INV_INV.ASSMBL_CD", new String[] { "A320" } );
      lArgs.addWhereIn( "WHERE_CLAUSE_GROUPS", "ACFT_GROUP_ASSIGNMENT.GROUP_ID",
            new String[] { "" } );

      // Execute the query
      QuerySet lFleetListDs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertNotNull( lFleetListDs );
      assertEquals( "Number of retrieved rows", 2, lFleetListDs.getRowCount() );

   }


   /**
    * Tests the expected data is returned when the fleet list query is executed
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testQueryReturnsExpectedData() throws Exception {

      // Set up the data
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aHrDbId", "4650" );
      lArgs.add( "aHrId", "1" );
      lArgs.add( "aNoGroupsExist", "0" );
      lArgs.addWhereIn( "WHERE_CLAUSE_ASSEMBLIES", "INV_INV.ASSMBL_CD",
            new String[] { "A320", "747" } );
      lArgs.addWhereIn( "WHERE_CLAUSE_GROUPS", "ACFT_GROUP_ASSIGNMENT.GROUP_ID",
            new String[] { "2001", "2002" } );

      // Execute query
      QuerySet lFleetListDs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );

      // ASSERT
      assertNotNull( lFleetListDs );
      lFleetListDs.next();

      // Testing to see if aircraft details data exists
      assertNotNull( lFleetListDs.getString( "aircraft_key" ) );
      assertNotNull( lFleetListDs.getString( "aircraft_capability_cd" ) );

      // Testing to see if aircraft location details data exist
      assertNotNull( lFleetListDs.getString( "aircraft_loc_key" ) );
      assertNotNull( lFleetListDs.getString( "aircraft_loc_cd" ) );

      // Testing to see if work package details data exist
      assertNotNull( lFleetListDs.getString( "wp_key" ) );
      assertNotNull( lFleetListDs.getString( "wp_user_subclass_cd" ) );

      // Testing to see if work package scheduling details data exist
      assertNotNull( lFleetListDs.getString( "wp_work_location_key" ) );
      assertNotNull( lFleetListDs.getString( "wp_sched_end_gdt" ) );

      // Testing to see if flight location details data exist
      assertNotNull( lFleetListDs.getString( "dep_loc_key" ) );
      assertNotNull( lFleetListDs.getString( "arr_loc_cd" ) );

   }

}
