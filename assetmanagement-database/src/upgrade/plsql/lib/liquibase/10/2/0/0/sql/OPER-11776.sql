--liquibase formatted sql

--changeSet OPER-11776:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE SCHED_LABOUR_ROLE MODIFY (
                    SCHED_HR NUMBER(9,5)
                 )
    ');
END;
/
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE SCHED_LABOUR_ROLE MODIFY (
                    ACTUAL_HR NUMBER(9,5)
                 )
    ');
END;
/
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE SCHED_LABOUR_ROLE MODIFY (
                    ADJUSTED_BILLING_HR NUMBER(9,5)
                 )
    ');
END;
/

--changeSet OPER-11776:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE SCHED_LABOUR MODIFY (
                    REM_HR NUMBER(9,5)
                 )
    ');
END;
/

--changeSet OPER-11776:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE TASK_LABOUR_LIST MODIFY (
                    INSP_HR NUMBER(9,5)
                 )
    ');
END;
/

BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE TASK_LABOUR_LIST MODIFY (
                    CERT_HR NUMBER(9,5)
                 )
    ');
END;
/

BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE TASK_LABOUR_LIST MODIFY (
                    WORK_PERF_HR NUMBER(9,5)
                 )
    ');
END;
/

--changeSet OPER-11776:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE DWT_TASK_LABOUR_SUMMARY MODIFY (
                    ACTUAL_TOTAL_MAN_HR NUMBER(9,5)
                 )
    ');
END;
/

BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE DWT_TASK_LABOUR_SUMMARY MODIFY (
                    SCHED_MAN_HR NUMBER(9,5)
                 )
    ');
END;
/

--changeSet OPER-11776:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE EVT_TOOL MODIFY (
                    ACTUAL_HR NUMBER(9,5)
                 )
    ');
END;
/

BEGIN
    utl_migr_schema_pkg.table_column_modify('
            ALTER TABLE EVT_TOOL MODIFY (
                    SCHED_HR NUMBER(9,5)
                 )
    ');
END;
/