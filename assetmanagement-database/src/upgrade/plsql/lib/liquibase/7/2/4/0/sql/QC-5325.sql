--liquibase formatted sql


--changeSet QC-5325:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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