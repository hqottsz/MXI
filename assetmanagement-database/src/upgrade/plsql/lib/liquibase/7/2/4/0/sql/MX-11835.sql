--liquibase formatted sql


--changeSet MX-11835:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_VENDOR_PRICE add (
	"CURRENCY_DB_ID" Number(10,0)
)
');
END;
/

--changeSet MX-11835:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table EQP_PART_VENDOR_PRICE add (
	"CURRENCY_CD" Varchar2 (8)
)
');
END;
/

--changeSet MX-11835:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "EQP_PART_VENDOR_PRICE" add Constraint "FK_REFCURRNCY_EQPPRTVENDR" foreign key ("CURRENCY_DB_ID","CURRENCY_CD") references "REF_CURRENCY" ("CURRENCY_DB_ID","CURRENCY_CD")  DEFERRABLE
');
END;
/

--changeSet MX-11835:4 stripComments:false
-- Update the currency from a quote if posible
UPDATE
   eqp_part_vendor_price
SET
   (eqp_part_vendor_price.currency_db_id,
    eqp_part_vendor_price.currency_cd)
   = (
      SELECT
         rfq_header.currency_db_id,
         rfq_header.currency_cd
      FROM
         rfq_header
      WHERE
         EXISTS (
            SELECT
               1
            FROM
               rfq_line_vendor
            WHERE
               rfq_header.rfq_db_id = rfq_line_vendor.rfq_db_id AND
               rfq_header.rfq_id    = rfq_line_vendor.rfq_id
               AND
               rfq_line_vendor.part_vendor_price_db_id = eqp_part_vendor_price.part_vendor_price_db_id AND
               rfq_line_vendor.part_vendor_price_id    = eqp_part_vendor_price.part_vendor_price_id
         )
   )
WHERE
   (
      eqp_part_vendor_price.currency_cd IS NULL OR
      eqp_part_vendor_price.currency_db_id IS NULL
   )
   AND
   EXISTS (
      SELECT
         1
      FROM
         rfq_line_vendor
      WHERE
         rfq_line_vendor.part_vendor_price_db_id = eqp_part_vendor_price.part_vendor_price_db_id AND
         rfq_line_vendor.part_vendor_price_id    = eqp_part_vendor_price.part_vendor_price_id
   )
;

--changeSet MX-11835:5 stripComments:false
-- If the vendor price still has no currency, use the vendor's default currency
UPDATE 
   eqp_part_vendor_price
SET
   (eqp_part_vendor_price.currency_db_id,
    eqp_part_vendor_price.currency_cd)
   = (
      SELECT
         org_vendor.currency_db_id,
         org_vendor.currency_cd
      FROM
         org_vendor
      WHERE
         org_vendor.vendor_db_id = eqp_part_vendor_price.vendor_db_id AND
         org_vendor.vendor_id    = eqp_part_vendor_price.vendor_id
     )
WHERE
   eqp_part_vendor_price.currency_cd IS NULL OR
   eqp_part_vendor_price.currency_db_id IS NULL
;

--changeSet MX-11835:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_PART_VENDOR_PRICE modify (
   "CURRENCY_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CURRENCY_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-11835:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table EQP_PART_VENDOR_PRICE modify (
   "CURRENCY_CD" Varchar2 (8) NOT NULL DEFERRABLE
)
');
END;
/