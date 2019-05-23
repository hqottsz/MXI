--liquibase formatted sql
         

--changeSet DEV-1251:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*****************************************************************************
 * Adding new configuration parameter ACTION_OBSOLESCE_VENDOR_PART_PRICE
 ******************************************************************************/
  BEGIN
       utl_migr_data_pkg.config_parm_insert(
                    'ACTION_OBSOLESCE_VENDOR_PART_PRICE',
                    'SECURED_RESOURCE',
                    'Permission to make vendor part price obsolete/historic.',
                    'USER',
                    'TRUE/FALSE',
                    'FALSE',
                     1,
                    'Purchasing - Prices',
                    '8.0',
                     0,
                     utl_migr_data_pkg.DbTypeCdList( 'OPER' )
                    );
    END;
    /        

--changeSet DEV-1251:2 stripComments:false
 /*****************************************************************************
  * Adding new job MX_CORE_OBSOLESCE_VENDOR_PART_PRICE
  ******************************************************************************/
 INSERT INTO 
   UTL_JOB 
   (
     JOB_CD, JOB_NAME, START_TIME, START_DELAY, REPEAT_INTERVAL, ACTIVE_BOOL, UTL_ID
    )
   SELECT 'MX_CORE_OBSOLESCE_VENDOR_PART_PRICE', 'Sets vendor part prices as historic if their effective to date have passed.', '1:00', null, 86400, 1, 0
   FROM
       dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_job WHERE job_cd = 'MX_CORE_OBSOLESCE_VENDOR_PART_PRICE' );                                