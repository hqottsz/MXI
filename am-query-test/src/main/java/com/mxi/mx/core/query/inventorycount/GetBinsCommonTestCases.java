package com.mxi.mx.core.query.inventorycount;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartCountBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocPartCountKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefAbcClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartUseKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;


/**
 * This class contains common test cases of GetBinsByxxx.qrx query.
 *
 * @author Libin Cai
 * @created May 1, 2019
 */
@RunWith( BlockJUnit4ClassRunner.class )
public abstract class GetBinsCommonTestCases {

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
   protected int iFilterType = 0;

   private final static int FILTER_BY_PART_USE_FOR_PART = 2;
   private final static int FILTER_BY_PART_USE_FOR_TOOLS = 3;


   @Before
   public void setup() {
      iHr = Domain.createHumanResource();

      Domain.createHumanResource( aHr -> {
         aHr.setUser( new UserKey( iFakeUserId ) );
      } );
   }


   @Test
   public void test_GIVEN_NonBinLoc_WHEN_ExecuteQuery_THEN_NoDataReturned() {

      LocationKey lNonBinLocation = Domain.createLocation( location -> {
         location.setCode( ABQ_STORE_BIN );
         location.setType( RefLocTypeKey.STORE );
      } );

      createCrew( lNonBinLocation );

      executeQuery();

      assertNoDataReturned();
   }


   @Test
   public void test_GIVEN_BinLocNotAssignedToDept_WHEN_ExecuteQuery_THEN_NoDataReturned() {

      createBin( ABQ_STORE_BIN );

      executeQuery();

      assertNoDataReturned();
   }


   @Test
   public void
         test_GIVEN_BinLevelWithPartUseForTools_WHEN_ExecuteQueryWithPartUseForToolsFilter_THEN_ThatBinReturned()
               throws MxException {

      Date dueToday = new Date();
      iPart = new PartNoBuilder().isTool().build();

      createBinAndPartCount( ABQ_STORE_BIN, dueToday );

      iFilterType = FILTER_BY_PART_USE_FOR_TOOLS;
      executeQuery();

      assertOneRowReturned();
   }


   @Test
   public void
         test_GIVEN_BinLevelWithPartUseForTools_WHEN_ExecuteQueryWithPartUseForPartFilter_THEN_ThatBinNotReturned()
               throws MxException {

      Date dueToday = new Date();
      iPart = new PartNoBuilder().isTool().build();

      createBinAndPartCount( ABQ_STORE_BIN, dueToday );

      iFilterType = FILTER_BY_PART_USE_FOR_PART;
      executeQuery();

      assertNoDataReturned();
   }


   @Test
   public void test_GIVEN_FourBins_WHEN_ExecuteQuery_THEN_FourRowReturnedWithCorrectOrder()
         throws MxException {

      final String ABQ_STORE_BIN1 = "ABQ/STORE/BIN1";
      final String ABQ_STORE_BIN2 = "ABQ/STORE/BIN2";
      final String ABQ_STORE_BIN10 = "ABQ/STORE/BIN10";

      createBinAndPartCount( ABQ_STORE_BIN );
      createBinAndPartCount( ABQ_STORE_BIN1 );
      createBinAndPartCount( ABQ_STORE_BIN2 );
      createBinAndPartCount( ABQ_STORE_BIN10 );

      executeQuery();

      // assert that total number of rows returned is as expected
      assertEquals( 4, iQuerySet.getRowCount() );

      iQuerySet.next();
      assertEquals( ABQ_STORE_BIN, iQuerySet.getString( "loc_cd" ) );
      iQuerySet.next();
      assertEquals( ABQ_STORE_BIN1, iQuerySet.getString( "loc_cd" ) );
      iQuerySet.next();
      assertEquals( ABQ_STORE_BIN2, iQuerySet.getString( "loc_cd" ) );
      iQuerySet.next();
      assertEquals( ABQ_STORE_BIN10, iQuerySet.getString( "loc_cd" ) );
   }


   abstract void executeQuery();


   protected void createBinAndPartCount( String binCode ) {
      createBinAndPartCount( binCode, new Date() );
   }


   protected void createBinAndPartCount( String binCode, Date nextCountDate ) {
      createBinAndPartCount( binCode, null, nextCountDate );
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

      InvLocPartCountKey partCountKey = new InvLocPartCountKey( iBinLoc, iPart, 1 );

      new PartCountBuilder( partCountKey ).withLastCountDate( lastCountDate )
            .withNextCountDate( nextCountDate ).isHistorical( false ).build();
   }


   protected void setFilter( DataSetArgument args ) {

      String[] partUseKeyCols =
            new String[] { "eqp_part_no.part_use_db_id", "eqp_part_no.part_use_cd" };
      switch ( iFilterType ) {
         case 1:
            // filter of ABC class A
            args.addWhereIn(
                  new String[] { "eqp_part_no.abc_class_db_id", "eqp_part_no.abc_class_cd" },
                  RefAbcClassKey.A );
            break;
         case 2:
            // filter of part use for part
            args.addWhereNotIn( partUseKeyCols, RefPartUseKey.TOOLS );
            break;
         case 3:
            // filter of part use for tools
            args.addWhereIn( partUseKeyCols, RefPartUseKey.TOOLS );
            break;
         default:
            break;
      }
   }


   protected void assertNoDataReturned() {
      assertEquals( 0, iQuerySet.getRowCount() );
   }


   protected void assertOneRowReturned() {
      assertEquals( 1, iQuerySet.getRowCount() );
   }


   private LocationKey createBin( String aLocationCode ) {

      LocationKey lBinLoc = Domain.createLocation( location -> {
         location.setCode( aLocationCode );
         location.setType( RefLocTypeKey.BIN );
      } );

      return lBinLoc;
   }


   private void createCrew( LocationKey aLocationKey ) {

      Domain.createCrew( crew -> {
         crew.setUsers( iHr );
         crew.setLocations( aLocationKey );
      } );
   }

}
