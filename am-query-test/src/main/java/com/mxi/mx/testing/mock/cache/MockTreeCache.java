package com.mxi.mx.testing.mock.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.transaction.TransactionManager;

import org.jboss.cache.CacheException;
import org.jboss.cache.DataNode;
import org.jboss.cache.Fqn;
import org.jboss.cache.GlobalTransaction;
import org.jboss.cache.Node;
import org.jboss.cache.RegionNotEmptyException;
import org.jboss.cache.TransactionManagerLookup;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.cache.config.Option;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.cache.marshall.RegionNameConflictException;
import org.jboss.cache.marshall.RegionNotFoundException;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelNotConnectedException;
import org.w3c.dom.Element;

import com.mxi.mx.common.exception.MxRuntimeException;


/**
 * Mock tree cache. <b>Absolute minimum functionality</b>; essentially a bunch of no-ops to avoid
 * JNDI calls during {@linkplain CacheFactory}'s creation.
 *
 * @author gpearson
 */
public class MockTreeCache implements TreeCacheMBean {

   private List<CacheLoader> iCacheLoaders = new ArrayList<CacheLoader>();


   /**
    * Creates a new {@linkplain MockTreeCache} object.
    *
    * @param aCacheLoader
    *           the cache loader that will be called for any requests.
    */
   public MockTreeCache(CacheLoader aCacheLoader) {
      this( new CacheLoader[] { aCacheLoader } );
   }


   /**
    * Creates a new {@linkplain MockTreeCache} object.
    *
    * @param aCacheLoaders
    *           multiple class loaders. The cache will chain requests to class loaders in the order
    *           specified by <tt>aCacheLoaders</tt>.
    */
   public MockTreeCache(CacheLoader... aCacheLoaders) {

      iCacheLoaders.addAll( Arrays.asList( aCacheLoaders ) );
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused parameter.
    * @param aArg1
    *           Unused parameter.
    * @param aArg2
    *           Unused parameter.
    * @param aArg3
    *           Unused parameter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void _put( GlobalTransaction aArg0, Fqn aArg1, Map aArg2, boolean aArg3 )
         throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused parameter.
    * @param aArg1
    *           Unused parameter.
    * @param aArg2
    *           Unused parameter.
    * @param aArg3
    *           Unused parameter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void _put( GlobalTransaction aArg0, String aArg1, Map aArg2, boolean aArg3 )
         throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused parameter.
    * @param aArg1
    *           Unused parameter.
    * @param aArg2
    *           Unused parameter.
    * @param aArg3
    *           Unused parameter.
    * @param aArg4
    *           Unused parameter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void _put( GlobalTransaction aArg0, Fqn aArg1, Map aArg2, boolean aArg3, boolean aArg4 )
         throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused parameter.
    * @param aArg1
    *           Unused parameter.
    * @param aArg2
    *           Unused parameter.
    * @param aArg3
    *           Unused parameter.
    * @param aArg4
    *           Unused parameter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object _put( GlobalTransaction aArg0, Fqn aArg1, Object aArg2, Object aArg3,
         boolean aArg4 ) throws CacheException {
      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused parameter.
    * @param aArg1
    *           Unused parameter.
    * @param aArg2
    *           Unused parameter.
    * @param aArg3
    *           Unused parameter.
    * @param aArg4
    *           Unused parameter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs
    */
   @Override
   public Object _put( GlobalTransaction aArg0, String aArg1, Object aArg2, Object aArg3,
         boolean aArg4 ) throws CacheException {
      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused parameter.
    * @param aArg1
    *           Unused parameter.
    * @param aArg2
    *           Unused parameter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void _remove( GlobalTransaction aArg0, Fqn aArg1, boolean aArg2 ) throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void _remove( GlobalTransaction aArg0, String aArg1, boolean aArg2 )
         throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    * @param aArg3
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object _remove( GlobalTransaction aArg0, Fqn aArg1, Object aArg2, boolean aArg3 )
         throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    * @param aArg3
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object _remove( GlobalTransaction aArg0, String aArg1, Object aArg2, boolean aArg3 )
         throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws RegionNotEmptyException
    *            If an error occurs.
    * @throws RegionNameConflictException
    *            If an error occurs.
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void activateRegion( String aArg0 )
         throws RegionNotEmptyException, RegionNameConflictException, CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    */
   @Override
   public void addTreeCacheListener( TreeCacheListener aArg0 ) {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    * @param aArg3
    *           Unused paramter.
    * @param aArg4
    *           Unused paramter.
    * @param aArg5
    *           Unused paramter.
    *
    * @return null
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public List callRemoteMethods( Vector aArg0, Method aArg1, Object[] aArg2, boolean aArg3,
         boolean aArg4, long aArg5 ) throws Exception {

      // No-op.
      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    * @param aArg3
    *           Unused paramter.
    * @param aArg4
    *           Unused paramter.
    * @param aArg5
    *           Unused paramter.
    * @param aArg6
    *           Unused paramter.
    *
    * @return null
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public List callRemoteMethods( Vector aArg0, String aArg1, Class[] aArg2, Object[] aArg3,
         boolean aArg4, boolean aArg5, long aArg6 ) throws Exception {

      // No-op.
      return null;
   }


   /**
    * No-op.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void create() throws Exception {
      // No-op.
   }


   /**
    * No-op.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void createService() throws Exception {
      // No-op.
   }


   /**
    * No-op.
    */
   @Override
   public void destroy() {
      // No-op.
   }


   /**
    * No-op.
    */
   @Override
   public void destroyService() {
      // No-op.
   }


   /**
    * No-op.
    *
    * @return null
    */
   @Override
   public String dumpTransactionTable() {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void evict( Fqn aArg0 ) throws CacheException {
      // No-op.
   }


   /**
    * Checks for existence of the named data in the cache.
    *
    * @param aArg0
    *           a cache identifier.
    *
    * @return true if the data exists.
    */
   @Override
   public boolean exists( Fqn aArg0 ) {
      boolean lResult = false;
      try {
         for ( CacheLoader lCache : iCacheLoaders ) {
            if ( lCache.exists( aArg0 ) ) {
               lResult = true;

               break;
            }
         }
      } catch ( Exception lEx ) {
         throw new RuntimeException( lEx );
      }

      return lResult;
   }


   /**
    * Checks for existence of the named data in the cache.
    *
    * @param aArg0
    *           a cache identifier.
    *
    * @return true if the data exists.
    */
   @Override
   public boolean exists( String aArg0 ) {
      return exists( new Fqn( aArg0 ) );
   }


   /**
    * Checks for existence of the named data in the cache.
    *
    * @param aArg0
    *           a cache identifier.
    * @param aKey
    *           key of an item in the cache.
    *
    * @return true if the cache contains data for the key.
    */
   @Override
   public boolean exists( Fqn aArg0, Object aKey ) {
      Node lNode;
      try {
         lNode = get( aArg0 );
      } catch ( Exception lEx ) {
         throw new RuntimeException( lEx );
      }

      return ( lNode != null ) && ( lNode.getData() != null )
            && lNode.getData().containsKey( aKey );
   }


   /**
    * Checks for existence of the named data in the cache.
    *
    * @param aArg0
    *           a cache identifier.
    * @param aKey
    *           key of an item in the cache.
    *
    * @return true if the cache contains data for the key.
    */
   @Override
   public boolean exists( String aArg0, Object aKey ) {
      return exists( new Fqn( aArg0 ), aKey );
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws ChannelClosedException
    *            If an error occurs.
    * @throws ChannelNotConnectedException
    *            If an error occurs.
    */
   @Override
   public void fetchState( long aArg0 )
         throws ChannelClosedException, ChannelNotConnectedException {
      // No-op.
   }


   /**
    * Returns the cache node for the specified cache identifier.
    *
    * @param aFqn
    *           cache identifier.
    *
    * @return the cached node if it exists, or null.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Node get( Fqn aFqn ) throws CacheException {
      Node lNode = null;
      try {
         for ( CacheLoader lCache : iCacheLoaders ) {

            Map lMap = lCache.get( aFqn );
            if ( lMap != null ) {
               lNode = new Node();
               lNode.put( lMap );

               break;
            }
         }
      } catch ( Exception e ) {
         throw new MxRuntimeException( e );
      }

      return lNode;
   }


   /**
    * Returns the cache node for the specified cache identifier.
    *
    * @param aFqn
    *           cache identifier.
    *
    * @return the cached node if it exists, or null.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Node get( String aFqn ) throws CacheException {

      return get( new Fqn( aFqn ) );
   }


   /**
    * Returns the cache data for the specified cache identifier and key.
    *
    * @param aFqn
    *           cache identifier.
    * @param aKey
    *           key for the found cache node.
    *
    * @return the cached data if it exists, or null.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object get( Fqn aFqn, Object aKey ) throws CacheException {
      if ( !exists( aFqn ) ) {
         return null;
      }

      return get( aFqn ).getData().get( aKey );
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public DataNode get( Fqn aArg0, Option aArg1 ) throws CacheException {

      throw new UnsupportedOperationException( "get with option not supported" );
   }


   /**
    * Returns the cache data for the specified cache identifier and key.
    *
    * @param aFqn
    *           cache identifier.
    * @param aKey
    *           key for the found cache node.
    *
    * @return the cached data if it exists, or null.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object get( String aFqn, Object aKey ) throws CacheException {

      return get( new Fqn( aFqn ), aKey );
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object get( Fqn aArg0, Object aArg1, Option aArg2 ) throws CacheException {
      throw new UnsupportedOperationException( "get with option not supported" );
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    * @param aArg3
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object get( Fqn aArg0, Object aArg1, boolean aArg2, Option aArg3 ) throws CacheException {
      throw new UnsupportedOperationException( "get with option not supported" );
   }


   @Override
   public Element getBuddyReplicationConfig() {

      return null;
   }


   @Override
   public CacheLoader getCacheLoader() {

      return null;
   }


   @Override
   public boolean getCacheLoaderAsynchronous() {

      return false;
   }


   @Override
   public String getCacheLoaderClass() {

      return null;
   }


   @Override
   public Properties getCacheLoaderConfig() {

      return null;
   }


   @Override
   public Element getCacheLoaderConfiguration() {

      return null;
   }


   @Override
   public boolean getCacheLoaderFetchPersistentState() {

      return false;
   }


   @Override
   public boolean getCacheLoaderFetchTransientState() {

      return false;
   }


   @Override
   public boolean getCacheLoaderPassivation() {

      return false;
   }


   @Override
   public String getCacheLoaderPreload() {

      return null;
   }


   @Override
   public boolean getCacheLoaderShared() {

      return false;
   }


   @Override
   public String getCacheMode() {

      return null;
   }


   @Override
   public Set getChildrenNames( Fqn aArg0 ) throws CacheException {

      return null;
   }


   @Override
   public Set getChildrenNames( String aArg0 ) throws CacheException {

      return null;
   }


   @Override
   public Set getChildrenNames( Fqn aArg0, Option aArg1 ) throws CacheException {

      return null;
   }


   @Override
   public String getClusterName() {

      return null;
   }


   @Override
   public String getClusterProperties() {

      return null;
   }


   @Override
   public boolean getDeadlockDetection() {

      return false;
   }


   @Override
   public String getEvictionPolicyClass() {

      return null;
   }


   @Override
   public int getEvictionThreadWakeupIntervalSeconds() {

      return 0;
   }


   @Override
   public boolean getFetchInMemoryState() {

      return false;
   }


   @Override
   public boolean getFetchStateOnStartup() {

      return false;
   }


   @Override
   public long getInitialStateRetrievalTimeout() {

      return 0;
   }


   @Override
   public TreeCache getInstance() {

      return null;
   }


   @Override
   public String getInterceptorChain() {

      return null;
   }


   @Override
   public List getInterceptors() {

      return null;
   }


   @Override
   public String getIsolationLevel() {

      return null;
   }


   @Override
   public Set getKeys( Fqn aArg0 ) throws CacheException {

      return null;
   }


   @Override
   public Set getKeys( String aArg0 ) throws CacheException {

      return null;
   }


   @Override
   public Object getLocalAddress() {

      return null;
   }


   @Override
   public long getLockAcquisitionTimeout() {

      return 0;
   }


   @Override
   public Vector getMembers() {

      return null;
   }


   @Override
   public String getMultiplexerService() {

      return null;
   }


   @Override
   public String getMultiplexerStack() {

      return null;
   }


   @Override
   public String getName() {

      return null;
   }


   @Override
   public String getNodeLockingScheme() {

      return null;
   }


   @Override
   public int getNumberOfAttributes() {

      return 0;
   }


   @Override
   public int getNumberOfLocksHeld() {

      return 0;
   }


   @Override
   public int getNumberOfNodes() {

      return 0;
   }


   @Override
   public Element getPojoCacheConfig() {

      return null;
   }


   @Override
   public String getReplicationVersion() {

      return null;
   }


   @Override
   public long getReplQueueInterval() {

      return 0;
   }


   @Override
   public int getReplQueueMaxElements() {

      return 0;
   }


   @Override
   public int getState() {

      return 0;
   }


   @Override
   public String getStateString() {

      return null;
   }


   @Override
   public short getStateTransferVersion() {

      return 0;
   }


   @Override
   public boolean getSyncCommitPhase() {

      return false;
   }


   @Override
   public long getSyncReplTimeout() {

      return 0;
   }


   @Override
   public boolean getSyncRollbackPhase() {

      return false;
   }


   @Override
   public TransactionManager getTransactionManager() {

      return null;
   }


   @Override
   public String getTransactionManagerLookupClass() {

      return null;
   }


   @Override
   public boolean getUseInterceptorMbeans() {

      return false;
   }


   @Override
   public boolean getUseMarshalling() {

      return false;
   }


   @Override
   public boolean getUseRegionBasedMarshalling() {

      return false;
   }


   @Override
   public boolean getUseReplQueue() {

      return false;
   }


   @Override
   public String getVersion() {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws RegionNameConflictException
    *            If an error occurs.
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void inactivateRegion( String aArg0 ) throws RegionNameConflictException, CacheException {
      // No-op.
   }


   @Override
   public boolean isCoordinator() {

      return false;
   }


   @Override
   public boolean isInactiveOnStartup() {

      return false;
   }


   @Override
   public boolean isUsingEviction() {

      return false;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void jbossInternalLifecycle( String aArg0 ) throws Exception {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void load( String aArg0 ) throws Exception {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @return null
    */
   @Override
   public String print( Fqn aArg0 ) {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @return null
    */
   @Override
   public String print( String aArg0 ) {

      return null;
   }


   /**
    * No-op.
    *
    * @return null
    */
   @Override
   public String printDetails() {

      return null;
   }


   /**
    * No-op.
    *
    * @return null
    */
   @Override
   public String printLockInfo() {

      return null;
   }


   /**
    * No-op.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void purgeCacheLoaders() throws Exception {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void put( Fqn aArg0, Map aArg1 ) throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void put( String aArg0, Map aArg1 ) throws CacheException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void put( Fqn aArg0, Map aArg1, Option aArg2 ) throws CacheException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object put( Fqn aArg0, Object aArg1, Object aArg2 ) throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object put( String aArg0, Object aArg1, Object aArg2 ) throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    * @param aArg3
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void put( Fqn aArg0, Object aArg1, Object aArg2, Option aArg3 ) throws CacheException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @throws RegionNameConflictException
    *            If an error occurs.
    */
   @Override
   public void registerClassLoader( String aArg0, ClassLoader aArg1 )
         throws RegionNameConflictException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    */
   @Override
   public void releaseAllLocks( Fqn aArg0 ) {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    */
   @Override
   public void releaseAllLocks( String aArg0 ) {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void remove( Fqn aArg0 ) throws CacheException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void remove( String aArg0 ) throws CacheException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object remove( Fqn aArg0, Object aArg1 ) throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void remove( Fqn aArg0, Option aArg1 ) throws CacheException {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object remove( String aArg0, Object aArg1 ) throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    * @param aArg1
    *           Unused paramter.
    * @param aArg2
    *           Unused paramter.
    *
    * @return null
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public Object remove( Fqn aArg0, Object aArg1, Option aArg2 ) throws CacheException {

      return null;
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void removeData( Fqn aArg0 ) throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws CacheException
    *            If an error occurs.
    */
   @Override
   public void removeData( String aArg0 ) throws CacheException {
      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    */
   @Override
   public void removeTreeCacheListener( TreeCacheListener aArg0 ) {

      // No-op.
   }


   @Override
   public void setBuddyReplicationConfig( Element aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoader( CacheLoader aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderAsynchronous( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderClass( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderConfig( Properties aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderConfiguration( Element aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderFetchPersistentState( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderFetchTransientState( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderPassivation( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderPreload( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheLoaderShared( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setCacheMode( String aArg0 ) throws Exception {
      // No-op.
   }


   @Override
   public void setClusterConfig( Element aArg0 ) {

      // No-op.
   }


   @Override
   public void setClusterName( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setClusterProperties( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setDeadlockDetection( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setEvictionPolicyClass( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setEvictionPolicyConfig( Element aArg0 ) {

      // No-op.
   }


   @Override
   public void setFetchInMemoryState( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setFetchStateOnStartup( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setInactiveOnStartup( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setInitialStateRetrievalTimeout( long aArg0 ) {

      // No-op.
   }


   @Override
   public void setIsolationLevel( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setLockAcquisitionTimeout( long aArg0 ) {

      // No-op.
   }


   @Override
   public void setMultiplexerService( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setMultiplexerStack( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setNodeLockingScheme( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setPojoCacheConfig( Element aArg0 ) throws CacheException {
      // No-op.
   }


   @Override
   public void setReplicationVersion( String aArg0 ) {

      // No-op.
   }


   @Override
   public void setReplQueueInterval( long aArg0 ) {

      // No-op.
   }


   @Override
   public void setReplQueueMaxElements( int aArg0 ) {

      // No-op.
   }


   @Override
   public void setStateTransferVersion( short aArg0 ) {

      // No-op.
   }


   @Override
   public void setSyncCommitPhase( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setSyncReplTimeout( long aArg0 ) {

      // No-op.
   }


   @Override
   public void setSyncRollbackPhase( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setTransactionManagerLookup( TransactionManagerLookup aArg0 ) {

      // No-op.
   }


   @Override
   public void setTransactionManagerLookupClass( String aArg0 ) throws Exception {
      // No-op.
   }


   @Override
   public void setUseMarshalling( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setUseRegionBasedMarshalling( boolean aArg0 ) {

      // No-op.
   }


   @Override
   public void setUseReplQueue( boolean aArg0 ) {

      // No-op.
   }


   /**
    * No-op.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void start() throws Exception {
      // No-op.
   }


   /**
    * No-op.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Override
   public void startService() throws Exception {
      // No-op.
   }


   /**
    * No-op.
    */
   @Override
   public void stop() {

      // No-op.
   }


   /**
    * No-op.
    */
   @Override
   public void stopService() {

      // No-op.
   }


   /**
    * No-op.
    *
    * @param aArg0
    *           Unused paramter.
    *
    * @throws RegionNotFoundException
    *            If an error occurs.
    */
   @Override
   public void unregisterClassLoader( String aArg0 ) throws RegionNotFoundException {
      // No-op.
   }
}
