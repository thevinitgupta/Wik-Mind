package com.wikmind.service.users.repository;

import com.wikmind.service.users.entity.ConnectedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConnectedAccountRepository extends JpaRepository<ConnectedAccount, UUID> {
    @Query("SELECT a FROM ConnectedAccount a WHERE a.provider = :provider AND a.providerUserId = :providerUserId")
    Optional<ConnectedAccount> findByProviderAndProviderId(@Param("provider") String provider, @Param("providerUserId") String providerUserId);

}
