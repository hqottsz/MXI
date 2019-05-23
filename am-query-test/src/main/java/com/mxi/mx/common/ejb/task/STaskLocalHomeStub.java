package com.mxi.mx.common.ejb.task;

import com.mxi.mx.core.ejb.stask.STaskBean;
import com.mxi.mx.core.ejb.stask.STaskLocal;
import com.mxi.mx.core.ejb.stask.STaskLocalHome;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;


/**
 * Stub form home interface for Stask local home.
 *
 * @author akovalevich
 */
public class STaskLocalHomeStub implements STaskLocalHome {

   /**
    * This method is used to create a new STask EJB instance.
    *
    * @return STaskLocal interface
    *
    * @exception CreateException
    *               if an error occurs in attempting the create opereration.
    */
   @Override
   public STaskLocal create() throws CreateException {
      STaskBean lSTaskBean = new STaskBean();
      lSTaskBean.ejbCreate();
      return new STaskLocalStub( lSTaskBean );
   }


   @Override
   public void remove( Object aO ) throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }
}
