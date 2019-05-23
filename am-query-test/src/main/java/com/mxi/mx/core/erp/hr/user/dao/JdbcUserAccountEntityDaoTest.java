
package com.mxi.mx.core.erp.hr.user.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.erp.hr.user.model.UserAccountEntity;
import com.mxi.mx.core.erp.hr.user.model.UserAccountId;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.unittest.table.utl.UtlUser;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.testing.matchers.MxMatchers;


/**
 * This class tests the {@link JdbcUserAccountEntityDao} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class JdbcUserAccountEntityDaoTest {

   private JdbcUserAccountEntityDao iDao;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests that the DAO properly loads the data from the database for multiple users.
    */
   @Test
   public void testThatDaoFindsUsersWithIds() {
      HumanResourceKey lHRKey1 = new HumanResourceDomainBuilder().withUsername( "testuser1" ).build();

      OrgKey lOrgKey = new OrgKey( "0:3" );
      DataSetArgument lOrgOrgArgs = new DataSetArgument();
      lOrgOrgArgs.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org", lOrgOrgArgs );

      DataSetArgument lOrgOrgHrArgs1 = new DataSetArgument();
      lOrgOrgHrArgs1.add( lHRKey1, "hr_db_id", "hr_id" );
      lOrgOrgHrArgs1.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lOrgOrgHrArgs1 );

      OrgHr lOrgHr1 = OrgHr.findByPrimaryKey( lHRKey1 );

      UtlUser lUtlUser1 = new UtlUser( lOrgHr1.getUser() );
      UserAccountId lId1 = new UserAccountId( lUtlUser1.getAlternateKey() );

      HumanResourceKey lHRKey2 = new HumanResourceDomainBuilder().withUsername( "testuser2" ).build();

      DataSetArgument lOrgOrgHrArgs2 = new DataSetArgument();
      lOrgOrgHrArgs2.add( lHRKey2, "hr_db_id", "hr_id" );
      lOrgOrgHrArgs2.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lOrgOrgHrArgs2 );

      OrgHr lOrgHr2 = OrgHr.findByPrimaryKey( lHRKey2 );
      UtlUser lUtlUser2 = new UtlUser( lOrgHr2.getUser() );
      UserAccountId lId2 = new UserAccountId( lUtlUser2.getAlternateKey() );

      Collection<UserAccountId> lIds = new ArrayList<UserAccountId>();
      lIds.add( lId1 );
      lIds.add( lId2 );

      Collection<UserAccountEntity> lUserEntities = iDao.find( lIds ).now();

      for ( UserAccountEntity lUserEntity : lUserEntities ) {
         MxMatchers.assertThat( lUserEntity.getId(), MxMatchers.isOneOf( lId1, lId2 ) );
         MxMatchers.assertThat( lUserEntity.getUsername(),
               MxMatchers.isOneOf( "testuser1", "testuser2" ) );
      }
   }


   /**
    * Tests that the DAO properly loads the data from the database.
    */
   @Test
   public void testThatDaoFindsUserWithId() {
      HumanResourceKey lHRKey = new HumanResourceDomainBuilder().withUsername( "testuser" ).build();

      OrgKey lOrgKey = new OrgKey( "0:3" );
      DataSetArgument lOrgOrgArgs = new DataSetArgument();
      lOrgOrgArgs.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org", lOrgOrgArgs );

      DataSetArgument lOrgOrgHrArgs = new DataSetArgument();
      lOrgOrgHrArgs.add( lHRKey, "hr_db_id", "hr_id" );
      lOrgOrgHrArgs.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lOrgOrgHrArgs );

      OrgHr lOrgHr = OrgHr.findByPrimaryKey( lHRKey );
      UtlUser lUtlUser = new UtlUser( lOrgHr.getUser() );

      UserAccountId lId = new UserAccountId( lUtlUser.getAlternateKey() );
      UserAccountEntity lUserEntity = iDao.find( lId ).now();

      assertEquals( "Id", lId, lUserEntity.getId() );
      assertEquals( "Username", "testuser", lUserEntity.getUsername() );
   }


   /**
    * Tests that the DAO properly returns an empty list when users with given ids do not exist.
    */
   @Test
   public void testThatDaoReturnsEmptyListWhenUserDoesNotExist() {
      Collection<UserAccountId> lIds = new ArrayList<UserAccountId>();
      lIds.add( new UserAccountId( new SequentialUuidGenerator().newUuid() ) );
      lIds.add( new UserAccountId( new SequentialUuidGenerator().newUuid() ) );

      Collection<UserAccountEntity> lUserEntities = iDao.find( lIds ).now();

      assertTrue( "Entity List", lUserEntities.isEmpty() );
   }


   /**
    * Tests that the DAO properly returns null when user with given id does not exist.
    */
   @Test
   public void testThatDaoReturnsNullWhenUserDoesNotExist() {
      UserAccountEntity lUserEntity =
            iDao.find( new UserAccountId( new SequentialUuidGenerator().newUuid() ) ).now();

      assertNull( "User Entity", lUserEntity );
   }


   /**
    * Tests that the DAO properly loads the data from the database for all users.
    */
   @Test
   public void testFindAll() {
      HumanResourceKey lHRKey1 = new HumanResourceDomainBuilder().withUsername( "testuser1" ).build();

      OrgKey lOrgKey = new OrgKey( "0:3" );
      DataSetArgument lOrgOrgArgs = new DataSetArgument();
      lOrgOrgArgs.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org", lOrgOrgArgs );

      DataSetArgument lOrgOrgHrArgs1 = new DataSetArgument();
      lOrgOrgHrArgs1.add( lHRKey1, "hr_db_id", "hr_id" );
      lOrgOrgHrArgs1.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lOrgOrgHrArgs1 );

      OrgHr lOrgHr1 = OrgHr.findByPrimaryKey( lHRKey1 );

      UtlUser lUtlUser1 = new UtlUser( lOrgHr1.getUser() );
      UserAccountId lId1 = new UserAccountId( lUtlUser1.getAlternateKey() );

      HumanResourceKey lHRKey2 = new HumanResourceDomainBuilder().withUsername( "testuser2" ).build();

      DataSetArgument lOrgOrgHrArgs2 = new DataSetArgument();
      lOrgOrgHrArgs2.add( lHRKey2, "hr_db_id", "hr_id" );
      lOrgOrgHrArgs2.add( lOrgKey, "org_db_id", "org_id" );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lOrgOrgHrArgs2 );

      OrgHr lOrgHr2 = OrgHr.findByPrimaryKey( lHRKey2 );
      UtlUser lUtlUser2 = new UtlUser( lOrgHr2.getUser() );
      UserAccountId lId2 = new UserAccountId( lUtlUser2.getAlternateKey() );

      Collection<UserAccountId> lIds = new ArrayList<UserAccountId>();
      lIds.add( lId1 );
      lIds.add( lId2 );

      Collection<UserAccountEntity> lUserEntities = iDao.find( lIds ).now();

      for ( UserAccountEntity lUserEntity : lUserEntities ) {
         MxMatchers.assertThat( lUserEntity.getId(), MxMatchers.isOneOf( lId1, lId2 ) );
         MxMatchers.assertThat( lUserEntity.getUsername(),
               MxMatchers.isOneOf( "testuser1", "testuser2" ) );
      }
   }


   /**
    * Sets up the test case.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Before
   public void setUp() throws Exception {
      iDao = new JdbcUserAccountEntityDao();
   }

}
