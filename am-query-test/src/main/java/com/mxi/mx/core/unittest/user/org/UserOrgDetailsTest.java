
package com.mxi.mx.core.unittest.user.org;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvalidKeyException;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.services.user.org.UserOrgService;
import com.mxi.mx.core.services.user.org.UserOrgServiceImpl;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;
import com.mxi.mx.core.unittest.user.UserServiceTestDelegate;


/**
 * This class ensures that methods extracting user-organization relationship details work as
 * expected.
 *
 * @author nsubotic
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UserOrgDetailsTest {

   // department organization under ADMIN company
   private Organization iAdminDept;

   // OPERATOR company
   private Organization iOperatorCompany;

   // department organization under OPERATOR company
   private Organization iOperatorDept;

   private OrganizationServiceTestDelegate iOrgService;
   private UserOrgService iUserOrgService;
   private UserServiceTestDelegate iUserService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests that isUserDefaultOrgUnderAdminCompany() method returns true for user that works for
    * department under ADMIN company
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testIsUserUnderAdminCompAdminUser() throws Exception {

      // user that works for department under ADMIN company
      HumanResourceKey lAdminDeptUser =
            iUserService.createUser( 1, "TestUser", iAdminDept.getOrgKey(), true );

      boolean lIsUnderAdmin = iUserOrgService.isUserDefaultOrgUnderAdminCompany( lAdminDeptUser );

      assertEquals( true, lIsUnderAdmin );
   }


   /**
    * Tests that isUserDefaultOrgUnderAdminCompany() method throws exception if user does not exist
    * in database.
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testIsUserUnderAdminNotExistUser() throws Exception {
      HumanResourceKey lDoesNotExistUser = new HumanResourceKey( "1:1" );

      try {

         // user that does not exist in db
         iUserOrgService.isUserDefaultOrgUnderAdminCompany( lDoesNotExistUser );

         // If this line executes, the exception wasn't thrown
         fail( "Expected InvalidKeyException" );
      } catch ( InvalidKeyException ex ) {
         // should reach here
      }
   }


   /**
    * Tests that isUserDefaultOrgUnderAdminCompany() method throws exception if null is passed as
    * human resource key value.
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testIsUserUnderAdminNullUser() throws Exception {

      try {

         // null user
         iUserOrgService.isUserDefaultOrgUnderAdminCompany( null );

         // If this line executes, the exception wasn't thrown
         fail( "Expected InvalidKeyException" );
      } catch ( InvalidKeyException ex ) {

         // should reach here
      }
   }


   /**
    * Tests that isUserDefaultOrgUnderAdminCompany() method returns false for user that works for
    * department under OPERATOR company.
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testIsUserUnderAdminOperUser() throws Exception {

      // user that works for department under OPERATOR company
      HumanResourceKey lOperDeptUser =
            iUserService.createUser( 1, "TestUser", iOperatorDept.getOrgKey(), true );

      boolean lIsUnderAdmin = iUserOrgService.isUserDefaultOrgUnderAdminCompany( lOperDeptUser );

      assertEquals( false, lIsUnderAdmin );
   }


   /**
    * Set up the test data
    *
    * @exception Exception
    *               if an unexpected error occurs.
    */
   @Before
   public void setUp() throws Exception {
      iOrgService = new OrganizationServiceTestDelegate();
      iUserService = new UserServiceTestDelegate();
      iUserOrgService = new UserOrgServiceImpl();

      iAdminDept = iOrgService.createOrg( OrgKey.ADMIN, "ADMDEPT", "ADMIN Department",
            RefOrgTypeKey.DEPT );

      // create an operator company
      iOperatorCompany =
            iOrgService.createOrg( OrgKey.ADMIN, "WESTJET", "WESTJET", RefOrgTypeKey.OPERATOR );

      iOperatorDept = iOrgService.createOrg( iOperatorCompany.getOrgKey(), "WESTDEPT",
            "WESTJET Department", RefOrgTypeKey.DEPT );
   }

}
