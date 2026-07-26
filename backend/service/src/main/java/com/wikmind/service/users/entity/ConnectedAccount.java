package com.wikmind.service.users.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "connected_accounts")
@Data
@NoArgsConstructor
public class ConnectedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NonNull
    private String provider;
    @NonNull
    private String providerUserId;
    @CreatedDate
    private LocalDateTime connectedAt;
    @Nullable
    private String refreshToken;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
