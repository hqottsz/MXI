package com.mxi.mx.core.query.fault;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.DeferralReference;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.task.TaskDefnTable;


/**
 * Test the GetRecurringTasksInitializedFromDeferRef query using builder for data set up
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetRecurringTasksInitializedFromDeferRefTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Returns two active recurring tasks initialized from the deferral reference
    *
    * @throws Exception
    *            if any exception occurs
    */
   @Test
   public void testGetRecurringTasksInitializedFromDeferRef() throws Exception {

      final String REQ1 = "D3DCA451FAD111E68EC1B1D7CDE0D34E";
      final String REQ2 = "0237E82EF25F492CA9B8481CD82E072E";
      final String DEFERRAL_REFERENCE = "DEF REF TEST";

      // Create two requirement definitions
      TaskDefnKey lTaskDefnKey = TaskDefnTable
            .create( new TaskDefnKey( 4650, 1 ), FormatUtil.stringToUuid( REQ1 ) ).insert();
      TaskTaskKey lReq1 = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskDefn( lTaskDefnKey ).build();

      lTaskDefnKey = TaskDefnTable
            .create( new TaskDefnKey( 4650, 2 ), FormatUtil.stringToUuid( REQ2 ) ).insert();
      TaskTaskKey lReq2 = new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ )
            .withTaskDefn( lTaskDefnKey ).build();

      // Create an assembly
      final AssemblyKey lAssemblyKey =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.setCode( "ACFT" );
               }
            } );

      // create aircraft
      InventoryKey lAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT )
            .withOriginalAssembly( lAssemblyKey ).build();

      // create a failed system config slot
      ConfigSlotKey lFailedSystemConfigSlotDbKey = new ConfigSlotBuilder( "CONFIG SLOT" ).build();

      final UUID lFailedSystemAltId =
            EqpAssmblBom.findByPrimaryKey( lFailedSystemConfigSlotDbKey ).getAlternateKey();

      // Create a deferral reference
      final List<String> lRecurringInspections = new ArrayList<>();
      lRecurringInspections.add( REQ1 );
      lRecurringInspections.add( REQ2 );

      Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

         @Override
         public void configure( DeferralReference aDeferralReferenceConfiguration ) {
            aDeferralReferenceConfiguration.setName( DEFERRAL_REFERENCE );
            aDeferralReferenceConfiguration.setAssemblyKey( lAssemblyKey );
            aDeferralReferenceConfiguration.setRecurringInspections( lRecurringInspections );
            aDeferralReferenceConfiguration.getFailedSystemInfo()
                  .setFailedSystemAltId( lFailedSystemAltId );
         }
      } );

      // Create a corrective task
      final TaskKey lCorrectiveTaskKey = new TaskBuilder().onInventory( lAircraft )
            .withTaskClass( RefTaskClassKey.CORR ).withStatus( RefEventStatusKey.ACTV ).build();

      // Create a fault
      FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setDeferralReference( DEFERRAL_REFERENCE );
            aFault.setCorrectiveTask( lCorrectiveTaskKey );
         }
      } );

      // Create actual task for requirement 1
      new TaskBuilder().onInventory( lAircraft ).withTaskRevision( lReq1 )
            .withStatus( RefEventStatusKey.ACTV ).build();

      // Create actual task for requirement 2
      new TaskBuilder().onInventory( lAircraft ).withTaskRevision( lReq2 )
            .withStatus( RefEventStatusKey.ACTV ).build();

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lFaultKey, "aFaultDbId", "aFaultId" );

      QuerySet lQs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      assertEquals( 2, lQs.getRowCount() );
   }
}
