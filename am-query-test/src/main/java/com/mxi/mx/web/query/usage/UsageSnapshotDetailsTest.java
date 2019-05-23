package com.mxi.mx.web.query.usage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.CalculatedUsageParameter;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.InstallationRecord;
import com.mxi.am.domain.Requirement;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


/**
 * Query unit test for UsageSnapshotDetails.qrx
 *
 */
public class UsageSnapshotDetailsTest {

   private final static DataTypeKey USAGE_PARM = DataTypeKey.HOURS;
   private final static String CALC_PARM_CODE = "CALC_PARM_CODE";
   private final static double NA_USAGE_VALUE = 10d;
   private final static BigDecimal NA = null;

   private static final boolean INCLUDE_CALC_PARMS = true;
   private static final boolean DO_NOT_INCLUDE_CALC_PARMS = false;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * Verify that the query returns both usage parameters and calculated parameters associated with
    * an event when aIncludeCalcParms is true.
    */
   @Test
   public void itReturnsAllParmsForEventWhenIncludeCalcParmsIsTrue() {

      // Given a fault against an engine, which is a type of event, with both usage and calculated
      // parameters.
      final AssemblyKey lEngineAssy = createEngineAssyWithCalcParms( CALC_PARM_CODE );
      final DataTypeKey lCalcParm = readParmameter( lEngineAssy, CALC_PARM_CODE );
      final InventoryKey lEngine =
            createEngineWithUsageAndCalcParms( lEngineAssy, USAGE_PARM, lCalcParm );

      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setInventory( lEngine );
            aBuilder.addUsageSnapshot( new UsageSnapshot( lEngine, USAGE_PARM, NA_USAGE_VALUE ) );
            aBuilder.addUsageSnapshot( new UsageSnapshot( lEngine, lCalcParm, NA_USAGE_VALUE ) );
         }
      } );

      // When the query is executed against the event and aIncludeCalcParms is set to true.
      QuerySet lQs = executeQuery( lFault.getEventKey(), INCLUDE_CALC_PARMS );

      // Then the results contain all the parameters.
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the usage parameter: " + USAGE_PARM,
            lReturnedParms.contains( USAGE_PARM ) );
      assertTrue( "The query did not return the calculated parameter: " + lCalcParm,
            lReturnedParms.contains( lCalcParm ) );
   }


   /**
    * Verify that the query does not return calculated parameters associated with an event when
    * aIncludeCalcParms is false.
    */
   @Test
   public void itDoesNotReturnCalcParmsForEventWhenIncludeCalcParmsIsFalse() {

      // Given a fault against an engine, which is a type of event, with both usage and calculated
      // parameters.
      final AssemblyKey lEngineAssy = createEngineAssyWithCalcParms( CALC_PARM_CODE );
      final DataTypeKey lCalcParm = readParmameter( lEngineAssy, CALC_PARM_CODE );
      final InventoryKey lEngine =
            createEngineWithUsageAndCalcParms( lEngineAssy, USAGE_PARM, lCalcParm );

      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setInventory( lEngine );
            aBuilder.addUsageSnapshot( new UsageSnapshot( lEngine, USAGE_PARM, NA_USAGE_VALUE ) );
            aBuilder.addUsageSnapshot( new UsageSnapshot( lEngine, lCalcParm, NA_USAGE_VALUE ) );
         }
      } );

      // When the query is executed against the event and aIncludeCalcParms is set to false.
      QuerySet lQs = executeQuery( lFault.getEventKey(), DO_NOT_INCLUDE_CALC_PARMS );

      // Then the results contain the usage parameter but not the calculated parameter.
      lQs.next();
      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected parameter returned", USAGE_PARM,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
   }


   /**
    *
    * Verify that the query returns both usage parameters and calculated parameters associated with
    * a configuration change event when aIncludeCalcParms is true.
    *
    */
   @Test
   public void itReturnsAllParmsForConfigChangeEventWhenIncludeCalcParmsIsTrue() {

      // Given a requirement with an engine install, which is a type of configuration change event,
      // with both usage and calculated parameters.
      final AssemblyKey lEngineAssy = createEngineAssyWithCalcParms( CALC_PARM_CODE );
      final DataTypeKey lCalcParm = readParmameter( lEngineAssy, CALC_PARM_CODE );
      final InventoryKey lEngine =
            createEngineWithUsageAndCalcParms( lEngineAssy, USAGE_PARM, lCalcParm );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {

            aBuilder.addInstall( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aBuilder ) {
                  aBuilder.setInventory( lEngine );
                  aBuilder.addUsageSnapshot(
                        new UsageSnapshot( lEngine, USAGE_PARM, NA_USAGE_VALUE ) );
                  aBuilder.addUsageSnapshot(
                        new UsageSnapshot( lEngine, lCalcParm, NA_USAGE_VALUE ) );
               }
            } );

         }
      } );

      // When the query is executed against the event and aIncludeCalcParms is set to true.
      QuerySet lQs = executeQuery( lReq.getEventKey(), INCLUDE_CALC_PARMS );

      // Then the results contain all the parameters.
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the usage parameter: " + USAGE_PARM,
            lReturnedParms.contains( USAGE_PARM ) );
      assertTrue( "The query did not return the calculated parameter: " + lCalcParm,
            lReturnedParms.contains( lCalcParm ) );
   }


   /**
    *
    * Verify that the query does not return calculated parameters associated with a configuration
    * change event when aIncludeCalcParms is false.
    *
    */
   @Test
   public void itDoesNotReturnCalcParmsForConfigChangeEventWhenIncludeCalcParmsIsFalse() {

      // Given a requirement with an engine install, which is a type of configuration change event,
      // with both usage and calculated parameters.
      final AssemblyKey lEngineAssy = createEngineAssyWithCalcParms( CALC_PARM_CODE );
      final DataTypeKey lCalcParm = readParmameter( lEngineAssy, CALC_PARM_CODE );
      final InventoryKey lEngine =
            createEngineWithUsageAndCalcParms( lEngineAssy, USAGE_PARM, lCalcParm );

      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aBuilder ) {

            aBuilder.addInstall( new DomainConfiguration<InstallationRecord>() {

               @Override
               public void configure( InstallationRecord aBuilder ) {
                  aBuilder.setInventory( lEngine );
                  aBuilder.addUsageSnapshot(
                        new UsageSnapshot( lEngine, USAGE_PARM, NA_USAGE_VALUE ) );
                  aBuilder.addUsageSnapshot(
                        new UsageSnapshot( lEngine, lCalcParm, NA_USAGE_VALUE ) );
               }
            } );

         }
      } );

      // When the query is executed against the event and aIncludeCalcParms is set to false.
      QuerySet lQs = executeQuery( lReq.getEventKey(), DO_NOT_INCLUDE_CALC_PARMS );

      // Then the results contain the usage parameter but not the calculated parameter.
      lQs.next();
      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );
      assertEquals( "Unexpected parameter returned", USAGE_PARM,
            lQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
   }


   /**
    * Verify that the default value for aIncludeCalcParms is true.
    */
   @Test
   public void itReturnsAllParmsByDefaultWhenIncludeCalcParmsIsNotProvided() {

      // Given a fault against an engine, which is a type of event, with both usage and calculated
      // parameters.
      final AssemblyKey lEngineAssy = createEngineAssyWithCalcParms( CALC_PARM_CODE );
      final DataTypeKey lCalcParm = readParmameter( lEngineAssy, CALC_PARM_CODE );
      final InventoryKey lEngine =
            createEngineWithUsageAndCalcParms( lEngineAssy, USAGE_PARM, lCalcParm );

      FaultKey lFault = Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setInventory( lEngine );
            aBuilder.addUsageSnapshot( new UsageSnapshot( lEngine, USAGE_PARM, NA_USAGE_VALUE ) );
            aBuilder.addUsageSnapshot( new UsageSnapshot( lEngine, lCalcParm, NA_USAGE_VALUE ) );
         }
      } );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocaleTask", NA );
      lArgs.add( "aLocaleWorkPAckage", NA );
      lArgs.add( "aLocaleRemoved", NA );
      lArgs.add( "aLocaleInstalled", NA );
      lArgs.add( lFault.getEventKey(), "aEventDbId", "aEventId" );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );

      // Then by default the results contain all the parameters.
      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );

      List<DataTypeKey> lReturnedParms = getParmsFromQueryResults( lQs );

      assertTrue( "The query did not return the usage parameter: " + USAGE_PARM,
            lReturnedParms.contains( USAGE_PARM ) );
      assertTrue( "The query did not return the calculated parameter: " + lCalcParm,
            lReturnedParms.contains( lCalcParm ) );
   }


   @Test
   public void itReturnsAircraftUsageFromWorkPackageUsageSnapshotForTaskUsageSnapshotDetails() {

      String lAircraftDescription = "Desc";
      InventoryKey lAircraft =
            Domain.createAircraft( aAcft -> aAcft.setDescription( lAircraftDescription ) );

      UsageSnapshot lTaskUsageSnapshot =
            new UsageSnapshot( lAircraft, DataTypeKey.HOURS, BigDecimal.ONE );

      TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setInventory( lAircraft );
         aReq.addUsage( lTaskUsageSnapshot );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
      } );

      UsageSnapshot lWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, DataTypeKey.HOURS, BigDecimal.TEN );

      Domain.createWorkPackage( aWp -> {
         aWp.setAircraft( lAircraft );
         aWp.addTask( lTask );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshot );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aLocaleTask", "Task" );
      lArgs.add( "aLocaleWorkPAckage", "Work Package" );
      lArgs.add( "aLocaleRemoved", NA );
      lArgs.add( "aLocaleInstalled", NA );

      lArgs.add( lTask.getEventKey(), "aEventDbId", "aEventId" );
      lArgs.add( "aIncludeCalcParms", 1 );

      QuerySet lQs = QueryExecutor.executeQuery( getClass(), lArgs );

      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );
      String lExpectedAircraftDescTaskSnapshot = lAircraftDescription + " [" + "Task" + "]";
      String lExpectedAircraftDescWorkPkgSnapshot =
            lAircraftDescription + " [" + "Work Package" + "]";
      while ( lQs.next() ) {

         InventoryKey lResultInventoryKey = lQs.getKey( InventoryKey.class, "inv_no_key" );
         DataTypeKey lResultDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
         BigDecimal lResultUsage = lQs.getBigDecimal( "tsn_qt" );

         String lResultAircraftDescSnapshot = lQs.getString( "inv_no_sdesc" );

         UsageSnapshot lResultUsageSnapshot =
               new UsageSnapshot( lResultInventoryKey, lResultDataType, lResultUsage );
         if ( lResultUsageSnapshot.equals( lWorkPackageUsageSnapshot ) ) {
            assertEquals( "UnExpected results returned", lExpectedAircraftDescWorkPkgSnapshot,
                  lResultAircraftDescSnapshot );
         } else if ( lResultUsageSnapshot.equals( lTaskUsageSnapshot ) ) {
            assertEquals( "UnExpected results returned", lExpectedAircraftDescTaskSnapshot,
                  lResultAircraftDescSnapshot );
         } else
            fail( "UnExpected results returned" );

      }

   }


   @Test
   public void
         itReturnsAircraftUsageFromWorkPackageUsageSnapshotAndComponentUsageFromTaskUsageSnapshotForTaskUsageSnapshotDetails() {

      final PartNoKey lAircraftPart =
            Domain.createPart( aAcftPart -> aAcftPart.setInventoryClass( RefInvClassKey.ACFT ) );
      final PartNoKey lTrkPart =
            Domain.createPart( aAcftPart -> aAcftPart.setInventoryClass( RefInvClassKey.TRK ) );

      AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.addPartGroup( rootConfigSlotPartGroup -> {
               rootConfigSlotPartGroup.setInventoryClass( RefInvClassKey.ACFT );
               rootConfigSlotPartGroup.addPart( lAircraftPart );
            } );
            rootConfigSlot.addConfigurationSlot( trkSlot -> {
               trkSlot.setConfigurationSlotClass( RefBOMClassKey.TRK );
               trkSlot.setCode( "TRK_SLOT" );
               trkSlot.addPartGroup( subAssyConfigSlotPartGroup -> {
                  subAssyConfigSlotPartGroup.setInventoryClass( RefInvClassKey.TRK );
                  subAssyConfigSlotPartGroup.addPart( lTrkPart );
               } );
            } );
         } );
      } );

      InventoryKey lAircraft = Domain.createAircraft( aAcft -> {
         aAcft.setAssembly( lAircraftAssembly );
         aAcft.setPart( lAircraftPart );
      } );

      InventoryKey lTrk = Domain.createTrackedInventory( aTrk -> {
         aTrk.setParent( lAircraft );
         aTrk.setPartNumber( lTrkPart );
      } );

      UsageSnapshot lTaskUsageSnapshot =
            new UsageSnapshot( lTrk, DataTypeKey.HOURS, BigDecimal.TEN );

      TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setInventory( lTrk );
         aReq.addUsage( lTaskUsageSnapshot );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
      } );

      UsageSnapshot lWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, DataTypeKey.HOURS, BigDecimal.TEN );

      Domain.createWorkPackage( aWp -> {
         aWp.setAircraft( lAircraft );
         aWp.addTask( lTask );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshot );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      QuerySet lQs = executeQuery( lTask.getEventKey(), false );

      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {

         InventoryKey lResultInventoryKey = lQs.getKey( InventoryKey.class, "inv_no_key" );
         DataTypeKey lResultDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
         BigDecimal lResultUsage = lQs.getBigDecimal( "tsn_qt" );
         UsageSnapshot lResultUsageSnapshot =
               new UsageSnapshot( lResultInventoryKey, lResultDataType, lResultUsage );
         if ( lResultInventoryKey.equals( lAircraft ) ) {
            assertEquals( "UnExpected usage snapshot returned", lWorkPackageUsageSnapshot,
                  lResultUsageSnapshot );
         } else if ( lResultInventoryKey.equals( lTrk ) ) {
            assertEquals( "UnExpected usage snapshot returned", lTaskUsageSnapshot,
                  lResultUsageSnapshot );
         } else
            fail( "Unexpected inventory returned by the query" );

      }
   }


   @Test
   public void
         itReturnsAircraftUsageFromWorkPackageUsageSnapshotAndEngineFromTaskUsageSnapshotForTaskOnEngineAssembly() {

      InventoryKey lAircraft = Domain.createAircraft();

      InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.setParent( lAircraft );
      } );

      UsageSnapshot lTaskUsageSnapshot =
            new UsageSnapshot( lEngine, DataTypeKey.HOURS, BigDecimal.TEN );

      TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setInventory( lEngine );
         aReq.addUsage( lTaskUsageSnapshot );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
      } );

      UsageSnapshot lWorkPackageUsageSnapshot =
            new UsageSnapshot( lAircraft, DataTypeKey.HOURS, BigDecimal.TEN );

      Domain.createWorkPackage( aWp -> {
         aWp.setAircraft( lAircraft );
         aWp.addTask( lTask );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshot );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      QuerySet lQs = executeQuery( lTask.getEventKey(), false );

      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {

         InventoryKey lResultInventoryKey = lQs.getKey( InventoryKey.class, "inv_no_key" );
         DataTypeKey lResultDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
         BigDecimal lResultUsage = lQs.getBigDecimal( "tsn_qt" );
         UsageSnapshot lResultUsageSnapshot =
               new UsageSnapshot( lResultInventoryKey, lResultDataType, lResultUsage );
         if ( lResultInventoryKey.equals( lAircraft ) ) {
            assertEquals( "UnExpected usage snapshot returned", lWorkPackageUsageSnapshot,
                  lResultUsageSnapshot );
         } else if ( lResultInventoryKey.equals( lEngine ) ) {
            assertEquals( "UnExpected usage snapshot returned", lTaskUsageSnapshot,
                  lResultUsageSnapshot );
         } else
            fail( "Unexpected inventory returned by the query" );
      }
   }


   @Test
   public void
         itReturnsEngineSubAssyFromWorkPackageUsageSnapshotAndTrkInvFromTaskUsageSnapshotForTaskOnEngineSubAssyTrkSubComponent() {

      InventoryKey lAircraft = Domain.createAircraft();

      InventoryKey lEngine = Domain.createEngine( aEngine -> {
         aEngine.setParent( lAircraft );
      } );

      InventoryKey lTrk = Domain.createTrackedInventory( aTrk -> {
         aTrk.setParent( lEngine );
      } );

      UsageSnapshot lTaskUsageSnapshot =
            new UsageSnapshot( lTrk, DataTypeKey.HOURS, BigDecimal.TEN );

      TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setInventory( lTrk );
         aReq.addUsage( lTaskUsageSnapshot );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
      } );

      UsageSnapshot lWorkPackageUsageSnapshotAircraft =
            new UsageSnapshot( lAircraft, DataTypeKey.HOURS, BigDecimal.TEN );

      UsageSnapshot lWorkPackageUsageSnapshotEngine =
            new UsageSnapshot( lEngine, DataTypeKey.HOURS, BigDecimal.TEN );

      Domain.createWorkPackage( aWp -> {
         aWp.setAircraft( lAircraft );
         aWp.addTask( lTask );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshotAircraft );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshotEngine );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      QuerySet lQs = executeQuery( lTask.getEventKey(), false );

      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {

         InventoryKey lResultInventoryKey = lQs.getKey( InventoryKey.class, "inv_no_key" );
         DataTypeKey lResultDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
         BigDecimal lResultUsage = lQs.getBigDecimal( "tsn_qt" );
         UsageSnapshot lResultUsageSnapshot =
               new UsageSnapshot( lResultInventoryKey, lResultDataType, lResultUsage );
         if ( lResultInventoryKey.equals( lEngine ) ) {
            assertEquals( "UnExpected usage snapshot returned", lWorkPackageUsageSnapshotEngine,
                  lResultUsageSnapshot );
         } else if ( lResultInventoryKey.equals( lTrk ) ) {
            assertEquals( "UnExpected usage snapshot returned", lTaskUsageSnapshot,
                  lResultUsageSnapshot );
         } else
            fail( "Unexpected inventory returned by the query" );
      }
   }


   @Test
   public void
         itReturnsEngineAssyUsageFromWorkPackageUsageSnapshotAndComponentFromTaskUsageSnapshotForTaskOnComponentConnectedToEngineAssembly() {

      InventoryKey lEngine = Domain.createEngine();

      InventoryKey lTrk = Domain.createTrackedInventory( aTrk -> aTrk.setParent( lEngine ) );

      UsageSnapshot lTaskUsageSnapshot =
            new UsageSnapshot( lTrk, DataTypeKey.HOURS, BigDecimal.TEN );

      TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setInventory( lTrk );
         aReq.addUsage( lTaskUsageSnapshot );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
      } );

      UsageSnapshot lWorkPackageUsageSnapshot =
            new UsageSnapshot( lEngine, DataTypeKey.HOURS, BigDecimal.TEN );

      Domain.createComponentWorkPackage( aWp -> {
         aWp.setInventory( lEngine );
         aWp.assignTask( lTask );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshot );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      DataSetArgument lArgs = new DataSetArgument();

      QuerySet lQs = executeQuery( lTask.getEventKey(), false );

      assertEquals( "The query did not return the expected number of rows.", 2, lQs.getRowCount() );
      while ( lQs.next() ) {

         InventoryKey lResultInventoryKey = lQs.getKey( InventoryKey.class, "inv_no_key" );
         DataTypeKey lResultDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
         BigDecimal lResultUsage = lQs.getBigDecimal( "tsn_qt" );
         UsageSnapshot lResultUsageSnapshot =
               new UsageSnapshot( lResultInventoryKey, lResultDataType, lResultUsage );
         if ( lResultInventoryKey.equals( lEngine ) ) {
            assertEquals( "UnExpected usage snapshot returned", lWorkPackageUsageSnapshot,
                  lResultUsageSnapshot );
         } else if ( lResultInventoryKey.equals( lTrk ) ) {
            assertEquals( "UnExpected usage snapshot returned", lTaskUsageSnapshot,
                  lResultUsageSnapshot );
         } else
            fail( "Unexpected inventory returned by the query" );
      }
   }


   @Test
   public void itReturnsSubComponentUsageFromTaskUsageSnapshotForTaskOnLooseSubComponent() {

      InventoryKey lTrk = Domain.createTrackedInventory();

      InventoryKey lSubTrk = Domain.createTrackedInventory( aSub -> aSub.setParent( lTrk ) );

      UsageSnapshot lTaskUsageSnapshot =
            new UsageSnapshot( lSubTrk, DataTypeKey.HOURS, BigDecimal.TEN );

      TaskKey lTask = Domain.createRequirement( aReq -> {
         aReq.setInventory( lSubTrk );
         aReq.addUsage( lTaskUsageSnapshot );
         aReq.setStatus( RefEventStatusKey.COMPLETE );
      } );

      UsageSnapshot lWorkPackageUsageSnapshot =
            new UsageSnapshot( lTrk, DataTypeKey.HOURS, BigDecimal.TEN );

      Domain.createComponentWorkPackage( aWp -> {
         aWp.setInventory( lTrk );
         aWp.assignTask( lTask );
         aWp.addUsageSnapshot( lWorkPackageUsageSnapshot );
         aWp.setStatus( RefEventStatusKey.COMPLETE );
      } );

      QuerySet lQs = executeQuery( lTask.getEventKey(), false );

      assertEquals( "The query did not return the expected number of rows.", 1, lQs.getRowCount() );
      lQs.next();

      InventoryKey lResultInventoryKey = lQs.getKey( InventoryKey.class, "inv_no_key" );
      DataTypeKey lResultDataType = lQs.getKey( DataTypeKey.class, "data_type_key" );
      BigDecimal lResultUsage = lQs.getBigDecimal( "tsn_qt" );
      UsageSnapshot lResultUsageSnapshot =
            new UsageSnapshot( lResultInventoryKey, lResultDataType, lResultUsage );
      assertEquals( "UnExpected usage snapshot returned", lTaskUsageSnapshot,
            lResultUsageSnapshot );
   }


   //
   // Private methods (end of test methods).
   //

   private AssemblyKey createEngineAssyWithCalcParms( final String aCalcParmCode ) {

      return Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

         @Override
         public void configure( EngineAssembly aBuilder ) {
            aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

               @Override
               public void configure( ConfigurationSlot aBuilder ) {
                  aBuilder.addCalculatedUsageParameter(
                        new DomainConfiguration<CalculatedUsageParameter>() {

                           @Override
                           public void configure( CalculatedUsageParameter aBuilder ) {
                              aBuilder.setCode( aCalcParmCode );
                           }
                        } );
               }
            } );
         }
      } );
   }


   private InventoryKey createEngineWithUsageAndCalcParms( final AssemblyKey aEngineAssy,
         final DataTypeKey aUsageParm, final DataTypeKey aCalcParm ) {

      return Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( aEngineAssy );
            aBuilder.addUsage( aUsageParm, NA );
            aBuilder.addUsage( aCalcParm, NA );
         }
      } );
   }


   private DataTypeKey readParmameter( AssemblyKey aAssy, String aParmCode ) {
      return Domain.readUsageParameter( Domain.readRootConfigurationSlot( aAssy ), aParmCode );
   }


   private QuerySet executeQuery( EventKey aEvent, boolean aIncludeCalcParms ) {
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aLocaleTask", NA );
      lArgs.add( "aLocaleWorkPAckage", NA );
      lArgs.add( "aLocaleRemoved", NA );
      lArgs.add( "aLocaleInstalled", NA );

      lArgs.add( aEvent, "aEventDbId", "aEventId" );
      lArgs.add( "aIncludeCalcParms", aIncludeCalcParms );

      return QueryExecutor.executeQuery( getClass(), lArgs );
   }


   private List<DataTypeKey> getParmsFromQueryResults( QuerySet aQs ) {

      int lRowNum = aQs.getRowNumber();
      aQs.beforeFirst();

      List<DataTypeKey> lParms = new ArrayList<DataTypeKey>();
      while ( aQs.next() ) {
         lParms.add( aQs.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );
      }
      aQs.setRowNumber( lRowNum );

      return lParms;
   }

}
