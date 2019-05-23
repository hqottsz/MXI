package com.mxi.mx.web.jsp.controller.bom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.bom.sensitivity.model.ConfigSlotSensitivityDetails;
import com.mxi.mx.web.jsp.controller.bom.service.ConfigSlotDetails;
import com.mxi.mx.web.jsp.controller.bom.service.ConfigSlotService;


/**
 * At the time this test was written, the {@link ConfigSlotInfo} object fetches config slot details
 * in its constructor. Until this behaviour is refactored into some service layer, this test will
 * ensure that the correct business logic is applied with respect to retrieval of sensitivity
 * information for the configuration slot.
 *
 * If the refactoring occurs, this test can be renamed as appropriate and should be reusable as it
 * assumes a simple contract of having a {@link ConfigSlotKey} passed in to fetch the corresponding
 * details.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class ConfigSlotService_getDetails_Test {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private static final ConfigSlotKey CONFIG_SLOT_WITH_SENSITIVITIES =
         new ConfigSlotKey( "4650:TEST:0" );
   private static final int NUM_OF_SENSITIVITIES_ACTIVE_AND_ENABLED_FOR_ASSEMBLY = 3;

   // Object under test
   private ConfigSlotService iService;


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            ConfigSlotService_getDetails_Test.class );
   }


   @Before
   public void setUp() {
      iService = new ConfigSlotService();
   }


   @Test
   public void getDetails_sensitivities() throws Throwable {

      ConfigSlotDetails lDetails = iService.getDetails( CONFIG_SLOT_WITH_SENSITIVITIES );

      List<ConfigSlotSensitivityDetails> lSensitivities = lDetails.getSensitivities();

      // Make sure we have the correct number of sensitivities for config slot based on global
      // and assembly level settings
      assertEquals( NUM_OF_SENSITIVITIES_ACTIVE_AND_ENABLED_FOR_ASSEMBLY, lSensitivities.size() );

      Boolean lIsCatIIIEnabled = null;
      Boolean lIsEtopsEnabled = null;
      Boolean lIsRvsmEnabled = null;

      for ( ConfigSlotSensitivityDetails lSensitivity : lSensitivities ) {
         if ( RefSensitivityKey.CAT_III.equals( lSensitivity.getSensitivity().getKey() ) ) {
            lIsCatIIIEnabled = lSensitivity.isEnabled();
            continue;
         }

         if ( RefSensitivityKey.ETOPS.equals( lSensitivity.getSensitivity().getKey() ) ) {
            lIsEtopsEnabled = lSensitivity.isEnabled();
            continue;
         }

         if ( RefSensitivityKey.RVSM.equals( lSensitivity.getSensitivity().getKey() ) ) {
            lIsRvsmEnabled = lSensitivity.isEnabled();
            continue;
         }
      }

      // Ensure that the correct sensitivities are enabled/disabled for the config slot
      assertNotNull(
            "Expected CAT III sensitivity to be present in sensitivities list for config slot.",
            lIsCatIIIEnabled );
      assertNotNull(
            "Expected ETOPS sensitivity to be present in sensitivities list for config slot.",
            lIsEtopsEnabled );
      assertNotNull(
            "Expected RVSM sensitivity to be present in sensitivities list for config slot.",
            lIsRvsmEnabled );

      assertTrue( "Expected CAT III sensitivity to be enabled.", lIsCatIIIEnabled.booleanValue() );
      assertTrue( "Expected ETOPS sensitivity to be enabled.", lIsEtopsEnabled.booleanValue() );
      assertFalse( "Expected RVSM sensitivity to be disabled.", lIsRvsmEnabled.booleanValue() );
   }

}
