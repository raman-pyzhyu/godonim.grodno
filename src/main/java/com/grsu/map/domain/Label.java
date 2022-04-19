package com.grsu.map.domain;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "labels")
public class Label {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "image")
    private String image;

    @Column(name = "coordinates")
    private String coordinates;

    @OneToMany
    private Set<Media> media;
}
