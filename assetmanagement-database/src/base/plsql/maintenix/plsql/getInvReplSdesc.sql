--liquibase formatted sql

--changeSet getInvReplSdesc:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment function for mt_core_fleet_list virtual column or any other caller
/********************************************************************************
*
* Function:    get_inv_repl_sdesc
*
* Arguments:   inv_no_db_id,inv_no_id of the inventory task is on.
*
* Return: VARCHAR2 That contains the configuration slot and position description
* 		  of the inventory the task is on. NULL is an acceptable and expected return value.
*
* 		  Example Return Values:
* 				71-00-00-00 (LH) ->05-20
* 				71-00-00-00 (RH) ->05-20
* 				57-00
* 				34-53-02-06 (1)
* 				34-53-02-06 (2)
* 				NULL
*
* Description: Provides the return data for the virtual column CONFIG_POS_SDESC.
*			   Used to provide accurate real-time data of tasks location on aircraft
********************************************************************************/
CREATE OR REPLACE FUNCTION get_inv_repl_sdesc
(
    iInv_no_db_id  IN NUMBER,
    iInv_no_id     IN NUMBER,
    iSched_db_id   IN NUMBER,
    iSched_id      IN NUMBER,
    iRepl_sched_id IN NUMBER
) RETURN VARCHAR2 DETERMINISTIC
IS
	lv2_sdesc   VARCHAR2 (4000);

	BEGIN
		--Start with most likely case
		IF(iRepl_sched_id IS NULL)
        THEN
        SELECT
         inv_desc_pkg.BuildInvConfigPosDesc
            (
            -- Inventory information
            av_InvClassCd    => inv_inv.inv_class_cd,
            an_InvNoDbId     => inv_inv.inv_no_db_id,
            an_InvNoId       => inv_inv.inv_no_id,
            an_NhInvNoDbId   => inv_inv.nh_inv_no_db_id,
            an_AssyInvNoDbId => inv_inv.assmbl_inv_no_db_id,
            an_AssyInvNoId   => inv_inv.assmbl_inv_no_id,
            an_HInvNoDbId    => inv_inv.h_inv_no_db_id,
            an_HInvNoId      => inv_inv.h_inv_no_id,
            -- Inventory slot and position info
            lv_InvBomCd      => eqp_assmbl_bom.assmbl_bom_cd,
            lv_InvPosCd      => eqp_assmbl_pos.eqp_pos_cd,
            ln_InvPosCt      => eqp_assmbl_bom.pos_ct,
            -- Assembly slot and position info
            lv_AssyBomCd     => assy_eqp_assmbl_bom.assmbl_bom_cd,
            lv_AssyPoscd     => assy_eqp_assmbl_pos.eqp_pos_cd,
            ln_AssyPosCt     => assy_eqp_assmbl_bom.pos_ct,
            -- Next highest position info
            ln_NhPosCt       => nh_assmbl_bom.pos_ct
            )
         AS config_desc INTO lv2_sdesc
        FROM
         inv_inv
         INNER JOIN eqp_assmbl_bom ON
            eqp_assmbl_bom.assmbl_db_id   = inv_inv.assmbl_db_id AND
            eqp_assmbl_bom.assmbl_cd      = inv_inv.assmbl_cd AND
            eqp_assmbl_bom.assmbl_bom_id  = inv_inv.assmbl_bom_id
         INNER JOIN eqp_assmbl_pos ON
            eqp_assmbl_pos.assmbl_db_id   = inv_inv.assmbl_db_id AND
            eqp_assmbl_pos.assmbl_cd      = inv_inv.assmbl_cd AND
            eqp_assmbl_pos.assmbl_bom_id  = inv_inv.assmbl_bom_id AND
            eqp_assmbl_pos.assmbl_pos_id  = inv_inv.assmbl_pos_id
         LEFT OUTER JOIN eqp_assmbl_bom nh_assmbl_bom ON
            nh_assmbl_bom.assmbl_db_id   = eqp_assmbl_bom.nh_assmbl_db_id AND
            nh_assmbl_bom.assmbl_cd      = eqp_assmbl_bom.nh_assmbl_cd AND
            nh_assmbl_bom.assmbl_bom_id  = eqp_assmbl_bom.nh_assmbl_bom_id
         LEFT OUTER JOIN inv_inv assy_inv_inv ON
            assy_inv_inv.inv_no_db_id    = inv_inv.assmbl_inv_no_db_id AND
            assy_inv_inv.inv_no_id       = inv_inv.assmbl_inv_no_id
         LEFT OUTER JOIN eqp_assmbl_bom assy_eqp_assmbl_bom ON
            assy_eqp_assmbl_bom.assmbl_db_id    = assy_inv_inv.assmbl_db_id AND
            assy_eqp_assmbl_bom.assmbl_cd       = assy_inv_inv.assmbl_cd AND
            assy_eqp_assmbl_bom.assmbl_bom_id   = assy_inv_inv.assmbl_bom_id
         LEFT OUTER JOIN eqp_assmbl_pos assy_eqp_assmbl_pos ON
            assy_eqp_assmbl_pos.assmbl_db_id    = assy_inv_inv.assmbl_db_id AND
            assy_eqp_assmbl_pos.assmbl_cd       = assy_inv_inv.assmbl_cd AND
            assy_eqp_assmbl_pos.assmbl_bom_id   = assy_inv_inv.assmbl_bom_id AND
            assy_eqp_assmbl_pos.assmbl_pos_id   = assy_inv_inv.assmbl_pos_id
        WHERE
            inv_inv.inv_no_db_id = iInv_no_db_id AND
            inv_inv.inv_no_id    = iInv_no_id
         ;
         RETURN (lv2_sdesc);
        END IF;

        -- We have a REPL Task and display is different
        IF(iRepl_sched_id IS NOT NULL)
        THEN
        SELECT
            CASE
                 WHEN nh_slot.bom_class_cd != 'SUBASSY' THEN NULL
                 ELSE nh_slot.assmbl_bom_cd
                    ||
                        CASE
                            WHEN ( nh_slot.pos_ct > 1 )
                                 OR ( nh_pos.eqp_pos_cd != '1' ) THEN ' ('
                            || nh_pos.eqp_pos_cd
                            || ') '
                            ELSE ''
                        END
                    || ' ->'
                END
            || repl_slot.assmbl_bom_cd
            ||
                CASE
                    WHEN ( repl_slot.pos_ct > 1 )
                         OR ( repl_pos.eqp_pos_cd != '1' ) THEN ' ('
                    || repl_pos.eqp_pos_cd
                    || ') '
                    ELSE ''
                END
            AS config_pos_sdesc INTO lv2_sdesc
        FROM
            sched_stask
            INNER JOIN sched_part ON
               sched_part.sched_db_id   = sched_stask.repl_sched_db_id   AND
               sched_part.sched_id      = sched_stask.repl_sched_id      AND
               sched_part.sched_part_id = sched_stask.repl_sched_part_id
            INNER JOIN sched_rmvd_part ON
               sched_rmvd_part.sched_db_id        = sched_part.sched_db_id   AND
               sched_rmvd_part.sched_id           = sched_part.sched_id      AND
               sched_rmvd_part.sched_part_id      = sched_part.sched_part_id AND
               sched_rmvd_part.sched_rmvd_part_id = 1
            INNER JOIN eqp_assmbl_bom repl_slot ON
               repl_slot.assmbl_db_id  = sched_part.assmbl_db_id  AND
               repl_slot.assmbl_cd     = sched_part.assmbl_cd     AND
               repl_slot.assmbl_bom_id = sched_part.assmbl_bom_id
            INNER JOIN eqp_assmbl_pos repl_pos ON
               repl_pos.assmbl_db_id   = sched_part.assmbl_db_id  AND
               repl_pos.assmbl_cd      = sched_part.assmbl_cd     AND
               repl_pos.assmbl_bom_id  = sched_part.assmbl_bom_id AND
               repl_pos.assmbl_pos_id  = sched_part.assmbl_pos_id
            INNER JOIN eqp_assmbl_bom nh_slot ON
               nh_slot.assmbl_db_id    = sched_part.nh_assmbl_db_id AND
               nh_slot.assmbl_cd       = sched_part.nh_assmbl_cd    AND
               nh_slot.assmbl_bom_id   = sched_part.nh_assmbl_bom_id
            INNER JOIN eqp_assmbl_pos nh_pos ON
               nh_pos.assmbl_db_id     = sched_part.nh_assmbl_db_id  AND
               nh_pos.assmbl_cd        = sched_part.nh_assmbl_cd     AND
               nh_pos.assmbl_bom_id    = sched_part.nh_assmbl_bom_id AND
               nh_pos.assmbl_pos_id    = sched_part.nh_assmbl_pos_id
         WHERE
               sched_stask.sched_db_id   = iSched_db_id AND
               sched_stask.sched_id      = iSched_id
            ;
          RETURN (lv2_sdesc);
        END IF;

    RETURN (lv2_sdesc);

	EXCEPTION
	       WHEN no_data_found THEN
	            RETURN (NULL);
	       WHEN too_many_rows THEN
	            RETURN (NULL);
	       WHEN OTHERS THEN
	            RAISE;

END get_inv_repl_sdesc;
/
