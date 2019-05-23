package com.mxi.mx.core.services.inventory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersStub;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.message.MxMessage.Type;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.plsql.StoredProcedureCall;
import com.mxi.mx.core.plsql.delegates.DeadlineProcedures;
import com.mxi.mx.core.services.inventory.exception.InvTaskDefnSchedDateException;
import com.mxi.mx.core.services.inventory.exception.LockedInventoryException;
import com.mxi.mx.core.services.inventory.exception.ManufactureAndReceivedDateValidator;
import com.mxi.mx.core.services.inventory.phys.AllowEditInventoryReceiveManufactureDateException;
import com.mxi.mx.core.services.inventory.phys.ManufactureDateAfterReceiveDateException;
import com.mxi.mx.core.services.inventory.phys.ManufactureDateSetInFutureException;
import com.mxi.mx.core.unittest.table.evt.EvtEventUtil;
import com.mxi.mx.core.unittest.table.evt.EvtInv;
import com.mxi.mx.core.unittest.table.inv.InvInv;


/**
 * This class tests the {@link MxInventoryDetailsService} class.
 */
public final class MxInventoryDetailsServiceTest {

   private static final InventoryKey LOCKED_INVENTORY = new InventoryKey( 4650, 1001 );

   private static final HumanResourceKey HUMAN_RESOURCE = new HumanResourceKey( 4650, 999 );
   private static final int USER_ID = 999;

   private static final InventoryKey INVENTORY = new InventoryKey( 4650, 1002 );

   private static final InventoryKey BATCH_INVENTORY_1 = new InventoryKey( 4650, 1003 );
   private static final InventoryKey BATCH_INVENTORY_2 = new InventoryKey( 4650, 1004 );

   private static final InventoryKey AIRCRAFT_INVENTORY = new InventoryKey( 4650, 1005 );
   private static final InventoryKey AIRCRAFT_SUBSYS1_INVENTORY = new InventoryKey( 4650, 1006 );
   private static final InventoryKey AIRCRAFT_SUBSYS2_INVENTORY = new InventoryKey( 4650, 1007 );

   private static final InventoryKey ASSEMBLY_INVENTORY = new InventoryKey( 4650, 1008 );
   private static final InventoryKey ASSEMBLY_SUBSYS1_INVENTORY = new InventoryKey( 4650, 1009 );
   private static final InventoryKey ASSEMBLY_SUBSYS2_INVENTORY = new InventoryKey( 4650, 1010 );

   /** Having the old dates as null to make sure no null-pointer exception is thrown */
   private static final InventoryKey INVENTORY_WITH_NULL_MANUFACT_AND_RECEIVED_DATE =
         new InventoryKey( 4650, 1011 );

   private final Date iJan_1_2012;

   private final Date iJan_2_2012;

   private DeadlineProcedures iMockDeadlineDelegate;

   private MxInventoryDetailsService iService;

   private final Date iTheDayAfterTomorrow;

   private final Date iTomorrow;

   private final Date iToday;

   private final Date iYesterday;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private UserParametersStub iUserParametersStub;


   /**
    * Creates a new {@linkplain MxInventoryDetailsServiceTest} object.
    */
   public MxInventoryDetailsServiceTest() {
      Calendar lCal = Calendar.getInstance();
      lCal.set( Calendar.HOUR_OF_DAY, 0 );
      lCal.set( Calendar.MINUTE, 0 );
      lCal.set( Calendar.SECOND, 0 );
      lCal.set( Calendar.MILLISECOND, 0 );
      iToday = lCal.getTime();

      lCal.add( Calendar.DATE, -1 );
      iYesterday = lCal.getTime();

      lCal.add( Calendar.DATE, +2 );
      iTomorrow = lCal.getTime();

      lCal.add( Calendar.DATE, +1 );
      iTheDayAfterTomorrow = lCal.getTime();

      lCal.set( 2012, 0, 1 );
      iJan_1_2012 = lCal.getTime();

      lCal.set( 2012, 0, 2 );
      iJan_2_2012 = lCal.getTime();
   }


   /**
    * Tests to ensure that no audit trail event is added when the manufactured date statys the same
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testNoAuditTrailWhenManufactDateNotChanged() throws Exception {

      iService.setManufactureAndReceivedDates( INVENTORY, iJan_1_2012, iJan_2_2012,
            HUMAN_RESOURCE );

      List<EventKey> lEvents = EvtInv.getLatestEventsForInv( INVENTORY, 1 );
      assertTrue( lEvents.isEmpty() );
   }


   /**
    * Tests that when the inventory is a BATCH, all inventory with the same batch no get updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatAllMatchingBatchesAreUpdated() throws Exception {

      iService.setManufactureAndReceivedDates( BATCH_INVENTORY_1, iYesterday, iToday,
            HUMAN_RESOURCE );

      InvInv lBatch1InvInv = new InvInv( BATCH_INVENTORY_1 );
      lBatch1InvInv.assertManufacturedDate( iYesterday );
      lBatch1InvInv.assertReceivedDate( iToday );

      InvInv lBatch2InvInv = new InvInv( BATCH_INVENTORY_2 );
      lBatch2InvInv.assertManufacturedDate( iYesterday );
      lBatch2InvInv.assertReceivedDate( iToday );
   }


   /**
    * Tests to ensure that an audit trail event is added when the manufactured date changes
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatAuditTrailIsGeneratedWhenManufactDateChanged() throws Exception {

      iService.setManufactureAndReceivedDates( INVENTORY, iYesterday, iToday, HUMAN_RESOURCE );

      EventKey lAuditEvent = EvtInv.getLatestEventForInv( INVENTORY );
      EvtEventUtil lAuditEvtEvent = new EvtEventUtil( lAuditEvent );
      lAuditEvtEvent.assertEventType( RefEventTypeKey.IMD.getCd() );
      lAuditEvtEvent.assertEditorHr( HUMAN_RESOURCE );
   }


   /**
    * Test that when the givem inventory is locked, a LockedInventoryException is thrown.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatLockedInventoryThrowsException() throws Exception {
      try {
         iService.setManufactureAndReceivedDates( LOCKED_INVENTORY, iYesterday, iToday,
               HUMAN_RESOURCE );

         fail( "Expected LockedInventoryException" );
      } catch ( LockedInventoryException e ) {
         ;
      }
   }


   /**
    * Tests that the validator is called during execution of the logic.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatManufacturedAndReceivedDateAreValidated() throws Exception {
      ManufactureAndReceivedDateValidator lValidator = new ManufactureAndReceivedDateValidator() {

         /**
          * Always add an error.
          *
          * @param aMessages
          *           A validator to store any problem messages
          * @param aInventory
          *           the date value
          * @param aManufactDate
          *           the config slot
          * @param aReceivedDate
          *           the part no key
          *
          * @throws InvTaskDefnSchedDateException
          *            if given date is null in the inventory and there exists task defns with the
          *            given date in maintenix.
          */
         @Override
         public void checkThat( Messages aMessages, InventoryKey aInventory, Date aManufactDate,
               Date aReceivedDate ) throws InvTaskDefnSchedDateException {
            aMessages.add( new MxMessage( Type.ERROR, "test.err.99999", "test.msg.99999" ) );
         }
      };

      MxInventoryDetailsService lService = new MxInventoryDetailsService( lValidator );

      try {
         lService.setManufactureAndReceivedDates( INVENTORY, iYesterday, iToday, HUMAN_RESOURCE );

         fail( "Expected ValidationException" );
      } catch ( ValidationException e ) {
         ;
      }
   }


   /**
    * Test that when the manufacture date is after the received date, a
    * ManufactureDateAfterReceiveDateException is thrown.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testThatSettingManufactAfterReceivedThrowsException() throws Exception {
      try {
         iService.setManufactureAndReceivedDates( INVENTORY, iToday, iYesterday, HUMAN_RESOURCE );

         fail( "Expected ManufactureDateAfterReceiveDateException" );
      } catch ( ManufactureDateAfterReceiveDateException e ) {
         ;
      }
   }


   /**
    * Test that when ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY is set as false and
    * the date is different a AllowEditInventoryReceiveManufactureDateException is thrown.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testAllowEditInventoryReceiveManufactureDateException() throws Exception {

      iUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            false );
      try {
         iService.setManufactureAndReceivedDates( INVENTORY_WITH_NULL_MANUFACT_AND_RECEIVED_DATE,
               iToday, iYesterday, HUMAN_RESOURCE );

         fail( "Expected AllowEditInventoryReceiveManufactureDateException" );
      } catch ( AllowEditInventoryReceiveManufactureDateException e ) {
         ;
      }
   }


   /**
    * Tests that when setting both the received and manufacture dates to their existing values, no
    * action is taken.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatSettingToSameCausesNoAction() throws Exception {

      iService.setManufactureAndReceivedDates( INVENTORY, iJan_1_2012, iJan_2_2012,
            HUMAN_RESOURCE );
   }


   /**
    * Tests that all subsystems are updated when the inventory is an aircraft.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatSubsystemsAreUpdatedForAircraft() throws Exception {

      iService.setManufactureAndReceivedDates( AIRCRAFT_INVENTORY, iYesterday, iToday,
            HUMAN_RESOURCE );

      InvInv lAcftInvInv = new InvInv( AIRCRAFT_INVENTORY );
      lAcftInvInv.assertManufacturedDate( iYesterday );
      lAcftInvInv.assertReceivedDate( iToday );

      InvInv lAcftSubSys1InvInv = new InvInv( AIRCRAFT_SUBSYS1_INVENTORY );
      lAcftSubSys1InvInv.assertManufacturedDate( iYesterday );
      lAcftSubSys1InvInv.assertReceivedDate( iToday );

      InvInv lAcftSubSys2InvInv = new InvInv( AIRCRAFT_SUBSYS2_INVENTORY );
      lAcftSubSys2InvInv.assertManufacturedDate( iYesterday );
      lAcftSubSys2InvInv.assertReceivedDate( iToday );
   }


   /**
    * Tests that all subsystems are updated when the inventory is an assembly.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testThatSubsystemsAreUpdatedForAssembly() throws Exception {

      iService.setManufactureAndReceivedDates( ASSEMBLY_INVENTORY, iYesterday, iToday,
            HUMAN_RESOURCE );

      InvInv lAssyInvInv = new InvInv( ASSEMBLY_INVENTORY );
      lAssyInvInv.assertManufacturedDate( iYesterday );
      lAssyInvInv.assertReceivedDate( iToday );

      InvInv lAssySubSys1InvInv = new InvInv( ASSEMBLY_SUBSYS1_INVENTORY );
      lAssySubSys1InvInv.assertManufacturedDate( iYesterday );
      lAssySubSys1InvInv.assertReceivedDate( iToday );

      InvInv lAssySubSys2InvInv = new InvInv( ASSEMBLY_SUBSYS2_INVENTORY );
      lAssySubSys2InvInv.assertManufacturedDate( iYesterday );
      lAssySubSys2InvInv.assertReceivedDate( iToday );
   }


   /**
    * Tests that if ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE is set to be true, user
    * can set the manufactured date to the future.
    */
   @Test
   public void
         setManufactureAndReceivedDates_withManufacturedDateInTheFutureAndAllowConfigParmIsTrue()
               throws Exception {

      iUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            true );

      iService.setManufactureAndReceivedDates( ASSEMBLY_INVENTORY, iTomorrow, iTheDayAfterTomorrow,
            HUMAN_RESOURCE );

      InvInv lAssyInvInv = new InvInv( ASSEMBLY_INVENTORY );
      lAssyInvInv.assertManufacturedDate( iTomorrow );
      lAssyInvInv.assertReceivedDate( iTheDayAfterTomorrow );

      InvInv lAssySubSys1InvInv = new InvInv( ASSEMBLY_SUBSYS1_INVENTORY );
      lAssySubSys1InvInv.assertManufacturedDate( iTomorrow );
      lAssySubSys1InvInv.assertReceivedDate( iTheDayAfterTomorrow );

      InvInv lAssySubSys2InvInv = new InvInv( ASSEMBLY_SUBSYS2_INVENTORY );
      lAssySubSys2InvInv.assertManufacturedDate( iTomorrow );
      lAssySubSys2InvInv.assertReceivedDate( iTheDayAfterTomorrow );
   }


   /**
    * Tests that if ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE is set to be false, user
    * cannot set the manufactured date to the future.
    */
   @Test( expected = ManufactureDateSetInFutureException.class )
   public void
         setManufactureAndReceivedDates_withManufacturedDateInTheFutureAndAllowConfigParmIsFalse()
               throws Exception {

      GlobalParametersStub lGlobalParamsLogic =
            new GlobalParametersStub( ParmTypeEnum.LOGIC.name() );
      lGlobalParamsLogic.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      GlobalParameters.setInstance( lGlobalParamsLogic );

      iService.setManufactureAndReceivedDates( ASSEMBLY_INVENTORY, iTomorrow, iTheDayAfterTomorrow,
            HUMAN_RESOURCE );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iMockDeadlineDelegate = Mockito.mock( DeadlineProcedures.class );
      StoredProcedureCall.getInstance().setDelegate( iMockDeadlineDelegate );

      iUserParametersStub = new UserParametersStub( USER_ID, "SECURED_RESOURCE" );
      iUserParametersStub.setBoolean( "ACTION_ALLOW_EDIT_RECEIVE_OR_MANUFACTURE_DATE_ON_INVENTORY",
            true );
      iUserParametersStub.setBoolean( "ACTION_ALLOW_CREATE_EDIT_INV_WITH_FUTURE_MANUFACT_DATE",
            false );
      UserParameters.setInstance( USER_ID, "SECURED_RESOURCE", iUserParametersStub );

      iService = new MxInventoryDetailsService();
   }


   @After
   public void tearDown() {
      StoredProcedureCall.getInstance().setDelegate( ( DeadlineProcedures ) null );
      UserParameters.setInstance( USER_ID, "SECURED_RESOURCE", null );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
