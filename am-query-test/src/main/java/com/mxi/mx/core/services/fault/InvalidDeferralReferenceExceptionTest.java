package com.mxi.mx.core.services.fault;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;


public class InvalidDeferralReferenceExceptionTest {

   private static final InventoryKey A_VALID_INVENTORY = new InventoryKey( 4650, 1 );
   private static final InventoryKey AN_INVALID_INVENTORY = new InventoryKey( 4650, 3 );
   private static final String A_VALID_DEFERRAL_REFERENCE = "ValidRef";
   private static final String AN_INVALID_DEFERRAL_REFERENCE = "InvalidRef";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void isValid_returnsTrueWhenDeferralReferenceIsValid() {
      assertTrue( InvalidDeferralReferenceException.isValid( A_VALID_INVENTORY,
            A_VALID_DEFERRAL_REFERENCE ) );
   }


   @Test
   public void isValid_returnsFalseWhenBomItemPositionOfHighestInventoryIsNull() {
      assertFalse( InvalidDeferralReferenceException.isValid( AN_INVALID_INVENTORY,
            A_VALID_DEFERRAL_REFERENCE ) );
   }


   @Test
   public void isValid_returnsFalseWhenDeferralReferenceIsInvalid() {
      assertFalse( InvalidDeferralReferenceException.isValid( A_VALID_INVENTORY,
            AN_INVALID_DEFERRAL_REFERENCE ) );
   }


   @Before
   public void loadData() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
