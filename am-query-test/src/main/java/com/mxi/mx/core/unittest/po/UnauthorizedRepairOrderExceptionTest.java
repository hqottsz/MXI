package com.mxi.mx.core.unittest.po;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.key.RefPoTypeKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.services.order.exception.UnauthorizedRepairOrderException;


public class UnauthorizedRepairOrderExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This tests the UnauthorizedRepairOrderExceptionException
    */
   @Test
   public void testUnauthorizedRepairOrderException() throws Exception {

      // create a vendor
      VendorKey lVendorKey = Domain.createVendor();

      // create unauthorized repair order
      PurchaseOrderKey lRepairOrderKey = Domain.createPurchaseOrder( aOrder -> {
         aOrder.orderType( RefPoTypeKey.REPAIR );
         aOrder.authStatus( RefPoAuthLvlStatusKey.REQUESTED );
         aOrder.vendor( lVendorKey );
      } );

      try {
         UnauthorizedRepairOrderException.validate( lRepairOrderKey );
         fail( "Expected UnauthorizedRepairOrderExceptionException" );
      } catch ( UnauthorizedRepairOrderException e ) {
         assertEquals( "[MXERR-31661] " + i18n.get( "core.err.31661", "REPAIR" ), e.getMessage() );
      }

   }
}
