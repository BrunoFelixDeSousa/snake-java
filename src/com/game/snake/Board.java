package com.game.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private final int B_LARGURA = 300;
	private final int B_ALTURA = 300;
	private final int TAMANHO_DO_PONTO = 10;
	private final int TODOS_OS_PONTOS = 900;
	private final int POSICAO_ALEATORIA = 29;
	private final int DELAY = 140;

	private final int x[] = new int[TODOS_OS_PONTOS];
	private final int y[] = new int[TODOS_OS_PONTOS];

	private int pontos;
	private int posicao_maca_x;
	private int posicao_maca_y;

	private boolean direcao_para_esquerda = false;
	private boolean direcao_para_direita = true;
	private boolean direcao_para_cima = false;
	private boolean direcao_para_baixo = true;
	private boolean noJogo = true;

	private Timer timer;
	private Image bola;
	private Image maca;
	private Image cabeca;

	public Board() {
		initBoard();
	}

	private void initBoard() {
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);

		setPreferredSize(new Dimension(B_ALTURA, B_LARGURA));
		carregarImagens();
		iniciarJogo();
	}

	private void carregarImagens() {
		ImageIcon iid = new ImageIcon("src/com/game/resource/dot.png");
		bola = iid.getImage();

		ImageIcon iia = new ImageIcon("src/com/game/resource/apple.png");
		maca = iia.getImage();

		ImageIcon iih = new ImageIcon("src/com/game/resource/head.png");
		cabeca = iih.getImage();
	}

	private void iniciarJogo() {
		pontos = 3;

		for (int i = 0; i < pontos; i++) {
			x[i] = 50 - i * 10;
			y[i] = 50;
		}

		localDaMaca();

		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		desenhar(g);
	}

	private void desenhar(Graphics g) {
		if (noJogo) {
			g.drawImage(maca, posicao_maca_x, posicao_maca_y, this);

			for (int i = 0; i < pontos; i++) {
				if (i == 0) {
					g.drawImage(cabeca, x[i], y[i], this);
				} else {
					g.drawImage(bola, x[i], y[i], this);
				}
			}
			Toolkit.getDefaultToolkit().sync();
		} else {
			gameOver(g);
		}
	}

	private void gameOver(Graphics g) {
		String mensagem = "Gamer Over!!!";
		Font fonte = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(fonte);

		g.setColor(Color.white);
		g.setFont(fonte);
		g.drawString(mensagem, (B_ALTURA - metr.stringWidth(mensagem)) / 2, B_LARGURA / 2);

	}

	private void checarMaca() {
		if ((x[0] == posicao_maca_x) && (y[0] == posicao_maca_y)) {
			pontos++;
			localDaMaca();
		}
	}

	private void mover() {
		for (int i = pontos; i > 0; i--) {
			x[i] = x[(i - 1)];
			y[i] = y[(i - 1)];
		}

		if (direcao_para_esquerda) {
			x[0] -= TAMANHO_DO_PONTO;
		}

		if (direcao_para_direita) {
			x[0] += TAMANHO_DO_PONTO;
		}

		if (direcao_para_cima) {
			y[0] -= TAMANHO_DO_PONTO;
		}

		if (direcao_para_baixo) {
			y[0] += TAMANHO_DO_PONTO;
		}
	}

	private void checarColisao() {

		for (int i = pontos; i > 0; i--) {

			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
				noJogo = false;
			}
		}

		if (y[0] >= B_ALTURA) {
			noJogo = false;
		}

		if (y[0] < 0) {
			noJogo = false;
		}

		if (x[0] >= B_LARGURA) {
			noJogo = false;
		}

		if (x[0] < 0) {
			noJogo = false;
		}

		if (!noJogo) {
			timer.stop();
		}
	}
	
	private void localDaMaca() {
		int r = (int) (Math.random() * POSICAO_ALEATORIA);
        posicao_maca_x = ((r * TAMANHO_DO_PONTO));

        r = (int) (Math.random() * POSICAO_ALEATORIA);
        posicao_maca_y = ((r * TAMANHO_DO_PONTO));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (noJogo) {

            checarMaca();
            checarColisao();
            mover();
        }

        repaint();

	}
	
	private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!direcao_para_direita)) {
            	direcao_para_esquerda = true;
            	direcao_para_cima = false;
            	direcao_para_baixo = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!direcao_para_esquerda)) {
            	direcao_para_direita = true;
            	direcao_para_cima = false;
            	direcao_para_baixo = false;
            }

            if ((key == KeyEvent.VK_UP) && (!direcao_para_baixo)) {
            	direcao_para_cima = true;
            	direcao_para_direita = false;
            	direcao_para_esquerda = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!direcao_para_direita)) {
            	direcao_para_baixo = true;
            	direcao_para_direita = false;
            	direcao_para_esquerda = false;
            }
        }
    }

}
