package com.org.design.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table( name= "chunks")
@AllArgsConstructor
@NoArgsConstructor
public class Chunks implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private double size;

    private LocalDateTime dateTime;

    private String path;

    //private int order ;

    private int totalChunks;

    @JsonIgnore
    @ManyToOne //(cascade = {CascadeType.ALL})
    @JoinColumn(name = "original_file_id")
    // @OnDelete(action = OnDeleteAction.CASCADE)
    //@Column(name = "original_file_id")
    private OriginalFile originalFile;
}
