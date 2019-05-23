package com.mxi.mx.core.services.inventory.inventoryassociation.exceptionmatchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.inventory.inventoryassociation.exceptions.BothInventoryInAssociationsException;


public class BothInventoryInAssociationsExceptionMatcher
      extends TypeSafeMatcher<BothInventoryInAssociationsException> {

   private InventoryKey iFromInventory;
   private String iFromInvDescription;
   private InventoryKey iToInventory;
   private String iToInvDescription;
   private Integer iAssociation;


   public BothInventoryInAssociationsExceptionMatcher(InventoryKey aFromInventory,
         String aFromInvDescription, InventoryKey aToInventory, String aToInvDescription) {
      super();
      iFromInventory = aFromInventory;
      iFromInvDescription = aFromInvDescription;
      iToInventory = aToInventory;
      iToInvDescription = aToInvDescription;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void describeTo( Description aDescription ) {
      aDescription.appendText( "expected From-InventoryKey=" + iFromInventory
            + ", From-Description=" + iFromInvDescription + ", To-InventoryKey=" + iToInventory
            + ", To-Description=" + iToInvDescription + ", Association Id=" + iAssociation );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void describeMismatchSafely( BothInventoryInAssociationsException aItem,
         Description aMismatchDescription ) {
      aMismatchDescription.appendText( "expected From-InventoryKey=" + aItem.getFromInventory()
            + ", From-Description=" + aItem.getFromInventoryDescription() + ", To-InventoryKey="
            + aItem.getToInventory() + ", To-Description=" + aItem.getToInventoryDescription() );
   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean matchesSafely( BothInventoryInAssociationsException aException ) {
      return ( iFromInventory.equals( aException.getFromInventory() )
            && iFromInvDescription.equals( aException.getFromInventoryDescription() )
            && iToInventory.equals( aException.getToInventory() )
            && iToInvDescription.equals( aException.getToInventoryDescription() ) );
   }

}
