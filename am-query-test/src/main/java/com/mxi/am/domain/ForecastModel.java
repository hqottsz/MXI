
package com.mxi.am.domain;

import java.util.Set;
import java.util.TreeSet;

import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.DataTypeKey;


/**
 * The forecast model
 */
public final class ForecastModel {

   private String iName;
   private AuthorityKey iAuthority;

   private Set<RangeInfo> iRanges = new TreeSet<RangeInfo>();


   ForecastModel() {

   }


   public void setAuthority( AuthorityKey aAuthorityKey ) {
      iAuthority = aAuthorityKey;
   }


   public void setName( String aName ) {
      iName = aName;
   }


   public void addRange( int aStartMonth, int aStartDay, DataTypeKey aDataType, double aRate ) {
      iRanges.add( new RangeInfo( aStartMonth, aStartDay, aDataType, aRate ) );
   }


   public String getName() {
      return iName;
   }


   public AuthorityKey getAuthority() {
      return iAuthority;
   }


   public Set<RangeInfo> getRanges() {
      return iRanges;
   }


   /**
    * POJO to hold forecast range information.
    */
   public class RangeInfo implements Comparable<RangeInfo> {

      private DataTypeKey iDataType;
      private double iRate;
      private int iStartDay;
      private int iStartMonth;


      public RangeInfo(int aStartMonth, int aStartDay, DataTypeKey aDataType, double aRate) {
         iStartMonth = aStartMonth;
         iStartDay = aStartDay;
         iDataType = aDataType;
         iRate = aRate;
      }


      public double getRate() {
         return iRate;
      }


      public int getStartDay() {
         return iStartDay;
      }


      public int getStartMonth() {
         return iStartMonth;
      }


      public DataTypeKey getDataType() {
         return iDataType;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int compareTo( RangeInfo aO ) {

         if ( this.getStartMonth() == aO.getStartMonth() )
            return this.getStartDay() - aO.getStartDay();

         return this.getStartMonth() - aO.getStartMonth();
      }
   }

}
