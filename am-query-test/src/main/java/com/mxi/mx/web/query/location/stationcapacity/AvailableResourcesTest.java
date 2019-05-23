
package com.mxi.mx.web.query.location.stationcapacity;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.ShiftPlanKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.web.query.location.stationcapacity.AvailableResources
 *
 * @author jcimino
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class AvailableResourcesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   /**
    * Scenario:<br>
    * 1. The location has a line capacity shift<br>
    * 2. Nobody is assigned the shift on the date<br>
    * <br>
    * Expected Outcome: Zero rows retrieved
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testNoResourcesAssignedToLineCapacityShift() throws Exception {

      // execute the query for the location on day 3
      DataSet lDs = execute( StationCapacityData.Location.YOW_LINE, StationCapacityData.DAY_DT_3 );

      // There should be 0 rows
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Scenario:<br>
    * 1. The location has a line capacity shift<br>
    * 2. One resource is assigned to the non-line capacity shift on the date<br>
    * <br>
    * Expected Outcome: Zero rows retrieved
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testResourceAssignedToNonLineCapacityShift() throws Exception {

      // execute the query for the location on day 2
      DataSet lDs = execute( StationCapacityData.Location.YOW_LINE, StationCapacityData.DAY_DT_2 );

      // There should be 0 rows
      MxAssert.assertEquals( "Number of retrieved rows", 0, lDs.getRowCount() );
   }


   /**
    * Scenario:<br>
    * 1. The location has a line capacity shift<br>
    * 2. There are 3 resources assigned to the shift on the date<br>
    * 3. Each resource has 2 skills<br>
    * <br>
    * Expected Outcome: Four rows retrieved
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testResourcesAssignedToLineCapacityShift() throws Exception {

      // execute the query for the location on day 1
      DataSet lDs = execute( StationCapacityData.Location.YOW_LINE, StationCapacityData.DAY_DT_1 );

      // There should be 6 rows, 2 for each user
      MxAssert.assertEquals( "Number of retrieved rows", 6, lDs.getRowCount() );

      // Row 1: User 1, secondary skill
      lDs.next();
      testRow( lDs, StationCapacityData.User.USER_1, StationCapacityData.User.USER_1_FIRST_NAME,
            StationCapacityData.User.USER_1_LAST_NAME, StationCapacityData.User.HR_1_SHIFT_PLAN_1,
            StationCapacityData.User.HR_1_LABOUR_SKILL_1,
            StationCapacityData.User.HR_1_SHIFT_PLAN_1_LABOUR_SKILL,
            StationCapacityData.Location.YOW_SHIFT_1_WORK_HOURS, false );

      // Row 2: User 1, primary skill
      lDs.next();
      testRow( lDs, StationCapacityData.User.USER_1, StationCapacityData.User.USER_1_FIRST_NAME,
            StationCapacityData.User.USER_1_LAST_NAME, StationCapacityData.User.HR_1_SHIFT_PLAN_1,
            StationCapacityData.User.HR_1_LABOUR_SKILL_2,
            StationCapacityData.User.HR_1_SHIFT_PLAN_1_LABOUR_SKILL,
            StationCapacityData.Location.YOW_SHIFT_1_WORK_HOURS, true );

      // Row 3: User 2, secondary skill
      lDs.next();
      testRow( lDs, StationCapacityData.User.USER_2, StationCapacityData.User.USER_2_FIRST_NAME,
            StationCapacityData.User.USER_2_LAST_NAME, StationCapacityData.User.HR_2_SHIFT_PLAN_1,
            new RefLabourSkillKey( "4650:RII-INSP" ),
            StationCapacityData.User.HR_2_SHIFT_PLAN_1_LABOUR_SKILL,
            StationCapacityData.Location.YOW_SHIFT_1_WORK_HOURS, false );

      // Row 4: User 2, primary skill
      lDs.next();
      testRow( lDs, StationCapacityData.User.USER_2, StationCapacityData.User.USER_2_FIRST_NAME,
            StationCapacityData.User.USER_2_LAST_NAME, StationCapacityData.User.HR_2_SHIFT_PLAN_1,
            StationCapacityData.User.HR_2_LABOUR_SKILL_1,
            StationCapacityData.User.HR_2_SHIFT_PLAN_1_LABOUR_SKILL,
            StationCapacityData.Location.YOW_SHIFT_1_WORK_HOURS, true );

      // Row 5: User 3, secondary skill
      lDs.next();
      testRow( lDs, StationCapacityData.User.USER_3, StationCapacityData.User.USER_3_FIRST_NAME,
            StationCapacityData.User.USER_3_LAST_NAME, StationCapacityData.User.HR_3_SHIFT_PLAN_1,
            StationCapacityData.User.HR_3_LABOUR_SKILL_1, new RefLabourSkillKey( "4650:RII-INSP" ),
            StationCapacityData.Location.YOW_SHIFT_1_WORK_HOURS, false );

      // Row 6: User 3, primary skill
      lDs.next();
      testRow( lDs, StationCapacityData.User.USER_3, StationCapacityData.User.USER_3_FIRST_NAME,
            StationCapacityData.User.USER_3_LAST_NAME, StationCapacityData.User.HR_3_SHIFT_PLAN_1,
            new RefLabourSkillKey( "4650:RII-INSP" ), new RefLabourSkillKey( "4650:RII-INSP" ),
            StationCapacityData.Location.YOW_SHIFT_1_WORK_HOURS, true );
   }


   /**
    * create the test data
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @BeforeClass
   public static void loadData() throws Exception {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), AvailableResourcesTest.class,
            new StationCapacityData().getDataFile() );
   }


   /**
    * Execute the query.
    *
    * @param aLocation
    *           location
    * @param aDate
    *           date
    *
    * @return the result
    */
   private DataSet execute( LocationKey aLocation, Date aDate ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( "aDate", aDate );

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           the dataset
    * @param aUser
    *           the expected user key
    * @param aFirstName
    *           the expected first name
    * @param aLastName
    *           the expected last name
    * @param aShiftPlan
    *           the expected shift plan key
    * @param aQualLabourSkill
    *           the expected labour skill
    * @param aShiftLabourSkill
    *           the expected shift labour skill
    * @param aWorkHours
    *           the expected work hours
    * @param aPrimary
    *           the expected primary skill boolean
    */
   private void testRow( DataSet aDs, UserKey aUser, String aFirstName, String aLastName,
         ShiftPlanKey aShiftPlan, RefLabourSkillKey aQualLabourSkill,
         RefLabourSkillKey aShiftLabourSkill, double aWorkHours, boolean aPrimary ) {
      MxAssert.assertEquals( "user_key", aUser.toString(), aDs.getString( "user_key" ) );
      MxAssert.assertEquals( "first_name", aFirstName, aDs.getString( "first_name" ) );
      MxAssert.assertEquals( "last_name", aLastName, aDs.getString( "last_name" ) );
      MxAssert.assertEquals( "hr_shift_plan_key", aShiftPlan.toString(),
            aDs.getString( "hr_shift_plan_key" ) );
      MxAssert.assertEquals( "qual_labour_skill_key", aQualLabourSkill.toString(),
            aDs.getString( "qual_labour_skill_key" ) );
      MxAssert.assertEquals( "shift_labour_skill_key", aShiftLabourSkill.toString(),
            aDs.getString( "shift_labour_skill_key" ) );
      MxAssert.assertEquals( "work_hours_qt", aWorkHours, aDs.getDouble( "work_hours_qt" ) );
      MxAssert.assertEquals( "labour_skill_cd", aQualLabourSkill.getCd(),
            aDs.getString( "labour_skill_cd" ) );
      MxAssert.assertEquals( "primary_skill_bool", aPrimary,
            aDs.getBoolean( "primary_skill_bool" ) );
   }
}
