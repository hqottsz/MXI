package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for C_REF table
 *
 */
public class cREF {

   String REF_TASK_CD;
   String REF_TASK_NAME;
   String REF_TASK_DESC;
   String ASSMBL_CD;
   String ATA_CD;
   String CLASS_CD;
   String SUBCLASS_CD;
   String ORIGINATOR_CD;


   public cREF(String REF_TASK_CD, String REF_TASK_NAME, String REF_TASK_DESC, String ASSMBL_CD,
         String ATA_CD, String CLASS_CD, String SUBCLASS_CD, String ORIGINATOR_CD) {

      this.REF_TASK_CD = REF_TASK_CD;
      this.REF_TASK_NAME = REF_TASK_NAME;
      this.REF_TASK_DESC = REF_TASK_DESC;
      this.ASSMBL_CD = ASSMBL_CD;
      this.ATA_CD = ATA_CD;
      this.CLASS_CD = CLASS_CD;
      this.SUBCLASS_CD = SUBCLASS_CD;
      this.ORIGINATOR_CD = ORIGINATOR_CD;

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
    * Returns the value of the rEF_TASK_NAME property.
    *
    * @return the value of the rEF_TASK_NAME property
    */
   public String getREF_TASK_NAME() {
      return REF_TASK_NAME;
   }


   /**
    * Sets a new value for the rEF_TASK_NAME property.
    *
    * @param rEF_TASK_NAME
    *           the new value for the rEF_TASK_NAME property
    */
   public void setREF_TASK_NAME( String rEF_TASK_NAME ) {
      REF_TASK_NAME = rEF_TASK_NAME;
   }


   /**
    * Returns the value of the rEF_TASK_DESC property.
    *
    * @return the value of the rEF_TASK_DESC property
    */
   public String getREF_TASK_DESC() {
      return REF_TASK_DESC;
   }


   /**
    * Sets a new value for the rEF_TASK_DESC property.
    *
    * @param rEF_TASK_DESC
    *           the new value for the rEF_TASK_DESC property
    */
   public void setREF_TASK_DESC( String rEF_TASK_DESC ) {
      REF_TASK_DESC = rEF_TASK_DESC;
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
    * Returns the value of the cLASS_CD property.
    *
    * @return the value of the cLASS_CD property
    */
   public String getCLASS_CD() {
      return CLASS_CD;
   }


   /**
    * Sets a new value for the cLASS_CD property.
    *
    * @param cLASS_CD
    *           the new value for the cLASS_CD property
    */
   public void setCLASS_CD( String cLASS_CD ) {
      CLASS_CD = cLASS_CD;
   }


   /**
    * Returns the value of the sUBCLASS_CD property.
    *
    * @return the value of the sUBCLASS_CD property
    */
   public String getSUBCLASS_CD() {
      return SUBCLASS_CD;
   }


   /**
    * Sets a new value for the sUBCLASS_CD property.
    *
    * @param sUBCLASS_CD
    *           the new value for the sUBCLASS_CD property
    */
   public void setSUBCLASS_CD( String sUBCLASS_CD ) {
      SUBCLASS_CD = sUBCLASS_CD;
   }


   /**
    * Returns the value of the oRIGINATOR_CD property.
    *
    * @return the value of the oRIGINATOR_CD property
    */
   public String getORIGINATOR_CD() {
      return ORIGINATOR_CD;
   }


   /**
    * Sets a new value for the oRIGINATOR_CD property.
    *
    * @param oRIGINATOR_CD
    *           the new value for the oRIGINATOR_CD property
    */
   public void setORIGINATOR_CD( String oRIGINATOR_CD ) {
      ORIGINATOR_CD = oRIGINATOR_CD;
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
      result = prime * result + ( ( CLASS_CD == null ) ? 0 : CLASS_CD.hashCode() );
      result = prime * result + ( ( ORIGINATOR_CD == null ) ? 0 : ORIGINATOR_CD.hashCode() );
      result = prime * result + ( ( REF_TASK_CD == null ) ? 0 : REF_TASK_CD.hashCode() );
      result = prime * result + ( ( REF_TASK_DESC == null ) ? 0 : REF_TASK_DESC.hashCode() );
      result = prime * result + ( ( REF_TASK_NAME == null ) ? 0 : REF_TASK_NAME.hashCode() );
      result = prime * result + ( ( SUBCLASS_CD == null ) ? 0 : SUBCLASS_CD.hashCode() );
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
      cREF other = ( cREF ) obj;
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
      if ( CLASS_CD == null ) {
         if ( other.CLASS_CD != null )
            return false;
      } else if ( !CLASS_CD.equals( other.CLASS_CD ) )
         return false;
      if ( ORIGINATOR_CD == null ) {
         if ( other.ORIGINATOR_CD != null )
            return false;
      } else if ( !ORIGINATOR_CD.equals( other.ORIGINATOR_CD ) )
         return false;
      if ( REF_TASK_CD == null ) {
         if ( other.REF_TASK_CD != null )
            return false;
      } else if ( !REF_TASK_CD.equals( other.REF_TASK_CD ) )
         return false;
      if ( REF_TASK_DESC == null ) {
         if ( other.REF_TASK_DESC != null )
            return false;
      } else if ( !REF_TASK_DESC.equals( other.REF_TASK_DESC ) )
         return false;
      if ( REF_TASK_NAME == null ) {
         if ( other.REF_TASK_NAME != null )
            return false;
      } else if ( !REF_TASK_NAME.equals( other.REF_TASK_NAME ) )
         return false;
      if ( SUBCLASS_CD == null ) {
         if ( other.SUBCLASS_CD != null )
            return false;
      } else if ( !SUBCLASS_CD.equals( other.SUBCLASS_CD ) )
         return false;
      return true;
   }

}
