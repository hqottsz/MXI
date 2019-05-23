
package com.mxi.mx.core.unittest.lrp.publish;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.dao.lrp.LrpDaoFactoryImpl;
import com.mxi.mx.core.dao.lrp.publish.PublishDao;
import com.mxi.mx.core.dao.lrp.publish.PublishDaoImpl;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LrpEventWorkscopeKey;
import com.mxi.mx.core.key.LrpInvAdhocPlanKey;
import com.mxi.mx.core.key.LrpInvTaskPlanKey;
import com.mxi.mx.core.key.LrpLocAdhocPlanKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.table.lrp.LrpEventWorkscopeTable;
import com.mxi.mx.core.unittest.table.lrp.LrpInvAdhocPlanTable;
import com.mxi.mx.core.unittest.table.lrp.LrpInvTaskPlanTable;
import com.mxi.mx.core.unittest.table.lrp.LrpLocAdhocPlanTable;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;
import com.mxi.mx.model.key.IdKey;
import com.mxi.mx.model.key.IdKeyImpl;


/**
 * This class tests the {@link PublishDaoImpl} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PublishDaoTest {

   private static final HumanResourceKey CURRENT_HUMAN_RESOURCE = new HumanResourceKey( 4650, 1 );

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( 1, "username" );

   private IdKey iLRPPlanKey = null;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * test matching lrp tasks to mx tasks
    */
   @Test
   public void testMatchingLrpTasks() {

      // init check data, the seeder tasks
      LrpEventWorkscopeTable lWksp =
            LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 1 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 2 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 7 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 7 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 9 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 9 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 2 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 5 ) );

      // initialize a publishing DAO
      PublishDao lPublishDao = LrpDaoFactoryImpl.getInstance().getPublishDao();

      // unflag existing Mx data
      lPublishDao.matchLrpTasks( iLRPPlanKey );

      // lrp task chain wksp1>wksp4>wksp5>wksp6 shall match mx task chain sched2>sched14>sched15
      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 4 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 14 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 5 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 15 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 6 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( null );

      // adhoc task chain is not matched
      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 8 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( null );

      // read only aircraft task chain is not matched
      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 11 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( null );

      // read only task defn task chain is not matched
      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 10 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( null );

      // linked lrp tasks are not updated.
      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 1 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 2 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 7 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 7 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 9 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 9 ) );

      lWksp = LrpEventWorkscopeTable.findByPrimaryKey( new LrpEventWorkscopeKey( 4650, 2 ) );
      lWksp.assertExist();
      lWksp.assertSchedStaskKey( new TaskKey( 4650, 5 ) );
   }


   /**
    * test publish adhoc aircraft and location information
    */
   @Test
   public void testPublishAdhoc() {

      // init check data
      LrpInvAdhocPlanTable lInvAdhoc =
            LrpInvAdhocPlanTable.findByPrimaryKey( new LrpInvAdhocPlanKey( 4650, 1 ) );
      lInvAdhoc.assertExist();

      lInvAdhoc = LrpInvAdhocPlanTable.findByPrimaryKey( new LrpInvAdhocPlanKey( 4650, 3 ) );
      lInvAdhoc.assertDoesNotExist();

      LrpLocAdhocPlanTable lLocAdhoc =
            LrpLocAdhocPlanTable.findByPrimaryKey( new LrpLocAdhocPlanKey( 4650, 1 ) );
      lLocAdhoc.assertExist();

      lLocAdhoc = LrpLocAdhocPlanTable.findByPrimaryKey( new LrpLocAdhocPlanKey( 4650, 2 ) );
      lLocAdhoc.assertDoesNotExist();

      // initialize a publishing DAO
      PublishDao lPublishDao = LrpDaoFactoryImpl.getInstance().getPublishDao();

      // publish adhoc aircraft and location information
      lPublishDao.publishAdhoc( iLRPPlanKey );

      // existing aircraft adhoc updated
      lInvAdhoc = LrpInvAdhocPlanTable.findByPrimaryKey( new LrpInvAdhocPlanKey( 4650, 1 ) );
      lInvAdhoc.assertExist();
      lInvAdhoc.assertLrpPlanKey( iLRPPlanKey );

      // new aircraft adhoc published
      lInvAdhoc = LrpInvAdhocPlanTable.findByPrimaryKey( new LrpInvAdhocPlanKey( 4650, 3 ) );
      lInvAdhoc.assertExist();
      lInvAdhoc.assertLrpPlanKey( iLRPPlanKey );

      // existing location adhoc updated
      lLocAdhoc = LrpLocAdhocPlanTable.findByPrimaryKey( new LrpLocAdhocPlanKey( 4650, 1 ) );
      lLocAdhoc.assertExist();
      lLocAdhoc.assertLrpPlanKey( iLRPPlanKey );

      // new location adhoc published.
      lLocAdhoc = LrpLocAdhocPlanTable.findByPrimaryKey( new LrpLocAdhocPlanKey( 4650, 2 ) );
      lLocAdhoc.assertExist();
      lLocAdhoc.assertLrpPlanKey( iLRPPlanKey );
   }


   /**
    * test publish aircraft taskdefn information
    */
   @Test
   public void testPublishAircraftTaskdefn() {

      // init check data
      LrpInvTaskPlanTable lInvTask =
            LrpInvTaskPlanTable.findByPrimaryKey( new LrpInvTaskPlanKey( 4650, 1, 4650, 1 ) );
      lInvTask.assertDoesNotExist();

      lInvTask = LrpInvTaskPlanTable.findByPrimaryKey( new LrpInvTaskPlanKey( 4650, 1, 4650, 2 ) );
      lInvTask.assertDoesNotExist();

      // initialize a publishing DAO
      PublishDao lPublishDao = LrpDaoFactoryImpl.getInstance().getPublishDao();

      // publish aircraft taskdefn information
      lPublishDao.publishAircraftTaskdefn( iLRPPlanKey );

      // assert published
      lInvTask = LrpInvTaskPlanTable.findByPrimaryKey( new LrpInvTaskPlanKey( 4650, 1, 4650, 1 ) );
      lInvTask.assertExist();
      lInvTask.assertLrpPlanKey( iLRPPlanKey );

      // assert not published
      lInvTask = LrpInvTaskPlanTable.findByPrimaryKey( new LrpInvTaskPlanKey( 4650, 1, 4650, 2 ) );
      lInvTask.assertDoesNotExist();

      lInvTask = LrpInvTaskPlanTable.findByPrimaryKey( new LrpInvTaskPlanKey( 4650, 1, 4650, 7 ) );
      lInvTask.assertDoesNotExist();
   }


   /**
    * test flag and unflag lrp bool for mx work packages and tasks
    */
   @Test
   public void testSetLRPFlag() {

      // init check data
      SchedStaskUtil lStask = new SchedStaskUtil( new TaskKey( 4650, 1 ) );
      lStask.assertLrpBool( true );

      lStask = new SchedStaskUtil( new TaskKey( 4650, 2 ) );
      lStask.assertLrpBool( false );

      // initialize a publishing DAO
      PublishDao lPublishDao = LrpDaoFactoryImpl.getInstance().getPublishDao();

      // unflag existing Mx data
      lPublishDao.setLRPFlag( iLRPPlanKey, false );

      // stask 1, 2 will be unflaged
      lStask = new SchedStaskUtil( new TaskKey( 4650, 1 ) );
      lStask.assertLrpBool( false );

      lStask = new SchedStaskUtil( new TaskKey( 4650, 2 ) );
      lStask.assertLrpBool( false );

      // stask 3 will not updated due to event type
      lStask = new SchedStaskUtil( new TaskKey( 4650, 3 ) );
      lStask.assertLrpBool( true );

      // stask 4 will not updated due to inv readonly
      lStask = new SchedStaskUtil( new TaskKey( 4650, 4 ) );
      lStask.assertLrpBool( true );

      // stask 5 will not updated due to taskdefn readonly
      lStask = new SchedStaskUtil( new TaskKey( 4650, 5 ) );
      lStask.assertLrpBool( true );

      // flag existing Mx data
      lStask = new SchedStaskUtil( new TaskKey( 4650, 16 ) );
      lStask.assertLrpBool( false );
      lPublishDao.setLRPFlag( iLRPPlanKey, true );
      // stask 16 will not be updated due to COMMIT status
      lStask.assertLrpBool( false );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iLRPPlanKey = new IdKeyImpl( 4650, 1 );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
