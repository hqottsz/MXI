/* Set up shifts and shift patterns */
DECLARE
   ln_shift_m shift_shift.shift_id%TYPE;
   ln_shift_a shift_shift.shift_id%TYPE;
   ln_shift_e shift_shift.shift_id%TYPE;
   ln_shift_pat_m user_shift_pattern.user_shift_pattern_id%TYPE;
   ln_shift_pat_a user_shift_pattern.user_shift_pattern_id%TYPE;
   ln_shift_pat_e user_shift_pattern.user_shift_pattern_id%TYPE;

BEGIN

INSERT INTO shift_shift(shift_db_id, shift_id, shift_cd, shift_name, start_hour, duration_qt, work_hours_qt, alt_id)
       SELECT 4650, (SELECT COUNT(*) FROM shift_shift)+1, 'MORN', 'Morning', 6.00, 8, 6, 'A667BE25DAB211E78B732F3AE1776FD2' FROM DUAL
UNION ALL
       SELECT 4650, (SELECT COUNT(*) FROM shift_shift)+2, 'AFT', 'Afternoon', 14.00, 8, 6, 'A899CE71DAB411E78A90D767F45E215A' FROM DUAL
UNION ALL
       SELECT 4650, (SELECT COUNT(*) FROM shift_shift)+3, 'EVEN', 'Evening', 22.00, 8, 6, 'A227CE71DAB411E78A90D767F45E2FDA' FROM DUAL;

-- Get shift ids
SELECT shift_id INTO ln_shift_m FROM shift_shift WHERE shift_cd = 'MORN';
SELECT shift_id INTO ln_shift_a FROM shift_shift WHERE shift_cd = 'AFT';
SELECT shift_id INTO ln_shift_e FROM shift_shift WHERE shift_cd = 'EVEN';

-- create hr shift
INSERT INTO org_hr_shift (hr_db_id, hr_id, hr_shift_id, day_dt, shift_id, shift_db_id, labour_skill_db_id, labour_skill_cd, loc_db_id, loc_id)
VALUES (4650, (select hr_id from org_hr join utl_user on org_hr.user_id = utl_user.user_id where utl_user.username = 'linetech'), (SELECT COUNT(*) FROM org_hr_shift)+1, TO_DATE('12/06/2017', 'MM/DD/YYYY'), ln_shift_a, 4650, 0, 'ENG', 4650, (SELECT loc_id FROM inv_loc WHERE loc_cd = 'SUPPLY1/USSTG'));

-- create hr shift patterns
INSERT INTO user_shift_pattern(user_shift_pattern_db_id, user_shift_pattern_id, user_shift_pattern_cd, user_shift_pattern_name, alt_id)
SELECT 4650, (SELECT COUNT(*) FROM user_shift_pattern)+1, 'SHIFTM', 'Morning', 'F2A71746DAB211E78B732F3AE1776FD2' FROM DUAL
UNION ALL
SELECT 4650, (SELECT COUNT(*) FROM user_shift_pattern)+2, 'SHIFTA', 'Afternoon', 'BA9F4D47DAB411E78B732F3AE1776FD2' FROM DUAL
UNION ALL
SELECT 4650, (SELECT COUNT(*) FROM user_shift_pattern)+3, 'SHIFTE', 'Evening', 'BA9F4D47DAB411E78B732F3AE1776FDA' FROM DUAL;

-- get shift pattern ids
SELECT user_shift_pattern_id INTO ln_shift_pat_m FROM user_shift_pattern WHERE user_shift_pattern_cd = 'SHIFTM';
SELECT user_shift_pattern_id INTO ln_shift_pat_a FROM user_shift_pattern WHERE user_shift_pattern_cd = 'SHIFTA';
SELECT user_shift_pattern_id INTO ln_shift_pat_e FROM user_shift_pattern WHERE user_shift_pattern_cd = 'SHIFTE';

INSERT INTO user_shift_pattern_day (user_shift_pattern_db_id, user_shift_pattern_id, user_shift_pattern_day_ord)
(SELECT 4650, ln_shift_pat_m, 1 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 2 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 3 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 4 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 5 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 6 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 7 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 1 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 2 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 3 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 4 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 5 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 6 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 7 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 1 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 2 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 3 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 4 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 5 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 6 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 7 FROM DUAL);

INSERT INTO user_shift_pattern_day_shift (user_shift_pattern_db_id, user_shift_pattern_id, user_shift_pattern_day_ord, shift_db_id, shift_id)
SELECT 4650, ln_shift_pat_m, 1, 4650, 1 FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 2, 4650, ln_shift_m FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 3, 4650, ln_shift_m FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 4, 4650, ln_shift_m FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 5, 4650, ln_shift_m FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 6, 4650, ln_shift_m FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_m, 7, 4650, ln_shift_m FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 1, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 2, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 3, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 4, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 5, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 6, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_a, 7, 4650, ln_shift_a FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 1, 4650, ln_shift_e FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 2, 4650, ln_shift_e FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 3, 4650, ln_shift_e FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 4, 4650, ln_shift_e FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 5, 4650, ln_shift_e FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 6, 4650, ln_shift_e FROM DUAL
UNION ALL SELECT 4650, ln_shift_pat_e, 7, 4650, ln_shift_e FROM DUAL;

END;
/