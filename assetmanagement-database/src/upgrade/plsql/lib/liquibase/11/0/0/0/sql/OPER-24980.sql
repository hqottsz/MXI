--liquibase formatted sql

--changeSet OPER-24980:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new log type 'TECHCAB' to the table REF_FAULT_LOG_TYPE
BEGIN
   INSERT INTO
      ref_fault_log_type
         (
            fault_log_type_db_id,
            fault_log_type_cd,
            name_sdesc,
            desc_ldesc,
            user_cd,
            rstat_cd,
            creation_dt,
            revision_dt,
            revision_db_id,
            revision_user
         )
   SELECT
      0,
      'TECHCAB',
      'Technical and Cabin Logbook',
      'Electronic system providing functionality equivalent to an aircraft''s paper-based technical logbook, and integrated to ground based systems.',
      'TECHCAB',
      0,
      TO_DATE('2018-10-03', 'YYYY-MM-DD'),
      TO_DATE('2018-10-03', 'YYYY-MM-DD'),
      0,
      'MXI'
   FROM DUAL
   WHERE NOT EXISTS
      (
         SELECT 1
         FROM   ref_fault_log_type
         WHERE  fault_log_type_cd = 'TECHCAB'
      );
END;
/