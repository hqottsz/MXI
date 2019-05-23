package com.mxi.mx.web.jsp.controller.flight;

import static com.mxi.mx.core.key.DataTypeKey.CYCLES;
import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static com.mxi.mx.core.key.DataTypeKey.LANDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefDataSourceKey;
import com.mxi.mx.core.key.UsageDefinitionKey;
import com.mxi.mx.core.table.eqp.EqpDataSourceSpec;
import com.mxi.mx.core.table.inv.InvInvTable;


public class EditFlightHelperTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private InventoryKey iAircraft;
   private FlightLegId iFlightLeg;

   private final static String CALC_PARM_CODE_1 = "CALC_PARM_CODE_1";
   private final static String CALC_PARM_CODE_2 = "CALC_PARM_CODE_2";
   private final static String ENG_ASSY_CODE_1 = "ENG_1";
   private final static String ENG_ASSY_CODE_2 = "ENG_2";

   private final static BigDecimal NA = null;


   @Test
   public void getAssyUsageDataSetForEditingExistingFlight_withReorderedUsageDefinition() {

      // Reorder
      reorderParameter( iAircraft, HOURS, 3 );
      reorderParameter( iAircraft, CYCLES, 1 );
      reorderParameter( iAircraft, LANDING, 2 );

      // After reorder
      DataSet lUsagesDataSetAfterReorder =
            EditFlightHelper.getAssyUsageDataSetForEditingExistingFlight( iFlightLeg );

      assertUsageDataBaselineOrder( lUsagesDataSetAfterReorder, 3, 1, 2 );
   }


   @Test
   public void
         getAssyUsageDataSetForEditingExistingFlight_withUnassignedAndReorderedUsageDefinition() {

      // Unassign and reorder
      unassignParameter( iAircraft, HOURS );
      reorderParameter( iAircraft, CYCLES, 1 );
      reorderParameter( iAircraft, LANDING, 2 );

      // After unassign and reorder
      DataSet lUsagesDataSetAfterReorder =
            EditFlightHelper.getAssyUsageDataSetForEditingExistingFlight( iFlightLeg );

      // Assert the unassigned usage parameter's baseline_order is null, then when sorting by
      // baseline order, it will be after other still assigned usage parameters
      assertUsageDataBaselineOrder( lUsagesDataSetAfterReorder, null, 1, 2 );
   }


   /**
    * Verify that the query returns an aircraft's editable usage parameter associated with a flight.
    */
   @Test
   public void
         getAssyUsageDataSetForEditingExistingFlight_returnsEdiableUsageParametersForAircraft() {

      //
      // Given an aircraft with an associated editable usage parameter.
      //

      // Create an aircraft assembly with an editable usage parameter assigned to its root config
      // slot.
      final AssemblyKey lAcftAssy = Domain.createAircraftAssembly( aBuilder -> aBuilder
            .setRootConfigurationSlot( aBuilder1 -> aBuilder1.addUsageParameter( HOURS ) ) );

      // Create an aircraft based on the aircraft assembly
      final InventoryKey lAcft =
            Domain.createAircraft( aBuilder -> aBuilder.setAssembly( lAcftAssy ) );

      //
      // Given a flight for the aircraft that collected the editable usage parameter.
      //
      FlightLegId lFlight =
            Domain.createFlight( aBuilder -> aBuilder.addUsage( lAcft, HOURS, NA ) );

      //
      // When the query is executed.
      //
      DataSet lUsageDataSet =
            EditFlightHelper.getAssyUsageDataSetForEditingExistingFlight( lFlight );

      //
      // Then the results contain the editable usage parameter.
      //
      assertEquals( "The query did not return the expected number of rows.", 1,
            lUsageDataSet.getRowCount() );
      lUsageDataSet.next();
      assertEquals( "The query did not return the ediable usage parameter.", HOURS,
            lUsageDataSet.getKey( DataTypeKey.class, "data_type_db_id", "data_type_id" ) );

   }


   /**
    *
    * Verify that the query does not return aircraft calculated parameters associated with a flight.
    *
    */
   @Test
   public void
         getAssyUsageDataSetForEditingExistingFlight_doesNotReturnCalculatedParametersForAircraft() {

      //
      // Given an aircraft with an associated calculated parameter.
      //

      // Create an aircraft assembly with a calculated parameter assigned to its root config slot.
      final AssemblyKey lAcftAssy = Domain.createAircraftAssembly( aBuilder -> aBuilder
            .setRootConfigurationSlot( aBuilder2 -> aBuilder2.addCalculatedUsageParameter(
                  aBuilder1 -> aBuilder1.setCode( CALC_PARM_CODE_1 ) ) ) );

      // Get the calculated parameter data type.
      final DataTypeKey lCalcParm = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lAcftAssy ), CALC_PARM_CODE_1 );

      // Create an aircraft based on the aircraft assembly
      final InventoryKey lAcft =
            Domain.createAircraft( aBuilder -> aBuilder.setAssembly( lAcftAssy ) );

      //
      // Given a flight for the aircraft that collected a calculated parameter.
      //
      FlightLegId lFlight =
            Domain.createFlight( aBuilder -> aBuilder.addUsage( lAcft, lCalcParm, NA ) );

      //
      // When the query is executed.
      //
      DataSet lUsageDataSet =
            EditFlightHelper.getAssyUsageDataSetForEditingExistingFlight( lFlight );

      //
      // Then the results do NOT contain the calculated parameter.
      //
      assertTrue( "Calculated parameter unexpectedly returned from query",
            lUsageDataSet.isEmpty() );

   }


   /**
    *
    * Verify that the query does not return sub-assembly calculated parameters associated with a
    * flight.
    *
    */
   @Test
   public void
         getAssyUsageDataSetForEditingExistingFlight_doesNotReturnCalculatedParametersForSubAssemblies() {

      //
      // Given and aircraft with sub-assemblies that have associated calculated parameters.
      //

      // Create one engine assembly with a calculated parameter assigned to its root config slot.
      final AssemblyKey lEngAssy1 = Domain.createEngineAssembly( aBuilder -> {
         aBuilder.setCode( ENG_ASSY_CODE_1 );
         aBuilder.setRootConfigurationSlot( aBuilder2 -> aBuilder2
               .addCalculatedUsageParameter( aBuilder1 -> aBuilder1.setCode( CALC_PARM_CODE_1 ) ) );
      } );

      // Create another engine assembly with a calculated parameter assigned to its root config
      // slot.
      final AssemblyKey lEngAssy2 = Domain.createEngineAssembly( aBuilder -> {
         aBuilder.setCode( ENG_ASSY_CODE_2 );
         aBuilder.setRootConfigurationSlot( aBuilder1 -> aBuilder1
               .addCalculatedUsageParameter( aBuilder2 -> aBuilder2.setCode( CALC_PARM_CODE_2 ) ) );
      } );

      // Get the calculated parameter data types.
      final DataTypeKey lCalcParm1 = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lEngAssy1 ), CALC_PARM_CODE_1 );
      final DataTypeKey lCalcParm2 = Domain
            .readUsageParameter( Domain.readRootConfigurationSlot( lEngAssy2 ), CALC_PARM_CODE_2 );

      // Create engines based on the engine assemblies.
      final InventoryKey lEng1 =
            Domain.createEngine( aBuilder -> aBuilder.setAssembly( lEngAssy1 ) );

      final InventoryKey lEng2 =
            Domain.createEngine( aBuilder -> aBuilder.setAssembly( lEngAssy2 ) );

      // Create an aircraft with the engines.
      final InventoryKey lAcft = Domain.createAircraft( aBuilder -> {
         aBuilder.addEngine( lEng1 );
         aBuilder.addEngine( lEng2 );
      } );

      //
      // Given a flight for the aircraft that collected the engine calculated parameters.
      //
      FlightLegId lFlight = Domain.createFlight( aBuilder -> {
         aBuilder.setAircraft( lAcft );
         aBuilder.addUsage( lEng1, lCalcParm1, NA );
         aBuilder.addUsage( lEng2, lCalcParm2, NA );
      } );

      //
      // When the query is executed.
      //
      DataSet lUsageDataSet =
            EditFlightHelper.getAssyUsageDataSetForEditingExistingFlight( lFlight );
      //
      // Then the results do NOT contain the calculated parameter.
      //
      assertTrue( "Calculated parameter unexpectedly returned from query",
            lUsageDataSet.isEmpty() );

   }


   private InventoryKey createAircraftWithHoursCyclesLandingUsage() {
      InventoryKey lAircraftKey = Domain.createAircraft( aAircraft -> {
         aAircraft.addUsage( HOURS, new BigDecimal( 200 ) );
         aAircraft.addUsage( CYCLES, new BigDecimal( 50 ) );
         aAircraft.addUsage( LANDING, new BigDecimal( 100 ) );
      } );

      // Create flight data source specification
      UsageDefinitionKey lUsageDefinition = new UsageDefinitionKey(
            InvInvTable.getAssemblyByInventoryKey( lAircraftKey ), RefDataSourceKey.MXFL );
      EqpDataSourceSpec.create( lUsageDefinition, HOURS );
      EqpDataSourceSpec.create( lUsageDefinition, CYCLES );
      EqpDataSourceSpec.create( lUsageDefinition, LANDING );

      return lAircraftKey;
   }


   /**
    * Reorder MXFL usage parameters.
    *
    * @param aInventoryKey
    *           the assembly
    * @param aDataTypeKey
    *           the usage parameter
    * @param aNewOrder
    *           the new order
    */
   private void reorderParameter( InventoryKey aInventoryKey, DataTypeKey aDataTypeKey,
         int aNewOrder ) {
      EqpDataSourceSpec lUsageParameter = EqpDataSourceSpec.findByPrimaryKey(
            new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ),
            aDataTypeKey );
      lUsageParameter.setDataOrd( aNewOrder );
      lUsageParameter.update();
   }


   /**
    * Unassign MXFL usage parameters.
    *
    * @param aInventoryKey
    *           the assembly
    * @param aDataTypeKey
    *           the usage parameter
    */
   private void unassignParameter( InventoryKey aInventoryKey, DataTypeKey aDataTypeKey ) {
      EqpDataSourceSpec lUsageParameter = EqpDataSourceSpec.findByPrimaryKey(
            new UsageDefinitionKey( InvInvTable.getAssemblyByInventoryKey( aInventoryKey ),
                  RefDataSourceKey.MXFL ),
            aDataTypeKey );
      lUsageParameter.delete();
   }


   /**
    * Assert the "baseline_order" in the usage DataSet is consistent with the order defined in usage
    * definition.
    *
    * @param aCreateFlightUsagesDS
    *           the DataSet
    * @param aHoursOrder
    *           HOURS order
    * @param aCyclesOrder
    *           CYCLES order
    * @param aLandingOrder
    *           LANDING order
    */
   private void assertUsageDataBaselineOrder( DataSet aCreateFlightUsagesDS, Integer aHoursOrder,
         Integer aCyclesOrder, Integer aLandingOrder ) {
      assertEquals( "Expected three results", 3, aCreateFlightUsagesDS.getRowCount() );

      Map<DataTypeKey, Integer> lDataTypeBaselineOrderMap = new HashMap<>();
      while ( aCreateFlightUsagesDS.next() ) {
         lDataTypeBaselineOrderMap.put(
               aCreateFlightUsagesDS.getKey( DataTypeKey.class, "data_type_key" ),
               aCreateFlightUsagesDS.getInteger( "baseline_order" ) );
      }

      boolean lHoursExists = false;
      boolean lCyclesExists = false;
      boolean lLandingExists = false;
      for ( DataTypeKey lUsageParameter : lDataTypeBaselineOrderMap.keySet() ) {
         if ( HOURS.equals( lUsageParameter ) ) {
            lHoursExists = true;
            assertEquals( "Expected the baseline order of HOURS is " + aHoursOrder, aHoursOrder,
                  lDataTypeBaselineOrderMap.get( lUsageParameter ) );
         } else if ( CYCLES.equals( lUsageParameter ) ) {
            lCyclesExists = true;
            assertEquals( "Expected the baseline order of CYCLES is " + aCyclesOrder, aCyclesOrder,
                  lDataTypeBaselineOrderMap.get( lUsageParameter ) );
         } else if ( LANDING.equals( lUsageParameter ) ) {
            lLandingExists = true;
            assertEquals( "Expected the order of LANDING is " + aLandingOrder, aLandingOrder,
                  lDataTypeBaselineOrderMap.get( lUsageParameter ) );
         }
      }
      assertTrue( lHoursExists && lCyclesExists && lLandingExists );
   }


   @Before
   public void setUpData() {

      iAircraft = createAircraftWithHoursCyclesLandingUsage();

      iFlightLeg = Domain.createFlight( aBuilder -> {
         aBuilder.setAircraft( iAircraft );
         aBuilder.addUsage( iAircraft, HOURS, new BigDecimal( 10.0 ), null );
         aBuilder.addUsage( iAircraft, CYCLES, new BigDecimal( 2.0 ), null );
         aBuilder.addUsage( iAircraft, LANDING, new BigDecimal( 3.0 ), null );
      } );
   }
}
