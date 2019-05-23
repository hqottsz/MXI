/********************************************
** INSERT SCRIPT FOR TABLE "REF_PO_AUTH_LVL"
** 10-Level
** DATE: 16-MARCH-2005 TIME: 00:00:00
*********************************************/

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'P_POAGNT', 10, 'PURCHASE', 'Purchasing Agent', 'Requires a purchasing agent to authorize this PO.', 'POAGENT', 1000, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'P_POMGR', 10, 'PURCHASE', 'Purchasing Manager', 'Requires a purchasing manager to authorize this PO.', 'POMGR', 5000, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'P_FINANC', 10, 'PURCHASE', 'Finance Manager', 'Requires a finance manager to authorize this PO.', 'FINANCE', 25000, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'P_CEO', 10, 'PURCHASE', 'CEO', 'Requires a CEO to authorize this PO..', 'CEO', 99999999, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'R_POAGNT', 10, 'REPAIR', 'Purchasing Agent', 'Requires a purchasing agent to authorize this PO.', 'POAGENT', 1000, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'R_POMGR', 10, 'REPAIR', 'Purchasing Manager', 'Requires a purchasing manager to authorize this PO.', 'POMGR', 5000, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'R_FINANC', 10, 'REPAIR', 'Finance Manager', 'Requires a finance manager to authorize this PO.', 'FINANCE', 25000, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'R_CEO', 10, 'REPAIR', 'CEO', 'Requires a CEO to authorize this PO..', 'CEO', 99999999, 0, '6-JUN-06', '6-JUN-06', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'E_POAGNT', 10, 'EXCHANGE', 'Purchasing Agent', 'Requires a purchasing agent to authorize this EO.', 'POAGENT', 1000, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'E_POMGR', 10, 'EXCHANGE', 'Purchasing Manager', 'Requires a purchasing manager to authorize this EO.', 'POMGR', 5000, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'E_FINANC', 10, 'EXCHANGE', 'Finance Manager', 'Requires a finance manager to authorize this EO.', 'FINANCE', 25000, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'E_CEO', 10, 'EXCHANGE', 'CEO', 'Requires a CEO to authorize this EO..', 'CEO', 99999999, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'B_POAGNT', 10, 'BORROW', 'Purchasing Agent', 'Requires a purchasing agent to authorize this BO.', 'POAGENT', 1000, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'B_POMGR', 10, 'BORROW', 'Purchasing Manager', 'Requires a purchasing manager to authorize this BO.', 'POMGR', 5000, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'B_FINANC', 10, 'BORROW', 'Finance Manager', 'Requires a finance manager to authorize this BO.', 'FINANCE', 25000, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into ref_po_auth_lvl( PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'B_CEO', 10, 'BORROW', 'CEO', 'Requires a CEO to authorize this BO..', 'CEO', 99999999, 0, '13-APR-07', '13-APR-07', 0, 'MXI');

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD)
values (10, 'C_CEO', 10, 'CONSIGN', 'CEO', 'Requires a CEO to authorize this Consignment Order..', 'CEO', 99999999, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD)
values (10, 'C_FINANC', 10, 'CONSIGN', 'Finance Manager', 'Requires a finance manager to authorize this Consignment Order.', 'FINANCE', 25000, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD) 
values (10, 'C_POMGR', 10, 'CONSIGN', 'Purchasing Manager', 'Requires a purchasing manager to authorize this Consignment Order.', 'POMGR', 5000, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD)
values (10, 'C_POAGNT', 10, 'CONSIGN', 'Purchasing Agent', 'Requires a purchasing agent to authorize this Consignment Order.', 'POAGENT', 1000, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD)
values (10, 'CE_CEO', 10, 'CSGNXCHG', 'CEO', 'Requires a CEO to authorize this Consignment Exchange Order..', 'CEO', 99999999, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD)
values (10, 'CE_FINANC', 10, 'CSGNXCHG', 'Finance Manager', 'Requires a finance manager to authorize this Consignment Exchange Order.', 'FINANCE', 25000, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD) 
values (10, 'CE_POMGR', 10, 'CSGNXCHG', 'Purchasing Manager', 'Requires a purchasing manager to authorize this Consignment Exchange Order.', 'POMGR', 5000, 0);

insert into REF_PO_AUTH_LVL (PO_AUTH_LVL_DB_ID, PO_AUTH_LVL_CD, PO_AUTH_FLOW_DB_ID, PO_AUTH_FLOW_CD, DESC_SDESC, DESC_LDESC, USER_CD, LIMIT_PRICE, RSTAT_CD)
values (10, 'CE_POAGNT', 10, 'CSGNXCHG', 'Purchasing Agent', 'Requires a purchasing agent to authorize this Consignment Exchange Order.', 'POAGENT', 1000, 0);
