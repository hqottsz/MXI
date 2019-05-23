package com.mxi.mx.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * This class manage the building of Where Clause of SQL statement
 *
 * @author Alicia Qian
 */
public class WhereClause {

   private Map<String, String> iColumnsSetting;


   public WhereClause() {
      this.iColumnsSetting = new LinkedHashMap<String, String>();
   }


   public void addArguments( String aArgName, String aValue ) {
      iColumnsSetting.put( aArgName, aValue );

   }


   public Set<Entry<String, String>> entrySet() {
      return this.iColumnsSetting.entrySet();
   }

}
