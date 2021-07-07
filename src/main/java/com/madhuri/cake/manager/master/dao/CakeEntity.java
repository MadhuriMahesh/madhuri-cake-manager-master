package com.madhuri.cake.manager.master.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "Cake", uniqueConstraints = {@UniqueConstraint(columnNames = "ID"), @UniqueConstraint(columnNames = "CAKE_NAME")})
public class CakeEntity {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "CAKE_NAME", unique = true, nullable = false, length = 100)
    @NonNull
    private String title;

    @Column(name = "DESC", unique = false, nullable = false, length = 100)
    private String description;

    @Column(name = "IMG", unique = false, nullable = false, length = 300)
    private String image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private User user;

}
