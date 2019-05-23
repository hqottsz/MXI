package com.mxi.am.domain.builder;

import static org.apache.commons.lang.ObjectUtils.defaultIfNull;

import com.mxi.am.domain.PartRequirementDefinition;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskPartListKey;
import com.mxi.mx.core.table.task.TaskPartList;


/**
 * Domain builder for Part Requirement Definition
 *
 */
public class PartRequirementDefinitionBuilder {

   public static TaskPartListKey build( PartRequirementDefinition aPartRequirement ) {

      TaskPartList lTaskPartList = TaskPartList
            .create( TaskPartList.generatePrimaryKey( aPartRequirement.getTaskDefinition() ) );
      lTaskPartList.setBOMPart( aPartRequirement.getPartGroup() );
      lTaskPartList.setBomItemPosition( aPartRequirement.getPosition() );
      lTaskPartList
            .setInstallBool( ( boolean ) defaultIfNull( aPartRequirement.isIsInstall(), false ) );
      lTaskPartList
            .setRemoveBool( ( boolean ) defaultIfNull( aPartRequirement.isIsRemoval(), false ) );
      lTaskPartList.setRemoveReason( aPartRequirement.getRemovalReason() );
      lTaskPartList.setSpecPartNo( aPartRequirement.getSpecificPart() );
      lTaskPartList.setReqQt( aPartRequirement.getQuantity() );

      lTaskPartList
            .setRequestAction( ( RefReqActionKey ) defaultIfNull( aPartRequirement.getAction(),
                  RefReqActionKey.REQ ) );

      return lTaskPartList.insert();

   }

}
