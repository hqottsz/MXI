package com.mxi.mx.common.ejb.task;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;

import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.ejb.stask.TaskLocal;
import com.mxi.mx.core.ejb.stask.TaskLocalHome;


/**
 * Stub form home interface for task local home.
 *
 * @author akovalevich
 */
public class TaskLocaHomelStub implements TaskLocalHome {

   /**
    * Creates a new task based on a Task Class with status as FORECAST if setTaskStatusToForecast
    * boolean is true.
    *
    * @return the new TaskLocal.
    *
    * @throws CreateException
    */
   @Override
   public TaskLocal create() throws CreateException {
      TaskBean lTaskBean = new TaskBean();
      lTaskBean.ejbCreate(); // initialization
      return new TaskLocalStub( lTaskBean );
   }


   @Override
   public void remove( Object aO ) throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }
}
