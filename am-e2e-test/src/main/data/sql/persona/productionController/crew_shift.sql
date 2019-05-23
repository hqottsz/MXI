/* Create the crew and crew schedule. */
DECLARE
   ln_dept_id org_work_dept.dept_id%TYPE;
   ld_start_date DATE;

BEGIN

SELECT MAX(dept_id)+1 INTO ln_dept_id FROM org_work_dept;
SELECT NEXT_DAY(SYSDATE, 'SUN') INTO ld_start_date FROM dual;

INSERT INTO org_work_dept (DEPT_DB_ID, DEPT_ID, DEPT_CD, DEPT_TYPE_DB_ID, DEPT_TYPE_CD, BITMAP_DB_ID, BITMAP_TAG, DESC_SDESC, DESC_LDESC, ALT_ID)
SELECT 4650, ln_dept_id, 'AIRPORT1-BASEMAINT', 0, 'CREW', 0, 1, 'bm-crew-1', 'Base Maintenance', '2A0C89E138D345D18F9E7DF9BC583727' FROM DUAL;

INSERT INTO inv_loc_dept (LOC_DB_ID, LOC_ID, DEPT_ID, DEPT_DB_ID)
SELECT 4650, (select loc_id from inv_loc where loc_cd = 'AIRPORT1/LINE'), ln_dept_id, 4650 FROM DUAL;

INSERT INTO org_dept_hr (DEPT_DB_ID, DEPT_ID, HR_DB_ID, HR_ID)
          SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user1') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user2') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user3') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user4') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user5') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'user6') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'mxi') FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'crewlead') FROM DUAL;

INSERT INTO org_crew_schedule( CREW_DB_ID, CREW_ID, CREW_SCHEDULE_ID, CREW_SHIFT_PATTERN_DB_ID, CREW_SHIFT_PATTERN_ID, START_DT, END_DT, ALT_ID)
          SELECT 4650, ln_dept_id, 1, 4650, 1, ld_start_date, ld_start_date+6, '2198B105B5BC4F288C7375BA18B301A2' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 2, 4650, 2, ld_start_date+7, ld_start_date+13, 'B32428355BAE4313A0048392CA90941D' FROM DUAL;

INSERT INTO org_crew_shift_plan
(CREW_DB_ID, CREW_ID, CREW_SHIFT_PLAN_ID, SHIFT_DB_ID, SHIFT_ID, SCHEDULE_ID, DAY_DT, START_HOUR, DURATION_QT, WORK_HOURS_QT, ALT_ID)
          SELECT 4650, ln_dept_id,  6, 4650, 1, '2198B105B5BC4F288C7375BA18B301A2', ld_start_date, 6.00, 8, 6, '79358CA585B048DE9B3CBBBCB4C198D0' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id,  7, 4650, 1, '2198B105B5BC4F288C7375BA18B301A2', ld_start_date+1, 6.00, 8, 6, '8CF410DA9E7D44F98B19BD5DD45F98DA' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id,  8, 4650, 1, '2198B105B5BC4F288C7375BA18B301A2', ld_start_date+2, 6.00, 8, 6, 'C7BBEE73F8EF461AB8F3A0A49821CE13' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id,  9, 4650, 1, '2198B105B5BC4F288C7375BA18B301A2', ld_start_date+3, 6.00, 8, 6, '2CB666D6A54244EA8E90E5C01FE2C059' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 10, 4650, 1, '2198B105B5BC4F288C7375BA18B301A2', ld_start_date+4, 6.00, 8, 6, '816CCA0582254480A2B172B5A56521FA' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 11, 4650, 2, 'B32428355BAE4313A0048392CA90941D', ld_start_date+5, 14.00, 8, 6, '4C71154B7FB64C1F8D245F12FDA30EAF' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 12, 4650, 2, 'B32428355BAE4313A0048392CA90941D', ld_start_date+6, 14.00, 8, 6, 'B1187D850DD542DD92C4C79F157AE22A' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 13, 4650, 2, 'B32428355BAE4313A0048392CA90941D', ld_start_date+7, 14.00, 8, 6, 'FB3922E7057C40A7AADB33CD40FDB465' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 14, 4650, 2, 'B32428355BAE4313A0048392CA90941D', ld_start_date+8, 14.00, 8, 6, 'BC6551ED3E774FEEA3F8F3ADA1B6E68C' FROM DUAL
UNION ALL SELECT 4650, ln_dept_id, 15, 4650, 2, 'B32428355BAE4313A0048392CA90941D', ld_start_date+9, 14.00, 8, 6, '67A9E7CD526A4A0EAA5E48FDC37036D5' FROM DUAL;

END;
/