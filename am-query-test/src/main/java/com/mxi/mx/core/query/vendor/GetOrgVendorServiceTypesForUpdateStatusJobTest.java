
package com.mxi.mx.core.query.vendor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * This test case is used to test GetOrgVendorServiceTypesForUpdateStatusJob.qrx
 *
 * This query is for getting all organization-vendor service type approvals whose status is approved
 * and has an expired approval expiry date or has an expired certificate expiry date after running
 * the job MX_CORE_UPDATEVENDORSTATUS
 */
public class GetOrgVendorServiceTypesForUpdateStatusJobTest {

   @Rule
   public DatabaseConnectionRule iConnection = new DatabaseConnectionRule();


   @Before
   public void loadData() {
      XmlLoader.load( iConnection.getConnection(),
            GetOrgVendorServiceTypesForUpdateStatusJobTest.class,
            "GetOrgVendorServiceTypesForUpdateStatusJobTest.xml" );
   }


   @Test
   public void testGetOrgVendorServiceTypesForUpdateStatusJob() {

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.core.query.vendor.GetOrgVendorServiceTypesForUpdateStatusJob" );

      // Assert for No of Rows
      Assert.assertEquals( "Number of retrieved rows", 8, lQs.getRowCount() );

      // CASE 1: Approval expiry date and certificate expiry date are both expired
      lQs.first();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:100001",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 3: Approval expiry date is still valid but certificate expiry date is expired
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:100003",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 4: Approval expiry date is expired but certificate expiry date is still valid
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:100004",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 5: Approval expiry date is expired and certificate expiry date is blank
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:100005",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 7: Approval expiry date is expired with WARNING status and certificate expiry date is
      // blank
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:100007",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 9: Approval expiry date is expired with WARNING status but certificate expiry date is
      // still valid
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:100009",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 10: Approval expiry date is expired with WARNING status and certificate expiry date is
      // also expired
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:1000010",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 12: Approval expiry date is still valid with WARNING status but certificate expiry
      // date is expired
      lQs.next();
      Assert.assertEquals( "org_vendor_service_type_key", "10:MOD:0:1:4650:1000012",
            lQs.getString( "org_vendor_service_type_key" ) );

      // CASE 2: Approval expiry date and certificate expiry date are both valid
      // CASE 6: Approval expiry date is still valid and certificate expiry date is blank
      // CASE 8: Approval expiry date is valid with WARNING status and certificate expiry date is
      // blank
      // CASE 11: Approval expiry date is still valid with WARNING status and certificate expiry
      // date is also valid
      Assert.assertFalse( lQs.next() );

   }

}
