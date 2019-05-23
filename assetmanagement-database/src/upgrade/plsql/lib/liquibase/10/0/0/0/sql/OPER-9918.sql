--liquibase formatted sql

--changeSet OPER-9918:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
-- Prepare migration
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TMP_INV_HIST');
END;
/

--changeSet OPER-9918:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('tmp_get_hitoric_config');
END;
/

--changeSet OPER-9918:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('tmp_config_rows');
END;
/

--changeSet OPER-9918:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('tmp_config_rec');
END;
/

--changeSet OPER-9918:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   utl_migr_schema_pkg.table_create('
      CREATE TABLE tmp_inv_hist
        (
          INV_NO_DB_ID         NUMBER (10) NOT NULL,
          INV_NO_ID            NUMBER (10) NOT NULL,
          NH_INV_NO_DB_ID      NUMBER (10) ,
          NH_INV_NO_ID         NUMBER (10)
        )
   ');
END;
/

--changeset OPER-9918:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE tmp_inv_hist ADD CONSTRAINT PK_tmp_inv_hist PRIMARY KEY ( INV_NO_DB_ID, INV_NO_ID )
   ');
END;
/  

--changeset OPER-9918:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE tmp_inv_hist ADD CONSTRAINT FK_TMP_INVHIST_INVINVNH FOREIGN KEY ( NH_INV_NO_DB_ID, NH_INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID )
   ');
END;
/  

--changeset OPER-9918:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_TMP_INVHIST_INVINVNH ON tmp_inv_hist
       (
         NH_INV_NO_DB_ID ASC ,
         NH_INV_NO_ID ASC
       )
   ');
END;
/

--changeset OPER-9918:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   INSERT INTO tmp_inv_hist
   select 
    i.inv_no_db_id, i.inv_no_id,
    i.nh_inv_no_db_id, i.nh_inv_no_id
   from inv_inv i;

   COMMIT;
END;
/

--changeset OPER-9918:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Prepare function
CREATE OR REPLACE TYPE tmp_config_rec
IS OBJECT (
   inv_no_db_id NUMBER(10),        
   inv_no_id NUMBER(10),
   nh_inv_no_db_id NUMBER(10),     
   nh_inv_no_id NUMBER(10),
   assmbl_inv_no_db_id NUMBER(10), 
   assmbl_inv_no_id NUMBER(10)
);
/

--changeset OPER-9918:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE tmp_config_rows
AS TABLE OF tmp_config_rec;
/

--changeset OPER-9918:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION tmp_get_hitoric_config
   (
      aInvNoDbId IN NUMBER,
      aInvNoId IN NUMBER
   )
   RETURN tmp_config_rows 
   PIPELINED
IS            
   PRAGMA AUTONOMOUS_TRANSACTION;
   lv_assmbl_inv_no_db_id NUMBER;
   lv_assmbl_inv_no_id NUMBER;
BEGIN
   SELECT
      CASE WHEN (inv_inv.inv_class_db_id = 0 AND inv_inv.inv_class_cd = 'ASSY') THEN inv_inv.inv_no_db_id ELSE null END, 
      CASE WHEN (inv_inv.inv_class_db_id = 0 AND inv_inv.inv_class_cd = 'ASSY') THEN inv_inv.inv_no_id ELSE null END
   INTO
      lv_assmbl_inv_no_db_id, lv_assmbl_inv_no_id
   FROM inv_inv
   WHERE
      inv_inv.inv_no_db_id = aInvNoDbId AND
      inv_inv.inv_no_id    = aInvNoId;

   FOR rec IN (
            SELECT
               i.inv_no_db_id,        i.inv_no_id,
               i.nh_inv_no_db_id,     i.nh_inv_no_id
            FROM tmp_inv_hist i
            START WITH
               i.inv_no_db_id = aInvNoDbId AND
               i.inv_no_id    = aInvNoId
            CONNECT BY
               PRIOR i.inv_no_db_id = i.nh_inv_no_db_id AND
               PRIOR i.inv_no_id    = i.nh_inv_no_id
   )
   LOOP
      PIPE ROW (
         tmp_config_rec(
            rec.inv_no_db_id,
            rec.inv_no_id,
            rec.nh_inv_no_db_id,
            rec.nh_inv_no_id,
            lv_assmbl_inv_no_db_id,
            lv_assmbl_inv_no_id
         )
      );
   END LOOP;                   
END;            
/

--changeset OPER-9918:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migration records
DECLARE
   CURSOR lcur_main_inv IS 
   SELECT 
     evt_event.event_db_id,
     evt_event.event_id,
     evt_inv.inv_no_db_id,
     evt_inv.inv_no_id,
     evt_inv.nh_inv_no_db_id,
     evt_inv.nh_inv_no_id,
     evt_inv.assmbl_inv_no_db_id,
     evt_inv.assmbl_inv_no_id,
     evt_inv.h_inv_no_db_id,
     evt_inv.h_inv_no_id,
     evt_event.event_status_cd,
     evt_event.event_dt
   FROM 
      evt_event   
   INNER JOIN evt_inv ON 
      evt_event.event_id = evt_inv.event_id AND
      evt_event.event_db_id = evt_inv.event_db_id
   WHERE 
      evt_inv.main_inv_bool = 1 
      AND
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'FG'
   ORDER BY evt_event.event_dt DESC, evt_event.event_id DESC;
BEGIN
   FOR i IN lcur_main_inv LOOP
      IF i.event_status_cd = 'FGINST' THEN 
         -- insert main inv record
         INSERT INTO inv_install
         (
            event_db_id, 
            event_id,
            inv_no_db_id, 
            inv_no_id,
            nh_inv_no_db_id, 
            nh_inv_no_id,
            assmbl_inv_no_db_id, 
            assmbl_inv_no_id,
            h_inv_no_db_id, 
            h_inv_no_id,
            event_dt,
            main_inv_bool,
            rstat_cd
         )
         SELECT 
           i.event_db_id,
           i.event_id,
           i.inv_no_db_id,
           i.inv_no_id,
           i.nh_inv_no_db_id,
           i.nh_inv_no_id,
           i.assmbl_inv_no_db_id,
           i.assmbl_inv_no_id,
           i.h_inv_no_db_id,
           i.h_inv_no_id,
           i.event_dt,
           1 as main_inv_bool,
           0 as rstat_cd
        FROM
           dual
        LEFT JOIN inv_install
           ON
              inv_install.event_db_id = i.event_db_id AND
              inv_install.event_id = i.event_id
              AND
              inv_install.inv_no_db_id = i.inv_no_db_id AND
              inv_install.inv_no_id = i.inv_no_id
        WHERE
           inv_install.inv_install_id is null;

         -- insert subcomponents
         INSERT INTO inv_install
         (
            event_db_id, 
            event_id,
            inv_no_db_id, 
            inv_no_id,
            nh_inv_no_db_id, 
            nh_inv_no_id,
            assmbl_inv_no_db_id, 
            assmbl_inv_no_id,
            h_inv_no_db_id, 
            h_inv_no_id,
            event_dt,
            main_inv_bool,
            rstat_cd
         )
         SELECT 
           i.event_db_id,
           i.event_id,
           component.inv_no_db_id,
           component.inv_no_id,
           component.nh_inv_no_db_id,
           component.nh_inv_no_id,
           NVL(component.assmbl_inv_no_db_id, i.assmbl_inv_no_db_id),
           NVL(component.assmbl_inv_no_id, i.assmbl_inv_no_id),
           i.h_inv_no_db_id,
           i.h_inv_no_id,
           i.event_dt, 
           0 as main_inv_bool,
           0 as rstat_cd 
        FROM
           table(tmp_get_hitoric_config(i.inv_no_db_id, i.inv_no_id)) component
        LEFT JOIN inv_install
           ON
              inv_install.event_db_id = i.event_db_id AND
              inv_install.event_id = i.event_id
              AND
              inv_install.inv_no_db_id = component.inv_no_db_id AND
              inv_install.inv_no_id = component.inv_no_id
        WHERE
           inv_install.inv_install_id is null
           AND
           (
              component.inv_no_db_id != i.inv_no_db_id OR
              component.inv_no_id    != i.inv_no_id
           );

        -- update tmp_inv_hist
        UPDATE tmp_inv_hist
        SET
           tmp_inv_hist.nh_inv_no_db_id = null,
           tmp_inv_hist.nh_inv_no_id    = null
        WHERE
           tmp_inv_hist.inv_no_db_id = i.inv_no_db_id AND
           tmp_inv_hist.inv_no_id    = i.inv_no_id;
        
      END IF;
         
      IF i.event_status_cd = 'FGRMVL' THEN 
         -- insert main inv record
         INSERT INTO inv_remove
         (
            event_db_id, 
            event_id,
            inv_no_db_id, 
            inv_no_id,
            nh_inv_no_db_id, 
            nh_inv_no_id,
            assmbl_inv_no_db_id, 
            assmbl_inv_no_id,
            h_inv_no_db_id, 
            h_inv_no_id,
            event_dt,
            main_inv_bool,
            rstat_cd
         )
         SELECT 
           i.event_db_id,
           i.event_id,
           i.inv_no_db_id,
           i.inv_no_id,
           i.nh_inv_no_db_id,
           i.nh_inv_no_id,
           i.assmbl_inv_no_db_id,
           i.assmbl_inv_no_id,
           i.h_inv_no_db_id,
           i.h_inv_no_id,
           i.event_dt,
           1 as main_inv_bool,
           0 as rstat_cd
        FROM
           dual
        LEFT JOIN inv_remove
           ON
              inv_remove.event_db_id = i.event_db_id AND
              inv_remove.event_id = i.event_id
              AND
              inv_remove.inv_no_db_id = i.inv_no_db_id AND
              inv_remove.inv_no_id = i.inv_no_id
        WHERE
           inv_remove.inv_remove_id is null;

         -- insert subcomponents
         INSERT INTO inv_remove
         (
            event_db_id, 
            event_id,
            inv_no_db_id, 
            inv_no_id,
            nh_inv_no_db_id, 
            nh_inv_no_id,
            assmbl_inv_no_db_id, 
            assmbl_inv_no_id,
            h_inv_no_db_id, 
            h_inv_no_id,
            event_dt,
            main_inv_bool,
            rstat_cd
         )
         SELECT 
           i.event_db_id,
           i.event_id,
           component.inv_no_db_id,
           component.inv_no_id,
           component.nh_inv_no_db_id,
           component.nh_inv_no_id,
           NVL(component.assmbl_inv_no_db_id, i.assmbl_inv_no_db_id),
           NVL(component.assmbl_inv_no_id, i.assmbl_inv_no_id),
           i.h_inv_no_db_id,
           i.h_inv_no_id,
           i.event_dt, 
           0 as main_inv_bool,
           0 as rstat_cd 
        FROM 
           table(tmp_get_hitoric_config(i.inv_no_db_id, i.inv_no_id)) component
        LEFT JOIN inv_remove
           ON
              inv_remove.event_db_id = i.event_db_id AND
              inv_remove.event_id = i.event_id
              AND
              inv_remove.inv_no_db_id = component.inv_no_db_id AND
              inv_remove.inv_no_id = component.inv_no_id
        WHERE
           inv_remove.inv_remove_id is null
           AND
           (
              component.inv_no_db_id != i.inv_no_db_id OR
              component.inv_no_id    != i.inv_no_id
           );

        -- update tmp_inv_hist
        UPDATE tmp_inv_hist
        SET
           tmp_inv_hist.nh_inv_no_db_id = i.nh_inv_no_db_id,
           tmp_inv_hist.nh_inv_no_id    = i.nh_inv_no_id
        WHERE
           tmp_inv_hist.inv_no_db_id = i.inv_no_db_id AND
           tmp_inv_hist.inv_no_id    = i.inv_no_id;

      END IF;

      COMMIT;
   END LOOP; 
END;
/

--changeset OPER-9918:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Clean up
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('TMP_INV_HIST');
END;
/

--changeset OPER-9918:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('tmp_get_hitoric_config');
END;
/

--changeset OPER-9918:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('tmp_config_rows');
END;
/

--changeset OPER-9918:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('tmp_config_rec');
END;
/
