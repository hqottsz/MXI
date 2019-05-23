
package com.mxi.mx.core.maintenance.plan.phoneupdeferral.raisefault.dao.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * Query test for com.mxi.mx.core.maintenance.plan.phoneupdeferral.raisefault.dao.query.
 * GetLogAndScheduleFaultDetails.qrx.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetLogAndScheduleFaultDetailsTest {

   // Test Data
   private static final String ID = "ABCDEF1234567890ABCDEF1234567890";
   private static final String CORR_TASK_SDESC = "A Fault Name";
   private static final String CORR_TASK_BARCODE = "T1234567890";
   private static final String WP_NO = "12347";
   private static final String WP_LINE_NO = "10";
   private static Date WP_ACTUAL_START_DT;
   private static Date WP_ESTIMATED_END_DT;

   // Query Access Object
   private final QueryAccessObject iQao = QuerySetFactory.getInstance();

   @ClassRule
   public static DatabaseConnectionRule sConnection = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() throws ParseException {

      // Load the dates
      WP_ACTUAL_START_DT = getAsDate( "09-SEP-2016 14:25:00" );
      WP_ESTIMATED_END_DT = getAsDate( "09-SEP-2016 15:25:00" );

      XmlLoader.load( sConnection.getConnection(), GetLogAndScheduleFaultDetailsTest.class,
            "GetLogAndScheduleFaultDetailsTest.xml" );
   }


   @Test
   public void itRetrievesDetails() {
      // Execute the query
      QuerySet lQs = this.executeQuery( ID );

      // Assume we have the details result
      Assert.assertTrue( lQs.next() );

      // Assert that the details are correct
      Assert.assertEquals( CORR_TASK_SDESC, lQs.getString( "corr_task_sdesc" ) );
      Assert.assertEquals( CORR_TASK_BARCODE, lQs.getString( "corr_task_barcode" ) );
      Assert.assertEquals( WP_NO, lQs.getString( "wp_no" ) );
      Assert.assertEquals( WP_LINE_NO, lQs.getString( "wp_line_no" ) );
      Assert.assertEquals( WP_ACTUAL_START_DT, lQs.getDate( "wp_actual_start_dt" ) );
      Assert.assertEquals( WP_ESTIMATED_END_DT, lQs.getDate( "wp_estimated_end_dt" ) );
   }


   private QuerySet executeQuery( String aId ) {
      String lQueryName =
            "com.mxi.mx.core.maintenance.plan.phoneupdeferral.raisefault.dao.query.GetLogAndScheduleFaultDetails";

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aId", aId );

      QuerySet lExecuteQuery = iQao.executeQuery( lQueryName, lArgs );
      return lExecuteQuery;
   }


   private static Date getAsDate( String aDateString ) throws ParseException {
      Date lDate = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" ).parse( aDateString );
      return lDate;
   }
}
