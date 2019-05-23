package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for C_REF_DYNAMIC_DEADLINE table
 *
 */
public class cRefDynamicDeadline {

   String REF_TASK_CD;
   String ASSMBL_CD;
   String ATA_CD;
   String SCHED_DATA_TYPE_CD;
   String SCHED_INTERVAL_QT;
   String SCHED_THRESHOLD_QT;


   public cRefDynamicDeadline(String REF_TASK_CD, String ASSMBL_CD, String ATA_CD,
         String SCHED_DATA_TYPE_CD, String SCHED_INTERVAL_QT, String SCHED_THRESHOLD_QT) {

      this.REF_TASK_CD = REF_TASK_CD;
      this.ASSMBL_CD = ASSMBL_CD;
      this.ATA_CD = ATA_CD;
      this.SCHED_DATA_TYPE_CD = SCHED_DATA_TYPE_CD;
      this.SCHED_INTERVAL_QT = SCHED_INTERVAL_QT;
      this.SCHED_THRESHOLD_QT = SCHED_THRESHOLD_QT;

   }


   /**
    * Returns the value of the rEF_TASK_CD property.
    *
    * @return the value of the rEF_TASK_CD property
    */
   public String getREF_TASK_CD() {
      return REF_TASK_CD;
   }


   /**
    * Sets a new value for the rEF_TASK_CD property.
    *
    * @param rEF_TASK_CD
    *           the new value for the rEF_TASK_CD property
    */
   public void setREF_TASK_CD( String rEF_TASK_CD ) {
      REF_TASK_CD = rEF_TASK_CD;
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
    * Returns the value of the aTA_CD property.
    *
    * @return the value of the aTA_CD property
    */
   public String getATA_CD() {
      return ATA_CD;
   }


   /**
    * Sets a new value for the aTA_CD property.
    *
    * @param aTA_CD
    *           the new value for the aTA_CD property
    */
   public void setATA_CD( String aTA_CD ) {
      ATA_CD = aTA_CD;
   }


   /**
    * Returns the value of the sCHED_DATA_TYPE_CD property.
    *
    * @return the value of the sCHED_DATA_TYPE_CD property
    */
   public String getSCHED_DATA_TYPE_CD() {
      return SCHED_DATA_TYPE_CD;
   }


   /**
    * Sets a new value for the sCHED_DATA_TYPE_CD property.
    *
    * @param sCHED_DATA_TYPE_CD
    *           the new value for the sCHED_DATA_TYPE_CD property
    */
   public void setSCHED_DATA_TYPE_CD( String sCHED_DATA_TYPE_CD ) {
      SCHED_DATA_TYPE_CD = sCHED_DATA_TYPE_CD;
   }


   /**
    * Returns the value of the sCHED_INTERVAL_QT property.
    *
    * @return the value of the sCHED_INTERVAL_QT property
    */
   public String getSCHED_INTERVAL_QT() {
      return SCHED_INTERVAL_QT;
   }


   /**
    * Sets a new value for the sCHED_INTERVAL_QT property.
    *
    * @param sCHED_INTERVAL_QT
    *           the new value for the sCHED_INTERVAL_QT property
    */
   public void setSCHED_INTERVAL_QT( String sCHED_INTERVAL_QT ) {
      SCHED_INTERVAL_QT = sCHED_INTERVAL_QT;
   }


   /**
    * Returns the value of the sCHED_THRESHOLD_QT property.
    *
    * @return the value of the sCHED_THRESHOLD_QT property
    */
   public String getSCHED_THRESHOLD_QT() {
      return SCHED_THRESHOLD_QT;
   }


   /**
    * Sets a new value for the sCHED_THRESHOLD_QT property.
    *
    * @param sCHED_THRESHOLD_QT
    *           the new value for the sCHED_THRESHOLD_QT property
    */
   public void setSCHED_THRESHOLD_QT( String sCHED_THRESHOLD_QT ) {
      SCHED_THRESHOLD_QT = sCHED_THRESHOLD_QT;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( ASSMBL_CD == null ) ? 0 : ASSMBL_CD.hashCode() );
      result = prime * result + ( ( ATA_CD == null ) ? 0 : ATA_CD.hashCode() );
      result = prime * result + ( ( REF_TASK_CD == null ) ? 0 : REF_TASK_CD.hashCode() );
      result =
            prime * result + ( ( SCHED_DATA_TYPE_CD == null ) ? 0 : SCHED_DATA_TYPE_CD.hashCode() );
      result =
            prime * result + ( ( SCHED_INTERVAL_QT == null ) ? 0 : SCHED_INTERVAL_QT.hashCode() );
      result =
            prime * result + ( ( SCHED_THRESHOLD_QT == null ) ? 0 : SCHED_THRESHOLD_QT.hashCode() );
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
      cRefDynamicDeadline other = ( cRefDynamicDeadline ) obj;
      if ( ASSMBL_CD == null ) {
         if ( other.ASSMBL_CD != null )
            return false;
      } else if ( !ASSMBL_CD.equals( other.ASSMBL_CD ) )
         return false;
      if ( ATA_CD == null ) {
         if ( other.ATA_CD != null )
            return false;
      } else if ( !ATA_CD.equals( other.ATA_CD ) )
         return false;
      if ( REF_TASK_CD == null ) {
         if ( other.REF_TASK_CD != null )
            return false;
      } else if ( !REF_TASK_CD.equals( other.REF_TASK_CD ) )
         return false;
      if ( SCHED_DATA_TYPE_CD == null ) {
         if ( other.SCHED_DATA_TYPE_CD != null )
            return false;
      } else if ( !SCHED_DATA_TYPE_CD.equals( other.SCHED_DATA_TYPE_CD ) )
         return false;
      if ( SCHED_INTERVAL_QT == null ) {
         if ( other.SCHED_INTERVAL_QT != null )
            return false;
      } else if ( !SCHED_INTERVAL_QT.equals( other.SCHED_INTERVAL_QT ) )
         return false;
      if ( SCHED_THRESHOLD_QT == null ) {
         if ( other.SCHED_THRESHOLD_QT != null )
            return false;
      } else if ( !SCHED_THRESHOLD_QT.equals( other.SCHED_THRESHOLD_QT ) )
         return false;
      return true;
   }

}
