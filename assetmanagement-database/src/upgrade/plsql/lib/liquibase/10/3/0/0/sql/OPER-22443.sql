--liquibase formatted sql

--changeSet OPER-22443:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
    lAssociationId NUMBER;
	-- Cursor to hold groups of inventories with same serial number and manufact_cd that belong to either same or alternate part numbers
    CURSOR lcur_SimilarInvGroupList
     IS
       SELECT
          LOWER( inv_inv.serial_no_oem ) AS serial_no_oem,
          eqp_part_no.manufact_db_id,
          eqp_part_no.manufact_cd,
          eqp_part_baseline.bom_part_db_id,
          eqp_part_baseline.bom_part_id
       FROM
          inv_inv
          INNER JOIN eqp_part_no ON
             inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
             inv_inv.part_no_id    = eqp_part_no.part_no_id
          INNER JOIN eqp_part_baseline ON
             eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
             eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
       WHERE 
          LOWER( inv_inv.serial_no_oem ) NOT IN ( 'xxx', 'n/a', 'unk'  ) AND
          LOWER( inv_inv.serial_no_oem ) NOT LIKE ('bn %') AND
          inv_inv.inv_class_cd IN ( 'TRK' , 'SER' ) AND
          LOWER( eqp_part_no.manufact_cd ) != 'unk'
       GROUP BY
          LOWER( inv_inv.serial_no_oem ),
          eqp_part_no.manufact_db_id,
          eqp_part_no.manufact_cd,
          eqp_part_baseline.bom_part_db_id,
          eqp_part_baseline.bom_part_id
       HAVING
          COUNT (*) > 1;

   -- Cursor to get the list of inventories associated to a group
   CURSOR lcur_SimilarInvList(
        serial_no_oem_in IN inv_inv.serial_no_oem%TYPE,
        bom_part_db_id_in IN inv_inv.bom_part_db_id%TYPE,
        bom_part_id_in IN inv_inv.bom_part_id%TYPE,
        manufact_db_id_in IN eqp_manufact.manufact_db_id%TYPE,
        manufact_cd_in IN eqp_manufact.manufact_cd%TYPE
        ) IS
       SELECT DISTINCT
          inv_inv.inv_no_db_id,
          inv_inv.inv_no_id
       FROM
          inv_inv
       INNER JOIN eqp_part_no ON
          inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
          inv_inv.part_no_id    = eqp_part_no.part_no_id
       INNER JOIN eqp_part_baseline ON
          eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
          eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id          
       WHERE
          eqp_part_no.manufact_db_id = manufact_db_id_in AND
          eqp_part_no.manufact_cd    = manufact_cd_in
          AND
          eqp_part_baseline.bom_part_db_id = bom_part_db_id_in AND
          eqp_part_baseline.bom_part_id    = bom_part_id_in
          AND
          LOWER( inv_inv.serial_no_oem )   = serial_no_oem_in;
             
BEGIN
  FOR lrec_SimilarInvGroupList IN lcur_SimilarInvGroupList LOOP
  
     lAssociationId := inv_association_id_seq.nextval;
     
       FOR lrec_SimilarInvList IN lcur_SimilarInvList(
       lrec_SimilarInvGroupList.serial_no_oem,
       lrec_SimilarInvGroupList.bom_part_db_id,
       lrec_SimilarInvGroupList.bom_part_id,
       lrec_SimilarInvGroupList.manufact_db_id,
       lrec_SimilarInvGroupList.manufact_cd
       )
	   -- Create association between inventories belonging to the groups identified
          LOOP
              INSERT INTO inv_association( association_id , inv_no_db_id , inv_no_id )
              SELECT
			     lAssociationId,
				 lrec_SimilarInvList.inv_no_db_id,
				 lrec_SimilarInvList.inv_no_id
			  FROM
			     DUAL
			  WHERE
                 NOT EXISTS ( SELECT 1 FROM inv_association WHERE inv_no_db_id = lrec_SimilarInvList.inv_no_db_id AND inv_no_id = lrec_SimilarInvList.inv_no_id);
          END LOOP;
                  
  END LOOP;
 
END;
/
