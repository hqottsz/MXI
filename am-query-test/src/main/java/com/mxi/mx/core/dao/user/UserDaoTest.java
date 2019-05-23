
package com.mxi.mx.core.dao.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.dao.org.OrgTestDataFactory;
import com.mxi.mx.core.dao.org.OrganizationDao;
import com.mxi.mx.core.dao.org.OrganizationDaoImpl;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.model.user.User;
import com.mxi.mx.core.model.user.UserOrganization;
import com.mxi.mx.core.services.user.InvalidUsernameException;


/**
 * Makes sure that the methods on UserDao class work properly
 *
 * @author asmolko
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UserDaoTest {

   Organization org2_3;
   Organization org2_4;
   Organization org2_5;
   Organization org2_6;

   private HumanResourceKey hrKey = new HumanResourceKey( 4650, 149 );
   private OrganizationDao orgDao;
   private OrgKey orgKey2_3 = new OrgKey( 2, 3 );
   private OrgKey orgKey2_4 = new OrgKey( 2, 4 );
   private OrgKey orgKey2_5 = new OrgKey( 2, 5 );
   private OrgKey orgKey2_6 = new OrgKey( 2, 6 );
   private UserDao userDao;
   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() throws Exception {
      userDao = new UserDaoImpl();
      orgDao = new OrganizationDaoImpl();
      hrKey = new HumanResourceKey( 4650, 149 );

      // Create organizations (org_org) table
      org2_3 = OrgTestDataFactory.genOperator( orgKey2_3, OrgKey.ADMIN );
      org2_4 = OrgTestDataFactory.genOperator( orgKey2_4, orgKey2_3 );
      org2_5 = OrgTestDataFactory.genDepartment( orgKey2_5, orgKey2_3 );
      org2_6 = OrgTestDataFactory.genDepartment( orgKey2_6, orgKey2_5 );

      orgDao.create( org2_3 );
      orgDao.create( org2_4 );
      orgDao.create( org2_5 );
      orgDao.create( org2_6 );

      // Assign the organizations to the specified user
      userDao.addUserOrganization( hrKey, orgKey2_3, false );
      userDao.addUserOrganization( hrKey, orgKey2_4, true );
      userDao.addUserOrganization( hrKey, orgKey2_5, false );
      userDao.addUserOrganization( hrKey, orgKey2_6, false );
   }


   /**
    * Makes sure that we can retrieve user organizations and default value is set properly
    */
   @Test
   public void testGetUserOrganizations() {

      Map<OrgKey, UserOrganization> result = userDao.getUserOrganizations( hrKey );
      assertFalse( result.get( orgKey2_3 ).isPrimary() );
      assertTrue( result.get( orgKey2_4 ).isPrimary() );
      assertFalse( result.get( orgKey2_5 ).isPrimary() );
      assertFalse( result.get( orgKey2_6 ).isPrimary() );
   }


   /**
    * MAkes ure that an organization get marked as default properly.
    */
   @Test
   public void testMarkAsDefaultOrg() {
      userDao.markAsDefaultOrg( hrKey, orgKey2_5 );

      Map<OrgKey, UserOrganization> result = userDao.getUserOrganizations( hrKey );
      assertFalse( result.get( orgKey2_3 ).isPrimary() );
      assertFalse( result.get( orgKey2_4 ).isPrimary() );

      // The 2,4 used to be default, it is not default anymore at the same time the 2,5 is default
      assertTrue( result.get( orgKey2_5 ).isPrimary() );
      assertFalse( result.get( orgKey2_6 ).isPrimary() );
   }


   /**
    * Makes sure that we can remove a user organization
    */
   @Test
   public void testRemoveUserOrganization() {
      userDao.removeUserOrganization( hrKey, orgKey2_5 );

      Map<OrgKey, UserOrganization> result = userDao.getUserOrganizations( hrKey );
      assertTrue( result.containsKey( orgKey2_3 ) );
      assertTrue( result.containsKey( orgKey2_4 ) );

      // 2.5 does not exist anymore
      assertFalse( result.containsKey( orgKey2_5 ) );
      assertTrue( result.containsKey( orgKey2_6 ) );
   }


   @Test
   public void testGetUser_System() throws InvalidUsernameException {
      User user = userDao.getUser( "mxsystem" );
      assertEquals( 11, user.getUserKey().getId() );
      assertNull( user.getHrKey() );
      assertEquals( "mxsystem", user.getFirstName() );
      assertEquals( "mxsystem", user.getLastName() );
   }

}
