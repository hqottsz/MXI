package com.mxi.mx.core.ejb.taskdefn.validator;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDepActionKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.exception.FollowingTaskOnInvalidConfigSlotValidator;


@RunWith( BlockJUnit4ClassRunner.class )
public class FollowingTaskOnInvalidConfigSlotValidatorTest {

   private final String CONFIG_SLOT_TRK_CODE_1 = "TRACKED_CODE_1";
   private final String CONFIG_SLOT_TRK_CODE_2 = "TRACKED_CODE_2";
   private final String ASSEMBLY_CODE = "A320";
   private final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";

   private FollowingTaskOnInvalidConfigSlotValidator iValidator;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iValidator = new FollowingTaskOnInvalidConfigSlotValidator();
   }


   /**
    * Given a repair reference on a TRK config slot of an assembly And a follow on task definition
    * on another TRK config slot of the assembly
    *
    * When I check if the follow on is valid for a CRT link to the repair reference
    *
    * Then there shall be no validation issue
    */
   @Test
   public void testIgnoreRepairReferenceType() throws Exception {

      final PartNoKey aircraftPart = Domain.createPart();
      final AssemblyKey lAssembly = Domain.createAircraftAssembly( aAircraftAssembly -> {

         aAircraftAssembly.setCode( ASSEMBLY_CODE );
         aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {

            aRootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            aRootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aircraftPart );
            } );

            aRootConfigSlot.addConfigurationSlot( aTrkedConfigSlot -> {
               aTrkedConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               aTrkedConfigSlot.setCode( CONFIG_SLOT_TRK_CODE_1 );
            } );
            aRootConfigSlot.addConfigurationSlot( aTrkedConfigSlot -> {
               aTrkedConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               aTrkedConfigSlot.setCode( CONFIG_SLOT_TRK_CODE_2 );
            } );
         } );

      } );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( lAssembly );
      final ConfigSlotKey trkConfigSlot1 =
            Domain.readSubConfigurationSlot( rootConfigSlot, CONFIG_SLOT_TRK_CODE_1 );
      final ConfigSlotKey trkConfigSlot2 =
            Domain.readSubConfigurationSlot( rootConfigSlot, CONFIG_SLOT_TRK_CODE_2 );

      final TaskTaskKey repairReferenceDefnKey =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( trkConfigSlot1 );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
            } );

      final TaskTaskKey followOnDefnKey =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( trkConfigSlot2 );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               aRequirementDefinition.setOnCondition( true );
            } );

      final Messages messages = new Messages( true );
      iValidator.checkThat( messages, RefTaskDepActionKey.CRT, repairReferenceDefnKey,
            followOnDefnKey );

      assertFalse( "Repair Reference Type shall not be checked for config slot",
            ( messages.hasErrors() || messages.hasWarnings() ) );

   }

}
