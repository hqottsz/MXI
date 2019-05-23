
package com.mxi.mx.web.query.po;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.mxi.mx.common.dataset.InvalidSearchCriteriaException;


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class OrderSearchByTypeBasicTest {

   private static final int USERID = 1;

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), OrderSearchByTypeBasicTest.class );
   }


   /** The query execution data set */
   private DataSet dataSet = null;


   @Test
   public void testDefaultSearch() {

      // Execute the Query
      execute( getDefaultDataSetArgument() );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByReceiptOrganization() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, invalid org
      dataSetArgument.addWhereLike( "org_org.org_cd", "NotMXI" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // change search criteria, valid org
      dataSetArgument.addWhereLike( "org_org.org_cd", "MXI" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPurchaseContact() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, invalid contact
      dataSetArgument.addWhereLike( "utl_user.username", "mxi" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // change search criteria, valid contact
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPurchaseContactWithOrderType() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, valid contact, type not there
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "BORROW" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // search criteria, valid contact, valid type
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "PURCHASE" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPurchaseContactWithStatus() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, valid contact, status incorrect
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "evt_event.event_status_cd", "POCLOSED" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // search criteria, valid contact, status correct
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "evt_event.event_status_cd", "POOPEN" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPurchaseContactWithPartGroupPurchaseType()
         throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, valid contact, no Consumable purchase orders
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "eqp_bom_part.purch_type_cd", "CONSUM" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // search criteria, valid contact, "Other" part group purchase type
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "eqp_bom_part.purch_type_cd", "OTHER" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // search criteria, valid contact, "Eng" part group purchase type
      dataSetArgument.addWhereLike( "utl_user.username", "testuser" );
      dataSetArgument.addWhereEquals( "eqp_bom_part.purch_type_cd", "ENG" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPartNoOem() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, part does not exist
      dataSetArgument.addWhereLike( "eqp_part_no.part_no_oem", "PART1" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // change search criteria, valid part
      dataSetArgument.addWhereLike( "eqp_part_no.part_no_oem", "TEST123" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPartNoOemWithOrderType() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, valid part, invalid order type
      dataSetArgument.addWhereLike( "eqp_part_no.part_no_oem", "TEST123" );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "BORROW" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // change search criteria, valid part, valid order type
      dataSetArgument.addWhereLike( "eqp_part_no.part_no_oem", "TEST123" );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "PURCHASE" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPartNoOemWithStatus() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, valid part, status incorrect
      dataSetArgument.addWhereLike( "eqp_part_no.part_no_oem", "TEST123" );
      dataSetArgument.addWhereEquals( "evt_event.event_status_cd", "POCLOSED" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // change search criteria, valid part, status correct
      dataSetArgument.addWhereLike( "eqp_part_no.part_no_oem", "TEST123" );
      dataSetArgument.addWhereEquals( "evt_event.event_status_cd", "POOPEN" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByOrderNumber() throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, a non existent order number
      dataSetArgument.addWhereLike( "evt_event.event_sdesc", "P01" );

      // Execute the Query
      execute( dataSetArgument );

      // assert no data is returned
      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, a valid order number
      dataSetArgument.addWhereLike( "evt_event.event_sdesc", "P0100012" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByOrderNumberWithExternalReference()
         throws InvalidSearchCriteriaException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, a valid order number, invalid external reference
      dataSetArgument.addWhereLike( "evt_event.event_sdesc", "P01" );
      dataSetArgument.addWhereLike( "evt_event.ext_key_sdesc", "EXTREF" );

      // Execute the Query
      execute( dataSetArgument );

      // assert no data is returned
      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, a valid order number, valid external reference
      dataSetArgument.addWhereLike( "evt_event.event_sdesc", "P0100012" );
      dataSetArgument.addWhereLike( "evt_event.ext_key_sdesc", "ext-ref" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByAOGAuthorization() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, AOG override set, date outside range
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-14 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-06-28 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );

      // AOG
      dataSetArgument.addWhere( "po_auth.po_db_id = po_header.po_db_id" );
      dataSetArgument.addWhere( "po_auth.po_id = po_header.po_id" );
      dataSetArgument.addWhere( "po_auth.aog_override_bool = 1" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, AOG override set, date inside range
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );

      // AOG
      dataSetArgument.addWhere( "po_auth.po_db_id = po_header.po_db_id" );
      dataSetArgument.addWhere( "po_auth.po_id = po_header.po_id" );
      dataSetArgument.addWhere( "po_auth.aog_override_bool = 1" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByAOGAuthorizationWithOrderType() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, AOG override set, date inside range, order type invalid
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );

      // AOG
      dataSetArgument.addWhere( "po_auth.po_db_id = po_header.po_db_id" );
      dataSetArgument.addWhere( "po_auth.po_id = po_header.po_id" );
      dataSetArgument.addWhere( "po_auth.aog_override_bool = 1" );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "BORROW" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, AOG override set, date inside range, order type valid
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );

      // AOG
      dataSetArgument.addWhere( "po_auth.po_db_id = po_header.po_db_id" );
      dataSetArgument.addWhere( "po_auth.po_id = po_header.po_id" );
      dataSetArgument.addWhere( "po_auth.aog_override_bool = 1" );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "PURCHASE" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByDateRange() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date outside range
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-14 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-06-28 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByDateRangeWithOrderType() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, invalid order type
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "BORROW" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, valid order type
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "po_header.po_type_cd", "PURCHASE" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByDateRangeWithStatus() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, status incorrect
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "evt_event.event_status_cd", "POCLOSED" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, status correct
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "evt_event.event_status_cd", "POOPEN" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByDateRangeWithVendor() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, vendor incorrect
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "org_vendor.vendor_cd", "TEST-VENDOR" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, vendor correct
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "org_vendor.vendor_cd", "VENDOR1" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByDateRangeWithPriority() throws ParseException {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, priority incorrect
      SimpleDateFormat lDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      Date lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      Date lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "po_header.req_priority_cd", "CRITICAL" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // Reset the arguments.
      dataSetArgument = getDefaultDataSetArgument();

      // add search criteria, date inside range, priority correct
      lPromisedAfter = lDateFormat.parse( "2018-06-20 18:00:00" );
      lPromisedBefore = lDateFormat.parse( "2018-07-01 18:00:00" );
      dataSetArgument.addWhereAfter( "po_line.promise_by_dt", lPromisedAfter );
      dataSetArgument.addWhereBefore( "po_line.promise_by_dt", lPromisedBefore );
      dataSetArgument.addWhereEquals( "po_header.req_priority_cd", "NORMAL" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   /**
    * asserts the data within the returned row, it should match from POSearchTest.xml file data
    *
    */
   private void assertReturnedData( DataSet dataSet ) {

      // assert 1 row was returned
      assertEquals( "Number of retrieved rows", 1, dataSet.getRowCount() );
      dataSet.next();

      // assert data in the returned row
      assertEquals( "po_key", "4650:100", dataSet.getString( "po_key" ) );
      assertEquals( "po_type_cd", "PURCHASE", dataSet.getString( "po_type_cd" ) );
      assertEquals( "created_org_key", "0:1", dataSet.getString( "created_org_key" ) );
      assertEquals( "created_org_cd_name", "MXI (Mxi Technologies)",
            dataSet.getString( "created_org_cd_name" ) );
      assertEquals( "user_status_cd", "OPEN", dataSet.getString( "user_status_cd" ) );
      assertEquals( "vendor_cd", "VENDOR1", dataSet.getString( "vendor_cd" ) );
      assertEquals( "vendor_name", "VENDOR1", dataSet.getString( "vendor_name" ) );
      assertEquals( "vendor_key", "4650:1000", dataSet.getString( "vendor_key" ) );
      assertEquals( "total_price_qt", "0", dataSet.getString( "total_price_qt" ) );
      assertEquals( "currency_cd", "USD", dataSet.getString( "currency_cd" ) );
      assertEquals( "sub_units_qt", "2", dataSet.getString( "sub_units_qt" ) );
      assertEquals( "req_priority_cd", "NORMAL", dataSet.getString( "req_priority_cd" ) );
      assertEquals( "priority_ord", "100", dataSet.getString( "priority_ord" ) );
      assertEquals( "ext_key_sdesc", "ext-ref", dataSet.getString( "ext_key_sdesc" ) );
      assertEquals( "user_id,", USERID, dataSet.getInt( "user_id" ) );
      assertEquals( "full_name", "test test", dataSet.getString( "full_name" ) );
      assertEquals( "auth_status", "PENDING", dataSet.getString( "auth_status" ) );
   }


   /**
    * construct the dataset arguments
    *
    * @return
    */
   private DataSetArgument getDefaultDataSetArgument() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aRowNum", 100 ); // 0 indicates return all rows
      return dataSetArgument;
   }


   /**
    * assert that no data was returned by the query
    *
    */
   private void assertNoDataReturned() {
      assertEquals( "Number of retrieved rows", 0, dataSet.getRowCount() );
   }


   /**
    * Execute the query.
    */
   private void execute( DataSetArgument dataSetArgument ) {
      dataSet = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), dataSetArgument );
   }

}
