package com.mxi.mx.db.plsql.prepdeadlinepkg;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Block;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DataTypeUtils;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefSchedFromKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.ProcedureStatementFactory;


/**
 * Tests for the Prep_Deadline_Pkg.getRescheduleFromDtValues PLSQL procedure.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRescheduleFromDtValues {

   private static final BigDecimal REPEAT_INTERVAL = new BigDecimal( 3 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify When a block contains a recurring task scheduled from WPEND, and the first instance of
    * the task is completed, then the next instance of that task will remain scheduled from WPEND.
    *
    * @throws Exception
    */
   @Test
   public void itSchedulesNextTaskFromWPEndWhenTaskCompletedInInCompletedBlockAssignedToWork()
         throws Exception {

      // *** Arrange ***
      final Date lDate = DateUtils.getDateTime( 2013, 3, 17, 11, 00, 00 );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            // create an aircraft
         }
      } );

      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  Date lDueDate = lDate;
                  aRequirement.setActualStartDate( lDate );
                  aRequirement.setScheduledStartDate( lDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CHR, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate );
                  aRequirement.setActualEndDate( lDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      final TaskKey lBlock = Domain.createBlock( new DomainConfiguration<Block>() {

         @Override
         public void configure( Block aBlock ) {
            aBlock.setInventory( lAircraft );
            aBlock.addRequirement( lRequirement );
            aBlock.setStatus( RefEventStatusKey.IN_WORK );
         }
      } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lBlock );
            aWorkPackage.setActualStartDate( lDate );
            aWorkPackage.setActualEndDate( lDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // *** Act ***
      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CHR, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      // *** Assert ***
      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );
   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsDueBeforeWPStartedAndCompletedInSideWPAndOutsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lCompletionDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskStartDate, 91 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setActualStartDate( lCompletionDate );
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lCompletionDate );
            aWorkPackage.setActualEndDate( lCompletionDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 9999
    * <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from last due date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromLastDueWhenPreviousRequirementIsOverdueBeforeWPStartedAndCompletedInsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lCompletionDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskStartDate, 91 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 9999 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lCompletionDate );
            aWorkPackage.setActualEndDate( lCompletionDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.LASTDUE.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is within the work package window <br>
    * And the requirement completion date is within the work package window <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsOverdueInTheWPAndCompletedInsideWPAndCompletedOutsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, -5 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 9999
    * <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is within the work package window <br>
    * And the requirement completion date is within the work package window <br>
    * When the work package is completed with a completion date inside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsOverdueInTheWPAndCompletedInsideTheWPAndCompletedInsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, -5 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 9999 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is not
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is within the work package window <br>
    * And the requirement completion date is within the work package window <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsNotOverdueAndDueDateIsInTheWPAndCompletedInsideWPAndCompletedOutsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, 5 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 9999
    * <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is not
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is within the work package window <br>
    * And the requirement completion date is within the work package window <br>
    * When the work package is completed with a completion date inside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsNotOverdueAndDueDateIsInTheWPAndCompletedInsideWPAndCompletedInsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, 5 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 9999 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is not
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is after the work package window <br>
    * And the requirement completion date is after the work package window <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsNotOverdueAndDueDateIsAfterTheWPEndAndCompletedInsideWPAndCompletedOutsideScheduleToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, 20 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from WPEND <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 9999
    * <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is not
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is after the work package window <br>
    * And the requirement completion date is after the work package window <br>
    * When the work package is completed with a completion date inside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from work package completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromWPEndWhenPreviousRequirementIsNotOverdueAndDueDateIsAfterTheWPEndAndCompletedInsideWPAndCompletedInsideScheduleToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, 20 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 9999 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.WPEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.WPEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.WPEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from Execute <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from execution date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromLastEndWhenPreviousRequirementIsDueBeforeWPStartedAndRescheduledFromExecuteAndCompletedInSideWPAndOutsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lCompletionDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskStartDate, 91 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setActualStartDate( lCompletionDate );
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.LASTEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lCompletionDate );
            aWorkPackage.setActualEndDate( lCompletionDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.LASTEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.LASTEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from Execute <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 9999
    * <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from last due date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromLastDueWhenPreviousRequirementIsOverdueBeforeWPStartedAndRescheduledFromExecuteAndCompletedInsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lCompletionDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskStartDate, 91 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 9999 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.LASTEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lCompletionDate );
            aWorkPackage.setActualEndDate( lCompletionDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.LASTEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.LASTDUE.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from Execute <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is within the work package window <br>
    * And the requirement completion date is within the work package window <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from execution date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromLastEndWhenPreviousRequirementIsOverdueInTheWPAndCompletedInsideWPAndRescheduledFromExecuteAndCompletedOutsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, -5 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.LASTEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.LASTEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.LASTEND.getCd(), lSchedFromCd );

   }


   /**
    * Given an aircraft with a manufacturing date <br>
    * And a recurring requirement initialized against the aircraft with start date as manufacturing
    * date <br>
    * And rescheduled from Execute <br>
    * And a calendar based deadline with a SCHEDULE_TO_PLAN_LOW and SCHEDULE_TO_PLAN_HIGH as 0 <br>
    * And the calendar based deadline is such that the first occurrence of the requirement is not
    * overdue <br>
    * And a work package containing this requirement <br>
    * And the requirement due date is within the work package window <br>
    * And the requirement completion date is within the work package window <br>
    * When the work package is completed with a completion date outside the SCHEDULE_TO_PLAN WINDOW
    * Then the next requirement is scheduled from execution completion date
    *
    */
   @Test
   public void
         itSchedulesNextRequirementFromLastEndWhenPreviousRequirementIsNotOverdueAndDueDateIsInTheWPAndRescheduleFromExecuteAndCompletedInsideWPAndCompletedOutsideSchedToPlanWindow()
               throws Exception {
      final Date lTaskStartDate = DateUtils.addDays( new Date(), -365 );
      final Date lManufactureDate = DateUtils.addDays( new Date(), -365 );
      final Date lTaskCompletionDate = DateUtils.addDays( new Date(), -10 );
      final Date lWorkPackageStartDate = DateUtils.addDays( new Date(), -90 );
      final Date lWorkPackageEndDate = new Date();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setManufacturedDate( lManufactureDate );
         }
      } );

      // Previous Requirement
      final TaskKey lRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               Date lDueDate = DateUtils.addDays( lTaskCompletionDate, 5 );
               BigDecimal lSchedToPlanLow = new BigDecimal( 0 );
               BigDecimal lSchedToPlanHigh = new BigDecimal( 0 );
               BigDecimal lUsage = new BigDecimal( -10 );


               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setScheduledStartDate( lTaskStartDate );
                  aRequirement.setActualStartDate( lTaskCompletionDate );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.addCalendarDeadline( DataTypeKey.CMON, RefSchedFromKey.LASTEND, null,
                        REPEAT_INTERVAL, lDueDate, lSchedToPlanLow, lSchedToPlanHigh, lUsage );
                  aRequirement.setActualEndDate( lTaskCompletionDate );
                  aRequirement.setStatus( RefEventStatusKey.COMPLETE );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {

            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lRequirement );
            aWorkPackage.setActualStartDate( lWorkPackageStartDate );
            aWorkPackage.setActualEndDate( lWorkPackageEndDate );
            aWorkPackage.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      // Execute

      // Primary Key of the Previous completed actual task
      EventKey lEventKey = lRequirement.getEventKey();
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( lEventKey, "an_EventDbId", "an_EventId" );
      lArgs.add( DataTypeKey.CMON, "an_DataTypeDbId", "an_DataTypeId" );
      lArgs.add( "av_ReschedFromCd", RefSchedFromKey.LASTEND.getCd() );
      lArgs.add( "an_ServiceCheck", 0 );

      DataSetArgument lOutArgs = new DataSetArgument();
      lOutArgs.add( "ad_StartDt", DataTypeUtils.STRING );
      lOutArgs.add( "av_SchedFromCd", DataTypeUtils.STRING );
      lOutArgs.add( "on_Return", DataTypeUtils.STRING );

      ProcedureStatementFactory.execute( iDatabaseConnectionRule.getConnection(),
            "Prep_Deadline_Pkg.GetRescheduleFromDtValues", lArgs, lOutArgs );

      String lSchedFromCd = lOutArgs.getString( "av_SchedFromCd" );
      assertEquals(
            "Error during Prep_Deadline_Pkg.GetRescheduleFromDtValues(av_SchedFromCd="
                  + lSchedFromCd + ": " + getPlsqlErrors(),
            RefSchedFromKey.LASTEND.getCd(), lSchedFromCd );

   }


   /**
    * Calls APP_OBJ_PKG_GETMXIERROR PLSQL procedure. Performs some error checking a
    *
    * @return description of the Returned Value.
    *
    * @exception MxRuntimeException
    *               if the PLSQL error occured.
    */
   protected String getPlsqlErrors() throws MxRuntimeException {

      DataSetArgument lArgs = new DataSetArgument();
      String lErrMsg;

      // Call the PL/SQL procedure
      lArgs = MxDataAccess.getInstance()
            .executeWithReturnParms( "com.mxi.mx.core.query.plsql.AppObjPkgGetMxiError", lArgs );

      // Extract the return arguments
      lErrMsg = lArgs.getString( "aErrMsg" );

      // If no error was returned, make sure to return empty string
      if ( StringUtils.isBlank( lErrMsg ) ) {

         return "";
      }

      // Return error message
      return lErrMsg;
   }

}
