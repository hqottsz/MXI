--liquibase formatted sql


--changeSet OPER-3714:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- new REQ_PART.ISSUED_DATE column
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REQ_PART add (
   ISSUE_DATE Date
)
');
END;
/

--changeSet OPER-3714:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- REQ_PART.RECEIVED_BY_HR_X columns - foreign key to ORG_HR
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REQ_PART add (
   RECEIVED_BY_HR_DB_ID Number(10,0) Check (RECEIVED_BY_HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-3714:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REQ_PART add (
   RECEIVED_BY_HR_ID Number(10,0) Check (RECEIVED_BY_HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-3714:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- RECEIVED BY foreign key constraints
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_ORGHR_REQPART_REC ON REQ_PART (RECEIVED_BY_HR_DB_ID,RECEIVED_BY_HR_ID)
');
END;
/

--changeSet OPER-3714:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
alter table REQ_PART add Constraint FK_ORGHR_REQPART_REC foreign key (RECEIVED_BY_HR_DB_ID,RECEIVED_BY_HR_ID) references ORG_HR (HR_DB_ID,HR_ID)  DEFERRABLE
');
END;
/

--changeSet OPER-3714:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REQ_PART add (
   SUPPLY_CHAIN_DB_ID Number(10,0)
)
');
END;
/

--changeSet OPER-3714:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REQ_PART add (
   SUPPLY_CHAIN_CD Varchar2(8)
)
');
END;
/

--changeSet OPER-3714:8 stripComments:false
ALTER TRIGGER TUBR_REQ_PART DISABLE;

--changeSet OPER-3714:9 stripComments:false
UPDATE REQ_PART SET SUPPLY_CHAIN_DB_ID = 0, SUPPLY_CHAIN_CD = 'DEFAULT' WHERE SUPPLY_CHAIN_CD is null; 

--changeSet OPER-3714:10 stripComments:false
ALTER TRIGGER TUBR_REQ_PART ENABLE; 

--changeSet OPER-3714:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REQ_PART modify (
   SUPPLY_CHAIN_DB_ID Number(10,0) NOT NULL DEFERRABLE Check (SUPPLY_CHAIN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-3714:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REQ_PART modify (
   SUPPLY_CHAIN_CD Varchar2 (8) NOT NULL DEFERRABLE 
)
');
END;
/

--changeSet OPER-3714:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- REF_SUPPLY_CHAIN BY foreign key constraints
BEGIN
utl_migr_schema_pkg.index_create('
Create Index IX_FK_SUPPLYCHAIN_REQPART ON REQ_PART (SUPPLY_CHAIN_DB_ID,SUPPLY_CHAIN_CD)
');
END;
/

--changeSet OPER-3714:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
alter table REQ_PART add Constraint FK_SUPPLYCHAIN_REQPART foreign key (SUPPLY_CHAIN_DB_ID,SUPPLY_CHAIN_CD) references REF_SUPPLY_CHAIN (SUPPLY_CHAIN_DB_ID,SUPPLY_CHAIN_CD)  DEFERRABLE
');
END;
/