--liquibase formatted sql


--changeSet MX-25726:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_schema_pkg.table_column_add('
      Alter table SCHED_STASK add (
         "HIST_BOOL_RO" Number(1,0) Check (HIST_BOOL_RO IN (0, 1) ) DEFERRABLE 
      )
   ');
END;
/

--changeSet MX-25726:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_schema_pkg.table_column_add('
      Alter table EVT_SCHED_DEAD add (
         "HIST_BOOL_RO" Number(1,0) Check (HIST_BOOL_RO IN (0, 1) ) DEFERRABLE 
      )
   ');
END;
/

--changeSet MX-25726:3 stripComments:false
UPDATE
   sched_stask
SET
   hist_bool_ro = (
      SELECT
         evt_event.hist_bool
      FROM
         evt_event
      WHERE
         sched_stask.sched_db_id = evt_event.event_db_id AND
         sched_stask.sched_id    = evt_event.event_id
      )
WHERE
   hist_bool_ro IS NULL;      

--changeSet MX-25726:4 stripComments:false
UPDATE
   evt_sched_dead
SET
   hist_bool_ro = (
      SELECT 
         evt_event.hist_bool 
      FROM 
         evt_event 
      WHERE 
         evt_sched_dead.event_db_id = evt_event.event_db_id AND 
         evt_sched_dead.event_id    = evt_event.event_id
      )
WHERE
   hist_bool_ro IS NULL;

--changeSet MX-25726:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table SCHED_STASK modify (
         HIST_BOOL_RO Number(1,0) DEFAULT 0 NOT NULL DEFERRABLE  Check (HIST_BOOL_RO IN (0, 1) ) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-25726:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   
   utl_migr_schema_pkg.table_column_modify('
      Alter table EVT_SCHED_DEAD modify (
         HIST_BOOL_RO Number(1,0) DEFAULT 0 NOT NULL DEFERRABLE  Check (HIST_BOOL_RO IN (0, 1) ) DEFERRABLE
      )
   ');
END;
/   

--changeSet MX-25726:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_STASK_HIST_BOOL" BEFORE INSERT
   ON "SCHED_STASK" FOR EACH ROW
DECLARE   
   CURSOR lc_EvtEvent(
         an_EventDbId IN EVT_EVENT.EVENT_DB_ID%TYPE, 
         an_EventId IN EVT_EVENT.EVENT_ID%TYPE
      ) IS
      SELECT 
         evt_event.hist_bool
      FROM 
         evt_event 
      WHERE 
         evt_event.event_db_id = an_EventDbId AND 
         evt_event.event_id    = an_EventId;
   
   ln_HistBool EVT_EVENT.HIST_BOOL%TYPE;
   
BEGIN

   OPEN lc_EvtEvent(:new.sched_db_id, :new.sched_id);
   FETCH lc_EvtEvent INTO ln_HistBool ;
   
   IF lc_EvtEvent%FOUND THEN
      :new.hist_bool_ro := ln_HistBool;
   END IF;
   
   CLOSE lc_EvtEvent;
END;
/   

--changeSet MX-25726:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EVT_SCHED_DEAD_HIST_BOOL" BEFORE INSERT
   ON "EVT_SCHED_DEAD" FOR EACH ROW
DECLARE   
   CURSOR lc_EvtEvent(
         an_EventDbId IN EVT_EVENT.EVENT_DB_ID%TYPE, 
         an_EventId IN EVT_EVENT.EVENT_ID%TYPE
      ) IS
      SELECT 
         evt_event.hist_bool
      FROM 
         evt_event 
      WHERE 
         evt_event.event_db_id = an_EventDbId AND 
         evt_event.event_id    = an_EventId;
   
   ln_HistBool EVT_EVENT.HIST_BOOL%TYPE;
BEGIN
   OPEN lc_EvtEvent(:new.event_db_id, :new.event_id);
   FETCH lc_EvtEvent INTO ln_HistBool ;
   
   IF lc_EvtEvent%FOUND THEN
      :new.hist_bool_ro := ln_HistBool;
   END IF;
   
   CLOSE lc_EvtEvent;
END;
/   

--changeSet MX-25726:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIUAR_EVT_EVENT_HIST_BOOL" AFTER INSERT OR UPDATE OF "HIST_BOOL"
   ON "EVT_EVENT" FOR EACH ROW
BEGIN
   -- run trigger logic for tasks only
   IF :new.event_type_db_id = 0 AND :new.event_type_cd = 'TS' THEN
      UPDATE
         sched_stask
      SET
         sched_stask.hist_bool_ro = :new.hist_bool
      WHERE
         sched_stask.sched_db_id = :new.event_db_id AND
         sched_stask.sched_id    = :new.event_id;
      
      UPDATE
         evt_sched_dead
      SET
         evt_sched_dead.hist_bool_ro = :new.hist_bool
      WHERE
         evt_sched_dead.event_db_id = :new.event_db_id AND
         evt_sched_dead.event_id    = :new.event_id;
   END IF;
END;
/

--changeSet MX-25726:10 stripComments:false
/**
 * This view provides a table containing all of the driving 
 * deadlines and supplementery data for all non-historic tasks.
 */
CREATE OR REPLACE VIEW vw_drv_deadline
AS
SELECT
   evt_sched_dead.event_db_id,
   evt_sched_dead.event_id,
   evt_sched_dead.data_type_db_id,
   evt_sched_dead.data_type_id,
   evt_sched_dead.deviation_qt,
   evt_sched_dead.usage_rem_qt,
   evt_sched_dead.sched_dead_dt,
   mim_data_type.domain_type_db_id,
   mim_data_type.domain_type_cd,
   mim_data_type.data_type_cd,
   mim_data_type.entry_prec_qt,
   ref_eng_unit.eng_unit_db_id,
   ref_eng_unit.eng_unit_cd,
   ref_eng_unit.ref_mult_qt
FROM
   evt_sched_dead
   INNER JOIN mim_data_type ON
      mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
      mim_data_type.data_type_id    = evt_sched_dead.data_type_id
   INNER JOIN ref_eng_unit ON
      ref_eng_unit.eng_unit_db_id = mim_data_type.eng_unit_db_id AND
      ref_eng_unit.eng_unit_cd    = mim_data_type.eng_unit_cd
WHERE
   evt_sched_dead.hist_bool_ro = 0
   AND
   evt_sched_dead.sched_driver_bool = 1
;

--changeSet MX-25726:11 stripComments:false
/**
 * This view creates the full mapping of human resources to inventory over which they have authority.
 *
 * There are three possible cases:
 *  1. The HR has all authority and so has authority over the aircraft
 *  2. The aircraft has no authority defined and so the HR has authority over the aircraft
 *  3. The HR and aircraft have the same authority mapping and so the HR has authority over the aircraft
 */
CREATE OR REPLACE VIEW vw_inv_ac_authority
AS
-- inventory with an authority, hr with the same authority
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   inv_inv.authority_db_id,
   inv_inv.authority_id
FROM
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   INNER JOIN org_hr_authority ON
      org_hr_authority.authority_db_id = inv_inv.authority_db_id AND
      org_hr_authority.authority_id    = inv_inv.authority_id
   INNER JOIN org_hr ON
      org_hr.hr_db_id = org_hr_authority.hr_db_id AND
      org_hr.hr_id    = org_hr_authority.hr_id
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT'
   AND
   org_hr.all_authority_bool = 0
UNION ALL
-- inventory with no authority, all hr have authority (exclude those with all_authority_bool)
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   null AS authority_db_id,
   null AS authority_id
FROM
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   CROSS JOIN org_hr
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT'
   AND
   inv_inv.authority_db_id IS NULL
   AND
   org_hr.all_authority_bool = 0   
UNION ALL
-- hr with all authority have authority over all aircraft
SELECT
   inv_inv.inv_no_db_id,
   inv_inv.inv_no_id,
   org_hr.hr_db_id,
   org_hr.hr_id,
   inv_inv.authority_db_id,
   inv_inv.authority_id
FROM
   inv_ac_reg
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
      inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   CROSS JOIN org_hr
WHERE
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT'
   AND
   org_hr.all_authority_bool = 1
;