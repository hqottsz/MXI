
package com.mxi.mx.core.services.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;


/**
 * This class tests the InvUtils class.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InvUtilsTest {

   /** Aircraft assembly */
   private static final AssemblyKey ACFT_ASSMBL = new AssemblyKey( 4650, "ACFT" );

   /** Aircraft root config slot */
   private static final ConfigSlotKey ACFT_ROOT_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSMBL, 0 );

   /** Aircraft root config position. */
   private static final ConfigSlotPositionKey ACFT_ROOT_CONFIG_POSITION =
         new ConfigSlotPositionKey( ACFT_ROOT_CONFIG_SLOT, 1 );

   /** A TRK config slot directly under the ACFT config slot. */
   private static final ConfigSlotKey TRK_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSMBL, 1 );

   /** TRK config slot code */
   private static final String TRK_CONFIG_SLOT_CODE = "TRK";

   /** TRK config position */
   private static final ConfigSlotPositionKey TRK_CONFIG_POSITION =
         new ConfigSlotPositionKey( TRK_CONFIG_SLOT, 1 );

   /** TRK under TRK config slot */
   private static final ConfigSlotKey TRKSUB_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSMBL, 2 );

   /** TRK under TRK config position */
   private static final ConfigSlotPositionKey TRKSUB_CONFIG_POSITION =
         new ConfigSlotPositionKey( TRKSUB_CONFIG_SLOT, 1 );

   /** SUBASSY config slot under ACFT */
   private static final ConfigSlotKey SUBASSY_CONFIG_SLOT = new ConfigSlotKey( ACFT_ASSMBL, 3 );

   /** SUBASSY config position */
   private static final ConfigSlotPositionKey SUBASSY_CONFIG_POSITION =
         new ConfigSlotPositionKey( SUBASSY_CONFIG_SLOT, 1 );

   /** Engine assembly */
   private static final AssemblyKey ENG_ASSMBL = new AssemblyKey( 4650, "ENG" );

   /** Engine root config slot */
   private static final ConfigSlotKey ENG_ROOT_CONFIG_SLOT = new ConfigSlotKey( ENG_ASSMBL, 0 );

   /** Engine root config slot code. */
   private static final String ENG_ROOT_CONFIG_SLOT_CODE = "ENG_ROOT";

   /** Engine root config slot position */
   private static final ConfigSlotPositionKey ENG_ROOT_CONFIG_POSITION =
         new ConfigSlotPositionKey( ENG_ROOT_CONFIG_SLOT, 1 );

   /** TRK under engine config slot */
   private static final ConfigSlotKey ENGTRK_CONFIG_SLOT = new ConfigSlotKey( ENG_ASSMBL, 1 );

   /** TRK under engine config slot code. */
   private static final String ENGTRK_CONFIG_SLOT_CODE = "ENG_TRK";

   /** TRK under engine config position */
   private static final ConfigSlotPositionKey ENGTRK_CONFIG_POSITION =
         new ConfigSlotPositionKey( ENGTRK_CONFIG_SLOT, 1 );

   /** TRK under TRK under engine config slot */
   private static final ConfigSlotKey ENGTRKSUB_CONFIG_SLOT = new ConfigSlotKey( ENG_ASSMBL, 2 );

   /** TRK under TRK under engine config position */
   private static final ConfigSlotPositionKey ENGTRKSUB_CONFIG_POSITION =
         new ConfigSlotPositionKey( ENGTRKSUB_CONFIG_SLOT, 1 );

   /** Aircraft inventory */
   private static final InventoryKey ACFT_INV = new InventoryKey( 4650, 1000 );

   /** TRK under TRK inventory */
   private static final InventoryKey TRKSUB_INV = new InventoryKey( 4650, 1001 );

   /** Engine inventory */
   private static final InventoryKey ENG_INV = new InventoryKey( 4650, 1002 );

   /** TRK under engine inventory */
   private static final InventoryKey ENGTRK_INV = new InventoryKey( 4650, 1003 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * {@inheritDoc}<br>
    * <br>
    * Note that if more tests are added for InvUtils class then the createConfigTree and
    * createAircraft may need to be moved into the testFindParentInventory methods if these
    * structures are not always needed.
    */
   @Before
   public void loadData() throws Exception {
      createConfigTree();
      createAircraft();
   }


   /**
    * Tests the no parent is found when there is a hole at the next highest config position.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testFindParentInventory_NoParent() throws Exception {
      InventoryKey lFoundInventory =
            InvUtils.findParentInventory( RefInvClassKey.TRK, TRKSUB_CONFIG_POSITION, ACFT_INV );

      assertNull( lFoundInventory );
   }


   /**
    * Tests the find parent find the root invnetory for serialized inventory.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testFindParentInventory_Serialized() throws Exception {
      InventoryKey lFoundInventory =
            InvUtils.findParentInventory( RefInvClassKey.SER, ACFT_ROOT_CONFIG_POSITION, ACFT_INV );

      assertEquals( ACFT_INV, lFoundInventory );
   }


   /**
    * Tests the a tracked config slot under a tracked inventory correctly find the parent tracked
    * item.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testFindParentInventory_Tracked() throws Exception {
      InventoryKey lFoundInventory =
            InvUtils.findParentInventory( RefInvClassKey.TRK, ENGTRKSUB_CONFIG_POSITION, ENG_INV );

      assertEquals( ENGTRK_INV, lFoundInventory );
   }


   /**
    * Tests that the assembly inventory is found when the position is directly under a ROOT config
    * slot.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testFindParentInventory_UnderRoot() throws Exception {
      InventoryKey lFoundInventory =
            InvUtils.findParentInventory( RefInvClassKey.TRK, ENGTRK_CONFIG_POSITION, ENG_INV );

      assertEquals( ENG_INV, lFoundInventory );
   }


   /**
    * Tests the getAllAircraft method. The result should not include deployed aircraft.
    *
    * @throws Exception
    *            If an unexpected error occurs.
    */
   @Test
   public void testGetAllAircraft() throws Exception {
      XmlLoader.load( iDatabaseConnectionRule.getConnection(), getClass(), "CreateAircraft.xml" );

      final AircraftKey lExpectedAircraftKey = new AircraftKey( 4650, 1 );

      List<AircraftKey> lAircraftList = InvUtils.getAllAircraft();

      assertEquals( "List Size", 1, lAircraftList.size() );
      assertEquals( "List Size", lExpectedAircraftKey, lAircraftList.get( 0 ) );
   }


   /**
    * Creates a minimal aircraft.
    */
   private void createAircraft() {

      // aircraft
      insertInventory( ACFT_INV, ACFT_ROOT_CONFIG_POSITION, ACFT_ASSMBL, ACFT_INV, ACFT_INV,
            RefInvClassKey.ACFT );

      // subtrk
      insertInventory( TRKSUB_INV, TRKSUB_CONFIG_POSITION, null, ACFT_INV, ACFT_INV,
            RefInvClassKey.TRK );

      // engine
      insertInventory( ENG_INV, SUBASSY_CONFIG_POSITION, ENG_ASSMBL, ACFT_INV, ACFT_INV,
            RefInvClassKey.ASSY );

      // engtrk
      insertInventory( ENGTRK_INV, ENGTRK_CONFIG_POSITION, null, ENG_INV, ACFT_INV,
            RefInvClassKey.TRK );
   }


   /**
    * Creates a minimal configuration tree.
    */
   private void createConfigTree() {
      insertConfigSlot( TRK_CONFIG_SLOT, TRK_CONFIG_SLOT_CODE, RefBOMClassKey.TRK );
      insertConfigPosition( TRK_CONFIG_POSITION, ACFT_ROOT_CONFIG_POSITION );

      insertConfigPosition( TRKSUB_CONFIG_POSITION, TRK_CONFIG_POSITION );

      insertConfigPosition( SUBASSY_CONFIG_POSITION, ACFT_ROOT_CONFIG_POSITION );

      insertConfigSlot( ENG_ROOT_CONFIG_SLOT, ENG_ROOT_CONFIG_SLOT_CODE, RefBOMClassKey.ROOT );

      insertConfigSlot( ENGTRK_CONFIG_SLOT, ENGTRK_CONFIG_SLOT_CODE, RefBOMClassKey.TRK );
      insertConfigPosition( ENGTRK_CONFIG_POSITION, ENG_ROOT_CONFIG_POSITION );

      insertConfigPosition( ENGTRKSUB_CONFIG_POSITION, ENGTRK_CONFIG_POSITION );
   }


   /**
    * Inserts a new config position.
    *
    * @param aPosition
    *           The position
    * @param aNextHighestPosition
    *           The next highest position.
    */
   private void insertConfigPosition( ConfigSlotPositionKey aPosition,
         ConfigSlotPositionKey aNextHighestPosition ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPosition, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id", "assmbl_pos_id" );
      lArgs.add( aNextHighestPosition, "nh_assmbl_db_id", "nh_assmbl_cd", "nh_assmbl_bom_id",
            "nh_assmbl_pos_id" );

      // Insert the record
      MxDataAccess.getInstance().executeInsert( "eqp_assmbl_pos", lArgs );
   }


   /**
    * Inserts a config slot.
    *
    * @param aConfigSlot
    *           The config slot.
    * @param aConfigSlotCode
    *           The config slot code
    * @param aBomClass
    *           The BOM class.
    */
   private void insertConfigSlot( ConfigSlotKey aConfigSlot, String aConfigSlotCode,
         RefBOMClassKey aBomClass ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigSlot, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );
      lArgs.add( "assmbl_bom_cd", aConfigSlotCode );
      lArgs.add( aBomClass, "bom_class_db_id", "bom_class_cd" );

      // Insert the record
      MxDataAccess.getInstance().executeInsert( "eqp_assmbl_bom", lArgs );
   }


   /**
    * Inserts inventory into inv_inv.
    *
    * @param aInventory
    *           The inventory key
    * @param aPosition
    *           The inventory position
    * @param aOrigAssembly
    *           The original assembly key
    * @param aAssmblInventory
    *           The assembly inventory key
    * @param aHighestInventory
    *           The highest inventory key
    * @param aInvClass
    *           The inventory class
    */
   private void insertInventory( InventoryKey aInventory, ConfigSlotPositionKey aPosition,
         AssemblyKey aOrigAssembly, InventoryKey aAssmblInventory, InventoryKey aHighestInventory,
         RefInvClassKey aInvClass ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventory, "inv_no_db_id", "inv_no_id" );
      lArgs.add( aPosition, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id", "assmbl_pos_id" );
      lArgs.add( aOrigAssembly, "orig_assmbl_db_id", "orig_assmbl_cd" );
      lArgs.add( aAssmblInventory, "assmbl_inv_no_db_id", "assmbl_inv_no_id" );
      lArgs.add( aHighestInventory, "h_inv_no_db_id", "h_inv_no_id" );
      lArgs.add( aInvClass, "inv_class_db_id", "inv_class_cd" );

      // Insert the record
      MxDataAccess.getInstance().executeInsert( "inv_inv", lArgs );
   }
}
