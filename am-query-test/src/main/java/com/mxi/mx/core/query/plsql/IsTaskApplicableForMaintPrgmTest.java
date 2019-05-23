package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.CarrierBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.maintprgm.MaintPrgmService;
import com.mxi.mx.core.services.maintprgm.MaintProgramTO;


/**
 * Tests the plsql function isTaskApplicableForMaintPrgm().
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsTaskApplicableForMaintPrgmTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String MAINTENANCE_PROGRAM_CODE = "MP_CODE";
   private static final String MAINTENANCE_PROGRAM_NAME = "MP_NAME";
   private static final String ACFT_ASSEMBLY_CODE = "ACFT_ASY";
   private static final String ACFT_CONFIG_SLOT_CODE = "ACFT_CS";
   private static final String ACFT_PART_GROUP_CODE = "ACFT_PG";
   private static final String TRK_CONFIG_SLOT_CODE = "TRK_CS";
   private static final String TRK_PART_GROUP_CODE = "TRK_PG";
   private static final String TRK_ASSEMBLY_CODE = "TRK_AC";

   private static final String PART_GROUP_APPL_RANGE = "100-200";
   private static final String NO_PART_GROUP_APPL_RANGE = null;

   private static final String TASK_APPL_RANGE = "100-200";
   private static final String NO_TASK_APPL_RANGE = null;

   private static final String APPL_CODE_WITHIN_RANGE = "150";
   private static final String APPL_CODE_OUTSIDE_RANGE = "300";
   private static final String NO_APPL_CODE = null;

   private static final String TASK_APPL_SERIAL_NUM_RULE = "ac_inv.serial_no_oem = '1111'";
   private static final String NO_TASK_APPL_RULE = null;

   private static final String APPL_SERIAL_NO = "1111";
   private static final String NON_APPL_SERIAL_NO = "0000";
   private static final String NO_APPL_SERIAL_NO = null;

   private AssemblyKey iAcftAssembly;
   private ConfigSlotKey iAcftConfigSlot;
   private ConfigSlotPositionKey iAcftConfigSlotPos;
   private InventoryKey iAircraft;
   private MaintPrgmKey iMaintPrgm;
   private CarrierKey iOperator;
   private TaskTaskKey iTaskDefn;
   private ConfigSlotKey iTrkConfigSlot;


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task HAS an applicability range</li>
    * <li>- Task HAS an applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group HAS an applicability rule</li>
    * <li>Aircraft inventory belonging to the Part Group but with no applicability code and with a
    * no serial number</li>
    * </ol>
    *
    * Verify that because there are applicability rules and ranges but the aircraft has no
    * applicability code nor serial number, that the function returns false.
    *
    * @throws Exception
    */
   @Test
   public void testWithNoApplicabilityCodeNorSerialNumber() throws Exception {
      withAircraft( PART_GROUP_APPL_RANGE, NO_APPL_CODE, null );

      withTask( iAcftConfigSlot, TASK_APPL_RANGE, TASK_APPL_SERIAL_NUM_RULE );

      assertEquals( 0, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task has no applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>Aircraft inventory belonging to the Part Group</li>
    * </ol>
    *
    * Verify that because there are no applicability rules or ranges, that the function returns true
    * (since there are no applicability restrictions, then the task is applicable).
    *
    * @throws Exception
    */
   @Test
   public void testWithNoApplicabilityRulesOrRanges() throws Exception {
      withAircraft( NO_PART_GROUP_APPL_RANGE, APPL_CODE_WITHIN_RANGE, APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, NO_TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 1, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task has no applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group HAS an applicability rule</li>
    * <li>Aircraft inventory belonging to the Part Group WITH an applicable code</li>
    * </ol>
    *
    * Verify that because the aircraft's applicability code is applicable to its part group
    * applicability rule, that the function returns true.
    *
    * @throws Exception
    */
   @Test
   public void testWithPartGroupApplRangeAndApplicableAircraft() throws Exception {
      withAircraft( PART_GROUP_APPL_RANGE, APPL_CODE_WITHIN_RANGE, NO_APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, NO_TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 1, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task has no applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group HAS an applicability rule</li>
    * <li>Aircraft inventory belonging to the Part Group but with a non-applicable applicabitiy code
    * </li>
    * </ol>
    *
    * Verify that because the aircraft's applicability code is NOT applicable to its part group
    * applicability rule, that the function returns false.
    *
    * @throws Exception
    */
   @Test
   public void testWithPartGroupApplRangeAndNonApplicableAircraft() throws Exception {

      withAircraft( PART_GROUP_APPL_RANGE, APPL_CODE_OUTSIDE_RANGE, NO_APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, NO_TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 0, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task HAS an applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>Aircraft inventory with an applicable code that is within the task's applicability
    * range</li>
    * </ol>
    *
    * Verify that because the aircraft's applicability code within the task's applicability range,
    * that the function returns true.
    *
    * @throws Exception
    */
   @Test
   public void testWithTaskApplRangeAndApplicableAircraft() throws Exception {
      withAircraft( NO_PART_GROUP_APPL_RANGE, APPL_CODE_WITHIN_RANGE, NO_APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 1, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task HAS an applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>Aircraft inventory belonging to the Part Group and with an applicable code that is within
    * the task's applicability range</li>
    * </ol>
    *
    * Verify that because the aircraft's applicability code is outside the task's applicability
    * range, that the function returns false.
    *
    * @throws Exception
    */
   @Test
   public void testWithTaskApplRangeAndNonApplicableAircraft() throws Exception {

      withAircraft( NO_PART_GROUP_APPL_RANGE, APPL_CODE_OUTSIDE_RANGE, NO_APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 0, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against a TRK component's root config slot</li>
    * <li>- Task HAS an applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the TRK component's root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>TRK component belonging to the Part Group and installed on the aircraft (NOTE: the TRK
    * component's applicability code is outside the task's applicability range)</li>
    * <li>Aircraft has an applicability code that is within the task's applicability range</li>
    * </ol>
    *
    * Verify that because the TRK component is installed on an aircraft and that aircraft has an
    * applicability code that is within the task's applicability range, that the function returns
    * true.<br>
    * (even though the task is against the TRK component, because it is installed on an aircraft the
    * aircraft's applicability code is used).
    *
    * @throws Exception
    */
   @Test
   public void testWithTaskApplRangeAndTrkInstalledOnApplicableAircraft() throws Exception {

      // Aircraft has an applicable code (within task range).
      withAircraft( NO_PART_GROUP_APPL_RANGE, APPL_CODE_WITHIN_RANGE, NO_APPL_SERIAL_NO );

      // Add a TRK component installed on the aircraft (but with an appl code outside the range).
      withInstalledTrkComponent( APPL_CODE_OUTSIDE_RANGE );

      // Task with range.
      withTask( iTrkConfigSlot, TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 1, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against a TRK component's root config slot</li>
    * <li>- Task HAS an applicability range</li>
    * <li>- Task has no applicability rule</li>
    * <li>Part Group for the TRK component's root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>TRK component belonging to the Part Group and installed on the aircraft (NOTE: the TRK
    * component's applicability code is inside the task's applicability range)</li>
    * <li>Aircraft has an applicability code that is outside the task's applicability range</li>
    * </ol>
    *
    * Verify that because the TRK component is installed on an aircraft and that aircraft has an
    * applicability code that is outside the task's applicability range, that the function returns
    * false.<br>
    * (even though the task is against the TRK component, because it is installed on an aircraft the
    * aircraft's applicability code is used - not the TRK applicability code).
    *
    * @throws Exception
    */
   @Test
   public void testWithTaskApplRangeAndTrkInstalledOnNonApplicableAircraft() throws Exception {

      // Aircraft has an applicable code (within task range).
      withAircraft( NO_PART_GROUP_APPL_RANGE, APPL_CODE_OUTSIDE_RANGE, NO_APPL_SERIAL_NO );

      // Add a TRK component installed on the aircraft (but with an appl code outside the range).
      withInstalledTrkComponent( APPL_CODE_WITHIN_RANGE );

      // Task with range.
      withTask( iTrkConfigSlot, TASK_APPL_RANGE, NO_TASK_APPL_RULE );

      assertEquals( 0, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task has no applicability range</li>
    * <li>- Task HAS an applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>Aircraft inventory with an applicable applicability code</li>
    * </ol>
    *
    * Verify that because the aircraft's applicability code is applicable to the task's
    * applicability rule, that the function returns true.
    *
    * @throws Exception
    */
   @Test
   public void testWithTaskApplRuleAndApplicableAircraft() throws Exception {
      withAircraft( NO_PART_GROUP_APPL_RANGE, NO_APPL_CODE, APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, NO_TASK_APPL_RANGE, TASK_APPL_SERIAL_NUM_RULE );

      assertEquals( 1, execute() );
   }


   /**
    * Given the following:<br>
    *
    * <ol>
    * <li>Maintenance Program against an aircraft assembly</li>
    * <li>Task against an aircraft root config slot</li>
    * <li>- Task has no applicability range</li>
    * <li>- Task HAS an applicability rule</li>
    * <li>Part Group for the aircraft root config slot</li>
    * <li>- Part Group has no applicability rule</li>
    * <li>Aircraft inventory with a NON-applicable applicability code</li>
    * </ol>
    *
    * Verify that because the aircraft's applicability code is not applicable to the task's
    * applicability rule, that the function returns false.
    *
    * @throws Exception
    */
   @Test
   public void testWithTaskApplRuleAndNonApplicableAircraft() throws Exception {

      withAircraft( NO_PART_GROUP_APPL_RANGE, NO_APPL_CODE, NON_APPL_SERIAL_NO );

      withTask( iAcftConfigSlot, NO_TASK_APPL_RANGE, TASK_APPL_SERIAL_NUM_RULE );

      assertEquals( 0, execute() );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iOperator = new CarrierBuilder().build();

      iAcftAssembly = new AssemblyBuilder( ACFT_ASSEMBLY_CODE ).build();

      // Create a maintenance program for the aircraft assembly and operator.
      MaintProgramTO lMpTo = new MaintProgramTO();
      lMpTo.setCode( MAINTENANCE_PROGRAM_CODE, "" );
      lMpTo.setName( MAINTENANCE_PROGRAM_NAME, "" );
      lMpTo.setAssembly( iAcftAssembly, "" );
      lMpTo.setOperator( iOperator, "" );
      iMaintPrgm = MaintPrgmService.create( lMpTo );

      iAcftConfigSlot = new ConfigSlotBuilder( ACFT_CONFIG_SLOT_CODE )
            .withClass( RefBOMClassKey.ROOT ).withRootAssembly( iAcftAssembly ).build();

      iAcftConfigSlotPos = new ConfigSlotPositionKey( iAcftConfigSlot, 1 );
   }


   /**
    * Execute the plsql function isTaskApplicableForMaintPrgm()
    *
    * @return return value of the funtion (1 if task is applicable, otherwise 0)
    *
    * @throws Exception
    */
   private int execute() throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTaskDefn, "aTaskDbId", "aTaskId" );
      lArgs.add( iMaintPrgm, "aMaintPrgmDbId", "aMaintPrgmId" );

      String[] lParmOrder = { "aTaskDbId", "aTaskId", "aMaintPrgmDbId", "aMaintPrgmId" };

      // Execute the query
      return Integer
            .parseInt( QueryExecutor.executeFunction( iDatabaseConnectionRule.getConnection(),
                  QueryExecutor.getFunctionName( getClass() ), lParmOrder, lArgs ) );
   }


   /**
    * Set up an aircraft for testing.
    *
    * @param aPartGroupApplRange
    *           applicability range for the aircraft's root part group
    * @param aApplCode
    *           applicablity code of the aircraft
    * @param aSerialNumber
    *           serial number of the aircraft
    */
   private void withAircraft( String aPartGroupApplRange, String aApplCode, String aSerialNumber ) {

      PartGroupKey lAcftPartGroup =
            new PartGroupDomainBuilder( ACFT_PART_GROUP_CODE ).withConfigSlot( iAcftConfigSlot )
                  .withApplicabilityRange( aPartGroupApplRange ).build();

      iAircraft = new InventoryBuilder().withClass( RefInvClassKey.ACFT ).withOperator( iOperator )
            .withConfigSlotPosition( iAcftConfigSlotPos ).withPartGroup( lAcftPartGroup )
            .withOriginalAssembly( iAcftAssembly ).withSerialNo( aSerialNumber )
            .withApplicabilityCode( aApplCode ).build();
   }


   /**
    * Set up a TRK component that is installed on the aircraft.
    *
    * @param aApplCode
    *           DOCUMENT_ME
    */
   private void withInstalledTrkComponent( String aApplCode ) {

      AssemblyKey lTrkAssembly = new AssemblyBuilder( TRK_ASSEMBLY_CODE ).build();

      // Config slot under the aircraft config slot.
      iTrkConfigSlot = new ConfigSlotBuilder( TRK_CONFIG_SLOT_CODE ).withClass( RefBOMClassKey.TRK )
            .withRootAssembly( iAcftAssembly ).build();

      ConfigSlotPositionKey lTrkConfigSlotPos =
            new ConfigSlotPositionBuilder().withConfigSlot( iTrkConfigSlot ).build();

      PartNoKey lTrkPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK ).build();
      PartGroupKey lTrkPartGroup =
            new PartGroupDomainBuilder( TRK_PART_GROUP_CODE ).withConfigSlot( iTrkConfigSlot )
                  .withInventoryClass( RefInvClassKey.TRK ).withPartNo( lTrkPartNo ).build();

      // Create the TRK component under the aircraft. Also, set the inventory applicability to be
      // outside the task's applicability range to ensure it is not being considered when Mx checks
      // applicability.
      new InventoryBuilder().withClass( RefInvClassKey.TRK ).withOperator( iOperator )
            .withConfigSlotPosition( lTrkConfigSlotPos ).withPartGroup( lTrkPartGroup )
            .withPartNo( lTrkPartNo ).withOriginalAssembly( lTrkAssembly )
            .withAssemblyInventory( iAircraft ).withParentInventory( iAircraft )
            .withApplicabilityCode( aApplCode ).build();
   }


   /**
    * Set up a task for testing.
    *
    * @param aConfigSlot
    *           DOCUMENT_ME
    * @param aTaskApplRange
    *           applicability range of the task
    * @param aTaskApplRule
    *           applicability rule of the task
    */
   private void withTask( ConfigSlotKey aConfigSlot, String aTaskApplRange, String aTaskApplRule ) {
      iTaskDefn = new TaskRevisionBuilder().withConfigSlot( aConfigSlot )
            .withApplicabilityRange( aTaskApplRange ).withApplicabilityRule( aTaskApplRule )
            .build();
   }
}
