package com.mxi.mx.core.query.inventorycount;

import java.util.Date;

import org.junit.Test;

import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.RefAbcClassKey;


/**
 * This test class ensures that GetBinsForCycleCount.qrx query returns correct data.
 *
 * @author Libin Cai
 * @created May 1, 2019
 */
public final class GetBinsForCycleCountTest extends GetBinsCommonTestCases {

   private final static int FILTER_BY_ABC_CLASS_A = 1;


   @Test
   public void test_GIVEN_BinDueToday_WHEN_ExecuteQueryWithDueDaysOneDayAfter_THEN_ThatBinReturned()
         throws MxException {

      Date dueToday = new Date();
      iPart = new PartNoBuilder().build();

      createBinAndPartCount( ABQ_STORE_BIN, dueToday );

      executeQuery();

      assertOneRowReturned();
   }


   @Test
   public void
         test_GIVEN_BinDueTwoDaysAfter_WHEN_ExecuteQueryWithDueDaysOneDayAfter_THEN_ThatBinNotReturned()
               throws MxException {

      Date dueTwoDayAfter = DateUtils.addDays( new Date(), 2 );
      iPart = new PartNoBuilder().build();

      createBinAndPartCount( ABQ_STORE_BIN, dueTwoDayAfter );

      executeQuery();

      assertNoDataReturned();
   }


   @Test
   public void
         test_GIVEN_BinLevelWithPartOfClassA_WHEN_ExecuteQueryWithAbcClassAFileter_THEN_ThatBinReturned()
               throws MxException {

      Date dueToday = new Date();
      iPart = new PartNoBuilder().withAbcClass( RefAbcClassKey.A ).build();

      createBinAndPartCount( ABQ_STORE_BIN, dueToday );

      iFilterType = FILTER_BY_ABC_CLASS_A;
      executeQuery();

      assertOneRowReturned();
   }


   @Test
   public void
         test_GIVEN_BinLevelWithPartOfClassB_WHEN_ExecuteQueryWithAbcClassAFileter_THEN_ThatBinNotReturned()
               throws MxException {

      Date dueToday = new Date();
      iPart = new PartNoBuilder().withAbcClass( RefAbcClassKey.B ).build();

      createBinAndPartCount( ABQ_STORE_BIN, dueToday );

      iFilterType = FILTER_BY_ABC_CLASS_A;
      executeQuery();

      assertNoDataReturned();
   }


   @Override
   void executeQuery() {

      DataSetArgument args = new DataSetArgument();
      args.add( iHr, "aHrDbId", "aHrId" );
      args.add( "aDays", 1 );

      setFilter( args );
      iQuerySet = QueryExecutor.executeQuery( getClass(), args );
   }

}
