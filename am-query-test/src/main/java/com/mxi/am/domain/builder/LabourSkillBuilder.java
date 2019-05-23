package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.LabourSkill;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.Table;
import com.mxi.mx.core.key.RefLabourSkillKey;


/**
 * Builder for Labour Skill entity
 *
 */
public class LabourSkillBuilder {

   private static int iLabourSkillCode = 0;


   public static RefLabourSkillKey build( LabourSkill aLabourSkill ) {

      RefLabourSkillKey lLabourSkillKey = new RefLabourSkillKey( Table.Util.getDatabaseId(),
            ( String ) defaultIfNull( aLabourSkill.getCode(), generateUniqueLabourSkillCode() ) );

      MxDataAccess.getInstance().executeInsert( "ref_labour_skill",
            lLabourSkillKey.getPKWhereArg() );

      return lLabourSkillKey;
   }


   private static String generateUniqueLabourSkillCode() {
      return "LabourSkill" + iLabourSkillCode++;
   }

}
