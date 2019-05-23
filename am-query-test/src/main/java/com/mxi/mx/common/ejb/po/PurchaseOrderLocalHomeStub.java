package com.mxi.mx.common.ejb.po;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;

import com.mxi.mx.core.ejb.po.PurchaseOrderBean;
import com.mxi.mx.core.ejb.po.PurchaseOrderLocal;
import com.mxi.mx.core.ejb.po.PurchaseOrderLocalHome;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Stub from home interface for PurchaseOrderLocalHome.
 *
 */
public class PurchaseOrderLocalHomeStub extends ResourceBeanTest implements PurchaseOrderLocalHome {

   /**
    * no-op method. Not currently required.
    */
   @Override
   public void remove( Object aArg0 ) throws RemoveException, EJBException {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public PurchaseOrderLocal create() throws CreateException {
      PurchaseOrderBean lPurchaseOrderBean = new PurchaseOrderBean();
      lPurchaseOrderBean.setSessionContext( getSessionContext() );
      lPurchaseOrderBean.ejbCreate();
      return new PurchaseOrderLocalStub( lPurchaseOrderBean );
   }

}
