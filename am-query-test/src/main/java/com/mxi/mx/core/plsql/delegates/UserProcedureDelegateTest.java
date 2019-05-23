package com.mxi.mx.core.plsql.delegates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.key.UtlUserRoleKey;
import com.mxi.mx.core.table.utl.UtlUserRole;
import com.mxi.mx.core.table.utl.UtlUserTempRole;


/**
 * This class tests methods in the UserProcedureDelegate.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class UserProcedureDelegateTest {

   private static final int USER_ID = 121;
   private static final UserKey USER_KEY = new UserKey( USER_ID );
   private static final int ROLE1_ID = 20000;
   private static final int ROLE2_ID = 20001;

   private static final UserRole ROLE1_TEMP = new UserRole( ROLE1_ID, true );
   private static final UserRole ROLE1_PERM = new UserRole( ROLE1_ID, false );
   private static final UserRole ROLE2_TEMP = new UserRole( ROLE1_ID, true );

   private static final Date DATE_1;
   private static final Date DATE_2;
   private static final Date DATE_3;
   private static final Date DATE_4;
   private static final Date DATE_5;

   static {
      Calendar lCalendar = new GregorianCalendar();
      // Abstract time definition, using POSIX time
      lCalendar.set( 1970, 0, 1 );
      DATE_1 = lCalendar.getTime();
      lCalendar.set( 1970, 0, 2 );
      DATE_2 = lCalendar.getTime();
      lCalendar.set( 1970, 0, 3 );
      DATE_3 = lCalendar.getTime();
      lCalendar.set( 1970, 8, 4 );
      DATE_4 = lCalendar.getTime();
      lCalendar.set( 1970, 8, 5 );
      DATE_5 = lCalendar.getTime();
      lCalendar.set( 1970, 8, 6 );
   }
   // ~ Instance fields
   // -----------------------------------------------------------------------------

   private UserProcedureDelegate iUserProcedureDelegate;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Preconditions:
    * <ol>
    * <li>One temporary assignment defined for specified time.</li>
    * <li>There is no PERM roles exist in UTL_USER_ROLE.</li>
    * </ol>
    *
    *
    * Effect: There is one role added.
    *
    */
   @Test
   public void scenario1() {

      createActiveUserRole( USER_ID, ROLE1_ID, DATE_2, DATE_3 );

      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertTrue( lRoles.isEmpty() );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_3 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 1, lRoles.size() );
      Assert.assertTrue( lRoles.contains( ROLE1_TEMP ) );
   }


   /**
    * Preconditions:
    * <ol>
    * <li>Temporary assignment is defined for specified time.</li>
    * <li>PERM role exists in UTL_USER_ROLE</li>
    * </ol>
    *
    * Effect: No TEMP role added. PERM role continue to exist.
    *
    */
   @Test
   public void scenario2() {

      createPermUserRole( USER_ID, ROLE1_ID );

      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 1, lRoles.size() );
      Assert.assertTrue( lRoles.contains( ROLE1_PERM ) );

      createActiveUserRole( USER_ID, ROLE1_ID, DATE_2, DATE_3 );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_3 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 1, lRoles.size() );
      Assert.assertTrue( lRoles.contains( ROLE1_PERM ) );
   }


   /**
    * Precondition:
    * <ol>
    * <li>Temporary assignment is defined in the past.</li>
    * <li>There is no PERM roles exist in UTL_USER_ROLE.</li>
    * </ol>
    *
    * Effect: There are no any roles added/exist.
    */
   @Test
   public void scenario3() {

      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );

      createActiveUserRole( USER_ID, ROLE1_ID, DATE_1, DATE_2 );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_3 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );
   }


   /**
    * Preconditions:
    * <ol>
    * <li>Two temporary assignments for two different roles defined for specified time.</li>
    * <li>There is no PERM roles exist in UTL_USER_ROLE.</li>
    * </ol>
    *
    *
    * Effect: There are two roles added.
    *
    */
   @Test
   public void scenario4() {

      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );

      createActiveUserRole( USER_ID, ROLE1_ID, DATE_2, DATE_4 );
      createActiveUserRole( USER_ID, ROLE2_ID, DATE_3, DATE_5 );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_4 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 2, lRoles.size() );
      Assert.assertTrue( lRoles.contains( ROLE1_TEMP ) );
      Assert.assertTrue( lRoles.contains( ROLE2_TEMP ) );
   }


   /**
    * Precondition:
    * <ol>
    * <li>Temporary assignment is defined in the future.</li>
    * <li>There is no PERM roles exist in UTL_USER_ROLE.</li>
    * </ol>
    *
    * Effect: There are no any roles added/exist.
    */
   @Test
   public void scenario5() {

      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );

      createActiveUserRole( USER_ID, ROLE1_ID, DATE_4, DATE_5 );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_3 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );
   }


   /**
    * Precondition:
    * <ol>
    * <li>An active temporary assignment was unassigned.</li>
    * <li>The TEMP role exists in UTL_USER_ROLE.</li>
    * </ol>
    *
    * Effect: The TEMP role is removed from UTL_USER_ROLE.
    */
   @Test
   public void scenario6() {

      createTempUserRole( USER_ID, ROLE1_ID );
      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 1, lRoles.size() );
      Assert.assertTrue( lRoles.contains( ROLE1_TEMP ) );

      createUnassignedUserRole( USER_ID, ROLE1_ID, DATE_2, DATE_4 );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_3 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );
   }


   /**
    * Precondition:
    * <ol>
    * <li>An active temporary assignment was unassigned.</li>
    * <li>The TEMP role does not exist in UTL_USER_ROLE.</li>
    * <li>There is no PERM roles exist in UTL_USER_ROLE.</li>
    * </ol>
    *
    * Effect: The TEMP will not be added to UTL_USER_ROLE.
    */
   @Test
   public void scenario7() {

      Collection<UserRole> lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );

      createUnassignedUserRole( USER_ID, ROLE1_ID, DATE_2, DATE_4 );

      iUserProcedureDelegate.refreshTempRoleAssignment( USER_ID, DATE_3 );

      lRoles = retrieveRolesForUser( USER_KEY );
      Assert.assertEquals( 0, lRoles.size() );
   }


   private void createPermUserRole( int aUserId, int aRoleId ) {
      UtlUserRole lUtlUserRole = UtlUserRole.create( new UtlUserRoleKey( aUserId, aRoleId ) );
      lUtlUserRole.setTemporary( false );
      lUtlUserRole.setUtlId( Table.Util.getDatabaseId() );
      lUtlUserRole.insert();
   }


   private void createTempUserRole( int aUserId, int aRoleId ) {
      UtlUserRole lUtlUserRole = UtlUserRole.create( new UtlUserRoleKey( aUserId, aRoleId ) );
      lUtlUserRole.setTemporary( true );
      lUtlUserRole.setUtlId( Table.Util.getDatabaseId() );
      lUtlUserRole.insert();
   }


   /**
    * Utility method for creation an active user role record.
    *
    * @param aUserId
    * @param aRoleId
    * @param aStartDate
    * @param aEndDate
    */
   private void createActiveUserRole( int aUserId, int aRoleId, Date aStartDate, Date aEndDate ) {
      UtlUserTempRole lUtlUserTempRole = UtlUserTempRole.create();
      lUtlUserTempRole.setUser( aUserId );
      lUtlUserTempRole.setRole( aRoleId );
      lUtlUserTempRole.setStartDt( aStartDate );
      lUtlUserTempRole.setEndDt( aEndDate );
      lUtlUserTempRole.setAssignedBy( aUserId );
      lUtlUserTempRole.setAssignedDt( aStartDate );
      lUtlUserTempRole.setUtlId( Table.Util.getDatabaseId() );
      lUtlUserTempRole.insert();
   }


   /**
    * Utility method for imitating of creation an unassigned user role record.
    *
    * @param aUserId
    * @param aRoleId
    * @param aStartDate
    * @param aEndDate
    */
   private void createUnassignedUserRole( int aUserId, int aRoleId, Date aStartDate,
         Date aEndDate ) {
      UtlUserTempRole lUtlUserTempRole = UtlUserTempRole.create();
      lUtlUserTempRole.setUser( aUserId );
      lUtlUserTempRole.setRole( aRoleId );
      lUtlUserTempRole.setStartDt( aStartDate );
      lUtlUserTempRole.setEndDt( aEndDate );
      lUtlUserTempRole.setAssignedBy( aUserId );
      lUtlUserTempRole.setAssignedDt( aStartDate );
      lUtlUserTempRole.setUnassignedBy( aUserId );
      lUtlUserTempRole.setUnassignedDt( aStartDate );
      lUtlUserTempRole.setUtlId( Table.Util.getDatabaseId() );
      lUtlUserTempRole.insert();
   }


   private Collection<UserRole> retrieveRolesForUser( UserKey aUserKey ) {
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery(
            new String[] { "role_id", "temp_bool" }, "utl_user_role", aUserKey.getPKWhereArg() );
      Collection<UserRole> lRes = new ArrayList<UserRole>();
      while ( lQs.next() ) {

         lRes.add( new UserRole( lQs.getInteger( "role_id" ), lQs.getBoolean( "temp_bool" ) ) );
      }
      return lRes;
   }


   @Before
   public void loadData() throws Exception {
      iUserProcedureDelegate = new UserProcedureDelegate();
   }


   private static final class UserRole {

      int iRoleId;
      boolean iTempBool;


      UserRole(int aRoleId, boolean aTempBool) {
         this.iRoleId = aRoleId;
         this.iTempBool = aTempBool;
      }


      @Override
      public String toString() {
         return iRoleId + "," + iTempBool;
      }


      @Override
      public int hashCode() {
         int lHashCode = 17;
         lHashCode = ( lHashCode * 37 ) + iRoleId;
         lHashCode = ( lHashCode * 37 ) + ( iTempBool ? 1 : 0 );

         return lHashCode;
      }


      @Override
      public boolean equals( Object aObj ) {
         if ( aObj == null )
            return false;
         if ( aObj instanceof UserRole ) {
            UserRole lOther = ( UserRole ) aObj;
            return this.iRoleId == lOther.iRoleId && this.iTempBool == lOther.iTempBool;
         }
         return false;
      }
   }
}
