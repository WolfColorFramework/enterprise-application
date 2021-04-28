package com.mit.mission.log.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "log_B")
public class B implements Serializable {

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
