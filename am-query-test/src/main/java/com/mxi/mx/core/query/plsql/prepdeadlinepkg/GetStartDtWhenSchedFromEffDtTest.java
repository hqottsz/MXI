
package com.mxi.mx.core.query.plsql.prepdeadlinepkg;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;


/**
 * Test case for the plsql package function prep_deadline_pkg.GetStartDtWhenSchedFromEffDt
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetStartDtWhenSchedFromEffDtTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    *
    * Test Case 1: Calculate the deadline start date when scheduled from effective date and
    * manufacturer date is before effective date.
    *
    * Preconditions:
    *
    * None, there is no data setup needed
    *
    * Action:
    *
    * Call the GetStartDtWhenSchedFromEffDt method
    *
    * Expectation:
    *
    * The effective date is returned as start date.
    *
    * @throws Exception
    */
   @Test
   public void testManufacturerDateBeforeEffectiveDate() throws Exception {

      // prepare the input date
      Calendar lEffectiveDateCalendar = Calendar.getInstance();
      lEffectiveDateCalendar.set( 2016, 1, 1 );
      Date lEffectiveDateDate = new Date( lEffectiveDateCalendar.getTimeInMillis() );

      Calendar lManufacturerDateCalendar = Calendar.getInstance();
      lManufacturerDateCalendar.set( 2015, 12, 23 );
      Date lManufacturerDateDate = new Date( lManufacturerDateCalendar.getTimeInMillis() );

      // prepare the expected date
      Calendar lExpectedStartDateCalendar = Calendar.getInstance();
      lExpectedStartDateCalendar.set( 2016, 1, 1 );
      Date lExpectedStartDate = new Date( lExpectedStartDateCalendar.getTimeInMillis() );

      int lSchedDbId = 4650;
      int lSchedId = 398927;
      int lDataTypeDbId = 100;
      int lDataTypeId = 2;
      int lSchedFromLatest = 1;
      boolean lSyncWithBaseline = false;

      // prepare the arguments to the function call
      String lPLSQL =
            "BEGIN ? := prep_deadline_pkg.GetStartDtWhenSchedFromEffDt(?, ?, ?, ?, ?, ?, ?, ?); END;";

      CallableStatement lStatement = iDatabaseConnectionRule.getConnection().prepareCall( lPLSQL );
      lStatement.registerOutParameter( 1, Types.DATE );
      lStatement.setInt( 2, lSchedDbId );
      lStatement.setInt( 3, lSchedId );
      lStatement.setInt( 4, lDataTypeDbId );
      lStatement.setInt( 5, lDataTypeId );
      lStatement.setInt( 6, lSchedFromLatest );
      lStatement.setBoolean( 7, lSyncWithBaseline );
      lStatement.setDate( 8, lEffectiveDateDate );
      lStatement.setDate( 9, lManufacturerDateDate );

      // call the plsql function
      lStatement.execute();

      // get the actual date
      Date lActualStartDate = lStatement.getDate( 1 );

      Assert.assertEquals( lExpectedStartDate.toString(), lActualStartDate.toString() );

   }


   /**
    *
    * Test Case 2: Calculate the deadline start date when scheduled from effective date and
    * manufacturer date is after effective date.
    *
    * Preconditions:
    *
    * None, there is no data setup needed
    *
    * Action:
    *
    * Call the GetStartDtWhenSchedFromEffDt method
    *
    * Expectation:
    *
    * The manufacturer date is returned as start date.
    *
    * @throws Exception
    */
   @Test
   public void testManufacturerDateAfterEffectiveDate() throws Exception {

      // prepare the input date
      Calendar lEffectiveDateCalendar = Calendar.getInstance();
      lEffectiveDateCalendar.set( 2016, 1, 1 );
      Date lEffectiveDateDate = new Date( lEffectiveDateCalendar.getTimeInMillis() );

      Calendar lManufacturerDateCalendar = Calendar.getInstance();
      lManufacturerDateCalendar.set( 2016, 1, 4 );
      Date lManufacturerDateDate = new Date( lManufacturerDateCalendar.getTimeInMillis() );

      // prepare the expected date
      Calendar lExpectedStartDateCalendar = Calendar.getInstance();
      lExpectedStartDateCalendar.set( 2016, 1, 4 );
      Date lExpectedStartDate = new Date( lExpectedStartDateCalendar.getTimeInMillis() );

      int lSchedDbId = 4650;
      int lSchedId = 398927;
      int lDataTypeDbId = 100;
      int lDataTypeId = 2;
      int lSchedFromLatest = 1;
      boolean lSyncWithBaseline = false;

      // prepare the arguments to the function call
      String lPLSQL =
            "BEGIN ? := prep_deadline_pkg.GetStartDtWhenSchedFromEffDt(?, ?, ?, ?, ?, ?, ?, ?); END;";

      CallableStatement lStatement = iDatabaseConnectionRule.getConnection().prepareCall( lPLSQL );
      lStatement.registerOutParameter( 1, Types.DATE );
      lStatement.setInt( 2, lSchedDbId );
      lStatement.setInt( 3, lSchedId );
      lStatement.setInt( 4, lDataTypeDbId );
      lStatement.setInt( 5, lDataTypeId );
      lStatement.setInt( 6, lSchedFromLatest );
      lStatement.setBoolean( 7, lSyncWithBaseline );
      lStatement.setDate( 8, lEffectiveDateDate );
      lStatement.setDate( 9, lManufacturerDateDate );

      // call the plsql function
      lStatement.execute();

      // get the actual date
      Date lActualStartDate = lStatement.getDate( 1 );

      Assert.assertEquals( lExpectedStartDate.toString(), lActualStartDate.toString() );

   }

}
