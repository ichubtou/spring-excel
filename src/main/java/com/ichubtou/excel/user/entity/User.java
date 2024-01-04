package com.ichubtou.excel.user.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@BatchSize(size = 10)
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(unique = true)
    private String id;

    private String pw;

    private String storeName;

    private String positionName;

    private String userName;

}
