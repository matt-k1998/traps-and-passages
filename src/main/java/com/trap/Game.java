package com.trap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    final Scanner scanner = new Scanner(System.in);
    /*
        Key to board:
        0 = normal spot
        1 = trap
        2 = secret passage
     */
    final static int [] gameBoard = { 0, 1, 2, 0, 0, 2, 0, 1, 0 };

    private List<Player> addPlayers(){
        List<Player> players = new ArrayList<>();
        System.out.println("how many players?");
        int noOfPlayers = scanner.nextInt();
        for (int i = 1; i <= noOfPlayers; i++) {
            System.out.println("Enter name of player " + i);
            String playerName = scanner.next();
            players.add(new Player(playerName));
        }
        System.out.println(players);
        return players;
    }

    private List<Quiz> importQuiz() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    new File("questions.json"),
                    new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private Quiz getRandomQuestion() {
        Random random = new Random();
        int randomIndex = random.nextInt(importQuiz().size());
        return importQuiz().get(randomIndex);
    }

    private String userAnswer() {
        return scanner.next();
    }

    private boolean checkAnswer(Quiz randomQuestion, String userAnswer) {
        return userAnswer.equalsIgnoreCase(randomQuestion.getAnswer());
    }

    private int rollDice() {
        return (int)(Math.random()*6) + 1;
    }

    private int checkCurrentSpot(int position) {
        if (position >= gameBoard.length-1){
            return 0;
        }
        //TODO: check the spot again until it is a 0??
        if (gameBoard[position-1] == 1)
            return -1;
        else if (gameBoard[position-1] == 2)
            return 1;
        else if (gameBoard[position-1] == 0)
            return 0;
        return 0;
    }

    public static void main(String[] args) {
        gameplay();
    }

    private static void gameplay() {
        Game game = new Game();
        List<Player> players = game.addPlayers();
        while(!game.hasTheGameEnded(players)) {
            for (Player player : players) {
                Quiz randomQuestion = game.getRandomQuestion();
                System.out.println(randomQuestion.getQuestion());
                String userAnswer = game.userAnswer();
                boolean isCorrectAnswer = game.checkAnswer(randomQuestion, userAnswer);
                if (isCorrectAnswer) {
                    final int dieResult = game.rollDice();
                    System.out.println("Player " + player.getName() + " rolled a " + dieResult);
                    player.setPosition(player.getPosition() + dieResult);
                    int positionsToMove = game.checkCurrentSpot(player.getPosition());
                    System.out.println("Player " + player.getName() + " should move further " + positionsToMove + " position");
                    player.setPosition(player.getPosition() + positionsToMove);
                    System.out.println("Player " + player.getName() + "'s position is: " + player.getPosition());
                } else {
                    if (player.getPosition() > 0 ) {
                        player.setPosition(player.getPosition() - 1);
                    }
                }
            }
            System.out.println(players);
        }
    }

    private boolean hasTheGameEnded(List<Player> players) {
        for (Player player: players){
            if (player.getPosition() >= gameBoard.length-1){
                System.out.println("Player " + player.getName() + " has reached position " + player.getPosition() + " and won");
                return true;
            }
        }
        return false;
    }
}
