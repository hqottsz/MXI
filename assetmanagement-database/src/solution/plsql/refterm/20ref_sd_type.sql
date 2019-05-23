/****************************************************
** 20-LEVEL INSERT SCRIPT FOR TABLE "REF_SD_TYPE"
****************************************************/
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) values (20, 'MIF', 0, 66, 'Minor Failure', 'Failure condition that does not significantly reduce airplane safety and threat involve crew action that are well within their capability.  Minor failure conditions may include, for example slight reduction in margin or functional capabilities, a slight increase in crew workload, such as routine flight plan changes, or some inconvenience to occupants.', 0, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) values (20, 'MAF', 0, 65, 'Major Failure', 'Failure condition that would reduce the capability of the airplane or the ability of the crew to cope with adverse condition of the extend that there would be, for example, a significant reduction in safety margin or functional capabilities, a significant increase in crew workload or in conditions impairing crew efficiency or discomfort to occupants, possibility including injuries.', 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) values (20, 'HF', 0, 116, 'Hazardous Failure', 'Failure conditions that would reduce the capability of the airplane or the ability of the crew to cope with adverse operating conditions to the extent that there would be: a large reduction in safety margins or functional capabilities, physical distress or higher workload such that the flight crew cannot be relied upon to perform their tasks accurately or completely, serious or fatal injury to a relatively small number of occupants.', 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_sd_type(sd_type_db_id, sd_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, report_reqd_bool,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) values (20, 'CF', 0, 115, 'Catastrophic Failure', 'Failure condition that would prevent the continued safe flight and landing of the airplane.', 1, 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');