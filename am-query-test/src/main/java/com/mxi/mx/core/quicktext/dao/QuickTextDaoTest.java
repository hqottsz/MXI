package com.mxi.mx.core.quicktext.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.quicktext.service.QuickTextTypeTO;


public class QuickTextDaoTest {

   private static final QuickTextTypeTO TYPE_1 =
         new QuickTextTypeTO( "TEST_TYPE_1", "Test Type 1" );
   private static final QuickTextTypeTO TYPE_2 =
         new QuickTextTypeTO( "TEST_TYPE_2", "Test Type 2" );

   private QuickTextDao quickTextDao;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      quickTextDao = InjectorContainer.get().getInstance( QuickTextDao.class );
   }


   @Test
   public void findAllTypes() {
      List<QuickTextTypeTO> types = quickTextDao.findAllTypes();

      assertEquals( 2, types.size() );
      assertTrue( types.contains( TYPE_1 ) );
      assertTrue( types.contains( TYPE_2 ) );
   }

}
