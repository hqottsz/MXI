package com.mxi.mx.core.services.taskdefn.exception;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

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


/**
 * Tests the behaviour of the LaborSkillNotMarkedAsInspRequiredExceptionTest.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class LaborSkillNotMarkedAsInspRequiredExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Ensure the validator throws a LaborSkillNotMarkedAsInspRequiredException when the labor skill
    * is not marked as certification required
    */
   @Test
   public void testLaborSkillNotMarkedAsInspRequiredException() throws MxException {

      // DATA SETUP: Create an executable requirement
      TaskTaskKey lTaskTask =
            new TaskRevisionBuilder().withTaskCode( "test" ).withTaskClass( RefTaskClassKey.REQ )
                  .isWorkscoped().build();

      // DATA SETUP: Add labor requirements to the task
      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.PILOT ).withCertRequired( true )
            .build();

      new LabourRequirementBuilder( lTaskTask, RefLabourSkillKey.ENG ).withCertRequired( true )
            .withInspRequired( true ).build();

      // DATA SETUP: Add Step
      new TaskStepBuilder().withTaskTask( lTaskTask ).build();

      Collection<RefLabourSkillKey> lStepSkills = new ArrayList<RefLabourSkillKey>();
      lStepSkills.add( RefLabourSkillKey.PILOT );

      try {
         LaborSkillNotMarkedAsInspRequiredException.validate( lTaskTask, lStepSkills );

         fail( "Expected LaborSkillNotMarkedAsInspRequiredException but it was not thrown." );

      } catch ( LaborSkillNotMarkedAsInspRequiredException e ) {
         ;
      }
   }

}
