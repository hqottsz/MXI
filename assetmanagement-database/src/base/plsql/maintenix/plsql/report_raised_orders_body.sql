--liquibase formatted sql


--changeSet report_raised_orders_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY reportraisedorders AS
/************************************************************************
** Date:        March 28, 2016
** Author:      Karan Tandon
** Description: raised orders by account
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
**
** Except as expressly provided by written license signed by a duly appointed
** officer of Mxi Technologies. any disclosure, distribution,
** reproduction, compilation, modification, creation of derivative works and/or
** other use of the Mxi source code is strictly prohibited.  Inclusion of a
** copyright notice shall not be taken to indicate that the source code has
** been published.
**
***************************************************************************/

   -----------------------------------------------------------------------------------------------------------
   -- local package constants
   -----------------------------------------------------------------------------------------------------------
   gk_row_limit    CONSTANT PLS_INTEGER := 100;

   -----------------------------------------------------------------------------
   -- Description: Bring raised orders by account results
   -----------------------------------------------------------------------------
   FUNCTION GetRaisedOrders(
      aidt_from_date        IN DATE,
      aidt_to_date          IN DATE,
      aiv_account_code      IN VARCHAR2
   ) RETURN RaisedOrderTTRec
     PIPELINED
     IS

      CURSOR lcur_rsd_ord IS
      SELECT
            po_header.po_type_cd,
            po_event.event_sdesc AS po_number,
            po_status.user_status_cd,
            po_line.order_qt,
            po_event.actual_start_dt,
            org_vendor.vendor_cd,
            org_vendor.vendor_name,
            eqp_part_no.part_no_oem,
            eqp_part_no.part_no_sdesc,
            po_line.line_ldesc,
            inv_inv.serial_no_oem,
            fnc_account.account_cd,
            fnc_account.account_type_cd,
            po_line.line_price,
            po_header.currency_cd,
            -- show the list of invoice numbers comma separated
            LISTAGG(invoice_event.event_sdesc, ', ') WITHIN GROUP (ORDER BY invoice_event.event_sdesc) AS invoice_number
      FROM
         po_header
         -- join to determine po line data
         INNER JOIN po_line ON
            po_header.po_db_id = po_line.po_db_id AND
            po_header.po_id    = po_line.po_id
         -- join determine invoice info
         LEFT JOIN po_invoice_line_map ON
            po_invoice_line_map.po_db_id = po_line.po_db_id AND
            po_invoice_line_map.po_id = po_line.po_id AND
            po_invoice_line_map.po_line_id = po_line.po_line_id
         -- join directly to evt_event as short path, since
         -- there is no usage for po_invoice_line and po_invoice in the
         -- select statement. This will also improve performance.
         LEFT JOIN evt_event invoice_event ON
            invoice_event.event_db_id = po_invoice_line_map.po_invoice_db_id AND
            invoice_event.event_id = po_invoice_line_map.po_invoice_id AND
            -- do not show invoice info when its status is CANCEL
            NOT
            (
                invoice_event.event_status_db_id = 0 AND
                invoice_event.event_status_cd = 'PICANCEL'
            )
         -- join to determine vendor code
         INNER JOIN org_vendor ON
            po_header.vendor_db_id = org_vendor.vendor_db_id AND
            po_header.vendor_id    = org_vendor.vendor_id
         -- join to determine account code
         INNER JOIN fnc_account ON
            fnc_account.account_db_id = po_line.account_db_id AND
            fnc_account.account_id    = po_line.account_id
         -- join to determine po number
         INNER JOIN evt_event po_event ON
            po_header.po_db_id = po_event.event_db_id AND
            po_header.po_id    = po_event.event_id
         -- join to determine the order status
         INNER JOIN ref_event_status po_status ON
            po_status.event_status_db_id = po_event.event_status_db_id AND
            po_status.event_status_cd    = po_event.event_status_cd
         -- join to determine first outbound shipment
         LEFT JOIN
            (
                -- get send repair inventory
                SELECT
                   po_line.po_db_id,
                   po_line.po_id,
                   po_line.po_line_id,
                   wp_stask.main_inv_no_db_id AS inv_no_db_id,
                   wp_stask.main_inv_no_id    AS inv_no_id
                FROM
                   po_line
                   INNER JOIN sched_stask wp_stask ON
                      po_line.sched_db_id = wp_stask.sched_db_id AND
                      po_line.sched_id    = wp_stask.sched_id
                WHERE
                   po_line.po_line_type_db_id = 0 AND
                   po_line.po_line_type_cd    = 'REPAIR'

                UNION ALL
                 -- get send exchange inventory
                SELECT
                   po_line.po_db_id,
                   po_line.po_id,
                   po_line.po_line_id,
                   ship_shipment_line.inv_no_db_id,
                   ship_shipment_line.inv_no_id
                FROM
                   po_line
                   INNER JOIN ship_shipment ON
                      po_line.po_db_id = ship_shipment.po_db_id AND
                      po_line.po_id    = ship_shipment.po_id
                   INNER JOIN ship_shipment_line ON
                      ship_shipment.shipment_db_id = ship_shipment_line.shipment_db_id AND
                      ship_shipment.shipment_id    = ship_shipment_line.shipment_id
                WHERE
                   po_line.po_line_type_db_id = 0 AND
                   po_line.po_line_type_cd    = 'EXCHANGE'
                   AND
                   ship_shipment.shipment_type_db_id = 0 AND
                   ship_shipment.shipment_type_cd    = 'SENDXCHG'
            ) po_inv ON
            po_inv.po_db_id   = po_line.po_db_id AND
            po_inv.po_id      = po_line.po_id AND
            po_inv.po_line_id = po_line.po_line_id
         LEFT JOIN inv_inv ON
            inv_inv.inv_no_db_id = po_inv.inv_no_db_id AND
            inv_inv.inv_no_id    = po_inv.inv_no_id
         -- join to determine part number
         LEFT JOIN eqp_part_no ON
            eqp_part_no.part_no_db_id = po_line.part_no_db_id AND
            eqp_part_no.part_no_id    = po_line.part_no_id
      WHERE
          po_event.actual_start_dt BETWEEN aidt_from_date AND aidt_to_date
          AND
          DECODE(aiv_account_code, '*ALL', '1', fnc_account.account_cd) = DECODE(aiv_account_code, '*ALL', '1', aiv_account_code)
          AND
          po_line.deleted_bool = 0
      -- group by it is necessary for the LISTAGG function.
      GROUP BY
         po_header.po_type_cd,
         po_event.event_sdesc,
         po_status.user_status_cd,
         po_line.order_qt,
         po_event.actual_start_dt,
         org_vendor.vendor_cd,
         org_vendor.vendor_name,
         eqp_part_no.part_no_oem,
         eqp_part_no.part_no_sdesc,
         po_line.line_ldesc,
         inv_inv.serial_no_oem,
         fnc_account.account_cd,
         fnc_account.account_type_cd,
         po_line.line_price,
         po_header.currency_cd
      ORDER BY
         po_event.actual_start_dt,
         po_event.event_sdesc;

      lrec_rsd_ord   RaisedOrderTTRec;

   BEGIN

      OPEN lcur_rsd_ord;

      LOOP

         FETCH lcur_rsd_ord
         BULK COLLECT INTO lrec_rsd_ord
         LIMIT gk_row_limit;

         EXIT WHEN lrec_rsd_ord.COUNT = 0;

         FOR i IN 1..lrec_rsd_ord.COUNT LOOP

            PIPE ROW (lrec_rsd_ord(i));

         END LOOP;

       END LOOP;

     CLOSE lcur_rsd_ord;

     RETURN;

     EXCEPTION
        WHEN OTHERS THEN
           RAISE;

   END GetRaisedOrders;

END reportraisedorders;
/