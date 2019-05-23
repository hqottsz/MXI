--liquibase formatted sql

--changeset OPER-23313:1 stripComments:false 
INSERT INTO 
   ref_quicktext_type (
      quicktext_type_cd, 
	  desc_sdesc, 
	  desc_ldesc, 
	  rstat_cd, 
	  ctrl_db_id, 
	  creation_dt, 
	  revision_no, 
	  revision_dt, 
	  revision_db_id, 
	  revision_user
   )
SELECT 
   'MX_MTC_CERT_INSP', 
   'Certification/Inspection Action', 
   'Certification and inspection action notes that can be used to quickly add actions to work capture.', 
   0, 
   0, 
   TO_DATE('2018-07-03', 'YYYY-MM-DD'), 
   1, 
   TO_DATE('2018-07-03', 'YYYY-MM-DD'), 
   0, 
   'MXI'
FROM 
   dual 
WHERE 
   NOT EXISTS (SELECT 1 FROM ref_quicktext_type WHERE quicktext_type_cd = 'MX_MTC_CERT_INSP');
