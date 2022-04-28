package org.litteraworlds;

import org.litteraworlds.game.MapGeneration;
import org.litteraworlds.input.Command;
import org.litteraworlds.map.Region;
import org.litteraworlds.objects.Creature;
import org.litteraworlds.objects.Player;
import org.litteraworlds.view.TextLines;
import org.litteraworlds.view.colors.TextColors;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;
import security.HashGen;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Array;

import static org.litteraworlds.input.PlayerInput.inputCommand;

public class GameLoop {

    private Socket clientSocket;

    private ServerInstance serverInstance;

    private static Player player;

    private Region region;

    public static Player getPlayer(){
        return player;
    }

    public void playerCreation(){

        GameScreen.putString(TextColors.GAME_MESSAGE,"Посреди осколков старого мира появляется таинственный герой");

        GameScreen.putString(TextColors.PLAYER_COLOR, "А имя его - "+player.getName());

        GameScreen.putString(TextColors.GAME_MESSAGE,"Если твою личность ещё предстоит раскрыть в путешествии, то" +
                        "с характеристиками можно решить уже сейчас.");



        GameScreen.putString(player.getAbilities().toString());

        int available = Creature.Abilities.class.getDeclaredFields().length;
        int points;

        GameScreen.putString(TextColors.NARRATOR_COLOR,"У тебя есть "+available+" очков. Почему именно столько? Ну так вышло." +
                " Почему ты спрашиваешь про очки, но не о том, что с тобой разговаривает кто-то?");
        GameScreen.putString(TextColors.NARRATOR_COLOR,"Итак, начнём с атаки, сколько добавишь?");
        GameScreen.putString(TextColors.HELP_MESSAGE, "Просто введи цифру");

        available -= points = getPoints(inputCommand(), available);
        player.getAbilities().setAtk(player.getAbilities().getAtk() + points);

        GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось "+available+" очков");

        if(available > 0) {
            GameScreen.putString(TextColors.NARRATOR_COLOR,"Давай на защиту накинем.");
            available -= points = getPoints(inputCommand(), available);
            player.getAbilities().setDef(player.getAbilities().getDef() + points);

            GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось "+available+" очков");
        }

        if(available > 0) {
            GameScreen.putString(TextColors.NARRATOR_COLOR,"Насколько ты ловкий?");
            available -= points = getPoints(inputCommand(), available);
            player.getAbilities().setDex(player.getAbilities().getDex() + points);


            GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось " + available + " очков");
        }

        if(available > 0) {
            GameScreen.putString(TextColors.NARRATOR_COLOR,"Добавим к крепости духа?");
            available -= points = getPoints(inputCommand(), available);
            player.getAbilities().setDex(player.getAbilities().getDex() + points);
        }

        GameScreen.putString(TextColors.HELP_MESSAGE, "Очков не осталось");

        GameScreen.putString(TextColors.NARRATOR_COLOR,"Ну что же, ты готов к приключениям?");

        GameScreen.putString(TextColors.NARRATOR_COLOR, "Вводи что хочешь - ты всё равно отправишься в путь");


    }


    public void clientAuthorization() throws IOException {



        try {
            clientSocket = new Socket("127.0.0.1", 8080);
            serverInstance = new ServerInstance(clientSocket);
            GameScreen.putString(MessageType.SYSTEM, "Соединение с сервером установлено");

        } catch (IOException e) {
            GameScreen.putString(MessageType.SYSTEM, "Сервак лежит...");
            e.printStackTrace();
            inputCommand();

        }


            GameScreen.putString(TextColors.HELP_MESSAGE, "Созайте свой аккаунт (введите команду /создать) ");
            GameScreen.putString(TextColors.HELP_MESSAGE, "или войдите в уже существующий аккаунт (введите команду /войти)");

            while (true) {
                String command = inputCommand();
                switch (command) {
                    case "/войти" -> {

                        for (String line : TextLines.getIntrolines()) {
                            GameScreen.putString(line);
                        }

                        GameScreen.putString(MessageType.SYSTEM, "Введите свой никнейм ");
                        String name = inputCommand();
                        GameScreen.putString(MessageType.SYSTEM, "Введите свой пароль ");
                        String password = inputCommand();


                        GameScreen.putString(MessageType.INFO, "Вы ввели ник: " + name);
                        GameScreen.putString(MessageType.INFO, "Вы ввели пароль: " + password);


                        GameScreen.putString(MessageType.SYSTEM, "Пока все ");

                    }

                    case "/создать" -> {


                        for (String line : TextLines.getIntrolines()) {
                            GameScreen.putString(line);
                        }

                        GameScreen.putString(TextColors.NARRATOR_COLOR, "Придумайте  никнейм,  используя  ");
                        GameScreen.putString(TextColors.NARRATOR_COLOR,"все потаеные места вашего воображения");

                        while(true) {



                            String name = inputCommand();


                            serverInstance.getOut().write(("CHNM" + name).getBytes(StandardCharsets.UTF_8));
                            serverInstance.getOut().flush();

                            int status = (int) serverInstance.getIn().read();
                            System.out.println(status);

                            if (status == 49) {

                                GameScreen.putString(MessageType.SYSTEM, "Такой ник уже занят, придумайте другой");
                                continue;

                            }
                            else if (status == 48) {

                                GameScreen.putString(MessageType.SYSTEM, "Добро пожаловать, "+name);
                                player = new Player(name);
                                playerCreation();

                                serverInstance.getOut().write(("GTKN" + player.getName()).getBytes(StandardCharsets.UTF_8));
                                serverInstance.getOut().flush();
                                GameScreen.putString(MessageType.SYSTEM, "1");

                                byte[] tokenID=new byte[1024];
                                serverInstance.getIn().read(tokenID);


                                String userPassword = HashGen.randomPasswordGenerator(tokenID);

                                byte[] hashUserPlayer =HashGen.getHash(userPassword);

                                PrintWriter writer = new PrintWriter(hashUserPlayer+".pdto", "UTF-8");
                                writer.println(userPassword);
                                writer.close();

                                GameScreen.putString(MessageType.SYSTEM, "Ваш супер надежный пароль: "+userPassword);


                                //serverInstance.getOut().write(());


                                break;
                            }
                            else {

                                GameScreen.putString(MessageType.ERROR, "Ошибка при передаче данных");

                            }

                        }

                    }

                    case "/выход" -> {
                        GameScreen.dispose();
                        return;
                    }

                }
                if (GameScreen.isClose()) {
                    break;
                }
            }




    }

    public int getPoints(String input, int available){
        try{
            while (true) {
                int points = Integer.parseInt(input);

                if (points <= available) {
                    return points;
                } else if (available > 0) {
                    GameScreen.putString(TextColors.GAME_MESSAGE, "Введено очков больше, чем доступно");

                    return getPoints(inputCommand(), available);
                } else {
                    return 0;
                }
            }
        } catch (NumberFormatException e) {
            GameScreen.putString(MessageType.ERROR, "Это не число!");
            GameScreen.putString(TextColors.GAME_MESSAGE, "Попробуй ещё раз:");
            return getPoints(inputCommand(), available);
        }
    }

    public void start() {
        GameScreen.putString(MessageType.SYSTEM, "Запуск игрового цикла");
        region = MapGeneration.generateNewRegion();
        region.putPlayerIntoRandomZone(player);

        GameScreen.putString(TextColors.PLAYER_COLOR, "Вы оказываетесь в регионе "+region+" в зоне "+player.getObjectPlace());
        GameScreen.putString(TextColors.GAME_MESSAGE,"Что будете делать дальше?");
        GameScreen.putString(TextColors.HELP_MESSAGE,"Наберите /помощь для списка команд");

        String commandLine;
        while (!(commandLine = inputCommand()).equals("/выход")){
            if(commandLine.startsWith("/")){
                Command.parse(commandLine);
            }
        }
    }

}
