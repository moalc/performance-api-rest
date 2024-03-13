package com.rok.faker.performanceapirest.controllers;

import com.rok.faker.performanceapirest.entitys.Player;
import com.rok.faker.performanceapirest.objects.PlayerFound;
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

    @GetMapping("/calculateDaysFromID")
    public long calculateDaysFromIDConParam(@Param("id") String id) {
        return calculateDaysFromID(id);
    }

    @GetMapping("/calculateDateFromID")
    public LocalDateTime calculateDateFromID(@Param("id") String id) {
        return getInitDate(id);
    }

    private long calculateDaysFromID(String id) {
        return ChronoUnit.DAYS.between(getInitDate(id), LocalDateTime.now());
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

    private SimpleRegression doLinealRegressionBetween2Dates(List<Player> playerList, int index) {
        SimpleRegression regression = new SimpleRegression();
        boolean encontrado = false;
        for (int i = index - 1; i >= 0 && !encontrado; i--) {
            if(playerList.get(i).getInitDate() != null) {
                regression.addData(playerList.get(i).getId(), playerList.get(i).getInitDate().toEpochSecond(ZoneOffset.UTC));
                encontrado = true;
            }
        }
        regression.addData(playerList.get(index).getId(), playerList.get(index).getInitDate().toEpochSecond(ZoneOffset.UTC));
        return regression;
    }

    private PlayerFound getIndexPlayer(Long id, List<Player> playerList) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId().longValue() == id.longValue()) {
                return new PlayerFound(i, true);
            } else if (playerList.get(i).getId().longValue() > id.longValue() && playerList.get(i).getInitDate() != null){
                return new PlayerFound(i, false);
            }
        }
        return new PlayerFound(-1, false);
    }

    private LocalDateTime getInitDate(String id) {
        List<Player> playerList = playerService.getAllPlayersSortedByID();
        PlayerFound playerFound = getIndexPlayer(Long.parseLong(id), playerList);

        if (playerFound.isNotFound()) {
            return LocalDateTime.ofEpochSecond((long) doLinealRegression().predict(Double.parseDouble(id)), 0, ZoneOffset.UTC);
        } else if (playerFound.isFound()) {
            return playerList.get(playerFound.getIndex()).getInitDate();
        } else {
            return calculateDateFromIndex(playerList, playerFound.getIndex(), id);
        }
    }

    private LocalDateTime calculateDateFromIndex(List<Player> playerList, int index, String id) {
        return LocalDateTime.ofEpochSecond((long) doLinealRegressionBetween2Dates(playerList, index).predict(Double.parseDouble(id)), 0, ZoneOffset.UTC);
    }
}
