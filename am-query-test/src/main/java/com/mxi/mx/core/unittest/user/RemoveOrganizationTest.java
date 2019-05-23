
package com.mxi.mx.core.unittest.user;

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
import com.mxi.mx.core.dao.user.UserDao;
import com.mxi.mx.core.dao.user.UserDaoImpl;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.model.user.UserOrgs;
import com.mxi.mx.core.services.user.org.CannotRemoveDefaultOrganizationException;
import com.mxi.mx.core.services.user.org.UserOrgServiceImpl;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;


/**
 * This calss ensures rthat removeOrganizationanization() method works as expected
 *
 * @author phovakimyan
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RemoveOrganizationTest {

   private Organization iNonPrimaryCompany;

   private Organization iNonPrimaryCompanyCrew;

   private OrganizationServiceTestDelegate iOrgService;

   private Organization iPrimaryCompany;

   private Organization iPrimaryCompanyDept;
   private UserDao iUserDao;
   private UserOrgServiceImpl iUserOrgService;
   private UserServiceTestDelegate iUserService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * If we have a user and that user ends up unassigned from two companies, he is actually assigned
    * to two DEFAULT sub-orgs under those companies. One of them will be marked as Primary. So if we
    * try to unassign user from both of them we should not be allowed, as any user must be assigned
    * to at least one organization.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUnassignUserFromAllDefaultOrganizations() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to default organizations marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr",
            iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), false );

      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertEquals( 2, lOrgs.length );

      try {

         // Execute the method under test
         iUserOrgService.removeOrganization( lNewHrKey,
               new OrgKey[] { iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(),
                     iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey() } );
         fail( "User cannot be unassigned from all organizations" );
      } catch ( CannotRemoveDefaultOrganizationException lEx ) {
         // Expected.
      }
   }


   /**
    * User is assigned to two sub-org under two different companies. After the user is unassigned in
    * one shot from both sub-orgs, he is assigned to the companies and the company of the primary
    * sub-org becomes primary company
    *
    * @throws Exception
    */
   @Test
   public void testUnassignUserFromAllSubOrgIncludingPrimary() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey =
            iUserService.createUser( 1, "Paruyr", iPrimaryCompanyDept.getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey, iNonPrimaryCompanyCrew.getOrgKey(), false );
      try {

         // Test the method
         iUserOrgService.removeOrganization( lNewHrKey, new OrgKey[] {
               iPrimaryCompanyDept.getOrgKey(), iNonPrimaryCompanyCrew.getOrgKey() } );
      } catch ( Exception lEx ) {
         fail( "The primary org can be removed when there is no more sub-orgs left" );
      }

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull(
            "After sub-organizations unassigned, expected to have the user assigned to default sub-org of companies ",
            lOrgs );
      Assert.assertEquals( 2, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iPrimaryCompany.getCompanyDefaultOrg().getOrgKey() );
      UserOrgBLAssert.assertUserOrgs( lOrgs,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey() );
      Assert.assertTrue( lUserOrgs.isPrimary( iPrimaryCompany.getCompanyDefaultOrg() ) );
   }


   /**
    * User is assigned to two sub-orgs within one company. One of the sub-orgs marked as primary. We
    * can unassing this user from both sub-orgs, but he will remain assigned to the DEFAULT org of
    * this company and it will be marked Primary.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUnassignUserFromAllSubOrgInOneCompany() throws Exception {

      // Prepare Test Data
      Organization lNonPrimaryCompanyDept = iOrgService.createOrg( iNonPrimaryCompany.getOrgKey(),
            "WESTDEPT", "WESTJET Dept", RefOrgTypeKey.DEPT );

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey =
            iUserService.createUser( 1, "Paruyr", iNonPrimaryCompanyCrew.getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey, lNonPrimaryCompanyDept.getOrgKey(), false );

      // Test the method
      iUserOrgService.removeOrganization( lNewHrKey, new OrgKey[] {
            iNonPrimaryCompanyCrew.getOrgKey(), lNonPrimaryCompanyDept.getOrgKey() } );

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull(
            "After one sub-organization unassigned, expected to have the user assigned to another one ",
            lOrgs );
      Assert.assertEquals( 1, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey() );
      Assert.assertTrue( lUserOrgs.isPrimary( iNonPrimaryCompany.getCompanyDefaultOrg() ) );
   }


   /**
    * Have a user assigned to two default orgs one of them is marked as primary. Unassign the user
    * from non-primary org, make sure that the user is assigned only to one org as result. And that
    * org is still primary.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUnassignUserFromNonPrimaryDefaultOrg() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to default organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr",
            iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), false );

      // Test the method
      iUserOrgService.removeOrganization( lNewHrKey,
            new OrgKey[] { iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey() } );

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull(
            "After one sub-orgfanization unassigned, expected to have the user assigned to another one only",
            lOrgs );
      Assert.assertEquals( 1, lOrgs.length );
      Assert.assertEquals( iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(),
            lOrgs[0].getOrgKey() );
      Assert.assertTrue( iPrimaryCompany.getCompanyDefaultOrg().isDefault() );
   }


   /**
    * User assigned to a a primary sub org and non primary suborg in two different companies.
    * Unassign user from non-primary sub-org, expect to see user still assigned to two sub-orgs the
    * primary sub-org and non-primary DEFAULT org.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUnassignUserFromNonPrimarySubOrg() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey =
            iUserService.createUser( 1, "Paruyr", iPrimaryCompanyDept.getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey, iNonPrimaryCompanyCrew.getOrgKey(), false );

      // Test the method
      iUserOrgService.removeOrganization( lNewHrKey,
            new OrgKey[] { iNonPrimaryCompanyCrew.getOrgKey() } );

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull(
            "After one sub-organization unassigned, expected to have the user assigned to another one ",
            lOrgs );
      Assert.assertEquals( 2, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iPrimaryCompanyDept.getOrgKey() );
      UserOrgBLAssert.assertUserOrgs( lOrgs,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey() );
      Assert.assertTrue( lUserOrgs.isPrimary( iPrimaryCompanyDept ) );
   }


   /**
    * If we have a user and that user ends up unassigned from two companies, he is actually assigned
    * to two DEFAULT sub-orgs under those companies. One of them will be marked as Primary.Unassign
    * user from primary default organization, expect that the other organization will remain
    * assigned to user and will be marked as primary
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUnassignUserFromPrimaryDefaultOrg() throws Exception {
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr",
            iPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey(), false );

      // Test the method
      iUserOrgService.removeOrganization( lNewHrKey,
            new OrgKey[] { iPrimaryCompany.getCompanyDefaultOrg().getOrgKey() } );

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull(
            "After one sub-organization unassigned, expected to have the user assigned to another one ",
            lOrgs );
      Assert.assertEquals( 1, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs,
            iNonPrimaryCompany.getCompanyDefaultOrg().getOrgKey() );
      Assert.assertTrue( lUserOrgs.isPrimary( iNonPrimaryCompany.getCompanyDefaultOrg() ) );
   }


   /**
    * Unassigning user from primary sub-organization of a company is not allowed. User must select
    * new primary org first
    *
    * @throws Exception
    */
   @Test
   public void testUnassignUserFromPrimarySubOrg() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey =
            iUserService.createUser( 1, "Paruyr", iPrimaryCompanyDept.getOrgKey(), true );
      iUserDao.addUserOrganization( lNewHrKey, iNonPrimaryCompanyCrew.getOrgKey(), false );
      try {

         // Test the method
         iUserOrgService.removeOrganization( lNewHrKey,
               new OrgKey[] { iPrimaryCompanyDept.getOrgKey() } );
         fail( "The primary org cannot be unassigned" );
      } catch ( Exception lEx ) {
         // Exception is expected
      }
   }


   /**
    * Have a user assigned to default org of the root. Attempt to unassign the user should get an
    * exception. Such user can only be deleted, not unassigned.
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUnassignUserFromRootDefaultOrg() throws Exception {

      // Prepare Test Data

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr", OrgKey.DEFAULT, true );
      try {

         // Test the method
         iUserOrgService.removeOrganization( lNewHrKey, new OrgKey[] { OrgKey.DEFAULT } );
         fail( "The last org cannot be unassigned" );
      } catch ( CannotRemoveDefaultOrganizationException lEx ) {
         // Exception is expected
      }

      // Make sure that user is still assigned to the default org of the root organization
      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Still assigned to the root's default", lOrgs );
      Assert.assertEquals( 1, lOrgs.length );
      Assert.assertEquals( OrgKey.DEFAULT, lOrgs[0].getOrgKey() );
   }


   /**
    * Constructor for the test case.
    *
    * @exception Exception
    *               if an unexpected error occurs.
    */
   @Before
   public void setUp() throws Exception {
      iUserDao = new UserDaoImpl();
      iOrgService = new OrganizationServiceTestDelegate();
      iUserService = new UserServiceTestDelegate();
      iUserOrgService = new UserOrgServiceImpl();

      // Create two companies under ADMIN
      iPrimaryCompany =
            iOrgService.createOrg( OrgKey.ADMIN, "EASYJET", "EASYJET", RefOrgTypeKey.MRO );
      iPrimaryCompanyDept = iOrgService.createOrg( iPrimaryCompany.getOrgKey(), "EASYDEPT",
            "EASYJET Dept", RefOrgTypeKey.DEPT );
      iNonPrimaryCompany =
            iOrgService.createOrg( OrgKey.ADMIN, "WESTJET", "WESTJET", RefOrgTypeKey.OPERATOR );
      iNonPrimaryCompanyCrew = iOrgService.createOrg( iNonPrimaryCompany.getOrgKey(), "WESTCRW",
            "WESTJET Crew", RefOrgTypeKey.CREW );
   }

}
