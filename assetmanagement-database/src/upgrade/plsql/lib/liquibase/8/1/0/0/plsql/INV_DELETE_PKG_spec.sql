--liquibase formatted sql


--changeSet INV_DELETE_PKG_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE Inv_Delete_Pkg IS
   --Procedure to delete aircraft   
/*------------------------------------ SUBTYPES ----------------------------*/
   -- Define a subtype for return codes
   SUBTYPE typn_RetCode IS NUMBER;
/*---------------------------------- Constants -----------------------------*/

   -- Basic error handling codes
   icn_Success CONSTANT typn_RetCode := 1;     -- Success
   icn_NoProc  CONSTANT typn_RetCode := 0;     -- No processing done
   icn_Error   CONSTANT typn_RetCode := -1;    -- Error

PROCEDURE DeleteAnAircraft(
       	anInvNoDbId   	IN inv_inv.inv_no_db_id%TYPE,
        anInvNoId     	IN inv_inv.inv_no_id%TYPE,
        on_Return  	OUT NUMBER );
   
PROCEDURE DeleteEvent1
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return    OUT NUMBER
);

PROCEDURE DeleteSchedEvt
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return    OUT NUMBER
);

PROCEDURE DeleteSchedInv1
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return      OUT NUMBER
);

PROCEDURE DeleteSchedInv2
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return      OUT NUMBER
);

PROCEDURE DeleteSchedRelated
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return    OUT NUMBER
);

PROCEDURE DeleteEvent2
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return    OUT NUMBER
);

PROCEDURE DeleteFault
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return    OUT NUMBER
);

PROCEDURE DeleteEvtEvent
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return    OUT NUMBER
);

PROCEDURE DeleteSchedRmvdPart
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return      OUT NUMBER
);

PROCEDURE DeleteSchedPart
(  anInvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
   anInvNoId     IN inv_inv.inv_no_id%TYPE,
   on_Return      OUT NUMBER
);

END Inv_Delete_Pkg;
/