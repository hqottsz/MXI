package com.mxi.mx.core.ejb.taskdefn;

import static com.mxi.mx.core.key.RefBOMClassKey.SYS;
import static com.mxi.mx.core.key.RefBOMClassKey.TRK;
import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;
import static com.mxi.mx.core.key.RefEventStatusKey.COMPLETE;
import static com.mxi.mx.core.key.RefEventStatusKey.TERMINATE;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.Attachment;
import com.mxi.am.domain.BlockDefinition;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.domain.Ietm;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.Operator;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.ReferenceDocument;
import com.mxi.am.domain.ReferenceDocumentDefinition;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.domain.TechnicalReference;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.services.work.WorkItemGeneratorExecuteImmediateFake;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskJicReqMapKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.bsync.synchronization.logic.InventorySynchronizer;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.services.stask.creation.CreationService;
import com.mxi.mx.core.services.taskdefn.TaskDefnUtils;
import com.mxi.mx.core.services.taskdefn.exception.BomItemUsageMismatchException;
import com.mxi.mx.core.services.taskdefn.exception.InvalidTaskDefinitionStatusException;
import com.mxi.mx.core.services.taskdefn.exception.SubTaskROOTOrSYSMismatchException;
import com.mxi.mx.core.services.taskdefn.exception.SubTaskTRKOrSUBASSYMismatchException;
import com.mxi.mx.core.services.taskdefn.transferobject.RepairReferenceDetailsTO;
import com.mxi.mx.core.services.taskdefn.transferobject.RequirementTO;
import com.mxi.mx.core.services.taskdefn.transferobject.TaskDefnRevTO;
import com.mxi.mx.core.table.evt.EvtAttachTable;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtIetm;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtSchedDeadTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.task.TaskJicReqMapTable;
import com.mxi.mx.core.table.task.TaskRepRefDao;
import com.mxi.mx.core.table.task.TaskRepRefKey;
import com.mxi.mx.core.table.task.TaskRepRefTableRow;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.unittest.table.sched.SchedLabour;
import com.mxi.mx.core.unittest.table.stask.SchedStaskUtil;


/**
 * Integration tests for {@link TaskDefnBean}
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class TaskDefnBeanTest {

   /** Constants */
   private static final String CONFIG_SLOT_ROOT = "CONFIG_SLOT_ROOT";
   private static final String CONFIG_SLOT_SUBASSY = "CONFIG_SLOT_SUBASSY";
   private static final String CONFIG_SLOT_TRK_1 = "CONFIG_SLOT_TRK_1";
   private static final String CONFIG_SLOT_TRK_2 = "CONFIG_SLOT_TRK_2";
   private static final String CONFIG_SLOT_SYS = "CONFIG_SLOT_SYS";
   private static final String OPERATIONAL_RESTRICTION_MESSAGE = "OPERATIONAL_RESTRICTION_MESSAGE";
   private static final String PERFORMANCE_PENALTIES_MESSAGE = "PERFORMANCE_PENALTIES_MESSAGE";
   private static final String REQ_DEFN_CODE_1 = "REQ_DEFN_CODE_1";
   private static final String REQ_DEFN_CODE_2 = "REQ_DEFN_CODE_2";
   private static final String REF_DOC_DEFN_CODE = "REF_DOC_DEFN_CODE";
   private static final String STEP_DESCRIPTION = "STEP_DESCRIPTION";
   private static final String TEST_ERROR_LABEL = "TEST_ERROR_LABEL";
   private static final HumanResourceKey HR = new HumanResourceKey( 1, 1 );
   private static final Integer REVISION_1 = 1;
   private static final Integer REVISION_2 = 2;
   private static final String TASK_REP_REP_TABLE = "TASK_REP_REF";
   private static final Date CURRENT_DATE_WITH_ZERO_TIME = DateUtils.getCurrentDateWithZeroTime();

   private static final Date MANUFACTURED_DATE = DateUtils.addDays( new Date(), -20 );
   private static final Date RECEIVED_DATE = DateUtils.addDays( new Date(), -15 );
   private static final Date ONE_DAY_FUTURE_EFFECTIVE_DATE =
         DateUtils.addDays( CURRENT_DATE_WITH_ZERO_TIME, 1 );
   private static final Date ONE_MONTH_FUTURE_EFFECTIVE_DATE =
         DateUtils.addDays( CURRENT_DATE_WITH_ZERO_TIME, 31 );
   private static final Date TWO_MONTHS_FUTURE_EFFECTIVE_DATE =
         DateUtils.addDays( CURRENT_DATE_WITH_ZERO_TIME, 61 );

   private static final SimpleDateFormat DATE_FORMAT_WITHOUT_MS =
         new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   private static final double DEFAULT_FC_MODEL_RATE = 2.252363464d;
   /**
    * The delta is an allowable precision difference used in the assertEquals for doubles in this
    * test class
    */
   private static final double ALLOWABLE_DELTA = 0.0001;
   private static final BigDecimal THRESHOLD_INTERVAL = new BigDecimal( 10.0 );
   private static final BigDecimal USAGE_HOURS = new BigDecimal( 20.0 );
   private static final String TECHNICAL_REFERENCE_NAME = "TechRef";
   private static final String ATTACHMENT_NAME = "Attachment";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setupConfigurationParameters() {
      GlobalParametersFake lConfigParms = new GlobalParametersFake( "WORK_MANAGER" );
      lConfigParms.setString( "WORK_ITEM_BSYNCVALIDATEACTUALS_THRESHOLD", "2" );
      GlobalParameters.setInstance( lConfigParms );
   }


   /**
    * <pre>
    *    Given a new non-executable requirement with execution information
    *     When the requirement is activated
    *     Then initialized requirements has no labor
    * </pre>
    *
    */
   @Test
   public void itIgnoreExecutionInformationOnNonExecutableRequirement() throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a new non-executable requirement with execution information
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setExecutable( false );

                  aReqDefn.addLabourRequirement( RefLabourSkillKey.ENG, BigDecimal.TEN );
               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      TaskKey lReqTask =
            SchedStaskUtil.findLastCreatedTaskForTaskDefinition( lActivatedReqDefnKey );

      int lCountLabourRow = SchedLabour.countLabours( lReqTask );

      assertEquals( "Unexpected labour was created for the requirement task.", 0, lCountLabourRow );

   }


   /**
    * <pre>
    *     Given a new on-condition requirement
    *     And an aircraft with synchronization enabled
    *     When the requirement is activated
    *     Then the requirement should not be initialized against the aircraft
    * </pre>
    *
    */
   @Test
   public void itDoesNotCreateANewOnConditionRequirement() throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a new on-condition requirement
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setOnCondition( true );

               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lActivatedReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      assertTrue( "The requirement should not be initialized against the aircraft.",
            lTasks.size() == 0 );

   }


   /**
    * <pre>
    *     Given a new requirement that is scheduled from manufactured date
    *     And an aircraft requires a forecast model
    *     And the requirement has a calendar based scheduling rule with a repeat interval
    *     And the requirement has a usage based scheduling rule with a repeat interval
    *     When I activate the requirement
    *     And a requirement is initialized
    *     Then the calendar based deadline of the task should be the manufactured date of the aircraft + the calendar based scheduling rule's interval
    *     And the usage based deadline of the task should be the usage of the aircraft + ( usage based scheduling rule's interval X the aircraft's forecast model's interval rate )
    * </pre>
    *
    */
   @Test
   public void itSchedulesBothCalendarAndUsageIntervalFromManufacturedDate() throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.setManufacturedDate( MANUFACTURED_DATE );
            aAircraft.setForecastModel(
                  Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                     }
                  } ) );
            aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );

         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a manufactured date requirement definition
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setScheduledFromManufacturedDate();
                  aReqDefn.addSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
                  aReqDefn.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );

               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnService.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lActivatedReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      // Then the calendar deadline should be manufactured date + interval
      Date lExpectedActiveDeadlineDate = DateUtils
            .getEndOfDay( DateUtils.addDays( MANUFACTURED_DATE, BigDecimal.TEN.intValue() ) );

      Date lActualActiveDeadlineDate = EvtSchedDeadTable
            .findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.CDY ).getDeadlineDate();
      assertEquals( "Unexpected deadline date for the active requirement",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedActiveDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lActualActiveDeadlineDate ) );

      // The expected value of the usage deadline is the aircraft usage + ( interval X forecast
      // interval rate )
      double lExpectedActiveDeadlineHours = BigDecimal.TEN.doubleValue();

      Double lActualActiveDeadlineHours = EvtSchedDeadTable
            .findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.HOURS ).getDeadlineQt();

      if ( lActualActiveDeadlineHours == null ) {
         fail( "Expect a deadline hour" );
      }

      assertEquals( "Unexpected deadline hour for the active requirement",
            lExpectedActiveDeadlineHours, lActualActiveDeadlineHours, ALLOWABLE_DELTA );

   }


   /**
    * <pre>
    *     Given a new requirement that is scheduled from received date
    *     And the requirement has a calendar based scheduling rule with a repeat interval
    *     And the requirement has a usage based scheduling rule with a repeat interval
    *     When I activate the requirement ( it will automatically initialized )
    *     Then the calendar based deadline of the task should be the received date of the aircraft + the calendar based scheduling rule's interval
    *     And the usage based deadline of the task should be the usage of the aircraft + ( usage based scheduling rule's interval X the aircraft's forecast model's interval rate )
    * </pre>
    */
   @Test
   public void itSchedulesBothCalendarAndUsageIntervalFromReceivedDate() throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.setReceivedDate( RECEIVED_DATE );
            aAircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );

         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a received date requirement definition
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setScheduledFromReceivedDate();
                  aReqDefn.addSchedulingRule( DataTypeKey.CDY, BigDecimal.TEN );
                  aReqDefn.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );

               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lActivatedReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      Date lExpectedActiveDeadlineDate =
            DateUtils.getEndOfDay( DateUtils.addDays( RECEIVED_DATE, BigDecimal.TEN.intValue() ) );

      Date lActualActiveDeadlineDate = EvtSchedDeadTable
            .findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.CDY ).getDeadlineDate();
      assertEquals( "Unexpected deadline date for the active requirement",
            DATE_FORMAT_WITHOUT_MS.format( lExpectedActiveDeadlineDate ),
            DATE_FORMAT_WITHOUT_MS.format( lActualActiveDeadlineDate ) );

      double lExpectedActiveDeadlineHours = BigDecimal.TEN.doubleValue();

      Double lActualActiveDeadlineHours = EvtSchedDeadTable
            .findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.HOURS ).getDeadlineQt();

      if ( lActualActiveDeadlineHours == null ) {
         fail( "Expect a deadline hour" );
      }

      assertEquals( "Unexpected deadline hour for the active requirement",
            lExpectedActiveDeadlineHours, lActualActiveDeadlineHours, ALLOWABLE_DELTA );

   }


   /**
    * Verify that a task's usage deadline value is accurately calculated when the task is based on a
    * definition and is scheduled from an effective date in the future. The effective date is also
    * within the same forecast model period as the current date.
    *
    * <pre>
    *    Given a new requirement that is scheduled from effective date in the one-day future
    *    And the requirement has a non-recurring usage based scheduling rule
    *    And an aircraft collecting the same usage parameter as the scheduling rule
    *    And an aircraft has a forecast model which only have one period for the whole year
    *    When I activate the requirement
    *    And the requirement is initialized
    *    Then the deadline start quantity should be the usage forecast at the effective date
    * </pre>
    *
    * This test will not reflect the intention when the date today is Dec 31st, because the
    * effective date will be within the next forecast model period in a new year.
    */
   @Test
   public void itCalcsUsageDeadlineWhenSchedFromEffectiveDateInCurrentForecastModel()
         throws Exception {

      // Set up support data.
      // In order for the task to be initialized by baseline sync, a validation is made
      // (InvalidInventoryForTaskDefinitionException) to ensure the part number of the aircraft
      // matches a part number of the assembly's root config slot. So set up a part number.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.addUsage( DataTypeKey.HOURS, USAGE_HOURS );
            aAircraft.setForecastModel(
                  Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                     }
                  } ) );

         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a effective date requirement definition
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setScheduledFromEffectiveDate( ONE_DAY_FUTURE_EFFECTIVE_DATE );
                  aReqDefn.addSchedulingRule( DataTypeKey.HOURS, THRESHOLD_INTERVAL );

               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lActivatedReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      assertEquals( "Unexpected number of initialized tasks", 1, lTasks.size() );

      double lDiffInDays = DateUtils.absoluteDifferenceInFractionalDays(
            ONE_DAY_FUTURE_EFFECTIVE_DATE, CURRENT_DATE_WITH_ZERO_TIME );

      double lExpectedActiveDeadlineStartValue =
            lDiffInDays * DEFAULT_FC_MODEL_RATE + USAGE_HOURS.doubleValue();

      Double lActualActiveDeadlineStartValue =
            EvtSchedDeadTable.findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.HOURS ).getStartQt();

      if ( lActualActiveDeadlineStartValue == null ) {
         fail( "Expect a deadline start value" );
      }

      assertEquals( "Unexpected deadline start value for the active requirement",
            lExpectedActiveDeadlineStartValue, lActualActiveDeadlineStartValue, ALLOWABLE_DELTA );

   }


   /**
    * Verify that a task's usage deadline value is accurately calculated when the task is based on a
    * definition and is scheduled from an effective date in the future. The effective date is within
    * the next forecast model period.
    *
    * <pre>
    *    Given a new requirement that is scheduled from effective date in the 31-days future
    *    And the requirement has a non-recurring usage based scheduling rule
    *    And an aircraft collecting the same usage parameter as the scheduling rule
    *    And an aircraft has a forecast model which has a period per month
    *    When I activate the requirement
    *    And the requirement is initialized
    *    Then the deadline start quantity should be the usage forecast at the effective date
    * </pre>
    *
    * This test will not reflect the intention when the date today is Jan 31st or Mar 31st or...
    * whose next month only has <=30 days, because the effective date will be within the next next
    * forecast model period.
    */
   @Test
   public void itCalcsUsageDeadlineWhenSchedFromEffectiveDateInNextForecastModelPeriod()
         throws Exception {

      // Set up support data.
      // In order for the task to be initialized by baseline sync, a validation is made
      // (InvalidInventoryForTaskDefinitionException) to ensure the part number of the aircraft
      // matches a part number of the assembly's root config slot. So set up a part number.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.addUsage( DataTypeKey.HOURS, USAGE_HOURS );
            aAircraft.setForecastModel(
                  Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 2, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 3, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 4, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 5, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 6, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 7, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 8, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 9, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 10, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 11, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 12, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                     }
                  } ) );

         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a effective date requirement definition
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setScheduledFromEffectiveDate( ONE_MONTH_FUTURE_EFFECTIVE_DATE );
                  aReqDefn.addSchedulingRule( DataTypeKey.HOURS, THRESHOLD_INTERVAL );

               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lActivatedReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      assertEquals( "Unexpected number of initialized tasks", 1, lTasks.size() );
      //
      // double lDiffInDays = DateUtils.absoluteDifferenceInFractionalDays(
      // ONE_MONTH_FUTURE_EFFECTIVE_DATE, CURRENT_DATE_WITH_ZERO_TIME );
      double lDiffInDays = 31d;

      double lExpectedActiveDeadlineStartValue =
            lDiffInDays * DEFAULT_FC_MODEL_RATE + USAGE_HOURS.doubleValue();

      Double lActualActiveDeadlineStartValue =
            EvtSchedDeadTable.findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.HOURS ).getStartQt();

      if ( lActualActiveDeadlineStartValue == null ) {
         fail( "Expect a deadline start value" );
      }

      assertEquals( "Unexpected deadline start value for the active requirement",
            lExpectedActiveDeadlineStartValue, lActualActiveDeadlineStartValue, ALLOWABLE_DELTA );

   }


   /**
    * Verify that a task's usage deadline value is accurately calculated when the task is based on a
    * definition and is scheduled from an effective date in the future. The effective date is within
    * the next next forecast model period.
    *
    * <pre>
    *    Given a new requirement that is scheduled from effective date in the 61-days future
    *    And the requirement has a non-recurring usage based scheduling rule
    *    And an aircraft collecting the same usage parameter as the scheduling rule
    *    And an aircraft has a forecast model which has a period per month
    *    When I activate the requirement
    *    And the requirement is initialized
    *    Then the deadline start quantity should be the usage forecast at the effective date
    * </pre>
    */
   @Test
   public void itCalcsUsageDeadlineWhenSchedFromEffectiveDateInNextNextForecastModelPeriod()
         throws Exception {

      // Set up support data.
      // In order for the task to be initialized by baseline sync, a validation is made
      // (InvalidInventoryForTaskDefinitionException) to ensure the part number of the aircraft
      // matches a part number of the assembly's root config slot. So set up a part number.
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
            aAircraft.addUsage( DataTypeKey.HOURS, USAGE_HOURS );
            aAircraft.setForecastModel(
                  Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                     @Override
                     public void configure( ForecastModel aForecastModel ) {
                        aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 2, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 3, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 4, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 5, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 6, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 7, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 8, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 9, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 10, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 11, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                        aForecastModel.addRange( 12, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                     }
                  } ) );

         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a effective date requirement definition
      final TaskTaskKey lActivatedReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRecurring( false );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.setScheduledFromEffectiveDate( TWO_MONTHS_FUTURE_EFFECTIVE_DATE );
                  aReqDefn.addSchedulingRule( DataTypeKey.HOURS, THRESHOLD_INTERVAL );

               }
            } );

      // When the requirement is activated
      new TaskDefnBean().activate( lActivatedReqDefnKey, new TaskDefnRevTO(), HR, true );

      // Emulate baseline sync processing of the inv_sync_queue rows
      // ( added by TaskDefnBean.activate() ).
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lActivatedReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      assertEquals( "Unexpected number of initialized tasks", 1, lTasks.size() );

      double lDiffInDays = DateUtils.absoluteDifferenceInFractionalDays(
            TWO_MONTHS_FUTURE_EFFECTIVE_DATE, CURRENT_DATE_WITH_ZERO_TIME );

      double lExpectedActiveDeadlineStartValue =
            lDiffInDays * DEFAULT_FC_MODEL_RATE + USAGE_HOURS.doubleValue();

      Double lActualActiveDeadlineStartValue =
            EvtSchedDeadTable.findByPrimaryKey( lTasks.get( 0 ), DataTypeKey.HOURS ).getStartQt();

      if ( lActualActiveDeadlineStartValue == null ) {
         fail( "Expect a deadline start value" );
      }

      assertEquals( "Unexpected deadline start value for the active requirement",
            lExpectedActiveDeadlineStartValue, lActualActiveDeadlineStartValue, ALLOWABLE_DELTA );

   }


   /**
    * <pre>
    * Given an active, not on-condition requirement (REQ1) that has terminate link to another
    * active, not on-condition requirement (REQ2)
    * When I package and complete task (REQ1)
    * Then the requirement (REQ2) should terminated immediately And it should not be re-initialized
    * </pre>
    */
   @Test
   public void itTerminatesActivatedLinkedRequirementWhenRequirementThatItWasLinkedWithCompleted()
         throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an active, not on-condition requirement (REQ1)
      final TaskTaskKey lReqDefnKey1 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      TaskKey lReqTaskKey1 =
            new CreationService().createTaskFromDefinition( lAircraft, lReqDefnKey1, null, HR );

      // Given an active, not on-condition requirement (REQ2) that has terminate link
      final TaskTaskKey lReqDefnKey2 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_2 );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.addFollowingTaskDefinition( RefTaskDepActionKey.TERMINATE,
                        lReqDefnKey1 );
               }
            } );

      TaskKey lReqTaskKey2 =
            new CreationService().createTaskFromDefinition( lAircraft, lReqDefnKey2, null, HR );

      // When package and complete task
      CompleteService lCompleteService = new CompleteService( lReqTaskKey2 );
      lCompleteService.fullyCompleteRoot( HR, new Date() );

      assertEquals( "Requirement was not terminated", TERMINATE, getEventStatus( lReqTaskKey1 ) );

      assertEquals( "Requirement was not complete", COMPLETE, getEventStatus( lReqTaskKey2 ) );

   }


   /**
    * <pre>
    *   Given that I am a System Engineer and I have two activated requirements
    *   And these requirements are part of a block
    *   When I obsolete one of the requirement
    *   Then the obsoleted requirement should be cancelled
    *   And the block should remain active
    *   And the other requirement remains active
    * </pre>
    */
   @Test
   public void itCancelsObsolotedReqButTheBlockAndItsOtherReqsAreNotCancelled() throws Exception {
      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( lAircraftPart );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given an activated requirement with a scheduling rule.
      final TaskTaskKey lReqDefnKey1 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lReqTaskKey1 =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setDefinition( lReqDefnKey1 );
                  aReq.setInventory( lAircraft );
               }
            } );

      // Given another activated requirement with a scheduling rule.
      final TaskTaskKey lReqDefnKey2 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_2 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lReqTaskKey2 =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setDefinition( lReqDefnKey2 );
                  aReq.setInventory( lAircraft );
               }
            } );

      // And requirements as part of activated one-time block.
      final TaskTaskKey lBlockDefnKey =
            Domain.createBlockDefinition( new DomainConfiguration<BlockDefinition>() {

               @Override
               public void configure( BlockDefinition aBlockDefn ) {
                  aBlockDefn.setConfigurationSlot( lAircraftRootConfigSlot );
                  aBlockDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aBlockDefn.addRequirementDefinition( lReqDefnKey1 );
                  aBlockDefn.addRequirementDefinition( lReqDefnKey2 );
               }
            } );

      final TaskKey lBlockTaskKey =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setDefinition( lBlockDefnKey );
                  aReq.setInventory( lAircraft );
               }
            } );

      // Given an active obsolete the requirement
      final TaskTaskKey lObsoleteReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKey1 );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_2 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
               }
            } );

      // Baseline Sync needs to have this permission for this test
      HumanResourceKey iHumanResourceKey = new HumanResourceDomainBuilder().build();
      int iAuthorizingHrId = OrgHr.findByPrimaryKey( iHumanResourceKey ).getUserId();
      UserParametersStub lUserParametersStub = new UserParametersStub( iAuthorizingHrId, "LOGIC" );
      lUserParametersStub.setBoolean( "ALLOW_ASSIGN_LRP_ITEM_FOR_INWORK_WP", false );
      UserParameters.setInstance( iAuthorizingHrId, "LOGIC", lUserParametersStub );

      // When I obsolete the requirement
      new TaskDefnBean().obsolete( lObsoleteReqDefnKey, new TaskDefnRevTO(), iHumanResourceKey );

      WorkItemGeneratorExecuteImmediateFake lWorkItemGenFake =
            new WorkItemGeneratorExecuteImmediateFake( iHumanResourceKey );
      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer( lWorkItemGenFake );

      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then the obsoleted requirement should be cancelled
      assertEquals( "Obsoleted requirement was not cancelled", RefEventStatusKey.CANCEL,
            getEventStatus( lReqTaskKey1 ) );

      // The requirement should remain active
      assertEquals( "The requirement did not remain active", RefEventStatusKey.ACTV,
            getEventStatus( lReqTaskKey2 ) );

      // The block should remain active
      assertEquals( "The block did not remain active", RefEventStatusKey.ACTV,
            getEventStatus( lBlockTaskKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer And I have created a reference document definition (REF-A)
    * And I have created a requirement definition (REQ-1) that has COMPLIES link to the reference document definition (REF-A)
    * And I have an initialized, but not completed (active), a reference document against an aircraft based on REF-A
    * When I activate the requirement definition (REQ-1)
    * Then a new requirement shall be created against aircraft based on REQ-1 (with status of active)
    * And the reference document remain active (not completed)
    * </pre>
    */

   @Test
   public void
         itReturnsActiveStatusWhenReferenceDocLinkForCompliesInRequirementHasBeenInitializedAndNotCompleted()
               throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a reference document definition
      final TaskTaskKey lRefDocDefnKey = Domain.createReferenceDocumentDefinition(
            new DomainConfiguration<ReferenceDocumentDefinition>() {

               @Override
               public void configure( ReferenceDocumentDefinition aRefDocDefn ) {
                  aRefDocDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aRefDocDefn.setCode( REF_DOC_DEFN_CODE );
                  aRefDocDefn.setOnCondition( false );
                  aRefDocDefn.setRevisionNumber( REVISION_1 );
                  aRefDocDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }
            } );

      // reference document is not complete (active)
      final TaskKey lTaskRefDocKey =
            Domain.createReferenceDocument( new DomainConfiguration<ReferenceDocument>() {

               @Override
               public void configure( ReferenceDocument aRefDoc ) {
                  aRefDoc.setDefinition( lRefDocDefnKey );
                  aRefDoc.setInventory( lAircraft );
                  aRefDoc.setStatus( RefEventStatusKey.ACTV );
               }
            } );

      // Given a new the requirement definition
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }
            } );

      // activate the requirement and emulate baseline sync
      new TaskDefnBean().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      assertEquals( "Expected only one actual task is created", 1, lTasks.size() );

      assertEquals( "The requirement shall remain active", RefEventStatusKey.ACTV,
            getEventStatus( lTasks.get( 0 ) ) );

      assertEquals( "the reference document remain active", RefEventStatusKey.ACTV,
            getEventStatus( lTaskRefDocKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer
    * And I have created a reference document definition (REF-A)
    * And I have created a requirement definition (REQ-1) that has COMPLIES link to the reference document definition (REF-A)
    * And I have an initialized and completed a reference document against an aircraft based on REF-A
    * When I activate the requirement definition (REQ-1)
    * Then a new requirement shall NOT be created against aircraft based on REQ-1
    * </pre>
    **/
   @Test
   public void
         itDoesNotReturnActiveStatusWhenReferenceDocLinkForCompliesInRequirementHasBeenInitializedAndCompleted()
               throws Exception {

      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.setPart( lAircraftPart );
            aAircraft.allowSynchronization();
         }
      } );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given reference document definition
      final TaskTaskKey lRefDocDefnKey = Domain.createReferenceDocumentDefinition(
            new DomainConfiguration<ReferenceDocumentDefinition>() {

               @Override
               public void configure( ReferenceDocumentDefinition aRefDocDefn ) {
                  aRefDocDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aRefDocDefn.setCode( REF_DOC_DEFN_CODE );
                  aRefDocDefn.setOnCondition( false );
                  aRefDocDefn.setRevisionNumber( REVISION_1 );
                  aRefDocDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );

               }
            } );

      // A new reference document is complete
      final TaskKey lTaskRefDocKey =
            Domain.createReferenceDocument( new DomainConfiguration<ReferenceDocument>() {

               @Override
               public void configure( ReferenceDocument aRefDoc ) {
                  aRefDoc.setDefinition( lRefDocDefnKey );
                  aRefDoc.setInventory( lAircraft );
                  aRefDoc.setStatus( RefEventStatusKey.COMPLETE );

               }
            } );

      // Given a new the requirement definition
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.addLinkedTaskDefinition( RefTaskDepActionKey.COMPLIES, lRefDocDefnKey );
               }
            } );

      // activate the requirement and emulate baseline sync
      new TaskDefnBean().activate( lReqDefnKey, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lReqDefnKey, lAircraft, RefEventStatusKey.ACTV );

      assertTrue( "A new requirement shall NOT be created against aircraft based on requirement.",
            lTasks.isEmpty() );

      assertEquals( "The reference document status is not complete.", RefEventStatusKey.COMPLETE,
            getEventStatus( lTaskRefDocKey ) );

   }


   /**
    * <pre>
    * Given I am a System Engineer
    * And there exists a requirement definition in revision status
    * And there exists a terminated task for an inventory based on the requirement
    * When the requirement is activated
    * Then the task remains unchanged (remains terminated with the same revision number)
    * And no new task is created for the inventory based on the requirement
    * </pre>
    */
   @Test
   public void
         itKeepsTerminatedTaskStatusUnchangedAndNoNewTaskIsCreatedWhenTheRequirementActivated()
               throws Exception {
      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );

      final InventoryKey lAircraft = createAircraftInventory( lAircraftAssembly, lAircraftPart );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a new the requirement definition
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      // Create Requirement with status of TERMINATE
      TaskKey lTerminateTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefnKey );
            aReq.setInventory( lAircraft );
            aReq.setStatus( RefEventStatusKey.TERMINATE );
         }
      } );

      // Revise Requirement Definition
      final TaskTaskKey lRevisedReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKey );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aReqDefn.setRevisionNumber( REVISION_2 );
               }
            } );

      // activate the requirement and emulate baseline sync
      new TaskDefnBean().activate( lRevisedReqDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lTasks =
            TaskDefnUtils.getActualTasks( lRevisedReqDefn, lAircraft, RefEventStatusKey.ACTV );

      assertEquals( "No new task is created for the inventory based on the requirement", 0,
            lTasks.size() );

      assertEquals( "Terminate task remain unchanged", RefEventStatusKey.TERMINATE,
            EvtEventTable.findByPrimaryKey( lTerminateTask ).getEventStatus() );

   }


   /**
    * <pre>
    * Given I am a System Engineer
    * And there exists a requirement definition in revision status
    * And there exists a cancelled task for an inventory based on the requirement
    * When the requirement is activated
    * Then the task remains unchanged (remains cancelled and same revision number)
    * And a new task is created for the inventory based on the requirement that is active and has the new revision number
    * </pre>
    */
   @Test
   public void
         itDoesNotChangeCanceledTaskStatusAndCreatesNewTaskForTheInventorywhenTheRequirementActivated()
               throws Exception {
      // Set up support data.
      final PartNoKey lAircraftPart = Domain.createPart();

      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );

      final InventoryKey lAircraft = createAircraftInventory( lAircraftAssembly, lAircraftPart );

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( lAircraftAssembly, 0 );

      // Given a new the requirement definition
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setOnCondition( false );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      // Create Requirement with status of Cancel
      TaskKey lTerminateTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefnKey );
            aReq.setInventory( lAircraft );
            aReq.setStatus( RefEventStatusKey.CANCEL );
         }
      } );

      /* Revise Requirement Definition */
      final TaskTaskKey lRevisedReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setPreviousRevision( lReqDefnKey );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aReqDefn.setRevisionNumber( REVISION_2 );
               }
            } );

      // activate the requirement and emulate baseline sync
      new TaskDefnBean().activate( lRevisedReqDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      List<TaskKey> lActualTasks =
            TaskDefnUtils.getActualTasks( lRevisedReqDefn, lAircraft, RefEventStatusKey.ACTV );

      assertEquals( "A new task is created for the inventory based on the requirement", 1,
            lActualTasks.size() );

      assertEquals( "Cancelled task remain unchanged", RefEventStatusKey.CANCEL,
            EvtEventTable.findByPrimaryKey( lTerminateTask ).getEventStatus() );

   }


   /**
    * Given an assembly with an activated requirement<br/>
    * And an aircraft based on this assembly has an activated instance of that requirement<br/>
    * And the aircraft has a committed work package containing that activated instance<br/>
    * And a revision of this requirement has been created<br/>
    * When the requirement is obsoleted<br/>
    * Then the activated instance should remain in active status
    */
   @Test
   public void itKeepsTaskInCommittedWPActiveWhenReqObsoleted() throws Exception {
      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lActvRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setDefinition( lActvRequirementDefn );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setStatus( ACTV );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lActvRequirement );
            aWorkPackage.setStatus( RefEventStatusKey.COMMIT );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               }

            } );

      // When
      HumanResourceKey lHumanResourceKey = new HumanResourceDomainBuilder().build();
      new TaskDefnBean().obsolete( lRevisedRequirementDefn, new TaskDefnRevTO(),
            lHumanResourceKey );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then
      RefEventStatusKey lActualRequirementStatus =
            EvtEventTable.findByPrimaryKey( lActvRequirement ).getEventStatus();
      RefEventStatusKey lExpectedRequirementStatus = ACTV;
      assertEquals( "Unexpected requirement status: " + lActualRequirementStatus,
            lExpectedRequirementStatus, lActualRequirementStatus );
   }


   /**
    * Given an assembly with an activated requirement<br/>
    * And an aircraft based on this assembly has an activated instance of that requirement<br/>
    * And the aircraft has a committed work package containing that activated instance<br/>
    * When the requirement is revised and activated<br/>
    * Then the requirement instance is still based on the older version of REQ definition
    */
   @Test
   public void itKeepsTasksInCommittedWPBasedOnOlderReqDefnWhenReqRevised() throws Exception {
      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setCode( CONFIG_SLOT_ROOT );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lActvRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setDefinition( lRequirementDefn );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setStatus( ACTV );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lActvRequirement );
            aWorkPackage.setStatus( RefEventStatusKey.COMMIT );
         }
      } );

      // When
      TaskDefnBean lTaskDefBean = new TaskDefnBean();

      TaskTaskKey lRevisedReqirementDefn =
            lTaskDefBean.createRevision( lRequirementDefn, new TaskDefnRevTO() );
      lTaskDefBean.activate( lRevisedReqirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then
      TaskTaskKey lActualReqiurementDefn =
            SchedStaskTable.findByPrimaryKey( lActvRequirement ).getTaskTaskKey();
      TaskTaskKey lExpectedReuirementDefn = lRequirementDefn;

      assertEquals( "Requirement is based on incorrect definition: ", lExpectedReuirementDefn,
            lActualReqiurementDefn );
   }


   /**
    * Given an assembly with an activated requirement<br/>
    * And one aircraft based on this assembly with applicability code<br/>
    * And the requirement has an applicability range setting which contains this aircraft<br/>
    * And the aircraft has a committed work package containing an activated instance of that
    * requirement<br/>
    * When the applicability range of the requirement is revised to exclude this aircraft<br/>
    * Then the requirement instance is not cancelled
    *
    */
   @Test
   public void itKeepsTaskInCommittedWPWhenTheRevisedRangeMakesTheInventoryNonApplicable()
         throws Exception {
      final String TASK_DEFN_INITIAL_APPL_RANGE = "300-500";
      final String TASK_DEFN_REVISED_APPL_RANGE = "300-400";
      final String ACFT_APPLICABILITY_CODE = "405";

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.setCode( CONFIG_SLOT_ROOT );
                           }
                        } );
               }
            } );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setApplicabilityCode( ACFT_APPLICABILITY_CODE );
         }
      } );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_INITIAL_APPL_RANGE );
               }
            } );

      final TaskKey lActvRequirement =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aRequirement ) {
                  aRequirement.setDefinition( lRequirementDefn );
                  aRequirement.setInventory( lAircraft );
                  aRequirement.setStatus( ACTV );
               }
            } );

      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWorkPackage ) {
            aWorkPackage.setAircraft( lAircraft );
            aWorkPackage.addTask( lActvRequirement );
            aWorkPackage.setStatus( RefEventStatusKey.COMMIT );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aReqDefn.setApplicabilityRange( TASK_DEFN_REVISED_APPL_RANGE );
               }
            } );

      // When
      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      // Then
      TaskTaskKey lActualReqiurementDefn =
            SchedStaskTable.findByPrimaryKey( lActvRequirement ).getTaskTaskKey();
      TaskTaskKey lExpectedReuirementDefn = lRequirementDefn;

      assertEquals( "Requirement is based on incorrect definition: ", lExpectedReuirementDefn,
            lActualReqiurementDefn );

      RefEventStatusKey lActualRequirementStatus =
            EvtEventTable.findByPrimaryKey( lActvRequirement ).getEventStatus();
      RefEventStatusKey lExpectedRequirementStatus = ACTV;

      assertEquals( "Unexpected requirement status: ", lExpectedRequirementStatus,
            lActualRequirementStatus );
   }


   /**
    * Given requirement1 on ROOT configuration slot<br/>
    * And requirement2 on ROOT configuration slot<br/>
    * And requirement3 on ROOT configuration slot<br/>
    * And a job card on ROOT configuration slot<br/>
    * When add the job card to these three requirements<br/>
    * Then the job card is added to requirement1<br/>
    * And the job card is added to requirement2<br/>
    * And the job card is added to requirement3
    */
   @Test
   public void itAddsOneJicToMutipleReqs() throws Exception {
      // Given
      final AssemblyKey lAssembly = Domain.createAircraftAssembly();
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lRequirementDefn1 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lRootConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lRequirementDefn2 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lRootConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lRequirementDefn3 =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lRootConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lRootConfigSlot );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      List<TaskTaskKey> lRequirements =
            Arrays.asList( lRequirementDefn1, lRequirementDefn2, lRequirementDefn3 );
      new TaskDefnBean().addJicToReq( lJobCardDefn, lRequirements );

      // Then
      assertNotNull( "Fails to add job card to requirement 1.",
            getJicReqMap( lRequirementDefn1, lJobCardDefn ) );

      assertNotNull( "Fails to add job card to requirement 2.",
            getJicReqMap( lRequirementDefn2, lJobCardDefn ) );

      assertNotNull( "Fails to add job card to requirement 3.",
            getJicReqMap( lRequirementDefn3, lJobCardDefn ) );
   }


   /**
    * Given a requirement on ROOT configuration slot<br/>
    * And job card 1 on ROOT configuration slot<br/>
    * And job card 2 on ROOT configuration slot<br/>
    * And job card 3 on ROOT configuration slot<br/>
    * When add these job cards to the requirement<br/>
    * Then job card 1 is added to the requirement<br/>
    * And job card 2 is added to the requirement<br/>
    * And job card 3 is added to the requirement
    */
   @Test
   public void itAddsMultipleJicsToOneReq() throws Exception {
      // Given
      final AssemblyKey lAssembly = Domain.createAircraftAssembly();
      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lRootConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn1 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lRootConfigSlot );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      final TaskTaskKey lJobCardDefn2 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lRootConfigSlot );
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      final TaskTaskKey lJobCardDefn3 =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lRootConfigSlot );
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      List<TaskTaskKey> lJobCards = Arrays.asList( lJobCardDefn1, lJobCardDefn2, lJobCardDefn3 );
      new TaskDefnBean().addJicToReq( lRequirementDefn, lJobCards );

      assertNotNull( "Fails to add job card 1 to the requirement.",
            getJicReqMap( lRequirementDefn, lJobCardDefn1 ) );

      assertNotNull( "Fails to add job card 2 to the requirement.",
            getJicReqMap( lRequirementDefn, lJobCardDefn2 ) );

      assertNotNull( "Fails to add job card 3 to the requirement.",
            getJicReqMap( lRequirementDefn, lJobCardDefn3 ) );
   }


   /**
    * Given a requirement on TRK configuration slot 1<br/>
    * And a job card on the same TRK configuration slot 1<br/>
    * When add the job card to the requirement<br/>
    * Then the job card is added to the requirement
    *
    */
   @Test
   public void itAddsJicToReqWhenTheyAreOnTheSameTrkConfigSlot() throws Exception {
      // Given
      final AssemblyKey lAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              // add a TRK config slot 1 to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aTrkConfigSlot ) {
                                          aTrkConfigSlot.setCode( CONFIG_SLOT_TRK_1 );
                                          aTrkConfigSlot.setConfigurationSlotClass( TRK );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lTrkConfigSlot1 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_1 );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lTrkConfigSlot1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lTrkConfigSlot1 );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      new TaskDefnBean().addJicToReq( lJobCardDefn, Arrays.asList( lRequirementDefn ) );

      // Then
      assertNotNull( "Fails to add job card to requirement.",
            getJicReqMap( lRequirementDefn, lJobCardDefn ) );
   }


   /**
    * This test is to verify that a job card on SYS configuration slot can be assigned to a
    * requirement that is on ROOT configuration slot.
    *
    * Given a requirement on ROOT config slot<br/>
    * And a job card on SYS config slot<br/>
    * When add the job card to the requirement<br/>
    * Then the job card is added to the requirement
    *
    */
   @Test
   public void itAddsJicOnSysConfigSlotToReqOnRootConfigSlot() throws Exception {
      // Given
      final AssemblyKey lAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              // add a SYS config slot to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aSysConfigSlot ) {
                                          aSysConfigSlot.setCode( CONFIG_SLOT_SYS );
                                          aSysConfigSlot.setConfigurationSlotClass( SYS );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lRootConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lSysConfigSlot );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      new TaskDefnBean().addJicToReq( lJobCardDefn, Arrays.asList( lRequirementDefn ) );

      TaskJicReqMapTable lTaskJicReqMapTable = getJicReqMap( lRequirementDefn, lJobCardDefn );

      assertNotNull( "Fails to add job card to requirement.", lTaskJicReqMapTable );
   }


   /**
    * This test is to verify that a job card on ROOT configuration slot can be assigned to a
    * requirement that is on SYS configuration slot.
    *
    * Given a requirement on SYS config slot<br/>
    * And a job card on ROOT config slot<br/>
    * When add the job card to the requirement<br/>
    * Then the job card is added to the requirement
    *
    */
   @Test
   public void itAddsJicOnRootConfigSlotToReqOnSysConfigSlot() throws Exception {
      // Given
      final AssemblyKey lAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              // add a SYS config slot to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aSysConfigSlot ) {
                                          aSysConfigSlot.setCode( CONFIG_SLOT_SYS );
                                          aSysConfigSlot.setConfigurationSlotClass( SYS );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lSysConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lRootConfigSlot );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      new TaskDefnBean().addJicToReq( lJobCardDefn, Arrays.asList( lRequirementDefn ) );

      TaskJicReqMapTable lTaskJicReqMapTable = getJicReqMap( lRequirementDefn, lJobCardDefn );

      assertNotNull( "Fails to add job card to requirement.", lTaskJicReqMapTable );
   }


   /**
    * Given a requirement on TRK config slot 1<br/>
    * And a job card on TRK config slot 2<br/>
    * When add the job card to the requirement<br/>
    * Then SubTaskTRKOrSUBASSYMismatchException exception is thrown
    *
    */
   @Test( expected = SubTaskTRKOrSUBASSYMismatchException.class )
   public void itFailsToAddJicToReqWhenTheyAreNotOnTheSameTrkConfigSlot() throws Exception {
      // Given
      final AssemblyKey lAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              // add a TRK config slot 1 to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aTrkConfigSlot ) {
                                          aTrkConfigSlot.setCode( CONFIG_SLOT_TRK_1 );
                                          aTrkConfigSlot.setConfigurationSlotClass( TRK );
                                       }

                                    } );

                              // add a TRK config slot 2 to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aTrkConfigSlot ) {
                                          aTrkConfigSlot.setCode( CONFIG_SLOT_TRK_2 );
                                          aTrkConfigSlot.setConfigurationSlotClass( TRK );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lTrkConfigSlot1 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_1 );
      final ConfigSlotKey lTrkConfigSlot2 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_2 );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lTrkConfigSlot1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lTrkConfigSlot2 );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      lTaskDefnBean.addJicToReq( lJobCardDefn, Arrays.asList( lRequirementDefn ) );
   }


   /**
    * <pre>
    * Given a sub-assembly config slot on an aircraft.
    * And an in-revision requirement against that sub-assembly config slot
    * And a job card against the sub-assembly config slot on the aircraft
    * When the job card is attempted to be added to the requirement.
    * Then the job card against that same sub-assembly config slot as the requirement is added (JIC1).
    * </pre>
    *
    */
   @Test
   public void itAddsJicToReqWhenTheyAreOnTheSameSubAssemblyConfigSlot() throws Exception {

      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAcftAssembly -> aAcftAssembly.setRootConfigurationSlot( aRootCs -> {
               aRootCs.addConfigurationSlot( aCs -> {
                  aCs.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                  aCs.setCode( CONFIG_SLOT_SUBASSY );
               } );
            } ) );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SUBASSY );

      TaskTaskKey lReqDefnAgainstSubAssySlot = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lSubAssyConfigSlot );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
      } );

      TaskTaskKey lJicAgainstSubAssySlot = Domain.createJobCardDefinition( aJicDefn -> {
         aJicDefn.setConfigurationSlot( lSubAssyConfigSlot );
         aJicDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
      } );

      // When
      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      lTaskDefnBean.addJicToReq( lReqDefnAgainstSubAssySlot,
            Arrays.asList( lJicAgainstSubAssySlot ) );

      // Then
      assertNotNull( "Fails to add job card to requirement.",
            getJicReqMap( lReqDefnAgainstSubAssySlot, lJicAgainstSubAssySlot ) );
   }


   /**
    * <pre>
    * Given a sub-assembly config slot on an aircraft.
    * And an in-revision requirement against that sub-assembly config slot
    * And a job card not against the same sub-assembly config slot on the aircraft
    * When the job card is attempted to be added to the requirement.
    * Then SubTaskTRKOrSUBASSYMismatchException exception is thrown
    * </pre>
    *
    */
   @Test( expected = SubTaskTRKOrSUBASSYMismatchException.class )
   public void itFailsToAddJicToReqWhenTheyAreNotOnTheSameSubAssemblyConfigSlot() throws Exception {

      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAcftAssembly -> aAcftAssembly.setRootConfigurationSlot( aRootCs -> {
               aRootCs.addConfigurationSlot( aCs -> {
                  aCs.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                  aCs.setCode( CONFIG_SLOT_SUBASSY );
               } );
            } ) );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lSubAssyConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SUBASSY );

      TaskTaskKey lReqDefnAgainstSubAssySlot = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lSubAssyConfigSlot );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
      } );

      TaskTaskKey lJicAgainstSubAssySlot = Domain.createJobCardDefinition( aJicDefn -> {
         aJicDefn.setConfigurationSlot( lRootConfigSlot );
         aJicDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
      } );

      // When
      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      lTaskDefnBean.addJicToReq( lReqDefnAgainstSubAssySlot,
            Arrays.asList( lJicAgainstSubAssySlot ) );
   }


   /**
    * Given a requirement on SYS config slot<br/>
    * And a job card on TRK config slot 1<br/>
    * When add the job card to the requirement<br/>
    * Then SubTaskROOTOrSYSMismatchException exception is thrown
    *
    */
   @Test( expected = SubTaskROOTOrSYSMismatchException.class )
   public void itFailsToAddJicOnTrkConfigSlotToReqWhenReqIsOnSysConfigSlot() throws Exception {
      // Given
      final AssemblyKey lAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootConfigSlot ) {
                              // add a TRK config slot 1 to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aTrkConfigSlot ) {
                                          aTrkConfigSlot.setCode( CONFIG_SLOT_TRK_1 );
                                          aTrkConfigSlot.setConfigurationSlotClass( TRK );
                                       }

                                    } );

                              // add a SYS config slot to root slot
                              aRootConfigSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aSysConfigSlot ) {
                                          aSysConfigSlot.setCode( CONFIG_SLOT_SYS );
                                          aSysConfigSlot.setConfigurationSlotClass( SYS );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey lTrkConfigSlot1 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_1 );
      final ConfigSlotKey lSysConfigSlot =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_SYS );

      final TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lSysConfigSlot );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskTaskKey lJobCardDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJobCard ) {
                  aJobCard.setConfigurationSlot( lTrkConfigSlot1 );
                  // a job card definition may only be assigned to a requirement definition if it
                  // has a status of BUILD or REVISION
                  aJobCard.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }

            } );

      // When
      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      lTaskDefnBean.addJicToReq( lJobCardDefn, Arrays.asList( lRequirementDefn ) );
   }


   /**
    * Get a job card and requirement map record from table TASK_JIC_REQ_MAP. If a map relationship
    * is found, then it means the job card is assigned to the requirement. Otherwise, it is not
    * assigned to the requirement.
    *
    * @param aRequirementKey
    *           the requirement
    * @param aJobCardKey
    *           the job card
    * @return a row from TASK_JIC_REQ_MAP table
    */
   private TaskJicReqMapTable getJicReqMap( final TaskTaskKey aRequirementKey,
         final TaskTaskKey aJobCardKey ) {
      TaskTaskTable lRequirementDefn = TaskTaskTable.findByPrimaryKey( aRequirementKey );
      TaskJicReqMapKey lTaskJicReqMapKey =
            new TaskJicReqMapKey( aJobCardKey, lRequirementDefn.getTaskDefn() );
      TaskJicReqMapTable lTaskJicReqMap = TaskJicReqMapTable.findByPrimaryKey( lTaskJicReqMapKey );

      return lTaskJicReqMap;
   }


   /**
    * <pre>
    * Given that I am a System Engineer and I have revised a requirement definition
    * When I add a new technical reference
    * Then the technical reference should be added to the task after active the requirement definition
    * </pre>
    */
   @Test
   public void itAddsTechnicalReferenceToTask() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lActvTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( lActvRequirementDefn );
            aRequirement.setInventory( lAircraft );
            aRequirement.setStatus( ACTV );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition
                        .againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               }

            } );

      IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aIetm ) {
            aIetm.addTechnicalReference( new DomainConfiguration<TechnicalReference>() {

               @Override
               public void configure( TechnicalReference aTechRef ) {
                  aTechRef.setName( TECHNICAL_REFERENCE_NAME );
               }

            } );
         }

      } );
      IetmTopicKey lTechnicalReferenceKey =
            Domain.readTechnicalReference( lIetm, TECHNICAL_REFERENCE_NAME );

      new TaskDefnBean().assignTechnicalReferences( lRevisedRequirementDefn,
            new IetmTopicKey[] { lTechnicalReferenceKey } );

      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      assertTrue( "Expect technical reference added to actual task.",
            EvtIetm.isEventIetmExists( lActvTask.getEventKey(), lTechnicalReferenceKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer and I have revised a requirement and removed an existing
    * technical reference When I activate the requirement Then the technical reference should be
    * removed from the revised task
    *
    * <pre>
    */
   @Test
   public void itRemovesTechnicalReferenceFromTask() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aIetm ) {
            aIetm.addTechnicalReference( new DomainConfiguration<TechnicalReference>() {

               @Override
               public void configure( TechnicalReference aTechRef ) {
                  aTechRef.setName( TECHNICAL_REFERENCE_NAME );
               }

            } );
         }

      } );
      final IetmTopicKey lTechnicalReferenceKey =
            Domain.readTechnicalReference( lIetm, TECHNICAL_REFERENCE_NAME );
      final Map<IetmDefinitionKey, IetmTopicKey> lTechnicalReferences =
            new HashMap<IetmDefinitionKey, IetmTopicKey>();
      lTechnicalReferences.put( lIetm, lTechnicalReferenceKey );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.addIetmTechnicalReferences( lTechnicalReferences );
               }
            } );

      final TaskKey lActvTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( lActvRequirementDefn );
            aRequirement.setInventory( lAircraft );
            aRequirement.setStatus( ACTV );
            aRequirement.addTechnicalReference( lTechnicalReferenceKey );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition
                        .againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               }

            } );

      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      assertFalse( "Expect technical reference removed from actual task.",
            EvtIetm.isEventIetmExists( lActvTask.getEventKey(), lTechnicalReferenceKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer and I have revised a requirement definition
    * When I add a new attachment
    * Then the attachment should be added to the task after active the requirement definition
    * </pre>
    */
   @Test
   public void itAddsAttachmentToTask() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lActvTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( lActvRequirementDefn );
            aRequirement.setInventory( lAircraft );
            aRequirement.setStatus( ACTV );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition
                        .againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               }

            } );

      IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aIetm ) {
            aIetm.addAttachment( new DomainConfiguration<Attachment>() {

               @Override
               public void configure( Attachment aAttached ) {
                  aAttached.setName( ATTACHMENT_NAME );
               }

            } );
         }

      } );
      IetmTopicKey lAttachmentKey = Domain.readAttachment( lIetm, ATTACHMENT_NAME );

      new TaskDefnBean().assignTechnicalReferences( lRevisedRequirementDefn,
            new IetmTopicKey[] { lAttachmentKey } );

      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      assertTrue( "Expect attachment added to actual task.",
            EvtAttachTable.isAttachmentExists( lActvTask.getEventKey(), lAttachmentKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer and I have revised a requirement and removed an existing
    * attachment When I activate the requirement Then the attachment should be removed from the
    * revised task
    *
    * <pre>
    */
   @Test
   public void itRemovesAttachmentFromTask() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
         }
      } );

      IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aIetm ) {
            aIetm.addAttachment( new DomainConfiguration<Attachment>() {

               @Override
               public void configure( Attachment aAttached ) {
                  aAttached.setName( ATTACHMENT_NAME );
               }

            } );
         }

      } );
      final IetmTopicKey lAttachmentKey = Domain.readAttachment( lIetm, ATTACHMENT_NAME );
      final Map<IetmDefinitionKey, IetmTopicKey> lAttachments =
            new HashMap<IetmDefinitionKey, IetmTopicKey>();
      lAttachments.put( lIetm, lAttachmentKey );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.addIetmAttachments( lAttachments );
               }
            } );

      final TaskKey lActvTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( lActvRequirementDefn );
            aRequirement.setInventory( lAircraft );
            aRequirement.setStatus( ACTV );
            aRequirement.addAttachment( lAttachmentKey );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition
                        .againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
               }

            } );

      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      assertFalse( "Expect attachment removed from actual task.",
            EvtAttachTable.isAttachmentExists( lActvTask.getEventKey(), lAttachmentKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer and I have revised a requirement and added a new operator-specific technical reference
    * When I activate the requirement
    * Then the technical reference should be added to tasks on aircraft that have the specified operator
    * </pre>
    */
   @Test
   public void itAddsOperatorSpecificTechnicalReferenceToTask() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final CarrierKey lCarrierKey1 = Domain.createOperator( new DomainConfiguration<Operator>() {

         @Override
         public void configure( Operator aOperator ) {
            aOperator.setIATACode( "IA" );
            aOperator.setCarrierCode( "CARR1" );
         }
      } );

      final IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aBuilder ) {
            aBuilder.addTechnicalReference( new DomainConfiguration<TechnicalReference>() {

               @Override
               public void configure( TechnicalReference aTechRef ) {
                  aTechRef.setName( TECHNICAL_REFERENCE_NAME );
                  aTechRef.addOperator( lCarrierKey1 );
               }

            } );
         }

      } );
      final IetmTopicKey lTechnicalReferenceKey =
            Domain.readTechnicalReference( lIetm, TECHNICAL_REFERENCE_NAME );
      final Map<IetmDefinitionKey, IetmTopicKey> lTechnicalReferences =
            new HashMap<IetmDefinitionKey, IetmTopicKey>();
      lTechnicalReferences.put( lIetm, lTechnicalReferenceKey );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setOperator( lCarrierKey1 );
         }
      } );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lActvTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( lActvRequirementDefn );
            aRequirement.setInventory( lAircraft );
            aRequirement.setStatus( ACTV );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition
                        .againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aRequirementDefinition.addIetmTechnicalReferences( lTechnicalReferences );
               }

            } );

      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      assertTrue( "Expect operator specific technical reference added to actual task.",
            EvtIetm.isEventIetmExists( lActvTask.getEventKey(), lTechnicalReferenceKey ) );

   }


   /**
    * <pre>
    * Given that I am a System Engineer and I have revised a requirement and added a new operator-specific technical reference
    * When I activate the requirement
    * Then the technical reference should not be added to tasks on aircraft that have a different operator
    * </pre>
    */
   @Test
   public void itDoesNotAddOperatorSpecificTechnicalReferenceToAnotherOperatorTask()
         throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final CarrierKey lCarrierKey1 = Domain.createOperator( new DomainConfiguration<Operator>() {

         @Override
         public void configure( Operator aOperator ) {
            aOperator.setIATACode( "IA" );
            aOperator.setCarrierCode( "CARR1" );
         }
      } );

      final CarrierKey lCarrierKey2 = Domain.createOperator( new DomainConfiguration<Operator>() {

         @Override
         public void configure( Operator aOperator ) {
            aOperator.setIATACode( "IA" );
            aOperator.setCarrierCode( "CARR2" );
         }
      } );

      final IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aBuilder ) {
            aBuilder.addTechnicalReference( new DomainConfiguration<TechnicalReference>() {

               @Override
               public void configure( TechnicalReference aTechRef ) {
                  aTechRef.setName( TECHNICAL_REFERENCE_NAME );
                  aTechRef.addOperator( lCarrierKey2 );
               }

            } );
         }

      } );
      final IetmTopicKey lTechnicalReferenceKey =
            Domain.readTechnicalReference( lIetm, TECHNICAL_REFERENCE_NAME );
      final Map<IetmDefinitionKey, IetmTopicKey> lTechnicalReferences =
            new HashMap<IetmDefinitionKey, IetmTopicKey>();
      lTechnicalReferences.put( lIetm, lTechnicalReferenceKey );

      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssembly );
            aAircraft.allowSynchronization();
            aAircraft.setOperator( lCarrierKey1 );
         }
      } );

      final TaskTaskKey lActvRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aReqDefn.setCode( REQ_DEFN_CODE_1 );
                  aReqDefn.setRevisionNumber( REVISION_1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
               }
            } );

      final TaskKey lActvTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( lActvRequirementDefn );
            aRequirement.setInventory( lAircraft );
            aRequirement.setStatus( ACTV );
         }
      } );

      final TaskTaskKey lRevisedRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition
                        .againstConfigurationSlot( new ConfigSlotKey( lAircraftAssembly, 0 ) );
                  aRequirementDefinition.setPreviousRevision( lActvRequirementDefn );
                  aRequirementDefinition.setRevisionNumber( REVISION_2 );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aRequirementDefinition.addIetmTechnicalReferences( lTechnicalReferences );
               }

            } );

      new TaskDefnBean().activate( lRevisedRequirementDefn, new TaskDefnRevTO(), HR, true );

      InventorySynchronizer lInvSynchronizer = new InventorySynchronizer();
      lInvSynchronizer.processInventory( lInvSynchronizer.getInventory() );

      assertFalse( "Expect technical reference does not add to actual task of other operator.",
            EvtIetm.isEventIetmExists( lActvTask.getEventKey(), lTechnicalReferenceKey ) );

   }


   /**
    * Given an aircraft assembly with a track config slot <br />
    * Given a tracked inventory with a part number on the config slot <br />
    * And a requirement definition based on the part number with a state of REVISION <br />
    * When the requirement definition is activated <br />
    * Then a requirement task is initialized against the inventory for the requirement definition
    */
   @Test
   public void itCreatesRequirementInstanceFromRequirementDefinitionBasedOnPartNumber()
         throws Exception {
      final PartNoKey lPartNo = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInventoryClass( RefInvClassKey.TRK );

         }

      } );

      // Assembly is required by requesting a baseline sync
      Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.setCode( "ROOT" );
                  aBuilder.addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.setCode( "TRACKED" );
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.addPart( lPartNo );

                           }

                        } );

                     }

                  } );

               }

            } );

         }

      } );

      InventoryKey lTrackedInventory =
            Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

               @Override
               public void configure( TrackedInventory aBuilder ) {
                  aBuilder.setPartNumber( lPartNo );
                  aBuilder.setAllowSynchronization( true );

               }
            } );

      TaskTaskKey lRequirementDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aBuilder ) {
                  aBuilder.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aBuilder.addPartNo( lPartNo );
                  aBuilder.setScheduledFromEffectiveDate( new Date() );
               }

            } );

      TaskDefnBean lTaskDefnBean = new TaskDefnBean();

      lTaskDefnBean.activate( lRequirementDefn, new TaskDefnRevTO(), HR, false );

      InventorySynchronizer lInventorySynchronizer = new InventorySynchronizer();
      lInventorySynchronizer.processInventory( lInventorySynchronizer.getInventory() );

      QuerySet lQuerySet = QuerySetFactory.getInstance().executeQueryTable( "sched_stask",
            lRequirementDefn.getPKWhereArg() );
      assertThat( "Requirement instance is not created", lQuerySet.first(), equalTo( true ) );

      EventKey lEventKey = lQuerySet.getKey( EventKey.class, "sched_db_id", "sched_id" );

      assertThat( "Requirement instance is not associated with proper inventory",
            EvtInvTable.findByEventAndInventory( lEventKey, lTrackedInventory ), notNullValue() );
   }


   /**
    * <pre>
    *        Given a requirement definition
    *        and the requirement definition is not on-condition
    *        and the requirement definition is not part of a maintenance program
    *        and the requirement definition has a step with description
    *        when the requirement definition is initialized against an inventory
    *        then the requirement initialized against the inventory doesn't have step description
    * </pre>
    */
   @Test
   public void
         itDoesNotCopyStepDescriptionForExecutableRequirementBasedOnRequirementDefinitionWithStepsHavingDescription()
               throws Exception {
      // Given
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );

      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setPart( lAircraftPart );
            aBuilder.setAssembly( lAircraftAssembly );
         }
      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.setExecutable( true );
                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.setDescription( STEP_DESCRIPTION );
                     }
                  } );
               }
            } );

      // When
      TaskBean lTaskBean = new TaskBean();
      TaskKey lReq = lTaskBean.createFromTaskClass( lAcft, lReqDefn, null, HR );

      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lReq, "sched_db_id", "sched_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_step", lArgs );

      if ( !lQs.next() ) {
         fail( String.format(
               "Expected a requirement step to be created for Task with TaskKey = %s",
               lReq.toString() ) );
      }
      Assert.assertNull( "Unexpected step description, it should be blank",
            lQs.getString( "step_ldesc" ) );

   }


   /**
    * <pre>
    *        Given a requirement definition
    *        and the requirement definition is not on-condition
    *        and the requirement definition is not part of a maintenance program
    *        and the requirement definition has a job card definition
    *        and the job card definition has a step with description
    *        when the requirement definition is initialized against an inventory
    *        then the job card for the requirement initialized against the inventory doesn't have step description
    * </pre>
    */
   @Test
   public void
         itDoesNotCopyStepDescriptionForJobCardInRequirementDefinitionWithStepsHavingDescription()
               throws Exception {
      // Given
      final PartNoKey lAircraftPart = Domain.createPart();
      final AssemblyKey lAircraftAssembly = createAircraftAssembly( lAircraftPart );

      final InventoryKey lAcft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setPart( lAircraftPart );
            aBuilder.setAssembly( lAircraftAssembly );
         }
      } );

      final TaskTaskKey lJicDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aBuilder ) {
                  aBuilder.setConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aBuilder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aBuilder.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.setDescription( STEP_DESCRIPTION );
                     }
                  } );
               }
            } );

      // Given a one-time requirement definition.
      final TaskTaskKey lReqDefnKey =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.addJobCardDefinition( lJicDefn );
               }
            } );

      // When
      TaskBean lTaskBean = new TaskBean();
      TaskKey lReq = lTaskBean.createFromTaskClass( lAcft, lReqDefnKey, null, HR );

      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lReq, "nh_event_db_id", "nh_event_id" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "evt_event", lArgs );
      if ( !lQs.next() ) {
         fail( String.format( "Expected a JIC to be created for the Requirement with TaskKey = %s",
               lReq.toString() ) );
      }
      // Get the Jic for the Req
      TaskKey lJic = lQs.getKey( TaskKey.class, "event_db_id", "event_id" );
      lArgs.clear();
      lArgs.add( lJic, "sched_db_id", "sched_id" );
      lQs = QuerySetFactory.getInstance().executeQueryTable( "sched_step", lArgs );

      if ( !lQs.next() ) {
         fail( String.format( "Expected a JIC step to be created for JIC with TaskKey = %s",
               lReq.toString() ) );
      }
      Assert.assertNull( "Unexpected step description, it should be blank",
            lQs.getString( "step_ldesc" ) );

   }


   /**
    * <pre>
    *
    * GIVEN an aircraft assembly
    * AND a non-ACTV requirement definition against a config slot
    * AND a usage based scheduling rule against the requirement definition
    * WHEN the requirement definition is moved to a new config slot
    * that has the usage parameter on which the scheduling rule is based
    * THEN the requirement definition should be moved to the new config slot.
    *
    * </pre>
    */
   @Test
   public void itMovesReqDefnToAnotherConfigSlotWithUsageParamThatIsOnExistingConfigSlot()
         throws Exception {

      // Given
      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAcftAssembly -> aAcftAssembly.setRootConfigurationSlot( aRootCs -> {
               aRootCs.addConfigurationSlot( aCs -> {
                  aCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aCs.setCode( CONFIG_SLOT_TRK_1 );
                  aCs.addUsageParameter( DataTypeKey.CYCLES );
               } );
               aRootCs.addConfigurationSlot( aCs -> {
                  aCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aCs.setCode( CONFIG_SLOT_TRK_2 );
                  aCs.addUsageParameter( DataTypeKey.CYCLES );
               } );
            } ) );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lTrkConfigSlot1 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_1 );
      final ConfigSlotKey lTrkConfigSlot2 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_2 );

      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lTrkConfigSlot1 );
         // Code is needed by Duplicate Code exception validation
         aReqDefn.setCode( REQ_DEFN_CODE_1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
         aReqDefn.addSchedulingRule( DataTypeKey.CYCLES, BigDecimal.ONE );
      } );

      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      // When
      lTaskDefnBean.moveTaskDefinition( lReqDefn, lTrkConfigSlot2 );

      // Then
      ConfigSlotKey lActualReqConfigSlot = TaskTaskTable.findByPrimaryKey( lReqDefn ).getBomItem();
      Assert.assertThat( "Requirement definition was expected to be moved to CONFIG_SLOT_TRK 2",
            lActualReqConfigSlot, equalTo( lTrkConfigSlot2 ) );

   }


   /**
    * <pre>
    *
    * GIVEN an aircraft assembly
    * AND a non-ACTV requirement definition against a config slot
    * AND a usage based scheduling rule against the requirement definition
    * WHEN the requirement definition is moved to a new config slot
    * that doesn't have the usage parameter on which the scheduling rule is based
    * THEN BomItemUsageMismatchException is thrown.
    *
    * </pre>
    */
   @Test( expected = BomItemUsageMismatchException.class )
   public void itFailsToMoveReqDefnToAnotherConfigSlotWithUsageParamThatIsNotOnExistingConfigSlot()
         throws Exception {

      // Given
      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly(
            aAcftAssembly -> aAcftAssembly.setRootConfigurationSlot( aRootCs -> {
               aRootCs.addConfigurationSlot( aCs -> {
                  aCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aCs.setCode( CONFIG_SLOT_TRK_1 );
                  aCs.addUsageParameter( DataTypeKey.CYCLES );
               } );
               aRootCs.addConfigurationSlot( aCs -> {
                  aCs.setConfigurationSlotClass( RefBOMClassKey.TRK );
                  aCs.setCode( CONFIG_SLOT_TRK_2 );
                  aCs.addUsageParameter( DataTypeKey.HOURS );
               } );
            } ) );

      final ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAircraftAssembly );
      final ConfigSlotKey lTrkConfigSlot1 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_1 );
      final ConfigSlotKey lTrkConfigSlot2 =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_TRK_2 );

      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.againstConfigurationSlot( lTrkConfigSlot1 );
         // Code is needed by Duplicate Code exception validation
         aReqDefn.setCode( REQ_DEFN_CODE_1 );
         aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
         aReqDefn.addSchedulingRule( DataTypeKey.CYCLES, BigDecimal.ONE );
      } );

      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      lTaskDefnBean.moveTaskDefinition( lReqDefn, lTrkConfigSlot2 );

   }


   /**
    *
    * Verify we can Create a REPREF requirement
    *
    * <pre>
    * Given - an assembly
    * When  - when we attempt to create a requirement definition of type REPREF
    * Then  - a requirement definition of type REPREF is created
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itCreatesRowInTaskRepRefWhenUserCreatesRepRefTaskDefinition() throws Exception {

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      /* Required for validation for InvalidOrganizationAssignToUserException method */
      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.MRO ) );

      final HumanResourceKey lHr = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setOrganization( lOrgkey );
      } );

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );

      /* TO data setup */
      final RequirementTO lRequirementTO = new RequirementTO();
      lRequirementTO.setAssembly( lAircraftAssembly, TEST_ERROR_LABEL );
      lRequirementTO.setBomItem( Domain.readRootConfigurationSlot( lAircraftAssembly ) );
      lRequirementTO.setCode( "REPREFCODE", TEST_ERROR_LABEL );
      lRequirementTO.setTaskClass( RefTaskClassKey.REPREF, TEST_ERROR_LABEL );
      lRequirementTO.setOrganization( lOrgkey, TEST_ERROR_LABEL );
      lRequirementTO.setForecastRange( Double.valueOf( 10 ), TEST_ERROR_LABEL );

      final TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      final TaskTaskKey lTaskTaskKey = lTaskDefnBean.create( lRequirementTO, lHr );

      TaskRepRefDao lDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
      TaskRepRefTableRow lRow = lDao.findByTaskTaskKey( lTaskTaskKey );

      assertEquals( "Expects a row to be created in TASK_REP_REF table", lRow.getPk(),
            new TaskRepRefKey( lTaskTaskKey ) );
      assertFalse(
            "Expects the created REPREF entry to have a default MOCAuthorization value of FALSE",
            lRow.getMocApprovalBool() );
      assertTrue(
            "Expects the created REPREF entry to have a default DamageRecordRequired value of TRUE",
            lRow.getDamageRecordBool() );
      assertFalse(
            "Expects the created REPREF entry to have a default DamagedComponentRequired value of FALSE",
            lRow.getDamagedComponentBool() );
   }


   /**
    *
    * Verify we can Edit a REPREF requirement's details
    *
    * <pre>
    * Given -
    * When  -
    * Then  -
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itUpdatesRowInTaskRepRefTableWhenUserEditsRepRefDetails() throws Exception {

      /* Required for validation for InvalidOrganizationAssignToUserException method */
      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.MRO ) );

      final HumanResourceKey lHr = Domain.createHumanResource( aHr -> {
         aHr.setUser( Domain.createUser() );
         aHr.setOrganization( lOrgkey );
      } );

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );

      /* Creating REPREF requirement */
      TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setTaskClass( RefTaskClassKey.REPREF );
         aReqDefn.setOrganization( lOrgkey );
      } );

      /* TO setup data */
      RepairReferenceDetailsTO repRefTO = new RepairReferenceDetailsTO();
      repRefTO.setMocAuthorizationRequired( true );
      repRefTO.setDamagedComponentRequired( true );
      repRefTO.setDamageRecordRequired( true );
      repRefTO.setOperationalRestrictions( OPERATIONAL_RESTRICTION_MESSAGE );
      repRefTO.setPerformancePenalties( PERFORMANCE_PENALTIES_MESSAGE );

      final TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );

      lTaskDefnBean.updateRepairReferenceDetails( lReqDefn, repRefTO );

      TaskRepRefDao lDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
      TaskRepRefTableRow lRow = lDao.findByTaskTaskKey( lReqDefn );

      assertEquals( "Check that MOCAuthorization fields are the same", lRow.getMocApprovalBool(),
            repRefTO.isMocAuthorizationRequired() );
      assertEquals( "Check that DamagedComponentRequired fields are the same",
            lRow.getDamagedComponentBool(), repRefTO.isDamagedComponentRequired() );
      assertEquals( "Check that DamageRecordRequired fields are the same",
            lRow.getDamageRecordBool(), repRefTO.isDamageRecordRequired() );
      assertEquals( "Check that OperationalRestrictions fields are the same",
            lRow.getOperationalRestrictionsDescription(), repRefTO.getOperationalRestrictions() );
      assertEquals( "Check that PerformancePenalties fields are the same",
            lRow.getPerformancePenaltiesDescription(), repRefTO.getPerformancePenalties() );
   }


   /**
    *
    * <pre>
    *    Given a requirement definition of type repair reference in BUILD state
    *    When the requirement definition is attempted to be deleted
    *    Then the attributes of repair reference requirement definition are deleted.
    * </pre>
    */
   @Test
   public void itDeletesRepairReferenceAttributesOfRepairReferenceReqDefnInBuildState()
         throws Exception {

      Boolean lMocApprovalBool = false;

      AssemblyKey lAssembly = Domain.createAircraftAssembly();
      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      TaskTaskKey lRepairRefReqDefn = Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.setTaskClass( RefTaskClassKey.REPREF );
         aRequirementDefn.againstConfigurationSlot( lRootConfigSlot );
         aRequirementDefn.setMocApprovalBool( lMocApprovalBool );
         aRequirementDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
      } );

      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );
      lTaskDefnBean.delete( lRepairRefReqDefn, false );

      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lRepairRefReqDefn );
      assertThat( "Unexpectedly, the repair reference requirement definition didnt get deleted",
            lTaskTaskTable.exists(), equalTo( false ) );

      TaskRepRefDao lTaskRepRefDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
      TaskRepRefTableRow lTaskRepRefTableRow =
            lTaskRepRefDao.findByTaskTaskKey( lRepairRefReqDefn );
      assertThat( "Unexpectedly, the repair reference details didn't get deleted",
            lTaskRepRefTableRow.exists(), equalTo( false ) );
   }


   /**
    *
    * <pre>
    *    Given a requirement definition of type repair reference in REVISION state
    *    When the requirement definition is attempted to be deleted
    *    Then the attributes of repair reference requirement definition are deleted.
    * </pre>
    */
   @Test
   public void itDeletesRepairReferenceAttributesOfRepairReferenceReqDefnInRevisionState()
         throws Exception {

      Boolean lMocApprovalBool = false;

      AssemblyKey lAssembly = Domain.createAircraftAssembly();
      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      TaskTaskKey lRepairRefReqDefn = Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.setTaskClass( RefTaskClassKey.REPREF );
         aRequirementDefn.againstConfigurationSlot( lRootConfigSlot );
         aRequirementDefn.setMocApprovalBool( lMocApprovalBool );
         aRequirementDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
      } );

      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );
      lTaskDefnBean.delete( lRepairRefReqDefn, false );

      TaskTaskTable lTaskTaskTable = TaskTaskTable.findByPrimaryKey( lRepairRefReqDefn );
      assertThat( "Unexpectedly, the repair reference requirement definition didnt get deleted",
            lTaskTaskTable.exists(), equalTo( false ) );

      TaskRepRefDao lTaskRepRefDao = InjectorContainer.get().getInstance( TaskRepRefDao.class );
      TaskRepRefTableRow lTaskRepRefTableRow =
            lTaskRepRefDao.findByTaskTaskKey( lRepairRefReqDefn );
      assertThat( "Unexpectedly, the repair reference details didn't get deleted",
            lTaskRepRefTableRow.exists(), equalTo( false ) );
   }


   /**
    *
    * <pre>
    *    Given a requirement definition in Active state
    *    When the requirement definition is attempted to be deleted
    *    Then an exception is thrown preventing the deletion
    * </pre>
    */
   @Test( expected = InvalidTaskDefinitionStatusException.class )
   public void itThrowsExceptionWhenRequirementDefinitionIsAttemptedToBeDeletedInActiveState()
         throws Exception {

      AssemblyKey lAssembly = Domain.createAircraftAssembly();
      ConfigSlotKey lRootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      TaskTaskKey lRefReqDefn = Domain.createRequirementDefinition( aRequirementDefn -> {
         aRequirementDefn.againstConfigurationSlot( lRootConfigSlot );
         aRequirementDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
      } );

      TaskDefnBean lTaskDefnBean = new TaskDefnBean();
      lTaskDefnBean.setSessionContext( new SessionContextFake() );
      lTaskDefnBean.delete( lRefReqDefn, false );
   }


   private InventoryKey createAircraftInventory( final AssemblyKey aAircraftAssembly,
         final PartNoKey aPart ) {
      InventoryKey lAircraftInventoryKey =
            Domain.createAircraft( new DomainConfiguration<Aircraft>() {

               @Override
               public void configure( Aircraft aAircraft ) {
                  aAircraft.setAssembly( aAircraftAssembly );
                  aAircraft.setPart( aPart );
                  aAircraft.allowSynchronization();
               }
            } );

      return lAircraftInventoryKey;
   }


   private AssemblyKey createAircraftAssembly( final PartNoKey aPark ) {

      AssemblyKey lAircraftAssemblyKey =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot
                                    .addPartGroup( new DomainConfiguration<PartGroup>() {

                                       @Override
                                       public void configure( PartGroup aPartGroup ) {
                                          aPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                                          aPartGroup.addPart( aPark );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      return lAircraftAssemblyKey;
   }


   private RefEventStatusKey getEventStatus( TaskKey aTaskKey ) {

      RefEventStatusKey lRefEventStatusKey =
            EvtEventTable.findByPrimaryKey( aTaskKey ).getEventStatus();
      return lRefEventStatusKey;
   }

}
