--liquibase formatted sql


--changeSet DEV-171:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PROD_PLAN_ID');
END;
/

--changeSet DEV-171:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PROD_PHASE_ID');
END;
/

--changeSet DEV-171:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PROD_MILESTONE_ID');
END;
/

--changeSet DEV-171:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PROD_WORK_AREA_ID');
END;
/

--changeSet DEV-171:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PROD_PHASE_CLASS_ID');
END;
/

--changeSet DEV-171:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('WORK_AREA_ID');
END;
/

--changeSet DEV-171:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PHASE_ID');
END;
/

--changeSet DEV-171:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('PHASE_CLASS_ID');
END;
/

--changeSet DEV-171:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('MILESTONE_ID');
END;
/ 

--changeSet DEV-171:10 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PROD_PLAN_ID';

--changeSet DEV-171:11 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PROD_PHASE_ID';    

--changeSet DEV-171:12 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PROD_MILESTONE_ID';

--changeSet DEV-171:13 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PROD_WORK_AREA_ID';      

--changeSet DEV-171:14 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PROD_PHASE_CLASS_ID';

--changeSet DEV-171:15 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'WORK_AREA_ID';    

--changeSet DEV-171:16 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PHASE_ID';

--changeSet DEV-171:17 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'PHASE_CLASS_ID';    

--changeSet DEV-171:18 stripComments:false
DELETE FROM utl_sequence
  WHERE sequence_cd = 'MILESTONE_ID';  

--changeSet DEV-171:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('GETPRODPLANPHASECOMPLETION');  
END;
/

--changeSet DEV-171:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PLAN', 'FK_EQPASSMBL_PRODPLAN');
END;
/

--changeSet DEV-171:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PLAN', 'FK_MIMDB_PRODPLAN');
END;
/

--changeSet DEV-171:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_PROD_PLAN_STATUS', 'FK_MIMDB_REFPRODPLANSTATUS');
END;
/

--changeSet DEV-171:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_PROJ_PHASE', 'FK_MIMDB_REFPROJPHASE');
END;
/

--changeSet DEV-171:24 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_PROD_PLAN_STATUS', 'FK_REFBITMAP_REFPRODPLANSTATUS');
END;
/

--changeSet DEV-171:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PHASE_CLASS', 'FK_REFTASKCLASS_PRODPHASECLASS');    
END;
/

--changeSet DEV-171:26 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE_CLASS', 'FK_REFTASKCLASS_SCHEDPHASECLAS');
END;
/

--changeSet DEV-171:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE_CLASS', 'FK_REFTASKSUBCLASS_SCHEDPHASEC');
END;
/

--changeSet DEV-171:28 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PLAN', 'FK_REFWORKTYPE_PRODPLAN');    
END;
/

--changeSet DEV-171:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_WORK_AREA', 'FK_SCHEDSTASK_SCHEDWORKAREA');
END;
/

--changeSet DEV-171:30 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_MILESTONE', 'FK_SCHEDSTASK_SCHEDMILESTONE');
END;
/

--changeSet DEV-171:31 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE', 'FK_SCHEDSTASK_SCHEDPHASE');   
END;
/

--changeSet DEV-171:32 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_PROD_PLAN_STATUS', 'FK_MIMRSTAT_REFPRODPLANSTATUS');
END;
/

--changeSet DEV-171:33 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PLAN', 'FK_MIMRSTAT_PRODPLAN');
END;
/

--changeSet DEV-171:34 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PHASE', 'FK_MIMRSTAT_PRODPHASE');   
END;
/

--changeSet DEV-171:35 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_PROJ_PHASE', 'FK_MIMRSTAT_REFPROJPHASE');
END;
/

--changeSet DEV-171:36 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_MILESTONE', 'FK_MIMRSTAT_PRODMILESTONE');
END;
/

--changeSet DEV-171:37 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_MILESTONE_COND', 'FK_MIMRSTAT_PRODMILESTONECOND');   
END;
/

--changeSet DEV-171:38 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_WORK_AREA', 'FK_MIMRSTAT_PRODWORKAREA');
END;
/

--changeSet DEV-171:39 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PHASE_CLASS', 'FK_MIMRSTAT_PRODPHASECLASS');
END;
/

--changeSet DEV-171:40 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_WORK_AREA_ZONE', 'FK_MIMRSTAT_PRODWORKAREAZONE');   
END;
/

--changeSet DEV-171:41 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_WORK_AREA', 'FK_MIMRSTAT_SCHEDWORKAREA');
END;
/

--changeSet DEV-171:42 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_WORK_AREA_ZONE', 'FK_MIMRSTAT_SCHEDWORKAREAZONE');
END;
/

--changeSet DEV-171:43 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_MILESTONE', 'FK_MIMRSTAT_SCHEDMILESTONE');   
END;
/

--changeSet DEV-171:44 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_MILESTONE_COND', 'FK_MIMRSTAT_SCHEDMILESTONECOND');
END;
/

--changeSet DEV-171:45 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE', 'FK_MIMRSTAT_SCHEDPHASE');
END;
/

--changeSet DEV-171:46 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE_CLASS', 'FK_MIMRSTAT_SCHEDPHASECLASS');   
END;
/

--changeSet DEV-171:47 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_WORK_AREA_ZONE', 'FK_EQPTASKZONE_PRODWORKAREAZON');
END;
/

--changeSet DEV-171:48 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_WORK_AREA_ZONE', 'FK_EQPTASKZONE_SCHEDWORKAREAZO');
END;
/

--changeSet DEV-171:49 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PHASE', 'FK_PRODPLAN_PRODPHASE');   
END;
/

--changeSet DEV-171:50 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_PRODPLAN_SCHEDSTASK');
END;
/

--changeSet DEV-171:51 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_MILESTONE', 'FK_PRODPLAN_PRODMILESTONE');
END;
/

--changeSet DEV-171:52 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_WORK_AREA', 'FK_PRODPLAN_PRODWORKAREA');   
END;
/

--changeSet DEV-171:53 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_APPLIEDPRODPLAN_SCHEDSTASK');
END;
/

--changeSet DEV-171:54 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PLAN', 'FK_REFPRODPLANSTATUS_PRODPLAN');
END;
/

--changeSet DEV-171:55 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PHASE_CLASS', 'FK_PRODPHASE_PRODPHASECLASS'); 
END;
/

--changeSet DEV-171:56 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_PHASE', 'FK_PROJPHASE_PRODPHASE');   
END;
/

--changeSet DEV-171:57 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE', 'FK_REFPROJPHASE_SCHED_PHASE');
END;
/

--changeSet DEV-171:58 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_MILESTONE', 'FK_REFTECHMILESTONE_PRODMILEST');
END;
/

--changeSet DEV-171:59 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_MILESTONE', 'FK_REFTECHMILESTONE_SCHEDMILES'); 
END;
/

--changeSet DEV-171:60 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_MILESTONE_COND', 'FK_PRODMILESTONE_PRODMILESTONE'); 
END;
/

--changeSet DEV-171:61 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_WORK_AREA_ZONE', 'FK_PRODWORKAREA_PRODWORKAREAZO');
END;
/

--changeSet DEV-171:62 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PROD_MILESTONE_COND', 'FK_ACCONDSETTING_PRODMILESTONE');
END;
/

--changeSet DEV-171:63 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_MILESTONE_COND', 'FK_ACCONDSETTING_SCHEDMILESTON'); 
END;
/

--changeSet DEV-171:64 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_WORK_AREA_ZONE', 'FK_SCHEDWORKAREA_SCHEDWORKAREA');
END;
/

--changeSet DEV-171:65 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_SCHEDWORKAREA_SCHEDSTASK');
END;
/

--changeSet DEV-171:66 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_MILESTONE_COND', 'FK_SCHEDMILESTONE_SCHEDMILESTO'); 
END;
/

--changeSet DEV-171:67 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_STARTSCHEDMILESTONE_SCHEDST'); 
END;
/

--changeSet DEV-171:68 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_ENDSCHEDMILESTONE_SCHEDSTAS');
END;
/

--changeSet DEV-171:69 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PHASE_CLASS', 'FK_SCHEDPHASE_SCHEDPHASECLASS');
END;
/

--changeSet DEV-171:70 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_SCHEDPHASE_SCHEDSTASK'); 
END;
/

--changeSet DEV-171:71 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_PART', 'FK_SCHEDSTASK_SCHEDPART');
END;
/

--changeSet DEV-171:72 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop any constraints, columns, and tables from the 6.7 PPC model if they exist.
-- The PROD_PHASE table may also have 6.7 PPC model columns but it will be dropped with the 7.1 PPC model.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_SCHEDPRODPHASE_SCHEDSTASK');
END;
/

--changeSet DEV-171:73 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STASK', 'FK_SCHEDPRODPHASE_SCHEDSTASKNR');
END;
/

--changeSet DEV-171:74 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_PROD_PHASE_STASK');
END;
/

--changeSet DEV-171:75 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'NR_SCHED_PROD_DB_ID');
END;
/

--changeSet DEV-171:76 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'NR_SCHED_PROD_ID');
END;
/

--changeSet DEV-171:77 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'NR_SCHED_PROD_PHASE_ID');
END;
/

--changeSet DEV-171:78 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'SCHED_PROD_DB_ID');
END;
/

--changeSet DEV-171:79 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'SCHED_PROD_ID');
END;
/

--changeSet DEV-171:80 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'SCHED_PROD_PHASE_ID');
END;
/

--changeSet DEV-171:81 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TASK_PROD_PLAN');
END;
/

--changeSet DEV-171:82 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TASK_PROD_PHASE');
END;
/

--changeSet DEV-171:83 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_PROD_PHASE');
END;
/

--changeSet DEV-171:84 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the tables from the 7.1 PPC model
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_MILESTONE_COND'); 
END;
/

--changeSet DEV-171:85 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_MILESTONE');
END;
/

--changeSet DEV-171:86 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_PHASE'); 
END;
/

--changeSet DEV-171:87 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_WORK_AREA'); 
END;
/

--changeSet DEV-171:88 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_PHASE_CLASS');
END;
/

--changeSet DEV-171:89 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_PLAN');
END;
/

--changeSet DEV-171:90 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('PROD_WORK_AREA_ZONE'); 
END;
/

--changeSet DEV-171:91 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_WORK_AREA');
END;
/

--changeSet DEV-171:92 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_WORK_AREA_ZONE'); 
END;
/

--changeSet DEV-171:93 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('REF_PROJ_PHASE'); 
END;
/

--changeSet DEV-171:94 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('REF_PROD_PLAN_STATUS');
END;
/

--changeSet DEV-171:95 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_MILESTONE');
END;
/

--changeSet DEV-171:96 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_MILESTONE_COND'); 
END;
/

--changeSet DEV-171:97 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_PHASE');
END;
/

--changeSet DEV-171:98 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('SCHED_PHASE_CLASS'); 
END;
/

--changeSet DEV-171:99 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'PROD_PLAN_DB_ID');
END;
/

--changeSet DEV-171:100 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'PROD_PLAN_ID');
END;
/

--changeSet DEV-171:101 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'APPLY_PROD_PLAN_DB_ID');
END;
/

--changeSet DEV-171:102 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'APPLY_PROD_PLAN_ID');
END;
/

--changeSet DEV-171:103 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'NR_MULT_QT');
END;
/

--changeSet DEV-171:104 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'AVG_BF_DURATION_QT');
END;
/

--changeSet DEV-171:105 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'START_SCHED_DB_ID');
END;
/

--changeSet DEV-171:106 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'START_SCHED_ID');
END;
/

--changeSet DEV-171:107 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'START_MILESTONE_ID');
END;
/

--changeSet DEV-171:108 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'END_SCHED_DB_ID');
END;
/

--changeSet DEV-171:109 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'END_SCHED_ID');
END;
/

--changeSet DEV-171:110 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'END_MILESTONE_ID');
END;
/

--changeSet DEV-171:111 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'PHASE_SCHED_DB_ID');
END;
/

--changeSet DEV-171:112 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'PHASE_SCHED_ID');
END;
/

--changeSet DEV-171:113 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'PHASE_ID');
END;
/

--changeSet DEV-171:114 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'WORKAREA_SCHED_DB_ID');
END;
/

--changeSet DEV-171:115 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'WORKAREA_SCHED_ID');
END;
/

--changeSet DEV-171:116 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'WORKAREA_ID');
END;
/

--changeSet DEV-171:117 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'MUST_START_AFTER_DT');
END;
/

--changeSet DEV-171:118 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('SCHED_STASK', 'MUST_END_BEFORE_DT');
END;
/

--changeSet DEV-171:119 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 1079;  

--changeSet DEV-171:120 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 1080;

--changeSet DEV-171:121 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 1083;    

--changeSet DEV-171:122 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 1084;

--changeSet DEV-171:123 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 1090;      

--changeSet DEV-171:124 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 1091;  

--changeSet DEV-171:125 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4124;

--changeSet DEV-171:126 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4125;    

--changeSet DEV-171:127 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4126;

--changeSet DEV-171:128 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4127;      

--changeSet DEV-171:129 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4128;      

--changeSet DEV-171:130 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4129;  

--changeSet DEV-171:131 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4130;

--changeSet DEV-171:132 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4219;    

--changeSet DEV-171:133 stripComments:false
DELETE FROM dpo_rep_table
  WHERE rep_table_id = 4220;

--changeSet DEV-171:134 stripComments:false
DELETE FROM dpo_rlm_rule
  WHERE rule_class_cd = 'EC_S_SCHED_STASK1' and rule_id = 'RULE_2';  

--changeSet DEV-171:135 stripComments:false
DELETE FROM dpo_rlm_rule
  WHERE rule_class_cd = 'EC_S_SCHED_STASK1' and rule_id = 'RULE_4';    

--changeSet DEV-171:136 stripComments:false
DELETE FROM dpo_rlm_rule
  WHERE rule_class_cd = 'EC_S_SCHED_STASK2' and rule_id = 'RULE_12';  

--changeSet DEV-171:137 stripComments:false
DELETE FROM dpo_rlm_rule
  WHERE rule_class_cd = 'EC_S_SCHED_STASK3' and rule_id = 'RULE_17';      

--changeSet DEV-171:138 stripComments:false
DELETE FROM dpo_rlm_rule
  WHERE rule_class_cd = 'EC_S_SCHED_STASK3' and rule_id = 'RULE_15';  

--changeSet DEV-171:139 stripComments:false
DELETE FROM dpo_rlm_rule
  WHERE rule_class_cd = 'EC_S_SCHED_STASK3' and rule_id = 'RULE_16';    

--changeSet DEV-171:140 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_MILESTONE' and filter_type = 'ASSEMBLY';

--changeSet DEV-171:141 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_MILESTONE_COND' and filter_type = 'ASSEMBLY';  

--changeSet DEV-171:142 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_PHASE' and filter_type = 'ASSEMBLY';  

--changeSet DEV-171:143 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_PHASE_CLASS' and filter_type = 'ASSEMBLY';  

--changeSet DEV-171:144 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_PLAN' and filter_type = 'ASSEMBLY';

--changeSet DEV-171:145 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_WORK_AREA' and filter_type = 'ASSEMBLY';  

--changeSet DEV-171:146 stripComments:false
DELETE FROM dpo_row_filtering
  WHERE table_name = 'PROD_WORK_AREA_ZONE' and filter_type = 'ASSEMBLY';  

--changeSet DEV-171:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_PART" add Constraint "FK_SCHEDSTASK_SCHEDPART" foreign key ("SCHED_DB_ID","SCHED_ID") references "SCHED_STASK" ("SCHED_DB_ID","SCHED_ID")  DEFERRABLE

');
END;
/	  

--changeSet DEV-171:148 stripComments:false
CREATE OR REPLACE VIEW vw_evt_stask(
      sched_db_id,
      sched_id,
      sched_sdesc,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      sub_event_ord,
      editor_hr_db_id,
      editor_hr_id,
      editor_hr_sdesc,
      sched_status_db_id,
      sched_status_cd,
      sched_status_user_cd,
      sched_reason_db_id,
      sched_reason_cd,
      sched_reason_user_cd,
      sched_priority_db_id,
      sched_priority_cd,
      sched_ldesc,
      sched_start_dt,
      sched_start_gdt,
      sched_end_dt,
      sched_end_gdt,
      sched_routine_bool,
      lrp_bool,
      sched_resource_sum_bool,
      warranty_note,
      actual_start_dt,
      actual_start_gdt,
      actual_end_dt,
      actual_end_gdt,
      ro_ref_sdesc,
      task_db_id,
      task_id,
      task_class_db_id,
      task_class_cd,
      task_subclass_db_id,
      task_subclass_cd,
      user_subclass_cd,
      task_originator_db_id,
      task_originator_cd,
      task_ref_sdesc,
      task_priority_db_id,
      task_priority_cd,
      task_instructions,
      work_loc_db_id,
      work_loc_id,
      work_loc_cd,
      work_dept_db_id,
      work_dept_id,
      work_dept_cd,
      action_ldesc,
      ro_vendor_db_id,
      ro_vendor_id,
      wo_ref_sdesc,
      ro_warranty_note,
      issued_dt,
      issued_gdt,
      ext_key_sdesc,
      doc_ref_sdesc,
      seq_err_bool,
      hist_bool,
      log_inv_no_db_id,
      log_inv_no_id,
      log_inv_no_sdesc,
      log_nh_inv_no_db_id,
      log_nh_inv_no_id,
      log_nh_inv_no_sdesc,
      log_assmbl_inv_no_db_id,
      log_assmbl_inv_no_id,
      log_assmbl_inv_no_sdesc,
      log_h_inv_no_db_id,
      log_h_inv_no_id,
      log_h_inv_no_sdesc,
      log_assmbl_db_id,
      log_assmbl_cd,
      log_assmbl_bom_id,
      log_assmbl_pos_id,
      log_assmbl_bom_cd,
      log_eqp_pos_cd,
      log_bom_part_db_id,
      log_bom_part_id,
      log_bom_part_cd,
      log_part_no_db_id,
      log_part_no_id,
      log_part_no_sdesc,
      log_part_no_oem,
      sched_dead_data_type_db_id,
      sched_dead_data_type_id,
      sched_dead_data_type_cd,
      sched_dead_qt,
      sched_dead_dt,
      sched_dead_usage_rem_qt,
      sched_dead_notify_qt,
      sched_dead_deviation_qt,
      soft_deadline_bool,
      contact_info_sdesc,
      parent_fault_db_id,
      parent_fault_id,
      parent_fault_sdesc,
      parent_incident_db_id,
      parent_incident_id,
      parent_incident_sdesc,
      nh_sched_db_id,
      nh_sched_id,
      nh_sched_sdesc,
      h_sched_db_id,
      h_sched_id,
      h_sched_sdesc,
      previous_sched_db_id,
      previous_sched_id,
      previous_sched_sdesc,
      previous_actual_end_gdt,
      mod_orig_part_no_db_id,
      mod_orig_part_no_id,
      mod_orig_part_no_sdesc,
      mod_orig_part_no_oem,
      inst_inv_no_db_id,
      inst_inv_no_id,
      inst_inv_no_sdesc,
      inst_hole_nh_inv_no_db_id,
      inst_hole_nh_inv_no_id,
      inst_hole_nh_inv_no_sdesc,
      inst_hole_assmbl_db_id,
      inst_hole_assmbl_cd,
      inst_hole_assmbl_bom_id,
      inst_hole_assmbl_pos_id,
      inst_hole_assmbl_bom_cd,
      inst_hole_eqp_pos_cd,
      barcode_sdesc,
      dup_jic_sched_db_id,
      dup_jic_sched_id,
      sched_min_plan_yield_pct,
      sched_est_duration_qt,
      issue_account_db_id,
      issue_account_id,
      issue_account_cd,
      issue_account_sdesc,
      class_mode_cd,
      etops_bool,
      domain_type_cd,
      precision_qt,
      ref_mult_qt,
      deferal_start_dt,
      eng_unit_cd,
      plan_by_dt
  ) AS
  SELECT /*+ rule */
        sched_stask.sched_db_id,
        sched_stask.sched_id,
        evt_event.event_sdesc,
        evt_event.bitmap_db_id,
        evt_event.bitmap_tag,
        ref_bitmap.bitmap_name,
        evt_event.sub_event_ord,
        evt_event.editor_hr_db_id,
        evt_event.editor_hr_id,
        DECODE( evt_event.editor_hr_db_id, NULL, NULL, org_hr.hr_cd || ' (' || utl_user.last_name || ', ' || utl_user.first_name || ')' ),
        evt_event.event_status_db_id,
        evt_event.event_status_cd,
        ref_event_status.user_status_cd,
        evt_event.event_reason_db_id,
        evt_event.event_reason_cd,
        ref_event_reason.user_reason_cd,
        evt_event.sched_priority_db_id,
        evt_event.sched_priority_cd,
        evt_event.event_ldesc,
        evt_event.sched_start_dt,
        evt_event.sched_start_gdt,
        evt_event.sched_end_dt,
        evt_event.sched_end_gdt,
        sched_stask.routine_bool,
        sched_stask.lrp_bool,
        sched_stask.resource_sum_bool,
        sched_stask.warranty_note,
        evt_event.actual_start_dt,
        evt_event.actual_start_gdt,
        evt_event.event_dt,
        evt_event.event_gdt,
        sched_stask.ro_ref_sdesc,
        sched_stask.task_db_id,
        sched_stask.task_id,
        sched_stask.task_class_db_id,
        sched_stask.task_class_cd,
        sched_stask.task_subclass_db_id,
        sched_stask.task_subclass_cd,
        ref_task_subclass.user_subclass_cd,
        sched_stask.task_originator_db_id,
        sched_stask.task_originator_cd,
        sched_stask.task_ref_sdesc,
        sched_stask.task_priority_db_id,
        sched_stask.task_priority_cd,
        sched_stask.instruction_ldesc,
        evt_loc.loc_db_id,
        evt_loc.loc_id,
        inv_loc.loc_cd,
        evt_dept.dept_db_id,
        evt_dept.dept_id,
        org_work_dept.dept_cd,
        getTaskActions( sched_stask.sched_db_id, sched_stask.sched_id ) AS action_ldesc,
        sched_stask.ro_vendor_db_id,
        sched_stask.ro_vendor_id,
        sched_stask.wo_ref_sdesc,
        sched_stask.warranty_note,
        sched_stask.issued_dt,
        sched_stask.issued_gdt,
        evt_event.ext_key_sdesc,
        evt_event.doc_ref_sdesc,
        evt_event.seq_err_bool,
        evt_event.hist_bool,
        evt_inv.inv_no_db_id,
        evt_inv.inv_no_id,
        inv_inv.inv_no_sdesc,
        evt_inv.nh_inv_no_db_id,
        evt_inv.nh_inv_no_id,
        nh_inv_inv.inv_no_sdesc,
        evt_inv.assmbl_inv_no_db_id,
        evt_inv.assmbl_inv_no_id,
        assmbl_inv_inv.inv_no_sdesc,
        evt_inv.h_inv_no_db_id,
        evt_inv.h_inv_no_id,
        h_inv_inv.inv_no_sdesc,
        evt_inv.assmbl_db_id,
        evt_inv.assmbl_cd,
        evt_inv.assmbl_bom_id,
        evt_inv.assmbl_pos_id,
        eqp_assmbl_bom.assmbl_bom_cd,
        eqp_assmbl_pos.eqp_pos_cd,
        evt_inv.bom_part_db_id,
        evt_inv.bom_part_id,
        eqp_bom_part.bom_part_cd,
        evt_inv.part_no_db_id,
        evt_inv.part_no_id,
        eqp_part_no.part_no_sdesc,
        eqp_part_no.part_no_oem,
        evt_sched_dead.data_type_db_id,
        evt_sched_dead.data_type_id,
        mim_data_type.data_type_cd,
        evt_sched_dead.sched_dead_qt,
        evt_sched_dead.sched_dead_dt,
        evt_sched_dead.usage_rem_qt,
        evt_sched_dead.notify_qt,
        evt_sched_dead.deviation_qt,
        sched_stask.soft_deadline_bool,
        evt_event.contact_info_sdesc,
        parent_fault_rel.event_db_id,
        parent_fault_rel.event_id,
        parent_fault_event.event_sdesc,
        parent_incident_rel.event_db_id,
        parent_incident_rel.event_id,
        parent_incident_event.event_sdesc,
        evt_event.nh_event_db_id,
        evt_event.nh_event_id,
        nh_sched_event.event_sdesc,
        evt_event.h_event_db_id,
        evt_event.h_event_id,
        h_sched_event.event_sdesc,
        previous_sched_rel.event_db_id,
        previous_sched_rel.event_id,
        previous_sched_event.event_sdesc,
        previous_sched_event.event_gdt,
        DECODE( sched_stask.task_class_cd, 'MOD', sched_stask.orig_part_no_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'MOD', sched_stask.orig_part_no_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'MOD', mod_eqp_part_no.part_no_sdesc, NULL ),
        DECODE( sched_stask.task_class_cd, 'MOD', mod_eqp_part_no.part_no_oem, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.inv_no_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.inv_no_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', inv_inv.inv_no_sdesc, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.nh_inv_no_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.nh_inv_no_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', nh_inv_inv.inv_no_sdesc, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_cd, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_bom_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_pos_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', eqp_assmbl_bom.assmbl_bom_cd, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', eqp_assmbl_pos.eqp_pos_cd, NULL ),
        sched_stask.barcode_sdesc,
        sched_stask.dup_jic_sched_db_id,
        sched_stask.dup_jic_sched_id,
        sched_stask.min_plan_yield_pct,
        sched_stask.est_duration_qt,
        sched_stask.issue_account_db_id,
        sched_stask.issue_account_id,
        fnc_account.account_cd,
        fnc_account.account_sdesc,
        ref_task_class.class_mode_cd,
        sched_stask.etops_bool,
   mim_data_type.domain_type_cd,
  mim_data_type.entry_prec_qt,
  ref_eng_unit.ref_mult_qt,
   evt_sched_dead.start_dt,
   ref_eng_unit.eng_unit_cd,
   sched_stask.plan_by_dt
   FROM sched_stask,
        evt_event,
        evt_inv,
        evt_sched_dead,
        evt_event_rel parent_incident_rel,
        evt_event_rel parent_fault_rel,
        evt_event_rel previous_sched_rel,
        ref_bitmap,
        ref_event_status,
        ref_event_reason,
        ref_task_priority,
        ref_task_subclass,
        eqp_assmbl_bom,
        eqp_assmbl_pos,
        eqp_bom_part,
        inv_inv,
        inv_inv nh_inv_inv,
        inv_inv h_inv_inv,
        inv_inv assmbl_inv_inv,
        eqp_part_no,
        eqp_part_no mod_eqp_part_no,
        evt_event previous_sched_event,
        evt_event parent_incident_event,
        evt_event parent_fault_event,
        evt_event nh_sched_event,
        evt_event h_sched_event,
        mim_data_type,
        (SELECT evt_dept.* FROM evt_dept, org_work_dept where evt_dept.dept_db_id = org_work_dept.dept_db_id AND evt_dept.dept_id = org_work_dept.dept_id AND org_work_dept.dept_type_cd <> 'CREW') evt_dept,
        org_work_dept,
        evt_loc,
        inv_loc,
        ref_task_class,
        org_hr,
        utl_user,
        fnc_account,
      ref_eng_unit
      WHERE evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id
        AND
        nh_sched_event.event_db_id (+)= evt_event.nh_event_db_id AND
        nh_sched_event.event_id    (+)= evt_event.nh_event_id
        AND
        h_sched_event.event_db_id = evt_event.h_event_db_id AND
        h_sched_event.event_id    = evt_event.h_event_id
        AND
        parent_incident_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
        parent_incident_rel.rel_event_id    (+)= sched_stask.sched_id AND
        parent_incident_rel.rel_type_cd     (+)= 'ITSK'
        AND
        parent_incident_event.event_db_id (+)= parent_incident_rel.event_db_id AND
        parent_incident_event.event_id    (+)= parent_incident_rel.event_id
        AND
        parent_fault_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
        parent_fault_rel.rel_event_id    (+)= sched_stask.sched_id AND
        parent_fault_rel.rel_type_cd     (+)= 'CORRECT'
        AND
        parent_fault_event.event_db_id (+)= parent_fault_rel.event_db_id AND
        parent_fault_event.event_id    (+)= parent_fault_rel.event_id
        AND
        previous_sched_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
        previous_sched_rel.rel_event_id    (+)= sched_stask.sched_id AND
        previous_sched_rel.rel_type_cd     (+)= 'DEPT'
        AND
        previous_sched_event.event_db_id (+)= previous_sched_rel.event_db_id AND
        previous_sched_event.event_id    (+)= previous_sched_rel.event_id
        AND
        ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
        ref_event_status.event_status_cd    = evt_event.event_status_cd
        AND
        ref_event_reason.event_reason_db_id (+)= evt_event.event_reason_db_id AND
        ref_event_reason.event_reason_cd    (+)= evt_event.event_reason_cd
        AND
        ref_bitmap.bitmap_db_id = evt_event.bitmap_db_id AND
        ref_bitmap.bitmap_tag   = evt_event.bitmap_tag
        AND
        evt_inv.event_db_id = sched_stask.sched_db_id AND
        evt_inv.event_id    = sched_stask.sched_id AND
        evt_inv.main_inv_bool = 1
        AND
        eqp_assmbl_bom.assmbl_db_id  (+)= evt_inv.assmbl_db_id AND
        eqp_assmbl_bom.assmbl_cd     (+)= evt_inv.assmbl_cd AND
        eqp_assmbl_bom.assmbl_bom_id (+)= evt_inv.assmbl_bom_id
        AND
        eqp_assmbl_pos.assmbl_db_id  (+)= evt_inv.assmbl_db_id AND
        eqp_assmbl_pos.assmbl_cd     (+)= evt_inv.assmbl_cd AND
        eqp_assmbl_pos.assmbl_bom_id (+)= evt_inv.assmbl_bom_id AND
        eqp_assmbl_pos.assmbl_pos_id (+)= evt_inv.assmbl_pos_id
        AND
        eqp_bom_part.bom_part_db_id (+)= evt_inv.bom_part_db_id AND
        eqp_bom_part.bom_part_id    (+)= evt_inv.bom_part_id
        AND
        inv_inv.inv_no_db_id (+)= evt_inv.inv_no_db_id AND
        inv_inv.inv_no_id    (+)= evt_inv.inv_no_id
        AND
        nh_inv_inv.inv_no_db_id (+)= evt_inv.nh_inv_no_db_id AND
        nh_inv_inv.inv_no_id    (+)= evt_inv.nh_inv_no_id
        AND
        h_inv_inv.inv_no_db_id (+)= evt_inv.h_inv_no_db_id AND
        h_inv_inv.inv_no_id    (+)= evt_inv.h_inv_no_id
        AND
        assmbl_inv_inv.inv_no_db_id (+)= evt_inv.assmbl_inv_no_db_id AND
        assmbl_inv_inv.inv_no_id    (+)= evt_inv.assmbl_inv_no_id
        AND
        eqp_part_no.part_no_db_id (+)= evt_inv.part_no_db_id AND
        eqp_part_no.part_no_id    (+)= evt_inv.part_no_id
        AND
        mod_eqp_part_no.part_no_db_id (+)= sched_stask.orig_part_no_db_id AND
        mod_eqp_part_no.part_no_id    (+)= sched_stask.orig_part_no_id
        AND
        evt_sched_dead.event_db_id (+)= sched_stask.sched_db_id AND
        evt_sched_dead.event_id    (+)= sched_stask.sched_id AND
        evt_sched_dead.sched_driver_bool (+)= 1
        AND
        mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
        mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id
        AND
        ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
        ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd
        AND
        org_work_dept.dept_db_id (+)= evt_dept.dept_db_id AND
        org_work_dept.dept_id    (+)= evt_dept.dept_id
        AND
        evt_dept.event_db_id (+)= sched_stask.sched_db_id AND
        evt_dept.event_id    (+)= sched_stask.sched_id
        AND
        evt_loc.event_db_id (+)= sched_stask.sched_db_id AND
        evt_loc.event_id    (+)= sched_stask.sched_id
        AND
        inv_loc.loc_db_id (+)= evt_loc.loc_db_id AND
        inv_loc.loc_id    (+)= evt_loc.loc_id
        AND
        ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
        ref_task_class.task_class_cd    = sched_stask.task_class_cd
        AND
        ref_task_priority.task_priority_db_id(+) = sched_stask.task_priority_db_id AND
        ref_task_priority.task_priority_cd(+)    = sched_stask.task_priority_cd
        AND
        ref_task_subclass.task_subclass_db_id  (+)= sched_stask.task_subclass_db_id AND
        ref_task_subclass.task_subclass_cd     (+)= sched_stask.task_subclass_cd
        AND
        org_hr.hr_db_id (+)= evt_event.editor_hr_db_id AND
        org_hr.hr_id    (+)= evt_event.editor_hr_id
        AND
        utl_user.user_id(+)= org_hr.user_id
        AND
        fnc_account.account_db_id (+)= sched_stask.issue_account_db_id AND
        fnc_account.account_id (+)= sched_stask.issue_account_id;                

--changeSet DEV-171:149 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.VIEW_DROP('VW_PROD_PLAN_JIC');  
END;
/  

--changeSet DEV-171:150 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.VIEW_DROP('VW_PROD_PLAN_TASKTREE');    
END;
/      