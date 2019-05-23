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