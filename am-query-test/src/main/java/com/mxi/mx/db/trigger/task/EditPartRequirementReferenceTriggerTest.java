package com.mxi.mx.db.trigger.task;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.PartRequirement;
import com.mxi.am.domain.builder.PartRequirementBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxBeforeAndAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.maintenance.plan.externalreference.app.ExternalReferenceItemAppService;
import com.mxi.mx.integration.exceptions.IntegrationException;


/**
 * This class tests the Edit Part Requirement Reference trigger.
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class EditPartRequirementReferenceTriggerTest
      implements MxBeforeAndAfterTrigger<TaskPartKey> {

   private static Boolean beforeTriggerCalled;
   private static Boolean afterTriggerCalled;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private ExternalReferenceItemAppService externalReferenceItemAppService;
   private PartRequirement partRequirement;
   private TaskPartKey partRequirementKey;
   private static final String NEW_REFERENCE = "NEW REFERENCE";


   @Before
   public void setUp() {

      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      afterTriggerCalled = false;
      beforeTriggerCalled = false;
      TriggerFactory.setInstance( null );

      partRequirement = new PartRequirement();
      partRequirement.setTaskKey( new TaskKey( "4650:1" ) );
      partRequirementKey = PartRequirementBuilder.build( partRequirement );

   }


   @Test
   public void testEditPartRequirementReferenceTrigger()
         throws MxException, TriggerException, IntegrationException {
      externalReferenceItemAppService =
            InjectorContainer.get().getInstance( ExternalReferenceItemAppService.class );
      externalReferenceItemAppService.updatePartRequirementReference( partRequirementKey,
            NEW_REFERENCE );

      assertTrue( "Before Trigger[MX_EDIT_PART_REQUIREMENT_REFRENCE] did not get invoked",
            beforeTriggerCalled );

      assertTrue( "After Trigger[MX_EDIT_PART_REQUIREMENT_REFRENCE] did not get invoked",
            afterTriggerCalled );
   }


   /**
    * This method gets called when the after trigger is invoked
    */
   @Override
   public void after( TaskPartKey taskPartKey ) throws TriggerException {
      afterTriggerCalled = true;
   }


   /**
    * This method gets called when the before trigger is invoked
    */
   @Override
   public void before( TaskPartKey taskPartKey ) throws TriggerException {
      beforeTriggerCalled = true;

   }

}
