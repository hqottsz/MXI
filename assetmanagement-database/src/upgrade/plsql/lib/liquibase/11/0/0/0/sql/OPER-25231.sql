--liquibase formatted sql

--changeset OPER-25231:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tables WHERE table_name = 'SD_FAULT_DEFERRAL_REQUEST';
BEGIN
   -- Enable parallel DML on sd_fault_reference
   utl_parallel_pkg.parallel_insert_begin('SD_FAULT_REFERENCE');

   -- Migrate deferral reference data from sd_fault_deferral_request into sd_fault_reference
   INSERT INTO sd_fault_reference
      (
         fault_ref_db_id,
         fault_ref_id,
         fault_db_id,
         fault_id,
         defer_ref_db_id,
         defer_ref_id,
         stage_reason_cd,
         stage_reason_db_id,
         notes,
         current_bool
       )
   SELECT
       Application_Object_Pkg.getdbid,
       sd_fault_reference_seq.nextval,
       sd_fault.fault_db_id,
       sd_fault.fault_id,
       fail_defer_ref.fail_defer_ref_db_id,
       fail_defer_ref.fail_defer_ref_id,
       sd_fault_deferral_request.stage_reason_cd,
       sd_fault_deferral_request.stage_reason_db_id,
       sd_fault_deferral_request.deferral_notes,
       1
   FROM sd_fault_deferral_request
       INNER JOIN sd_fault ON
          sd_fault_deferral_request.fault_id = sd_fault.alt_id
       INNER JOIN fail_defer_ref ON
          fail_defer_ref.alt_id = sd_fault_deferral_request.fail_defer_ref_id;

   -- Disable parallel DML on sd_fault_reference
   utl_parallel_pkg.parallel_insert_end('SD_FAULT_REFERENCE', TRUE);

END;
/

--changeset OPER-25231:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tables WHERE table_name = 'SD_FAULT_DEFERRAL_REQUEST';
BEGIN
   -- Enable parallel DML on sd_fault_reference_request
   utl_parallel_pkg.parallel_insert_begin('SD_FAULT_REFERENCE_REQUEST');

   -- Migrate deferral requests from sd_fault_deferral_request into sd_fault_reference_request
   INSERT INTO sd_fault_reference_request
      (
         fault_ref_req_db_id,
         fault_ref_req_id,
         hr_db_id,
         hr_id,
         request_status_cd,
         date_requested,
         date_resolved
      )
      SELECT
         sd_fault_reference.fault_ref_db_id,
         sd_fault_reference.fault_ref_id,
         org_hr.hr_db_id,
         org_hr.hr_id,
         sd_fault_deferral_request.deferral_request_status_cd,
         sd_fault_deferral_request.date_requested,
         NULL
      FROM
         sd_fault_deferral_request
         INNER JOIN org_hr ON
            org_hr.alt_id = sd_fault_deferral_request.hr_id
         INNER JOIN sd_fault ON
            sd_fault.alt_id = sd_fault_deferral_request.fault_id
         INNER JOIN sd_fault_reference ON
            sd_fault_reference.fault_db_id = sd_fault.fault_db_id AND
            sd_fault_reference.fault_id    = sd_fault.fault_id;

   -- Disable parallel DML on sd_fault_reference
   utl_parallel_pkg.parallel_insert_end('SD_FAULT_REFERENCE', TRUE);

END;
/

--changeset OPER-25231:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tables WHERE table_name = 'SD_FAULT_DEFERRAL_REQUEST';
-- Drop sd_fault_deferral_request table
BEGIN
   upg_migr_schema_v1_pkg.table_drop('SD_FAULT_DEFERRAL_REQUEST');
END;
/
