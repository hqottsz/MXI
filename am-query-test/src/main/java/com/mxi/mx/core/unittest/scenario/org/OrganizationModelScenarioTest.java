
package com.mxi.mx.core.unittest.scenario.org;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.asrt.org.OrgUIAssert;
import com.mxi.mx.common.key.PrimaryKeyService;
import com.mxi.mx.common.key.PrimaryKeyServiceStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.services.org.DuplicateOrganizationCodeException;
import com.mxi.mx.core.services.user.org.UserOrgService;
import com.mxi.mx.core.services.user.org.UserOrgServiceImpl;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;
import com.mxi.mx.core.unittest.user.UserServiceTestDelegate;


/**
 * This class contains scenarios related to Org Model. It by-passess EJB layer, goes straight to
 * service classes for business logic as well as it uses JSP controllers to reteive information that
 * we display on UI for verification purposes. Can be run outside of appserver, as long as unitest
 * query db is accessible.
 *
 * @author asmolko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrganizationModelScenarioTest {

   private Organization iCompany1;
   private Organization iCompany2;

   private OrgKey iEJB787FleetOrgKey;

   private OrgKey iEJOPEROrgKey;
   private OrgKey iFCB757FleetOrgKey;

   private OrgKey iFCOPEROrgKey;
   private OrgKey iGCOPEROrgKey;
   private HumanResourceKey iNewUser;
   private Organization iOrganization1;
   private Organization iOrganization2;
   private OrganizationServiceTestDelegate iOrgService;
   private Organization iRootDept;
   private UserOrgService iUserOrgService;
   private UserServiceTestDelegate iUserService;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Simulates actions of ADMIN user when setting up organization tree on a fresh db
    *
    * <ol>
    * <li>Make sure that ADMIN can see only one organization of type ADMIN</li>
    * <li>Change the ADMIN organization to a meaningful code and name</li>
    * <li>Make sure that those meaningful names reflected on UI</li>
    * <li>Set up org tree with 4 levels</li>
    * <li>Attempt to change a code within the same org to a duplicate code (exception)</li>
    * <li>Attempt to create a new org with a duplicate code within the same company (exception)</li>
    * <li>Change an org code to the same code but in a different company (allowed)</li>
    * <li>Change the org code back to "real" unique code</li>
    * <li>Make sure that org tree can be viewed properly by verifying two levels of the tree</li>
    * </ol>
    *
    * @throws Exception
    *            if something goes wrong
    */
   @Test
   public void testSettingUpOrgTree() throws Exception {

      // Make sure that out-of-the-box Admin user is assigned only to the top level organization
      OrgUIAssert.assertAdminTopLevelOrg( "MXI (Mxi Technologies)" );

      // Rename the ADMIN location to represent real organization
      iOrgService.setCodeName( OrgKey.ADMIN, "GoLdCAre", "GoldCare" );

      // Make sure that those meaningful names reflected on UI
      // ... and that the code has been uppercased.
      OrgUIAssert.assertAdminTopLevelOrg( "GOLDCARE (GoldCare)" );

      // Set up org tree with 4 levels
      setUpOrgTree();

      // Attempt to change a code within the same org to a duplicate code (exception)
      try {
         iOrgService.setCode( iFCB757FleetOrgKey, "B767FL" );
         fail( "Exception expected" );
      } catch ( DuplicateOrganizationCodeException lEx ) {
         // that's expected
      }

      // Attempt to create a new org with a duplicate code within the same company (exception)
      try {
         iOrgService.createOrg( iFCOPEROrgKey, "B767FL", "B767 Fleet", RefOrgTypeKey.DEPT );
         fail( "Exception expected" );
      } catch ( DuplicateOrganizationCodeException lEx ) {
         // that's expected
      }

      // Change an org code to the same code but in a different company (allowed)
      iOrgService.setCode( iEJB787FleetOrgKey, "B767FL" );

      // Change the org code back to "real" unique code
      iOrgService.setCode( iEJB787FleetOrgKey, "B787FL" );

      // Make sure that org tree can be viewed properly by verifying two levels of the tree
      OrgUIAssert.assertSubOrgs( iGCOPEROrgKey, "FCOPER (First Choice)", "EJOPER (EasyJet)" );
      OrgUIAssert.assertSubOrgs( iFCOPEROrgKey, "FCOPER/B757FL (Boeing 757 Fleet)",
            "FCOPER/B767FL (Boeing 767 Fleet)", "FCOPER/B787FL (Boeing 787 Fleet)" );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSettingUpUserOrg() throws Exception {

      // Setting up initial values
      setUpUserOrg();

      // Creating a new user, adding to First Choice company and verifying that it assigned
      // to First Choice company but has no organization
      iNewUser = iUserService.createUser( 1000, "TUser",
            iCompany1.getCompanyDefaultOrg().getOrgKey(), true );
      Assert.assertTrue( iUserOrgService.getUserOrgs( iNewUser ).isAssignedToCompany( iCompany1 ) );
      Assert.assertSame( 1, iUserService.getUserOrganizations( iNewUser ).size() );

      // Creating new users and adding First Choice company to it
      HumanResourceKey lNewUser1 = iUserService.createUser( 1001, "TUser1",
            iCompany1.getCompanyDefaultOrg().getOrgKey(), false );
      HumanResourceKey lNewUser2 = iUserService.createUser( 1002, "TUser2",
            iCompany1.getCompanyDefaultOrg().getOrgKey(), false );
      Assert.assertTrue(
            iUserOrgService.getUserOrgs( lNewUser1 ).isAssignedToCompany( iCompany1 ) );
      Assert.assertTrue(
            iUserOrgService.getUserOrgs( lNewUser2 ).isAssignedToCompany( iCompany1 ) );

      // Getting the list of unassigned users for First Choice company and verifying it's content
      List<HumanResourceKey> lUnassignedUserList =
            iOrgService.getUnassinedUsersList( iCompany1.getOrgKey() );

      Assert.assertTrue( lUnassignedUserList.contains( iNewUser ) );
      Assert.assertTrue( lUnassignedUserList.contains( lNewUser1 ) );
      Assert.assertTrue( lUnassignedUserList.contains( lNewUser1 ) );

      // making Boeing 757 Fleet as default organization of created users
      iUserService.addUserOrganization( iNewUser, iOrganization1.getOrgKey(), true );
      iUserService.addUserOrganization( lNewUser1, iOrganization1.getOrgKey(), true );
      iUserService.addUserOrganization( lNewUser2, iOrganization1.getOrgKey(), true );

      // Getting the list of unassigned users for First Choice company and verifying is it empty
      lUnassignedUserList = iOrgService.getUnassinedUsersList( iCompany1.getOrgKey() );
      Assert.assertTrue( lUnassignedUserList.isEmpty() );

      // Verifying are one of the created users have First Choice and Boeing 757 Fleet
      Assert.assertTrue(
            iUserOrgService.getUserOrgs( lNewUser1 ).isAssignedToCompany( iCompany1 ) );
      Assert.assertTrue(
            iUserOrgService.getUserOrgs( lNewUser1 ).isAssignedToOrg( iOrganization1 ) );

      // making Boeing 757 Fleet as default and verifying
      iUserService.markAsDefaultOrg( iNewUser, iOrganization1.getOrgKey() );
      iUserService.markAsDefaultOrg( lNewUser1, iOrganization1.getOrgKey() );
      iUserService.markAsDefaultOrg( lNewUser2, iOrganization1.getOrgKey() );
      Assert.assertEquals( iOrganization1, iUserOrgService.getUserPrimaryOrg( iNewUser ) );
      Assert.assertEquals( iOrganization1, iUserOrgService.getUserPrimaryOrg( lNewUser1 ) );
      Assert.assertEquals( iOrganization1, iUserOrgService.getUserPrimaryOrg( lNewUser2 ) );

      // removing all orgs for created users and verifying
      iUserService.removeUserOrganization( iNewUser, iOrganization1.getOrgKey() );
      iUserService.removeUserOrganization( lNewUser1, iOrganization1.getOrgKey() );
      iUserService.removeUserOrganization( lNewUser2, iOrganization1.getOrgKey() );
      Assert.assertSame( 1, iUserService.getUserOrganizations( iNewUser ).size() );
      Assert.assertSame( 1, iUserService.getUserOrganizations( lNewUser1 ).size() );
      Assert.assertSame( 1, iUserService.getUserOrganizations( lNewUser2 ).size() );

      // Verify that the user is listed in Unassigned Users list for the First Choice company
      lUnassignedUserList = iOrgService.getUnassinedUsersList( iCompany1.getOrgKey() );
      Assert.assertTrue( lUnassignedUserList.contains( iNewUser ) );
      Assert.assertTrue( lUnassignedUserList.contains( lNewUser1 ) );
      Assert.assertTrue( lUnassignedUserList.contains( lNewUser1 ) );

      // Assigning the user to EasyJet->787 department and verifying
      iUserService.addUserOrganization( iNewUser, iOrganization2.getOrgKey(), true );
      iUserService.addUserOrganization( lNewUser1, iOrganization2.getOrgKey(), true );
      iUserService.addUserOrganization( lNewUser2, iOrganization2.getOrgKey(), true );
      Assert.assertEquals( iOrganization2, iUserOrgService.getUserPrimaryOrg( iNewUser ) );
      Assert.assertEquals( iOrganization2, iUserOrgService.getUserPrimaryOrg( lNewUser1 ) );
      Assert.assertEquals( iOrganization2, iUserOrgService.getUserPrimaryOrg( lNewUser2 ) );
   }


   @Before
   public void setUp() throws Exception {
      iOrgService = new OrganizationServiceTestDelegate();
   }


   /**
    * Creates an org tree with four levels
    *
    * @throws Exception
    *            if something goes wrong
    */
   private void setUpOrgTree() throws Exception {

      iGCOPEROrgKey = iOrgService
            .createOrg( OrgKey.ADMIN, "GCOPERs", "GoldCare Operators", RefOrgTypeKey.ADMIN )
            .getOrgKey();

      iFCOPEROrgKey =
            iOrgService.createOrg( iGCOPEROrgKey, "FCOPER", "First Choice", RefOrgTypeKey.OPERATOR )
                  .getOrgKey();

      iEJOPEROrgKey = iOrgService
            .createOrg( iGCOPEROrgKey, "EJOPER", "EasyJet", RefOrgTypeKey.OPERATOR ).getOrgKey();
      iFCB757FleetOrgKey =
            iOrgService.createOrg( iFCOPEROrgKey, "B757FL", "Boeing 757 Fleet", RefOrgTypeKey.DEPT )
                  .getOrgKey();
      iOrgService.createOrg( iFCOPEROrgKey, "B767FL", "Boeing 767 Fleet", RefOrgTypeKey.DEPT );
      iOrgService.createOrg( iFCOPEROrgKey, "B787FL", "Boeing 787 Fleet", RefOrgTypeKey.DEPT );
      iEJB787FleetOrgKey =
            iOrgService.createOrg( iEJOPEROrgKey, "B787FL", "Boeing 787 Fleet", RefOrgTypeKey.DEPT )
                  .getOrgKey();
   }


   private void setUpUserOrg() throws Exception {
      PrimaryKeyService.setSingleton( new PrimaryKeyServiceStub() );

      iUserService = new UserServiceTestDelegate();
      iUserOrgService = new UserOrgServiceImpl();

      iRootDept = iOrgService.createOrg( OrgKey.ADMIN, "ROOTDEPT", "Root Department",
            RefOrgTypeKey.ADMIN );
      iCompany1 = iOrgService.createOrg( iRootDept.getOrgKey(), "FChoice", "FChoice",
            RefOrgTypeKey.OPERATOR );
      iOrganization1 =
            iOrgService.createOrg( iCompany1.getOrgKey(), "B757", "B757", RefOrgTypeKey.DEPT );

      iCompany2 = iOrgService.createOrg( iRootDept.getOrgKey(), "EasyJet", "EasyJet",
            RefOrgTypeKey.OPERATOR );

      iOrganization2 =
            iOrgService.createOrg( iCompany2.getOrgKey(), "B787", "B787", RefOrgTypeKey.DEPT );
   }

}
