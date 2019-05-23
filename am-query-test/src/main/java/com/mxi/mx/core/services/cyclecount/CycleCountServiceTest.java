package com.mxi.mx.core.services.cyclecount;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocBinKey;
import com.mxi.mx.core.key.InvLocInvRecountKey;
import com.mxi.mx.core.key.InvLocPartCountKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.materials.inventorycount.domain.BinLocationLogType;
import com.mxi.mx.core.services.part.binlevel.BinLevelService;
import com.mxi.mx.core.services.part.binlevel.BinLevelTO;
import com.mxi.mx.repository.location.JdbcBinLocationLogRepository;
import com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class CycleCountServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUser = new OperateAsUserRule( 1000, "currentuser" );

   private BinLevelService binLevelService;
   private HumanResourceKey hrKey;
   private LocationKey locationKey;
   private PartNoKey partNoKey;


   @Before
   public void setup() throws MxException {
      binLevelService = new BinLevelService();
      hrKey = Domain.createHumanResource( hr -> {
         hr.setUser( new UserKey( 1000 ) );
      } );

      locationKey = Domain.createLocation( location -> {
         location.setType( RefLocTypeKey.BIN );
      } );

      partNoKey = Domain.createPart();
      final BinLevelTO binLevelTO = new BinLevelTO();
      binLevelTO.setMaxQt( 10d, "" );
      binLevelTO.setMinQt( 2d, "" );
      binLevelService.add( partNoKey, locationKey, binLevelTO );

      this.purgeAuditTable();
   }


   @Test
   public void testCountBin() throws MxException {
      final Integer actualQt = 8;
      final CountBinTO countBinTO = new CountBinTO();
      countBinTO.setCountActualQt( actualQt );
      countBinTO.setLastCountDt( new Date() );
      countBinTO.setLocation( locationKey, "" );
      countBinTO.setPartNo( partNoKey, "" );

      // ACT
      CycleCountService.countBin( countBinTO, hrKey );

      // ASSERT
      final QuerySet querySet = retrieveRecord( locationKey, partNoKey );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_COUNT_RECORDED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_COUNT_RECORDED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      assertEquals( actualQt, querySet.getInteger( ColumnName.COUNT_ACTUAL_QT.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( ColumnName.HR_ID.name() ) );
   }


   @Test
   public void testIgnoreDiscrepencies() throws MxException {
      final Integer actualQt = 8;
      final CountBinTO countBinTO = new CountBinTO();
      countBinTO.setCountActualQt( actualQt );
      countBinTO.setLastCountDt( new Date() );
      countBinTO.setLocation( locationKey, "" );
      countBinTO.setPartNo( partNoKey, "" );
      final InvLocPartCountKey invLocPartCountKey = CycleCountService.countBin( countBinTO, hrKey );

      this.purgeAuditTable();

      // ACT
      CycleCountService.ignoreDiscrepencies( new InvLocPartCountKey[] { invLocPartCountKey } );

      // ASSERT
      final QuerySet querySet = retrieveRecord( locationKey, partNoKey );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_DISCREPENCY_COUNT_IGNORED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_DISCREPENCY_COUNT_IGNORED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( ColumnName.HR_ID.name() ) );
   }


   @Test
   public void testRequestRecount() throws MxException {
      final Integer actualQt = 8;
      final CountBinTO countBinTO = new CountBinTO();
      countBinTO.setCountActualQt( actualQt );
      countBinTO.setLastCountDt( new Date() );
      countBinTO.setLocation( locationKey, "" );
      countBinTO.setPartNo( partNoKey, "" );
      final InvLocPartCountKey invLocPartCountKey = CycleCountService.countBin( countBinTO, hrKey );

      this.purgeAuditTable();

      // ACT
      CycleCountService.requestRecount( new InvLocPartCountKey[] { invLocPartCountKey } );

      // ASSERT
      final QuerySet querySet = retrieveRecord( locationKey, partNoKey );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_RECOUNT_REQUESTED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_RECOUNT_REQUESTED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( ColumnName.HR_ID.name() ) );
   }


   @Test
   public void testSetNextCountDate() throws MxException {
      final Integer actualQt = 8;
      final CountBinTO countBinTO = new CountBinTO();
      countBinTO.setCountActualQt( actualQt );
      countBinTO.setLastCountDt( new Date() );
      countBinTO.setLocation( locationKey, "" );
      countBinTO.setPartNo( partNoKey, "" );
      CycleCountService.countBin( countBinTO, hrKey );

      this.purgeAuditTable();

      // ACT
      CycleCountService.setNextCountDate(
            new InvLocBinKey( locationKey.toString() + ":" + partNoKey.toString() ), new Date() );;

      // ASSERT
      final QuerySet querySet = retrieveRecord( locationKey, partNoKey );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_NEXT_COUNT_DATE_SET.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_NEXT_COUNT_DATE_SET.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( ColumnName.HR_ID.name() ) );
   }


   @Test
   public void testItemRecount() throws Exception {
      final Integer actualQt = 8;
      final CountBinTO countBinTO = new CountBinTO();
      countBinTO.setCountActualQt( actualQt );
      countBinTO.setLastCountDt( new Date() );
      countBinTO.setLocation( locationKey, "" );
      countBinTO.setPartNo( partNoKey, "" );
      final InvLocPartCountKey invLocPartCountKey = CycleCountService.countBin( countBinTO, hrKey );
      CycleCountService.requestRecount( new InvLocPartCountKey[] { invLocPartCountKey } );
      this.purgeAuditTable();

      // ACT
      final InvLocInvRecountKey[] recountKeys = CycleCountService.itemRecount( invLocPartCountKey,
            new InvRecountTO[] { InvRecountTO.getEmptyRecount() }, hrKey );

      // ASSERT
      final QuerySet querySet = retrieveRecord( locationKey, partNoKey );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_RECOUNT_RECORDED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_RECOUNT_RECORDED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( ColumnName.HR_ID.name() ) );
   }


   @Test
   public void testRecountCorrections() throws Exception {
      final Integer actualQt = 8;
      final CountBinTO countBinTO = new CountBinTO();
      countBinTO.setCountActualQt( actualQt );
      countBinTO.setLastCountDt( new Date() );
      countBinTO.setLocation( locationKey, "" );
      countBinTO.setPartNo( partNoKey, "" );
      final InvLocPartCountKey invLocPartCountKey = CycleCountService.countBin( countBinTO, hrKey );
      CycleCountService.requestRecount( new InvLocPartCountKey[] { invLocPartCountKey } );
      final InvLocInvRecountKey[] recountKeys = CycleCountService.itemRecount( invLocPartCountKey,
            new InvRecountTO[] { InvRecountTO.getEmptyRecount() }, hrKey );
      this.purgeAuditTable();

      // ACT
      CycleCountService.recountCorrections( recountKeys, null, "123", hrKey );

      // ASSERT
      final QuerySet querySet = retrieveRecord( locationKey, partNoKey );
      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_RECOUNT_CORRECTED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_RECOUNT_CORRECTED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );
      assertEquals( hrKey.getId(), querySet.getInt( ColumnName.HR_ID.name() ) );
   }


   private QuerySet retrieveRecord( LocationKey locationKey, PartNoKey partNoKey ) {
      DataSetArgument args = new DataSetArgument();
      args.add( locationKey, ColumnName.LOC_DB_ID.name(), ColumnName.LOC_ID.name() );
      args.add( partNoKey, ColumnName.PART_NO_DB_ID.name(), ColumnName.PART_NO_ID.name() );

      final List<String> columns = new ArrayList<>();
      Stream.of( ColumnName.values() ).forEach( element -> {
         columns.add( element.name() );
      } );
      final QuerySet querySet = QuerySetFactory.getInstance().executeQuery(
            columns.toArray( new String[0] ), JdbcBinLocationLogRepository.TABLE_NAME, args );
      return querySet;
   }


   private void purgeAuditTable() {
      MxDataAccess.getInstance().executeDelete( JdbcBinLocationLogRepository.TABLE_NAME, null );
   }
}
