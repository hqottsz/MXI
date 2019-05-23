package com.mxi.mx.web.servlet.maint;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.MaintenanceProgram;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.MaintPrgmDefnKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.maint.MaintPrgmDefnTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * Tests for {@linkplain MultipleOperatorMaintPrgmValidator}
 *
 */
public class MultipleOperatorMaintPrgmValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /*
    * Verify that when a maintenance program is activated for one operator but does not have the
    * requirement assigned to it, that the validation is successful.
    */
   @Test
   public void itIsValidWhenMpWithoutReqActivatedForOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given a maintenance program that is based on the maintenance program definition, is
      // activated for the operator, and DOES NOT HAVE the requirement definition assigned to it.
      final CarrierKey lOperator = Domain.createOperator();
      final MaintPrgmKey lMaintPrgm =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator );
                  aMp.setLatestRevisionForOperator( true );
               }
            } );

      // When the maintenance program and requirement definition is validated then the validation
      // succeeds.
      Assert.assertTrue(
            MultipleOperatorMaintPrgmValidator.isValidToUnassignReq( lMaintPrgm, lReqDefn ) );
   }


   /*
    * Verify that when a maintenance program is activated for more than one operator but both do not
    * have the requirement assigned to it, that the validation is successful.
    */
   @Test
   public void itIsValidWhenMpWithoutReqActivatedForMoreThanOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given a maintenance program that is based on the maintenance program definition, is
      // activated for the operator, and DOES NOT HAVE the requirement definition assigned to it.
      final CarrierKey lOperator1 = Domain.createOperator();
      final MaintPrgmKey lMaintPrgm1 =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator1 );
                  aMp.setLatestRevisionForOperator( true );
               }
            } );

      // Given ANOTHER maintenance program that is based on the maintenance program definition, is
      // activated for ANOTHER operator, and DOES NOT HAVE the requirement definition assigned to
      // it.
      final CarrierKey lOperator2 = Domain.createOperator();
      final MaintPrgmKey lMaintPrgm2 =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator2 );
                  aMp.setLatestRevisionForOperator( true );
               }
            } );

      // When either of the maintenance programs and requirement definition are validated then the
      // validation succeeds (note: admittedly combining two tests into one to reduce amount of
      // code).
      Assert.assertTrue( "Expected maint pgrm key " + lMaintPrgm1 + " to be valid.",
            MultipleOperatorMaintPrgmValidator.isValidToUnassignReq( lMaintPrgm1, lReqDefn ) );
      Assert.assertTrue( "Expected maint pgrm key " + lMaintPrgm2 + " to be valid.",
            MultipleOperatorMaintPrgmValidator.isValidToUnassignReq( lMaintPrgm2, lReqDefn ) );
   }


   /*
    * Verify that when a maintenance program is activated for one operator and the requirement is
    * assigned to it, that the validation is successful.
    */
   @Test
   public void itIsValidWhenMpWithReqIsActivatedForOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given a maintenance program that is based on the maintenance program definition, is
      // activated for the operator, and HAS the requirement definition assigned to it.
      final MaintPrgmKey lMaintPrgm =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( Domain.createOperator() );
                  aMp.setLatestRevisionForOperator( true );
                  aMp.addRequirementDefinition( lReqDefn, lReqDefnRev );
               }
            } );

      // When the maintenance program and requirement definition is validated then the validation
      // succeeds.
      Assert.assertTrue(
            MultipleOperatorMaintPrgmValidator.isValidToUnassignReq( lMaintPrgm, lReqDefn ) );
   }


   /*
    * Verify that when a maintenance program is activated for more than one operator and the
    * requirement is assigned to both maintenance programs, that the validation fails.
    */
   @Test
   public void itIsNotValidWhenMpWithReqIsActivatedForMoreThanOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given two operators.
      final CarrierKey lOperator1 = Domain.createOperator();
      final CarrierKey lOperator2 = Domain.createOperator();

      // Given a maintenance program that is based on the maintenance program definition, is
      // activated for the first operator, and HAS the requirement definition assigned to it.
      final MaintPrgmKey lMaintPrgm1 =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator1 );
                  aMp.setLatestRevisionForOperator( true );
                  aMp.addRequirementDefinition( lReqDefn, lReqDefnRev );
               }
            } );

      // Given another maintenance program that is based on the maintenance program definition, is
      // activated for the second operator, and HAS the requirement definition assigned to it.
      Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

         @Override
         public void configure( MaintenanceProgram aMp ) {
            aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
            aMp.setOperator( lOperator2 );
            aMp.setLatestRevisionForOperator( true );
            aMp.addRequirementDefinition( lReqDefn, lReqDefnRev );
         }
      } );

      // When one of the maintenance programs and the requirement definition is validated then the
      // validation fails.
      Assert.assertFalse(
            MultipleOperatorMaintPrgmValidator.isValidToUnassignReq( lMaintPrgm1, lReqDefn ) );
   }


   /*
    * Verify that when a maintenance program is activated for more than one operator and the
    * requirement is assigned to only one of those maintenance programs, that the validation is
    * successful.
    */
   @Test
   public void itIsValidWhenReqOnlyInOneMpActivatedForMoreThanOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given two operators.
      final CarrierKey lOperator1 = Domain.createOperator();
      final CarrierKey lOperator2 = Domain.createOperator();

      // Given a maintenance program that is based on the maintenance program definition, is
      // activated for the first operator, and HAS the requirement definition assigned to it.
      final MaintPrgmKey lMaintPrgm1 =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator1 );
                  aMp.setLatestRevisionForOperator( true );
                  aMp.addRequirementDefinition( lReqDefn, lReqDefnRev );
               }
            } );

      // Given a maintenance program that is based on the maintenance program definition, is
      // activated for the second operator, and DOES NOT HAVE the requirement definition assigned to
      // it.
      Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

         @Override
         public void configure( MaintenanceProgram aMp ) {
            aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
            aMp.setOperator( lOperator2 );
            aMp.setLatestRevisionForOperator( true );
         }
      } );

      // When one of the maintenance programs and the requirement definition is validated then the
      // validation succeeds.
      Assert.assertTrue(
            MultipleOperatorMaintPrgmValidator.isValidToUnassignReq( lMaintPrgm1, lReqDefn ) );
   }


   /*
    * Verify that when there exists both a superseded and an active maintenance program for one
    * operator and the requirement is assigned to ONLY the superseded maintenance program, that the
    * validation is successful.
    */
   @Test
   public void itIsValidWhenReqNoLongerInActivatedMpForOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given one operator.
      final CarrierKey lOperator1 = Domain.createOperator();

      // Given a maintenance program that is based on the maintenance program definition, is
      // SUPERSEDED for the operator, and HAD the requirement definition assigned to it.
      Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

         @Override
         public void configure( MaintenanceProgram aMp ) {
            aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
            aMp.setOperator( lOperator1 );
            aMp.setLatestRevisionForOperator( false );
            aMp.addRequirementDefinition( lReqDefn, lReqDefnRev );
         }
      } );

      // Given another maintenance program that is based on the maintenance program definition, is
      // ACTIVATED for the operator, and DOES NOT HAVE the requirement definition assigned to it.
      final MaintPrgmKey lActivatedMaintPrgm =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator1 );
                  aMp.setLatestRevisionForOperator( true );
               }
            } );

      // When the activated maintenance program and the requirement definition is validated then the
      // validation succeeds.
      Assert.assertTrue( MultipleOperatorMaintPrgmValidator
            .isValidToUnassignReq( lActivatedMaintPrgm, lReqDefn ) );
   }


   /*
    * Verify that when there exists both a superseded and an active maintenance program for more
    * than one operator and the requirement is assigned to ONLY the superseded maintenance programs,
    * that the validation is successful.
    */
   @Test
   public void itIsValidWhenReqNoLongerInActivatedMpsForMoreThanOneOperator() {

      // Given a task definition.
      final TaskTaskKey lReqDefnRev = Domain.createRequirementDefinition();
      final TaskDefnKey lReqDefn = TaskTaskTable.findByPrimaryKey( lReqDefnRev ).getTaskDefn();

      // Given an aircraft assembly with a maintenance program definition.
      final AssemblyKey lAcftAssy =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.addMaintenanceProgramDefinition();
               }
            } );
      final MaintPrgmDefnTable lMaintPrgmDefn = getMaintPrgmDefn( lAcftAssy );

      // Given two operators.
      final CarrierKey lOperator1 = Domain.createOperator();
      final CarrierKey lOperator2 = Domain.createOperator();

      // Given a maintenance program that is based on the maintenance program definition, is
      // SUPERSEDED for the first operator, and HAD the requirement definition assigned to it.
      Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

         @Override
         public void configure( MaintenanceProgram aMp ) {
            aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
            aMp.setOperator( lOperator1 );
            aMp.setLatestRevisionForOperator( false );
            aMp.addRequirementDefinition( lReqDefn, lReqDefnRev );
         }
      } );

      // Given another maintenance program that is based on the maintenance program definition, is
      // ACTIVATED for the first operator, and DOES NOT HAVE the requirement definition assigned to
      // it.
      final MaintPrgmKey lActivatedMaintPrgmForOperator1 =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator1 );
                  aMp.setLatestRevisionForOperator( true );
               }
            } );

      // Given a maintenance program that is based on the maintenance program definition, is
      // SUPERSEDED for the second operator, and HAD the requirement definition assigned to it.
      Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

         @Override
         public void configure( MaintenanceProgram aMp ) {
            aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
            aMp.setOperator( lOperator2 );
            aMp.setLatestRevisionForOperator( false );
         }
      } );

      // Given another maintenance program that is based on the maintenance program definition, is
      // ACTIVATED for the second operator, and DOES NOT HAVE the requirement definition assigned to
      // it.
      final MaintPrgmKey lActivatedMaintPrgmForOperator2 =
            Domain.createMaintenanceProgram( new DomainConfiguration<MaintenanceProgram>() {

               @Override
               public void configure( MaintenanceProgram aMp ) {
                  aMp.basedOnDefinition( lMaintPrgmDefn.getPk() );
                  aMp.setOperator( lOperator2 );
                  aMp.setLatestRevisionForOperator( true );
               }
            } );

      // When either of the activated maintenance programs and the requirement definition are
      // validated then the validation succeeds (note: admittedly combining two tests into one to
      // reduce amount of code).
      Assert.assertTrue(
            "Expected maint pgrm key " + lActivatedMaintPrgmForOperator1 + " to be valid.",
            MultipleOperatorMaintPrgmValidator
                  .isValidToUnassignReq( lActivatedMaintPrgmForOperator1, lReqDefn ) );
      Assert.assertTrue(
            "Expected maint pgrm key " + lActivatedMaintPrgmForOperator2 + " to be valid.",
            MultipleOperatorMaintPrgmValidator
                  .isValidToUnassignReq( lActivatedMaintPrgmForOperator2, lReqDefn ) );
   }


   private MaintPrgmDefnTable getMaintPrgmDefn( AssemblyKey aAcftAssy ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aAcftAssy, "assmbl_db_id", "assmbl_cd" );
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "maint_prgm_defn", lArgs );
      lQs.next();
      MaintPrgmDefnKey lMaintPrgmDefnKey =
            lQs.getKey( MaintPrgmDefnKey.class, "maint_prgm_defn_db_id", "maint_prgm_defn_id" );

      return MaintPrgmDefnTable.findByPrimaryKey( lMaintPrgmDefnKey );
   }

}
