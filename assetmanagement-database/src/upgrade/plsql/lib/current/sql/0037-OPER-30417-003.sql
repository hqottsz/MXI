--liquibase formatted sql


--changeSet OPER-30417-003:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "BLT_WF_ERROR_LOG_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : BLT_WF_ERROR_LOG_PKG
   -- Object Type : Package Header
   -- Date        : Nov 25,2011
   -- Coder       : Vince Chan
   -- Recent Date : Nov 25,2011
   -- Recent Coder: Vince Chan
   -- Description :
   -- This package contains methods for inserting into blt_wf_error_log
   --
   ----------------------------------------------------------------------------
   -- Copyright @2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------


   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_error
   -- Arguments:    p_wf_error_log_id
   --               p_wf_rec_log_id
   --               p_wf_cycle_log_id
   --               p_ref_error_cd
   --               p_wf_error_rec_identifier
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_error_log table
   -----------------------------------------------------------------------------------------
 PROCEDURE insert_blt_wf_error(p_wf_error_log_id  IN OUT blt_wf_error_log.wf_error_log_id%TYPE,
                       p_wf_rec_log_id               IN blt_wf_error_log.wf_rec_log_id%TYPE,
                       p_wf_cycle_log_id          IN blt_wf_error_log.wf_cycle_log_id%TYPE,
                       p_ref_error_cd             IN blt_wf_error_log.ref_error_cd%TYPE,
                       p_wf_error_rec_identifier  IN blt_wf_error_log.wf_error_rec_identifier%TYPE
  );

END BLT_WF_ERROR_LOG_PKG;
/ 
