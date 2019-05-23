--liquibase formatted sql


--changeSet ALERT_PKG_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "ALERT_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : ALERT_PKG
   -- Object Type : Package Header
   -- Date        : February 22, 2009
   -- Coder       : Ed Irvine
   -- Recent Date : December 7, 2009
   -- Recent Coder: Ed Irvine
   -- Description :
   -- This package contains methods for raising alerts in maintenix from
   -- a PLSQL framework.
   --
   -- Procedures : raise_alert
   ----------------------------------------------------------------------------
   -- Copyright © 2009-2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   gc_import_alert_id         CONSTANT NUMBER := 202; --  LCR file imports for DPO
   gc_export_alert_id         CONSTANT NUMBER := 201; --  LCR file exports for DPO
   gc_validate_alert_id       CONSTANT NUMBER := 200; -- Streams objects validation DPO
   gc_apply_alert_id          CONSTANT NUMBER := 203; -- Apply process errors DPO
   gc_inv_xfer_export_success CONSTANT NUMBER := 204;
   gc_inv_xfer_export_failure CONSTANT NUMBER := 205;
   gc_inv_xfer_import_success CONSTANT NUMBER := 206;
   gc_inv_xfer_import_failure CONSTANT NUMBER := 207;
   gc_inv_xfer_induct_success CONSTANT NUMBER := 208;
   gc_inv_xfer_induct_failure CONSTANT NUMBER := 209;
   gc_alert_status_cd_new CONSTANT utl_alert.alert_status_cd%TYPE := 'NEW';


   -----------------------------------------------------------------------------
   -- Public Package Types
   -----------------------------------------------------------------------------
   TYPE gt_alert_parm IS RECORD(
      parm_ord         utl_alert_parm.parm_ord%TYPE,
      parm_type        utl_alert_parm.parm_type%TYPE,
      parm_value_sdesc utl_alert_parm.parm_value_sdesc%TYPE,
      parm_value       utl_alert_parm.parm_value%TYPE);

   TYPE gt_tab_alert_parm IS TABLE OF gt_alert_parm INDEX BY BINARY_INTEGER;


   ----------------------------------------------------------------------------
   -- Package Exceptions
   ----------------------------------------------------------------------------
   gc_ex_alert_err CONSTANT NUMBER := -20100;
   gex_alert_err EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_alert_err,
                         -20100);

   gc_ex_alert_notfound CONSTANT NUMBER := -20101;
   gex_alert_notfound EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_alert_notfound,
                         -20101);
   gv_err_msg_alert_notfound CONSTANT VARCHAR2(2000) := 'The alert being raised has no associated alert type. Please check your configuration.';


   ----------------------------------------------------------------------------
   -- Procedure:   raise_alert
   -- Arguments:   p_alert_type_id   (NUMBER) -- alert type to be created
   --              p_alert_priority  (NUMBER) -- the priority of the alert
   --              p_alert_parm_tb   (GT_TAB_ALERT_PARM) -- plsql table of parameters
   --
   -- Description:  Takes alert information as parameters and executes initiating
   --               an alert into the maintenix alert framework.
   ----------------------------------------------------------------------------
   PROCEDURE raise_alert(p_alert_type_id  IN utl_alert_type.alert_type_id%TYPE,
                         p_alert_priority IN utl_alert_type.priority%TYPE DEFAULT 0,
                         p_alert_parm_tb  IN gt_tab_alert_parm);

   ----------------------------------------------------------------------------
   -- Procedure:   raise_job_failed_alert
   -- Arguments:   p_error_text   (VARCHAR) -- errroe message for job fail
   --              p_priority  (NUMBER) -- the priority of the alert
   --
   --
   -- Description:  Takes alert information from parameters and enters a record
   --               in
   ----------------------------------------------------------------------------
   PROCEDURE raise_job_failed_alert(p_error_text  IN utl_alert_parm.parm_value%TYPE,
                          p_priority IN utl_alert.priority%TYPE);

END alert_pkg;
/