
package com.mxi.mx.core.unittest.user.org;

import static org.junit.Assert.fail;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.asrt.user.org.UserOrgBLAssert;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.model.user.UserOrgs;
import com.mxi.mx.core.services.org.AdminUserMovementException;
import com.mxi.mx.core.services.org.UserAssignedToOrganizationException;
import com.mxi.mx.core.services.user.org.UserOrgService;
import com.mxi.mx.core.services.user.org.UserOrgServiceImpl;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;
import com.mxi.mx.core.unittest.user.UserServiceTestDelegate;


/**
 * This class ensures that addOrganization() method works as expected
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AddOrganizationTest {

   private Organization iNonPrimaryCompany;

   private OrganizationServiceTestDelegate iOrgService;

   private Organization iPrimaryCompany;
   private UserOrgService iUserOrgService;
   private UserServiceTestDelegate iUserService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * This test ensures that ADMIN cannot change his primary organization
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAdminChangePrimary() throws Exception {

      // Make sure that user is assigned to proper orgs
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( HumanResourceKey.ADMIN );
      Organization[] lOrgs = lUserOrgs.getOrganizations();

      try {
         iUserOrgService.markAsDefaultOrg( HumanResourceKey.ADMIN, lOrgs[0].getOrgKey() );
         fail( "Exception expected" );
      } catch ( AdminUserMovementException lEx ) {
         // should reach here
      }
   }


   /**
    * This Test Ensures that user ADMIN cannot be assigned to other organization
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testAssignAdminToOrg() throws Exception {

      // Prepare Test Data
      Organization lPrimaryCompanyChild = iOrgService.createOrg( iPrimaryCompany.getOrgKey(),
            "Dept1", "Dept1", RefOrgTypeKey.DEPT );

      try {
         iUserOrgService.addOrganization( HumanResourceKey.ADMIN,
               new OrgKey[] { lPrimaryCompanyChild.getOrgKey() } );
         fail( "Exception expected" );
      } catch ( AdminUserMovementException lEx ) {
         // should reach here
      }
   }


   /**
    * This tests ensures that Admin couldn't be unassigned from his ADMIN organization
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUnAssignAdmin() throws Exception {

      // Make sure that user is assigned to proper orgs
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( HumanResourceKey.ADMIN );
      Organization[] lOrgs = lUserOrgs.getOrganizations();

      try {
         iUserOrgService.removeOrganization( HumanResourceKey.ADMIN,
               new OrgKey[] { lOrgs[0].getOrgKey() } );
         fail( "Exception expected" );
      } catch ( AdminUserMovementException lEx ) {
         // should reach here
      }
   }


   /**
    * Tests UserAssignedToOrganizationException when assign a user to an organization but the user
    * is already assigned to that organization.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testUserAssignedToOrganizationException() throws Exception {

      // Create a new user and assign the user to the organization
      HumanResourceKey lNewHrKey =
            iUserService.createUser( 2, "Parker", iPrimaryCompany.getOrgKey(), true );

      try {

         // assign the user to the same organization again
         iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iPrimaryCompany.getOrgKey() } );
         fail( "UserAssignedToOrganizationException was expected" );
      } catch ( UserAssignedToOrganizationException lException ) {
         ;
      }
   }


   /**
    * User assigned to a company, assigning to a sub-org leaves him assigned to the company and adds
    * another sub-org.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testUserInCompanyAssignToSubOrg() throws Exception {

      // Prepare Test Data
      Organization lPrimaryCompanyChild = iOrgService.createOrg( iPrimaryCompany.getOrgKey(),
            "Dept1", "Dept1", RefOrgTypeKey.DEPT );

      // Create the new user and assign this user to primary organization
      HumanResourceKey lNewHrKey =
            iUserService.createUser( 1, "Paruyr", iPrimaryCompany.getOrgKey(), true );

      // add a department under the company
      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { lPrimaryCompanyChild.getOrgKey() } );

      // Make sure that user is assigned to proper orgs
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to some orgs", lOrgs );

      // Actually two
      Assert.assertEquals( 2, lOrgs.length );

      // The company and the department, and the company remains as primary
      UserOrgBLAssert.assertUserOrgs( lOrgs, iPrimaryCompany.getOrgKey() );
      UserOrgBLAssert.assertUserOrgs( lOrgs, lPrimaryCompanyChild.getOrgKey() );
      Assert.assertTrue( lUserOrgs.isPrimary( iPrimaryCompany ) );
   }


   /**
    * User assigned to default org of a company. Assign him to sub-org, he should be unassigned from
    * the company's default and assigned to the sub-org only
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUserInCompanyDefaultAssignToSubOrg() throws Exception {

      // Prepare Test Data
      Organization lPrimaryCompanyChild = iOrgService.createOrg( iPrimaryCompany.getOrgKey(),
            "Dept1", "Dept1", RefOrgTypeKey.DEPT );

      // Create the new user and assign this user to primary organization
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr",
            iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), true );

      // add a department under the company
      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { lPrimaryCompanyChild.getOrgKey() } );

      // Make sure that user is assigned to proper orgs
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to some orgs", lOrgs );

      // Actually just one
      Assert.assertEquals( 1, lOrgs.length );

      // The department only
      Assert.assertEquals( lPrimaryCompanyChild.getOrgKey(), lOrgs[0].getOrgKey() );

      // And it is marked as primary
      lUserOrgs.isPrimary( lOrgs[0] );
   }


   /**
    * USer is assigned to a company, than assign this user to a department in a different company
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUserInCompanyDefaultAssignToSubOrgInDiffCompany() throws Exception {

      // Prepare Test Data
      Organization lNonPrimaryCompanyChild = iOrgService.createOrg( iNonPrimaryCompany.getOrgKey(),
            "Dept1", "Dept1", RefOrgTypeKey.DEPT );

      // Create the new user and assign this user to primary organization
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr",
            iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), true );

      // add a department under the company
      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { lNonPrimaryCompanyChild.getOrgKey() } );

      // Make sure that user is assigned to proper orgs
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to some orgs", lOrgs );

      // Actually two
      Assert.assertEquals( 2, lOrgs.length );

      // the original company's default and the department in different company, the department is
      // the primary
      UserOrgBLAssert.assertUserOrgs( lOrgs, iPrimaryCompany.getCompanyDefaultOrg().getOrgKey() );
      UserOrgBLAssert.assertUserOrgs( lOrgs, lNonPrimaryCompanyChild.getOrgKey() );
      Assert.assertTrue( lUserOrgs.isPrimary( lNonPrimaryCompanyChild.getOrgKey() ) );
   }


   /**
    * Brand new user (assigned to root's default) assign multiple sub-org, one of them will be
    * marked as Primary
    *
    * @throws Exception
    */
   @Test
   public void testUserInRootDefaulAssignToMulltipleOrgs() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr", OrgKey.DEFAULT, true );

      // Test the method
      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { iPrimaryCompany.getOrgKey(), iNonPrimaryCompany.getOrgKey() } );

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to organizations", lOrgs );
      Assert.assertEquals( 3, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs, OrgKey.DEFAULT );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iPrimaryCompany.getOrgKey() );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iNonPrimaryCompany.getOrgKey() );
      UserOrgBLAssert.assertIsPrimary( lUserOrgs, iPrimaryCompany.getOrgKey(),
            iNonPrimaryCompany.getOrgKey() );
   }


   /**
    * Brand new user (assigned to root's default) assign one suborg. It will be marked as primary.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUserInRootDefaultToCompany() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to root's default
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr", OrgKey.DEFAULT, true );

      // Execute the method under test
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iPrimaryCompany.getOrgKey() } );

      //
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "After assigning user to an organization we "
            + "should have some organizations assigned to the user", lOrgs );

      // There should be only one
      Assert.assertEquals( 2, lOrgs.length );

      // And it should be the new org, marked as primary
      UserOrgBLAssert.assertUserOrgs( lOrgs, OrgKey.DEFAULT );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iPrimaryCompany.getOrgKey() );
      UserOrgBLAssert.assertIsPrimary( lUserOrgs, iPrimaryCompany.getOrgKey() );
   }


   /**
    * Constructor for the test case.
    *
    * @exception Exception
    *               if an unexpected error occurs.
    */
   @Before
   public void setUp() throws Exception {
      iOrgService = new OrganizationServiceTestDelegate();
      iUserService = new UserServiceTestDelegate();
      iUserOrgService = new UserOrgServiceImpl();

      // Create two companies under ADMIN
      iPrimaryCompany =
            iOrgService.createOrg( OrgKey.ADMIN, "EASYJET", "EASYJET", RefOrgTypeKey.OPERATOR );

      iNonPrimaryCompany =
            iOrgService.createOrg( OrgKey.ADMIN, "WESTJET", "WESTJET", RefOrgTypeKey.OPERATOR );
   }

}
