--liquibase formatted sql

--changeSet OPER-9933:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE FLIGHT_OUT_OF_SEQUENCE_PKG IS

PROCEDURE process_usage_data(
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id      IN VARCHAR2TABLE,
   aittb_assmbl_inv_id         IN VARCHAR2TABLE,
   aittb_data_type_db_id       IN VARCHAR2TABLE,
   aittb_data_type_id          IN VARCHAR2TABLE,
   aittb_new_delta             IN FLOATTABLE,
   aiv_system_note             IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
);


END FLIGHT_OUT_OF_SEQUENCE_PKG;
/
