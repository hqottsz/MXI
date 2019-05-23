--liquibase formatted sql


--changeSet DeactivateWarranties:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedue:      DeactivateWarranties
* Arguments:     none
*
* Description:   This procedure de-activates the warranty contracts whose expiry date has elapsed but if warranty contract is assigned to order line which has parts received but not inspected as serviceable, warranty will not get deactivated.
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE DeactivateWarranties

 IS

BEGIN

  UPDATE warranty_defn
     SET status_cd = 'DEACTV'
   WHERE EXISTS
   (SELECT 1
            FROM warranty_defn exp_defn
            LEFT OUTER JOIN po_line_warranty ON po_line_warranty.warranty_defn_db_id = exp_defn.warranty_defn_db_id
                                            AND po_line_warranty.warranty_defn_id = exp_defn.warranty_defn_id
            LEFT OUTER JOIN po_line ON po_line.po_db_id = po_line_warranty.po_db_id
                                   AND po_line.po_id = po_line_warranty.po_id
                                   AND po_line.po_line_id = po_line_warranty.po_line_id

           WHERE (exp_defn.status_db_id, exp_defn.status_cd) NOT IN (('0','DEACTV')) AND
             exp_defn.expire_gdt IS NOT NULL AND
             exp_defn.expire_gdt < SYSDATE
	         AND
             warranty_defn.warranty_defn_db_id = exp_defn.warranty_defn_db_id AND
             warranty_defn.warranty_defn_id = exp_defn.warranty_defn_id
             AND
             (
                po_line.po_id IS NULL
                OR
                (po_line.warranty_bool = 1 AND
                 po_line.rstat_cd = 0 AND
                 po_line.pre_insp_qt = 0
                 )
             ));
END DeactivateWarranties;
/