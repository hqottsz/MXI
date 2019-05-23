
package com.mxi.mx.core.unittest.fault;

import static org.junit.Assert.fail;

import java.util.Date;

import javax.ejb.SessionContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.EventBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.fault.SFaultBean;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.fault.FaultDetailsService;
import com.mxi.mx.core.services.inventory.exception.LockedInventoryException;


/**
 * This class tests corresponding method from SFaultLocal interface.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class CFSetRaisedDateTest {

   private final Mockery iContext = new Mockery();
   private FaultDetailsService iMockFaultDetailsService;
   private SessionContext iMockSessionCtx;
   private SFaultBean iSFaultBean;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Sets up the test case.
    *
    * @exception Exception
    *               if an error occurs.
    */
   @Before
   public void loadData() throws Exception {
      iMockSessionCtx = iContext.mock( SessionContext.class );
      iMockFaultDetailsService = iContext.mock( FaultDetailsService.class );

      iSFaultBean = new SFaultBean( iMockFaultDetailsService );
      iSFaultBean.setSessionContext( iMockSessionCtx );
   }


   /**
    * Tests the conditions when {@link LockedInventoryExceptionLockedInventoryException} is thrown.
    *
    * <ol>
    * <li>Obtain a locked fault.</li>
    * <li>Attempt to set the raised date to a valid value.</li>
    * <li>Make sure {@link LockedInventoryExceptionLockedInventoryException} is thrown.</li>
    * </ol>
    *
    * <p>
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testLockedInventory() throws Exception {
      InventoryKey lInventory = new InventoryBuilder().isLocked().build();
      EventKey lEvent = new EventBuilder().onInventory( lInventory ).build();

      expectTransactionRollback();

      try {

         // call business method
         iSFaultBean.setRaisedDate( new FaultKey( lEvent ), new Date() );
         fail( "Should raise an LockedInventoryException" );
      } catch ( LockedInventoryException lLockedInventoryException ) {
         // pass
      }

      iContext.assertIsSatisfied();
   }


   /**
    * Tests the conditions when {@link MandatoryArgumentExceptionMandatoryArgumentException} is
    * thrown.
    *
    * <ol>
    * <li>Obtain a historic fault.</li>
    * <li>Attempt to set the raised date to null.</li>
    * <li>Make sure that {@link MandatoryArgumentExceptionMandatoryArgumentException} is
    * thrown.</li>
    * </ol>
    *
    * <p>
    *
    * @exception Exception
    *               if something goes wrong.
    */
   @Test
   public void testMandatoryArgument() throws Exception {
      expectTransactionRollback();

      try {

         // call business method
         iSFaultBean.setRaisedDate( new FaultKey( 4650, 1 ), null );
         fail( "Should raise an MandatoryArgumentException" );
      } catch ( MandatoryArgumentException lMandatoryArgumentException ) {
         // pass
      }

      iContext.assertIsSatisfied();
   }


   /**
    * Add an expectation that the tracation will be rolled back.
    */
   private void expectTransactionRollback() {
      iContext.checking( new Expectations() {

         {
            one( iMockSessionCtx ).setRollbackOnly();
         }
      } );
   }
}
