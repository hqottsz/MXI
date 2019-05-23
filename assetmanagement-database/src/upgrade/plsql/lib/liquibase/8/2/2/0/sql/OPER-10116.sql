--liquibase formatted sql


--changeSet OPER-10116:1 stripComments:false
INSERT
    INTO
        REF_DEFERRAL_REQUEST_STATUS (
            DEFERRAL_REQUEST_STATUS_CD,
            DESC_LDESC,
            BITMAP_DB_ID,
            BITMAP_TAG
        ) 
        SELECT
            'CANCELLED',
            'Cancelled', 
            0,           
            1      
        FROM
            dual
        WHERE
            NOT EXISTS(
                SELECT
                    1
                FROM
                    REF_DEFERRAL_REQUEST_STATUS
                WHERE
                    DEFERRAL_REQUEST_STATUS_CD = 'CANCELLED'
            );

