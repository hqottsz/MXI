--liquibase formatted sql


--changeSet MX-20639:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN  
  -- first add the column
  utl_migr_schema_pkg.table_column_add('  
    Alter table SCHED_LABOUR_ACTION add (   
    SIGNED_BOOL Number(1,0)  
    )  
  ');  
END;  
/  

--changeSet MX-20639:2 stripComments:false
-- update existing actions to be signed
UPDATE SCHED_LABOUR_ACTION SET SIGNED_BOOL = 1;   

--changeSet MX-20639:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
  -- modify the column to be non-null and  default to 0
  utl_migr_schema_pkg.table_column_modify(' 
    Alter table SCHED_LABOUR_ACTION modify (  
    SIGNED_BOOL Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SIGNED_BOOL IN (0, 1) ) DEFERRABLE  
    ) 
  '); 
END; 
/  