--liquibase formatted sql

--changeSet OPER-24977:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE FLIGHT_OUT_OF_SEQUENCE_PKG IS

PROCEDURE process_usage_data(
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id      IN INTEGERTABLE,
   aittb_assmbl_inv_id         IN INTEGERTABLE,
   aittb_data_type_db_id       IN INTEGERTABLE,
   aittb_data_type_id          IN INTEGERTABLE,
   aittb_new_delta             IN FLOATTABLE,
   aiv_system_note             IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
);

PROCEDURE process_usage_date_change (
   aid_new_usage_date          IN DATE,
   aib_date_outside_window     IN NUMBER,
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id      IN INTEGERTABLE,
   aittb_assmbl_inv_id         IN INTEGERTABLE,
   aittb_data_type_db_id       IN INTEGERTABLE,
   aittb_data_type_id          IN INTEGERTABLE,
   aittb_new_delta             IN FLOATTABLE,
   aiv_system_note             IN VARCHAR2,
   aiv_negating_system_note    IN VARCHAR2,
   aiv_ratify_system_note      IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
);

END FLIGHT_OUT_OF_SEQUENCE_PKG;
/