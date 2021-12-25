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
    final int gameBoard [] = { 0, 2, 2, 1, 0, 2, 0, 1, 0 };

    private List<Player> addPlayers(){
        List<Player> players = new ArrayList<>();
        System.out.println("how many players?");
        int noOfPlayers = scanner.nextInt();
        for (int i = 1; i<= noOfPlayers; i++){
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

    private int calculatePoints() {
        return 0;
    }

    public static void main(String[] args) {
        gameplay();
    }

    private static void gameplay() {
        Game game = new Game();
        List<Player> players = game.addPlayers();
        for (Player player: players){
            Quiz randomQuestion = game.getRandomQuestion();
            System.out.println(randomQuestion.getQuestion());
            String userAnswer = game.userAnswer();
            boolean isCorrectAnswer = game.checkAnswer(randomQuestion, userAnswer);
            System.out.println(isCorrectAnswer);
            if (isCorrectAnswer) {
                player.setPosition(player.getPosition() + 1);
            } else {
                player.setPosition(player.getPosition() - 1);
            }
            int calculatePoints = game.calculatePoints(); //todo implement logic for calculating points and positions
        }
        System.out.println(players);
    }
}
