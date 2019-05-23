--liquibase formatted sql

--changeSet OPER-1502:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_drop('IX_FLLEGDISRUPT_NK');
END;
/

--changeSet OPER-1502:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_drop('IX_FLLEGFAILEFFECT_NK');
END;
/

--changeSet OPER-1502:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGDISRUPT_NK" ON "FL_LEG_DISRUPT" ("LEG_ID","DELAY_CODE_CD","DELAY_CODE_DB_ID")
');
END;
/

--changeSet OPER-1502:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGFAILEFFECT_NK" ON "FL_LEG_FAIL_EFFECT" ("LEG_ID","FAIL_EFFECT_TYPE_CD","FAIL_EFFECT_TYPE_DB_ID","FAIL_EFFECT_ID","FAIL_EFFECT_DB_ID","EFFECT_DT")
');
END;
/
