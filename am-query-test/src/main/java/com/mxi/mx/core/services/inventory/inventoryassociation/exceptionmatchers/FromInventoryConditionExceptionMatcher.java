package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.FromInventoryConditionException;


public class FromInventoryConditionExceptionMatcher
      extends TypeSafeMatcher<FromInventoryConditionException> {

   private InventoryKey iInventory;
   private String iDescription;
   private RefInvCondKey iCondition;


   public FromInventoryConditionExceptionMatcher(InventoryKey aInventory, String aDescription,
         RefInvCondKey aCondition) {
      super();
      iInventory = aInventory;
      iDescription = aDescription;
      iCondition = aCondition;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "expected InventoryKey=" + iInventory + ", Description="
            + iDescription + ", Condition=" + iCondition );
   }


   @Override
   protected void describeMismatchSafely( FromInventoryConditionException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "was InventoryKey=" + aItem.getInventory().get()
            + ", Description=" + aItem.getInventoryDescription().get() + ", Condition="
            + aItem.getInventoryCondition() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( FromInventoryConditionException aException ) {
      return ( iInventory.equals( aException.getInventory().get() )
            && iDescription.equals( aException.getInventoryDescription().get() )
            && iCondition.equals( aException.getInventoryCondition() ) );
   }

}
