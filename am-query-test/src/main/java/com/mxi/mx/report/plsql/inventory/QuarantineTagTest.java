package com.mxi.mx.report.plsql.inventory;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.QuarQuarKey;


/**
 * Tests the report_quarantine_tag_pkg PL/SQL functions
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class QuarantineTagTest {

   private static final String PACKAGE_NAME = "report_quarantine_tag_pkg";
   private static final String QUARANTINE_TAG_FUNCTION = "GetQuarantineTag";
   private static final String QUARANTINE_ACTION_FUNCTION = "GetQuarantineAction";
   private static final String TEST_DATA_FILE = "QuarantineTagTest.xml";
   private static final QuarQuarKey QUAR_QUAR_KEY_1 = new QuarQuarKey( 4650, 1 );
   private static final QuarQuarKey QUAR_QUAR_KEY_2 = new QuarQuarKey( 4650, 2 );


   public enum QuarantineTagColumName {
      QUARANTINED_DATE, USER_FIRST_NAME, USER_LAST_NAME, QUARANTINE_REASON_CD,
      QUARANTINE_REASON_DESC, QUARANTINE_LOCATION, QUARANTINE_NOTES, PART_NUMBER, PART_NAME,
      MANUFACTURER_CD, MANUFACTURER_NAME, SERIAL_NO, RECEIVED_ON, PURCHASE_ORDER_NO, QUANTITY,
      QUANTITY_UNIT_CD, VENDOR_CD, VENDOR_NAME, BARCODE, QUAR_DB_ID, QUAR_ID
   }

   public enum QuarantineActionColumName {
      ACTION_BARCODE, DISCREPANCY_DESC, ACTION_DESC, STATUS_CD
   }


   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      XmlLoader.load( iConnection.getConnection(), QuarantineTagTest.class, TEST_DATA_FILE );

   }


   private QuerySet executeFunction( String aFunctionName, QuarQuarKey aQuarQuarKey )
         throws SQLException {

      DataSetArgument lArgs = aQuarQuarKey.getPKWhereArg();

      return QueryExecutor.executeTableFunction( null, lArgs, iConnection.getConnection(),
            PACKAGE_NAME, aFunctionName );
   }


   /**
    * Test the report_quarantine_tag_pkg.GetQuarantineTag() function
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void itGetQuarantineTagHeaderInfo() throws Exception {
      QuerySet lQuerySet = executeFunction( QUARANTINE_TAG_FUNCTION, QUAR_QUAR_KEY_1 );
      lQuerySet.first();

      assertEquals( 1, lQuerySet.getRowCount() );

      assertEquals( "Incorrect first name", "Jonhy",
            lQuerySet.getString( QuarantineTagColumName.USER_FIRST_NAME.toString() ) );
      assertEquals( "Incorrect last name", "Cash",
            lQuerySet.getString( QuarantineTagColumName.USER_LAST_NAME.toString() ) );
      assertEquals( "Incorrect quarantine date", "02-Jan-2017 00:00:01",
            lQuerySet.getString( QuarantineTagColumName.QUARANTINED_DATE.toString() ) );
      assertEquals( "Incorrect quarantine reason", "MYREASON",
            lQuerySet.getString( QuarantineTagColumName.QUARANTINE_REASON_CD.toString() ) );
      assertEquals( "Incorrect stage reason", "Stage Reason for testing", lQuerySet
            .getString( QuarantineTagColumName.QUARANTINE_REASON_DESC.toString() ).toString() );
      assertEquals( "Incorrect location", "YOW",
            lQuerySet.getString( QuarantineTagColumName.QUARANTINE_LOCATION.toString() ) );
      assertEquals( "Incorrect part no", "F50B766204",
            lQuerySet.getString( QuarantineTagColumName.PART_NUMBER.toString() ) );
      assertEquals( "Incorrect part name", "TUBE",
            lQuerySet.getString( QuarantineTagColumName.PART_NAME.toString() ) );
      assertEquals( "AUXILEC",
            lQuerySet.getString( QuarantineTagColumName.MANUFACTURER_CD.toString() ) );
      assertEquals( "Incorrect manufacturer name", "Auxilec Company",
            lQuerySet.getString( QuarantineTagColumName.MANUFACTURER_NAME.toString() ) );
      assertEquals( "Incorrect serial no", "sn-150",
            lQuerySet.getString( QuarantineTagColumName.SERIAL_NO.toString() ) );
      assertEquals( "02-Jan-2016 00:00:01",
            lQuerySet.getString( QuarantineTagColumName.RECEIVED_ON.toString() ) );
      assertEquals( "Incorrect purchase order no", "PO-10001",
            lQuerySet.getString( QuarantineTagColumName.PURCHASE_ORDER_NO.toString() ).toString() );
      assertEquals( "Incorrect quantity", "5",
            lQuerySet.getString( QuarantineTagColumName.QUANTITY.toString() ) );
      assertEquals( "EA",
            lQuerySet.getString( QuarantineTagColumName.QUANTITY_UNIT_CD.toString() ) );
      assertEquals( "Incorrect vendor", "AIR",
            lQuerySet.getString( QuarantineTagColumName.VENDOR_CD.toString() ) );
      assertEquals( "AIRBUS",
            lQuerySet.getString( QuarantineTagColumName.VENDOR_NAME.toString() ) );
      assertEquals( "Incorrect barcode", "Q0002JR9",
            lQuerySet.getString( QuarantineTagColumName.BARCODE.toString() ) );
      assertEquals( "Incorrect quarantine db id", "4650",
            lQuerySet.getString( QuarantineTagColumName.QUAR_DB_ID.toString() ) );
      assertEquals( "Incorrect quarantine id", "1",
            lQuerySet.getString( QuarantineTagColumName.QUAR_ID.toString() ) );
   }


   /**
    * Test the report_quarantine_tag_pkg.GetQuarantineAction() function
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void itGetQuarantineTagActionInfo() throws Exception {

      QuerySet lQuerySet = executeFunction( QUARANTINE_ACTION_FUNCTION, QUAR_QUAR_KEY_2 );

      assertEquals( 2, lQuerySet.getRowCount() );

      lQuerySet.first();
      assertEquals( "P0112JR9",
            lQuerySet.getString( QuarantineActionColumName.ACTION_BARCODE.toString() ) );
      assertEquals( "Discrepancy 1",
            lQuerySet.getString( QuarantineActionColumName.DISCREPANCY_DESC.toString() ) );
      assertEquals( "Action 1",
            lQuerySet.getString( QuarantineActionColumName.ACTION_DESC.toString() ) );
      assertEquals( "OPEN", lQuerySet.getString( QuarantineActionColumName.STATUS_CD.toString() ) );

      lQuerySet.last();
      assertEquals( "Y0652GP1",
            lQuerySet.getString( QuarantineActionColumName.ACTION_BARCODE.toString() ) );
      assertEquals( "Discrepancy 2",
            lQuerySet.getString( QuarantineActionColumName.DISCREPANCY_DESC.toString() ) );
      assertEquals( "Action 2",
            lQuerySet.getString( QuarantineActionColumName.ACTION_DESC.toString() ) );
      assertEquals( "OPEN", lQuerySet.getString( QuarantineActionColumName.STATUS_CD.toString() ) );

   }

}
