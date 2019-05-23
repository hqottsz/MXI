/* Create the crew and crew schedule. */
DECLARE
   ln_dept_id org_work_dept.dept_id%TYPE;
   ld_start_date DATE;

BEGIN

SELECT MAX(dept_id)+1 INTO ln_dept_id FROM org_work_dept;
SELECT NEXT_DAY(SYSDATE, 'SUN') INTO ld_start_date FROM dual;

INSERT INTO org_work_dept (DEPT_DB_ID, DEPT_ID, DEPT_CD, DEPT_TYPE_DB_ID, DEPT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, ALT_ID)
SELECT 4650, ln_dept_id, 'AIRPORT1-HVYMAINT', 0, 'CREW', 0, 1, 'bme-crew-1', 'Base Heavy Maintenance', '7E5E54C3DC3811E7A4A3F3A7F5CE9017' FROM DUAL;

INSERT INTO inv_loc_dept (LOC_DB_ID, LOC_ID, DEPT_ID, DEPT_DB_ID)
SELECT 4650, (select loc_id from inv_loc where loc_cd = 'AIRPORT1/LINE'), ln_dept_id, 4650 FROM DUAL;

INSERT INTO org_dept_hr (DEPT_DB_ID, DEPT_ID, HR_DB_ID, HR_ID)
          SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user1') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user2') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user3') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user4') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'crewlead') FROM DUAL;

INSERT INTO org_crew_schedule( CREW_DB_ID, CREW_ID, CREW_SCHEDULE_ID, CREW_SHIFT_PATTERN_DB_ID, CREW_SHIFT_PATTERN_ID, START_DT, END_DT, ALT_ID)
          SELECT 4650, ln_dept_id, 1, 4650, 1, ld_start_date, ld_start_date+6, '297CE273DAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 2, 4650, 2, ld_start_date+7, ld_start_date+13, '425FE309DAB711E789C5A0D3C126505D' FROM DUAL;

INSERT INTO org_crew_shift_plan
(CREW_DB_ID, CREW_ID, CREW_SHIFT_PLAN_ID, SHIFT_DB_ID, SHIFT_ID, SCHEDULE_ID, DAY_DT, START_HOUR, DURATION_QT, WORK_HOURS_QT, ALT_ID)
          SELECT 4650, ln_dept_id,  6, 4650, 1, '297CE273DAB711E789C5A0D3C126505D', ld_start_date, 6.00, 8, 6, '29803DD4DAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id,  7, 4650, 1, '297CE273DAB711E789C5A0D3C126505D', ld_start_date+1, 6.00, 8, 6, '29817655DAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id,  8, 4650, 1, '297CE273DAB711E789C5A0D3C126505D', ld_start_date+2, 6.00, 8, 6, '2982AED6DAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id,  9, 4650, 1, '297CE273DAB711E789C5A0D3C126505D', ld_start_date+3, 6.00, 8, 6, '29840E67DAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 10, 4650, 1, '297CE273DAB711E789C5A0D3C126505D', ld_start_date+4, 6.00, 8, 6, '29856DF8DAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 11, 4650, 2, '425FE309DAB711E789C5A0D3C126505D', ld_start_date+5, 14.00, 8, 6, '4262C93ADAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 12, 4650, 2, '425FE309DAB711E789C5A0D3C126505D', ld_start_date+6, 14.00, 8, 6, '426401BBDAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 13, 4650, 2, '425FE309DAB711E789C5A0D3C126505D', ld_start_date+7, 14.00, 8, 6, '42653A3CDAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 14, 4650, 2, '425FE309DAB711E789C5A0D3C126505D', ld_start_date+8, 14.00, 8, 6, '42664BADDAB711E789C5A0D3C126505D' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 15, 4650, 2, '425FE309DAB711E789C5A0D3C126505D', ld_start_date+9, 14.00, 8, 6, '4267842EDAB711E789C5A0D3C126505D' FROM DUAL;

END;
/