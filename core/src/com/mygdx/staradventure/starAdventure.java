package com.mygdx.staradventure;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.Random;


public class starAdventure extends ApplicationAdapter{

	private SpriteBatch batch;
    private ShapeRenderer shape;
	private Texture[] robo;
	private Texture[] fundo;
    private Texture[] fundo2;
    private Texture[] pedraDia;
    private Texture[] pedraNoite;
    private Texture gameOver;

    private BitmapFont fonteScore;
    private BitmapFont fonteHiScore;
    private BitmapFont mensagem;

	private Circle circuloRobo;
    private Rectangle retanguloObstaculo;

    private int width;
    private int height;
    private int estadoJogo = 0; //0-> jogo não iniciado 1-> jogo iniciado 2->Game over
    private int resto = 0;
    private int score = 0;
    private int hiScore = 0;
    private int velocidadeObstaculo = 200;

    private boolean marcouPonto=false;

    private float posicaoObstaculoHorizontal = 0;
    private float posicaoInicialVertical   = 0;
    private float deltaTime;
    private float variacao = 0;
    private float velocidadePulo = 0;
    private float dificuldade = 250;
    private float variacaoRandom = 0;




	
	@Override
	public void create () {

        try{

            batch = new SpriteBatch();
            shape = new ShapeRenderer();

            robo   = new Texture[4];
            fundo  = new Texture[4];
            fundo2 = new Texture[4];
            pedraDia   = new Texture[3];
            pedraNoite = new Texture[3];

            robo[0] = new Texture("robo1.png");
            robo[1] = new Texture("robo2.png");
            robo[2] = new Texture("robo3.png");
            robo[3] = new Texture("robo4.png");
            fundo[0]   = new Texture("bgDia1.png");
            fundo[1]   = new Texture("bgDia2.png");
            fundo[2]   = new Texture("bgDia3.png");
            fundo[3]   = new Texture("bgDia4.png");

            fundo2[0]  = new Texture("bgNoite1.png");
            fundo2[1]  = new Texture("bgNoite2.png");
            fundo2[2]  = new Texture("bgNoite3.png");
            fundo2[3]  = new Texture("bgNoite4.png");

            pedraDia[0]  = new Texture("pedra1.png");
            pedraDia[1]  = new Texture("pedra2.png");
            pedraDia[2]  = new Texture("pedra3.png");


            pedraNoite[0] = new Texture("pedraNoite1.png");
            pedraNoite[1] = new Texture("pedraNoite2.png");
            pedraNoite[2] = new Texture("pedraNoite3.png");

            gameOver   = new Texture("gameOver.png");

            //fonte para pontuação
            fonteScore = new BitmapFont();
            fonteScore.setColor(Color.WHITE);
            fonteScore.getData().setScale(2);

            //fonte hiScore
            fonteHiScore = new BitmapFont();
            fonteHiScore.setColor(Color.WHITE);
            fonteHiScore.getData().setScale(2);

            //fonte Mensagem final
            mensagem = new BitmapFont();
            mensagem.setColor(Color.DARK_GRAY);
            mensagem.getData().setScale(2);


            //capturar altura e largura da tela
            width  = Gdx.graphics.getWidth();
            height = Gdx.graphics.getHeight();

            //Posicao inicial obstaculo
            posicaoObstaculoHorizontal = width + pedraDia[0].getWidth() / 2;
            posicaoInicialVertical     = height / 2 - 400;





        }catch (Exception e){
            e.printStackTrace();
        }


	}

	@Override
	public void render () {


        try{

            if(estadoJogo == 0){

                if(Gdx.input.justTouched()){
                    estadoJogo = 1;
                }

            }

            if(estadoJogo == 1){
                deltaTime = Gdx.graphics.getDeltaTime();

                //criar o movimento do robô e cenário
                variacao  += deltaTime * 5;
                if(variacao > 3){
                    variacao = 0;
                }

                //aumenta a dificuldade
                resto = score % 2;
                if(resto == 0){
                    dificuldade = dificuldade + deltaTime * 20;
                }

                //velocidade dos obstaculos
                posicaoObstaculoHorizontal -= deltaTime * dificuldade;

                //regula a saida do obstaculo da tela
                if(posicaoObstaculoHorizontal < -pedraDia[0].getWidth()){
                    posicaoObstaculoHorizontal = width - 100;
                    marcouPonto = false;


                }

                //regula toque e pulo
                velocidadePulo++;
                if(posicaoInicialVertical <= 240 && (Gdx.input.justTouched())){

                    //subtrai -27 para o robo pular
                    velocidadePulo = -27;

                }

                if(posicaoInicialVertical > 240 || velocidadePulo < 0){
                    posicaoInicialVertical = posicaoInicialVertical - velocidadePulo;
                }

                //limitar altura do pulo
                if(posicaoInicialVertical > height /2 + 50){

                    posicaoInicialVertical = posicaoInicialVertical - velocidadePulo;
                }

                //Detectar a colisão do robo com os obstaculos
                if(Intersector.overlaps(circuloRobo, retanguloObstaculo)){
                    //Gdx.app.log("ALERTA", "BATEU ");
                    estadoJogo = 2;
                }

                //Pontuacao
                if(posicaoObstaculoHorizontal < 30){

                    if(!marcouPonto){
                        score++;
                        marcouPonto = true;
                    }
                }


                //Gdx.app.log("Posicao", "Valor "+posicaoInicialVertical);
            }else if(estadoJogo == 2){

                if(score > hiScore){
                    hiScore = score;
                }
                if(Gdx.input.justTouched()){
                    score   = 0;
                    resto   = 0;
                    estadoJogo = 1;
                    variacao = 0;
                    velocidadePulo = 0;
                    dificuldade = 250;
                    posicaoObstaculoHorizontal = width + pedraDia[0].getWidth() / 2;
                    posicaoInicialVertical     = height / 2 - 400;
                    create();
                }


            }




            //Desenho de formas para gerenciar o impacto do passáro com os canos

            circuloRobo        = new Circle();
            retanguloObstaculo = new Rectangle();


            circuloRobo.set(30 + robo[0].getWidth()/2, posicaoInicialVertical+robo[0].getHeight() / 2 - 5, robo[0].getWidth() / 2);

            retanguloObstaculo.set(posicaoObstaculoHorizontal, pedraDia[0].getHeight() / 2, 90, 90);


            //desenhar tela do jogo
            batch.begin();

            //alterar desenho do plano de fundo
            if(score > 10){

                batch.draw(fundo2[(int) variacao], 0, 0, width, height);
                batch.draw(pedraNoite[(int) variacaoRandom], posicaoObstaculoHorizontal, 240, 100, 150);
                batch.draw(robo[(int) variacao], 30, posicaoInicialVertical);
                fonteScore.draw(batch, String.valueOf(score), width / 2 + width / 3, height - height / 25 );
                fonteScore.draw(batch, "HI "+String.valueOf(hiScore), width / 2 - width / 3 , height - height / 25 );
            }else{
                batch.draw(fundo[(int) variacao], 0, 0, width, height);
                batch.draw(pedraDia[(int) variacaoRandom], posicaoObstaculoHorizontal, 240, 100, 150);
                batch.draw(robo[(int) variacao], 30, posicaoInicialVertical);
                fonteScore.draw(batch, String.valueOf(score), width / 2 + width / 3, height - height / 25 );
                fonteScore.draw(batch, "HI "+String.valueOf(hiScore), width / 2 - width / 3, height - height / 25 );
            }

            //Game Over
            if(estadoJogo == 2){

                mensagem.setColor(Color.WHITE);
                batch.draw(gameOver, 0, 0, width, height);
                mensagem.draw(batch, "Toque na tela para recomeçar o jogo.", width /2 - 230 , height / 2 - 150);

            }

            batch.end();


            //criar forma na tela para testar as funcionalidades do jogo
            /*
            shape.begin(ShapeRenderer.ShapeType.Filled);

            shape.circle(circuloRobo.x, circuloRobo.y, circuloRobo.radius);
            shape.rect(retanguloObstaculo.x, retanguloObstaculo.y, retanguloObstaculo.width, retanguloObstaculo.height);
            //shape.rect(retCanoTopo.x, retCanoTopo.y, retCanoTopo.width, retCanoTopo.height);

            shape.setColor(Color.RED);
            shape.end();
            */

        }catch (Exception e){

            e.printStackTrace();

        }

	}

}
