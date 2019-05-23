--liquibase formatted sql


--changeSet OPER-1243:1 stripComments:false
ALTER TRIGGER tubr_ppc_milestone DISABLE;

--changeSet OPER-1243:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_MILESTONE add (START_DT Date)
   ');
END;
/

--changeSet OPER-1243:3 stripComments:false
UPDATE 
   ppc_milestone 
SET 
   start_dt=(
      SELECT
         DECODE (ppc_wp.START_DT, null, SYSDATE, ppc_wp.START_DT)
      FROM
         ppc_wp
         INNER JOIN ppc_activity ON ppc_activity.work_package_id = ppc_wp.work_package_id
      WHERE
         ppc_activity.activity_id = ppc_milestone.milestone_id
   );

--changeSet OPER-1243:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table PPC_MILESTONE modify (START_DT Date NOT NULL DEFERRABLE)
   ');
END;
/

--changeSet OPER-1243:5 stripComments:false
ALTER TRIGGER tubr_ppc_milestone ENABLE;