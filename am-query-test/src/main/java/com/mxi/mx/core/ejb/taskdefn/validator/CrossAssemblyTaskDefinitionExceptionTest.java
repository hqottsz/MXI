package com.mxi.mx.core.ejb.taskdefn.validator;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.exception.CrossAssemblyTaskDefinitionException;


@RunWith( BlockJUnit4ClassRunner.class )
public class CrossAssemblyTaskDefinitionExceptionTest {

   private final String CONFIG_SLOT_TRK_CODE_1 = "TRACKED_CODE_1";
   private final String CONFIG_SLOT_TRK_CODE_2 = "TRACKED_CODE_2";
   private final String ASSEMBLY_CODE = "A320";
   private final String END_CODE = "ENGINE";
   private final String CONFIG_SLOT_ASSY = "ENG_CODE";
   private final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given a repair reference on a TRK config slot of an assembly And a follow on task definition
    * on a TRK config slot of engine assembly
    *
    * When I check if the follow on is valid for a CRT link to the repair reference
    *
    * Then there shall be no validation issue
    */
   @Test
   public void testIgnoreRepairReferenceType() throws Exception {

      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey aircraftTrkPart = Domain.createPart();
      final PartNoKey enginePart = Domain.createPart();
      final PartNoKey engineTrkPart = Domain.createPart();

      final AssemblyKey assembly = Domain.createAircraftAssembly( aircraftAssembly -> {

         aircraftAssembly.setCode( ASSEMBLY_CODE );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            rootConfigSlot.addPartGroup( aRootCsPartGroup -> {
               aRootCsPartGroup.setInventoryClass( ACFT );
               aRootCsPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( trkedConfigSlot -> {
               trkedConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               trkedConfigSlot.setCode( CONFIG_SLOT_TRK_CODE_1 );
               trkedConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                  trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                  trkConfigSlotPartGroup.addPart( aircraftTrkPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( assyConfigSlot -> {
               assyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               assyConfigSlot.setCode( CONFIG_SLOT_ASSY );
               assyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                  subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                  subAssyConfigSlotPartGroup.addPart( enginePart );
               } );
            } );
         } );

      } );

      final AssemblyKey lEngineAssembly = Domain.createEngineAssembly( enginetAssembly -> {
         enginetAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( END_CODE );
            rootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               rootConfigSlotPartGroup.addPart( enginePart );
            } );

            rootConfigSlot.addConfigurationSlot( trkConfigSlot -> {
               trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               trkConfigSlot.setCode( CONFIG_SLOT_TRK_CODE_2 );
               trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                  trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                  trkConfigSlotPartGroup.addPart( engineTrkPart );
               } );
            } );

         } );
      } );

      final ConfigSlotKey rootConfigSlot = Domain.readRootConfigurationSlot( assembly );
      final ConfigSlotKey aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( rootConfigSlot, CONFIG_SLOT_TRK_CODE_1 );

      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );
      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, CONFIG_SLOT_TRK_CODE_2 );

      final TaskTaskKey repairReferenceDefnKey =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( aircraftTrkConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
            } );

      final TaskTaskKey followOnDefnKey =
            Domain.createRequirementDefinition( aRequirementDefinition -> {
               aRequirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
               aRequirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               aRequirementDefinition.setOnCondition( true );
            } );

      try {
         CrossAssemblyTaskDefinitionException.validate( repairReferenceDefnKey, followOnDefnKey );
      } catch ( CrossAssemblyTaskDefinitionException e ) {

         fail( "CrossAssemblyTaskDefinitionException is not expected" );
      }

   }

}
