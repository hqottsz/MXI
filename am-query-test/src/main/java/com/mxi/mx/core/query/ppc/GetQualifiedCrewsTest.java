
package com.mxi.mx.core.query.ppc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

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
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetQualifiedCrewsTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetQualifiedCrewsTest.class );
   }


   /**
    * Tests that the query returns tasks and qualified crews
    *
    * @throws Exception
    *            unknown exception is thrown.
    */
   @Test
   public void testQuery() throws Exception {
      DataSet lResult = execute();

      // Asserts that 3 rows are returned for Task 1, Task 2 (from work package 1) and Task 6 (from
      // work package 2)
      assertEquals( 3, lResult.getRowCount() );

      lResult.first();

      // Task 1 (Work Package 1) should have crew will ALL skills (Crew 2). Crew with LBR skills
      // (Crew 4) should not be returned here because the task has multiple labour skills
      assertEquals(

            // UUID format for task_id = 10 from GetQualifiedCrewsTest.xml
            UUID.fromString( "6b31bdfa-7f9b-3ece-a633-81ffa91bd6a9" ),
            UUID.nameUUIDFromBytes( lResult.getBytes( "task_id" ) ) );

      assertEquals(

            // UUID format for crew_id = 101 from GetQualifiedCrewsTest.xml
            UUID.fromString( "249ba627-7758-3506-95e8-f5909bacd6d3" ),
            UUID.nameUUIDFromBytes( lResult.getBytes( "crew_id" ) ) );

      assertTrue( lResult.isNull( "crew_labour_skill_db_id" ) );
      assertTrue( lResult.isNull( "crew_labour_skill_cd" ) );

      lResult.next();

      // Task 2 (Work Package 1) should have crew with the matching skill LBR (Crew 4)
      assertEquals(
            // UUID format for task_id = 20 from GetQualifiedCrewsTest.xml
            UUID.fromString( "7215ee9c-7d9d-3229-9292-1a40e899ec5f" ),
            UUID.nameUUIDFromBytes( lResult.getBytes( "task_id" ) ) );

      assertEquals(

            // UUID format for crew_id = 103 from GetQualifiedCrewsTest.xml
            UUID.fromString( "706acf32-04a3-3094-942c-7bc49eba2aa8" ),
            UUID.nameUUIDFromBytes( lResult.getBytes( "crew_id" ) ) );

      assertEquals( 0, lResult.getInt( "crew_labour_skill_db_id" ) );
      assertEquals( "LBR", lResult.getString( "crew_labour_skill_cd" ) );

      // Note: Task 3 (Work Package 1) should not be returned because work area does not have any
      // crew with AVIONICS

      lResult.next();

      // Note: Task 4 (Work Package 2) should not be returned since it has 2 labour rows (AVIONICS
      // and ENG). However, the work area does not have any crew with multiple skills

      // Task 5 (Work Package 2) should have crew with matching skill AVIONICS (Crew 5). Since the
      // work area contains two crews (Crew 5 and Crew 6) with AVIONICS skill, only the first crew
      // (ie. Crew 5) should be returned
      assertEquals(
            // UUID format for task_id = 50 from GetQualifiedCrewsTest.xml
            UUID.fromString( "44c29edb-103a-3872-b519-ad0c9a0fdaaa" ),
            UUID.nameUUIDFromBytes( lResult.getBytes( "task_id" ) ) );

      assertEquals(
            // UUID format for crew_id = 104 from xml from GetQualifiedCrewsTest.xml
            UUID.fromString( "09960d18-c947-355e-8b79-7d2c266e0825" ),
            UUID.nameUUIDFromBytes( lResult.getBytes( "crew_id" ) ) );

      assertEquals( 10, lResult.getInt( "crew_labour_skill_db_id" ) );
      assertEquals( "AVIONICS", lResult.getString( "crew_labour_skill_cd" ) );
   }


   /**
    * Execute the query.
    *
    * @return dataSet result.
    */
   private DataSet execute() {

      // generate a new UUID for plan id
      UUID lPlanId = UUID.fromString( "f8ff4655-9d5f-11e3-965c-a4badbecd042" );

      DataSetArgument lArgs = new DataSetArgument();

      // convert plan id to string since data in xml file is string
      lArgs.add( "aPlanId", UuidUtils.toHexString( lPlanId ) );

      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
