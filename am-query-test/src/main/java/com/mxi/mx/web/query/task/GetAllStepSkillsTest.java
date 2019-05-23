package com.mxi.mx.web.query.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.SchedLabourStepKey;
import com.mxi.mx.core.key.SchedStepKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskStepSkillKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusAccessor;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourStepTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.table.sched.SchedStepTable;
import com.mxi.mx.core.table.task.JdbcTaskStepSkillDao;
import com.mxi.mx.core.table.task.TaskStepSkillTable;
import com.mxi.mx.core.task.model.StepStatus;


/**
 * This class tests the query com.mxi.mx.web.query.task.GetAllStepSkills.qrx
 *
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAllStepSkillsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private TaskTaskKey iTaskDefn;

   private HumanResourceKey iTechnician;

   private HumanResourceKey iCertifier;

   private HumanResourceKey iInspector;

   private String TECH_NAME = "TECH";

   private String CERT_NAME = "CERT";

   private String INSP_NAME = "INSP";

   private RefLabourSkillKey ENG_SKILL = RefLabourSkillKey.ENG;


   /**
    * Tests the step skill that is not yet signed off
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testNotSignedStepSkills() throws Exception {
      // DATA SETUP: Create requirement
      createRequirement();

      // DATA SETUP: Create an actual task based on the executable requirement above
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iTaskDefn );
         }
      } );

      QuerySet lQs = execute( lActualTask );

      // Asserts that there is a step skill that has not yet signed
      assertEquals( 1, lQs.getRowCount() );

      lQs.next();
      assertEquals( ENG_SKILL,
            lQs.getKey( RefLabourSkillKey.class, "labour_skill_db_id", "labour_skill_cd" ) );
      assertTrue( lQs.getBoolean( "req_ind_insp" ) );
      assertEquals( null, lQs.getString( "skill_status" ) );
      assertEquals( StepStatus.MXPENDING.getDisplayName(),
            lQs.getString( "default_step_skill_status" ) );
   }


   /**
    * Tests the step skill that is signed, certified and inspected and work capture edited.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testSignedStepSkills_WorkCapture_Edited() throws Exception {

      // DATA SETUP: Create requirement
      createRequirement();

      // DATA SETUP: Create an actual task based on the executable requirement above
      TaskKey lBaselineTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( iTaskDefn );
            aReq.addLabour( aLabour -> {
               aLabour.setSkill( ENG_SKILL );
               aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
               aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
               aLabour.setInspectorRole( insp -> insp.setScheduledHours( 2 ) );
            } );
         }
      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lBaselineTask, "sched_db_id", "sched_id" );
      lArgs.add( ENG_SKILL, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();
      SchedLabourKey lSchedLabourKey =
            lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lBaselineTask, lSchedLabourKey, 1, 1, StepStatus.MXPARTIAL );

      // TECH completed: captured work and edit work capture
      // sched_labour_role
      SchedLabourRoleTable lSchedLabourRole = SchedLabourRoleTable.create();
      lSchedLabourRole.setLabourRoleType( RefLabourRoleTypeKey.TECH );
      lSchedLabourRole.setSchedLabour( lSchedLabourKey );
      SchedLabourRoleKey llSchedLabourRoleKey = lSchedLabourRole.insert();

      // sched_labour_role_status
      SchedLabourRoleStatusTable lSchedLabourRoleStatus = SchedLabourRoleStatusTable.create();
      lSchedLabourRoleStatus.setSchedLabourRole( llSchedLabourRoleKey );
      lSchedLabourRoleStatus.setHr( iTechnician );
      lSchedLabourRoleStatus.setLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
      lSchedLabourRoleStatus.setStatusOrder( 1 );
      lSchedLabourRoleStatus.insert();

      // Simulate data for 3 times of Edit Work Capture: when we edit Work Capture another COMPLETE
      // labor role status added
      lSchedLabourRoleStatus = SchedLabourRoleStatusTable.create();
      lSchedLabourRoleStatus.setSchedLabourRole( llSchedLabourRoleKey );
      lSchedLabourRoleStatus.setHr( iTechnician );
      lSchedLabourRoleStatus.setLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
      lSchedLabourRoleStatus.setStatusOrder( 2 );
      lSchedLabourRoleStatus.insert();

      lSchedLabourRoleStatus = SchedLabourRoleStatusTable.create();
      lSchedLabourRoleStatus.setSchedLabourRole( llSchedLabourRoleKey );
      lSchedLabourRoleStatus.setHr( iTechnician );
      lSchedLabourRoleStatus.setLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
      lSchedLabourRoleStatus.setStatusOrder( 3 );
      lSchedLabourRoleStatus.insert();

      lSchedLabourRoleStatus = SchedLabourRoleStatusTable.create();
      lSchedLabourRoleStatus.setSchedLabourRole( llSchedLabourRoleKey );
      lSchedLabourRoleStatus.setHr( iTechnician );
      lSchedLabourRoleStatus.setLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
      lSchedLabourRoleStatus.setStatusOrder( 4 );
      lSchedLabourRoleStatus.insert();

      // Set a current status order of the labor to 4
      setCurrentStatusOrderForLabor( lSchedLabourKey, 4 );

      /*
       * END OF DATA SETUP
       */

      lQs = execute( lBaselineTask );

      // Asserts that there is a step skill is signed off
      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      assertEquals( ENG_SKILL,
            lQs.getKey( RefLabourSkillKey.class, "labour_skill_db_id", "labour_skill_cd" ) );
      assertTrue( lQs.getBoolean( "req_ind_insp" ) );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(), lQs.getString( "skill_status" ) );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(),
            lQs.getString( "default_step_skill_status" ) );
      assertEquals( "1. " + TECH_NAME + ", " + TECH_NAME, lQs.getString( "performed_by" ) );
   }


   /**
    * Tests the step skill that is signed, certified and inspected.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testSignedStepSkills() throws Exception {

      // DATA SETUP: Create requirement
      createRequirement();

      // DATA SETUP: Create an actual task based on the executable requirement above
      TaskKey lActualTask = Domain.createRequirement( ( Requirement aRequirementBuilder ) -> {

         aRequirementBuilder.setDefinition( iTaskDefn );
         aRequirementBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG_SKILL );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
            aLabour.setInspectorRole( insp -> insp.setScheduledHours( 2 ) );
         } );

      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( ENG_SKILL, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();
      SchedLabourKey lSchedLabour = lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour, 1, 1, StepStatus.MXPARTIAL );

      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour, iTechnician );

      signOffLabour( RefLabourRoleTypeKey.CERT, lSchedLabour, iCertifier );

      signOffLabour( RefLabourRoleTypeKey.INSP, lSchedLabour, iInspector );

      setCurrentStatusOrderForLabor( lSchedLabour, 1 );

      lQs = execute( lActualTask );

      // Asserts that there is a step skill is signed off
      assertEquals( 1, lQs.getRowCount() );

      lQs.next();

      assertEquals( ENG_SKILL,
            lQs.getKey( RefLabourSkillKey.class, "labour_skill_db_id", "labour_skill_cd" ) );
      assertTrue( lQs.getBoolean( "req_ind_insp" ) );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(), lQs.getString( "skill_status" ) );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(),
            lQs.getString( "default_step_skill_status" ) );
      assertEquals( "1. " + TECH_NAME + ", " + TECH_NAME, lQs.getString( "performed_by" ) );
      assertEquals( "1. " + CERT_NAME + ", " + CERT_NAME, lQs.getString( "certified_by" ) );
      assertEquals( "1. " + INSP_NAME + ", " + INSP_NAME, lQs.getString( "inspected_by" ) );
   }


   /**
    * Tests sign off multiple step skills
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testWorkPerformedNoteOrder() throws Exception {

      // DATA SETUP: Create a requirement definition with multiple steps
      createRequirementWithMultipleSteps();

      // DATA SETUP: Create an actual task based on the executable requirement above
      TaskKey lActualTask = Domain.createRequirement( ( Requirement aRequirementBuilder ) -> {

         aRequirementBuilder.setDefinition( iTaskDefn );
         aRequirementBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG_SKILL );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
         aRequirementBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG_SKILL );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );
         aRequirementBuilder.addLabour( aLabour -> {
            aLabour.setSkill( ENG_SKILL );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 4 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         } );

      } );

      // RETRIEVE DATA: Get SchedLabourkey
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lActualTask, "sched_db_id", "sched_id" );
      lArgs.add( ENG_SKILL, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "SCHED_LABOUR", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      assertEquals( 3, lQs.getRowCount() );

      // capture the first step
      lQs.next();
      SchedLabourKey lSchedLabour1 =
            lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour1, 1, 1, StepStatus.MXPARTIAL );
      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour1, iTechnician );

      SchedLabourTable lSchedLabourTable1 = SchedLabourTable.findByPrimaryKey( lSchedLabour1 );

      lSchedLabourTable1.setCurrentStatusOrder( 1 );
      lSchedLabourTable1.update();

      // capture the second step
      lQs.next();
      SchedLabourKey lSchedLabour2 =
            lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour2, 2, 1, StepStatus.MXPARTIAL );
      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour2, iTechnician );
      setCurrentStatusOrderForLabor( lSchedLabour2, 1 );

      // capture the first step again
      lQs.next();
      SchedLabourKey lSchedLabour3 =
            lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      captureStep( lActualTask, lSchedLabour3, 1, 2, StepStatus.MXPENDING );
      signOffLabour( RefLabourRoleTypeKey.TECH, lSchedLabour3, iTechnician );

      setCurrentStatusOrderForLabor( lSchedLabour3, 1 );

      DataSet lDs = execute( lActualTask );
      lDs.addSort( "dsString(step_ord)", true );

      // Asserts that there is a step skill is signed off
      assertEquals( 3, lDs.getRowCount() );

      lDs.beforeFirst();

      // Step 1 was captured so the Work Performed Number is 1
      lDs.next();

      assertEquals( lDs.getInt( "step_id" ), 1 );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(), lDs.getString( "skill_status" ) );
      assertEquals( StepStatus.MXPENDING.getDisplayName(),
            lDs.getString( "default_step_skill_status" ) );
      assertEquals( "1. " + TECH_NAME + ", " + TECH_NAME, lDs.getString( "performed_by" ) );

      // Step 1 was captured again so the Work Performed Number is 2
      lDs.next();

      assertEquals( lDs.getInt( "step_id" ), 1 );
      assertEquals( StepStatus.MXPENDING.getDisplayName(), lDs.getString( "skill_status" ) );
      assertEquals( StepStatus.MXPENDING.getDisplayName(),
            lDs.getString( "default_step_skill_status" ) );
      assertEquals( "2. " + TECH_NAME + ", " + TECH_NAME, lDs.getString( "performed_by" ) );

      // Step 2 was captured so the Work Performed Number is 1
      lDs.next();

      assertEquals( lDs.getInt( "step_id" ), 2 );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(), lDs.getString( "skill_status" ) );
      assertEquals( StepStatus.MXPARTIAL.getDisplayName(),
            lDs.getString( "default_step_skill_status" ) );
      assertEquals( "1. " + TECH_NAME + ", " + TECH_NAME, lDs.getString( "performed_by" ) );
   }


   private void setCurrentStatusOrderForLabor( SchedLabourKey lSchedLabour2, int aStatusOrder ) {
      SchedLabourTable lSchedLabourTable1;
      lSchedLabourTable1 = SchedLabourTable.findByPrimaryKey( lSchedLabour2 );

      lSchedLabourTable1.setCurrentStatusOrder( aStatusOrder );
      lSchedLabourTable1.update();
   }


   private void createRequirementWithMultipleSteps() {
      iTaskDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               private DomainConfiguration<Step> createStep1() {
                  return new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        aStep.setDescription( "Step 1 description" );
                        aStep.addStepSkill( ENG_SKILL, true );
                     }
                  };
               }


               private DomainConfiguration<Step> createStep2() {
                  return new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        aStep.setDescription( "Step 2 description" );
                        aStep.addStepSkill( ENG_SKILL, false );
                     }
                  };
               }


               @Override
               public void configure( RequirementDefinition aReqDefinition ) {

                  aReqDefinition.setExecutable( true );
                  aReqDefinition.addStep( createStep1() );
                  aReqDefinition.addStep( createStep2() );
               }
            } );

   }


   private void createRequirement() {

      // DATA SETUP: Create an executable requirement with steps with step skills
      iTaskDefn = Domain.createRequirementDefinition(
            ( RequirementDefinition aRequirementDefinitionBuilder ) -> {

               aRequirementDefinitionBuilder.setExecutable( true );
               aRequirementDefinitionBuilder.addStep( ( Step aStepBuilder ) -> {

                  aStepBuilder.setDescription( "Step description" );
                  aStepBuilder.addStepSkill( ENG_SKILL, true );
               } );
            } );
   }


   private void signOffLabour( RefLabourRoleTypeKey aLabourRoleType, SchedLabourKey aSchedLabour,
         HumanResourceKey aHr ) {

      signOffLabour( aLabourRoleType, aSchedLabour, aHr, RefLabourRoleStatusKey.COMPLETE, 1 );

   }


   private SchedLabourRoleKey signOffLabour( RefLabourRoleTypeKey aLabourRoleType,
         SchedLabourKey aSchedLabour, HumanResourceKey aHr,
         RefLabourRoleStatusKey aLabourRoleStatusKey, int aStatusOrder ) {

      SchedLabourRoleStatusAccessor lStatus =
            SchedLabourRoleStatusTable.findByForeignKey( aSchedLabour, aLabourRoleType );
      lStatus.setHr( aHr );
      lStatus.setLabourRoleStatus( aLabourRoleStatusKey );
      lStatus.update();

      SchedLabourRoleStatusTable lSchedLabourRoleStatusTable =
            SchedLabourRoleStatusTable.findByPrimaryKey( lStatus.getPk() );
      lSchedLabourRoleStatusTable.setStatusOrder( aStatusOrder );
      lSchedLabourRoleStatusTable.update();

      return lStatus.getSchedLabourRole();

   }


   private void captureStep( TaskKey aActualTask, SchedLabourKey aSchedLabour, int aStepOrd,
         int aOrdId, StepStatus aStepSkillStatus ) {

      // Get task skill from the task definition to the link to the actual step
      TaskStepKey lTaskStep = new TaskStepKey( iTaskDefn, aStepOrd );

      TaskStepSkillKey lTaskStepSkill = new TaskStepSkillKey( lTaskStep, ENG_SKILL );

      TaskStepSkillTable lTaskStepSkillTable =
            new JdbcTaskStepSkillDao().findByPrimaryKey( lTaskStepSkill );

      // Get the actual task step
      SchedStepKey lSchedStep = new SchedStepKey( aActualTask, aStepOrd );

      SchedLabourStepKey lSchedLabourStep = new SchedLabourStepKey( aSchedLabour, lSchedStep );

      SchedLabourStepTable lSchedLabourStepTable = SchedLabourStepTable.create( lSchedLabourStep );
      lSchedLabourStepTable.setStepSkillId( lTaskStepSkillTable.getAlternateKey() );
      lSchedLabourStepTable.setStepStatusId( aStepSkillStatus );
      lSchedLabourStepTable.setOrdId( aOrdId );
      lSchedLabourStepTable.insert();

      // Update the step status
      SchedStepTable lSchedStepTable = SchedStepTable.findByPrimaryKey( lSchedStep );
      lSchedStepTable.setStepStatus( aStepSkillStatus );
      lSchedStepTable.update();
   }


   /**
    * This method executes the query in GetAllStepSkills.qrx
    *
    *
    * @return The dataset after execution.
    */
   private DataSet execute( TaskKey aTask ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTask, "aSchedDbId", "aSchedId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   @Before
   public void setUp() throws Exception {
      iTechnician = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser( user -> {
            user.setUserId( 100 );
            user.setFirstName( TECH_NAME );
            user.setLastName( TECH_NAME );
         } ) );
      } );

      iCertifier = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser( user -> {
            user.setUserId( 101 );
            user.setFirstName( CERT_NAME );
            user.setLastName( CERT_NAME );
         } ) );
      } );

      iInspector = Domain.createHumanResource( hr -> {
         hr.setUser( Domain.createUser( user -> {
            user.setUserId( 102 );
            user.setFirstName( INSP_NAME );
            user.setLastName( INSP_NAME );
         } ) );
      } );
   }
}
