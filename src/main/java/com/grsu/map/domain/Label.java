package com.grsu.map.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;

@Setter
@Getter
@NoArgsConstructor
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

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "image")
    private String image;

    @Column(name = "coordinates")
    private String coordinates;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "label_id")
    @OrderBy("id")
    private Set<Media> media = new HashSet<>();
}
