--liquibase formatted sql


--changeSet OPER-30417-004:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "BLT_WF_ERROR_LOG_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : BLT_WF_ERROR_LOG_PKG
   -- Object Type : Package Body
   -- Date        : Nov 25,2011
   -- Coder       : Vince Chan
   -- Recent Date : Nov 25,2011
   -- Recent Coder: Vince Chan
   -- Description :
   -- This package contains methods for inserting into blt_wf_error_log
   --
   ----------------------------------------------------------------------------
   -- Copyright @2010-11 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   -----------------------------------------------------------------------------
   -- Local Package Constants
   -----------------------------------------------------------------------------
   c_pkg_name         CONSTANT VARCHAR2(30) := 'BLT_WF_ERROR_LOG_PKG';

   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


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
  )
  AS

      v_method_name VARCHAR2(30) := 'insert_blt_wf_error';
      PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
      --if p_blt_log_id is null, then new blt wf cycle log is to be inserted.
      v_step := 10;
      IF p_wf_error_log_id IS NULL
      THEN
         v_step := 20;
         SELECT mx_key_pkg.new_uuid
           INTO p_wf_error_log_id
           FROM dual;

         v_step := 30;
         INSERT INTO blt_wf_error_log
            (wf_error_log_id,
             wf_rec_log_id,
             wf_cycle_log_id,
             ref_error_cd,
             wf_error_rec_identifier )
         VALUES
            (p_wf_error_log_id,
             p_wf_rec_log_id,
             p_wf_cycle_log_id,
             p_ref_error_cd,
             p_wf_error_rec_identifier);

      ELSE
         --update existing log
         v_step := 40;
         UPDATE blt_wf_error_log
            SET wf_rec_log_id            = CASE WHEN p_wf_rec_log_id IS NOT NULL THEN p_wf_rec_log_id ELSE wf_rec_log_id END,
                wf_cycle_log_id          = CASE WHEN p_wf_cycle_log_id IS NOT NULL THEN p_wf_cycle_log_id ELSE wf_cycle_log_id END,
                ref_error_cd             = CASE WHEN p_ref_error_cd IS NOT NULL THEN p_ref_error_cd ELSE ref_error_cd END,
                wf_error_rec_identifier  = CASE WHEN p_wf_error_rec_identifier IS NOT NULL THEN p_wf_error_rec_identifier ELSE wf_error_rec_identifier END
          WHERE wf_error_log_id = p_wf_error_log_id;
      END IF;

      COMMIT;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);
  END insert_blt_wf_error;

END blt_wf_error_log_pkg;
/
