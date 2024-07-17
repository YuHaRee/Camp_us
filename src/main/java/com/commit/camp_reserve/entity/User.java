package com.commit.camp_reserve.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="user")
@Getter
@Builder
@ToString
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Column(name = "userId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth_day")
    private String birthDay;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "enroll_date")
    private LocalDateTime enrollDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_addr")
    private String userAddr;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Builder
    public User(Long userId, String email, String password, String name, String nickname, String birthDay, LocalDateTime registrationDate, LocalDateTime enrollDate, String phoneNumber, String userAddr, String profileImageUrl) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDay = birthDay;
        this.registrationDate = registrationDate;
        this.enrollDate = enrollDate;
        this.phoneNumber = phoneNumber;
        this.userAddr = userAddr;
        this.profileImageUrl = profileImageUrl;
    }
}