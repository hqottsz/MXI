package com.mxi.mx.core.services.stask.workcapture;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Labour;
import com.mxi.am.domain.LabourRequirement;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.stask.labour.LabourStepTO;


/**
 * Unit test suite for the {@link BaselinedTaskLaborStepSkillIndepInspValidator} class.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class BaselinedTaskLaborStepSkillIndepInspMatchValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   private LabourRequirement iLabourReq_No_Insp_Req;
   private LabourRequirement iLabourReq_Insp_Req;
   private TaskTaskKey iReqDefn;
   private DomainConfiguration<Labour> iLabour_No_Insp_Req;
   private DomainConfiguration<Labour> iLabour_Insp_Req;


   private void createRequirement() {

      // DATA SETUP: Create a requirement definition
      iReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.setTaskClass( RefTaskClassKey.REQ );
                  aReqDefn.setExecutable( true );
                  aReqDefn.addLabourRequirement( iLabourReq_No_Insp_Req );

                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aStep ) {
                        // With Indep Inspection Required
                        aStep.addStepSkill( iLabourReq_Insp_Req.getLabourSkill(), true );
                     }

                  } );
               }
            } );

   }


   private SchedLabourKey getSchedLabourKey( TaskKey aActualTask, RefLabourSkillKey aLabourSkill,
         boolean aInspRequired ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aActualTask, "sched_db_id", "sched_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );
      lArgs.add( "insp_bool", aInspRequired );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "sched_labour", lArgs,
            new String[] { "labour_db_id", "labour_id" } );

      lQs.next();

      SchedLabourKey lSchedLabourKey =
            lQs.getKey( SchedLabourKey.class, "labour_db_id", "labour_id" );

      return lSchedLabourKey;
   }


   private UUID getStepSkillId( TaskTaskKey aTaskDefn, RefLabourSkillKey aLabourSkill ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aTaskDefn, "task_db_id", "task_id" );
      lArgs.add( aLabourSkill, "labour_skill_db_id", "labour_skill_cd" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "task_step_skill", lArgs,
            new String[] { "step_skill_id" } );

      lQs.next();

      return lQs.getUuid( "step_skill_id" );
   }


   @Before
   public void setUp() {

      // Labour requirements (of the definitions).
      iLabourReq_No_Insp_Req = new LabourRequirement( RefLabourSkillKey.PILOT, new BigDecimal( 2 ),
            new BigDecimal( 2 ), null );

      iLabourReq_Insp_Req = new LabourRequirement( RefLabourSkillKey.PILOT, new BigDecimal( 2 ),
            new BigDecimal( 1 ), new BigDecimal( 1 ) );

      // Labour rows (of the tasks).
      iLabour_No_Insp_Req = new DomainConfiguration<Labour>() {

         @Override
         public void configure( Labour aLabour ) {
            aLabour.setSkill( RefLabourSkillKey.PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 2 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 2 ) );
         }
      };

      iLabour_Insp_Req = new DomainConfiguration<Labour>() {

         @Override
         public void configure( Labour aLabour ) {
            aLabour.setSkill( RefLabourSkillKey.PILOT );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 2 ) );
            aLabour.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
         }
      };

   }


   /**
    * Ensure the validator throws a BaselinedTaskLaborStepSkillIndepInspDoNotMatchException when the
    * when an indep inspection flag on step skill is set to ON then a indep insp flag on labor row
    * being finished or job stopped being certify are not matched
    *
    */
   @Test
   public void testBaselinedTaskIndepInspFlagsDoNotMatch() throws MxException {

      // DATA SETUP: Create requirement definition
      createRequirement();

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( iReqDefn );
            aRequirement.addLabour( iLabour_No_Insp_Req );
            aRequirement.addLabour( aNewLabourSkill -> {
               aNewLabourSkill.setSkill( RefLabourSkillKey.PILOT );
               aNewLabourSkill.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
               aNewLabourSkill.setCertifierRole( cert -> cert.setScheduledHours( 3 ) );
            } );
         }
      } );

      // DATA RETRIEVAL: Get the newly created labor row that does not require inspection
      SchedLabourKey lSchedLabour =
            getSchedLabourKey( lActualTask, RefLabourSkillKey.PILOT, false );

      try {
         // DATA RETRIEVAL: Get the task defn step skill key for ENG skill
         UUID lStepSkillId = getStepSkillId( iReqDefn, iLabourReq_Insp_Req.getLabourSkill() );

         // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG from another task
         // definition
         LabourStepTO lLabourStepTO = new LabourStepTO();
         lLabourStepTO.setStepSkillId( lStepSkillId );
         BaselinedTaskLaborStepSkillIndepInspValidator.validate( lSchedLabour,
               Arrays.asList( lLabourStepTO ) );

         fail( "Expected BaselinedTaskLaborStepSkillIndepInspDoNotMatchException but it was not thrown." );

      } catch ( BaselinedTaskLaborStepSkillIndepInspDoNotMatchException e ) {
         ;
      }
   }


   /**
    * Ensure the validator DOES NOT throw a BaselinedTaskLaborStepSkillIndepInspDoNotMatchException
    * when an indep inspection flag on step skill is set to ON then a indep insp flag on labor row
    * being finished or job stopped being certify are matched
    */
   @Test
   public void testBaselinedTaskInspFlagsMatch() throws MxException {

      // DATA SETUP: Create requirement definition
      createRequirement();

      // DATA SETUP: Create an actual task
      TaskKey lActualTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aRequirement ) {
            aRequirement.setDefinition( iReqDefn );
            aRequirement.addLabour( iLabour_Insp_Req );
            aRequirement.addLabour( aNewLabourSkill -> {
               aNewLabourSkill.setSkill( RefLabourSkillKey.PILOT );
               aNewLabourSkill.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
               aNewLabourSkill.setCertifierRole( cert -> cert.setScheduledHours( 1 ) );
               aNewLabourSkill.setInspectorRole( insp -> insp.setScheduledHours( 1 ) );
            } );
         }
      } );

      // DATA RETRIEVAL: Get the newly created labor row that does require inspection
      SchedLabourKey lSchedLabour = getSchedLabourKey( lActualTask, RefLabourSkillKey.PILOT, true );

      try {
         // DATA RETRIEVAL: Get the task defn step skill key for ENG skill
         UUID lStepSkillId = getStepSkillId( iReqDefn, iLabourReq_Insp_Req.getLabourSkill() );

         // DATA SETUP: Sign off labor row with ENG skill with job step skill ENG from another task
         // definition
         LabourStepTO lLabourStepTO = new LabourStepTO();
         lLabourStepTO.setStepSkillId( lStepSkillId );
         BaselinedTaskLaborStepSkillIndepInspValidator.validate( lSchedLabour,
               Arrays.asList( lLabourStepTO ) );

      } catch ( BaselinedTaskLaborStepSkillIndepInspDoNotMatchException e ) {

         fail( "Expected BaselinedTaskLaborStepSkillIndepInspDoNotMatchException is not thrown." );
      }
   }

}
