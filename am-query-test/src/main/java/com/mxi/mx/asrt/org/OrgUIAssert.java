
package com.mxi.mx.asrt.org;

import java.util.List;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.ArrayUtils;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.user.UserDetails;


/**
 * DOCUMENT_ME
 *
 * @author asmolko
 */
public class OrgUIAssert {

   /**
    * Makes sure that ADMIN user sees the organization with the specified code on User Details and
    * User Details->Organization tab
    *
    * @param aCode
    *           Code of the org that we would like to see
    */
   public static void assertAdminTopLevelOrg( String aCode ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( UserKey.ADMIN, "aUserId" );

      QuerySet lDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.user.OrganizationsByUser", lArgs );

      Assert.assertEquals( "Expected to see only one organization assigned", 1, lDs.getRowCount() );

      lDs.next();
      Assert.assertEquals( aCode, lDs.getString( "user_org_name" ) );

      UserDetails lUserDetails = new UserDetails( UserKey.ADMIN );
      Assert.assertEquals( OrgKey.ADMIN, lUserDetails.getUserDefaultOrganizationKey() );
   }


   /**
    * Asserts that the specified organization has the specified list of sub orgs on Organization
    * Details->SubOrganizationTab page
    *
    * @param aOrgKey
    *           organization
    * @param aCode
    *           lis of sub-organizations
    */
   public static void assertSubOrgs( OrgKey aOrgKey, String... aCode ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aOrgKey, "aOrgDbId", "aOrgId" );

      QuerySet lDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.web.query.org.AdditionalOrgDetails", lArgs );

      List<String> lListOrgs = ArrayUtils.asList( aCode );
      Assert.assertEquals( "Expected list: " + lListOrgs + " Actual dataset: " + lDs, aCode.length,
            lDs.getRowCount() );

      lDs.beforeFirst();
      while ( lDs.next() ) {
         String lSubOrgCd = lDs.getString( "sub_org_name" );
         if ( !lListOrgs.remove( lSubOrgCd ) ) {
            Assert.fail( "The list of expected values:" + lListOrgs + " does not contain "
                  + lSubOrgCd + " Here is the actual dataset.\n " + lDs.toString() );
         }
      }
   }
}
