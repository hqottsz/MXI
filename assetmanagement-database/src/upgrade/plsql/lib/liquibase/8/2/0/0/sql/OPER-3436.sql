--liquibase formatted sql


--changeSet OPER-3436:1 stripComments:false
-- disable the update trigger since there is a record in org_carrie table that has rstat_cd=3
-- and the trigger would stop update for that record 
/************************************************************
** Set supply chain columns in org_carrier table to not null
** 
*************************************************************/
ALTER TRIGGER TUBR_ORG_CARRIER DISABLE;

--changeSet OPER-3436:2 stripComments:false
UPDATE org_carrier
SET supply_chain_db_id = 0,
    supply_chain_cd = 'DEFAULT'
WHERE
    supply_chain_db_id IS NULL;        

--changeSet OPER-3436:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table org_carrier modify (
   supply_chain_db_id Number(10,0) NOT NULL DEFERRABLE Check (supply_chain_db_id BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-3436:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table org_carrier modify (
   supply_chain_cd Varchar2 (8) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-3436:5 stripComments:false
-- enable the trigger again
ALTER TRIGGER TUBR_ORG_CARRIER ENABLE;