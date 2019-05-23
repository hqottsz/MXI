package com.mxi.mx.repository.system;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.domain.Id;
import com.mxi.mx.domain.configslot.ConfigSlot;
import com.mxi.mx.domain.system.System;


public class JdbcSystemRepositoryTest {

   private static final RefSensitivityKey SENS_1 = new RefSensitivityKey( "SENS_1" );
   private static final RefSensitivityKey SENS_2 = new RefSensitivityKey( "SENS_2" );
   private static final RefSensitivityKey SENS_3 = new RefSensitivityKey( "SENS_3" );
   private static final RefSensitivityKey SENS_4 = new RefSensitivityKey( "SENS_4" );
   private static final RefSensitivityKey SENS_5 = new RefSensitivityKey( "SENS_5" );
   private static final RefSensitivityKey SENS_6 = new RefSensitivityKey( "SENS_6" );
   private static final RefSensitivityKey SENS_7 = new RefSensitivityKey( "SENS_7" );
   private static final RefSensitivityKey SENS_8 = new RefSensitivityKey( "SENS_8" );

   private static final String ASSEMBLY = "B-737";
   private static final ConfigSlotKey ROOT_CONFIG_SLOT_KEY = new ConfigSlotKey( 4650, ASSEMBLY, 0 );
   private static final System.Id CHAPTER_SYSTEM_ID = new System.Id( 4650, ASSEMBLY, 1 );
   private static final String CHAPTER_ATA_CODE = "61";
   private static final System CHAPTER_SYSTEM = new System.Builder().id( CHAPTER_SYSTEM_ID )
         .ataCode( CHAPTER_ATA_CODE ).sensitivity( SENS_1 ).build();
   private static final Id<ConfigSlot> CHAPTER_SYSTEM_CONFIGURATION_SLOT_ID =
         new Id<>( "00000000000000000000000000000001" );

   private static final System CHILD_SYSTEM =
         new System.Builder().id( new System.Id( 4650, ASSEMBLY, 2 ) ).ataCode( "61-10" ).build();
   private static final Id<ConfigSlot> CHILD_SYSTEM_CONFIGURATION_SLOT_ID =
         new Id<>( "00000000000000000000000000000002" );
   private static final ConfigSlotKey CHAPTER_BOM_ITEM_KEY = new ConfigSlotKey( 4650, "B-737", 1 );
   private static final System SECTION_SYSTEM =
         new System.Builder().id( new System.Id( 4650, ASSEMBLY, 2 ) ).ataCode( "61-10" ).build();
   private static final System UNIT_SYSTEM = new System.Builder()
         .id( new System.Id( 4650, ASSEMBLY, 3 ) ).ataCode( "61-10-05" ).build();
   private static final ConfigSlotKey CHAPTER_SIBLING_BOM_ITEM_KEY =
         new ConfigSlotKey( 4650, "B-737", 7 );

   private static final JdbcSystemRepository JDBC_SYSTEM_REPOSITORY = new JdbcSystemRepository();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   @Test
   public void findSystemByTrackedConfigurationSlotWithSystemParent() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.find( new Id<ConfigSlot>( "00000000000000000000000000000004" ) );

      assertTrue( lSystem.isPresent() );
      assertEquals( CHAPTER_SYSTEM, lSystem.get() );
   }


   @Test
   public void findSystemByTrackedConfigurationSlotWithRootParentReturnsEmpty() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.find( new Id<ConfigSlot>( "00000000000000000000000000000008" ) );

      assertFalse( lSystem.isPresent() );
   }


   @Test
   public void findSystemByRootConfigurationSlotReturnsEmpty() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.find( new Id<ConfigSlot>( "00000000000000000000000000000000" ) );

      assertFalse( lSystem.isPresent() );
   }


   @Test
   public void findSystemBySystemConfigurationSlotWithRootParentReturnsItself() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.find( CHAPTER_SYSTEM_CONFIGURATION_SLOT_ID );

      assertTrue( lSystem.isPresent() );
      assertEquals( CHAPTER_SYSTEM, lSystem.get() );
   }


   @Test
   public void findSystemByTrackedConfigSlotWithTrackedParent() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.find( new Id<ConfigSlot>( "00000000000000000000000000000005" ) );

      assertTrue( lSystem.isPresent() );
      assertEquals( CHAPTER_SYSTEM, lSystem.get() );
   }


   @Test
   public void findSystemBySystemConfigurationSlotWithSystemParentReturnsItself() {
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.find( CHILD_SYSTEM_CONFIGURATION_SLOT_ID );

      assertTrue( lSystem.isPresent() );
      assertEquals( CHILD_SYSTEM, lSystem.get() );
   }


   @Test
   public void findByNaturalKeyReturnsSystem() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.findByNaturalKey( ASSEMBLY, CHAPTER_ATA_CODE );

      assertTrue( lSystem.isPresent() );
      assertEquals( CHAPTER_SYSTEM, lSystem.get() );
   }


   @Test
   public void findByNaturalKeyReturnsEmptyWhenSystemDoesNotExist() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.findByNaturalKey( ASSEMBLY, "DoesNotExist" );

      assertFalse( lSystem.isPresent() );
   }


   @Test
   public void findByNaturalKeyReturnsEmptyWhenConfigurationSlotIsNotASystem() {
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.findByNaturalKey(
            ROOT_CONFIG_SLOT_KEY.getAssemblyKey().getCd(), ROOT_CONFIG_SLOT_KEY.getCd() );

      assertFalse( lSystem.isPresent() );
   }


   @Test
   public void getReturnsSystem() {
      System lSystem = JDBC_SYSTEM_REPOSITORY.get( CHILD_SYSTEM_CONFIGURATION_SLOT_ID );
      assertEquals( CHILD_SYSTEM, lSystem );
   }


   @Test( expected = IllegalStateException.class )
   public void getThrowsExceptionWhenSystemDoesNotExist() {
      JDBC_SYSTEM_REPOSITORY.get( new Id<ConfigSlot>( "00000000000000000000000000000099" ) );
   }


   @Test
   public void getConfigurationSlotId() {
      assertEquals( CHAPTER_SYSTEM_CONFIGURATION_SLOT_ID,
            JDBC_SYSTEM_REPOSITORY.getConfigurationSlotId( CHAPTER_SYSTEM ) );
   }


   @Test
   public void findByBomItemKeyReturnsSystem() {
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.find( CHAPTER_SYSTEM_ID.get() );

      assertTrue( lSystem.isPresent() );
      assertEquals( CHAPTER_SYSTEM, lSystem.get() );
   }


   @Test
   public void findByBomItemKeyReturnsEmptyWhenSystemDoesNotExist() {
      Optional<System> lSystem =
            JDBC_SYSTEM_REPOSITORY.find( new ConfigSlotKey( 4650, "DOES_NOT_EXIST", 2 ) );

      assertFalse( lSystem.isPresent() );
   }


   @Test
   public void findByBomItemKeyReturnsEmptyWhenConfigurationSlotIsNotASystem() {
      EqpAssmblBom.getBomItemKey( ASSEMBLY, "Root" );
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.find( ROOT_CONFIG_SLOT_KEY );

      assertFalse( lSystem.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void getAllSubSystems_throwsExceptionOnNull() {
      JDBC_SYSTEM_REPOSITORY.getAllSubSystems( null );
   }


   /**
    * By virtue of the data set up, this asserts that neither tracked nor sub-assembly config slots
    * are inappropriately treated as systems
    */
   @Test
   public void getAllSubSystems_returnsAllSubSystems() {
      List<System> lSystems = JDBC_SYSTEM_REPOSITORY.getAllSubSystems( CHAPTER_BOM_ITEM_KEY );

      assertEquals( Arrays.asList( SECTION_SYSTEM, UNIT_SYSTEM ), lSystems );
   }


   @Test
   public void getAllSubSystems_doesNotReturnItself() {
      List<System> lSystems = JDBC_SYSTEM_REPOSITORY.getAllSubSystems( CHAPTER_BOM_ITEM_KEY );

      assertFalse( lSystems.contains( CHAPTER_SYSTEM ) );
   }


   @Test
   public void getAllSubSystems_doesNotReturnSiblingSystems() {
      List<System> lSystems = JDBC_SYSTEM_REPOSITORY.getAllSubSystems( CHAPTER_BOM_ITEM_KEY );

      assertFalse( lSystems.contains( CHAPTER_SIBLING_BOM_ITEM_KEY ) );
   }


   @Test
   public void getAllSubSystems_returnsEmptyListWhenNoSubSystemsExist() {
      List<System> lSystems = JDBC_SYSTEM_REPOSITORY.getAllSubSystems( UNIT_SYSTEM.getId().get() );

      assertTrue( lSystems.isEmpty() );
   }


   @Test
   public void hasEnabledSystemSensitivities() {
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.find( CHAPTER_BOM_ITEM_KEY );

      assertTrue( lSystem.isPresent() );
      assertThat( lSystem.get().getSensitivities(), contains( SENS_1 ) );
   }


   @Test
   public void canHaveNoEnabledSystemSensitivities() {
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.find( CHAPTER_SIBLING_BOM_ITEM_KEY );

      assertTrue( lSystem.isPresent() );
      assertThat( lSystem.get().getSensitivities(), is( empty() ) );
   }


   @Test
   public void doesNotHaveDisabledSensitivities() {
      Optional<System> lSystem = JDBC_SYSTEM_REPOSITORY.find( CHAPTER_BOM_ITEM_KEY );

      assertTrue( lSystem.isPresent() );

      List<RefSensitivityKey> lActualSensitivities = lSystem.get().getSensitivities();
      // active, applicable, not enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_2 ) ) );

      // active, not applicable, enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_3 ) ) );

      // active, not applicable, not enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_4 ) ) );

      // not active, applicable, enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_5 ) ) );

      // not active, applicable, not enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_6 ) ) );

      // not active, not applicable, enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_7 ) ) );

      // not active, not applicable, not enabled
      assertThat( lActualSensitivities, not( hasItem( SENS_8 ) ) );
   }


   @Test
   public void get_byConfigSlotKeyReturnsSystem() {
      assertThat( JDBC_SYSTEM_REPOSITORY.get( CHAPTER_SYSTEM.getId().get() ),
            is( CHAPTER_SYSTEM ) );
   }


   @Test( expected = NullPointerException.class )
   public void get_byConfigSlotKeyThrowsExceptionOnNullKey() {
      JDBC_SYSTEM_REPOSITORY.get( ( ConfigSlotKey ) null );
   }
}
