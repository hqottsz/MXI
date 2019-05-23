package com.mxi.mx.core.maintenance.exec.fault.deferral.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.DeferralReference;
import com.mxi.am.domain.DeferralReference.DeferralReferenceDeadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Operator;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.FormatUtil;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailDeferKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.maintenance.exec.fault.deferral.model.DeferredFaultDetails;
import com.mxi.mx.core.maintenance.exec.fault.deferral.model.FaultDeferralTO;
import com.mxi.mx.core.services.stask.deadline.CalendarDeadline;
import com.mxi.mx.core.services.stask.deadline.Deadline;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.fail.FailDeferRef;
import com.mxi.mx.core.table.fail.FailDeferRefDead;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.core.table.task.TaskDefnTable;


/**
 * An integration test suite for the {@link FaultDeferralService} class.
 */
public class FaultDeferralServiceTest {

   private static final double CUSTOM_DEADLINE_DAYS = 13.0;
   private static final String DEFERRAL_REFERENCE_NAME = "Deferral Reference 123";
   private static final UUID RECURRING_INSPECTION_1_ID = UUID.randomUUID();
   private static final UUID RECURRING_INSPECTION_2_ID = UUID.randomUUID();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // System under test
   private FaultDeferralService iService = new FaultDeferralService();

   // Test data
   private FaultKey iFault;
   private FailDeferRefKey iDeferralReference;
   private HumanResourceKey iHr;
   private InventoryKey iAircraft;
   private ConfigSlotKey iConfigSlotKey;


   @Before
   public void setUp() {
      iAircraft = Domain.createAircraft();
      HumanResourceDomainBuilder lHrBuilder = new HumanResourceDomainBuilder();
      iHr = lHrBuilder.build();
      iConfigSlotKey = new ConfigSlotBuilder( "SYS-1" ).build();
   }


   @Test
   public void deferFault_fault_updated() throws Exception {
      DeferredFaultDetails lDeferredFault =
            iService.deferFault( buildFaultDeferralTO( false, false ) );

      SdFaultTable lFault = SdFaultTable.findByPrimaryKey( lDeferredFault.getFault() );
      assertEquals( DEFERRAL_REFERENCE_NAME, lFault.getDeferRefSDesc() );

      EvtEventTable lEvent = EvtEventTable.findByPrimaryKey( lFault.getEventKey() );
      assertEquals( RefEventStatusKey.CFDEFER, lEvent.getEventStatus() );
   }


   @Test
   public void deferFault_recurringInspections_initialized() throws Exception {
      DeferredFaultDetails lDeferredFault =
            iService.deferFault( buildFaultDeferralTO( false, true ) );
      assertTrue( lDeferredFault.isRecurringInspectionsInitialized() );
   }


   @Test
   public void deferFault_deadlines_custom() throws Exception {
      DeferredFaultDetails lDeferredFault =
            iService.deferFault( buildFaultDeferralTO( true, false ) );

      Deadline lDrivingDeadline = lDeferredFault.getDeadline();
      assertTrue( lDrivingDeadline instanceof CalendarDeadline );

      double lQuantity = lDrivingDeadline.getInterval();
      assertEquals( CUSTOM_DEADLINE_DAYS, lQuantity, 0.01 );
   }


   private FaultDeferralTO buildFaultDeferralTO( boolean aWithCustomDeadlines,
         boolean aWithRecurringInspections ) {

      List<DeferralReferenceDeadline> lCustomDeadlines = new ArrayList<>();
      if ( aWithCustomDeadlines ) {
         DeferralReferenceDeadline lCreateCalendarDayDeadline = new DeferralReferenceDeadline();
         {
            lCreateCalendarDayDeadline.setDataType( DataTypeKey.CDY );
            lCreateCalendarDayDeadline.setQuantity( new BigDecimal( CUSTOM_DEADLINE_DAYS )
                  .setScale( FailDeferRefDead.ALLOWED_CUSTOM_DEADLINE_QUANTITY_DECIMAL_PLACES ) );
         }
         lCustomDeadlines.add( lCreateCalendarDayDeadline );
      }

      List<String> lRecurringInspections = new ArrayList<>();
      if ( aWithRecurringInspections ) {
         UUID lRecurringInspectionId1 = UUID.randomUUID();
         TaskDefnKey lTaskDefnKey =
               TaskDefnTable.create( new TaskDefnKey( 4650, 1 ), lRecurringInspectionId1 ).insert();

         new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskDefn( lTaskDefnKey )
               .build();

         UUID lRecurringInspectionId2 = UUID.randomUUID();
         lTaskDefnKey =
               TaskDefnTable.create( new TaskDefnKey( 4650, 2 ), lRecurringInspectionId2 ).insert();

         new TaskRevisionBuilder().withTaskClass( RefTaskClassKey.REQ ).withTaskDefn( lTaskDefnKey )
               .build();

         lRecurringInspections.add( FormatUtil.uuidToString( RECURRING_INSPECTION_1_ID ) );
         lRecurringInspections.add( FormatUtil.uuidToString( RECURRING_INSPECTION_2_ID ) );
      }

      iDeferralReference = createDeferralReference( lCustomDeadlines, lRecurringInspections );
      iFault = createFault();

      FailDeferRef lDeferralReference = FailDeferRef.findByPrimaryKey( iDeferralReference );
      UUID lDeferralReferenceId = lDeferralReference.getAlternateKey();

      InvInvTable lAircraft = InvInvTable.findByPrimaryKey( iAircraft );
      UUID lAircraftId = lAircraft.getAlternateKey();

      FaultDeferralTO lTO = new FaultDeferralTO();
      {
         lTO.setAircraft( lAircraftId );
         lTO.setAuthorizingHr( iHr );
         lTO.setDeferralReference( lDeferralReferenceId );
         lTO.setFault( iFault );
         lTO.setSeverity( RefFailureSeverityKey.MEL );
      }
      return lTO;
   }


   private FaultKey createFault() {

      ConfigSlotPositionKey lConfigSlotPositionKey = new ConfigSlotPositionKey(
            iConfigSlotKey.getDbId(), iConfigSlotKey.getCd(), iConfigSlotKey.getBomId(), 1 );
      // Create main inventory for the task
      InventoryKey lInventoryKey =
            new InventoryBuilder().withConfigSlotPosition( lConfigSlotPositionKey ).build();
      // Set main inventory for the task
      TaskBuilder lTaskBuilder = new TaskBuilder();
      TaskKey lTask =
            lTaskBuilder.withTaskClass( RefTaskClassKey.CORR ).onInventory( lInventoryKey ).build();
      // Create fault
      SdFaultBuilder lFaultBuilder = new SdFaultBuilder();
      return lFaultBuilder.withFailureSeverity( RefFailureSeverityKey.MEL )
            .withCorrectiveTask( lTask ).withStatus( RefEventStatusKey.CFACTV )
            .onInventory( iAircraft ).build();

   }


   private FailDeferRefKey createDeferralReference(
         final List<DeferralReferenceDeadline> aCustomDeadlines,
         final List<String> aRecurringInspections ) {
      final AssemblyKey lAssemblyKey =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAcftAssy ) {
                  aAcftAssy.setCode( "ACFT-1" );
               }
            } );

      final CarrierKey lCarrierKey = Domain.createOperator( new DomainConfiguration<Operator>() {

         @Override
         public void configure( Operator aOperator ) {
            aOperator.setIATACode( "AA" );
            aOperator.setIATACode( "BBB" );
            aOperator.setCarrierCode( "AA-BBB" );
         }
      } );

      EqpAssmblBom lFailedSystemConfigSlot = EqpAssmblBom.findByPrimaryKey( iConfigSlotKey );
      final UUID lFailedSystemAltId = lFailedSystemConfigSlot.getAlternateKey();

      return Domain.createDeferralReference( new DomainConfiguration<DeferralReference>() {

         @Override
         public void configure( DeferralReference aDeferralReference ) {
            aDeferralReference.setName( DEFERRAL_REFERENCE_NAME );
            aDeferralReference.setStatus( "ACTV" );
            aDeferralReference.setFaultSeverityKey( RefFailureSeverityKey.MEL );
            aDeferralReference.setFaultDeferralKey( new RefFailDeferKey( "MEL A" ) );
            aDeferralReference.setAssemblyKey( lAssemblyKey );
            aDeferralReference.getFailedSystemInfo().setFailedSystemAltId( lFailedSystemAltId );
            aDeferralReference.setInstalledSystems( 1 );
            aDeferralReference.setOperationalSystemsForDispatch( 1 );

            List<CarrierKey> lOperators = new ArrayList<>();
            lOperators.add( lCarrierKey );

            aDeferralReference.setOperators( lOperators );
            aDeferralReference.setDeadlines( aCustomDeadlines );
            aDeferralReference.setRecurringInspections( aRecurringInspections );
         }
      } );
   }

}
