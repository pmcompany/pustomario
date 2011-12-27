package com.github.pmcompany.pustomario.core;

import java.util.LinkedHashMap;

/**
 * @author dector (dector9@gmail.com)
 */
public class Rating {
    private java.util.Map<Player, Integer> score;
    private Player winner;

    public Rating() {
        score = new LinkedHashMap<Player, Integer>();
    }

    public int getScore(Player p) {
        if (p != null) {
            return score.get(p);
        } else {
            return Integer.MIN_VALUE;
        }
    }

    public void addPlayer(Player p) {
        addPlayer(p, 0);
    }

    public void addPlayer(Player p, int initialScore) {
        if (score.isEmpty()) {
            winner = p;
        }

        score.put(p, initialScore);
    }

    public void incScore(Player p, int value) {
        System.out.println("Scoring " + p.getName() + " +" + value);

        if (score.containsKey(p)) {
            score.put(p, score.get(p) + value);
        }

        if (! score.isEmpty()) {
            System.out.printf("In rating: killer %d, winner %d%n", score.get(p), score.get(winner));

            if (score.get(p) >= score.get(winner)) {
                winner = p;
            }
        }
    }
    
    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
    
    public void clearRate() {
        for (Player p : score.keySet()) {
            score.put(p, 0);
        }
    }
}
