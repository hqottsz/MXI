
package com.mxi.mx.core.unittest.org;

import static org.junit.Assert.fail;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.cache.CacheFactory;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.services.location.CreateLocationTO;
import com.mxi.mx.core.services.location.LocationService;
import com.mxi.mx.core.services.org.AssignedUsersException;
import com.mxi.mx.core.services.org.DependentSubOrganizationsException;
import com.mxi.mx.core.services.org.DependentTaskDefnException;
import com.mxi.mx.core.services.org.IllegalOrgDeleteException;
import com.mxi.mx.core.services.org.OperatorTO;
import com.mxi.mx.core.services.org.OrganizationHasLocationException;
import com.mxi.mx.core.services.org.RestrictedUsersException;
import com.mxi.mx.core.unittest.MxTestUtils;
import com.mxi.mx.core.unittest.MxUnittestDao;
import com.mxi.mx.core.unittest.api.org.OrganizationServiceTestDelegate;
import com.mxi.mx.core.unittest.user.UserServiceTestDelegate;
import com.mxi.mx.testing.mock.cache.MockCacheFactory;


/**
 * Tests the delete organization method
 *
 * @author jduan
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrgDeleteTest {

   private Organization iNewOrg;
   private OrganizationServiceTestDelegate iOrgService;
   private UserServiceTestDelegate iUserService;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests to see if an organization not linked with operator or locatons can be deleted.
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrg() throws Exception {

      // Create the testing organization with the operator type and an address
      OrgKey lNewOrg = iNewOrg.getOrgKey();

      // Test that the row is initially in the org_org table
      Assert.assertNotNull( lNewOrg );

      try {

         // Delete the organization
         iOrgService.delete( lNewOrg );
      } catch ( Exception e ) {
         fail( "Delete organization has failed" );
      }
   }


   /**
    * Tests the delete method when the organization has assigned users.
    *
    * @throws AssignedUsersException
    *            if the organization has assigned users.
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithAssignedUsers() throws AssignedUsersException, Exception {

      // Create the testing organization with the operator type and an address
      OrgKey lNewOrg = iNewOrg.getOrgKey();

      // Test that the row is initially in the org_org table
      Assert.assertNotNull( lNewOrg );

      // Create a new user and assign the user to the organization
      iUserService.createUser( 2, "Parker", lNewOrg, true );

      try {

         // Delete the organization
         iOrgService.delete( lNewOrg );
         fail( "AssignedUsersException was expected" );
      } catch ( AssignedUsersException lException ) {
         ;
      }
   }


   /**
    * Tests the delete method when the organization has the location associated with it
    *
    * @throws OrganizationHasLocationException
    *            DOCUMENT_ME
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithAssociatedLocation()
         throws OrganizationHasLocationException, Exception {

      CacheFactory
            .setInstance( new MockCacheFactory( new MockCacheFactory.MockTimeZoneCacheLoader() ) );

      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      // Create the new organization
      OrgKey lNewOrg =
            iOrgService.createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.OPERATOR ).getOrgKey();

      // Create a new dock
      CreateLocationTO lCreateLocationTO = new CreateLocationTO();
      lCreateLocationTO.setCode( "NA/CA/ATL/DOCK", "aCode" );
      lCreateLocationTO.setName( "Dock Location", "aName" );
      lCreateLocationTO.setTypeCode( "DOCK", "aTypeCode" );
      lCreateLocationTO.setOrgKey( lNewOrg );

      LocationService.create( lCreateLocationTO );

      try {

         // Delete the organization
         iOrgService.delete( lNewOrg );
         fail( "OrganizationHasLocationException was expected" );
      } catch ( OrganizationHasLocationException lException ) {
         ;
      }
   }


   /**
    * Tests the delete method when the organization has the operator associated with it
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithAssociatedOperator() throws Exception {

      OrgKey lOrgKey = OrgKey.ADMIN;

      // Set the code and name to the maximum string length
      String lCode = MxTestUtils.generateString( 8 );
      String lName = MxTestUtils.generateString( 40 );

      OperatorTO lOperatorTO = new OperatorTO();
      lOperatorTO.setCode( "test" );
      lOperatorTO.setIATACode( "tt" );
      lOperatorTO.setICAOCode( "tt" );

      // Create the new organization
      OrgKey lNewOrg = iOrgService
            .createOrg( lOrgKey, lCode, lName, RefOrgTypeKey.OPERATOR, null, null, lOperatorTO )
            .getOrgKey();

      // Delete the organization
      iOrgService.delete( lNewOrg );
   }


   /**
    * Tests the delete method when the organization has sub-organizations.
    *
    * @throws DependentSubOrganizationsException
    *            if the organization has sub-organizations.
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithChild() throws DependentSubOrganizationsException, Exception {

      // Create the testing organization with the operator type and an address
      OrgKey lNewOrg = iNewOrg.getOrgKey();

      // Create a department, HR, within the organization
      String lDeptCode = "HR";
      String lDeptName = "Human Resources";

      // Create a department type sub-organization for the organization
      iOrgService.createOrg( lNewOrg, lDeptCode, lDeptName, RefOrgTypeKey.DEPT );

      // Asserts that the organization is actually created.
      Assert.assertNotNull( lNewOrg );

      try {

         // Delete the organization
         iOrgService.delete( lNewOrg );
         fail( "DependentSubOrganizationsException was expected" );
      } catch ( DependentSubOrganizationsException lException ) {
         ;
      }
   }


   /**
    * Tests the delete method when the organization has restricted users
    *
    * @throws RestrictedUsersException
    *            if the organization has restricted users.
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithRestrictedUsers() throws RestrictedUsersException, Exception {
      // Create the testing organization with the operator type

      OrgKey lOrgKey = iNewOrg.getOrgKey();

      // Create a new user and assign the user to this company's default organization
      iUserService.createUser( 1, "Paruyr", iNewOrg.getCompanyDefaultOrg().getOrgKey(), true );
      try {

         // Delete the organization
         iOrgService.delete( lOrgKey );
         fail( "RestrictedUsersException was expected" );
      } catch ( RestrictedUsersException lException ) {
         ;
      }
   }


   /**
    * Tests the delete method when the organization has the admin type
    *
    * @throws IllegalOrgDeleteException
    *            DOCUMENT_ME
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithTypeAdmin() throws IllegalOrgDeleteException, Exception {

      OrgKey lOrgKey = OrgKey.ADMIN;

      try {

         // Delete the organization
         iOrgService.delete( lOrgKey );
         fail( "IllegalOrgDeleteException was expected" );
      } catch ( IllegalOrgDeleteException lException ) {
         ;
      }
   }


   /**
    * Tests the delete method when the organization has the default type
    *
    * @throws IllegalOrgDeleteException
    *            DOCUMENT_ME
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDeleteOrgWithTypeDefault() throws IllegalOrgDeleteException, Exception {

      OrgKey lOrgKey = OrgKey.DEFAULT;

      try {

         // Delete the organization
         iOrgService.delete( lOrgKey );
         fail( "IllegalOrgDeleteException was expected" );
      } catch ( IllegalOrgDeleteException lException ) {
         ;
      }
   }


   /**
    * Test DependentTaskDefnException is thrown
    *
    * @throws Exception
    *            if an error occurs.
    */
   @Test
   public void testDependentTaskDefnException() throws Exception {

      OrgKey lOrgKey = iNewOrg.getOrgKey();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lOrgKey, "org_db_id", "org_id" );
      lArgs.add( new TaskTaskKey( 1234, 4321 ), "task_db_id", "task_id" );
      new MxUnittestDao( iDatabaseConnectionRule.getConnection() ).executeInsert( "TASK_TASK",
            lArgs );

      try {

         // Delete the organization
         iOrgService.delete( lOrgKey );
         fail( "DependentTaskDefnException was expected" );
      } catch ( DependentTaskDefnException lException ) {
         ;
      }
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
      iNewOrg = iOrgService.createOrg( OrgKey.ADMIN, "AC", "Air Canada", RefOrgTypeKey.OPERATOR );
   }

}
