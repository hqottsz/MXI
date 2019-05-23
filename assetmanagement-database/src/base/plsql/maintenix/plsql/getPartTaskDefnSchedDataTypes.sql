--liquibase formatted sql


--changeSet getPartTaskDefnSchedDataTypes:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     getPartTaskDefnSchedDataTypes
* Arguments:    aTaskDbId, aTaskId 	- pk for the task definition revision
*               aTable			- Placeholder table
* Description:  Retrieve the list of Usage Parameters common to all Parts assigned
*               to a Component Type task Definition
* Returns PK of common Usage Parameters, and PK of all Calendar Parameters
*
* Orig.Coder:    Amar
* Recent Coder:  Abhishek
*********************************************************************************/
CREATE OR REPLACE FUNCTION getPartTaskDefnSchedDataTypes
(
   aTaskDefnDbId   task_task.task_db_id%TYPE,
   aTaskDefnId     task_task.task_id%TYPE
) RETURN mxKeyTable
IS


 CURSOR lCur_SchedDataType IS
   -- this sections retrieves all the calendar type parameters
      SELECT
         mim_data_type.data_type_db_id ,
         mim_data_type.data_type_id
      FROM
         mim_data_type
      WHERE
         mim_data_type.domain_type_cd = 'CA'

      UNION

      -- this section retrieves usage parameters common to each assigned part, if parts of both type (TRK and SER) exists.
      SELECT
         usage_parms.data_type_db_id,
         usage_parms.data_type_id
      FROM
         (
            -- get the details of common usage parms for TRK parts only
            SELECT
         	mim_data_type.data_type_db_id ,
         	mim_data_type.data_type_id
            FROM
                mim_data_type
                WHERE
            mim_data_type.data_type_db_id || ':' || mim_data_type.data_type_id IN
            (
               -- get the common usage parms for TRK parts only
               SELECT
                         trk_parm.data_type_key
                   FROM
                   (
                   SELECT
                          trk_parms.data_type_key,
                          COUNT(trk_parms.data_type_key) as trk_parm_count
                   FROM
                   (
                        -- identify datatptypes in the context of the part; thus if a usage parameter is
                        -- assigned to more than one usage definition for a part, only one record will be returned
                        SELECT
                        DISTINCT
                          eqp_part_no.part_no_db_id ||':'||eqp_part_no.part_no_id AS part_no_key,
                          trk_data_type.data_type_db_id || ':' || trk_data_type.data_type_id AS data_type_key
                        FROM
                        task_part_map
                        INNER JOIN eqp_part_no ON
                           eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                           eqp_part_no.part_no_id    = task_part_map.part_no_id
                        INNER JOIN eqp_part_baseline ON
                           eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
                           eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
                        INNER JOIN eqp_bom_part ON
                           eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
                           eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
                        INNER JOIN mim_part_numdata ON
                           mim_part_numdata.assmbl_db_id  = eqp_bom_part.assmbl_db_id  AND
                           mim_part_numdata.assmbl_cd     = eqp_bom_part.assmbl_cd     AND
                           mim_part_numdata.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
                        INNER JOIN mim_data_type trk_data_type ON
                           trk_data_type.data_type_db_id = mim_part_numdata.data_type_db_id  AND
                           trk_data_type.data_type_id    = mim_part_numdata.data_type_id
                     WHERE
                        eqp_part_no.inv_class_cd = 'TRK'		AND
         		eqp_part_no.rstat_cd	 = 0
                        AND
                        trk_data_type.domain_type_cd = 'US'
                        AND
                        task_part_map.task_db_id = aTaskDefnDbId  AND
                        task_part_map.task_id    = aTaskDefnId
                        )  trk_parms
                        GROUP BY
                        trk_parms.data_type_key
                        )trk_parm
                        WHERE
                        trk_parm.trk_parm_count = (
                                                SELECT
                                                   COUNT(*)
                                                FROM
                                                   task_part_map JOIN eqp_part_no
                                                   ON task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
                                                   task_part_map.part_no_id = eqp_part_no.part_no_id

                                                WHERE
                                                eqp_part_no.inv_class_cd = 'TRK' AND
                                                   task_part_map.task_db_id = aTaskDefnDbId  AND
                                                   task_part_map.task_id    = aTaskDefnId
                                             )
            )
            INTERSECT

            -- get the details of common usage parms for SER parts only
            SELECT
         	mim_data_type.data_type_db_id ,
         	mim_data_type.data_type_id
            FROM
               mim_data_type
            WHERE
               mim_data_type.data_type_db_id || ':' || mim_data_type.data_type_id IN
               (
                  -- get the common usage parms for SER parts only
                  SELECT
                            ser_parm.data_type_key
                      FROM
                      (
                      SELECT
                             ser_parms.data_type_key,
                             COUNT(ser_parms.data_type_key) as ser_parm_count
                      FROM
                      (
                           -- identify datatypes in the context of the part; thus if a usage parameter is
                           -- assigned to more than one usage definition for a part, only one record will be returned
                           SELECT
                           DISTINCT
                             eqp_part_no.part_no_db_id ||':'||eqp_part_no.part_no_id AS part_no_key,
                             ser_data_type.data_type_db_id || ':' || ser_data_type.data_type_id AS data_type_key
                           FROM
                           task_part_map
                           INNER JOIN eqp_part_no ON
                              -- identify the parts linked with a task definition
                              eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                              eqp_part_no.part_no_id    = task_part_map.part_no_id
                           INNER JOIN eqp_part_baseline ON
                              -- identify the baseline entries for each part; this step needed to identify the assembly
                              eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
                              eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
                           INNER JOIN eqp_bom_part ON
                              -- identify the assembly
                              eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
                              eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
                           INNER JOIN eqp_data_source_spec ON
                              -- enumerate the data-types applicable to a particular assembly
                              eqp_data_source_spec.assmbl_db_id  = eqp_bom_part.assmbl_db_id  AND
                              eqp_data_source_spec.assmbl_cd     = eqp_bom_part.assmbl_cd
                           INNER JOIN mim_data_type ser_data_type ON
                              ser_data_type.data_type_db_id = eqp_data_source_spec.data_type_db_id  AND
                              ser_data_type.data_type_id    = eqp_data_source_spec.data_type_id
                        WHERE
                           eqp_part_no.inv_class_cd = 'SER'		AND
         		   eqp_part_no.rstat_cd	 = 0
                           AND
                           ser_data_type.domain_type_cd = 'US'
                           AND
                           task_part_map.task_db_id = aTaskDefnDbId  AND
                           task_part_map.task_id    = aTaskDefnId
                           )  ser_parms
                           GROUP BY
                           ser_parms.data_type_key
                           )ser_parm
                           WHERE
                           ser_parm.ser_parm_count = (
                                                   SELECT
                                                      COUNT(*)
                                                   FROM
                                                      task_part_map JOIN eqp_part_no
                                                      ON task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
                                                      task_part_map.part_no_id = eqp_part_no.part_no_id
                                                   WHERE
                                                      eqp_part_no.inv_class_cd = 'SER' AND
                                                      task_part_map.task_db_id = aTaskDefnDbId  AND
                                                      task_part_map.task_id    = aTaskDefnId
                                                )
               )
            ) usage_parms
         WHERE
         -- select usage parms only when no BATCH type part is assigned.
         NOT EXISTS
         (
            SELECT
               1
            FROM
               task_part_map
               INNER JOIN eqp_part_no ON
                  eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                  eqp_part_no.part_no_id    = task_part_map.part_no_id
            WHERE
               eqp_part_no.inv_class_cd = 'BATCH'		AND
               eqp_part_no.rstat_cd	= 0
               AND
               task_part_map.task_db_id = aTaskDefnDbId  AND
               task_part_map.task_id    = aTaskDefnId
         )

      UNION

      -- this section retrieves usage parameters common to each assigned part, if parts of type (TRK ONLY) exists.

      -- get the details of common usage parms for TRK parts only
      SELECT
         mim_data_type.data_type_db_id ,
         mim_data_type.data_type_id
      FROM
         mim_data_type
      WHERE
         mim_data_type.data_type_db_id || ':' || mim_data_type.data_type_id IN
         (
            -- get the common usage parms for TRK parts only
            SELECT
                      trk_parm.data_type_key
                FROM
                (
                SELECT
                       trk_parms.data_type_key,
                       COUNT(trk_parms.data_type_key) as trk_parm_count
                FROM
                (
                     -- identify datatptypes in the context of the part; thus if a usage parameter is
                     -- assigned to more than one usage definition for a part, only one record will be returned
                     SELECT
                     DISTINCT
                          eqp_part_no.part_no_db_id ||':'||eqp_part_no.part_no_id AS part_no_key,
                       trk_data_type.data_type_db_id || ':' || trk_data_type.data_type_id AS data_type_key
                     FROM
                     task_part_map
                     INNER JOIN eqp_part_no ON
                        eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                        eqp_part_no.part_no_id    = task_part_map.part_no_id
                     INNER JOIN eqp_part_baseline ON
                        eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
                        eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
                     INNER JOIN eqp_bom_part ON
                        eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
                        eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
                     INNER JOIN mim_part_numdata ON
                        mim_part_numdata.assmbl_db_id  = eqp_bom_part.assmbl_db_id  AND
                        mim_part_numdata.assmbl_cd     = eqp_bom_part.assmbl_cd     AND
                        mim_part_numdata.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
                     INNER JOIN mim_data_type trk_data_type ON
                        trk_data_type.data_type_db_id = mim_part_numdata.data_type_db_id  AND
                        trk_data_type.data_type_id    = mim_part_numdata.data_type_id
                  WHERE
                     eqp_part_no.inv_class_cd = 'TRK'		AND
         	     eqp_part_no.rstat_cd     = 0
                     AND
                     trk_data_type.domain_type_cd = 'US'
                     AND
                     task_part_map.task_db_id = aTaskDefnDbId  AND
                     task_part_map.task_id    = aTaskDefnId
                     )  trk_parms
                     GROUP BY
                     trk_parms.data_type_key
                     )trk_parm
                     WHERE
                     trk_parm.trk_parm_count = (
                                             SELECT
                                                COUNT(*)
                                             FROM
                                                task_part_map JOIN eqp_part_no
                                                ON task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
                                                task_part_map.part_no_id = eqp_part_no.part_no_id
                                                WHERE
                                                eqp_part_no.inv_class_cd = 'TRK'
                                                AND
                                                task_part_map.task_db_id = aTaskDefnDbId  AND
                                                task_part_map.task_id    = aTaskDefnId
                                          )
         )
         AND
         -- select usage parms only when no BATCH and SER type part is assigned.
         NOT EXISTS
         (
            SELECT
               1
            FROM
               task_part_map
               INNER JOIN eqp_part_no ON
                  eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                  eqp_part_no.part_no_id    = task_part_map.part_no_id
            WHERE
               eqp_part_no.inv_class_cd IN ('BATCH', 'SER')		AND
               eqp_part_no.rstat_cd	 = 0
               AND
               task_part_map.task_db_id = aTaskDefnDbId  AND
               task_part_map.task_id    = aTaskDefnId
         )


      UNION

      -- this section retrieves usage parameters common to each assigned part, if parts of type (SER ONLY) exists.

      -- get the details of common usage parms for SER parts only
      SELECT
         mim_data_type.data_type_db_id ,
         mim_data_type.data_type_id
      FROM
         mim_data_type
      WHERE
         mim_data_type.data_type_db_id || ':' || mim_data_type.data_type_id IN
         (
            -- get the common usage parms for SER parts only
            SELECT
                      ser_parm.data_type_key
                FROM
                (
                SELECT
                       ser_parms.data_type_key,
                       COUNT(ser_parms.data_type_key) as ser_parm_count
                FROM
                (
                     -- identify datatypes in the context of the part; thus if a usage parameter is
                     -- assigned to more than one usage definition for a part, only one record will be returned
                     SELECT
                     DISTINCT
                       eqp_part_no.part_no_db_id ||':'||eqp_part_no.part_no_id AS part_no_key,
                       ser_data_type.data_type_db_id || ':' || ser_data_type.data_type_id AS data_type_key
                     FROM
                     task_part_map
                     INNER JOIN eqp_part_no ON
                        -- identify the parts linked with a task definition
                        eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                        eqp_part_no.part_no_id    = task_part_map.part_no_id
                     INNER JOIN eqp_part_baseline ON
                        -- identify the baseline entries for each part; this step needed to identify the assembly
                        eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
                        eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
                     INNER JOIN eqp_bom_part ON
                        -- identify the assembly
                        eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
                        eqp_bom_part.bom_part_id    = eqp_part_baseline.bom_part_id
                     INNER JOIN eqp_data_source_spec ON
                        -- enumerate the data-types applicable to a particular assembly
                        eqp_data_source_spec.assmbl_db_id  = eqp_bom_part.assmbl_db_id  AND
                        eqp_data_source_spec.assmbl_cd     = eqp_bom_part.assmbl_cd
                     INNER JOIN mim_data_type ser_data_type ON
                        ser_data_type.data_type_db_id = eqp_data_source_spec.data_type_db_id  AND
                        ser_data_type.data_type_id    = eqp_data_source_spec.data_type_id
                  WHERE
                     eqp_part_no.inv_class_cd = 'SER'		AND
         	     eqp_part_no.rstat_cd     = 0
                     AND
                     ser_data_type.domain_type_cd = 'US'
                     AND
                     task_part_map.task_db_id = aTaskDefnDbId  AND
                     task_part_map.task_id    = aTaskDefnId
                     )  ser_parms
                     GROUP BY
                     ser_parms.data_type_key
                     )ser_parm
                     WHERE
                     ser_parm.ser_parm_count = (
                                             SELECT
                                                COUNT(*)
                                             FROM
                                                task_part_map JOIN eqp_part_no
                                                ON task_part_map.part_no_db_id = eqp_part_no.part_no_db_id AND
                                                task_part_map.part_no_id = eqp_part_no.part_no_id
                                                WHERE
                                                eqp_part_no.inv_class_cd = 'SER' AND
                                                task_part_map.task_db_id = aTaskDefnDbId  AND
                                                task_part_map.task_id    = aTaskDefnId
                                          )
         )
         AND
         -- select usage parms only when no BATCH and TRK type part is assigned.
         NOT EXISTS
         (
            SELECT
               1
            FROM
               task_part_map
               INNER JOIN eqp_part_no ON
                  eqp_part_no.part_no_db_id = task_part_map.part_no_db_id AND
                  eqp_part_no.part_no_id    = task_part_map.part_no_id
            WHERE
               eqp_part_no.inv_class_cd IN ('BATCH', 'TRK')		AND
               eqp_part_no.rstat_cd	 = 0
               AND
               task_part_map.task_db_id = aTaskDefnDbId  AND
               task_part_map.task_id    = aTaskDefnId
         );

    lRecSchedDataType lCur_SchedDataType%ROWTYPE;
    lTable mxKeyTable;
    lIndex NUMBER;

 BEGIN

    -- initialize local table variable
        lTable := mxKeyTable(mxKey(-1,-1));

     	FOR lRecSchedDataType IN lCur_SchedDataType LOOP
     	 	lTable.EXTEND;
     	 	lIndex := lTable.LAST;
     	 	lTable(lIndex) := mxKey(lRecSchedDataType.data_type_db_id, lRecSchedDataType.data_type_id);
     	END LOOP;


    RETURN lTable;

END getPartTaskDefnSchedDataTypes;
/