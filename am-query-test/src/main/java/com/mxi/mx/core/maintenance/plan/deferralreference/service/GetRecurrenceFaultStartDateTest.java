package com.mxi.mx.core.maintenance.plan.deferralreference.service;

import java.util.Date;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.util.FormatUtil;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.EventKey;


/**
 * Tests com.mxi.mx.core.maintenance.plan.deferralreference.service.GetRecurrenceFaultStartDate
 * query
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetRecurrenceFaultStartDateTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(),
            GetRecurrenceFaultStartDateTest.class, "GetRecurrenceFaultStartDateTest.xml" );
   }


   @Test
   public void getRecurrenceFaultStartDateFromDeadline() {

      final EventKey lCorrectiveTaskKey = new EventKey( "4650:44" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( lCorrectiveTaskKey, "aEventDbId", "aEventId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      // There should be exactly one result
      Assert.assertEquals( 1, lDataSet.getRowCount() );

      // There should be a result
      Assert.assertTrue( lDataSet.next() );

      Date lDate = new Date( "11-MAY-2012 12:00:00" );
      UUID lUuid =
            UUID.fromString( FormatUtil.formatUniqueId( "AB481D95FFD140EBB6F71CED2B8EBF65" ) );

      Assert.assertEquals( lDate, lDataSet.getDate( "recur_start_dt" ) );
      Assert.assertEquals( lUuid, lDataSet.getUuid( "alt_id" ) );
   }


   @Test
   public void getRecurrenceFaultStartDateFromPreviousIncident() {

      final EventKey lCorrectiveTaskKey = new EventKey( "4650:54" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( lCorrectiveTaskKey, "aEventDbId", "aEventId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      // There should be exactly one result
      Assert.assertEquals( 1, lDataSet.getRowCount() );

      // There should be a result
      Assert.assertTrue( lDataSet.next() );

      Date lDate = new Date( "12-MAY-2012 12:00:00" );
      UUID lUuid =
            UUID.fromString( FormatUtil.formatUniqueId( "BB481D95FFD140EBB6F71CED2B8EBF65" ) );

      Assert.assertEquals( lDate, lDataSet.getDate( "recur_start_dt" ) );
      Assert.assertEquals( lUuid, lDataSet.getUuid( "alt_id" ) );
   }


   @Test
   public void getRecurrenceFaultStartDate_EmptyDataset() {

      final EventKey lCorrectiveTaskKey = new EventKey( "4650:40" );

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( lCorrectiveTaskKey, "aEventDbId", "aEventId" );

      DataSet lDataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lDataSetArgument );

      Assert.assertTrue( lDataSet.isEmpty() );
   }

}
