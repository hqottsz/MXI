/******************************************************************************
* Script Filename  : eqp_data_source.sql
*
* Script Description: Customer specific data type
*                     
*                     
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2016 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/

-- 
-- Flight Usage Definition for aircraft assembly ACFT_CD1
--
INSERT 
INTO 
   eqp_data_source
   (
     assmbl_db_id, 
     assmbl_cd, 
     data_source_db_id, 
     data_source_cd, 
     data_source_name
   )
VALUES
   (
     4650,
     'ACFT_CD1',
     0,
     'MXFL',
     'MXFL - ACFT_CD1'
   );
   

INSERT 
INTO 
   eqp_data_source_spec
   (
        assmbl_db_id, 
        assmbl_cd, 
        data_source_db_id, 
        data_source_cd, 
        data_type_db_id,
        data_type_id,
        data_ord
      )
   VALUES
      (
        4650,
        'ACFT_CD1',
        0,
        'MXFL',
        0,
        10,
        1
   );
   
INSERT 
INTO 
   eqp_data_source_spec
   (
        assmbl_db_id, 
        assmbl_cd, 
        data_source_db_id, 
        data_source_cd, 
        data_type_db_id,
        data_type_id,
        data_ord
      )
   VALUES
      (
        4650,
        'ACFT_CD1',
        0,
        'MXFL',
        0,
        1,
        2
   );
   
-- 
-- Flight Usage Definition for engine assembly ENG_CD1
--
INSERT 
INTO 
   eqp_data_source
   (
     assmbl_db_id, 
     assmbl_cd, 
     data_source_db_id, 
     data_source_cd, 
     data_source_name
   )
VALUES
   (
     4650,
     'ENG_CD1',
     0,
     'MXFL',
     'MXFL - ENG_CD1'
   );
   

INSERT 
INTO 
   eqp_data_source_spec
   (
        assmbl_db_id, 
        assmbl_cd, 
        data_source_db_id, 
        data_source_cd, 
        data_type_db_id,
        data_type_id,
        data_ord
      )
   VALUES
      (
        4650,
        'ENG_CD1',
        0,
        'MXFL',
        0,
        10,
        1
   );
   
INSERT 
INTO 
   eqp_data_source_spec
   (
        assmbl_db_id, 
        assmbl_cd, 
        data_source_db_id, 
        data_source_cd, 
        data_type_db_id,
        data_type_id,
        data_ord
      )
   VALUES
      (
        4650,
        'ENG_CD1',
        0,
        'MXFL',
        0,
        1,
        2
   );
   
INSERT 
INTO 
   eqp_data_source_spec
   (
        assmbl_db_id, 
        assmbl_cd, 
        data_source_db_id, 
        data_source_cd, 
        data_type_db_id,
        data_type_id,
        data_ord
      )
   VALUES
      (
        4650,
        'ENG_CD1',
        0,
        'MXFL',
        10,
        25,
        3
   );
   
INSERT 
INTO 
   eqp_data_source_spec
   (
        assmbl_db_id, 
        assmbl_cd, 
        data_source_db_id, 
        data_source_cd, 
        data_type_db_id,
        data_type_id,
        data_ord
      )
   VALUES
      (
        4650,
        'ENG_CD1',
        0,
        'MXFL',
        10,
        27,
        4
   );
   
 -- 
 -- Flight Usage Definition for APU assembly APU_CD1
 --
 INSERT 
 INTO 
    eqp_data_source
    (
      assmbl_db_id, 
      assmbl_cd, 
      data_source_db_id, 
      data_source_cd, 
      data_source_name
    )
 VALUES
    (
      4650,
      'APU_CD1',
      0,
      'MXFL',
      'MXFL - APU_CD1'
    );
    
 
 INSERT 
 INTO 
    eqp_data_source_spec
    (
         assmbl_db_id, 
         assmbl_cd, 
         data_source_db_id, 
         data_source_cd, 
         data_type_db_id,
         data_type_id,
         data_ord
       )
    VALUES
       (
         4650,
         'APU_CD1',
         0,
         'MXFL',
         0,
         10,
         1
    );
    
 INSERT 
 INTO 
    eqp_data_source_spec
    (
         assmbl_db_id, 
         assmbl_cd, 
         data_source_db_id, 
         data_source_cd, 
         data_type_db_id,
         data_type_id,
         data_ord
       )
    VALUES
       (
         4650,
         'APU_CD1',
         0,
         'MXFL',
         0,
         1,
         2
    );
    
 INSERT 
 INTO 
    eqp_data_source_spec
    (
         assmbl_db_id, 
         assmbl_cd, 
         data_source_db_id, 
         data_source_cd, 
         data_type_db_id,
         data_type_id,
         data_ord
       )
    VALUES
       (
         4650,
         'APU_CD1',
         0,
         'MXFL',
         10,
         26,
         3
    );
    
 INSERT 
 INTO 
    eqp_data_source_spec
    (
         assmbl_db_id, 
         assmbl_cd, 
         data_source_db_id, 
         data_source_cd, 
         data_type_db_id,
         data_type_id,
         data_ord
       )
    VALUES
       (
         4650,
         'APU_CD1',
         0,
         'MXFL',
         10,
         28,
         4
   );

 --
 -- Update BULK Usage Definitions
 --
 UPDATE 
    eqp_data_source
 SET 
    data_source_name = 'BULK - ' || assmbl_cd
 WHERE 
    data_source_cd = 'BULK';