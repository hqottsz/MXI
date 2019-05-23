
package com.mxi.am.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


public final class Repl {

   private TaskKey iComponentWorkPackage;
   private List<UsageDeadline> iUsageDeadlines = new ArrayList<>();
   private InventoryKey iMainInventory;


   Repl() {

   }


   public void setComponentWorkPackage( TaskKey aComponentWorkPackage ) {
      iComponentWorkPackage = aComponentWorkPackage;
   }


   public void addUsageDeadline( final DataTypeKey aType, final double aDueQuantity,
         final Date aDueDate ) {
      addUsageDeadline( new DomainConfiguration<UsageDeadline>() {

         @Override
         public void configure( UsageDeadline aUsageDeadline ) {
            aUsageDeadline.setType( aType );
            aUsageDeadline.setDueQuantity( aDueQuantity );
            aUsageDeadline.setDueDate( aDueDate );
         }
      } );
   }


   public void addUsageDeadline( DomainConfiguration<UsageDeadline> aUsageDeadlineConfiguration ) {
      UsageDeadline lUsageDeadline = new UsageDeadline();
      aUsageDeadlineConfiguration.configure( lUsageDeadline );

      iUsageDeadlines.add( lUsageDeadline );
   }


   public TaskKey getComponentWorkPackage() {
      return iComponentWorkPackage;
   }


   public List<UsageDeadline> getUsageDeadline() {
      return iUsageDeadlines;
   }


   public InventoryKey getMainInventory() {
      return iMainInventory;
   }


   public void setMainInventory( InventoryKey aMainInventory ) {
      iMainInventory = aMainInventory;
   }


   public static final class UsageDeadline {

      private DataTypeKey iType;
      private Double iDueQuantity;
      private Date iDueDate;


      public DataTypeKey getType() {
         return iType;
      }


      public void setType( DataTypeKey aType ) {
         iType = aType;
      }


      public Double getDueQuantity() {
         return iDueQuantity;
      }


      public void setDueQuantity( Double aDueQuantity ) {
         iDueQuantity = aDueQuantity;
      }


      public Date getDueDate() {
         return iDueDate;
      }


      public void setDueDate( Date aDueDate ) {
         iDueDate = aDueDate;
      }
   }
}
