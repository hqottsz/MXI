--liquibase formatted sql


--changeSet MX-20971:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- This script reorders primary keys in the EQP_ASSMBL_POS table.
-- Maintenix requires a BOM's assmbl_pos_ids to be ordered 1..n, with no gaps in the sequence.
--
-- As an example, consider a config slot with positions 2, 4, 6
-- This script will query for the slot's positions in ascending order, then update primary+foreign keys for each position, i.e:
--    2 becomes 1,
--    4 becomes 2,
--    6 becomes 3
-- First, disable all foreign key constraints for the EQP_ASSMBL_POS primary key
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'EQP_ASSMBL_POS', 'FK_EQPASSMBLPOS_EQPASSMBLPOS' );
END;
/

--changeSet MX-20971:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'EVT_INV', 'FK_EQPASSMBLPOS_EVTINV' );
END;
/

--changeSet MX-20971:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'INV_INV', 'FK_EQPASSMBLPOS_INVINV' );
END;
/

--changeSet MX-20971:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'SCHED_PART', 'FK_EQPASSMBLPOS_SCHEDPART' );
END;
/

--changeSet MX-20971:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'SCHED_PART', 'FK_NH_EQPASSMBLPOS_SCHEDPART' );
END;
/

--changeSet MX-20971:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'TASK_PART_LIST', 'FK_EQPASSPOS_TASKPRTLIST' );
END;
/

--changeSet MX-20971:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_drop( 'USG_USAGE_DATA', 'FK_EQPASSMBLPOS_USGUSAGEDATA' );
END;
/

--changeSet MX-20971:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Constraints are removed.  It is now safe to reorder primary keys.
DECLARE

   /**
    * Gets all BOMs with out of sequence positions.  Important points here are:
    *    1. A correctly ordered BOM will have positions 1..n
    *    2. The sum of 1..n positions is ( n  * (n + 1) / 2 )
    *       - (see http://en.wikipedia.org/wiki/Triangular_number)
    *    3. An incorrectly orderd BOM's positions will not sum to 1..n
    *       - e.g. "1, 3, 4, 9" sums to 17 instead of 10
    *
    * The following query would be shorter if eqp_assmbl_bom.pos_ct contained a reliable value.
    * However, the dev DB (and at least one customer DB) contain slots with extra positions.
    * A query to illustrate this follows:
    *    SELECT
    *       bom.assmbl_db_id, bom.assmbl_cd, bom.assmbl_bom_id, pos_ct, COUNT( assmbl_pos_id )
    *    FROM
    *       eqp_assmbl_bom bom
    *       JOIN   eqp_assmbl_pos ON eqp_assmbl_pos.assmbl_db_id  = bom.assmbl_db_id  AND
    *                                eqp_assmbl_pos.assmbl_cd     = bom.assmbl_cd     AND
    *                                eqp_assmbl_pos.assmbl_bom_id = bom.assmbl_bom_id
    *    GROUP BY
    *       bom.assmbl_db_id, bom.assmbl_cd, bom.assmbl_bom_id, pos_ct
    *    HAVING
    *       COUNT( assmbl_pos_id ) != pos_ct
    */
    CURSOR lcur_MisorderedBOMS IS
      SELECT
         misordered_boms.*
      FROM
         (
            SELECT
               eqp_assmbl_pos.assmbl_db_id,
               eqp_assmbl_pos.assmbl_cd,
               eqp_assmbl_pos.assmbl_bom_id,
               COUNT( assmbl_pos_id ) AS position_count,
               SUM( assmbl_pos_id ) AS actual_sum,
               ( COUNT( assmbl_pos_id ) * ( COUNT( assmbl_pos_id ) + 1 ) / 2 ) AS expected_sum
            FROM
               eqp_assmbl_pos
            GROUP BY
               assmbl_db_id,
               assmbl_cd,
               assmbl_bom_id
         ) misordered_boms
      WHERE
         -- Only select BOMs with out-of-sequence positions
         actual_sum != expected_sum
         -- Only select BOMs in which *every* row with a reference its positions has rstat_cd = 0
         AND NOT EXISTS (
            SELECT
               *
            FROM
               eqp_assmbl_pos
            WHERE
               assmbl_db_id   = misordered_boms.assmbl_db_id   AND
               assmbl_cd      = misordered_boms.assmbl_cd      AND
               assmbl_bom_id  = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
         AND NOT EXISTS (
            SELECT
               *
            FROM
               eqp_assmbl_pos
            WHERE
               eqp_assmbl_pos.nh_assmbl_db_id  = misordered_boms.assmbl_db_id   AND
               eqp_assmbl_pos.nh_assmbl_cd     = misordered_boms.assmbl_cd      AND
               eqp_assmbl_pos.nh_assmbl_bom_id = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
         AND NOT EXISTS (
            SELECT
               *
            FROM
               evt_inv
            WHERE
               evt_inv.assmbl_db_id  = misordered_boms.assmbl_db_id   AND
               evt_inv.assmbl_cd     = misordered_boms.assmbl_cd      AND
               evt_inv.assmbl_bom_id = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
         AND NOT EXISTS (
            SELECT
               *
            FROM
               inv_inv
            WHERE
               inv_inv.assmbl_db_id  = misordered_boms.assmbl_db_id   AND
               inv_inv.assmbl_cd     = misordered_boms.assmbl_cd      AND
               inv_inv.assmbl_bom_id = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
         AND NOT EXISTS (
            SELECT
               *
            FROM
               sched_part
            WHERE
               sched_part.assmbl_db_id   = misordered_boms.assmbl_db_id   AND
               sched_part.assmbl_cd      = misordered_boms.assmbl_cd      AND
               sched_part.assmbl_bom_id  = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
         AND NOT EXISTS (
            SELECT
               *
            FROM
               sched_part
            WHERE
               nh_assmbl_db_id   = misordered_boms.assmbl_db_id   AND
               nh_assmbl_cd      = misordered_boms.assmbl_cd      AND
               nh_assmbl_bom_id  = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
         AND NOT EXISTS (
            SELECT
               *
            FROM
               task_part_list
            WHERE
               assmbl_db_id   = misordered_boms.assmbl_db_id   AND
               assmbl_cd      = misordered_boms.assmbl_cd      AND
               assmbl_bom_id  = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
		 AND NOT EXISTS (
            SELECT
               *
            FROM
               usg_usage_data
            WHERE
               assmbl_db_id   = misordered_boms.assmbl_db_id   AND
               assmbl_cd      = misordered_boms.assmbl_cd      AND
               assmbl_bom_id  = misordered_boms.assmbl_bom_id
               AND
               rstat_cd != 0
         )
      ;

   -- Fetches all positions for a BOM.  Ordered by ascending position id.
   CURSOR lcur_BOMPositions (
         cl_AssmblDbId   eqp_assmbl_pos.assmbl_db_id  %TYPE,
         cs_AssmblCd     eqp_assmbl_pos.assmbl_cd     %TYPE,
         cl_AssmblBomId  eqp_assmbl_pos.assmbl_bom_id %TYPE
      )
      IS
         SELECT
            eqp_assmbl_pos.assmbl_pos_id
         FROM
            eqp_assmbl_pos
         WHERE
            eqp_assmbl_pos.assmbl_db_id   = cl_AssmblDbId AND
            eqp_assmbl_pos.assmbl_cd      = cs_AssmblCd   AND
            eqp_assmbl_pos.assmbl_bom_id  = cl_AssmblBomId
         ORDER BY
            assmbl_pos_id ASC;

   lrec_MisorderedBOM lcur_MisorderedBOMS %ROWTYPE;
   lrec_BOMPosition   lcur_BOMPositions   %ROWTYPE;

   ln_PositionCount INTEGER;

BEGIN

   -- For each BOM with out-of-sequence positions:
   --    - Get all positions in order
   --    - Update all references to the lowest position to have assmbl_pos_id = 1
   --    - Continue on to the next position and update to assmbl_pos_id = 2
   --    - Repeat until all positions are in order
   FOR lrec_MisorderedBOM IN lcur_MisorderedBOMS LOOP

      dbms_output.put_line(
         'INFO: Updating sequence of bom positions for ' ||
            lrec_MisorderedBOM.assmbl_db_id || ':' ||
            lrec_MisorderedBOM.assmbl_cd    || ':' ||
            lrec_MisorderedBOM.assmbl_bom_id
         );

      -- Positions are indexed from 1..n.
      -- ln_PositionCount holds the next in-sequence position id.
      ln_PositionCount := 1;

      FOR lrec_BOMPosition IN lcur_BOMPositions( lrec_MisorderedBOM.Assmbl_Db_Id, lrec_MisorderedBOM.Assmbl_Cd, lrec_MisorderedBOM.Assmbl_Bom_Id ) LOOP

         dbms_output.put_line(
            'INFO:   Updating current position ' || lrec_BOMPosition.assmbl_pos_id || ' to ' || ln_PositionCount
            );

         -- Update all references to the key to have ln_PositionCount
         UPDATE eqp_assmbl_pos
         SET    assmbl_pos_id = ln_PositionCount
         WHERE  assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


         UPDATE eqp_assmbl_pos
         SET    nh_assmbl_pos_id = ln_PositionCount
         WHERE  nh_assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                nh_assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                nh_assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                nh_assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


         UPDATE evt_inv
         SET    assmbl_pos_id = ln_PositionCount
         WHERE  assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


         UPDATE inv_inv
         SET    assmbl_pos_id = ln_PositionCount
         WHERE  assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


         UPDATE sched_part
         SET    assmbl_pos_id = ln_PositionCount
         WHERE  assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


         UPDATE sched_part
         SET    nh_assmbl_pos_id = ln_PositionCount
         WHERE  nh_assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                nh_assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                nh_assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                nh_assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


         UPDATE task_part_list
         SET    assmbl_pos_id = ln_PositionCount
         WHERE  assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;


		 UPDATE usg_usage_data
         SET    assmbl_pos_id = ln_PositionCount
         WHERE  assmbl_db_id  = lrec_MisorderedBOM.assmbl_db_id AND
                assmbl_cd     = lrec_MisorderedBOM.assmbl_cd    AND
                assmbl_bom_id = lrec_MisorderedBOM.assmbl_bom_id AND
                assmbl_pos_id = lrec_BOMPosition.assmbl_pos_id;

         -- Increment to the next actual position value
         ln_PositionCount := ln_PositionCount + 1;
      END LOOP; -- process the next position in this BOM
   END LOOP; -- process the next BOM

END;
/

--changeSet MX-20971:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Primary keys have been reordered.  Enable foreign key constraints.
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
      Alter table "EQP_ASSMBL_POS" add Constraint "FK_EQPASSMBLPOS_EQPASSMBLPOS" foreign key ("NH_ASSMBL_DB_ID","NH_ASSMBL_CD","NH_ASSMBL_BOM_ID","NH_ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-20971:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
       Alter table "EVT_INV" add Constraint "FK_EQPASSMBLPOS_EVTINV" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-20971:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
       Alter table "INV_INV" add Constraint "FK_EQPASSMBLPOS_INVINV" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-20971:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
       Alter table "SCHED_PART" add Constraint "FK_EQPASSMBLPOS_SCHEDPART" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-20971:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
       Alter table "SCHED_PART" add Constraint "FK_NH_EQPASSMBLPOS_SCHEDPART" foreign key ("NH_ASSMBL_DB_ID","NH_ASSMBL_CD","NH_ASSMBL_BOM_ID","NH_ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-20971:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
       Alter table "TASK_PART_LIST" add Constraint "FK_EQPASSPOS_TASKPRTLIST" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/

--changeSet MX-20971:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_constraint_add('
       Alter table "USG_USAGE_DATA" add Constraint "FK_EQPASSMBLPOS_USGUSAGEDATA" foreign key ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID") references "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")  DEFERRABLE
    ');
END;
/