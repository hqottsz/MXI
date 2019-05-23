--liquibase formatted sql

--changeSet OPER-23033:1 stripComments:false
UPDATE ref_spec2k_cust
SET
    default_bool = 1
WHERE
    (
	    SELECT
	    	count(*)
	    FROM
	    	ref_spec2k_cust
	    WHERE
	    	default_bool = 1
    ) = 0 AND
    rownum = 1;