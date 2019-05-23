package com.mxi.mx.core.services.part;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartCountBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocPartCountKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;
import com.mxi.mx.core.table.inv.InvLocPartCount;


/**
 * This test class ensures that the recalculateABCClass stored procedure functions as expected and
 * updates the next count date for the bin correctly.
 *
 * @author Brent Ludington
 * @created May 17, 2019
 */
public final class ABCClassRecalculationCountDataTest {

   private final int iFakeUserId = 9999;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( iFakeUserId, "testuser" );

   protected final static String ABQ_STORE_BIN = "ABQ/STORE/BIN";

   protected HumanResourceKey iHr;
   protected QuerySet iQuerySet;

   protected LocationKey iBinLoc;
   protected PartNoKey iPart;
   protected InvLocPartCountKey iPartCountKey;

   protected Calendar lastCountDate;
   protected Calendar nextCountDate;
   protected Calendar expectedDate;

   private static final String DATE_PATTERN = "dd/MM/yyyy";


   @Before
   public void setup() {
      iHr = Domain.createHumanResource();
      Domain.createHumanResource( aHr -> {
         aHr.setUser( new UserKey( iFakeUserId ) );
      } );

      lastCountDate = Calendar.getInstance();
      nextCountDate = Calendar.getInstance();
      expectedDate = Calendar.getInstance();
   }


   @Test
   public void
         test_GIVEN_BinLevelWithNoLastAndNextCounts_WHEN_AbcClassRecalcProcRuns_THEN_NextCountSetToToday()
               throws MxException {
      // ABC Class is originally D but changes to A when recalculateAbcClass is called.
      // With last count and next count set to null, the next count date will get set to today.

      executeTest( null, null, new Date() );
   }


   @Test
   public void
         test_GIVEN_BinLevelWithNextCountWithinRange_WHEN_AbcClassRecalcProcRuns_THEN_NextCountRemainsUnchanged()
               throws MxException {
      // ABC Class is originally D but changes to A when recalculateAbcClass is called.
      // With last count set to null and next count set for 3 days from now,
      // when moving from D to A, the interval is changed to 3 months.
      // The current next count day falls within that interval so it remains unchanged.

      nextCountDate.add( Calendar.DATE, 3 );

      executeTest( null, nextCountDate.getTime(), nextCountDate.getTime() );
   }


   @Test
   public void
         test_GIVEN_BinLevelWithNextCountOutsideRange_WHEN_AbcClassRecalcProcRuns_THEN_NextCountMovedEarlier()
               throws MxException {
      // ABC Class is originally D but changes to A when recalculateAbcClass is called.
      // With a null last count date and next count set for 1 year from now,
      // when moving from D to A, the interval is changed to 3 months.
      // The current next count date falls outside of that 3 months so it
      // is moved in to the end of the 3 month window.

      nextCountDate.add( Calendar.YEAR, 1 );

      expectedDate.add( Calendar.MONTH, 3 );

      executeTest( null, nextCountDate.getTime(), expectedDate.getTime() );
   }


   @Test
   public void
         test_GIVEN_BinLevelWithNextCountAndLastCount_WHEN_AbcClassRecalcProcRuns_THEN_NextCountIsLastCountPlusInterval()
               throws MxException {
      // ABC Class is originally D but changes to A when recalculateAbcClass is called.
      // With last count set to 2 months ago and next count set for 10 months out,
      // when moving from D to A, the interval is changed to 3 months.
      // The new next count day should be last count + new interval.

      lastCountDate.add( Calendar.MONTH, -2 );

      nextCountDate.add( Calendar.MONTH, 10 );

      expectedDate.add( Calendar.MONTH, 1 );

      executeTest( lastCountDate.getTime(), nextCountDate.getTime(), expectedDate.getTime() );
   }


   private void executeTest( Date lastCountDate, Date nextCountDate, Date expectedDate ) {
      iPart = new PartNoBuilder().withAbcClass( RefAbcClassKey.D )
            .withAverageUnitPrice( BigDecimal.TEN ).build();

      createBinAndPartCount( ABQ_STORE_BIN, lastCountDate, nextCountDate );
      StoredProcedureCall.getInstance().recalculateABCClass();

      InvLocPartCount invLocPartCount = InvLocPartCount.findByPrimaryKey( iPartCountKey );

      assertEqualDates( expectedDate, invLocPartCount.getNextCountDt() );

   }


   protected void assertEqualDates( Date expected, Date value ) {
      DateFormat formatter = new SimpleDateFormat( DATE_PATTERN );
      String strExpected = formatter.format( expected );
      String strValue = formatter.format( value );
      assertEquals( strExpected, strValue );
   }


   protected void createBinAndPartCount( String binCode, Date lastCountDate, Date nextCountDate ) {

      iBinLoc = Domain.createLocation( location -> {
         location.setCode( binCode );
         location.setType( RefLocTypeKey.BIN );
      } );

      createCrew( iBinLoc );

      if ( iPart == null ) {
         iPart = new PartNoBuilder().build();
      }
      try {
         new BinLevelService().add( iPart, null, iBinLoc, new BinLevelTO() );
      } catch ( MxException e ) {
         e.printStackTrace();
      }

      // InvLocPartCountKey partCountKey = new InvLocPartCountKey( iBinLoc, iPart, 1 );
      iPartCountKey = new InvLocPartCountKey( iBinLoc, iPart, 1 );

      new PartCountBuilder( iPartCountKey ).withLastCountDate( lastCountDate )
            .withNextCountDate( nextCountDate ).isHistorical( false ).build();
   }


   private void createCrew( LocationKey aLocationKey ) {

      Domain.createCrew( crew -> {
         crew.setUsers( iHr );
         crew.setLocations( aLocationKey );
      } );
   }
}
