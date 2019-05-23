--liquibase formatted sql


--changeSet oper-236-002:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY opr_rbl_utility_pkg IS

-- private constant
lcv_start_stage   CONSTANT  VARCHAR2(5) := 'START';
lcv_end_stage     CONSTANT  VARCHAR2(3) := 'END';
lcv_in_working    CONSTANT  VARCHAR2(7) := 'WORKING';
lcv_in_error      CONSTANT  VARCHAR2(5) := 'ERROR';
lcv_in_idle       CONSTANT  VARCHAR2(4) := 'IDLE';

/******************************************************************************
*
*  Procedure:    opr_get_config_param
*  Arguments:
*  Return:
*  Description:  Function that will return the parallel mode parameter value
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  17-Dec-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

FUNCTION opr_get_config_param
RETURN BOOLEAN
IS

  lv_parm_value    utl_config_parm.parm_value%TYPE;

BEGIN

   SELECT
      NVL(parm_name,'FALSE')
   INTO
      lv_parm_value
   FROM
      utl_config_parm
   WHERE
      parm_name = 'MX_CORE_RELIABILITY_PARALLEL_MODE';

   IF lv_parm_value = 'TRUE' THEN
      RETURN TRUE;
   ELSE
      RETURN FALSE;
   END IF;

EXCEPTION
   WHEN no_data_found THEN
      RETURN FALSE;

END;


/******************************************************************************
*
*  Procedure:    opr_rbl_extract_proc
*  Arguments:
*  Return:
*  Description:  Procedure to execute extraction procedure
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_extract_proc AS

   -- regeneration cursor
   CURSOR lcur_regen IS
   SELECT
      opr_report_regen.ROWID row_id,
      opr_report.report_code,
      opr_report.program_name xtract_program_name,
      (
        SELECT
           program_name
        FROM
           opr_report xform_prog
        WHERE
           xform_prog.report_code = opr_report.report_code
           AND
           xform_prog.program_type_code = 'XFORM'
      ) xform_program_name,
      opr_report_regen.start_date,
      opr_report_regen.end_date,
      opr_report.exec_parallel_flag
   FROM
      opr_report
      INNER JOIN opr_report_regen ON
         opr_report.report_code = opr_report_regen.report_code
         AND
         opr_report_regen.extracted_date IS NULL
   WHERE
      program_type_code = 'XTRACK'
      AND
      active_flag = 1
   ORDER BY
      execution_order;

   -- daily extraction cursor
   CURSOR lcur_report IS
   SELECT
      opr_report.ROWID row_id,
      opr_report.report_code,
      opr_report.program_name,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(start_date,SYSDATE)
         ELSE
            TRUNC((last_success_end_date - 1))
      END start_date,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(end_date,SYSDATE)
         ELSE
            TRUNC(SYSDATE)
      END end_date,
      opr_report.exec_parallel_flag
   FROM
      opr_report
   WHERE
      program_type_code = 'XTRACK'
      AND
      active_flag = 1
   ORDER BY
      execution_order;

   lcv_error_msg        opr_report.last_error%TYPE;
   ln_row_processed     opr_report.row_processed%TYPE;
   lbl_parallel_mode     BOOLEAN DEFAULT NULL;
   lv_regen_stage       VARCHAR2(20);

BEGIN

   -- get parallel mode config
   lbl_parallel_mode := opr_get_config_param;

   --# regeneration
   FOR lrec_regen IN lcur_regen LOOP

      BEGIN

         IF lbl_parallel_mode THEN
            --
            -- parallel mode
            -- submit an Oracle job
               NULL;

         ELSE

               -- execute the extraction program
               lv_regen_stage := 'XT';
               EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_regen.xtract_program_name || '; END;' USING OUT ln_row_processed, lrec_regen.start_date, lrec_regen.end_date;

               -- logging
               opr_rbl_regen_logger_proc (
                  airid_row_id        => lrec_regen.row_id,
                  aiv_execute_type_cd => 'XT'
               );

               -- execute the transformation program
               lv_regen_stage := 'XF';
               EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_regen.xform_program_name || '; END;' USING OUT ln_row_processed, lrec_regen.start_date, lrec_regen.end_date;

               -- logging
               opr_rbl_regen_logger_proc (
                  airid_row_id        => lrec_regen.row_id,
                  aiv_execute_type_cd => 'XF'
               );

               -- commit the work
               COMMIT;

         END IF;

     EXCEPTION
        WHEN OTHERS THEN
           -- error logging
           lcv_error_msg := SQLERRM;
           opr_rbl_regen_logger_proc (
              airid_row_id        => lrec_regen.row_id,
              aiv_execute_type_cd => lv_regen_stage,
              aiv_error_message   => lv_regen_stage || ' ' ||lcv_error_msg
           );

           IF lrec_regen.exec_parallel_flag = 0 THEN
             -- exit on this loop
             EXIT;
           END IF;
     END;

   END LOOP;


   --# daily extraction
   FOR lrec_report IN lcur_report LOOP

      BEGIN

         IF lbl_parallel_mode THEN
            --
            -- parallel mode
            -- submit an Oracle job
               NULL;

         ELSE

            -- start logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_start_stage,
               aiv_exec_status_code => lcv_in_working
            );

            -- normal daily extraction
            EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_report.program_name || '; END;' USING OUT ln_row_processed, lrec_report.start_date, lrec_report.end_date;

            -- commit the work
            COMMIT;

            -- end logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               ain_row_processed    => ln_row_processed,
               aiv_exec_status_code => lcv_in_idle
            );

         END IF;

      EXCEPTION
         WHEN OTHERS THEN
            lcv_error_msg := SQLERRM;
            -- error logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               aiv_error_message    => lcv_error_msg,
               aiv_exec_status_code => lcv_in_error
            );

           IF lrec_report.exec_parallel_flag = 0 THEN
              -- exit on this loop
              EXIT;
           END IF;

      END;

   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
     -- utl job error logging - to be developed
      ROLLBACK;
      RAISE;

END opr_rbl_extract_proc;


/******************************************************************************
*
*  Procedure:    opr_rbl_transform_proc
*  Arguments:
*  Return:
*  Description:  Procedure to execute transformation procedure
*
*  Orig. Coder:  Max Gabua
*  Recent Coder:
*  Recent Date:  28-Nov-2013
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2013 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
PROCEDURE opr_rbl_transform_proc AS

   CURSOR lcur_report IS
   SELECT
      opr_report.ROWID row_id,
      opr_report.report_code,
      opr_report.program_name,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(start_date,SYSDATE)
         ELSE
            TRUNC((last_success_end_date - 1))
      END start_date,
      CASE
         WHEN last_success_end_date IS NULL THEN
            NVL(end_date,SYSDATE)
         ELSE
            TRUNC(SYSDATE)
      END end_date,
      opr_report.exec_parallel_flag
   FROM
      opr_report
   WHERE
      program_type_code = 'XFORM'
      AND
      active_flag = 1
   ORDER BY
      execution_order;

   ln_row_processed     opr_report.row_processed%TYPE;
   lcv_error_msg        opr_report.last_error%TYPE;
   lv_parallel_mode     BOOLEAN;

BEGIN

   -- get parallel mode config
   lv_parallel_mode := opr_get_config_param;

   -- transformation
   FOR lrec_report IN lcur_report LOOP

      BEGIN

         IF lv_parallel_mode THEN
            --
            -- parallel mode
            -- submit an Oracle job
               NULL;

         ELSE

            -- start logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_start_stage,
               aiv_exec_status_code => lcv_in_working
            );

            -- normal daily extraction
            EXECUTE IMMEDIATE 'BEGIN :aon_rec_count := ' || lrec_report.program_name || '; END;' USING OUT ln_row_processed, lrec_report.start_date, lrec_report.end_date;

            -- commit the work
            COMMIT;

            -- end logging
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               ain_row_processed    => ln_row_processed,
               aiv_exec_status_code => lcv_in_idle
            );

         END IF;

      EXCEPTION
         WHEN OTHERS THEN
            lcv_error_msg := SQLERRM;
            opr_rbl_logger_proc (
               airid_row_id         => lrec_report.row_id,
               aiv_exec_stage_code  => lcv_end_stage,
               aiv_error_message    => lcv_error_msg,
               aiv_exec_status_code => lcv_in_error
            );

           IF lrec_report.exec_parallel_flag = 0 THEN
              -- exit on this loop
              EXIT;

           END IF;

      END;

   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
     -- utl job error logging - to be developed
      ROLLBACK;
      RAISE;

END opr_rbl_transform_proc;




-- procedure to do the error logging
PROCEDURE opr_rbl_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_exec_stage_code    IN    VARCHAR2  DEFAULT 'START',
   aiv_exec_status_code   IN    VARCHAR2  DEFAULT 'ERROR',
   ain_row_processed      IN    opr_report.row_processed%TYPE DEFAULT 0,
   aiv_error_message      IN    opr_report.last_error%TYPE DEFAULT NULL
) IS

  PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

   -- f1: check for execution stage
   IF UPPER(aiv_exec_stage_code) = 'START' THEN

      -- start stage
      UPDATE
         opr_report
      SET
         last_start_date = SYSDATE,
         status_code     = aiv_exec_status_code
      WHERE
         ROWID = airid_row_id;

   ELSE

      -- end stage
      --
      -- f2: check the execution status -- [S]uccess | [E]rror
      IF aiv_exec_status_code = 'ERROR' THEN

         -- error status
         UPDATE
            opr_report
         SET
            last_error_end_date = SYSDATE,
            last_error          = aiv_error_message,
            status_code         = aiv_exec_status_code
         WHERE
            ROWID = airid_row_id;

      ELSE

         -- success status
         UPDATE
            opr_report
         SET
            last_success_end_date = SYSDATE,
            last_error_end_date   = NULL,
            last_error            = NULL,
            row_processed         = ain_row_processed,
            status_code           = aiv_exec_status_code
         WHERE
            ROWID = airid_row_id;

      END IF; -- f2

   END IF; -- f1

   -- commit the work
   COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END opr_rbl_logger_proc;


-- procedure to do the error logging
PROCEDURE opr_rbl_regen_logger_proc (
   airid_row_id           IN    ROWID,
   aiv_execute_type_cd    IN    VARCHAR DEFAULT 'XT',
   aiv_error_message      IN    opr_report_regen.error%TYPE DEFAULT NULL
) IS

  PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

   -- f1: check for execution stage
   IF UPPER(aiv_execute_type_cd) = 'XT' THEN

         UPDATE
            opr_report_regen
         SET
            extracted_date = SYSDATE,
            error          = aiv_error_message
         WHERE
            ROWID = airid_row_id;
   ELSE

      -- error status
      UPDATE
         opr_report_regen
      SET
         transformed_date = SYSDATE,
         error            = aiv_error_message
      WHERE
         ROWID = airid_row_id;

   END IF; -- f1

   -- commit the work
   COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      RAISE;

END opr_rbl_regen_logger_proc;


END opr_rbl_utility_pkg;
/