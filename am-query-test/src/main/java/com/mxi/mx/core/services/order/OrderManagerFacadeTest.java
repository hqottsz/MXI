
package com.mxi.mx.core.services.order;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.OrderBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.dao.po.PoLineMpTableDao;
import com.mxi.mx.core.exception.DuplicateMpiKeyException;
import com.mxi.mx.core.facade.order.OrderManagerFacade;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.PoLineMpKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.model.order.OrderModifier;


/**
 * This class test the methods in {@link OrderManagerFacade}.
 *
 * @author Libin Cai
 * @created May 29, 2018
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class OrderManagerFacadeTest {

   private static PurchaseOrderKey iOrder;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * This test case executes OrderManagerFacade.manageOrder to create a order line with MP KEY and
    * make sure the MP KEY is saved into the database.
    *
    * @throws Exception
    *            if any error occurs
    */
   @Test
   public void testAddPartLine() throws Exception {

      addPartLine();

      Assert.assertEquals( "MP_KEY_SDESC",
            InjectorContainer.get().getInstance( PoLineMpTableDao.class )
                  .findByPrimaryKey( new PoLineMpKey( iOrder.getDbId(), iOrder.getId(), 1 ) )
                  .getMpKeySdesc() );

   }


   /*
    * GIVEN a purchase order is created through MPI (Material Planning Integration) and purchase
    * order line created with received MP_KEY_SDESC WHEN another order line received through MPI
    * with same MP_KEY_SDESC THEN returns a exception
    *
    * @throws Exception if any error occurs
    */

   @Test
   public void testDuplicateMpKeyException() throws Exception {

      // add part line
      addPartLine();
      try {
         // add another part line with same MP_KEY_SDESC
         addPartLine();
         Assert.fail( "Expected DuplicateMpiKeyException" );
      } catch ( DuplicateMpiKeyException lException ) {
         // Expected DuplicateMpiKeyException
      }
   }


   @Before
   public void loadData() throws Exception {

      HumanResourceKey lHr =
            Domain.createHumanResource( aHr -> aHr.setUser( Domain.createUser() ) );

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHr ) );

      iOrder = new OrderBuilder().withVendor( new VendorBuilder().build() ).build();

   }


   private void addPartLine() throws Exception {
      OrderLineTO lOrderLineTo = new OrderLineTO();
      lOrderLineTo.setMpKeySdesc( "MP_KEY_SDESC" );
      lOrderLineTo.setPartNo( new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).build(),
            "Part No" );
      OrderManagerFacade.manageOrder( iOrder, new OrderModifier( iOrder, lOrderLineTo ), null );
   }

}
