--liquibase formatted sql


--changeSet DEV-714:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Need to disable the constraints so we can add rows that do not have all the values filled out*/
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('ORG_ORG','ORG_SUB_TYPE_DB_ID');
END;
/

--changeSet DEV-714:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_CONS_CHK_DROP('ORG_ORG','ORG_SUB_TYPE_CD');
END;
/

--changeSet DEV-714:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Created on 11122012 by SLEVERT 
DECLARE

  -- all records in org_carrier table are in this cursor
  CURSOR lcur_org_carrier IS
  -- all the operators for which we need to migrate
    SELECT org_carrier.carrier_db_id,
           org_carrier.carrier_id,
           org_carrier.carrier_name AS carrier_name,
           org_carrier.iata_cd || '-' || org_carrier.icao_cd AS carrier_cd,
           org_carrier.icao_cd,
           org_carrier.job_title,
           org_carrier.phone_ph,
           org_carrier.fax_ph,
           org_carrier.address_email,
           org_carrier.org_db_id,
           org_carrier.org_id,
           org_carrier.zip_cd,
           org_carrier.address_pmail_1,
           org_carrier.address_pmail_2,
           org_carrier.country_db_id,
           org_carrier.country_cd,
           org_carrier.state_cd,
           org_carrier.logo_blob,
           org_carrier.logo_content_type,
           org_carrier.logo_filename,
           org_org.org_type_cd AS org_type,
           org_org.org_sdesc AS org_name,
           org_org.nh_org_db_id,
           org_org.nh_org_id,
           org_contact_list.contact_db_id,
           org_contact_list.contact_id,
           org_address_list.address_db_id,
           org_address_list.address_id,
           org_logo.blob_db_id,
           org_logo.blob_id
      FROM org_carrier
      LEFT JOIN org_org ON org_org.org_db_id = org_carrier.org_db_id AND
                           org_org.org_id = org_carrier.org_id
      LEFT JOIN org_contact_list ON org_contact_list.org_db_id =
                                    org_org.org_db_id AND
                                    org_contact_list.org_id =
                                    org_org.org_id
      LEFT JOIN org_address_list ON org_address_list.org_db_id =
                                    org_org.org_db_id AND
                                    org_address_list.org_id =
                                    org_org.org_id
      LEFT JOIN org_logo ON org_logo.org_db_id = org_org.org_db_id AND
                            org_logo.org_id = org_org.org_id
     WHERE org_carrier.org_id IS NOT NULL;

  lrec_org_carrier lcur_org_carrier%ROWTYPE;

  -- collection of operators already in the org_org table
  CURSOR lcur_existing_operators IS
    SELECT org_org.org_db_id,
           org_org.org_id,
           org_org.nh_org_db_id,
           org_org.nh_org_id,
           org_org.icao_cd,
           org_org.org_sdesc AS org_name,
           org_contact_list.contact_db_id,
           org_contact_list.contact_id,
           org_address_list.address_db_id,
           org_address_list.address_id,
           org_logo.blob_db_id,
           org_logo.blob_id
      FROM org_org
      LEFT JOIN org_contact_list ON org_contact_list.org_db_id =
                                    org_org.org_db_id AND
                                    org_contact_list.org_id =
                                    org_org.org_id
      LEFT JOIN org_address_list ON org_address_list.org_db_id =
                                    org_org.org_db_id AND
                                    org_address_list.org_id =
                                    org_org.org_id
      LEFT JOIN org_logo ON org_logo.org_db_id = org_org.org_db_id AND
                            org_logo.org_id = org_org.org_id
     WHERE org_org.org_type_cd = 'OPERATOR';

  lrec_existing_operator lcur_existing_operators%ROWTYPE;

  -- vars for new id's we will be creating
  ln_contact_id    org_contact_list.contact_id%TYPE;
  ln_contact_db_id org_contact_list.contact_db_id%TYPE;
  ln_address_id    org_address_list.address_id%TYPE;
  ln_address_db_id org_address_list.address_db_id%TYPE;
  ln_logo_id       org_logo.blob_id%TYPE;
  ln_logo_db_id    org_logo.blob_db_id%TYPE;
  ln_org_db_id     org_org.org_db_id%TYPE;
  ln_org_id        org_org.org_id%TYPE;

  -- booleans to determine when to execute certain migration steps
  ln_org_exists BOOLEAN;

BEGIN

  FOR lrec_org_carrier IN lcur_org_carrier
  LOOP
  
    -- DBMS_OUTPUT.PUT_LINE('Processing ORG_CARRIER KEY: (' ||
    --                      lrec_org_carrier.carrier_db_id || ':' ||
    --                      lrec_org_carrier.carrier_id || ')' ||
    --                      ' ORG KEY: (' || lrec_org_carrier.org_db_id || ':' ||
    --                      lrec_org_carrier.org_id || ')');
  
    ln_org_exists    := FALSE;
    ln_contact_id    := NULL;
		ln_contact_db_id := NULL;
		ln_address_id    := NULL;
		ln_address_db_id := NULL;
		ln_logo_id       := NULL;
		ln_logo_db_id    := NULL;
	  ln_org_db_id     := NULL;
    ln_org_id        := NULL;
  
    -- test to see if the org_org operator exists
    IF lrec_org_carrier.org_type = 'OPERATOR'
    THEN
      -- DBMS_OUTPUT.PUT_LINE('Org Exists: ' || lrec_org_carrier.carrier_name);
      ln_org_exists    := TRUE;
      ln_org_id        := lrec_org_carrier.org_id;
      ln_org_db_id     := lrec_org_carrier.org_db_id;
      ln_contact_id    := lrec_org_carrier.contact_id;
      ln_contact_db_id := lrec_org_carrier.contact_db_id;
      ln_address_id    := lrec_org_carrier.address_id;
      ln_address_db_id := lrec_org_carrier.address_db_id;
      ln_logo_id       := lrec_org_carrier.blob_id;
      ln_logo_db_id    := lrec_org_carrier.blob_db_id;
    END IF;
  
    IF ln_org_exists = FALSE
    THEN
    
      --  DBMS_OUTPUT.PUT_LINE('Org Does Not Exists: ' ||
      --                       lrec_org_carrier.carrier_name);
      --  DBMS_OUTPUT.PUT_LINE('Finding Alternate Org for ' ||
      --                       lrec_org_carrier.carrier_name);
      -- is there a matching org in the org_org table
      -- i.e. ( operator_name = org_name ) && ( icao_cd = icao_cd )
      FOR lrec_existing_operator IN lcur_existing_operators
      LOOP
        IF (lrec_existing_operator.org_name = lrec_org_carrier.carrier_name) AND
           (lrec_existing_operator.icao_cd = lrec_org_carrier.icao_cd)
        THEN
          ln_org_exists    := TRUE;
          ln_org_id        := lrec_existing_operator.org_id;
          ln_org_db_id     := lrec_existing_operator.org_db_id;
          ln_contact_id    := lrec_existing_operator.contact_id;
          ln_contact_db_id := lrec_existing_operator.contact_db_id;
          ln_address_id    := lrec_existing_operator.address_id;
          ln_address_db_id := lrec_existing_operator.address_db_id;
          ln_logo_id       := lrec_existing_operator.blob_id;
          ln_logo_db_id    := lrec_existing_operator.blob_db_id;
          EXIT;
        END IF;
      END LOOP;
    
      IF ln_org_exists = FALSE
      THEN
      
        -- DBMS_OUTPUT.PUT_LINE('Org Does Not Exist: ' ||
        --                       lrec_org_carrier.carrier_name);
      
        SELECT org_id.NEXTVAL INTO ln_org_id FROM DUAL;
        ln_org_db_id := lrec_org_carrier.carrier_db_id;
      
        -- create the org_org entry
        INSERT INTO org_org
          (org_db_id,
           org_id,
           org_sdesc,
           company_org_db_id,
           company_org_id,
           nh_org_db_id,
           nh_org_id,
           org_type_db_id,
           org_type_cd,
           org_cd)
        VALUES
          (ln_org_db_id,
           ln_org_id,
           lrec_org_carrier.carrier_name,
           ln_org_db_id,
           ln_org_id,
           lrec_org_carrier.org_db_id,
           lrec_org_carrier.org_id,
           0,
           'OPERATOR',
           lrec_org_carrier.carrier_cd);
      
      
      END IF;
    
    END IF;
  
    -- update the org_carrier entry
    UPDATE org_carrier
       SET org_carrier.org_db_id = ln_org_db_id,
           org_carrier.org_id    = ln_org_id
     WHERE org_carrier.carrier_db_id = lrec_org_carrier.carrier_db_id AND
           org_carrier.carrier_id = lrec_org_carrier.carrier_id;
  
    -- migrate the contact data from the org_carrier table to the org_contact table
    IF ln_contact_id IS NULL
    THEN
    
      -- entry in the contact table does not exist
      -- DBMS_OUTPUT.PUT_LINE('entry in the contact table does not exist');
    
      SELECT org_contact_id_seq.NEXTVAL INTO ln_contact_id FROM DUAL;
    
      INSERT INTO org_contact
        (contact_db_id,
         contact_id,
         contact_name,
         job_title,
         phone_ph,
         fax_ph,
         address_email)
      VALUES
        (lrec_org_carrier.carrier_db_id,
         ln_contact_id,
         lrec_org_carrier.carrier_name,
         lrec_org_carrier.job_title,
         lrec_org_carrier.phone_ph,
         lrec_org_carrier.fax_ph,
         lrec_org_carrier.address_email);
    
      -- create the link between the org_org table and the org_contact
      INSERT INTO org_contact_list
        (contact_db_id,
         contact_id,
         org_db_id,
         org_id)
      VALUES
        (lrec_org_carrier.carrier_db_id,
         ln_contact_id,
         ln_org_db_id,
         ln_org_id);
    
    ELSE
    
      -- update the existing contact entry
      -- DBMS_OUTPUT.PUT_LINE('update the existing contact entry');
          UPDATE org_contact
         SET contact_name  = lrec_org_carrier.carrier_name,
             job_title     = lrec_org_carrier.job_title,
             phone_ph      = lrec_org_carrier.phone_ph,
             fax_ph        = lrec_org_carrier.fax_ph,
             address_email = lrec_org_carrier.address_email
       WHERE org_contact.contact_id = ln_contact_id AND
             org_contact.contact_id = ln_contact_db_id;
    
    END IF;
  
    -- migrate the addresses
    IF ln_address_id IS NULL
    THEN
    
      -- entry in the address table doe not exist
      -- DBMS_OUTPUT.PUT_LINE('entry in the address table doe not exist');
    
      SELECT org_address_id_seq.NEXTVAL INTO ln_address_id FROM DUAL;
    
      INSERT INTO org_address
        (address_db_id,
         address_id,
         country_db_id,
         country_cd,
         state_cd,
         address_line1,
         address_line2,
         zip_cd)
      VALUES
        (lrec_org_carrier.carrier_db_id,
         ln_address_id,
         lrec_org_carrier.country_db_id,
         lrec_org_carrier.country_cd,
         lrec_org_carrier.state_cd,
         lrec_org_carrier.address_pmail_1,
         lrec_org_carrier.address_pmail_2,
         lrec_org_carrier.zip_cd);
    
      -- create the link between the org_org table and the org_contact
      INSERT INTO org_address_list
        (address_db_id,
         address_id,
         org_db_id,
         org_id)
      VALUES
        (lrec_org_carrier.carrier_db_id,
         ln_address_id,
         ln_org_db_id,
         ln_org_id);
    
    ELSE
    
      -- update the existing entry
      -- DBMS_OUTPUT.PUT_LINE('update the existing address entry');
    
      UPDATE org_address
         SET country_db_id = lrec_org_carrier.country_db_id,
             country_cd    = lrec_org_carrier.country_cd,
             state_cd      = lrec_org_carrier.state_cd,
             address_line1 = lrec_org_carrier.address_pmail_1,
             address_line2 = lrec_org_carrier.address_pmail_2,
             zip_cd        = lrec_org_carrier.zip_cd
       WHERE org_address.address_id = ln_address_id AND
             org_address.address_db_id = ln_address_db_id;
    
    END IF;
  
    -- migrate the logo information
  
    IF lrec_org_carrier.logo_blob IS NOT NULL
    THEN
    
      IF ln_logo_id IS NULL
      THEN
      
        -- entry in the blob table does not exist
        DBMS_OUTPUT.PUT_LINE('entry in the blob table does not exist');
      
        SELECT blob_id_seq.NEXTVAL INTO ln_logo_id FROM DUAL;
      
        INSERT INTO blob_data
          (blob_db_id,
           blob_id,
           blob_data,
           blob_file_name,
           blob_content_type)
        VALUES
          (lrec_org_carrier.carrier_db_id,
           ln_logo_id,
           lrec_org_carrier.logo_blob,
           lrec_org_carrier.logo_filename,
           lrec_org_carrier.logo_content_type);
      
        -- create the link between a logo and a org
        INSERT INTO org_logo
          (blob_db_id,
           blob_id,
           org_db_id,
           org_id)
        VALUES
          (lrec_org_carrier.carrier_db_id,
           ln_logo_id,
           ln_org_db_id,
           ln_org_id);
      
      ELSE
      
        -- update the existing blob entry      
        -- DBMS_OUTPUT.PUT_LINE('update the existing blob entry');
      
        UPDATE blob_data
           SET blob_data         = lrec_org_carrier.logo_blob,
               blob_file_name    = lrec_org_carrier.logo_filename,
               blob_content_type = lrec_org_carrier.logo_content_type
         WHERE blob_data.blob_db_id = ln_logo_db_id AND
               blob_data.blob_id = ln_logo_id;
      
      END IF;
    
    END IF;
  
  END LOOP;

END;
/

--changeSet DEV-714:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Remove the FK Constraints on columns that will no longer exist */
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_CARRIER', 'FK_REFSTATE_ORGCARRIER');
END;
/

--changeSet DEV-714:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ORG_CARRIER', 'FK_REFCOUNTRY_ORGCARRIER');
END;
/

--changeSet DEV-714:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Drop the columns */
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','CARRIER_NAME');
END;
/

--changeSet DEV-714:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','LOGO_BLOB');
END;
/

--changeSet DEV-714:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','LOGO_FILENAME');
END;
/

--changeSet DEV-714:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','LOGO_CONTENT_TYPE');
END;
/

--changeSet DEV-714:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','COUNTRY_DB_ID');
END;
/

--changeSet DEV-714:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','COUNTRY_CD');
END;
/

--changeSet DEV-714:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','STATE_CD');
END;
/

--changeSet DEV-714:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','ADDRESS_PMAIL');
END;
/

--changeSet DEV-714:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','CITY_NAME');
END;
/

--changeSet DEV-714:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','ZIP_CD');
END;
/

--changeSet DEV-714:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','PHONE_PH');
END;
/

--changeSet DEV-714:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','FAX_PH');
END;
/

--changeSet DEV-714:18 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','ADDRESS_EMAIL');
END;
/

--changeSet DEV-714:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','ADDRESS_PMAIL_1');
END;
/

--changeSet DEV-714:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','ADDRESS_PMAIL_2');
END;
/

--changeSet DEV-714:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','CONTACT_NAME');
END;
/

--changeSet DEV-714:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_CARRIER','JOB_TITLE');
END;
/

--changeSet DEV-714:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_ORG','ICAO_CD');
END;
/

--changeSet DEV-714:24 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP(  'ORG_ORG','CALLSIGN_SDESC');
END;
/

--changeSet DEV-714:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Re-instate the column constrains on the org_org table */
BEGIN
utl_migr_schema_pkg.table_column_modify(
'Alter table ORG_ORG modify ( "ORG_SUB_TYPE_DB_ID" Number(10,0) Check ("ORG_SUB_TYPE_DB_ID" BETWEEN 0 AND 4294967295 ) DEFERRABLE  )'
);
END;
/

--changeSet DEV-714:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify(
'Alter table ORG_ORG modify ( "ORG_SUB_TYPE_CD" Varchar2 (8) )'
);
END;
/

--changeSet DEV-714:27 stripComments:false
CREATE OR REPLACE VIEW vw_inv_acft(
      inv_no_db_id,
      inv_no_id,
      inv_no_sdesc,
      inv_cond_db_id,
      inv_cond_cd,
      inv_note,
      part_no_db_id,
      part_no_id,
      part_no_sdesc,
      part_no_oem,
      authority_db_id,
      authority_id,
      authority_cd,
      loc_db_id,
      loc_id,
      loc_cd,
      owner_db_id,
      owner_id,
      owner_cd,
      icn_no_sdesc,
      manufact_dt,
      lot_no_oem,
      po_ref_sdesc,
      received_dt,
      severity_cd,
      used_bool,
      vendor_db_id,
      vendor_id,
      serial_no_oem,
      assmbl_db_id,
      assmbl_cd,
      complete_bool,
      locked_bool,
      reserved_bool,
      inv_oper_db_id,
      inv_oper_cd,
      inv_capability_db_id,
      inv_capability_cd,
      carrier_db_id,
      carrier_name,
      country_db_id,
      coutry_cd,
      ac_reg_cd,
      airworth_cd,
      private_bool,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      avail_bool
   ) AS
  SELECT /*+ rule */
           inv_inv.inv_no_db_id,
           inv_inv.inv_no_id,
           inv_inv.inv_no_sdesc,
           inv_inv.inv_cond_db_id,
           inv_inv.inv_cond_cd,
           inv_inv.note,
           inv_inv.part_no_db_id,
           inv_inv.part_no_id,
           eqp_part_no.part_no_sdesc,
           eqp_part_no.part_no_oem,
           inv_inv.authority_db_id,
           inv_inv.authority_id,
           org_authority.owner_cd,
           inv_inv.loc_db_id,
           inv_inv.loc_id,
           inv_loc.loc_cd,
           inv_inv.owner_db_id,
           inv_inv.owner_id,
           inv_owner.owner_cd,
           inv_inv.icn_no_sdesc,
           inv_inv.manufact_dt,
           inv_inv.lot_oem_tag,
           inv_inv.po_ref_sdesc,
           inv_inv.received_dt,
           inv_inv.severity_cd,
           inv_inv.used_bool,
           inv_inv.vendor_db_id,
           inv_inv.vendor_id,
           inv_inv.serial_no_oem,
           inv_inv.assmbl_db_id,
           inv_inv.assmbl_cd,
           inv_inv.complete_bool,
           inv_inv.locked_bool,
           inv_inv.reserved_bool,
           inv_ac_reg.inv_oper_db_id,
           inv_ac_reg.inv_oper_cd,
           inv_ac_reg.inv_capability_db_id,
           inv_ac_reg.inv_capability_cd,
           org_org.org_db_id,
           org_org.org_sdesc,
           inv_ac_reg.country_db_id,
           inv_ac_reg.country_cd,
           inv_ac_reg.ac_reg_cd,
           inv_ac_reg.airworth_cd,
           inv_ac_reg.private_bool,
           ref_bitmap.bitmap_db_id,
           ref_bitmap.bitmap_tag,
           ref_bitmap.bitmap_name,
           ref_inv_oper.avail_bool
      FROM inv_inv,
           eqp_part_no,
           inv_owner,
           inv_owner org_authority,
           inv_loc,
           ref_bitmap,
           ref_inv_oper,
           inv_ac_reg,
           org_carrier,
           org_org
     WHERE inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
           inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
           AND
           eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
           eqp_part_no.part_no_id    = inv_inv.part_no_id
           AND
           inv_owner.owner_db_id (+)= inv_inv.owner_db_id AND
           inv_owner.owner_id    (+)= inv_inv.owner_id
           AND
           org_authority.owner_db_id (+)= inv_inv.authority_db_id AND
           org_authority.owner_id    (+)= inv_inv.authority_id
           AND
           inv_loc.loc_db_id (+)= inv_inv.loc_db_id AND
           inv_loc.loc_id    (+)= inv_inv.loc_id
           AND
           ref_bitmap.bitmap_db_id = eqp_part_no.bitmap_db_id AND
           ref_bitmap.bitmap_tag   = eqp_part_no.bitmap_tag
           AND
           ref_inv_oper.inv_oper_db_id = inv_ac_reg.inv_oper_db_id AND
           ref_inv_oper.inv_oper_cd    = inv_ac_reg.inv_oper_cd
           AND
           org_carrier.carrier_db_id  (+)= inv_inv.carrier_db_id AND
           org_carrier.carrier_id     (+)= inv_inv.carrier_id
           AND
           org_org.org_db_id  (+)= org_carrier.org_db_id AND
           org_org.org_id     (+)= org_carrier.org_id;


--changeSet DEV-714:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/* Remove existing reference to the Search By Operator page.*/
BEGIN
delete utl_menu_group_item where menu_id = 10145;
delete utl_menu_item where menu_id = 10145;
END;
/