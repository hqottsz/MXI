--liquibase formatted sql


--changeSet MX-12217:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_STASK add (
	"ORPHAN_FRCT_BOOL" Number(1,0) 
)
');
END;
/

--changeSet MX-12217:2 stripComments:false
UPDATE SCHED_STASK SET ORPHAN_FRCT_BOOL = 0 WHERE ORPHAN_FRCT_BOOL IS NULL;

--changeSet MX-12217:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_STASK modify (
	"ORPHAN_FRCT_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ORPHAN_FRCT_BOOL IN (0, 1) ) DEFERRABLE 
)
');
END;
/

--changeSet MX-12217:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION IsOrphanedForecastedTask(
      an_EventDbId IN evt_event.event_db_id%TYPE,
      an_EventId   IN evt_event.event_id%TYPE)
    RETURN NUMBER
  IS
    /* Returns 0 is the task does not belong to an orphaned chain of forecasted tasks */
    ln_OrphanedCount NUMBER;
    lv_Status        VARCHAR(20);
  BEGIN
    /* If the passed in Task is not Forecasted, then it cannot be orphaned */
    SELECT event_status_cd
    INTO lv_Status
    FROM evt_event
    WHERE event_db_id = an_EventDbId
    AND event_id      = an_EventId;
    IF lv_Status     <> 'FORECAST' THEN
      RETURN 0;
    END IF;
    /* check if this task is orphaned forecasted task by join sched_stask and evt_event tables and .
    * orphan_frct_bool is 1 in sched_stask table.
    */
    SELECT COUNT(*)
    INTO ln_OrphanedCount
    FROM
      (SELECT 1
      FROM sched_stask ss,
        evt_event ee
      WHERE ss.sched_db_id   = ee.event_db_id
      AND ss.sched_id        = ee.event_id
      AND ee.event_db_id     = an_EventDbId
      AND ee.event_id        = an_EventId
      AND ss.orphan_frct_bool=1
      );
    -- If forecasted orphaned tasks exist, then return true
    IF ln_OrphanedCount > 0 THEN
      RETURN 1;
    END IF;
    RETURN 0;
  END IsOrphanedForecastedTask;
/