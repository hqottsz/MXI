package com.mxi.mx.common.ejb;

import com.mxi.mx.common.ejb.alert.AlertEngineLocal;
import com.mxi.mx.common.ejb.alert.AlertEngineLocalStub;
import com.mxi.mx.common.ejb.dao.DAOLocal;
import com.mxi.mx.common.ejb.security.SecurityIdentityLocal;
import com.mxi.mx.common.ejb.security.SecurityIdentityStub;
import com.mxi.mx.common.ejb.sequence.SequenceGeneratorLocal;
import com.mxi.mx.common.ejb.sequence.SequenceGeneratorStub;


/**
 * This stub provides delegate stubs to avoid a dependency on a running application server.
 */
public class EjbFactoryStub extends EjbFactory {

   private final SecurityIdentityLocal iSecurityIdentityStub;

   private SequenceGeneratorStub iSequenceGenerator;

   private DAOLocalStub iDataAccessObjectStub;


   /**
    * Creates a new EjBFactoryStub object.
    */
   public EjbFactoryStub() {
      this( new SecurityIdentityStub(), new DAOLocalStub() );
   }


   /**
    * Creates a new EjBFactoryStub object.
    *
    * @param aSecurityIdentityStub
    *           The injected security stub
    */
   public EjbFactoryStub(SecurityIdentityLocal aSecurityIdentityStub,
         DAOLocalStub aDataAccessObjectStub) {
      super( true );
      iSecurityIdentityStub = aSecurityIdentityStub;
      iDataAccessObjectStub = aDataAccessObjectStub;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public AlertEngineLocal createAlertEngine() {
      return new AlertEngineLocalStub();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public SecurityIdentityLocal createSecurityIdentity() {
      return iSecurityIdentityStub;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public SequenceGeneratorLocal createSequenceGenerator() {
      if ( iSequenceGenerator == null ) {
         iSequenceGenerator = new SequenceGeneratorStub();
      }

      return iSequenceGenerator;
   }


   @Override
   public DAOLocal createDataAccessObject() {
      return iDataAccessObjectStub;
   }
}
