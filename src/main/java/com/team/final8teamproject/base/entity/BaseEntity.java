package com.team.final8teamproject.base.entity;


import com.team.final8teamproject.share.Timestamped;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class BaseEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String nickName;

    public BaseEntity(String username, String password,
                      String email, UserRoleEnum role,
                      String nickName){
        this.username = username;
        this.password = password;
        this.email=email;
        this.role = role;
        this.nickName = nickName;
    }

    public BaseEntity(String username, String password,
                      UserRoleEnum role, String nickName){
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickName = nickName;
    }
    public BaseEntity(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickNme(String nickName) {
        this.nickName = nickName;
    }
    public boolean isUserId(Long userid) {
        return this.id.equals(userid);
    }

    public boolean isUsername(String username){ return this.username.equals(username);}

    public boolean isEmail(String email){return this.email.equals(email);}
    public String getWriterName() {
        return this.username;
    }


    public void approvalManager(UserRoleEnum role) {
        this.role = role;
    }

    public void modifyBaseProfile(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
