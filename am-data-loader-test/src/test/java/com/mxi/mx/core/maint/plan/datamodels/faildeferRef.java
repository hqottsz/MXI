package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for fail defer ref information
 *
 */
public class faildeferRef {

   String ASSMBL_CD;
   String DEFER_REF_NAME;
   String CONFIG_SLOT_CD;
   // String OPERATOR_CD_LIST;
   String FAIL_SEV_CD;
   String FAIL_DEFER_CD;
   String DEFER_REF_LDESC;
   String DEFER_REF_STATUS_CD;
   String INST_SYSTEMS_QT;
   String DISPATCH_SYSTEMS_QT;
   String APPL_LDESC;
   String OPER_RESTRICTIONS_LDESC;
   String PERF_PENALTIES_LDESC;
   String MAINT_ACTIONS_LDESC;
   String MOC_APPROVAL_BOOL;


   public faildeferRef(String ASSMBL_CD, String DEFER_REF_NAME, String CONFIG_SLOT_CD,
         String FAIL_SEV_CD, String FAIL_DEFER_CD, String DEFER_REF_LDESC,
         String DEFER_REF_STATUS_CD, String INST_SYSTEMS_QT, String DISPATCH_SYSTEMS_QT,
         String APPL_LDESC, String OPER_RESTRICTIONS_LDESC, String PERF_PENALTIES_LDESC,
         String MAINT_ACTIONS_LDESC, String MOC_APPROVAL_BOOL) {

      this.ASSMBL_CD = ASSMBL_CD;
      this.DEFER_REF_NAME = DEFER_REF_NAME;
      this.CONFIG_SLOT_CD = CONFIG_SLOT_CD;
      // this.OPERATOR_CD_LIST = OPERATOR_CD_LIST;
      this.FAIL_SEV_CD = FAIL_SEV_CD;
      this.FAIL_DEFER_CD = FAIL_DEFER_CD;
      this.DEFER_REF_LDESC = DEFER_REF_LDESC;
      this.DEFER_REF_STATUS_CD = DEFER_REF_STATUS_CD;
      this.INST_SYSTEMS_QT = INST_SYSTEMS_QT;
      this.DISPATCH_SYSTEMS_QT = DISPATCH_SYSTEMS_QT;
      this.APPL_LDESC = APPL_LDESC;
      this.OPER_RESTRICTIONS_LDESC = OPER_RESTRICTIONS_LDESC;
      this.PERF_PENALTIES_LDESC = PERF_PENALTIES_LDESC;
      this.MAINT_ACTIONS_LDESC = MAINT_ACTIONS_LDESC;
      this.MOC_APPROVAL_BOOL = MOC_APPROVAL_BOOL;

   }


   /**
    * Returns the value of the mOC_APPROVAL_BOOL property.
    *
    * @return the value of the mOC_APPROVAL_BOOL property
    */
   public String getMOC_APPROVAL_BOOL() {
      return MOC_APPROVAL_BOOL;
   }


   /**
    * Sets a new value for the mOC_APPROVAL_BOOL property.
    *
    * @param mOC_APPROVAL_BOOL
    *           the new value for the mOC_APPROVAL_BOOL property
    */
   public void setMOC_APPROVAL_BOOL( String mOC_APPROVAL_BOOL ) {
      MOC_APPROVAL_BOOL = mOC_APPROVAL_BOOL;
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
    * @param aSSMBL_CD
    *           the new value for the aSSMBL_CD property
    */
   public void setASSMBL_CD( String aSSMBL_CD ) {
      ASSMBL_CD = aSSMBL_CD;
   }


   /**
    * Returns the value of the dEFER_REF_NAME property.
    *
    * @return the value of the dEFER_REF_NAME property
    */
   public String getDEFER_REF_NAME() {
      return DEFER_REF_NAME;
   }


   /**
    * Sets a new value for the dEFER_REF_NAME property.
    *
    * @param dEFER_REF_NAME
    *           the new value for the dEFER_REF_NAME property
    */
   public void setDEFER_REF_NAME( String dEFER_REF_NAME ) {
      DEFER_REF_NAME = dEFER_REF_NAME;
   }


   /**
    * Returns the value of the cONFIG_SLOT_CD property.
    *
    * @return the value of the cONFIG_SLOT_CD property
    */
   public String getCONFIG_SLOT_CD() {
      return CONFIG_SLOT_CD;
   }


   /**
    * Sets a new value for the cONFIG_SLOT_CD property.
    *
    * @param cONFIG_SLOT_CD
    *           the new value for the cONFIG_SLOT_CD property
    */
   public void setCONFIG_SLOT_CD( String cONFIG_SLOT_CD ) {
      CONFIG_SLOT_CD = cONFIG_SLOT_CD;
   }


   /**
    * Returns the value of the fAIL_SEV_CD property.
    *
    * @return the value of the fAIL_SEV_CD property
    */
   public String getFAIL_SEV_CD() {
      return FAIL_SEV_CD;
   }


   /**
    * Sets a new value for the fAIL_SEV_CD property.
    *
    * @param fAIL_SEV_CD
    *           the new value for the fAIL_SEV_CD property
    */
   public void setFAIL_SEV_CD( String fAIL_SEV_CD ) {
      FAIL_SEV_CD = fAIL_SEV_CD;
   }


   /**
    * Returns the value of the fAIL_DEFER_CD property.
    *
    * @return the value of the fAIL_DEFER_CD property
    */
   public String getFAIL_DEFER_CD() {
      return FAIL_DEFER_CD;
   }


   /**
    * Sets a new value for the fAIL_DEFER_CD property.
    *
    * @param fAIL_DEFER_CD
    *           the new value for the fAIL_DEFER_CD property
    */
   public void setFAIL_DEFER_CD( String fAIL_DEFER_CD ) {
      FAIL_DEFER_CD = fAIL_DEFER_CD;
   }


   /**
    * Returns the value of the dEFER_REF_LDESC property.
    *
    * @return the value of the dEFER_REF_LDESC property
    */
   public String getDEFER_REF_LDESC() {
      return DEFER_REF_LDESC;
   }


   /**
    * Sets a new value for the dEFER_REF_LDESC property.
    *
    * @param dEFER_REF_LDESC
    *           the new value for the dEFER_REF_LDESC property
    */
   public void setDEFER_REF_LDESC( String dEFER_REF_LDESC ) {
      DEFER_REF_LDESC = dEFER_REF_LDESC;
   }


   /**
    * Returns the value of the dEFER_REF_STATUS_CD property.
    *
    * @return the value of the dEFER_REF_STATUS_CD property
    */
   public String getDEFER_REF_STATUS_CD() {
      return DEFER_REF_STATUS_CD;
   }


   /**
    * Sets a new value for the dEFER_REF_STATUS_CD property.
    *
    * @param dEFER_REF_STATUS_CD
    *           the new value for the dEFER_REF_STATUS_CD property
    */
   public void setDEFER_REF_STATUS_CD( String dEFER_REF_STATUS_CD ) {
      DEFER_REF_STATUS_CD = dEFER_REF_STATUS_CD;
   }


   /**
    * Returns the value of the iNST_SYSTEMS_QT property.
    *
    * @return the value of the iNST_SYSTEMS_QT property
    */
   public String getINST_SYSTEMS_QT() {
      return INST_SYSTEMS_QT;
   }


   /**
    * Sets a new value for the iNST_SYSTEMS_QT property.
    *
    * @param iNST_SYSTEMS_QT
    *           the new value for the iNST_SYSTEMS_QT property
    */
   public void setINST_SYSTEMS_QT( String iNST_SYSTEMS_QT ) {
      INST_SYSTEMS_QT = iNST_SYSTEMS_QT;
   }


   /**
    * Returns the value of the dISPATCH_SYSTEMS_QT property.
    *
    * @return the value of the dISPATCH_SYSTEMS_QT property
    */
   public String getDISPATCH_SYSTEMS_QT() {
      return DISPATCH_SYSTEMS_QT;
   }


   /**
    * Sets a new value for the dISPATCH_SYSTEMS_QT property.
    *
    * @param dISPATCH_SYSTEMS_QT
    *           the new value for the dISPATCH_SYSTEMS_QT property
    */
   public void setDISPATCH_SYSTEMS_QT( String dISPATCH_SYSTEMS_QT ) {
      DISPATCH_SYSTEMS_QT = dISPATCH_SYSTEMS_QT;
   }


   /**
    * Returns the value of the aPPL_LDESC property.
    *
    * @return the value of the aPPL_LDESC property
    */
   public String getAPPL_LDESC() {
      return APPL_LDESC;
   }


   /**
    * Sets a new value for the aPPL_LDESC property.
    *
    * @param aPPL_LDESC
    *           the new value for the aPPL_LDESC property
    */
   public void setAPPL_LDESC( String aPPL_LDESC ) {
      APPL_LDESC = aPPL_LDESC;
   }


   /**
    * Returns the value of the oPER_RESTRICTIONS_LDESC property.
    *
    * @return the value of the oPER_RESTRICTIONS_LDESC property
    */
   public String getOPER_RESTRICTIONS_LDESC() {
      return OPER_RESTRICTIONS_LDESC;
   }


   /**
    * Sets a new value for the oPER_RESTRICTIONS_LDESC property.
    *
    * @param oPER_RESTRICTIONS_LDESC
    *           the new value for the oPER_RESTRICTIONS_LDESC property
    */
   public void setOPER_RESTRICTIONS_LDESC( String oPER_RESTRICTIONS_LDESC ) {
      OPER_RESTRICTIONS_LDESC = oPER_RESTRICTIONS_LDESC;
   }


   /**
    * Returns the value of the pERF_PENALTIES_LDESC property.
    *
    * @return the value of the pERF_PENALTIES_LDESC property
    */
   public String getPERF_PENALTIES_LDESC() {
      return PERF_PENALTIES_LDESC;
   }


   /**
    * Sets a new value for the pERF_PENALTIES_LDESC property.
    *
    * @param pERF_PENALTIES_LDESC
    *           the new value for the pERF_PENALTIES_LDESC property
    */
   public void setPERF_PENALTIES_LDESC( String pERF_PENALTIES_LDESC ) {
      PERF_PENALTIES_LDESC = pERF_PENALTIES_LDESC;
   }


   /**
    * Returns the value of the mAINT_ACTIONS_LDESC property.
    *
    * @return the value of the mAINT_ACTIONS_LDESC property
    */
   public String getMAINT_ACTIONS_LDESC() {
      return MAINT_ACTIONS_LDESC;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( APPL_LDESC == null ) ? 0 : APPL_LDESC.hashCode() );
      result = prime * result + ( ( ASSMBL_CD == null ) ? 0 : ASSMBL_CD.hashCode() );
      result = prime * result + ( ( CONFIG_SLOT_CD == null ) ? 0 : CONFIG_SLOT_CD.hashCode() );
      result = prime * result + ( ( DEFER_REF_LDESC == null ) ? 0 : DEFER_REF_LDESC.hashCode() );
      result = prime * result + ( ( DEFER_REF_NAME == null ) ? 0 : DEFER_REF_NAME.hashCode() );
      result = prime * result
            + ( ( DEFER_REF_STATUS_CD == null ) ? 0 : DEFER_REF_STATUS_CD.hashCode() );
      result = prime * result
            + ( ( DISPATCH_SYSTEMS_QT == null ) ? 0 : DISPATCH_SYSTEMS_QT.hashCode() );
      result = prime * result + ( ( FAIL_DEFER_CD == null ) ? 0 : FAIL_DEFER_CD.hashCode() );
      result = prime * result + ( ( FAIL_SEV_CD == null ) ? 0 : FAIL_SEV_CD.hashCode() );
      result = prime * result + ( ( INST_SYSTEMS_QT == null ) ? 0 : INST_SYSTEMS_QT.hashCode() );
      result = prime * result
            + ( ( MAINT_ACTIONS_LDESC == null ) ? 0 : MAINT_ACTIONS_LDESC.hashCode() );
      result =
            prime * result + ( ( MOC_APPROVAL_BOOL == null ) ? 0 : MOC_APPROVAL_BOOL.hashCode() );
      result = prime * result
            + ( ( OPER_RESTRICTIONS_LDESC == null ) ? 0 : OPER_RESTRICTIONS_LDESC.hashCode() );
      result = prime * result
            + ( ( PERF_PENALTIES_LDESC == null ) ? 0 : PERF_PENALTIES_LDESC.hashCode() );
      return result;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals( Object obj ) {
      if ( this == obj )
         return true;
      if ( obj == null )
         return false;
      if ( getClass() != obj.getClass() )
         return false;
      faildeferRef other = ( faildeferRef ) obj;
      if ( APPL_LDESC == null ) {
         if ( other.APPL_LDESC != null )
            return false;
      } else if ( !APPL_LDESC.equals( other.APPL_LDESC ) )
         return false;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equals( other.ASSMBL_CD ) )
         return false;
      if ( CONFIG_SLOT_CD == null ) {
         if ( other.CONFIG_SLOT_CD != null )
            return false;
      } else if ( !CONFIG_SLOT_CD.equals( other.CONFIG_SLOT_CD ) )
         return false;
      if ( DEFER_REF_LDESC == null ) {
         if ( other.DEFER_REF_LDESC != null )
            return false;
      } else if ( !DEFER_REF_LDESC.equals( other.DEFER_REF_LDESC ) )
         return false;
      if ( DEFER_REF_NAME == null ) {
         if ( other.DEFER_REF_NAME != null )
            return false;
      } else if ( !DEFER_REF_NAME.equals( other.DEFER_REF_NAME ) )
         return false;
      if ( DEFER_REF_STATUS_CD == null ) {
         if ( other.DEFER_REF_STATUS_CD != null )
            return false;
      } else if ( !DEFER_REF_STATUS_CD.equals( other.DEFER_REF_STATUS_CD ) )
         return false;
      if ( DISPATCH_SYSTEMS_QT == null ) {
         if ( other.DISPATCH_SYSTEMS_QT != null )
            return false;
      } else if ( !DISPATCH_SYSTEMS_QT.equals( other.DISPATCH_SYSTEMS_QT ) )
         return false;
      if ( FAIL_DEFER_CD == null ) {
         if ( other.FAIL_DEFER_CD != null )
            return false;
      } else if ( !FAIL_DEFER_CD.equals( other.FAIL_DEFER_CD ) )
         return false;
      if ( FAIL_SEV_CD == null ) {
         if ( other.FAIL_SEV_CD != null )
            return false;
      } else if ( !FAIL_SEV_CD.equals( other.FAIL_SEV_CD ) )
         return false;
      if ( INST_SYSTEMS_QT == null ) {
         if ( other.INST_SYSTEMS_QT != null )
            return false;
      } else if ( !INST_SYSTEMS_QT.equals( other.INST_SYSTEMS_QT ) )
         return false;
      if ( MAINT_ACTIONS_LDESC == null ) {
         if ( other.MAINT_ACTIONS_LDESC != null )
            return false;
      } else if ( !MAINT_ACTIONS_LDESC.equals( other.MAINT_ACTIONS_LDESC ) )
         return false;
      if ( MOC_APPROVAL_BOOL == null ) {
         if ( other.MOC_APPROVAL_BOOL != null )
            return false;
      } else if ( !MOC_APPROVAL_BOOL.equals( other.MOC_APPROVAL_BOOL ) )
         return false;
      if ( OPER_RESTRICTIONS_LDESC == null ) {
         if ( other.OPER_RESTRICTIONS_LDESC != null )
            return false;
      } else if ( !OPER_RESTRICTIONS_LDESC.equals( other.OPER_RESTRICTIONS_LDESC ) )
         return false;
      if ( PERF_PENALTIES_LDESC == null ) {
         if ( other.PERF_PENALTIES_LDESC != null )
            return false;
      } else if ( !PERF_PENALTIES_LDESC.equals( other.PERF_PENALTIES_LDESC ) )
         return false;
      return true;
   }

}
