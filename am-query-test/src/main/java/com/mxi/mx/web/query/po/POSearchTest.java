
package com.mxi.mx.web.query.po;

import static org.junit.Assert.assertEquals;

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


/**
 * This class performs unit testing on the query file with the same package and name.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class POSearchTest {

   private static final int USERID = 1;

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), POSearchTest.class );
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
   public void testSearchByExternalReference() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add external reference search criteria
      dataSetArgument.add( "aExternalReference", "EXTREF1" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change external reference search criteria
      dataSetArgument.add( "aExternalReference", "ext-ref" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );

   }


   @Test
   public void testSearchByPurchaseContact() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria
      dataSetArgument.add( "aContactHr", "mxi" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aContactHr", "testuser" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByAuthorizationStatus() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria
      dataSetArgument.add( "aAuthorizationStatusCd", "BYPASSED" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aAuthorizationStatusCd", "PENDING" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPartNoOem() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria
      dataSetArgument.add( "aOEMPartNo", "PART1" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aOEMPartNo", "TEST123" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByVendor() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      // add search criteria
      dataSetArgument.add( "aVendorCd", "TEST-VENDOR" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aVendorCd", "VENDOR1" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByOrderType() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      dataSetArgument.add( "aType", "BORROW" );

      // Execute the Query
      execute( dataSetArgument );

      // assert no data is returned
      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aType", "PURCHASE" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPriority() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      dataSetArgument.add( "aPriority", "CRITICAL" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aPriority", "NORMAL" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByRequestID() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      dataSetArgument.add( "aRequestID", "R020000" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aRequestID", "R0100001" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByStatus() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      dataSetArgument.add( "aStatus", "POCLOSED" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aStatus", "POOPEN" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPartName() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      dataSetArgument.add( "aPartName", "PART1" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria
      dataSetArgument.add( "aPartName", "TEST123" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );
   }


   @Test
   public void testSearchByPartGroupPurchaseType() {

      DataSetArgument dataSetArgument = getDefaultDataSetArgument();

      dataSetArgument.add( "aPartGroupPurchaseType", "CONSUM" );

      // Execute the Query
      execute( dataSetArgument );

      assertNoDataReturned();

      // change search criteria to OTHER purchase type
      dataSetArgument.add( "aPartGroupPurchaseType", "OTHER" );

      // Execute the Query
      execute( dataSetArgument );

      // assert the returned data
      assertReturnedData( dataSet );

      // change search criteria to ENG purchase type
      dataSetArgument.add( "aPartGroupPurchaseType", "ENG" );

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
      assertEquals( "budget_status", "PENDING", dataSet.getString( "budget_status" ) );
   }


   /**
    * construct the dataset arguments
    *
    * @return
    */
   private DataSetArgument getDefaultDataSetArgument() {
      DataSetArgument dataSetArgument = new DataSetArgument();
      dataSetArgument.add( "aOEMPartNo", ( String ) null );
      dataSetArgument.add( "aUserId", USERID );
      dataSetArgument.add( "aStatus", ( String ) null );
      dataSetArgument.add( "aBarcode", ( String ) null );
      dataSetArgument.add( "aType", ( String ) null );
      dataSetArgument.add( "aPriority", ( String ) null );
      dataSetArgument.add( "aContactHr", ( String ) null );
      dataSetArgument.add( "aVendorCd", ( String ) null );
      dataSetArgument.add( "aRequestID", ( String ) null );
      dataSetArgument.add( "aExternalReference", ( String ) null );
      dataSetArgument.add( "aPartName", ( String ) null );
      dataSetArgument.add( "aHistoric", false );
      dataSetArgument.add( "aNonHistoric", true );
      dataSetArgument.add( "aAuthorizationStatusCd", ( String ) null );
      dataSetArgument.add( "aPoAuthLvlStatusCd", ( String ) null );
      dataSetArgument.add( "aPartGroupPurchaseType", ( String ) null );
      dataSetArgument.add( "aSearchMethod", "STARTS_WITH" );
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
