
package com.mxi.mx.web.unittest.expression.tagextendeddeadline;

import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.web.expression.TagExtendedDeadline;
import com.mxi.mx.web.utils.SRT;


/**
 * This class tests the specified method
 *
 * @author $Author: mlennox $
 * @version $Revision: 1.2 $
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetContentTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule sFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();


   /**
    * Test the calendar deadlien case
    *
    * @throws Exception
    *            if an error occur
    */
   @Test
   public void testCalendarDomainType() throws Exception {

      String lResult = TagExtendedDeadline.getContent( SRT.DomainType.CALENDAR, null, null, 0, // precision_qt
            new GregorianCalendar( 2004, 1, 10 ).getTime(), null,
            new GregorianCalendar( 2004, 1, 10 ).getTime() );
      Assert.assertEquals( "<nobr>10-FEB-2004 00:00 EST</nobr>", lResult );
   }


   /**
    * Test teh calendar deadline with deviation case
    *
    * @throws Exception
    *            if an error occur
    */
   @Test
   public void testCalendarDomainWithDeviation() throws Exception {

      String lResult =
            TagExtendedDeadline.getContent( SRT.DomainType.CALENDAR, new Double( 10 ), "UNIT", 0, // precision_qt
                  new GregorianCalendar( 2004, 1, 10 ).getTime(), new Double( 5 ),
                  new GregorianCalendar( 2004, 1, 15 ).getTime() );
      Assert.assertEquals( "<nobr>10-FEB-2004 00:00 EST<br>(15-FEB-2004 00:00 EST)</nobr>",
            lResult );
   }


   /**
    * Test teh calendar deadline with null date case
    *
    * @throws Exception
    *            if an error occur
    */
   @Test
   public void testCalendarDomainWithNullDate() throws Exception {

      String lResult =
            TagExtendedDeadline.getContent( SRT.DomainType.CALENDAR, new Double( 10 ), "UNIT", 0, // precision_qt
                  null, new Double( 5 ), null );
      Assert.assertEquals( "<nobr>" + i18n.get( "web.msg.NOT_AVAILABLE" ) + "</nobr>", lResult );
   }


   /**
    * Test the usage deadline case
    *
    * @throws Exception
    *            if an error occur
    */
   @Test
   public void testUsageDomain() throws Exception {

      String lResult =
            TagExtendedDeadline.getContent( SRT.DomainType.USAGE, new Double( 10 ), "UNIT", 0, // precision_qt
                  new GregorianCalendar( 2004, 1, 10 ).getTime(), null,
                  new GregorianCalendar( 2004, 1, 10 ).getTime() );
      Assert.assertEquals( "<nobr>10 UNIT<BR>10-FEB-2004 00:00 EST</nobr>", lResult );
   }


   /**
    * Test teh calendar deadline with deviation case
    *
    * @throws Exception
    *            if an error occur
    */
   @Test
   public void testUsageDomainWithDeviation() throws Exception {

      String lResult =
            TagExtendedDeadline.getContent( SRT.DomainType.USAGE, new Double( 10 ), "UNIT", 0, // precision_qt
                  new GregorianCalendar( 2004, 1, 10 ).getTime(), new Double( 5 ),
                  new GregorianCalendar( 2004, 1, 15 ).getTime() );
      Assert.assertEquals( "<nobr>10 UNIT (15 UNIT)<BR>15-FEB-2004 00:00 EST</nobr>", lResult );
   }
}
