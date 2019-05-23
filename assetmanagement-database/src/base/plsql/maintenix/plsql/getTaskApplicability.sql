--liquibase formatted sql


--changeSet getTaskApplicability:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskApplicability
* Arguments:     aInvNoDbId, aInvNoId - pk for the inventory item
*                  aTaskApplSqlLdesc - applicability filter clause
* Description:   This function determines if applicability clause for the task
*      if applicable to the inventory item
*
* Orig.Coder:    JWurster
* Recent Coder:  jbajer
* Recent Date:   February 24, 2012
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskApplicability
(
    aInvNoDbId        NUMBER,
    aInvNoId          NUMBER,
    aTaskApplSqlLdesc STRING
)  RETURN NUMBER
IS
   ln_TaskApplies     NUMBER;
   ln_CursorHandle    NUMBER;
   li_DbmsReturn      INTEGER;

   ls_FromClause      VARCHAR2(32767);
   ls_WhereClause     VARCHAR2(32767);

   -- Aircraft
   lb_Ac_EqpPartNo    BOOLEAN := FALSE;
   lb_Ac_InvInv       BOOLEAN := FALSE;
   lb_Ac_InvAcReg     BOOLEAN := FALSE;
   lb_Ac_OrgCarrier   BOOLEAN := FALSE;

   -- Assembly
   lb_Ass_InvInv      BOOLEAN := FALSE;
   lb_Ass_EqpPartNo   BOOLEAN := FALSE;
   lb_Ass_InvOwner    BOOLEAN := FALSE;
   lb_Ass_OrgCarrier  BOOLEAN := FALSE;

   -- Component
   lb_Inv_EqpPartNo   BOOLEAN := FALSE;
   lb_Inv_EqpManufact BOOLEAN := FALSE;
   lb_Inv_InvOwner    BOOLEAN := FALSE;


BEGIN
     IF aTaskApplSqlLdesc IS NULL THEN
        RETURN 1;
     END IF;

     -- [Aircraft ...] = '...'
     lb_Ac_EqpPartNo    := instr(lower(aTaskApplSqlLdesc), 'rootpart.'     ) > 0;
     lb_Ac_InvInv       := instr(lower(aTaskApplSqlLdesc), 'ac_inv.'       ) > 0;
     lb_Ac_InvAcReg     := instr(lower(aTaskApplSqlLdesc), 'inv_ac_reg.'   ) > 0;
     lb_Ac_OrgCarrier   := instr(lower(aTaskApplSqlLdesc), 'org_carrier.'  ) > 0;
     -- [Assembly ...] = '...'
     lb_Ass_InvInv      := instr(lower(aTaskApplSqlLdesc), 'assembly.'     ) > 0;
     lb_Ass_EqpPartNo   := instr(lower(aTaskApplSqlLdesc), 'asspart.'      ) > 0;
     lb_Ass_InvOwner    := instr(lower(aTaskApplSqlLdesc), 'ass_owner.'    ) > 0;
     lb_Ass_OrgCarrier  := instr(lower(aTaskApplSqlLdesc), 'ass_carrier.'  ) > 0;
     -- [Component ...] = '...'
     lb_Inv_EqpPartNo   := instr(lower(aTaskApplSqlLdesc), 'eqp_part_no.'  ) > 0;
     lb_Inv_EqpManufact := instr(lower(aTaskApplSqlLdesc), 'eqp_manufact.' ) > 0;
     lb_Inv_InvOwner    := instr(lower(aTaskApplSqlLdesc), 'inv_owner.'    ) > 0;

     ls_FromClause := ' SELECT 1 FROM inv_inv';
     ls_WhereClause := ' WHERE inv_inv.inv_no_db_id = :inv_no_db_id AND inv_inv.inv_no_id = :inv_no_id AND inv_inv.rstat_cd = 0 ';

     -- Aircraft
     IF lb_Ac_InvInv OR lb_Ac_EqpPartNo OR lb_Ac_InvAcReg OR lb_Ac_OrgCarrier THEN
       ls_FromClause  := ls_FromClause || ', inv_inv ac_inv, inv_ac_reg ';
       ls_WhereClause := ls_WhereClause                           ||
             ' AND '                                              ||
             ' ac_inv.inv_no_db_id (+)= inv_inv.h_inv_no_db_id AND ' ||
             ' ac_inv.inv_no_id    (+)= inv_inv.h_inv_no_id '        ||
             ' AND '                                              ||
             ' inv_ac_reg.inv_no_db_id (+)= ac_inv.inv_no_db_id AND '||
             ' inv_ac_reg.inv_no_id    (+)= ac_inv.inv_no_id ';

       IF lb_Ac_EqpPartNo THEN
           ls_FromClause := ls_FromClause || ', eqp_part_no rootpart ';
           ls_WhereClause := ls_WhereClause                            ||
                 ' AND '                                               ||
                 ' rootpart.part_no_db_id (+)= ac_inv.part_no_db_id AND ' ||
                 ' rootpart.part_no_id    (+)= ac_inv.part_no_id ';
       END IF;


       IF lb_Ac_OrgCarrier THEN
           ls_FromClause := ls_FromClause || ', org_carrier ';
           ls_WhereClause := ls_WhereClause                               ||
                 ' AND '                                                  ||
                 ' org_carrier.carrier_db_id (+)= ac_inv.carrier_db_id AND ' ||
                 ' org_carrier.carrier_id    (+)= ac_inv.carrier_id ';
       END IF;
     END IF;

     -- Assembly
     IF lb_Ass_InvInv OR lb_Ass_EqpPartNo OR lb_Ass_InvOwner OR lb_Ass_OrgCarrier THEN
       ls_FromClause := ls_FromClause || ', inv_inv assembly ';
       ls_WhereClause := ls_WhereClause ||
             ' AND ' ||
             ' assembly.inv_no_db_id (+)= DECODE(inv_inv.inv_class_cd, ''ASSY'', inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id) AND ' ||
             ' assembly.inv_no_id    (+)= DECODE(inv_inv.inv_class_cd, ''ASSY'', inv_inv.inv_no_id,     inv_inv.assmbl_inv_no_id) ';

       IF lb_Ass_EqpPartNo THEN
           ls_FromClause := ls_FromClause || ', eqp_part_no asspart ';
           ls_WhereClause := ls_WhereClause                             ||
                 ' AND '                                                ||
                 ' asspart.part_no_db_id (+)= assembly.part_no_db_id AND ' ||
                 ' asspart.part_no_id    (+)= assembly.part_no_id ';
       END IF;

       IF lb_Ass_InvOwner THEN
           ls_FromClause := ls_FromClause || ', inv_owner ass_owner ';
           ls_WhereClause := ls_WhereClause                             ||
                 ' AND '                                                ||
                 ' ass_owner.owner_db_id (+)= assembly.owner_db_id AND '   ||
                 ' ass_owner.owner_id    (+)= assembly.owner_id ';
       END IF;

       IF lb_Ass_OrgCarrier THEN
           ls_FromClause := ls_FromClause || ', org_carrier ass_carrier ';
           ls_WhereClause := ls_WhereClause                                 ||
                 ' AND '                                                    ||
                 ' ass_carrier.carrier_db_id (+)= assembly.carrier_db_id AND ' ||
                 ' ass_carrier.carrier_id    (+)= assembly.carrier_id ';
       END IF;
     END IF;

     -- Component
     IF lb_Inv_EqpPartNo OR lb_Inv_EqpManufact OR lb_Inv_InvOwner THEN
       IF lb_Inv_EqpPartNo OR lb_Inv_EqpManufact THEN
           ls_FromClause := ls_FromClause || ', eqp_part_no ';
           ls_WhereClause := ls_WhereClause                                ||
                 ' AND '                                                   ||
                 ' eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND ' ||
                 ' eqp_part_no.part_no_id    = inv_inv.part_no_id ';
           IF lb_Inv_EqpManufact THEN
               ls_FromClause := ls_FromClause || ', eqp_manufact';
               ls_WhereClause := ls_WhereClause                                       ||
                     ' AND '                                                          ||
                     ' eqp_manufact.manufact_db_id = eqp_part_no.manufact_db_id AND ' ||
                     ' eqp_manufact.manufact_cd    = eqp_part_no.manufact_cd ';
           END IF;
       END IF;

       IF lb_Inv_InvOwner THEN
           ls_FromClause := ls_FromClause || ', inv_owner ';
           ls_WhereClause := ls_WhereClause                             ||
                 ' AND '                                                ||
                 ' inv_owner.owner_db_id = inv_inv.owner_db_id AND '    ||
                 ' inv_owner.owner_id    = inv_inv.owner_id ';
       END IF;
     END IF;

     ls_WhereClause := ls_WhereClause  ||
             ' AND '                   ||
             '( ' || aTaskApplSqlLdesc || ' )' ||
             ' AND ROWNUM = 1 '        ||
             ' UNION ALL '             ||
             ' SELECT 0 FROM DUAL ';

     -- open dynamic cursor
     ln_CursorHandle := DBMS_SQL.OPEN_CURSOR;

     -- parse and define variables for the SQL statement
     DBMS_SQL.PARSE(ln_CursorHandle, ls_FromClause || ls_WhereClause, DBMS_SQL.V7);
     dbms_sql.bind_variable(ln_CursorHandle, ':inv_no_db_id', aInvNoDbId);
     dbms_sql.bind_variable(ln_CursorHandle, ':inv_no_id', aInvNoId);
     DBMS_SQL.DEFINE_COLUMN(ln_CursorHandle, 1, ln_TaskApplies);

     -- excute and fetch the dynamic SQL
     li_DbmsReturn := DBMS_SQL.EXECUTE(ln_CursorHandle);
     li_DbmsReturn := DBMS_SQL.FETCH_ROWS(ln_CursorHandle);

     -- retrieves a value from the cursor into local variable ln_TaskApplies
     DBMS_SQL.COLUMN_VALUE(ln_CursorHandle, 1, ln_TaskApplies);
     DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle);

     RETURN ln_TaskApplies;

EXCEPTION
   WHEN OTHERS THEN
      ln_TaskApplies := 0;
      IF DBMS_SQL.IS_OPEN(ln_CursorHandle) THEN  DBMS_SQL.CLOSE_CURSOR(ln_CursorHandle); END IF;
   RETURN ln_TaskApplies;

END getTaskApplicability;
/