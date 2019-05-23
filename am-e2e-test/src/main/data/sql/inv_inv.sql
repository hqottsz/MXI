-- allow synchronization
UPDATE
   inv_inv
SET
   prevent_synch_bool = 0
WHERE
   (  inv_inv.inv_no_db_id,
      inv_inv.inv_no_id ) IN
   (
      SELECT
         inv_no_db_id,
         inv_no_id
      FROM
         inv_ac_reg
      WHERE
         ac_reg_cd IN ('RJIC-ACFT1','RREQ-ACFT1','OBSREQ-AC1','OBSREQ-AC2','STRAT-92','STRAT-119','STRAT-118')
   );
