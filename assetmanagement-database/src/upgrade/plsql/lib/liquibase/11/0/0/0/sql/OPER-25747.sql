--liquibase formatted sql


--changeSet OPER-25747:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_column_add('
		ALTER TABLE INV_XFER ADD (
			QTY_UNIT_DB_ID   NUMBER (10) NOT NULL DEFERRABLE ,
			QTY_UNIT_CD      VARCHAR2 (8) NOT NULL DEFERRABLE
		)
	');
END;
/

--changeSet OPER-25747:2 stripComments:false
  COMMENT ON COLUMN INV_XFER.QTY_UNIT_DB_ID
IS
  'Unit of measure for the transfer quantity. FK to REF_QTY_UNIT.' ;
  COMMENT ON COLUMN INV_XFER.QTY_UNIT_CD
IS
  'Unit of measure for the transfer quantity. FK to REF_QTY_UNIT.' ;


--changeSet OPER-25747:3 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'INV_XFER' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('INV_XFER');
  UPDATE
      (
		SELECT
			eqp_part_no.qty_unit_db_id epn_qty_unit_db_id,
			eqp_part_no.qty_unit_cd epn_qty_unit_cd,
			inv_xfer.qty_unit_db_id ix_qty_unit_db_id,
			inv_xfer.qty_unit_cd ix_qty_unit_cd
		FROM
			inv_xfer
			INNER JOIN inv_inv ON
				inv_inv.inv_no_db_id = inv_xfer.inv_no_db_id AND
				inv_inv.inv_no_id = inv_xfer.inv_no_id
			INNER JOIN eqp_part_no ON
				eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
				eqp_part_no.part_no_id = inv_inv.part_no_id
		WHERE
			inv_xfer.qty_unit_db_id IS NULL AND
			inv_xfer.qty_unit_cd IS NULL AND
			inv_xfer.inv_no_id IS NOT NULL AND
			inv_xfer.inv_no_db_id IS NOT NULL
	  )
	SET
		ix_qty_unit_db_id = epn_qty_unit_db_id,
		ix_qty_unit_cd = epn_qty_unit_cd;
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('INV_XFER', FALSE);
END;
/

--changeSet OPER-25747:4 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'INV_XFER' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('INV_XFER');
  UPDATE inv_xfer
  SET (inv_xfer.qty_unit_db_id, inv_xfer.qty_unit_cd) =
      (
		SELECT
			eqp_part_no.qty_unit_db_id,
			eqp_part_no.qty_unit_cd
		FROM
			evt_inv
			INNER JOIN inv_inv ON
				inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
				inv_inv.inv_no_id = evt_inv.inv_no_id
			INNER JOIN eqp_part_no ON
				eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
				eqp_part_no.part_no_id = inv_inv.part_no_id
		WHERE
			evt_inv.event_db_id = inv_xfer.init_event_db_id AND
			evt_inv.event_id = inv_xfer.init_event_id
	  )
	  WHERE
		inv_xfer.qty_unit_db_id IS NULL AND
		inv_xfer.qty_unit_cd IS NULL AND
		inv_xfer.init_event_id IS NOT NULL AND
		inv_xfer.init_event_db_id IS NOT NULL AND
		inv_xfer.inv_no_id IS NULL AND
		inv_xfer.inv_no_db_id IS NULL;
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('INV_XFER', FALSE);
END;
/

--changeSet OPER-25747:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add('
		ALTER TABLE INV_XFER ADD CONSTRAINT FK_REFQTYUNIT_INVXFER FOREIGN KEY ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) REFERENCES REF_QTY_UNIT ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) DEFERRABLE
	');
END;
/

--changeSet OPER-25747:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_UOM_INVXFER
/********************************************************************************
*
* Trigger:    TIBR_UOM_INVXFER
*
* Description:  Before insertion of rows in inv_xfer table, the unit_qty_cd and unit_qty_db_id columns will be checked
*					for null and if the columns are null, UOM from eqp_part_no table will be copied to the columns.
*
********************************************************************************/
BEFORE INSERT ON INV_XFER
FOR EACH ROW
WHEN (new.qty_unit_cd IS NULL OR new.qty_unit_db_id IS NULL)
BEGIN
	IF :new.inv_no_id IS NOT NULL THEN
		SELECT
			eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
		INTO
			:new.qty_unit_db_id, :new.qty_unit_cd
		FROM
			eqp_part_no
			INNER JOIN inv_inv ON
				eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
				eqp_part_no.part_no_id = inv_inv.part_no_id
		WHERE
			inv_inv.inv_no_db_id = :new.inv_no_db_id AND
			inv_inv.inv_no_id = :new.inv_no_id;
	ELSIF :new.init_event_id IS NOT NULL THEN
		SELECT
			eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
		INTO
			:new.qty_unit_db_id, :new.qty_unit_cd
		FROM
			evt_inv
			INNER JOIN inv_inv ON
				evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
				evt_inv.inv_no_id = inv_inv.inv_no_id
			INNER JOIN eqp_part_no ON
				eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
				eqp_part_no.part_no_id = inv_inv.part_no_id
		WHERE
			evt_inv.event_db_id = :new.init_event_db_id AND
			evt_inv.event_id = :new.init_event_id;
	END IF;
END;
/