package com.ymmihw.hibernateenvers.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OrderBy;
import org.hibernate.envers.Audited;
import org.jboss.logging.Logger;

@Entity
@Audited
public class Bar implements Serializable {

  private static Logger logger = Logger.getLogger(Bar.class);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "bar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy(clause = "name DESC")
  // @NotAudited
  private Set<Foo> fooSet = new HashSet<>();

  public Bar() {
    super();
  }

  public Bar(final String name) {
    super();

    this.name = name;
  }

  // API

  public Set<Foo> getFooSet() {
    return fooSet;
  }

  public void setFooSet(final Set<Foo> fooSet) {
    this.fooSet = fooSet;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Bar other = (Bar) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Bar [name=").append(name).append("]");
    return builder.toString();
  }

}
