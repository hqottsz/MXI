-- Add 4650 to mim_db
INSERT INTO mim_db( db_id, site_cd, db_type_cd, db_name, db_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
SELECT 4650, 0, 'CUST', 'DBNAME', 'DBLDESC', 0, SYSDATE, SYSDATE, 0, 'MXI' FROM DUAL WHERE NOT EXISTS (SELECT db_id FROM mim_db WHERE db_id = 4650);

-- Update mim_local_db to 4650
DELETE FROM mim_local_db;
INSERT INTO mim_local_db( db_id, exec_mode_cd )
VALUES (4650, NULL);


--Change view
@sql/disableForeignKey.sql

-- Add Common Hardware
@sql/tse-comhw-assembly.sql

-- Add Data Type
@sql/mim_data_type.sql

-- Add MXI Carrier
@sql/org_carrier.sql

-- Add default forecast model
@sql/forecast-model.sql

--Add task_Defn and task_task for faults module
@sql/task_faults.sql

--Add duplicate fail sev cd
@sql/ref_fail_sev.sql

--Move from control.post.sql
@sql/ref_fail_defer.sql
@sql/ref_fail_sev_defer.sql

--Add ref defer status
@sql/ref_fail_defer_ref_status.sql

-- Add long owner cd
@sql/inv_owner.sql

--Move from control.after.baseline.sql
@sql/assmbl_cap_levels.sql

@sql/ref_ietm_type.sql

--cal parameter setting
@sql/usage_cyc.sql
@sql/usage_cyc_body.sql
@sql/usage_hr.sql
@sql/usage_hr_body.sql

@sql/apu_usage_apucyc.sql
@sql/apu_usage_apucyc_body.sql
--add sensitivity
@sql/ref_sensitivity.sql
@sql/ref_inv_cond.sql




