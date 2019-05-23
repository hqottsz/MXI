package com.mxi.mx.core.unittest.inventory.config;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.services.inventory.config.AttachableInventoryService;
import com.mxi.mx.core.services.usgrec.UsageRecordService;
import com.mxi.mx.core.unittest.table.evt.EvtInv;


/**
 * This class tests AttachableInventoryService's attachTrackedInventory function. After execution of
 * the function the tasks and faults on the inventory that is attached must have their configuration
 * snapshots updated appropriately (see each test for expected behavior).
 *
 * Special note: this test is moved from testingendejb as part of the Epic IDE-159: testingendejb -
 * Test Suite Deprecation.
 *
 * @author abruce
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class INAttachInventoryRoleTest {

   private final String iUserName = "TestUser";
   private final int iUserId = 1;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule iOperateAsUserRule = new OperateAsUserRule( iUserId, iUserName );


   /**
    * Tests that an inventory's configuration snapshot is taken properly when it is attached.
    *
    * <ol>
    * <li>Assert that the database is configured properly prior to testing:</li>
    * <li style="list-style: none">
    * <ul>
    * <li>The engine's task should have a configuration snapshot specifying its parent and root
    * inventory.</li>
    * </ul>
    * </li>
    * <li>Attach the inventory to its new parent.</li>
    * <li>Assert that the database was updated properly:</li>
    * <li style="list-style: none">
    * <ul>
    * <li>The inventory's task should have had its configuration snapshot modified to reflect its
    * new parent and root.</li>
    * </ul>
    * </li>
    * </ol>
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testAttachInventoryVerifyConfigSnapshotTask() throws Exception {

      final HumanResourceKey lHumanResourceKey = setupUser();

      final LocationKey lLocation = setupLocation();

      // Baseline: aircraft part definition
      final PartNoKey lAircraftPart = Domain.createPart( aAircraftPart -> {
         aAircraftPart.setInventoryClass( RefInvClassKey.ACFT );
         aAircraftPart.setInDefaultPartGroup( true );
      } );

      // Baseline: engine part definition
      final PartNoKey lEnginePart = Domain.createPart( aEnginePart -> {
         aEnginePart.setInventoryClass( RefInvClassKey.ASSY );
         aEnginePart.setInDefaultPartGroup( true );
      } );

      final AssemblyKey lAircraftAssemblyDefnKey =
            createAircraftBaselineAssembly( lAircraftPart, lEnginePart );

      final AssemblyKey lEngineAssemblyDefnKey = createEngineBaselineAssembly( lEnginePart );

      // config slot, position and part group keys
      ConfigSlotKey lRootConfigSlotKey =
            Domain.readRootConfigurationSlot( lAircraftAssemblyDefnKey );
      ConfigSlotKey lSystemConfigSlotKey =
            Domain.readSubConfigurationSlot( lRootConfigSlotKey, "ENGSYS" );
      ConfigSlotPositionKey lSystemConfigSlotPositionKey =
            Domain.readConfigurationSlotPosition( lSystemConfigSlotKey, "1" );
      ConfigSlotKey lEngAssyConfigSlotKey =
            Domain.readSubConfigurationSlot( lSystemConfigSlotKey, "ENGASSY" );
      ConfigSlotPositionKey lEngineConfigSlotPositionKey =
            Domain.readConfigurationSlotPosition( lEngAssyConfigSlotKey, "Left" );
      PartGroupKey lAttachAsPartGroup = Domain.readPartGroup( lEngAssyConfigSlotKey, "ENGASSY" );

      final InventoryKey lEngineInventoryKey = createEngineInventory( lLocation, lEnginePart,
            lEngineAssemblyDefnKey, lEngineConfigSlotPositionKey );

      final InventoryKey lAircraftInventoryKey = createAircraftInventory( lLocation, lAircraftPart,
            lAircraftAssemblyDefnKey, lSystemConfigSlotPositionKey );

      // Given: open work package, root of engine,
      final TaskKey lTask = Domain.createComponentWorkPackage( aComponentWorkPackage -> {
         aComponentWorkPackage.setInventory( lEngineInventoryKey );
         aComponentWorkPackage.setStatus( RefEventStatusKey.ACTV );
         aComponentWorkPackage.setName( "Test WP" );
      } );

      // Assert that the database is configured properly prior to testing:
      // Ensure the inventory has a task with a known configuration snapshot
      EvtInv lEvtInv = new EvtInv( lTask.getEventKey(), lEngineInventoryKey );
      lEvtInv.assertHInventory( lEngineInventoryKey );
      lEvtInv.assertNhInventory( null );

      /* Now synchronize the usage by executing SQL Package */
      UsageRecordService.synchronizeUsage();

      // create the service class instance
      AttachableInventoryService lService =
            new InventoryServiceFactory().getAttachableInventoryService( lEngineInventoryKey );

      // attach engine inventory
      lService.attachTrackedInventory( lAircraftInventoryKey, lEngineConfigSlotPositionKey,
            lAttachAsPartGroup, null, lHumanResourceKey, false, null, null, true, false, false,
            false, false );

      // Assert that the database was updated properly:
      // Ensure the inventory's task had its configuration snapshot updated to reflect the
      // configuration change.
      lEvtInv = new EvtInv( lTask.getEventKey(), lEngineInventoryKey );
      lEvtInv.assertHInventory( lAircraftInventoryKey );
      // NH is the aircraft itself given the baseline setup above
      lEvtInv.assertNhInventory( Domain.readSystem( lAircraftInventoryKey, "Engine System" ) );
   }


   /**
    * Tests that an engine's configuration snapshot is taken properly when it is attached.
    *
    * <ol>
    * <li>Assert that the database is configured properly prior to testing:</li>
    * <li style="list-style: none">
    * <ul>
    * <li>The inventory's fault should have a configuration snapshot specifying its parent and root
    * inventory.</li>
    * </ul>
    * </li>
    * <li>Attach the inventory to its new parent.</li>
    * <li>Assert that the database was updated properly:</li>
    * <li style="list-style: none">
    * <ul>
    * <li>The inventory's fault should not have had its configuration snapshot modified. It should
    * still appear as though the inventory is loose.</li>
    * </ul>
    * </li>
    * </ol>
    *
    * @throws Exception
    *            if an unexpected error occurs.
    */
   @Test
   public void testAttachInventoryVerifyConfigSnapshotFault() throws Exception {

      final HumanResourceKey lHumanResourceKey = setupUser();

      final LocationKey lLocation = setupLocation();

      // Baseline: aircraft part definition
      final PartNoKey lAircraftPart = Domain.createPart( aAircraftPart -> {
         aAircraftPart.setInventoryClass( RefInvClassKey.ACFT );
         aAircraftPart.setInDefaultPartGroup( true );
      } );

      // Baseline: engine part definition
      final PartNoKey lEnginePart = Domain.createPart( aEnginePart -> {
         aEnginePart.setInventoryClass( RefInvClassKey.ASSY );
         aEnginePart.setInDefaultPartGroup( true );
      } );

      final AssemblyKey lAircraftAssemblyDefnKey =
            createAircraftBaselineAssembly( lAircraftPart, lEnginePart );

      final AssemblyKey lEngineAssemblyDefnKey = createEngineBaselineAssembly( lEnginePart );

      // config slot, position and part group keys
      ConfigSlotKey lRootConfigSlotKey =
            Domain.readRootConfigurationSlot( lAircraftAssemblyDefnKey );
      ConfigSlotKey lSystemConfigSlotKey =
            Domain.readSubConfigurationSlot( lRootConfigSlotKey, "ENGSYS" );
      ConfigSlotPositionKey lSystemConfigSlotPositionKey =
            Domain.readConfigurationSlotPosition( lSystemConfigSlotKey, "1" );
      ConfigSlotKey lEngAssyConfigSlotKey =
            Domain.readSubConfigurationSlot( lSystemConfigSlotKey, "ENGASSY" );
      ConfigSlotPositionKey lEngineConfigSlotPositionKey =
            Domain.readConfigurationSlotPosition( lEngAssyConfigSlotKey, "Left" );
      PartGroupKey lAttachAsPartGroup = Domain.readPartGroup( lEngAssyConfigSlotKey, "ENGASSY" );

      final InventoryKey lEngineInventoryKey = createEngineInventory( lLocation, lEnginePart,
            lEngineAssemblyDefnKey, lEngineConfigSlotPositionKey );

      final InventoryKey lAircraftInventoryKey = createAircraftInventory( lLocation, lAircraftPart,
            lAircraftAssemblyDefnKey, lSystemConfigSlotPositionKey );

      final FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setInventory( lEngineInventoryKey );
         aFault.setName( "Test Fault" );
         aFault.setStatus( RefEventStatusKey.CFACTV );
         aFault.setFoundOnDate( new Date( 101910240000L ) ); // 18-APR-2002
      } );

      // Ensure the inventory has a fault with a known configuration snapshot
      EvtInv lEvtInv = new EvtInv( lFaultKey.getEventKey(), lEngineInventoryKey );
      lEvtInv.assertHInventory( lEngineInventoryKey );
      lEvtInv.assertNhInventory( null );

      /* Now synchronize the usage by executing SQL Package */
      UsageRecordService.synchronizeUsage();

      // create the service class instance
      AttachableInventoryService lService =
            new InventoryServiceFactory().getAttachableInventoryService( lEngineInventoryKey );

      // attach engine inventory
      lService.attachTrackedInventory( lAircraftInventoryKey, lEngineConfigSlotPositionKey,
            lAttachAsPartGroup, null, lHumanResourceKey, false, null, null, true, false, false,
            false, false );

      // Assert that the database was updated properly:
      // Ensure the inventory's fault did not have its configuration snapshot updated
      lEvtInv = new EvtInv( lFaultKey.getEventKey(), lEngineInventoryKey );
      lEvtInv.assertHInventory( lEngineInventoryKey );
      lEvtInv.assertNhInventory( null );
   }


   /**
    * Sets up test user and associated user parms
    *
    * @return HumanResourceKey lHumanResourceKey
    */
   private HumanResourceKey setupUser() {
      final HumanResourceKey lHumanResourceKey = Domain.createHumanResource( aHumanResource -> {
         aHumanResource.setUser( Domain.createUser( aUser -> {
            aUser.setUsername( iUserName );
            aUser.setUserId( iUserId );
         } ) );
      } );

      UserParametersFake lUserParametersFake = new UserParametersFake( iUserId, "LOGIC" );
      lUserParametersFake.setBoolean( "ATTACH_INVENTORY_FROM_DIFFERENT_LOCATION", true );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParametersFake );

      return lHumanResourceKey;
   }


   /**
    * Sets up location of type LINE required for test
    *
    * @return LocationKey lLocation
    */
   private LocationKey setupLocation() {
      final LocationKey lLocation = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.LINE );
      } );
      return lLocation;
   }


   /**
    * Sets up engine baseline assembly
    *
    * @param lEnginePart
    * @return AssemblyKey lEngineAssemblyDefnKey
    */
   private AssemblyKey createEngineBaselineAssembly( final PartNoKey lEnginePart ) {
      // Given: engine assembly, with usage definition
      final AssemblyKey lEngineAssemblyDefnKey = Domain.createEngineAssembly( aEngineAssembly -> {
         aEngineAssembly.setCode( "CFM56" );
         aEngineAssembly.setRootConfigurationSlot( aEngineRootConfigSlot -> {
            aEngineRootConfigSlot.addPartGroup( aEnginePartGroup -> {
               aEnginePartGroup.setInventoryClass( RefInvClassKey.ASSY );
               aEnginePartGroup.addPart( lEnginePart );
            } );
            aEngineRootConfigSlot.addUsageParameter( DataTypeKey.HOURS );
            aEngineRootConfigSlot.addPosition( "1" );
         } );
      } );
      return lEngineAssemblyDefnKey;
   }


   /**
    * Sets up aircraft baseline assembly
    *
    * @param lAircraftPart
    * @param lEnginePart
    * @return AssemblyKey lAircraftAssemblyDefnKey
    */
   private AssemblyKey createAircraftBaselineAssembly( final PartNoKey lAircraftPart,
         final PartNoKey lEnginePart ) {
      // Baseline: aircraft assembly with engine sub-assembly config slot, hours parms and part
      // groups
      // Baseline: aircraft assembly with engine sub-assembly config slot, hours parms and part
      // groups
      final AssemblyKey lAircraftAssemblyDefnKey =
            Domain.createAircraftAssembly( aAircraftAssembly -> {
               aAircraftAssembly.setRootConfigurationSlot( aRootConfigSlot -> {
                  aRootConfigSlot.addPartGroup( aAircraftPartGroup -> {
                     aAircraftPartGroup.setInventoryClass( RefInvClassKey.ACFT );
                     aAircraftPartGroup.addPart( lAircraftPart );
                  } );
                  aRootConfigSlot.addUsageParameter( DataTypeKey.HOURS );
                  aRootConfigSlot.addConfigurationSlot( aSysConfigurationSlot -> {
                     aSysConfigurationSlot.setCode( "ENGSYS" );
                     aSysConfigurationSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
                     aSysConfigurationSlot.setName( "Engine System" );
                     aSysConfigurationSlot.addPosition( "1" );
                     aSysConfigurationSlot.addConfigurationSlot( aEngineConfigSlot -> {
                        aEngineConfigSlot.setCode( "ENGASSY" );
                        aEngineConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SUBASSY );
                        aEngineConfigSlot.addPosition( "Left" );
                        aEngineConfigSlot.addPartGroup( aEnginePartGroup -> {
                           aEnginePartGroup.setCode( "ENGASSY" );
                           aEnginePartGroup.setInventoryClass( RefInvClassKey.ASSY );
                           aEnginePartGroup.addPart( lEnginePart );
                        } );
                        aEngineConfigSlot.addUsageParameter( DataTypeKey.HOURS );
                     } );
                  } );
               } );
            } );
      return lAircraftAssemblyDefnKey;

   };


   /**
    * Sets up aircraft inventory
    *
    * @param lLocation
    * @param lAircraftPart
    * @param lAircraftAssemblyDefnKey
    * @param lSystemConfigSlotPositionKey
    * @return InventoryKey lAircraftInventoryKey
    */
   private InventoryKey createAircraftInventory( final LocationKey lLocation,
         final PartNoKey lAircraftPart, final AssemblyKey lAircraftAssemblyDefnKey,
         ConfigSlotPositionKey lSystemConfigSlotPositionKey ) {
      final InventoryKey lAircraftInventoryKey = Domain.createAircraft( aAircraftInventory -> {
         aAircraftInventory.setAssembly( lAircraftAssemblyDefnKey );
         aAircraftInventory.setPart( lAircraftPart );
         aAircraftInventory.setAllowSynchronization( true );
         aAircraftInventory.setCondition( RefInvCondKey.INSRV );
         aAircraftInventory.setLocation( lLocation );
         aAircraftInventory.addUsage( DataTypeKey.HOURS, new BigDecimal( 100 ) );
         aAircraftInventory.addSystem( aSystem -> {
            aSystem.setName( "Engine System" );
            aSystem.setPosition( lSystemConfigSlotPositionKey );
            aSystem.setLocation( lLocation );
         } );
      } );
      return lAircraftInventoryKey;
   }


   /**
    * Sets up engine inventory
    *
    * @param lLocation
    * @param lEnginePart
    * @param lEngineAssemblyDefnKey
    * @param lEngineConfigSlotPositionKey
    * @return InventoryKey lEngineInventoryKey
    */
   private InventoryKey createEngineInventory( final LocationKey lLocation,
         final PartNoKey lEnginePart, final AssemblyKey lEngineAssemblyDefnKey,
         ConfigSlotPositionKey lEngineConfigSlotPositionKey ) {
      final InventoryKey lEngineInventoryKey = Domain.createEngine( aEngineInventory -> {
         aEngineInventory.setAssembly( lEngineAssemblyDefnKey );
         aEngineInventory.setPartNumber( lEnginePart );
         aEngineInventory.setOriginalAssembly( lEngineAssemblyDefnKey );
         aEngineInventory.setCondition( RefInvCondKey.INSRV );
         // finds config slot position key from the root config slot
         aEngineInventory.setPosition( lEngineConfigSlotPositionKey );
         aEngineInventory.addUsage( DataTypeKey.HOURS, new BigDecimal( 99 ) );
         aEngineInventory.setLocation( lLocation );
      } );
      return lEngineInventoryKey;
   }

}
