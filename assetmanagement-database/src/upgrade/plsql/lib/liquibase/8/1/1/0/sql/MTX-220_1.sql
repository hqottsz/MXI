--liquibase formatted sql


--changeSet MTX-220_1:1 stripComments:false
/** ARC MESSAGE RECORDS PURGING**/
INSERT INTO UTL_PURGE_GROUP
  (purge_group_cd,
   group_name,
   group_ldesc,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC',
       'ARC Message Records Purging',
       'A set of purging policies related to removing the data from the ARC tables',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_GROUP
                  WHERE purge_group_cd = 'ARC'
                 );

--changeSet MTX-220_1:2 stripComments:false
INSERT INTO UTL_PURGE_POLICY
  (purge_policy_cd,
  purge_group_cd,
  policy_name,
  policy_ldesc,
  retention_period,
  active_bool,
  utl_id,
  rstat_cd,
  creation_dt,
  revision_dt,
  revision_db_id,
  revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC',
       'ARC message records',
       'Purges the message records that exceed the retention period',
       90,
       1,
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_POLICY
                  WHERE purge_policy_cd = 'ARC_RECORDS'
                 );

--changeSet MTX-220_1:3 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RESULT',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_RESULT'
                 );

--changeSet MTX-220_1:4 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_CONFIG',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_CONFIG'
                 );

--changeSet MTX-220_1:5 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_INV_DETAILS',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_INV_DETAILS'
                 );

--changeSet MTX-220_1:6 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_TASK_USAGE',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_TASK_USAGE'
                 );

--changeSet MTX-220_1:7 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_TASK',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_TASK'
                 );

--changeSet MTX-220_1:8 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_FAULT_USAGE',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_FAULT_USAGE'
                 );

--changeSet MTX-220_1:9 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_FAULT_DUE_VALUE',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_FAULT_DUE_VALUE'
                 );

--changeSet MTX-220_1:10 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_FAULT',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_FAULT'
                 );

--changeSet MTX-220_1:11 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_INV_USAGE',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_INV_USAGE'
                 );

--changeSet MTX-220_1:12 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_INV_MAP',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_INV_MAP'
                 );

--changeSet MTX-220_1:13 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_ASSET',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_ASSET'
                 );

--changeSet MTX-220_1:14 stripComments:false
INSERT INTO UTL_PURGE_TABLE
  (purge_table_cd,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_MESSAGE',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_TABLE
                  WHERE purge_table_cd = 'ARC_MESSAGE'
                 );

--changeSet MTX-220_1:15 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_RESULT',
       10,
       'ARC_RESULT.MSG_ID IN 
        (SELECT ARC_MESSAGE.ID 
         FROM ARC_MESSAGE 
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_RESULT'
                 );

--changeSet MTX-220_1:16 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_CONFIG',
       20,
       'ARC_CONFIG.ASSET_ID IN 
        (SELECT ARC_ASSET.ID 
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON 
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_CONFIG'
                 );

--changeSet MTX-220_1:17 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_INV_DETAILS',
       30,
       'ARC_INV_DETAILS.ASSET_ID IN 
        (SELECT ARC_ASSET.ID 
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON 
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_INV_DETAILS'
                 );

--changeSet MTX-220_1:18 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_TASK_USAGE',
       40,
       'ARC_TASK_USAGE.TASK_ID IN 
        (SELECT ARC_TASK.ID 
         FROM ARC_TASK
            INNER JOIN ARC_ASSET ON
               ARC_ASSET.ID = ARC_TASK.ASSET_ID
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_TASK_USAGE'
                 );

--changeSet MTX-220_1:19 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_TASK',
       50,
       'ARC_TASK.ASSET_ID IN 
        (SELECT ARC_ASSET.ID 
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON 
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_TASK'
                 );

--changeSet MTX-220_1:20 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_FAULT_USAGE',
       60,
       'ARC_FAULT_USAGE.FAULT_ID IN 
        (SELECT ARC_FAULT.ID 
         FROM ARC_FAULT
            INNER JOIN ARC_ASSET ON
               ARC_ASSET.ID = ARC_FAULT.ASSET_ID
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_FAULT_USAGE'
                 );

--changeSet MTX-220_1:21 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_FAULT_DUE_VALUE',
       70,
       'ARC_FAULT_DUE_VALUE.FAULT_ID IN 
        (SELECT ARC_FAULT.ID 
         FROM ARC_FAULT
            INNER JOIN ARC_ASSET ON
               ARC_ASSET.ID = ARC_FAULT.ASSET_ID
            INNER JOIN ARC_MESSAGE ON
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_FAULT_DUE_VALUE'
                 );

--changeSet MTX-220_1:22 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_FAULT',
       80,
       'ARC_FAULT.ASSET_ID IN 
        (SELECT ARC_ASSET.ID 
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON 
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_FAULT'
                 );

--changeSet MTX-220_1:23 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_INV_USAGE',
       90,
       'ARC_INV_USAGE.ASSET_ID IN 
        (SELECT ARC_ASSET.ID 
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON 
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_INV_USAGE'
                 );

--changeSet MTX-220_1:24 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_INV_MAP',
       100,
       'ARC_INV_MAP.ASSET_ID IN 
        (SELECT ARC_ASSET.ID 
         FROM ARC_ASSET
            INNER JOIN ARC_MESSAGE ON 
               ARC_MESSAGE.ID = ARC_ASSET.MSG_ID
         WHERE ARC_MESSAGE.RECEIVED_DT  <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_INV_MAP'
                 );

--changeSet MTX-220_1:25 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_ASSET',
       110,
       'ARC_ASSET.MSG_ID IN 
        (SELECT ID 
         FROM ARC_MESSAGE 
         WHERE ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       )',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_ASSET'
                 );

--changeSet MTX-220_1:26 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY
  (purge_policy_cd,
   purge_table_cd,
   purge_ord,
   predicate_sql,
   utl_id,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
  )
SELECT 'ARC_RECORDS',
       'ARC_MESSAGE',
       120,
       'ARC_MESSAGE.RECEIVED_DT <= TRUNC( sysdate ) - :1
       ',
       0,
       0,
       sysdate,
       sysdate,
       0,
       'MXI'
FROM dual
WHERE NOT EXISTS (SELECT *
                  FROM UTL_PURGE_STRATEGY
                  WHERE purge_policy_cd = 'ARC_RECORDS' AND
                        purge_table_cd  = 'ARC_MESSAGE'
                 );