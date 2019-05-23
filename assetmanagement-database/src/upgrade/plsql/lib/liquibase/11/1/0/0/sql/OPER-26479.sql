--liquibase formatted sql

--changeSet OPER-26479:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment removing possible duplicate rows in the enhanced part search table
BEGIN
    DELETE FROM mt_enh_part_search
    WHERE ROWID
    IN(
        SELECT
            rid
        FROM (SELECT
                ROWID
                 rid,
                 row_number() OVER (
                    PARTITION BY
                        eqp_part_no_part_no_db_id,
                        eqp_part_no_part_no_id,
                        eqp_bom_part_bom_part_db_id ,
                        eqp_bom_part_bom_part_id,
                        eqp_assmbl_pos_assmbl_db_id,
                        eqp_assmbl_pos_assmbl_cd,
                        eqp_assmbl_pos_assmbl_bom_id,
                        eqp_assmbl_pos_assmbl_pos_id,
                        subassy_pos_assmbl_pos_id,
                        subassy_pos_pos
                    ORDER BY ROWID) rn
               FROM mt_enh_part_search)
           WHERE rn <> 1
        );
    COMMIT;
END;
/
--changeSet OPER-26479:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment adding new constraint to mt_enh_part_search table to prevent duplicates
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
        ALTER TABLE MT_ENH_PART_SEARCH
        ADD CONSTRAINT IX_ENH_PART_SEARCH_UNQ
        UNIQUE
        (
            EQP_PART_NO_PART_NO_DB_ID ,
            EQP_PART_NO_PART_NO_ID ,
            EQP_BOM_PART_BOM_PART_DB_ID ,
            EQP_BOM_PART_BOM_PART_ID ,
            EQP_ASSMBL_POS_ASSMBL_DB_ID ,
            EQP_ASSMBL_POS_ASSMBL_CD ,
            EQP_ASSMBL_POS_ASSMBL_BOM_ID ,
            EQP_ASSMBL_POS_ASSMBL_POS_ID ,
            SUBASSY_POS_ASSMBL_POS_ID ,
            SUBASSY_POS_POS
        )
    ');
END;
/
