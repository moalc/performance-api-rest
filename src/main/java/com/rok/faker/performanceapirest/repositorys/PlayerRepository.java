package com.rok.faker.performanceapirest.repositorys;

import com.rok.faker.performanceapirest.entitys.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {



}
