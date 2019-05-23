package com.mxi.mx.db.trigger.fault;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.exception.StringTooLongException;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.MxAfterTrigger;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.trigger.TriggerFactory;
import com.mxi.mx.core.ejb.fault.FaultBean;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.fault.FaultService;
import com.mxi.mx.core.services.fault.RaiseFaultDetailsTO;


/**
 * This class tests the fault create triggers.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FaultCreateTriggerTest implements MxAfterTrigger<UUID> {

   private static Boolean triggerCalled;

   private RaiseFaultDetailsTO raiseFaultDetailsTO;
   private static final String AIRCRAFT_ID = "00000000000000000000000000000002";
   private static final String FAULT_NAME = "faultname";
   private static final String FAULT_SOURCE_CODE = "PILOT";
   private Date date;
   private static final String FAULT_ID = "00000000000000000000000000000001";
   private static final String AUTHORIZING_HR_KEY = "4650:1221";
   private static final String INVENTORY_ID = "00000000000000000000000000000013";

   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      GlobalParametersFake triggerParams =
            new GlobalParametersFake( ParmTypeEnum.TRIGGER_CACHE.name() );
      GlobalParameters.setInstance( triggerParams );
      TriggerFactory.setInstance( null );
      triggerCalled = false;
   }


   @Test
   public void testFaultCreateTrigger()
         throws KeyConversionException, ParseException, MxException, TriggerException {
      FaultBean faultBean = new FaultBean();
      faultBean.setSessionContext( new SessionContextFake() );
      raiseFaultDetailsTO = buildRaiseFaultDetailsTOData();
      HumanResourceKey humanResourceKey = new HumanResourceKey( 1, 1 );
      faultBean.createFault( raiseFaultDetailsTO, humanResourceKey );
      assertTrue( "Trigger did not get invoked", triggerCalled );

   }


   @Test
   public void testFaultDuplicateTrigger()
         throws KeyConversionException, ParseException, MxException, TriggerException {
      FaultKey faultKey = legacyKeyUtil.altIdToLegacyKey( FAULT_ID, FaultKey.class );
      FaultService faultService = new FaultService( faultKey );
      HumanResourceKey humanResourceKey = new HumanResourceKey( AUTHORIZING_HR_KEY );
      Date raisedDate = getDate();
      InventoryKey failedSystemKey =
            legacyKeyUtil.altIdToLegacyKey( INVENTORY_ID, InventoryKey.class );
      faultService.duplicate( raisedDate, failedSystemKey, humanResourceKey );
      assertTrue( "Trigger did not get invoked", triggerCalled );
   }


   /**
    *
    * {@inheritDoc}
    */
   @Override
   public void after( UUID faultId ) throws TriggerException {
      triggerCalled = true;
   }


   private Date getDate() throws ParseException {
      DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      String todayDate = dateFormat.format( new Date() );
      date = dateFormat.parse( todayDate );
      return date;
   }


   // create RaiseFaultDetailsTO data
   private RaiseFaultDetailsTO buildRaiseFaultDetailsTOData() throws KeyConversionException,
         MandatoryArgumentException, StringTooLongException, ParseException {
      RaiseFaultDetailsTO raiseFaultDetailsTO = new RaiseFaultDetailsTO();
      InventoryKey failedAircraftKey =
            legacyKeyUtil.altIdToLegacyKey( AIRCRAFT_ID, InventoryKey.class );
      raiseFaultDetailsTO.setFailedSystem( failedAircraftKey );
      raiseFaultDetailsTO.setFaultName( FAULT_NAME );
      raiseFaultDetailsTO.setFaultSource( FAULT_SOURCE_CODE );
      raiseFaultDetailsTO.setFoundOnDate( getDate() );
      return raiseFaultDetailsTO;
   }

}
