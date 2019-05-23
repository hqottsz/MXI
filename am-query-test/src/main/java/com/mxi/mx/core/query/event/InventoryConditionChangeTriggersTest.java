package com.mxi.mx.core.query.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventInventoryKey;
import com.mxi.mx.core.key.EventInventoryUsageKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtInvTable;
import com.mxi.mx.core.table.evt.EvtInvUsageTable;


/**
 * Tests the triggers as follows:
 *
 * <pre>
 *    1. tiudbr_inv_cnd_chg_event on table inv_chg_event
 *    2. tiudbr_inv_cnd_chg_stage on table inv_cnd_chg_stage
 *    3. tiudbr_inv_cnd_chg_inv on table inv_cnd_chg_inv
 *    4. tiudbr_inv_cnd_chg_inv_usage on table inv_cnd_chg_inv_usage
 * </pre>
 */
public final class InventoryConditionChangeTriggersTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   private static enum InvCndChgEventColumn {
      EVENT_DB_ID, EVENT_ID, STAGE_REASON_DB_ID, STAGE_REASON_CD, EDITOR_HR_DB_ID, EDITOR_HR_ID,
      EVENT_STATUS_DB_ID, EVENT_STATUS_CD, EVENT_REASON_DB_ID, EVENT_REASON_CD, EVENT_SDESC,
      EXT_KEY_SDESC, SEQ_ERR_BOOL, EVENT_LDESC, EVENT_DT, SCHED_START_DT, SUB_EVENT_ORD
   };

   private static enum InvCndChgInvColumn {
      EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, INV_NO_DB_ID, INV_NO_ID, NH_INV_NO_DB_ID, NH_INV_NO_ID,
      ASSMBL_INV_NO_DB_ID, ASSMBL_INV_NO_ID, H_INV_NO_DB_ID, H_INV_NO_ID, ASSMBL_DB_ID, ASSMBL_CD,
      ASSMBL_BOM_ID, ASSMBL_POS_ID, PART_NO_DB_ID, PART_NO_ID, BOM_PART_DB_ID, BOM_PART_ID,
      MAIN_INV_BOOL
   };

   private static enum InvCndChgInvUsageColumn {
      EVENT_DB_ID, EVENT_ID, EVENT_INV_ID, DATA_TYPE_DB_ID, DATA_TYPE_ID, TSN_QT, TSO_QT, TSI_QT,
      ASSMBL_TSN_QT, ASSMBL_TSO_QT, H_TSN_QT, H_TSO_QT, NH_TSN_QT, NH_TSO_QT, NEGATED_BOOL
   };


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_event which copy same data to table evt_event when insert
    * data into table inv_cnd_chg_event
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgEventWithInsert() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();
      RefStageReasonKey lRefStageReasonKey = new RefStageReasonKey( 0, "NEW" );
      HumanResourceKey lHumanResourceKey = new HumanResourceKey( 4650, 100 );
      RefEventStatusKey lRefEventStatusKey = new RefEventStatusKey( 0, "COMPLETE" );
      Calendar lEventCalendar = Calendar.getInstance();
      lEventCalendar.add( Calendar.DATE, -5 );
      Calendar lSchedStartCalendar = Calendar.getInstance();
      lSchedStartCalendar.add( Calendar.DATE, -6 );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventKey, InvCndChgEventColumn.EVENT_DB_ID.name(),
            InvCndChgEventColumn.EVENT_ID.name() );
      lArgs.add( lRefStageReasonKey, InvCndChgEventColumn.STAGE_REASON_DB_ID.name(),
            InvCndChgEventColumn.STAGE_REASON_CD.name() );
      lArgs.add( lHumanResourceKey, InvCndChgEventColumn.EDITOR_HR_DB_ID.name(),
            InvCndChgEventColumn.EDITOR_HR_ID.name() );
      lArgs.add( lRefEventStatusKey, InvCndChgEventColumn.EVENT_STATUS_DB_ID.name(),
            InvCndChgEventColumn.EVENT_STATUS_CD.name() );
      lArgs.add( InvCndChgEventColumn.EVENT_REASON_DB_ID.name(), 10 );
      lArgs.add( InvCndChgEventColumn.EVENT_REASON_CD.name(), "ADMIN" );
      lArgs.add( InvCndChgEventColumn.EVENT_SDESC.name(), "test" );
      lArgs.add( InvCndChgEventColumn.EXT_KEY_SDESC.name(), "ex key" );
      lArgs.add( InvCndChgEventColumn.EVENT_LDESC.name(), "test test" );
      lArgs.add( InvCndChgEventColumn.EVENT_DT.name(), lEventCalendar.getTime() );
      lArgs.add( InvCndChgEventColumn.SCHED_START_DT.name(), lSchedStartCalendar.getTime() );

      // ACT
      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", lArgs );

      // ASSERT
      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable lEvtEventTable = lEvtEventDao.findByPrimaryKey( lEventKey );

      RefStageReasonKey lExpectedRefStageReason = lRefStageReasonKey;
      RefStageReasonKey lActualRefStageReason = lEvtEventTable.getStageReasonKey();
      HumanResourceKey lExpectedHumanResource = lHumanResourceKey;
      HumanResourceKey lActualHumanResource = lEvtEventTable.getEditorHr();
      RefEventStatusKey lExpectedRefEventStatus = lRefEventStatusKey;
      RefEventStatusKey lActualRefEventStatus = lEvtEventTable.getEventStatus();
      String lExpectedEventReason = "ADMIN";
      String lActualRefEventReason = lEvtEventTable.getEventReasonCd();
      String lExpectedEventSdesc = "test";
      String lActualEventSdesc = lEvtEventTable.getEventSdesc();
      String lExpectedExtKeySDesc = "ex key";
      String lActualExtKeySDesc = lEvtEventTable.getExtKeySDesc();
      String lExpectedEventLdesc = "test test";
      String lActualEventLdesc = lEvtEventTable.getEventLdesc();
      long lExpectedEventDate = lEventCalendar.getTimeInMillis();
      long lActualEventDate = lEvtEventTable.getEventDate().getTime();
      long lExpectedSchedStartDt = lSchedStartCalendar.getTimeInMillis();
      long lActualSchedStartDt = lEvtEventTable.getSchedStartDt().getTime();

      assertTrue( lEvtEventTable.exists() );

      assertEquals( "Incorrect stage reason.", lExpectedRefStageReason, lActualRefStageReason );
      assertEquals( "Incorrect editor human resource.", lExpectedHumanResource,
            lActualHumanResource );
      assertEquals( "Incorrect event staus.", lExpectedRefEventStatus, lActualRefEventStatus );
      assertEquals( "Incorrect event reason.", lExpectedEventReason, lActualRefEventReason );
      assertEquals( "Incorrect event short description.", lExpectedEventSdesc, lActualEventSdesc );
      assertEquals( "Incorrect external key.", lExpectedExtKeySDesc, lActualExtKeySDesc );
      assertEquals( "Incorrect event long description.", lExpectedEventLdesc, lActualEventLdesc );
      assertTrue( "Incorrect event date in millseconds",
            Math.abs( lExpectedEventDate - lActualEventDate ) < 1000 );
      assertTrue( "Incorrect scheduled start date in millseconds",
            Math.abs( lExpectedSchedStartDt - lActualSchedStartDt ) < 1000 );

   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_event which copy same data to table evt_event when update
    * data in table inv_cnd_chg_event
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgEventWithUpdate() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();
      RefStageReasonKey lRefStageReasonKey = new RefStageReasonKey( 0, "NEW" );
      HumanResourceKey lHumanResourceKey = new HumanResourceKey( 4650, 100 );
      RefEventStatusKey lRefEventStatusKey = new RefEventStatusKey( 0, "COMPLETE" );
      Calendar lEventCalendar = Calendar.getInstance();
      lEventCalendar.add( Calendar.DATE, -5 );
      Calendar lSchedStartCalendar = Calendar.getInstance();
      lSchedStartCalendar.add( Calendar.DATE, -6 );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventKey, InvCndChgEventColumn.EVENT_DB_ID.name(),
            InvCndChgEventColumn.EVENT_ID.name() );
      lArgs.add( lRefStageReasonKey, InvCndChgEventColumn.STAGE_REASON_DB_ID.name(),
            InvCndChgEventColumn.STAGE_REASON_CD.name() );
      lArgs.add( lHumanResourceKey, InvCndChgEventColumn.EDITOR_HR_DB_ID.name(),
            InvCndChgEventColumn.EDITOR_HR_ID.name() );
      lArgs.add( lRefEventStatusKey, InvCndChgEventColumn.EVENT_STATUS_DB_ID.name(),
            InvCndChgEventColumn.EVENT_STATUS_CD.name() );
      lArgs.add( InvCndChgEventColumn.EVENT_REASON_DB_ID.name(), 10 );
      lArgs.add( InvCndChgEventColumn.EVENT_REASON_CD.name(), "ADMIN" );
      lArgs.add( InvCndChgEventColumn.EVENT_SDESC.name(), "test" );
      lArgs.add( InvCndChgEventColumn.EXT_KEY_SDESC.name(), "ex key" );
      lArgs.add( InvCndChgEventColumn.EVENT_LDESC.name(), "test test" );
      lArgs.add( InvCndChgEventColumn.EVENT_DT.name(), lEventCalendar.getTime() );
      lArgs.add( InvCndChgEventColumn.SCHED_START_DT.name(), lSchedStartCalendar.getTime() );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", lArgs );

      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable lEvtEventTable = lEvtEventDao.findByPrimaryKey( lEventKey );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lEventKey, InvCndChgEventColumn.EVENT_DB_ID.name(),
            InvCndChgEventColumn.EVENT_ID.name() );
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( InvCndChgEventColumn.EVENT_SDESC.name(), "test1" );

      // ACT
      MxDataAccess.getInstance().executeSmartUpdate( "inv_cnd_chg_event", lSetArgs, lWhereArgs );

      // ASSERT
      lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      lEvtEventTable = lEvtEventDao.findByPrimaryKey( lEventKey );

      String lExpectedEventSdesc = "test1";
      String lActualEventSdesc = lEvtEventTable.getEventSdesc();

      assertEquals( "Incorrect event short description.", lExpectedEventSdesc, lActualEventSdesc );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_event which deletes same data to table evt_event when
    * delete data in table inv_cnd_chg_event
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgEventWithDelete() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();
      RefStageReasonKey lRefStageReasonKey = new RefStageReasonKey( 0, "NEW" );
      HumanResourceKey lHumanResourceKey = new HumanResourceKey( 4650, 100 );
      RefEventStatusKey lRefEventStatusKey = new RefEventStatusKey( 0, "COMPLETE" );
      Calendar lEventCalendar = Calendar.getInstance();
      lEventCalendar.add( Calendar.DATE, -5 );
      Calendar lSchedStartCalendar = Calendar.getInstance();
      lSchedStartCalendar.add( Calendar.DATE, -6 );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventKey, InvCndChgEventColumn.EVENT_DB_ID.name(),
            InvCndChgEventColumn.EVENT_ID.name() );
      lArgs.add( lRefStageReasonKey, InvCndChgEventColumn.STAGE_REASON_DB_ID.name(),
            InvCndChgEventColumn.STAGE_REASON_CD.name() );
      lArgs.add( lHumanResourceKey, InvCndChgEventColumn.EDITOR_HR_DB_ID.name(),
            InvCndChgEventColumn.EDITOR_HR_ID.name() );
      lArgs.add( lRefEventStatusKey, InvCndChgEventColumn.EVENT_STATUS_DB_ID.name(),
            InvCndChgEventColumn.EVENT_STATUS_CD.name() );
      lArgs.add( InvCndChgEventColumn.EVENT_REASON_DB_ID.name(), 10 );
      lArgs.add( InvCndChgEventColumn.EVENT_REASON_CD.name(), "ADMIN" );
      lArgs.add( InvCndChgEventColumn.EVENT_SDESC.name(), "test" );
      lArgs.add( InvCndChgEventColumn.EXT_KEY_SDESC.name(), "ex key" );
      lArgs.add( InvCndChgEventColumn.EVENT_LDESC.name(), "test test" );
      lArgs.add( InvCndChgEventColumn.EVENT_DT.name(), lEventCalendar.getTime() );
      lArgs.add( InvCndChgEventColumn.SCHED_START_DT.name(), lSchedStartCalendar.getTime() );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_event", lArgs );

      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EvtEventTable lEvtEventTable = lEvtEventDao.findByPrimaryKey( lEventKey );

      lArgs.clear();
      lArgs.add( lEventKey, InvCndChgEventColumn.EVENT_DB_ID.name(),
            InvCndChgEventColumn.EVENT_ID.name() );

      // ACT
      MxDataAccess.getInstance().executeDelete( "inv_cnd_chg_event", lArgs );

      // ASSERT
      lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      lEvtEventTable = lEvtEventDao.findByPrimaryKey( lEventKey );

      assertFalse( "No data", lEvtEventTable.exists() );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_inv which copy same data to table evt_inv when insert
    * data into table inv_cnd_chg_inv
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgInvWithInsert() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();

      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lEventKey, 1 );
      InventoryKey lInventoryKey = new InventoryKey( 5000000, 132181 );
      InventoryKey lNHInventoryKey = new InventoryKey( 5000000, 132158 );
      InventoryKey lAssemblyInventoryKey = new InventoryKey( 5000000, 129648 );
      InventoryKey lHInventoryKey = new InventoryKey( 5000000, 129648 );
      ConfigSlotPositionKey lConfigSlotPositionKey =
            new ConfigSlotPositionKey( 5000000, "A320", 362, 1 );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventInventoryKey, InvCndChgInvColumn.EVENT_DB_ID.name(),
            InvCndChgInvColumn.EVENT_ID.name(), InvCndChgInvColumn.EVENT_INV_ID.name() );
      lArgs.add( lInventoryKey, InvCndChgInvColumn.INV_NO_DB_ID.name(),
            InvCndChgInvColumn.INV_NO_ID.name() );
      lArgs.add( lNHInventoryKey, InvCndChgInvColumn.NH_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.NH_INV_NO_ID.name() );
      lArgs.add( lAssemblyInventoryKey, InvCndChgInvColumn.ASSMBL_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.ASSMBL_INV_NO_ID.name() );
      lArgs.add( lHInventoryKey, InvCndChgInvColumn.H_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.H_INV_NO_ID.name() );
      lArgs.add( lConfigSlotPositionKey, InvCndChgInvColumn.ASSMBL_DB_ID.name(),
            InvCndChgInvColumn.ASSMBL_CD.name(), InvCndChgInvColumn.ASSMBL_BOM_ID.name(),
            InvCndChgInvColumn.ASSMBL_POS_ID.name() );
      lArgs.add( null, InvCndChgInvColumn.PART_NO_DB_ID.name(),
            InvCndChgInvColumn.PART_NO_ID.name() );
      lArgs.add( null, InvCndChgInvColumn.BOM_PART_DB_ID.name(),
            InvCndChgInvColumn.BOM_PART_ID.name() );
      lArgs.add( InvCndChgInvColumn.MAIN_INV_BOOL.name(), 1 );

      // ACT
      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv", lArgs );

      // ASSERT
      EvtInvTable lEvtInvTable =
            EvtInvTable.findByPrimaryKey( new EventInventoryKey( lEventKey, 1 ) );
      InventoryKey lExpectedInventoryKey = lInventoryKey;
      InventoryKey lActualInventoryKey = lEvtInvTable.getInventory();
      InventoryKey lExpectedNHInventoryKey = lNHInventoryKey;
      InventoryKey lActualNHInventoryKey = lEvtInvTable.getNHInventoryKey();
      InventoryKey lExpectedHInventoryKey = lHInventoryKey;
      InventoryKey lActualHInventoryKey = lEvtInvTable.getHInventory();
      InventoryKey lExpectedAssemblyInventoryKey = lAssemblyInventoryKey;
      InventoryKey lActualAssemblyInventoryKey =
            lEvtInvTable.getAssmblInventoryKey( lInventoryKey );
      ConfigSlotPositionKey lExpectedConfigSlotPositionKey = lConfigSlotPositionKey;
      ConfigSlotPositionKey lActualConfigSlotPositionKey = lEvtInvTable.getAssmbl();
      PartNoKey lActualPartNoKey = lEvtInvTable.getPartNoKey();
      PartGroupKey lActualPartGroupKey = lEvtInvTable.getBomPartKey();
      boolean lActualMainInvBool = lEvtInvTable.getMainInvBool();

      assertTrue( lEvtInvTable.exists() );

      assertEquals( "Incorrect inventory.", lExpectedInventoryKey, lActualInventoryKey );
      assertEquals( "Incorrect next highest inventory.", lExpectedNHInventoryKey,
            lActualNHInventoryKey );
      assertEquals( "Incorrect highest inventory.", lExpectedHInventoryKey, lActualHInventoryKey );
      assertEquals( "Incorrect assembly inventory.", lExpectedAssemblyInventoryKey,
            lActualAssemblyInventoryKey );
      assertEquals( "Incorrect assembly position.", lExpectedConfigSlotPositionKey,
            lActualConfigSlotPositionKey );
      assertNull( "Incorrect part no.", lActualPartNoKey );
      assertNull( "Incorrect bom part no.", lActualPartGroupKey );
      assertTrue( "The inventory is not main inventory", lActualMainInvBool );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_inv which update same data in table evt_inv when update
    * data in table inv_cnd_chg_inv
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgInvWithUpdate() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();

      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lEventKey, 1 );
      InventoryKey lInventoryKey = new InventoryKey( 5000000, 132181 );
      InventoryKey lNHInventoryKey = new InventoryKey( 5000000, 132158 );
      InventoryKey lAssemblyInventoryKey = new InventoryKey( 5000000, 129648 );
      InventoryKey lHInventoryKey = new InventoryKey( 5000000, 129648 );
      ConfigSlotPositionKey lConfigSlotPositionKey =
            new ConfigSlotPositionKey( 5000000, "A320", 362, 1 );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventInventoryKey, InvCndChgInvColumn.EVENT_DB_ID.name(),
            InvCndChgInvColumn.EVENT_ID.name(), InvCndChgInvColumn.EVENT_INV_ID.name() );
      lArgs.add( lInventoryKey, InvCndChgInvColumn.INV_NO_DB_ID.name(),
            InvCndChgInvColumn.INV_NO_ID.name() );
      lArgs.add( lNHInventoryKey, InvCndChgInvColumn.NH_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.NH_INV_NO_ID.name() );
      lArgs.add( lAssemblyInventoryKey, InvCndChgInvColumn.ASSMBL_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.ASSMBL_INV_NO_ID.name() );
      lArgs.add( lHInventoryKey, InvCndChgInvColumn.H_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.H_INV_NO_ID.name() );
      lArgs.add( lConfigSlotPositionKey, InvCndChgInvColumn.ASSMBL_DB_ID.name(),
            InvCndChgInvColumn.ASSMBL_CD.name(), InvCndChgInvColumn.ASSMBL_BOM_ID.name(),
            InvCndChgInvColumn.ASSMBL_POS_ID.name() );
      lArgs.add( null, InvCndChgInvColumn.PART_NO_DB_ID.name(),
            InvCndChgInvColumn.PART_NO_ID.name() );
      lArgs.add( null, InvCndChgInvColumn.BOM_PART_DB_ID.name(),
            InvCndChgInvColumn.BOM_PART_ID.name() );
      lArgs.add( InvCndChgInvColumn.MAIN_INV_BOOL.name(), 1 );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv", lArgs );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lEventInventoryKey, InvCndChgInvColumn.EVENT_DB_ID.name(),
            InvCndChgInvColumn.EVENT_ID.name(), InvCndChgInvColumn.EVENT_INV_ID.name() );
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( InvCndChgInvColumn.MAIN_INV_BOOL.name(), 0 );

      // ACT
      MxDataAccess.getInstance().executeUpdate( "inv_cnd_chg_inv", lSetArgs, lWhereArgs );

      // ASSERT
      EvtInvTable lEvtInvTable =
            EvtInvTable.findByPrimaryKey( new EventInventoryKey( lEventKey, 1 ) );
      boolean lActualMainInvBool = lEvtInvTable.getMainInvBool();

      assertTrue( lEvtInvTable.exists() );

      assertFalse( "The inventory is main inventory", lActualMainInvBool );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_inv which delete same data in table evt_inv when delete
    * data in table inv_cnd_chg_inv
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgInvWithDelete() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();

      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lEventKey, 1 );
      InventoryKey lInventoryKey = new InventoryKey( 5000000, 132181 );
      InventoryKey lNHInventoryKey = new InventoryKey( 5000000, 132158 );
      InventoryKey lAssemblyInventoryKey = new InventoryKey( 5000000, 129648 );
      InventoryKey lHInventoryKey = new InventoryKey( 5000000, 129648 );
      ConfigSlotPositionKey lConfigSlotPositionKey =
            new ConfigSlotPositionKey( 5000000, "A320", 362, 1 );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventInventoryKey, InvCndChgInvColumn.EVENT_DB_ID.name(),
            InvCndChgInvColumn.EVENT_ID.name(), InvCndChgInvColumn.EVENT_INV_ID.name() );
      lArgs.add( lInventoryKey, InvCndChgInvColumn.INV_NO_DB_ID.name(),
            InvCndChgInvColumn.INV_NO_ID.name() );
      lArgs.add( lNHInventoryKey, InvCndChgInvColumn.NH_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.NH_INV_NO_ID.name() );
      lArgs.add( lAssemblyInventoryKey, InvCndChgInvColumn.ASSMBL_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.ASSMBL_INV_NO_ID.name() );
      lArgs.add( lHInventoryKey, InvCndChgInvColumn.H_INV_NO_DB_ID.name(),
            InvCndChgInvColumn.H_INV_NO_ID.name() );
      lArgs.add( lConfigSlotPositionKey, InvCndChgInvColumn.ASSMBL_DB_ID.name(),
            InvCndChgInvColumn.ASSMBL_CD.name(), InvCndChgInvColumn.ASSMBL_BOM_ID.name(),
            InvCndChgInvColumn.ASSMBL_POS_ID.name() );
      lArgs.add( null, InvCndChgInvColumn.PART_NO_DB_ID.name(),
            InvCndChgInvColumn.PART_NO_ID.name() );
      lArgs.add( null, InvCndChgInvColumn.BOM_PART_DB_ID.name(),
            InvCndChgInvColumn.BOM_PART_ID.name() );
      lArgs.add( InvCndChgInvColumn.MAIN_INV_BOOL.name(), 1 );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv", lArgs );

      lArgs.clear();
      lArgs.add( lEventInventoryKey, InvCndChgInvColumn.EVENT_DB_ID.name(),
            InvCndChgInvColumn.EVENT_ID.name(), InvCndChgInvColumn.EVENT_INV_ID.name() );

      // ACT
      MxDataAccess.getInstance().executeDelete( "inv_cnd_chg_inv", lArgs );

      // ASSERT
      EvtInvTable lEvtInvTable =
            EvtInvTable.findByPrimaryKey( new EventInventoryKey( lEventKey, 1 ) );

      assertFalse( "No data", lEvtInvTable.exists() );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_inv_usage which copy same data to table evt_inv_usge when
    * insert data into table inv_cnd_chg_inv_usage
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgInvUsageWithInsert() {
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();

      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lEventKey, 1 );
      DataTypeKey lDataTypeKey = new DataTypeKey( 0, 1 );
      EventInventoryUsageKey lEventInventoryUsageKey =
            new EventInventoryUsageKey( lEventInventoryKey, lDataTypeKey );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventInventoryKey, InvCndChgInvUsageColumn.EVENT_DB_ID.name(),
            InvCndChgInvUsageColumn.EVENT_ID.name(), InvCndChgInvUsageColumn.EVENT_INV_ID.name() );
      lArgs.add( lDataTypeKey, InvCndChgInvUsageColumn.DATA_TYPE_DB_ID.name(),
            InvCndChgInvUsageColumn.DATA_TYPE_ID.name() );
      lArgs.add( InvCndChgInvUsageColumn.TSN_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.TSO_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.TSI_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.ASSMBL_TSN_QT.name(), 20 );
      lArgs.add( InvCndChgInvUsageColumn.ASSMBL_TSO_QT.name(), 20 );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv_usage", lArgs );

      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( lEventInventoryUsageKey );
      double lExpectedTsnQt = 10;
      double lActualTsnQt = lEvtInvUsageTable.getTsnQt();
      double lExpectedTsoQt = 10;
      double lActualTsoQt = lEvtInvUsageTable.getTsoQt();
      double lExpectedTsiQt = 10;
      double lActualTsiQt = lEvtInvUsageTable.getTsiQt();
      double lExpectedAssmblTsnQt = 20;
      double lActualAssmblTsnQt = lEvtInvUsageTable.getAssmblTsnQt();
      double lExpectedAssmblTsoQt = 20;
      double lActualAssmblTsoQt = lEvtInvUsageTable.getAssmblTsoQt();

      assertTrue( lEvtInvUsageTable.exists() );

      assertEquals( "Incorrect tsn quantity.", lExpectedTsnQt, lActualTsnQt, 0.01 );
      assertEquals( "Incorrect tso quantity.", lExpectedTsoQt, lActualTsoQt, 0.01 );
      assertEquals( "Incorrect tsi quantity.", lExpectedTsiQt, lActualTsiQt, 0.01 );
      assertEquals( "Incorrect assembly tsn quantity.", lExpectedAssmblTsnQt, lActualAssmblTsnQt,
            0.01 );
      assertEquals( "Incorrect assembly tso quantity.", lExpectedAssmblTsoQt, lActualAssmblTsoQt,
            0.01 );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_inv_usage which update same data in table evt_inv_usge
    * when update data in table inv_cnd_chg_inv_usage
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgInvUsageWithUpdate() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();

      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lEventKey, 1 );
      DataTypeKey lDataTypeKey = new DataTypeKey( 0, 1 );
      EventInventoryUsageKey lEventInventoryUsageKey =
            new EventInventoryUsageKey( lEventInventoryKey, lDataTypeKey );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventInventoryKey, InvCndChgInvUsageColumn.EVENT_DB_ID.name(),
            InvCndChgInvUsageColumn.EVENT_ID.name(), InvCndChgInvUsageColumn.EVENT_INV_ID.name() );
      lArgs.add( lDataTypeKey, InvCndChgInvUsageColumn.DATA_TYPE_DB_ID.name(),
            InvCndChgInvUsageColumn.DATA_TYPE_ID.name() );
      lArgs.add( InvCndChgInvUsageColumn.TSN_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.TSO_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.TSI_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.ASSMBL_TSN_QT.name(), 20 );
      lArgs.add( InvCndChgInvUsageColumn.ASSMBL_TSO_QT.name(), 20 );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv_usage", lArgs );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lEventInventoryKey, InvCndChgInvUsageColumn.EVENT_DB_ID.name(),
            InvCndChgInvUsageColumn.EVENT_ID.name(), InvCndChgInvUsageColumn.EVENT_INV_ID.name() );
      DataSetArgument lSetArgs = new DataSetArgument();
      lSetArgs.add( InvCndChgInvUsageColumn.TSN_QT.name(), 12 );
      lSetArgs.add( InvCndChgInvUsageColumn.TSO_QT.name(), 12 );
      lSetArgs.add( InvCndChgInvUsageColumn.TSI_QT.name(), 12 );

      // ACT
      MxDataAccess.getInstance().executeUpdate( "inv_cnd_chg_inv_usage", lSetArgs, lWhereArgs );

      // ASSERT
      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( lEventInventoryUsageKey );
      double lExpectedTsnQt = 12;
      double lActualTsnQt = lEvtInvUsageTable.getTsnQt();
      double lExpectedTsoQt = 12;
      double lActualTsoQt = lEvtInvUsageTable.getTsoQt();
      double lExpectedTsiQt = 12;
      double lActualTsiQt = lEvtInvUsageTable.getTsiQt();

      assertTrue( lEvtInvUsageTable.exists() );

      assertEquals( "Incorrect tsn quantity.", lExpectedTsnQt, lActualTsnQt, 0.01 );
      assertEquals( "Incorrect tso quantity.", lExpectedTsoQt, lActualTsoQt, 0.01 );
      assertEquals( "Incorrect tsi quantity.", lExpectedTsiQt, lActualTsiQt, 0.01 );
   }


   /**
    * Tests the trigger tiudbr_inv_cnd_chg_inv_usage which deletes same data in table evt_inv_usge
    * when delete data in table inv_cnd_chg_inv_usage
    *
    */
   @Test
   public void testTriggerTiudbrInvCndChgInvUsageWithDelete() {
      // ARRANGE
      EvtEventDao iEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      EventKey lEventKey = iEvtEventDao.generatePrimaryKey();

      EventInventoryKey lEventInventoryKey = new EventInventoryKey( lEventKey, 1 );
      DataTypeKey lDataTypeKey = new DataTypeKey( 0, 1 );
      EventInventoryUsageKey lEventInventoryUsageKey =
            new EventInventoryUsageKey( lEventInventoryKey, lDataTypeKey );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lEventInventoryKey, InvCndChgInvUsageColumn.EVENT_DB_ID.name(),
            InvCndChgInvUsageColumn.EVENT_ID.name(), InvCndChgInvUsageColumn.EVENT_INV_ID.name() );
      lArgs.add( lDataTypeKey, InvCndChgInvUsageColumn.DATA_TYPE_DB_ID.name(),
            InvCndChgInvUsageColumn.DATA_TYPE_ID.name() );
      lArgs.add( InvCndChgInvUsageColumn.TSN_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.TSO_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.TSI_QT.name(), 10 );
      lArgs.add( InvCndChgInvUsageColumn.ASSMBL_TSN_QT.name(), 20 );
      lArgs.add( InvCndChgInvUsageColumn.ASSMBL_TSO_QT.name(), 20 );

      MxDataAccess.getInstance().executeInsert( "inv_cnd_chg_inv_usage", lArgs );

      lArgs.clear();
      lArgs.add( lEventInventoryKey, InvCndChgInvUsageColumn.EVENT_DB_ID.name(),
            InvCndChgInvUsageColumn.EVENT_ID.name(), InvCndChgInvUsageColumn.EVENT_INV_ID.name() );

      // ACT
      MxDataAccess.getInstance().executeDelete( "inv_cnd_chg_inv_usage", lArgs );

      // ASSERT
      EvtInvUsageTable lEvtInvUsageTable =
            EvtInvUsageTable.findByPrimaryKey( lEventInventoryUsageKey );

      assertFalse( lEvtInvUsageTable.exists() );
   }
}
