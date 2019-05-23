
package com.mxi.mx.core.unittest.scenario.usage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.diagnostics.DiagnosticsAdapterService;
import com.mxi.mx.core.services.diagnostics.Usage;
import com.mxi.mx.core.services.event.usage.CollectedUsageParm;
import com.mxi.mx.core.services.flighthist.FlightHistService;
import com.mxi.mx.core.services.flighthist.FlightInformationTO;
import com.mxi.mx.core.services.flighthist.Measurement;
import com.mxi.mx.core.services.inventory.InvUtils;
import com.mxi.mx.core.services.inventory.InventoryInfoTO;
import com.mxi.mx.core.services.inventory.InventoryLocatorService;
import com.mxi.mx.core.services.inventory.creation.InventoryCreationService;
import com.mxi.mx.core.unittest.table.inv.InvCurrUsage;
import com.mxi.mx.core.usage.service.UsageDelta;
import com.mxi.mx.core.usage.service.UsageUtils;


/**
 * This is a scenario test case to ensure that tracked parts properly track usage depending on the
 * config slot to which they are attached. They should always have a record in inv_curr_usage of the
 * tracked parms but should only accrue usage that is specified on the config slot.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MultiParmTackedPartsTest {

   /**
    * The human resource. This number is based on
    * com.mxi.mx.common.ejb.security.SecurityIdentityStub.getCurrentUserId()
    */
   private static final HumanResourceKey HR = new HumanResourceKey( 4650, 9999999 );

   /**
    * The user ID. This number is based on
    * com.mxi.mx.common.ejb.security.SecurityIdentityStub.getCurrentUserId()
    */
   private static final int USER_ID = 9999999;

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( USER_ID, "username" );

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   /** Ottawa Airport */
   private static final LocationKey LOCATION_YOW = new LocationKey( 4650, 1001 );

   /** Vancouver Airport */
   private static final LocationKey LOCATION_YVR = new LocationKey( 4650, 1002 );

   /** The Aircraft's Part */
   private static final PartNoKey PART_ACFT = new PartNoKey( 4650, 2001 );

   /** The serial number of the aircraft that will be created. */
   private static final String SERIAL_NO_ACFT1 = "ACFT001";

   /** The registration codde of the aircraft that will be created. */
   private static final String AC_REG_CD_ACFT1 = "ACFT001";

   /** A forecast model. */
   private static final FcModelKey FC_MODEL = new FcModelKey( 4650, 3001 );

   /** The charge to acount, must be of type ADJQTY. */
   private static final FncAccountKey CHARGE_TO_ACCT = new FncAccountKey( 4650, 4001 );

   /** The issue to acount, must be of type EXPENSE */
   private static final FncAccountKey ISSUE_TO_ACCT = new FncAccountKey( 4650, 4002 );

   /** The default owner */
   private static final OwnerKey OWNER = new OwnerKey( 4650, 5001 );

   /** The main assembly. */
   private static final AssemblyKey TESTACFT = new AssemblyKey( 4650, "TESTACFT" );

   /** The first tracked config slot, tracks HOURS */
   private static final ConfigSlotKey TRK_1 = new ConfigSlotKey( TESTACFT, 2 );

   /** The second tracked config slot, tracks CYCLES */
   private static final ConfigSlotKey TRK_2 = new ConfigSlotKey( TESTACFT, 3 );

   /** The position of the first tracked inventory. */
   private static final ConfigSlotPositionKey TRK_1_POS_1 = new ConfigSlotPositionKey( TRK_1, 1 );

   /** The position of the second tracked inventory. */
   private static final ConfigSlotPositionKey TRK_2_POS_1 = new ConfigSlotPositionKey( TRK_2, 1 );

   /** The inventory key of the aircraft once it is created. */
   private InventoryKey iAircraftKey;

   /** The manufacture date of the aircraft. */
   private Date iDateAcftManufact;

   /** The receive date of the aircraft. */
   private Date iDateAcftRcvd;

   /** The key of the first tracked inventory, once it is created. */
   private InventoryKey iTrkInv1;

   /** The key of the second tracked inventory, once it is created. */
   private InventoryKey iTrkInv2;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Tests that usage accrual via the Diagnostics Adapter with one parm when a TRK inventory has
    * one role but accrues another usage when the inventory has a different role.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testUsageAccrualWithDiagAdapter() throws Exception {

      final double lOrigAcftHours =
            new InvCurrUsage( new UsageParmKey( iAircraftKey, DataTypeKey.HOURS ) ).getTsnQt();
      final double lOrigAcftCycles =
            new InvCurrUsage( new UsageParmKey( iAircraftKey, DataTypeKey.CYCLES ) ).getTsnQt();

      final double lOrigTrk1Hours =
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.HOURS ) ).getTsnQt();
      final double lOrigTrk1Cycles =
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.CYCLES ) ).getTsnQt();

      final double lOrigTrk2Hours =
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.HOURS ) ).getTsnQt();
      final double lOrigTrk2Cycles =
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.CYCLES ) ).getTsnQt();

      final double lHoursDelta = 5.0;
      final double lCyclesDelta = 1.0;

      Usage[] lCollectedUsageParm = new Usage[] { new Usage( "HOURS", lHoursDelta, false ),
            new Usage( "CYCLES", 1.0, false ) };

      DiagnosticsAdapterService.updateUsageParameters( null, iAircraftKey, lCollectedUsageParm,
            new Date(), true, "Test Note", HR );

      assertUsageAccrualAdapter( lHoursDelta, lCyclesDelta, lOrigAcftHours, lOrigAcftCycles,
            lOrigTrk1Hours, lOrigTrk1Cycles, lOrigTrk2Hours, lOrigTrk2Cycles );
   }


   /**
    * Tests that usage accrual via a historic flight with one parm when a TRK inventory has one role
    * but accrues another usage when the inventory has a different role.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testUsageAccrualWithFlight() throws Exception {

      final UsageParmKey lAircraftHoursUsageParm =
            new UsageParmKey( iAircraftKey, DataTypeKey.HOURS );
      final UsageParmKey lAircraftCyclesUsageParm =
            new UsageParmKey( iAircraftKey, DataTypeKey.CYCLES );

      final double lOrigAcftHours = new InvCurrUsage( lAircraftHoursUsageParm ).getTsnQt();
      final double lOrigAcftCycles = new InvCurrUsage( lAircraftCyclesUsageParm ).getTsnQt();

      final double lOrigTrk1Hours =
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.HOURS ) ).getTsnQt();
      final double lOrigTrk1Cycles =
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.CYCLES ) ).getTsnQt();

      final double lOrigTrk2Hours =
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.HOURS ) ).getTsnQt();
      final double lOrigTrk2Cycles =
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.CYCLES ) ).getTsnQt();

      Calendar lCalendar = Calendar.getInstance();

      // set the date to one hour ago
      lCalendar.add( Calendar.HOUR, -1 );

      final Date lArrDate = lCalendar.getTime();

      // set the date to 6 hours ago
      lCalendar.add( Calendar.HOUR, -5 );

      final Date lDepDate = lCalendar.getTime();

      final double lHoursDelta = 5.0;
      final double lCyclesDelta = 1.0;

      CollectedUsageParm[] lCollectedUsageParm = new CollectedUsageParm[] {
            new CollectedUsageParm( lAircraftHoursUsageParm, lHoursDelta ),
            new CollectedUsageParm( lAircraftCyclesUsageParm, lCyclesDelta ) };
      Measurement[] lMeasurements = new Measurement[0];

      FlightInformationTO lFlightInfo =
            new FlightInformationTO( "TestFlight", "TestFlight Description", "TF001", "TEST",
                  "LOGREF01", "TEST", LOCATION_YVR, LOCATION_YOW, "A", "B", lDepDate, lArrDate,
                  lDepDate, lArrDate, lDepDate, lArrDate, false, true );

      FlightHistService lFlightHistService = new FlightHistService();
      FlightLegId lFlightKey = lFlightHistService.createHistFlight(
            new AircraftKey( iAircraftKey ), HR, lFlightInfo, new UsageUtils()
                  .convertToUsageDelta( lCollectedUsageParm ).toArray( new UsageDelta[0] ),
            lMeasurements );

      assertUsageAccrualWebForm( lHoursDelta, lCyclesDelta, lOrigAcftHours, lOrigAcftCycles,
            lOrigTrk1Hours, lOrigTrk1Cycles, lOrigTrk2Hours, lOrigTrk2Cycles );

      // update the flight to have different usages to test the UpdateCurrentUsagesByDeltaChanges
      // query
      final double lHoursDelta2 = 3.0;
      final double lCyclesDelta2 = 2.0;

      lCollectedUsageParm = new CollectedUsageParm[] {
            new CollectedUsageParm( lAircraftHoursUsageParm, lHoursDelta2 ),
            new CollectedUsageParm( lAircraftCyclesUsageParm, lCyclesDelta2 ) };

      lFlightHistService.editHistFlight( lFlightKey, HR, lFlightInfo,
            new UsageUtils().convertToUsageDelta( lCollectedUsageParm ), lMeasurements );

      assertUsageAccrualWebForm( lHoursDelta2, lCyclesDelta2, lOrigAcftHours, lOrigAcftCycles,
            lOrigTrk1Hours, lOrigTrk1Cycles, lOrigTrk2Hours, lOrigTrk2Cycles );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      Calendar lCalendar = Calendar.getInstance();

      // set the time to 10:00:00
      lCalendar.set( Calendar.HOUR, 10 );
      lCalendar.set( Calendar.MINUTE, 0 );
      lCalendar.set( Calendar.SECOND, 0 );

      // set the date to yesterday
      lCalendar.add( Calendar.DATE, -1 );
      iDateAcftRcvd = lCalendar.getTime();

      // set the date to two days ago
      lCalendar.add( Calendar.DATE, -1 );
      iDateAcftManufact = lCalendar.getTime();

      // initialize the aircraft to be used by the test cases
      initInventory();
   }


   /**
    * {@inheritDoc}
    */
   @After
   public void tearDown() throws Exception {
      UserParameters.setInstance( USER_ID, ParmTypeEnum.LOGIC.name(), null );
   }


   /**
    * Asserts that the usage of the aircraft and two tracked components have accrued usage as
    * expected. The aircraft should accrue both HOURS and CYCLES. The tracked components should also
    * accrue both HOURS and CYCLES
    *
    * @param aHoursDelta
    *           The change in the HOURS usage.
    * @param aCyclesDelta
    *           The change in the CYCLES usage.
    * @param aOrigAcftHours
    *           The original HOURS usage of the aircraft.
    * @param aOrigAcftCycles
    *           The original CYCLES usage of the aircraft.
    * @param aOrigTrk1Hours
    *           The original HOURS usage of the first tracked component.
    * @param aOrigTrk1Cycles
    *           The original CYCLES usage of the first tracked component
    * @param aOrigTrk2Hours
    *           The original HOURS usage of the second tracked component
    * @param aOrigTrk2Cycles
    *           The original CYCLES usage of the second tracked component
    */
   private void assertUsageAccrualWebForm( double aHoursDelta, double aCyclesDelta,
         double aOrigAcftHours, double aOrigAcftCycles, double aOrigTrk1Hours,
         double aOrigTrk1Cycles, double aOrigTrk2Hours, double aOrigTrk2Cycles ) {

      // ensure the aircraft accrued the given usage (lHoursDelta HOURS and lCyclesDelta CYCLES)
      assertEquals( Double.valueOf( aOrigAcftHours + aHoursDelta ),
            new InvCurrUsage( new UsageParmKey( iAircraftKey, DataTypeKey.HOURS ) ).getTsnQt() );
      assertEquals( Double.valueOf( aOrigAcftCycles + aCyclesDelta ),
            new InvCurrUsage( new UsageParmKey( iAircraftKey, DataTypeKey.CYCLES ) ).getTsnQt() );

      // ensure the tracked part in config slot 1 accrued HOURS and CYCLES
      assertEquals( Double.valueOf( aOrigTrk1Hours + aHoursDelta ),
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.HOURS ) ).getTsnQt() );
      assertEquals( Double.valueOf( aOrigTrk1Cycles + aCyclesDelta ),
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.CYCLES ) ).getTsnQt() );

      // ensure the tracked part in config slot 2 accrued HOURS and CYCLES
      assertEquals( Double.valueOf( aOrigTrk2Hours + aHoursDelta ),
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.HOURS ) ).getTsnQt() );
      assertEquals( Double.valueOf( aOrigTrk2Cycles + aCyclesDelta ),
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.CYCLES ) ).getTsnQt() );
   }


   /**
    * Asserts that the usage of the aircraft and two tracked components have accrued usage as
    * expected. The aircraft should accrue both HOURS and CYCLES. The first tracked component should
    * accrue only HOURS and the second component should accrue only CYCLES.
    *
    * @param aHoursDelta
    *           The change in the HOURS usage.
    * @param aCyclesDelta
    *           The change in the CYCLES usage.
    * @param aOrigAcftHours
    *           The original HOURS usage of the aircraft.
    * @param aOrigAcftCycles
    *           The original CYCLES usage of the aircraft.
    * @param aOrigTrk1Hours
    *           The original HOURS usage of the first tracked component.
    * @param aOrigTrk1Cycles
    *           The original CYCLES usage of the first tracked component
    * @param aOrigTrk2Hours
    *           The original HOURS usage of the second tracked component
    * @param aOrigTrk2Cycles
    *           The original CYCLES usage of the second tracked component
    */
   private void assertUsageAccrualAdapter( double aHoursDelta, double aCyclesDelta,
         double aOrigAcftHours, double aOrigAcftCycles, double aOrigTrk1Hours,
         double aOrigTrk1Cycles, double aOrigTrk2Hours, double aOrigTrk2Cycles ) {

      // ensure the aircraft accrued the given usage (lHoursDelta HOURS and lCyclesDelta CYCLES)
      assertEquals( Double.valueOf( aOrigAcftHours + aHoursDelta ),
            new InvCurrUsage( new UsageParmKey( iAircraftKey, DataTypeKey.HOURS ) ).getTsnQt() );
      assertEquals( Double.valueOf( aOrigAcftCycles + aCyclesDelta ),
            new InvCurrUsage( new UsageParmKey( iAircraftKey, DataTypeKey.CYCLES ) ).getTsnQt() );

      // ensure the tracked part in config slot 1 accrued HOURS
      assertEquals( Double.valueOf( aOrigTrk1Hours + aHoursDelta ),
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.HOURS ) ).getTsnQt() );
      assertEquals( Double.valueOf( aOrigTrk1Cycles ),
            new InvCurrUsage( new UsageParmKey( iTrkInv1, DataTypeKey.CYCLES ) ).getTsnQt() );

      // ensure the tracked part in config slot 2 accrued CYCLES
      assertEquals( Double.valueOf( aOrigTrk2Hours ),
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.HOURS ) ).getTsnQt() );
      assertEquals( Double.valueOf( aOrigTrk2Cycles + aCyclesDelta ),
            new InvCurrUsage( new UsageParmKey( iTrkInv2, DataTypeKey.CYCLES ) ).getTsnQt() );
   }


   /**
    * Create an aircraft. Find the two tracked components created under the aircraft. Asserts the
    * inventory exist and that they are tracking both HOURS and CYCLES.
    *
    * @throws Exception
    *            If an error occurs.
    */
   private void initInventory() throws Exception {

      // try to find the aircraft
      InventoryInfoTO lAcftInfo =
            InventoryLocatorService.findInventory( PART_ACFT, SERIAL_NO_ACFT1, null );

      // if the aircraft already exists
      if ( lAcftInfo != null ) {
         iAircraftKey = lAcftInfo.getInventoryKey();
      } else {

         // create the aircraft
         iAircraftKey = new InventoryCreationService().createAircraft( RefInvCondKey.RFI,
               LOCATION_YOW, PART_ACFT, SERIAL_NO_ACFT1, null, AC_REG_CD_ACFT1, true, null,
               FC_MODEL, CHARGE_TO_ACCT, OWNER, ISSUE_TO_ACCT, iDateAcftRcvd, iDateAcftManufact,
               true, true, null );
      }

      // test that the aircraft tracks cycles and hours
      InvCurrUsage.assertExist( iAircraftKey, DataTypeKey.HOURS );
      InvCurrUsage.assertExist( iAircraftKey, DataTypeKey.CYCLES );

      iTrkInv1 = InvUtils.getInvByConfigSlotPosition( iAircraftKey, TRK_1_POS_1 );
      iTrkInv2 = InvUtils.getInvByConfigSlotPosition( iAircraftKey, TRK_2_POS_1 );

      // ensure both tracked inventory were created
      assertNotNull( iTrkInv1 );
      assertNotNull( iTrkInv2 );

      // test that both tracked inventory track the usage for each config slot to which they are
      // applicable
      InvCurrUsage.assertExist( iTrkInv1, DataTypeKey.HOURS );
      InvCurrUsage.assertExist( iTrkInv1, DataTypeKey.CYCLES );
      InvCurrUsage.assertExist( iTrkInv2, DataTypeKey.HOURS );
      InvCurrUsage.assertExist( iTrkInv2, DataTypeKey.CYCLES );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
