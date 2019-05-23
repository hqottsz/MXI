package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.ToInventoryIsSystemException;


public class ToInventoryIsSystemExceptionMatcher
      extends TypeSafeMatcher<ToInventoryIsSystemException> {

   private InventoryKey iInventory;
   private String iInvDescription;


   public ToInventoryIsSystemExceptionMatcher(InventoryKey aInventory, String aInvDescription) {
      super();
      iInventory = aInventory;
      iInvDescription = aInvDescription;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText(
            "expected InventoryKey=" + iInventory + ", Description=" + iInvDescription );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void describeMismatchSafely( ToInventoryIsSystemException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "expected InventoryKey=" + aItem.getInventory().get()
            + ", Description=" + aItem.getInventoryDescription().get() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( ToInventoryIsSystemException aException ) {
      return ( iInventory.equals( aException.getInventory().get() )
            && iInvDescription.equals( aException.getInventoryDescription().get() ) );
   }

}
