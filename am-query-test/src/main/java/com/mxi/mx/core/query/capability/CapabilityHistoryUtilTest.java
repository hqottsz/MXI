
package com.mxi.mx.core.query.capability;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.capability.CapabilityHistoryUtil;
import com.mxi.mx.core.services.capability.CapabilityHistoryUtil.CapCodeValue;
import com.mxi.mx.core.services.capability.CapabilityHistoryUtil.CapabilityChange;


/**
 * This class tests the logic of the CapabilityHistoryUtil to see if the proper information in order
 * to create history notes is retrieved when an aircraft is unlocked.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class CapabilityHistoryUtilTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), CapabilityHistoryUtilTest.class );
   }


   private CapabilityHistoryUtil iCapabilityHistoryUtil = new CapabilityHistoryUtil();


   /**
    * Tests the proper changes are recognized when a capability level is removed.
    *
    * Preconditions:
    *
    * Data is set up such that capabilities are added to the inventory
    *
    * Action:
    *
    * Get the values before the capability level is removed. Remove the capability level. Compare
    * for the differences in capabilities
    *
    * Expectation:
    *
    * For the capabilities to be changed to Unknown
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testRemovalOfCapabilityLevelWhenUnlocked() throws Exception {

      InventoryKey lInventory = new InventoryKey( 4650, 300785 );
      List<CapCodeValue> lCapCodes = iCapabilityHistoryUtil.getCapCodeValues( lInventory );
      List<String> lConfiguredCapbilities = new ArrayList<String>();

      List<String> lRows = new ArrayList<String>();

      for ( CapCodeValue lCapCodeValue : lCapCodes ) {
         lConfiguredCapbilities.add( lCapCodeValue.getConfigCode() );
         lRows.add( lCapCodeValue.getCapCode() );
      }

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lInventory, new String[] { "aAcftNoDbId", "aAcftNoId" } );
      lArgs.add( "aCapability", "ETOPS" );

      // remove the capability level from acft table but leave the capability
      MxDataAccess.getInstance()
            .executeUpdate( "com.mxi.mx.core.query.capability.DeleteCapabilityLevel", lArgs );

      List<CapabilityChange> lConfigChanges =
            iCapabilityHistoryUtil.getCapabilityLevelInventoryChangesWhenUnlocked( lInventory,
                  lConfiguredCapbilities, lRows, "config_level_cd" );

      // ASSERT
      assertEquals( "Number of retrieved config rows", 1, lConfigChanges.size() );
      assertEquals( "Unexpected New Value: " + lConfigChanges.get( 0 ).getNewValue(), "Unknown",
            lConfigChanges.get( 0 ).getNewValue() );

   }


   /**
    * Tests the proper changes are recognized when a capability level is removed.
    *
    * Preconditions:
    *
    * Data is set up such that capabilities are added to the inventory
    *
    * Action:
    *
    * Get the values before the capability level is removed. Remove the capability level. Compare
    * for the differences in capabilities
    *
    * Expectation:
    *
    * For the capabilities to be changed to Unknown
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testRemovalOfSeatCountWhenUnlocked() throws Exception {

      InventoryKey lInventory = new InventoryKey( 4650, 300785 );
      List<CapCodeValue> lCapCodes = iCapabilityHistoryUtil.getCapCodeValues( lInventory );
      List<String> lCurrentCapbilities = new ArrayList<String>();

      List<String> lRows = new ArrayList<String>();

      for ( CapCodeValue lCapCodeValue : lCapCodes ) {
         lCurrentCapbilities.add( lCapCodeValue.getCurrentCode() );
         lRows.add( lCapCodeValue.getCapCode() );
      }

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lInventory, new String[] { "aAcftNoDbId", "aAcftNoId" } );
      lArgs.add( "aCapability", "SEATNUM" );

      // remove the capability level from acft table but leave the capability
      MxDataAccess.getInstance()
            .executeUpdate( "com.mxi.mx.core.query.capability.DeleteCapabilityLevel", lArgs );

      List<CapabilityChange> lConfigChanges =
            iCapabilityHistoryUtil.getCapabilityLevelInventoryChangesWhenUnlocked( lInventory,
                  lCurrentCapbilities, lRows, "level_cd" );

      // ASSERT
      assertEquals( "Number of retrieved config rows", 1, lConfigChanges.size() );
      assertEquals( "Unexpected New Value: " + lConfigChanges.get( 0 ).getNewValue(), "Unknown",
            lConfigChanges.get( 0 ).getNewValue() );

   }


   /**
    * Tests the proper changes are recognized when a capability is removed.
    *
    * Preconditions:
    *
    * Data is set up such that capabilities are added to the inventory
    *
    * Action:
    *
    * Get the values before the capability is removed. Remove the capability. Compare for the
    * differences in capabilities
    *
    * Expectation:
    *
    * For the existing capabilities to not be affected
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testRemovalOfCapabilityWhenUnlocked() throws Exception {

      InventoryKey lInventory = new InventoryKey( 4650, 300785 );
      List<CapCodeValue> lCapCodes = iCapabilityHistoryUtil.getCapCodeValues( lInventory );
      List<String> lConfiguredCapbilities = new ArrayList<String>();
      List<String> lCurrentCapbilities = new ArrayList<String>();
      List<String> lRows = new ArrayList<String>();

      for ( CapCodeValue lCapCodeValue : lCapCodes ) {
         lConfiguredCapbilities.add( lCapCodeValue.getConfigCode() );
         lCurrentCapbilities.add( lCapCodeValue.getCurrentCode() );
         lRows.add( lCapCodeValue.getCapCode() );
      }

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lInventory, new String[] { "aAcftNoDbId", "aAcftNoId" } );
      lArgs.add( "aCapability", "ETOPS" );

      MxDataAccess.getInstance().executeUpdate( "com.mxi.mx.core.query.capability.DeleteCapability",
            lArgs );

      List<CapabilityChange> lConfigChanges =
            iCapabilityHistoryUtil.getCapabilityLevelInventoryChangesWhenUnlocked( lInventory,
                  lConfiguredCapbilities, lRows, "config_level_cd" );

      List<CapabilityChange> lCurrentChanges =
            iCapabilityHistoryUtil.getCapabilityLevelInventoryChangesWhenUnlocked( lInventory,
                  lCurrentCapbilities, lRows, "level_cd" );

      // ASSERT
      assertEquals( "Number of retrieved rows", 0, lConfigChanges.size() );
      assertEquals( "Number of retrieved rows", 0, lCurrentChanges.size() );

   }


   /**
    * Tests the proper changes are recognized when a capability is added.
    *
    * Preconditions:
    *
    * Data is set up such that capabilities are added to the inventory
    *
    * Action:
    *
    * Get the values before the capability is added. Add the capability. Compare for the differences
    * in capabilities
    *
    * Expectation:
    *
    * No change is registered or returned for a add.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testAdditionOfCapabilityWhenUnlocked() throws Exception {

      InventoryKey lInventory = new InventoryKey( 4650, 300785 );
      List<CapCodeValue> lCapCodes = iCapabilityHistoryUtil.getCapCodeValues( lInventory );
      List<String> lConfiguredCapbilities = new ArrayList<String>();
      List<String> lCurrentCapbilities = new ArrayList<String>();
      List<String> lRows = new ArrayList<String>();

      for ( CapCodeValue lCapCodeValue : lCapCodes ) {
         lConfiguredCapbilities.add( lCapCodeValue.getConfigCode() );
         lCurrentCapbilities.add( lCapCodeValue.getCurrentCode() );
         lRows.add( lCapCodeValue.getCapCode() );
      }

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lInventory, new String[] { "aAcftNoDbId", "aAcftNoId" } );
      lArgs.add( "aCapability", "ETOPS" );
      lArgs.add( "aCurrent", "ETOPS_90" );
      lArgs.add( "aConfigured", "ETOPS_90" );

      MxDataAccess.getInstance().executeUpdate( "com.mxi.mx.core.query.capability.AddCapability",
            lArgs );

      List<CapabilityChange> lConfigChanges =
            iCapabilityHistoryUtil.getCapabilityLevelInventoryChangesWhenUnlocked( lInventory,
                  lConfiguredCapbilities, lRows, "config_level_cd" );

      List<CapabilityChange> lCurrentChanges =
            iCapabilityHistoryUtil.getCapabilityLevelInventoryChangesWhenUnlocked( lInventory,
                  lCurrentCapbilities, lRows, "level_cd" );

      // ASSERT
      assertEquals( "Number of retrieved rows", 0, lConfigChanges.size() );
      assertEquals( "Number of retrieved rows", 0, lCurrentChanges.size() );

   }


   /**
    * Tests the proper baseline capabilities are added when an aircraft is created.
    *
    * Preconditions:
    *
    * Data is set up such that capabilities are added to the baseline.
    *
    * Action:
    *
    * Call the method getCapabilityChangesForNewAircraft.
    *
    * Expectation:
    *
    * Get all of capabilities changes.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetCapabilityChangesForNewAircraft() throws Exception {

      // ARRANGE
      AssemblyKey lAssemblyKey = new AssemblyKey( 5000000, "A320" );

      // ACT
      List<CapabilityChange> lCapabilityChanges = iCapabilityHistoryUtil
            .getCapabilityChangesForNewAircraft( lAssemblyKey, HumanResourceKey.ADMIN );

      // ASSERT
      assertTrue( "Number of retrieved rows", lCapabilityChanges.size() == 5 );

   }


   /**
    * Tests that the removals when an aircraft is locked are logged
    *
    * Preconditions:
    *
    * Data is set up such that capabilities are added to the baseline. With ETOPS having set levels
    * and RSVM not having set levels
    *
    * Action:
    *
    * Call the method getCapabilityRemovalsChangesWhenUnlocked.
    *
    * Expectation:
    *
    * Get only the capability removal for set capabilities.
    *
    * @throws Exception
    *            if an error occurs during retrieval.
    */
   @Test
   public void testGetCapabilityRemovalsChangesWhenUnlocked() throws Exception {

      // ARRANGE
      InventoryKey lInventory = new InventoryKey( 4650, 300785 );
      List<String> lCapbilities = new ArrayList<String>();
      List<CapCodeValue> lCapCodes = iCapabilityHistoryUtil.getCapCodeValues( lInventory );
      for ( CapCodeValue lCapCodeValue : lCapCodes ) {
         lCapbilities.add( lCapCodeValue.getCapCode() );
      }

      // ACT
      // Delete ETOPS capability which is set for the aircraft
      DataSetArgument lArgs1 = new DataSetArgument();
      lArgs1.add( "assmbl_db_id", 5000000 );
      lArgs1.add( "assmbl_cd", "A320" );
      lArgs1.add( "acft_cap_db_id", 10 );
      lArgs1.add( "acft_cap_cd", "ETOPS" );
      MxDataAccess.getInstance().executeDelete( "assmbl_cap_levels", lArgs1 );

      // Delete RSVM capability which is not set for the aircraft
      DataSetArgument lArgs2 = new DataSetArgument();
      lArgs2.add( "assmbl_db_id", 5000000 );
      lArgs2.add( "assmbl_cd", "A320" );
      lArgs2.add( "acft_cap_db_id", 10 );
      lArgs2.add( "acft_cap_cd", "RSVM" );
      MxDataAccess.getInstance().executeDelete( "assmbl_cap_levels", lArgs2 );

      // get the changes
      List<CapabilityChange> lCapabilityChanges = iCapabilityHistoryUtil
            .getCapabilityRemovalChangesWhenUnlocked( lInventory, lCapbilities );

      // ASSERT
      // Only ETOPS removal is logged and not RSVM
      assertTrue( "Number of retrieved rows", lCapabilityChanges.size() == 1 );
      assertTrue( "Number of retrieved rows",
            "Extended Operations".equals( lCapabilityChanges.get( 0 ).getDescription() ) );

   }

}
