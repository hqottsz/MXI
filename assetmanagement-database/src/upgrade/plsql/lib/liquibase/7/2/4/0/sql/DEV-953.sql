--liquibase formatted sql


--changeSet DEV-953:1 stripComments:false
DELETE FROM int_bp_lookup t WHERE t.ref_type = 'SERVICE';