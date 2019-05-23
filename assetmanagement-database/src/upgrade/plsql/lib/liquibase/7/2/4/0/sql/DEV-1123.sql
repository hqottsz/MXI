--liquibase formatted sql


--changeSet DEV-1123:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************************************
 * Add two new columns to PO_HEADER table, CREATED_BY_ORG_DB_ID and CREATED_BY_ORG_ID and populate them
 * for all records with ADMIN company's key(0:1) which in our system is MXI organization.
 * They are the foreign keys to the ORG_ORG table. 
 ******************************************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   CREATED_BY_ORG_DB_ID Number(10,0) Check (CREATED_BY_ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1123:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   CREATED_BY_ORG_ID Number(10,0) Check (CREATED_BY_ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1123:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table PO_HEADER add constraint FK_CBORGORG_POHEADER foreign key (CREATED_BY_ORG_DB_ID,CREATED_BY_ORG_ID) references ORG_ORG (ORG_DB_ID,ORG_ID) DEFERRABLE
');
END;
/

--changeSet DEV-1123:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER DISABLE';
END;
/

--changeSet DEV-1123:5 stripComments:false
-- Update all records with the MXI organization key (0:1) which is the ADMIN company
UPDATE 
  po_header
SET 
  po_header.created_by_org_db_id=0, 
  po_header.created_by_org_id=1
WHERE 
  po_header.created_by_org_db_id IS NULL AND 
  po_header.created_by_org_id IS NULL
;

--changeSet DEV-1123:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER ENABLE';
END;
/

--changeSet DEV-1123:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add the NOT NULL constraint to the columns now that they are populated
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   CREATED_BY_ORG_DB_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1123:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   CREATED_BY_ORG_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1123:9 stripComments:false
-- Purchase orders
/******************************************************************************************************
 * Set owner to the default local owner for PURCHASE orders
 * Set owner to the repaired inventory's owner for REPAIR orders
 ******************************************************************************************************/
UPDATE
      po_line
SET      
      (
      owner_db_id,
      owner_id
      )
      =
      (
       SELECT 
        owner_db_id, 
        owner_id 
       FROM 
        inv_owner 
       WHERE 
        local_bool = 1 
        AND 
        default_bool = 1
      )
WHERE
      EXISTS(
            SELECT
                  1
            FROM
                po_header
            WHERE
                po_header.po_db_id = po_line.po_db_id AND
                po_header.po_id    = po_line.po_id
                AND
                po_header.po_type_cd = 'PURCHASE'
      )
      AND
      owner_db_id IS NULL;            

--changeSet DEV-1123:10 stripComments:false
-- Repair orders
UPDATE
    po_line tbu
SET(
    owner_db_id,
    owner_id
   ) = (
    SELECT
        inv_inv.owner_db_id,
        inv_inv.owner_id 
    FROM
        po_line INNER JOIN sched_stask
        ON po_line.sched_db_id = sched_stask.sched_db_id AND
           po_line.sched_id    = sched_stask.sched_id     
        INNER JOIN inv_inv 
        ON inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
           inv_inv.inv_no_id    = sched_stask.main_inv_no_id
    WHERE
         po_line.po_db_id   = tbu.po_db_id AND
         po_line.po_id      = tbu.po_id AND
         po_line.po_line_id = tbu.po_line_id                
     )
WHERE
    tbu.po_line_type_cd='REPAIR' 
    AND
    tbu.owner_id IS NULL;                     

--changeSet DEV-1123:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************************************
 * Create new package user_pkg and function is_user_auth_to_view_order
 ******************************************************************************************************/
 CREATE OR REPLACE PACKAGE USER_PKG
 IS
    
 /* subtype declarations */
 SUBTYPE typn_RetCode IS NUMBER;
 SUBTYPE typn_Id IS po_header.po_id%TYPE;
  
  
 /* constant declarations (error codes) */
 icn_Success CONSTANT typn_RetCode := 1;   -- Success
 icn_NoProc  CONSTANT typn_RetCode := 0;   -- No processing done
 icn_Error   CONSTANT typn_RetCode := -1;  -- Error
  
  
 FUNCTION is_user_auth_to_view_order(
                             an_PoDbId IN typn_Id,
                             an_PoId IN typn_Id,
                             an_CurrUserId IN typn_Id) RETURN NUMBER;
  
 END USER_PKG;
 /

--changeSet DEV-1123:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY USER_PKG
IS


FUNCTION is_user_auth_to_view_order(
                           an_PoDbId IN typn_Id,
                           an_PoId IN typn_Id,
                           an_CurrUserId IN typn_Id) RETURN NUMBER
 IS

   -- Local Variables
   v_count NUMBER;

BEGIN

   SELECT COUNT(*)
     INTO v_count

     FROM org_hr
    INNER JOIN org_org_hr ON org_org_hr.hr_db_id = org_hr.hr_db_id
                         AND org_org_hr.hr_id = org_hr.hr_id

    INNER JOIN org_org user_org ON user_org.org_db_id = org_org_hr.org_db_id
                               AND user_org.org_id = org_org_hr.org_id

    INNER JOIN org_org user_company ON user_company.org_db_id =
                                       user_org.company_org_db_id
                                   AND user_company.org_id =
                                       user_org.company_org_id
    WHERE org_hr.user_id = an_CurrUserId
      AND org_org_hr.default_org_bool = 1
      AND user_company.org_type_cd = 'ADMIN';

   IF v_count > 0
   THEN
      RETURN 1;
   ELSE
      SELECT COUNT(*)
        INTO v_count
        FROM org_hr
       INNER JOIN org_org_hr ON org_org_hr.hr_db_id = org_hr.hr_db_id
                            AND org_org_hr.hr_id = org_hr.hr_id

       INNER JOIN org_org user_org ON user_org.org_db_id = org_org_hr.org_db_id
                                  AND user_org.org_id = org_org_hr.org_id

       INNER JOIN po_header ON po_header.created_by_org_db_id =
                               user_org.company_org_db_id
                           AND po_header.created_by_org_id =
                               user_org.company_org_id
       WHERE org_hr.user_id = an_CurrUserId
         AND po_header.po_db_id = an_PoDbId
         AND po_header.po_id = an_PoId
         AND org_org_hr.default_org_bool = 1;

      IF v_count > 0
      THEN
         RETURN 1;
      ELSE
         RETURN 0;
      END IF;
   END IF;

END is_user_auth_to_view_order;

END USER_PKG;
/  

--changeSet DEV-1123:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/******************************************************************************************************
 * Drop the is_user_auth_to_view_order function if exists outside of a package
 ******************************************************************************************************/
 BEGIN
 utl_migr_schema_pkg.function_drop('is_user_auth_to_view_order');
 END;
 /

--changeSet DEV-1123:14 stripComments:false
/******************************************************************************************************
 * Update vew vw_evt_po
 ******************************************************************************************************/
CREATE OR REPLACE VIEW vw_evt_po (
   po_db_id,
   po_id,
   po_number,
   po_type_db_id,
   po_type_cd,
   po_type_sdesc,
   vendor_db_id,
   vendor_id,
   vendor_cd,
   event_status_db_id,
   event_status_cd,
   user_status_cd,
   event_status_sdesc,
   issued_dt,
   closed_dt,
   hist_dt,
   hist_bool,
   needed_by_dt,
   promised_by_dt,
   contact_hr_db_id,
   contact_hr_id,
   contact_user_id,
   contact_hr_sdesc,
   contact_username,
   req_priority_db_id,
   req_priority_cd,
   req_priority_sdesc,
   currency_db_id,
   currency_cd,
   currency_sdesc,
   currency_bitmap_db_id,
   currency_bitmap_tag,
   total_price_qt,
   exchg_qt,
   terms_conditions_db_id,
   terms_conditions_cd,
   terms_conditions_sdesc,
   fob_db_id,
   fob_cd,
   fob_sdesc,
   vendor_account_db_id,
   vendor_account_id,
   vendor_account_cd,
   vendor_loc_db_id,
   vendor_loc_id,
   vendor_loc_cd,
   vendor_contact_name,
   vendor_phone_ph,
   org_db_id,
   org_id,
   created_by_org_db_id,
   created_by_org_id,
   ship_to_loc_db_id,
   ship_to_loc_id,
   ship_to_loc_cd,
   ship_to_address_pmail_1,
   ship_to_address_pmail_2,
   ship_to_city_name,
   ship_to_state_cd,
   ship_to_country_db_id,
   ship_to_country_cd,
   ship_to_zip_cd,
   ship_to_code,
   spec2k_cust_db_id,
   spec2k_cust_cd,
   transport_type_db_id,
   transport_type_cd,
   transport_type_sdesc,
   vendor_note,
   ext_key_sdesc, 
   po_auth_flow_db_id, 
   po_auth_flow_cd, 
   po_auth_flow_sdesc,
   borrow_rate_db_id, 
   borrow_rate_cd, 
   borrow_rate_sdesc,
   last_mod_dt,
   receipt_insp_bool,
   auth_bool
) AS
SELECT
      po_header.po_db_id,
      po_header.po_id,
      evt_event.event_sdesc,
      po_header.po_type_db_id,
      po_header.po_type_cd,
      ref_po_type.desc_sdesc,
      po_header.vendor_db_id,
      po_header.vendor_id,
      org_vendor.vendor_cd,
      evt_event.event_status_db_id,
      evt_event.event_status_cd,
      ref_event_status.user_status_cd,
      ref_event_status.desc_sdesc,
      po_header.issued_dt,
      po_header.closed_dt,
      evt_event.event_dt,
      evt_event.hist_bool,
      (
         SELECT
            MIN( req_part.req_by_dt )
         FROM
            req_part
         WHERE
            req_part.po_db_id = po_header.po_db_id AND
            req_part.po_id    = po_header.po_id
      ),
      (
         SELECT
            MAX( po_line.promise_by_dt )
         FROM
            po_line
         WHERE
            po_line.po_db_id = po_header.po_db_id AND
            po_line.po_id    = po_header.po_id
      ),
      po_header.contact_hr_db_id,
      po_header.contact_hr_id,
      utl_user.user_id,
      utl_user.last_name || ', ' || utl_user.first_name,
      utl_user.username,
      po_header.req_priority_db_id,
      po_header.req_priority_cd,
      ref_req_priority.desc_sdesc,
      po_header.currency_db_id,
      po_header.currency_cd,
      ref_currency.desc_sdesc,
      ref_currency.bitmap_db_id,
      ref_currency.bitmap_tag,
      (
         SELECT
            SUM( po_line.line_price )
         FROM
            po_line
         WHERE
            po_line.po_db_id = po_header.po_db_id AND
            po_line.po_id    = po_header.po_id
      ),
      po_header.exchg_qt,
      po_header.terms_conditions_db_id,
      po_header.terms_conditions_cd,
      ref_terms_conditions.desc_sdesc,
      po_header.fob_db_id,
      po_header.fob_cd,
      ref_fob.desc_sdesc,
      po_header.vendor_account_db_id,
      po_header.vendor_account_id,
      po_header.vendor_account_cd,
      po_header.vendor_loc_db_id,
      po_header.vendor_loc_id,
      vendor_loc.loc_cd,
      vendor_contact.contact_name,
      vendor_contact.phone_ph,
      po_header.org_db_id,
      po_header.org_id,
      po_header.created_by_org_db_id,
      po_header.created_by_org_id,
      po_header.ship_to_loc_db_id,
      po_header.ship_to_loc_id,
      ship_to_loc.loc_cd,
      ship_to_loc.address_pmail_1,
      ship_to_loc.address_pmail_2,
      ship_to_loc.city_name,
      ship_to_loc.state_cd,
      ship_to_loc.country_db_id,
      ship_to_loc.country_cd,
      ship_to_loc.zip_cd,
      po_header.ship_to_code,
      po_header.spec2k_cust_db_id,
      po_header.spec2k_cust_cd,
      po_header.transport_type_db_id,
      po_header.transport_type_cd,
      ref_transport_type.desc_sdesc,
      po_header.vendor_note,
      evt_event.ext_key_sdesc,
      po_header.po_auth_flow_db_id,
      po_header.po_auth_flow_cd,
      ref_po_auth_flow.desc_sdesc,
      po_header.borrow_rate_db_id,
      po_header.borrow_rate_cd,
      ref_borrow_rate.desc_sdesc,
      po_header.last_mod_dt,
      po_header.receipt_insp_bool,
      po_header.auth_bool
   FROM
      evt_event,
      po_header,
      ref_event_status,
      ref_po_type,
      ref_req_priority,
      inv_loc ship_to_loc,
      org_vendor,
      inv_loc vendor_loc,
      inv_loc_contact vendor_contact,
      ref_currency,
      org_hr,
      utl_user,
      ref_terms_conditions,
      ref_fob,
      ref_transport_type,
      ref_po_auth_flow,
      ref_borrow_rate
   WHERE
      evt_event.event_db_id = po_header.po_db_id AND
      evt_event.event_id    = po_header.po_id
      AND
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
      AND
      ref_po_type.po_type_db_id = po_header.po_type_db_id AND
      ref_po_type.po_type_cd    = po_header.po_type_cd
      AND
      ref_req_priority.req_priority_db_id = po_header.req_priority_db_id AND
      ref_req_priority.req_priority_cd    = po_header.req_priority_cd
      AND
      ship_to_loc.loc_db_id = po_header.ship_to_loc_db_id AND
      ship_to_loc.loc_id    = po_header.ship_to_loc_id
      AND
      org_vendor.vendor_db_id = po_header.vendor_db_id AND
      org_vendor.vendor_id    = po_header.vendor_id
      AND
      vendor_loc.loc_db_id = po_header.vendor_loc_db_id AND
      vendor_loc.loc_id    = po_header.vendor_loc_id
      AND
      vendor_contact.loc_db_id    (+)= vendor_loc.loc_db_id AND
      vendor_contact.loc_id       (+)= vendor_loc.loc_id AND
      vendor_contact.default_bool (+)= 1
      AND
      ref_currency.currency_db_id = po_header.currency_db_id AND
      ref_currency.currency_cd    = po_header.currency_cd
      AND
      org_hr.hr_db_id  = po_header.contact_hr_db_id AND
      org_hr.hr_id     = po_header.contact_hr_id AND
      utl_user.user_id = org_hr.user_id
      AND
      ref_terms_conditions.terms_conditions_db_id (+)= po_header.terms_conditions_db_id AND
      ref_terms_conditions.terms_conditions_cd    (+)= po_header.terms_conditions_cd
      AND
      ref_fob.fob_db_id (+)= po_header.fob_db_id AND
      ref_fob.fob_cd    (+)= po_header.fob_cd
      AND
      ref_transport_type.transport_type_db_id (+)= po_header.transport_type_db_id AND
      ref_transport_type.transport_type_cd    (+)= po_header.transport_type_cd
      AND
      ref_po_auth_flow.po_auth_flow_db_id (+)= po_header.po_auth_flow_db_id AND
      ref_po_auth_flow.po_auth_flow_cd    (+)= po_header.po_auth_flow_cd
      AND
      ref_borrow_rate.borrow_rate_db_id (+)= po_header.borrow_rate_db_id AND
      ref_borrow_rate.borrow_rate_cd    (+)= po_header.borrow_rate_cd
;

--changeSet DEV-1123:15 stripComments:false
-- disable foreign key constraint from utl_user_parm
/******************************************************************************************************
 * Update SHOW_COST configuration parameter. Set: parameter type=SECURED_RESOURCE, 
 * parameter category='Purchasing - Prices', default value='false'
 ******************************************************************************************************/
ALTER TABLE UTL_USER_PARM DISABLE CONSTRAINT FK_UTLCONFIGPARM_UTLUSERPARM;

--changeSet DEV-1123:16 stripComments:false
-- disable foreign key constraint from utl_role_parm
ALTER TABLE UTL_ROLE_PARM DISABLE CONSTRAINT FK_UTLCONFIGPARM_UTLROLEPARM;

--changeSet DEV-1123:17 stripComments:false
-- disable foreign key constraint from db_type_config_parm
ALTER TABLE DB_TYPE_CONFIG_PARM DISABLE CONSTRAINT FK_UTLCONFIGPARM_DBTYPECFGPARM;

--changeSet DEV-1123:18 stripComments:false
-- update the config parm type in utl_user_parm, utl_role_parm, db_type_config_parm, utl_config_parm
UPDATE utl_user_parm SET parm_type = 'SECURED_RESOURCE' 
WHERE parm_name = 'SHOW_COST' AND parm_type = 'MXWEB' ;

--changeSet DEV-1123:19 stripComments:false
UPDATE utl_role_parm SET parm_type = 'SECURED_RESOURCE'
WHERE parm_name = 'SHOW_COST'AND parm_type = 'MXWEB' ;

--changeSet DEV-1123:20 stripComments:false
UPDATE db_type_config_parm SET parm_type = 'SECURED_RESOURCE' 
WHERE parm_name = 'SHOW_COST' AND parm_type = 'MXWEB' ;

--changeSet DEV-1123:21 stripComments:false
UPDATE utl_config_parm SET parm_type = 'SECURED_RESOURCE', category = 'Purchasing - Prices', default_value = 'false'
WHERE parm_name = 'SHOW_COST'AND parm_type = 'MXWEB' ;

--changeSet DEV-1123:22 stripComments:false
-- enable the foreign key constraints in the utl_user_parm table
ALTER TABLE UTL_USER_PARM ENABLE CONSTRAINT FK_UTLCONFIGPARM_UTLUSERPARM;

--changeSet DEV-1123:23 stripComments:false
-- enable the foreign key constraints in the utl_role_parm table
ALTER TABLE UTL_ROLE_PARM ENABLE CONSTRAINT FK_UTLCONFIGPARM_UTLROLEPARM;

--changeSet DEV-1123:24 stripComments:false
-- enable foreign key constraint in the db_type_config_parm
ALTER TABLE DB_TYPE_CONFIG_PARM ENABLE CONSTRAINT FK_UTLCONFIGPARM_DBTYPECFGPARM;