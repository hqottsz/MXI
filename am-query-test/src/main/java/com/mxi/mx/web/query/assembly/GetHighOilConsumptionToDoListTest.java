
package com.mxi.mx.web.query.assembly;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class runs a unit test on the GetHighOilConsumptionToDoList query file.
 *
 * @author krangaswamy
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetHighOilConsumptionToDoListTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            GetHighOilConsumptionToDoListTest.class );
   }


   /** The ASSEMBLY_KEY */
   private static final String ASSEMBLY_KEY = "5001:CFM56";

   /** The ASSEMBLY_NAME */
   private static final String ASSEMBLY_NAME = "CFM56 (CFM56-3C)";

   /** The PART_NO_KEY */
   private static final String PART_NO_KEY = "5001:1001";

   /** The PART_NO_NAME */
   private static final String PART_NO_NAME = "CFM56-3C";

   /** The HOC_INV_NO_DB_ID */
   private static final String HOC_INV_NO_DB_ID = "4650";

   /** The HOC_INV_NO_ID */
   private static final String HOC_INV_NO_ID = "392792";

   /** The SERIAL_NO_KEY */
   private static final String SERIAL_NO_KEY = "4650:392792";

   /** The SERIAL_NO_NAME */
   private static final String SERIAL_NO_NAME = "XXX";

   /** The OPERATOR_KEY */
   private static final String OPERATOR_KEY = "5006:100001";

   private static final String ORG_KEY = "5006:100001";

   /** The OPERATOR_CODES_NAME */
   private static final String OPERATOR_CODES_NAME = "DL-DAL / DL / DAL";

   /** The INSTALLED_ON_KEY */
   private static final String INSTALLED_ON_KEY = "4650:381830";

   /** The INSTALLED_ON_NAME */
   private static final String INSTALLED_ON_NAME = "Airbus A319/A320 - 100200";

   /** The STATUS */
   private static final String STATUS = "CAUTION";

   /** The STATUS_ORDER */
   private static final String STATUS_ORDER = "1";

   /** The STATUS_CHANGE_DATE */
   private static final String STATUS_CHANGE_DATE = "12-Dec-2010 03:31:12";

   /** The CHANGED_BY_TASK_KEY */
   private static final String CHANGED_BY_TASK_KEY = "4650:134138";

   /** The CHANGED_BY_TASK_BARCODE */
   private static final String CHANGED_BY_TASK_BARCODE = "T40S00017V5";

   /** The LOG_REASON_CD */
   private static final String LOG_REASON_CD = "THRESHOLD";

   /** The NOTES */
   private static final String NOTES = "TEST";

   /** The USAGE_PARAMETER */
   private static final String USAGE_PARAMETER = "CYCLES";

   /** The USAGE_TSN */
   private static final String USAGE_TSN = "4.0";

   /** The USAGE_TSO */
   private static final String USAGE_TSO = "4.0";

   /** The USAGE_TSI */
   private static final String USAGE_TSI = "4.0";

   /** The REPEAT_FLAG */
   private static final String REPEAT_FLAG = "1";


   /**
    * Execute GetHighOilConsumptionToDoList.qrx
    */
   @Test
   public void testGetHighOilConsumptionToDoList() {

      DataSetArgument lArgs = new DataSetArgument();

      // Parameters required by the query.
      lArgs.add( "aAssemblyClassNum", "0" );
      lArgs.add( "aAssemblyName", "CFM56" );
      lArgs.add( "aOemPartNo", "CFM56-3C" );
      lArgs.add( "aCarrierDbId", 5006 );
      lArgs.add( "aCarrierId", 100001 );
      lArgs.add( "aOCStatusDbId", 10 );
      lArgs.add( "aOCStatusCd", "CAUTION" );
      lArgs.add( "aRepeatStartDate", "13-Nov-2009 03:37:06" );
      lArgs.add( "aHrDbId", 4650 );
      lArgs.add( "aHrId", 6000159 );

      // Execute query!
      DataSet lResult = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      // Get the first row.
      lResult.next();

      // Verify the contents of the row returned.
      testRow( lResult );
   }


   /**
    * Verify the contents of the row returned.
    *
    * @param aResult
    *           the dataset
    */
   private void testRow( DataSet aResult ) {

      // Determine if the following are returned :
      // inv_inv.orig_assmbl_db_id || ':' || inv_inv.orig_assmbl_cd
      MxAssert.assertEquals( ASSEMBLY_KEY, aResult.getString( "assembly_key" ) );

      // eqp_assmbl.assmbl_cd || ' (' || eqp_assmbl.assmbl_name || ')'
      MxAssert.assertEquals( ASSEMBLY_NAME, aResult.getString( "assembly_name" ) );

      // eqp_part_no.part_no_db_id || ':' || eqp_part_no.part_no_id
      MxAssert.assertEquals( PART_NO_KEY, aResult.getString( "part_no_key" ) );

      // eqp_part_no.part_no_oem
      MxAssert.assertEquals( PART_NO_NAME, aResult.getString( "part_no_name" ) );

      // inv_inv.Inv_No_Db_Id
      MxAssert.assertEquals( HOC_INV_NO_DB_ID, aResult.getString( "hoc_inv_no_db_id" ) );

      // inv_inv.Inv_No_Id
      MxAssert.assertEquals( HOC_INV_NO_ID, aResult.getString( "hoc_inv_no_id" ) );

      // inv_inv.Inv_No_Db_Id || ':' || inv_inv.Inv_No_Id
      MxAssert.assertEquals( SERIAL_NO_KEY, aResult.getString( "serial_no_key" ) );

      // inv_inv.serial_no_oem
      MxAssert.assertEquals( SERIAL_NO_NAME, aResult.getString( "serial_no_name" ) );

      // org_carrier.carrier_db_id || ':' || org_carrier.carrier_id
      MxAssert.assertEquals( OPERATOR_KEY, aResult.getString( "operator_key" ) );

      // org_carrier.org_db_id || ':' || org_carrier.org_id
      MxAssert.assertEquals( ORG_KEY, aResult.getString( "org_key" ) );

      // org_carrier.carrier_cd || '/' || org_carrier.iata_cd || '/' || org_carrier.icao_cd
      MxAssert.assertEquals( OPERATOR_CODES_NAME, aResult.getString( "operator_codes_name" ) );

      // h_inv_inv.inv_no_db_id || ':' || h_inv_inv.inv_no_id
      MxAssert.assertEquals( INSTALLED_ON_KEY, aResult.getString( "installed_on_key" ) );

      // h_inv_inv.inv_no_sdesc
      MxAssert.assertEquals( INSTALLED_ON_NAME, aResult.getString( "installed_on_name" ) );

      // inv_oil_status_log.oil_status_cd AS status
      MxAssert.assertEquals( STATUS, aResult.getString( "status" ) );

      // ref_oil_status.oil_status_ord
      MxAssert.assertEquals( STATUS_ORDER, aResult.getString( "status_order" ) );

      // inv_oil_status_log.log_dt
      MxAssert.assertEquals( STATUS_CHANGE_DATE, aResult.getString( "status_change_date" ) );

      // inv_oil_status_log.sched_db_id || ':' || inv_oil_status_log.sched_id
      MxAssert.assertEquals( CHANGED_BY_TASK_KEY, aResult.getString( "changed_by_task_key" ) );

      // sched_stask.barcode_sdesc
      MxAssert.assertEquals( CHANGED_BY_TASK_BARCODE,
            aResult.getString( "changed_by_task_barcode" ) );

      // inv_oil_status_log.log_reason_cd
      MxAssert.assertEquals( LOG_REASON_CD, aResult.getString( "reason" ) );

      // inv_oil_status_log.note_mdesc
      MxAssert.assertEquals( NOTES, aResult.getString( "notes" ) );

      // mim_data_type.data_type_cd AS usage_parameter
      MxAssert.assertEquals( USAGE_PARAMETER, aResult.getString( "usage_parameter" ) );

      // evt_inv_usage.tsn_qt
      MxAssert.assertEquals( USAGE_TSN, aResult.getString( "usage_tsn" ) );

      // Output of the isRepeatHighOilConsumption.fnc
      MxAssert.assertEquals( REPEAT_FLAG, aResult.getString( "repeat_flag" ) );

      // Determine if one row is returned.
      MxAssert.assertEquals( "Number of retrieved rows", 1, aResult.getRowCount() );
   }
}
