package com.mxi.mx.web.services.taskdefn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.SelectTaskDefinitionService;
import com.mxi.mx.web.jsp.controller.taskdefn.SelectTaskDefinitionController;
import com.mxi.mx.web.services.taskdefn.diff.StepsDataSetDiffService;


/**
 * Unit test suite for the {@link StepsDataSetDiffService} class.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class SelectTaskDefinitionServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ACFT_CS_CD = "ACFT";
   private static final String ACFT_SYS_CS_CD = "ACFTSYS";
   private static final String ACFT_TRK_CS_CD = "ACFTTRK";
   private static final String ACFT_SUBASSY_CS_CD = "ACSUB";
   private static final String ACFT_CS_CD2 = "ACFT2";
   private static final String ACFT_SUBASSY_CS_CD2 = "ACFTSUB2";

   private static final String ENG_CS_CD = "ENG";
   private static final String ENG_SYS_CS_CD = "ENGSYS";
   private static final String ENG_TRK_CS_CD = "ENGTRK";


   /**
    * <pre>
    * Given aircraft assembly has follow on task definitions on ROOT, SYS, TRK and SUBASSY config slots
    * When retrieving follow on task definitions for a repair reference task definition
    * Then these 4 follow on task definitions shall be all returned
    * </pre>
    */
   @Test
   public void itReturnsFollowOnTaskDefinitionsOnAircraftAssembly() {

      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.ADMIN ) );

      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey aircraftSysPart = Domain.createPart();
      final PartNoKey aircraftTrkPart = Domain.createPart();
      final PartNoKey enginePart = Domain.createPart();

      final AssemblyKey lAcftAssembly = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( ACFT_CS_CD );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ACFT_SYS_CS_CD );
               sysConfigSlot.addPartGroup( sysConfigSlotPartGroup -> {
                  sysConfigSlotPartGroup.setInventoryClass( RefInvClassKey.SYS );
                  sysConfigSlotPartGroup.addPart( aircraftSysPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( trkConfigSlot -> {
               trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               trkConfigSlot.setCode( ACFT_TRK_CS_CD );
               trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                  trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                  trkConfigSlotPartGroup.addPart( aircraftTrkPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {
               subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               subAssyConfigSlot.setCode( ACFT_SUBASSY_CS_CD );
               subAssyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                  subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                  subAssyConfigSlotPartGroup.addPart( enginePart );
               } );
            } );

         } );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAcftAssembly );

      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );

      final ConfigSlotKey aircraftTrkConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_TRK_CS_CD );

      final ConfigSlotKey aircraftSubassyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SUBASSY_CS_CD );

      final TaskTaskKey followOnTaskOnAcftRootTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftRootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey followOnTaskOnAcftSysTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey followOnTaskOnAcftTrkTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftTrkConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey followOnTaskOnAcftSubassyTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSubassyConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey lRepairRefDefn =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      DataSet lResultDs = new SelectTaskDefinitionService().getConfigSlotBasedTaskDefnDs(
            lRepairRefDefn, SelectTaskDefinitionController.MODE_SELECT_FOLLOWING_TASK,
            RefTaskClassKey.REPREF, null, RefTaskClassKey.FOLLOW.getCd(),
            SelectTaskDefinitionController.STARTS_WITH );

      assertNotNull( "Follow on task definitions are expected", lResultDs );

      Collection<TaskTaskKey> lFollowTaskKeys = new ArrayList<TaskTaskKey>();
      while ( lResultDs.next() ) {
         lFollowTaskKeys.add( lResultDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      }

      assertEquals( "4 Follow on task definitions are expected", 4, lFollowTaskKeys.size() );
      assertTrue( "Follow on task definitions on ACFT Root slot is expected",
            lFollowTaskKeys.contains( followOnTaskOnAcftRootTaskKey ) );
      assertTrue( "Follow on task definitions on ACFT SYS slot is expected",
            lFollowTaskKeys.contains( followOnTaskOnAcftSysTaskKey ) );
      assertTrue( "Follow on task definitions on ACFT TRK slot is expected",
            lFollowTaskKeys.contains( followOnTaskOnAcftTrkTaskKey ) );
      assertTrue( "Follow on task definitions on ACFT sub-assembly slot is expected",
            lFollowTaskKeys.contains( followOnTaskOnAcftSubassyTaskKey ) );
   }


   /**
    * <pre>
    * Given Engine assembly has follow on task definitions on ROOT, SYS, TRK config slots
    * When retrieving follow on task definitions for a repair reference task definition on an aircraft assembly
    * Then follow on task definitions defined on ROOT and TRK config slot shall be returned
    * </pre>
    */
   @Test
   public void itReturnsFollowOnTaskDefinitionsOnEngineAssembly() {

      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.ADMIN ) );

      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey aircraftSysPart = Domain.createPart();
      final PartNoKey enginePart = Domain.createPart();
      final PartNoKey engineSysPart = Domain.createPart();
      final PartNoKey engineTrkPart = Domain.createPart();

      final AssemblyKey lAcftAssembly = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( ACFT_CS_CD );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ACFT_SYS_CS_CD );
               sysConfigSlot.addPartGroup( sysConfigSlotPartGroup -> {
                  sysConfigSlotPartGroup.setInventoryClass( RefInvClassKey.SYS );
                  sysConfigSlotPartGroup.addPart( aircraftSysPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {
               subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               subAssyConfigSlot.setCode( ACFT_SUBASSY_CS_CD );
               subAssyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                  subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                  subAssyConfigSlotPartGroup.addPart( enginePart );
               } );
            } );
         } );
      } );

      final AssemblyKey lEngineAssembly = Domain.createEngineAssembly( enginetAssembly -> {
         enginetAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( ENG_CS_CD );
            rootConfigSlot.setConfigurationSlotClass( RefBOMClassKey.ROOT );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
               rootConfigSlotPartGroup.addPart( enginePart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ENG_SYS_CS_CD );
               sysConfigSlot.addPartGroup( sysConfigSlotPartGroup -> {
                  sysConfigSlotPartGroup.setInventoryClass( RefInvClassKey.SYS );
                  sysConfigSlotPartGroup.addPart( engineSysPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( trkConfigSlot -> {
               trkConfigSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               trkConfigSlot.setCode( ENG_TRK_CS_CD );
               trkConfigSlot.addPartGroup( trkConfigSlotPartGroup -> {
                  trkConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                  trkConfigSlotPartGroup.addPart( engineTrkPart );
               } );
            } );

         } );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAcftAssembly );

      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );

      final ConfigSlotKey engineRootConfigSlot =
            Domain.readRootConfigurationSlot( lEngineAssembly );

      final ConfigSlotKey engineSysConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_SYS_CS_CD );

      final ConfigSlotKey engineTrkConfigSlot =
            Domain.readSubConfigurationSlot( engineRootConfigSlot, ENG_TRK_CS_CD );

      final TaskTaskKey followOnTaskOnEngRootTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( engineRootConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey followOnTaskOnEngSysTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( engineSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey followOnTaskOnEngTrkTaskKey =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( engineTrkConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey lRepairRefDefn =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      DataSet lResultDs = new SelectTaskDefinitionService().getConfigSlotBasedTaskDefnDs(
            lRepairRefDefn, SelectTaskDefinitionController.MODE_SELECT_FOLLOWING_TASK,
            RefTaskClassKey.REPREF, null, RefTaskClassKey.FOLLOW.getCd(),
            SelectTaskDefinitionController.STARTS_WITH );

      assertNotNull( "Follow on task definitions are expected", lResultDs );

      Collection<TaskTaskKey> lFollowTaskKeys = new ArrayList<TaskTaskKey>();
      while ( lResultDs.next() ) {
         lFollowTaskKeys.add( lResultDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      }

      assertEquals( "2 Follow on task definitions are expected", 2, lFollowTaskKeys.size() );
      assertTrue( "Follow on task definitions on Engine Root slot is expected",
            lFollowTaskKeys.contains( followOnTaskOnEngRootTaskKey ) );
      assertFalse( "Follow on task definitions on Engine SYS slot is NOT expected",
            lFollowTaskKeys.contains( followOnTaskOnEngSysTaskKey ) );
      assertTrue( "Follow on task definitions on Engine TRK slot is expected",
            lFollowTaskKeys.contains( followOnTaskOnEngTrkTaskKey ) );
   }


   /**
    * <pre>
    * Given Two aircraft assemblies both have follow on task definitions on Engine sub-assebmly config slots
    * When retrieving follow on task definitions for a repair reference task definition on the first aircraft assembly
    * Then follow on task definitions defined on another aircraft engine sub-assembly config slot shall NOT be returned
    * </pre>
    */
   @Test
   public void itDoesNotReturnFollowOnTaskDefinitionsOnAnotherAircraftAssembly() {

      final OrgKey lOrgkey = Domain
            .createOrganization( aOrganization -> aOrganization.setType( RefOrgTypeKey.ADMIN ) );

      final PartNoKey aircraftPart = Domain.createPart();
      final PartNoKey aircraftSysPart = Domain.createPart();
      final PartNoKey enginePart = Domain.createPart();
      final PartNoKey aircraft2Part = Domain.createPart();

      final AssemblyKey lAcftAssembly = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( ACFT_CS_CD );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraftPart );
            } );

            rootConfigSlot.addConfigurationSlot( sysConfigSlot -> {
               sysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               sysConfigSlot.setCode( ACFT_SYS_CS_CD );
               sysConfigSlot.addPartGroup( sysConfigSlotPartGroup -> {
                  sysConfigSlotPartGroup.setInventoryClass( RefInvClassKey.SYS );
                  sysConfigSlotPartGroup.addPart( aircraftSysPart );
               } );
            } );

            rootConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {
               subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               subAssyConfigSlot.setCode( ACFT_SUBASSY_CS_CD );
               subAssyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                  subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                  subAssyConfigSlotPartGroup.addPart( enginePart );
               } );
            } );

         } );
      } );

      final AssemblyKey lAcftAssembly2 = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setCode( ACFT_CS_CD2 );
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {

            rootConfigSlot.setCode( ACFT_CS_CD2 );
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( aircraft2Part );
            } );

            rootConfigSlot.addConfigurationSlot( subAssyConfigSlot -> {
               subAssyConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
               subAssyConfigSlot.setCode( ACFT_SUBASSY_CS_CD2 );
               subAssyConfigSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                  subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ASSY );
                  subAssyConfigSlotPartGroup.addPart( enginePart );
               } );
            } );

         } );
      } );

      final ConfigSlotKey aircraftRootConfigSlot =
            Domain.readRootConfigurationSlot( lAcftAssembly );

      final ConfigSlotKey aircraftSysConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SYS_CS_CD );

      final ConfigSlotKey aircraftSubassyConfigSlot =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot, ACFT_SUBASSY_CS_CD );

      final ConfigSlotKey aircraftRootConfigSlot2 =
            Domain.readRootConfigurationSlot( lAcftAssembly2 );

      final ConfigSlotKey aircraftSubassyConfigSlot2 =
            Domain.readSubConfigurationSlot( aircraftRootConfigSlot2, ACFT_SUBASSY_CS_CD2 );

      Domain.createRequirementDefinition( requirementDefinition -> {
         requirementDefinition.againstConfigurationSlot( aircraftSubassyConfigSlot );
         requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
         requirementDefinition.setOrganization( lOrgkey );
      } );

      final TaskTaskKey followOnTaskOnAcftSubassyTaskKey2 =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSubassyConfigSlot2 );
               requirementDefinition.setTaskClass( RefTaskClassKey.FOLLOW );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      final TaskTaskKey lRepairRefDefn =
            Domain.createRequirementDefinition( requirementDefinition -> {
               requirementDefinition.againstConfigurationSlot( aircraftSysConfigSlot );
               requirementDefinition.setTaskClass( RefTaskClassKey.REPREF );
               requirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
               requirementDefinition.setOrganization( lOrgkey );
            } );

      DataSet lResultDs = new SelectTaskDefinitionService().getConfigSlotBasedTaskDefnDs(
            lRepairRefDefn, SelectTaskDefinitionController.MODE_SELECT_FOLLOWING_TASK,
            RefTaskClassKey.REPREF, null, RefTaskClassKey.FOLLOW.getCd(),
            SelectTaskDefinitionController.STARTS_WITH );

      assertNotNull( "Follow on task definitions are expected", lResultDs );

      Collection<TaskTaskKey> lFollowTaskKeys = new ArrayList<TaskTaskKey>();
      while ( lResultDs.next() ) {
         lFollowTaskKeys.add( lResultDs.getKey( TaskTaskKey.class, "task_defn_key" ) );
      }

      assertEquals( "1 Follow on task definitions are expected", 1, lFollowTaskKeys.size() );
      assertFalse( "Follow on task definitions on 2nd aircraft sub-assembly slot is NOT expected",
            lFollowTaskKeys.contains( followOnTaskOnAcftSubassyTaskKey2 ) );
   }
}
