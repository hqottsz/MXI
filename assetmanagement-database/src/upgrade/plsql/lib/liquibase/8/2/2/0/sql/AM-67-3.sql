--liquibase formatted sql


--changeSet AM-67-3:1 stripComments:false
-- this was not added previously due the error in 0charge.sql base file
INSERT 
INTO
   charge (
      charge_id,
      charge_code,
      charge_name,
      recoverable_bool,
      archive_bool,
      rstat_cd,
      revision_no,
      ctrl_db_id,
      creation_dt,
      creation_db_id,
      revision_dt,
      revision_db_id,
      revision_user
   )
SELECT
  mx_key_pkg.new_uuid(),
  'BORROW',
  'BORROW CHARGES',
  0 AS recoverable_bool,
  0 AS archive_bool,
  0,
  0,
  0,
  SYSDATE,
  0,
  SYSDATE,
  0,
  USER
FROM
   dual
WHERE
   NOT EXISTS (
      SELECT
         1
      FROM
         charge
      WHERE
         charge_code = 'BORROW'
   );      

--changeSet AM-67-3:2 stripComments:false
-- this was not added previously due the error in 0dpo_row_filtering.sql base file
INSERT 
INTO
   dpo_row_filtering (
      table_name,
      filter_type,
      filter_sdesc,
      hierarchy_lvl,
      filter_extra_condition,
      parent_table_name,
      link_fk_constraint_name,
      parent_pk_constraint_name,
      rstat_cd,
      creation_dt,
      revision_dt,
      revision_db_id,
      revision_user
   )
SELECT
   'WARRANTY_DEFN_TASK',
   'ASSEMBLY',
   null,
   3,
   null,
   'TASK_TASK',
   'FK_TASKTASK_WARRANTYDEFNTASK',
   'PK_TASK_TASK',
   0,
   to_date('11-02-2009', 'dd-mm-yyyy'),
   to_date('11-02-2009', 'dd-mm-yyyy'),
   0,
   'MXI'
FROM
   dual
WHERE
   NOT EXISTS (
      SELECT
         1
      FROM
         dpo_row_filtering
      WHERE
         table_name  = 'WARRANTY_DEFN_TASK' AND
         filter_type = 'ASSEMBLY'
   );      