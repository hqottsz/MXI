package com.mxi.am.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FaultKey;


/**
 * DOCUMENT_ME
 *
 */
public class LegacyKeyUtilTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();

   private FaultKey faultKey = new FaultKey( 1, 4651 );


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), LegacyKeyUtilTest.class );
   }


   @Test
   public void getLegacyKeyByAltId() {

      FaultKey key =
            legacyKeyUtil.getLegacyKeyByAltId( "59720662DDEF479DAA9038BB9B10B879", FaultKey.class );

      assertNotNull( key );
      assertEquals( key, faultKey );
   }


   @Test( expected = IllegalStateException.class )
   public void legacyKey_doesNotExist() {
      legacyKeyUtil.getLegacyKeyByAltId( "59720662DDEF479DAA9038BB9B10B811", FaultKey.class );
   }

}
