--liquibase formatted sql


--changeSet 0ref_supply_chain:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SUPPLY_CHAIN"
** 0-Level
** DATE: 21-APRIL-2015
*********************************************/
INSERT INTO REF_SUPPLY_CHAIN( SUPPLY_CHAIN_DB_ID, SUPPLY_CHAIN_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'DEFAULT', 'Material requests fulfilled internally', 'The material requests are fulfilled by Maintenix Operator Edition.', 0, TO_DATE('2015-04-21','YYYY-MM-DD'), TO_DATE('2015-04-21','YYYY-MM-DD'), 100, 'MXI' );

--changeSet 0ref_supply_chain:2 stripComments:false
INSERT INTO REF_SUPPLY_CHAIN( SUPPLY_CHAIN_DB_ID, SUPPLY_CHAIN_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'EXTERNAL', 'Material requests fulfilled externally', 'The material requests are fulfilled by an external supply chain.', 0, TO_DATE('2015-04-21','YYYY-MM-DD'), TO_DATE('2015-04-21','YYYY-MM-DD'), 100, 'MXI' );