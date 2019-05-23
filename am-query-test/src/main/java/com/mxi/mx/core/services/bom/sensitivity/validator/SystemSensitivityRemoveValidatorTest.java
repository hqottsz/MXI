package com.mxi.mx.core.services.bom.sensitivity.validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.core.exception.DoesNotExistException;
import com.mxi.mx.core.key.ConfigSlotKey;


public class SystemSensitivityRemoveValidatorTest {

   // Test Data
   private static final ConfigSlotKey SYSTEM_NON_EXISTING = new ConfigSlotKey( 9999, "FAKE", 999 );
   private static final ConfigSlotKey SYSTEM_VALID = new ConfigSlotKey( "4650:TEST:0" );

   @Rule
   public DatabaseConnectionRule iConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   // Object under test
   private SystemSensitivityRemoveValidator iValidator;


   @Before
   public void setUp() {
      DataLoaders.load( iConnectionRule.getConnection(), this.getClass() );
      iValidator = new SystemSensitivityRemoveValidator();
   }


   @Test( expected = MandatoryArgumentException.class )
   public void validate_null() throws Throwable {
      iValidator.validate( null );
   }


   @Test( expected = DoesNotExistException.class )
   public void validate_nonExistingConfigSlot() throws Throwable {
      iValidator.validate( SYSTEM_NON_EXISTING );
   }


   @Test
   public void validate_valid() throws Throwable {
      iValidator.validate( SYSTEM_VALID );
   }
}
