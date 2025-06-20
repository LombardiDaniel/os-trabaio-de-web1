package com.aa2.GamePlatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "strategies")
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @Column(name = "examples", columnDefinition = "TEXT")
    private String examples;

    @Column(name = "hints", columnDefinition = "TEXT")
    private String hints;

//    private List<String> images;

    public Strategy() {}

    public Strategy(String name, String description, String examples, String hints) {
        this.name = name;
        this.description = description;
        this.examples = examples;
        this.hints = hints;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }
}
