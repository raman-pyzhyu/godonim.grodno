package com.grsu.map.domain;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "media")
public class Media {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "media")
    private String media;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "label_id")
    private Label label;
}
