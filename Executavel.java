package jogodacobrinha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Executavel extends JFrame {

    private JPanel painel;
    private JPanel menu;
    private JButton iniciarButton, resetButton, pauseButton;
    private JTextField placarField;
    private ArrayList<Quadrado> cobra;
    private Quadrado maca;
    private int larguraTabuleiro, alturaTabuleiro;
    private int incremento = 10;
    private String direcao = "direita";
    private boolean emExecucao = false;
    private boolean pausado = false;
    private Timer timer;
    private int velocidade = 150; // Velocidade inicial
    private int placar = 0;
    private boolean jogoEncerrado = false;
    private boolean modoBordas = true; // true = colisão, false = ressurge do outro lado

    public Executavel() {
        larguraTabuleiro = alturaTabuleiro = 500;

        setTitle("Jogo da Cobrinha");
        setSize(larguraTabuleiro, alturaTabuleiro + 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new JPanel();
        menu.setLayout(new FlowLayout());

        iniciarButton = new JButton("Iniciar");
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        placarField = new JTextField("Placar: 0");
        placarField.setEditable(false);

        menu.add(iniciarButton);
        menu.add(resetButton);
        menu.add(pauseButton);
        menu.add(placarField);

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Quadrado parteCobra : cobra) {
                    g.setColor(parteCobra.cor);
                    g.fillRect(parteCobra.x, parteCobra.y, parteCobra.largura, parteCobra.altura);
                }
                g.setColor(maca.cor);
                g.fillRect(maca.x, maca.y, maca.largura, maca.altura);
            }
        };

        add(menu, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);
        painel.setFocusable(true);
        painel.requestFocusInWindow();

        iniciarButton.addActionListener(e -> iniciarJogo());
        resetButton.addActionListener(e -> reiniciarJogo());
        pauseButton.addActionListener(e -> pausarJogo());

        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!jogoEncerrado && !pausado) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            if (!direcao.equals("direita")) direcao = "esquerda";
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (!direcao.equals("esquerda")) direcao = "direita";
                            break;
                        case KeyEvent.VK_UP:
                            if (!direcao.equals("baixo")) direcao = "cima";
                            break;
                        case KeyEvent.VK_DOWN:
                            if (!direcao.equals("cima")) direcao = "baixo";
                            break;
                        case KeyEvent.VK_M: // Alternar modo de bordas
                            modoBordas = !modoBordas;
                            break;
                    }
                }
            }
        });
    }

    private void iniciarJogo() {
        if (!emExecucao) {
            emExecucao = true;
            pausado = false;
            jogoEncerrado = false;
            cobra = new ArrayList<>();
            cobra.add(new Quadrado(incremento, incremento, Color.green));
            cobra.get(0).x = larguraTabuleiro / 2;
            cobra.get(0).y = alturaTabuleiro / 2;
            gerarMaca();

            timer = new Timer(velocidade, e -> atualizarJogo());
            timer.start();
        }
    }

    private void atualizarJogo() {
        moverCobra();
        verificarColisoes();
        verificarMaca();
        painel.repaint();
    }

    private void moverCobra() {
        if (!emExecucao || pausado) return;

        Quadrado cabeca = cobra.get(0);
        int novaX = cabeca.x;
        int novaY = cabeca.y;

        switch (direcao) {
            case "esquerda":
                novaX -= incremento;
                break;
            case "direita":
                novaX += incremento;
                break;
            case "cima":
                novaY -= incremento;
                break;
            case "baixo":
                novaY += incremento;
                break;
        }

        if (modoBordas) {
            if (novaX < 0 || novaX >= larguraTabuleiro || novaY < 0 || novaY >= alturaTabuleiro) {
                encerrarJogo();
                return;
            }
        } else {
            if (novaX < 0) novaX = larguraTabuleiro - incremento;
            else if (novaX >= larguraTabuleiro) novaX = 0;
            if (novaY < 0) novaY = alturaTabuleiro - incremento;
            else if (novaY >= alturaTabuleiro) novaY = 0;
        }

        moverCobraSegmentos(novaX, novaY);
    }

    private void moverCobraSegmentos(int novaX, int novaY) {
        for (int i = cobra.size() - 1; i > 0; i--) {
            cobra.get(i).x = cobra.get(i - 1).x;
            cobra.get(i).y = cobra.get(i - 1).y;
        }
        cobra.get(0).x = novaX;
        cobra.get(0).y = novaY;
    }

    private void verificarColisoes() {
        Quadrado cabeca = cobra.get(0);

        for (int i = 1; i < cobra.size(); i++) {
            if (cabeca.x == cobra.get(i).x && cabeca.y == cobra.get(i).y) {
                encerrarJogo();
            }
        }
    }

    private void verificarMaca() {
        Quadrado cabeca = cobra.get(0);

        if (cabeca.x == maca.x && cabeca.y == maca.y) {
            crescerCobra();
            gerarMaca();
            placar += 10;
            placarField.setText("Placar: " + placar);
            aumentarDificuldade();
        }
    }

    private void gerarMaca() {
        Random rand = new Random();
        int x = rand.nextInt(larguraTabuleiro / incremento) * incremento;
        int y = rand.nextInt(alturaTabuleiro / incremento) * incremento;
        maca = new Quadrado(incremento, incremento, Color.red);
        maca.x = x;
        maca.y = y;
    }

    private void crescerCobra() {
        Quadrado cauda = cobra.get(cobra.size() - 1);
        cobra.add(new Quadrado(incremento, incremento, Color.green));
        cobra.get(cobra.size() - 1).x = cauda.x;
        cobra.get(cobra.size() - 1).y = cauda.y;
    }

    private void aumentarDificuldade() {
        velocidade = Math.max(50, velocidade - 10);
        timer.setDelay(velocidade);
    }

    private void pausarJogo() {
        if (emExecucao) {
            pausado = !pausado;
            if (pausado) {
                timer.stop();
            } else {
                timer.start();
            }
        }
    }

    private void encerrarJogo() {
        emExecucao = false;
        pausado = false;
        jogoEncerrado = true;
        timer.stop();
        JOptionPane.showMessageDialog(this, "Jogo Encerrado! Pontuação: " + placar, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reiniciarJogo() {
        if (emExecucao) {
            emExecucao = false;
            pausado = false;
            jogoEncerrado = false;
            placar = 0;
            velocidade = 150;
            placarField.setText("Placar: 0");
            timer.stop();
            iniciarJogo();
        }
    }

    public static void main(String[] args) {
        new Tabuleiro();
    }
}
