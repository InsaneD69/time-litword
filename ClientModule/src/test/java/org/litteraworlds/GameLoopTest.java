package org.litteraworlds;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.litteraworlds.view.GameScreen;

import java.io.IOException;

public class GameLoopTest {


   static GameLoop gameLoop;
    @BeforeAll
    public static void  init(){

        GameScreen.init();

        gameLoop=new GameLoop();
    }


    @Test
    public  void create() throws IOException {


        gameLoop.clientAuthorization();
    }



}
