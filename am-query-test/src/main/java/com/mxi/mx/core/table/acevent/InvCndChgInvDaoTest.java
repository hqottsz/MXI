package com.mxi.mx.core.table.acevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.InvCndChgInvKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class InvCndChgInvDaoTest {

   private static final InvCndChgInvKey NEW_EVENT_INV_KEY = new InvCndChgInvKey( "4650:1:2" );
   private static final InvCndChgInvKey EVENT_INV_KEY = new InvCndChgInvKey( "4650:1:1" );
   private static final InvCndChgEventKey EVENT_KEY = new InvCndChgEventKey( "4650:1" );
   private static final InvCndChgEventKey NEW_EVENT_KEY = new InvCndChgEventKey( "4650:2" );
   private static final InventoryKey INVENTORY_KEY = new InventoryKey( "4650:56530" );
   private static final InventoryKey NEW_INVENTORY_KEY = new InventoryKey( "4650:56531" );
   private static final String ASSMBLY_CD = "F-2000";
   private static final String NEW_ASSMBLY_CD = "F-3000";
   private static final ConfigSlotKey BOM_KEY = new ConfigSlotKey( "4318:F-2000:52" );
   private static final ConfigSlotKey NEW_BOM_KEY = new ConfigSlotKey( "4319:F-3000:53" );

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
            }
         } );

   private InvCndChgInvDao iInvCndChgInvDao;


   @Before
   public void setup() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iInvCndChgInvDao = InjectorContainer.get().getInstance( InvCndChgInvDao.class );
   }


   @Test
   public void createDefault() {

      InvCndChgInvTable lInvCndChgInvTable = iInvCndChgInvDao.create();

      assertNotNull( lInvCndChgInvTable );
      assertNull( lInvCndChgInvTable.getPk() );
   }


   @Test
   public void createWithValidKey() {

      InvCndChgInvTable lInvCndChgInvTable = iInvCndChgInvDao.create( NEW_EVENT_INV_KEY );

      assertEquals( NEW_EVENT_INV_KEY, lInvCndChgInvTable.getPk() );
   }


   @Test
   public void findByPrimaryKeyNull() {
      InvCndChgInvTable lRow = iInvCndChgInvDao.findByPrimaryKey( null );

      assertNull( lRow.getPk() );
   }


   @Test
   public void findByPrimaryKeyValid() {

      InvCndChgInvTable lRow = iInvCndChgInvDao.findByPrimaryKey( EVENT_INV_KEY );
      assertNotNull( lRow );
      assertEquals( INVENTORY_KEY, lRow.getInventory() );
      assertEquals( ASSMBLY_CD, lRow.getBomItem().getCd() );
      assertTrue( lRow.getMainInvBool() );
   }


   @Test( expected = NullPointerException.class )
   public void insertNull() {
      iInvCndChgInvDao.insert( null );
   }


   @Test
   public void insertDoesntGenerateKey() {
      InvCndChgInvTable lRow = new InvCndChgInvTable( NEW_EVENT_INV_KEY );
      {

         lRow.setBomItem( NEW_BOM_KEY );
      }

      InvCndChgInvKey lInvCndChgInvKey = iInvCndChgInvDao.insert( lRow );
      assertEquals( NEW_EVENT_INV_KEY, lInvCndChgInvKey );
      assertTrue( hasRecord( NEW_EVENT_INV_KEY ) );
   }


   @Test
   public void insertGeneratesKey() {
      InvCndChgInvTable lRow = new InvCndChgInvTable( null );
      {
         lRow.setEventKey( NEW_EVENT_KEY );
         lRow.setBomItem( NEW_BOM_KEY );
      }

      InvCndChgInvKey lInvCndChgInvKey = iInvCndChgInvDao.insert( lRow );
      assertTrue( hasRecord( lInvCndChgInvKey ) );

   }


   @Test( expected = NullPointerException.class )
   public void updateNull() {
      iInvCndChgInvDao.update( null );
   }


   @Test
   public void updateWithValidKey() {
      InvCndChgInvTable lRow = new InvCndChgInvTable( EVENT_INV_KEY );
      {
         lRow.setEventKey( NEW_EVENT_KEY );
         lRow.setBomItem( NEW_BOM_KEY );
         lRow.setInventory( NEW_INVENTORY_KEY );

      }

      iInvCndChgInvDao.update( lRow );
      InvCndChgInvTable lRetrievedRow = iInvCndChgInvDao.findByPrimaryKey( EVENT_INV_KEY );
      assertEquals( NEW_BOM_KEY, lRetrievedRow.getBomItem() );
      assertEquals( NEW_INVENTORY_KEY, lRetrievedRow.getInventory() );
   }


   @Test( expected = NullPointerException.class )
   public void refreshNull() {
      iInvCndChgInvDao.refresh( null );
   }


   @Test
   public void refreshWithValidKey() {
      InvCndChgInvTable lRow = new InvCndChgInvTable( EVENT_INV_KEY );

      assertEquals( BOM_KEY, lRow.getBomItem() );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( ColumnName.ASSMBL_CD.name(), NEW_ASSMBLY_CD );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lRow.getPk(), ColumnName.EVENT_DB_ID.name(), ColumnName.EVENT_ID.name(),
            ColumnName.EVENT_INV_ID.name() );

      MxDataAccess.getInstance().executeUpdate( InvCndChgInvTable.TABLE_NAME, lArgs, lWhereArgs );

      iInvCndChgInvDao.refresh( lRow );

      assertEquals( NEW_ASSMBLY_CD, lRow.getBomItem().getCd() );

   }


   @Test( expected = NullPointerException.class )
   public void deleteNull() {
      iInvCndChgInvDao.delete( null );
   }


   @Test
   public void deleteWithValidKey() {
      InvCndChgInvTable lRow = new InvCndChgInvTable( EVENT_INV_KEY );

      assertTrue( iInvCndChgInvDao.delete( lRow ) );
      assertFalse( hasRecord( EVENT_INV_KEY ) );
   }


   @Test
   public void deleteWithInvalidKey() {
      InvCndChgInvTable lRow = new InvCndChgInvTable( NEW_EVENT_INV_KEY );

      assertFalse( iInvCndChgInvDao.delete( lRow ) );
   }


   @Test
   public void generatePrimaryKeyUnique() {
      InvCndChgInvKey lKey1 = iInvCndChgInvDao.generatePrimaryKey( EVENT_KEY );
      InvCndChgInvKey lKey2 = iInvCndChgInvDao.generatePrimaryKey( NEW_EVENT_KEY );

      assertFalse( lKey1.getId() == lKey2.getId() );
   }


   private boolean hasRecord( InvCndChgInvKey aKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "event_db_id", "event_id", "event_inv_id" );

      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( InvCndChgInvTable.TABLE_NAME, lArgs );

      if ( lQs.next() ) {
         return true;
      }

      return false;
   }

}
