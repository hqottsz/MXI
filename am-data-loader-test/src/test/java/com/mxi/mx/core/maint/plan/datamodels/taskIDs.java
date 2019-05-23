package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to hold TASK Ids (task defn DBID, task_defn ID, task db id and task id)
 * information. This class is shared class which just IDs information.
 *
 */
public class taskIDs {

   simpleIDs TASK_DEFN_IDs;
   simpleIDs TASK_IDs;


   public taskIDs(simpleIDs TASK_DEFN_IDs, simpleIDs TASK_IDs) {
      this.TASK_DEFN_IDs = TASK_DEFN_IDs;
      this.TASK_IDs = TASK_IDs;
   }


   /**
    * Returns the value of the tASK_DEFN_IDs property.
    *
    * @return the value of the tASK_DEFN_IDs property
    */
   public simpleIDs getTASK_DEFN_IDs() {
      return TASK_DEFN_IDs;
   }


   /**
    * Sets a new value for the tASK_DEFN_IDs property.
    *
    * @param tASK_DEFN_IDs
    *           the new value for the tASK_DEFN_IDs property
    */
   public void setTASK_DEFN_IDs( simpleIDs tASK_DEFN_IDs ) {
      TASK_DEFN_IDs = tASK_DEFN_IDs;
   }


   /**
    * Returns the value of the tASK_IDs property.
    *
    * @return the value of the tASK_IDs property
    */
   public simpleIDs getTASK_IDs() {
      return TASK_IDs;
   }


   /**
    * Sets a new value for the tASK_IDs property.
    *
    * @param tASK_IDs
    *           the new value for the tASK_IDs property
    */
   public void setTASK_IDs( simpleIDs tASK_IDs ) {
      TASK_IDs = tASK_IDs;
   }

}
