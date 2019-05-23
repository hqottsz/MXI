package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is store data from finance account query
 *
 */
public class fnc_account {

   String ACCOUNT_TYPE_CD;
   String ACCOUNT_CD;
   String ACCOUNT_NAME;


   public fnc_account(String ACCOUNT_TYPE_CD, String ACCOUNT_CD, String ACCOUNT_NAME) {
      this.ACCOUNT_TYPE_CD = ACCOUNT_TYPE_CD;
      this.ACCOUNT_CD = ACCOUNT_CD;
      this.ACCOUNT_NAME = ACCOUNT_NAME;
   }


   /**
    * Returns the value of the aCCOUNT_TYPE_CD property.
    *
    * @return the value of the aCCOUNT_TYPE_CD property
    */
   public String getACCOUNT_TYPE_CD() {
      return ACCOUNT_TYPE_CD;
   }


   /**
    * Sets a new value for the aCCOUNT_TYPE_CD property.
    *
    * @param aACCOUNT_TYPE_CD
    *           the new value for the aCCOUNT_TYPE_CD property
    */
   public void setACCOUNT_TYPE_CD( String aACCOUNT_TYPE_CD ) {
      ACCOUNT_TYPE_CD = aACCOUNT_TYPE_CD;
   }


   /**
    * Returns the value of the aCCOUNT_CD property.
    *
    * @return the value of the aCCOUNT_CD property
    */
   public String getACCOUNT_CD() {
      return ACCOUNT_CD;
   }


   /**
    * Sets a new value for the aCCOUNT_CD property.
    *
    * @param aACCOUNT_CD
    *           the new value for the aCCOUNT_CD property
    */
   public void setACCOUNT_CD( String aACCOUNT_CD ) {
      ACCOUNT_CD = aACCOUNT_CD;
   }


   /**
    * Returns the value of the aCCOUNT_NAME property.
    *
    * @return the value of the aCCOUNT_NAME property
    */
   public String getACCOUNT_NAME() {
      return ACCOUNT_NAME;
   }


   /**
    * Sets a new value for the aCCOUNT_NAME property.
    *
    * @param aACCOUNT_NAME
    *           the new value for the aCCOUNT_NAME property
    */
   public void setACCOUNT_NAME( String aACCOUNT_NAME ) {
      ACCOUNT_NAME = aACCOUNT_NAME;
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
      fnc_account other = ( fnc_account ) obj;
      if ( ACCOUNT_CD == null ) {
         if ( other.ACCOUNT_CD != null )
            return false;
      } else if ( !ACCOUNT_CD.equalsIgnoreCase( other.ACCOUNT_CD ) )
         return false;
      if ( ACCOUNT_NAME == null ) {
         if ( other.ACCOUNT_NAME != null )
            return false;
      } else if ( !ACCOUNT_NAME.equalsIgnoreCase( other.ACCOUNT_NAME ) )
         return false;
      if ( ACCOUNT_TYPE_CD == null ) {
         if ( other.ACCOUNT_TYPE_CD != null )
            return false;
      } else if ( !ACCOUNT_TYPE_CD.equalsIgnoreCase( other.ACCOUNT_TYPE_CD ) )
         return false;
      return true;
   }

}
