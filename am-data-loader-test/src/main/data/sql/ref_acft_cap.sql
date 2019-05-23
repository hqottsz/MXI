INSERT INTO ref_acft_cap (acft_cap_DB_ID, acft_cap_cd,desc_sdesc,cap_order,rstat_cd)
VALUES (10, 'AUTOTEST','AUTOTEST SDESC', (select max(cap_order)+1 from ref_acft_cap),0);
INSERT INTO ref_acft_cap (acft_cap_DB_ID, acft_cap_cd,desc_sdesc,cap_order,rstat_cd)
VALUES (10, 'autotest','autotest sdesc', (select max(cap_order)+1 from ref_acft_cap),0);
INSERT INTO ref_acft_cap (acft_cap_DB_ID, acft_cap_cd,desc_sdesc,cap_order,rstat_cd)
VALUES (10, 'CAPTEST','CAPTEST SDESC', (select max(cap_order)+1 from ref_acft_cap),0);
