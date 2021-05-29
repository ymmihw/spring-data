package com.ymmihw;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "tbl_products")
@SQLDelete(sql = "UPDATE tbl_products SET deleted = true WHERE id=?")
@FilterDef(
    name = "deletedProductFilter",
    parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
@Getter
@Setter
public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private double price;

  private boolean deleted = Boolean.FALSE;
}
