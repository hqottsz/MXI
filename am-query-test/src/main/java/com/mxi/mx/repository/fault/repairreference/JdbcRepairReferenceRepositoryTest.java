package com.mxi.mx.repository.fault.repairreference;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.domain.fault.repairreference.RepairReference;
import com.mxi.mx.domain.fault.repairreference.TaskSubClass;


@RunWith( BlockJUnit4ClassRunner.class )
public class JdbcRepairReferenceRepositoryTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private static final String APPL_RANGE_CORRECT = "05-10";
   private static final String APPL_RANGE_INCORRECT = "01-04";
   private static final String VALID_RULE = "inv_inv.serial_no_oem = '123456'";
   private static final String INVALID_RULE = "inv_inv.serial_no_oem = '654321'";
   private static final String AIRCRAFT_SERIAL_NO = "123456";
   private static final AssemblyKey AIRCRAFT_ORIG_ASSEMBLY = new AssemblyKey( "1000:ABC" );

   private InventoryKey aircraftKey;

   private RepairReferenceRepository repository;


   @Before
   public void setUp() {
      repository = InjectorContainer.get().getInstance( JdbcRepairReferenceRepository.class );

      DataLoaders.load( databaseConnectionRule.getConnection(),
            JdbcRepairReferenceRepositoryTest.class );

      // creates a repair reference on an assembly not used by this test suite's tests
      DomainConfiguration<RequirementDefinition> repairReferenceDifferentAssemblyConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceOtherAssembly" );
                  builder.setCode( "abc" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:badAss:1" ) );
               }
            };

      aircraftKey = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setApplicabilityCode( "06" );
            aBuilder.setSerialNumber( AIRCRAFT_SERIAL_NO );
            aBuilder.setOriginalAssembly( AIRCRAFT_ORIG_ASSEMBLY );
         }
      } );

      Domain.createRequirementDefinition( repairReferenceDifferentAssemblyConfiguration );

   }


   @Test( expected = NullPointerException.class )
   public void search_nullAssembly() {
      repository.search( null, Optional.empty(), "", Optional.empty() );
   }


   @Test( expected = NullPointerException.class )
   public void search_nullTaskName() {
      repository.search( new AssemblyKey( "1000:ABC" ), Optional.empty(), null, Optional.empty() );
   }


   /**
    * Creates an ACTV and and OBSOLETE repair reference against a single assembly. Expects that only
    * the ACTV repair reference is returned in the search results.
    */
   @Test
   public void search_onlyActiveRepairReferencesFound() {
      DomainConfiguration<RequirementDefinition> repairReferenceCorrectConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceActive" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReferenceWrongConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceObsolete" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.OBSOLETE );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      TaskTaskKey correctRepairReferenceKey =
            Domain.createRequirementDefinition( repairReferenceCorrectConfiguration );

      Domain.createRequirementDefinition( repairReferenceWrongConfiguration );

      List<RepairReference> searchResults = repository.search( AIRCRAFT_ORIG_ASSEMBLY,
            Optional.empty(), "repairReference", Optional.empty() );

      Assert.assertEquals( 1, searchResults.size() );
      Assert.assertEquals( correctRepairReferenceKey, searchResults.get( 0 ).getId() );
   }


   /**
    * Creates two repair references, each against a different assembly. Expects that only the repair
    * reference against the specified assembly is returned in the search results.
    */
   @Test
   public void search_onlyRepairReferencesForSpecificAssemblyFound() {
      String correctAssemblyKeyString = "1000:ABC";
      String incorrectAssemblyKeyString = "1000:XYZ";
      DomainConfiguration<RequirementDefinition> repairReferenceCorrectConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectConfigurationAssembly" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot(
                        new ConfigSlotKey( correctAssemblyKeyString + ":1" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReferenceWrongConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceWrongConfigurationAssembly" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot(
                        new ConfigSlotKey( incorrectAssemblyKeyString + ":1" ) );
               }
            };
      TaskTaskKey correctRepairReferenceKey =
            Domain.createRequirementDefinition( repairReferenceCorrectConfiguration );

      Domain.createRequirementDefinition( repairReferenceWrongConfiguration );

      List<RepairReference> searchResults =
            repository.search( new AssemblyKey( correctAssemblyKeyString ), Optional.empty(),
                  "repairReference", Optional.empty() );

      Assert.assertEquals( 1, searchResults.size() );
      Assert.assertEquals( correctRepairReferenceKey, searchResults.get( 0 ).getId() );
   }


   /**
    * Creates two repair references, each against the same assembly. Expects that only the repair
    * reference whose name matches the specified search parameter is returned in the search results.
    */
   @Test
   public void search_onlyRepairReferencesWithMatchingNameFound() {
      DomainConfiguration<RequirementDefinition> repairReferenceCorrectConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectConfigurationAssembly" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReferenceWrongConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceWrongConfigurationAssembly" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      TaskTaskKey correctRepairReferenceKey =
            Domain.createRequirementDefinition( repairReferenceCorrectConfiguration );

      Domain.createRequirementDefinition( repairReferenceWrongConfiguration );

      List<RepairReference> searchResults = repository.search( AIRCRAFT_ORIG_ASSEMBLY,
            Optional.empty(), "Correct", Optional.empty() );

      Assert.assertEquals( 1, searchResults.size() );
      Assert.assertEquals( correctRepairReferenceKey, searchResults.get( 0 ).getId() );
   }


   /**
    * Creates two repair references, each against the same assembly. Expects that only the repair
    * reference whose name and code matches the specified search parameters is returned in the
    * search results.
    */
   @Test
   public void search_onlyRepairReferencesWithMatchingCodeFound() {
      DomainConfiguration<RequirementDefinition> repairReferenceCorrectConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectConfigurationAssembly" );
                  builder.setCode( "abcxyz" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReferenceWrongConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceWrongConfigurationAssembly" );
                  builder.setCode( "abc" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      TaskTaskKey correctRepairReferenceKey =
            Domain.createRequirementDefinition( repairReferenceCorrectConfiguration );

      Domain.createRequirementDefinition( repairReferenceWrongConfiguration );

      List<RepairReference> searchResults =
            repository.search( AIRCRAFT_ORIG_ASSEMBLY, Optional.empty(), "xy", Optional.empty() );

      Assert.assertEquals( 1, searchResults.size() );
      Assert.assertEquals( correctRepairReferenceKey, searchResults.get( 0 ).getId() );
   }


   /**
    * Creates two repair references, each against the same assembly. Set up the search parameters so
    * that neither the specified name or code matches either of the created repair references.
    * Expects an empty search result set.
    */
   @Test
   public void search_onlyRepairReferencesForSpecificNameAndCodeNoMatch() {
      DomainConfiguration<RequirementDefinition> repairReferenceCorrectConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectConfigurationAssembly" );
                  builder.setCode( "abc" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReferenceWrongConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceWrongConfigurationAssembly" );
                  builder.setCode( "abc" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
               }
            };

      Domain.createRequirementDefinition( repairReferenceCorrectConfiguration );
      Domain.createRequirementDefinition( repairReferenceWrongConfiguration );

      List<RepairReference> searchResults = repository.search( AIRCRAFT_ORIG_ASSEMBLY,
            Optional.empty(), "noMatch", Optional.empty() );

      Assert.assertEquals( 0, searchResults.size() );
   }


   /**
    * Creates two repair references, each against the same assembly but different types. Expects
    * that only the repair reference whose type matches the specified search parameters is returned
    * in the search results.
    */
   @Test
   public void search_onlyRepairReferencesWithMatchingNameAndType() {
      DomainConfiguration<RequirementDefinition> repairReferenceCorrectConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectType" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskSubClass( new RefTaskSubclassKey( "10:MEL" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReferenceWrongConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceWrongType" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskSubClass( new RefTaskSubclassKey( "10:ABC" ) );

               }
            };
      TaskTaskKey correctRepairReferenceKey =
            Domain.createRequirementDefinition( repairReferenceCorrectConfiguration );

      Domain.createRequirementDefinition( repairReferenceWrongConfiguration );

      List<RepairReference> searchResults = repository.search( AIRCRAFT_ORIG_ASSEMBLY,
            Optional.of( new RefTaskSubclassKey( "10:MEL" ) ), "Correct", Optional.empty() );

      Assert.assertEquals( 1, searchResults.size() );
      Assert.assertEquals( correctRepairReferenceKey, searchResults.get( 0 ).getId() );
   }


   /**
    * Creates two repair references, each against the same assembly and type. Set up the search
    * parameters so that the type does neither of the created repair references. Expects an empty
    * search result set.
    */
   @Test
   public void search_onlyRepairReferencesWitTypeNoMatch() {
      DomainConfiguration<RequirementDefinition> repairReference1 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectType1" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskSubClass( new RefTaskSubclassKey( "10:ABC" ) );
               }
            };
      DomainConfiguration<RequirementDefinition> repairReference2 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceCorrectType2" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskSubClass( new RefTaskSubclassKey( "10:ABC" ) );

               }
            };
      Domain.createRequirementDefinition( repairReference1 );

      Domain.createRequirementDefinition( repairReference2 );

      List<RepairReference> searchResults = repository.search( AIRCRAFT_ORIG_ASSEMBLY,
            Optional.of( new RefTaskSubclassKey( "0:ABX" ) ), "repairReferenceCorrectType",
            Optional.empty() );

      Assert.assertTrue( searchResults.isEmpty() );

   }


   /**
    * Creates three repair references, each against the same assembly and type. Set it up so that
    * only two of the references are applicable to the aircraft that is searching for them.
    */
   @Test
   public void search_onlyReferencesApplicableToAircraft() {
      DomainConfiguration<RequirementDefinition> RR1 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceActive1" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setApplicabilityRange( APPL_RANGE_CORRECT );
                  builder.setApplicabilityRule( VALID_RULE );
               }
            };

      DomainConfiguration<RequirementDefinition> RR2 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceActive2" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setApplicabilityRange( APPL_RANGE_CORRECT );
                  builder.setApplicabilityRule( VALID_RULE );
               }
            };
      DomainConfiguration<RequirementDefinition> RR3 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceActive3" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setApplicabilityRange( APPL_RANGE_INCORRECT );
               }
            };
      DomainConfiguration<RequirementDefinition> RR4 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReferenceActive3" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setApplicabilityRule( "inv_inv.serial_no_oem IS NULL" );
               }
            };

      Domain.createRequirementDefinition( RR1 );
      Domain.createRequirementDefinition( RR2 );
      Domain.createRequirementDefinition( RR3 );
      Domain.createRequirementDefinition( RR4 );

      List<RepairReference> searchResults = repository.search( AIRCRAFT_ORIG_ASSEMBLY,
            Optional.empty(), "", Optional.of( aircraftKey ) );

      Assert.assertEquals( 2, searchResults.size() );
      searchResults.forEach( reference -> {
         assertTrue( reference.getName().equals( "repairReferenceActive1" )
               || reference.getName().equals( "repairReferenceActive2" ) );
      } );

   }


   /**
    * Returns only repair reference types (Task subclasses) for Taskclass=REPREF the SQL.
    * JdbcRepairReferenceRepository.sql script sets up 4 task subclasses but only 3 of them are for
    * Taskclass=REPREF. The test sets up 3 repair references but only 2 are valid for the aircraft.
    */
   @Test
   public void searchRepairReferencesTypes() {

      DomainConfiguration<RequirementDefinition> RR1 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference1" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setTaskSubClass( new RefTaskSubclassKey( 10, "EA" ) );
                  builder.setApplicabilityRange( APPL_RANGE_CORRECT );
               }
            };

      DomainConfiguration<RequirementDefinition> RR2 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference2" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setTaskSubClass( new RefTaskSubclassKey( 10, "SRM" ) );
                  builder.setApplicabilityRange( APPL_RANGE_CORRECT );
               }
            };

      DomainConfiguration<RequirementDefinition> RR3 =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference3" );
                  builder.setCode( "code" );
                  builder.againstConfigurationSlot( new ConfigSlotKey( "1000:ABC:1" ) );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setTaskSubClass( new RefTaskSubclassKey( 10, "DD" ) );
                  builder.setApplicabilityRange( APPL_RANGE_INCORRECT );
               }
            };

      Domain.createRequirementDefinition( RR1 );
      Domain.createRequirementDefinition( RR2 );
      Domain.createRequirementDefinition( RR3 );

      List<TaskSubClass> types = repository.searchTypes( aircraftKey, AIRCRAFT_ORIG_ASSEMBLY );

      Assert.assertEquals( 2, types.size() );
      types.forEach( type -> {
         assertTrue( type.getId().getCd().equals( "EA" ) || type.getId().getCd().equals( "SRM" ) );
      } );

   }


   @Test
   public void getRepairReference() {
      String operRestrictions = "operatinal restrictions";

      DomainConfiguration<RequirementDefinition> repairDefinitionConfiguration =
            new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition builder ) {
                  builder.setTaskName( "repairReference" );
                  builder.setCode( "code" );
                  builder.setTaskClass( RefTaskClassKey.REPREF );
                  builder.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  builder.setOpsRestrictionsDesc( operRestrictions );
               }
            };

      TaskTaskKey taskKey = Domain.createRequirementDefinition( repairDefinitionConfiguration );

      Optional<RepairReference> repairReference = repository.getRepairReference( taskKey );
      Assert.assertTrue( repairReference.isPresent() );
      Assert.assertFalse( repairReference.get().isMocApprovalRequired() );
      Assert.assertEquals( repairReference.get().getOperationalRestrictions(), operRestrictions );

   }


   @Test
   public void getRepairReference_doesNotExist() {

      Optional<RepairReference> repairReference =
            repository.getRepairReference( new TaskTaskKey( "0:3444" ) );
      Assert.assertFalse( repairReference.isPresent() );
   }

}
