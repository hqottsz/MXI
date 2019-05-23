
package com.mxi.mx.core.query.plsql.prepdeadlinepkg;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.holder.DateHolder;
import com.mxi.mx.common.holder.DoubleHolder;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.plsql.delegates.DeadlineProcedureDelegate;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * CDY 0 21 CWK 0 22 CMON 0 23 CYR 0 24 CLMON 0 25 CHR 0 31
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class FindCalendarDeadlineVariablesTest {

   private Date iFEB_1_2012;

   private Date iFEB_28_2011;

   private Date iFEB_28_2013;

   private Date iFEB_29_2012;

   private Date iJAN_1_2012;

   private Date iJAN_1_2012_ELEVEN;

   private Date iJAN_1_2012_NOON;

   private Date iJAN_1_2012_ONE_THIRTY;

   private Date iJAN_1_2013;

   private Date iJAN_2_2012;

   private Date iJAN_31_2012;

   private Date iJAN_8_2012;

   private Date iJUL_2_2012;

   private Date iMAR_31_2012;

   private DeadlineProcedureDelegate iProc;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 1.0 and a start date
    * of Jan 1, 2012 for CDY data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCDY() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CDY, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_2_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 1.5 and a start date
    * of Jan 1, 2012 at 11:59:59 for CDY data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCDYRounding() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CDY, new DateHolder( iJAN_1_2012_NOON ),
            new DoubleHolder( 1.5 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_2_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 1, 2012 at 11:59:59 is determined from an interval of 1.0 and a
    * start date of Jan 1, 2012 at 10:59:59 for CHR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCHR() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CHR, new DateHolder( iJAN_1_2012_ELEVEN ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_1_2012_NOON, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 1, 2012 at 13:29:59 is determined from an interval of 1.5 and a
    * start date of Jan 1, 2012 at 11:59:59 for CHR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCHRFractional() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CHR, new DateHolder( iJAN_1_2012_NOON ),
            new DoubleHolder( 1.5 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_1_2012_ONE_THIRTY, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Feb 29, 2012 is determined from an interval of 1.0 and a start date
    * of Jan 31, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCLMON() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, new DateHolder( iJAN_31_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iFEB_29_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Mar 31, 2012 is determined from an interval of 1.0 and a start date
    * of Feb 29, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCLMONLeapYear() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, new DateHolder( iFEB_29_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iMAR_31_2012, lEndDt.getValue() );
   }


   /**
    * Tests the start date is not moved to the end of the month when we use a start date of Jan 1,
    * 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCLMONNonLastDayDeadline() throws Exception {
      DateHolder lStartDt = new DateHolder( iJAN_1_2012 );
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, lStartDt, new DoubleHolder( 1.0 ),
            lEndDt );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );

      MxAssert.assertEquals( "End Date", iFEB_29_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Feb 1, 2012 is determined from an interval of 1.0 and a deadline
    * date of Jan 1, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCMON() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iFEB_1_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Mar 31, 2012 is determined from an interval of 1.0 and a deadline
    * date of Feb 29, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCMONLeapYear() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, new DateHolder( iFEB_29_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iMAR_31_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 0.033334 and a start
    * date of Jan 1, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCMONOneDayPrecision() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 0.033334 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_2_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 8, 2012 is determined from an interval of 1.0 and a start date
    * of Jan 1, 2012 for CWK data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCWK() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CWK, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_8_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 0.143 and a start date
    * of Jan 1, 2012 for CWK data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCWKOneDayPrecision() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CWK, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 0.143 ), lEndDt );

      MxAssert.assertEquals( "lEndDt Date", iJAN_2_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 1, 2013 is determined from an interval of 1.0 and a start date
    * of Jan 1, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCYR() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_1_2013, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Feb 28, 2013 is determined from an interval of 1.0 and a start date
    * of Feb 29, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCYRLeapYear() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iFEB_29_2012 ),
            new DoubleHolder( 1.0 ), lEndDt );

      MxAssert.assertEquals( "End Date", iFEB_28_2013, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 0.00274 and a start
    * date of Jan 1, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCYROneDayPrecision() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 0.00274 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJAN_2_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an end date of Jul 2, 2012 is determined from an interval of 0.50274 and a start
    * date of Jan 1, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullEndDateCYRSixMonthsPlusOneDayPrecision() throws Exception {
      DateHolder lEndDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iJAN_1_2012 ),
            new DoubleHolder( 0.50274 ), lEndDt );

      MxAssert.assertEquals( "End Date", iJUL_2_2012, lEndDt.getValue() );
   }


   /**
    * Tests that an interval of 1.0 is determined from an end date of Jan 2, 2012 and a start date
    * of Jan 1, 2012 for CDY data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCDY() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CDY, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJAN_2_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.00001 );
   }


   /**
    * Tests that an interval of 1.5 is determined from an end date of Jan 2, 2012 and a start date
    * of Jan 1, 2012 at 11:59:59 for CDY data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCDYRounding() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CDY, new DateHolder( iJAN_1_2012_NOON ),
            lInterval, new DateHolder( iJAN_2_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.00001 );
   }


   /**
    * Tests that an interval of 1.0 is determined from an end date of Jan 1, 2012 at 11:59:59 and a
    * start date of Jan 1, 2012 at 10:59:59 for CHR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCHR() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CHR, new DateHolder( iJAN_1_2012_ELEVEN ),
            lInterval, new DateHolder( iJAN_1_2012_NOON ) );

      Assert.assertEquals( "Interval", 1.0, lInterval.getValue().doubleValue(), 0.0001 );
   }


   /**
    * Tests that an interval of 1.5 is determined from an end date of Jan 1, 2012 at 13:29:59 and a
    * start date of Jan 1, 2012 at 11:59:59 for CHR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCHRFractional() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CHR, new DateHolder( iJAN_1_2012_NOON ),
            lInterval, new DateHolder( iJAN_1_2012_ONE_THIRTY ) );

      Assert.assertEquals( "Interval", new Double( 1.5 ), lInterval.getValue(), 0.0001 );
   }


   /**
    * Tests that an interval of 1.0 is determined from an end date of Feb 29, 2012 and a start date
    * of Jan 31, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCLMON() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, new DateHolder( iJAN_31_2012 ),
            lInterval, new DateHolder( iFEB_29_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.0003 );
   }


   /**
    * Tests that an interval of 1.0 is determined from an end date of Mar 31, 2012 and a start date
    * of Feb 29, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCLMONLeapYear() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, new DateHolder( iFEB_29_2012 ),
            lInterval, new DateHolder( iMAR_31_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.0003 );
   }


   /**
    * Tests the start date is not moved to the end of the month when we use a start date of Jan 1,
    * 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCLMONNonLastDayDeadline() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();
      DateHolder lStartDt = new DateHolder( iJAN_1_2012 );
      DateHolder lEndDt = new DateHolder( iFEB_1_2012 );

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, lStartDt, lInterval, lEndDt );

      MxAssert.assertEquals( "Start Date", iJAN_31_2012, lStartDt.getValue() );
      MxAssert.assertEquals( "End Date", iFEB_29_2012, lEndDt.getValue() );
      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.0003 );
   }


   /**
    * Tests that an end date of Feb 1, 2012 is determined from an interval of 1.0 and a deadline
    * date of Jan 1, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCMON() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iFEB_1_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.0003 );
   }


   /**
    * Tests that an end date of Mar 31, 2012 is determined from an interval of 1.0 and a deadline
    * date of Feb 29, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCMONLeapYear() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, new DateHolder( iFEB_29_2012 ),
            lInterval, new DateHolder( iMAR_31_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(), 0.0003 );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 0.033334 and a start
    * date of Jan 1, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCMONOneDayPrecision() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJAN_2_2012 ) );

      Assert.assertEquals( "Interval", 1.0 / 31.0, lInterval.getValue(), 0.0003 );
   }


   /**
    * Tests that an end date of Jan 8, 2012 is determined from an interval of 1.0 and a start date
    * of Jan 1, 2012 for CWK data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCWK() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CWK, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJAN_8_2012 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(),
            0.0014285714285714 );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 0.143 and a start date
    * of Jan 1, 2012 for CWK data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCWKOneDayPrecision() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CWK, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJAN_2_2012 ) );

      Assert.assertEquals( "Interval", new Double( 0.143 ), lInterval.getValue(),
            0.0014285714285714 );
   }


   /**
    * Tests that an end date of Jan 1, 2013 is determined from an interval of 1.0 and a start date
    * of Jan 1, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCYR() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJAN_1_2013 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(),
            2.73972602739726e-5 );
   }


   /**
    * Tests that an end date of Feb 28, 2013 is determined from an interval of 1.0 and a start date
    * of Feb 29, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCYRLeapYear() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iFEB_29_2012 ),
            lInterval, new DateHolder( iFEB_28_2013 ) );

      Assert.assertEquals( "Interval", new Double( 1.0 ), lInterval.getValue(),
            2.73972602739726e-5 );
   }


   /**
    * Tests that an end date of Jan 2, 2012 is determined from an interval of 0.00274 and a start
    * date of Jan 1, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCYROneDayPrecision() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJAN_2_2012 ) );

      Assert.assertEquals( "Interval", new Double( 0.00274 ), lInterval.getValue(), 0.001369863 );
   }


   /**
    * Tests that an end date of Jul 2, 2012 is determined from an interval of 0.50274 and a start
    * date of Jan 1, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullIntervalCYRSixMonthsPlusOneDayPrecision() throws Exception {
      DoubleHolder lInterval = new DoubleHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, new DateHolder( iJAN_1_2012 ),
            lInterval, new DateHolder( iJUL_2_2012 ) );

      Assert.assertEquals( "Interval", new Double( 0.50274 ), lInterval.getValue(), 0.001369863 );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 1.0 and a deadline
    * date of Jan 2, 2012 for CDY data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCDY() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CDY, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iJAN_2_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 at 11:59:59 is determined from an interval of 1.5 and a
    * deadline date of Jan 2, 2012 for CDY data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCDYRounding() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CDY, lStartDt, new DoubleHolder( 1.5 ),
            new DateHolder( iJAN_2_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012_NOON, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 at 10:59:59 is determined from an interval of 1.0 and a
    * deadline date of Jan 1, 2012 at 11:59:59 for CHR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCHR() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CHR, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iJAN_1_2012_NOON ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012_ELEVEN, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 at 11:59:59 is determined from an interval of 1.5 and a
    * deadline date of Jan 1, 2012 at 13:29:59 for CHR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCHRFractional() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CHR, lStartDt, new DoubleHolder( 1.5 ),
            new DateHolder( iJAN_1_2012_ONE_THIRTY ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012_NOON, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 31, 2012 is determined from an interval of 1.0 and a deadline
    * date of Feb 29, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCLMON() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iFEB_29_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_31_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Feb 29, 2012 is determined from an interval of 1.0 and a deadline
    * date of Mar 31, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCLMONLeapYear() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iMAR_31_2012 ) );

      MxAssert.assertEquals( "Start Date", iFEB_29_2012, lStartDt.getValue() );
   }


   /**
    * Tests the date is automatically moved to the end of the month when we use a deadline date of
    * Feb 1, 2012 for CLMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCLMONNonLastDayDeadline() throws Exception {
      DateHolder lStartDt = new DateHolder();
      DateHolder lEndDt = new DateHolder( iFEB_1_2012 );

      iProc.findCalendarDeadlineVariables( DataTypeKey.CLMON, lStartDt, new DoubleHolder( 1.0 ),
            lEndDt );

      MxAssert.assertEquals( "Start Date", iJAN_31_2012, lStartDt.getValue() );

      MxAssert.assertEquals( "End Date", iFEB_29_2012, lEndDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 1.0 and a deadline
    * date of Feb 1, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCMON() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iFEB_1_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Feb 29, 2012 is determined from an interval of 1.0 and a deadline
    * date of Mar 31, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCMONLeapYear() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iMAR_31_2012 ) );

      MxAssert.assertEquals( "Start Date", iFEB_29_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 0.033333 and a
    * deadline date of Jan 2, 2012 for CMON data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCMONOneDayPrecision() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CMON, lStartDt, new DoubleHolder( 0.033333 ),
            new DateHolder( iJAN_2_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 1.0 and a deadline
    * date of Jan 8, 2012 for CWK data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCWK() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CWK, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iJAN_8_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 0.142857 and a
    * deadline date of Jan 2, 2012 for CWK data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCWKOneDayPrecision() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CWK, lStartDt, new DoubleHolder( 0.142857 ),
            new DateHolder( iJAN_2_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 1.0 and a deadline
    * date of Jan 1, 2013 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCYR() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iJAN_1_2013 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Feb 28, 2011 is determined from an interval of 1.0 and a deadline
    * date of Feb 29, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCYRLeapYear() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, lStartDt, new DoubleHolder( 1.0 ),
            new DateHolder( iFEB_29_2012 ) );

      MxAssert.assertEquals( "Start Date", iFEB_28_2011, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 0.002739 and a
    * deadline date of Jan 2, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCYROneDayPrecision() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, lStartDt, new DoubleHolder( 0.00274 ),
            new DateHolder( iJAN_2_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * Tests that a start date of Jan 1, 2012 is determined from an interval of 0.502739 and a
    * deadline date of Jul 2, 2012 for CYR data type.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testNullStartDateCYRSixMonthsPlusOneDayPrecision() throws Exception {
      DateHolder lStartDt = new DateHolder();

      iProc.findCalendarDeadlineVariables( DataTypeKey.CYR, lStartDt, new DoubleHolder( 0.50274 ),
            new DateHolder( iJUL_2_2012 ) );

      MxAssert.assertEquals( "Start Date", iJAN_1_2012, lStartDt.getValue() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      iProc = new DeadlineProcedureDelegate();

      Calendar lCalendar = Calendar.getInstance();

      lCalendar.set( 2011, 1, 28, 23, 59, 59 );
      iFEB_28_2011 = lCalendar.getTime();

      lCalendar.set( 2012, 0, 1, 10, 59, 59 );
      iJAN_1_2012_ELEVEN = lCalendar.getTime();

      lCalendar.set( 2012, 0, 1, 11, 59, 59 );
      iJAN_1_2012_NOON = lCalendar.getTime();

      lCalendar.set( 2012, 0, 1, 13, 29, 59 );
      iJAN_1_2012_ONE_THIRTY = lCalendar.getTime();

      lCalendar.set( 2012, 0, 1, 23, 59, 59 );
      iJAN_1_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 0, 2, 23, 59, 59 );
      iJAN_2_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 0, 8, 23, 59, 59 );
      iJAN_8_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 0, 31, 23, 59, 59 );
      iJAN_31_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 1, 1, 23, 59, 59 );
      iFEB_1_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 1, 29, 23, 59, 59 );
      iFEB_29_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 2, 31, 23, 59, 59 );
      iMAR_31_2012 = lCalendar.getTime();

      lCalendar.set( 2012, 6, 2, 23, 59, 59 );
      iJUL_2_2012 = lCalendar.getTime();

      lCalendar.set( 2013, 0, 1, 23, 59, 59 );
      iJAN_1_2013 = lCalendar.getTime();

      lCalendar.set( 2013, 1, 28, 23, 59, 59 );
      iFEB_28_2013 = lCalendar.getTime();
   }
}
