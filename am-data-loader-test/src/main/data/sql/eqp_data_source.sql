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