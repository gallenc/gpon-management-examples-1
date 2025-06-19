package org.opennms.rest.model;

public class Parameter {
   
   private String name;
   private String value;
   private String type;
   
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getValue() {
      return value;
   }
   public void setValue(String value) {
      this.value = value;
   }
   public String getType() {
      return type;
   }
   public void setType(String type) {
      this.type = type;
   }
   
   @Override
   public String toString() {
      return "Parameter [name=" + name + ", value=" + value + ", type=" + type + "]";
   }
   
   

}
