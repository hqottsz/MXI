package com.mxi.mx.core.services.bom;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefLogCardFormKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.SystemService;
import com.mxi.mx.core.services.ietm.IetmAssemblyMapDoesNotExistException;
import com.mxi.mx.core.services.sensitivity.model.SensitivityConfigurationTO;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.domain.system.System;


public class ConfigSlotServiceTest {

   private static final SystemService SYSTEM_SERVICE = new SystemService();
   private static final HumanResourceKey AUTHORIZING_HR = new HumanResourceKey( 4650, 123 );
   private static final ConfigSlotKey ROOT_CONFIG_SLOT = new ConfigSlotKey( 4650, "B-737", 0 );
   private static final ConfigSlotKey SYSTEM_CONFIG_SLOT = new ConfigSlotKey( 4650, "B-737", 1 );
   private static final ConfigSlotKey TRACKED_CONFIG_SLOT = new ConfigSlotKey( 4650, "B-737", 3 );
   private static final ConfigSlotKey SUB_ASSEMBLY_CONFIG_SLOT =
         new ConfigSlotKey( 4650, "B-737", 4 );
   private static final ConfigSlotKey SYSTEM_WITH_SENSITIVITIES =
         new ConfigSlotKey( 4650, "B-737", 5 );
   private static final String SYSTEM_WITH_SENSITIVITIES_ATA_CODE = "SYS-delete";

   private static final String ASSEMBLY = "B-737";
   private static final String NAME = "NAME";
   private static final boolean IS_RVSM = true;
   private static final boolean IS_ETOPS = true;
   private static final boolean IS_SOFTWARE = true;
   private static final String ASSMBL_BOM_CD = "ACFT_CD1";
   private static final String FUNCTION_CODE = "NAME";
   private static final String ZONE_CODE = "NAME";
   private static final RefLogCardFormKey LOG_CARD_FORM = new RefLogCardFormKey( 4650, "CODE" );
   private static final String IETM_TOPIC = "topic";
   private static final IetmTopicKey IETM_TOPIC_KEY = new IetmTopicKey( 4650, 1, 0 );
   private static final String IETM_TOPIC_NOT_ON_ASSEMBLY = "DNE";
   private static final RefSensitivityKey SENS_1 = new RefSensitivityKey( "SENS_1" );
   private static final RefSensitivityKey SENS_2 = new RefSensitivityKey( "SENS_2" );

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), ConfigSlotServiceTest.class );
   }


   @Test( expected = InvalidOperationForSystemException.class )
   public void create_SystemConfigSlotWithMultiplePositionsThrowsException() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setNoOfPosition( 2 );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
      }
      ConfigSlotService.create( lConfigSlotDetails );
   }


   @Test( expected = InvalidOperationForSystemException.class )
   public void create_SystemConfigSlotWithNoPositionsThrowsException() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setNoOfPosition( 0 );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
      }
      ConfigSlotService.create( lConfigSlotDetails );
   }


   @Test( expected = ParentBomItemNotFoundException.class )
   public void create_SystemConfigSlotWithNoParentThrowsException() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setParentBomItem( null );
         lConfigSlotDetails.setNoOfPosition( 1 );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
      }
      ConfigSlotService.create( lConfigSlotDetails );
   }


   @Test( expected = InvalidSystemMandatoryBoolValue.class )
   public void create_SystemConfigSlotThatIsOptionalThrowsException() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setMandatory( false );
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setNoOfPosition( 1 );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
      }
      ConfigSlotService.create( lConfigSlotDetails );
   }


   @Test( expected = SoftwareConfigClassException.class )
   public void create_SystemConfigSlotThatIsSoftwareThrowsException() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setSoftware( true );
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setNoOfPosition( 1 );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
      }
      ConfigSlotService.create( lConfigSlotDetails );
   }


   @Test( expected = IetmAssemblyMapDoesNotExistException.class )
   public void create_SystemConfigSlotWithIetmThatDoesNotApplyToAssemblyThrowsException()
         throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC_NOT_ON_ASSEMBLY );
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setNoOfPosition( 1 );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
      }
      ConfigSlotService.create( lConfigSlotDetails );
   }


   @Test
   public void create_SystemConfigSlot() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
         lConfigSlotDetails.setMandatory( true );
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
         lConfigSlotDetails.setNoOfPosition( 1 );
         lConfigSlotDetails
               .setSensitivities( Arrays.asList( new SensitivityConfigurationTO( SENS_1, false ),
                     new SensitivityConfigurationTO( SENS_2, false ) ) );
      }

      ConfigSlotKey lCreatedConfigSlotKey = ConfigSlotService.create( lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( lCreatedConfigSlotKey );
      assertThat( lAfter.getParentBomItem(), is( SYSTEM_CONFIG_SLOT ) );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.getBomClass(), is( RefBOMClassKey.SYS ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( ASSMBL_BOM_CD ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );
   }


   @Test
   public void create_SystemConfigSlotWithSensitivities() throws Throwable {
      String lAtaCode = "SENSITIVE";
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails
               .setSensitivities( Arrays.asList( new SensitivityConfigurationTO( SENS_1, true ),
                     new SensitivityConfigurationTO( SENS_2, false ) ) );
         lConfigSlotDetails.setNoOfPosition( 1 );
         lConfigSlotDetails.setATACode( lAtaCode );
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SYS );
      }

      ConfigSlotService.create( lConfigSlotDetails );

      Optional<System> lSystem = SYSTEM_SERVICE.findByNaturalKey( ASSEMBLY, lAtaCode );
      assertTrue( lSystem.isPresent() );
      List<RefSensitivityKey> lSensitivities = lSystem.get().getSensitivities();
      assertThat( lSensitivities, contains( SENS_1 ) );
      assertThat( lSensitivities, not( contains( SENS_2 ) ) );
   }


   @Test
   public void create_TrackedConfigSlot() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.TRK );
         lConfigSlotDetails.setMandatory( true );
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
         lConfigSlotDetails.setNoOfPosition( 1 );
      }

      ConfigSlotKey lCreatedConfigSlotKey = ConfigSlotService.create( lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( lCreatedConfigSlotKey );
      assertThat( lAfter.getParentBomItem(), is( SYSTEM_CONFIG_SLOT ) );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.getBomClass(), is( RefBOMClassKey.TRK ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( ASSMBL_BOM_CD ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );
   }


   @Test
   public void create_SubAssemblyConfigSlot() throws Throwable {
      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setParentBomItem( SYSTEM_CONFIG_SLOT );
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setConfigClass( RefBOMClassKey.SUBASSY );
         lConfigSlotDetails.setMandatory( true );
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
         lConfigSlotDetails.setNoOfPosition( 1 );
      }

      ConfigSlotKey lCreatedConfigSlotKey = ConfigSlotService.create( lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( lCreatedConfigSlotKey );
      assertThat( lAfter.getParentBomItem(), is( SYSTEM_CONFIG_SLOT ) );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.getBomClass(), is( RefBOMClassKey.SUBASSY ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( ASSMBL_BOM_CD ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );
   }


   @Test( expected = SoftwareConfigClassException.class )
   public void set_RootConfigSlotToBeSoftwareThrowsException() throws Throwable {
      EqpAssmblBom lBefore = new EqpAssmblBom( ROOT_CONFIG_SLOT );
      assumeThat( lBefore.isSoftware(), is( false ) );

      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setSoftware( true );
      }

      ConfigSlotService.set( ROOT_CONFIG_SLOT, lConfigSlotDetails );
   }


   @Test( expected = SoftwareConfigClassException.class )
   public void set_SystemConfigSlotToBeSoftwareThrowsException() throws Throwable {
      EqpAssmblBom lBefore = new EqpAssmblBom( SYSTEM_CONFIG_SLOT );
      assumeThat( lBefore.isSoftware(), is( false ) );

      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setSoftware( true );
      }

      ConfigSlotService.set( SYSTEM_CONFIG_SLOT, lConfigSlotDetails );
   }


   @Test( expected = SoftwareConfigClassException.class )
   public void set_UpdatesSubAssemblyConfigSlotToBeSoftwareThrowsException() throws Throwable {
      EqpAssmblBom lBefore = new EqpAssmblBom( SUB_ASSEMBLY_CONFIG_SLOT );
      assumeThat( lBefore.isSoftware(), is( false ) );

      BomItemDetailsTO lConfigSlotDetails = buildConfigSlotTO();
      {
         lConfigSlotDetails.setSoftware( true );
      }

      ConfigSlotService.set( SUB_ASSEMBLY_CONFIG_SLOT, lConfigSlotDetails );
   }


   @Test
   public void set_UpdatesRootConfigSlot() throws MxException {
      validateDataSetUp( ROOT_CONFIG_SLOT, false );

      BomItemDetailsTO lConfigSlotDetails = new BomItemDetailsTO();
      {
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setMandatory( true ); // Keep this the same to side step async inventory
                                                  // completeness validation
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
      }

      ConfigSlotService.set( ROOT_CONFIG_SLOT, lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( ROOT_CONFIG_SLOT );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( ASSMBL_BOM_CD ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );
   }


   @Test
   public void set_UpdatesSystemConfigSlot() throws MxException {
      String lAtaCode = "SENSITIVE";
      validateDataSetUp( SYSTEM_CONFIG_SLOT, false );

      BomItemDetailsTO lConfigSlotDetails = new BomItemDetailsTO();
      {
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setMandatory( true ); // Keep this the same to side step async inventory
                                                  // completeness validation
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( lAtaCode );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
         lConfigSlotDetails
               .setSensitivities( Arrays.asList( new SensitivityConfigurationTO( SENS_1, false ),
                     new SensitivityConfigurationTO( SENS_2, false ) ) );
      }

      ConfigSlotService.set( SYSTEM_CONFIG_SLOT, lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( SYSTEM_CONFIG_SLOT );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( lAtaCode ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );

      Optional<System> lSystem = SYSTEM_SERVICE.findByNaturalKey( ASSEMBLY, lAtaCode );
      assertTrue( lSystem.isPresent() );
      List<RefSensitivityKey> lSensitivities = lSystem.get().getSensitivities();
      assertThat( lSensitivities, not( contains( SENS_1, SENS_2 ) ) );
   }


   @Test
   public void set_UpdatesTrackedConfigSlot() throws MxException {
      validateDataSetUp( TRACKED_CONFIG_SLOT, !IS_SOFTWARE );

      BomItemDetailsTO lConfigSlotDetails = new BomItemDetailsTO();
      {
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setMandatory( true ); // Keep this the same to side step async inventory
                                                  // completeness validation
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setSoftware( IS_SOFTWARE );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
      }

      ConfigSlotService.set( TRACKED_CONFIG_SLOT, lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( TRACKED_CONFIG_SLOT );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( ASSMBL_BOM_CD ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );
   }


   @Test
   public void set_UpdatesSubAssemblyConfigSlot() throws MxException {
      validateDataSetUp( SUB_ASSEMBLY_CONFIG_SLOT, false );

      BomItemDetailsTO lConfigSlotDetails = new BomItemDetailsTO();
      {
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setMandatory( true ); // Keep this the same to side step async inventory
                                                  // completeness validation
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
      }

      ConfigSlotService.set( SUB_ASSEMBLY_CONFIG_SLOT, lConfigSlotDetails );

      EqpAssmblBom lAfter = new EqpAssmblBom( SUB_ASSEMBLY_CONFIG_SLOT );
      assertThat( lAfter.getAssemblBomName(), is( NAME ) );
      assertThat( lAfter.isMandatoryFlag(), is( true ) );
      assertThat( lAfter.isRVSMFlag(), is( IS_RVSM ) );
      assertThat( lAfter.isETOPSFlag(), is( IS_ETOPS ) );
      assertThat( lAfter.getAssemblBomCd(), is( ASSMBL_BOM_CD ) );
      assertThat( lAfter.getFunctionCode(), is( FUNCTION_CODE ) );
      assertThat( lAfter.getZoneCode(), is( ZONE_CODE ) );
      assertThat( lAfter.getLogCardForm(), is( LOG_CARD_FORM ) );
      assertThat( lAfter.getIetmTopic(), is( IETM_TOPIC_KEY ) );
   }


   @Test( expected = RootBomItemDeletionException.class )
   public void delete_RootConfigSlotThrowsException() throws Throwable {
      new ConfigSlotService( ROOT_CONFIG_SLOT ).delete( AUTHORIZING_HR );
   }


   @Test( expected = NotALeafNodeException.class )
   public void delete_ConfigSlotWithChildrenThrowsException() throws Throwable {
      new ConfigSlotService( SYSTEM_CONFIG_SLOT ).delete( AUTHORIZING_HR );
   }


   @Test
   public void delete_SystemConfigSlotWithSensitivities() throws Throwable {
      Optional<System> lSystem =
            SYSTEM_SERVICE.findByNaturalKey( ASSEMBLY, SYSTEM_WITH_SENSITIVITIES_ATA_CODE );
      assumeThat( lSystem.isPresent(), is( true ) );
      assumeThat( lSystem.get().getId().get(), is( SYSTEM_WITH_SENSITIVITIES ) );
      assumeThat( lSystem.get().getSensitivities(), is( not( empty() ) ) );

      new ConfigSlotService( SYSTEM_WITH_SENSITIVITIES ).delete( AUTHORIZING_HR );

      assertThat( new EqpAssmblBom( SYSTEM_WITH_SENSITIVITIES ).exists(), is( false ) );
   }


   private void validateDataSetUp( ConfigSlotKey aConfigSlotKey, boolean aIsSoftwareBefore ) {
      EqpAssmblBom lBefore = new EqpAssmblBom( aConfigSlotKey );
      assumeThat( lBefore.getAssemblBomName(), is( not( NAME ) ) );
      assumeThat( lBefore.isMandatoryFlag(), is( true ) );
      assumeThat( lBefore.isRVSMFlag(), is( not( IS_RVSM ) ) );
      assumeThat( lBefore.isETOPSFlag(), is( not( IS_ETOPS ) ) );
      assumeThat( lBefore.isSoftware(), is( aIsSoftwareBefore ) );
      assumeThat( lBefore.getAssemblBomCd(), is( not( ASSMBL_BOM_CD ) ) );
      assumeThat( lBefore.getFunctionCode(), is( not( FUNCTION_CODE ) ) );
      assumeThat( lBefore.getZoneCode(), is( not( ZONE_CODE ) ) );
      assumeThat( lBefore.getLogCardForm(), is( not( LOG_CARD_FORM ) ) );
      assumeThat( lBefore.getIetmTopic(), is( not( IETM_TOPIC_KEY ) ) );
   }


   private BomItemDetailsTO buildConfigSlotTO() {
      BomItemDetailsTO lConfigSlotDetails = new BomItemDetailsTO();
      try {
         lConfigSlotDetails.setHrKey( AUTHORIZING_HR );
         lConfigSlotDetails.setName( NAME );
         lConfigSlotDetails.setMandatory( true );
         lConfigSlotDetails.setRVSM( IS_RVSM );
         lConfigSlotDetails.setETOPS( IS_ETOPS );
         lConfigSlotDetails.setATACode( ASSMBL_BOM_CD );
         lConfigSlotDetails.setFunctionCode( FUNCTION_CODE );
         lConfigSlotDetails.setZoneCode( ZONE_CODE );
         lConfigSlotDetails.setLogCardForm( LOG_CARD_FORM );
         lConfigSlotDetails.setIetmTopicSDesc( IETM_TOPIC );
      } catch ( MandatoryArgumentException | StringTooLongException e ) {
         throw new RuntimeException( "Data set up has failed", e );
      }
      return lConfigSlotDetails;
   }
}
