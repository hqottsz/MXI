--liquibase formatted sql


--changeSet MX-16298:1 stripComments:false
INSERT INTO utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) 
  SELECT 'INV-00001', 'INV', 'Inventory Messages', 'Inventory must have a unique part/serial number combination.','1', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_rule WHERE rule_id = 'INV-00001' );

--changeSet MX-16298:2 stripComments:false
-- Inventory should not have duplicate part+serial numbers - this now applies to ARCHIVE inventory.
UPDATE utl_rule SET rule_sql =
                  'SELECT                                                           ' || CHR (10) ||
                  '    this_inv.inv_no_db_id , this_inv.inv_no_id ,                 ' || CHR (10) ||
                  '    this_inv.part_no_db_id, this_inv.part_no_id,                 ' || CHR (10) ||
                  '    this_inv.serial_no_oem                                       ' || CHR (10) ||
                  'FROM                                                             ' || CHR (10) || 
                  '   inv_inv this_inv,                                             ' || CHR (10) ||
                  '   inv_inv that_inv                                              ' || CHR (10) ||
                  'WHERE                                                            ' || CHR (10) ||
                  '   this_inv.part_no_db_id   = that_inv.part_no_db_id   AND       ' || CHR (10) ||
                  '   this_inv.part_no_id      = that_inv.part_no_id                ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.serial_no_oem   = that_inv.serial_no_oem             ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.inv_no_db_id   != that_inv.inv_no_db_id    AND       ' || CHR (10) || 
                  '   this_inv.inv_no_id      != that_inv.inv_no_id                 ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.serial_no_oem  NOT IN ( ''XXX'', ''UNK'', ''N/A'' )  ' || CHR (10) ||
                  'ORDER BY                                                         ' || CHR (10) ||
                  '   this_inv.serial_no_oem,                                       ' || CHR (10) ||
                  '   this_inv.part_no_db_id,                                       ' || CHR (10) ||
                  '   this_inv.part_no_id,                                          ' || CHR (10) ||
                  '   this_inv.inv_no_db_id,                                        ' || CHR (10) ||
                  '   this_inv.inv_no_id                                            ' || CHR (10) 
WHERE rule_id = 'INV-00001';