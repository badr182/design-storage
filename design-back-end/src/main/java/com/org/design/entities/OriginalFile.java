package com.org.design.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table( name = "original_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OriginalFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    /**
     * @TODO type allowed
     */
    private String type;
    /**
     * @TODO size with MB
     */
    private double size;

    private int numberChunks;
    // cascade = CascadeType.ALL, fetch = FetchType.EAGER,
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "originalFile")
    // @JoinColumn(name = "original_file_id", nullable = false)
    // @JoinColumn(name = "pc_fid", referencedColumnName = "id")
    private List<Chunks> chunks ;
}