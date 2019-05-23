package com.mxi.mx.core.query.inventorycount;

import java.util.Date;

import org.junit.Test;

import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.utils.DateUtils;


/**
 * This test class ensures that GetBinsForAdhocCount.qrx query returns correct data.
 *
 * @author Libin Cai
 * @created May 1, 2019
 */
public final class GetBinsForAdhocCountTest extends GetBinsCommonTestCases {

   private boolean iIncludeLastDayCount = true;


   @Test
   public void test_GIVEN_InvalidBinCode_WHEN_ExecuteQuery_THEN_NoDataReturned() {

      createBinAndPartCount( "INVALID_BIN_CODE" );

      executeQuery();

      assertNoDataReturned();
   }


   @Test
   public void
         test_GIVEN_BinCountedIn24Hours_WHEN_ExecuteQueryNotIncludeLastDayCount_THEN_ThatBinNotReturned() {

      iIncludeLastDayCount = false;
      createBinAndPartCount();

      executeQuery();

      assertNoDataReturned();
   }


   @Test
   public void
         test_GIVEN_BinCountedIn24Hours_WHEN_ExecuteQueryIncludeLastDayCount_THEN_ThatBinReturned() {

      iIncludeLastDayCount = true;
      createBinAndPartCount();

      executeQuery();

      assertOneRowReturned();
   }


   private void createBinAndPartCount() {
      Date lastCountDate = DateUtils.addHours( new Date(), -2 );
      createBinAndPartCount( ABQ_STORE_BIN, lastCountDate, null );
   }


   @Override
   void executeQuery() {

      DataSetArgument args = new DataSetArgument();
      args.add( iHr, "aHrDbId", "aHrId" );
      args.add( "aPartialBinCode", "ABQ/STO" );
      args.add( "aIncludeLastDayCount", iIncludeLastDayCount );

      setFilter( args );
      iQuerySet = QueryExecutor.executeQuery( getClass(), args );
   }

}
