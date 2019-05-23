package com.mxi.mx.core.ejb.authority;

import static com.mxi.mx.core.key.RefInvClassKey.ACFT;
import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.AircraftKey;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.production.aircraft.domain.AircraftAuthorityChangedEvent;
import com.mxi.mx.core.services.user.InvalidUsernameException;


public class AuthorityBeanTest {

   private static final String CONFIG_SLOT_ROOT_CODE = "ROOT_CODE";
   private static final String ASSEMBLY_CODE = "A320";
   private static final String CONFIG_SLOT_SYS_CODE = "SYS_CODE";
   private static final String SYSTEM_NAME = "SYSTEM_NAME";

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private SessionContextFake sessionContextFake = new SessionContextFake() {

      @Override
      public Principal getCallerPrincipal() {
         return new Principal() {

            @Override
            public String getName() {
               return "admin";
            }
         };
      }
   };

   private RecordingEventBus eventBus;

   private AuthorityBean authorityBean;


   @Test
   public void
         whenAssigningOneAircraftToAuthorityThenOneAircraftAuthorityChangedEventMustBeEmitted()
               throws MxException {
      // ARRANGE
      final CarrierKey originalCarrierKey = Domain.createOperator();
      final Date effectiveDate = new Date();
      final InventoryKey inventoryKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( originalCarrierKey );
         aAircraft.withAuthority( null );
         aAircraft.setAssembly( Domain.createAircraftAssembly() );
      } );

      final AuthorityKey authorityKey = Domain.createAuthority();
      final InventoryKey[] inventoryKeys = { inventoryKey };

      // ACT
      authorityBean.addInventory( authorityKey, inventoryKeys, effectiveDate );

      // ASSERT
      HumanResourceKey humanResourceKey = new HumanResourceKey( InvalidUsernameException
            .validateToHrKey( sessionContextFake.getCallerPrincipal().getName() ).toValueString() );
      assertEquals(
            new AircraftAuthorityChangedEvent( new AircraftKey( inventoryKey ), authorityKey,
                  effectiveDate, humanResourceKey ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void
         whenAssigningThreeAircraftsToAuthorityThenThreeAircraftAuthorityChangedEventsMustBeEmitted()
               throws MxException {

      // ARRANGE
      final HumanResourceKey humanResourceKey = new HumanResourceKey( InvalidUsernameException
            .validateToHrKey( sessionContextFake.getCallerPrincipal().getName() ).toValueString() );
      final CarrierKey originalCarrierKey = Domain.createOperator();
      final Date effectiveDate = new Date();
      final PartNoKey lAircraftPartRoot = Domain.createPart( aPart -> {
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      final AssemblyKey lAircraftAssembly = Domain.createAircraftAssembly( aAssembly -> {
         aAssembly.setCode( ASSEMBLY_CODE );
         aAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( CONFIG_SLOT_ROOT_CODE );
            rootConfigSlot.addPartGroup( rootCsPartGroup -> {
               rootCsPartGroup.setInventoryClass( ACFT );
               rootCsPartGroup.addPart( lAircraftPartRoot );
            } );
            rootConfigSlot.addConfigurationSlot( aSysConfigSlot -> {
               aSysConfigSlot.setCode( CONFIG_SLOT_SYS_CODE );
               aSysConfigSlot.setConfigurationSlotClass( RefBOMClassKey.SYS );
               aSysConfigSlot.addPartGroup( aSysCsPartGroup -> {
                  aSysCsPartGroup.setInventoryClass( RefInvClassKey.SYS );
               } );
            } );
         } );
      } );

      final InventoryKey inventoryKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( originalCarrierKey );
         aAircraft.withAuthority( null );
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      final InventoryKey inventoryKeyTwo = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( originalCarrierKey );
         aAircraft.withAuthority( null );
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      final InventoryKey inventoryKeyThree = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( originalCarrierKey );
         aAircraft.withAuthority( null );
         aAircraft.setAssembly( lAircraftAssembly );
      } );

      final AuthorityKey authorityKey = Domain.createAuthority();
      final InventoryKey[] inventoryKeys = { inventoryKey, inventoryKeyTwo, inventoryKeyThree };

      // ACT
      authorityBean.addInventory( authorityKey, inventoryKeys, effectiveDate );

      // ASSERT
      assertEquals( 3, eventBus.getEventMessages().size() );

      assertEquals(
            new AircraftAuthorityChangedEvent( new AircraftKey( inventoryKey ), authorityKey,
                  effectiveDate, humanResourceKey ),
            eventBus.getEventMessages().get( 0 ).getPayload() );

      assertEquals(
            new AircraftAuthorityChangedEvent( new AircraftKey( inventoryKeyTwo ), authorityKey,
                  effectiveDate, humanResourceKey ),
            eventBus.getEventMessages().get( 1 ).getPayload() );

      assertEquals(
            new AircraftAuthorityChangedEvent( new AircraftKey( inventoryKeyThree ), authorityKey,
                  effectiveDate, humanResourceKey ),
            eventBus.getEventMessages().get( 2 ).getPayload() );
   }


   @Test
   public void
         whenAssigningTrackedInventoryToAuthorityThenAircraftAuthorityChangedEventMustNotBeEmitted()
               throws MxException {

      // ARRANGE
      final OwnerKey owner = Domain.createOwner();
      final LocationKey location = Domain.createLocation();
      final PartNoKey partNo = Domain.createPart();

      final InventoryKey inventory = Domain.createTrackedInventory( inventoryConfiguration -> {
         inventoryConfiguration.setLocation( location );
         inventoryConfiguration.setPartNumber( partNo );
         inventoryConfiguration.setCondition( RefInvCondKey.ARCHIVE );
         inventoryConfiguration.setOwner( owner );
         inventoryConfiguration.setComplete( true );
      } );

      final AuthorityKey authorityKey = Domain.createAuthority();
      final InventoryKey[] inventoryKeys = { inventory };

      // ACT
      authorityBean.addInventory( authorityKey, inventoryKeys, new Date() );

      // ASSERT
      assertEquals( 0, eventBus.getEventMessages().size() );
   }


   @Test
   public void whenUnassigningAircraftFromAuthorityThenAircraftChangedEventMustBeEmitted()
         throws MxException {
      // ARRANGE
      final AuthorityKey authorityKey = Domain.createAuthority();
      final CarrierKey originalCarrierKey = Domain.createOperator();
      final Date effectiveDate = new Date();
      final InventoryKey inventoryKey = Domain.createAircraft( aAircraft -> {
         aAircraft.setOperator( originalCarrierKey );
         aAircraft.withAuthority( authorityKey );
         aAircraft.setAssembly( Domain.createAircraftAssembly() );
      } );
      final InventoryKey[] inventoryKeys = { inventoryKey };

      // ACT
      authorityBean.removeInventory( inventoryKeys, effectiveDate );

      // ASSERT
      HumanResourceKey humanResourceKey = new HumanResourceKey( InvalidUsernameException
            .validateToHrKey( sessionContextFake.getCallerPrincipal().getName() ).toValueString() );
      assertEquals( new AircraftAuthorityChangedEvent( new AircraftKey( inventoryKey ), null,
            effectiveDate, humanResourceKey ), eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Before
   public void setUp() {
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
      authorityBean = new AuthorityBean();
      authorityBean.setSessionContext( sessionContextFake );
   }
}
