package com.mxi.mx.core.adapter.services.ema.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.AssemblyBuilder;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.ZoneKey;


/**
 * This class tests the {@link ZoneRetrievalUtility} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ZoneRetrievalUtilityTest {

   private static final String UNIQUE_ZONE_CD = "100";

   private static final String NONUNIQUE_ZONE_CD = "TESTZONE";

   private static final String PARMTYPE_LOGIC = "LOGIC";

   private InventoryKey iInventoryKey;

   private ZoneKey iZone1;

   private ZoneKey iZone2;

   /** The class under test. */
   private ZoneRetrievalUtility iZoneRetrievalUtil;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Given a non-unique zone code and an invnetory key, the zone key for the inventory's assembly
    * will be returned.
    */
   @Test
   public void testGetZoneWithNonUniqueCode() {
      assertEquals( iZone1, iZoneRetrievalUtil.getZoneKey( NONUNIQUE_ZONE_CD, iInventoryKey ) );
   }


   /**
    * Tests that the getZone method returns null when a nonunique zone code is given but no
    * inventory is given.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetZoneWithNonUniqueCodeNoInventory() throws Exception {
      assertNull( iZoneRetrievalUtil.getZoneKey( NONUNIQUE_ZONE_CD, null ) );
   }


   /**
    * Tests that the getZone method returns null when a null zone code is given.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testGetZoneWithNullZoneCode() throws Exception {
      assertNull( iZoneRetrievalUtil.getZoneKey( null, null ) );
   }


   /**
    * Given a unique zone code, but no ivnentory, the zone will be retrieved.
    */
   @Test
   public void testGetZoneWithUniqueCodeNoInventory() {
      assertEquals( iZone2, iZoneRetrievalUtil.getZoneKey( UNIQUE_ZONE_CD, null ) );
   }


   /**
    * Sets up the test case. Creatyes the class under test instance.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void loadData() throws Exception {
      // create assembly 1, with a config slot and a position
      AssemblyKey lAssembly1 = new AssemblyBuilder( "ASSY1" ).build();
      ConfigSlotKey lConfigSlot1 =
            new ConfigSlotBuilder( "SLOT1" ).withRootAssembly( lAssembly1 ).build();
      ConfigSlotPositionKey lConfigSlotPosition1 =
            new ConfigSlotPositionBuilder().withConfigSlot( lConfigSlot1 ).build();

      // create assembly two, without any other items
      AssemblyKey lAssembly2 = new AssemblyBuilder( "ASSY2" ).build();

      // zone one has assembly one
      iZone1 = Domain.createZone( zone -> {
         zone.setZoneCode( NONUNIQUE_ZONE_CD );
         zone.setParentAssembly( lAssembly1 );
      } );

      // create another zone with the same code but a different assembly
      Domain.createZone( zone -> {
         zone.setZoneCode( NONUNIQUE_ZONE_CD );
         zone.setParentAssembly( lAssembly2 );
      } );

      // create a third zone with a unique code
      iZone2 = Domain.createZone( zone -> {
         zone.setZoneCode( UNIQUE_ZONE_CD );
         zone.setParentAssembly( lAssembly1 );
      } );

      // create an inventory that has the first assembly
      iInventoryKey = new InventoryBuilder().withConfigSlotPosition( lConfigSlotPosition1 ).build();

      // create the instance to be tested
      iZoneRetrievalUtil = new ZoneRetrievalUtility();
   }
}
