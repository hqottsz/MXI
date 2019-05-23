package com.mxi.mx.web.services.taskdefn.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.LabourSkill;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.Step;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.taskdefn.diff.engine.common.DataSetDiffService;


/**
 * Unit test suite for the {@link StepsDataSetDiffService} class.
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class StepsDataSetDiffServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String SKILL_REMOVED_FROM_CURRENT_REVISION = "AMT";
   private static final String SKILL_MODIFIED_IN_CURRENT_REVISION = "INSP";
   private static final String SKILL_ADDED_IN_CURRENT_REVISION = "ENG";
   private static final String REQ_DEFN_CODE = "REQ_DEFN_CODE";
   public static final String MERGED_DATA_SET_DIFF_VALUES_COLUMN = "diff_values";
   public static final String BLANK_SPACE = " ";


   @Test
   public
         void
         itPreparesDSDiffValueInSkillInspectionLocaleSpecificTextDiffStatusTripletBasedOnChangesBetweenTaskDefinitionRevisions() {

      final AssemblyKey lAcftAssembly = Domain.createAircraftAssembly();

      final RefLabourSkillKey lSkillRemovedFromCurrentRevision =
            createLabourSkill( SKILL_REMOVED_FROM_CURRENT_REVISION );
      final RefLabourSkillKey lSkillModifiedInCurrentRevision =
            createLabourSkill( SKILL_MODIFIED_IN_CURRENT_REVISION );
      final RefLabourSkillKey lSkillAddedInCurrentRevision =
            createLabourSkill( SKILL_ADDED_IN_CURRENT_REVISION );

      final TaskTaskKey lFormerReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( Domain
                        .readRootConfigurationSlot( lAcftAssembly ) );
                  aReqDefn.setCode( REQ_DEFN_CODE );
                  aReqDefn.addLabourRequirement( lSkillRemovedFromCurrentRevision, BigDecimal.ZERO );
                  aReqDefn.addLabourRequirement( lSkillModifiedInCurrentRevision, BigDecimal.ZERO );
                  aReqDefn.setRevisionNumber( 1 );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.REVISION );
                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lSkillRemovedFromCurrentRevision, true );
                        aBuilder.addStepSkill( lSkillModifiedInCurrentRevision, false );
                     }
                  } );
               }
            } );

      final TaskTaskKey lReqDefn =
            Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

               @Override
               public void configure( RequirementDefinition aReqDefn ) {
                  aReqDefn.againstConfigurationSlot( Domain
                        .readRootConfigurationSlot( lAcftAssembly ) );
                  aReqDefn.setStatus( RefTaskDefinitionStatusKey.ACTV );
                  aReqDefn.setPreviousRevision( lFormerReqDefn );
                  aReqDefn.setRevisionNumber( 2 );
                  aReqDefn.addLabourRequirement( lSkillAddedInCurrentRevision, BigDecimal.ZERO );
                  aReqDefn.addLabourRequirement( lSkillModifiedInCurrentRevision, BigDecimal.ZERO );
                  aReqDefn.addStep( new DomainConfiguration<Step>() {

                     @Override
                     public void configure( Step aBuilder ) {
                        aBuilder.addStepSkill( lSkillAddedInCurrentRevision, true );
                        aBuilder.addStepSkill( lSkillModifiedInCurrentRevision, true );
                     }
                  } );
               }
            } );

      DataSet lResultDs = new StepsDataSetDiffService().initDiffDs( lReqDefn, lFormerReqDefn );

      assertNotNull( lResultDs );

      String lRegexDiffValues = "\\bidStepSkills\\d*\\b=\\s*(.*)\\d*";

      lResultDs.next();
      String lDiffValue = lResultDs.getString( MERGED_DATA_SET_DIFF_VALUES_COLUMN );
      String lSkillDiffValueList = "";
      final Pattern lPattern = Pattern.compile( lRegexDiffValues, Pattern.CASE_INSENSITIVE );
      Matcher lMatcher = lPattern.matcher( lDiffValue );

      if ( lMatcher.find() ) {
         lSkillDiffValueList = lMatcher.group( 1 );
      }

      String lExpectedSkillInspStatusList =
            lSkillRemovedFromCurrentRevision.getCd() + BLANK_SPACE
                  + i18n.get( "web.lbl.INDEPENDENT_INSPECTION_REQUIRED_FOR_SKILL" ) + ":"
                  + DataSetDiffService.STATUS_REMOVED + "," + lSkillAddedInCurrentRevision.getCd()
                  + BLANK_SPACE + i18n.get( "web.lbl.INDEPENDENT_INSPECTION_REQUIRED_FOR_SKILL" )
                  + ":" + DataSetDiffService.STATUS_ADDED + ","
                  + lSkillModifiedInCurrentRevision.getCd() + ":"
                  + DataSetDiffService.STATUS_MODIFIED;

      assertEquals( "Unexpected step differences between task definition revisions",
            lExpectedSkillInspStatusList, lSkillDiffValueList );

   }


   private RefLabourSkillKey createLabourSkill( final String aSkillCode ) {
      return Domain.createLabourSkill( new DomainConfiguration<LabourSkill>() {

         @Override
         public void configure( LabourSkill aBuilder ) {
            aBuilder.setCode( aSkillCode );
         }
      } );
   }

}
