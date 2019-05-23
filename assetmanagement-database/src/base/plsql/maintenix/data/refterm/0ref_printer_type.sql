--liquibase formatted sql


--changeSet 0ref_printer_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PRINTER_TYPE"
** 0-Level
*********************************************/
INSERT INTO ref_printer_type ( printer_type_db_id, printer_type_cd, desc_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ( 0, 'PHYSICAL', 'Physical printer type', 0, '23-MAR-01', '23-MAR-01', 0, 'MXI' );

--changeSet 0ref_printer_type:2 stripComments:false
INSERT INTO ref_printer_type ( printer_type_db_id, printer_type_cd, desc_sdesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ( 0, 'FILE', 'Save to file', 0, '20-SEP-06', '20-SEP-06', 0, 'MXI' );

--changeSet 0ref_printer_type:3 stripComments:false
INSERT INTO ref_printer_type ( printer_type_db_id, printer_type_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
VALUES ( 0, 'EXTERNAL', 'Custom printer implementation', 'Core print logic will not execute, a custom implementation must be defined for printing to occur', 1, TO_DATE('2018-12-18', 'YYYY-MM-DD'), TO_DATE('2018-12-18', 'YYYY-MM-DD'), 0, 'MXI' );