
/*
This test case consists of a tree of inventory from the inv_inv table
connected by nh_inv_no_id. The tree has a height of three element, each
with a different class code.
*/

-- Top Element (aircraft) ------------------------------------------------------
INSERT INTO inv_inv (
   alt_id,
   inv_no_db_id,
   inv_no_id,
   nh_inv_no_db_id,
   nh_inv_no_id,
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,
   assmbl_inv_no_db_id,
   assmbl_inv_no_id,

   inv_no_sdesc,
   inv_class_cd
) VALUES (
   '00000000000000000000000000000101',
   1234,
   101,
   NULL,
   NULL,
   1234,
   '201',
   301,
   401,
   1234,
   101,

   'Top1',
   'ACFT'
);

INSERT INTO eqp_assmbl_bom (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,

   assmbl_bom_cd
) VALUES (
   1234,
   '201',
   301,

   'Aircraft'
);

INSERT INTO eqp_assmbl_pos (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,

   eqp_pos_cd
) VALUES (
   1234,
   '201',
   301,
   401,

   '501'
);


-- Upper element (system) ------------------------------------------------------
INSERT INTO inv_inv (
   alt_id,
   inv_no_db_id,
   inv_no_id,
   nh_inv_no_db_id,
   nh_inv_no_id,
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,
   assmbl_inv_no_db_id,
   assmbl_inv_no_id,

   inv_no_sdesc,
   inv_class_cd
) VALUES (
   '00000000000000000000000000000102',
   1234,
   102,
   1234,
   101,
   1234,
   '202',
   302,
   402,
   1234,
   101,

   'Upper1',
   'SYS'
);

INSERT INTO eqp_assmbl_bom (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,

   assmbl_bom_cd
) VALUES (
   1234,
   '202',
   302,

   '10-00-00'
);

INSERT INTO eqp_assmbl_pos (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,

   eqp_pos_cd
) VALUES (
   1234,
   '202',
   302,
   402,

   '502'
);


-- Lower element (tracked part) ------------------------------------------------
INSERT INTO inv_inv (
   alt_id,
   inv_no_db_id,
   inv_no_id,
   nh_inv_no_db_id,
   nh_inv_no_id,
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,
   assmbl_inv_no_db_id,
   assmbl_inv_no_id,

   inv_no_sdesc,
   inv_class_cd
) VALUES (
   '00000000000000000000000000000103',
   1234,
   103,
   1234,
   102,
   1234,
   '203',
   303,
   403,
   1234,
   101,

   'Lower1',
   'ASSY'
);

INSERT INTO eqp_assmbl_bom (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,

   assmbl_bom_cd
) VALUES (
   1234,
   '203',
   303,

   '10-10-00'
);

INSERT INTO eqp_assmbl_pos (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,

   eqp_pos_cd
) VALUES (
   1234,
   '203',
   303,
   403,

   '503'
);


-- Bottom element (tracked part) -----------------------------------------------
INSERT INTO inv_inv (
   alt_id,
   inv_no_db_id,
   inv_no_id,
   nh_inv_no_db_id,
   nh_inv_no_id,
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,
   assmbl_inv_no_db_id,
   assmbl_inv_no_id,

   inv_no_sdesc,
   inv_class_cd
) VALUES (
   '00000000000000000000000000000104',
   1234,
   104,
   1234,
   103,
   1234,
   '204',
   304,
   404,
   1234,
   103,

   'Bottom1',
   'TRK'
);


INSERT INTO eqp_assmbl_bom (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,

   assmbl_bom_cd
) VALUES (
   1234,
   '204',
   304,

   '10-10-10'
);

INSERT INTO eqp_assmbl_pos (
   assmbl_db_id,
   assmbl_cd,
   assmbl_bom_id,
   assmbl_pos_id,

   eqp_pos_cd
) VALUES (
   1234,
   '204',
   304,
   404,

   '504'
);