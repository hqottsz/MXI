
package com.mxi.mx.core.dao.org;

import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.model.organization.Organization;


/**
 * This class serves as factory to generate various types of Organizations for test purposes
 *
 * @author asmolko
 */
public class OrgTestDataFactory {

   /**
    * Generates an organization of type Department
    *
    * @param aOrgKey
    *           the PK of the organization to generate
    * @param aParentOrgKey
    *           the PK of the parent organization to generate
    *
    * @return Organization of type Department
    */
   public static Organization genDepartment( OrgKey aOrgKey, OrgKey aParentOrgKey ) {
      return new Organization( aOrgKey, "CD" + aOrgKey.toString(),
            "CODE_MDESC: " + aOrgKey.toString(), "SDESC " + aOrgKey.toString(),
            "LDESC " + aOrgKey.toString(), RefOrgTypeKey.DEPT, null, aOrgKey, aParentOrgKey, true );
   }


   /**
    * Generates an organization of type Operator
    *
    * @param aOrgKey
    *           the PK of the organization to generate
    * @param aParentOrgKey
    *           the PK of the parent organization to generate
    *
    * @return Organization of type Operator
    */
   public static Organization genOperator( OrgKey aOrgKey, OrgKey aParentOrgKey ) {
      return new Organization( aOrgKey, "CD" + aOrgKey.toString(),
            "CODE_MDESC" + aOrgKey.toString(), "SDESC " + aOrgKey.toString(),
            "LDESC " + aOrgKey.toString(), RefOrgTypeKey.OPERATOR, null, aOrgKey, aParentOrgKey,
            true );
   }
}
