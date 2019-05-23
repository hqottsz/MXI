
package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.ShiftDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLabourStageKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtToolTable;
import com.mxi.mx.core.table.inv.InvLocTable;
import com.mxi.mx.core.table.org.OrgHrShiftPlan;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;


/**
 * Tests the com.mxi.mx.web.query.todolist.CapacitySummary query using jUnit Theories in order to
 * exercise the combinations of various available quantities at multiple locations.
 */
@RunWith( Theories.class )
public final class CapacitySummaryTheoriesTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final int REQUIRED_QUANITY = 10;

   private static final double LABOUR_CAPACITY_WARNING_PERCENTAGE = 0.5;
   private static final int WARNING_QUANITY = 20;

   private static final int TOOLS_AVAILABLE = 1;
   private static final int TOOLS_NOT_AVAILABLE = -1;

   private static final int PARTS_AVAILABLE_REMOTELY = 2;
   private static final int PARTS_AVAILABLE_LOCALLY = 1;
   private static final int PARTS_NOT_AVAILABLE = -1;

   private static final int LABOUR_AVAILABLE = 1;
   private static final int LABOUR_AVAILABLE_BUT_ITS_TIGHT = 0;
   private static final int LABOUR_NOT_AVAILABLE = -1;

   public static final Date TEST_DATE = DateUtils.getMidnight( new Date() );

   private static final String PART_GROUP = "PART_GROUP";
   private static final String TOOL_PART_GROUP = "TOOL_PART_GROUP";

   private static final Double SHIFT_WORKING_HOURS = 1.0;
   private static final Double SHIFT_DURATION = 8.0;
   private static final Double SHIFT_START_HOUR = 8.0;

   private static final Date SHIFT_START =
         DateUtils.addHours( TEST_DATE, SHIFT_START_HOUR.intValue() );
   private static final Date SHIFT_END =
         DateUtils.addHours( SHIFT_START, SHIFT_DURATION.intValue() );

   // The check must start and end within the shift.
   private static final Date CHECK_SCHEDULED_START = DateUtils.addHours( SHIFT_START, 1 );
   private static final Date CHECK_SCHEDULED_END = DateUtils.addHours( SHIFT_END, -1 );

   private static final RefLabourSkillKey LABOUR_SKILL = RefLabourSkillKey.ENG;

   /*
    * This test used to use 0, 1, 10, 20, 30 but performance was terrible as the number of
    * permutations became too large. Now it uses the required values for checks: 0 (below required
    * quantity) 10 (required quantity) 20 (warning quantity)
    */
   @DataPoints
   public static Integer[] iQuantities = { 0, 10, 20 };

   private InventoryKey iAircraft;
   private LocationKey iAircraftCheckLocation;
   private LocationKey iAnotherUnrelatedLocation;
   private TaskKey iCheck;
   private LocationKey iHubLocation;
   private PartGroupKey iPartGroup;
   private PartNoKey iPartNo;
   private TaskKey iReqTask;
   private LocationKey iSupplyLocation;
   private TimeZoneKey iTimeZone;
   private PartGroupKey iToolPartGroup;
   private PartNoKey iToolPartNo;


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      // Retrieve the system default time zone. Needed because the query uses the same value.
      if ( iTimeZone == null ) {
         DataSetArgument lArgs = new DataSetArgument();
         lArgs.add( "default_bool", true );

         QuerySet lQuerySet =
               QuerySetFactory.getInstance().executeQueryTable( "utl_timezone", lArgs );
         lQuerySet.first();

         iTimeZone = new TimeZoneKey( lQuerySet.getString( "timezone_cd" ) );
      }

      AssemblyKey lAssemblyKey = new AssemblyBuilder( "assmbl" ).build();
      ConfigSlotKey lConfigSlotKey =
            new ConfigSlotBuilder( "" ).withRootAssembly( lAssemblyKey ).build();

      iPartNo = new PartNoBuilder().build();
      iPartGroup = new PartGroupDomainBuilder( PART_GROUP ).withPartNo( iPartNo )
            .withConfigSlot( lConfigSlotKey ).build();

      iToolPartNo = new PartNoBuilder().build();
      iToolPartGroup = new PartGroupDomainBuilder( TOOL_PART_GROUP ).withPartNo( iToolPartNo )
            .withConfigSlot( lConfigSlotKey ).build();
   }


   /**
    * Verify that when there is available labour at the check location that meets or exceeds the
    * "capacity warning percentage" that the query returns a "labour_capacity" indicating labour is
    * available.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLabourAvailable( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      assumeTrue( aLocalQuantity >= WARNING_QUANITY );

      withCheckRequiringLabour( REQUIRED_QUANITY );

      withShiftAtCheckLocation();

      withLabour( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, LABOUR_AVAILABLE, lDataSet.getInt( "labour_capacity" ) );
   }


   /**
    * Verify that when there is available labour at the check location that meets or exceeds the
    * required labour but is less than the "capacity warning percentage", that the query returns a
    * "labour_capacity" indicating labour is available but is tight.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLabourAvailableButTight( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      assumeTrue( ( aLocalQuantity >= REQUIRED_QUANITY ) && ( aLocalQuantity < WARNING_QUANITY ) );

      withCheckRequiringLabour( REQUIRED_QUANITY );

      withShiftAtCheckLocation();

      withLabour( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, LABOUR_AVAILABLE_BUT_ITS_TIGHT,
            lDataSet.getInt( "labour_capacity" ) );
   }


   /**
    * Verify that when there is either no labour available at the check location or there is labour
    * is available but is less than required, that the query returns a "labour_capacity" indicating
    * labour is not available.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLabourNotAvailable( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      assumeTrue( ( aLocalQuantity < REQUIRED_QUANITY ) );

      withCheckRequiringLabour( REQUIRED_QUANITY );

      withShiftAtCheckLocation();

      withLabour( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, LABOUR_NOT_AVAILABLE, lDataSet.getInt( "labour_capacity" ) );
   }


   /**
    * Verify that when there is no labour required, that the query always returns a
    * "labour_capacity" indicating labour is available.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testLabourNotRequired( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      // Only need to test when each of the quanities is either 0 or 1.
      assumeTrue( aLocalQuantity <= 1 );
      assumeTrue( aSupplyQuantity <= 1 );
      assumeTrue( aHubQuantity <= 1 );
      assumeTrue( aOtherQuantity <= 1 );

      withCheckRequiringLabour( 0 );

      withShiftAtCheckLocation();

      withLabour( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, LABOUR_AVAILABLE, lDataSet.getInt( "labour_capacity" ) );
   }


   /**
    * Verify that when the combined available parts at both the check location and the supply
    * location is less then the required part quantitiy AND when the available parts at the hub
    * location is also less then required, that the query returns a "part_capacity" indicating parts
    * are unavailable.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testPartsNotAvailable( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      assumeTrue( ( aLocalQuantity + aSupplyQuantity ) < REQUIRED_QUANITY );
      assumeTrue( aHubQuantity < REQUIRED_QUANITY );

      withCheckRequiringParts( REQUIRED_QUANITY );

      withShiftAtCheckLocation();

      withParts( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, PARTS_NOT_AVAILABLE, lDataSet.getInt( "part_capacity" ) );
   }


   /**
    * Verify that when there are no parts required, that the query always returns a "part_capacity"
    * indicating parts are available locally.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testPartsNotRequired( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      // Only need to test when each of the quanities is either 0 or 1.
      assumeTrue( aLocalQuantity <= 1 );
      assumeTrue( aSupplyQuantity <= 1 );
      assumeTrue( aHubQuantity <= 1 );
      assumeTrue( aOtherQuantity <= 1 );

      withCheckRequiringParts( 0 );

      withShiftAtCheckLocation();

      withParts( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, PARTS_AVAILABLE_LOCALLY, lDataSet.getInt( "part_capacity" ) );
   }


   /**
    * Verify that when there is a required tool available at the check location or supply location,
    * that the query returns a "tool_capacity" indicating the tool is available.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testToolsAvailable( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      // The tool capacity will be considered "OK" if the tool exists either at the local (check)
      // location or at another location under the supply location. There is no concept of
      // quanity for tools, they either exist or not.
      assumeTrue( ( aLocalQuantity + aSupplyQuantity ) >= 1 );

      withCheckRequiringTools( 1 );

      withShiftAtCheckLocation();

      withTools( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, TOOLS_AVAILABLE, lDataSet.getInt( "tool_capacity" ) );
   }


   /**
    * Verify that when there are no required tools available at the check location or supply
    * location, that the query returns a "tool_capacity" indicating the tool is unavailable.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testToolsNotAvailable( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      // The tool capacity will be considered "ERROR" if no tools exists either at the local (check)
      // location or at another location under the supply location. There is no concept of quanity
      // for tools, they either exist or not.
      assumeTrue( ( aLocalQuantity + aSupplyQuantity ) < 1 );

      withCheckRequiringTools( 1 );

      withShiftAtCheckLocation();

      withTools( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, TOOLS_NOT_AVAILABLE, lDataSet.getInt( "tool_capacity" ) );
   }


   /**
    * Verify that when there are no required tools, that the query always returns a "tool_capacity"
    * indicating the tool is available.
    *
    * @param aLocalQuantity
    *           labour at the check location
    * @param aSupplyQuantity
    *           labour at the supply location of the check location
    * @param aHubQuantity
    *           labour at the hub location associated to the supply location
    * @param aOtherQuantity
    *           labour at other locations not associated with the supply location
    *
    * @throws Exception
    */
   @Theory
   @Test
   public void testToolsNotRequired( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) throws Exception {

      // Only need to test when each of the quanities is either 0 or 1.
      assumeTrue( aLocalQuantity <= 1 );
      assumeTrue( aSupplyQuantity <= 1 );
      assumeTrue( aHubQuantity <= 1 );
      assumeTrue( aOtherQuantity <= 1 );

      withCheckRequiringTools( 0 );

      withShiftAtCheckLocation();

      withTools( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      String lTestDesc =
            buildTestDescription( aLocalQuantity, aSupplyQuantity, aHubQuantity, aOtherQuantity );

      // Execute the test.
      DataSet lDataSet = execute();
      lDataSet.first();

      assertFalse( lTestDesc, lDataSet.isEmpty() );
      assertEquals( lTestDesc, 1, lDataSet.getRowCount() );
      assertEquals( lTestDesc, TOOLS_AVAILABLE, lDataSet.getInt( "tool_capacity" ) );
   }


   /**
    * Helper method to build a string describing the values for the test quantities.
    *
    * @param aLocalQuantity
    * @param aSupplyQuantity
    * @param aHubQuantity
    * @param aOtherQuantity
    *
    * @return Test description string containing the test quantities.
    */
   private String buildTestDescription( Integer aLocalQuantity, Integer aSupplyQuantity,
         Integer aHubQuantity, Integer aOtherQuantity ) {
      StringBuilder lSb = new StringBuilder();

      lSb.append( "Local Quantity = " );
      lSb.append( aLocalQuantity );
      lSb.append( " , Supply Quantity = " );
      lSb.append( aSupplyQuantity );
      lSb.append( " , Hub Quantity = " );
      lSb.append( aHubQuantity );
      lSb.append( " , Other Quantity = " );
      lSb.append( aOtherQuantity );

      return lSb.toString();
   }


   /**
    * Sets up the query arguments and exectutes the query.
    *
    * @return Data set returned from the query.
    */
   private DataSet execute() {

      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( "aDate", TEST_DATE );
      lArgs.add( iAircraftCheckLocation, new String[] { "aLocDbId", "aLocId" } );
      lArgs.add( "aCapacityWarningPercent", LABOUR_CAPACITY_WARNING_PERCENTAGE );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.todolist.CapacitySummary", lArgs );
   }


   /**
    * Sets up an aircraft and a check with a REQ against that aircraft. Also set up the various
    * locations associated to the check and the various tests.
    */
   private void withCheck() {
      iHubLocation = new LocationDomainBuilder().isSupplyLocation().build();

      iSupplyLocation =
            new LocationDomainBuilder().isSupplyLocation().withHubLocation( iHubLocation ).build();

      iAircraftCheckLocation = new LocationDomainBuilder().withSupplyLocation( iSupplyLocation )
            .withTimeZone( iTimeZone ).build();

      iAnotherUnrelatedLocation = new LocationDomainBuilder()
            .withSupplyLocation( new LocationDomainBuilder().isSupplyLocation().build() )
            .withTimeZone( iTimeZone ).build();

      iAircraft = new InventoryBuilder().atLocation( iAircraftCheckLocation ).build();

      iCheck = new TaskBuilder().onInventory( iAircraft ).atLocation( iAircraftCheckLocation )
            .withTaskClass( RefTaskClassKey.CHECK ).withScheduledStart( CHECK_SCHEDULED_START )
            .withScheduledEnd( CHECK_SCHEDULED_END ).build();

      iReqTask = new TaskBuilder().onInventory( iAircraft ).withTaskClass( RefTaskClassKey.REQ )
            .withParentTask( iCheck ).build();
   }


   /**
    * Sets up a check that requires the provided scheduled hours of labour.
    *
    * @param aRequiredQuanity
    *           scheduled hours of labour required
    */
   private void withCheckRequiringLabour( int aRequiredQuanity ) {
      withCheck();

      // Add a TECH labour requirement to the REQ with the required quantity of scheduled hours.
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      lSchedLabourTable.setTask( iReqTask );
      lSchedLabourTable.setLabourSkill( LABOUR_SKILL );
      lSchedLabourTable.setLabourStage( RefLabourStageKey.ACTV );

      SchedLabourKey lSchedLabourKey = lSchedLabourTable.insert();

      SchedLabourRoleTable lSchedLabourRoleTable = SchedLabourRoleTable.create();
      lSchedLabourRoleTable.setSchedLabour( lSchedLabourKey );
      lSchedLabourRoleTable.setLabourRoleType( RefLabourRoleTypeKey.TECH );
      lSchedLabourRoleTable.setSchedHours( new Double( aRequiredQuanity ) );
      lSchedLabourRoleTable.insert();
   }


   /**
    * Sets up a check with a part requirement for the provided quantity of parts.
    *
    * @param aRequiredQuanity
    */
   private void withCheckRequiringParts( int aRequiredQuanity ) {
      withCheck();

      // Add a part requirement to the REQ.
      new PartRequirementDomainBuilder( iReqTask )
            .withInstallQuantity( new Double( aRequiredQuanity ) ).forPartGroup( iPartGroup )
            .build();
   }


   /**
    * Sets up a check with a tool requirement for the provided quantity of tool.
    *
    * @param aNumRequiredTools
    */
   private void withCheckRequiringTools( int aNumRequiredTools ) {

      withCheck();

      // Add a tool requirement to the REQ.
      for ( int lIndex = 0; lIndex < aNumRequiredTools; lIndex++ ) {
         EvtToolTable.create( iReqTask.getEventKey(), iToolPartGroup, iToolPartNo );
      }
   }


   /**
    * Sets up labour availablity at the various locations.
    *
    * @param aLocalQuantity
    *           number of available working hours at the check location
    * @param aSupplyQuantity
    *           number of available working hours at the supply location
    * @param aHubQuantity
    *           number of available working hours at the hub location
    * @param aOtherQuantity
    *           number of available working hours at another location
    */
   private void withLabour( Integer aLocalQuantity, Integer aSupplyQuantity, Integer aHubQuantity,
         Integer aOtherQuantity ) {
      withLabourAtLocation( iAircraftCheckLocation, aLocalQuantity );
      withLabourAtLocation( iSupplyLocation, aSupplyQuantity );
      withLabourAtLocation( iHubLocation, aHubQuantity );
      withLabourAtLocation( iAnotherUnrelatedLocation, aOtherQuantity );
   }


   /**
    * Sets up the provided quantity of planned shifts at the provided location. Each planned shift
    * will consist of one working hour to make calculations easier. This provides the available
    * labour at the location.
    *
    * @param aLocation
    *           shift location
    * @param aQuantity
    *           number of shifts
    */
   private void withLabourAtLocation( LocationKey aLocation, Integer aQuantity ) {

      for ( int lIndex = 0; lIndex < aQuantity; lIndex++ ) {

         OrgHrShiftPlan lOrgHrShiftPlan = OrgHrShiftPlan.create( new HumanResourceKey( 1, 1 ) );

         lOrgHrShiftPlan.setDayDt( TEST_DATE );
         lOrgHrShiftPlan.setLabourSkill( LABOUR_SKILL );
         lOrgHrShiftPlan.setLocation( aLocation );
         lOrgHrShiftPlan.setWorkHoursQt( SHIFT_WORKING_HOURS );

         lOrgHrShiftPlan.insert();
      }
   }


   /**
    * Sets up part availablity at the various locations.
    *
    * @param aLocalQuantity
    *           number of parts at the check location
    * @param aSupplyQuantity
    *           number of parts at the supply location
    * @param aHubQuantity
    *           number of parts at the hub location
    * @param aOtherQuantity
    *           number of parts at another location
    */
   private void withParts( Integer aLocalQuantity, Integer aSupplyQuantity, Integer aHubQuantity,
         Integer aOtherQuantity ) {

      withPartsAtLocation( iAircraftCheckLocation, aLocalQuantity );
      withPartsAtLocation( iSupplyLocation, aSupplyQuantity );
      withPartsAtLocation( iHubLocation, aHubQuantity );
      withPartsAtLocation( iAnotherUnrelatedLocation, aOtherQuantity );
   }


   /**
    * Sets up the provided quantity of available parts at the provided location.
    *
    * @param aLocation
    * @param aQuantity
    */
   private void withPartsAtLocation( LocationKey aLocation, int aQuantity ) {
      for ( int lIndex = 0; lIndex < aQuantity; lIndex++ ) {

         new InventoryBuilder().atLocation( aLocation ).withClass( RefInvClassKey.TRK )
               .withCondition( RefInvCondKey.RFI ).withPartNo( iPartNo ).build();
      }
   }


   /**
    * Sets up a shift at a check location. Note, this is NOT a planned shift, just a shift which a
    * planned shift is based on.
    */
   private void withShiftAtCheckLocation() {
      ShiftKey lShift = new ShiftDomainBuilder().withStartHour( SHIFT_START_HOUR )
            .withDurationInHours( SHIFT_DURATION ).withWorkHours( SHIFT_WORKING_HOURS ).build();

      InvLocTable lInvLocTable = InvLocTable.findByPrimaryKey( iAircraftCheckLocation );
      lInvLocTable.setOvernightShift( lShift );
      lInvLocTable.update();
   }


   /**
    * Sets up tool availablity at the various locations.
    *
    * @param aLocalQuantity
    *           number of tools at the check location
    * @param aSupplyQuantity
    *           number of tools at the supply location
    * @param aHubQuantity
    *           number of tools at the hub location
    * @param aOtherQuantity
    *           number of tools at another location
    */
   private void withTools( Integer aLocalQuantity, Integer aSupplyQuantity, Integer aHubQuantity,
         Integer aOtherQuantity ) {

      withToolsAtLocation( iAircraftCheckLocation, aLocalQuantity );
      withToolsAtLocation( iSupplyLocation, aSupplyQuantity );
      withToolsAtLocation( iHubLocation, aHubQuantity );
      withToolsAtLocation( iAnotherUnrelatedLocation, aOtherQuantity );
   }


   /**
    * Sets up the provided quantity of available tools at the provided location.
    *
    * @param aLocation
    * @param aQuantity
    */
   private void withToolsAtLocation( LocationKey aLocation, int aQuantity ) {
      for ( int lIndex = 0; lIndex < aQuantity; lIndex++ ) {

         new InventoryBuilder().atLocation( aLocation ).withClass( RefInvClassKey.BATCH )
               .withCondition( RefInvCondKey.RFI ).withPartNo( iToolPartNo ).build();
      }
   }
}
