--liquibase formatted sql


--changeSet MX-17536:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_FAIL_SEV_DEFER" (
	"FAIL_SEV_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_SEV_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FAIL_SEV_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"FAIL_DEFER_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (FAIL_DEFER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"FAIL_DEFER_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_FAIL_SEV_DEFER" primary key ("FAIL_SEV_DB_ID","FAIL_SEV_CD","FAIL_DEFER_DB_ID","FAIL_DEFER_CD") 
) 
');
END;
/

--changeSet MX-17536:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FAIL_SEV_DEFER" add Constraint "FK_REFFAILDEFER_REFFAILSEVDFR" foreign key ("FAIL_DEFER_DB_ID","FAIL_DEFER_CD") references "REF_FAIL_DEFER" ("FAIL_DEFER_DB_ID","FAIL_DEFER_CD")  DEFERRABLE
');
END;
/

--changeSet MX-17536:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FAIL_SEV_DEFER" add Constraint "FK_REFFAILSEV_REFFAILSEVDFR" foreign key ("FAIL_SEV_DB_ID","FAIL_SEV_CD") references "REF_FAIL_SEV" ("FAIL_SEV_DB_ID","FAIL_SEV_CD")  DEFERRABLE
');
END;
/

--changeSet MX-17536:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_FAIL_SEV_DEFER" add Constraint "FK_MIMRSTAT_REFFAILSEVDFR" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-17536:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_FAIL_SEV_DEFER" BEFORE INSERT
   ON "REF_FAIL_SEV_DEFER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
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

--changeSet MX-17536:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_FAIL_SEV_DEFER" BEFORE UPDATE
   ON "REF_FAIL_SEV_DEFER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
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

--changeSet MX-17536:7 stripComments:false
INSERT INTO
   utl_rule
   (
      rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id
   )
   SELECT 'FAULT-00001', 'FAULT', 'Fault Messages', 'Fault deferral reference must have a valid combination of fault severity and deferral class.','1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_rule WHERE rule_id = 'FAULT-00001' );          

--changeSet MX-17536:8 stripComments:false
INSERT INTO
   utl_rule
   (
      rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id
   )
   SELECT 'FAULT-00002', 'FAULT', 'Fault Messages', 'Fault definition must have a valid combination of fault severity and deferral class.','1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_rule WHERE rule_id = 'FAULT-00002' );         

--changeSet MX-17536:9 stripComments:false
INSERT INTO
   utl_rule
   (
      rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id
   )
   SELECT 'FAULT-00003', 'FAULT', 'Fault Messages', 'Fault must have a valid combination of fault severity and deferral class.','1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_rule WHERE rule_id = 'FAULT-00003' );               

--changeSet MX-17536:10 stripComments:false
      update utl_rule set rule_sql =
                       'SELECT task_task.task_db_id, '                                       || CHR (10) ||
                       '       task_task.task_id '                                           || CHR (10) ||
                       '  FROM fail_mode, '                                                  || CHR (10) ||
                       '       task_task, '                                                  || CHR (10) ||
                       '       task_labour_list '                                            || CHR (10) ||
                       ' WHERE fail_mode.task_db_id   = task_task.task_db_id '               || CHR (10) ||
                       '   AND fail_mode.task_id      = task_task.task_id '                  || CHR (10) ||
                       '   AND task_task.task_db_id   = task_labour_list.task_db_id (+) '    || CHR (10) ||
                       '   AND task_task.task_id      = task_labour_list.task_id (+) '       || CHR (10) ||
               'AND NOT EXISTS (SELECT 1 '                                                   || CHR (10) ||
                       '          FROM task_labour_list '                                    || CHR (10) ||
                       '         WHERE task_task.task_db_id = task_labour_list.task_db_id '  || CHR (10) ||
                       '           AND task_task.task_id    = task_labour_list.task_id) '    || CHR (10)
 where rule_id = 'BSS-01602';  

--changeSet MX-17536:11 stripComments:false
-- All companies have to have one and only one RESTIRCTED sub-org
update utl_rule set rule_sql =
                  'SELECT company_org.org_db_id,  '                                               || CHR (10) ||
                  '       company_org.org_id, '                                                   || CHR (10) ||
                  '       company_org.org_type_cd, '                                              || CHR (10) ||
                  '       company_org.code_mdesc, '                                               || CHR (10) ||
                  '       company_org.org_cd,'                                                    || CHR (10) ||
                  '       (SELECT count(*)'                                                       || CHR (10) ||
                  '                    FROM org_org '                                             || CHR (10) ||
                  '                  WHERE '                                                      || CHR (10) ||
                  '                     org_org.company_org_db_id=company_org.org_db_id AND'      || CHR (10) ||
                  '                     org_org.company_org_id=company_org.org_id'                || CHR (10) ||
                  '                     AND'                                                      || CHR (10) ||
                  '                     org_org.org_type_db_id=0 AND'                             || CHR (10) ||
                  '                     org_org.org_type_cd = ''RESTRICTED'') num_of_RESTRICTED_org'|| CHR (10) ||
                  'FROM org_org company_org     '                                                 || CHR (10) ||
                  'WHERE '                                                                        || CHR (10) ||
                  '      company_org.company_org_db_id = company_org.org_db_id AND'               || CHR (10) ||
                  '      company_org.company_org_id = company_org.org_id'                         || CHR (10) ||
                  '      AND'                                                                     || CHR (10) ||
                  '      (SELECT count(*)'                                                        || CHR (10) ||
                  '                    FROM org_org '                                             || CHR (10) ||
                  '                  WHERE '                                                      || CHR (10) ||
                  '                     org_org.company_org_db_id=company_org.org_db_id AND'      || CHR (10) ||
                  '                     org_org.company_org_id=company_org.org_id'                || CHR (10) ||
                  '                     AND'                                                      || CHR (10) ||
                  '                     org_org.org_type_db_id=0 AND'                             || CHR (10) ||
                  '                     org_org.org_type_cd = ''RESTRICTED'') <> 1 '                || CHR (10)
where rule_id = 'ORG-00001';

--changeSet MX-17536:12 stripComments:false
-- All human resources must have one and only one organization marked as primary
update utl_rule set rule_sql =
                  'SELECT org_hr.hr_db_id, '                             || CHR (10) ||
                  '       org_hr.hr_id, '                                || CHR (10) ||
                  '       org_hr.user_id, '                              || CHR (10) ||
                  '       org_hr.hr_cd'                                  || CHR (10) ||
                  '   FROM org_hr'                                       || CHR (10) ||
                  'WHERE '                                               || CHR (10) ||
                  '     (SELECT COUNT(*) '                               || CHR (10) ||
                  '            FROM org_org_hr '                         || CHR (10) ||
                  '      WHERE org_org_hr.hr_db_id=org_hr.hr_db_id AND'  || CHR (10) ||
                  '            org_org_hr.hr_id=org_hr.hr_id AND '       || CHR (10) ||
                  '            org_org_hr.default_org_bool=1) != 1'      || CHR (10)
where rule_id = 'ORG-00003';

--changeSet MX-17536:13 stripComments:false
-- All companies have to reference itself in the company link
update utl_rule set rule_sql =
                  'SELECT company_org.org_db_id,  '                                   || CHR (10) ||
                  '       company_org.org_id, '                                       || CHR (10) ||
                  '       company_org.org_type_cd, '                                  || CHR (10) ||
                  '       company_org.code_mdesc, '                                   || CHR (10) ||
                  '       company_org.org_cd'                                         || CHR (10) ||
                  'FROM org_org company_org,'                                         || CHR (10) ||
                  '     ref_org_type'                                                 || CHR (10) ||
                  'WHERE '                                                            || CHR (10) ||
                  '      ref_org_type.company_bool = 1'                               || CHR (10) ||
                  '      AND'                                                         || CHR (10) ||
                  '      company_org.org_type_db_id = ref_org_type.org_type_db_id AND'|| CHR (10) ||
                  '      company_org.org_type_cd    = ref_org_type.org_type_cd'       || CHR (10) ||
                  '      AND NOT'                                                     || CHR (10) ||
                  '      (company_org.company_org_db_id = company_org.org_db_id AND'  || CHR (10) ||
                  '       company_org.company_org_id = company_org.org_id)'           || CHR (10)
where rule_id = 'ORG-00004';

--changeSet MX-17536:14 stripComments:false
-- User should not be scheduled with primary labour skills outwith the user's list of skills
UPDATE utl_rule SET rule_sql =
                  'SELECT                                  '                                                           || CHR (10) ||
                  '   org_hr_schedule.hr_db_id,            '                                                           || CHR (10) ||
                  '   org_hr_schedule.hr_id,               '                                                           || CHR (10) ||
                  '   org_hr_schedule.hr_schedule_id,      '                                                           || CHR (10) ||
                  '   org_hr_schedule.labour_skill_db_id,  '                                                           || CHR (10) ||
                  '   org_hr_schedule.labour_skill_cd,     '                                                           || CHR (10) ||
                  '   org_hr_schedule.schedule_db_id,      '                                                           || CHR (10) ||
                  '   org_hr_schedule.schedule_id          '                                                           || CHR (10) ||
                  'FROM                                    '                                                           || CHR (10) ||
                  '   org_hr_schedule                      '                                                           || CHR (10) ||
                  'LEFT JOIN org_hr_qual ON org_hr_qual.hr_db_id           = org_hr_schedule.hr_db_id            AND ' || CHR (10) ||
                  '                         org_hr_qual.hr_id              = org_hr_schedule.hr_id               AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_db_id = org_hr_schedule.labour_skill_db_id  AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_cd    = org_hr_schedule.labour_skill_cd         ' || CHR (10) ||
                  'WHERE org_hr_qual.hr_db_id IS NULL      '                                                           || CHR (10) ||
                  'AND   TRUNC( org_hr_schedule.end_dt ) >= TRUNC( SYSDATE ) '
WHERE rule_id = 'HR-00001';

--changeSet MX-17536:15 stripComments:false
UPDATE utl_rule SET rule_sql =
                  'SELECT                                                           ' || CHR (10) ||
                  '    this_inv.inv_no_db_id , this_inv.inv_no_id ,                 ' || CHR (10) ||
                  '    this_inv.part_no_db_id, this_inv.part_no_id,                 ' || CHR (10) ||
                  '    this_inv.serial_no_oem                                       ' || CHR (10) ||
                  'FROM                                                             ' || CHR (10) ||
                  '   inv_inv this_inv,                                             ' || CHR (10) ||
                  '   inv_inv that_inv                                              ' || CHR (10) ||
                  'WHERE                                                            ' || CHR (10) ||
                  '   this_inv.part_no_db_id   = that_inv.part_no_db_id   AND       ' || CHR (10) ||
                  '   this_inv.part_no_id      = that_inv.part_no_id                ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.serial_no_oem   = that_inv.serial_no_oem             ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.inv_no_db_id   != that_inv.inv_no_db_id    AND       ' || CHR (10) ||
                  '   this_inv.inv_no_id      != that_inv.inv_no_id                 ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.serial_no_oem  NOT IN ( ''XXX'', ''UNK'', ''N/A'' )  ' || CHR (10) ||
                  'ORDER BY                                                         ' || CHR (10) ||
                  '   this_inv.serial_no_oem,                                       ' || CHR (10) ||
                  '   this_inv.part_no_db_id,                                       ' || CHR (10) ||
                  '   this_inv.part_no_id,                                          ' || CHR (10) ||
                  '   this_inv.inv_no_db_id,                                        ' || CHR (10) ||
                  '   this_inv.inv_no_id                                            ' || CHR (10)
WHERE rule_id = 'INV-00001';

--changeSet MX-17536:16 stripComments:false
-- Fault deferral reference must have a valid combination of fault severity and deferral class.
UPDATE utl_rule SET rule_sql =
                  'SELECT                                                                               '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_ref_db_id,                                             '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_ref_id,                                                '|| CHR (10) ||
                  '    fail_defer_ref.defer_ref_sdesc,                                                  '|| CHR (10) ||
                  '    fail_defer_ref.assmbl_db_id,                                                     '|| CHR (10) ||
                  '    fail_defer_ref.assmbl_cd,                                                        '|| CHR (10) ||
                  '    fail_defer_ref.fail_sev_db_id,                                                   '|| CHR (10) ||
                  '    fail_defer_ref.fail_sev_cd,                                                      '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_db_id,                                                 '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_cd                                                     '|| CHR (10) ||
                  'FROM                                                                                 '|| CHR (10) ||
                  '   fail_defer_ref,                                                                   '|| CHR (10) ||
                  '   ref_fail_sev                                                                      '|| CHR (10) ||
                  'WHERE                                                                                '|| CHR (10) ||
                  '    ref_fail_sev.fail_sev_db_id = fail_defer_ref.fail_sev_db_id AND                  '|| CHR (10) ||
                  '    ref_fail_sev.fail_sev_cd    = fail_defer_ref.fail_sev_cd AND                     '|| CHR (10) ||
                  '    ref_fail_sev.sev_type_cd    = ''MEL''                                            '|| CHR (10) ||
                  '    AND                                                                              '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_db_id IS NOT NULL                                      '|| CHR (10) ||
                  '    AND NOT EXISTS                                                                   '|| CHR (10) ||
                  '      (SELECT                                                                        '|| CHR (10) ||
                  '          1                                                                          '|| CHR (10) ||
                  '       FROM                                                                          '|| CHR (10) ||
                  '          ref_fail_sev_defer                                                         '|| CHR (10) ||
                  '       WHERE                                                                         '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_sev_db_id = fail_defer_ref.fail_sev_db_id AND      '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_sev_cd    = fail_defer_ref.fail_sev_cd             '|| CHR (10) ||
                  '          AND                                                                        '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND  '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_defer_cd    = fail_defer_ref.fail_defer_cd)        '|| CHR (10) ||
                  'ORDER BY                                                                             '|| CHR (10) ||
                  '    fail_defer_ref.fail_sev_cd                                                       '|| CHR (10) 
WHERE rule_id = 'FAULT-00001';

--changeSet MX-17536:17 stripComments:false
-- Fault definition must have a valid combination of fault severity and deferral class.
UPDATE utl_rule SET rule_sql =
                'SELECT                                                                            '|| CHR (10) ||
                '    fail_mode.fail_mode_db_id,                                                    '|| CHR (10) ||
                '    fail_mode.fail_mode_id,                                                       '|| CHR (10) ||
                '    fail_mode.assmbl_db_id,                                                       '|| CHR (10) ||
                '    fail_mode.assmbl_cd,                                                          '|| CHR (10) ||
                '    fail_mode.fail_sev_db_id,                                                     '|| CHR (10) ||
                '    fail_mode.fail_sev_cd,                                                        '|| CHR (10) ||
                '    fail_mode.fail_defer_db_id,                                                   '|| CHR (10) ||
                '    fail_mode.fail_defer_cd                                                       '|| CHR (10) ||
                'FROM                                                                              '|| CHR (10) ||
                '    fail_mode,                                                                    '|| CHR (10) ||
                '    ref_fail_sev                                                                  '|| CHR (10) ||
                'WHERE                                                                             '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_db_id = fail_mode.fail_sev_db_id AND                   '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_cd    = fail_mode.fail_sev_cd AND                      '|| CHR (10) ||
                '     ref_fail_sev.sev_type_cd    = ''MEL''                                        '|| CHR (10) ||
                '     AND                                                                          '|| CHR (10) ||
                '     fail_mode.fail_defer_db_id IS NOT NULL                                       '|| CHR (10) ||
                '     AND NOT EXISTS                                                               '|| CHR (10) ||
                '         (SELECT                                                                  '|| CHR (10) ||
                '              1                                                                   '|| CHR (10) ||
                '          FROM                                                                    '|| CHR (10) ||
                '             ref_fail_sev_defer                                                   '|| CHR (10) ||
                '          WHERE                                                                   '|| CHR (10) ||
                '             ref_fail_sev_defer.fail_sev_db_id = fail_mode.fail_sev_db_id AND     '|| CHR (10) ||
                '             ref_fail_sev_defer.fail_sev_cd    = fail_mode.fail_sev_cd            '|| CHR (10) ||
                '             AND                                                                  '|| CHR (10) ||
                '             ref_fail_sev_defer.fail_defer_db_id = fail_mode.fail_defer_db_id AND '|| CHR (10) ||
                '            ref_fail_sev_defer.fail_defer_cd     = fail_mode.fail_defer_cd)       '|| CHR (10) ||
                'ORDER BY fail_mode.fail_sev_cd                                                    '|| CHR (10)             
WHERE rule_id = 'FAULT-00002';

--changeSet MX-17536:18 stripComments:false
-- Fault must have a valid combination of fault severity and deferral class.
UPDATE utl_rule SET rule_sql =
                'SELECT                                                                            '|| CHR (10) ||
                '    sd_fault.fault_db_id,                                                         '|| CHR (10) ||                                                   
                '    sd_fault.fault_id,                                                            '|| CHR (10) ||
                '    sched_stask.barcode_sdesc,                                                    '|| CHR (10) ||
                '    sd_fault.fail_sev_db_id,                                                      '|| CHR (10) ||
                '    sd_fault.fail_sev_cd,                                                         '|| CHR (10) ||
                '    sd_fault.fail_defer_db_id,                                                    '|| CHR (10) ||
                '    sd_fault.fail_defer_cd                                                        '|| CHR (10) ||
                '  FROM                                                                            '|| CHR (10) ||
                '     sd_fault,                                                                    '|| CHR (10) ||
                '     ref_fail_sev,                                                                '|| CHR (10) ||
                '     sched_stask                                                                  '|| CHR (10) ||
                ' WHERE                                                                            '|| CHR (10) ||        
                '     sched_stask.fault_db_id = sd_fault.fault_db_id AND                           '|| CHR (10) ||
                '     sched_stask.fault_id    = sd_fault.fault_id                                  '|| CHR (10) ||
                '     AND                                                                          '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_db_id = sd_fault.fail_sev_db_id AND                    '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_cd    = sd_fault.fail_sev_cd AND                       '|| CHR (10) ||
                '     ref_fail_sev.sev_type_cd    = ''MEL''                                        '|| CHR (10) ||
                '     AND                                                                          '|| CHR (10) || 
                '     sd_fault.fail_defer_db_id IS NOT NULL                                        '|| CHR (10) ||
                '     AND NOT EXISTS                                                               '|| CHR (10) ||
                '         (SELECT                                                                  '|| CHR (10) ||
                '              1                                                                   '|| CHR (10) ||
                '          FROM                                                                    '|| CHR (10) || 
                '              ref_fail_sev_defer                                                  '|| CHR (10) ||
                '          WHERE                                                                   '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_sev_db_id = sd_fault.fail_sev_db_id  AND    '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_sev_cd    = sd_fault.fail_sev_cd            '|| CHR (10) ||
                '              AND                                                                 '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_defer_db_id = sd_fault.fail_defer_db_id AND '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_defer_cd    = sd_fault.fail_defer_cd        '|| CHR (10) ||
                '          )                                                                       '|| CHR (10) ||     
                ' ORDER BY sd_fault.fail_sev_cd                                                    '|| CHR (10) 
WHERE rule_id = 'FAULT-00003';

--changeSet MX-17536:19 stripComments:false
-- Inserts into ref_fail_sev_defer table mapping between all fault severities from ref_fail_sev table with severity type 'MEL'
-- and all deferral classes from ref_fail_defer table.
INSERT INTO ref_fail_sev_defer
  (fail_sev_db_id,
   fail_sev_cd,
   fail_defer_db_id,
   fail_defer_cd,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user)
  SELECT ref_fail_sev.fail_sev_db_id,
         ref_fail_sev.fail_sev_cd,
         ref_fail_defer.fail_defer_db_id,
         ref_fail_defer.fail_defer_cd,
         0,
         TO_DATE('2010-03-15', 'YYYY-MM-DD'),
         TO_DATE('2010-03-15', 'YYYY-MM-DD'),
         10,
         'MXI'
    FROM ref_fail_sev, ref_fail_defer, dual
   WHERE ref_fail_sev.sev_type_cd = 'MEL'
     AND NOT EXISTS (SELECT 1 FROM ref_fail_sev_defer 
                      WHERE fail_sev_db_id = ref_fail_sev.fail_sev_db_id 
                        AND fail_sev_cd = ref_fail_sev.fail_sev_cd
                        AND fail_defer_db_id = ref_fail_defer.fail_defer_db_id 
                        AND fail_defer_cd = ref_fail_defer.fail_defer_cd);

--changeSet MX-17536:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DBMS_STATS.GATHER_TABLE_STATS(USER,'REF_FAIL_SEV_DEFER');
END;
/