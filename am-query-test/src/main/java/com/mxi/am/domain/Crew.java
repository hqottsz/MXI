package com.mxi.am.domain;

import java.util.HashSet;
import java.util.Set;

import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefLabourSkillKey;


public class Crew {

   private String code;
   private String name;
   private RefLabourSkillKey skill;
   private Set<HumanResourceKey> humanResources = new HashSet<HumanResourceKey>();
   private Set<LocationKey> locations = new HashSet<LocationKey>();


   public String getCode() {
      return code;
   }


   public void setCode( String code ) {
      this.code = code;
   }


   public String getName() {
      return name;
   }


   public void setName( String name ) {
      this.name = name;
   }


   public RefLabourSkillKey getSkill() {
      return skill;
   }


   public void setSkill( RefLabourSkillKey skill ) {
      this.skill = skill;
   }


   public Set<HumanResourceKey> getHumanResources() {
      return humanResources;
   }


   public void setUsers( HumanResourceKey humanResource ) {
      humanResources.add( humanResource );
   }


   public void addHumanResource( HumanResourceKey humanResource ) {
      humanResources.add( humanResource );
   }


   public Set<LocationKey> getLocations() {
      return locations;
   }


   public void setLocations( LocationKey location ) {
      locations.add( location );
   }

}
