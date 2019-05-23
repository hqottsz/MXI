package com.mxi.mx.core.services.bom;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EqpAssmblBomLogKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefSensitivityKey;
import com.mxi.mx.core.services.bom.sensitivity.SystemSensitivityLogService;
import com.mxi.mx.core.services.bom.sensitivity.impl.SystemSensitivityLogServiceImpl;
import com.mxi.mx.core.services.sensitivity.model.SensitivityConfigurationTO;
import com.mxi.mx.core.table.eqp.EqpAssmblBomLogTable;


@RunWith( BlockJUnit4ClassRunner.class )
public class SystemSensitivityLogServiceImplTest {

   private static final String CAT_III = "CAT III";
   private static final String ETOPS = "ETOPS";

   private static final boolean CAT_III_DISABLED = false;
   private static final boolean CAT_III_ENABLED = true;
   private static final boolean ETOPS_DISABLED = false;
   private static final boolean ETOPS_ENABLED = true;

   // Configuration testing system (single history note)
   private static final ConfigSlotKey SYSTEM_CONFIGURE = new ConfigSlotKey( "4650:CONFIG:0" );

   // Propagation testing system (batch history notes)
   private static final String SYSTEM_PARENT_ROOT_NAME = "PROP-0 (Propagation)";
   private static final ConfigSlotKey SYSTEM_PARENT_ROOT = new ConfigSlotKey( 4650, "PROP", 0 );
   private static final ConfigSlotKey SYSTEM_CHILD_SYS = new ConfigSlotKey( 4650, "PROP", 1 );
   private static final ConfigSlotKey SYSTEM_GRANDCHILD_SYS = new ConfigSlotKey( 4650, "PROP", 2 );
   private static final ConfigSlotKey SYSTEM_CHILD_SUBASSY = new ConfigSlotKey( 4650, "PROP", 3 );

   private static final List<ConfigSlotKey> SYSTEM_CHILDREN = new ArrayList<>();
   {
      SYSTEM_CHILDREN.add( SYSTEM_CHILD_SYS );
      SYSTEM_CHILDREN.add( SYSTEM_GRANDCHILD_SYS );
      SYSTEM_CHILDREN.add( SYSTEM_CHILD_SUBASSY );
   }

   private SystemSensitivityLogService iService;

   @Rule
   public DatabaseConnectionRule iConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void loadData() {
      DataLoaders.load( iConnectionRule.getConnection(),
            SystemSensitivityLogServiceImplTest.class );
      iService = new SystemSensitivityLogServiceImpl();

   }


   @Test( expected = MandatoryArgumentException.class )
   public void addHistoryNote_bomItem_null() throws Exception {
      iService.addHistoryNote( null, HumanResourceKey.ADMIN,
            new ArrayList<SensitivityConfigurationTO>() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void addHistoryNote_humanResource_null() throws Exception {
      iService.addHistoryNote( SYSTEM_CONFIGURE, null,
            new ArrayList<SensitivityConfigurationTO>() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void addHistoryNote_sensitivityConfig_null() throws Exception {
      iService.addHistoryNote( SYSTEM_CONFIGURE, HumanResourceKey.ADMIN, null );
   }


   @Test
   public void addHistoryNote_enabledSystems() throws Exception {

      EqpAssmblBomLogKey lLogKey = iService.addHistoryNote( SYSTEM_CONFIGURE,
            HumanResourceKey.ADMIN, buildSystemSettings( CAT_III_ENABLED, ETOPS_ENABLED ) );

      EqpAssmblBomLogTable lHistoryNote = getLog( lLogKey );

      assertEquals( getExpectedConfigureHistoryNote( CAT_III, ETOPS ),
            lHistoryNote.getSystemNote() );
      assertEquals( SYSTEM_CONFIGURE, lHistoryNote.getBomItemKey() );
   }


   @Test
   public void addHistoryNote_disabledSystems() throws Exception {

      EqpAssmblBomLogKey lLogKey = iService.addHistoryNote( SYSTEM_CONFIGURE,
            HumanResourceKey.ADMIN, buildSystemSettings( CAT_III_DISABLED, ETOPS_DISABLED ) );

      EqpAssmblBomLogTable lHistoryNote = getLog( lLogKey );

      assertEquals( getExpectedConfigureHistoryNote(), lHistoryNote.getSystemNote() );
      assertEquals( SYSTEM_CONFIGURE, lHistoryNote.getBomItemKey() );
   }


   @Test
   public void addHistoryNote_enabledAndDisabled() throws Exception {

      EqpAssmblBomLogKey lLogKey = iService.addHistoryNote( SYSTEM_CONFIGURE,
            HumanResourceKey.ADMIN, buildSystemSettings( CAT_III_DISABLED, ETOPS_ENABLED ) );

      EqpAssmblBomLogTable lHistoryNote = getLog( lLogKey );

      assertEquals( getExpectedConfigureHistoryNote( ETOPS ), lHistoryNote.getSystemNote() );
      assertEquals( SYSTEM_CONFIGURE, lHistoryNote.getBomItemKey() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void propagateHistoryNotes_bomItem_null() throws Exception {
      iService.propagateHistoryNotes( null, new ArrayList<ConfigSlotKey>(), HumanResourceKey.ADMIN,
            new ArrayList<SensitivityConfigurationTO>() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void propagateHistoryNotes_humanResource_null() throws Exception {
      iService.propagateHistoryNotes( SYSTEM_CONFIGURE, new ArrayList<ConfigSlotKey>(), null,
            new ArrayList<SensitivityConfigurationTO>() );
   }


   @Test( expected = MandatoryArgumentException.class )
   public void propagateHistoryNotes_sensitivityConfig_null() throws Exception {
      iService.propagateHistoryNotes( SYSTEM_CONFIGURE, new ArrayList<ConfigSlotKey>(),
            HumanResourceKey.ADMIN, null );
   }


   @Test
   public void propagateHistoryNotes_childBomItems_allowsNull() throws Exception {
      iService.propagateHistoryNotes( SYSTEM_CONFIGURE, null, HumanResourceKey.ADMIN,
            new ArrayList<SensitivityConfigurationTO>() );
   }


   @Test
   public void propagateHistoryNotes_enabledSystems() throws Exception {

      iService.propagateHistoryNotes( SYSTEM_PARENT_ROOT, SYSTEM_CHILDREN, HumanResourceKey.ADMIN,
            buildSystemSettings( CAT_III_ENABLED, ETOPS_ENABLED ) );

      String lExpectedHistoryNote =
            getExpectedPropagationHistoryNote( SYSTEM_PARENT_ROOT_NAME, CAT_III, ETOPS );

      for ( ConfigSlotKey lChildKey : SYSTEM_CHILDREN ) {
         assertEquals( lExpectedHistoryNote, getHistoryNote( lChildKey ) );
      }
   }


   @Test
   public void propagateHistoryNotes_disabledSystems() throws Exception {

      iService.propagateHistoryNotes( SYSTEM_PARENT_ROOT, SYSTEM_CHILDREN, HumanResourceKey.ADMIN,
            buildSystemSettings( CAT_III_DISABLED, ETOPS_DISABLED ) );

      String lExpectedHistoryNote = getExpectedPropagationHistoryNote( SYSTEM_PARENT_ROOT_NAME );

      for ( ConfigSlotKey lChildKey : SYSTEM_CHILDREN ) {
         assertEquals( lExpectedHistoryNote, getHistoryNote( lChildKey ) );
      }
   }


   @Test
   public void propagateHistoryNotes_enabledAndDisabled() throws Exception {

      iService.propagateHistoryNotes( SYSTEM_PARENT_ROOT, SYSTEM_CHILDREN, HumanResourceKey.ADMIN,
            buildSystemSettings( CAT_III_ENABLED, ETOPS_DISABLED ) );

      String lExpectedHistoryNote =
            getExpectedPropagationHistoryNote( SYSTEM_PARENT_ROOT_NAME, CAT_III );

      for ( ConfigSlotKey lChildKey : SYSTEM_CHILDREN ) {
         assertEquals( lExpectedHistoryNote, getHistoryNote( lChildKey ) );
      }
   }


   /**
    * This method queries the history notes table and determines if the history notes corresponding
    * to sensitivities that has been found.
    *
    * @param aDetailsTO
    *           BomItemDetails Transfer Object
    * @param aMessage
    *           The audit message for history notes
    * @return Returns a boolean whether the history note has been found in the list of history notes
    *         for the config slot
    * @throws Exception
    *            Throws a mandatory argument exception if a mandatory argument is missing
    */
   private String getHistoryNote( ConfigSlotKey aConfigSlotKey ) throws Exception {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aConfigSlotKey, "assmbl_db_id", "assmbl_cd", "assmbl_bom_id" );

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "eqp_assmbl_bom_log", lArgs );

      return lQs.first() ? lQs.getString( "system_note" ) : null;
   }


   private EqpAssmblBomLogTable getLog( EqpAssmblBomLogKey aKey ) {
      return EqpAssmblBomLogTable.findByPrimaryKey( aKey );
   }


   private List<SensitivityConfigurationTO> buildSystemSettings( boolean aIsCATIII,
         boolean aIsETOPS ) throws Exception {

      List<SensitivityConfigurationTO> lSensitivities = new ArrayList<>();
      {
         lSensitivities
               .add( new SensitivityConfigurationTO( RefSensitivityKey.CAT_III, aIsCATIII ) );
         lSensitivities.add( new SensitivityConfigurationTO( RefSensitivityKey.ETOPS, aIsETOPS ) );
      }

      return lSensitivities;
   }


   private String getExpectedConfigureHistoryNote( String... aEnabledSystems ) {

      if ( aEnabledSystems.length > 0 ) {
         return i18n.get( SystemSensitivityLogServiceImpl.CONFIGURE_NOTE_ENABLED,
               Arrays.toString( aEnabledSystems ) );
      }
      return i18n.get( SystemSensitivityLogServiceImpl.CONFIGURE_NOTE_ALL_DISABLED );
   }


   private String getExpectedPropagationHistoryNote( String aParentConfigSlotName,
         String... aEnabledSystems ) {

      if ( aEnabledSystems.length > 0 ) {
         return i18n.get( SystemSensitivityLogServiceImpl.PROPAGATE_NOTE_ENABLED,
               Arrays.toString( aEnabledSystems ), aParentConfigSlotName );
      }
      return i18n.get( SystemSensitivityLogServiceImpl.PROPAGATE_NOTE_ALL_DISABLED,
            aParentConfigSlotName );
   }
}
