INSERT INTO ref_sensitivity (SENSITIVITY_CD, DESC_SDESC, DESC_LDESC, ORD_ID,WARNING_LDESC, ACTIVE_BOOL)
VALUES ('AT_SENS', 'ATTESTSDESC', 'ATTESTLDESC',(select MAX(ORD_ID)+1 from ref_sensitivity),'BL TESTs USED', 0);
INSERT INTO ref_sensitivity (SENSITIVITY_CD, DESC_SDESC, DESC_LDESC, ORD_ID,WARNING_LDESC, ACTIVE_BOOL)
VALUES ('AT_SN2', 'ATTESTSDESC2', 'ATTESTLDESC2',(select MAX(ORD_ID)+1 from ref_sensitivity),'BL TESTs 2 USED', 0);
INSERT INTO ref_sensitivity (SENSITIVITY_CD, DESC_SDESC, DESC_LDESC, ORD_ID,WARNING_LDESC, ACTIVE_BOOL)
VALUES ('AT_SN3', 'ATTESTSDESC3', 'ATTESTLDESC3',(select MAX(ORD_ID)+1 from ref_sensitivity),'BL TESTs 3 USED', 0);
INSERT INTO ref_sensitivity (SENSITIVITY_CD, DESC_SDESC, DESC_LDESC, ORD_ID,WARNING_LDESC, ACTIVE_BOOL)
VALUES ('at_sn3', 'attestdesc3', 'attestldesc3',(select MAX(ORD_ID)+1 from ref_sensitivity),'BL TESTs 3 USED', 0);
INSERT INTO ref_sensitivity (SENSITIVITY_CD, DESC_SDESC, DESC_LDESC, ORD_ID,WARNING_LDESC, ACTIVE_BOOL)
VALUES ('AUTOSENS', 'AUTOTESTSDESC', 'AUTOTESTLDESC',(select MAX(ORD_ID)+1 from ref_sensitivity),'AUTO TESTs USED', 0);