-- Add 4650 to mim_db
INSERT INTO mim_db( db_id, site_cd, db_type_cd, db_name, db_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
SELECT 4650, 0, 'CUST', 'DBNAME', 'DBLDESC', 0, SYSDATE, SYSDATE, 0, 'MXI' FROM DUAL WHERE NOT EXISTS (SELECT db_id FROM mim_db WHERE db_id = 4650);

-- Update mim_local_db to 4650
DELETE FROM mim_local_db;
INSERT INTO mim_local_db( db_id, exec_mode_cd )
VALUES (4650, NULL);

-- Add Common Hardware
@sql/tse-comhw-assembly.sql

-- Add Data Type
@sql/mim_data_type.sql

-- Add MXI Carrier
@sql/org_carrier.sql

-- Add default forecast model
@sql/forecast-model.sql

--add labour skills
@sql/ref_labour_skill.sql

--add labour skills to organizations
@sql/org_labour_skill_map.sql

--add task subclasses
@sql/ref_task_subclass.sql

--add ref doc disposition
@sql/ref_task_def_disposition.sql

--add MOC Carrier
@sql/persona/maintenanceController/create_moc_carrier.sql
