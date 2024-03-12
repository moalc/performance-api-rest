package com.rok.faker.performanceapirest.controllers;

import com.rok.faker.performanceapirest.entitys.Player;
import com.rok.faker.performanceapirest.services.PlayerService;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:4200") // Especifica el origen permitido
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Player>> getListAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @PostMapping("/new")
    public Player createPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @PostMapping("/newList")
    public List<Player> createListPlayers(@RequestBody List<Player> listPlayers) {
        List<Player> list = new ArrayList<>();
        for (Player player : listPlayers) {
            list.add(playerService.createPlayer(player));
        }
        return list;
    }

    @GetMapping("/calculateDaysFromIDParam")
    public long calculateDaysFromIDConParam(@Param("id") String id) {
        return calculateDaysFromID(Long.parseLong(id));
    }

    @GetMapping("/calculateDaysFromIDBody")
    public long calculateDaysFromIDConBody(@RequestBody String id) {
        return calculateDaysFromID(Long.parseLong(id));
    }

    @GetMapping("/calculateDateFromIDParam")
    public LocalDateTime calculateDateFromID(@Param("id") String id) {
        return LocalDateTime.ofEpochSecond((long) doLinealRegression().predict(Double.parseDouble(id)), 0, ZoneOffset.UTC);
    }

    private long calculateDaysFromID(Long id) {
        return ChronoUnit.DAYS.between(LocalDateTime.ofEpochSecond((long) doLinealRegression().predict(id), 0, ZoneOffset.UTC),
        LocalDateTime.now());
    }


    private SimpleRegression doLinealRegression() {
        SimpleRegression regression = new SimpleRegression();
        List<Player> playerList = playerService.getAllPlayers();
        for(Player player : playerList) {
            if(player.getInitDate() != null) {
                regression.addData(player.getId(), player.getInitDate().toEpochSecond(ZoneOffset.UTC));
            }
        }
        return regression;
    }
}
