--liquibase formatted sql


--changeSet OPER-25772:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_column_add('
		ALTER TABLE REQ_PART ADD (
			QTY_UNIT_DB_ID     NUMBER (10) ,
    		QTY_UNIT_CD        VARCHAR2 (8)
		)
	');
END;
/

--changeset OPER-25772:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment drop not null constraints from columns REQ_PART.QTY_UNIT_DB_ID and REQ_PART.QTY_UNIT_CD if any exist
BEGIN
   upg_migr_schema_v1_pkg.table_column_cons_nn_drop('REQ_PART', 'QTY_UNIT_DB_ID');
   upg_migr_schema_v1_pkg.table_column_cons_nn_drop('REQ_PART', 'QTY_UNIT_CD');
END;
/

--changeSet OPER-25772:3 stripComments:false
  COMMENT ON COLUMN REQ_PART.QTY_UNIT_DB_ID
IS
  'The db_id of the unit of measure for the part request quantity.' ;
  COMMENT ON COLUMN REQ_PART.QTY_UNIT_CD
IS
  'The code of the unit of measure for the part request quantity' ;

--changeSet OPER-25772:4 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--fetches unit of measure for part requests associated with a part
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'REQ_PART' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('REQ_PART');

  UPDATE (
   SELECT
      req_part.qty_unit_db_id	 pr_qty_unit_db_id,
      req_part.qty_unit_cd 		 pr_qty_unit_cd,
      eqp_part_no.qty_unit_db_id pt_qty_unit_db_id,
      eqp_part_no.qty_unit_cd 	 pt_qty_unit_cd
   FROM
      req_part
      INNER JOIN eqp_part_no ON
         eqp_part_no.part_no_db_id = req_part.req_spec_part_no_db_id AND
         eqp_part_no.part_no_id = req_part.req_spec_part_no_id
   WHERE
      req_part.qty_unit_db_id IS NULL AND
      req_part.qty_unit_cd IS NULL
   )
   SET
      pr_qty_unit_db_id = pt_qty_unit_db_id ,
      pr_qty_unit_cd = pt_qty_unit_cd;
   UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('REQ_PART', FALSE);
END;
/

--changeSet OPER-25772:5 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--fetches unit of measure for part requests associated with stock which have neither part or part group
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'REQ_PART' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('REQ_PART');

  UPDATE (
   SELECT
      req_part.qty_unit_db_id 	 pr_qty_unit_db_id,
      req_part.qty_unit_cd 	     pr_qty_unit_cd,
      eqp_stock_no.qty_unit_db_id stock_qty_unit_db_id,
      eqp_stock_no.qty_unit_cd 	  stock_qty_unit_cd
   FROM
      req_part
      INNER JOIN eqp_stock_no ON
         eqp_stock_no.stock_no_db_id = req_part.req_stock_no_db_id AND
         eqp_stock_no.stock_no_id = req_part.req_stock_no_id
   WHERE
      req_part.qty_unit_db_id IS NULL AND
      req_part.qty_unit_cd IS NULL AND
      req_part.req_spec_part_no_db_id IS NULL AND
      req_part.req_bom_part_db_id IS NULL
   )
   SET
      pr_qty_unit_db_id = stock_qty_unit_db_id ,
      pr_qty_unit_cd = stock_qty_unit_cd;
   UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('REQ_PART', FALSE);
END;
/

--changeSet OPER-25772:6 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--fetches unit of measure for part requests associated with a part group but not a part - uom is the standard uom of the standard part in the part group
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'REQ_PART' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('REQ_PART');
UPDATE req_part
SET (qty_unit_db_id, qty_unit_cd) =
(
SELECT
      eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
FROM
    eqp_part_no
      JOIN eqp_part_baseline ON
         eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
         eqp_part_no.part_no_id = eqp_part_baseline.part_no_id AND
         eqp_part_no.rstat_cd = 0
WHERE
         eqp_part_baseline.bom_part_db_id = req_part.req_bom_part_db_id AND
         eqp_part_baseline.bom_part_id= req_part.req_bom_part_id AND
         eqp_part_baseline.standard_bool = 1
)
WHERE
      req_part.rstat_cd = 0 and
      req_part.qty_unit_db_id IS NULL AND
      req_part.qty_unit_cd IS NULL AND
      req_part.req_spec_part_no_db_id IS NULL AND
      req_part.req_spec_part_no_id IS NULL;
 UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('REQ_PART', FALSE);
END;
/


--changeSet OPER-25772:7 stripComments:false failOnError:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--sets the standard unit of measure of all remaining part requests to EA (each)
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM user_tab_columns WHERE table_name = 'REQ_PART' AND column_name = 'QTY_UNIT_CD';
BEGIN
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_BEGIN('REQ_PART');
UPDATE req_part
SET qty_unit_db_id = 0,
	qty_unit_cd = 'EA'
WHERE
	qty_unit_db_id IS NULL AND
	qty_unit_cd IS NULL;
  UPG_PARALLEL_V1_PKG.PARALLEL_UPDATE_END('REQ_PART', FALSE);
END;
/

--changeSet OPER-25772:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_constraint_add('
		ALTER TABLE REQ_PART ADD CONSTRAINT FK_REFQTYUNIT_REQPART FOREIGN KEY ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) REFERENCES REF_QTY_UNIT ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) NOT DEFERRABLE
	');
END;
/


--changeSet OPER-25772:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment trigger for req_part to fetch and store the unit of measure of the part when new records are added
CREATE OR REPLACE TRIGGER TIBR_UOM_REQPART
/********************************************************************************
*
* Trigger:    TIBR_UOM_REQPART
*
* Description:  This is a ROW based trigger.
* When a new row is added to the req_part table:
* 		1. If the new row is associated with a specific part
* 			the uom of the row is updated to the standard unit of measure of the requested part from eqp_part_no.
* 		2. If the new row has an associated part group, but not a specific part,
* 			the uom of the row is updated to the standard unit of measure of the preferred part in the part group by joining eqp_part_baseline and eqp_bom_part.
* 		3. If the new request is a stock request with neither a specific part or part group,
* 			the uom of the row is updated to the uom of the stock from eqp_stock_no.
*
* *****************************************************************************/
BEFORE INSERT ON REQ_PART
FOR EACH ROW
BEGIN
	-- Do not assign a default UoM when a Qty is not provided
	IF (:new.qty_unit_cd IS NULL AND :new.req_qt IS NOT NULL) THEN
		IF (:new.req_spec_part_no_db_id IS NOT NULL AND :new.req_spec_part_no_id IS NOT NULL) THEN
			SELECT
				eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
			INTO
				:new.qty_unit_db_id, :new.qty_unit_cd
			FROM eqp_part_no
			WHERE
				eqp_part_no.part_no_db_id = :new.req_spec_part_no_db_id AND
				eqp_part_no.part_no_id = :new.req_spec_part_no_id;
		ELSIF (:new.req_bom_part_db_id IS NOT NULL AND :new.req_bom_part_id IS NOT NULL) THEN
			SELECT
				eqp_part_no.qty_unit_db_id, eqp_part_no.qty_unit_cd
			INTO
				:new.qty_unit_db_id, :new.qty_unit_cd
			FROM eqp_part_no
			INNER JOIN eqp_part_baseline ON
				eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
				eqp_part_no.part_no_id = eqp_part_baseline.part_no_id
			WHERE
				eqp_part_baseline.bom_part_db_id = :new.req_bom_part_db_id AND
				eqp_part_baseline.bom_part_id = :new.req_bom_part_id AND
				eqp_part_baseline.standard_bool = 1 AND
				eqp_part_no.rstat_cd = 0;
		ELSIF (:new.req_stock_no_db_id IS NOT NULL AND :new.req_stock_no_id IS NOT NULL) THEN
			SELECT
				eqp_stock_no.qty_unit_db_id, eqp_stock_no.qty_unit_cd
			INTO
				:new.qty_unit_db_id, :new.qty_unit_cd
			FROM
				eqp_stock_no
			WHERE
				eqp_stock_no.stock_no_db_id = :new.req_stock_no_db_id AND
				eqp_stock_no.stock_no_id = :new.req_stock_no_id;
		ELSE
			-- Qty exists however a UoM is not available; default to EACH
			:new.qty_unit_db_id  := 0;
			:new.qty_unit_cd := 'EA';
		END IF;
	END IF;
END;
/

--changeSet OPER-25772:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
   UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('getQtyUnitCdForReqPart');
END;
/

--changeSet OPER-25772:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*
BEGIN
	UTL_MIGR_SCHEMA_PKG.FUNCTION_DROP('getDecimalPlacesQtForReqPart');
END;
/