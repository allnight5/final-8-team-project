package com.team.final8teamproject.manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import net.minidev.json.annotate.JsonIgnore;

@Entity

public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String manager;
    @JsonIgnore
    @NotBlank
    private String password;
    private String profileImage;
    @NotBlank
    private String nickname;

}
