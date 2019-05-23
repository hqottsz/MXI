INSERT INTO eqp_part_baseline (BOM_PART_DB_ID, BOM_PART_ID, PART_NO_DB_ID, PART_NO_ID, STANDARD_BOOL, INTERCHG_ORD, APPL_EFF_LDESC, APPROVED_BOOL, CONDITIONAL_BOOL)
VALUES (4650, (SELECT bom_part_id FROM eqp_bom_part t where t.bom_part_cd = 'TRK-NO-PART-APPLIC' ), 4650, part_no_id_seq.nextval, 1, 1, '001-002', 1, 0);

INSERT INTO eqp_part_baseline (BOM_PART_DB_ID, BOM_PART_ID, PART_NO_DB_ID, PART_NO_ID, STANDARD_BOOL, INTERCHG_ORD, APPL_EFF_LDESC, APPROVED_BOOL, CONDITIONAL_BOOL)
VALUES (4650, (SELECT bom_part_id FROM eqp_bom_part t where t.bom_part_cd = 'TRK-ACFT-T1-G1' ), 4650, (SELECT PART_NO_ID FROM eqp_part_no t where t.part_no_oem = 'PN200' ), 1, 1, null, 1, 0);

INSERT INTO eqp_part_baseline (BOM_PART_DB_ID, BOM_PART_ID, PART_NO_DB_ID, PART_NO_ID, STANDARD_BOOL, INTERCHG_ORD, APPL_EFF_LDESC, APPROVED_BOOL, CONDITIONAL_BOOL)
VALUES (4650, (SELECT bom_part_id FROM eqp_bom_part t where t.bom_part_cd = 'TRK-ACFT-T1-G2' ), 4650, (SELECT PART_NO_ID FROM eqp_part_no t where t.part_no_oem = 'PN100' ), 1, 1, null, 1, 0);
