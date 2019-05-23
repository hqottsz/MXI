--liquibase formatted sql


--changeSet QC-7917:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "SSO_TICKET" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"PRINCIPAL" Varchar2 (40) NOT NULL DEFERRABLE ,
	"EXPIRE_DT" Date NOT NULL DEFERRABLE ,
 Constraint "PK_SSO_TICKET" primary key ("ID") 
) 
');
END;
/