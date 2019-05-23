package com.mxi.mx.core.services.bom.sensitivity.validator;

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
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.exception.DoesNotExistException;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.bom.sensitivity.exception.SensitivitiesNotInSyncException;
import com.mxi.mx.core.services.bom.sensitivity.model.ConfigSlotSensitivityConfigurationTO;


@RunWith( BlockJUnit4ClassRunner.class )
public class SensitivityValidatorTest {

   // Test Data
   private static final ConfigSlotKey CONFIG_SLOT_NON_EXISTING =
         new ConfigSlotKey( 9999, "FAKE", 999 );
   private static final ConfigSlotKey CONFIG_SLOT_TRK = new ConfigSlotKey( "4650:TEST:1" );
   private static final ConfigSlotKey CONFIG_SLOT_VALID = new ConfigSlotKey( "4650:TEST:0" );

   private static final RefSensitivityKey RVSM = RefSensitivityKey.RVSM;
   private static final RefSensitivityKey ETOPS = RefSensitivityKey.ETOPS;
   private static final RefSensitivityKey CAT_III = RefSensitivityKey.CAT_III;
   private static final RefSensitivityKey FCBS = RefSensitivityKey.FCBS;
   private static final RefSensitivityKey RII = RefSensitivityKey.RII;

   @ClassRule
   public static DatabaseConnectionRule sConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Object under test
   private SensitivityValidator iValidator;


   @Before
   public void setUp() {
      iValidator = new SensitivityValidator();
   }


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sConnectionRule.getConnection(), SensitivityValidatorTest.class );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void validate_null() throws Exception {
      iValidator.validate( null );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void validate_missingBomItemKey() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            getValidTOBuilder().configSlotKey( null ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void validate_missingHumanResourceKey() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            getValidTOBuilder().humanResourceKey( null ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = DoesNotExistException.class )
   public void configure_nonExistingConfigSlot() throws Throwable {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            getValidTOBuilder().configSlotKey( CONFIG_SLOT_NON_EXISTING ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = DoesNotExistException.class )
   public void validate_nonSystemConfigSlot_withEnabledSensitivities() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            getValidTOBuilder().configSlotKey( CONFIG_SLOT_TRK ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = DoesNotExistException.class )
   public void validate_nonSystemConfigSlot_withoutEnabledSensitivities() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( CONFIG_SLOT_TRK )
                  .humanResourceKey( HumanResourceKey.ADMIN ).sensitivity( CAT_III, false )
                  .sensitivity( ETOPS, false ).sensitivity( FCBS, false ).sensitivity( RVSM, false )
                  .sensitivity( RII, false ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void validate_configureInactive() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( CONFIG_SLOT_VALID )
                  .humanResourceKey( HumanResourceKey.ADMIN ).sensitivity( CAT_III, false )
                  .sensitivity( ETOPS, true ).sensitivity( RII, true ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void validate_configureMoreThanActive() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            getValidTOBuilder().sensitivity( FCBS, false ).build();

      iValidator.validate( lConfigTO );
   }


   @Test( expected = SensitivitiesNotInSyncException.class )
   public void validate_configureLessThanActive() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO =
            new ConfigSlotSensitivityConfigurationTO.Builder().configSlotKey( CONFIG_SLOT_VALID )
                  .humanResourceKey( HumanResourceKey.ADMIN ).sensitivity( CAT_III, true )
                  .sensitivity( ETOPS, false ).build();

      iValidator.validate( lConfigTO );
   }


   @Test
   public void validate_validScenario() throws Exception {
      ConfigSlotSensitivityConfigurationTO lConfigTO = getValidTOBuilder().build();

      iValidator.validate( lConfigTO );
   }


   /**
    * Sets up a valid configuration TO object to be built or modified to test error conditions.
    *
    * @return a builder that is preset with valid properties on the
    *         {@link ConfigSlotSensitivityConfigurationTO} that it can build.
    */
   private ConfigSlotSensitivityConfigurationTO.Builder getValidTOBuilder() {
      ConfigSlotSensitivityConfigurationTO.Builder lBuilder =
            new ConfigSlotSensitivityConfigurationTO.Builder();
      {
         lBuilder.configSlotKey( CONFIG_SLOT_VALID );
         lBuilder.humanResourceKey( HumanResourceKey.ADMIN );
         lBuilder.sensitivity( CAT_III, true );
         lBuilder.sensitivity( ETOPS, false );
         lBuilder.sensitivity( RVSM, true );
      }
      return lBuilder;
   }

}
