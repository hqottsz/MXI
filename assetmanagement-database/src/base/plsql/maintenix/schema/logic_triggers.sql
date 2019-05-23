--liquibase formatted sql


--changeSet logic_triggers:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TDBR_INV_POS_DESC_QUEUE_DEL" BEFORE DELETE
   ON "INV_INV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  -- remove any row in the INV_POS_DESC_DEL before deleting from INV_INV.
  DELETE FROM INV_POS_DESC_QUEUE WHERE :OLD.inv_no_db_id=INV_POS_DESC_QUEUE.inv_no_db_id AND :OLD.inv_no_id = INV_POS_DESC_QUEUE.inv_no_id;

  EXCEPTION
      WHEN OTHERS THEN
           APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'TDBR_INV_POS_DESC_QUEUE_DEL:' || SQLERRM);
           RAISE;
end;
/

--changeSet logic_triggers:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_JOB" BEFORE INSERT
   ON "UTL_JOB" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
begin
  :new.schedule_date := sysdate;
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_JOB" BEFORE UPDATE
   ON "UTL_JOB" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
begin
  /* Only update the Schedule Date if a scheduling parameter changes */
  IF (:new.start_time <> :old.start_time) OR (:new.repeat_interval <> :old.repeat_interval) OR
     (:new.start_delay <> :old.start_delay)
  THEN
     :new.schedule_date := sysdate;
  END IF;
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_ORG" BEFORE UPDATE
   ON "ORG_ORG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;

   -- Get Parents of current node
   CURSOR cur_node_parent IS
         SELECT org_suborg_cache.org_db_id,org_suborg_cache.org_id
         FROM   org_suborg_cache
         WHERE  org_suborg_cache.sub_org_db_id = :OLD.org_db_id
         AND    org_suborg_cache.sub_org_id    = :OLD.org_id;

  -- Get Childs of current node
   CURSOR cur_node_child IS
         SELECT org_suborg_cache.sub_org_db_id,org_suborg_cache.sub_org_id
         FROM   org_suborg_cache
         WHERE  org_suborg_cache.org_db_id = :OLD.org_db_id
         AND    org_suborg_cache.org_id    = :OLD.org_id;

   -- Get Parents of New Node
   CURSOR new_node_parent IS
         SELECT org_suborg_cache.org_db_id,org_suborg_cache.org_id
         FROM   org_suborg_cache
         WHERE  org_suborg_cache.sub_org_db_id = :NEW.nh_org_db_id
         AND    org_suborg_cache.sub_org_id    = :NEW.nh_org_id;

begin

  IF ((:NEW.nh_org_db_id != :OLD.nh_org_db_id) OR (:NEW.nh_org_id != :OLD.nh_org_id)) THEN

  FOR lcur_node_parent IN cur_node_parent
  LOOP
     FOR lcur_node_child IN cur_node_child
     LOOP
        -- Break link between current node's parents and current node's childs in cache
        DELETE FROM ORG_SUBORG_CACHE
        WHERE
           ORG_SUBORG_CACHE.ORG_DB_ID        = lcur_node_parent.org_db_id  AND
           ORG_SUBORG_CACHE.ORG_ID           = lcur_node_parent.org_id     AND
           ORG_SUBORG_CACHE.SUB_ORG_DB_ID    = lcur_node_child.sub_org_db_id  AND
           ORG_SUBORG_CACHE.SUB_ORG_ID       = lcur_node_child.sub_org_id ;
     END LOOP;

        -- Break link between current node's parents and current node
        DELETE FROM ORG_SUBORG_CACHE
        WHERE
           ORG_SUBORG_CACHE.ORG_DB_ID        = lcur_node_parent.org_db_id  AND
           ORG_SUBORG_CACHE.ORG_ID           = lcur_node_parent.org_id     AND
           ORG_SUBORG_CACHE.SUB_ORG_DB_ID    = :OLD.org_db_id  AND
           ORG_SUBORG_CACHE.SUB_ORG_ID       = :OLD.org_id;

  END LOOP;



 FOR lnew_node_parent IN new_node_parent
  LOOP
     FOR lcur_node_child IN cur_node_child
     LOOP
        -- Create link between new node's parents and current node's childs
        INSERT INTO ORG_SUBORG_CACHE(ORG_SUBORG_CACHE.ORG_DB_ID,ORG_SUBORG_CACHE.ORG_ID ,ORG_SUBORG_CACHE.SUB_ORG_DB_ID,ORG_SUBORG_CACHE.SUB_ORG_ID)
        VALUES (lnew_node_parent.org_db_id,lnew_node_parent.org_id,lcur_node_child.sub_org_db_id,lcur_node_child.sub_org_id) ;

     END LOOP;
        -- Create link between new node's parents and current node
        INSERT INTO ORG_SUBORG_CACHE(ORG_SUBORG_CACHE.ORG_DB_ID,ORG_SUBORG_CACHE.ORG_ID ,ORG_SUBORG_CACHE.SUB_ORG_DB_ID,ORG_SUBORG_CACHE.SUB_ORG_ID)
        VALUES (lnew_node_parent.org_db_id,lnew_node_parent.org_id,:OLD.org_db_id,:OLD.org_id) ;

  END LOOP;

 FOR lcur_node_child IN cur_node_child
 LOOP
    -- Create link between new node and current node's childs
    INSERT INTO ORG_SUBORG_CACHE(ORG_SUBORG_CACHE.ORG_DB_ID,ORG_SUBORG_CACHE.ORG_ID ,ORG_SUBORG_CACHE.SUB_ORG_DB_ID,ORG_SUBORG_CACHE.SUB_ORG_ID)
    VALUES (:NEW.nh_org_db_id,:NEW.nh_org_id,lcur_node_child.sub_org_db_id,lcur_node_child.sub_org_id) ;

 END LOOP;

  -- Create link between new node and current node
  INSERT INTO ORG_SUBORG_CACHE(ORG_SUBORG_CACHE.ORG_DB_ID,ORG_SUBORG_CACHE.ORG_ID ,ORG_SUBORG_CACHE.SUB_ORG_DB_ID,ORG_SUBORG_CACHE.SUB_ORG_ID)
  VALUES (:NEW.nh_org_db_id,:NEW.nh_org_id,:OLD.org_db_id,:OLD.org_id) ;

  END IF;

  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_EQP_PART_VENDOR_ADVSRY" BEFORE INSERT
   ON "EQP_PART_VENDOR_ADVSRY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
  totalFound NUMBER := 0;
  CURSOR c1
  IS
  SELECT COUNT(numFound) AS totalNumFound
    FROM
    (
      SELECT 1 AS numFound FROM DUAL WHERE EXISTS
      (
        SELECT 1
          FROM eqp_part_vendor
         WHERE vendor_db_id = :NEW.vendor_db_id
           AND vendor_id = :NEW.vendor_id
           AND part_no_db_id = :NEW.part_no_db_id
           AND part_no_id = :NEW.part_no_id
      )
      UNION
      SELECT 1 AS numFound FROM DUAL WHERE EXISTS
      (
        SELECT 1
          FROM eqp_part_vendor_rep
         WHERE vendor_db_id = :NEW.vendor_db_id
           AND vendor_id = :NEW.vendor_id
           AND part_no_db_id = :NEW.part_no_db_id
           AND part_no_id = :NEW.part_no_id
      )
      UNION
      SELECT 1 AS numFound FROM DUAL WHERE EXISTS
      (
        SELECT 1
          FROM eqp_part_vendor_exchg
         WHERE vendor_db_id = :NEW.vendor_db_id
           AND vendor_id = :NEW.vendor_id
           AND part_no_db_id = :NEW.part_no_db_id
           AND part_no_id = :NEW.part_no_id
      )
    );
begin
  -- Al Hogan - 16 July 2009
  -- Refer to concept document 0909 Boeing CRs Part 1 vers 1.5 (Part Number Advisory - ERD)
  --
  -- The feature requires a constraint on the EQP_PART_VENDOR_ADVSRY
  -- which acts like a foreign key referring to the PK of only one of the tables
  -- EQP_PART_VENDOR, EQP_PART_VENDOR_REP, or EQP_PART_VENDOR_EXCHG.
  --
  -- Since this can not be done directly via constaints, we have to enforce the
  -- relationship via this "insert before" trigger.
  -- Prior to inserting we will check that the vendor+partNo exists in at least one of
  -- the three tables (listed above).  If not, then we will raise an user-defined
  -- oracle exception (between -20000 and -20999).  Since Maintenix does not currently
  -- have a way of managing user-defined oracle exceptions (there are not many) I
  -- simply picked a number.  This should be alright as we only need to indicate it
  -- is user-defined.
  --
  OPEN c1;

  FETCH c1
   INTO totalFound;

  CLOSE c1;

  IF (totalFound = 0)
  THEN
     RAISE_APPLICATION_ERROR (-20222,'Vendor/PartNumber must exist in either eqp_part_vendor, eqp_part_vendor_rep, or eqp_part_vendor_exchg');
  END IF;

  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TDBR_TASK_TASK_FLAG_DEL" BEFORE DELETE
   ON "TASK_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin

  -- remove any row in the TASK_TASK_FLAGS before deleting from TASK_TASK.
  DELETE FROM TASK_TASK_FLAGS WHERE :OLD.task_db_id=TASK_TASK_FLAGS.task_db_id AND :OLD.task_id = TASK_TASK_FLAGS.task_id;

  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIAF_TASK_TASK_FLAG_INSRT" AFTER INSERT
   ON "TASK_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin

  -- create a record in the flags for newly created tasks.
  INSERT INTO TASK_TASK_FLAGS (TASK_DB_ID, TASK_ID)
  VALUES (:NEW.task_db_id, :NEW.task_id);

  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;

end;
/

--changeSet logic_triggers:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TDBR_SCHED_STASK_FLAGS_DEL" BEFORE DELETE
   ON "SCHED_STASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin

  -- remove any row in the SCHED_STASK_FLAGS before deleting from SCHED_STASK.
  DELETE FROM SCHED_STASK_FLAGS WHERE :OLD.sched_db_id=SCHED_STASK_FLAGS.sched_db_id AND :OLD.sched_id = SCHED_STASK_FLAGS.sched_id;

  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIAF_SCHED_STASK_FLAGS_INSRT" AFTER INSERT
   ON "SCHED_STASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin

  -- create a record in the flags for newly created tasks.
  INSERT INTO SCHED_STASK_FLAGS (SCHED_DB_ID, SCHED_ID)
  VALUES (:NEW.sched_db_id, :NEW.sched_id);

  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;

end;
/

--changeSet logic_triggers:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TDBR_SCHED_WP_DEL" BEFORE DELETE
   ON "SCHED_STASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin

  -- remove any row in the SCHED_WP before deleting from SCHED_STASK
  DELETE FROM SCHED_WP WHERE :OLD.sched_db_id=SCHED_WP.sched_db_id AND :OLD.sched_id = SCHED_WP.sched_id;

  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet logic_triggers:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIAF_SCHED_WP_INSRT" AFTER INSERT
   ON "SCHED_STASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  lv_is_component_wp VARCHAR2(10) := 'false';
  ln_enforce_nsv NUMBER := 0;
  audit_error EXCEPTION;
begin

  -- create a record in the sched_wp for newly created WORK PACKAGE tasks.
  IF(:NEW.task_class_db_id = 0 AND :NEW.task_class_cd IN ('CHECK', 'RO')) THEN

    BEGIN
      -- get the inv class of the main inv to determine if this is a component wp or not
      -- (component wp are against a non-aircraft inv)
      SELECT
        DECODE(inv_class_db_id || ':' || inv_class_cd, '0:ACFT', 'false', 'true')
      INTO
        lv_is_component_wp
      FROM
        inv_inv
      WHERE
        inv_no_db_id = :NEW.main_inv_no_db_id AND
        inv_no_id    = :NEW.main_inv_no_id
      ;
    EXCEPTION
      -- if main inv does not exists then re throw with a more meaningful msg
      WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20000, 'Inserting into SCHED_STASK (PK ' || :NEW.sched_db_id || ':' || :NEW.sched_id || ') requires the main inventory to exist in INV_INV.', TRUE);
    END;

    -- set the enforce_nsv to 1 for component wp, otherwise 0
    IF lv_is_component_wp = 'true' THEN
      ln_enforce_nsv := 1;
    END IF;

    -- insert a corresponding sched_wp row
    INSERT INTO SCHED_WP (SCHED_DB_ID, SCHED_ID, ENFORCE_NSV)
    VALUES (:NEW.sched_db_id, :NEW.sched_id, ln_enforce_nsv);

  END IF;

  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;

end;
/

--changeSet logic_triggers:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- After update trigger on INV_INV to ensure that inventory conditions changes of records
-- that are assigned to a kit will cause the kit to be resynchronized.
CREATE OR REPLACE TRIGGER "TUAF_INV_INV" AFTER UPDATE
OF inv_cond_db_id, inv_cond_cd
ON inv_inv
REFERENCING NEW AS newRow OLD AS oldRow
   FOR EACH ROW
   WHEN ((newRow.inv_cond_cd != oldRow.inv_cond_cd) OR (newRow.inv_cond_db_id != oldRow.inv_cond_db_id))
   DECLARE
      lKitInvNoDbId           inv_inv.inv_no_db_id%TYPE;
      lKitInvNoId             inv_inv.inv_no_id%TYPE;
      lReturn                 NUMBER;
      kit_enqueue_exception   EXCEPTION;
   BEGIN
      -- get the kit for the inventory
      SELECT
         kit_inv_no_db_id,
         kit_inv_no_id
      INTO
         lKitInvNoDbId,
         lKitInvNoId
      FROM
         inv_kit_map
      WHERE
         inv_kit_map.inv_no_db_id = :newRow.inv_no_db_id AND
         inv_kit_map.inv_no_id = :newRow.inv_no_id;

      -- enqueue the kit for synchronization
      kit_pkg.enqueueInventory(lKitInvNoDbId, lKitInvNoId, lReturn);

      IF(lReturn = -1) THEN
         RAISE kit_enqueue_exception;
      END IF;

   EXCEPTION
      -- if there is no kit, then do nothing
      WHEN no_data_found THEN
         RETURN;
      WHEN kit_enqueue_exception THEN
         APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'TUAF_INV_INV:' || SQLERRM);
         RAISE;
END "TUAF_INV_INV";
/

--changeSet logic_triggers:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_SUBORG_CACHE_SP_INSRT" BEFORE INSERT
ON "ORG_ORG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
WHEN (NEW.nh_org_db_id IS NOT NULL AND NEW.nh_org_id IS NOT NULL)
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
   -- create records in the ORG_SUBORG_CACHE_SP for newly created ORG_ORG.
   INSERT INTO ORG_SUBORG_CACHE_SP (ORG_DB_ID, ORG_ID, CHILD_DB_ID, CHILD_ID)
   SELECT ORG_DB_ID, ORG_ID , :NEW.org_db_id , :NEW.org_id
   FROM ORG_ORG
   START WITH
      (ORG_DB_ID = :NEW.nh_org_db_id AND
       ORG_ID    = :NEW.nh_org_id)
   CONNECT BY
     PRIOR NH_ORG_DB_ID =   ORG_DB_ID AND
     PRIOR NH_ORG_ID    =   ORG_ID;

    IF ln_error <> 0 THEN
      RAISE_APPLICATION_ERROR (ln_error, lv_error);
    END IF;
END;
/

--changeSet logic_triggers:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIAF_ORG_SUBORG_CACHE_INSRT" AFTER INSERT
ON "ORG_ORG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
WHEN (NEW.nh_org_db_id IS NOT NULL AND NEW.nh_org_id IS NOT NULL)
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
    -- create records in the ORG_SUBORG_CACHE for newly created ORG_ORG from ORG_SUBORG_CACHE_SP
   INSERT INTO ORG_SUBORG_CACHE (ORG_DB_ID, ORG_ID,SUB_ORG_DB_ID,SUB_ORG_ID)
   SELECT ORG_DB_ID, ORG_ID, CHILD_DB_ID, CHILD_ID
   FROM ORG_SUBORG_CACHE_SP
   WHERE ORG_SUBORG_CACHE_SP.CHILD_DB_ID =  :NEW.org_db_id AND
         ORG_SUBORG_CACHE_SP.CHILD_ID    =  :NEW.org_id;

   DELETE FROM ORG_SUBORG_CACHE_SP
   WHERE ORG_SUBORG_CACHE_SP.CHILD_DB_ID =  :NEW.org_db_id AND
         ORG_SUBORG_CACHE_SP.CHILD_ID    =  :NEW.org_id;

    IF ln_error <> 0 THEN
      RAISE_APPLICATION_ERROR (ln_error, lv_error);
    END IF;
END;
/

--changeSet logic_triggers:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TDBR_ORG_SUBORG_CACHE_DEL" BEFORE DELETE
ON "ORG_ORG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
  DELETE FROM ORG_SUBORG_CACHE WHERE
  ORG_SUBORG_CACHE.org_db_id = :OLD.org_db_id AND
  ORG_SUBORG_CACHE.org_id    = :OLD.org_id;

  DELETE FROM ORG_SUBORG_CACHE WHERE
  ORG_SUBORG_CACHE.sub_org_db_id = :OLD.org_db_id AND
  ORG_SUBORG_CACHE.sub_org_id    = :OLD.org_id;


  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
END;
/

--changeSet logic_triggers:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TAUR_UTL_JOB" AFTER INSERT OR UPDATE OR DELETE
     ON utl_job FOR EACH ROW

when (NEW.JOB_TYPE = 'ORACLE' OR OLD.JOB_TYPE = 'ORACLE')
DECLARE

   lv_start_date            all_scheduler_schedules.start_date%TYPE;
   lv_repeat_interval       all_scheduler_schedules.repeat_interval%TYPE;
   lv_plsql_tab             alert_pkg.gt_tab_alert_parm;
   lv_current_active_status NUMBER;
   lv_job_exist             NUMBER := 1;
   lv_schedule_exist        NUMBER := 1;
   lv_schedule_modified     BOOLEAN := FALSE;
   v_state                  VARCHAR2(30);
   v_repeat_expression      VARCHAR2(200);

   v_step     NUMBER;
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);
   gv_err_msg_gen    CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred details are as follows: ';
   gc_ex_utl_job_err CONSTANT NUMBER := -20102;
   gex_utl_job_err EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_utl_job_err,
                         -20102);

   gex_cannot_modify_err EXCEPTION;

   PRAGMA AUTONOMOUS_TRANSACTION;

   CURSOR cur_get_job_status(p_job_cd IN utl_job.job_cd%TYPE) IS
      SELECT CASE
                WHEN enabled = 'TRUE' THEN
                 1
                ELSE
                 0
             END AS enabled,
             COUNT(*) AS exist
        INTO lv_current_active_status,
             lv_job_exist
        FROM all_scheduler_jobs
       WHERE upper(job_name) = upper(jobs_admin_pkg.get_job_name(p_job_cd))
       GROUP BY enabled,
                job_name;

   CURSOR cur_get_schedule_info(p_job_cd IN utl_job.job_cd%TYPE) IS
      SELECT start_date,
             repeat_interval,
             COUNT(*) AS exist
        INTO lv_start_date,
             lv_repeat_interval,
             lv_schedule_exist
        FROM all_scheduler_schedules
       WHERE schedule_name = p_job_cd
       GROUP BY repeat_interval,
                start_date,
                schedule_name;

BEGIN

   -- Check if job is being deleted if so remove schedule and job
   v_step := 10;
   IF deleting
   THEN
      jobs_admin_pkg.drop_job_and_schedule(p_job_cd     => :OLD.job_cd,
                                           p_force_bool => TRUE);
      RETURN;
   END IF;

   -- Set alert plsql table values
   v_step := 20;
   lv_plsql_tab.DELETE;
   lv_plsql_tab(1).parm_ord := 1;
   lv_plsql_tab(1).parm_type := 'STRING';
   lv_plsql_tab(1).parm_value_sdesc := 'schedule_name';
   lv_plsql_tab(1).parm_value := :OLD.job_cd;

   -- Check whether job currently exists and what status it is in
   v_step := 30;
   OPEN cur_get_job_status(:OLD.job_cd);
   FETCH cur_get_job_status
      INTO lv_current_active_status, lv_job_exist;
   IF cur_get_job_status%NOTFOUND
   THEN
      lv_job_exist := 0;
   END IF;
   CLOSE cur_get_job_status;

   -- Get the new repeat expression
   v_repeat_expression := jobs_admin_pkg.get_repeat_expression(p_start_time        => :NEW.start_time,
                                                               p_repeat_interval   => :NEW.repeat_interval,
                                                               p_repeat_expression => :NEW.repeat_expression);

   -- Check if oracle jobs needs to be enabled/disabled based on the updated
   -- value in the utl_job table
   v_step := 40;
   IF lv_job_exist = 1
   THEN
      -- Check if trying to update job_cd, program_name or program_type
      -- None of these can be changed without dropping and recreating job
      v_step := 45;
      IF updating
      THEN
         IF :NEW.job_cd != :OLD.job_cd
         THEN
            RAISE gex_cannot_modify_err;
         ELSIF :NEW.program_name != :OLD.program_name
         THEN
            RAISE gex_cannot_modify_err;
         ELSIF :NEW.program_type != :OLD.program_type
         THEN
            RAISE gex_cannot_modify_err;
         END IF;
      END IF;

      -- Enable or Disabe based on active bool
      IF :NEW.active_bool != lv_current_active_status
      THEN
         IF :NEW.active_bool = 1
         THEN
            jobs_admin_pkg.enable_job(:OLD.job_cd);
            v_state := 'Enabled';
         ELSE
            jobs_admin_pkg.disable_job(:OLD.job_cd);
            v_state := 'Disabled';
         END IF;
      ELSE
         -- Raise alert to indicate job has been synched
         v_step := 45;
         lv_plsql_tab(1).parm_value := ' Schedule for job ' || :OLD.job_cd ||
                                       ' synchronsied.';
         alert_pkg.raise_alert(p_alert_type_id  => 210,
                               p_alert_priority => 0,
                               p_alert_parm_tb  => lv_plsql_tab);
      END IF;

      -- Raise alert to indicate job has been enabled
      v_step := 50;
      lv_plsql_tab(1).parm_value := ' Schedule for job ' || :OLD.job_cd || ' ' ||
                                    v_state;
      alert_pkg.raise_alert(p_alert_type_id  => 210,
                            p_alert_priority => 0,
                            p_alert_parm_tb  => lv_plsql_tab);
   ELSE
      -- If the job does not exist then we need to create the job and schedule
      v_step := 55;
      jobs_admin_pkg.create_job_and_schedule(:NEW.job_cd,
                                             nvl(:NEW.schedule_date,
                                                 SYSDATE),
                                             v_repeat_expression,
                                             :NEW.program_type,
                                             :NEW.program_name,
                                             :NEW.active_bool);

   END IF;

   -- The next section of the trigger aims to ensure when updating the scheduler information
   -- is correct in the oracle job engine

   -- This only need to be done if we are updating
   IF updating
   THEN
      -- Get schedule information
      v_step := 60;
      OPEN cur_get_schedule_info(:OLD.job_cd);
      FETCH cur_get_schedule_info
         INTO lv_start_date, lv_repeat_interval, lv_schedule_exist;
      IF cur_get_schedule_info%NOTFOUND
      THEN
         lv_schedule_exist := 0;
      END IF;
      CLOSE cur_get_schedule_info;

      -- Modify scheduler
      v_step := 70;
      IF lv_schedule_exist = 1
      THEN

         -- Set new repeat expression
         v_step := 80;
         IF v_repeat_expression != lv_repeat_interval
         THEN
            dbms_scheduler.set_attribute(:OLD.job_cd,
                                         'repeat_interval',
                                         v_repeat_expression);
            lv_schedule_modified := TRUE;
         END IF;

         -- Set start date
         v_step := 85;
         IF :NEW.schedule_date != lv_start_date
         THEN
            dbms_scheduler.set_attribute(:OLD.job_cd,
                                         'start_date',
                                         :NEW.schedule_date);
            lv_schedule_modified := TRUE;
         END IF;

         v_step := 90;
         IF lv_schedule_modified = TRUE
         THEN
            lv_plsql_tab(1).parm_value := ' Schedule for job ' || :OLD.job_cd ||
                                          ' modified.';
            alert_pkg.raise_alert(p_alert_type_id  => 210,
                                  p_alert_priority => 0,
                                  p_alert_parm_tb  => lv_plsql_tab);
         END IF;
      END IF;
   END IF;

   COMMIT;

EXCEPTION
   WHEN gex_cannot_modify_err THEN
      v_err_msg := 'Cannot update program_name, program_type or job code name once job exists.';
      raise_application_error(gc_ex_utl_job_err,
                              substr('Trigger TAUR_UTL_JOB : ' || v_err_msg,
                                     1,
                                     2000),
                              TRUE);
   WHEN OTHERS THEN
      v_err_code := SQLCODE;
      v_err_msg  := SQLERRM;
      raise_application_error(gc_ex_utl_job_err,
                              substr('Trigger TAUR_UTL_JOB : ' ||
                                     gv_err_msg_gen || v_err_code || ' ' ||
                                     v_err_msg || ' at Step: ' || v_step,
                                     1,
                                     2000),
                              TRUE);
END;
/

--changeSet logic_triggers:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet logic_triggers:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet logic_triggers:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUAR_EVT_EVENT_HIST_BOOL" AFTER UPDATE OF "HIST_BOOL"
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

--changeSet logic_triggers:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_FL_LEG_RECORDED_DT" BEFORE INSERT
   ON "FL_LEG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

   IF :new.recorded_dt IS NULL THEN
      :new.recorded_dt := SYSDATE;
   END IF;

END;
/

--changeSet logic_triggers:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_USG_USAGE_RECORDED_DT" BEFORE INSERT
   ON "USG_USAGE_RECORD" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

   IF :new.recorded_dt IS NULL THEN
      :new.recorded_dt := SYSDATE;
   END IF;

END;
/

--changeSet logic_triggers:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- com.mxi.mx.common.cache.ConfigCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_CONFIG_PARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_CONFIG_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update CacheFactory.CONFIG_GLOBAL_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','Global');
   END AFTER STATEMENT;
END "TTRK_UTL_CONFIG_PARM_CACHE";
/

--changeSet logic_triggers:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ROLE_PARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ROLE_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_ROLE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','Role');
   END AFTER STATEMENT;
END "TTRK_UTL_ROLE_PARM_CACHE";
/

--changeSet logic_triggers:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_USER_PARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_USER_PARM"
   COMPOUND TRIGGER
   AFTER EACH ROW IS
   BEGIN
      -- Update  CacheFactory.CONFIG_USER_NODE_NAME cache timestamp
      CASE
      WHEN INSERTING THEN
         -- Ignore Session Parm Changes as they are not configuration
         IF (:NEW.PARM_TYPE <> 'SESSION') THEN
            UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','User');
         END IF;
      WHEN UPDATING THEN
         -- Ignore Session Parm Changes as they are not configuration
         IF (:NEW.PARM_TYPE <> 'SESSION') THEN
            UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','User');
         END IF;
      WHEN DELETING THEN
         -- Ignore Session Parm Changes as they are not configuration
         IF (:OLD.PARM_TYPE <> 'SESSION') THEN
            UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','User');
         END IF;
      END CASE;
   END AFTER EACH ROW;
END "TTRK_UTL_USER_PARM_CACHE";
/

--changeSet logic_triggers:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ACTION_CONFIG_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ACTION_CONFIG_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_GLOBAL_ACTION_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','GlobalAction');
   END AFTER STATEMENT;
END "TTRK_UTL_ACTION_CONFIG_CACHE";
/

--changeSet logic_triggers:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ACTIONROLEPARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ACTION_ROLE_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_ROLE_ACTION_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','RoleAction');
   END AFTER STATEMENT;
END "TTRK_UTL_ACTIONROLEPARM_CACHE";
/

--changeSet logic_triggers:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ACTIONUSERPARM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ACTION_USER_PARM"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.CONFIG_USER_ACTION_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('Config','UserAction');
   END AFTER STATEMENT;
END "TTRK_UTL_ACTIONUSERPARM_CACHE";
/

--changeSet logic_triggers:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.common.alert.AlertEngineCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_ALERT_TYPE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ALERT_TYPE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.ALERT_TYPE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertTypes');
      -- Update  CacheFactory.ALERT_NOTIFICATION_RULE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertNotificationRules');
      -- Update  CacheFactory.ALERT_PRIORITY_CALCULATOR_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertPriorityCalculators');
   END AFTER STATEMENT;
END "TTRK_UTL_ALERT_TYPE_CACHE";
/

--changeSet logic_triggers:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_ALERT_TYPE_ROLE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_ALERT_TYPE_ROLE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.ALERT_TYPE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_NODE('AlertEngine','AlertTypes');
   END AFTER STATEMENT;
END "TTRK_UTL_ALERT_TYPE_ROLE_CACHE";
/

--changeSet logic_triggers:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.common.timezone.TimeZoneCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_TIMEZONE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_TIMEZONE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.TIME_ZONE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Timezone');
   END AFTER STATEMENT;
END "TTRK_UTL_TIMEZONE_CACHE";
/

--changeSet logic_triggers:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.core.timezone.LocationTimeZoneCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_INV_LOC_TIMEZONECD_CACHE"
   FOR INSERT OR UPDATE OR DELETE OF TIMEZONE_CD ON "INV_LOC"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.LOCATION_TIME_ZONE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('LocationTimeZones');
   END AFTER STATEMENT;
END "TTRK_INV_LOC_TIMEZONECD_CACHE";
/


--changeSet logic_triggers:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.integration.services.transport.logging.LoggingCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_INT_BP_LOOKUP_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "INT_BP_LOOKUP"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.BP_PROCESS_LOG_MAP_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('BPProcessLogMap');
   END AFTER STATEMENT;
END "TTRK_INT_BP_LOOKUP_CACHE";
/

--changeSet logic_triggers:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--com.mxi.mx.common.report.ReportTypeCacheLoader
CREATE OR REPLACE TRIGGER "TTRK_UTL_REPORT_TYPE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_REPORT_TYPE"
   COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.REPORT_TYPE_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('ReportType');
   END AFTER STATEMENT;
END "TTRK_UTL_REPORT_TYPE_CACHE";
/
--changeSet logic_triggers:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_USER_ROLE_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_USER_ROLE"

COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.MENU_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Menu');
   END AFTER STATEMENT;
END "TTRK_UTL_USER_ROLE_CACHE";
/

--changeSet logic_triggers:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_MENU_GROUP_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_MENU_GROUP"

COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.MENU_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Menu');
   END AFTER STATEMENT;
END "TTRK_UTL_MENU_GROUP_CACHE";
/

--changeSet logic_triggers:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_MENU_GROUP_ITEM_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_MENU_GROUP_ITEM"

COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.MENU_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Menu');
   END AFTER STATEMENT;
END "TTRK_UTL_MENU_GROUP_ITEM_CACHE";
/

--changeSet logic_triggers:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_MENU_ITEM_ARG_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_MENU_ITEM_ARG"

COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.MENU_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Menu');
   END AFTER STATEMENT;
END "TTRK_UTL_MENU_ITEM_ARG_CACHE";
/

--changeSet logic_triggers:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TTRK_UTL_MENU_ARG_CACHE"
   FOR INSERT OR UPDATE OR DELETE ON "UTL_MENU_ARG"

COMPOUND TRIGGER
   AFTER STATEMENT IS
   BEGIN
      -- Update  CacheFactory.MENU_NODE_NAME cache timestamp
      UTL_CACHE_PKG.UPDATE_CACHE_TREE_ROOT_NODE('Menu');
   END AFTER STATEMENT;
END "TTRK_UTL_MENU_ARG_CACHE";
/

--changeSet logic_triggers:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment trigger to update materialized data as part of enhanced part search OPER-21136
CREATE OR REPLACE TRIGGER TIUDAR_MT_ENH_BOM_APL_LOG
AFTER
UPDATE OR INSERT OR DELETE ON EQP_BOM_PART
FOR EACH ROW
DECLARE
BEGIN
	IF (inserting or updating)
	THEN
		INSERT INTO MT_ENH_BOM_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 UD_DT
		)
		VALUES
		(
			 :NEW.BOM_PART_DB_ID,
			 :NEW.BOM_PART_ID,
			 SYSDATE
		);
	RETURN;
	END IF;

	IF (deleting)
	THEN
		INSERT INTO MT_ENH_BOM_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 UD_DT
		)
		VALUES
		(
			 :OLD.BOM_PART_DB_ID,
			 :OLD.BOM_PART_ID,
			 SYSDATE
		);
	END IF;
END;
/

--changeSet logic_triggers:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment trigger to update materialized data as part of enhanced part search OPER-21136
CREATE OR REPLACE TRIGGER TIUDAR_MT_ENH_PRT_APL_LOG
AFTER
UPDATE OR INSERT OR DELETE ON EQP_PART_BASELINE
FOR EACH ROW
DECLARE
BEGIN
	IF (inserting or updating)
	THEN
		INSERT INTO MT_ENH_PRT_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
		)
		VALUES
		(
			 :NEW.BOM_PART_DB_ID,
			 :NEW.BOM_PART_ID,
			 :NEW.PART_NO_DB_ID,
			 :NEW.PART_NO_ID,
			 SYSDATE
		);
	RETURN;
	END IF;

	IF (deleting)
	THEN
		INSERT INTO MT_ENH_PRT_APL_LOG
		(
			 BOM_PART_DB_ID,
			 BOM_PART_ID,
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
		)
		VALUES
		(
			 :OLD.BOM_PART_DB_ID,
			 :OLD.BOM_PART_ID,
			 :OLD.PART_NO_DB_ID,
			 :OLD.PART_NO_ID,
			 SYSDATE
		);
	END IF;
END;
/

--changeSet logic_triggers:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment trigger to update materialized data as part of enhanced part search OPER-21136
CREATE OR REPLACE TRIGGER TIUDAR_PRT_NO_LOG
AFTER
UPDATE OR INSERT OR DELETE ON EQP_PART_NO
FOR EACH ROW
DECLARE
BEGIN
	IF (inserting or updating)
	THEN
		INSERT INTO MT_ENH_PART_NO_LOG
		(
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
	    )
		VALUES
		(
			 :NEW.PART_NO_DB_ID,
			 :NEW.PART_NO_ID,
			 SYSDATE
		);
	RETURN;
	END IF;

	IF (deleting)
	THEN
		INSERT INTO MT_ENH_PART_NO_LOG
		(
			 PART_NO_DB_ID,
			 PART_NO_ID,
			 UD_DT
	    )
		VALUES
		(
			 :OLD.PART_NO_DB_ID,
			 :OLD.PART_NO_ID,
			 SYSDATE
		 );
	END IF;
END;
/

--changeSet logic_triggers:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on evt_sched_dead that reads the log of updates to MT_DRV_SCHED_INFO
CREATE OR REPLACE TRIGGER TIUDA_MT_DRV_SCHED_INFO
/********************************************************************************
*
* Trigger:    TIUDA_MT_DRV_SCHED_INFO
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*       it will look into the mt_rep_int_evt_scded_pkg and stream_update
*       [0,N) records in the log objects for this purpose.
*
********************************************************************************/
  AFTER DELETE OR INSERT OR UPDATE ON evt_sched_dead
DISABLE
BEGIN
  mt_rep_int_evt_scded_pkg.stream_update;
END;
/

--changeSet logic_triggers:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on evt_sched_dead that will create log of updates to MT_DRV_SCHED_INFO
CREATE or REPLACE TRIGGER TIUDAR_MT_DRV_SCHED_INFO
/********************************************************************************
*
* Trigger:    TIUDAR_MT_DRV_SCHED_INFO
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*       will be entered in the mt_rep_int_evt_scded_pkg table objects
*       for that purpose using the post_update procedure.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON evt_sched_dead
FOR EACH ROW
DISABLE
DECLARE
  lt_log_rec mt_rep_int_evt_scded_pkg.gt_evt_rec;

BEGIN
  IF (inserting) THEN
    IF (:NEW.hist_bool_ro = 0 AND :NEW.sched_driver_bool = 1)
	THEN
      lt_log_rec.event_db_id       := :NEW.event_db_id;
      lt_log_rec.event_id          := :NEW.event_id;
      lt_log_rec.data_type_db_id   := :NEW.data_type_db_id;
      lt_log_rec.data_type_id      := :NEW.data_type_id;
      lt_log_rec.action            := 'I';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
    END IF;
  END IF;

  IF (updating) THEN
    IF (:NEW.hist_bool_ro = 0 AND :NEW.sched_driver_bool = 1)
	THEN
      lt_log_rec.event_db_id       := :NEW.event_db_id;
      lt_log_rec.event_id          := :NEW.event_id;
      lt_log_rec.data_type_db_id   := :NEW.data_type_db_id;
      lt_log_rec.data_type_id      := :NEW.data_type_id;
      lt_log_rec.action            := 'U';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
	END IF;
	IF (:NEW.hist_bool_ro <> 0 OR :NEW.sched_driver_bool <> 1 AND
        (:OLD.hist_bool_ro = 0 AND :OLD.sched_driver_bool = 1))
	THEN
      lt_log_rec.event_db_id := :NEW.event_db_id;
      lt_log_rec.event_id    := :NEW.event_id;
      lt_log_rec.action      := 'D';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
	END IF;
  END IF;

  IF (deleting) THEN
    IF (:OLD.hist_bool_ro = 0 AND :OLD.sched_driver_bool = 1)
	THEN
      lt_log_rec.event_db_id := :OLD.event_db_id;
      lt_log_rec.event_id    := :OLD.event_id;
      lt_log_rec.action      := 'D';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
    END IF;
  END IF;
END;
/

--changeSet logic_triggers:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on inv_inv that will consume log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiuda_mt_inv_inv
/********************************************************************************
*
* Trigger:    TIUDA_MT_INV_INV
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*				it will look into the mt_rep_int_evt_scded_pkg and stream_update
*				[0,N) records in the log objects for this purpose.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON inv_inv
DISABLE
BEGIN
       mt_inv_ac_auth_inv_inv_pkg.gi_stream_mode := 1;
       mt_inv_ac_auth_inv_inv_pkg.stream_update;
END;
/


--changeSet logic_triggers:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on org_hr that will consume log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiuda_mt_mt_org_hr
/********************************************************************************
*
* Trigger:    TIUDA_MT_MT_ORG_HR
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*				it will look into the mt_rep_int_evt_scded_pkg and stream_update
*				[0,N) records in the log objects for this purpose.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON org_hr
DISABLE
BEGIN
       mt_inv_ac_auth_inv_inv_pkg.gi_stream_mode := 2;
       mt_inv_ac_auth_inv_inv_pkg.stream_update;
END;
/


--changeSet logic_triggers:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on org_hr_authority that will consume log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiuda_mt_org_hr_auth
/********************************************************************************
*
* Trigger:    TIUDA_MT_ORG_HR_AUTH
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*				it will look into the mt_rep_int_evt_scded_pkg and stream_update
*				[0,N) records in the log objects for this purpose.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON org_hr_authority
DISABLE
BEGIN
       mt_inv_ac_auth_inv_inv_pkg.gi_stream_mode := 3;
       mt_inv_ac_auth_inv_inv_pkg.stream_update;
END;
/


--changeSet logic_triggers:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on org_hr_authority that will create log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiudar_mt_org_hr_auth
/********************************************************************************
*
* Trigger:    tiudar_mt_org_hr_auth
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*				will be entered in the mt_rep_int_evt_scded_pkg table objects
*				for that purpose using the post_update procedure.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON org_hr_authority
FOR EACH ROW
DISABLE
DECLARE

		lt_work_record		mt_inv_ac_auth_inv_inv_pkg.gt_inv_rec;

BEGIN

	IF (deleting)
	THEN
		lt_work_record.authority_db_id  := :OLD.authority_db_id;
		lt_work_record.authority_id     := :OLD.authority_id;
		lt_work_record.hr_db_id         := :OLD.hr_db_id;
		lt_work_record.hr_id            := :OLD.hr_id;
		lt_work_record.action           := 'D';
		mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;

	IF (inserting)
	THEN
		lt_work_record.authority_db_id  := :NEW.authority_db_id;
		lt_work_record.authority_id     := :NEW.authority_id;
		lt_work_record.hr_db_id         := :NEW.hr_db_id;
		lt_work_record.hr_id            := :NEW.hr_id;
		lt_work_record.action           := 'I';
		mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;

	IF (updating)
	THEN
		lt_work_record.authority_db_id  := :NEW.authority_db_id;
		lt_work_record.authority_id     := :NEW.authority_id;
		lt_work_record.hr_db_id         := :NEW.hr_db_id;
		lt_work_record.hr_id            := :NEW.hr_id;
		lt_work_record.action           := 'U';
		mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;
END;
/




--changeSet logic_triggers:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on org_hr that will create log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiudar_mt_org_hr
/********************************************************************************
*
* Trigger:    TIUDAR_MT_ORG_HR
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*				will be entered in the mt_rep_int_evt_scded_pkg table objects
*				for that purpose using the post_update procedure.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON org_hr
FOR EACH ROW
DISABLE
DECLARE
       lt_work_record    mt_inv_ac_auth_inv_inv_pkg.gt_inv_rec;


BEGIN
   IF (deleting)
   THEN
		lt_work_record.hr_db_id     := :OLD.hr_db_id;
		lt_work_record.hr_id        := :OLD.hr_id;
		lt_work_record.action       := 'D';
		mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
   END IF;

   IF (inserting)
   THEN
		lt_work_record.hr_db_id           := :NEW.hr_db_id;
		lt_work_record.hr_id              := :NEW.hr_id;
		lt_work_record.all_authority_bool := :NEW.all_authority_bool;
		lt_work_record.action             := 'I';
		mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
   END IF;

   IF (updating)
   THEN
		lt_work_record.hr_db_id           := :NEW.hr_db_id;
		lt_work_record.hr_id              := :NEW.hr_id;
		lt_work_record.all_authority_bool := :NEW.all_authority_bool;
		lt_work_record.action             := 'U';
		mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
   END IF;
END;
/



--changeSet logic_triggers:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on inv_inv that will create log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiudar_mt_inv_inv
/********************************************************************************
*
* Trigger:    TIUDAR_MT_INV_INV
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*				will be entered in the mt_rep_int_evt_scded_pkg table objects
*				for that purpose using the post_update procedure.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE OF inv_no_db_id, inv_no_id, authority_db_id, authority_id, locked_bool, inv_class_cd, inv_class_db_id ON inv_inv
FOR EACH ROW
DISABLE
DECLARE
       lt_work_record	mt_inv_ac_auth_inv_inv_pkg.gt_inv_rec;

BEGIN
	IF (deleting)
		THEN
			IF (:OLD.inv_class_cd    != 'ACFT')
			THEN
			   RETURN;
			END IF;

			IF (:OLD.inv_class_db_id  != 0)
			THEN
			   RETURN;
			END IF;

			lt_work_record.inv_no_db_id     := :OLD.inv_no_db_id;
			lt_work_record.inv_no_id        := :OLD.inv_no_id;
			lt_work_record.action           := 'D';
			mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;

	IF (inserting)
		THEN
			IF (:NEW.inv_class_cd    != 'ACFT')
			THEN
			   RETURN;
			END IF;

			IF (:NEW.inv_class_db_id  != 0)
			THEN
			   RETURN;
			END IF;

			lt_work_record.inv_no_db_id    := :NEW.inv_no_db_id;
			lt_work_record.inv_no_id       := :NEW.inv_no_id;
			lt_work_record.authority_db_id := :NEW.authority_db_id;
			lt_work_record.authority_id    := :NEW.authority_id;
			lt_work_record.locked_bool     := :NEW.locked_bool;
			lt_work_record.action          := 'I';
			mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;


    IF (updating)
    	THEN
			IF (:NEW.inv_class_cd    != 'ACFT')
			THEN
				RETURN;
			END IF;

			IF (:NEW.inv_class_db_id  != 0)
			THEN
				RETURN;
			END IF;

			lt_work_record.inv_no_db_id    := :NEW.inv_no_db_id;
			lt_work_record.inv_no_id       := :NEW.inv_no_id;
			lt_work_record.authority_db_id := :NEW.authority_db_id;
			lt_work_record.authority_id    := :NEW.authority_id;
			lt_work_record.locked_bool     := :NEW.locked_bool;
			lt_work_record.action          := 'U';
			mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;
END;
/


--changeSet logic_triggers:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on mt_drv_sched_info that will consume log of updates for MT_CORE_FLEET_LIST
CREATE OR REPLACE TRIGGER TIUDA_MT_CORE_FLEET_LIST
/********************************************************************************
*
* Trigger:    TIUDA_MT_CORE_FLEET_LIST
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*				it will look into the mt_rep_int_evt_scded_pkg and stream_update
*				[0,N) records in the log objects for this purpose.
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON mt_drv_sched_info
DISABLE
BEGIN
       mt_core_flt_list_pkg.stream_update;
END;
/


--changeSet logic_triggers:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on mt_drv_sched_info that will create log of updates for MT_CORE_FLEET_LIST
CREATE OR REPLACE TRIGGER TIUDAR_MT_CORE_FLEET_LIST
/********************************************************************************
*
* Trigger:    TIUDAR_MT_CORE_FLEET_LIST
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*				will be entered in the mt_rep_int_evt_scded_pkg table objects
*				for that purpose using the post_update procedure.
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON MT_DRV_SCHED_INFO
FOR EACH ROW
DISABLE
DECLARE
	lt_log_rec      mt_core_flt_list_pkg.gt_evt_rec;

BEGIN
	IF (inserting)
	THEN
		lt_log_rec.event_db_id := :NEW.event_db_id;
		lt_log_rec.event_id    := :NEW.event_id;
		lt_log_rec.action      := 'I';
		mt_core_flt_list_pkg.post_update (lt_log_rec);
	END IF;

	IF (updating)
	THEN
		lt_log_rec.event_db_id := :NEW.event_db_id;
		lt_log_rec.event_id    := :NEW.event_id;
		lt_log_rec.action      := 'U';
		mt_core_flt_list_pkg.post_update (lt_log_rec);
	END IF;

	IF (deleting)
	THEN
		lt_log_rec.event_db_id := :OLD.event_db_id;
		lt_log_rec.event_id    := :OLD.event_id;
		lt_log_rec.action      := 'D';
		mt_core_flt_list_pkg.post_update (lt_log_rec);
	END IF;
END;
/

--changeSet logic_triggers:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_event
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CND_CHG_EVENT
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_chg_event,
*				        the corresponding same action must be executed on the row of the table evt_event first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_event
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_event t
      USING dual
      ON (t.event_db_id = :new.event_db_id AND t.event_id = :new.event_id)
      WHEN MATCHED THEN
         UPDATE SET
            stage_reason_db_id = :new.stage_reason_db_id,
            stage_reason_cd    = :new.stage_reason_cd,
            editor_hr_db_id    = :new.editor_hr_db_id,
            editor_hr_id       = :new.editor_hr_id,
            event_status_db_id = :new.event_status_db_id,
            event_status_cd    = :new.event_status_cd,
            event_reason_db_id = :new.event_reason_db_id,
            event_reason_cd    = :new.event_reason_cd,
            data_source_db_id  = :new.data_source_db_id,
            data_source_cd     = :new.data_source_cd,
            h_event_db_id      = :new.h_event_db_id,
            h_event_id         = :new.h_event_id,
            event_sdesc        = :new.event_sdesc,
            ext_key_sdesc      = :new.ext_key_sdesc,
            seq_err_bool       = :new.seq_err_bool,
            event_ldesc        = :new.event_ldesc,
            event_dt           = :new.event_dt,
            event_gdt          = :new.event_dt,
            sched_start_dt     = :new.sched_start_dt,
            sub_event_ord      = :new.sub_event_ord
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
          event_id,
          event_type_db_id,
          event_type_cd,
          stage_reason_db_id,
          stage_reason_cd,
          editor_hr_db_id,
          editor_hr_id,
          event_status_db_id,
          event_status_cd,
          event_reason_db_id,
          event_reason_cd,
          data_source_db_id,
          data_source_cd,
          h_event_db_id,
          h_event_id,
          event_sdesc,
          ext_key_sdesc,
          hist_bool,
          seq_err_bool,
          event_ldesc,
          event_dt,
          event_gdt,
          sched_start_dt,
          sub_event_ord,
          alt_id,
          rstat_cd,
          ctrl_db_id,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user
          )
         VALUES
         (:new.event_db_id,
          :new.event_id,
          0,
          'AC',
          :new.stage_reason_db_id,
          :new.stage_reason_cd,
          :new.editor_hr_db_id,
          :new.editor_hr_id,
          :new.event_status_db_id,
          :new.event_status_cd,
          :new.event_reason_db_id,
          :new.event_reason_cd,
          :new.data_source_db_id,
          :new.data_source_cd,
          :new.h_event_db_id,
          :new.h_event_id,
          :new.event_sdesc,
          :new.ext_key_sdesc,
          1,
          :new.seq_err_bool,
          :new.event_ldesc,
          :new.event_dt,
          :new.event_dt,
          :new.sched_start_dt,
          :new.sub_event_ord,
          :new.alt_id,
          :new.rstat_cd,
          :new.ctrl_db_id,
          :new.creation_dt,
          :new.revision_dt,
          :new.revision_db_id,
          :new.revision_user);
      RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_event
      WHERE event_db_id = :old.event_db_id AND
            event_id    = :old.event_id;
      RETURN;
   END IF;
END;
/

--changeSet logic_triggers:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_inv
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CND_CHG_INV
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_cnd_chg_inv,
*				        the corresponding same action must be executed on the row of the table evt_inv first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_inv
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_inv t
      USING dual
      ON (t.event_db_id  = :new.event_db_id  AND
          t.event_id     = :new.event_id     AND
          t.event_inv_id = :new.event_inv_id)
      WHEN MATCHED THEN
         UPDATE SET
            inv_no_db_id        = :new.inv_no_db_id,
            inv_no_id           = :new.inv_no_id,
            nh_inv_no_db_id     = :new.nh_inv_no_db_id,
            nh_inv_no_id        = :new.nh_inv_no_id,
            assmbl_inv_no_db_id = :new.assmbl_inv_no_db_id,
            assmbl_inv_no_id    = :new.assmbl_inv_no_id,
            h_inv_no_db_id      = :new.h_inv_no_db_id,
            h_inv_no_id         = :new.h_inv_no_id,
            assmbl_db_id        = :new.assmbl_db_id,
            assmbl_cd           = :new.assmbl_cd,
            assmbl_bom_id       = :new.assmbl_bom_id,
            assmbl_pos_id       = :new.assmbl_pos_id,
            part_no_db_id       = :new.part_no_db_id,
            part_no_id          = :new.part_no_id,
            bom_part_db_id      = :new.bom_part_db_id,
            bom_part_id         = :new.bom_part_id,
            main_inv_bool       = :new.main_inv_bool
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
          event_id,
          event_inv_id,
          inv_no_db_id,
          inv_no_id,
          nh_inv_no_db_id,
          nh_inv_no_id,
          assmbl_inv_no_db_id,
          assmbl_inv_no_id,
          h_inv_no_db_id,
          h_inv_no_id,
          assmbl_db_id,
          assmbl_cd,
          assmbl_bom_id,
          assmbl_pos_id,
          part_no_db_id,
          part_no_id,
          bom_part_db_id,
          bom_part_id,
          main_inv_bool,
          rstat_cd,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user)
      VALUES
        (:new.event_db_id,
         :new.event_id,
         :new.event_inv_id,
         :new.inv_no_db_id,
         :new.inv_no_id,
         :new.nh_inv_no_db_id,
         :new.nh_inv_no_id,
         :new.assmbl_inv_no_db_id,
         :new.assmbl_inv_no_id,
         :new.h_inv_no_db_id,
         :new.h_inv_no_id,
         :new.assmbl_db_id,
         :new.assmbl_cd,
         :new.assmbl_bom_id,
         :new.assmbl_pos_id,
         :new.part_no_db_id,
         :new.part_no_id,
         :new.bom_part_db_id,
         :new.bom_part_id,
         :new.main_inv_bool,
         :new.rstat_cd,
         :new.creation_dt,
         :new.revision_dt,
         :new.revision_db_id,
         :new.revision_user);
      RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_inv
   WHERE event_db_id  = :old.event_db_id   AND
         event_id     = :old.event_id      AND
         event_inv_id = :old.event_inv_id;
      RETURN;
   END IF;
END;
/
--changeSet logic_triggers:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_inv_usage
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CND_CHG_INV_USAGE
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_cnd_chg_inv_usage,
*				        the corresponding same action must be executed on the row of the table evt_inv first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_inv_usage
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_inv_usage t
      USING dual
      ON (t.event_db_id     = :new.event_db_id     AND
          t.event_id        = :new.event_id        AND
          t.event_inv_id    = :new.event_inv_id    AND
          t.data_type_db_id = :new.data_type_db_id AND
          t.data_type_id    = :new.data_type_id)
      WHEN MATCHED THEN
         UPDATE SET
            tsn_qt = :new.tsn_qt,
            tso_qt = :new.tso_qt,
            tsi_qt = :new.tsi_qt,
            assmbl_tsn_qt = :new.assmbl_tsn_qt,
            assmbl_tso_qt = :new.assmbl_tso_qt,
            h_tsn_qt = :new.h_tsn_qt,
            h_tso_qt = :new.h_tso_qt,
            nh_tsn_qt = :new.nh_tsn_qt,
            nh_tso_qt = :new.nh_tso_qt,
            negated_bool = :new.negated_bool
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
          event_id,
          event_inv_id,
          data_type_db_id,
          data_type_id,
          tsn_qt,
          tso_qt,
          tsi_qt,
          assmbl_tsn_qt,
          assmbl_tso_qt,
          h_tsn_qt,
          h_tso_qt,
          nh_tsn_qt,
          nh_tso_qt,
          negated_bool,
          source_db_id,
          source_cd,
          rstat_cd,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user)
         VALUES
         (:new.event_db_id,
          :new.event_id,
          :new.event_inv_id,
          :new.data_type_db_id,
          :new.data_type_id,
          :new.tsn_qt,
          :new.tso_qt,
          :new.tsi_qt,
          :new.assmbl_tsn_qt,
          :new.assmbl_tso_qt,
          :new.h_tsn_qt,
          :new.h_tso_qt,
          :new.nh_tsn_qt,
          :new.nh_tso_qt,
          :new.negated_bool,
          0,
          'MAINTENIX',
          :new.rstat_cd,
          :new.creation_dt,
          :new.revision_dt,
          :new.revision_db_id,
          :new.revision_user);
		    RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_inv_usage
      WHERE event_db_id     = :old.event_db_id     AND
            event_id        = :old.event_id        AND
            event_inv_id    = :old.event_inv_id    AND
            data_type_db_id = :old.data_type_db_id AND
            data_type_id    = :old.data_type_id;
		  RETURN;
   END IF;
END;
/

--changeSet logic_triggers:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on ship_shipment_line that fills unit of measure columns using part standard unit of measure value
CREATE OR REPLACE TRIGGER TIUBR_UOM_SHIPLINE
/********************************************************************************
*
* Trigger:    TIUBR_UOM_SHIPLINE
*
* Description: This is a TABLE based trigger. When inserting values to ship_shipment_line table,
* if the new qty_unit_cd, qty_unit_db_id value is null, or if the shipment line part changes, then this trigger updates those columns by fetching standard
* unit of measure eqp_part_no
********************************************************************************/
BEFORE INSERT OR UPDATE ON SHIP_SHIPMENT_LINE
FOR EACH ROW

DECLARE
BEGIN

  IF :new.qty_unit_cd is null OR (:new.part_no_db_id||':'|| :new.part_no_id != :old.part_no_db_id||':'||:old.part_no_id ) THEN
    SELECT
    eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
    INTO
    :new.qty_unit_db_id, :new.qty_unit_cd
    FROM
    eqp_part_no
    WHERE
    eqp_part_no.part_no_db_id = :new.part_no_db_id AND
    eqp_part_no.part_no_id = :new.part_no_id;
  END IF;
END;
/

--changeSet logic_triggers:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment trigger for req_part to fetch and store the unit of measure of the part when new records are added
CREATE OR REPLACE TRIGGER TIBR_UOM_REQPART
/********************************************************************************
*
* Trigger:    TIBR_UOM_REQPART
*
* Description:  This is a ROW based trigger.
* When a new row is added to the req_part table:
* 		1. If the new row is associated with a specific part
* 			the uom of the row is updated to the standard unit of measure of the requested part from eqp_part_no.
* 		2. If the new row has an associated part group, but not a specific part,
* 			the uom of the row is updated to the standard unit of measure of the preferred part in the part group by joining eqp_part_baseline and eqp_bom_part.
* 		3. If the new request is a stock request with neither a specific part or part group,
* 			the uom of the row is updated to the uom of the stock from eqp_stock_no.
*
* *****************************************************************************/
BEFORE INSERT ON REQ_PART
FOR EACH ROW
BEGIN
	-- Do not assign a default UoM when a Qty is not provided
	IF (:new.qty_unit_cd IS NULL AND :new.req_qt IS NOT NULL) THEN
		IF (:new.req_spec_part_no_db_id IS NOT NULL AND :new.req_spec_part_no_id IS NOT NULL) THEN
			SELECT
				eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
			INTO
				:new.qty_unit_db_id, :new.qty_unit_cd
			FROM eqp_part_no
			WHERE
				eqp_part_no.part_no_db_id = :new.req_spec_part_no_db_id AND
				eqp_part_no.part_no_id = :new.req_spec_part_no_id;
		ELSIF (:new.req_bom_part_db_id IS NOT NULL AND :new.req_bom_part_id IS NOT NULL) THEN
			SELECT
				eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
			INTO
				:new.qty_unit_db_id, :new.qty_unit_cd
			FROM eqp_part_no
			INNER JOIN eqp_part_baseline ON
				eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
				eqp_part_no.part_no_id = eqp_part_baseline.part_no_id
			WHERE
				eqp_part_baseline.bom_part_db_id = :new.req_bom_part_db_id AND
				eqp_part_baseline.bom_part_id = :new.req_bom_part_id AND
				eqp_part_baseline.standard_bool = 1 AND
				eqp_part_no.rstat_cd = 0;
		ELSIF (:new.req_stock_no_db_id IS NOT NULL AND :new.req_stock_no_id IS NOT NULL) THEN
			SELECT
				eqp_stock_no.qty_unit_db_id, eqp_stock_no.qty_unit_cd
			INTO
				:new.qty_unit_db_id, :new.qty_unit_cd
			FROM
				eqp_stock_no
			WHERE
				eqp_stock_no.stock_no_db_id = :new.req_stock_no_db_id AND
				eqp_stock_no.stock_no_id = :new.req_stock_no_id;
		ELSE
			-- Qty exists however a UoM is not available; default to EACH
			:new.qty_unit_db_id  := 0;
			:new.qty_unit_cd := 'EA';
		END IF;
	END IF;
END;
/

--changeSet logic_triggers:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UOM_INVXFER
/********************************************************************************
*
* Trigger:    TIBR_UOM_INVXFER
*
* Description:  Before insertion of rows in inv_xfer table, the unit_qty_cd and unit_qty_db_id columns will be checked
*					for null and if the columns are null, UOM from eqp_part_no table will be copied to the columns.
*
********************************************************************************/
BEFORE INSERT ON INV_XFER
FOR EACH ROW
WHEN (new.qty_unit_cd IS NULL OR new.qty_unit_db_id IS NULL)
BEGIN
	IF :new.inv_no_id IS NOT NULL THEN
		SELECT
			eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
		INTO
			:new.qty_unit_db_id, :new.qty_unit_cd
		FROM
			eqp_part_no
			INNER JOIN inv_inv ON
				eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
				eqp_part_no.part_no_id = inv_inv.part_no_id
		WHERE
			inv_inv.inv_no_db_id = :new.inv_no_db_id AND
			inv_inv.inv_no_id = :new.inv_no_id;
	ELSIF :new.init_event_id IS NOT NULL THEN
		SELECT
			eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
		INTO
			:new.qty_unit_db_id, :new.qty_unit_cd
		FROM
			evt_inv
			INNER JOIN inv_inv ON
				evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
				evt_inv.inv_no_id = inv_inv.inv_no_id
			INNER JOIN eqp_part_no ON
				eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
				eqp_part_no.part_no_id = inv_inv.part_no_id
		WHERE
			evt_inv.event_db_id = :new.init_event_db_id AND
			evt_inv.event_id = :new.init_event_id;
	END IF;
END;
/

--changeSet logic_triggers:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_AXON_DOM_EVENT_ENTRY_ID
/********************************************************************************
*
* Trigger:    TIBR_AXON_DOM_EVENT_ENTRY_ID
*
* Description:  Axon Framework trigger on the AXON_DOMAIN_EVENT_ENTRY table.
*
********************************************************************************/
   BEFORE INSERT ON AXON_DOMAIN_EVENT_ENTRY
   FOR EACH ROW
   BEGIN
      :new.globalIndex := AXON_DOMAIN_EVENT_ENTRY_SEQ.nextval;
   END;
/
--changeSet logic_triggers:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_AXON_ASSOC_VALUE_ENTRY_ID
/********************************************************************************
*
* Trigger:    TIBR_AXON_ASSOC_VALUE_ENTRY_ID
*
* Description:  Axon Framework trigger on the AXON_ASSOC_VALUE_ENTRY table.
*
********************************************************************************/
   BEFORE INSERT ON AXON_ASSOC_VALUE_ENTRY
   FOR EACH ROW
   BEGIN
      :new.id := AXON_ASSOC_VALUE_ENTRY_SEQ.nextval;
   END;
/

--changeSet logic_triggers:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIUBR_EXT_REF_ITEM" BEFORE INSERT OR UPDATE
   ON "EXT_REF_ITEM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  -- force the reference item name to uppercase 
  :new.reference_item_name := UPPER(:new.reference_item_name);
END;
/

--changeSet logic_triggers:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_INV_LOC_BIN_LOG_ID
/********************************************************************************
*
* Trigger:    TIBR_INV_LOC_BIN_LOG_ID
*
* Description:  Trigger for INV_LOC_BIN_LOG_ID.
*
********************************************************************************/
   BEFORE INSERT ON INV_LOC_BIN_LOG
   FOR EACH ROW
   BEGIN
      :new.inv_loc_bin_log_id := INV_LOC_BIN_LOG_ID_SEQ.nextval;
   END;
/