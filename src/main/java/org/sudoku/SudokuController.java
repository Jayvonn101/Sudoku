package org.sudoku;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/game")

public class SudokuController {

    private final GameStore gameStore;

public SudokuController(GameStore gameStore) {
    this.gameStore = gameStore;
}

@PostMapping("/new")
public ResponseEntity<?> newGame(@RequestParam String difficulty) {
    int emptyCells = switch (difficulty.toLowerCase()) {
        case "easy" -> 30;
        case "medium" -> 45;
        case "hard" -> 55;
        default -> 45;
    };
    String id = gameStore.createGame(difficulty, emptyCells);
    return ResponseEntity.ok(Map.of("id", id, "difficulty", difficulty));
}
@GetMapping("/{id}")
public ResponseEntity<?> getGame(@PathVariable String id) {
    GameSession session = gameStore.getGame(id);
    if (session == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(Map.of(
        "sudoku", session.getSudoku().getGrid(),
        "difficulty", session.getDifficulty(),
        "elapsedTime", session.getElapsedTime()
    ));

    }
@PostMapping("/{id}/move")
public ResponseEntity<?> makeMove(@PathVariable String id,
                                   @RequestParam int row,
                                   @RequestParam int col,
                                   @RequestParam int num) {
    GameSession session = gameStore.getGame(id);
    if (session == null) {
        return ResponseEntity.notFound().build();
    }
    boolean success = session.getSudoku().placeNum(row - 1, col - 1, num);
    if (!success) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid move"));
    }
    return ResponseEntity.ok(Map.of("board", session.getSudoku().getGrid()));
}
@DeleteMapping("/{id}/move")
public ResponseEntity<?> removeMove(@PathVariable String id,
                                     @RequestParam int row,
                                     @RequestParam int col) {
    GameSession session = gameStore.getGame(id);
    if (session == null) {
        return ResponseEntity.notFound().build();
    }
    boolean success = session.getSudoku().removeNum(row - 1, col - 1);
    if (!success) {
        return ResponseEntity.badRequest().body(Map.of("error", "Cannot remove this cell"));
    }
    return ResponseEntity.ok(Map.of("board", session.getSudoku().getGrid()));
}
@GetMapping("/{id}/hint")
public ResponseEntity<?> getHint(@PathVariable String id) {
    GameSession session = gameStore.getGame(id);
    if (session == null) {
        return ResponseEntity.notFound().build();
    }
    Sudoku.Hint hint = session.getSudoku().getHint();
    if (hint == null) {
        return ResponseEntity.ok(Map.of("message", "Puzzle is already complete"));
    }
    return ResponseEntity.ok(Map.of("row", hint.row + 1, "col", hint.col + 1, "value", hint.value));
}

@GetMapping("/{id}/solve")
public ResponseEntity<?> solveGame(@PathVariable String id) {
    GameSession session = gameStore.getGame(id);
    if (session == null) {
        return ResponseEntity.notFound().build();
    }
    session.getSudoku().solve();
    return ResponseEntity.ok(Map.of("board", session.getSudoku().getGrid()));
}


}
