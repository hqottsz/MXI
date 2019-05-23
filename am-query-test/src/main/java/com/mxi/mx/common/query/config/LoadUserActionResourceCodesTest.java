
package com.mxi.mx.common.query.config;

import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assume.assumeThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.mx.common.cache.config.ActionParameter;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RoleKey;
import com.mxi.mx.core.key.UserKey;


/**
 * Tests the behaviours of the LoadUserActionResourceCodes query:
 *
 * <ol>
 * <li>This query will return all action config parms for the provided user (from the global config
 * parm table).</li>
 * <li>Each parm will be uniquely identified by its name.</li>
 * <li>Global parms will be overwritten by user parms.</li>
 * <li>Global parms will be overwritten by role parms.</li>
 * <li>Global parms will be overwritten by role parms even if the role order is null.</li>
 * <li>Role parms (and thus global parms) will be overwritten by user parms.</li>
 * <li>The overwritten parms for one user will be independent from another user.</li>
 * <li>When user has multiple roles, the role with the lowest order will be used.</li>
 * <li>When user has a role with a null order value, this will be considered the highest order.</li>
 * <li>When user has multiple roles with same lowest order, the role with the lowest role id will be
 * used.</li>
 * <li>When the local DB type is unknown and the parm is not DOPS enabled, the parms will contain
 * default (not global) values.</li>
 * </ol>
 *
 * @author ahogan
 */
@RunWith( Theories.class )
public final class LoadUserActionResourceCodesTest {

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   // The followng parms are configured in the supporting xml file
   public static final RoleKey ROLE_1 = new RoleKey( 1 );
   public static final RoleKey ROLE_2 = new RoleKey( 2 );
   public static final RoleKey ROLE_3 = new RoleKey( 3 );

   // DataPoints for the tests
   @DataPoints
   public static final UserKey[] VALID_USERS = new UserKey[] { new UserKey( 5 ), new UserKey( 4 ) };
   @DataPoint
   public static final UserKey INVALID_USER = new UserKey( 19 );
   @DataPoints
   public static final LocalDbType[] LOCAL_DB_TYPES = LocalDbType.values();

   // Base test values
   private static final String BASE_PARM_NAME = "base_parm";

   private static final ActionParameter BASE_GLOBAL_PARM =
         new ActionParameter( BASE_PARM_NAME, "base_global_parm_value", Boolean.FALSE );

   private static final ActionParameter BASE_DEFAULT_PARM =
         new ActionParameter( BASE_PARM_NAME, "false", Boolean.FALSE );

   private static final ActionParameter BASE_ROLE_PARM =
         new ActionParameter( BASE_PARM_NAME, "base_role_parm_value", Boolean.TRUE );

   private static final ActionParameter ANOTHER_BASE_ROLE_PARM =
         new ActionParameter( BASE_PARM_NAME, "another_base_role_parm_value", Boolean.FALSE );

   private static final ActionParameter YET_ANOTHER_BASE_ROLE_PARM =
         new ActionParameter( BASE_PARM_NAME, "yet_another_base_role_parm_value", Boolean.FALSE );

   private static final ActionParameter BASE_USER_PARM =
         new ActionParameter( BASE_PARM_NAME, "base_user_parm_value", Boolean.FALSE );

   private static final ActionParameter ANOTHER_BASE_USER_PARM =
         new ActionParameter( BASE_PARM_NAME, "another_base_user_parm_value", Boolean.TRUE );

   private static final Integer NULL_ORDER = null;
   private static final Integer LOWEST_ORDER = 0;
   private static final Integer HIGHER_ORDER = 10;


   /**
    * local db type values enum to be used as datapoints
    *
    * @author ahogan
    */

   enum LocalDbType {
      SYS, CUST, UNKNOWN
   }


   private Integer iOrigLocalDbId;

   private QuerySet iQs;


   @Before
   public void setUp() {
      retrieveSetupTestData();
      validatePrerequisiteData();
   }


   /**
    * Base test, tests the following behaviour;<br>
    *
    * <li>This query will return all action config parms for the provided user (from the global
    * config parm table).</li>
    *
    * @param aUser
    *           user datapoint
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testAllGlobalConfigParmsAreReturnedByDefault( UserKey aUser ) throws Exception {
      // intended to test against all user datapoints

      List<ActionParameter> lActualParms = retrieveAllGlobalParms();
      String lParmName;
      ActionParameter lParm;

      updateLocalDbType( LocalDbType.SYS );

      runQuery( aUser );

      Assert.assertEquals( lActualParms.size(), iQs.getRowCount() );

      iQs.first();
      while ( iQs.next() ) {
         lParmName = iQs.getString( "parm_name" );

         // verify the parm is an actual parm
         lParm = buildActionParameter( iQs );

         if ( !lActualParms.contains( lParm ) ) {
            Assert.fail(
                  "LoadUserActionResourceCodes.qrx returned parm name that should not exist; "
                        + lParmName );
         }
      }
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>Each parm will be uniquely identified by its name.</li>
    *
    * @param aUser
    *           user datapoint
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testConfigParmsHaveUniqueNames( UserKey aUser ) throws Exception {
      // intended to test against all user datapoints

      String lParmName;
      Set<String> lParmNames = new HashSet<String>();

      updateLocalDbType( LocalDbType.SYS );

      runQuery( aUser );

      iQs.first();
      while ( iQs.next() ) {
         lParmName = iQs.getString( "parm_name" );

         // verify the parms are all unique
         if ( !lParmNames.add( lParmName ) ) {
            Assert.fail(
                  "LoadUserActionResourceCodes.qrx returned non-unique parm name; " + lParmName );
         }
      }
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>When the local DB type is unknown and the parm is not DOPS enabled, the parms will contain
    * default (not global) values.</li>
    *
    * @param aUser
    *           user datapoint
    */
   @Theory
   @Test
   public void testDefaultParmReturnedWhenLocalDbTypeUnknowAndParmNotDops( UserKey aUser ) {
      updateLocalDbType( LocalDbType.UNKNOWN );
      addGlobalParm( BASE_GLOBAL_PARM );

      addUserParm( VALID_USERS[0], BASE_USER_PARM );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_DEFAULT_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>When user has multiple roles, the role with the lowest order will be used.</li>
    *
    * @param aLocalDbType
    *           local db type datapoint
    */
   @Theory
   @Test
   public void testRoleParmsOfLowestOrderOverwriteGlobalParms( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addRoleParm( ROLE_1, BASE_ROLE_PARM );
      addRoleToUser( ROLE_1, VALID_USERS[0], HIGHER_ORDER );
      addRoleParm( ROLE_2, ANOTHER_BASE_ROLE_PARM );
      addRoleToUser( ROLE_2, VALID_USERS[0], LOWEST_ORDER );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( ANOTHER_BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>Global parms will be overwritten by role parms.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   @Test
   public void testRoleParmsOverwriteGlobalParms( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addRoleToUser( ROLE_1, VALID_USERS[0] );
      addRoleParm( ROLE_1, BASE_ROLE_PARM );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>When user has a role with a null order value, this will be considered the highest
    * order.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   @Test
   public void testRoleParmsWithNullOrderAreConsideredHighestOrder( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addRoleParm( ROLE_1, BASE_ROLE_PARM );
      addRoleToUser( ROLE_1, VALID_USERS[0], HIGHER_ORDER );
      addRoleParm( ROLE_2, ANOTHER_BASE_ROLE_PARM );
      addRoleToUser( ROLE_2, VALID_USERS[0], NULL_ORDER );
      addRoleParm( ROLE_3, YET_ANOTHER_BASE_ROLE_PARM );
      addRoleToUser( ROLE_3, VALID_USERS[0], LOWEST_ORDER );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( YET_ANOTHER_BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( ANOTHER_BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>Global parms will be overwritten by role parms even if the role order is null.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   @Test
   public void testRoleParmsWithNullOrderOverwriteGlobalParms( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addRoleToUser( ROLE_1, VALID_USERS[0], NULL_ORDER );
      addRoleParm( ROLE_1, BASE_ROLE_PARM );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>When user has multiple roles with same lowest order, the role with the lowest role id will
    * be used.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   @Test
   public void testRoleParmsWithSameOrderUseLowestRoleId( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addRoleParm( ROLE_1, BASE_ROLE_PARM );
      addRoleToUser( ROLE_1, VALID_USERS[0], LOWEST_ORDER );
      addRoleParm( ROLE_2, ANOTHER_BASE_ROLE_PARM );
      addRoleToUser( ROLE_2, VALID_USERS[0], LOWEST_ORDER );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( ANOTHER_BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>Global parms will be overwritten by user parms.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   @Test
   public void testUserParmsOverwriteGlobalParms( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addUserParm( VALID_USERS[0], BASE_USER_PARM );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>The overwritten parms for one user will be independent from another user.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   public void
         testUserParmsOverwriteGlobalParmsAndAreIndependentForUsers( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addUserParm( VALID_USERS[0], BASE_USER_PARM );
      addUserParm( VALID_USERS[1], ANOTHER_BASE_USER_PARM );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( ANOTHER_BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );

      runQuery( VALID_USERS[1] );

      Assert.assertTrue( resultsContainParm( ANOTHER_BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Tests the following behaviour;<br>
    *
    * <li>Role parms (and thus global parms) will be overwritten by user parms.</li>
    *
    * @param aLocalDbType
    */
   @Theory
   @Test
   public void testUserParmsOverwriteRoleAndGlobalParms( LocalDbType aLocalDbType ) {
      assumeThat( aLocalDbType, isOneOf( LocalDbType.SYS, LocalDbType.CUST ) );

      updateLocalDbType( aLocalDbType );
      addGlobalParm( BASE_GLOBAL_PARM );

      addRoleToUser( ROLE_1, VALID_USERS[0] );
      addRoleParm( ROLE_1, BASE_ROLE_PARM );
      addUserParm( VALID_USERS[0], BASE_USER_PARM );

      runQuery( VALID_USERS[0] );

      Assert.assertTrue( resultsContainParm( BASE_USER_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_ROLE_PARM ) );
      Assert.assertFalse( resultsContainParm( BASE_GLOBAL_PARM ) );
   }


   /**
    * Adds a the provided global parm into the DB.
    *
    * @param aParm
    */
   private void addGlobalParm( ActionParameter aParm ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "parm_name", aParm.getParmName() );
      lArgs.add( "parm_value", aParm.getParmValue() );
      lArgs.add( "session_auth_bool", aParm.getParmSessionAuth() );

      MxDataAccess.getInstance().executeInsert( "utl_action_config_parm", lArgs );
   }


   /**
    * Adds a the provided role parm record into the DB for the provided role.
    *
    * @param aRole
    * @param aParm
    */
   private void addRoleParm( RoleKey aRole, ActionParameter aParm ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "role_id", aRole.getRoleId() );
      lArgs.add( "parm_name", aParm.getParmName() );
      lArgs.add( "parm_value", aParm.getParmValue() );
      lArgs.add( "session_auth_bool", aParm.getParmSessionAuth() );

      MxDataAccess.getInstance().executeInsert( "utl_action_role_parm", lArgs );
   }


   /**
    * Adds the provided role to the provided user in the DB with a role order of 1.
    *
    * @param aRole
    * @param aUser
    */
   private void addRoleToUser( RoleKey aRole, UserKey aUser ) {

      // set role order to 1
      addRoleToUser( aRole, aUser, 1 );
   }


   /**
    * Adds the provided role to the provided user (using the provided role order) into the DB.
    *
    * @param aRole
    * @param aUser
    * @param aRoleOrder
    */
   private void addRoleToUser( RoleKey aRole, UserKey aUser, Integer aRoleOrder ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "user_id", aUser.getId() );
      lArgs.add( "role_id", aRole.getRoleId() );
      lArgs.add( "role_order", aRoleOrder );

      MxDataAccess.getInstance().executeInsert( "utl_user_role", lArgs );
   }


   /**
    * Adds the provided user parm record into the DB for the provided user.
    *
    * @param aUser
    * @param aParm
    */
   private void addUserParm( UserKey aUser, ActionParameter aParm ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "user_id", aUser.getId() );
      lArgs.add( "parm_name", aParm.getParmName() );
      lArgs.add( "parm_value", aParm.getParmValue() );
      lArgs.add( "session_auth_bool", aParm.getParmSessionAuth() );

      MxDataAccess.getInstance().executeInsert( "utl_action_user_parm", lArgs );
   }


   /**
    * Builds a parm object given a QuerySet. The QuerySet must contain proper values for the
    * following column names; parm_name, parm_value, session_auth_bool
    *
    * @param aQs
    *
    * @return populated ActionParameter object
    */
   private ActionParameter buildActionParameter( QuerySet aQs ) {

      ActionParameter lParm = new ActionParameter( aQs.getString( "parm_name" ),
            aQs.getString( "parm_value" ), aQs.getBooleanObj( "session_auth_bool" ) );

      return lParm;
   }


   /**
    * Retrieves a list of all global parms from the DB.
    *
    * @return list of all global parms
    *
    * @throws Exception
    *            if there is insufficient global parms in the DB to test with
    */
   private List<ActionParameter> retrieveAllGlobalParms() throws Exception {
      List<ActionParameter> lParmNames = new ArrayList<ActionParameter>();
      ActionParameter lParm;

      DataSetArgument lArgs = new DataSetArgument();
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_action_config_parm", lArgs,
            "parm_name", "parm_value", "session_auth_bool" );

      // Ensure sufficient number of parms to test with (at least 2). If this exception is thrown,
      // we should update this test case to add sufficient test data. This check is here in case
      // the unit test DB gets modified in the future.
      if ( lQs.getRowCount() < 2 ) {
         throw new Exception( "Insufficient number of action config parms to test with." );
      }

      while ( lQs.next() ) {
         lParm = buildActionParameter( lQs );
         lParmNames.add( lParm );
      }

      return lParmNames;
   }


   /**
    * Retrieves and stores data needed to setup these tests.
    */
   private void retrieveSetupTestData() {
      DataSetArgument lArgs = new DataSetArgument();
      QuerySet lQs;

      // Retreive the original local db id
      lQs = QuerySetFactory.getInstance().executeQuery( "mim_local_db", lArgs, "db_id" );
      Assert.assertTrue( lQs.next() );
      iOrigLocalDbId = lQs.getInteger( "db_id" );
   }


   /**
    * Executes the LoadUserActionResourceCodes query using the provided user key and stores the
    * resulting QuerySet.
    *
    * @param aUser
    */
   private void runQuery( UserKey aUser ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aUserId", aUser.getId() );

      iQs = QueryExecutor.executeQuery( getClass(), lArgs );
   }


   /**
    * Updates the local db type in the DB with the provided type value.
    *
    * @param aLocalDbType
    */
   private void updateLocalDbType( LocalDbType aLocalDbType ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "db_type_cd", aLocalDbType.toString() );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( "db_id", iOrigLocalDbId );

      MxDataAccess.getInstance().executeUpdate( "mim_db", lArgs, lWhereArgs );
   }


   /**
    * Validates the prerequisite test data.
    */
   private void validatePrerequisiteData() {
      QuerySet lQs;

      // Ensure the invalid user does not exist
      lQs = QuerySetFactory.getInstance().executeQueryTable( INVALID_USER.getTableName(),
            INVALID_USER.getPKWhereArg() );
      Assert.assertFalse( lQs.next() );

      // Ensure the valid users do exist
      lQs = QuerySetFactory.getInstance().executeQueryTable( VALID_USERS[0].getTableName(),
            VALID_USERS[0].getPKWhereArg() );
      Assert.assertTrue( lQs.next() );
   }


   /**
    * Tests whether the current QuerySet contains the provided parm.
    *
    * @param aParm
    *
    * @return true if the current QuerySet contains the provided parm, otherwise false
    */
   private boolean resultsContainParm( ActionParameter aParm ) {
      iQs.first();
      while ( iQs.next() ) {
         if ( aParm.getParmName().equals( iQs.getString( "parm_name" ) )
               && aParm.getParmValue().equals( iQs.getString( "parm_value" ) )
               && aParm.getParmSessionAuth().equals( iQs.getBooleanObj( "session_auth_bool" ) ) ) {
            return true;
         }
      }

      return false;
   }
}
