--liquibase formatted sql


--changeSet MTX-394:1 stripComments:false
ALTER TRIGGER tubr_ppc_plan DISABLE;

--changeSet MTX-394:2 stripComments:false
ALTER TRIGGER tubr_ppc_hr DISABLE;

--changeSet MTX-394:3 stripComments:false
ALTER TRIGGER tubr_ppc_loc DISABLE;

--changeSet MTX-394:4 stripComments:false
ALTER TRIGGER tubr_ppc_loc_exclude DISABLE;

--changeSet MTX-394:5 stripComments:false
ALTER TRIGGER tubr_ppc_opt_status DISABLE;

--changeSet MTX-394:6 stripComments:false
ALTER TRIGGER tubr_ppc_wp DISABLE;

--changeSet MTX-394:7 stripComments:false
ALTER TRIGGER tubr_ppc_activity DISABLE;

--changeSet MTX-394:8 stripComments:false
ALTER TRIGGER tubr_ppc_crew DISABLE;

--changeSet MTX-394:9 stripComments:false
ALTER TRIGGER tubr_ppc_dependency DISABLE;

--changeSet MTX-394:10 stripComments:false
ALTER TRIGGER tubr_ppc_hr_lic DISABLE;

--changeSet MTX-394:11 stripComments:false
ALTER TRIGGER tubr_ppc_hr_shift_plan DISABLE;

--changeSet MTX-394:12 stripComments:false
ALTER TRIGGER tubr_ppc_hr_slot DISABLE;

--changeSet MTX-394:13 stripComments:false
ALTER TRIGGER tubr_ppc_labour DISABLE;

--changeSet MTX-394:14 stripComments:false
ALTER TRIGGER tubr_ppc_labour_role DISABLE;

--changeSet MTX-394:15 stripComments:false
ALTER TRIGGER tubr_ppc_loc_capacity DISABLE;

--changeSet MTX-394:16 stripComments:false
ALTER TRIGGER tubr_ppc_loc_exclude DISABLE;

--changeSet MTX-394:17 stripComments:false
ALTER TRIGGER tubr_ppc_milestone DISABLE;

--changeSet MTX-394:18 stripComments:false
ALTER TRIGGER tubr_ppc_milestone_cond DISABLE;

--changeSet MTX-394:19 stripComments:false
ALTER TRIGGER tubr_ppc_phase DISABLE;

--changeSet MTX-394:20 stripComments:false
ALTER TRIGGER tubr_ppc_phase_class DISABLE;

--changeSet MTX-394:21 stripComments:false
ALTER TRIGGER tubr_ppc_planning_type DISABLE;

--changeSet MTX-394:22 stripComments:false
ALTER TRIGGER tubr_ppc_planning_type_skill DISABLE;

--changeSet MTX-394:23 stripComments:false
ALTER TRIGGER tubr_ppc_publish DISABLE;

--changeSet MTX-394:24 stripComments:false
ALTER TRIGGER tubr_ppc_publish_failure DISABLE;

--changeSet MTX-394:25 stripComments:false
ALTER TRIGGER tubr_ppc_task DISABLE;

--changeSet MTX-394:26 stripComments:false
ALTER TRIGGER tubr_ppc_task_defn DISABLE;

--changeSet MTX-394:27 stripComments:false
ALTER TRIGGER tubr_ppc_task_defn_map DISABLE;

--changeSet MTX-394:28 stripComments:false
ALTER TRIGGER tubr_ppc_work_area DISABLE;

--changeSet MTX-394:29 stripComments:false
ALTER TRIGGER tubr_ppc_work_area_crew DISABLE;

--changeSet MTX-394:30 stripComments:false
ALTER TRIGGER tubr_ppc_work_area_zone DISABLE;

--changeSet MTX-394:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add temporary columns
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLAN ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLAN ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:33 stripComments:false
UPDATE ppc_plan SET ppc_db_id_old=ppc_db_id, ppc_id_old=ppc_id;

--changeSet MTX-394:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR ADD(
         PPC_HR_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR ADD(
         PPC_HR_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:38 stripComments:false
UPDATE
   ppc_hr
SET
   ppc_hr_db_id_old=ppc_hr_db_id,
   ppc_hr_id_old=ppc_hr_id,
   ppc_db_id_old=ppc_db_id,
   ppc_id_old=ppc_id;

--changeSet MTX-394:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC ADD(
         PPC_LOC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC ADD(
         PPC_LOC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC ADD(
         NH_PPC_LOC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC ADD(
         NH_PPC_LOC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:45 stripComments:false
UPDATE
   ppc_loc
SET
   ppc_loc_db_id_old=ppc_loc_db_id,
   ppc_loc_id_old=ppc_loc_id,
   ppc_db_id_old=ppc_db_id,
   ppc_id_old=ppc_id,
   nh_ppc_loc_db_id_old=nh_ppc_loc_db_id,
   nh_ppc_loc_id_old=nh_ppc_loc_id;

--changeSet MTX-394:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_EXCLUDE ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_EXCLUDE ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:48 stripComments:false
UPDATE ppc_loc_exclude SET ppc_db_id_old=ppc_db_id, ppc_id_old=ppc_id;

--changeSet MTX-394:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_OPT_STATUS ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_OPT_STATUS ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:51 stripComments:false
UPDATE
   ppc_opt_status
SET
   ppc_db_id_old=ppc_db_id,
   ppc_id_old=ppc_id;

--changeSet MTX-394:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         PPC_LOC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         PPC_LOC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         NR_PHASE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         NR_PHASE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         TEMPLATE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WP ADD(
         TEMPLATE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:62 stripComments:false
UPDATE
   ppc_wp
SET
   ppc_wp_db_id_old=ppc_wp_db_id,
   ppc_wp_id_old=ppc_wp_id,
   ppc_db_id_old=ppc_db_id,
   ppc_id_old=ppc_id,
   ppc_loc_db_id_old=ppc_loc_db_id,
   ppc_loc_id_old=ppc_loc_id,
   nr_phase_db_id_old=nr_phase_db_id,
   nr_phase_id_old=nr_phase_id,
   template_db_id_old=template_db_id,
   template_id_old=template_id;

--changeSet MTX-394:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_ACTIVITY ADD(
         PPC_ACTIVITY_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_ACTIVITY ADD(
         PPC_ACTIVITY_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_ACTIVITY ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_ACTIVITY ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:67 stripComments:false
UPDATE
   ppc_activity
SET
   ppc_activity_db_id_old = ppc_activity_db_id,
   ppc_activity_id_old    = ppc_activity_id,
   ppc_wp_db_id_old       = ppc_wp_db_id,
   ppc_wp_id_old          = ppc_wp_id;

--changeSet MTX-394:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_CREW ADD(
         PPC_CREW_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_CREW ADD(
         PPC_CREW_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_CREW ADD(
         PPC_LOC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_CREW ADD(
         PPC_LOC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:72 stripComments:false
UPDATE
   ppc_crew
SET
   ppc_crew_db_id_old = ppc_crew_db_id,
   ppc_crew_id_old    = ppc_crew_id,
   ppc_loc_db_id_old       = ppc_loc_db_id,
   ppc_loc_id_old          = ppc_loc_id;

--changeSet MTX-394:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_DEPENDENCY ADD(
         FROM_ACTIVITY_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_DEPENDENCY ADD(
         FROM_ACTIVITY_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_DEPENDENCY ADD(
         TO_ACTIVITY_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_DEPENDENCY ADD(
         TO_ACTIVITY_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:77 stripComments:false
UPDATE
   ppc_dependency
SET
   from_activity_db_id_old = from_activity_db_id,
   from_activity_id_old    = from_activity_id,
   to_activity_db_id_old   = to_activity_db_id,
   to_activity_id_old      = to_activity_id;

--changeSet MTX-394:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_LIC ADD(
         PPC_HR_LIC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_LIC ADD(
         PPC_HR_LIC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_LIC ADD(
         PPC_HR_SHIFT_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_LIC ADD(
         PPC_HR_SHIFT_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:82 stripComments:false
UPDATE
   ppc_hr_lic
SET
   ppc_hr_lic_db_id_old=ppc_hr_lic_db_id,
   ppc_hr_lic_id_old=ppc_hr_lic_id,
   ppc_hr_shift_db_id_old=ppc_hr_shift_db_id,
   ppc_hr_shift_id_old=ppc_hr_shift_id;

--changeSet MTX-394:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_HR_SHIFT_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_HR_SHIFT_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_HR_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_HR_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_CREW_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_CREW_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_CAPACITY_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_CAPACITY_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_LOC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SHIFT_PLAN ADD(
         PPC_LOC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:93 stripComments:false
UPDATE
   ppc_hr_shift_plan
SET
   ppc_hr_shift_db_id_old=ppc_hr_shift_db_id,
   ppc_hr_shift_id_old=ppc_hr_shift_id,
   ppc_hr_db_id_old=ppc_hr_db_id,
   ppc_hr_id_old=ppc_hr_id,
   ppc_crew_db_id_old=ppc_crew_db_id,
   ppc_crew_id_old=ppc_crew_id,
   ppc_capacity_db_id_old=ppc_capacity_db_id,
   ppc_capacity_id_old=ppc_capacity_id,
   ppc_loc_db_id_old=ppc_loc_db_id,
   ppc_loc_id_old=ppc_loc_id;

--changeSet MTX-394:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SLOT ADD(
         PPC_HR_SLOT_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SLOT ADD(
         PPC_HR_SLOT_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SLOT ADD(
         LABOUR_ROLE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SLOT ADD(
         LABOUR_ROLE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SLOT ADD(
         PPC_HR_SHIFT_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_HR_SLOT ADD(
         PPC_HR_SHIFT_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:100 stripComments:false
UPDATE
   ppc_hr_slot
SET
   ppc_hr_slot_db_id_old=ppc_hr_slot_db_id,
   ppc_hr_slot_id_old=ppc_hr_slot_id,
   labour_role_db_id_old=labour_role_db_id,
   labour_role_id_old=labour_role_id,
   ppc_hr_shift_db_id_old=ppc_hr_shift_db_id,
   ppc_hr_shift_id_old=ppc_hr_shift_id;

--changeSet MTX-394:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR ADD(
         LABOUR_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR ADD(
         LABOUR_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR ADD(
         PPC_TASK_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR ADD(
         PPC_TASK_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:105 stripComments:false
UPDATE
   ppc_labour
SET
   labour_db_id_old=labour_db_id,
   labour_id_old=labour_id,
   ppc_task_db_id_old=ppc_task_db_id,
   ppc_task_id_old=ppc_task_id;

--changeSet MTX-394:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR_ROLE ADD(
         LABOUR_ROLE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR_ROLE ADD(
         LABOUR_ROLE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR_ROLE ADD(
         LABOUR_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LABOUR_ROLE ADD(
         LABOUR_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:110 stripComments:false
UPDATE
   ppc_labour_role
SET
   labour_role_db_id_old=labour_role_db_id,
   labour_role_id_old=labour_role_id,
   labour_db_id_old=labour_db_id,
   labour_id_old=labour_id;

--changeSet MTX-394:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_CAPACITY ADD(
         PPC_CAPACITY_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_CAPACITY ADD(
         PPC_CAPACITY_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_CAPACITY ADD(
         PPC_LOC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_CAPACITY ADD(
         PPC_LOC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:115 stripComments:false
UPDATE
   ppc_loc_capacity
SET
   ppc_capacity_db_id_old=ppc_capacity_db_id,
   ppc_capacity_id_old=ppc_capacity_id,
   ppc_loc_db_id_old=ppc_loc_db_id,
   ppc_loc_id_old=ppc_loc_id;

--changeSet MTX-394:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_EXCLUDE ADD(
         PPC_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_LOC_EXCLUDE ADD(
         PPC_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:118 stripComments:false
UPDATE ppc_loc_exclude SET ppc_db_id_old=ppc_db_id, ppc_id_old=ppc_id;

--changeSet MTX-394:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_MILESTONE ADD(
         PPC_MILESTONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_MILESTONE ADD(
         PPC_MILESTONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:121 stripComments:false
UPDATE
   ppc_milestone
SET
   ppc_milestone_db_id_old=ppc_milestone_db_id,
   ppc_milestone_id_old=ppc_milestone_id;

--changeSet MTX-394:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_MILESTONE_COND ADD(
         PPC_MILESTONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_MILESTONE_COND ADD(
         PPC_MILESTONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:124 stripComments:false
UPDATE
   ppc_milestone_cond
SET
   ppc_milestone_db_id_old=ppc_milestone_db_id,
   ppc_milestone_id_old=ppc_milestone_id;

--changeSet MTX-394:125 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         PPC_PHASE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         PPC_PHASE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         NR_PHASE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         NR_PHASE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:129 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         NR_START_MILESTONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         NR_START_MILESTONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         NR_END_MILESTONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE ADD(
         NR_END_MILESTONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:133 stripComments:false
UPDATE
   ppc_phase
SET
   ppc_phase_db_id_old=ppc_phase_db_id,
   ppc_phase_id_old=ppc_phase_id,
   nr_phase_db_id_old=nr_phase_db_id,
   nr_phase_id_old=nr_phase_id,
   nr_start_milestone_db_id_old=nr_start_milestone_db_id,
   nr_start_milestone_id_old=nr_start_milestone_id,
   nr_end_milestone_db_id_old=nr_end_milestone_db_id,
   nr_end_milestone_id_old=nr_end_milestone_id;

--changeSet MTX-394:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE_CLASS ADD(
         PPC_PHASE_CLASS_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE_CLASS ADD(
         PPC_PHASE_CLASS_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE_CLASS ADD(
         PPC_PHASE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PHASE_CLASS ADD(
         PPC_PHASE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:138 stripComments:false
UPDATE
   ppc_phase_class
SET
   ppc_phase_class_db_id_old=ppc_phase_class_db_id,
   ppc_phase_class_id_old=ppc_phase_class_id,
   ppc_phase_db_id_old=ppc_phase_db_id,
   ppc_phase_id_old=ppc_phase_id;

--changeSet MTX-394:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLANNING_TYPE ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLANNING_TYPE ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:141 stripComments:false
UPDATE ppc_planning_type SET ppc_wp_db_id_old=ppc_wp_db_id, ppc_wp_id_old=ppc_wp_id;

--changeSet MTX-394:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLANNING_TYPE_SKILL ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLANNING_TYPE_SKILL ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:144 stripComments:false
UPDATE ppc_planning_type_skill SET ppc_wp_db_id_old=ppc_wp_db_id, ppc_wp_id_old=ppc_wp_id;

--changeSet MTX-394:145 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:147 stripComments:false
UPDATE ppc_publish SET ppc_wp_db_id_old=ppc_wp_db_id, ppc_wp_id_old=ppc_wp_id;

--changeSet MTX-394:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH_FAILURE ADD(
         PPC_FAILURE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH_FAILURE ADD(
         PPC_FAILURE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH_FAILURE ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH_FAILURE ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH_FAILURE ADD(
         PPC_TASK_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PUBLISH_FAILURE ADD(
         PPC_TASK_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:154 stripComments:false
UPDATE
   ppc_publish_failure
SET
   ppc_failure_db_id_old=ppc_failure_db_id,
   ppc_failure_id_old=ppc_failure_id,
   ppc_wp_db_id_old=ppc_wp_db_id,
   ppc_wp_id_old=ppc_wp_id,
   ppc_task_db_id_old=ppc_task_db_id,
   ppc_task_id_old=ppc_task_id;

--changeSet MTX-394:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_TASK_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_TASK_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_PHASE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_PHASE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_WORK_AREA_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_WORK_AREA_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         NR_PHASE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         NR_PHASE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         NR_START_MILESTONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         NR_START_MILESTONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:165 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         NR_END_MILESTONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         NR_END_MILESTONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_CREW_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK ADD(
         PPC_CREW_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:169 stripComments:false
UPDATE
   ppc_task
SET
   ppc_task_db_id_old=ppc_task_db_id,
   ppc_task_id_old=ppc_task_id,
   ppc_phase_db_id_old=ppc_phase_db_id,
   ppc_phase_id_old=ppc_phase_id,
   ppc_work_area_db_id_old=ppc_work_area_db_id,
   ppc_work_area_id_old=ppc_work_area_id,
   nr_phase_db_id_old=nr_phase_db_id,
   nr_phase_id_old=nr_phase_id,
   nr_start_milestone_db_id_old=nr_start_milestone_db_id,
   nr_start_milestone_id_old=nr_start_milestone_id,
   nr_end_milestone_db_id_old=nr_end_milestone_db_id,
   nr_end_milestone_id_old=nr_end_milestone_id,
   ppc_crew_db_id_old=ppc_crew_db_id,
   ppc_crew_id_old=ppc_crew_id;

--changeSet MTX-394:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN ADD(
         PPC_TASK_DEFN_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN ADD(
         PPC_TASK_DEFN_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN ADD(
         PPC_WP_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN ADD(
         PPC_WP_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:174 stripComments:false
UPDATE
   ppc_task_defn
SET
   ppc_task_defn_db_id_old=ppc_task_defn_db_id,
   ppc_task_defn_id_old=ppc_task_defn_id,
   ppc_wp_db_id_old=ppc_wp_db_id,
   ppc_wp_id_old=ppc_wp_id;

--changeSet MTX-394:175 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN_MAP ADD(
         PPC_TASK_DEFN_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN_MAP ADD(
         PPC_TASK_DEFN_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN_MAP ADD(
         PPC_TASK_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_TASK_DEFN_MAP ADD(
         PPC_TASK_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:179 stripComments:false
UPDATE
   ppc_task_defn_map
SET
   ppc_task_defn_db_id_old=ppc_task_defn_db_id,
   ppc_task_defn_id_old=ppc_task_defn_id,
   ppc_task_db_id_old=ppc_task_db_id,
   ppc_task_id_old=ppc_task_id;

--changeSet MTX-394:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA ADD(
         PPC_WORK_AREA_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:181 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA ADD(
         PPC_WORK_AREA_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:182 stripComments:false
UPDATE
   ppc_work_area
SET
   ppc_work_area_db_id_old=ppc_work_area_db_id,
   ppc_work_area_id_old=ppc_work_area_id;

--changeSet MTX-394:183 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_CREW ADD(
         PPC_WORK_AREA_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_CREW ADD(
         PPC_WORK_AREA_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_CREW ADD(
         PPC_CREW_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_CREW ADD(
         PPC_CREW_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:187 stripComments:false
UPDATE
   ppc_work_area_crew
SET
   ppc_work_area_db_id_old=ppc_work_area_db_id,
   ppc_work_area_id_old=ppc_work_area_id,
   ppc_crew_db_id_old=ppc_crew_db_id,
   ppc_crew_id_old=ppc_crew_id;

--changeSet MTX-394:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_ZONE ADD(
         PPC_WORK_AREA_ZONE_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:189 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_ZONE ADD(
         PPC_WORK_AREA_ZONE_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_ZONE ADD(
         PPC_WORK_AREA_DB_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_WORK_AREA_ZONE ADD(
         PPC_WORK_AREA_ID_OLD Number(10,0)
      )
   ');
END;
/

--changeSet MTX-394:192 stripComments:false
UPDATE
   ppc_work_area_zone
SET
   ppc_work_area_zone_db_id_old=ppc_work_area_zone_db_id,
   ppc_work_area_zone_id_old=ppc_work_area_zone_id,
   ppc_work_area_db_id_old=ppc_work_area_db_id,
   ppc_work_area_id_old=ppc_work_area_id;

--changeSet MTX-394:193 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop foreighn keys among all PPC compound keys.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_MILESTONE_COND','FK_REFACCONDSETTING_PPCMILESTO');
END;
/

--changeSet MTX-394:194 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PLANNING_TYPE','FK_EQPPLNTYP_PPCPLNTYP');
END;
/

--changeSet MTX-394:195 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_ACTIVITY','FK_PPCWP_PPCACTIVITY');
END;
/

--changeSet MTX-394:196 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_CREW','FK_PPCLOC_PPCCREW');
END;
/

--changeSet MTX-394:197 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_DEPENDENCY','FK_FROMACTIVITY_DEPENDENCY');
END;
/

--changeSet MTX-394:198 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_DEPENDENCY','FK_TOACTIVITY_DEPENDENCY');
END;
/

--changeSet MTX-394:199 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR','FK_PPCPLAN_PPCHR');
END;
/

--changeSet MTX-394:200 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_LIC','FK_PPCHRSHFTPLN_PPCHRLIC');
END;
/

--changeSet MTX-394:201 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SHIFT_PLAN','FK_PPCCREW_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:202 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SHIFT_PLAN','FK_PPCHR_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:203 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SHIFT_PLAN','FK_PPCLOC_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:204 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SHIFT_PLAN','FK_PPCLOCCAP_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:205 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SLOT','FK_PPCHRSHIFTPLAN_PPCHRSLOT');
END;
/

--changeSet MTX-394:206 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SLOT','FK_PPCLABROLE_HRSLOT');
END;
/

--changeSet MTX-394:207 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LABOUR','FK_PPCTASK_PPCLABOUR');
END;
/

--changeSet MTX-394:208 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LABOUR_ROLE','FK_PPCLABOUR_ROLE');
END;
/

--changeSet MTX-394:209 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC','FK_PPCLOC_NHPPCLOC');
END;
/

--changeSet MTX-394:210 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC','FK_PPCPLAN_PPCLOC');
END;
/

--changeSet MTX-394:211 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC_CAPACITY','FK_PPCLOC_PPCLOCCAP');
END;
/

--changeSet MTX-394:212 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC_EXCLUDE','FK_PPCPLAN_PPCLOCEX');
END;
/

--changeSet MTX-394:213 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_MILESTONE','FK_PPCACTIVITY_PPCMILESTONE');
END;
/

--changeSet MTX-394:214 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_MILESTONE_COND','FK_PPCMILESTONE_PPCMILESTONECO');
END;
/

--changeSet MTX-394:215 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_OPT_STATUS','FK_PPCPLAN_PPCOPTSTAT');
END;
/

--changeSet MTX-394:216 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE','FK_PPCACTIVITY_PPCPHASE');
END;
/

--changeSet MTX-394:217 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE','FK_NRENDMLSTN_PPCPHASE');
END;
/

--changeSet MTX-394:218 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE','FK_NRSTRTMLSTN_PPCPHASE');
END;
/

--changeSet MTX-394:219 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE','FK_NRPHASE_PPCPHASE');
END;
/

--changeSet MTX-394:220 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE_CLASS','FK_PPCPHASE_PPCPHASECLASS');
END;
/

--changeSet MTX-394:221 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PLANNING_TYPE','FK_PPCWP_PLANNINGTYPE');
END;
/

--changeSet MTX-394:222 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PLANNING_TYPE_SKILL','FK_PPCPLNTYP_PLNTYPSKILL');
END;
/

--changeSet MTX-394:223 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PUBLISH','FK_PPCWP_PPCPUBLISH');
END;
/

--changeSet MTX-394:224 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PUBLISH_FAILURE','FK_PPCPUBLISH_PUBFAILURE');
END;
/

--changeSet MTX-394:225 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PUBLISH_FAILURE','FK_PPCTASK_PPCPUBLISHFAILURE');
END;
/

--changeSet MTX-394:226 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_PPCACTIVITY_PPCTASK');
END;
/

--changeSet MTX-394:227 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_PPCCREW_PPCTASK');
END;
/

--changeSet MTX-394:228 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_NRENDMILESTONE_PPCTASK');
END;
/

--changeSet MTX-394:229 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_NRSTARTMILESTONE_PPCTASK');
END;
/

--changeSet MTX-394:230 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_NRPHASE_PPCTASK');
END;
/

--changeSet MTX-394:231 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_PPCPHASE_PPCTASK');
END;
/

--changeSet MTX-394:232 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','FK_PPCWORKAREA_PPCTASK');
END;
/

--changeSet MTX-394:233 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK_DEFN','FK_PPCWP_TASKDEFN');
END;
/

--changeSet MTX-394:234 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK_DEFN_MAP','FK_PPCTASK_PPCTASKDEFNMAP');
END;
/

--changeSet MTX-394:235 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK_DEFN_MAP','FK_PPCTASKDEFN_PPCTASKDEFNMAP');
END;
/

--changeSet MTX-394:236 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA','FK_PPCACTIVITY_PPCWORKAREA');
END;
/

--changeSet MTX-394:237 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA_CREW','FK_PPCCREW_PPCWRKAREACRW');
END;
/

--changeSet MTX-394:238 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA_CREW','FK_PPCWRKAREA_PPCWRKAREACRW');
END;
/

--changeSet MTX-394:239 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA_ZONE','FK_PPCWORKAREA_PPCWORKAREAZONE');
END;
/

--changeSet MTX-394:240 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WP','FK_PPCLOC_PPCWP');
END;
/

--changeSet MTX-394:241 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WP','FK_NRPHASE_PPCWP');
END;
/

--changeSet MTX-394:242 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WP','FK_PPCPLAN_PPCWP');
END;
/

--changeSet MTX-394:243 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WP','FK_PPCTEMPLATE_PPCWP');
END;
/

--changeSet MTX-394:244 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop indexes for removed PPC foreighn keys.
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCWP_PPCACTIVITY');
END;
/

--changeSet MTX-394:245 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCLOC_PPCCREW');
END;
/

--changeSet MTX-394:246 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_FROMACTIVITY_DEPENDENCY');
END;
/

--changeSet MTX-394:247 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_TOACTIVITY_DEPENDENCY');
END;
/

--changeSet MTX-394:248 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPLAN_PPCHR');
END;
/

--changeSet MTX-394:249 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCHRSHFTPLN_PPCHRLIC');
END;
/

--changeSet MTX-394:250 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCCREW_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:251 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCHR_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:252 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCLOCCAP_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:253 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCLOC_PPCHRSHFTPLN');
END;
/

--changeSet MTX-394:254 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCHRSHIFTPLAN_PPCHRSLOT');
END;
/

--changeSet MTX-394:255 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCTASK_PPCLABOUR');
END;
/

--changeSet MTX-394:256 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCLOC_NHPPCLOC');
END;
/

--changeSet MTX-394:257 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPLAN_PPCLOC');
END;
/

--changeSet MTX-394:258 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCLOC_PPCLOCCAP');
END;
/

--changeSet MTX-394:259 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPLAN_PPCLOCEX');
END;
/

--changeSet MTX-394:260 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRENDMLSTN_PPCPHASE');
END;
/

--changeSet MTX-394:261 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRPHASE_PPCPHASE');
END;
/

--changeSet MTX-394:262 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRSTRTMLSTN_PPCPHASE');
END;
/

--changeSet MTX-394:263 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPHASE_PPCPHASECLASS');
END;
/

--changeSet MTX-394:264 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCWP_PLANNINGTYPE');
END;
/

--changeSet MTX-394:265 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPLNTYP_PLNTYPSKILL');
END;
/

--changeSet MTX-394:266 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPUBLISH_PUBFAILURE');
END;
/

--changeSet MTX-394:267 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCTASK_PPCPUBLISHFAILURE');
END;
/

--changeSet MTX-394:268 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRENDMILESTONE_PPCTASK');
END;
/

--changeSet MTX-394:269 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRPHASE_PPCTASK');
END;
/

--changeSet MTX-394:270 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRSTARTMILESTONE_PPCTASK');
END;
/

--changeSet MTX-394:271 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCCREW_PPCTASK');
END;
/

--changeSet MTX-394:272 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPHASE_PPCTASK');
END;
/

--changeSet MTX-394:273 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCWORKAREA_PPCTASK');
END;
/

--changeSet MTX-394:274 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCWP_TASKDEFN');
END;
/

--changeSet MTX-394:275 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCTASKDEFN_PPCTASKDEFNMAP');
END;
/

--changeSet MTX-394:276 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCTASK_PPCTASKDEFNMAP');
END;
/

--changeSet MTX-394:277 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCCREW_PPCWRKAREACRW');
END;
/

--changeSet MTX-394:278 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCWRKAREA_PPCWRKAREACRW');
END;
/

--changeSet MTX-394:279 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCWORKAREA_PPCWORKAREAZONE');
END;
/

--changeSet MTX-394:280 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_NRPHASE_PPCWP');
END;
/

--changeSet MTX-394:281 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCLOC_PPCWP');
END;
/

--changeSet MTX-394:282 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCPLAN_PPCWP');
END;
/

--changeSet MTX-394:283 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_PPCTEMPLATE_PPCWP');
END;
/

--changeSet MTX-394:284 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EQPPLNTYP_PPCPLNTYP');
END;
/

--changeSet MTX-394:285 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_REFACCONDSETTING_PPCMILESTO');
END;
/

--changeSet MTX-394:286 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop primary key constraints for all PPC tables.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_ACTIVITY','PK_PPC_ACTIVITY');
END;
/

--changeSet MTX-394:287 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_CREW','PK_PPC_CREW');
END;
/

--changeSet MTX-394:288 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_DEPENDENCY','PK_PPC_DEPENDENCY');
END;
/

--changeSet MTX-394:289 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR','PK_PPC_HR');
END;
/

--changeSet MTX-394:290 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_LIC','PK_PPC_HR_LIC');
END;
/

--changeSet MTX-394:291 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SHIFT_PLAN','PK_PPC_HR_SHIFT_PLAN');
END;
/

--changeSet MTX-394:292 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_HR_SLOT','PK_PPC_HR_SLOT');
END;
/

--changeSet MTX-394:293 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LABOUR','PK_PPC_LABOUR');
END;
/

--changeSet MTX-394:294 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LABOUR_ROLE','PK_PPC_LABOUR_ROLE');
END;
/

--changeSet MTX-394:295 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC','PK_PPC_LOC');
END;
/

--changeSet MTX-394:296 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC_CAPACITY','PK_PPC_LOC_CAPACITY');
END;
/

--changeSet MTX-394:297 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_LOC_EXCLUDE','PK_PPC_LOC_EXCLUDE');
END;
/

--changeSet MTX-394:298 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_MILESTONE','PK_PPC_MILESTONE');
END;
/

--changeSet MTX-394:299 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_MILESTONE_COND','PK_PPC_MILESTONE_COND');
END;
/

--changeSet MTX-394:300 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_OPT_STATUS','PK_PPC_OPT_STATUS');
END;
/

--changeSet MTX-394:301 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE','PK_PPC_PHASE');
END;
/

--changeSet MTX-394:302 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PHASE_CLASS','PK_PPC_PHASE_CLASS');
END;
/

--changeSet MTX-394:303 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PLAN','PK_PPC_PLAN');
END;
/

--changeSet MTX-394:304 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PLANNING_TYPE','PK_PPC_PLANNING_TYPE');
END;
/

--changeSet MTX-394:305 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PLANNING_TYPE_SKILL','PK_PPC_PLANNING_TYPE_SKILL');
END;
/

--changeSet MTX-394:306 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PUBLISH','PK_PPC_PUBLISH');
END;
/

--changeSet MTX-394:307 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_PUBLISH_FAILURE','PK_PPC_PUBLISH_FAILURE');
END;
/

--changeSet MTX-394:308 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK','PK_PPC_TASK');
END;
/

--changeSet MTX-394:309 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK_DEFN','PK_PPC_TASK_DEFN');
END;
/

--changeSet MTX-394:310 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_TASK_DEFN_MAP','PK_PPC_TASK_DEFN_MAP');
END;
/

--changeSet MTX-394:311 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA','PK_PPC_WORK_AREA');
END;
/

--changeSet MTX-394:312 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA_CREW','PK_PPC_WORK_AREA_CREW');
END;
/

--changeSet MTX-394:313 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WORK_AREA_ZONE','PK_PPC_WORK_AREA_ZONE');
END;
/

--changeSet MTX-394:314 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PPC_WP','PK_PPC_WP');
END;
/

--changeSet MTX-394:315 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop primary key indexes for all PPC tables.
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_ACTIVITY');
END;
/

--changeSet MTX-394:316 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_CREW');
END;
/

--changeSet MTX-394:317 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_DEPENDENCY');
END;
/

--changeSet MTX-394:318 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_HR');
END;
/

--changeSet MTX-394:319 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_HR_LIC');
END;
/

--changeSet MTX-394:320 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_HR_SHIFT_PLAN');
END;
/

--changeSet MTX-394:321 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_HR_SLOT');
END;
/

--changeSet MTX-394:322 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_LABOUR');
END;
/

--changeSet MTX-394:323 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_LABOUR_ROLE');
END;
/

--changeSet MTX-394:324 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_LOC');
END;
/

--changeSet MTX-394:325 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_LOC_CAPACITY');
END;
/

--changeSet MTX-394:326 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_LOC_EXCLUDE');
END;
/

--changeSet MTX-394:327 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_MILESTONE');
END;
/

--changeSet MTX-394:328 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_MILESTONE_COND');
END;
/

--changeSet MTX-394:329 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_OPT_STATUS');
END;
/

--changeSet MTX-394:330 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PHASE');
END;
/

--changeSet MTX-394:331 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PHASE_CLASS');
END;
/

--changeSet MTX-394:332 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PLAN');
END;
/

--changeSet MTX-394:333 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PLANNING_TYPE');
END;
/

--changeSet MTX-394:334 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PLANNING_TYPE_SKILL');
END;
/

--changeSet MTX-394:335 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PUBLISH');
END;
/

--changeSet MTX-394:336 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_PUBLISH_FAILURE');
END;
/

--changeSet MTX-394:337 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_TASK');
END;
/

--changeSet MTX-394:338 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_TASK_DEFN');
END;
/

--changeSet MTX-394:339 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_TASK_DEFN_MAP');
END;
/

--changeSet MTX-394:340 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_WORK_AREA');
END;
/

--changeSet MTX-394:341 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_WORK_AREA_CREW');
END;
/

--changeSet MTX-394:342 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_WORK_AREA_ZONE');
END;
/

--changeSet MTX-394:343 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PPC_WP');
END;
/

--changeSet MTX-394:344 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Rename all ID columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_ACTIVITY','PPC_ACTIVITY_ID','ACTIVITY_ID');
END;
/

--changeSet MTX-394:345 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_ACTIVITY','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:346 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_ACTVTY_SNAPSHOT','ACTIVITY_ALT_ID','ACTIVITY_ID');
END;
/

--changeSet MTX-394:347 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_CREW','PPC_CREW_ID','CREW_ID');
END;
/

--changeSet MTX-394:348 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_CREW','PPC_LOC_ID','LOCATION_ID');
END;
/

--changeSet MTX-394:349 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR','PPC_HR_ID','HUMAN_RESOURCE_ID');
END;
/

--changeSet MTX-394:350 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR','PPC_ID','PLAN_ID');
END;
/

--changeSet MTX-394:351 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_LIC','PPC_HR_LIC_ID','HUMAN_RESOURCE_LIC_ID');
END;
/

--changeSet MTX-394:352 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_LIC','PPC_HR_SHIFT_ID','HUMAN_RESOURCE_SHIFT_ID');
END;
/

--changeSet MTX-394:353 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SHIFT_PLAN','PPC_HR_SHIFT_ID','HUMAN_RESOURCE_SHIFT_ID');
END;
/

--changeSet MTX-394:354 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SHIFT_PLAN','PPC_HR_ID','HUMAN_RESOURCE_ID');
END;
/

--changeSet MTX-394:355 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SHIFT_PLAN','PPC_CREW_ID','CREW_ID');
END;
/

--changeSet MTX-394:356 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SHIFT_PLAN','PPC_CAPACITY_ID','CAPACITY_ID');
END;
/

--changeSet MTX-394:357 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SHIFT_PLAN','PPC_LOC_ID','LOCATION_ID');
END;
/

--changeSet MTX-394:358 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SLOT','PPC_HR_SLOT_ID','HUMAN_RESOURCE_SLOT_ID');
END;
/

--changeSet MTX-394:359 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_HR_SLOT','PPC_HR_SHIFT_ID','HUMAN_RESOURCE_SHIFT_ID');
END;
/

--changeSet MTX-394:360 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LABOUR','PPC_TASK_ID','TASK_ID');
END;
/

--changeSet MTX-394:361 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LOC','PPC_LOC_ID','LOCATION_ID');
END;
/

--changeSet MTX-394:362 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LOC','PPC_ID','PLAN_ID');
END;
/

--changeSet MTX-394:363 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LOC','NH_PPC_LOC_ID','NH_LOCATION_ID');
END;
/

--changeSet MTX-394:364 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LOC_CAPACITY','PPC_CAPACITY_ID','LOCATION_CAPACITY_ID');
END;
/

--changeSet MTX-394:365 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LOC_CAPACITY','PPC_LOC_ID','LOCATION_ID');
END;
/

--changeSet MTX-394:366 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_LOC_EXCLUDE','PPC_ID','PLAN_ID');
END;
/

--changeSet MTX-394:367 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_MILESTONE','PPC_MILESTONE_ID','MILESTONE_ID');
END;
/

--changeSet MTX-394:368 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_MILESTONE_COND','PPC_MILESTONE_ID','MILESTONE_ID');
END;
/

--changeSet MTX-394:369 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_OPT_STATUS','PPC_ID','PLAN_ID');
END;
/

--changeSet MTX-394:370 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PHASE','PPC_PHASE_ID','PHASE_ID');
END;
/

--changeSet MTX-394:371 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PHASE_CLASS','PPC_PHASE_CLASS_ID','PHASE_CLASS_ID');
END;
/

--changeSet MTX-394:372 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PHASE_CLASS','PPC_PHASE_ID','PHASE_ID');
END;
/

--changeSet MTX-394:373 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PLAN','PPC_ID','PLAN_ID');
END;
/

--changeSet MTX-394:374 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PLANNING_TYPE','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:375 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PLANNING_TYPE_SKILL','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:376 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PUBLISH','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:377 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PUBLISH_FAILURE','PPC_FAILURE_ID','FAILURE_ID');
END;
/

--changeSet MTX-394:378 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PUBLISH_FAILURE','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:379 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_PUBLISH_FAILURE','PPC_TASK_ID','TASK_ID');
END;
/

--changeSet MTX-394:380 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK','PPC_TASK_ID','TASK_ID');
END;
/

--changeSet MTX-394:381 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK','PPC_PHASE_ID','PHASE_ID');
END;
/

--changeSet MTX-394:382 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK','PPC_WORK_AREA_ID','WORK_AREA_ID');
END;
/

--changeSet MTX-394:383 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK','PPC_CREW_ID','CREW_ID');
END;
/

--changeSet MTX-394:384 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK_DEFN','PPC_TASK_DEFN_ID','TASK_DEFINITION_ID');
END;
/

--changeSet MTX-394:385 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK_DEFN','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:386 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK_DEFN_MAP','PPC_TASK_DEFN_ID','TASK_DEFINITION_ID');
END;
/

--changeSet MTX-394:387 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_TASK_DEFN_MAP','PPC_TASK_ID','TASK_ID');
END;
/

--changeSet MTX-394:388 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WORK_AREA','PPC_WORK_AREA_ID','WORK_AREA_ID');
END;
/

--changeSet MTX-394:389 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WORK_AREA_CREW','PPC_WORK_AREA_ID','WORK_AREA_ID');
END;
/

--changeSet MTX-394:390 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WORK_AREA_CREW','PPC_CREW_ID','CREW_ID');
END;
/

--changeSet MTX-394:391 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_ZONE_ID','WORK_AREA_ZONE_ID');
END;
/

--changeSet MTX-394:392 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_ID','WORK_AREA_ID');
END;
/

--changeSet MTX-394:393 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WP','PPC_WP_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:394 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WP','PPC_ID','PLAN_ID');
END;
/

--changeSet MTX-394:395 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WP','PPC_LOC_ID','LOCATION_ID');
END;
/

--changeSet MTX-394:396 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_RENAME('PPC_WP_SNAPSHOT','WP_ALT_ID','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:397 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop "check" constraints for all PPC ID fields.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_ACTIVITY','ACTIVITY_ID');
END;
/

--changeSet MTX-394:398 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_ACTIVITY','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:399 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_CREW','CREW_ID');
END;
/

--changeSet MTX-394:400 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_CREW','LOCATION_ID');
END;
/

--changeSet MTX-394:401 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_DEPENDENCY','FROM_ACTIVITY_ID');
END;
/

--changeSet MTX-394:402 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_DEPENDENCY','TO_ACTIVITY_ID');
END;
/

--changeSet MTX-394:403 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR','HUMAN_RESOURCE_ID');
END;
/

--changeSet MTX-394:404 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR','PLAN_ID');
END;
/

--changeSet MTX-394:405 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_LIC','HUMAN_RESOURCE_LIC_ID');
END;
/

--changeSet MTX-394:406 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_LIC','HUMAN_RESOURCE_SHIFT_ID');
END;
/

--changeSet MTX-394:407 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SHIFT_PLAN','HUMAN_RESOURCE_SHIFT_ID');
END;
/

--changeSet MTX-394:408 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SHIFT_PLAN','LOCATION_ID');
END;
/

--changeSet MTX-394:409 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SHIFT_PLAN','CAPACITY_ID');
END;
/

--changeSet MTX-394:410 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SHIFT_PLAN','CREW_ID');
END;
/

--changeSet MTX-394:411 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SHIFT_PLAN','HUMAN_RESOURCE_ID');
END;
/

--changeSet MTX-394:412 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SLOT','HUMAN_RESOURCE_SLOT_ID');
END;
/

--changeSet MTX-394:413 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SLOT','LABOUR_ROLE_ID');
END;
/

--changeSet MTX-394:414 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_HR_SLOT','HUMAN_RESOURCE_SHIFT_ID');
END;
/

--changeSet MTX-394:415 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LABOUR','LABOUR_ID');
END;
/

--changeSet MTX-394:416 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LABOUR','TASK_ID');
END;
/

--changeSet MTX-394:417 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LABOUR_ROLE','LABOUR_ROLE_ID');
END;
/

--changeSet MTX-394:418 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LABOUR_ROLE','LABOUR_ID');
END;
/

--changeSet MTX-394:419 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LOC','LOCATION_ID');
END;
/

--changeSet MTX-394:420 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LOC','PLAN_ID');
END;
/

--changeSet MTX-394:421 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LOC','NH_LOCATION_ID');
END;
/

--changeSet MTX-394:422 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LOC_CAPACITY','LOCATION_CAPACITY_ID');
END;
/

--changeSet MTX-394:423 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LOC_CAPACITY','LOCATION_ID');
END;
/

--changeSet MTX-394:424 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_LOC_EXCLUDE','PLAN_ID');
END;
/

--changeSet MTX-394:425 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_MILESTONE','MILESTONE_ID');
END;
/

--changeSet MTX-394:426 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_MILESTONE_COND','MILESTONE_ID');
END;
/

--changeSet MTX-394:427 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_OPT_STATUS','PLAN_ID');
END;
/

--changeSet MTX-394:428 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PHASE','PHASE_ID');
END;
/

--changeSet MTX-394:429 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PHASE','NR_PHASE_ID');
END;
/

--changeSet MTX-394:430 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PHASE','NR_START_MILESTONE_ID');
END;
/

--changeSet MTX-394:431 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PHASE','NR_END_MILESTONE_ID');
END;
/

--changeSet MTX-394:432 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PHASE_CLASS','PHASE_CLASS_ID');
END;
/

--changeSet MTX-394:433 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PHASE_CLASS','PHASE_ID');
END;
/

--changeSet MTX-394:434 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PLAN','PLAN_ID');
END;
/

--changeSet MTX-394:435 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PLANNING_TYPE','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:436 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PLANNING_TYPE_SKILL','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:437 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PUBLISH','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:438 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PUBLISH_FAILURE','FAILURE_ID');
END;
/

--changeSet MTX-394:439 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PUBLISH_FAILURE','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:440 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_PUBLISH_FAILURE','TASK_ID');
END;
/

--changeSet MTX-394:441 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','TASK_ID');
END;
/

--changeSet MTX-394:442 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','PHASE_ID');
END;
/

--changeSet MTX-394:443 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','WORK_AREA_ID');
END;
/

--changeSet MTX-394:444 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','NR_PHASE_ID');
END;
/

--changeSet MTX-394:445 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','NR_START_MILESTONE_ID');
END;
/

--changeSet MTX-394:446 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','NR_END_MILESTONE_ID');
END;
/

--changeSet MTX-394:447 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK','CREW_ID');
END;
/

--changeSet MTX-394:448 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK_DEFN','TASK_DEFINITION_ID');
END;
/

--changeSet MTX-394:449 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK_DEFN','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:450 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK_DEFN_MAP','TASK_DEFINITION_ID');
END;
/

--changeSet MTX-394:451 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_TASK_DEFN_MAP','TASK_ID');
END;
/

--changeSet MTX-394:452 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WORK_AREA','WORK_AREA_ID');
END;
/

--changeSet MTX-394:453 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WORK_AREA_CREW','WORK_AREA_ID');
END;
/

--changeSet MTX-394:454 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WORK_AREA_CREW','CREW_ID');
END;
/

--changeSet MTX-394:455 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WORK_AREA_ZONE','WORK_AREA_ZONE_ID');
END;
/

--changeSet MTX-394:456 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WORK_AREA_ZONE','WORK_AREA_ID');
END;
/

--changeSet MTX-394:457 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WP','WORK_PACKAGE_ID');
END;
/

--changeSet MTX-394:458 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WP','PLAN_ID');
END;
/

--changeSet MTX-394:459 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WP','LOCATION_ID');
END;
/

--changeSet MTX-394:460 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WP','NR_PHASE_ID');
END;
/

--changeSet MTX-394:461 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('PPC_WP','TEMPLATE_ID');
END;
/

--changeSet MTX-394:462 stripComments:false
-- Zap existing data in affected columns by NULL for further data type change.
UPDATE ppc_activity SET activity_id=NULL, work_package_id=NULL;

--changeSet MTX-394:463 stripComments:false
UPDATE ppc_crew SET crew_id=NULL, location_id=NULL;

--changeSet MTX-394:464 stripComments:false
UPDATE ppc_dependency SET from_activity_id=NULL, to_activity_id=NULL;

--changeSet MTX-394:465 stripComments:false
UPDATE ppc_hr SET human_resource_id=NULL, plan_id=NULL;

--changeSet MTX-394:466 stripComments:false
UPDATE ppc_hr_lic SET human_resource_lic_id=NULL, human_resource_shift_id=NULL;

--changeSet MTX-394:467 stripComments:false
UPDATE ppc_hr_shift_plan SET human_resource_shift_id=NULL, location_id=NULL, capacity_id=NULL, crew_id=NULL, human_resource_id=NULL;

--changeSet MTX-394:468 stripComments:false
UPDATE ppc_hr_slot SET human_resource_slot_id=NULL, labour_role_id=NULL, human_resource_shift_id=NULL;

--changeSet MTX-394:469 stripComments:false
UPDATE ppc_labour SET labour_id=NULL,task_id=NULL;

--changeSet MTX-394:470 stripComments:false
UPDATE ppc_labour_role SET labour_role_id=NULL, labour_id=NULL;

--changeSet MTX-394:471 stripComments:false
UPDATE ppc_loc SET location_id=NULL, plan_id=NULL, nh_location_id=NULL;

--changeSet MTX-394:472 stripComments:false
UPDATE ppc_loc_capacity SET location_capacity_id=NULL, location_id=NULL;

--changeSet MTX-394:473 stripComments:false
UPDATE ppc_loc_exclude SET plan_id=NULL;

--changeSet MTX-394:474 stripComments:false
UPDATE ppc_milestone SET milestone_id=NULL;

--changeSet MTX-394:475 stripComments:false
UPDATE ppc_milestone_cond SET milestone_id=NULL;

--changeSet MTX-394:476 stripComments:false
UPDATE ppc_opt_status SET plan_id=NULL;

--changeSet MTX-394:477 stripComments:false
UPDATE ppc_phase SET phase_id=NULL, nr_phase_id=NULL, nr_start_milestone_id=NULL, nr_end_milestone_id=NULL;

--changeSet MTX-394:478 stripComments:false
UPDATE ppc_phase_class SET phase_class_id=NULL, phase_id=NULL;

--changeSet MTX-394:479 stripComments:false
UPDATE ppc_plan SET plan_id=NULL;

--changeSet MTX-394:480 stripComments:false
UPDATE ppc_planning_type SET work_package_id=NULL;

--changeSet MTX-394:481 stripComments:false
UPDATE ppc_planning_type_skill SET work_package_id=NULL;

--changeSet MTX-394:482 stripComments:false
UPDATE ppc_publish SET work_package_id=NULL;

--changeSet MTX-394:483 stripComments:false
UPDATE ppc_publish_failure SET failure_id=NULL, work_package_id=NULL, task_id=NULL;

--changeSet MTX-394:484 stripComments:false
UPDATE ppc_task SET task_id=NULL, phase_id=NULL, work_area_id=NULL, nr_phase_id=NULL, nr_start_milestone_id=NULL, nr_end_milestone_id=NULL, crew_id=NULL;

--changeSet MTX-394:485 stripComments:false
UPDATE ppc_task_defn SET task_definition_id=NULL, work_package_id=NULL;

--changeSet MTX-394:486 stripComments:false
UPDATE ppc_task_defn_map SET task_definition_id=NULL, task_id=NULL;

--changeSet MTX-394:487 stripComments:false
UPDATE ppc_work_area SET work_area_id=NULL;

--changeSet MTX-394:488 stripComments:false
UPDATE ppc_work_area_crew SET work_area_id=NULL, crew_id=NULL;

--changeSet MTX-394:489 stripComments:false
UPDATE ppc_work_area_zone SET work_area_zone_id=NULL, work_area_id=NULL;

--changeSet MTX-394:490 stripComments:false
UPDATE ppc_wp SET work_package_id=NULL, plan_id=NULL, location_id=NULL, nr_phase_id=NULL, template_id=NULL;

--changeSet MTX-394:491 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Alter all PPC tables for UUID data type.
BEGIN
   -- PPC_ACTIVITY
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PPC_ACTIVITY MODIFY (
         ACTIVITY_ID Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:492 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_activity MODIFY (
         work_package_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:493 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_CREW
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_crew MODIFY (
         crew_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:494 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_crew MODIFY (
         location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:495 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_DEPENDENCY
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_dependency MODIFY (
         from_activity_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:496 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_dependency MODIFY (
         to_activity_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:497 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr MODIFY (
         human_resource_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:498 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr MODIFY (
         plan_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:499 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR_LIC
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_lic MODIFY (
         human_resource_lic_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:500 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_lic MODIFY (
         human_resource_shift_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:501 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR_SHIFT_PLAN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         human_resource_shift_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:502 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         human_resource_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:503 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         crew_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:504 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         capacity_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:505 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:506 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR_SLOT
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
         human_resource_slot_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:507 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
         labour_role_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:508 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
         human_resource_shift_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:509 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LABOUR
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour MODIFY (
         labour_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:510 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour MODIFY (
         task_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:511 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LABOUR_ROLE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour_role MODIFY (
         labour_role_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:512 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour_role MODIFY (
         labour_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:513 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LOC
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc MODIFY (
         location_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:514 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc MODIFY (
         plan_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:515 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc MODIFY (
         nh_location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:516 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LOC_CAPACITY
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc_capacity MODIFY (
         location_capacity_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:517 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc_capacity MODIFY (
         location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:518 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LOC_EXCLUDE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc_exclude MODIFY (
         plan_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:519 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_MILESTONE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_milestone MODIFY (
         milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:520 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_MILESTONE_COND
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_milestone_cond MODIFY (
         milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:521 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_OPT_STATUS
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_opt_status MODIFY (
         plan_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:522 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PHASE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:523 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         nr_phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:524 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         nr_start_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:525 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         nr_end_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:526 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PHASE_CLASS
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase_class MODIFY (
         phase_class_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:527 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase_class MODIFY (
         phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:528 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PLAN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_plan MODIFY (
         plan_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:529 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PLANNING_TYPE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_planning_type MODIFY (
         work_package_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:530 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PLANNING_TYPE_SKILL
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_planning_type_skill MODIFY (
         work_package_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:531 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PUBLISH
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish MODIFY (
         work_package_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:532 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PUBLISH_FAILURE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish_failure MODIFY (
         failure_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:533 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish_failure MODIFY (
         work_package_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:534 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish_failure MODIFY (
         task_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:535 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_TASK
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         task_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:536 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:537 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         work_area_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:538 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         crew_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:539 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         nr_phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:540 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         nr_start_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:541 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         nr_end_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:542 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_TASK_DEFN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn MODIFY (
         task_definition_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:543 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn MODIFY (
         work_package_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:544 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_TASK_DEFN_MAP
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn_map MODIFY (
         task_definition_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:545 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn_map MODIFY (
         task_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:546 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WORK_AREA
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area MODIFY (
         work_area_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:547 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WORK_AREA_CREW
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_crew MODIFY (
         work_area_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:548 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_crew MODIFY (
         crew_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:549 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WORK_AREA_ZONE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_zone MODIFY (
         work_area_zone_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:550 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_zone MODIFY (
         work_area_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:551 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WP
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         work_package_id Raw(16) Default SYS_GUID()
      )
   ');

END;
/

--changeSet MTX-394:552 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         plan_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:553 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:554 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         nr_phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:555 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         template_id Raw(16)
      )
   ');
END;
/

--changeSet MTX-394:556 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop all DB_ID columns
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','PPC_ACTIVITY_DB_ID');
END;
/

--changeSet MTX-394:557 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:558 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_CREW','PPC_CREW_DB_ID');
END;
/

--changeSet MTX-394:559 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_CREW','PPC_LOC_DB_ID');
END;
/

--changeSet MTX-394:560 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_DEPENDENCY','FROM_ACTIVITY_DB_ID');
END;
/

--changeSet MTX-394:561 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_DEPENDENCY','TO_ACTIVITY_DB_ID');
END;
/

--changeSet MTX-394:562 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR','PPC_HR_DB_ID');
END;
/

--changeSet MTX-394:563 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR','PPC_DB_ID');
END;
/

--changeSet MTX-394:564 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_LIC','PPC_HR_LIC_DB_ID');
END;
/

--changeSet MTX-394:565 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_LIC','PPC_HR_SHIFT_DB_ID');
END;
/

--changeSet MTX-394:566 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_HR_SHIFT_DB_ID');
END;
/

--changeSet MTX-394:567 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_LOC_DB_ID');
END;
/

--changeSet MTX-394:568 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_CAPACITY_DB_ID');
END;
/

--changeSet MTX-394:569 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_CREW_DB_ID');
END;
/

--changeSet MTX-394:570 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_HR_DB_ID');
END;
/

--changeSet MTX-394:571 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','PPC_HR_SLOT_DB_ID');
END;
/

--changeSet MTX-394:572 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','LABOUR_ROLE_DB_ID');
END;
/

--changeSet MTX-394:573 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','PPC_HR_SHIFT_DB_ID');
END;
/

--changeSet MTX-394:574 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR','LABOUR_DB_ID');
END;
/

--changeSet MTX-394:575 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR','PPC_TASK_DB_ID');
END;
/

--changeSet MTX-394:576 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR_ROLE','LABOUR_ROLE_DB_ID');
END;
/

--changeSet MTX-394:577 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR_ROLE','LABOUR_DB_ID');
END;
/

--changeSet MTX-394:578 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','PPC_LOC_DB_ID');
END;
/

--changeSet MTX-394:579 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','PPC_DB_ID');
END;
/

--changeSet MTX-394:580 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','NH_PPC_LOC_DB_ID');
END;
/

--changeSet MTX-394:581 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_CAPACITY','PPC_CAPACITY_DB_ID');
END;
/

--changeSet MTX-394:582 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_CAPACITY','PPC_LOC_DB_ID');
END;
/

--changeSet MTX-394:583 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_EXCLUDE','PPC_DB_ID');
END;
/

--changeSet MTX-394:584 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_MILESTONE','PPC_MILESTONE_DB_ID');
END;
/

--changeSet MTX-394:585 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_MILESTONE_COND','PPC_MILESTONE_DB_ID');
END;
/

--changeSet MTX-394:586 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_OPT_STATUS','PPC_DB_ID');
END;
/

--changeSet MTX-394:587 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','PPC_PHASE_DB_ID');
END;
/

--changeSet MTX-394:588 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_PHASE_DB_ID');
END;
/

--changeSet MTX-394:589 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_START_MILESTONE_DB_ID');
END;
/

--changeSet MTX-394:590 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_END_MILESTONE_DB_ID');
END;
/

--changeSet MTX-394:591 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE_CLASS','PPC_PHASE_CLASS_DB_ID');
END;
/

--changeSet MTX-394:592 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE_CLASS','PPC_PHASE_DB_ID');
END;
/

--changeSet MTX-394:593 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLAN','PPC_DB_ID');
END;
/

--changeSet MTX-394:594 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLANNING_TYPE','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:595 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLANNING_TYPE_SKILL','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:596 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:597 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_FAILURE_DB_ID');
END;
/

--changeSet MTX-394:598 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:599 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_TASK_DB_ID');
END;
/

--changeSet MTX-394:600 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_TASK_DB_ID');
END;
/

--changeSet MTX-394:601 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_PHASE_DB_ID');
END;
/

--changeSet MTX-394:602 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_WORK_AREA_DB_ID');
END;
/

--changeSet MTX-394:603 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_PHASE_DB_ID');
END;
/

--changeSet MTX-394:604 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_START_MILESTONE_DB_ID');
END;
/

--changeSet MTX-394:605 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_END_MILESTONE_DB_ID');
END;
/

--changeSet MTX-394:606 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_CREW_DB_ID');
END;
/

--changeSet MTX-394:607 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN','PPC_TASK_DEFN_DB_ID');
END;
/

--changeSet MTX-394:608 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:609 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN_MAP','PPC_TASK_DEFN_DB_ID');
END;
/

--changeSet MTX-394:610 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN_MAP','PPC_TASK_DB_ID');
END;
/

--changeSet MTX-394:611 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA','PPC_WORK_AREA_DB_ID');
END;
/

--changeSet MTX-394:612 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_CREW','PPC_WORK_AREA_DB_ID');
END;
/

--changeSet MTX-394:613 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_CREW','PPC_CREW_DB_ID');
END;
/

--changeSet MTX-394:614 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_ZONE_DB_ID');
END;
/

--changeSet MTX-394:615 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_DB_ID');
END;
/

--changeSet MTX-394:616 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_WP_DB_ID');
END;
/

--changeSet MTX-394:617 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_DB_ID');
END;
/

--changeSet MTX-394:618 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_LOC_DB_ID');
END;
/

--changeSet MTX-394:619 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','NR_PHASE_DB_ID');
END;
/

--changeSet MTX-394:620 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','TEMPLATE_DB_ID');
END;
/

--changeSet MTX-394:621 stripComments:false
-- Data migration
-- Populate ID into respective entity
-- PPC_ACTIVITY: we use existing ID from the "alt_id" field
UPDATE
   ppc_activity
SET
   activity_id = alt_id;

--changeSet MTX-394:622 stripComments:false
-- PPC_CREW: system generated ID
UPDATE
   ppc_crew
SET
   crew_id = SYS_GUID();

--changeSet MTX-394:623 stripComments:false
-- PPC_HR: system generated ID
UPDATE
   ppc_hr
SET
   human_resource_id = SYS_GUID();

--changeSet MTX-394:624 stripComments:false
-- PPC_HR_LIC: system generated ID
UPDATE
   ppc_hr_lic
SET
   human_resource_lic_id = SYS_GUID();

--changeSet MTX-394:625 stripComments:false
-- PPC_HR_SHIFT_PLAN: system generated ID
UPDATE
   ppc_hr_shift_plan
SET
   human_resource_shift_id = SYS_GUID();

--changeSet MTX-394:626 stripComments:false
-- PPC_HR_SLOT: system generated ID
UPDATE
   ppc_hr_slot
SET
   human_resource_slot_id = SYS_GUID();

--changeSet MTX-394:627 stripComments:false
-- PPC_LABOUR: system generated ID
UPDATE
   ppc_labour
SET
   labour_id = SYS_GUID();

--changeSet MTX-394:628 stripComments:false
-- PPC_LABOUR_ROLE: system generated ID
UPDATE
   ppc_labour_role
SET
   labour_role_id = SYS_GUID();

--changeSet MTX-394:629 stripComments:false
-- PPC_LOC: system generated ID
UPDATE
   ppc_loc
SET
   location_id = SYS_GUID();

--changeSet MTX-394:630 stripComments:false
-- PPC_LOC_CAPACITY: system generated ID
UPDATE
   ppc_loc_capacity
SET
   location_capacity_id = SYS_GUID();

--changeSet MTX-394:631 stripComments:false
-- PPC_PHASE_CLASS: system generated ID
UPDATE
   ppc_phase_class
SET
   phase_class_id = SYS_GUID();

--changeSet MTX-394:632 stripComments:false
-- PPC_PLAN: we use existing ID from the "alt_id" field
UPDATE
   ppc_plan
SET
   plan_id = alt_id;

--changeSet MTX-394:633 stripComments:false
-- PPC_TASK_DEFN: system generated ID
UPDATE
   ppc_task_defn
SET
   task_definition_id = SYS_GUID();

--changeSet MTX-394:634 stripComments:false
-- PPC_WORK_AREA_ZONE: system generated ID
UPDATE
   ppc_work_area_zone
SET
   work_area_zone_id = SYS_GUID();

--changeSet MTX-394:635 stripComments:false
-- PPC_WP: we use existing ID from the "alt_id" field
UPDATE
   ppc_wp
SET
   work_package_id = alt_id;

--changeSet MTX-394:636 stripComments:false
-- Restore references by ID
-- PPC_ACTIVITY
UPDATE
   ppc_activity
SET
   work_package_id = (SELECT
                         ppc_wp.work_package_id
                      FROM
                         ppc_wp
                      WHERE
                         ppc_wp.ppc_wp_db_id_old=ppc_activity.ppc_wp_db_id_old AND
                         ppc_wp.ppc_wp_id_old =ppc_activity.ppc_wp_id_old
                     );

--changeSet MTX-394:637 stripComments:false
-- PPC_CREW
UPDATE
   ppc_crew
SET
   location_id = (SELECT
                     ppc_loc.location_id
                  FROM
                     ppc_loc
                  WHERE
                     ppc_loc.ppc_loc_db_id_old=ppc_crew.ppc_loc_db_id_old AND
                     ppc_loc.ppc_loc_id_old=ppc_crew.ppc_loc_id_old
                  );

--changeSet MTX-394:638 stripComments:false
-- PPC_DEPENDENCY
UPDATE
   ppc_dependency
SET
   from_activity_id = (SELECT
                          ppc_activity.activity_id
                       FROM
                          ppc_activity
                       WHERE
                          ppc_activity.ppc_activity_db_id_old=ppc_dependency.from_activity_db_id_old AND
                          ppc_activity.ppc_activity_id_old=ppc_dependency.from_activity_id_old
                      ),
   to_activity_id = (SELECT
                          ppc_activity.activity_id
                       FROM
                          ppc_activity
                       WHERE
                          ppc_activity.ppc_activity_db_id_old=ppc_dependency.to_activity_db_id_old AND
                          ppc_activity.ppc_activity_id_old=ppc_dependency.to_activity_id_old
                      );

--changeSet MTX-394:639 stripComments:false
-- PPC_HR
UPDATE
   ppc_hr
SET
   plan_id = (SELECT
                 ppc_plan.plan_id
              FROM
                 ppc_plan
              WHERE
                 ppc_plan.ppc_db_id_old=ppc_hr.ppc_db_id_old AND
                 ppc_plan.ppc_id_old=ppc_hr.ppc_id_old
             );

--changeSet MTX-394:640 stripComments:false
-- PPC_HR_LIC
UPDATE
   ppc_hr_lic
SET
   human_resource_shift_id = (SELECT
                                 ppc_hr_shift_plan.human_resource_shift_id
                              FROM
                                 ppc_hr_shift_plan
                              WHERE
                                 ppc_hr_shift_plan.ppc_hr_shift_db_id_old=ppc_hr_lic.ppc_hr_shift_db_id_old AND
                                 ppc_hr_shift_plan.ppc_hr_shift_id_old=ppc_hr_lic.ppc_hr_shift_id_old
                             );

--changeSet MTX-394:641 stripComments:false
-- PPC_HR_SHIFT_PLAN
UPDATE
   ppc_hr_shift_plan
SET
   human_resource_id = (SELECT
                                 ppc_hr.human_resource_id
                              FROM
                                 ppc_hr
                              WHERE
                                 ppc_hr.ppc_hr_db_id_old=ppc_hr_shift_plan.ppc_hr_db_id_old AND
                                 ppc_hr.ppc_hr_id_old=ppc_hr_shift_plan.ppc_hr_id_old
                             ),
   crew_id = (SELECT
                 ppc_crew.crew_id
              FROM
                 ppc_crew
              WHERE
                 ppc_crew.ppc_crew_db_id_old=ppc_hr_shift_plan.ppc_crew_db_id_old AND
                 ppc_crew.ppc_crew_id_old=ppc_hr_shift_plan.ppc_crew_id_old
             ),
   capacity_id = (SELECT
                     ppc_loc_capacity.location_capacity_id
                  FROM
                     ppc_loc_capacity
                  WHERE
                     ppc_loc_capacity.ppc_capacity_db_id_old=ppc_hr_shift_plan.ppc_capacity_db_id_old AND
                     ppc_loc_capacity.ppc_capacity_id_old=ppc_hr_shift_plan.ppc_capacity_id_old
                 ),
   location_id = (SELECT
                     ppc_loc.location_id
                  FROM
                     ppc_loc
                  WHERE
                     ppc_loc.ppc_loc_db_id_old=ppc_hr_shift_plan.ppc_loc_db_id_old AND
                     ppc_loc.ppc_loc_id_old=ppc_hr_shift_plan.ppc_loc_id_old
                 );

--changeSet MTX-394:642 stripComments:false
-- PPC_HR_SLOT
UPDATE
   ppc_hr_slot
SET
   labour_role_id = (SELECT
                        ppc_labour_role.labour_role_id
                     FROM
                        ppc_labour_role
                     WHERE
                        ppc_labour_role.labour_role_db_id_old=ppc_hr_slot.labour_role_db_id_old AND
                        ppc_labour_role.labour_role_id_old=ppc_hr_slot.labour_role_id_old
                    ),
   human_resource_shift_id = (SELECT
                                   ppc_hr_shift_plan.human_resource_shift_id
                                FROM
                                 ppc_hr_shift_plan
                              WHERE
                                 ppc_hr_shift_plan.ppc_hr_shift_db_id_old=ppc_hr_slot.ppc_hr_shift_db_id_old AND
                                 ppc_hr_shift_plan.ppc_hr_shift_id_old=ppc_hr_slot.ppc_hr_shift_id_old
                              );

--changeSet MTX-394:643 stripComments:false
-- PPC_LABOUR
UPDATE
   ppc_labour
SET
   task_id = (SELECT
                 ppc_activity.activity_id
              FROM
                 ppc_activity
              WHERE
                 ppc_activity.ppc_activity_db_id_old=ppc_labour.ppc_task_db_id_old AND
                 ppc_activity.ppc_activity_id_old=ppc_labour.ppc_task_id_old
             );

--changeSet MTX-394:644 stripComments:false
-- PPC_LABOUR_ROLE
UPDATE
   ppc_labour_role
SET
   labour_id = (SELECT
                 ppc_labour.labour_id
              FROM
                 ppc_labour
              WHERE
                 ppc_labour.labour_db_id_old=ppc_labour_role.labour_db_id_old AND
                 ppc_labour.labour_id_old=ppc_labour_role.labour_id_old
             );

--changeSet MTX-394:645 stripComments:false
-- PPC_LOC
UPDATE
   ppc_loc
SET
   plan_id = (SELECT
                 ppc_plan.plan_id
              FROM
                 ppc_plan
              WHERE
                 ppc_plan.ppc_db_id_old=ppc_loc.ppc_db_id_old AND
                 ppc_plan.ppc_id_old=ppc_loc.ppc_id_old
             ),
   nh_location_id = (SELECT
                          nh_loc.location_id
                       FROM
                        ppc_loc nh_loc
                     WHERE
                        nh_loc.ppc_loc_db_id_old=ppc_loc.nh_ppc_loc_db_id_old AND
                        nh_loc.ppc_loc_id_old=ppc_loc.nh_ppc_loc_id_old
                    );

--changeSet MTX-394:646 stripComments:false
-- PPC_LOC_CAPACITY
UPDATE
   ppc_loc_capacity
SET
   location_id = (SELECT
                     ppc_loc.location_id
                  FROM
                     ppc_loc
                  WHERE
                     ppc_loc.ppc_loc_db_id_old=ppc_loc_capacity.ppc_loc_db_id_old AND
                     ppc_loc.ppc_loc_id_old=ppc_loc_capacity.ppc_loc_id_old
                 );

--changeSet MTX-394:647 stripComments:false
-- PPC_LOC_EXCLUDE
UPDATE
   ppc_loc_exclude
SET
   plan_id = (SELECT
                 ppc_plan.plan_id
              FROM
                 ppc_plan
              WHERE
                 ppc_plan.ppc_db_id_old=ppc_loc_exclude.ppc_db_id_old AND
                 ppc_plan.ppc_id_old=ppc_loc_exclude.ppc_id_old
             );

--changeSet MTX-394:648 stripComments:false
-- PPC_MILESTONE
UPDATE
   ppc_milestone
SET
   milestone_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_milestone.ppc_milestone_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_milestone.ppc_milestone_id_old
                  );

--changeSet MTX-394:649 stripComments:false
-- PPC_MILESTONE_COND
UPDATE
   ppc_milestone_cond
SET
   milestone_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_milestone_cond.ppc_milestone_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_milestone_cond.ppc_milestone_id_old
                  );

--changeSet MTX-394:650 stripComments:false
-- PPC_OPT_STATUS
UPDATE
   ppc_opt_status
SET
   plan_id = (SELECT
                 ppc_plan.plan_id
              FROM
                 ppc_plan
              WHERE
                 ppc_plan.ppc_db_id_old=ppc_opt_status.ppc_db_id_old AND
                 ppc_plan.ppc_id_old=ppc_opt_status.ppc_id_old
             );

--changeSet MTX-394:651 stripComments:false
-- PPC_PHASE
UPDATE
   ppc_phase
SET
   phase_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_phase.ppc_phase_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_phase.ppc_phase_id_old
                  ),
   nr_phase_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_phase.nr_phase_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_phase.nr_phase_id_old
                  ),
   nr_start_milestone_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_phase.nr_start_milestone_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_phase.nr_start_milestone_id_old
                  ),
   nr_end_milestone_id  = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_phase.nr_end_milestone_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_phase.nr_end_milestone_id_old
                  );

--changeSet MTX-394:652 stripComments:false
-- PPC_PHASE_CLASS
UPDATE
   ppc_phase_class
SET
   phase_id = (SELECT
                  ppc_activity.activity_id
               FROM
                  ppc_activity
               WHERE
                  ppc_activity.ppc_activity_db_id_old=ppc_phase_class.ppc_phase_db_id_old AND
                  ppc_activity.ppc_activity_id_old=ppc_phase_class.ppc_phase_id_old
              );

--changeSet MTX-394:653 stripComments:false
-- PPC_PLANNING_TYPE
UPDATE
   ppc_planning_type
SET
   work_package_id = (SELECT
                         ppc_wp.work_package_id
                      FROM
                         ppc_wp
                      WHERE
                         ppc_wp.ppc_wp_db_id_old=ppc_planning_type.ppc_wp_db_id_old AND
                         ppc_wp.ppc_wp_id_old=ppc_planning_type.ppc_wp_id_old
                     );

--changeSet MTX-394:654 stripComments:false
-- PPC_PLANNING_TYPE_SKILL
UPDATE
   ppc_planning_type_skill
SET
   work_package_id = (SELECT
                         ppc_wp.work_package_id
                      FROM
                         ppc_wp
                      WHERE
                         ppc_wp.ppc_wp_db_id_old=ppc_planning_type_skill.ppc_wp_db_id_old AND
                         ppc_wp.ppc_wp_id_old=ppc_planning_type_skill.ppc_wp_id_old
                     );

--changeSet MTX-394:655 stripComments:false
-- PPC_PUBLISH
UPDATE
   ppc_publish
SET
   work_package_id = (SELECT
                         ppc_wp.work_package_id
                      FROM
                         ppc_wp
                      WHERE
                         ppc_wp.ppc_wp_db_id_old=ppc_publish.ppc_wp_db_id_old AND
                         ppc_wp.ppc_wp_id_old=ppc_publish.ppc_wp_id_old
                     );

--changeSet MTX-394:656 stripComments:false
-- PPC_PUBLISH_FAILURE
UPDATE
   ppc_publish_failure
SET
   work_package_id = (SELECT
                         ppc_wp.work_package_id
                      FROM
                         ppc_wp
                      WHERE
                         ppc_wp.ppc_wp_db_id_old=ppc_publish_failure.ppc_wp_db_id_old AND
                         ppc_wp.ppc_wp_id_old=ppc_publish_failure.ppc_wp_id_old
                     ),
   task_id = (SELECT
                 ppc_activity.activity_id
              FROM
                 ppc_activity
              WHERE
                 ppc_activity.ppc_activity_db_id_old=ppc_publish_failure.ppc_task_db_id_old AND
                 ppc_activity.ppc_activity_id_old=ppc_publish_failure.ppc_task_id_old
             );

--changeSet MTX-394:657 stripComments:false
-- PPC_TASK
UPDATE
   ppc_task
SET
   task_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_task.ppc_task_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_task.ppc_task_id_old
                  ),
   phase_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_task.ppc_phase_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_task.ppc_phase_id_old
                  ),
   work_area_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_task.ppc_work_area_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_task.ppc_work_area_id_old
                  ),
   nr_phase_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_task.nr_phase_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_task.nr_phase_id_old
                  ),
   nr_start_milestone_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_task.nr_start_milestone_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_task.nr_start_milestone_id_old
                  ),
   nr_end_milestone_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_task.nr_end_milestone_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_task.nr_end_milestone_id_old
                  ),
   crew_id = (SELECT
                      ppc_crew.crew_id
                   FROM
                      ppc_crew
                   WHERE
                      ppc_crew.ppc_crew_db_id_old=ppc_task.ppc_crew_db_id_old AND
                      ppc_crew.ppc_crew_id_old=ppc_task.ppc_crew_id_old
                  );

--changeSet MTX-394:658 stripComments:false
-- PPC_TASK_DEFN
UPDATE
   ppc_task_defn
SET
   work_package_id = (SELECT
                         ppc_wp.work_package_id
                      FROM
                         ppc_wp
                      WHERE
                         ppc_wp.ppc_wp_db_id_old=ppc_task_defn.ppc_wp_db_id_old AND
                         ppc_wp.ppc_wp_id_old=ppc_task_defn.ppc_wp_id_old
                     );

--changeSet MTX-394:659 stripComments:false
-- PPC_TASK_DEFN_MAP
UPDATE
   ppc_task_defn_map
SET
   task_definition_id = (SELECT
                         ppc_task_defn.task_definition_id
                      FROM
                         ppc_task_defn
                      WHERE
                         ppc_task_defn.ppc_task_defn_db_id_old=ppc_task_defn_map.ppc_task_defn_db_id_old AND
                         ppc_task_defn.ppc_task_defn_id_old=ppc_task_defn_map.ppc_task_defn_id_old
                     ),
   task_id = (SELECT
                         ppc_activity.activity_id
                      FROM
                         ppc_activity
                      WHERE
                         ppc_activity.ppc_activity_db_id_old=ppc_task_defn_map.ppc_task_db_id_old AND
                         ppc_activity.ppc_activity_id_old=ppc_task_defn_map.ppc_task_id_old
                     );

--changeSet MTX-394:660 stripComments:false
-- PPC_WORK_AREA
UPDATE
   ppc_work_area
SET
   work_area_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_work_area.ppc_work_area_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_work_area.ppc_work_area_id_old
                  );

--changeSet MTX-394:661 stripComments:false
-- PPC_WORK_AREA_CREW
UPDATE
   ppc_work_area_crew
SET
   work_area_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_work_area_crew.ppc_work_area_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_work_area_crew.ppc_work_area_id_old
                  ),
   crew_id = (SELECT
                      ppc_crew.crew_id
                   FROM
                      ppc_crew
                   WHERE
                      ppc_crew.ppc_crew_db_id_old=ppc_work_area_crew.ppc_crew_db_id_old AND
                      ppc_crew.ppc_crew_id_old=ppc_work_area_crew.ppc_crew_id_old
                  );

--changeSet MTX-394:662 stripComments:false
-- PPC_WORK_AREA_ZONE
UPDATE
   ppc_work_area_zone
SET
   work_area_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_work_area_zone.ppc_work_area_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_work_area_zone.ppc_work_area_id_old
                  );

--changeSet MTX-394:663 stripComments:false
-- PPC_WP
UPDATE
   ppc_wp
SET
   plan_id = (SELECT
                 ppc_plan.plan_id
              FROM
                 ppc_plan
              WHERE
                 ppc_plan.ppc_db_id_old=ppc_wp.ppc_db_id_old AND
                 ppc_plan.ppc_id_old=ppc_wp.ppc_id_old
             ),
   location_id = (SELECT
                 ppc_loc.location_id
              FROM
                 ppc_loc
              WHERE
                 ppc_loc.ppc_loc_db_id_old=ppc_wp.ppc_loc_db_id_old AND
                 ppc_loc.ppc_loc_id_old=ppc_wp.ppc_loc_id_old
             ),
   nr_phase_id = (SELECT
                      ppc_activity.activity_id
                   FROM
                      ppc_activity
                   WHERE
                      ppc_activity.ppc_activity_db_id_old=ppc_wp.nr_phase_db_id_old AND
                      ppc_activity.ppc_activity_id_old=ppc_wp.nr_phase_id_old
                  ),
   template_id = (SELECT
                      template_wp.work_package_id
                   FROM
                      ppc_wp template_wp
                   WHERE
                      template_wp.ppc_wp_db_id_old=ppc_wp.template_db_id_old AND
                      template_wp.ppc_wp_id_old=ppc_wp.template_id_old
                  );

--changeSet MTX-394:664 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Define the primary key constraints
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_ACTIVITY" add Constraint "PK_PPC_ACTIVITY"  primary key ("ACTIVITY_ID")
   ');
END;
/

--changeSet MTX-394:665 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_CREW" add Constraint "PK_PPC_CREW"  primary key ("CREW_ID")
   ');
END;
/

--changeSet MTX-394:666 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_DEPENDENCY" add Constraint "PK_PPC_DEPENDENCY"  primary key ("FROM_ACTIVITY_ID", "TO_ACTIVITY_ID")
   ');
END;
/

--changeSet MTX-394:667 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_HR" add Constraint "PK_PPC_HR"  primary key ("HUMAN_RESOURCE_ID")
   ');
END;
/

--changeSet MTX-394:668 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_HR_LIC" add Constraint "PK_PPC_HR_LIC"  primary key ("HUMAN_RESOURCE_LIC_ID")
   ');
END;
/

--changeSet MTX-394:669 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_HR_SHIFT_PLAN" add Constraint "PK_PPC_HR_SHIFT_PLAN"  primary key ("HUMAN_RESOURCE_SHIFT_ID")
   ');
END;
/

--changeSet MTX-394:670 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_HR_SLOT" add Constraint "PK_PPC_HR_SLOT"  primary key ("HUMAN_RESOURCE_SLOT_ID")
   ');
END;
/

--changeSet MTX-394:671 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_LABOUR" add Constraint "PK_PPC_LABOUR"  primary key ("LABOUR_ID")
   ');
END;
/

--changeSet MTX-394:672 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_LABOUR_ROLE" add Constraint "PK_PPC_LABOUR_ROLE"  primary key ("LABOUR_ROLE_ID")
   ');
END;
/

--changeSet MTX-394:673 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_LOC" add Constraint "PK_PPC_LOC"  primary key ("LOCATION_ID")
   ');
END;
/

--changeSet MTX-394:674 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_LOC_CAPACITY" add Constraint "PK_PPC_LOC_CAPACITY"  primary key ("LOCATION_CAPACITY_ID")
   ');
END;
/

--changeSet MTX-394:675 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_LOC_EXCLUDE" add Constraint "PK_PPC_LOC_EXCLUDE"  primary key ("PLAN_ID","LOC_DB_ID","LOC_ID")
   ');
END;
/

--changeSet MTX-394:676 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_MILESTONE" add Constraint "PK_PPC_MILESTONE"  primary key ("MILESTONE_ID")
   ');
END;
/

--changeSet MTX-394:677 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_MILESTONE_COND" add Constraint "PK_PPC_MILESTONE_COND"  primary key ("MILESTONE_ID","AC_COND_DB_ID","AC_COND_CD","COND_SET_DB_ID","COND_SET_CD")
   ');
END;
/

--changeSet MTX-394:678 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_OPT_STATUS" add Constraint "PK_PPC_OPT_STATUS"  primary key ("PLAN_ID")
   ');
END;
/

--changeSet MTX-394:679 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PHASE" add Constraint "PK_PPC_PHASE"  primary key ("PHASE_ID")
   ');
END;
/

--changeSet MTX-394:680 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PHASE_CLASS" add Constraint "PK_PPC_PHASE_CLASS"  primary key ("PHASE_CLASS_ID")
   ');
END;
/

--changeSet MTX-394:681 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PLAN" add Constraint "PK_PPC_PLAN"  primary key ("PLAN_ID")
   ');
END;
/

--changeSet MTX-394:682 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PLANNING_TYPE" add Constraint "PK_PPC_PLANNING_TYPE"  primary key ("WORK_PACKAGE_ID","PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
   ');
END;
/

--changeSet MTX-394:683 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PLANNING_TYPE_SKILL" add Constraint "PK_PPC_PLANNING_TYPE_SKILL"  primary key ("WORK_PACKAGE_ID","PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID","LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")
   ');
END;
/

--changeSet MTX-394:684 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PUBLISH" add Constraint "PK_PPC_PUBLISH"  primary key ("WORK_PACKAGE_ID")
   ');
END;
/

--changeSet MTX-394:685 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PUBLISH_FAILURE" add Constraint "PK_PPC_PUBLISH_FAILURE"  primary key ("FAILURE_ID")
   ');
END;
/

--changeSet MTX-394:686 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_TASK" add Constraint "PK_PPC_TASK"  primary key ("TASK_ID")
   ');
END;
/

--changeSet MTX-394:687 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_TASK_DEFN" add Constraint "PK_PPC_TASK_DEFN"  primary key ("TASK_DEFINITION_ID")
   ');
END;
/

--changeSet MTX-394:688 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_TASK_DEFN_MAP" add Constraint "PK_PPC_TASK_DEFN_MAP"  primary key ("TASK_DEFINITION_ID","TASK_ID")
   ');
END;
/

--changeSet MTX-394:689 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WORK_AREA" add Constraint "PK_PPC_WORK_AREA"  primary key ("WORK_AREA_ID")
   ');
END;
/

--changeSet MTX-394:690 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WORK_AREA_CREW" add Constraint "PK_PPC_WORK_AREA_CREW"  primary key ("WORK_AREA_ID","CREW_ID")
   ');
END;
/

--changeSet MTX-394:691 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WORK_AREA_ZONE" add Constraint "PK_PPC_WORK_AREA_ZONE"  primary key ("WORK_AREA_ZONE_ID")
   ');
END;
/

--changeSet MTX-394:692 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_WP" add Constraint "PK_PPC_WP"  primary key ("WORK_PACKAGE_ID")
   ');
END;
/

--changeSet MTX-394:693 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Define NOT NULL constraints
-- PPC_MILESTONE_COND
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PPC_MILESTONE_COND MODIFY (
         AC_COND_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (AC_COND_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:694 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PPC_MILESTONE_COND MODIFY (
         AC_COND_CD Varchar2 (8) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:695 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PPC_MILESTONE_COND MODIFY (
         COND_SET_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (COND_SET_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:696 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PPC_MILESTONE_COND MODIFY (
         COND_SET_CD Varchar2 (8) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:697 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_ACTIVITY
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE PPC_ACTIVITY MODIFY (
         ACTIVITY_ID Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:698 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_activity MODIFY (
         work_package_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:699 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_CREW
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_crew MODIFY (
         crew_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:700 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_crew MODIFY (
         location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:701 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_DEPENDENCY
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_dependency MODIFY (
         from_activity_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:702 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_dependency MODIFY (
         to_activity_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:703 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr MODIFY (
         human_resource_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:704 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr MODIFY (
         plan_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:705 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR_LIC
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_lic MODIFY (
         human_resource_lic_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:706 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_lic MODIFY (
         human_resource_shift_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:707 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR_SHIFT_PLAN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         human_resource_shift_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:708 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         human_resource_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:709 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         crew_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:710 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         capacity_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:711 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_shift_plan MODIFY (
         location_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:712 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_HR_SLOT
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
         human_resource_slot_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:713 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
         labour_role_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:714 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_hr_slot MODIFY (
         human_resource_shift_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:715 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LABOUR
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour MODIFY (
         labour_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:716 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour MODIFY (
         task_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:717 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LABOUR_ROLE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour_role MODIFY (
         labour_role_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:718 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_labour_role MODIFY (
         labour_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:719 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LOC
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc MODIFY (
         location_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:720 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc MODIFY (
         plan_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:721 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc MODIFY (
         nh_location_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:722 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LOC_CAPACITY
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc_capacity MODIFY (
         location_capacity_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:723 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc_capacity MODIFY (
         location_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:724 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_LOC_EXCLUDE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_loc_exclude MODIFY (
         plan_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:725 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_MILESTONE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_milestone MODIFY (
         milestone_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:726 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_MILESTONE_COND
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_milestone_cond MODIFY (
         milestone_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:727 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_OPT_STATUS
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_opt_status MODIFY (
         plan_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:728 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PHASE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         phase_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:729 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         nr_phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:730 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         nr_start_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:731 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase MODIFY (
         nr_end_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:732 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PHASE_CLASS
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase_class MODIFY (
         phase_class_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:733 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_phase_class MODIFY (
         phase_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:734 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PLAN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_plan MODIFY (
         plan_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:735 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PLANNING_TYPE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_planning_type MODIFY (
         work_package_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:736 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PLANNING_TYPE_SKILL
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_planning_type_skill MODIFY (
         work_package_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:737 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PUBLISH
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish MODIFY (
         work_package_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:738 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_PUBLISH_FAILURE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish_failure MODIFY (
         failure_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:739 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish_failure MODIFY (
         work_package_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:740 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_publish_failure MODIFY (
         task_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:741 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_TASK
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         task_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:742 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:743 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         work_area_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:744 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         crew_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:745 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         nr_phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:746 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         nr_start_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:747 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task MODIFY (
         nr_end_milestone_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:748 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_TASK_DEFN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn MODIFY (
         task_definition_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:749 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn MODIFY (
         work_package_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:750 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_TASK_DEFN_MAP
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn_map MODIFY (
         task_definition_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:751 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_task_defn_map MODIFY (
         task_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:752 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WORK_AREA
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area MODIFY (
         work_area_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:753 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WORK_AREA_CREW
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_crew MODIFY (
         work_area_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:754 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_crew MODIFY (
         crew_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:755 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WORK_AREA_ZONE
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_zone MODIFY (
         work_area_zone_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:756 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_work_area_zone MODIFY (
         work_area_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:757 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- PPC_WP
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         work_package_id Raw(16) Default SYS_GUID() NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:758 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         plan_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:759 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         location_id Raw(16) NOT NULL DEFERRABLE
      )
   ');

END;
/

--changeSet MTX-394:760 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         nr_phase_id Raw(16)
      )
   ');

END;
/

--changeSet MTX-394:761 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ppc_wp MODIFY (
         template_id Raw(16)
      )
   ');
END;
/

--changeSet MTX-394:762 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Reestablish foreign keys.
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_activity ADD CONSTRAINT fk_ppcwp_ppcactivity FOREIGN KEY (work_package_id) REFERENCES ppc_wp (work_package_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:763 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_crew ADD CONSTRAINT fk_ppcloc_ppccrew FOREIGN KEY (location_id) REFERENCES ppc_loc (location_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:764 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_dependency ADD CONSTRAINT fk_fromactivity_dependency FOREIGN KEY (from_activity_id) REFERENCES ppc_activity (activity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:765 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_dependency ADD CONSTRAINT fk_toactivity_dependency FOREIGN KEY (to_activity_id) REFERENCES ppc_activity (activity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:766 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr ADD CONSTRAINT fk_ppcplan_ppchr FOREIGN KEY (plan_id) REFERENCES ppc_plan (plan_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:767 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_lic ADD CONSTRAINT fk_ppchrshftpln_ppchrlic FOREIGN KEY (human_resource_shift_id) REFERENCES ppc_hr_shift_plan (human_resource_shift_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:768 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_shift_plan ADD CONSTRAINT fk_ppccrew_ppchrshftpln FOREIGN KEY (crew_id) REFERENCES ppc_crew (crew_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:769 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_shift_plan ADD CONSTRAINT fk_ppchr_ppchrshftpln FOREIGN KEY (human_resource_id) REFERENCES ppc_hr (human_resource_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:770 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_shift_plan ADD CONSTRAINT fk_ppcloc_ppchrshftpln FOREIGN KEY (location_id) REFERENCES ppc_loc (location_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:771 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_shift_plan ADD CONSTRAINT fk_ppcloccap_ppchrshftpln FOREIGN KEY (capacity_id) REFERENCES ppc_loc_capacity (location_capacity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:772 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_slot ADD CONSTRAINT fk_ppchrshiftplan_ppchrslot FOREIGN KEY (human_resource_shift_id) REFERENCES ppc_hr_shift_plan (human_resource_shift_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:773 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_hr_slot ADD CONSTRAINT fk_ppclabrole_ppchrslot FOREIGN KEY (labour_role_id) REFERENCES ppc_labour_role (labour_role_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:774 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_labour ADD CONSTRAINT fk_ppctask_ppclabour FOREIGN KEY (task_id) REFERENCES ppc_task (task_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:775 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_labour_role ADD CONSTRAINT fk_ppclabour_role FOREIGN KEY (labour_id) REFERENCES ppc_labour (labour_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:776 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_loc ADD CONSTRAINT fk_ppcplan_ppcloc FOREIGN KEY (plan_id) REFERENCES ppc_plan (plan_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:777 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_loc_capacity ADD CONSTRAINT fk_ppcloc_ppcloccap FOREIGN KEY (location_id) REFERENCES ppc_loc (location_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:778 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_loc_exclude ADD CONSTRAINT fk_ppcplan_ppclocex FOREIGN KEY (plan_id) REFERENCES ppc_plan (plan_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:779 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_milestone ADD CONSTRAINT fk_ppcactivity_ppcmilestone FOREIGN KEY (milestone_id) REFERENCES ppc_activity (activity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:780 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_milestone_cond ADD CONSTRAINT fk_ppcmilestone_ppcmilestoneco FOREIGN KEY (milestone_id) REFERENCES ppc_milestone (milestone_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:781 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_opt_status ADD CONSTRAINT fk_ppcplan_ppcoptstat FOREIGN KEY (plan_id) REFERENCES ppc_plan (plan_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:782 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_phase ADD CONSTRAINT fk_ppcactivity_ppcphase FOREIGN KEY (phase_id) REFERENCES ppc_activity (activity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:783 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_phase ADD CONSTRAINT fk_nrendmlstn_ppcphase FOREIGN KEY (nr_end_milestone_id) REFERENCES ppc_milestone (milestone_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:784 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_phase ADD CONSTRAINT fk_nrstrtmlstn_ppcphase FOREIGN KEY (nr_start_milestone_id) REFERENCES ppc_milestone (milestone_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:785 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_phase_class ADD CONSTRAINT fk_ppcphase_ppcphaseclass FOREIGN KEY (phase_id) REFERENCES ppc_phase (phase_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:786 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_planning_type ADD CONSTRAINT fk_ppcwp_planningtype FOREIGN KEY (work_package_id) REFERENCES ppc_wp (work_package_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:787 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_planning_type_skill ADD CONSTRAINT fk_ppcplntyp_plntypskill FOREIGN KEY (work_package_id,planning_type_db_id,planning_type_id) REFERENCES ppc_planning_type (work_package_id,planning_type_db_id,planning_type_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:788 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_publish ADD CONSTRAINT fk_ppcwp_ppcpublish FOREIGN KEY (work_package_id) REFERENCES ppc_wp (work_package_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:789 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_publish_failure ADD CONSTRAINT fk_ppcpublish_pubfailure FOREIGN KEY (work_package_id) REFERENCES ppc_publish (work_package_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:790 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_publish_failure ADD CONSTRAINT fk_ppctask_ppcpublishfailure FOREIGN KEY (task_id) REFERENCES ppc_task (task_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:791 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_ppcactivity_ppctask FOREIGN KEY (task_id) REFERENCES ppc_activity (activity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:792 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_ppccrew_ppctask FOREIGN KEY (crew_id) REFERENCES ppc_crew (crew_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:793 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_nrendmilestone_ppctask FOREIGN KEY (nr_end_milestone_id) REFERENCES ppc_milestone (milestone_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:794 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_nrstartmilestone_ppctask FOREIGN KEY (nr_start_milestone_id) REFERENCES ppc_milestone (milestone_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:795 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_nrphase_ppctask FOREIGN KEY (nr_phase_id) REFERENCES ppc_phase (phase_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:796 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_ppcphase_ppctask FOREIGN KEY (phase_id) REFERENCES ppc_phase (phase_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:797 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task ADD CONSTRAINT fk_ppcworkarea_ppctask FOREIGN KEY (work_area_id) REFERENCES ppc_work_area (work_area_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:798 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task_defn ADD CONSTRAINT fk_ppcwp_taskdefn FOREIGN KEY (work_package_id) REFERENCES ppc_wp (work_package_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:799 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task_defn_map ADD CONSTRAINT fk_ppctask_ppctaskdefnmap FOREIGN KEY (task_id) REFERENCES ppc_task (task_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:800 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_task_defn_map ADD CONSTRAINT fk_ppctaskdefn_ppctaskdefnmap FOREIGN KEY (task_definition_id) REFERENCES ppc_task_defn (task_definition_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:801 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_work_area ADD CONSTRAINT fk_ppcactivity_ppcworkarea FOREIGN KEY (work_area_id) REFERENCES ppc_activity (activity_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:802 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_work_area_crew ADD CONSTRAINT fk_ppccrew_ppcwrkareacrw FOREIGN KEY (crew_id) REFERENCES ppc_crew (crew_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:803 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_work_area_crew ADD CONSTRAINT fk_ppcwrkarea_ppcwrkareacrw FOREIGN KEY (work_area_id) REFERENCES ppc_work_area (work_area_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:804 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_work_area_zone ADD CONSTRAINT fk_ppcworkarea_ppcworkareazone FOREIGN KEY (work_area_id) REFERENCES ppc_work_area (work_area_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:805 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_wp ADD CONSTRAINT fk_ppcplan_ppcwp FOREIGN KEY (plan_id) REFERENCES ppc_plan (plan_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:806 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE ppc_wp ADD CONSTRAINT fk_ppctemplate_ppcwp FOREIGN KEY (template_id) REFERENCES ppc_plan (plan_id)  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:807 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_MILESTONE_COND" add Constraint "FK_ACCONDSET_PPCMILESTCOND" foreign key ("AC_COND_DB_ID","AC_COND_CD","COND_SET_DB_ID","COND_SET_CD") references "AC_COND_SETTING" ("AC_COND_DB_ID","AC_COND_CD","COND_SET_DB_ID","COND_SET_CD")  DEFERRABLE
   ');
END;
/

--changeSet MTX-394:808 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Recreate indexes.
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcwp_ppcactivity ON ppc_activity (work_package_id)
   ');
END;
/

--changeSet MTX-394:809 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcloc_ppccrew ON ppc_crew (location_id)
   ');
END;
/

--changeSet MTX-394:810 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_fromactivity_dependency ON ppc_dependency (from_activity_id)
   ');
END;
/

--changeSet MTX-394:811 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_toactivity_dependency ON ppc_dependency (to_activity_id)
   ');
END;
/

--changeSet MTX-394:812 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcplan_ppchr ON ppc_hr (plan_id)
   ');
END;
/

--changeSet MTX-394:813 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppchrshftpln_ppchrlic ON ppc_hr_lic (human_resource_shift_id)
   ');
END;
/

--changeSet MTX-394:814 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppccrew_ppchrshftpln ON ppc_hr_shift_plan (crew_id)
   ');
END;
/

--changeSet MTX-394:815 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppchr_ppchrshftpln ON ppc_hr_shift_plan (human_resource_id )
   ');
END;
/

--changeSet MTX-394:816 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcloccap_ppchrshftpln ON ppc_hr_shift_plan (capacity_id)
   ');
END;
/

--changeSet MTX-394:817 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcloc_ppchrshftpln ON ppc_hr_shift_plan (location_id)
   ');
END;
/

--changeSet MTX-394:818 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppchrshiftplan_ppchrslot ON ppc_hr_slot (human_resource_shift_id)
   ');
END;
/

--changeSet MTX-394:819 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_pplabour_ppchrslot ON ppc_hr_slot (labour_role_id)
   ');
END;
/

--changeSet MTX-394:820 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppctask_ppclabour ON ppc_labour (task_id)
   ');
END;
/

--changeSet MTX-394:821 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_pplabour_pplabourrole ON ppc_labour_role (labour_id)
   ');
END;
/

--changeSet MTX-394:822 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcplan_ppcloc ON ppc_loc (plan_id)
   ');
END;
/

--changeSet MTX-394:823 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcloc_ppcloccap ON ppc_loc_capacity (location_id)
   ');
END;
/

--changeSet MTX-394:824 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_nrendmlstn_ppcphase ON ppc_phase (nr_end_milestone_id)
   ');
END;
/

--changeSet MTX-394:825 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_nrstrtmlstn_ppcphase ON ppc_phase (nr_start_milestone_id)
   ');
END;
/

--changeSet MTX-394:826 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcphase_ppcphaseclass ON ppc_phase_class (phase_id)
   ');
END;
/

--changeSet MTX-394:827 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcplntyp_plntypskill ON ppc_planning_type_skill (work_package_id,planning_type_db_id,planning_type_id)
   ');
END;
/

--changeSet MTX-394:828 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcpublish_pubfailure ON ppc_publish_failure (work_package_id)
   ');
END;
/

--changeSet MTX-394:829 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppctask_ppcpublishfailure ON ppc_publish_failure (task_id)
   ');
END;
/

--changeSet MTX-394:830 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_nrendmilestone_ppctask ON ppc_task (nr_end_milestone_id)
   ');
END;
/

--changeSet MTX-394:831 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_nrphase_ppctask ON ppc_task (nr_phase_id)
   ');
END;
/

--changeSet MTX-394:832 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_nrstartmilestone_ppctask ON ppc_task (nr_start_milestone_id)
   ');
END;
/

--changeSet MTX-394:833 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppccrew_ppctask ON ppc_task (crew_id)
   ');
END;
/

--changeSet MTX-394:834 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcphase_ppctask ON ppc_task (phase_id)
   ');
END;
/

--changeSet MTX-394:835 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcworkarea_ppctask ON ppc_task (work_area_id)
   ');
END;
/

--changeSet MTX-394:836 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcwp_taskdefn ON ppc_task_defn (work_package_id)
   ');
END;
/

--changeSet MTX-394:837 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppctaskdefn_ppctaskdefnmap ON ppc_task_defn_map (task_definition_id)
   ');
END;
/

--changeSet MTX-394:838 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppctask_ppctaskdefnmap ON ppc_task_defn_map (task_id)
   ');
END;
/

--changeSet MTX-394:839 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppccrew_ppcwrkareacrw ON ppc_work_area_crew (crew_id)
   ');
END;
/

--changeSet MTX-394:840 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcwrkarea_ppcwrkareacrw ON ppc_work_area_crew (work_area_id)
   ');
END;
/

--changeSet MTX-394:841 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcworkarea_ppcworkareazone ON ppc_work_area_zone (work_area_id)
   ');
END;
/

--changeSet MTX-394:842 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppcplan_ppcwp ON ppc_wp (plan_id)
   ');
END;
/

--changeSet MTX-394:843 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index ix_ppctemplate_ppcwp ON ppc_wp (template_id)
   ');
END;
/

--changeSet MTX-394:844 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_ACCONDSET_PPCMILSTCOND" ON "PPC_MILESTONE_COND" ("AC_COND_DB_ID","AC_COND_CD","COND_SET_DB_ID","COND_SET_CD")
   ');
END;
/

--changeSet MTX-394:845 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_PPCMILESTONE_PPCMILSTCOND" ON "PPC_MILESTONE_COND" ("MILESTONE_ID")
   ');
END;
/

--changeSet MTX-394:846 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop ALT_ID triggers
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_PPC_ACTIVITY_ALT_ID');
END;
/

--changeSet MTX-394:847 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_PPC_PLAN_ALT_ID');
END;
/

--changeSet MTX-394:848 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_PPC_WP_ALT_ID');
END;
/

--changeSet MTX-394:849 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop ALT_ID columns
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','ALT_ID');
END;
/

--changeSet MTX-394:850 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLAN','ALT_ID');
END;
/

--changeSet MTX-394:851 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','ALT_ID');
END;
/

--changeSet MTX-394:852 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop temporary columns
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLAN','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:853 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLAN','PPC_ID_OLD');
END;
/

--changeSet MTX-394:854 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR','PPC_HR_DB_ID_OLD');
END;
/

--changeSet MTX-394:855 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR','PPC_HR_ID_OLD');
END;
/

--changeSet MTX-394:856 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:857 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR','PPC_ID_OLD');
END;
/

--changeSet MTX-394:858 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','PPC_LOC_DB_ID_OLD');
END;
/

--changeSet MTX-394:859 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','PPC_LOC_ID_OLD');
END;
/

--changeSet MTX-394:860 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:861 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','PPC_ID_OLD');
END;
/

--changeSet MTX-394:862 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','NH_PPC_LOC_DB_ID_OLD');
END;
/

--changeSet MTX-394:863 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC','NH_PPC_LOC_ID_OLD');
END;
/

--changeSet MTX-394:864 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_EXCLUDE','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:865 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_EXCLUDE','PPC_ID_OLD');
END;
/

--changeSet MTX-394:866 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_OPT_STATUS','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:867 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_OPT_STATUS','PPC_ID_OLD');
END;
/

--changeSet MTX-394:868 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:869 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:870 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:871 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_ID_OLD');
END;
/

--changeSet MTX-394:872 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_LOC_DB_ID_OLD');
END;
/

--changeSet MTX-394:873 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','PPC_LOC_ID_OLD');
END;
/

--changeSet MTX-394:874 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','NR_PHASE_DB_ID_OLD');
END;
/

--changeSet MTX-394:875 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','NR_PHASE_ID_OLD');
END;
/

--changeSet MTX-394:876 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','TEMPLATE_DB_ID_OLD');
END;
/

--changeSet MTX-394:877 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WP','TEMPLATE_ID_OLD');
END;
/

--changeSet MTX-394:878 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','PPC_ACTIVITY_DB_ID_OLD');
END;
/

--changeSet MTX-394:879 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','PPC_ACTIVITY_ID_OLD');
END;
/

--changeSet MTX-394:880 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:881 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_ACTIVITY','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:882 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_CREW','PPC_CREW_DB_ID_OLD');
END;
/

--changeSet MTX-394:883 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_CREW','PPC_CREW_ID_OLD');
END;
/

--changeSet MTX-394:884 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_CREW','PPC_LOC_DB_ID_OLD');
END;
/

--changeSet MTX-394:885 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_CREW','PPC_LOC_ID_OLD');
END;
/

--changeSet MTX-394:886 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_DEPENDENCY','FROM_ACTIVITY_DB_ID_OLD');
END;
/

--changeSet MTX-394:887 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_DEPENDENCY','FROM_ACTIVITY_ID_OLD');
END;
/

--changeSet MTX-394:888 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_DEPENDENCY','TO_ACTIVITY_DB_ID_OLD');
END;
/

--changeSet MTX-394:889 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_DEPENDENCY','TO_ACTIVITY_ID_OLD');
END;
/

--changeSet MTX-394:890 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_LIC','PPC_HR_LIC_DB_ID_OLD');
END;
/

--changeSet MTX-394:891 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_LIC','PPC_HR_LIC_ID_OLD');
END;
/

--changeSet MTX-394:892 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_LIC','PPC_HR_SHIFT_DB_ID_OLD');
END;
/

--changeSet MTX-394:893 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_LIC','PPC_HR_SHIFT_ID_OLD');
END;
/

--changeSet MTX-394:894 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_HR_SHIFT_DB_ID_OLD');
END;
/

--changeSet MTX-394:895 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_HR_SHIFT_ID_OLD');
END;
/

--changeSet MTX-394:896 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_HR_DB_ID_OLD');
END;
/

--changeSet MTX-394:897 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_HR_ID_OLD');
END;
/

--changeSet MTX-394:898 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_CREW_DB_ID_OLD');
END;
/

--changeSet MTX-394:899 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_CREW_ID_OLD');
END;
/

--changeSet MTX-394:900 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_CAPACITY_DB_ID_OLD');
END;
/

--changeSet MTX-394:901 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_CAPACITY_ID_OLD');
END;
/

--changeSet MTX-394:902 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_LOC_DB_ID_OLD');
END;
/

--changeSet MTX-394:903 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SHIFT_PLAN','PPC_LOC_ID_OLD');
END;
/

--changeSet MTX-394:904 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','PPC_HR_SLOT_DB_ID_OLD');
END;
/

--changeSet MTX-394:905 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','PPC_HR_SLOT_ID_OLD');
END;
/

--changeSet MTX-394:906 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','LABOUR_ROLE_DB_ID_OLD');
END;
/

--changeSet MTX-394:907 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','LABOUR_ROLE_ID_OLD');
END;
/

--changeSet MTX-394:908 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','PPC_HR_SHIFT_DB_ID_OLD');
END;
/

--changeSet MTX-394:909 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_HR_SLOT','PPC_HR_SHIFT_ID_OLD');
END;
/

--changeSet MTX-394:910 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR','LABOUR_DB_ID_OLD');
END;
/

--changeSet MTX-394:911 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR','LABOUR_ID_OLD');
END;
/

--changeSet MTX-394:912 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR','PPC_TASK_DB_ID_OLD');
END;
/

--changeSet MTX-394:913 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR','PPC_TASK_ID_OLD');
END;
/

--changeSet MTX-394:914 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR_ROLE','LABOUR_ROLE_DB_ID_OLD');
END;
/

--changeSet MTX-394:915 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR_ROLE','LABOUR_ROLE_ID_OLD');
END;
/

--changeSet MTX-394:916 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR_ROLE','LABOUR_DB_ID_OLD');
END;
/

--changeSet MTX-394:917 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LABOUR_ROLE','LABOUR_ID_OLD');
END;
/

--changeSet MTX-394:918 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_CAPACITY','PPC_CAPACITY_DB_ID_OLD');
END;
/

--changeSet MTX-394:919 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_CAPACITY','PPC_CAPACITY_ID_OLD');
END;
/

--changeSet MTX-394:920 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_CAPACITY','PPC_LOC_DB_ID_OLD');
END;
/

--changeSet MTX-394:921 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_CAPACITY','PPC_LOC_ID_OLD');
END;
/

--changeSet MTX-394:922 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_EXCLUDE','PPC_DB_ID_OLD');
END;
/

--changeSet MTX-394:923 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_LOC_EXCLUDE','PPC_ID_OLD');
END;
/

--changeSet MTX-394:924 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_MILESTONE','PPC_MILESTONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:925 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_MILESTONE','PPC_MILESTONE_ID_OLD');
END;
/

--changeSet MTX-394:926 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_MILESTONE_COND','PPC_MILESTONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:927 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_MILESTONE_COND','PPC_MILESTONE_ID_OLD');
END;
/

--changeSet MTX-394:928 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','PPC_PHASE_DB_ID_OLD');
END;
/

--changeSet MTX-394:929 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','PPC_PHASE_ID_OLD');
END;
/

--changeSet MTX-394:930 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_PHASE_DB_ID_OLD');
END;
/

--changeSet MTX-394:931 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_PHASE_ID_OLD');
END;
/

--changeSet MTX-394:932 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_START_MILESTONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:933 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_START_MILESTONE_ID_OLD');
END;
/

--changeSet MTX-394:934 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_END_MILESTONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:935 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE','NR_END_MILESTONE_ID_OLD');
END;
/

--changeSet MTX-394:936 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE_CLASS','PPC_PHASE_CLASS_DB_ID_OLD');
END;
/

--changeSet MTX-394:937 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE_CLASS','PPC_PHASE_CLASS_ID_OLD');
END;
/

--changeSet MTX-394:938 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE_CLASS','PPC_PHASE_DB_ID_OLD');
END;
/

--changeSet MTX-394:939 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PHASE_CLASS','PPC_PHASE_ID_OLD');
END;
/

--changeSet MTX-394:940 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLANNING_TYPE','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:941 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLANNING_TYPE','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:942 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLANNING_TYPE_SKILL','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:943 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PLANNING_TYPE_SKILL','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:944 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:945 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:946 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_FAILURE_DB_ID_OLD');
END;
/

--changeSet MTX-394:947 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_FAILURE_ID_OLD');
END;
/

--changeSet MTX-394:948 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:949 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:950 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_TASK_DB_ID_OLD');
END;
/

--changeSet MTX-394:951 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_PUBLISH_FAILURE','PPC_TASK_ID_OLD');
END;
/

--changeSet MTX-394:952 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_TASK_DB_ID_OLD');
END;
/

--changeSet MTX-394:953 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_TASK_ID_OLD');
END;
/

--changeSet MTX-394:954 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_PHASE_DB_ID_OLD');
END;
/

--changeSet MTX-394:955 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_PHASE_ID_OLD');
END;
/

--changeSet MTX-394:956 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_WORK_AREA_DB_ID_OLD');
END;
/

--changeSet MTX-394:957 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_WORK_AREA_ID_OLD');
END;
/

--changeSet MTX-394:958 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_PHASE_DB_ID_OLD');
END;
/

--changeSet MTX-394:959 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_PHASE_ID_OLD');
END;
/

--changeSet MTX-394:960 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_START_MILESTONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:961 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_START_MILESTONE_ID_OLD');
END;
/

--changeSet MTX-394:962 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_END_MILESTONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:963 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','NR_END_MILESTONE_ID_OLD');
END;
/

--changeSet MTX-394:964 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_CREW_DB_ID_OLD');
END;
/

--changeSet MTX-394:965 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK','PPC_CREW_ID_OLD');
END;
/

--changeSet MTX-394:966 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN','PPC_TASK_DEFN_DB_ID_OLD');
END;
/

--changeSet MTX-394:967 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN','PPC_TASK_DEFN_ID_OLD');
END;
/

--changeSet MTX-394:968 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN','PPC_WP_DB_ID_OLD');
END;
/

--changeSet MTX-394:969 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN','PPC_WP_ID_OLD');
END;
/

--changeSet MTX-394:970 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN_MAP','PPC_TASK_DEFN_DB_ID_OLD');
END;
/

--changeSet MTX-394:971 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN_MAP','PPC_TASK_DEFN_ID_OLD');
END;
/

--changeSet MTX-394:972 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN_MAP','PPC_TASK_DB_ID_OLD');
END;
/

--changeSet MTX-394:973 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_TASK_DEFN_MAP','PPC_TASK_ID_OLD');
END;
/

--changeSet MTX-394:974 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA','PPC_WORK_AREA_DB_ID_OLD');
END;
/

--changeSet MTX-394:975 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA','PPC_WORK_AREA_ID_OLD');
END;
/

--changeSet MTX-394:976 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_CREW','PPC_WORK_AREA_DB_ID_OLD');
END;
/

--changeSet MTX-394:977 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_CREW','PPC_WORK_AREA_ID_OLD');
END;
/

--changeSet MTX-394:978 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_CREW','PPC_CREW_DB_ID_OLD');
END;
/

--changeSet MTX-394:979 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_CREW','PPC_CREW_ID_OLD');
END;
/

--changeSet MTX-394:980 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_ZONE_DB_ID_OLD');
END;
/

--changeSet MTX-394:981 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_ZONE_ID_OLD');
END;
/

--changeSet MTX-394:982 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_DB_ID_OLD');
END;
/

--changeSet MTX-394:983 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PPC_WORK_AREA_ZONE','PPC_WORK_AREA_ID_OLD');
END;
/

--changeSet MTX-394:984 stripComments:false
ALTER TRIGGER tubr_ppc_work_area_zone ENABLE;

--changeSet MTX-394:985 stripComments:false
ALTER TRIGGER tubr_ppc_work_area_crew ENABLE;

--changeSet MTX-394:986 stripComments:false
ALTER TRIGGER tubr_ppc_work_area ENABLE;

--changeSet MTX-394:987 stripComments:false
ALTER TRIGGER tubr_ppc_task_defn_map ENABLE;

--changeSet MTX-394:988 stripComments:false
ALTER TRIGGER tubr_ppc_task_defn ENABLE;

--changeSet MTX-394:989 stripComments:false
ALTER TRIGGER tubr_ppc_task ENABLE;

--changeSet MTX-394:990 stripComments:false
ALTER TRIGGER tubr_ppc_publish_failure ENABLE;

--changeSet MTX-394:991 stripComments:false
ALTER TRIGGER tubr_ppc_publish ENABLE;

--changeSet MTX-394:992 stripComments:false
ALTER TRIGGER tubr_ppc_planning_type_skill ENABLE;

--changeSet MTX-394:993 stripComments:false
ALTER TRIGGER tubr_ppc_planning_type ENABLE;

--changeSet MTX-394:994 stripComments:false
ALTER TRIGGER tubr_ppc_phase_class ENABLE;

--changeSet MTX-394:995 stripComments:false
ALTER TRIGGER tubr_ppc_phase ENABLE;

--changeSet MTX-394:996 stripComments:false
ALTER TRIGGER tubr_ppc_milestone_cond ENABLE;

--changeSet MTX-394:997 stripComments:false
ALTER TRIGGER tubr_ppc_milestone ENABLE;

--changeSet MTX-394:998 stripComments:false
ALTER TRIGGER tubr_ppc_loc_exclude ENABLE;

--changeSet MTX-394:999 stripComments:false
ALTER TRIGGER tubr_ppc_loc_capacity ENABLE;

--changeSet MTX-394:1000 stripComments:false
ALTER TRIGGER tubr_ppc_labour_role ENABLE;

--changeSet MTX-394:1001 stripComments:false
ALTER TRIGGER tubr_ppc_labour ENABLE;

--changeSet MTX-394:1002 stripComments:false
ALTER TRIGGER tubr_ppc_hr_slot ENABLE;

--changeSet MTX-394:1003 stripComments:false
ALTER TRIGGER tubr_ppc_hr_shift_plan ENABLE;

--changeSet MTX-394:1004 stripComments:false
ALTER TRIGGER tubr_ppc_hr_lic ENABLE;

--changeSet MTX-394:1005 stripComments:false
ALTER TRIGGER tubr_ppc_dependency ENABLE;

--changeSet MTX-394:1006 stripComments:false
ALTER TRIGGER tubr_ppc_crew ENABLE;

--changeSet MTX-394:1007 stripComments:false
ALTER TRIGGER tubr_ppc_activity ENABLE;

--changeSet MTX-394:1008 stripComments:false
ALTER TRIGGER tubr_ppc_wp ENABLE;

--changeSet MTX-394:1009 stripComments:false
ALTER TRIGGER tubr_ppc_opt_status ENABLE;

--changeSet MTX-394:1010 stripComments:false
ALTER TRIGGER tubr_ppc_loc_exclude ENABLE;

--changeSet MTX-394:1011 stripComments:false
ALTER TRIGGER tubr_ppc_loc ENABLE;

--changeSet MTX-394:1012 stripComments:false
ALTER TRIGGER tubr_ppc_hr ENABLE;

--changeSet MTX-394:1013 stripComments:false
ALTER TRIGGER tubr_ppc_plan ENABLE;