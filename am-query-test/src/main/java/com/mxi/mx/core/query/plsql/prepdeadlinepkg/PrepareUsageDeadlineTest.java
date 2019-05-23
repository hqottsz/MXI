package com.mxi.mx.core.query.plsql.prepdeadlinepkg;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.RefSchedFromKey.CUSTOM;
import static com.mxi.mx.core.key.RefSchedFromKey.EFFECTIV;
import static com.mxi.mx.core.key.RefTaskClassKey.REQ;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.SUPRSEDE;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.OneTimeSchedulingRule;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OneTimeSchedulingRuleBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskDeadlineBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventDeadlineKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.plsql.delegates.DeadlineProcedureDelegate;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.evt.EvtSchedDead;


/**
 * Test case for the plsql package procedure prep_deadline_pkg.PrepareUsageDeadline
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class PrepareUsageDeadlineTest {

   private static final boolean SYNC_WITH_BASELINE = true;
   private static final boolean DO_NOT_SYNC_WITH_BASELINE = false;
   private static final Double USAGE_VALUE = Double.valueOf( 123 );

   private DeadlineProcedureDelegate iProc;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given a task whose effective date was set at initialization time and whose original task
    * definition is:<br>
    * - on-condition<br>
    * - scheduled from an effective date but that date is not set<br>
    * - has an existing usage based scheduling rule<br>
    * When a new revision of the task definition has another usage based scheduling rule added and
    * baseline sync is requested, then PrepareUsageDeadline will use the overwritten effective date
    * to calculate the start value of the new usage based scheduling rule.<br>
    * <br>
    * Note, by the name of this test method, you can tell that the plsql is a candidate for
    * refactoring :)
    *
    * @throws Exception
    */
   @Test
   public void testWhenBsyncAndOnConditionTaskHasOverwrittenEffectiveDateWithNewUsageRule()
         throws Exception {
      // 1. Setup

      Date lNow = new Date();
      Date lUsageDate = DateUtils.addDays( lNow, -5 );
      Date lDeadlineStartDate = DateUtils.addDays( lNow, 3 );

      InventoryKey lInvKey = new InventoryBuilder().build();

      // Create a usage record in the past.
      withUsage( lInvKey, lUsageDate, CYCLES, USAGE_VALUE );

      // Create an on-condition task revision that is schedulded from an effective date, but no
      // effective date provided.
      TaskRevisionBuilder lTaskRev1Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isOnCondition().isScheduledFromEffectiveDate( null ).withStatus( SUPRSEDE )
            .withRevisionNumber( 1 );
      TaskTaskKey lTaskRev1Key = lTaskRev1Builder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CYCLES, BigDecimal.TEN );
      // Add a usage based scheduling rule to the task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev1Key );

      // Get the task definition.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev1Key ).getTaskDefn();

      // Create a second revision of the task definition.
      TaskRevisionBuilder lTaskRev2Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isOnCondition().isScheduledFromEffectiveDate( null ).withStatus( ACTV )
            .withRevisionNumber( 2 ).withTaskDefn( lTaskDefnKey );
      TaskTaskKey lTaskRev2Key = lTaskRev2Builder.build();

      // Add another usage based scheduling rule to the second task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev2Key );

      // Create a task based on the second task revision.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lTaskRev2Key )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      // This deadline overwrites the scheduling rule's effective date (which is null), thus making
      // it CUSTOM.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( HOURS )
            .withInterval( BigDecimal.TEN.doubleValue() ).scheduledFrom( CUSTOM )
            .withStartDate( lDeadlineStartDate ).build();

      // 2. Execute test

      // Call PREP_DEADLINE_PKG.PrepareUsageDeadline() to create the second task revision's
      // scheduling rule.
      iProc.prepareUsageDeadline( lTaskKey, lTaskRev2Key, CYCLES, SYNC_WITH_BASELINE );

      // Retreive the task scheduling information to validate.
      EventDeadlineKey lCycleDeadlineKey = new EventDeadlineKey( lTaskKey.getEventKey(), CYCLES );
      EvtSchedDeadTable lCycleDeadline = EvtSchedDeadTable.findByPrimaryKey( lCycleDeadlineKey );

      // 3. Verify results

      // Verify that the deadline start value is scheduled from CUSTOM and is using a start value
      // that equals the usage.
      assertEquals( "Unexpected scheduled from value.", CUSTOM, lCycleDeadline.getScheduledFrom() );
      assertEquals( "Unexpected start value.", USAGE_VALUE, lCycleDeadline.getStartQt() );
   }


   /**
    * <pre>
    *
    *       Given the following scenario:
    *       - task defn revision
    *       -- scheduled from an effective date
    *       -- with usage based scheduling rule
    *       - task initialized against task defn revision with deadline
    *       - new task defn revision with modified effective date
    *       - task updated to new task defn revision
    *       -> test prepareUsageDeadline() to update the task's deadline
    *
    *       Test calling prepareUsageDeadline() with "sync with baseline" set to true, expecting the
    *       deadline's start date to be updated to the effective date of the revised task defn.
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenTaskDefnEffectiveDateRevisedAndRequestedToSyncWithBaseline()
         throws Exception {

      // Create test effective dates.
      Calendar lCal = new GregorianCalendar();
      lCal.set( 2015, 2, 17 );

      Date lEffectiveDate = lCal.getTime();
      Date lNewEffectiveDate = DateUtils.addDays( lEffectiveDate, 2 );

      // Create an task revision that is schedulded from an effective date.
      TaskRevisionBuilder lTaskRev1Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( SUPRSEDE )
            .withRevisionNumber( 1 );
      TaskTaskKey lTaskRev1Key = lTaskRev1Builder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CYCLES, BigDecimal.TEN );
      // Add a scheduling rule to the first task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev1Key );

      // Get the task definition.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev1Key ).getTaskDefn();

      // Create a second revision of the task definition with a revised effective date.
      TaskRevisionBuilder lTaskRev2Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lNewEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 2 ).withTaskDefn( lTaskDefnKey );
      TaskTaskKey lTaskRev2Key = lTaskRev2Builder.build();

      // Add the same scheduling rule to the second task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev2Key );

      // Create a task based on the second task revision (emulating a task that was revised from
      // the first to the second task revision).
      InventoryKey lInvKey = new InventoryBuilder().build();
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lTaskRev2Key )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CYCLES )
            .withInterval( BigDecimal.TEN.doubleValue() ).scheduledFrom( EFFECTIV )
            .withStartDate( lEffectiveDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CYCLES );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );

      // Call prepareUsageDeadline(), indicating the task is to be synced with the baseline.
      iProc.prepareUsageDeadline( lTaskKey, lTaskRev1Key, CYCLES, SYNC_WITH_BASELINE );

      // Verify the scheduled deadline was modified with the new task revision's effective date.
      lEvtSchedDead = new EvtSchedDead( lTaskKey, CYCLES );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lNewEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );
   }


   /**
    * <pre>
    *
    *      Given the following scenario:
    *      - task defn revision
    *      -- scheduled from an effective date
    *      -- with usage based scheduling rule
    *      - task initialized against task defn revision with deadline
    *      - new task defn revision with modified effective date
    *      - task updated to new task defn revision
    *      -> test prepareUsageDeadline() to update the task's deadline
    *
    *      Test calling prepareUsageDeadline() with "sync with baseline" set to false, expecting the
    *      deadline's start date to not be updated. Instead the start date will remain being the
    *      effective date of the task's original task defn revision.
    *
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void testWhenTaskDefnEffectiveDateRevisedButRequestedNotToSyncWithBaseline()
         throws Exception {

      // Create test effective dates.
      Calendar lCal = new GregorianCalendar();
      lCal.set( 2015, 2, 17 );

      Date lEffectiveDate = lCal.getTime();
      Date lNewEffectiveDate = DateUtils.addDays( lEffectiveDate, 2 );

      // Create an task revision that is schedulded from an effective date.
      TaskRevisionBuilder lTaskRev1Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lEffectiveDate ).withStatus( SUPRSEDE )
            .withRevisionNumber( 1 );
      TaskTaskKey lTaskRev1Key = lTaskRev1Builder.build();

      OneTimeSchedulingRule lOneTimeSchedulingRule =
            new OneTimeSchedulingRule( CYCLES, BigDecimal.TEN );
      // Add a scheduling rule to the first task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev1Key );

      // Get the task definition.
      TaskDefnKey lTaskDefnKey = TaskTaskTable.findByPrimaryKey( lTaskRev1Key ).getTaskDefn();

      // Create a second revision of the task definition with a revised effective date.
      TaskRevisionBuilder lTaskRev2Builder = new TaskRevisionBuilder().withTaskClass( REQ )
            .isScheduledFromEffectiveDate( lNewEffectiveDate ).withStatus( ACTV )
            .withRevisionNumber( 2 ).withTaskDefn( lTaskDefnKey );
      TaskTaskKey lTaskRev2Key = lTaskRev2Builder.build();

      // Add the same scheduling rule to the second task revision.
      new OneTimeSchedulingRuleBuilder().build( lOneTimeSchedulingRule, lTaskRev2Key );

      // Create a task based on the first task revision (emulating a task that was revised from
      // the first to the second task revision).
      InventoryKey lInvKey = new InventoryBuilder().build();
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInvKey ).withTaskRevision( lTaskRev1Key )
            .withTaskClass( REQ ).build();

      // Create the task deadline for the corresponding scheduling rule of the first task revision.
      new TaskDeadlineBuilder( lTaskKey ).withDataType( CYCLES )
            .withInterval( BigDecimal.TEN.doubleValue() ).scheduledFrom( EFFECTIV )
            .withStartDate( lEffectiveDate ).build();

      // Ensure the scheduled deadline is configured as expected.
      EvtSchedDead lEvtSchedDead = new EvtSchedDead( lTaskKey, CYCLES );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );

      // Call prepareUsageDeadline(), indicateding the task is NOT to be synced with the baseline.
      iProc.prepareUsageDeadline( lTaskKey, lTaskRev2Key, CYCLES, DO_NOT_SYNC_WITH_BASELINE );

      // Verify the scheduled deadline was NOT modified.
      lEvtSchedDead = new EvtSchedDead( lTaskKey, CYCLES );
      lEvtSchedDead.assertSchedFromCd( EFFECTIV.getCd() );
      lEvtSchedDead.assertStartDt( lEffectiveDate );
      lEvtSchedDead.assertIntervalQt( BigDecimal.TEN.doubleValue() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iProc = new DeadlineProcedureDelegate();
   }


   /**
    * Creates a usage record.
    *
    * @param aInvKey
    *           inventory to which the usage is applied
    * @param aUsageDate
    *           date of the usage
    * @param aParameter
    *           usage parameter (data type)
    * @param aUsageValue
    *           usage value
    */
   private void withUsage( InventoryKey aInvKey, Date aUsageDate, DataTypeKey aParameter,
         double aUsageValue ) {
      UUID lUsageRecId = UUID.randomUUID();
      DataSetArgument lUsageRecArgs = new DataSetArgument();
      lUsageRecArgs.add( "usage_record_id", lUsageRecId );
      lUsageRecArgs.add( "usage_dt", aUsageDate );
      lUsageRecArgs.add( "creation_dt", aUsageDate );
      MxDataAccess.getInstance().executeInsert( "usg_usage_record", lUsageRecArgs );

      UUID lUsageDataId = UUID.randomUUID();
      DataSetArgument lUsageDataArgs = new DataSetArgument();
      lUsageDataArgs.add( "usage_data_id", lUsageDataId );
      lUsageDataArgs.add( "usage_record_id", lUsageRecId );
      lUsageDataArgs.add( aInvKey, "inv_no_db_id", "inv_no_id" );
      lUsageDataArgs.add( aParameter, "data_type_db_id", "data_type_id" );
      lUsageDataArgs.add( "tsn_qt", aUsageValue );
      lUsageDataArgs.add( "negated_bool", 0 );
      MxDataAccess.getInstance().executeInsert( "usg_usage_data", lUsageDataArgs );
   }
}
