package com.mxi.mx.web.query.task;

import static com.mxi.am.domain.Domain.createRequirement;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.DataTypeKey.CDY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.TaskKey;


/**
 * Integration unit test for AssignedTasksTabOverLimit.qrx
 *
 */
public class AssignedTasksTabOverLimitTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // The times returned from the DB have a granularity of seconds.
   private static final Date DUE_DATE = floorSecond( addDays( new Date(), 1 ) );
   private static final Date PLAN_BY_DATE = floorSecond( addDays( DUE_DATE, 1 ) );


   @Test
   public void itReturnsPlanByDateOfAssignedReqWithPlanByDate() {

      // Given a requirement with both a due date and a plan by date.
      TaskKey requirement = createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( DUE_DATE );
         } );
         req.setPlanByDate( PLAN_BY_DATE );
      } );

      // Given a work package to which the requirement is assigned.
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addTask( requirement );
      } );

      // When the query is executed.
      QuerySet qs = execute( workPackage );
      qs.next();

      // Then the query returns the plan by date
      // and is not affected by the due date.
      assertThat( "Unexpected number of rows returned from query.", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected plan by date.", qs.getDate( "plan_by_date" ), is( PLAN_BY_DATE ) );

   }


   @Test
   public void itReturnsDueDateOfAssignedReqWithPlanByDate() {

      // Given a requirement with both a due date and a plan by date.
      TaskKey requirement = createRequirement( req -> {
         req.addDeadline( deadline -> {
            deadline.setUsageType( CDY );
            deadline.setDueDate( DUE_DATE );
         } );
         req.setPlanByDate( PLAN_BY_DATE );
      } );

      // Given a work package to which the requirement is assigned.
      TaskKey workPackage = createWorkPackage( wp -> {
         wp.addTask( requirement );
      } );

      // When the query is executed.
      QuerySet qs = execute( workPackage );
      qs.next();

      // Then the query returns the due date
      // and is not affected by the plan by date.
      assertThat( "Unexpected number of rows returned from query.", qs.getRowCount(), is( 1 ) );
      assertThat( "Unexpected plan by date.", qs.getDate( "sched_dead_dt" ), is( DUE_DATE ) );

   }


   private QuerySet execute( TaskKey aWorkPackage ) {
      DataSetArgument args = new DataSetArgument();
      args.add( aWorkPackage, "aCheckDbId", "aCheckId" );

      return QueryExecutor.executeQuery( databaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), args );
   }

}
