--liquibase formatted sql


--changeSet formatApplSQLForDisplay:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION formatApplSQLForDisplay (
  iv_task_appl_sql_ldesc  task_task.task_appl_sql_ldesc%TYPE
)
RETURN VARCHAR IS

  lv_formatted       VARCHAR2(4000 CHAR);

 BEGIN

   SELECT
      LISTAGG(replaced,' ') WITHIN GROUP (ORDER BY sorting)
   INTO
      lv_formatted
   FROM
      (
         SELECT
            replaced,
            sorting,
            ROW_NUMBER() OVER (PARTITION BY sorting ORDER BY replaced_flag DESC) rn
         FROM
            (
               SELECT
                  -- distinct has been used to filter out duplicate
                  -- caused by CROSS JOIN
                  DISTINCT
                  chunk.lvl sorting,
                  REPLACE(chunk.entity,sql_table_field_map,identifier_cd) replaced,
                  CASE
                    WHEN INSTR(chunk.entity,sql_table_field_map) > 0 THEN
                       1
                    ELSE
                       0
                  END replaced_flag
               FROM
                 (
                  SELECT
                    sql_table_field_map,
                    identifier_cd
                  FROM
                    ( -- Originally stored in OPR_APPL_CODE table. Hardcoded to be able to move to CORE.
                        SELECT
                          'rootpart.part_no_oem' sql_table_field_map,
                          '[Aircraft Part No]'   identifier_cd
                        FROM
                           dual
                        UNION ALL
                        SELECT 'ac_inv.serial_no_oem'   ,'[Aircraft Serial No]'     FROM dual UNION ALL
                        SELECT 'inv_ac_reg.fin_no_cd'   ,'[Aircraft Fin No]'        FROM dual UNION ALL
                        SELECT 'inv_ac_reg.var_no_oem'  ,'[Aircraft Var No]'        FROM dual UNION ALL
                        SELECT 'inv_ac_reg.line_no_oem' ,'[Aircraft Line No]'       FROM dual UNION ALL
                        SELECT 'org_carrier.carrier_cd' ,'[Aircraft Operator]'      FROM dual UNION ALL
                        SELECT 'org_carrier.iata_cd'    ,'[Aircraft Operator]'      FROM dual UNION ALL
                        SELECT 'org_carrier.icao_cd'    ,'[Aircraft Operator]'      FROM dual UNION ALL
                        SELECT 'asspart.part_no_oem'    ,'[Assembly Part No]'       FROM dual UNION ALL
                        SELECT 'assembly.serial_no_oem' ,'[Assembly Serial No]'     FROM dual UNION ALL
                        SELECT 'ass_carrier.carrier_cd' ,'[Assembly Operator]'      FROM dual UNION ALL
                        SELECT 'ass_owner.owner_name'   ,'[Assembly Owner Name]'    FROM dual UNION ALL
                        SELECT 'ass_owner.owner_code'   ,'[Assembly Owner Code]'    FROM dual UNION ALL
                        SELECT 'eqp_part_no.part_no_oem','[Component Part No]'      FROM dual UNION ALL
                        SELECT 'inv_inv.serial_no_oem'  ,'[Component Serial No]'    FROM dual UNION ALL
                        SELECT 'eqp_part_no.manufact_cd','[Component Manufacturer]' FROM dual UNION ALL
                        SELECT 'inv_owner.owner_name'   ,'[Component Owner Name]'   FROM dual UNION ALL
                        SELECT 'inv_owner.owner_cd'     ,'[Component Owner Code]'   FROM dual
                    )
                 )
                 CROSS JOIN ( -- chunk: coverting from row to column using space as delimiter
                              SELECT
                                 LEVEL lvl,
                                 REGEXP_SUBSTR(tbc,'[^ ]+',1,LEVEL) entity
                              FROM
                                 (
                                   SELECT
                                      iv_task_appl_sql_ldesc tbc
                                   FROM
                                     dual
                                  )
                              CONNECT BY REGEXP_SUBSTR(tbc,'[^ ]+',1,level) IS NOT NULL
                            ) chunk
              )
        )
   WHERE
      rn = 1;

  RETURN  lv_formatted;

END;
/