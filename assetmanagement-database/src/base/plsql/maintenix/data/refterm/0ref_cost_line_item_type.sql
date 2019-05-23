--liquibase formatted sql


--changeSet 0ref_cost_line_item_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "ref_cost_line_item_type"
** 0-Level
** DATE: 12/31/2007 TIME: 00:00:00
*********************************************/
insert into ref_cost_line_item_type (COST_LINE_ITEM_TYPE_DB_ID, COST_LINE_ITEM_TYPE_CD,PAYABLE_BOOL,DESC_SDESC,DESC_LDESC,USER_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values ( 0 , 'LABOUR' , 1 , 'Shop Labour' , 'This is the Labour for Shop Purpose' , 'LABOUR', 0, '22-JAN-08', '22-JAN-08', 100, 'MXI');

--changeSet 0ref_cost_line_item_type:2 stripComments:false
insert into ref_cost_line_item_type (COST_LINE_ITEM_TYPE_DB_ID, COST_LINE_ITEM_TYPE_CD,PAYABLE_BOOL,DESC_SDESC,DESC_LDESC,USER_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values ( 0 , 'MATPAY' , 1 , 'Material - Payable' , 'Material for which we have to pay' , 'MATPAY', 0, '22-JAN-08', '22-JAN-08', 100, 'MXI');

--changeSet 0ref_cost_line_item_type:3 stripComments:false
insert into ref_cost_line_item_type (COST_LINE_ITEM_TYPE_DB_ID, COST_LINE_ITEM_TYPE_CD,PAYABLE_BOOL,DESC_SDESC,DESC_LDESC,USER_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values ( 0 , 'MATREC' , 0 , 'Material - Receivable' , 'Material for which we have to Receive Money', 'MATREC', 0, '22-JAN-08', '22-JAN-08', 100, 'MXI');