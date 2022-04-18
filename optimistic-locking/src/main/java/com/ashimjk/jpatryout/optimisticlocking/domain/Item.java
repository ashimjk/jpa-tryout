package com.ashimjk.jpatryout.optimisticlocking.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class Item extends BaseEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    private int amount = 0;

}
