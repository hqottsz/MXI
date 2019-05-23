--liquibase formatted sql


--changeSet kit_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "KIT_PKG" IS

   /*********************        Local Types                    *******************/
   kit_complete EXCEPTION;
   kit_shelf_life EXCEPTION;
   kit_ownership EXCEPTION;
   kit_partgroups EXCEPTION;
   kit_condition EXCEPTION;

   /*********************        PROCEDURES                     *******************/

   /********************************************************************************
   * Procedure:  setKitMapPartGroups
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *              aKitId    number  the kit part no
   *              aReturn   1 if no error occurred
   *
   * Description:   This procedure sets the EQP_KIT_PART_GROUP key on inv_kit_map for
   *                 any inventory that has a null FK, but a mapped part number in the kit definition
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
   PROCEDURE setkitmappartgroups(akitnodbid IN inv_inv.inv_no_db_id%TYPE,
                                 akitnoid   IN inv_inv.inv_no_id%TYPE,
                                 areturn    OUT NUMBER) IS
   BEGIN

      UPDATE inv_kit_map
         SET (inv_kit_map.eqp_kit_part_group_db_id, inv_kit_map.eqp_kit_part_group_id) = (SELECT eqp_kit_part_group_map.eqp_kit_part_group_db_id,
                                                                                                 eqp_kit_part_group_map.eqp_kit_part_group_id
                                                                                            FROM inv_inv
                                                                                           INNER JOIN eqp_kit_part_group_map ON inv_inv.part_no_db_id =
                                                                                                                                eqp_kit_part_group_map.kit_part_no_db_id
                                                                                                                            AND inv_inv.part_no_id =
                                                                                                                                eqp_kit_part_group_map.kit_part_no_id
                                                                                           INNER JOIN eqp_kit_part_map ON eqp_kit_part_group_map.eqp_kit_part_group_db_id =
                                                                                                                          eqp_kit_part_map.eqp_kit_part_group_db_id
                                                                                                                      AND eqp_kit_part_group_map.eqp_kit_part_group_id =
                                                                                                                          eqp_kit_part_map.eqp_kit_part_group_id
                                                                                           INNER JOIN inv_inv sub_inv ON eqp_kit_part_map.part_no_db_id =
                                                                                                                         sub_inv.part_no_db_id
                                                                                                                     AND eqp_kit_part_map.part_no_id =
                                                                                                                         sub_inv.part_no_id
                                                                                           WHERE inv_inv.inv_no_db_id =
                                                                                                 inv_kit_map.kit_inv_no_db_id
                                                                                             AND inv_inv.inv_no_id =
                                                                                                 inv_kit_map.kit_inv_no_id
                                                                                             AND inv_inv.rstat_cd = 0
                                                                                             AND sub_inv.inv_no_db_id =
                                                                                                 inv_kit_map.inv_no_db_id
                                                                                             AND sub_inv.inv_no_id =
                                                                                                 inv_kit_map.inv_no_id
                                                                                             AND sub_inv.rstat_cd = 0)
       WHERE inv_kit_map.kit_inv_no_db_id = akitnodbid
         AND inv_kit_map.kit_inv_no_id = akitnoid
         AND inv_kit_map.eqp_kit_part_group_db_id IS NULL
         AND inv_kit_map.eqp_kit_part_group_id IS NULL;

      areturn := icn_success;
   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.setKitMapPartGroups:' ||
                                            SQLERRM);
   END setkitmappartgroups;

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
                             areturn    OUT NUMBER) IS
      ikitserviceable        NUMBER;
      ikitunserviceablecount NUMBER;
   BEGIN

      --determine if the kit is already unserviceable
      SELECT ref_inv_cond.srv_bool
        INTO ikitserviceable
        FROM inv_inv
       INNER JOIN ref_inv_cond ON inv_inv.inv_cond_db_id =
                                  ref_inv_cond.inv_cond_db_id
                              AND inv_inv.inv_cond_cd =
                                  ref_inv_cond.inv_cond_cd
       WHERE inv_inv.inv_no_db_id = akitnodbid
         AND inv_inv.inv_no_id = akitnoid
         AND inv_inv.rstat_cd = 0;

      -- if the kit is unserviceable and incomplete, then do nothing
      IF ikitserviceable = 0
      THEN
         areturn := icn_success;
         RETURN;
      END IF;

      --determine how many sub-kit inventory are unserviceable
      SELECT COUNT(*)
        INTO ikitunserviceablecount
        FROM inv_kit_map
       INNER JOIN inv_inv ON inv_kit_map.inv_no_db_id = inv_inv.inv_no_db_id
                         AND inv_kit_map.inv_no_id = inv_inv.inv_no_id
       INNER JOIN ref_inv_cond ON inv_inv.inv_cond_db_id =
                                  ref_inv_cond.inv_cond_db_id
                              AND inv_inv.inv_cond_cd =
                                  ref_inv_cond.inv_cond_cd
       WHERE inv_kit_map.kit_inv_no_db_id = akitnodbid
         AND inv_kit_map.kit_inv_no_id = akitnoid
         AND ref_inv_cond.srv_bool = 0;

      IF ikitunserviceablecount > 0
      THEN
         UPDATE inv_inv
            SET inv_inv.inv_cond_db_id = 0,
                inv_inv.inv_cond_cd    = 'INSPREQ'
          WHERE inv_inv.inv_no_db_id = akitnodbid
            AND inv_inv.inv_no_id = akitnoid
            AND inv_inv.rstat_cd = 0;
      END IF;

      areturn := icn_success;

   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.SETKITCONDITION:' ||
                                            SQLERRM);
   END setkitcondition;

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
                         areturn    OUT NUMBER) IS
      ireturn NUMBER;
   BEGIN
      -- update the kit mappings
      kit_pkg.setkitmappartgroups(ainvnodbid,
                                  ainvnoid,
                                  ireturn);

      IF ireturn = icn_failure
      THEN
         RAISE kit_partgroups;
      END IF;

      -- update the completeness
      kit_pkg.setkitcomplete(ainvnodbid,
                             ainvnoid,
                             ireturn);

      IF ireturn = icn_failure
      THEN
         RAISE kit_complete;
      END IF;

      -- update the shelf life
      kit_pkg.setkitshelflife(ainvnodbid,
                              ainvnoid,
                              ireturn);

      IF ireturn = icn_failure
      THEN
         RAISE kit_shelf_life;
      END IF;

      -- update the ownership
      kit_pkg.setkitownership(ainvnodbid,
                              ainvnoid,
                              ireturn);

      IF ireturn = icn_failure
      THEN
         RAISE kit_ownership;
      END IF;

      -- update the kit condition
      kit_pkg.setkitcondition(ainvnodbid,
                              ainvnoid,
                              ireturn);

      IF ireturn = icn_failure
      THEN
         RAISE kit_condition;
      END IF;

      areturn := icn_success;
   EXCEPTION
      WHEN kit_partgroups THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.synchronize-setting kit part group mappings:' ||
                                            SQLERRM);
         RAISE kit_partgroups;
      WHEN kit_complete THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.synchronize-setting kit complete:' ||
                                            SQLERRM);
         RAISE kit_complete;
      WHEN kit_shelf_life THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.synchronize-setting kit shelf life:' ||
                                            SQLERRM);
         RAISE kit_shelf_life;
      WHEN kit_ownership THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.synchronize-setting kit ownership:' ||
                                            SQLERRM);
         RAISE kit_ownership;
      WHEN kit_condition THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.synchronize-setting kit condition:' ||
                                            SQLERRM);
         RAISE kit_condition;
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.synchronize:' || SQLERRM);
         RAISE;
   END;

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
   PROCEDURE synchronize(areturn OUT NUMBER) IS
      -- pl/sql type to store inventory records
      TYPE typeinvsyncrecord IS RECORD(
         invnodbid inv_inv.inv_no_db_id%TYPE,
         invnoid   inv_inv.inv_no_id%TYPE);
      TYPE typeinvsynctable IS TABLE OF typeinvsyncrecord;

      ireturn         NUMBER;
      iinventorytable typeinvsynctable;

   BEGIN
      SAVEPOINT inventory_sync_start;
      -- get the list of kits that need to be synchronized
      SELECT DISTINCT inv_no_db_id,
                      inv_no_id BULK COLLECT
        INTO iinventorytable
        FROM kit_sync_queue;

      FOR i IN 1 .. iinventorytable.COUNT
      LOOP

         SAVEPOINT inventory_sync_start;

         -- synchronize the inventory
         kit_pkg.synchronize(iinventorytable(i).invnodbid,
                             iinventorytable(i).invnoid,
                             ireturn);

         -- remove the synchronized inventory from the table
         DELETE FROM kit_sync_queue
          WHERE kit_sync_queue.inv_no_db_id = iinventorytable(i)
         .invnodbid
            AND kit_sync_queue.inv_no_id = iinventorytable(i).invnoid;
      END LOOP;

      areturn := icn_success;
   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         ROLLBACK TO inventory_sync_start;
   END synchronize;

   /********************************************************************************
   * Procedure:  setKitComplete
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *              aKitId    number  the kit part no
   *              aReturn   number  the number indicating if the operation was a success
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
                            areturn    OUT NUMBER) IS
      icomplete NUMBER;
   BEGIN

      icomplete := kit_pkg.iskitcomplete(akitnodbid,
                                         akitnoid);

      UPDATE inv_kit
         SET inv_kit.kit_complete_bool = icomplete
       WHERE inv_kit.inv_no_db_id = akitnodbid
         AND inv_kit.inv_no_id = akitnoid
         AND inv_kit.rstat_cd = 0;

      areturn := icn_success;
   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.SETKITCOMPLETE:' ||
                                            SQLERRM);
   END setkitcomplete;

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
                             areturn    OUT NUMBER) IS
      -- pl/sql type to store the inventory owners
      TYPE typeinvownerrecord IS RECORD(
         invownerdbid     inv_inv.owner_db_id%TYPE,
         invownerid       inv_inv.owner_id%TYPE,
         invownershipdbid ref_owner_type.owner_type_db_id%TYPE,
         invownershipcd   ref_owner_type.owner_type_cd%TYPE);
      TYPE typeinvowner IS TABLE OF typeinvownerrecord;
      iowners typeinvowner;

   BEGIN

      -- get the list of owners
      SELECT inv_inv.owner_db_id,
             inv_inv.owner_id,
             inv_inv.owner_type_db_id,
             inv_inv.owner_type_cd BULK COLLECT
        INTO iowners
        FROM inv_inv
       INNER JOIN inv_kit_map ON inv_inv.inv_no_db_id =
                                 inv_kit_map.inv_no_db_id
                             AND inv_inv.inv_no_id = inv_kit_map.inv_no_id
                             AND inv_inv.rstat_cd = 0
       WHERE inv_kit_map.kit_inv_no_db_id = akitnodbid
         AND inv_kit_map.kit_inv_no_id = akitnoid
       GROUP BY inv_inv.owner_db_id,
                inv_inv.owner_id,
                inv_inv.owner_type_db_id,
                inv_inv.owner_type_cd;

      -- if there is only 1 owner for all kitted inventory, set the the owner/owner type for the kit to match
      IF iowners.COUNT = 1
      THEN
         UPDATE inv_inv
            SET inv_inv.owner_db_id      = iowners(1).invownerdbid,
                inv_inv.owner_id         = iowners(1).invownerid,
                inv_inv.owner_type_db_id = iowners(1).invownershipdbid,
                inv_inv.owner_type_cd    = iowners(1).invownershipcd
          WHERE inv_inv.inv_no_db_id = akitnodbid
            AND inv_inv.inv_no_id = akitnoid
            AND inv_inv.rstat_cd = 0;
         -- if there are multiple owners, then set the kit to the default values
      ELSE
         UPDATE inv_inv
            SET (inv_inv.owner_db_id, inv_inv.owner_id, inv_inv.owner_type_db_id, inv_inv.owner_type_cd) = (SELECT inv_owner.owner_db_id,
                                                                                                                   inv_owner.owner_id,
                                                                                                                   0,
                                                                                                                   'OTHER'
                                                                                                              FROM inv_owner
                                                                                                             WHERE inv_owner.local_bool = 1
                                                                                                               AND inv_owner.default_bool = 1)
          WHERE inv_inv.inv_no_db_id = akitnodbid
            AND inv_inv.inv_no_id = akitnoid
            AND inv_inv.rstat_cd = 0;
      END IF;

      areturn := icn_success;

   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.SETKITOWNERSHIP:' ||
                                            SQLERRM);

   END setkitownership;

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
                             areturn    OUT NUMBER) IS
   BEGIN

      -- set the kit shelf life to be the minimum of all inventory assigned to it.
      UPDATE inv_inv
         SET inv_inv.shelf_expiry_dt = (SELECT MIN(inv_inv.shelf_expiry_dt)
                                          FROM inv_inv
                                         INNER JOIN inv_kit_map ON inv_inv.inv_no_db_id =
                                                                   inv_kit_map.inv_no_db_id
                                                               AND inv_inv.inv_no_id =
                                                                   inv_kit_map.inv_no_id
                                         WHERE inv_kit_map.kit_inv_no_db_id =
                                               akitnodbid
                                           AND inv_kit_map.kit_inv_no_id =
                                               akitnoid)
       WHERE inv_inv.inv_no_db_id = akitnodbid
         AND inv_inv.inv_no_id = akitnoid
         AND inv_inv.rstat_cd = 0;

      areturn := icn_success;
   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.SETKITSHELFLIFE:' ||
                                            SQLERRM);
   END setkitshelflife;

   /********************************************************************************
   * Procedure:  enqueue
   *
   * Arguments:  aKitDbId  number  the kit part DB no
   *              aKitId    number  the kit part no
   *              aReturn   number  the number indicating if the operation was a success
   *
   * Description: This procedure enqueues the kit for synchronization.
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
                         areturn  OUT NUMBER) IS

   BEGIN
      -- insert the kit into the queue table
      INSERT INTO kit_sync_queue
         (inv_no_db_id,
          inv_no_id)
         SELECT inv_no_db_id,
                inv_no_id
           FROM inv_inv
          WHERE inv_inv.part_no_db_id = akitdbid
            AND inv_inv.part_no_id = akitid
            AND inv_inv.inv_class_db_id = 0
            AND inv_inv.inv_class_cd = 'KIT';

      areturn := icn_success;
   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.ENQUEUEPART:' || SQLERRM);
   END enqueuepart;

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
                              areturn  OUT NUMBER) IS
   BEGIN
      -- insert the kit into the queue table
      IF NOT gv_avoid_kit_sync
      THEN
         INSERT INTO kit_sync_queue
            (inv_no_db_id,
             inv_no_id)
         VALUES
            (akitdbid,
             akitid);
      END IF;
      areturn := icn_success;
   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            'KIT_PKG.ENQUEUEPART:' || SQLERRM);
   END enqueueinventory;

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
                                        areturn     OUT NUMBER) IS

      -- local variable
      labcclasscd ref_abc_class.abc_class_cd%TYPE;

   BEGIN

      -- get the recalculated value for the ABC Classification property of a kit part
      labcclasscd := kit_pkg.getabcclassificationforkit(apartnodbid,
                                                        apartnoid);

      -- set the new value for the ABC Classification property for a kit part
      UPDATE eqp_part_no
         SET eqp_part_no.abc_class_cd = labcclasscd
       WHERE eqp_part_no.part_no_db_id = apartnodbid
         AND eqp_part_no.part_no_id = apartnoid
         AND eqp_part_no.rstat_cd = 0;

      areturn := icn_success;

   EXCEPTION
      WHEN OTHERS THEN
         areturn := icn_failure;
         application_object_pkg.setmxierror('DEV-99999',
                                            SQLERRM);

   END setabcclassificationforkit;

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
      RETURN NUMBER IS
      ln_altpartsinsamekitline NUMBER;
      ln_partinotherkitlines   NUMBER;

   BEGIN
      ln_altpartsinsamekitline := 0;
      ln_partinotherkitlines   := 0;

      /* find the numbers of alternate part number in the same kit line */
      SELECT COUNT(*)
        INTO ln_altpartsinsamekitline
        FROM eqp_kit_part_map
       WHERE eqp_kit_part_map.eqp_kit_part_group_db_id = akitpartgroupdbid
         AND eqp_kit_part_map.eqp_kit_part_group_id = akitpartgroupid;

      /* find the numbers of the alternate part number (aAltPartDbId:aAltPartId) specified by other kit lines in same kit */
      SELECT COUNT(*)
        INTO ln_partinotherkitlines
        FROM eqp_kit_part_group_map
       INNER JOIN eqp_kit_part_map ON eqp_kit_part_map.eqp_kit_part_group_db_id =
                                      eqp_kit_part_group_map.eqp_kit_part_group_db_id
                                  AND eqp_kit_part_map.eqp_kit_part_group_id =
                                      eqp_kit_part_group_map.eqp_kit_part_group_id
       WHERE eqp_kit_part_group_map.kit_part_no_db_id = akitpartdbid
         AND eqp_kit_part_group_map.kit_part_no_id = akitpartid
         AND NOT (eqp_kit_part_group_map.eqp_kit_part_group_db_id =
              akitpartgroupdbid AND
              eqp_kit_part_group_map.eqp_kit_part_group_id = akitpartgroupid)
         AND eqp_kit_part_map.part_no_db_id = aaltpartdbid
         AND eqp_kit_part_map.part_no_id = aaltpartid;

      -- return '1' (editable) if the number of alternate parts in the same kit line > 1 and this part number is not specified by another kit in same kit.
      -- Otherwise, return '0' (non-editable).
      IF (ln_altpartsinsamekitline > 1 AND ln_partinotherkitlines = 0)
      THEN
         RETURN 1;
      ELSE
         RETURN 0;
      END IF;

   END iseditable;

   /********************************************************************************
   * Function: getPossibleKitsForLocation
   *
   * Arguments:  aKitDbId  number  the kit part no
   *       aKitId    number
   *       aLocDbId  number  the supply location
   *       aLocId    number
   * Return:   the number of possible kits at the specified location (number)
   * Description:  This function returns the possible number of kits that can be created based on the inventory at a given supply location.  For the inventory to count it must be: uninstalled, serviceable, not kitted, located at the supply location.  Reserved/Unreserved status does not matter.
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
                                       alocid   inv_loc.loc_id%TYPE) RETURN NUMBER IS
      -- store the possible number of kits for the location
      lpossiblekits NUMBER;
   BEGIN

      --default the possible kits to 0
      lpossiblekits := 0;

      -- query used to determine the number of available kits at the location
      SELECT
      -- get the smallest number available for all parts as the number of kits
       nvl(MIN(kit_values),
           0)
        INTO lpossiblekits
        FROM (SELECT
              -- get the total inventory quantity available for that bom_part and divide by number required for a kit
               floor(SUM(inv_qt) / kit_qt) AS kit_values
                FROM (SELECT vw_kit_baseline.kit_bom_part_db_id,
                              vw_kit_baseline.kit_bom_part_id,
                              vw_kit_baseline.kit_qt,
                              -- only count inventory that is not in a kit, else 0
                              -- only count inventory that is serviceable and not installed
                              -- if it is serialized, then inventory quanitity is 1, else it is batch then use bin_qt
                              nvl2(inv_kit_map.inv_no_db_id,
                                   0,
                                   CASE
                                      WHEN ref_inv_cond.srv_bool = 1
                                           AND inv_at_supply_loc.nh_inv_no_db_id IS NULL THEN
                                       CASE ref_inv_class.serial_bool
                                      WHEN 1 THEN
                                       1
                                      ELSE
                                       inv_at_supply_loc.bin_qt
                                   END ELSE 0 END) AS inv_qt
                         FROM vw_kit_baseline
                         LEFT OUTER JOIN (SELECT inv_inv.inv_no_db_id,
                                                inv_inv.inv_no_id,
                                                inv_inv.part_no_db_id,
                                                inv_inv.part_no_id,
                                                inv_inv.inv_cond_db_id,
                                                inv_inv.inv_cond_cd,
                                                inv_inv.nh_inv_no_db_id,
                                                inv_inv.nh_inv_no_id,
                                                inv_inv.inv_class_db_id,
                                                inv_inv.inv_class_cd,
                                                inv_inv.bin_qt
                                           FROM inv_inv
                                          INNER JOIN inv_loc ON inv_inv.loc_db_id =
                                                                inv_loc.loc_db_id
                                                            AND inv_inv.loc_id =
                                                                inv_loc.loc_id
                                                            AND inv_inv.rstat_cd = 0
                                          WHERE inv_loc.supply_loc_db_id =
                                                alocdbid
                                            AND inv_loc.supply_loc_id = alocid) inv_at_supply_loc ON vw_kit_baseline.kit_item_part_no_db_id =
                                                                                                     inv_at_supply_loc.part_no_db_id
                                                                                                 AND vw_kit_baseline.kit_item_part_no_id =
                                                                                                     inv_at_supply_loc.part_no_id
                         LEFT OUTER JOIN ref_inv_cond ON inv_at_supply_loc.inv_cond_db_id =
                                                         ref_inv_cond.inv_cond_db_id
                                                     AND inv_at_supply_loc.inv_cond_cd =
                                                         ref_inv_cond.inv_cond_cd
                         LEFT OUTER JOIN ref_inv_class ON inv_at_supply_loc.inv_class_db_id =
                                                          ref_inv_class.inv_class_db_id
                                                      AND inv_at_supply_loc.inv_class_cd =
                                                          ref_inv_class.inv_class_cd
                         LEFT OUTER JOIN inv_kit_map ON inv_kit_map.inv_no_db_id =
                                                        inv_at_supply_loc.inv_no_db_id
                                                    AND inv_kit_map.inv_no_id =
                                                        inv_at_supply_loc.inv_no_id
                        WHERE
                       -- get all the parts in the kit
                        vw_kit_baseline.kit_part_no_db_id = akitdbid
                     AND vw_kit_baseline.kit_part_no_id = akitid)
               GROUP BY kit_bom_part_db_id,
                        kit_bom_part_id,
                        kit_qt);

      RETURN lpossiblekits;

   EXCEPTION
      -- handles a zero division error caused if the kit_qt is set to 0.  This data would be invalid
      WHEN zero_divide THEN
         RETURN 0;
   END getpossiblekitsforlocation;

   /********************************************************************************
   * Function:    getKitEstimatedValue
   *
   * Arguments:   aKitPartNoDbId  number the kit part DB no
   *        aKitPartNoId    number  the kit part no
   * Return:      the estimated value for a kit part
   * Description: This function returns the estimated value for the kit part
   *
   * Orig Coder:  sarya
   * Date:        Jun 24, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/

   FUNCTION getkitestimatedvalue(akitpartnodbid eqp_kit_part_group_map.kit_part_no_db_id %TYPE,
                                 akitpartnoid   eqp_kit_part_group_map.kit_part_no_id %TYPE)
      RETURN NUMBER IS
      lestimatedvalue NUMBER;
      lpartqty        NUMBER;
      lavgunitprice   NUMBER;

      CURSOR lkitgroups IS
      /*get all the part groups having alterate parts maped with the kit part*/
         SELECT COUNT(*) linecount,
                eqp_kit_part_group_map.eqp_kit_part_group_db_id,
                eqp_kit_part_group_map.eqp_kit_part_group_id
           FROM eqp_kit_part_group_map
          INNER JOIN eqp_kit_part_groups ON eqp_kit_part_groups.eqp_kit_part_group_db_id =
                                            eqp_kit_part_group_map.eqp_kit_part_group_db_id
                                        AND eqp_kit_part_groups.eqp_kit_part_group_id =
                                            eqp_kit_part_group_map.eqp_kit_part_group_id
          INNER JOIN eqp_kit_part_map ON eqp_kit_part_map.eqp_kit_part_group_db_id =
                                         eqp_kit_part_groups.eqp_kit_part_group_db_id
                                     AND eqp_kit_part_map.eqp_kit_part_group_id =
                                         eqp_kit_part_groups.eqp_kit_part_group_id
          INNER JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                                    eqp_kit_part_group_map.kit_part_no_db_id
                                AND eqp_part_no.part_no_id =
                                    eqp_kit_part_group_map.kit_part_no_id
          WHERE eqp_kit_part_group_map.kit_part_no_db_id = akitpartnodbid
            AND eqp_kit_part_group_map.kit_part_no_id = akitpartnoid
          GROUP BY eqp_kit_part_group_map.eqp_kit_part_group_db_id,
                   eqp_kit_part_group_map.eqp_kit_part_group_id;

      lkitgroup lkitgroups%ROWTYPE;

   BEGIN
      lestimatedvalue := 0;

      OPEN lkitgroups;
      LOOP
         FETCH lkitgroups
            INTO lkitgroup;
         EXIT WHEN lkitgroups%NOTFOUND;
         lpartqty      := 0;
         lavgunitprice := 0;
         /*if a group has more than one alternate, make sure to get the standard one*/
         IF lkitgroup.linecount > 1
         THEN
            SELECT eqp_kit_part_groups.kit_qt AS part_qty,
                   eqp_part_no.avg_unit_price AS avg_unit_price
              INTO lpartqty,
                   lavgunitprice
              FROM eqp_kit_part_groups
             INNER JOIN eqp_kit_part_map ON eqp_kit_part_map.eqp_kit_part_group_db_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_db_id
                                        AND eqp_kit_part_map.eqp_kit_part_group_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_id
             INNER JOIN eqp_bom_part ON eqp_bom_part.bom_part_db_id =
                                        eqp_kit_part_groups.bom_part_db_id
                                    AND eqp_bom_part.bom_part_id =
                                        eqp_kit_part_groups.bom_part_id
             INNER JOIN eqp_part_baseline ON eqp_part_baseline.bom_part_db_id =
                                             eqp_bom_part.bom_part_db_id
                                         AND eqp_part_baseline.bom_part_id =
                                             eqp_bom_part.bom_part_id
                                         AND eqp_part_baseline.part_no_db_id =
                                             eqp_kit_part_map.part_no_db_id
                                         AND eqp_part_baseline.part_no_id =
                                             eqp_kit_part_map.part_no_id
             INNER JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                                       eqp_part_baseline.part_no_db_id
                                   AND eqp_part_no.part_no_id =
                                       eqp_part_baseline.part_no_id

             WHERE eqp_kit_part_groups.eqp_kit_part_group_db_id =
                   lkitgroup.eqp_kit_part_group_db_id
               AND eqp_kit_part_groups.eqp_kit_part_group_id =
                   lkitgroup.eqp_kit_part_group_id
               AND eqp_part_baseline.standard_bool = 1;

         ELSE

            SELECT eqp_kit_part_groups.kit_qt AS part_qty,
                   eqp_part_no.avg_unit_price AS avg_unit_price
              INTO lpartqty,
                   lavgunitprice
              FROM eqp_kit_part_groups
             INNER JOIN eqp_kit_part_map ON eqp_kit_part_map.eqp_kit_part_group_db_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_db_id
                                        AND eqp_kit_part_map.eqp_kit_part_group_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_id
             INNER JOIN eqp_bom_part ON eqp_bom_part.bom_part_db_id =
                                        eqp_kit_part_groups.bom_part_db_id
                                    AND eqp_bom_part.bom_part_id =
                                        eqp_kit_part_groups.bom_part_id
             INNER JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                                       eqp_kit_part_map.part_no_db_id
                                   AND eqp_part_no.part_no_id =
                                       eqp_kit_part_map.part_no_id

             WHERE eqp_kit_part_groups.eqp_kit_part_group_db_id =
                   lkitgroup.eqp_kit_part_group_db_id
               AND eqp_kit_part_groups.eqp_kit_part_group_id =
                   lkitgroup.eqp_kit_part_group_id;
         END IF;

         lestimatedvalue := lestimatedvalue + (lpartqty * lavgunitprice);

      END LOOP;
      CLOSE lkitgroups;
      RETURN lestimatedvalue;

   END getkitestimatedvalue;

   /********************************************************************************
   * Function:    getABCClassificationForKit
   *
   * Arguments:   aKitPartNoDbId  number   the kit part db no
   *        aKitPartNoId    number   the kit part no
   * Return:      the ABC Classification code for a kit part
   * Description: This function returns the ABC Classification code for a kit part
   *
   * Orig Coder:  sarya
   * Date:        Jul 14, 2009
   *********************************************************************************
   *
   *  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   FUNCTION getabcclassificationforkit(akitpartnodbid eqp_kit_part_group_map.kit_part_no_db_id %TYPE,
                                       akitpartnoid   eqp_kit_part_group_map.kit_part_no_id %TYPE)
      RETURN ref_abc_class.abc_class_cd%TYPE IS
      labcclasscd ref_abc_class.abc_class_cd%TYPE;

   BEGIN
      SELECT ref_abc_class.abc_class_cd
        INTO labcclasscd
        FROM ref_abc_class
       WHERE ref_abc_class.value_ord =
             nvl((SELECT MIN(ref_abc_class.value_ord)
                   FROM eqp_kit_part_group_map
                  INNER JOIN eqp_kit_part_groups ON eqp_kit_part_groups.eqp_kit_part_group_db_id =
                                                    eqp_kit_part_group_map.eqp_kit_part_group_db_id
                                                AND eqp_kit_part_groups.eqp_kit_part_group_id =
                                                    eqp_kit_part_group_map.eqp_kit_part_group_id
                   LEFT OUTER JOIN eqp_kit_part_map ON eqp_kit_part_groups.eqp_kit_part_group_db_id =
                                                       eqp_kit_part_map.eqp_kit_part_group_db_id
                                                   AND eqp_kit_part_groups.eqp_kit_part_group_id =
                                                       eqp_kit_part_map.eqp_kit_part_group_id
                  INNER JOIN eqp_part_no ON eqp_part_no.part_no_db_id =
                                            eqp_kit_part_map.part_no_db_id
                                        AND eqp_part_no.part_no_id =
                                            eqp_kit_part_map.part_no_id
                   LEFT OUTER JOIN ref_abc_class ON ref_abc_class.abc_class_db_id =
                                                    eqp_part_no.abc_class_db_id
                                                AND ref_abc_class.abc_class_cd =
                                                    eqp_part_no.abc_class_cd
                  WHERE eqp_kit_part_group_map.kit_part_no_db_id =
                        akitpartnodbid
                    AND eqp_kit_part_group_map.kit_part_no_id = akitpartnoid),
                 (SELECT MIN(ref_abc_class.value_ord)
                    FROM ref_abc_class)

                 );

      RETURN labcclasscd;

   END getabcclassificationforkit;

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
      RETURN inv_inv.bin_qt%TYPE

    IS
      lactualkitlineqty inv_inv.bin_qt%TYPE;

   BEGIN
      lactualkitlineqty := 0;

      SELECT SUM(nvl(inv_inv.bin_qt,
                     1))
        INTO lactualkitlineqty
        FROM inv_kit_map
       INNER JOIN inv_inv ON inv_inv.inv_no_db_id = inv_kit_map.inv_no_db_id
                         AND inv_inv.inv_no_id = inv_kit_map.inv_no_id
                         AND inv_inv.rstat_cd = 0
       WHERE inv_kit_map.kit_inv_no_db_id = akitinvnodbid
         AND inv_kit_map.kit_inv_no_id = akitinvnoid
         AND inv_kit_map.eqp_kit_part_group_db_id = akitpartgroupdbid
         AND inv_kit_map.eqp_kit_part_group_id = akitpartgroupid;

      IF lactualkitlineqty IS NULL
      THEN
         lactualkitlineqty := 0;
      END IF;

      RETURN lactualkitlineqty;

   END getactualkitlineqty;

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
                          akitnoid   inv_inv.inv_no_id%TYPE) RETURN NUMBER IS
      -- pl/sql type to store the kit line records
      TYPE typepartgrouprecord IS RECORD(
         kitpartgroupdbid eqp_kit_part_groups.eqp_kit_part_group_db_id%TYPE,
         kitpartgroupid   eqp_kit_part_groups.eqp_kit_part_group_id%TYPE,
         kitbompartqt     eqp_kit_part_groups.kit_qt%TYPE);
      TYPE typekitpartgroup IS TABLE OF typepartgrouprecord INDEX BY BINARY_INTEGER;

      ikitpartgroups typekitpartgroup;

      ikitreqqt NUMBER;
      ikitinvqt NUMBER;
   BEGIN
      -- get the kit definition from the kit
      SELECT eqp_kit_part_groups.bom_part_db_id,
             eqp_kit_part_groups.bom_part_id,
             eqp_kit_part_groups.kit_qt BULK COLLECT
        INTO ikitpartgroups
        FROM -- get the kit part definition
             inv_inv
       INNER JOIN eqp_kit_part_group_map ON inv_inv.part_no_db_id =
                                            eqp_kit_part_group_map.kit_part_no_db_id
                                        AND inv_inv.part_no_id =
                                            eqp_kit_part_group_map.kit_part_no_id
       INNER JOIN eqp_kit_part_groups ON eqp_kit_part_group_map.eqp_kit_part_group_db_id =
                                         eqp_kit_part_groups.eqp_kit_part_group_db_id
                                     AND eqp_kit_part_group_map.eqp_kit_part_group_id =
                                         eqp_kit_part_groups.eqp_kit_part_group_id
       WHERE inv_inv.inv_no_db_id = akitnodbid
         AND inv_inv.inv_no_id = akitnoid
         AND inv_inv.rstat_cd = 0
         AND inv_inv.inv_class_db_id = 0
         AND inv_inv.inv_class_cd = 'KIT';

      -- for all the part groups assigned to the kit, ensure that there is enough quantity of the mapped parts available
      FOR i IN 1 .. ikitpartgroups.COUNT
      LOOP
         -- get the kit line qt
         SELECT nvl(eqp_kit_part_groups.kit_qt,
                    0)
           INTO ikitreqqt
           FROM inv_inv
          INNER JOIN eqp_kit_part_group_map ON inv_inv.part_no_db_id =
                                               eqp_kit_part_group_map.kit_part_no_db_id
                                           AND inv_inv.part_no_id =
                                               eqp_kit_part_group_map.kit_part_no_id
          INNER JOIN eqp_kit_part_groups ON eqp_kit_part_group_map.eqp_kit_part_group_db_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_db_id
                                        AND eqp_kit_part_group_map.eqp_kit_part_group_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_id
          WHERE inv_inv.inv_no_db_id = akitnodbid
            AND inv_inv.inv_no_id = akitnoid
            AND inv_inv.rstat_cd = 0
            AND eqp_kit_part_groups.bom_part_db_id = ikitpartgroups(i)
         .kitpartgroupdbid
            AND eqp_kit_part_groups.bom_part_id = ikitpartgroups(i)
         .kitpartgroupid;

         SELECT
         -- determine the quantity of inventory available for the kit line
          nvl(

              SUM(CASE
                     WHEN ref_inv_class.serial_bool = 1 THEN
                      1
                     ELSE
                      inv_inv.bin_qt
                  END),
              0)
           INTO ikitinvqt
           FROM inv_kit_map
          INNER JOIN inv_inv ON inv_kit_map.inv_no_db_id = inv_inv.inv_no_db_id
                            AND inv_kit_map.inv_no_id = inv_inv.inv_no_id
                            AND inv_inv.rstat_cd = 0
          INNER JOIN ref_inv_class ON inv_inv.inv_class_db_id =
                                      ref_inv_class.inv_class_db_id
                                  AND inv_inv.inv_class_cd =
                                      ref_inv_class.inv_class_cd
          INNER JOIN ref_inv_cond ON inv_inv.inv_cond_db_id =
                                     ref_inv_cond.inv_cond_db_id
                                 AND inv_inv.inv_cond_cd =
                                     ref_inv_cond.inv_cond_cd
          INNER JOIN eqp_kit_part_groups ON inv_kit_map.eqp_kit_part_group_db_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_db_id
                                        AND inv_kit_map.eqp_kit_part_group_id =
                                            eqp_kit_part_groups.eqp_kit_part_group_id
          WHERE inv_kit_map.kit_inv_no_db_id = akitnodbid
            AND inv_kit_map.kit_inv_no_id = akitnoid
            AND eqp_kit_part_groups.bom_part_db_id = ikitpartgroups(i)
         .kitpartgroupdbid
            AND eqp_kit_part_groups.bom_part_id = ikitpartgroups(i)
         .kitpartgroupid
            AND ref_inv_cond.srv_bool = 1;

         -- if any kit line is incomplete, return 0
         IF (ikitinvqt < ikitreqqt)
         THEN
            RETURN 0;
         END IF;

      END LOOP;

      -- if all kit lines have enough inventory, then return 1
      RETURN 1;
   END iskitcomplete;

END kit_pkg;
/