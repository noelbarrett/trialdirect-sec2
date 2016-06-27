package com.tekenable.tdsec2.model;

import com.google.common.collect.Maps;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by nbarrett on 22/06/2016.
 * Taken from firecrest commons/db library.
 */
@MappedSuperclass
public abstract class EntityBean implements Serializable {

    public static final String MEATADATA_FILED_IDENTIFIER = "identifier";
    public static final String MEATADATA_FILED_TYPE = "type";
    public static final String MEATADATA_FILED_LABEL = "label";
    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "test-native-strategy",
            strategy = "native"
    )
    @GeneratedValue(
            generator = "test-native-strategy"
    )
    @Column(
            name = "pk"
    )
    private Long pk;

    public EntityBean() {
    }

    private static Class<?> unproxiedClass(Object o) {
        return o instanceof HibernateProxy ?((HibernateProxy)o).getHibernateLazyInitializer().getPersistentClass():o.getClass();
    }

    public boolean equals(Object o) {
        if(!(o instanceof EntityBean)) {
            return false;
        } else if(o == this) {
            return true;
        } else if(this.getPk() == null) {
            return false;
        } else {
            EntityBean e = (EntityBean)o;
            return (new EqualsBuilder()).append(this.getPk(), e.getPk()).append(unproxiedClass(this), unproxiedClass(e)).isEquals();
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder()).append(unproxiedClass(this)).append(this.getPk()).toHashCode();
    }

    public Long getPk() {
        return this.pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    protected void addMetadataIdentifier(List<Map<String, String>> metadata, String identifier, String type, String label) {
        Map entry = (Map) Maps.newHashMap();
        entry.put("identifier", identifier);
        entry.put("type", type);
        entry.put("label", label);
        metadata.add(entry);
    }

    protected void addMetadataIdentifier(List<Map<String, String>> metadata, String identifier, String type) {
        Map entry = (Map)Maps.newHashMap();
        entry.put("identifier", identifier);
        entry.put("type", type);
        metadata.add(entry);
    }


}
