package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is to store data for usage unit information
 *
 */
public class usageInfor {

   String iTSN_QT;
   String iTSO_QT;
   String iTSI_QT;
   String iTSN_DELTA_QT;
   String iTSO_DELTA_QT;
   String iTSI_DELTA_QT;


   public usageInfor(String iTSN_QT, String iTSO_QT, String iTSI_QT, String iTSN_DELTA_QT,
         String iTSO_DELTA_QT, String iTSI_DELTA_QT) {

      this.iTSN_QT = iTSN_QT;
      this.iTSO_QT = iTSO_QT;
      this.iTSI_QT = iTSI_QT;
      this.iTSN_DELTA_QT = iTSN_DELTA_QT;
      this.iTSO_DELTA_QT = iTSO_DELTA_QT;
      this.iTSI_DELTA_QT = iTSI_DELTA_QT;

   }


   /**
    * Returns the value of the tSN_QT property.
    *
    * @return the value of the tSN_QT property
    */
   public String getTSN_QT() {
      return iTSN_QT;
   }


   /**
    * Sets a new value for the tSN_QT property.
    *
    * @param aTSN_QT
    *           the new value for the tSN_QT property
    */
   public void setTSN_QT( String aTSN_QT ) {
      iTSN_QT = aTSN_QT;
   }


   /**
    * Returns the value of the tSO_QT property.
    *
    * @return the value of the tSO_QT property
    */
   public String getTSO_QT() {
      return iTSO_QT;
   }


   /**
    * Sets a new value for the tSO_QT property.
    *
    * @param aTSO_QT
    *           the new value for the tSO_QT property
    */
   public void setTSO_QT( String aTSO_QT ) {
      iTSO_QT = aTSO_QT;
   }


   /**
    * Returns the value of the tSI_QT property.
    *
    * @return the value of the tSI_QT property
    */
   public String getTSI_QT() {
      return iTSI_QT;
   }


   /**
    * Sets a new value for the tSI_QT property.
    *
    * @param aTSI_QT
    *           the new value for the tSI_QT property
    */
   public void setTSI_QT( String aTSI_QT ) {
      iTSI_QT = aTSI_QT;
   }


   /**
    * Returns the value of the tSN_DELTA_QT property.
    *
    * @return the value of the tSN_DELTA_QT property
    */
   public String getTSN_DELTA_QT() {
      return iTSN_DELTA_QT;
   }


   /**
    * Sets a new value for the tSN_DELTA_QT property.
    *
    * @param aTSN_DELTA_QT
    *           the new value for the tSN_DELTA_QT property
    */
   public void setTSN_DELTA_QT( String aTSN_DELTA_QT ) {
      iTSN_DELTA_QT = aTSN_DELTA_QT;
   }


   /**
    * Returns the value of the tSO_DELTA_QT property.
    *
    * @return the value of the tSO_DELTA_QT property
    */
   public String getTSO_DELTA_QT() {
      return iTSO_DELTA_QT;
   }


   /**
    * Sets a new value for the tSO_DELTA_QT property.
    *
    * @param aTSO_DELTA_QT
    *           the new value for the tSO_DELTA_QT property
    */
   public void setTSO_DELTA_QT( String aTSO_DELTA_QT ) {
      iTSO_DELTA_QT = aTSO_DELTA_QT;
   }


   /**
    * Returns the value of the tSI_DELTA_QT property.
    *
    * @return the value of the tSI_DELTA_QT property
    */
   public String getTSI_DELTA_QT() {
      return iTSI_DELTA_QT;
   }


   /**
    * Sets a new value for the tSI_DELTA_QT property.
    *
    * @param aTSI_DELTA_QT
    *           the new value for the tSI_DELTA_QT property
    */
   public void setTSI_DELTA_QT( String aTSI_DELTA_QT ) {
      iTSI_DELTA_QT = aTSI_DELTA_QT;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ( ( iTSI_DELTA_QT == null ) ? 0 : iTSI_DELTA_QT.hashCode() );
      result = prime * result + ( ( iTSI_QT == null ) ? 0 : iTSI_QT.hashCode() );
      result = prime * result + ( ( iTSN_DELTA_QT == null ) ? 0 : iTSN_DELTA_QT.hashCode() );
      result = prime * result + ( ( iTSN_QT == null ) ? 0 : iTSN_QT.hashCode() );
      result = prime * result + ( ( iTSO_DELTA_QT == null ) ? 0 : iTSO_DELTA_QT.hashCode() );
      result = prime * result + ( ( iTSO_QT == null ) ? 0 : iTSO_QT.hashCode() );
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
      usageInfor other = ( usageInfor ) obj;
      if ( iTSI_DELTA_QT == null ) {
         if ( other.iTSI_DELTA_QT != null )
            return false;
      } else if ( !iTSI_DELTA_QT.equals( other.iTSI_DELTA_QT ) )
         return false;
      if ( iTSI_QT == null ) {
         if ( other.iTSI_QT != null )
            return false;
      } else if ( !iTSI_QT.equals( other.iTSI_QT ) )
         return false;
      if ( iTSN_DELTA_QT == null ) {
         if ( other.iTSN_DELTA_QT != null )
            return false;
      } else if ( !iTSN_DELTA_QT.equals( other.iTSN_DELTA_QT ) )
         return false;
      if ( iTSN_QT == null ) {
         if ( other.iTSN_QT != null )
            return false;
      } else if ( !iTSN_QT.equals( other.iTSN_QT ) )
         return false;
      if ( iTSO_DELTA_QT == null ) {
         if ( other.iTSO_DELTA_QT != null )
            return false;
      } else if ( !iTSO_DELTA_QT.equals( other.iTSO_DELTA_QT ) )
         return false;
      if ( iTSO_QT == null ) {
         if ( other.iTSO_QT != null )
            return false;
      } else if ( !iTSO_QT.equals( other.iTSO_QT ) )
         return false;
      return true;
   }

}
