--liquibase formatted sql


--changeSet DEV-1246:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************
 * Migrate all the eqp_part_price_breaks records to the eqp_part_vendor_price table
 ********************************************/
declare
  -- Local variables here
  CURSOR lcur_part_prices IS
  select 
    vendor_price.PART_VENDOR_PRICE_DB_ID,
    vendor_price.PART_NO_DB_ID,
    vendor_price.PART_NO_ID,
    vendor_price.VENDOR_DB_ID,
    vendor_price.VENDOR_ID,
    vendor_price.HIST_BOOL,
    price_break.UNIT_PRICE,
    vendor_price.CURRENCY_DB_ID,
    vendor_price.CURRENCY_CD,
    vendor_price.QTY_UNIT_DB_ID,
    vendor_price.QTY_UNIT_CD,
    vendor_price.LEAD_TIME,
    vendor_price.EFFECTIVE_FROM_DT,
    vendor_price.EFFECTIVE_TO_DT,
    vendor_price.DISCOUNT_PCT,
    vendor_price.STD_SALE_QT,
    price_break.MIN_QT,
    vendor_price.PRICE_TYPE_DB_ID,
    vendor_price.PRICE_TYPE_CD,
    vendor_price.VENDOR_NOTE,
    vendor_price.DOC_REF_SDESC,
    vendor_price.QUOTE_DT
  from 
    eqp_part_price_break price_break
    left join eqp_part_vendor_price vendor_price on price_break.part_vendor_price_db_id = vendor_price.part_vendor_price_db_id and 
                                            price_break.part_vendor_price_id = vendor_price.part_vendor_price_id;
  
lrec_part_price lcur_part_prices%rowtype;

BEGIN

  FOR lrec_part_price IN lcur_part_prices
    LOOP
             
     /* Add an eqp_part_vendor_price row for the price break with the new price id, unit price and min qty */
     INSERT INTO eqp_part_vendor_price (
       PART_VENDOR_PRICE_DB_ID,
       PART_VENDOR_PRICE_ID,
       PART_NO_DB_ID,
       PART_NO_ID,
       VENDOR_DB_ID,
       VENDOR_ID,
       HIST_BOOL,
       UNIT_PRICE,
       CURRENCY_DB_ID,
       CURRENCY_CD,
       QTY_UNIT_DB_ID,
       QTY_UNIT_CD,
       LEAD_TIME,
       EFFECTIVE_FROM_DT,
       EFFECTIVE_TO_DT,
       DISCOUNT_PCT,
       STD_SALE_QT,
       MIN_ORDER_QT,
       PRICE_TYPE_DB_ID,
       PRICE_TYPE_CD,
       VENDOR_NOTE,
       DOC_REF_SDESC,
       QUOTE_DT
     )
     VALUES (
       lrec_part_price.PART_VENDOR_PRICE_DB_ID,
       EQP_PART_VENDR_PRI_SEQ.nextval + 1,
       lrec_part_price.PART_NO_DB_ID,
       lrec_part_price.PART_NO_ID,
       lrec_part_price.VENDOR_DB_ID,
       lrec_part_price.VENDOR_ID,
       lrec_part_price.HIST_BOOL,
       lrec_part_price.UNIT_PRICE,
       lrec_part_price.CURRENCY_DB_ID,
       lrec_part_price.CURRENCY_CD,
       lrec_part_price.QTY_UNIT_DB_ID,
       lrec_part_price.QTY_UNIT_CD,
       lrec_part_price.LEAD_TIME,
       lrec_part_price.EFFECTIVE_FROM_DT,
       lrec_part_price.EFFECTIVE_TO_DT,
       lrec_part_price.DISCOUNT_PCT,
       lrec_part_price.STD_SALE_QT,
       lrec_part_price.MIN_QT,
       lrec_part_price.PRICE_TYPE_DB_ID,
       lrec_part_price.PRICE_TYPE_CD,
       lrec_part_price.VENDOR_NOTE,
       lrec_part_price.DOC_REF_SDESC,
       lrec_part_price.QUOTE_DT
     );
    
    END LOOP;
END;

/