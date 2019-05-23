--liquibase formatted sql


--changeSet DEV-236:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migration script for 1003 AF-KLM
-- Author: Rangarajan Sundararajan
-- Date  : February 10, 2010
DECLARE
-- Cursor to iterate through ORG_HR_SHIFT_PLAN table.
CURSOR lcur_orghrshiftplan IS
SELECT
org_hr_shift_plan.shift_db_id,
org_hr_shift_plan.shift_id,
org_hr_shift_plan.hr_db_id,
org_hr_shift_plan.hr_id,
org_hr_shift_plan.hr_shift_plan_id
FROM
org_hr_shift_plan
WHERE
org_hr_shift_plan.shift_db_id IS NOT NULL;

-- Variable to iterate through lcur_orghrshiftplan cursor.
lrec_orghrshiftplan lcur_orghrshiftplan%ROWTYPE;
-- Variable to hold shift start hour.
ln_starthour shift_shift.start_hour%TYPE;
-- Variable to hold shift work hours quantity.
ln_workhours shift_shift.work_hours_qt%TYPE;
-- Variable to hold shift duration.
ln_duration shift_shift.duration_qt%TYPE;
--  Variable to hold counter.
ln_count INTEGER;

BEGIN
-- For each row in org_hr_shift_plan table that has shift, do the following.
FOR lrec_orghrshiftplan IN lcur_orghrshiftplan
LOOP
    -- Retrieve the shift start hour, work hours and duration from shift_shift table
    -- based on the shift key.
    
    SELECT 
    COUNT(*) 
    INTO
    ln_count
    FROM
    shift_shift
    WHERE
    shift_shift.shift_db_id = lrec_orghrshiftplan.shift_db_id AND
    shift_shift.shift_id = lrec_orghrshiftplan.shift_id;    
    
    IF ln_count > 0 THEN
    
    SELECT 
    shift_shift.start_hour,
    shift_shift.work_hours_qt,
    shift_shift.duration_qt
    INTO
    ln_starthour,
    ln_workhours,
    ln_duration
    FROM
    shift_shift
    WHERE
    shift_shift.shift_db_id = lrec_orghrshiftplan.shift_db_id AND
    shift_shift.shift_id = lrec_orghrshiftplan.shift_id;
    -- Update the shift information in org_hr_shift_plan table.
    UPDATE org_hr_shift_plan
    SET
    org_hr_shift_plan.start_hour = ln_starthour,
    org_hr_shift_plan.work_hours_qt = ln_workhours,
    org_hr_shift_plan.duration_qt = ln_duration,
    org_hr_shift_plan.crew_db_id = null,
    org_hr_shift_plan.crew_id = null
    WHERE
    org_hr_shift_plan.hr_db_id = lrec_orghrshiftplan.hr_db_id AND
    org_hr_shift_plan.hr_id = lrec_orghrshiftplan.hr_id AND
    org_hr_shift_plan.hr_shift_plan_id = lrec_orghrshiftplan.hr_shift_plan_id;
    
    END IF;
END LOOP;
END;
/