--liquibase formatted sql


--changeSet OPER-2966:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add new columns to PO_HEADER
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- add foreign key AUTH_STATUS_DB_ID and AUTH_STATUS_CD to PO_HEADER table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   AUTH_STATUS_DB_ID Number(10,0) Check (AUTH_STATUS_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet OPER-2966:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   AUTH_STATUS_CD Varchar2 (10) 
)
');
END;
/

--changeSet OPER-2966:3 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Populate the values for the new columns
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
ALTER TRIGGER TUBR_PO_HEADER DISABLE;

--changeSet OPER-2966:4 stripComments:false
UPDATE 
   po_header 
SET
   auth_status_db_id = 0,
   auth_status_cd    = 'BLKOUT'
WHERE
   po_db_id = 0
;

--changeSet OPER-2966:5 stripComments:false
UPDATE 
   po_header
SET 
   ( auth_status_db_id, auth_status_cd ) 
   = 
   ( 
      SELECT
         auth_status_db_id,
         auth_status_cd
      FROM
         (
            -- get the auth status for all POs in the po_header
            SELECT
               po_header.po_db_id,
               po_header.po_id,
               NVL( auth.auth_lvl_status_db_id, 0 )      AS auth_status_db_id,
               NVL( auth.auth_lvl_status_cd, 'PENDING' ) AS auth_status_cd              
            FROM
               po_header
               LEFT JOIN
                  (
                     -- get the last auth level
                     SELECT
                        * 
                     FROM
                        (
                           -- order auth levels with rn for each order
                           SELECT 
                              po_db_id,
                              po_id,
                              auth_lvl_status_db_id,
                              auth_lvl_status_cd,
                              ROW_NUMBER() OVER ( PARTITION BY po_db_id, po_id ORDER BY po_auth.auth_dt DESC ) AS rn
                           FROM
                              po_auth
                           WHERE
                              po_auth.reverted_bool = 0
                        )
                     WHERE
                        rn = 1
                  ) auth ON
                  po_header.po_db_id = auth.po_db_id AND
                  po_header.po_id    = auth.po_id
         ) auth_status 
      WHERE
         auth_status.po_db_id = po_header.po_db_id AND
         auth_status.po_id    = po_header.po_id
   )
WHERE
   auth_status_db_id IS NULL
   AND
   po_db_id != 0   
;

--changeSet OPER-2966:6 stripComments:false
ALTER TRIGGER TUBR_PO_HEADER ENABLE;

--changeSet OPER-2966:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Add the FK and NOT NULL constraint to the columns now that they are populated
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- add foreign key constraint from ref_po_auth_lvl_status to po_header FK_REFPOAUTHLVLSTATUS_HEADE1
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table PO_HEADER add Constraint FK_REFPOAUTHLVLSTATUS_POHEADE1
   foreign key (AUTH_STATUS_DB_ID, AUTH_STATUS_CD) 
   references REF_PO_AUTH_LVL_STATUS (PO_AUTH_LVL_STATUS_DB_ID,PO_AUTH_LVL_STATUS_CD)  
   DEFERRABLE
');
END;
/   

--changeSet OPER-2966:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   AUTH_STATUS_DB_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet OPER-2966:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   AUTH_STATUS_CD Varchar2 (10) NOT NULL DEFERRABLE
)
');
END;
/