/********************************************
** INSERT SCRIPT FOR TABLE "REF_PART_USE"
** 10-Level
** DATE: 26-NOV-02 TIME: 16:09:03
*********************************************/
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CONSUM', 0, 147,  'Consumables', 'Items that are NOT in the IPC but are used to service an aircraft. Fuel, lint-free cloths, etc are examples',  0, '18-APR-02', '18-APR-02', 100, 'MXI');
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'MODKIT', 0, 1,  'MOD KIT', 'An item that is itself never installed on an aircraft; it is decomposed into constituent part numbers that are in turn installed; it is purely used to BUY collections of items',  0, '30-OCT-02', '30-OCT-02', 100, 'MXI');
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'FLYKIT', 0, 1,  'FLY AWAY KIT', 'An item that is installed on an aircraft that CONTAINS other items; you can extract stuff from the kit to install on an aircraft',  0, '30-OCT-02', '30-OCT-02', 100, 'MXI');
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'BULK', 0, 55,  'Bulk', 'Items that are consumables but are also unit of measure controlled rather than integer quantities.',  0, '30-OCT-02', '30-OCT-02', 100, 'MXI');
insert into ref_part_use (part_use_db_id, part_use_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'SHPCNT', 0, 114,  'Shipping Container', 'Use for any parts that are purely used in the process of shipping (ex. Module shipping container).',  0, '30-OCT-02', '30-OCT-02', 100, 'MXI');
