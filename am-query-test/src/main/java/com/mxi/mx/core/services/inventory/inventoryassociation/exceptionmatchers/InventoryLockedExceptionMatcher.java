package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.FromInventoryLockedException;


public class InventoryLockedExceptionMatcher extends TypeSafeMatcher<FromInventoryLockedException> {

   private InventoryKey iInventory;
   private String iDescription;


   public InventoryLockedExceptionMatcher(InventoryKey aInventory, String aDescription) {
      super();
      iInventory = aInventory;
      iDescription = aDescription;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription
            .appendText( "expected InventoryKey=" + iInventory + ", Description=" + iDescription );
   }


   @Override
   protected void describeMismatchSafely( FromInventoryLockedException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "was InventoryKey=" + aItem.getInventory().get()
            + ", Description=" + aItem.getInventoryDescription().get() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( FromInventoryLockedException aException ) {
      return ( iInventory.equals( aException.getInventory().get() )
            && iDescription.equals( aException.getInventoryDescription().get() ) );
   }

}
