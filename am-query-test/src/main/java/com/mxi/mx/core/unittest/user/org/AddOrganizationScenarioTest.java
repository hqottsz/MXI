
package com.mxi.mx.core.unittest.user.org;

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
import com.mxi.mx.core.services.user.org.UserOrgService;
import com.mxi.mx.core.services.user.org.UserOrgServiceImpl;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;
import com.mxi.mx.core.unittest.user.UserServiceTestDelegate;


/**
 * This class ensures that addOrganization() method works as expected
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AddOrganizationScenarioTest {

   private Organization iCompany1;
   private Organization iCompany1Dept;
   private Organization iCompany2;
   private Organization iCompany2Dept;

   private OrganizationServiceTestDelegate iOrgService;
   private Organization iRootDept;
   private UserOrgService iUserOrgService;
   private UserServiceTestDelegate iUserService;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testUserInDefaultsBulkAssignOrgsScenario() throws Exception {

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr", OrgKey.DEFAULT, true );
      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { iCompany1.getCompanyDefaultOrg().getOrgKey(),
                  iCompany2.getCompanyDefaultOrg().getOrgKey() } );

      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { OrgKey.ADMIN, iRootDept.getOrgKey(), iCompany1.getOrgKey(),
                  iCompany1Dept.getOrgKey(), iCompany2.getOrgKey(), iCompany2Dept.getOrgKey() } );

      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to organizations", lOrgs );

      // Actually to the ones we specifically assigned the user in the test, minus the Root's
      // Default
      Assert.assertEquals( 6, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs, OrgKey.ADMIN );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iRootDept );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany1 );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany1Dept );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany2 );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany2Dept );

      // As root department was assigned first it should be the primary one
      UserOrgBLAssert.assertIsPrimary( lUserOrgs, OrgKey.ADMIN, iRootDept.getOrgKey(),
            iCompany1.getOrgKey(), iCompany1Dept.getOrgKey(), iCompany2.getOrgKey(),
            iCompany2Dept.getOrgKey() );
   }


   /**
    * Start with a user assigned to a root's default. Have an org model, with root ADMIN, a
    * department under it, two companies under that, and one dept under each company. Assign user to
    * each one of them, for companies frst assign to the company than to sub-org
    *
    * <ol>
    * <li>Assign to the root company</li>
    * <li>Assign to department under the root</li>
    * <li>Assign to the first sub-company</li>
    * <li>Assign to the second sub-company</li>
    * <li>Assign to the first sub-company's department</li>
    * <li>Assign to the second sub-company's department</li>
    * <li></li>
    * </ol>
    *
    * @throws Exception
    */
   @Test
   public void testUserInRootDefaultAssignOrgOneByOneScenario() throws Exception {

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr", OrgKey.DEFAULT, true );
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { OrgKey.ADMIN } );
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iRootDept.getOrgKey() } );
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iCompany1.getOrgKey() } );
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iCompany1Dept.getOrgKey() } );
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iCompany2.getOrgKey() } );
      iUserOrgService.addOrganization( lNewHrKey, new OrgKey[] { iCompany2Dept.getOrgKey() } );

      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to organizations", lOrgs );

      // Actually to the ones we specifically assigned the user in the test, minus the Root's
      // Default
      Assert.assertEquals( 6, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs, OrgKey.ADMIN );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iRootDept );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany1 );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany1Dept );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany2 );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany2Dept );

      // As root company was assigned first it should be the primary one
      UserOrgBLAssert.assertIsPrimary( lUserOrgs, OrgKey.ADMIN );
   }


   /**
    * Start with a user assigned to a root's default. Have an org model, with root ADMIN, a
    * department under it, two companies under that, and one dept under each company. Assign user to
    * all of them.
    *
    * <ol>
    * <li>Assign to department under the root</li>
    * <li>Assign to the first sub-company</li>
    * <li>Assign to the second sub-company</li>
    * <li>Assign to the first sub-company's department</li>
    * <li>Assign to the second sub-company's department</li>
    * <li></li>
    * </ol>
    *
    * @throws Exception
    */
   @Test
   public void testUserInRootDefaultBulkAssignOrgsScenario() throws Exception {

      // Create the new user and assign this user to detault organization marking one of them
      // Primary
      HumanResourceKey lNewHrKey = iUserService.createUser( 1, "Paruyr", OrgKey.DEFAULT, true );
      iUserOrgService.addOrganization( lNewHrKey,
            new OrgKey[] { OrgKey.ADMIN, iRootDept.getOrgKey(), iCompany1.getOrgKey(),
                  iCompany1Dept.getOrgKey(), iCompany2.getOrgKey(), iCompany2Dept.getOrgKey() } );

      UserOrgs lUserOrgs = iUserOrgService.getUserOrgs( lNewHrKey );
      Organization[] lOrgs = lUserOrgs.getOrganizations();
      Assert.assertNotNull( "Assigned to organizations", lOrgs );

      // Actually to the ones we specifically assigned the user in the test, minus the Root's
      // Default
      Assert.assertEquals( 6, lOrgs.length );
      UserOrgBLAssert.assertUserOrgs( lOrgs, OrgKey.ADMIN );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iRootDept );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany1 );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany1Dept );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany2 );
      UserOrgBLAssert.assertUserOrgs( lOrgs, iCompany2Dept );

      // As root department was assigned first it should be the primary one
      UserOrgBLAssert.assertIsPrimary( lUserOrgs, OrgKey.ADMIN, iRootDept.getOrgKey(),
            iCompany1.getOrgKey(), iCompany1Dept.getOrgKey(), iCompany2.getOrgKey(),
            iCompany2Dept.getOrgKey() );
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

      iRootDept = iOrgService.createOrg( OrgKey.ADMIN, "ROOTDEPT", "Root Department",
            RefOrgTypeKey.ADMIN );
      iCompany1 = iOrgService.createOrg( iRootDept.getOrgKey(), "EASYJET", "EASYJET",
            RefOrgTypeKey.OPERATOR );
      iCompany1Dept = iOrgService.createOrg( iCompany1.getOrgKey(), "EASYDEPT",
            "EASYJET Department", RefOrgTypeKey.DEPT );

      iCompany2 = iOrgService.createOrg( iRootDept.getOrgKey(), "WESTJET", "WESTJET",
            RefOrgTypeKey.OPERATOR );
      iCompany2Dept = iOrgService.createOrg( iCompany2.getOrgKey(), "WESTDEPT",
            "WESTJET Department", RefOrgTypeKey.DEPT );
   }

}
