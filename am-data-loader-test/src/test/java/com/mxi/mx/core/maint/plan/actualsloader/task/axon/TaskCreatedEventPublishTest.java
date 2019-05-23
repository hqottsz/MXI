package com.mxi.mx.core.maint.plan.actualsloader.task.axon;

import static com.mxi.mx.util.TableUtil.AL_PROC_HIST_TASK;
import static com.mxi.mx.util.TableUtil.AL_PROC_TASKS;
import static com.mxi.mx.util.TableUtil.AXON_DOMAIN_EVENT_ENTRY_TABLE;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mxi.mx.core.maint.plan.datamodels.simpleIDs;
import com.mxi.mx.util.ActualsLoaderTest;
import com.mxi.mx.util.TableUtil;

import oracle.sql.RAW;


/**
 * This class is to test the publish of task created event via data loader.
 *
 */
public class TaskCreatedEventPublishTest extends ActualsLoaderTest {

   private static final String CYCLE_ID = UUID.randomUUID().toString().replace( "-", "" );
   private static final String TERMINATED_DATE = "to_date('2017/01/01','yyyy/mm/dd')";
   private static final String COMPLETION_DATE = "to_date('2017/01/01','yyyy/mm/dd')";

   private simpleIDs newCreatedTaskId;


   @Override
   @Before
   public void before() throws Exception {
      super.before();
      clearActualsLoaderTables();
      deleteAllFromTable( AXON_DOMAIN_EVENT_ENTRY_TABLE );
   }


   @Override
   @After
   public void after() throws Exception {
      deleteAllTaskTables( newCreatedTaskId );
      deleteAllFromTable( AXON_DOMAIN_EVENT_ENTRY_TABLE );
      super.after();
   }


   @Test
   @Ignore( "OPER-28629" )
   public void itPublishTaskCreatedEventWhenCreateActvTask() throws SQLException {

      // ARRANGE
      Map<String, String> procTaskMap = buildProcTaskMap();
      procTaskMap.put( "ACTV_EVENT_DB_ID", "'" + newCreatedTaskId.getNO_DB_ID() + "'" );
      procTaskMap.put( "ACTV_EVENT_ID", "'" + newCreatedTaskId.getNO_ID() + "'" );

      runInsert( TableUtil.getInsertForTableByMap( AL_PROC_TASKS, procTaskMap ) );
      runInsert( TableUtil.getInsertForTableByMap( AL_PROC_HIST_TASK, buildProcHistTaskMap() ) );

      // ACT
      PreparedStatement prepareCall = getConnection()
            .prepareCall( "BEGIN task_event_pkg.create_actv_task( ?, ?, ?, ?, ?); END;" );

      prepareCall.setBytes( 1, RAW.hexString2Bytes( CYCLE_ID ) );
      prepareCall.setInt( 2, 100542 );
      prepareCall.setInt( 3, 100544 );
      prepareCall.setInt( 4, 0 );
      prepareCall.setInt( 5, 0 );

      prepareCall.execute();
      commit();

      // ASSERT
      verifyTaskCreatedEventPublish();
   }


   @Test
   @Ignore( "OPER-28629" )
   public void itPublishTaskCreatedEventWhenCreateTerminatedTask() throws SQLException {

      // ARRANGE
      Map<String, String> procTaskMap = buildProcTaskMap();
      procTaskMap.put( "TERMINATED_DT", TERMINATED_DATE );
      procTaskMap.put( "TERMINATED_EVENT_DB_ID", "'" + newCreatedTaskId.getNO_DB_ID() + "'" );
      procTaskMap.put( "TERMINATED_EVENT_ID", "'" + newCreatedTaskId.getNO_ID() + "'" );

      runInsert( TableUtil.getInsertForTableByMap( AL_PROC_TASKS, procTaskMap ) );

      Map<String, String> procHistTaskMap = buildProcHistTaskMap();
      procHistTaskMap.put( "TERMINATED_DT", TERMINATED_DATE );

      runInsert( TableUtil.getInsertForTableByMap( AL_PROC_HIST_TASK, procHistTaskMap ) );

      // ACT
      PreparedStatement prepareCall = getConnection()
            .prepareCall( "BEGIN task_event_pkg.create_terminated_task( ?, ?, ?); END;" );

      byte[] cycleId = RAW.hexString2Bytes( CYCLE_ID );
      prepareCall.setBytes( 1, cycleId );
      prepareCall.setInt( 2, 100542 );
      prepareCall.setInt( 3, 100544 );

      prepareCall.execute();
      commit();

      // ASSERT
      verifyTaskCreatedEventPublish();
   }


   @Test
   public void itPublishTaskCreatedEventWhenCreateHistoricTask() throws SQLException {

      // ARRANGE
      Map<String, String> procTaskMap = buildProcTaskMap();
      procTaskMap.put( "COMPLETION_DATE", COMPLETION_DATE );
      procTaskMap.put( "HIST_EVENT_DB_ID", "'" + newCreatedTaskId.getNO_DB_ID() + "'" );
      procTaskMap.put( "HIST_EVENT_ID", "'" + newCreatedTaskId.getNO_ID() + "'" );

      runInsert( TableUtil.getInsertForTableByMap( AL_PROC_TASKS, procTaskMap ) );

      Map<String, String> procHistTaskMap = buildProcHistTaskMap();
      procHistTaskMap.put( "COMPLETION_DATE", COMPLETION_DATE );

      runInsert( TableUtil.getInsertForTableByMap( AL_PROC_HIST_TASK, procHistTaskMap ) );

      // ACT
      PreparedStatement prepareCall = getConnection()
            .prepareCall( "BEGIN task_event_pkg.create_historic_task( ?, ?, ?); END;" );

      byte[] cycleId = RAW.hexString2Bytes( CYCLE_ID );
      prepareCall.setBytes( 1, cycleId );
      prepareCall.setInt( 2, 100542 );
      prepareCall.setInt( 3, 100544 );

      prepareCall.execute();
      commit();

      // ASSERT
      verifyTaskCreatedEventPublish();
   }


   private Map<String, String> buildProcTaskMap() {

      newCreatedTaskId = new simpleIDs( "4650", "999999" );

      Map<String, String> procTaskMap = new LinkedHashMap<String, String>();

      procTaskMap.put( "CYCLE_ID", "'" + CYCLE_ID + "'" );
      procTaskMap.put( "RECORD_ID", "'1105'" );
      procTaskMap.put( "SERIAL_NO_OEM", "'SN000001'" );
      procTaskMap.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      procTaskMap.put( "MANUFACT_CD", "'10001'" );
      procTaskMap.put( "TASK_CD", "'AT_TEST'" );
      procTaskMap.put( "INV_NO_DB_ID", "'4650'" );
      procTaskMap.put( "INV_NO_ID", "'100543'" );
      procTaskMap.put( "ACFT_NO_DB_ID", "'4650'" );
      procTaskMap.put( "ACFT_NO_ID", "'100543'" );
      procTaskMap.put( "PART_NO_DB_ID", "'4650'" );
      procTaskMap.put( "PART_NO_ID", "'100000'" );
      procTaskMap.put( "TASK_DB_ID", "'4650'" );
      procTaskMap.put( "TASK_ID", "'100428'" );
      procTaskMap.put( "TASK_DEFN_DB_ID", "'4650'" );
      procTaskMap.put( "TASK_DEFN_ID", "'100428'" );
      procTaskMap.put( "FIRST_TIME_BOOL", "'1'" );

      return procTaskMap;
   }


   private Map<String, String> buildProcHistTaskMap() {
      Map<String, String> procHistTaskMap = new LinkedHashMap<String, String>();

      procHistTaskMap.put( "RECORD_ID", "'1105'" );
      procHistTaskMap.put( "CYCLE_ID", "'" + CYCLE_ID + "'" );
      procHistTaskMap.put( "SERIAL_NO_OEM", "'SN000001'" );
      procHistTaskMap.put( "PART_NO_OEM", "'ACFT_ASSY_PN1'" );
      procHistTaskMap.put( "MANUFACT_CD", "'10001'" );
      procHistTaskMap.put( "TASK_CD", "'AT_TEST'" );
      procHistTaskMap.put( "FIRST_TIME_BOOL", "'1'" );
      procHistTaskMap.put( "INV_NO_DB_ID", "'4650'" );
      procHistTaskMap.put( "INV_NO_ID", "'100543'" );
      procHistTaskMap.put( "INV_CLASS_CD", "'ACFT'" );
      procHistTaskMap.put( "ACFT_NO_DB_ID", "'4650'" );
      procHistTaskMap.put( "ACFT_NO_ID", "'100543'" );
      procHistTaskMap.put( "PART_NO_DB_ID", "'4650'" );
      procHistTaskMap.put( "PART_NO_ID", "'100000'" );
      procHistTaskMap.put( "ASSMBL_DB_ID", "'4650'" );
      procHistTaskMap.put( "ASSMBL_BOM_ID", "'0'" );
      procHistTaskMap.put( "TSK_ASSMBL_BOM_ID", "'0'" );
      procHistTaskMap.put( "INV_ASSMBL_BOM_ID", "'0'" );
      procHistTaskMap.put( "ASSMBL_CD", "'ACFT_CD1'" );
      procHistTaskMap.put( "INV_ASSMBL_CD", "'ACFT_CD1'" );
      procHistTaskMap.put( "TSK_ASSMBL_CD", "'ACFT_CD1'" );
      procHistTaskMap.put( "ASSMBL_INV_NO_DB_ID", "'4650'" );
      procHistTaskMap.put( "ASSMBL_INV_NO_ID", "'100543'" );
      procHistTaskMap.put( "ORIG_ASSMBL_CD", "'ACFT_CD1'" );
      procHistTaskMap.put( "APPL_EFF_CD", "'001'" );
      procHistTaskMap.put( "TASK_DEFN_DB_ID", "'4650'" );
      procHistTaskMap.put( "TASK_DEFN_ID", "'100428'" );
      procHistTaskMap.put( "TASK_DB_ID", "'4650'" );
      procHistTaskMap.put( "TASK_ID", "'100428'" );
      procHistTaskMap.put( "TASK_ERROR_FLAG", "'0'" );
      procHistTaskMap.put( "CLASS_MODE_CD", "'REQ'" );
      procHistTaskMap.put( "TASK_DEF_STATUS_CD", "'ACTV'" );
      procHistTaskMap.put( "RECURRING_TASK_BOOL", "'0'" );

      return procHistTaskMap;
   }

}
