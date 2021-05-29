package it.polimi.db2_project.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "dirtyWord", schema = "db2_app")
@NamedQuery(name = "DirtyWord.checkSentence", query = "SELECT d FROM DirtyWord d WHERE d.word = ?1")
public class DirtyWord implements Serializable {

    public DirtyWord() {
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="dirtyID")
    private int wordId;

    @Column(name ="text")
    private String word;

}
