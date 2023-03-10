package com.team.final8teamproject.user.dto;

import com.amazonaws.thirdparty.jackson.annotation.JsonCreator;
import com.amazonaws.thirdparty.jackson.annotation.JsonIgnoreProperties;
import com.amazonaws.thirdparty.jackson.annotation.JsonProperty;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SetRedisRefreshToken{
    private static final long serialVersionUID = 6494678977089006639L;
    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String refreshToken;
    private String username;
    private UserRoleEnum role;

    //역직렬화 해주기
    @JsonCreator
    public SetRedisRefreshToken(@JsonProperty("refreshToken") String refreshToken,
                                @JsonProperty("username")  String username,
                                @JsonProperty("role") UserRoleEnum role){
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;
    }
}
