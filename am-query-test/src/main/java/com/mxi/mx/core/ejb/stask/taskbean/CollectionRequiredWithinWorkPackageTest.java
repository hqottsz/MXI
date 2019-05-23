package com.mxi.mx.core.ejb.stask.taskbean;

import static com.mxi.am.domain.Domain.createWorkPackage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.status.ScheduleInternalTO;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 *
 * Verifies the behavior of management of collected required in the work scope of a committed Work
 * Package
 *
 */
public class CollectionRequiredWithinWorkPackageTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey AUTHORIZING_HR = new HumanResourceKey( 4650, 235 );
   private final String WORK_ORDER_NO = "WO - 116217";


   /**
    *
    * Verify that when enabled the management of collected tasks in the work scope of a committed
    * work package is marked as true"
    *
    */
   @Test
   public void collectionTaskInTheWorkScopeOfCommittedWPEnabled() throws Exception {

      // Given a work package with Collection Required boolean set to TRUE
      final TaskKey lWorkPackage = createWorkPackage();

      schedule( lWorkPackage, true );

      SchedStaskTable lStaskTable = InjectorContainer.get().getInstance( SchedStaskDao.class )
            .findByPrimaryKey( lWorkPackage );

      // Then Collection Required param of work package set to TRUE
      assertTrue( lStaskTable.isCollectionRequiredBool() );
   }


   /**
    *
    * Create Schedule internal transfer object with collection req boolean value
    *
    * @param aCollecttionReqBool
    * @return Schedule internal transfer object property.
    * @throws Exception
    */
   private void schedule( TaskKey aWorkPackage, boolean aCollectionReqBool ) throws Exception {
      ScheduleInternalTO lTO = new ScheduleInternalTO();
      lTO.setSchedDates( new Date(), new Date() );
      lTO.setWorkOrderNo( WORK_ORDER_NO, "" );
      lTO.setCollectionRequired( aCollectionReqBool );

      STaskBean staskBean = new STaskBean();
      staskBean.setSessionContext( new SessionContextFake() );
      staskBean.schedule( aWorkPackage, lTO, AUTHORIZING_HR );

   }


   /**
    *
    * Verify that when disabled the management of collected tasks in the work scope of a committed
    * work package is marked as false"
    *
    */
   @Test
   public void collectionTaskInTheWorkScopeOfCommittedWPDisabled() throws Exception {

      // Given a work package with Collection Required boolean set to TRUE
      final TaskKey lWorkPackage = createWorkPackage();

      schedule( lWorkPackage, false );

      SchedStaskTable lStaskTable = InjectorContainer.get().getInstance( SchedStaskDao.class )
            .findByPrimaryKey( lWorkPackage );

      // Then Collection Required param of work package set to FALSE
      assertFalse( lStaskTable.isCollectionRequiredBool() );
   }

}
