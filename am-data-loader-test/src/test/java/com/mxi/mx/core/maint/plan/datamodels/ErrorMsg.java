package com.mxi.mx.core.maint.plan.datamodels;

/**
 * This class is store error code description
 *
 */
public class ErrorMsg {

   public String RESULT_CD;
   public String DESC_LDESC;


   public ErrorMsg(String RESULT_CD, String DESC_LDESC) {
      this.RESULT_CD = RESULT_CD;
      this.DESC_LDESC = DESC_LDESC;
   }


   /**
    * Returns the value of the rESULT_CD property.
    *
    * @return the value of the rESULT_CD property
    */
   public String getRESULT_CD() {
      return RESULT_CD;
   }


   /**
    * Sets a new value for the rESULT_CD property.
    *
    * @param aRESULT_CD
    *           the new value for the rESULT_CD property
    */
   public void setRESULT_CD( String aRESULT_CD ) {
      RESULT_CD = aRESULT_CD;
   }


   /**
    * Returns the value of the dESC_LDESC property.
    *
    * @return the value of the dESC_LDESC property
    */
   public String getDESC_LDESC() {
      return DESC_LDESC;
   }


   /**
    * Sets a new value for the dESC_LDESC property.
    *
    * @param aDESC_LDESC
    *           the new value for the dESC_LDESC property
    */
   public void setDESC_LDESC( String aDESC_LDESC ) {
      DESC_LDESC = aDESC_LDESC;
   }

}
