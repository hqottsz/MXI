package com.mxi.mx.web.unittest.expression;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.web.expression.TagETA;


/**
 * This class tests TagETA.
 *
 * @author Libin Cai
 * @created October 13, 2017
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class TagETATest {

   private static final Date DATE_2009 = new GregorianCalendar( 2009, 1, 10 ).getTime();
   private static final Date DATE_2010 = new GregorianCalendar( 2010, 1, 10 ).getTime();
   private static final Date DATE_2011 = new GregorianCalendar( 2011, 1, 10 ).getTime();

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();


   /**
    * Test highlight when ETA Date is after needed by date.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHighlightWhenETADateIsAfterNeededByDate() throws Exception {

      Date lNeededByDate = DATE_2010;
      Date lETADate = DATE_2011;

      String lResult = TagETA.getContent( lNeededByDate, lETADate, null );

      Assert.assertTrue( lResult.contains( "color:red;" ) );
   }


   /**
    * Test highlight when Delivery ETA date is after needed by date.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testHighlightWhenDeliveryETADateIsAfterNeededByDate() throws Exception {

      Date lNeededByDate = DATE_2010;
      Date lETADate = DATE_2009;
      Date lDeliveryETADate = DATE_2011;

      String lResult = TagETA.getContent( lNeededByDate, lETADate, lDeliveryETADate );

      Assert.assertTrue( lResult.contains( "color:red;" ) );
   }


   /**
    * Test date format when both ETA Date and Delivery ETA date exist.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDateFormatWhenBothETADateAndDeliveryETADateExist() throws Exception {

      Date lETADate = DATE_2009;
      Date lDeliveryETADate = DATE_2011;

      String lResult = TagETA.getContent( null, lETADate, lDeliveryETADate );

      Assert.assertTrue( lResult.contains( "10-FEB-2011 00:00 EST (10-FEB-2009 00:00 EST)" ) );
   }


   /**
    * Test date format when only ETA Date exists.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDateFormatWhenOnlyETADateExists() throws Exception {

      Date lETADate = DATE_2009;

      String lResult = TagETA.getContent( null, lETADate, null );

      Assert.assertEquals( "(10-FEB-2009 00:00 EST)", lResult );
   }


   /**
    * Test date format when only Delivery ETA date exist.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testDateFormatWhenOnlyDeliveryETADateExists() throws Exception {

      Date lDeliveryETADate = DATE_2011;

      String lResult = TagETA.getContent( null, null, lDeliveryETADate );

      Assert.assertEquals( "10-FEB-2011 00:00 EST", lResult );
   }

}
