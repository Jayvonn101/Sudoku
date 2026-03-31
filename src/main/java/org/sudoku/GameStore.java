package org.sudoku;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GameStore {
    private final Map<String, GameSession> games = new HashMap<>();

    public String createGame(String difficulty, int emptyCells) {
        String id = UUID.randomUUID().toString();
        games.put(id, new GameSession(emptyCells, difficulty));
        return id;
    }

    public GameSession getGame(String id) {
        return games.get(id);
    }

    public void removeGame(String id) {
        games.remove(id);
    }
}

