package com.mxi.mx.web.query.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.EventKeyInterface;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefFaultSourceKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.table.sd.SdFaultTable;


@RunWith( BlockJUnit4ClassRunner.class )
public class ParentFaultDetailsQueryTest {

   // Query under test
   private static final String QUERY = "com.mxi.mx.web.query.fault.ParentFaultDetails";

   // Test Data
   private static final Date FAULT_FOUND_ON_DATE =
         DateUtils.truncate( new Date(), Calendar.SECOND );
   private static final boolean FAULT_IS_EVALUATED = true;
   private static final String FAULT_NAME = "Test Fault";
   private static final String FAULT_OPERATIONAL_RESTRICTION = "An operational restriction.";
   private static final RefFailureSeverityKey FAULT_SEVERITY = RefFailureSeverityKey.MEL;
   private static final String FAULT_SEVERITY_MSG = "MEL (MEL failure)";
   private static final RefFaultSourceKey FAULT_SOURCE = RefFaultSourceKey.MECH;
   private static final String FAULT_SOURCE_MSG = "MECH (Mechanic)";
   private static final RefEventStatusKey FAULT_STATUS = RefEventStatusKey.CFCERT;
   private static final String CONFIG_SLOT_ATA = "10-20-30";
   private static final String CONFIG_SLOT_NAME = "Test Resolution Config slot";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * This test was added when the Resolution Config Slot field was included. It should ideally
    * cover all the data in the query under test but due to time constraints it is acting as a bit
    * of a smoke test. There was no prior coverage of this query beforehand.
    */
   @Test
   public void itLoadsBasicFaultDetails() {
      EventKey lCorrectiveTaskEvent = createFaultWithResolutionConfigSlot();

      QuerySet lFaultQs = executeQuery( lCorrectiveTaskEvent );

      assertTrue( lFaultQs.first() );
      assertEquals( FAULT_FOUND_ON_DATE, lFaultQs.getDate( "raised_gdt" ) );
      assertEquals( FAULT_IS_EVALUATED, lFaultQs.getBoolean( "eval_bool" ) );
      assertEquals( FAULT_NAME, lFaultQs.getString( "fault_sdesc" ) );
      assertEquals( FAULT_OPERATIONAL_RESTRICTION, lFaultQs.getString( "op_restriction_ldesc" ) );
      assertEquals( FAULT_SEVERITY_MSG, lFaultQs.getString( "severity" ) );
      assertEquals( FAULT_SOURCE_MSG, lFaultQs.getString( "fault_source" ) );
      assertEquals( FAULT_STATUS.getCd(), lFaultQs.getString( "status" ) );
      assertEquals( CONFIG_SLOT_ATA, lFaultQs.getString( "resolution_config_slot_ata" ) );
      assertEquals( CONFIG_SLOT_NAME, lFaultQs.getString( "resolution_config_slot_name" ) );
   }


   private EventKey createFaultWithResolutionConfigSlot() {
      final SchedStaskTable lCorrectiveTask = createCorrectiveTask();
      final SdFaultTable lFault =
            createFault( lCorrectiveTask.getPk(), new ConfigSlotBuilder( CONFIG_SLOT_ATA )
                  .withClass( RefBOMClassKey.TRK ).withName( CONFIG_SLOT_NAME ).build() );

      createFaultInventoryEvent( lFault.getPk() );
      EventKey lCorrectiveTaskEvent = new EventKey( lCorrectiveTask.getPk().toString() );
      return lCorrectiveTaskEvent;
   }


   private SdFaultTable createFault( final TaskKey aCorrectiveTaskKey,
         final ConfigSlotKey aResolutionConfigSlot ) {
      final FaultKey lFaultKey = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aFault ) {
            aFault.setCorrectiveTask( aCorrectiveTaskKey );
            aFault.setFaultSource( FAULT_SOURCE );
            aFault.setFoundOnDate( FAULT_FOUND_ON_DATE );
            aFault.setIsEvaluated( FAULT_IS_EVALUATED );
            aFault.setName( FAULT_NAME );
            aFault.setOperationalRestriction( FAULT_OPERATIONAL_RESTRICTION );
            aFault.setResolutionConfigSlot( aResolutionConfigSlot );
            aFault.setSeverity( FAULT_SEVERITY );
            aFault.setStatus( FAULT_STATUS );
         }
      } );
      return SdFaultTable.findByPrimaryKey( lFaultKey );
   }


   private SchedStaskTable createCorrectiveTask() {
      final TaskKey lCorrectiveTaskKey =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aTask ) {
               }
            } );

      return SchedStaskTable.findByPrimaryKey( lCorrectiveTaskKey );
   }


   private EvtInvTable createFaultInventoryEvent( final FaultKey lFaultKey ) {
      EvtInvTable lFaultInvEvent = EvtInvTable.create( new EventKeyInterface() {

         @Override
         public EventKey getEventKey() {
            return new EventKey( lFaultKey.toString() );
         }
      } );
      lFaultInvEvent.setMainInvBool( true );
      lFaultInvEvent.insert();

      return lFaultInvEvent;
   }


   private QuerySet executeQuery( EventKey aCorrectiveTaskEvent ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aCorrectiveTaskEvent, "aEventDbId", "aEventId" );
      return QuerySetFactory.getInstance().executeQuery( QUERY, lArgs );
   }

}
