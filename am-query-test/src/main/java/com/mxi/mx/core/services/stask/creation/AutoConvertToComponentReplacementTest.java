package com.mxi.mx.core.services.stask.creation;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.TaskDefinitionPartRequirement;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.ConfigurationSlotBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefRemoveReasonKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskMustRemoveKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedStaskTable;


@RunWith( BlockJUnit4ClassRunner.class )
public class AutoConvertToComponentReplacementTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private InventoryKey iInventory;
   private TaskTaskKey iTaskTask;
   private HumanResourceKey iAuthorizingHr;


   @Before
   public void loadData() {
      // create organization
      final OrgKey lOrgKey = createOrganization( null, RefOrgTypeKey.ADMIN );

      // create location
      final LocationKey lLocationKey = Domain.createLocation();

      // Set up the user
      iAuthorizingHr = createUser( lOrgKey, lLocationKey );

      // create an aircraft assembly and inventory
      final AssemblyKey lAircraftAssembly = createAircraft( lLocationKey );
      InventoryKey lAircraftInventory = createAircraftInventory( lAircraftAssembly, lLocationKey );

      // create config slots
      final ConfigSlotKey lRootConfigSlot = EqpAssmblBom.getBomItemKey( "ACFT", "ACFT-1" );
      final ConfigSlotKey lSysConfigSlot = createConfigSlot( "SYS-1", RefBOMClassKey.SYS,
            lAircraftAssembly, "POS-1", lRootConfigSlot, "SYSCFG1" );
      final ConfigSlotKey lTrkConfigSlot2 = createConfigSlot( "SYS-2", RefBOMClassKey.TRK,
            lAircraftAssembly, "POS-2", lSysConfigSlot, "TRKCFG2" );

      final ConfigSlotKey lTrkConfigSlot3 = createConfigSlot( "SYS-3", RefBOMClassKey.TRK,
            lAircraftAssembly, "POS-3", lTrkConfigSlot2, "TRKCFG3" );

      // create part groups for the tracked config slots
      final PartGroupKey lPartGroup2 = createPartGroup( "PG1", lTrkConfigSlot2, "PG-2" );
      final PartGroupKey lPartGroup3 = createPartGroup( "PG2", lTrkConfigSlot3, "PG-3" );

      // create parts
      final PartNoKey lPart2 = createPart( lPartGroup2, "Tracked Part-2" );
      final PartNoKey lPart3 = createPart( lPartGroup3, "Tracked Part-3" );

      // create TRK inventories attached to the aircraft
      iInventory = createTrackedInventory( lPartGroup2, lLocationKey, lPart2, lTrkConfigSlot2,
            lAircraftInventory, "SYS-2", "POS-2" );
      createTrackedInventory( lPartGroup3, lLocationKey, lPart3, lTrkConfigSlot3,
            lAircraftInventory, "SYS-3", "POS-3" );

      // create REPL requirement definition and requirement for the config slot 2
      new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REPL )
            .withConfigSlot( lTrkConfigSlot2 ).build();

      // create REQ requirement definition and requirement for the config slot 2 with 'On
      // condition', LBR, 'OffWing', 'RM/IN'
      iTaskTask = createRequirementDefinition( lTrkConfigSlot2, lPartGroup2, lOrgKey );
   }


   @Test
   public void testCreateTaskFromDefinitionCreatesComponentReplacementWP() throws Exception {
      // execute logic
      TaskKey lNewTaskKey = new CreationService().createTaskFromDefinition( null, iInventory,
            iTaskTask, iAuthorizingHr, null, false, true, true, new Date() );

      EvtEventTable lEvent =
            EvtEventTable.findByPrimaryKey( new EventKey( lNewTaskKey.toString() ) );
      EventKey lHighestInventory = lEvent.getHEvent();
      SchedStaskTable lWP =
            SchedStaskTable.findByPrimaryKey( new TaskKey( lHighestInventory.toString() ) );

      // verify
      assertTrue( lWP != null );
      assertTrue( lWP.exists() );
      assertTrue(
            RefTaskClassKey.RO.getKeyValue( 2 ).equals( lWP.getTaskClass().getKeyValue( 2 ) ) );
   }


   private OrgKey createOrganization( OrgKey aParentOrgKey, RefOrgTypeKey aOrgType ) {

      OrganizationDomainBuilder lBuilder =
            new OrganizationDomainBuilder().withCode( "ORG1" ).withDescription( "ORG-1" );
      // By default the parent organization is "MXI" (key=0:1).
      if ( aParentOrgKey != null ) {
         lBuilder.withParentOrganization( aParentOrgKey );
      }
      // By default the organization type is OPERATOR.
      if ( aOrgType != null ) {
         lBuilder.withType( aOrgType );
      }

      return lBuilder.build();
   }


   private HumanResourceKey createUser( OrgKey aOrgKey, LocationKey lLocationKey ) {

      HumanResourceKey iHumanResourceKey = new HumanResourceDomainBuilder().withUsername( "USER-1" )
            .inOrganization( aOrgKey ).withSupplyLocation( lLocationKey ).build();
      SecurityIdentificationUtils
            .setInstance( new SecurityIdentificationStub( iHumanResourceKey ) );

      // Set up config parms
      int lUserId = OrgHr.findByPrimaryKey( iHumanResourceKey ).getUserId();
      UserParametersFake lUserParms = new UserParametersFake( lUserId, "LOGIC" );
      UserParameters.setInstance( lUserId, "LOGIC", lUserParms );

      GlobalParametersFake lConfigParms = new GlobalParametersFake( "LOGIC" );
      lConfigParms.setString( "BLANK_RO_SIGNATURE", "LBR" );
      GlobalParameters.setInstance( lConfigParms );

      return iHumanResourceKey;

   }


   private AssemblyKey createAircraft( final LocationKey aLocationKey ) {

      return Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAcftAssy ) {
            aAcftAssy.setCode( "ACFT" );
            aAcftAssy.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aConfigurationSlot ) {
                  aConfigurationSlot.setCode( "ACFT-1" );
                  aConfigurationSlot.setName( "SU-34" );
               }

            } );
         }
      } );

   }


   private InventoryKey createAircraftInventory( final AssemblyKey lAircraftAssemblyKey,
         final LocationKey aLocationKey ) {
      return Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAircraftAssemblyKey );
            aAircraft.setLocation( aLocationKey );
         }
      } );
   }


   private ConfigSlotKey createConfigSlot( String aCode, RefBOMClassKey aRefBOMClassKey,
         AssemblyKey aAircraftAssembly, String aPosition, ConfigSlotKey aParentConfigSlot,
         String aName ) {

      ConfigurationSlot aConfigurationSlot = new ConfigurationSlot();
      aConfigurationSlot.setCode( aCode );
      aConfigurationSlot.setParentConfigurationSlot( aParentConfigSlot );
      aConfigurationSlot.setName( aName );
      aConfigurationSlot.setConfigurationSlotClass( aRefBOMClassKey );
      aConfigurationSlot.setRootAssembly( aAircraftAssembly );
      aConfigurationSlot.addPosition( aPosition );

      return ConfigurationSlotBuilder.build( aConfigurationSlot );

   }


   private PartGroupKey createPartGroup( final String aCode, final ConfigSlotKey aConfigSlot,
         final String aName ) {
      return Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aPartGroup ) {
            aPartGroup.setCode( aCode );
            aPartGroup.setName( aName );
            aPartGroup.setInventoryClass( RefInvClassKey.TRK );
            aPartGroup.setConfigurationSlot( aConfigSlot );
         }
      } );
   }


   private PartNoKey createPart( final PartGroupKey aPartGroupKey, final String aDescription ) {

      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aPart ) {
            aPart.setPartGroup( aPartGroupKey, true );
            aPart.setInventoryClass( RefInvClassKey.TRK );
            aPart.setPartStatus( RefPartStatusKey.ACTV );
            aPart.setShortDescription( aDescription );
         }
      } );
   }


   private InventoryKey createTrackedInventory( final PartGroupKey lPartGroup,
         final LocationKey lLocation, final PartNoKey aPartNumber, final ConfigSlotKey aConfigSlot,
         final InventoryKey aParent, final String aConfigSlotCd, final String aPositionCd ) {

      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrackedInventory ) {
            aTrackedInventory.setPartGroup( lPartGroup );
            aTrackedInventory.setCondition( RefInvCondKey.RFI );
            aTrackedInventory.setLocation( lLocation );
            aTrackedInventory.setPartNumber( aPartNumber );
            aTrackedInventory.setParent( aParent );
            aTrackedInventory.setLastKnownConfigSlot( "ACFT", aConfigSlotCd, aPositionCd );
         }
      } );
   }


   private TaskTaskKey createRequirementDefinition( final ConfigSlotKey aConfigurationSlot,
         final PartGroupKey aPartGroup, final OrgKey aOrgKey ) {

      final TaskTaskKey lRequirementsDefinition =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aRequirementDefinition ) {
                  aRequirementDefinition.setCode( "RDEF1" );
                  aRequirementDefinition.againstConfigurationSlot( aConfigurationSlot );
                  aRequirementDefinition.setOnCondition( true );
                  aRequirementDefinition.setMustRemove( RefTaskMustRemoveKey.OFFWING );
                  aRequirementDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aRequirementDefinition.addLabourRequirement( RefLabourSkillKey.LBR,
                        new BigDecimal( 1 ) );
                  aRequirementDefinition.setOrganization( aOrgKey );

               }
            } );

      Domain.createTaskDefinitionPartRequirement(
            new DomainConfiguration<TaskDefinitionPartRequirement>() {

               @Override
               public void
                     configure( TaskDefinitionPartRequirement aTaskDefinitionPartRequirementn ) {
                  aTaskDefinitionPartRequirementn.setPartGroup( aPartGroup );
                  aTaskDefinitionPartRequirementn.setRequiredQuantity( 1L );
                  aTaskDefinitionPartRequirementn.setIsRemove( true );
                  aTaskDefinitionPartRequirementn.setIsInstall( true );
                  aTaskDefinitionPartRequirementn
                        .setBomItemPosition( new ConfigSlotPositionKey( aConfigurationSlot, 1 ) );
                  aTaskDefinitionPartRequirementn.setRemoveReason( RefRemoveReasonKey.IMSCHD );
                  aTaskDefinitionPartRequirementn.setReqAction( RefReqActionKey.REQ );
                  aTaskDefinitionPartRequirementn.setTaskDefinition( lRequirementsDefinition );

               }
            } );
      return lRequirementsDefinition;
   }

}
