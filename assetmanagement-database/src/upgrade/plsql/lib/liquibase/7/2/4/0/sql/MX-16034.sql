--liquibase formatted sql


--changeSet MX-16034:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE 

lNull VARCHAR2(10);

BEGIN 
 lNull := NULL;
 
  update_user_supply_locations(lNull, lNull, lNull, lNull, lNull, lNull);
END ;
/