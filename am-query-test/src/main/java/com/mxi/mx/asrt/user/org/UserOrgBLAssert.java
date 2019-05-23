
package com.mxi.mx.asrt.user.org;

import org.junit.Assert;

import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.model.user.UserOrgs;


/**
 * DOCUMENT_ME
 *
 * @author asmolko
 */
public class UserOrgBLAssert {

   /**
    * DOCUMENT_ME
    *
    * @param aUserOrgs
    *           DOCUMENT_ME
    * @param aExpectedOrg
    *           DOCUMENT_ME
    */
   public static void assertIsPrimary( UserOrgs aUserOrgs, OrgKey... aExpectedOrg ) {

      for ( OrgKey lExpectedPrimaryOrgKey : aExpectedOrg ) {
         if ( aUserOrgs.isPrimary( lExpectedPrimaryOrgKey ) ) {
            return;
         }
      }

      Assert.fail( "List of expected primary organizations does not include the primary org: "
            + aUserOrgs.getPrimaryOrganization().getOrgKey() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgs
    *           DOCUMENT_ME
    * @param aExpectedOrg
    *           DOCUMENT_ME
    */
   public static void assertUserOrgs( Organization[] aOrgs, OrgKey aExpectedOrg ) {
      for ( Organization lLoopOrg : aOrgs ) {
         if ( lLoopOrg.getOrgKey().equals( aExpectedOrg ) ) {
            return;
         }
      }

      Assert.fail( "Expected organization is not in the list" );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aOrgs
    *           DOCUMENT_ME
    * @param aExpectedOrg
    *           DOCUMENT_ME
    */
   public static void assertUserOrgs( Organization[] aOrgs, Organization aExpectedOrg ) {
      assertUserOrgs( aOrgs, aExpectedOrg.getOrgKey() );
   }
}
