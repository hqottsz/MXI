package com.mxi.mx.testing.mock.cache;

import org.jboss.cache.loader.CacheLoader;

import com.mxi.mx.common.cache.CacheFactory;
import com.mxi.mx.common.cache.ConfigCacheLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DatabaseStatement;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.timezone.TimeZoneCacheLoader;


/**
 * <p>
 * Minimal mock request data to facilitate testing without having a server running. This circumvents
 * JNDI calls during cache creation.
 * </p>
 *
 * <p>
 * <b>Warning: this implementation is extremely limited.</b> If you plan to use it in your tests,
 * you may have to extend its functionality. At the moment, it allows only one cache to be defined.
 *
 * @author gpearson
 */
public class MockCacheFactory extends CacheFactory {

   private static final long serialVersionUID = 1L;


   /**
    * Creates a new MockCacheFactory object.
    *
    * @param aMockCacheLoader
    *           data cache for this factory.
    *
    * @throws Exception
    *            if an error occurs.
    */
   public MockCacheFactory(CacheLoader aMockCacheLoader) throws Exception {

      this( new CacheLoader[] { aMockCacheLoader } );
   }


   /**
    * Creates a new {@linkplain MockCacheFactory} object.
    *
    * @param aMockCacheLoaders
    *           data caches for this factory.
    *
    * @throws Exception
    *            if an error occurs.
    */
   public MockCacheFactory(CacheLoader... aMockCacheLoaders) throws Exception {

      super( new MockTreeCache( aMockCacheLoaders ) );
   }


   /**
    * Circumvents calls to StatementLoader inside the default <code>executeDatabaseQuery</code>
    * method.
    */
   public static class MockConfigCacheLoader extends ConfigCacheLoader {

      /**
       * Creates a new MockConfigCacheLoader object.
       */
      public MockConfigCacheLoader() {
         super( null );
      }


      /**
       * DOCUMENT_ME
       *
       * @param aStatement
       *           DOCUMENT_ME
       * @param aArgs
       *           DOCUMENT_ME
       * @param aMaxNumRows
       *           DOCUMENT_ME
       * @param aSetUserSession
       *           DOCUMENT_ME
       *
       * @return DOCUMENT_ME
       */
      @Override
      protected DataSet executeDatabaseQuery( DatabaseStatement aStatement, DataSetArgument aArgs,
            int aMaxNumRows, boolean aSetUserSession ) {
         return MxDataAccess.getInstance().executeQuery( aStatement, aArgs );
      }
   }

   /**
    * Circumvents calls to StatementLoader inside the default <code>executeDatabaseQuery</code>
    * method.
    *
    * @author gpearson
    */
   public static class MockTimeZoneCacheLoader extends TimeZoneCacheLoader {

      /**
       * Creates a new MockTimeZoneCacheLoader object.
       */
      public MockTimeZoneCacheLoader() {
         super( null );
      }


      /**
       * Non-JNDI version of the parent class's query method.
       *
       * @param aQueryName
       *           a named query.
       * @param aArgs
       *           query arguments.
       *
       * @return a data set with the query's results.
       */
      @Override
      protected DataSet executeDatabaseQuery( String aQueryName, DataSetArgument aArgs ) {
         return MxDataAccess.getInstance().executeQuery( aQueryName, aArgs );
      }
   }

   public static class NoTimeZoneCacheLoader extends TimeZoneCacheLoader {

      /**
       * Creates a new NoTimeZoneCacheLoader object.
       */
      public NoTimeZoneCacheLoader() {
         super( null );
      }


      /**
       * Non-JNDI version of the parent class's query method.
       *
       * @param aQueryName
       *           a named query.
       * @param aArgs
       *           query arguments.
       *
       * @return a data set with the query's results.
       */
      @Override
      protected DataSet executeDatabaseQuery( String aQueryName, DataSetArgument aArgs ) {
         return new DataSet();
      }
   }
}
