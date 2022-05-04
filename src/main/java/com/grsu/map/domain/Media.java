package com.grsu.map.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "media")
public class Media {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "file_name")
    private String fileName;
}
