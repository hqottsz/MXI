--liquibase formatted sql


--changeSet MX-16925:1 stripComments:false
-- If the part group's inventory clas is ACFT, change the LRU to false
UPDATE eqp_bom_part SET lru_bool = 0 WHERE inv_class_cd = 'ACFT';