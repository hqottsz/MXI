
package com.mxi.mx.core.unittest.po;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.services.order.exception.OrderAuthorizationInProgressException;
import com.mxi.mx.core.services.order.exception.OrderAuthorizationInProgressValidator;


/**
 * This class tests the OrderAuthorizationInProgressValidator method.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrderAuthorizationInProgressValidatorTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Validates that the xception is thrown when the order is in the middle of authorization
    * process.
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test( expected = OrderAuthorizationInProgressException.class )
   public void testValidateOrderAuthorizationInProgress() throws Exception {
      new OrderAuthorizationInProgressValidator(
            new OrderBuilder().withAuthStatus( RefPoAuthLvlStatusKey.REQUESTED ).build() )
                  .validate();
   }

}
