--liquibase formatted sql


--changeSet DEV-367:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION generate_parm_hash
(
    p_alert_id utl_alert.alert_id%TYPE,
    p_alert_type_id utl_alert.alert_type_id%TYPE
) RETURN VARCHAR2
IS
   v_string VARCHAR2(32767);
   v_parm_value utl_alert_parm.parm_value%TYPE;
   CURSOR cur_alert_parms( p_lookup_alert_id utl_alert.alert_id%TYPE ) IS
      SELECT
         utl_alert_parm.parm_value
      FROM
         utl_alert_parm
      WHERE
         utl_alert_parm.alert_id = p_lookup_alert_id
      ORDER BY
         utl_alert_parm.parm_ord;
BEGIN
   -- Initialize the string
   v_string := p_alert_type_id;

   -- Loop over alert parameters and append values to the string
   OPEN cur_alert_parms( p_alert_id );
   LOOP
       FETCH cur_alert_parms INTO v_parm_value;
       EXIT WHEN cur_alert_parms%NOTFOUND;
       v_string := v_string || '|' || v_parm_value;
   END LOOP;
   CLOSE cur_alert_parms;

   RETURN mx_utils_pkg.hashmd5( v_string );

END generate_parm_hash;
/

--changeSet DEV-367:2 stripComments:false
UPDATE 
   utl_alert
SET
   utl_alert.parm_hash = generate_parm_hash( utl_alert.alert_id, utl_alert.alert_type_id )
WHERE
   utl_alert.parm_hash IS NULL;

--changeSet DEV-367:3 stripComments:false
DROP FUNCTION generate_parm_hash;

--changeSet DEV-367:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.index_create('
    Create Index "UTL_ALERT_DUPLICATE" ON "UTL_ALERT" ("ALERT_STATUS_CD","ALERT_TYPE_ID","PARM_HASH")
     ');
     END;
     /             

--changeSet DEV-367:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table UTL_ALERT modify (
    "PARM_HASH" Varchar2 (32) NOT NULL DEFERRABLE
  )
  ');
 END;
 /             