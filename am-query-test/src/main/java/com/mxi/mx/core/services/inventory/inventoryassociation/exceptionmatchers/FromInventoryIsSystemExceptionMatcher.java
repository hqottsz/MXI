package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.FromInventoryIsSystemException;


public class FromInventoryIsSystemExceptionMatcher
      extends TypeSafeMatcher<FromInventoryIsSystemException> {

   private InventoryKey iInventory;
   private String iInvDescription;


   public FromInventoryIsSystemExceptionMatcher(InventoryKey aInventory, String aInvDescription) {
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
   protected void describeMismatchSafely( FromInventoryIsSystemException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "expected InventoryKey=" + aItem.getInventory().get()
            + ", Description=" + aItem.getInventoryDescription().get() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( FromInventoryIsSystemException aException ) {
      return ( iInventory.equals( aException.getInventory().get() )
            && iInvDescription.equals( aException.getInventoryDescription().get() ) );
   }

}
