WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3

DEFINE pchk_file_name=pchk_associate_inventory
DEFINE pchk_severity=INFO
DEFINE pchk_table_name=pchk_associate_inventory

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

CREATE TABLE pchk_associate_inventory(
   association_id NUMBER(10),
   barcode VARCHAR2(80),
   serial_number VARCHAR2(80),
   part_number VARCHAR2(80),
   part_name VARCHAR2(80),
   inv_no_db_id NUMBER(30),
   inv_no_id NUMBER(30)
)
;

DECLARE
   lAssociationId NUMBER;
   CURSOR lcur_SimilarInvGroupList
     IS
	 SELECT
	    group_list.*,
		rownum
	 FROM
	 (
       SELECT
          LOWER( inv_inv.serial_no_oem) AS serial_no_oem,
          eqp_part_no.manufact_db_id,
          eqp_part_no.manufact_cd,
          eqp_part_baseline.bom_part_db_id,
          eqp_part_baseline.bom_part_id
       FROM
          inv_inv
          INNER JOIN eqp_part_no ON
             inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
             inv_inv.part_no_id    = eqp_part_no.part_no_id
          INNER JOIN eqp_part_baseline ON
             eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
             eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id
       WHERE 
          LOWER( inv_inv.serial_no_oem ) NOT IN ( 'xxx', 'n/a', 'unk'  ) AND
          LOWER( inv_inv.serial_no_oem ) NOT LIKE ('bn %') AND
          inv_inv.inv_class_cd IN ( 'TRK' , 'SER' ) AND
          LOWER( eqp_part_no.manufact_cd ) != 'unk'
       GROUP BY
          LOWER( inv_inv.serial_no_oem ),
          eqp_part_no.manufact_db_id,
          eqp_part_no.manufact_cd,
          eqp_part_baseline.bom_part_db_id,
          eqp_part_baseline.bom_part_id
       HAVING
          COUNT (*) > 1
	 ) group_list;

   CURSOR lcur_SimilarInvList(
        serial_no_oem_in IN inv_inv.serial_no_oem%TYPE,
        bom_part_db_id_in IN inv_inv.bom_part_db_id%TYPE,
        bom_part_id_in IN inv_inv.bom_part_id%TYPE,
        manufact_db_id_in IN eqp_manufact.manufact_db_id%TYPE,
        manufact_cd_in IN eqp_manufact.manufact_cd%TYPE
        ) IS
       SELECT
		  inv_inv.barcode_sdesc,
		  inv_inv.serial_no_oem,
		  eqp_part_no.part_no_oem,
		  eqp_part_no.part_no_sdesc,
		  inv_inv.inv_no_db_id,
		  inv_inv.inv_no_id
       FROM
          inv_inv
       INNER JOIN eqp_part_no ON
          inv_inv.part_no_db_id = eqp_part_no.part_no_db_id AND
          inv_inv.part_no_id    = eqp_part_no.part_no_id
       INNER JOIN eqp_part_baseline ON
          eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id AND
          eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id          
       WHERE
          eqp_part_no.manufact_db_id = manufact_db_id_in AND
          eqp_part_no.manufact_cd    = manufact_cd_in
          AND
          eqp_part_baseline.bom_part_db_id = bom_part_db_id_in AND
          eqp_part_baseline.bom_part_id    = bom_part_id_in
          AND
          LOWER( inv_inv.serial_no_oem )   = serial_no_oem_in;
   
   BEGIN
      FOR lrec_SimilarInvGroupList IN lcur_SimilarInvGroupList LOOP
  
        lAssociationId := lrec_SimilarInvGroupList.rownum;
     
        FOR lrec_SimilarInvList IN lcur_SimilarInvList(
           lrec_SimilarInvGroupList.serial_no_oem,
           lrec_SimilarInvGroupList.bom_part_db_id,
		   lrec_SimilarInvGroupList.bom_part_id,
           lrec_SimilarInvGroupList.manufact_db_id,
           lrec_SimilarInvGroupList.manufact_cd
        ) LOOP
           INSERT INTO pchk_associate_inventory( association_id , barcode , serial_number , part_number , part_name , inv_no_db_id , inv_no_id )
		   SELECT
		      lAssociationId,
		      lrec_SimilarInvList.barcode_sdesc,
		      lrec_SimilarInvList.serial_no_oem,
			  lrec_SimilarInvList.part_no_oem,
			  lrec_SimilarInvList.part_no_sdesc,
		      lrec_SimilarInvList.inv_no_db_id,
		      lrec_SimilarInvList.inv_no_id
		   FROM
		      DUAL
		   WHERE
              NOT EXISTS ( SELECT 1 FROM pchk_associate_inventory WHERE inv_no_db_id = lrec_SimilarInvList.inv_no_db_id AND inv_no_id = lrec_SimilarInvList.inv_no_id);
        END LOOP;
                  
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
   association_id AS "GROUP",
   barcode AS "Barcode",
   serial_number AS "Serial Number", 
   part_number AS "Part Number", 
   part_name AS "Part Name" 
FROM
   pchk_associate_inventory
ORDER BY
   association_id asc
;
 
SET TERMOUT ON
SET MARKUP HTML OFF
SPOOL OFF

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