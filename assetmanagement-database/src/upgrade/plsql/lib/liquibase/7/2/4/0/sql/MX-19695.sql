--liquibase formatted sql


--changeSet MX-19695:1 stripComments:false
UPDATE utl_sequence SET next_value = 200000 WHERE sequence_cd = 'UTL_MENU_ITEM_ID';

--changeSet MX-19695:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('UTL_MENU_ITEM_ID');
END;
/

--changeSet MX-19695:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('UTL_MENU_ITEM_ID', 200000);
END;
/

--changeSet MX-19695:4 stripComments:false
UPDATE utl_sequence SET next_value = 200000 WHERE sequence_cd = 'TRIGGER_ID_SEQ';

--changeSet MX-19695:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('TRIGGER_ID_SEQ');
END;
/

--changeSet MX-19695:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('TRIGGER_ID_SEQ', 200000);
END;
/