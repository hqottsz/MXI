--liquibase formatted sql


--changeSet 0ref_qty_unit:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_QTY_UNIT"
** 0-Level
** DATE: 09/22/2004 TIME: 00:00:00
*********************************************/
insert into REF_QTY_UNIT (QTY_UNIT_DB_ID, QTY_UNIT_CD, DESC_SDESC, DESC_LDESC, DECIMAL_PLACES_QT, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'EA', 'Each', 'Quantity units for inventory that is tracked by individual item', 0, null, null, 0, to_date('01-10-2004', 'dd-mm-yyyy'), to_date('01-10-2004', 'dd-mm-yyyy'), 0, 'kramer_master');