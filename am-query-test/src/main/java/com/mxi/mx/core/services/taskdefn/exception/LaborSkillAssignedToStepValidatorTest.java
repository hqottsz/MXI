package com.mxi.mx.core.services.taskdefn.exception;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.LabourRequirementBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.domain.builder.TaskStepBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.validator.LaborSkillAssignedToStepValidator;


/**
 * Tests the behaviour of the LaborSkillAssignedToStepValidator class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class LaborSkillAssignedToStepValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensure the validator throws a LabourSkillAssignedToStepException when the labor skill is not
    * marked as certification required
    */
   @Test
   public void testLabourSkillAssignedToStepException() throws MxException {

      // DATA SETUP: Create an executable requirement
      TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskCode( "test" ).withTaskClass( RefTaskClassKey.REQ )
                  .isWorkscoped().build();

      // DATA SETUP: Add labor requirements to the task
      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.PILOT ).withCertRequired( true )
            .withInspRequired( true ).build();

      // DATA SETUP: Add Step with skill
      new TaskStepBuilder().withTaskTask( lTaskTask ).withStepSkill( RefLabourSkillKey.PILOT, true )
            .build();

      try {
         LaborSkillAssignedToStepValidator.validate( lTaskTask, RefLabourSkillKey.PILOT );

         fail( "Expected LabourSkillAssignedToStepException but it was not thrown." );

      } catch ( LabourSkillAssignedToStepException e ) {
         ;
      }
   }

}
