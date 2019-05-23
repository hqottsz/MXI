package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.SameInventoryException;


public class SameInventoryExceptionMatcher extends TypeSafeMatcher<SameInventoryException> {

   private InventoryKey iInventory;
   private String iDescription;
   private String iBarcode;


   public SameInventoryExceptionMatcher(InventoryKey aInventory, String aDescription,
         String aBarcode) {
      super();
      iInventory = aInventory;
      iDescription = aDescription;
      iBarcode = aBarcode;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "expected InventoryKey=" + iInventory + ", Description="
            + iDescription + ", Barcode=" + iBarcode );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void describeMismatchSafely( SameInventoryException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "was InventoryKey=" + aItem.getInventory().get()
            + ", Description=" + aItem.getInventoryDescription().get() + ", Barcode="
            + aItem.getInventoryBarcode().get() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( SameInventoryException aException ) {
      return ( iInventory.equals( aException.getInventory().get() )
            && iDescription.equals( aException.getInventoryDescription().get() )
            && iBarcode.equals( aException.getInventoryBarcode().get() ) );
   }

}
