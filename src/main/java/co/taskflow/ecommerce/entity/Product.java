package co.taskflow.ecommerce.entity;

import java.math.BigDecimal;

import co.taskflow.ecommerce.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id; 

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_des")
    private String description;

    @Column(name = "product_unit_price")
    private BigDecimal price;

    @Column(name = "product_stock")
    private Integer stockQty;

    @Column(name = "product_image")
    private String imageUrl;

    @Column(name = "product_status")
    private Boolean status;

}
