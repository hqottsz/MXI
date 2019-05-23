--liquibase formatted sql
SET SERVEROUTPUT ON SIZE UNLIMITED

--changeSet MX-23184:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- CANCEL_SIGNED_BOOL column is added to SCHED_ACTION table
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE SCHED_ACTION add (
"CANCEL_SIGNED_BOOL" Number(1,0)
)
');
END;
/

--changeSet MX-23184:2 stripComments:false
-- update existing actions to be marked for not rollback action cancell info
UPDATE SCHED_ACTION SET CANCEL_SIGNED_BOOL = 1; 

--changeSet MX-23184:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE SCHED_ACTION modify (
"CANCEL_SIGNED_BOOL" Number(1,0) Default 1 NOT NULL DEFERRABLE Check (CANCEL_SIGNED_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet MX-23184:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- The primary key of the REF_PART_PROVIDER_TYPE was renamed from
-- pk_REF_PART_PROVIDER_TYPE to PK_REF_PART_PROVIDER_TYPE in the 
-- datamodel changes for this JIRA, therefore, this migration script
-- also needs to rename this primary key to uppercase.
DECLARE
   v_sql VARCHAR(200);
BEGIN 

   FOR x IN (SELECT table_name, constraint_name
             FROM   user_constraints
             WHERE  table_name = 'REF_PART_PROVIDER_TYPE'
             AND    constraint_name = 'pk_REF_PART_PROVIDER_TYPE')
   LOOP
   
      v_sql := 'ALTER TABLE ' || x.table_name || ' RENAME CONSTRAINT "' || 
               x.constraint_name || '" TO "' || UPPER(x.constraint_name) || '"';

      DBMS_OUTPUT.PUT_LINE('RUNNING THE FOLLOWING SQL STATEMENT: ' || CHR(10) || v_sql || ';');
               
      EXECUTE IMMEDIATE (v_sql);
   
   END LOOP;

END;
/