package com.mxi.mx.core.ejb.taskdefn.taskdefnbean;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Advisory;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Ietm;
import com.mxi.am.domain.Impact;
import com.mxi.am.domain.LabourSkill;
import com.mxi.am.domain.Panel;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.PartRequirementDefinition;
import com.mxi.am.domain.PlanningType;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.domain.TechnicalReference;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.ejb.taskdefn.TaskDefnBean;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EqpPlanningTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartTransformationKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefTaskAdvisoryTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefWorkTypeKey;
import com.mxi.mx.core.key.TaskAdvisoryKey;
import com.mxi.mx.core.key.TaskPanelKey;
import com.mxi.mx.core.key.TaskParmDataKey;
import com.mxi.mx.core.key.TaskPlanningTypeSkillKey;
import com.mxi.mx.core.key.TaskSchedRuleKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskFlagsKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.TaskToolListKey;
import com.mxi.mx.core.key.TaskWorkTypeKey;
import com.mxi.mx.core.key.TaskZoneKey;
import com.mxi.mx.core.key.ZoneKey;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.services.taskdefn.transferobject.DuplicateTaskDefinitionTO;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.task.TaskAdvisoryTable;
import com.mxi.mx.core.table.task.TaskPanelTable;
import com.mxi.mx.core.table.task.TaskParmData;
import com.mxi.mx.core.table.task.TaskPartTransform;
import com.mxi.mx.core.table.task.TaskPlanningTypeSkillTable;
import com.mxi.mx.core.table.task.TaskSchedRule;
import com.mxi.mx.core.table.task.TaskStepSkillDao;
import com.mxi.mx.core.table.task.TaskStepSkillTable;
import com.mxi.mx.core.table.task.TaskStepTable;
import com.mxi.mx.core.table.task.TaskTaskFlagsTable;
import com.mxi.mx.core.table.task.TaskTaskTable;
import com.mxi.mx.core.table.task.TaskToolList;
import com.mxi.mx.core.table.task.TaskWorkTypeTable;
import com.mxi.mx.core.table.task.TaskZoneTable;
import com.mxi.mx.core.table.utl.UtlRole;


/**
 * Tests for {@linkplain TaskDefnBean#duplicate} that involve duplicating a requirement and checking
 * the various requirement attributes are copied over to the newly created requirement
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class DuplicateTest {

   private static final String ROOT_SLOT = "Root_Slot";
   private static final String REQ_DEFN_CODE = "REQ_DEFN_CODE";
   private static final String DUPLICATE_REQ_DEFN_NAME = "DUPLICATE_REQ_DEFN_NAME";
   private static final String DUPLICATE_REQ_DEFN_CODE = "DUPLICATE_REQ_DEFN_CODE";
   private static final String TRANSFER_OBJ_MSG = "TRANSFER_OBJ_MSG";
   private static final String TECHNICAL_REFERENCE_NAME = "TechRef";
   private static final String IMPACT_CODE = "TEST_IMPACT_CD";
   private static final String IMPACT_DESCRIPTION = "Impact Description";
   private static final String ADVISORY_DESCRIPTION = "ADVISORY_DESCRIPTION";
   private static final String APPLICABILITY_RANGE = "A01-A10";
   private static final String AIRCRAFT_ASSEMBLY_A_CODE = "ASM_A";
   private static final String AIRCRAFT_ASSEMBLY_B_CODE = "ASM_B";
   private static final String CONFIG_SLOT_A_CODE = "CONFIG_SLOT_A_CODE";
   private static final String CONFIG_SLOT_B_CODE = "CONFIG_SLOT_B_CODE";

   private HumanResourceKey iAuthorizingHr;
   private int iUserId;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String AET_SKILL = "AET";


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has usage based scheduling rules
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same usage
    */
   @Test
   public void itDuplicatesRequirementSchedulingRule() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
                  aReqDefn.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      DataTypeKey lExpectedTaskSchedRuleDataType = DataTypeKey.HOURS;
      BigDecimal lExpectedTaskSchedRuleThreshold = BigDecimal.TEN;

      TaskSchedRule lActualTaskSchedRule = TaskSchedRule
            .findByPrimaryKey( new TaskSchedRuleKey( lDuplicateReqDefn, DataTypeKey.HOURS ) );
      DataTypeKey lActualTaskSchedRuleDataType = lActualTaskSchedRule.getDataType();
      BigDecimal lActualTaskSchedRuleThreshold =
            new BigDecimal( lActualTaskSchedRule.getIntervalQt() );

      Assert.assertEquals(
            String.format( "Usage based scheduling rule wasn't duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedTaskSchedRuleDataType, lActualTaskSchedRuleDataType );
      Assert.assertEquals(
            String.format(
                  "Usage based scheduling rule threshold wasn't duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedTaskSchedRuleThreshold, lActualTaskSchedRuleThreshold );

   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement is in Build state
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement will be in Build state
    */
   @Test
   public void itDuplicatesRequirementWithCorrectStatus() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.BUILD );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then

      RefTaskDefinitionStatusKey lExpectedReqDefnStatus = RefTaskDefinitionStatusKey.BUILD;
      RefTaskDefinitionStatusKey lActualReqDefnStatus =
            TaskTaskTable.findByPrimaryKey( lDuplicateReqDefn ).getTaskDefStatus();

      Assert.assertEquals( "Duplicated Requirement Definition has incorrect status",
            lExpectedReqDefnStatus, lActualReqDefnStatus );

   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has applicability rules
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same
    * applicability rules
    */
   @Test
   public void itDuplicatesRequirementApplicabilityRule() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setApplicabilityRange( APPLICABILITY_RANGE );
                  aReqDefn.setApplicabilityRule( APPLICABILITY_RANGE );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      String lExpectedApplicabilityRule = APPLICABILITY_RANGE;
      String lExpectedApplicabilityRange = APPLICABILITY_RANGE;

      String lActualApplicabilityRule =
            TaskTaskTable.findByPrimaryKey( lDuplicateReqDefn ).getTaskApplSqlLdesc();
      String lActualApplicabilityRange =
            TaskTaskTable.findByPrimaryKey( lDuplicateReqDefn ).getTaskApplEffLdesc();

      Assert.assertEquals(
            String.format( "Applicability Range wasn't duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedApplicabilityRange, lActualApplicabilityRange );
      Assert.assertEquals(
            String.format( "Applicability Rule wasn't duplicated correctly from %s to %s", lReqDefn,
                  lDuplicateReqDefn ),
            lExpectedApplicabilityRule, lActualApplicabilityRule );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a panel
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same panel
    */
   @Test
   public void itDuplicatesRequirementPanel() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {
                  aBuilder.addPanelConfiguration( new DomainConfiguration<Panel>() {

                     @Override
                     public void configure( Panel aBuilder ) {
                        aBuilder.setCode( "Panel_Cd" );
                     }
                  } );

               }

            } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addPanel( "Panel_Cd" );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      PanelKey lExpectedPanelKey =
            TaskPanelTable.findByPrimaryKey( new TaskPanelKey( lReqDefn, 1 ) ).getPanel();
      PanelKey lActualPanelKey =
            TaskPanelTable.findByPrimaryKey( new TaskPanelKey( lDuplicateReqDefn, 1 ) ).getPanel();
      Assert.assertEquals( String.format( "Panel wasn't duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lExpectedPanelKey, lActualPanelKey );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a zone
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same zone
    */
   @Test
   public void itDuplicatesRequirementZone() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addZone( "Zone_Cd" );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      ZoneKey lExpectedZoneKey =
            TaskZoneTable.findByPrimaryKey( new TaskZoneKey( lReqDefn, 1 ) ).getZone();
      ZoneKey lActualZoneKey =
            TaskZoneTable.findByPrimaryKey( new TaskZoneKey( lDuplicateReqDefn, 1 ) ).getZone();
      Assert.assertEquals( String.format( "Zone wasn't duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lExpectedZoneKey, lActualZoneKey );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a TURN work type
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same work
    * type
    */
   @Test
   public void itDuplicatesRequirementWorkType() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addWorkType( RefWorkTypeKey.TURN );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      RefWorkTypeKey lExpectedWorkType = TaskWorkTypeTable
            .findByPrimaryKey( new TaskWorkTypeKey( lReqDefn, RefWorkTypeKey.TURN ) )
            .getWorkTypes( lReqDefn ).get( 0 );
      RefWorkTypeKey lActualWorkType = TaskWorkTypeTable
            .findByPrimaryKey( new TaskWorkTypeKey( lReqDefn, RefWorkTypeKey.TURN ) )
            .getWorkTypes( lDuplicateReqDefn ).get( 0 );
      Assert.assertEquals( String.format( "Work Type wasn't duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lExpectedWorkType, lActualWorkType );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a labor requirement
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same labor
    * requirement
    */
   @Test
   public void itDuplicatesRequirementLaborRequirement() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addLabourRequirement( RefLabourSkillKey.ENG, BigDecimal.TEN );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereIn( new String[] { "task_db_id", "task_id" }, lDuplicateReqDefn );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "task_labour_list", lArgs );
      lQs.next();

      RefLabourSkillKey lExpectedLabourSkill = RefLabourSkillKey.ENG;
      RefLabourSkillKey lActualLabourSkill = new RefLabourSkillKey(
            lQs.getInt( "labour_skill_db_id" ), lQs.getString( "labour_skill_cd" ) );

      BigDecimal lExpectedLabourWorkHours = BigDecimal.TEN;
      BigDecimal lActualLabourWorkHours = lQs.getBigDecimal( "work_perf_hr" );

      Assert.assertEquals( String.format( "Labour Skill wasn't duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lExpectedLabourSkill, lActualLabourSkill );
      Assert.assertEquals(
            String.format( "Labour Work Hour wasn't duplicated correctly from %s to %s", lReqDefn,
                  lDuplicateReqDefn ),
            lExpectedLabourWorkHours, lActualLabourWorkHours );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a tool requirement
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same tool
    * requirement
    */
   @Test
   public void itDuplicatesRequirementToolRequirement() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final PartNoKey lPart = Domain.createPart( toolPart -> {
         toolPart.setInDefaultPartGroup( true );
         toolPart.setInventoryClass( RefInvClassKey.SER );
         toolPart.setPartStatus( RefPartStatusKey.ACTV );
         toolPart.setTool( true );
      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addToolRequirement( lPart, BigDecimal.TEN );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      TaskToolListKey lReqTaskToolListKey = new TaskToolListKey( lReqDefn, 1 );
      TaskToolList lTaskToolList = TaskToolList.findByPrimaryKey( lReqTaskToolListKey );
      PartGroupKey lExpectedToolPartGroup = lTaskToolList.getBomPart();
      BigDecimal lExpectedSchedHours = new BigDecimal( lTaskToolList.getSchedHr() );

      TaskToolListKey lDuplicatedReqTaskToolListKey = new TaskToolListKey( lDuplicateReqDefn, 2 );
      lTaskToolList = TaskToolList.findByPrimaryKey( lDuplicatedReqTaskToolListKey );
      PartGroupKey lActualToolPartGroup = lTaskToolList.getBomPart();
      BigDecimal lActualSchedHours = new BigDecimal( lTaskToolList.getSchedHr() );

      Assert.assertEquals( String.format(
            "Tool Requirement Tool Part Group wasn't duplicated correctly from %s to %s", lReqDefn,
            lDuplicateReqDefn ), lExpectedToolPartGroup, lActualToolPartGroup );

      Assert.assertEquals( String.format(
            "Tool Requirement Sched Hours were not duplicated correctly from %s to %s", lReqDefn,
            lDuplicateReqDefn ), lExpectedSchedHours, lActualSchedHours );

   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a part requirement
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same part
    * requirement
    */
   @Test
   public void itDuplicatesRequirementPartRequirement() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final PartGroupKey lPartGroup = Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aBuilder ) {
            aBuilder.setInventoryClass( RefInvClassKey.SER );
         }
      } );

      final PartNoKey lPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInDefaultPartGroup( true );
            aBuilder.setInventoryClass( RefInvClassKey.SER );
            aBuilder.setPartGroup( lPartGroup, true );
         }
      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addPartRequirementDefinition(
                        new DomainConfiguration<PartRequirementDefinition>() {

                           @Override
                           public void configure( PartRequirementDefinition aBuilder ) {
                              aBuilder.setAction( RefReqActionKey.REQ );
                              aBuilder.setIsInstall( true );
                              aBuilder.setQuantity( Double.valueOf( 1 ) );
                              aBuilder.setPartGroup( lPartGroup );
                              aBuilder.setSpecificPart( lPart );
                           }

                        } );

               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      PartNoKey lExpectedPartRequirementPart = lPart;
      PartGroupKey lExpectedPartRequirementPartGroup = lPartGroup;
      int lExpectedTaskPartId = 1;
      RefReqActionKey lExpectedAction = RefReqActionKey.REQ;

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereIn( new String[] { "task_db_id", "task_id" }, lDuplicateReqDefn );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "task_part_list", lArgs );
      lQs.next();

      PartNoKey lActualPartRequirementPart =
            lQs.getKey( PartNoKey.class, new String[] { "spec_part_no_db_id", "spec_part_no_id" } );
      PartGroupKey lActualPartRequirementPartGroup =
            lQs.getKey( PartGroupKey.class, new String[] { "bom_part_db_id", "bom_part_id" } );
      int lActualTaskPartId = lQs.getInt( "task_part_id" );
      RefReqActionKey lActualAction = lQs.getKey( RefReqActionKey.class,
            new String[] { "req_action_db_id", "req_action_cd" } );

      Assert.assertEquals(
            String.format( "Part Requirement Part Group wasn't duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedPartRequirementPartGroup, lActualPartRequirementPartGroup );

      Assert.assertEquals(
            String.format( "Part Requirement part was not duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedPartRequirementPart, lActualPartRequirementPart );

      Assert.assertEquals( String.format(
            "Part Requirement task part id was not duplicated correctly from %s to %s", lReqDefn,
            lDuplicateReqDefn ), lExpectedTaskPartId, lActualTaskPartId );

      Assert.assertEquals(
            String.format( "Part Requirement action was not duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedAction, lActualAction );

   }


   /**
    * This test is to verify that when duplicating a requirement, its part transformation is copied
    * to the new requirement if exist any.
    *
    * Given a requirement of type MOD
    *
    * And the requirement has a part transformation added
    *
    * When duplicate the requirement
    *
    * Then the part transformation is duplicated to the new requirement
    *
    */
   @Test
   public void itDuplicatesRequirementPartTransformation() throws Exception {
      // Given
      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly();

      final PartNoKey lOldPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInDefaultPartGroup( true );
            aBuilder.setInventoryClass( RefInvClassKey.TRK );
         }
      } );

      final PartNoKey lNewPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInDefaultPartGroup( true );
            aBuilder.setInventoryClass( RefInvClassKey.TRK );
         }
      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setTaskClass( RefTaskClassKey.MOD );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addPartTransformation( lOldPart, lNewPart );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefnTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefnTO, iAuthorizingHr );

      // Then
      PartTransformationKey lTaskPartTransformKey =
            new PartTransformationKey( lDuplicateReqDefn, lOldPart );

      TaskPartTransform lTaskPartTransform =
            TaskPartTransform.findByPrimaryKey( lTaskPartTransformKey );

      Assert.assertNotNull( "Fails to duplicate the part transform to the new requirement.",
            lTaskPartTransform );

      PartNoKey lExpectedPartNo = lNewPart;
      PartNoKey lActualPartNo = lTaskPartTransform.getNewPartNo();

      Assert.assertEquals( "Wrong new part number, the dulicated part transform is not correct.",
            lExpectedPartNo, lActualPartNo );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a technical reference to an IETM
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same
    * technical reference
    */
   @Test
   public void itDuplicatesRequirementTechnicalReference() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final IetmDefinitionKey lIetm = Domain.createIetm( new DomainConfiguration<Ietm>() {

         @Override
         public void configure( Ietm aBuilder ) {
            aBuilder.setCode( "IETM1" );
            aBuilder.setName( "IETM" );
            aBuilder.addTechnicalReference( new DomainConfiguration<TechnicalReference>() {

               @Override
               public void configure( TechnicalReference aBuilder ) {
                  aBuilder.setName( TECHNICAL_REFERENCE_NAME );
                  aBuilder.setTaskContext( "TASK_CONTEXT" );
                  aBuilder.setApplicabilityRange( APPLICABILITY_RANGE );
               }

            } );
         }

      } );

      final Map<IetmDefinitionKey, IetmTopicKey> lTechnicalReferences =
            new HashMap<IetmDefinitionKey, IetmTopicKey>();
      lTechnicalReferences.put( lIetm,
            Domain.readTechnicalReference( lIetm, TECHNICAL_REFERENCE_NAME ) );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addIetmTechnicalReferences( lTechnicalReferences );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereIn( new String[] { "task_db_id", "task_id" }, lReqDefn );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "task_task_ietm", lArgs );
      lQs.next();

      int lExpectedTaskIetmId = lQs.getInt( "task_ietm_id" );
      IetmTopicKey lExpectedIetmTopicKey = lQs.getKey( IetmTopicKey.class,
            new String[] { "ietm_db_id", "ietm_id", "ietm_topic_id" } );
      int lExpectedIetmOrder = lQs.getInt( "ietm_ord" );

      lArgs.clear();
      lArgs.addWhereIn( new String[] { "task_db_id", "task_id" }, lDuplicateReqDefn );
      lQs = QuerySetFactory.getInstance().executeQueryTable( "task_task_ietm", lArgs );
      lQs.next();

      int lActualTaskIetmId = lQs.getInt( "task_ietm_id" );
      IetmTopicKey lActualIetmTopicKey = lQs.getKey( IetmTopicKey.class,
            new String[] { "ietm_db_id", "ietm_id", "ietm_topic_id" } );
      int lActualIetmOrder = lQs.getInt( "ietm_ord" );

      if ( !( lExpectedIetmOrder == lActualIetmOrder
            && lExpectedIetmTopicKey.equals( lActualIetmTopicKey )
            && lExpectedTaskIetmId == lActualTaskIetmId ) ) {
         Assert.fail( String.format(
               "Technical Reference action was not duplicated correctly from %s to %s", lReqDefn,
               lDuplicateReqDefn ) );
      }

   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a Measurement
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same
    * measurement
    */
   @Test
   public void itDuplicatesRequirementMeasurement() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final DataTypeKey lMeasurement = Domain.createMeasurementDefinition();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addMeasurement( lMeasurement );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then

      TaskParmDataKey lDuplicatedReqDefnTaskParmDataKey =
            new TaskParmDataKey( lDuplicateReqDefn, lMeasurement );
      TaskParmData lTaskParmData =
            TaskParmData.findByPrimaryKey( lDuplicatedReqDefnTaskParmDataKey );

      Assert.assertTrue( String.format( "Measurement was not duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lTaskParmData.exists() );

   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a step
    *
    * And the step has a skill
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same step and
    * skill
    */
   @Test
   public void itDuplicatesRequirementStepAndSkills() throws Exception {

      TaskStepSkillDao lTaskStepSkillDao =
            InjectorContainer.get().getInstance( TaskStepSkillDao.class );

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final RefLabourSkillKey lSkill =
            Domain.createLabourSkill( new DomainConfiguration<LabourSkill>() {

               @Override
               public void configure( LabourSkill aBuilder ) {
                  aBuilder.setCode( AET_SKILL );

               }
            } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addLabourRequirement( lSkill, BigDecimal.ZERO );
                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lSkill, true );
                        aBuilder.setDescription( "STEP" );
                        aBuilder.setApplicabilityRange( APPLICABILITY_RANGE );
                     }

                  } );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      TaskStepKey lOldTaskStepKey = new TaskStepKey( lReqDefn, 1 );
      TaskStepTable lTaskStepTable = TaskStepTable.findByPrimaryKey( lOldTaskStepKey );
      int lExpectedStepOrder = lTaskStepTable.getStepOrd();
      String lExpectedApplicabilityRange = lTaskStepTable.getApplRangeLdesc();
      String lExpectedStepDescription = lTaskStepTable.getStepLdesc();

      TaskStepKey lDuplicatedTaskStepKey = new TaskStepKey( lDuplicateReqDefn, 2 );
      lTaskStepTable = TaskStepTable.findByPrimaryKey( lDuplicatedTaskStepKey );
      int lActualStepOrder = lTaskStepTable.getStepOrd();
      String lActualApplicabilityRange = lTaskStepTable.getApplRangeLdesc();
      String lActualStepDescription = lTaskStepTable.getStepLdesc();

      TaskStepSkillTable lDuplicateTaskStepSkillTable = lTaskStepSkillDao
            .findByPrimaryKey( new TaskStepSkillKey( lDuplicatedTaskStepKey, lSkill ) );

      Assert.assertEquals( String.format( "Step Order was not duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lExpectedStepOrder, lActualStepOrder );
      Assert.assertEquals(
            String.format( "Step Applicability Range was not duplicated correctly from %s to %s",
                  lReqDefn, lDuplicateReqDefn ),
            lExpectedApplicabilityRange, lActualApplicabilityRange );
      Assert.assertEquals(
            String.format( "Step Description was not duplicated correctly from %s to %s", lReqDefn,
                  lDuplicateReqDefn ),
            lExpectedStepDescription, lActualStepDescription );
      Assert.assertNotNull( String.format( "Step skill was not duplicated correctly from %s to %s",
            lReqDefn, lDuplicateReqDefn ), lDuplicateTaskStepSkillTable );

   }


   /**
    * Given an aircraft assembly with an executable requirement
    *
    * And the requirement has a custom planning skills
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same custom
    * planning skills
    */
   @Test
   public void itDuplicatesRequirementPlanningTypeSkills() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final EqpPlanningTypeKey lCustomPlanningType =
            Domain.createPlanningType( new DomainConfiguration<PlanningType>() {

               @Override
               public void configure( PlanningType aPlanningType ) {
                  aPlanningType.setAssembly( lAircraftAssembly );
                  aPlanningType.setPlanningTypeCode( EqpPlanningTypeKey.CUSTOM );
               }
            } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setExecutable( true );
                  aReqDefn.setPlanningType( lCustomPlanningType );
                  aReqDefn.addCustomPlanningSkill( RefLabourSkillKey.ENG, new BigDecimal( 3.0 ) );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      TaskPlanningTypeSkillTable lCustomPlanningTypeSkill =
            TaskPlanningTypeSkillTable.findByPrimaryKey(
                  new TaskPlanningTypeSkillKey( lDuplicateReqDefn, RefLabourSkillKey.ENG ) );
      Assert.assertTrue(
            "Expect custom planning type skill copied from original requirement definition",
            lCustomPlanningTypeSkill.exists() );

   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has a impact
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same impact
    */
   @Test
   public void itDuplicatesRequirementImpacts() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final RefImpactKey lImpactKey = Domain.createImpact( new DomainConfiguration<Impact>() {

         @Override
         public void configure( Impact aImpact ) {
            aImpact.setImpactCode( IMPACT_CODE );
         }

      } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addImpact( lImpactKey, IMPACT_DESCRIPTION );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then

      Map<RefImpactKey, String> lImpacts = null;
      lImpacts = TaskDefnService.getImpact( lDuplicateReqDefn );

      Assert.assertEquals( "Expected impact added to task.", 1, lImpacts.size() );

      Assert.assertEquals( "Expected created impact added to task.", true,
            lImpacts.containsKey( lImpactKey ) );

      Assert.assertEquals( "Expected impact description added to task.", IMPACT_DESCRIPTION,
            lImpacts.get( lImpactKey ) );

   }


   /**
    * Given an aircraft assembly with a next shop visit requirement
    *
    * And the requirement is prevented execution
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is also a prevented execution
    * next shop visit one
    */
   @Test
   public void itDuplicatesRequirementFlags() throws Exception {
      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.setNextShopVisit( true );
                  aReqDefn.setPreventExecution( true );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      TaskTaskFlagsKey lTaskFlagKey = new TaskTaskFlagsKey( lDuplicateReqDefn );
      TaskTaskFlagsTable lTaskFlags = TaskTaskFlagsTable.findByPrimaryKey( lTaskFlagKey );
      Assert.assertTrue( "Expect task flags copied from original requirement definition",
            lTaskFlags.exists() );

      Assert.assertTrue( "Expect duplicated requirement definition is next shop visit",
            lTaskFlags.isNSVBool() );

      Assert.assertTrue( "Expect duplicated requirement definition is prevented execution",
            lTaskFlags.isPreventExeBool() );
   }


   /**
    * Given an aircraft assembly with a requirement
    *
    * And the requirement has an advisory
    *
    * When the requirement is duplicated
    *
    * Then a new duplicate requirement based on the former requirement is created with same advisory
    */
   @Test
   public void itDuplicatesRequirementAdvisory() throws Exception {

      // Given
      final AssemblyKey lAircraftAssembly = createAircraftAssembly();

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot(
                        Domain.readRootConfigurationSlot( lAircraftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addAdvisory( new DomainConfiguration<Advisory>() {

                     @Override
                     public void configure( Advisory aBuilder ) {
                        aBuilder.setNotes( ADVISORY_DESCRIPTION );
                        aBuilder.setType( RefTaskAdvisoryTypeKey.EXECUTE );
                     }

                  } );
               }
            } );

      // When
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO =
            createDuplicateTaskDefinitionTO( lAircraftAssembly );
      TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      // Then
      int lAdminRoleId = UtlRole.getRoleId( "ADMIN" ).getRoleId();
      TaskAdvisoryKey lTaskAdvisoryKey =
            new TaskAdvisoryKey( lDuplicateReqDefn, lAdminRoleId, RefTaskAdvisoryTypeKey.EXECUTE );

      TaskAdvisoryTable lActualTaskAdvisoryTable =
            TaskAdvisoryTable.findByPrimaryKey( lTaskAdvisoryKey );
      Assert.assertTrue( String.format(
            "Task Advisory was not duplicated from requirement: %s to requirement: %s", lReqDefn,
            lDuplicateReqDefn ), lActualTaskAdvisoryTable.exists() );
      Assert.assertEquals( String.format(
            "Task Advisory description was not duplicated from requirement: %s to requirement: %s",
            lReqDefn, lDuplicateReqDefn ), ADVISORY_DESCRIPTION,
            lActualTaskAdvisoryTable.getAdvisoryLdesc() );
   }


   /**
    *
    * It correctly creates a duplicate REQ definition on the same assembly, different config slot.
    *
    * <pre>
    * Given - an aircraft assembly with a root config slot AND
    *         the root config slot has config slot A and config slot B AND
    *         a REQ definition (with a task definition code) assigned to config slot A
    * When  - the REQ defn is duplicated with a different task definition code on config slot B
    * Then  - the duplicated REQ has the new task definition code and config slot, a task definition status of build,
    *         a revision of 1, and the same task definition name, and assembly as the original task definition
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itDuplicatesReqOnSameAssemblyDifferentConfigSlot() throws Exception {

      final AssemblyKey lAircraftAssemblyWithMultipleConfigSlots =
            createAircraftAssemblyWithMultipleConfigSlots( AIRCRAFT_ASSEMBLY_A_CODE,
                  CONFIG_SLOT_A_CODE, CONFIG_SLOT_B_CODE );

      final ConfigSlotKey lRootConfigSlot =
            Domain.readRootConfigurationSlot( lAircraftAssemblyWithMultipleConfigSlots );
      final ConfigSlotKey lConfigSlotA =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_A_CODE );
      final ConfigSlotKey lConfigSlotB =
            Domain.readSubConfigurationSlot( lRootConfigSlot, CONFIG_SLOT_B_CODE );

      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ_DEFN_CODE );
         aReqDefn.againstConfigurationSlot( lConfigSlotA );
      } );

      final DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO = createDuplicateTaskDefinitionTO(
            lAircraftAssemblyWithMultipleConfigSlots, lConfigSlotB, DUPLICATE_REQ_DEFN_CODE );

      final TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      TaskTaskTable lOrigTaskTaskTable = TaskTaskTable.findByPrimaryKey( lReqDefn );
      TaskTaskTable lDuplicatedTaskTaskTable = TaskTaskTable.findByPrimaryKey( lDuplicateReqDefn );

      assertThat( "The duplicate requirement definition was not created.",
            lDuplicatedTaskTaskTable.exists(), is( true ) );
      assertThat( "The duplicate requirement does not have the correct code.",
            lDuplicatedTaskTaskTable.getTaskCd(), is( DUPLICATE_REQ_DEFN_CODE ) );
      assertThat( "The duplicate requirement does not have the correct config slot.",
            lDuplicatedTaskTaskTable.getBomItem(), is( lConfigSlotB ) );
      assertThat( "The duplicate requirement does not have the correct status.",
            lDuplicatedTaskTaskTable.getTaskDefStatus(), is( RefTaskDefinitionStatusKey.BUILD ) );
      assertThat( "The duplicate requirement does not have the correct name.",
            lDuplicatedTaskTaskTable.getTaskName(), is( lOrigTaskTaskTable.getTaskName() ) );
      assertThat( "The duplicate requirement does not have the correct revision.",
            lDuplicatedTaskTaskTable.getRevisionOrd(), is( 1 ) );
      assertThat( "The duplicate requirement does not have the correct assembly.",
            lDuplicatedTaskTaskTable.getAssembly(), is( lOrigTaskTaskTable.getAssembly() ) );

   }


   /**
    *
    * It correctly creates a duplicate REQ definition on a different assembly.
    *
    * <pre>
    * Given - an aircraft assembly A AND
    *         an aircraft assembly B AND
    *         a REQ definition (with a task definition code) assigned to assembly  A
    * When  - the REQ defn is duplicated with a different task definition code on assembly B
    * Then  - the duplicated REQ has the new task definition code and assembly, a task definition status of build,
    *         a revision of 1, the config slot of assembly B, and the same task definition name as the original task definition
    * </pre>
    *
    * @throws Exception
    */
   @Test
   public void itDuplicatesReqOnDifferentAssembly() throws Exception {

      final AssemblyKey lAircraftAssemblyA = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( AIRCRAFT_ASSEMBLY_A_CODE );
      } );
      final AssemblyKey lAircraftAssemblyB = Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( AIRCRAFT_ASSEMBLY_B_CODE );
      } );

      final ConfigSlotKey lAircraftAssemblyBRootCs =
            Domain.readRootConfigurationSlot( lAircraftAssemblyB );

      final TaskTaskKey lReqDefn = Domain.createRequirementDefinition( aReqDefn -> {
         aReqDefn.setCode( REQ_DEFN_CODE );
         aReqDefn
               .againstConfigurationSlot( Domain.readRootConfigurationSlot( lAircraftAssemblyA ) );
      } );

      final DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO = createDuplicateTaskDefinitionTO(
            lAircraftAssemblyB, lAircraftAssemblyBRootCs, DUPLICATE_REQ_DEFN_CODE );

      final TaskTaskKey lDuplicateReqDefn =
            new TaskDefnBean().duplicate( lReqDefn, lDuplicateTaskDefinitionTO, iAuthorizingHr );

      TaskTaskTable lOrigTaskTaskTable = TaskTaskTable.findByPrimaryKey( lReqDefn );
      TaskTaskTable lDuplicatedTaskTaskTable = TaskTaskTable.findByPrimaryKey( lDuplicateReqDefn );

      assertThat( "The duplicate requirement definition was not created.",
            lDuplicatedTaskTaskTable.exists(), is( true ) );
      assertThat( "The duplicate requirement does not have the correct code.",
            lDuplicatedTaskTaskTable.getTaskCd(), is( DUPLICATE_REQ_DEFN_CODE ) );
      assertThat( "The duplicate requirement does not have the correct config slot.",
            lDuplicatedTaskTaskTable.getBomItem(), is( lAircraftAssemblyBRootCs ) );
      assertThat( "The duplicate requirement does not have the correct status.",
            lDuplicatedTaskTaskTable.getTaskDefStatus(), is( RefTaskDefinitionStatusKey.BUILD ) );
      assertThat( "The duplicate requirement does not have the correct name.",
            lDuplicatedTaskTaskTable.getTaskName(), is( lOrigTaskTaskTable.getTaskName() ) );
      assertThat( "The duplicate requirement does not have the correct revision.",
            lDuplicatedTaskTaskTable.getRevisionOrd(), is( 1 ) );
      assertThat( "The duplicate requirement does not have the correct assembly.",
            lDuplicatedTaskTaskTable.getAssembly(), is( lAircraftAssemblyB ) );

   }


   private DuplicateTaskDefinitionTO createDuplicateTaskDefinitionTO( AssemblyKey aAssemblyKey )
         throws Exception {
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO = new DuplicateTaskDefinitionTO();
      lDuplicateTaskDefinitionTO.setTaskName( DUPLICATE_REQ_DEFN_NAME, TRANSFER_OBJ_MSG );
      lDuplicateTaskDefinitionTO.setAssembly( aAssemblyKey, TRANSFER_OBJ_MSG );
      lDuplicateTaskDefinitionTO.setBOMItem( new ConfigSlotKey( aAssemblyKey, 0 ),
            TRANSFER_OBJ_MSG );
      lDuplicateTaskDefinitionTO.setTaskClass( RefTaskClassKey.REQ );
      lDuplicateTaskDefinitionTO.setTaskCode( DUPLICATE_REQ_DEFN_CODE, TRANSFER_OBJ_MSG );
      return lDuplicateTaskDefinitionTO;
   }


   private DuplicateTaskDefinitionTO createDuplicateTaskDefinitionTO( AssemblyKey aAssemblyKey,
         ConfigSlotKey aConfigSlotKey, String aTaskCode ) throws Exception {
      DuplicateTaskDefinitionTO lDuplicateTaskDefinitionTO = new DuplicateTaskDefinitionTO();
      lDuplicateTaskDefinitionTO.setAssembly( aAssemblyKey, TRANSFER_OBJ_MSG );
      lDuplicateTaskDefinitionTO.setBOMItem( aConfigSlotKey, TRANSFER_OBJ_MSG );
      lDuplicateTaskDefinitionTO.setTaskCode( aTaskCode, TRANSFER_OBJ_MSG );
      return lDuplicateTaskDefinitionTO;
   }


   private AssemblyKey createAircraftAssembly() {
      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAircraftAssembly ) {
            aAircraftAssembly
                  .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aRootCs ) {
                        aRootCs.setCode( ROOT_SLOT );
                        aRootCs.addUsageParameter( DataTypeKey.HOURS );
                     }
                  } );
         }
      } );
   }


   private AssemblyKey createAircraftAssemblyWithMultipleConfigSlots( String aAircraftAssemblyCode,
         String aFirstConfigSlotCode, String aSecondConfigSlotCode ) {
      return Domain.createAircraftAssembly( aAircraftAssembly -> {
         aAircraftAssembly.setCode( aAircraftAssemblyCode );
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
            aRootConfigSlot.addConfigurationSlot( aConfigSlot -> {
               aConfigSlot.setCode( aFirstConfigSlotCode );
            } );
            aRootConfigSlot.addConfigurationSlot( aConfigSlot -> {
               aConfigSlot.setCode( aSecondConfigSlotCode );
            } );
         } );
      } );
   }


   @Before
   public void setup() {

      iAuthorizingHr = new HumanResourceDomainBuilder().build();
      iUserId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();

      UserParametersFake lUserParametersFake = new UserParametersFake( iUserId, "LOGIC" );
      lUserParametersFake.setBoolean( "SPEC2000_UPPERCASE_ASSMBL_CD", false );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParametersFake );
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }

}
