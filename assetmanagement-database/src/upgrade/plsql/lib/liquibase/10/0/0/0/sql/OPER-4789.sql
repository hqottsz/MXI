--liquibase formatted sql


--changeSet OPER-4789:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE report_quarantine_tag_pkg AS
   /************************************************************************
   ** Date:        March 27, 2017
   ** Author:      Gustavo Pichetto
   ** Description: Quarantine Tag Report
   *************************************************************************
   **
   ** Confidential, proprietary and/or trade secret information of
   ** Mxi Technologies.
   **
   ** Copyright 2005-2017 Mxi Technologies. All Rights Reserved.
   **
   ** Except as expressly provided by written license signed by a duly appointed
   ** officer of Mxi Technologies. any disclosure, distribution,
   ** reproduction, compilation, modification, creation of derivative works and/or
   ** other use of the Mxi source code is strictly prohibited.  Inclusion of a
   ** copyright notice shall not be taken to indicate that the source code has
   ** been published.
   **
   ***************************************************************************/
  
   TYPE QuarantineTagTRec IS RECORD (
      quarantined_date        evt_event.event_dt%TYPE,
      user_first_name         utl_user.first_name%TYPE,
      user_last_name          utl_user.last_name%TYPE,
      quarantine_reason_cd    ref_stage_reason.user_reason_cd%TYPE,
      quarantine_reason_desc  ref_stage_reason.desc_sdesc%TYPE,
      quarantine_location     inv_loc.loc_cd%TYPE,
      quarantine_notes        quar_quar.quar_note%TYPE,
      part_number             eqp_part_no.part_no_oem%TYPE,
      part_name               eqp_part_no.part_no_sdesc%TYPE,
      manufacturer_cd         eqp_part_no.manufact_cd%TYPE,
      manufacturer_name       eqp_manufact.manufact_name%TYPE,
      serial_no               inv_inv.serial_no_oem%TYPE,
      received_on             inv_inv.received_dt%TYPE,
      purchase_order_no       inv_inv.po_ref_sdesc%TYPE,
      quantity                inv_inv.bin_qt%TYPE,
      quantity_unit_cd        eqp_part_no.qty_unit_cd%TYPE,
      vendor_cd               org_vendor.vendor_cd%TYPE,
      vendor_name             org_vendor.vendor_name%TYPE,
      barcode                 quar_quar.barcode_sdesc%TYPE,
      quar_db_id              quar_quar.quar_db_id%TYPE,
      quar_id                 quar_quar.quar_id%TYPE
   );
   
   TYPE QuarantineTagTTRec IS TABLE OF QuarantineTagTRec;
   
   TYPE QuarantineActionTRec IS RECORD (
      action_barcode         quar_action.barcode_sdesc%TYPE,
      discrepancy_desc       quar_action.discrepancy_ldesc%TYPE,
      action_desc            quar_action.action_ldesc%TYPE,
      status_cd              quar_action_status.quar_action_status_cd%TYPE 
   );
   
   TYPE QuarantineActionTTRec IS TABLE OF QuarantineActionTRec;
  
   -----------------------------------------------------------------------------------------------------------
   -- Function     : GetQuarantineTag
   -- Return       : QuarantineTagTTRec (Type Table) - collection of data
   -- Description  : Get Quarantine Tag information
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetQuarantineTag (
      ain_quar_db_id  IN quar_quar.quar_db_id%TYPE,
      ain_quar_id     IN quar_quar.quar_id%TYPE
   ) RETURN QuarantineTagTTRec
   PIPELINED;

   -----------------------------------------------------------------------------------------------------------
   -- Function     : GetQuarantineAction
   -- Return       : QuarantineActionTTRec (Type Table) - collection of data
   -- Description  : Get Quarantine Action details
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetQuarantineAction (
      ain_quar_action_db_id IN quar_action.quar_db_id%TYPE,
      ain_quar_action_id    IN quar_action.quar_id%TYPE
   ) RETURN QuarantineActionTTRec
   PIPELINED;

END report_quarantine_tag_pkg;
/

--changeSet OPER-4789:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY report_quarantine_tag_pkg AS
   /************************************************************************
   ** Date:        March 27, 2017
   ** Author:      Gustavo Pichetto
   ** Description: Quarantine Tag Report
   *************************************************************************
   **
   ** Confidential, proprietary and/or trade secret information of
   ** Mxi Technologies.
   **
   ** Copyright 2005-2017 Mxi Technologies. All Rights Reserved.
   **
   ** Except as expressly provided by written license signed by a duly appointed
   ** officer of Mxi Technologies. any disclosure, distribution,
   ** reproduction, compilation, modification, creation of derivative works and/or
   ** other use of the Mxi source code is strictly prohibited.  Inclusion of a
   ** copyright notice shall not be taken to indicate that the source code has
   ** been published.
   **
   ***************************************************************************/
   
   gk_row_limit              CONSTANT PLS_INTEGER := 100;
   gk_quar_action_row_limit  CONSTANT PLS_INTEGER := 5;
 
   -----------------------------------------------------------------------------------------------------------
   -- Function     : GetQuarantineTag
   -- Return       : QuarantineTagTTRec (Type Table) - collection of data
   -- Description  : Get Quarantine Tag information
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetQuarantineTag (
      ain_quar_db_id  IN quar_quar.quar_db_id%TYPE,
      ain_quar_id     IN quar_quar.quar_id%TYPE
   ) RETURN QuarantineTagTTRec
   PIPELINED
   IS
      CURSOR lcur_quarantine_tag IS
      SELECT
         evt_event.event_dt,
         utl_user.first_name,
         utl_user.last_name,
         ref_stage_reason.user_reason_cd,
         ref_stage_reason.desc_sdesc,
         inv_loc.loc_cd,
         quar_quar.quar_note AS quarantine_notes,
         eqp_part_no.part_no_oem,
         eqp_part_no.part_no_sdesc,
         eqp_part_no.manufact_cd, 
         eqp_manufact.manufact_name,
         inv_inv.serial_no_oem,
         inv_inv.received_dt AS received_on,
         inv_inv.po_ref_sdesc AS purchase_order_no,
         NVL(inv_inv.bin_qt, 1), 
         eqp_part_no.qty_unit_cd,
         org_vendor.vendor_cd, 
         org_vendor.vendor_name,
         quar_quar.barcode_sdesc AS barcode,
         quar_quar.quar_db_id,
         quar_quar.quar_id
      FROM
         quar_quar
         INNER JOIN inv_inv ON
            inv_inv.inv_no_db_id = quar_quar.inv_no_db_id AND
            inv_inv.inv_no_id    = quar_quar.inv_no_id
         INNER JOIN inv_loc ON
            inv_loc.loc_db_id = DECODE(quar_quar.historic_bool, 1, quar_quar.loc_db_id, inv_inv.loc_db_id) AND
            inv_loc.loc_id    = DECODE(quar_quar.historic_bool, 1, quar_quar.loc_id, inv_inv.loc_id)
         INNER JOIN eqp_part_no ON
            inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
            inv_inv.part_no_id    = eqp_part_no.part_no_id
         INNER JOIN eqp_manufact ON
            eqp_part_no.manufact_db_id = eqp_manufact.manufact_db_id AND
            eqp_part_no.manufact_cd    = eqp_manufact.manufact_cd
         LEFT JOIN evt_event ON
            evt_event.event_db_id = quar_quar.event_db_id AND
            evt_event.event_id    = quar_quar.event_id
         LEFT JOIN ref_stage_reason ON
            ref_stage_reason.stage_reason_db_id = evt_event.stage_reason_db_id AND
            ref_stage_reason.stage_reason_cd    = evt_event.stage_reason_cd
         LEFT JOIN po_header ON
            po_header.po_db_id = inv_inv.po_db_id AND
            po_header.po_id    = inv_inv.po_id
         LEFT JOIN org_hr ON
            evt_event.editor_hr_db_id = org_hr.hr_db_id AND
            evt_event.editor_hr_id    = org_hr.hr_id
         LEFT JOIN utl_user ON
            org_hr.user_id  = utl_user.user_id
         LEFT JOIN org_vendor ON
            po_header.vendor_db_id = org_vendor.vendor_db_id AND
            po_header.vendor_id    = org_vendor.vendor_id
      WHERE
         quar_quar.quar_db_id = ain_quar_db_id AND
         quar_quar.quar_id    = ain_quar_id      
      ;

      lrec_quarantine_tag   QuarantineTagTTRec;

       BEGIN

       OPEN lcur_quarantine_tag;
       LOOP
          FETCH lcur_quarantine_tag
          BULK COLLECT INTO lrec_quarantine_tag
          LIMIT gk_row_limit;

             EXIT WHEN lrec_quarantine_tag.COUNT = 0;
             FOR i IN 1..lrec_quarantine_tag.COUNT LOOP

                PIPE ROW (lrec_quarantine_tag(i));

             END LOOP;
       END LOOP;
       CLOSE lcur_quarantine_tag;

       RETURN;

   EXCEPTION
      WHEN OTHERS THEN
         RAISE;

   END GetQuarantineTag;
   
   -----------------------------------------------------------------------------------------------------------
   -- Function     : GetQuarantineAction
   -- Return       : QuarantineActionTTRec (Type Table) - collection of data
   -- Description  : Get Quarantine Action details
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetQuarantineAction (
      ain_quar_action_db_id IN quar_action.quar_db_id%TYPE,
      ain_quar_action_id    IN quar_action.quar_id%TYPE
   ) RETURN QuarantineActionTTRec
   PIPELINED
   IS
      CURSOR lcur_quar_action IS
       
      SELECT
         quar_action.barcode_sdesc,
         quar_action.discrepancy_ldesc,
         quar_action.action_ldesc,
         quar_action_status.quar_action_status_cd
      FROM
         quar_action
         INNER JOIN quar_action_status ON
            quar_action_status.quar_db_id     = quar_action.quar_db_id AND
            quar_action_status.quar_id        = quar_action.quar_id AND
            quar_action_status.quar_action_id = quar_action.quar_action_id 
      WHERE
         quar_action.quar_db_id = ain_quar_action_db_id AND
         quar_action.quar_id    = ain_quar_action_id 
         AND
         -- nested query to not include quarantine actions with CLOSE status 
         -- as per report requirement
         quar_action.barcode_sdesc NOT IN
         (
              SELECT 
                quar_action.barcode_sdesc AS barcode
              FROM
                quar_action
                INNER JOIN quar_action_status ON
                   quar_action_status.quar_db_id     = quar_action.quar_db_id AND
                   quar_action_status.quar_id        = quar_action.quar_id AND
                   quar_action_status.quar_action_id = quar_action.quar_action_id 
              WHERE
                   quar_action_status.quar_action_status_db_id = 0 AND
                   quar_action_status.quar_action_status_cd    = 'CLOSED'        
         )     
         AND
         -- do not show more than 5 rows as per report requirement
         ROWNUM <= gk_quar_action_row_limit   
      ;

        lrec_quar_action QuarantineActionTTRec;

        BEGIN

        OPEN lcur_quar_action;
        LOOP
           FETCH lcur_quar_action
           BULK COLLECT INTO lrec_quar_action
           LIMIT gk_row_limit;

             EXIT WHEN lrec_quar_action.COUNT = 0;
             FOR i IN 1..lrec_quar_action.COUNT LOOP

                PIPE ROW (lrec_quar_action(i));

             END LOOP;
        END LOOP;
        CLOSE lcur_quar_action;

        RETURN;

   EXCEPTION
      WHEN OTHERS THEN
         RAISE;
   END GetQuarantineAction;

END report_quarantine_tag_pkg;
/