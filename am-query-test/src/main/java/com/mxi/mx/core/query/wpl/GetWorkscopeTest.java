
package com.mxi.mx.core.query.wpl;

import static com.mxi.mx.core.key.RefTaskDefinitionStatusKey.ACTV;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.JobCard;
import com.mxi.am.domain.JobCardDefinition;
import com.mxi.am.domain.Requirement;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Integration tests for getWorkscope.qrx
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetWorkscopeTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;

   private AssemblyKey iAcftAssy;


   /**
    * Retrieves adhoc task
    *
    */
   @Test
   public void testGetAdhocTask() throws Exception {

      // create an adhoc task
      final TaskKey lAdhocTask = new TaskBuilder().withStatus( RefEventStatusKey.ACTV )
            .withLabour( RefLabourSkillKey.ENG, 1, 2, 3 ).onInventory( iAircraft ).build();

      // create a work package and assign adhoc to it
      TaskKey lWpKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWp ) {
            aWp.setAircraft( iAircraft );
            aWp.addTask( lAdhocTask );
         }
      } );

      QuerySet lResults = executeQuery( lWpKey );

      assertEquals( lResults.getRowCount(), 1 );
   }


   /**
    * Retrieves executable requirement task
    *
    */
   @Test
   public void testGetExecutableReq() throws Exception {

      // Create Executable Requirement Definition
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( new ConfigSlotKey( iAcftAssy, 0 ) );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setExecutable( true );
               }
            } );

      // Create an actual task
      final TaskKey lExecReqTask =
            Domain.createRequirement( new DomainConfiguration<Requirement>() {

               @Override
               public void configure( Requirement aReq ) {
                  aReq.setDefinition( lReqDefn );
                  aReq.setStatus( RefEventStatusKey.ACTV );
                  aReq.setInventory( iAircraft );
                  aReq.addLabour( lLabour -> {
                     lLabour.setSkill( RefLabourSkillKey.PILOT );
                     lLabour.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
                  } );
               }
            } );

      // create a work package and assign executable requirement task to it
      TaskKey lWpKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWp ) {
            aWp.setAircraft( iAircraft );
            aWp.addTask( lExecReqTask );
         }
      } );

      QuerySet lResults = executeQuery( lWpKey );

      assertEquals( lResults.getRowCount(), 1 );
   }


   /**
    * Retrieves JIC task
    *
    */
   @Test
   public void testGetJic() throws Exception {

      final ConfigSlotKey lAircraftRootConfigSlot = new ConfigSlotKey( iAcftAssy, 0 );

      // Create Job Card Definition
      final TaskTaskKey lJicDefn =
            Domain.createJobCardDefinition( new DomainConfiguration<JobCardDefinition>() {

               @Override
               public void configure( JobCardDefinition aJicDefn ) {
                  aJicDefn.setConfigurationSlot( lAircraftRootConfigSlot );
               }
            } );

      // Create Requirement Definition
      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( lAircraftRootConfigSlot );
                  aReqDefn.setStatus( ACTV );
                  aReqDefn.setExecutable( false );
                  aReqDefn.addJobCardDefinition( lJicDefn );
               }
            } );

      // Create a jic actual task
      final TaskKey lJicTask = Domain.createJobCard( new DomainConfiguration<JobCard>() {

         @Override
         public void configure( JobCard aJobCard ) {
            aJobCard.setDefinition( lJicDefn );
            aJobCard.setStatus( RefEventStatusKey.ACTV );
            aJobCard.setInventory( iAircraft );
            aJobCard.addLabour( lLabour -> {
               lLabour.setSkill( RefLabourSkillKey.PILOT );
               lLabour.setTechnicianRole( tech -> tech.setScheduledHours( 3 ) );
            } );
         }
      } );

      // Create a requirement actual task
      final TaskKey lReqTask = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setDefinition( lReqDefn );
            aReq.setStatus( RefEventStatusKey.ACTV );
            aReq.setInventory( iAircraft );
            aReq.addJobCard( lJicTask );
         }
      } );

      // create a work package and assign the requirement to it
      TaskKey lWpKey = Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aWp ) {
            aWp.setAircraft( iAircraft );
            aWp.addTask( lReqTask );
         }
      } );

      QuerySet lResults = executeQuery( lWpKey );

      assertEquals( lResults.getRowCount(), 1 );
   }


   /**
    * Executes the query being tested and returns the results.
    *
    * @param aWorkPackageId
    *           The alt id
    *
    * @return The results of the query
    */
   private QuerySet executeQuery( TaskKey aWorkPackage ) {

      SchedStaskTable lStaskTable = SchedStaskTable.findByPrimaryKey( aWorkPackage );
      String lWpAltId =
            FormatUtil.formatUniqueIdRemoveHyphens( lStaskTable.getAlternateKey().toString() );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aWorkPackageId", lWpAltId );

      // run the query
      QuerySet lResults = QuerySetFactory.getInstance().executeQuery(
            "com.mxi.mx.wpl.loader.induction.service.workbook.query.getWorkscope", lArgs );

      return lResults;
   }


   @Before
   public void setup() {

      iAcftAssy = Domain.createAircraftAssembly();

      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( iAcftAssy );
         }
      } );

   }

}
