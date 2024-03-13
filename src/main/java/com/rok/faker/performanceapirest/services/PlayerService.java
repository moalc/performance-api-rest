package com.rok.faker.performanceapirest.services;

import com.rok.faker.performanceapirest.entitys.Player;
import com.rok.faker.performanceapirest.repositorys.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getAllPlayersSortedByID() {
        return playerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
