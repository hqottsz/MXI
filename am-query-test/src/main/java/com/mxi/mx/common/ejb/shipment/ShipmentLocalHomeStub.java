package com.mxi.mx.common.ejb.shipment;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;

import com.mxi.mx.core.ejb.shipment.ShipmentBean;
import com.mxi.mx.core.ejb.shipment.ShipmentLocal;
import com.mxi.mx.core.ejb.shipment.ShipmentLocalHome;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Stub from home interface for ShipmentLocalHome.
 *
 */
public class ShipmentLocalHomeStub extends ResourceBeanTest implements ShipmentLocalHome {

   private static final String AUTHORIZED = "authorized";


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
   public ShipmentLocal create() throws CreateException {
      ShipmentBean lShipmentBean = new ShipmentBean();
      lShipmentBean.setSessionContext( getSessionContext() );
      lShipmentBean.ejbCreate();
      return new ShipmentLocalStub( lShipmentBean );
   }

}
