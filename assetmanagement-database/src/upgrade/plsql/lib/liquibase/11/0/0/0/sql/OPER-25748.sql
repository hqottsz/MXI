--liquibase formatted sql

--changeSet OPER-25748:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE ship_shipment_line ADD (
         qty_unit_db_id NUMBER (10) NOT NULL DEFERRABLE
      )
   ');
END;
/

--changeSet OPER-25748:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE ship_shipment_line ADD (
         qty_unit_cd VARCHAR2 (8) NOT NULL DEFERRABLE
      )
   ');
END;
/

--changeSet OPER-25748:3
  COMMENT ON COLUMN SHIP_SHIPMENT_LINE.QTY_UNIT_DB_ID
IS
  'Column to hold Units of measure that are used to describe inventory quantities for movement. FK to REF_QTY_UNIT.' ;
  COMMENT ON COLUMN SHIP_SHIPMENT_LINE.QTY_UNIT_CD
IS
  'Column to hold Units of measure that are used to describe inventory quantities for movement. FK to REF_QTY_UNIT.' ;

--changeSet OPER-25748:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on ship_shipment_line that fills unit of measure columns using part standard unit of measure value
CREATE OR REPLACE TRIGGER TIUBR_UOM_SHIPLINE
/********************************************************************************
*
* Trigger:    TIUBR_UOM_SHIPLINE
*
* Description: This is a TABLE based trigger. When inserting values to ship_shipment_line table,
* if the new qty_unit_cd, qty_unit_db_id value is null, or if the shipment line part changes, then this trigger updates those columns by fetching standard
* unit of measure eqp_part_no
********************************************************************************/
BEFORE INSERT OR UPDATE ON SHIP_SHIPMENT_LINE
FOR EACH ROW

DECLARE
BEGIN

  IF :new.qty_unit_cd is null OR (:new.part_no_db_id||':'|| :new.part_no_id != :old.part_no_db_id||':'||:old.part_no_id ) THEN
    SELECT
    eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
    INTO
    :new.qty_unit_db_id, :new.qty_unit_cd
    FROM
    eqp_part_no
    WHERE
    eqp_part_no.part_no_db_id = :new.part_no_db_id AND
    eqp_part_no.part_no_id = :new.part_no_id;
  END IF;
END;
/

--changeSet OPER-25748:5 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'SHIP_SHIPMENT_LINE' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('SHIP_SHIPMENT_LINE');

UPDATE (
SELECT
	ship_shipment_line.qty_unit_db_id ssl_qty_unit_db_id,
	ship_shipment_line.qty_unit_cd ssl_qty_unit_cd,
	eqp_part_no.qty_unit_db_id pt_qty_unit_db_id,
	eqp_part_no.qty_unit_cd pt_qty_unit_cd
FROM
	ship_shipment_line
INNER JOIN eqp_part_no ON
	eqp_part_no.part_no_db_id = ship_shipment_line.part_no_db_id AND
	eqp_part_no.part_no_id = ship_shipment_line.part_no_id
WHERE
	ship_shipment_line.qty_unit_db_id IS NULL AND
	ship_shipment_line.qty_unit_cd IS NULL
)
SET
	ssl_qty_unit_db_id = pt_qty_unit_db_id , ssl_qty_unit_cd  = pt_qty_unit_cd;

  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('SHIP_SHIPMENT_LINE', TRUE);
END;
/

--changeSet OPER-25748:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE SHIP_SHIPMENT_LINE ADD CONSTRAINT FK_REF_QTY_UNIT_SHIPMENTLINE FOREIGN KEY ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) REFERENCES REF_QTY_UNIT ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) DEFERRABLE');
END;
/
