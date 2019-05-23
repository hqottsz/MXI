package com.mxi.mx.core.ejb.stask.taskbean;

import static com.mxi.am.domain.Domain.createFault;
import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createRequirement;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Integration unit test for method
 * {@link TaskBean#unassignTask(com.mxi.mx.core.key.HumanResourceKey, String, String)}
 *
 */
public class UnassignTaskTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static EvtEventDao evtEventDao = new JdbcEvtEventDao();

   private static final String NA_REASON = null;
   private static final String NA_NOTE = null;


   /**
    *
    * Verify that when a corrective task of a fault is unassigned from a committed work package that
    * it remains in the work scope of that work package but is marked as being unassigned.
    *
    * Note: in this bean layer there is no validation performed as to whether the fault is deferred
    * or not, that validation is done in the servlet layer (i.e. deferred fault may be unassigned).
    * Therefore, it is not relevant whether the fault is deferred or not.
    *
    */
   @Test
   public void unassignedFaultRemainsInWorkScopeButIsMarkedAsUnassigned() throws Exception {

      // Given a fault with a corrective task.
      TaskKey correctiveTask = createRequirement();
      createFault( fault -> {
         fault.setCorrectiveTask( correctiveTask );
      } );

      // Given a committed work package to which the task is assigned (and thus the task is in its
      // work scope).
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.setStatus( COMMIT );
         wp.addTask( correctiveTask );
         wp.addWorkScopeTask( correctiveTask );
      } );

      // When the corrective task is unassigned.
      executeUnassign( correctiveTask, NA_REASON, NA_NOTE );

      // Then the corrective task remains part of the work package's work scope but is marked as
      // being unassigned.
      boolean unassignBoolean = getWorkScopeUnassignBoolean( workpackage, correctiveTask );
      assertThat( "Corretive task is not marked as unassigned from work order of workpackage.",
            unassignBoolean, is( true ) );
      assertThat(
            "Corrective task not unassigned from work packge; its highest event key is not itself.",
            evtEventDao.findByPrimaryKey( correctiveTask ).getHEvent(),
            is( correctiveTask.getEventKey() ) );
   }


   private void executeUnassign( TaskKey task, String reason, String note ) throws Exception {
      // Note: unassignTask() requires user parameter ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP to be set.
      HumanResourceKey hr = createHumanResource();
      int userId = OrgHr.findByPrimaryKey( hr ).getUserId();
      UserParameters.setInstance( userId, "LOGIC", new UserParametersFake( userId, "LOGIC" ) );

      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );
      taskBean.unassignTask( task, hr, reason, note );
   }


   private boolean getWorkScopeUnassignBoolean( TaskKey workpackage, TaskKey task ) {
      DataSetArgument args = new DataSetArgument();
      args.add( workpackage, "wo_sched_db_id", "wo_sched_id" );
      args.add( task, "sched_db_id", "sched_id" );
      QuerySet qs = QuerySetFactory.getInstance().executeQueryTable( "sched_wo_line", args );
      qs.next();
      return qs.getBoolean( "unassign_bool" );
   }

}
