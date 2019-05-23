--liquibase formatted sql


--changeSet opr_rbl_flight:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_drop('OPR_RBL_FLIGHT', 'PK_OPR_RBL_FLIGHT'); 
END;
/

--changeSet opr_rbl_flight:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_drop('PK_OPR_RBL_FLIGHT');
END;
/

--changeSet opr_rbl_flight:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      alter table opr_rbl_flight add constraint PK_OPR_RBL_FLIGHT primary key (FLIGHT_ID, FLIGHT_NUMBER, SCHEDULED_DEPARTURE_DATE, DEPARTURE_AIRPORT)
   '); 
END;
/

--changeSet opr_rbl_flight:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table OPR_RBL_FLIGHT modify (
       SCHEDULED_DEPARTURE_DATE Date NOT NULL 
   )
   ');
END;
/