package com.aa2.GamePlatform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Integer> {

    public Client findByEmail(String email);
}
