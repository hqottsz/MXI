package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data inv_inv table
 *
 */
public class inventoryData {

   String INV_NO_DB_ID;
   String INV_NO_ID;
   String INV_CLASS_DB_ID;
   String INV_CLASS_CD;
   String BOM_PART_DB_ID;
   String BOM_PART_ID;
   String LOC_DB_ID = null;
   String LOC_ID = null;
   String PART_NO_DB_ID;
   String PART_NO_ID;
   String H_INV_NO_DB_ID;
   String H_INV_NO_ID;
   String ASSMBL_INV_NO_DB_ID;
   String ASSMBL_INV_NO_ID;
   String INV_COND_DB_ID;
   String INV_COND_CD;
   String ASSMBL_DB_ID;
   String ASSMBL_CD;
   String ASSMBL_BOM_ID;
   String ASSMBL_POS_ID;
   String ORIG_ASSMBL_DB_ID;
   String ORIG_ASSMBL_CD;
   String ASSMBL_SUBTYPE_DB_ID;
   String ASSMBL_SUBTYPE_ID;
   String SCHED_DB_ID = null;
   String SCHED_ID = null;
   String SCHED_PART_ID = null;
   String SCHED_RMVD_PART_ID = null;
   String INV_NO_SDESC;
   String SERIAL_NO_OEM;
   String COMPLETE_BOOL;
   String LOCKED_BOOL = "0";
   String FINANCE_STATUS_CD;
   String APPL_EFF_CD;
   String CARRIER_DB_ID;
   String CARRIER_ID;


   public inventoryData(String INV_NO_DB_ID, String INV_NO_ID, String INV_CLASS_DB_ID,
         String INV_CLASS_CD, String BOM_PART_DB_ID, String BOM_PART_ID, String PART_NO_DB_ID,
         String PART_NO_ID, String H_INV_NO_DB_ID, String H_INV_NO_ID, String ASSMBL_INV_NO_DB_ID,
         String ASSMBL_INV_NO_ID, String INV_COND_DB_ID, String INV_COND_CD, String ASSMBL_DB_ID,
         String ASSMBL_CD, String ASSMBL_BOM_ID, String ASSMBL_POS_ID, String ORIG_ASSMBL_DB_ID,
         String ORIG_ASSMBL_CD, String INV_NO_SDESC, String SERIAL_NO_OEM, String FINANCE_STATUS_CD,
         String APPL_EFF_CD, String CARRIER_DB_ID, String CARRIER_ID) {

      this.INV_NO_DB_ID = INV_NO_DB_ID;
      this.INV_NO_ID = INV_NO_ID;
      this.INV_CLASS_DB_ID = INV_CLASS_DB_ID;
      this.INV_CLASS_CD = INV_CLASS_CD;
      this.BOM_PART_DB_ID = BOM_PART_DB_ID;
      this.BOM_PART_ID = BOM_PART_ID;
      this.PART_NO_DB_ID = PART_NO_DB_ID;
      this.PART_NO_ID = PART_NO_ID;
      this.H_INV_NO_DB_ID = H_INV_NO_DB_ID;
      this.H_INV_NO_ID = H_INV_NO_ID;
      this.ASSMBL_INV_NO_DB_ID = ASSMBL_INV_NO_DB_ID;
      this.ASSMBL_INV_NO_ID = ASSMBL_INV_NO_ID;
      this.INV_COND_DB_ID = INV_COND_DB_ID;
      this.INV_COND_CD = INV_COND_CD;
      this.ASSMBL_DB_ID = ASSMBL_DB_ID;
      this.ASSMBL_CD = ASSMBL_CD;
      this.ASSMBL_BOM_ID = ASSMBL_BOM_ID;
      this.ASSMBL_POS_ID = ASSMBL_POS_ID;
      this.ORIG_ASSMBL_DB_ID = ORIG_ASSMBL_DB_ID;
      this.ORIG_ASSMBL_CD = ORIG_ASSMBL_CD;
      this.INV_NO_SDESC = INV_NO_SDESC;
      this.SERIAL_NO_OEM = SERIAL_NO_OEM;
      this.FINANCE_STATUS_CD = FINANCE_STATUS_CD;
      this.APPL_EFF_CD = APPL_EFF_CD;
      this.CARRIER_DB_ID = CARRIER_DB_ID;
      this.CARRIER_ID = CARRIER_ID;

   }


   public inventoryData(String INV_NO_ID, String ASSMBL_BOM_ID, String ASSMBL_POS_ID,
         String PART_NO_ID, String BOM_PART_ID) {
      this.INV_NO_ID = INV_NO_ID;
      this.ASSMBL_BOM_ID = ASSMBL_BOM_ID;
      this.ASSMBL_POS_ID = ASSMBL_POS_ID;
      this.PART_NO_ID = PART_NO_ID;
      this.BOM_PART_ID = BOM_PART_ID;
   }


   /**
    * Returns the value of the iNV_NO_DB_ID property.
    *
    * @return the value of the iNV_NO_DB_ID property
    */
   public String getINV_NO_DB_ID() {
      return INV_NO_DB_ID;
   }


   /**
    * Sets a new value for the iNV_NO_DB_ID property.
    *
    * @param aINV_NO_DB_ID
    *           the new value for the iNV_NO_DB_ID property
    */
   public void setINV_NO_DB_ID( String aINV_NO_DB_ID ) {
      INV_NO_DB_ID = aINV_NO_DB_ID;
   }


   /**
    * Returns the value of the iNV_NO_ID property.
    *
    * @return the value of the iNV_NO_ID property
    */
   public String getINV_NO_ID() {
      return INV_NO_ID;
   }


   /**
    * Sets a new value for the iNV_NO_ID property.
    *
    * @param aINV_NO_ID
    *           the new value for the iNV_NO_ID property
    */
   public void setINV_NO_ID( String aINV_NO_ID ) {
      INV_NO_ID = aINV_NO_ID;
   }


   /**
    * Returns the value of the iNV_CLASS_DB_ID property.
    *
    * @return the value of the iNV_CLASS_DB_ID property
    */
   public String getINV_CLASS_DB_ID() {
      return INV_CLASS_DB_ID;
   }


   /**
    * Sets a new value for the iNV_CLASS_DB_ID property.
    *
    * @param aINV_CLASS_DB_ID
    *           the new value for the iNV_CLASS_DB_ID property
    */
   public void setINV_CLASS_DB_ID( String aINV_CLASS_DB_ID ) {
      INV_CLASS_DB_ID = aINV_CLASS_DB_ID;
   }


   /**
    * Returns the value of the iNV_CLASS_CD property.
    *
    * @return the value of the iNV_CLASS_CD property
    */
   public String getINV_CLASS_CD() {
      return INV_CLASS_CD;
   }


   /**
    * Sets a new value for the iNV_CLASS_CD property.
    *
    * @param aINV_CLASS_CD
    *           the new value for the iNV_CLASS_CD property
    */
   public void setINV_CLASS_CD( String aINV_CLASS_CD ) {
      INV_CLASS_CD = aINV_CLASS_CD;
   }


   /**
    * Returns the value of the bOM_PART_DB_ID property.
    *
    * @return the value of the bOM_PART_DB_ID property
    */
   public String getBOM_PART_DB_ID() {
      return BOM_PART_DB_ID;
   }


   /**
    * Sets a new value for the bOM_PART_DB_ID property.
    *
    * @param aBOM_PART_DB_ID
    *           the new value for the bOM_PART_DB_ID property
    */
   public void setBOM_PART_DB_ID( String aBOM_PART_DB_ID ) {
      BOM_PART_DB_ID = aBOM_PART_DB_ID;
   }


   /**
    * Returns the value of the bOM_PART_ID property.
    *
    * @return the value of the bOM_PART_ID property
    */
   public String getBOM_PART_ID() {
      return BOM_PART_ID;
   }


   /**
    * Sets a new value for the bOM_PART_ID property.
    *
    * @param aBOM_PART_ID
    *           the new value for the bOM_PART_ID property
    */
   public void setBOM_PART_ID( String aBOM_PART_ID ) {
      BOM_PART_ID = aBOM_PART_ID;
   }


   /**
    * Returns the value of the lOC_DB_ID property.
    *
    * @return the value of the lOC_DB_ID property
    */
   public String getLOC_DB_ID() {
      return LOC_DB_ID;
   }


   /**
    * Sets a new value for the lOC_DB_ID property.
    *
    * @param aLOC_DB_ID
    *           the new value for the lOC_DB_ID property
    */
   public void setLOC_DB_ID( String aLOC_DB_ID ) {
      LOC_DB_ID = aLOC_DB_ID;
   }


   /**
    * Returns the value of the lOC_ID property.
    *
    * @return the value of the lOC_ID property
    */
   public String getLOC_ID() {
      return LOC_ID;
   }


   /**
    * Sets a new value for the lOC_ID property.
    *
    * @param aLOC_ID
    *           the new value for the lOC_ID property
    */
   public void setLOC_ID( String aLOC_ID ) {
      LOC_ID = aLOC_ID;
   }


   /**
    * Returns the value of the pART_NO_DB_ID property.
    *
    * @return the value of the pART_NO_DB_ID property
    */
   public String getPART_NO_DB_ID() {
      return PART_NO_DB_ID;
   }


   /**
    * Sets a new value for the pART_NO_DB_ID property.
    *
    * @param aPART_NO_DB_ID
    *           the new value for the pART_NO_DB_ID property
    */
   public void setPART_NO_DB_ID( String aPART_NO_DB_ID ) {
      PART_NO_DB_ID = aPART_NO_DB_ID;
   }


   /**
    * Returns the value of the pART_NO_ID property.
    *
    * @return the value of the pART_NO_ID property
    */
   public String getPART_NO_ID() {
      return PART_NO_ID;
   }


   /**
    * Sets a new value for the pART_NO_ID property.
    *
    * @param aPART_NO_ID
    *           the new value for the pART_NO_ID property
    */
   public void setPART_NO_ID( String aPART_NO_ID ) {
      PART_NO_ID = aPART_NO_ID;
   }


   /**
    * Returns the value of the h_INV_NO_DB_ID property.
    *
    * @return the value of the h_INV_NO_DB_ID property
    */
   public String getH_INV_NO_DB_ID() {
      return H_INV_NO_DB_ID;
   }


   /**
    * Sets a new value for the h_INV_NO_DB_ID property.
    *
    * @param aH_INV_NO_DB_ID
    *           the new value for the h_INV_NO_DB_ID property
    */
   public void setH_INV_NO_DB_ID( String aH_INV_NO_DB_ID ) {
      H_INV_NO_DB_ID = aH_INV_NO_DB_ID;
   }


   /**
    * Returns the value of the h_INV_NO_ID property.
    *
    * @return the value of the h_INV_NO_ID property
    */
   public String getH_INV_NO_ID() {
      return H_INV_NO_ID;
   }


   /**
    * Sets a new value for the h_INV_NO_ID property.
    *
    * @param aH_INV_NO_ID
    *           the new value for the h_INV_NO_ID property
    */
   public void setH_INV_NO_ID( String aH_INV_NO_ID ) {
      H_INV_NO_ID = aH_INV_NO_ID;
   }


   /**
    * Returns the value of the aSSMBL_INV_NO_DB_ID property.
    *
    * @return the value of the aSSMBL_INV_NO_DB_ID property
    */
   public String getASSMBL_INV_NO_DB_ID() {
      return ASSMBL_INV_NO_DB_ID;
   }


   /**
    * Sets a new value for the aSSMBL_INV_NO_DB_ID property.
    *
    * @param aASSMBL_INV_NO_DB_ID
    *           the new value for the aSSMBL_INV_NO_DB_ID property
    */
   public void setASSMBL_INV_NO_DB_ID( String aASSMBL_INV_NO_DB_ID ) {
      ASSMBL_INV_NO_DB_ID = aASSMBL_INV_NO_DB_ID;
   }


   /**
    * Returns the value of the aSSMBL_INV_NO_ID property.
    *
    * @return the value of the aSSMBL_INV_NO_ID property
    */
   public String getASSMBL_INV_NO_ID() {
      return ASSMBL_INV_NO_ID;
   }


   /**
    * Returns the value of the iNV_COND_CD property.
    *
    * @return the value of the iNV_COND_CD property
    */
   public String getINV_COND_CD() {
      return INV_COND_CD;
   }


   /**
    * Sets a new value for the iNV_COND_CD property.
    *
    * @param aINV_COND_CD
    *           the new value for the iNV_COND_CD property
    */
   public void setINV_COND_CD( String aINV_COND_CD ) {
      INV_COND_CD = aINV_COND_CD;
   }


   /**
    * Sets a new value for the aSSMBL_INV_NO_ID property.
    *
    * @param aASSMBL_INV_NO_ID
    *           the new value for the aSSMBL_INV_NO_ID property
    */
   public void setASSMBL_INV_NO_ID( String aASSMBL_INV_NO_ID ) {
      ASSMBL_INV_NO_ID = aASSMBL_INV_NO_ID;
   }


   /**
    * Returns the value of the iNV_COND_DB_ID property.
    *
    * @return the value of the iNV_COND_DB_ID property
    */
   public String getINV_COND_DB_ID() {
      return INV_COND_DB_ID;
   }


   /**
    * Sets a new value for the iNV_COND_DB_ID property.
    *
    * @param aINV_COND_DB_ID
    *           the new value for the iNV_COND_DB_ID property
    */
   public void setINV_COND_DB_ID( String aINV_COND_DB_ID ) {
      INV_COND_DB_ID = aINV_COND_DB_ID;
   }


   /**
    * Returns the value of the aSSMBL_DB_ID property.
    *
    * @return the value of the aSSMBL_DB_ID property
    */
   public String getASSMBL_DB_ID() {
      return ASSMBL_DB_ID;
   }


   /**
    * Sets a new value for the aSSMBL_DB_ID property.
    *
    * @param aASSMBL_DB_ID
    *           the new value for the aSSMBL_DB_ID property
    */
   public void setASSMBL_DB_ID( String aASSMBL_DB_ID ) {
      ASSMBL_DB_ID = aASSMBL_DB_ID;
   }


   /**
    * Returns the value of the aSSMBL_CD property.
    *
    * @return the value of the aSSMBL_CD property
    */
   public String getASSMBL_CD() {
      return ASSMBL_CD;
   }


   /**
    * Sets a new value for the aSSMBL_CD property.
    *
    * @param aASSMBL_CD
    *           the new value for the aSSMBL_CD property
    */
   public void setASSMBL_CD( String aASSMBL_CD ) {
      ASSMBL_CD = aASSMBL_CD;
   }


   /**
    * Returns the value of the aSSMBL_BOM_ID property.
    *
    * @return the value of the aSSMBL_BOM_ID property
    */
   public String getASSMBL_BOM_ID() {
      return ASSMBL_BOM_ID;
   }


   /**
    * Sets a new value for the aSSMBL_BOM_ID property.
    *
    * @param aASSMBL_BOM_ID
    *           the new value for the aSSMBL_BOM_ID property
    */
   public void setASSMBL_BOM_ID( String aASSMBL_BOM_ID ) {
      ASSMBL_BOM_ID = aASSMBL_BOM_ID;
   }


   /**
    * Returns the value of the aSSMBL_POS_ID property.
    *
    * @return the value of the aSSMBL_POS_ID property
    */
   public String getASSMBL_POS_ID() {
      return ASSMBL_POS_ID;
   }


   /**
    * Sets a new value for the aSSMBL_POS_ID property.
    *
    * @param aASSMBL_POS_ID
    *           the new value for the aSSMBL_POS_ID property
    */
   public void setASSMBL_POS_ID( String aASSMBL_POS_ID ) {
      ASSMBL_POS_ID = aASSMBL_POS_ID;
   }


   /**
    * Returns the value of the oRIG_ASSMBL_DB_ID property.
    *
    * @return the value of the oRIG_ASSMBL_DB_ID property
    */
   public String getORIG_ASSMBL_DB_ID() {
      return ORIG_ASSMBL_DB_ID;
   }


   /**
    * Sets a new value for the oRIG_ASSMBL_DB_ID property.
    *
    * @param aORIG_ASSMBL_DB_ID
    *           the new value for the oRIG_ASSMBL_DB_ID property
    */
   public void setORIG_ASSMBL_DB_ID( String aORIG_ASSMBL_DB_ID ) {
      ORIG_ASSMBL_DB_ID = aORIG_ASSMBL_DB_ID;
   }


   /**
    * Returns the value of the oRIG_ASSMBL_CD property.
    *
    * @return the value of the oRIG_ASSMBL_CD property
    */
   public String getORIG_ASSMBL_CD() {
      return ORIG_ASSMBL_CD;
   }


   /**
    * Sets a new value for the oRIG_ASSMBL_CD property.
    *
    * @param aORIG_ASSMBL_CD
    *           the new value for the oRIG_ASSMBL_CD property
    */
   public void setORIG_ASSMBL_CD( String aORIG_ASSMBL_CD ) {
      ORIG_ASSMBL_CD = aORIG_ASSMBL_CD;
   }


   /**
    * Returns the value of the aSSMBL_SUBTYPE_DB_ID property.
    *
    * @return the value of the aSSMBL_SUBTYPE_DB_ID property
    */
   public String getASSMBL_SUBTYPE_DB_ID() {
      return ASSMBL_SUBTYPE_DB_ID;
   }


   /**
    * Sets a new value for the aSSMBL_SUBTYPE_DB_ID property.
    *
    * @param aASSMBL_SUBTYPE_DB_ID
    *           the new value for the aSSMBL_SUBTYPE_DB_ID property
    */
   public void setASSMBL_SUBTYPE_DB_ID( String aASSMBL_SUBTYPE_DB_ID ) {
      ASSMBL_SUBTYPE_DB_ID = aASSMBL_SUBTYPE_DB_ID;
   }


   /**
    * Returns the value of the aSSMBL_SUBTYPE_ID property.
    *
    * @return the value of the aSSMBL_SUBTYPE_ID property
    */
   public String getASSMBL_SUBTYPE_ID() {
      return ASSMBL_SUBTYPE_ID;
   }


   /**
    * Sets a new value for the aSSMBL_SUBTYPE_ID property.
    *
    * @param aASSMBL_SUBTYPE_ID
    *           the new value for the aSSMBL_SUBTYPE_ID property
    */
   public void setASSMBL_SUBTYPE_ID( String aASSMBL_SUBTYPE_ID ) {
      ASSMBL_SUBTYPE_ID = aASSMBL_SUBTYPE_ID;
   }


   /**
    * Returns the value of the sCHED_DB_ID property.
    *
    * @return the value of the sCHED_DB_ID property
    */
   public String getSCHED_DB_ID() {
      return SCHED_DB_ID;
   }


   /**
    * Sets a new value for the sCHED_DB_ID property.
    *
    * @param aSCHED_DB_ID
    *           the new value for the sCHED_DB_ID property
    */
   public void setSCHED_DB_ID( String aSCHED_DB_ID ) {
      SCHED_DB_ID = aSCHED_DB_ID;
   }


   /**
    * Returns the value of the sCHED_ID property.
    *
    * @return the value of the sCHED_ID property
    */
   public String getSCHED_ID() {
      return SCHED_ID;
   }


   /**
    * Sets a new value for the sCHED_ID property.
    *
    * @param aSCHED_ID
    *           the new value for the sCHED_ID property
    */
   public void setSCHED_ID( String aSCHED_ID ) {
      SCHED_ID = aSCHED_ID;
   }


   /**
    * Returns the value of the sCHED_PART_ID property.
    *
    * @return the value of the sCHED_PART_ID property
    */
   public String getSCHED_PART_ID() {
      return SCHED_PART_ID;
   }


   /**
    * Sets a new value for the sCHED_PART_ID property.
    *
    * @param aSCHED_PART_ID
    *           the new value for the sCHED_PART_ID property
    */
   public void setSCHED_PART_ID( String aSCHED_PART_ID ) {
      SCHED_PART_ID = aSCHED_PART_ID;
   }


   /**
    * Returns the value of the sCHED_RMVD_PART_ID property.
    *
    * @return the value of the sCHED_RMVD_PART_ID property
    */
   public String getSCHED_RMVD_PART_ID() {
      return SCHED_RMVD_PART_ID;
   }


   /**
    * Sets a new value for the sCHED_RMVD_PART_ID property.
    *
    * @param aSCHED_RMVD_PART_ID
    *           the new value for the sCHED_RMVD_PART_ID property
    */
   public void setSCHED_RMVD_PART_ID( String aSCHED_RMVD_PART_ID ) {
      SCHED_RMVD_PART_ID = aSCHED_RMVD_PART_ID;
   }


   /**
    * Returns the value of the iNV_NO_SDESC property.
    *
    * @return the value of the iNV_NO_SDESC property
    */
   public String getINV_NO_SDESC() {
      return INV_NO_SDESC;
   }


   /**
    * Sets a new value for the iNV_NO_SDESC property.
    *
    * @param aINV_NO_SDESC
    *           the new value for the iNV_NO_SDESC property
    */
   public void setINV_NO_SDESC( String aINV_NO_SDESC ) {
      INV_NO_SDESC = aINV_NO_SDESC;
   }


   /**
    * Returns the value of the sERIAL_NO_OEM property.
    *
    * @return the value of the sERIAL_NO_OEM property
    */
   public String getSERIAL_NO_OEM() {
      return SERIAL_NO_OEM;
   }


   /**
    * Sets a new value for the sERIAL_NO_OEM property.
    *
    * @param aSERIAL_NO_OEM
    *           the new value for the sERIAL_NO_OEM property
    */
   public void setSERIAL_NO_OEM( String aSERIAL_NO_OEM ) {
      SERIAL_NO_OEM = aSERIAL_NO_OEM;
   }


   /**
    * Returns the value of the cOMPLETE_BOOL property.
    *
    * @return the value of the cOMPLETE_BOOL property
    */
   public String getCOMPLETE_BOOL() {
      return COMPLETE_BOOL;
   }


   /**
    * Sets a new value for the cOMPLETE_BOOL property.
    *
    * @param aCOMPLETE_BOOL
    *           the new value for the cOMPLETE_BOOL property
    */
   public void setCOMPLETE_BOOL( String aCOMPLETE_BOOL ) {
      COMPLETE_BOOL = aCOMPLETE_BOOL;
   }


   /**
    * Returns the value of the lOCKED_BOOL property.
    *
    * @return the value of the lOCKED_BOOL property
    */
   public String getLOCKED_BOOL() {
      return LOCKED_BOOL;
   }


   /**
    * Sets a new value for the lOCKED_BOOL property.
    *
    * @param aLOCKED_BOOL
    *           the new value for the lOCKED_BOOL property
    */
   public void setLOCKED_BOOL( String aLOCKED_BOOL ) {
      LOCKED_BOOL = aLOCKED_BOOL;
   }


   /**
    * Returns the value of the fINANCE_STATUS_CD property.
    *
    * @return the value of the fINANCE_STATUS_CD property
    */
   public String getFINANCE_STATUS_CD() {
      return FINANCE_STATUS_CD;
   }


   /**
    * Sets a new value for the fINANCE_STATUS_CD property.
    *
    * @param aFINANCE_STATUS_CD
    *           the new value for the fINANCE_STATUS_CD property
    */
   public void setFINANCE_STATUS_CD( String aFINANCE_STATUS_CD ) {
      FINANCE_STATUS_CD = aFINANCE_STATUS_CD;
   }


   /**
    * Returns the value of the aPPL_EFF_CD property.
    *
    * @return the value of the aPPL_EFF_CD property
    */
   public String getAPPL_EFF_CD() {
      return APPL_EFF_CD;
   }


   /**
    * Sets a new value for the aPPL_EFF_CD property.
    *
    * @param aAPPL_EFF_CD
    *           the new value for the aPPL_EFF_CD property
    */
   public void setAPPL_EFF_CD( String aAPPL_EFF_CD ) {
      APPL_EFF_CD = aAPPL_EFF_CD;
   }


   /**
    * Returns the value of the cARRIER_DB_ID property.
    *
    * @return the value of the cARRIER_DB_ID property
    */
   public String getCARRIER_DB_ID() {
      return CARRIER_DB_ID;
   }


   /**
    * Sets a new value for the cARRIER_DB_ID property.
    *
    * @param aCARRIER_DB_ID
    *           the new value for the cARRIER_DB_ID property
    */
   public void setCARRIER_DB_ID( String aCARRIER_DB_ID ) {
      CARRIER_DB_ID = aCARRIER_DB_ID;
   }


   /**
    * Returns the value of the cARRIER_ID property.
    *
    * @return the value of the cARRIER_ID property
    */
   public String getCARRIER_ID() {
      return CARRIER_ID;
   }


   /**
    * Sets a new value for the cARRIER_ID property.
    *
    * @param aCARRIER_ID
    *           the new value for the cARRIER_ID property
    */
   public void setCARRIER_ID( String aCARRIER_ID ) {
      CARRIER_ID = aCARRIER_ID;
   }

}
