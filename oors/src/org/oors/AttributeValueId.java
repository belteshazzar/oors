package org.oors;

import java.io.Serializable;

public final class AttributeValueId implements Serializable {
	
	private static final long serialVersionUID = -987158191570100284L;
	
	public long projectBranchId;
	public long attributeId;
	public long valueForId;

    public AttributeValueId() {}

    public AttributeValueId(long projectBranchId, long id, long valueForId ) {
        this.projectBranchId = projectBranchId;
        this.attributeId = id;
        this.valueForId = valueForId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributeValueId)) {
            return false;
        }
        AttributeValueId id = (AttributeValueId) o;
        return this.projectBranchId==id.projectBranchId
        		&&
        		this.attributeId == id.attributeId
        		&&
        		this.valueForId == id.valueForId;
    }

    public int hashCode() {
        return (int)projectBranchId ^ (int)attributeId ^ (int)valueForId;
    }

    public String toString() {
        return "AttributeValueId[projectBranchId=" + projectBranchId +",attributeId="+attributeId+",valueForId="+valueForId+"]";
    }
}