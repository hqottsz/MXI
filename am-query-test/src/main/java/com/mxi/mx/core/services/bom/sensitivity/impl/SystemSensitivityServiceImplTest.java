package com.mxi.mx.core.services.bom.sensitivity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.inject.Injector;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.key.SystemSensitivityKey;
import com.mxi.mx.core.services.bom.sensitivity.SystemSensitivityService;
import com.mxi.mx.core.services.bom.sensitivity.model.ConfigSlotSensitivityConfigurationTO;
import com.mxi.mx.core.services.bom.sensitivity.model.ConfigSlotSensitivityDetails;
import com.mxi.mx.core.services.sensitivity.model.SensitivitySearchCriteria;
import com.mxi.mx.core.services.sensitivity.model.SensitivitySearchCriteria.GloballyActiveFilter;
import com.mxi.mx.core.table.eqp.SystemSensitivityTable;


public class SystemSensitivityServiceImplTest {

   private static final String PROPAGATION_ASSEMBLY = "PROP";
   private static final String TEST_ASSEMBLY = "TEST1";
   private static final String CAT_III = "CAT III";
   private static final String ETOPS = "ETOPS";

   private static final int NUM_ACTIVE_SENSITIVITIES = 3;
   private static final int NUM_SYSTEMS = 9;

   private static final ConfigSlotKey SYSTEM_ALL_ENABLED =
         new ConfigSlotKey( 4650, TEST_ASSEMBLY, 1 );
   private static final ConfigSlotKey SYSTEM_CAT_III_ENABLED =
         new ConfigSlotKey( 4650, TEST_ASSEMBLY, 3 );
   private static final ConfigSlotKey SYSTEM_NON_EXISTING = new ConfigSlotKey( 9999, "FAKE", 999 );

   // Propagation testing config slots
   private static final String CHAPTER_ATA_CODE = "61";
   private static final ConfigSlotKey SYSTEM_CHAPTER =
         new ConfigSlotKey( 4650, PROPAGATION_ASSEMBLY, 1 );
   private static final String SECTION_ATA_CODE = CHAPTER_ATA_CODE + "-10";
   private static final ConfigSlotKey SYSTEM_SECTION =
         new ConfigSlotKey( 4650, PROPAGATION_ASSEMBLY, 2 );
   private static final String UNIT_ATA_CODE = SECTION_ATA_CODE + "-05";
   private static final ConfigSlotKey SYSTEM_UNIT =
         new ConfigSlotKey( 4650, PROPAGATION_ASSEMBLY, 3 );
   private static final ConfigSlotKey SYSTEM_SECTION_SIBLING =
         new ConfigSlotKey( 4650, PROPAGATION_ASSEMBLY, 4 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private Injector iInjector;
   // Object under test
   private SystemSensitivityService iService;


   @Before
   public void setUp() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iService = new SystemSensitivityServiceImpl();
   }


   @Test
   public void get_activeOnly() throws Throwable {
      SensitivitySearchCriteria lActiveOnlyCriteria =
            buildSearchCriteria( GloballyActiveFilter.ACTIVE, null, null );

      List<ConfigSlotSensitivityDetails> lSensitivities = iService.get( lActiveOnlyCriteria );
      assertEquals( NUM_ACTIVE_SENSITIVITIES * NUM_SYSTEMS, lSensitivities.size() );
   }


   @Test
   public void get_activeByConfigSlot_allEnabled() throws Throwable {
      SensitivitySearchCriteria lActiveByConfigSlot =
            buildSearchCriteria( GloballyActiveFilter.ACTIVE, SYSTEM_ALL_ENABLED, null );

      List<ConfigSlotSensitivityDetails> lSensitivities = iService.get( lActiveByConfigSlot );

      assertEquals( NUM_ACTIVE_SENSITIVITIES, lSensitivities.size() );

      for ( ConfigSlotSensitivityDetails lDetails : lSensitivities ) {
         assertTrue( lDetails.isEnabled() );
      }
   }


   @Test
   public void get_activeByConfigSlot_catIIIEnabled_rvsmEnabledButInactive() throws Throwable {
      SensitivitySearchCriteria lActiveByConfigSlot =
            buildSearchCriteria( GloballyActiveFilter.ACTIVE, SYSTEM_CAT_III_ENABLED, null );

      List<ConfigSlotSensitivityDetails> lSensitivities = iService.get( lActiveByConfigSlot );

      assertEquals( NUM_ACTIVE_SENSITIVITIES, lSensitivities.size() );

      boolean lHasCatIIIEnabled = false;
      boolean lHasRvsmInList = false;

      // Ensure that CAT_III is in the list and enabled
      // Ensure that RVSM is not in the list since it is not active globally and should have been
      // filtered out
      for ( ConfigSlotSensitivityDetails lDetails : lSensitivities ) {
         if ( RefSensitivityKey.CAT_III.equals( lDetails.getSensitivity().getKey() ) ) {
            lHasCatIIIEnabled = lDetails.isEnabled();
         }
         if ( RefSensitivityKey.RVSM.equals( lDetails.getSensitivity().getKey() ) ) {
            lHasRvsmInList = true;
         }
      }

      assertTrue(
            "The configuration slot has CAT III enabled but it did not come back in the search results as such.",
            lHasCatIIIEnabled );
      assertFalse(
            "RVSM is inactive in the ref table, but came back in a search that explicitly asked for active sensitivities.",
            lHasRvsmInList );
   }


   @Test
   public void get_nonExistingConfigSlot() throws Throwable {
      SensitivitySearchCriteria lSearchCriteria =
            SensitivitySearchCriteria.builder().forConfigSlot( SYSTEM_NON_EXISTING ).build();

      List<ConfigSlotSensitivityDetails> lResults = iService.get( lSearchCriteria );

      assertTrue( lResults.isEmpty() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void configure_null() throws Throwable {
      iService.configure( null );
   }


   @Test
   public void configure_canEnableSensitivitiesThatWerePreviouslyDisabled() throws Throwable {
      assumeFalse( exists( SYSTEM_UNIT, RefSensitivityKey.CAT_III ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_UNIT )
                  .sensitivity( RefSensitivityKey.CAT_III, true )
                  .sensitivity( RefSensitivityKey.ETOPS, true ) // Reflects data set up
                  .sensitivity( RefSensitivityKey.RII, false ) // Reflects data set up
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      assertTrue( exists( SYSTEM_UNIT, RefSensitivityKey.CAT_III ) );
   }


   @Test
   public void configure_canDisableSensitivitiesThatWerePreviouslyEnabled() throws Throwable {
      assumeTrue( exists( SYSTEM_UNIT, RefSensitivityKey.ETOPS ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_UNIT )
                  .sensitivity( RefSensitivityKey.ETOPS, false )
                  .sensitivity( RefSensitivityKey.CAT_III, false )// Reflects data set up
                  .sensitivity( RefSensitivityKey.RII, false ) // Reflects data set up
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      assertFalse( exists( SYSTEM_UNIT, RefSensitivityKey.ETOPS ) );
   }


   @Test
   public void configure_createsNoteWhenSensitivityStateIsChanged() throws Throwable {
      assumeFalse( exists( SYSTEM_UNIT, RefSensitivityKey.CAT_III ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_UNIT )
                  .sensitivity( RefSensitivityKey.CAT_III, true )
                  .sensitivity( RefSensitivityKey.ETOPS, true ) // Reflects data set up
                  .sensitivity( RefSensitivityKey.RII, false ) // Reflects data set up
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      assertEquals( getExpectedConfigureHistoryNote( CAT_III, ETOPS ),
            getHistoryNote( SYSTEM_UNIT ) );
   }


   @Test
   public void configure_doesNotRecordHistoryNotesWhenStatusDoesNotChange() throws Throwable {
      assumeFalse( exists( SYSTEM_UNIT, RefSensitivityKey.CAT_III ) );
      assumeTrue( exists( SYSTEM_UNIT, RefSensitivityKey.ETOPS ) );
      assumeFalse( exists( SYSTEM_UNIT, RefSensitivityKey.RII ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_UNIT )
                  .sensitivity( RefSensitivityKey.CAT_III, false )
                  .sensitivity( RefSensitivityKey.ETOPS, true )
                  .sensitivity( RefSensitivityKey.RII, false )
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      // Verify there is no history note
      assertNull( getHistoryNote( SYSTEM_UNIT ) );
   }


   @Test
   public void configure_propagatesToSubsystems() throws Throwable {
      assumeTrue( exists( SYSTEM_SECTION, RefSensitivityKey.CAT_III ) );
      assumeFalse( exists( SYSTEM_SECTION, RefSensitivityKey.ETOPS ) );
      assumeTrue( exists( SYSTEM_UNIT, RefSensitivityKey.ETOPS ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_CHAPTER )
                  .propagate( true ).sensitivity( RefSensitivityKey.CAT_III, false )
                  .sensitivity( RefSensitivityKey.ETOPS, true )
                  .sensitivity( RefSensitivityKey.RII, false )
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      // Direct subsystem
      assertFalse( exists( SYSTEM_SECTION, RefSensitivityKey.CAT_III ) );
      assertTrue( exists( SYSTEM_SECTION, RefSensitivityKey.ETOPS ) );
      assertFalse( exists( SYSTEM_SECTION, RefSensitivityKey.RII ) );

      // subsystem's subsystem
      assertFalse( exists( SYSTEM_UNIT, RefSensitivityKey.CAT_III ) );
      assertTrue( exists( SYSTEM_UNIT, RefSensitivityKey.ETOPS ) );
      assertFalse( exists( SYSTEM_UNIT, RefSensitivityKey.RII ) );
   }


   @Test
   public void configure_subsystemPropagationCreatesPropagationNote() throws Throwable {
      assumeTrue( exists( SYSTEM_SECTION, RefSensitivityKey.CAT_III ) );
      assumeFalse( exists( SYSTEM_SECTION, RefSensitivityKey.ETOPS ) );
      assumeTrue( exists( SYSTEM_UNIT, RefSensitivityKey.ETOPS ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_CHAPTER )
                  .propagate( true ).sensitivity( RefSensitivityKey.CAT_III, false )
                  .sensitivity( RefSensitivityKey.ETOPS, true )
                  .sensitivity( RefSensitivityKey.RII, false )
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      // Direct subsystem
      getExpectedPropagationHistoryNote( SECTION_ATA_CODE, ETOPS );
      // subsystem's subsystem
      getExpectedPropagationHistoryNote( UNIT_ATA_CODE, ETOPS );
   }


   @Test
   public void configure_doesNotPropagateToParentSystems() throws Throwable {
      assumeFalse( exists( SYSTEM_CHAPTER, RefSensitivityKey.CAT_III ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_SECTION )
                  .propagate( true ).sensitivity( RefSensitivityKey.CAT_III, true )
                  .sensitivity( RefSensitivityKey.ETOPS, false )
                  .sensitivity( RefSensitivityKey.RII, false )
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      // Parent system should not have changed
      assertFalse( exists( SYSTEM_CHAPTER, RefSensitivityKey.CAT_III ) );
   }


   @Test
   public void configure_doesNotPropagateToSiblingSystems() throws Throwable {
      assumeFalse( exists( SYSTEM_SECTION_SIBLING, RefSensitivityKey.CAT_III ) );

      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( SYSTEM_SECTION )
                  .propagate( true ).sensitivity( RefSensitivityKey.CAT_III, true )
                  .sensitivity( RefSensitivityKey.ETOPS, false )
                  .sensitivity( RefSensitivityKey.RII, false )
                  .humanResourceKey( HumanResourceKey.ADMIN ).build();

      iService.configure( lConfigTO );

      // Parent system should not have changed
      assertFalse( exists( SYSTEM_SECTION_SIBLING, RefSensitivityKey.CAT_III ) );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void removeAll_null() throws Throwable {
      iService.removeAll( null );
   }


   @Test
   public void removeAll_valid() throws Throwable {
      // ensure sensitivities are enabled
      assertTrue( exists( SYSTEM_ALL_ENABLED, RefSensitivityKey.CAT_III ) );
      assertTrue( exists( SYSTEM_ALL_ENABLED, RefSensitivityKey.ETOPS ) );
      assertTrue( exists( SYSTEM_ALL_ENABLED, RefSensitivityKey.RII ) );

      // delete records for the given Configuration Slot
      iService.removeAll( SYSTEM_ALL_ENABLED );

      // verify sensitivities were removed
      assertFalse( exists( SYSTEM_ALL_ENABLED, RefSensitivityKey.CAT_III ) );
      assertFalse( exists( SYSTEM_ALL_ENABLED, RefSensitivityKey.ETOPS ) );
      assertFalse( exists( SYSTEM_ALL_ENABLED, RefSensitivityKey.RII ) );
   }


   /**
    * Builds a search criteria object for searching sensitivity records for configuration slots.
    *
    * @param aIsGloballyActive
    * @param aConfigSlotKey
    * @return a search criteria object
    */
   private SensitivitySearchCriteria buildSearchCriteria(
         GloballyActiveFilter aGloballyActiveFilter, ConfigSlotKey aConfigSlotKey,
         Boolean aIsAssignedToAssembly ) {
      SensitivitySearchCriteria.Builder lBuilder =
            SensitivitySearchCriteria.builder().globallyActive( aGloballyActiveFilter )
                  .forConfigSlot( aConfigSlotKey ).assignedToAssembly( aIsAssignedToAssembly );

      return lBuilder.build();
   }


   /**
    * Determines whether or not a config slot has the specified sensitivity enabled.
    *
    * @param aBomItem
    * @param aSensitivity
    * @return true if the record exists, false otherwise
    */
   private boolean exists( ConfigSlotKey aBomItem, RefSensitivityKey aSensitivity ) {
      SystemSensitivityTable lConfiguration =
            SystemSensitivityTable.findByPrimaryKey( new SystemSensitivityKey(
                  String.format( "%s:%s", aBomItem.toString(), aSensitivity.toString() ) ) );
      return lConfiguration.exists();
   }


   /**
    * Retrieves the history note for the specified config slot.
    *
    * @param aConfigSlotKey
    * @return a string representing the history note message.
    * @throws Exception
    */
   private String getHistoryNote( ConfigSlotKey aConfigSlotKey ) throws Exception {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigSlotKey, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );

      QuerySet lHistoryNotes =
            QuerySetFactory.getInstance().executeQueryTable( "eqp_assmbl_bom_log", lArgs );

      if ( lHistoryNotes.hasNext() ) {
         lHistoryNotes.next();
         return lHistoryNotes.getString( "system_note" );
      }

      return null;
   }


   private String getExpectedConfigureHistoryNote( String... aEnabledSystems ) {
      if ( aEnabledSystems.length > 0 ) {
         return i18n.get( SystemSensitivityLogServiceImpl.CONFIGURE_NOTE_ENABLED,
               Arrays.toString( aEnabledSystems ) );
      }
      return i18n.get( SystemSensitivityLogServiceImpl.CONFIGURE_NOTE_ALL_DISABLED );
   }


   private String getExpectedPropagationHistoryNote( String aParentConfigSlotName,
         String... aEnabledSystems ) {
      if ( aEnabledSystems.length > 0 ) {
         return i18n.get( SystemSensitivityLogServiceImpl.PROPAGATE_NOTE_ENABLED,
               Arrays.toString( aEnabledSystems ), aParentConfigSlotName );
      }
      return i18n.get( SystemSensitivityLogServiceImpl.PROPAGATE_NOTE_ALL_DISABLED,
            aParentConfigSlotName );
   }
}
