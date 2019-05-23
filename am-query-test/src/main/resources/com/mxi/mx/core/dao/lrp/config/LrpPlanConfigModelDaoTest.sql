delete from REF_LRP_CONFIG_SEV;
insert into REF_LRP_CONFIG_SEV (LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'CRITICAL', 'Critical', 'Events that are critical', 'FF0000', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into REF_LRP_CONFIG_SEV (LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'IGNORE', 'Ignore', 'Events that can be ignored', 'FFFFFF', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
insert into REF_LRP_CONFIG_SEV (LRP_CONFIG_SEV_DB_ID, LRP_CONFIG_SEV_CD, DESC_SDESC, DESC_LDESC, HEX_COLOR, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'NONCRITC', 'Non-critical', 'Events that are non-critical', 'FFFF00', 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');