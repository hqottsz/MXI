--liquibase formatted sql

--changeset OPER-21156:1 stripComments:false
--comment update column ref_spec2k_cmnd.desc_ldesc
UPDATE
   ref_spec2k_cmnd
SET
   desc_ldesc = 'Allows IFS Maintenix to send RFQs to and receive RFQ responses from SPEC2000 vendors automatically.'
WHERE
   spec2k_cmnd_cd = 'S1QUOTES';