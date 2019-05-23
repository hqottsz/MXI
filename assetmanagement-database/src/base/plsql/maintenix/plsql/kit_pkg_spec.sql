--liquibase formatted sql


--changeSet kit_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "KIT_PKG" IS

   /*********************        PACKAGE VARIABLES              *******************/
   -- Constant declarations (return codes)
   icn_success CONSTANT NUMBER := 1; -- Success
   icn_failure CONSTANT NUMBER := -1; -- Unsuccessful

   /*********************        GLOBAL VARIABLES                *******************/
   gv_avoid_kit_sync BOOLEAN := FALSE;

   /*********************        PROCEDURES                     *******************/

   /********************************************************************************
   * Procedure:  setKitCondition
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *               aKitId    number  the kit part no
   *               aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedures sets the condition of the kit based on its sub-inventory
   *
   * Orig Coder:  cdaley
   * Date:        Sept 21, 2009
   *********************************************************************************
   *
   *  Copyright 2009-09-30 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setkitcondition(akitnodbid IN inv_inv.inv_no_db_id%TYPE,
                             akitnoid   IN inv_inv.inv_no_id%TYPE,
                             areturn    OUT NUMBER);

   /********************************************************************************
   * Procedure:  synchronize
   *
   * Arguments:  aInvNoDbId  the inventory to synchronize
                   aInvNoId
                   aReturn   1 if the synchronization was a success
   *
   * Description: This procedure synchronizes the specific inventory provided
   *
   * Orig Coder:  cdaley
   * Date:        Jul 22, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE synchronize(ainvnodbid IN inv_inv.inv_no_db_id%TYPE,
                         ainvnoid   IN inv_inv.inv_no_id%TYPE,
                         areturn    OUT NUMBER);

   /********************************************************************************
   * Procedure:  synchronize
   *
   * Arguments:  aReturn   1 if the synchronization was a success
   *
   * Description: This procedures synchronizes the kit inventory in the KIT_SYNC_TABLE
   *
   * Orig Coder:  cdaley
   * Date:        Jul 22, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE synchronize(areturn OUT NUMBER);

   /********************************************************************************
   * Procedure:  setKitComplete
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *               aKitId    number  the kit part no
   *               aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedures sets the complete bool for a kit based on its contents
   *
   * Orig Coder:  cdaley
   * Date:        Jul 22, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setkitcomplete(akitnodbid IN inv_inv.inv_no_db_id%TYPE,
                            akitnoid   IN inv_inv.inv_no_id%TYPE,
                            areturn    OUT NUMBER);

   /********************************************************************************
   * Procedure:  setKitOwnership
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *               aKitId    number  the kit part no
   *               aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedures sets the ownership for a kit based on its contents.
   *        If the owner/ownership is not the same for all the kit inventory, then the kit value is defaulted
   *
   * Orig Coder:  cdaley
   * Date:        Jul 22, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setkitownership(akitnodbid IN inv_inv.inv_no_db_id%TYPE,
                             akitnoid   IN inv_inv.inv_no_id%TYPE,
                             areturn    OUT NUMBER);

   /********************************************************************************
   * Procedure:  setKitShelfLife
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *               aKitId    number  the kit part no
   *               aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedures sets the shelf life for a kit based on its contents.
   *
   * Orig Coder:  cdaley
   * Date:        Jul 22, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setkitshelflife(akitnodbid IN inv_inv.inv_no_db_id%TYPE,
                             akitnoid   IN inv_inv.inv_no_id%TYPE,
                             areturn    OUT NUMBER);

   /********************************************************************************
   * Procedure:  enqueuePart
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *              aKitId    number  the kit part no
   *              aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedure enqueues the inventory based on the kit for synchronization.
   *
   * Orig Coder:  cdaley
   * Date:        Jul 15rd, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE enqueuepart(akitdbid IN eqp_part_no.part_no_db_id%TYPE,
                         akitid   IN eqp_part_no.part_no_id%TYPE,
                         areturn  OUT NUMBER);

   /********************************************************************************
   * Procedure:  enqueueInventory
   *
   * Arguments:  aKitDbId  number  the inventory db id
   *              aKitId    number  the inventory id
   *              aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedure enqueues the kit inventory for synchronization.
   *
   * Orig Coder:  cdaley
   * Date:        Jul 15rd, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE enqueueinventory(akitdbid IN inv_inv.inv_no_db_id%TYPE,
                              akitid   IN inv_inv.inv_no_id%TYPE,
                              areturn  OUT NUMBER);

   /********************************************************************************
   * Procedure:    setAbcClassificationForKit
   *
   * Arguments:   aKitPartNoDbId  number   the kit part db no
   *        aKitPartNoId    number   the kit part no
   * Return:      1 if updated successfully
   * Description: This procedure updates the recalculated ABC Classification code for a kit part
   *
   * Orig Coder:  sarya
   * Date:        Jul 16, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/

   PROCEDURE setabcclassificationforkit(apartnodbid IN NUMBER,
                                        apartnoid   IN NUMBER,
                                        areturn     OUT NUMBER);

   /*********************        FUNCTIONS                     ********************/

   /********************************************************************************
   *
   * Function:      isEditable
   * Arguments:     aKitPartDbId      - kit part db id
   *                aKitPartId        - kit part id
   *                aKitPartGroupDbId - kit part group map db id
   *                aKitPartGroupId   - kit part group map id
   *                aAltPartDbId      - alternate part db id
   *                aAltPartId        - alternate part id
   * Description:   This function determines if the alternate part is editable
   *                on Edit Kit Contents page
   *
   * Returns:       1 if more than one alternate in kit line and no aleternate in
   *                other kit lines, otherwise 0.
   *
   * Created: Libin Cai
   * Date:    July 22, 2009
   *
   *********************************************************************************/
   FUNCTION iseditable(akitpartdbid      eqp_kit_part_group_map.kit_part_no_db_id%TYPE,
                       akitpartid        eqp_kit_part_group_map.kit_part_no_id%TYPE,
                       akitpartgroupdbid eqp_kit_part_group_map.eqp_kit_part_group_db_id%TYPE,
                       akitpartgroupid   eqp_kit_part_group_map.eqp_kit_part_group_id%TYPE,
                       aaltpartdbid      eqp_part_no.part_no_db_id%TYPE,
                       aaltpartid        eqp_part_no.part_no_id%TYPE)
      RETURN NUMBER;

   /********************************************************************************
   * Function: getPossibleKitsForLocation
   *
   * Arguments:  aKitDbId  number  the kit part no
   *          aKitId   number
   *          aLocDbId number  the location to check
   *          aLocId   number
   * Return:      the number of possible kits at the specified location  (number)
   * Description:   This function returns the possible number of kits that can be created based on the inventory at a given location.
   *                 For the inventory to count it must be: uninstalled, serviceable, not kitted, located at the supply location.  Reserved/Unreserved status does not matter.
   *
   * Orig Coder: cdaley
   * Date: Jun 23rd, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   FUNCTION getpossiblekitsforlocation(
                                       -- the kit part no
                                       akitdbid eqp_part_no.part_no_db_id%TYPE,
                                       akitid   eqp_part_no.part_no_id%TYPE,
                                       -- the location to check
                                       alocdbid inv_loc.loc_db_id%TYPE,
                                       alocid   inv_loc.loc_id%TYPE) RETURN NUMBER;

   /********************************************************************************
   * Function:    getKitEstimatedValue
   *
   * Arguments:   aKitPartNoDbId  number the kit part DB no
   *        aKitPartNoId    number  the kit part no
   * Return:      the estimated value for a kit part
   * Description: This function returns the estimated value for the kit part
   *
   * Orig Coder:  sarya
   * Date:        Jun 24rd, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   FUNCTION getkitestimatedvalue(akitpartnodbid eqp_kit_part_group_map.kit_part_no_db_id %TYPE,
                                 akitpartnoid   eqp_kit_part_group_map.kit_part_no_id %TYPE)
      RETURN NUMBER;

   /********************************************************************************
   * Function:    getABCClassificationForKit
   *
   * Arguments:   aKitPartNoDbId  number   the kit part DB no
   *        aKitPartNoId    number   the kit part no
   * Return:      the ABC Classification code for a kit part
   * Description: This function returns the ABC Classification code for a kit part
   *
   * Orig Coder:  sarya
   * Date:        Jul 14rd, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   FUNCTION getabcclassificationforkit(akitpartnodbid eqp_kit_part_group_map.kit_part_no_db_id %TYPE,
                                       akitpartnoid   eqp_kit_part_group_map.kit_part_no_id %TYPE)
      RETURN ref_abc_class.abc_class_cd%TYPE;

   /********************************************************************************
   * Function:    getActualKitLineQty
   *
   * Arguments:   aKitInvNoDbId      number   the kit inv DB no
   *        aKitInvNoId        number   the kit inv no
   *                    aKitPartGroupDbId  number   the kit part group DB no
   *                    aKitPartGroupId    number   the kit part group no
   * Return:      the sum(inv_inv.bin_qt) the actual kit line quantity
   * Description: This function returns the sum(inv_inv.bin_qt) the actual kit line quantity for a kit line
   *
   * Orig Coder:  sarya
   * Date:        Jul 21st, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   FUNCTION getactualkitlineqty(akitinvnodbid     inv_kit_map.kit_inv_no_db_id %TYPE,
                                akitinvnoid       inv_kit_map.kit_inv_no_id %TYPE,
                                akitpartgroupdbid inv_kit_map.eqp_kit_part_group_db_id %TYPE,
                                akitpartgroupid   inv_kit_map.eqp_kit_part_group_id %TYPE)
      RETURN inv_inv.bin_qt%TYPE;

   /********************************************************************************
   * Function:    isKitComplete
   *
   * Arguments:  aKitInvNoDbId      number   the kit inv DB no
   *         aKitInvNoId        number   the kit inv no

   * Return:      1 if the kit is complete, 0 otherwise
   * Description: This function returns whether or not the kit is complete
   *
   * Orig Coder:  cdaley
   * Date:        Jul 22, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   FUNCTION iskitcomplete(akitnodbid inv_inv.inv_no_db_id%TYPE,
                          akitnoid   inv_inv.inv_no_id%TYPE) RETURN NUMBER;

END kit_pkg;
/