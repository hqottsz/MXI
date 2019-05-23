package com.mxi.mx.core.services.part.binlevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.PartCountBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.InvLocBinKey;
import com.mxi.mx.core.key.InvLocPartCountKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.materials.inventorycount.domain.BinLocationLogGroup;
import com.mxi.mx.core.materials.inventorycount.domain.BinLocationLogType;
import com.mxi.mx.repository.location.JdbcBinLocationLogRepository;
import com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class BinLevelServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUser = new OperateAsUserRule( 1000, "currentuser" );

   private BinLevelService binLevelService;

   private final static String ABQ_STORE_BIN = "ABQ/STORE/BIN";
   private LocationKey iBinLoc;
   private PartNoKey iPart;
   private InvLocPartCountKey iInvLocPartCount;
   private InvLocBinKey iInvLocBin;


   @Before
   public void setup() {
      binLevelService = new BinLevelService();
      Domain.createHumanResource( hr -> {
         hr.setUser( new UserKey( 1000 ) );
      } );

      // create bin location
      iBinLoc = Domain.createLocation( location -> {
         location.setCode( ABQ_STORE_BIN );
         location.setType( RefLocTypeKey.BIN );
      } );

      // create part
      iPart = new PartNoBuilder().build();

      // create the bin level for the part
      try {
         iInvLocBin = new BinLevelService().add( iPart, null, iBinLoc, new BinLevelTO() );
      } catch ( MxException e ) {
         e.printStackTrace();
      }

      // create the cycle count record
      iInvLocPartCount = new InvLocPartCountKey( iBinLoc, iPart, 1 );
      iInvLocPartCount = new PartCountBuilder( iInvLocPartCount ).withNextCountDate( new Date() )
            .isHistorical( false ).build();
   }


   @Test
   public void itCreateBinLevel() throws MxException {
      final LocationKey locationKey = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.BIN );
      } );

      final PartNoKey partNoKey = Domain.createPart();
      final BinLevelTO binLevelTO = new BinLevelTO();
      binLevelTO.setMaxQt( 10d );
      binLevelTO.setMinQt( 2d );

      // ACT
      binLevelService.add( partNoKey, locationKey, binLevelTO );

      // ASSERT
      DataSetArgument args = new DataSetArgument();
      args.add( locationKey, ColumnName.LOC_DB_ID.name(), ColumnName.LOC_ID.name() );
      args.add( partNoKey, ColumnName.PART_NO_DB_ID.name(), ColumnName.PART_NO_ID.name() );

      final List<String> columns = new ArrayList<>();
      Stream.of( ColumnName.values() ).forEach( element -> {
         columns.add( element.name() );
      } );
      final QuerySet querySet = QuerySetFactory.getInstance().executeQuery(
            columns.toArray( new String[0] ), JdbcBinLocationLogRepository.TABLE_NAME, args );

      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.BIN_LEVEL_CREATED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.BIN_LEVEL_CREATED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
   }


   @Test
   public void itEditBinLevel() throws MxException {
      final LocationKey locationKey = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.BIN );
      } );

      final PartNoKey partNoKey = Domain.createPart();
      final BinLevelTO binLevelTO = new BinLevelTO();
      binLevelTO.setMaxQt( 10d );
      binLevelTO.setMinQt( 2d );

      binLevelService.add( partNoKey, locationKey, binLevelTO );

      // ACT
      binLevelTO.setMaxQt( 30d );
      binLevelService.set( new InvLocBinKey( locationKey, partNoKey ), binLevelTO );

      // ASSERT
      DataSetArgument args = new DataSetArgument();
      args.add( locationKey, ColumnName.LOC_DB_ID.name(), ColumnName.LOC_ID.name() );
      args.add( partNoKey, ColumnName.PART_NO_DB_ID.name(), ColumnName.PART_NO_ID.name() );

      final List<String> columns = new ArrayList<>();
      Stream.of( ColumnName.values() ).forEach( element -> {
         columns.add( element.name() );
      } );
      final QuerySet querySet = QuerySetFactory.getInstance().executeQuery(
            columns.toArray( new String[0] ), JdbcBinLocationLogRepository.TABLE_NAME, args );

      assertEquals( 2, querySet.getRowCount() );
      Set<String> binLocationLogTypes = new HashSet<String>();
      Set<String> binLocationLogGroups = new HashSet<String>();
      while ( querySet.next() ) {
         binLocationLogTypes.add( querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
         binLocationLogGroups.add( querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      }
      assertTrue( binLocationLogTypes.contains( BinLocationLogType.BIN_LEVEL_CREATED.getCode() ) );
      assertTrue( binLocationLogTypes.contains( BinLocationLogType.BIN_LEVEL_EDITED.getCode() ) );
      assertTrue( binLocationLogGroups.contains( BinLocationLogGroup.BIN_UPDATE.getCode() ) );
   }


   @Test
   public void itRemoveBinLevel() throws MxException {
      final LocationKey locationKey = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.BIN );
      } );

      final PartNoKey partNoKey = Domain.createPart();
      final BinLevelTO binLevelTO = new BinLevelTO();
      binLevelTO.setMaxQt( 10d );
      binLevelTO.setMinQt( 2d );

      binLevelService.add( partNoKey, locationKey, binLevelTO );

      // ACT
      binLevelService.remove( new InvLocBinKey( locationKey, partNoKey ) );

      // ASSERT
      DataSetArgument args = new DataSetArgument();
      args.add( locationKey, ColumnName.LOC_DB_ID.name(), ColumnName.LOC_ID.name() );
      args.add( partNoKey, ColumnName.PART_NO_DB_ID.name(), ColumnName.PART_NO_ID.name() );

      final List<String> columns = new ArrayList<>();
      Stream.of( ColumnName.values() ).forEach( element -> {
         columns.add( element.name() );
      } );
      final QuerySet querySet = QuerySetFactory.getInstance().executeQuery(
            columns.toArray( new String[0] ), JdbcBinLocationLogRepository.TABLE_NAME, args );

      assertEquals( 2, querySet.getRowCount() );
      Set<String> binLocationLogTypes = new HashSet<String>();
      Set<String> binLocationLogGroups = new HashSet<String>();
      while ( querySet.next() ) {
         binLocationLogTypes.add( querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
         binLocationLogGroups.add( querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      }
      assertTrue( binLocationLogTypes.contains( BinLocationLogType.BIN_LEVEL_CREATED.getCode() ) );
      assertTrue( binLocationLogTypes.contains( BinLocationLogType.BIN_LEVEL_REMOVED.getCode() ) );
      assertTrue( binLocationLogGroups.contains( BinLocationLogGroup.BIN_UPDATE.getCode() ) );
   }


   /**
    *
    * Verify that when a bin level is removed, and no inventory exists at this bin for part then
    * logic will mark cycle-count record as historic
    *
    * @throws Exception
    */
   @Test
   public void itRemovesBinLevelAndMarksCycleCountRecordHistoric() throws Exception {

      // remove the bin level
      new BinLevelService().remove( iInvLocBin );

      // assert that bin level is deleted
      assertBinLevelDeleted();

      // assert that cycle count record is marked historic
      assertCycleCountRecordIsHistoric( true );
   }


   /**
    *
    * Verify that when a bin level is removed, and inventory exists at this bin for part then
    * cycle-count record is not marked as historic
    *
    * @throws Exception
    */
   @Test
   public void itRemovesBinLevelButDoesNotMarkCycleCountRecordHistoric() throws Exception {

      // create inventory at the bin location for part
      new InventoryBuilder().withPartNo( iPart ).atLocation( iBinLoc ).build();

      // remove the bin level
      new BinLevelService().remove( iInvLocBin );

      // assert that bin level is deleted
      assertBinLevelDeleted();

      // assert that cycle count record is not marked historic
      assertCycleCountRecordIsHistoric( false );
   }


   /**
    *
    * Verify that the method returns true when inventory exists at this bin for part
    *
    * @throws Exception
    */
   @Test
   public void isInventoryStoredInBinReturnsTrueWhenInventoryExists() throws Exception {

      // create inventory at the bin location for part
      new InventoryBuilder().withPartNo( iPart ).atLocation( iBinLoc ).build();

      // call test method
      assertIsInventoryStoredInBin( true );
   }


   /**
    *
    * Verify that the method returns false when no inventory exists at this bin for part
    *
    * @throws Exception
    */
   @Test
   public void isInventoryStoredInBinReturnsFalseWhenNoInventoryExists() throws Exception {

      // call test method without creating any inventory at bin
      assertIsInventoryStoredInBin( false );
   }


   private void assertBinLevelDeleted() {
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_loc_bin",
            iInvLocBin.getPKWhereArg() );
      assertEquals( "No Bin level should exist", 0, lQs.getRowCount() );
   }


   private void assertCycleCountRecordIsHistoric( Boolean aHistoric ) {
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "inv_loc_part_count",
            iInvLocPartCount.getPKWhereArg() );
      lQs.next();
      assertEquals( "cycle count hist_bool not as expected", aHistoric,
            lQs.getBoolean( "hist_bool" ) );
   }


   private void assertIsInventoryStoredInBin( boolean isInvStored ) {
      // call test method
      boolean isInventoryStored = new BinLevelService().isInventoryStoredInBin( iInvLocBin );

      // assert the expected result
      assertEquals( "unexpected result", isInvStored, isInventoryStored );
   }
}
