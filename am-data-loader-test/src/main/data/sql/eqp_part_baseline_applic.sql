UPDATE eqp_part_baseline t SET appl_eff_ldesc = '001-098'
WHERE t.bom_part_id IN (SELECT bom_part_id FROM eqp_bom_part u WHERE u.assmbl_cd = 'ACFT_CD1' AND u.bom_part_cd = 'ENG-ASSY') AND
t.part_no_id IN (SELECT v.part_no_id FROM eqp_part_no v WHERE v.part_no_oem = 'ENG_ASSY_PN2' AND v.manufact_cd = 'ABC11');

UPDATE eqp_part_baseline t SET appl_eff_ldesc = '001-098'
WHERE t.bom_part_id IN (SELECT bom_part_id FROM eqp_bom_part u WHERE u.assmbl_cd = 'ACFT_CD1' AND u.bom_part_cd = 'ACFT_CD1') AND
t.part_no_id IN (SELECT v.part_no_id FROM eqp_part_no v WHERE v.part_no_oem = 'ACFT_ASSY_PN1' AND v.manufact_cd = '10001');

UPDATE eqp_part_baseline t SET appl_eff_ldesc = '002-005'
WHERE t.bom_part_id IN (SELECT bom_part_id FROM eqp_bom_part u WHERE u.assmbl_cd = 'ACFT_CD1' AND u.bom_part_cd = 'ACFT-SYS-1-1-TRK-P1') AND
t.part_no_id IN (SELECT v.part_no_id FROM eqp_part_no v WHERE v.part_no_oem = 'A0000001REC' AND v.manufact_cd = '10001');

UPDATE eqp_part_baseline t SET appl_eff_ldesc = '001-002'
WHERE t.bom_part_id IN (SELECT bom_part_id FROM eqp_bom_part u WHERE u.assmbl_cd = 'NO_PARTS' AND u.bom_part_cd = 'TRK-NO-PART-APPLIC') AND
t.part_no_id IN (SELECT v.part_no_id FROM eqp_part_no v WHERE v.part_no_oem = 'NP0001' AND v.manufact_cd = '10001');