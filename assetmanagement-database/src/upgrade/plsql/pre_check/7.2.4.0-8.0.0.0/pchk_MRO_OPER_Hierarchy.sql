WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_MRO_OPER_Hierarchy
DEFINE pchk_table_name=pchk_mro_oper_hierarchy
DEFINE pchk_severity=WARNING
 
SET VERIFY OFF 
SET SQLPROMPT "_date _user> "
SET TIME ON

SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON
SET FEEDBACK ON
SET HEADING ON
SET PAGESIZE 50000
SET LINESIZE 32767
SET TRIMSPOOL ON
SET SQLBLANKLINES ON
SET DEFINE ON
SET CONCAT OFF
SET MARKUP HTML OFF
 
SET ECHO OFF
PROMPT ***
PROMPT *** Opening file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
CREATE TABLE pchk_mro_oper_hierarchy
AS
SELECT
   org_carrier.carrier_db_id,
   org_carrier.carrier_id,
   org_carrier.carrier_name,
   org_carrier.icao_cd,
   CAST(NULL AS VARCHAR2(4000)) AS warning_message,
   CAST(NULL AS NUMBER(9)) AS warning_ord
FROM
   org_carrier
WHERE 
   rownum = 0
;
 
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
  
  -- output variables
  ln_warning_message  VARCHAR2(4000);
  ln_warning_ord         NUMBER(9) := 0;

BEGIN

  FOR lrec_org_carrier IN lcur_org_carrier
  LOOP
  
    ln_org_exists := FALSE;
    ln_logo_id       := NULL;
      ln_logo_db_id    := NULL;
      ln_contact_id    := NULL;
      ln_contact_db_id := NULL;
      ln_address_id    := NULL;
      ln_address_db_id := NULL;
      ln_logo_id       := NULL;
      ln_logo_db_id    := NULL;
  
    -- test to see if the org_org operator exists
    IF lrec_org_carrier.org_type = 'OPERATOR'
    THEN
    
      ln_warning_message := 'An existing entry ' ||
                            lrec_org_carrier.org_db_id || ':' ||
                            lrec_org_carrier.org_id ||
                            ' exists for OPERATOR: ' ||
                            lrec_org_carrier.carrier_name ||
                            ' in the org_org table.  No new record will be created.';
      
      ln_warning_ord := ln_warning_ord + 1;
      
      INSERT INTO
         pchk_mro_oper_hierarchy
      VALUES
         (
            lrec_org_carrier.carrier_db_id,
            lrec_org_carrier.carrier_id,
            lrec_org_carrier.carrier_name,
            lrec_org_carrier.icao_cd,
            ln_warning_message,
            ln_warning_ord
         )
      ;
      
      ln_org_exists    := TRUE;
      ln_logo_id       := lrec_org_carrier.blob_id;
      ln_logo_db_id    := lrec_org_carrier.blob_db_id;
      ln_contact_id    := lrec_org_carrier.contact_id;
      ln_contact_db_id := lrec_org_carrier.contact_db_id;
      ln_address_id    := lrec_org_carrier.address_id;
      ln_address_db_id := lrec_org_carrier.address_db_id;
      ln_logo_id       := lrec_org_carrier.blob_id;
      ln_logo_db_id    := lrec_org_carrier.blob_db_id;
    END IF;
  
    IF ln_org_exists = FALSE
    THEN
      
      ln_warning_message := 'The ' ||
                            lrec_org_carrier.carrier_name ||
                            ' OPERATOR is not pointing to an operator in the org_org table. Looking for alternate OPERATOR.';
    
      ln_warning_ord := ln_warning_ord + 1;
      
      INSERT INTO
         pchk_mro_oper_hierarchy
      VALUES
         (
            lrec_org_carrier.carrier_db_id,
            lrec_org_carrier.carrier_id,
            lrec_org_carrier.carrier_name,
            lrec_org_carrier.icao_cd,
            ln_warning_message,
            ln_warning_ord
         )
      ;
    
      FOR lrec_existing_operator IN lcur_existing_operators
      LOOP
        IF (lrec_existing_operator.org_name = lrec_org_carrier.carrier_name) AND
           (lrec_existing_operator.icao_cd = lrec_org_carrier.icao_cd)
        THEN
        
          ln_warning_message := 'Found alternate OPERATOR in ORG_ORG ( ' ||
                                lrec_existing_operator.org_name || ', ' ||
                                lrec_existing_operator.icao_cd || ', ' ||
                                lrec_existing_operator.org_db_id || ':' ||
                                lrec_existing_operator.org_id || ' )' ;

          ln_warning_ord := ln_warning_ord + 1;

          INSERT INTO
             pchk_mro_oper_hierarchy
          VALUES
             (
                lrec_org_carrier.carrier_db_id,
                lrec_org_carrier.carrier_id,
                lrec_org_carrier.carrier_name,
                lrec_org_carrier.icao_cd,
                ln_warning_message,
                ln_warning_ord
             )
          ;

          ln_warning_message := 'Changing ORG_CARRIER org key from ' ||
                                lrec_org_carrier.org_db_id || ':' ||
                                lrec_org_carrier.org_id || ' to ' ||
                                lrec_existing_operator.org_db_id || ':' ||
                                lrec_existing_operator.org_id || '.';

          ln_warning_ord := ln_warning_ord + 1;

          INSERT INTO
             pchk_mro_oper_hierarchy
          VALUES
             (
                lrec_org_carrier.carrier_db_id,
                lrec_org_carrier.carrier_id,
                lrec_org_carrier.carrier_name,
                lrec_org_carrier.icao_cd,
                ln_warning_message,
                ln_warning_ord
             )
          ;
        
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
        ln_warning_message := 'The following OPERATOR does not exists ' ||
                             lrec_org_carrier.carrier_name || '.';
        
        ln_warning_ord := ln_warning_ord + 1;
      
        INSERT INTO
           pchk_mro_oper_hierarchy
        VALUES
           (
              lrec_org_carrier.carrier_db_id,
              lrec_org_carrier.carrier_id,
              lrec_org_carrier.carrier_name,
              lrec_org_carrier.icao_cd,
              ln_warning_message,
              ln_warning_ord
           )
        ;
      
        ln_warning_message := 'A new ORG_ORG entry will be created in for ' ||
                              lrec_org_carrier.carrier_name || '.  Please set the RSTAT_CD to 1 or 2 in order to reduce visiblity of this new operator in organization drop downs.';

        ln_warning_ord := ln_warning_ord + 1;
      
        INSERT INTO
           pchk_mro_oper_hierarchy
        VALUES
           (
              lrec_org_carrier.carrier_db_id,
              lrec_org_carrier.carrier_id,
              lrec_org_carrier.carrier_name,
              lrec_org_carrier.icao_cd,
              ln_warning_message,
              ln_warning_ord
           )
        ;

      END IF;
    
    END IF;
  
    -- migrate the contact data from the org_carrier table to the org_contact table
    IF ln_contact_id IS NULL
    THEN
      -- entry in the contact table does not exist
      ln_warning_message := 'A new ORG_CONTACT entry will be created ' || 
                            '( CARRIER_NAME=' || lrec_org_carrier.carrier_name || 
                            ', JOB_TITLE=' || lrec_org_carrier.job_title || 
                            ', PHONE_PH=' || lrec_org_carrier.phone_ph || 
                            ', FAX_PH=' || lrec_org_carrier.fax_ph || 
                            ', ADDRESS_EMAIL=' || lrec_org_carrier.address_email ||
                            ' ) for ' ||
                            lrec_org_carrier.carrier_name || '.';

      ln_warning_ord := ln_warning_ord + 1;
      
      INSERT INTO
         pchk_mro_oper_hierarchy
      VALUES
         (
            lrec_org_carrier.carrier_db_id,
            lrec_org_carrier.carrier_id,
            lrec_org_carrier.carrier_name,
            lrec_org_carrier.icao_cd,
            ln_warning_message,
            ln_warning_ord
         )
      ;

    ELSE
      -- update the existing contact entry
      ln_warning_message := 'An existing ORG_CONTACT entry ' || ln_contact_db_id || ':' || ln_contact_id || ' will be updated to ' || 
                            '( CARRIER_NAME=' || lrec_org_carrier.carrier_name ||
                            ', JOB_TITLE=' || lrec_org_carrier.job_title || 
                            ', PHONE_PH=' || lrec_org_carrier.phone_ph || 
                            ', FAX_PH=' || lrec_org_carrier.fax_ph || 
                            ', ADDRESS_EMAIL=' || lrec_org_carrier.address_email ||
                            ' ) for ' ||
                            lrec_org_carrier.carrier_name || '.';

      ln_warning_ord := ln_warning_ord + 1;
      
      INSERT INTO
         pchk_mro_oper_hierarchy
      VALUES
         (
            lrec_org_carrier.carrier_db_id,
            lrec_org_carrier.carrier_id,
            lrec_org_carrier.carrier_name,
            lrec_org_carrier.icao_cd,
            ln_warning_message,
            ln_warning_ord
         )
      ;
      
    END IF;
  
    -- migrate the addresses
    IF ln_address_id IS NULL
    THEN
      -- entry in the address table doe not exist
      ln_warning_message := 'A new ORG_ADDRESS entry will be created ' ||
                            '( ADDRESS_1=' || lrec_org_carrier.address_pmail_1 ||
                            ', STATE_CD=' || lrec_org_carrier.state_cd ||
                            ', COUNTRY_CD=' || lrec_org_carrier.country_cd ||
                            ', ZIP_CD=' || lrec_org_carrier.zip_cd ||
                            ', ADDRESS2= ' || lrec_org_carrier.address_pmail_2 ||
                            ' ) for ' ||
                            lrec_org_carrier.carrier_name || '.';

      ln_warning_ord := ln_warning_ord + 1;
      
      INSERT INTO
         pchk_mro_oper_hierarchy
      VALUES
         (
            lrec_org_carrier.carrier_db_id,
            lrec_org_carrier.carrier_id,
            lrec_org_carrier.carrier_name,
            lrec_org_carrier.icao_cd,
            ln_warning_message,
            ln_warning_ord
         )
      ;

    ELSE
      -- update the existing entry
      ln_warning_message := 'An existing ORG_ADDRESS entry ' || ln_address_db_id || ':' || ln_address_id || ' will be updated to ' || 
                            '( ADDRESS_1=' || lrec_org_carrier.address_pmail_1 ||
                            ', STATE_CD=' || lrec_org_carrier.state_cd ||
                            ', COUNTRY_CD=' || lrec_org_carrier.country_cd ||
                            ', ZIP_CD=' || lrec_org_carrier.zip_cd ||
                            ', ADDRESS2= ' || lrec_org_carrier.address_pmail_2 ||
                            ' ) for ' ||
                            lrec_org_carrier.carrier_name || '.';

      
      ln_warning_ord := ln_warning_ord + 1;
      
      INSERT INTO
         pchk_mro_oper_hierarchy
      VALUES
         (
            lrec_org_carrier.carrier_db_id,
            lrec_org_carrier.carrier_id,
            lrec_org_carrier.carrier_name,
            lrec_org_carrier.icao_cd,
            ln_warning_message,
            ln_warning_ord
         )
      ;

    END IF;
  
    -- migrate the logo information
  
    IF lrec_org_carrier.logo_blob IS NOT NULL
    THEN
      IF ln_logo_id IS NULL
      THEN
        -- entry in the blob table does not exist
        ln_warning_message := 'A new BLOB_DATA entry will be created'||
                              '( CONTENT_TYPE=' || lrec_org_carrier.logo_content_type ||
                              ', LOGO_FILENAME=' || lrec_org_carrier.logo_filename ||
                              ' ) for ' ||
                              lrec_org_carrier.carrier_name || '.';
      
        ln_warning_ord := ln_warning_ord + 1;

        INSERT INTO
           pchk_mro_oper_hierarchy
        VALUES
           (
              lrec_org_carrier.carrier_db_id,
              lrec_org_carrier.carrier_id,
              lrec_org_carrier.carrier_name,
              lrec_org_carrier.icao_cd,
              ln_warning_message,
              ln_warning_ord
           )
        ;

      ELSE
        -- update the existing blob entry      
        ln_warning_message := 'An existing BLOB_DATA entry ' || ln_logo_db_id || ':' || ln_logo_id || ' will be updated to ' || 
                              '( CONTENT_TYPE=' || lrec_org_carrier.logo_content_type ||
                              ', LOGO_FILENAME=' || lrec_org_carrier.logo_filename ||
                              ' ) for ' ||
                              lrec_org_carrier.carrier_name || '.';

        ln_warning_ord := ln_warning_ord + 1;

        INSERT INTO
           pchk_mro_oper_hierarchy
        VALUES
           (
              lrec_org_carrier.carrier_db_id,
              lrec_org_carrier.carrier_id,
              lrec_org_carrier.carrier_name,
              lrec_org_carrier.icao_cd,
              ln_warning_message,
              ln_warning_ord
           )
        ;

      END IF;
    END IF;
  
  END LOOP;

END;
/
  
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to &pchk_file_name.html
PROMPT ***
SET ECHO ON
SPOOL OFF
 
SET ECHO OFF
SET TERMOUT OFF
SET MARKUP -
   HTML ON -
   HEAD " -
   <STYLE type='text/css'> -
      BODY {font-family:verdana;font-size:11px; font-weight:bold;} -
      TABLE {border-collapse:collapse; font-size:11px; width:100normal;} -
      TH {background-color:#4682B4; color:#fff; height:30px; font-weight:bold;} -
   </STYLE> " -
   BODY "" -
   TABLE "border=1 bordercolor=black" -
   SPOOL ON -
   ENTMAP ON -
   PREFORMAT OFF 
 
SPOOL &pchk_file_name.html

SELECT 
   '&pchk_severity' AS "SEV",
   carrier_db_id,
   carrier_id,
   carrier_name,
   icao_cd,
   warning_message
FROM
   pchk_mro_oper_hierarchy
ORDER BY 
   warning_ord
;

SPOOL OFF
SET MARKUP HTML OFF
SET TERMOUT ON
 
SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
SET ECHO OFF
PROMPT ***
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
UNDEFINE pchk_file_name
UNDEFINE pchk_table_name
UNDEFINE pchk_severity
