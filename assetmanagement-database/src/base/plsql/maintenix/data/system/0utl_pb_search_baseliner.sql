--liquibase formatted sql


--changeSet 0utl_pb_search_baseliner:1 stripComments:false
insert into UTL_PB_SEARCH (APP_CD, SEARCH_NAME, OUTPUT_DW, SEARCH_VALUE_NAME, SEARCH_COL_NAME, TARGET_WINDOW, WINDOW_CONTEXT, WINDOW_COLS, TARGET_CONTROL, CONTROL_COLS, UTL_ID)
values ('BL', 'Assembly ', 'd_search_assmbl_cd', 'Code:', 'assmbl_cd', 'w_s_asmcat', 'SEARCH', 'assmbl_db_id@@@assmbl_cd', 'dw_assmbl', 'assmbl_db_id@@@assmbl_cd',0);

--changeSet 0utl_pb_search_baseliner:2 stripComments:false
insert into UTL_PB_SEARCH (APP_CD, SEARCH_NAME, OUTPUT_DW, SEARCH_VALUE_NAME, SEARCH_COL_NAME, TARGET_WINDOW, WINDOW_CONTEXT, WINDOW_COLS, TARGET_CONTROL, CONTROL_COLS, UTL_ID)
values ('BL', 'BOM', 'd_search_assmbl_bom_cd', 'BOM Code:', 'assmbl_bom_cd', 'w_s_assmbl_explorer', 'SEARCH', 'assmbl_db_id@@@assmbl_cd@@@assmbl_bom_id', 'tv_bom', 'assmbl_db_Id@@@assmbl_cd@@@assmbl_bom_id',0);