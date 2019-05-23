
package com.mxi.mx.common.ejb.sequence;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.key.MxDbKey;
import com.mxi.mx.common.services.sequence.MxContextSequence;
import com.mxi.mx.common.table.QuerySetFactory;


/**
 * DOCUMENT_ME
 *
 * @author asmolko
 */
public class SequenceGeneratorStub implements SequenceGeneratorLocal {

   private Map<String, Integer> iSequenceMap = new HashMap<String, Integer>();


   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public Object getPrimaryKey() throws EJBException {
      throw new UnsupportedOperationException();
   }


   @Override
   public boolean isIdentical( EJBLocalObject aObj ) throws EJBException {
      throw new UnsupportedOperationException();
   }


   /**
    * DOCUMENT_ME
    *
    * @param aSequenceCode
    *           DOCUMENT_ME
    *
    * @return DOCUMENT_ME
    */
   @Override
   public int nextValue( String aSequenceCode ) {
      Integer lValue = iSequenceMap.get( aSequenceCode );
      if ( lValue == null ) {
         lValue = 1;
      } else {
         lValue = lValue.intValue() + 1;
      }

      iSequenceMap.put( aSequenceCode, lValue );

      return lValue;
   }


   /**
    * Gets the next specified number of values for a given sequence.
    *
    * @param aSequenceCode
    *           the sequence code
    * @param aCount
    *           the number of values to retrieve
    *
    * @return an array of new sequence values
    */
   @Override
   public int[] nextValues( String aSequenceCode, int aCount ) {
      int[] lNextValues = new int[aCount];
      for ( int i = 0; i < aCount; i++ ) {
         lNextValues[i] = nextValue( aSequenceCode );
      }

      return lNextValues;
   }


   /**
    * DOCUMENT_ME
    *
    * @param aContextSequence
    *           DOCUMENT_ME
    * @param aContextKey
    *           DOCUMENT_ME
    *
    * @return DOCUMENT_ME
    *
    * @throws MandatoryArgumentException
    *            DOCUMENT_ME
    */
   @Override
   public int nextValueWithContext( MxContextSequence aContextSequence, MxDbKey aContextKey )
         throws MandatoryArgumentException {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aContextKey, aContextSequence.getContextColNames() );

      Integer lValue = null;
      QuerySet lResults = QuerySetFactory.getInstance().executeQuery(
            new String[] { "MAX(" + aContextSequence.getValueColName() + ") as value" },
            aContextSequence.getTableName(), lArgs );
      if ( lResults.first() ) {
         lValue = lResults.getInt( "value" ) + 1;
      } else {
         lValue = 1;
      }

      return lValue.intValue();
   }


   /**
    * DOCUMENT_ME
    *
    * @throws RemoveException
    *            DOCUMENT_ME
    * @throws EJBException
    *            DOCUMENT_ME
    */
   @Override
   public void remove() throws RemoveException, EJBException {
      throw new UnsupportedOperationException();
   }


   /**
    * DOCUMENT_ME
    *
    * @param aContextSequence
    *           DOCUMENT_ME
    * @param aContextKey
    *           DOCUMENT_ME
    *
    * @throws MandatoryArgumentException
    *            DOCUMENT_ME
    */
   @Override
   public void removeContextSequence( MxContextSequence aContextSequence, MxDbKey aContextKey )
         throws MandatoryArgumentException {
      throw new UnsupportedOperationException();
   }
}
