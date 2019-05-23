
package com.mxi.mx.core.unittest.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.dao.org.OrganizationDao;
import com.mxi.mx.core.dao.org.OrganizationDaoImpl;
import com.mxi.mx.core.dao.user.UserDao;
import com.mxi.mx.core.dao.user.UserDaoImpl;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.model.organization.Organization;
import com.mxi.mx.core.model.user.UserOrganization;


/**
 * DOCUMENT_ME
 *
 * @author phovakimyan
 */
public class UserServiceTestDelegate {

   private OrganizationDao iOrganizationDao;
   private UserDao iUserDao;


   /**
    * Creates a new OrganizationServiceTestDelegate object.
    */
   public UserServiceTestDelegate() {
      iUserDao = new UserDaoImpl();
      iOrganizationDao = new OrganizationDaoImpl();
   }


   /**
    * DOCUMENT_ME
    *
    * @param aHrKey
    *           the hr key
    * @param aOrgKey
    *           the orgkey
    * @param aDefault
    *           whether or not the organization is of default type
    */
   public void addUserOrganization( HumanResourceKey aHrKey, OrgKey aOrgKey, boolean aDefault ) {
      Organization lOrganization = iOrganizationDao
            .get( iOrganizationDao.get( aOrgKey ).getParentKey() ).getCompanyDefaultOrg();
      if ( ( iUserDao.getUserOrganizations( aHrKey ) != null )
            && !iUserDao.getUserOrganizations( aHrKey ).isEmpty() ) {
         List<Object> lArrayList =
               new ArrayList<Object>( iUserDao.getUserOrganizations( aHrKey ).values() );
         for ( int i = 0; i < lArrayList.size(); i++ ) {
            if ( ( ( UserOrganization ) lArrayList.get( i ) ).getOrganization().toString()
                  .equals( lOrganization.toString() ) ) {
               iUserDao.removeUserOrganization( aHrKey, lOrganization.getOrgKey() );
            }
         }
      }

      iUserDao.addUserOrganization( aHrKey, aOrgKey, true );
   }


   /**
    * Creates a user
    *
    * @param aUserId
    *           the userid
    * @param aUser
    *           the user
    *
    * @return the created user's HumanResource key
    *
    * @throws Exception
    *            when an error occurs
    */
   public HumanResourceKey createUser( int aUserId, String aUser ) throws Exception {

      // Create the user in utl_user
      UserKey lNewUserKey = new UserKey( aUserId );
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lNewUserKey, "user_id" );
      lArgs.add( "first_name", aUser );
      lArgs.add( "last_name", aUser );
      lArgs.add( "username", aUser );
      lArgs.add( "password", "password" );
      lArgs.add( "utl_id", Table.Util.getDatabaseId() );
      MxDataAccess.getInstance().executeInsert( "utl_user", lArgs );

      // Generate a new HumanResourceKey
      HumanResourceKey lNewHrKey = new HumanResourceKey( 1000, aUserId );

      // Create the user in org_hr
      lArgs.clear();
      lArgs.add( lNewHrKey, new String[] { "hr_db_id", "hr_id" } );
      lArgs.add( lNewUserKey, "user_id" );
      lArgs.add( "hr_cd", aUser );

      MxDataAccess.getInstance().executeInsert( "org_hr", lArgs );

      return lNewHrKey;
   }


   /**
    * Creates a user and assigns him/her to an organization
    *
    * @param aUserId
    *           the userid
    * @param aUser
    *           the user
    * @param aOrgToAssign
    *           the OrgKey
    * @param aPrimary
    *           whether or not the organization is primary
    *
    * @return the created user's HumanResource key
    *
    * @throws Exception
    *            when an error occurs
    */
   public HumanResourceKey createUser( int aUserId, String aUser, OrgKey aOrgToAssign,
         boolean aPrimary ) throws Exception {

      // Create the user in utl_user
      UserKey lNewUserKey = new UserKey( aUserId );
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lNewUserKey, "user_id" );
      lArgs.add( "first_name", aUser );
      lArgs.add( "last_name", aUser );
      lArgs.add( "username", aUser );
      lArgs.add( "password", "password" );

      // lArgs.addInteger( "utl_id", MxDbTable.getDatabaseId() );
      MxDataAccess.getInstance().executeInsert( "utl_user", lArgs );

      // Generate a new HumanResourceKey
      HumanResourceKey lNewHrKey = new HumanResourceKey( 1000, aUserId );

      // Create the user in org_hr
      lArgs.clear();
      lArgs.add( lNewHrKey, new String[] { "hr_db_id", "hr_id" } );
      lArgs.add( lNewUserKey, "user_id" );
      lArgs.add( "hr_cd", aUser );

      MxDataAccess.getInstance().executeInsert( "org_hr", lArgs );

      new UserDaoImpl().addUserOrganization( lNewHrKey, aOrgToAssign, aPrimary );

      return lNewHrKey;
   }


   public Map<OrgKey, UserOrganization> getUserOrganizations( HumanResourceKey aHrKey ) {
      return iUserDao.getUserOrganizations( aHrKey );
   }


   /**
    * Marks an organization of a given user's as the default organization
    *
    * @param aHrKey
    *           the HumanResource key
    * @param aOrgKey
    *           the OrgKey
    */
   public void markAsDefaultOrg( HumanResourceKey aHrKey, OrgKey aOrgKey ) {
      iUserDao.markAsDefaultOrg( aHrKey, aOrgKey );
   }


   /**
    * Unassigns a user from an organization
    *
    * @param aHrKey
    *           the HumanResource key
    * @param aOrgKey
    *           the orgkey
    */
   public void removeUserOrganization( HumanResourceKey aHrKey, OrgKey aOrgKey ) {
      iUserDao.removeUserOrganization( aHrKey, aOrgKey );

      if ( ( iUserDao.getUserOrganizations( aHrKey ) != null )
            && iUserDao.getUserOrganizations( aHrKey ).isEmpty() ) {
         iUserDao.addUserOrganization( aHrKey,
               iOrganizationDao.get( iOrganizationDao.get( aOrgKey ).getParentKey() )
                     .getCompanyDefaultOrg().getOrgKey(),
               false );
      }
   }
}
