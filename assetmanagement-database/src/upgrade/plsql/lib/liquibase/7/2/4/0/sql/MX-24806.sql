--liquibase formatted sql


--changeSet MX-24806:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
--
-- Migration for MX-24806
--
-- 1) Add the new column ENFORCE_NSV to the SCHED_WP table.
--    This tri-state column indicates
--      0 = if the enforce NSV tasks feature is not applicable to this wp 
--         (only applicable for component work packages)
--      1 = component work package is enforcing NSV tasks (default)
--      2 = component work package is ignoring NSV tasks
--
-- 2) Update the TIAF_SCHED_WP_INSRT insert trigger to populate the 
--    ENFORCE_NSV column the SCHED_WP table. 
--
-- 3) Create the four new action parms and copy the value of the existing 
--    action parm ACTION_ENFORCE_NSV_DEADLINES into these four new parms.  
--    Then delete the ACTION_ENFORCE_NSV_DEADLINES action parm.
--
--    Therefore, any permissions set previously using 
--    ACTION_ENFORCE_NSV_DEADLINES will be applied to the four new parms.
--
-- 4) Create a config parm to store the session value of the 
--    Show Next Show Visit Tasks parm used by various search pages.
--
-- 5) Remove the config parms 
--       ENFORCE_NEXT_SHOP_VISIT_DEADLINES and sEnforceNSVDeadlines 
--    as the enforce NSV deadlines functionality is replaced with the 
--    Enforce NSV Tasks functionality of MX-24806.
--
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
--
-- 1) Add the new column ENFORCE_NSV to the SCHED_WP table.
--
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table SCHED_WP add (
         ENFORCE_NSV Number(10,0) Check (ENFORCE_NSV IN (0,1,2)) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-24806:2 stripComments:false
-- 
-- set the default value of sched_wp.enforce_nsv based on the following
--   0 if it is not a component work package (not applicable)
--   1 if it is a component work package (nsv tasks enforced)
--
UPDATE
   sched_wp
SET
   sched_wp.enforce_nsv = 
   (
      SELECT 
         -- if the main inv of the work package is an aircraft 
         -- then this is NOT a component work package
         DECODE( 
            (inv_inv.inv_class_db_id || ':' || inv_inv.inv_class_cd),
            ('0:ACFT'),
            0,
            1
         )
      FROM 
         sched_stask
         INNER JOIN inv_inv ON
            inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
            inv_inv.inv_no_id    = sched_stask.main_inv_no_id 
      WHERE
         sched_stask.sched_db_id = sched_wp.sched_db_id AND
         sched_stask.sched_id    = sched_wp.sched_id
   )
WHERE
   sched_wp.enforce_nsv IS NULL
;

--changeSet MX-24806:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table SCHED_WP modify (
         ENFORCE_NSV Number(10,0) DEFAULT 0 NOT NULL DEFERRABLE  Check (ENFORCE_NSV IN (0,1,2)) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-24806:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- 2) Update the TIAF_SCHED_WP_INSRT insert trigger to populate the ENFORCE_NSV column the SCHED_WP table.
--    Note: it was originally suggested that a new trigger be used to update the ENFORCE_NSV column whenever
--          a new row was inserted, however, this trigger performs the insert.  And becuase this trigger is 
--          on the SCHED_STASK table and that table is also needed to determine if the WP is a component WP, 
--          we cannot use the proposed new trigger on SCHED_WP (mutating table error).
--
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

--changeSet MX-24806:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- 3) Remove action parm ACTION_ENFORCE_NSV_DEADLINES and add the new parms.
--
--
-- 3.1) Create the four new action parms.
--
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ENFORCE_NSV_TASKS_DURING_PLANNING',
      'Permission to enforce next shop visit tasks during planning of work package.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.0',
	  0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'OPER' )
   );
END;
/

--changeSet MX-24806:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_ENFORCE_NSV_TASKS_DURING_EXECUTION',
      'Permission to enforce next shop visit tasks during execution of work package.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.0',
	  0,
      0,
      utl_migr_data_pkg.DbTypeCdList( 'OPER' )
   );
END;
/

--changeSet MX-24806:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_IGNORE_NSV_TASKS_DURING_PLANNING',
      'Permission to ignore next shop visit tasks during planning of work package.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.0',
      0,
	  0,
      utl_migr_data_pkg.DbTypeCdList( 'OPER' )
   );
END;
/

--changeSet MX-24806:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_IGNORE_NSV_TASKS_DURING_EXECUTION',
      'Permission to ignore next shop visit tasks during execution of work package.',
      'TRUE/FALSE',
      'FALSE',
      1,
      'Maint - Work Packages',
      '8.0',
      0,
	  0,
      utl_migr_data_pkg.DbTypeCdList( 'OPER' )
   );
END;
/

--changeSet MX-24806:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- 3.2) Copy to one value of ACTION_ENFORCE_NSV_DEADLINES into the four new action parms.
--      This utility copy also creates any appropriate role or user parms.
--
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_ENFORCE_NSV_TASKS_DURING_PLANNING',
      'ACTION_ENFORCE_NSV_DEADLINES'
   );
END;
/

--changeSet MX-24806:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_ENFORCE_NSV_TASKS_DURING_EXECUTION',
      'ACTION_ENFORCE_NSV_DEADLINES'
   );
END;
/

--changeSet MX-24806:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_IGNORE_NSV_TASKS_DURING_PLANNING',
      'ACTION_ENFORCE_NSV_DEADLINES'
   );
END;
/

--changeSet MX-24806:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
      'ACTION_IGNORE_NSV_TASKS_DURING_EXECUTION',
      'ACTION_ENFORCE_NSV_DEADLINES'
   );
END;
/

--changeSet MX-24806:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- 3.3) Remove the ACTION_ENFORCE_NSV_DEADLINES action parm.
--
BEGIN
   utl_migr_data_pkg.action_parm_delete(
      'ACTION_ENFORCE_NSV_DEADLINES'
   );
END;
/   

--changeSet MX-24806:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- 4) Create a config parm to store the session value of the 
--    Show Next Show Visit Tasks parm used by various search pages.
--
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'sShowNsvTasks',
      'SESSION',
      'Whether to hide next shop visit tasks.',
      'USER',
      'TRUE/FALSE',
      'FALSE',
      0,
      'SESSION',
      '8.0',
      0
   );
END;
/

--changeSet MX-24806:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- 5) Remove the config parms for the depricated enforce NSV deadlines functionality.
--
BEGIN
   utl_migr_data_pkg.config_parm_delete(
      'ENFORCE_NEXT_SHOP_VISIT_DEADLINES'
   );
END;
/

--changeSet MX-24806:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_delete(
      'sEnforceNSVDeadlines'
   );
END;
/