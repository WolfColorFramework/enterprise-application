package com.mit.mission.flowable.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "flowable_A")
public class A implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识UUID
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String uuid;

    /**
     * 站台id
     */
    @Column(name = "name")
    private String name;
}