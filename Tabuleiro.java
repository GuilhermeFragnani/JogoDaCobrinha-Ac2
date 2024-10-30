package jogodacobrinha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tabuleiro extends JFrame {

    private JPanel painel;
    private JPanel menu;
    private JButton iniciarButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JTextField placarField;
    private int x, y;
    private String direcao = "direita";
    private long tempoAtualizacao = 10;
    private int incremento = 2;
    private Quadrado obstaculo, cobra;
    private int larguraTabuleiro, alturaTabuleiro;
    private int placar = 0;
    private java.util.List<Quadrado> partesCobra = new java.util.ArrayList<>();
    private boolean ressurgeNaBorda = true; // Mudar para false para encerrar o jogo ao colidir com bordas

    public Tabuleiro() {

        larguraTabuleiro = alturaTabuleiro = 800;

        cobra = new Quadrado(10, 10, Color.green);
        cobra.x = larguraTabuleiro / 2;
        cobra.y = alturaTabuleiro / 2;

        obstaculo = new Quadrado(10, 10, Color.red);
        obstaculo.x = larguraTabuleiro / 2;
        obstaculo.y = alturaTabuleiro / 2;

        setTitle("Jogo da Cobrona");
        setSize(alturaTabuleiro, larguraTabuleiro + 30);
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
                g.setColor(cobra.cor);
                for (Quadrado parte : partesCobra) {
                    g.fillRect(parte.x, parte.y, parte.altura, parte.largura);
                }

                super.paintComponent(g);
                g.setColor(cobra.cor);
                g.fillRect(cobra.x, cobra.y, cobra.altura, cobra.largura);

                g.setColor(obstaculo.cor);
                g.fillRect(obstaculo.x, obstaculo.y, obstaculo.largura, obstaculo.altura);
            }
        };

        add(menu, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);

        setVisible(true);

        // ActionListener para o botÃ£o Iniciar
        iniciarButton.addActionListener(e -> {
            Iniciar();
            painel.requestFocusInWindow(); // Devolve o foco para o painel
        });

        // ActionListener para o botÃ£o Reset
        resetButton.addActionListener(e -> {
            Reiniciar();

        });

        // ActionListener para o botÃ£o Pausar
        pauseButton.addActionListener(e -> {
            Pausar();

        });
        //...............................................................
// Movimenta o corpo da cobra
for (int i = partesCobra.size() - 1; i > 0; i--) {
    partesCobra.get(i).x = partesCobra.get(i - 1).x;
    partesCobra.get(i).y = partesCobra.get(i - 1).y;
}

// Movimenta a primeira parte para a posição da cabeça
if (!partesCobra.isEmpty()) {
    partesCobra.get(0).x = cobra.x;
    partesCobra.get(0).y = cobra.y;
}
//...........................................................................
        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                //Exemplo de uso do campo de Texto placarField
                placarField.setText("Placar: " + placar++);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!direcao.equals("direita")) {
                            direcao = "esquerda";
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!direcao.equals("esquerda")) {
                            direcao = "direita";
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (!direcao.equals("baixo")) {
                            direcao = "cima";
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!direcao.equals("cima")) {
                            direcao = "baixo";
                        }
                        break;

                }
            }
        });

        painel.setFocusable(true);
        painel.requestFocusInWindow();

    }

    private void Iniciar() {
        for (int i = partesCobra.size() - 1; i > 0; i--) {
            partesCobra.get(i).x = partesCobra.get(i - 1).x;
            partesCobra.get(i).y = partesCobra.get(i - 1).y;
        }
        if (partesCobra.size() > 0) {
            partesCobra.get(0).x = cobra.x;
            partesCobra.get(0).y = cobra.y;
        }

        if (cobra.x == obstaculo.x && cobra.y == obstaculo.y) {
            placar++;
            placarField.setText("Placar: " + placar);

            // Reposicionar maçã
            obstaculo.x = (int) (Math.random() * larguraTabuleiro / 10) * 10;
            obstaculo.y = (int) (Math.random() * alturaTabuleiro / 10) * 10;

            // Adicionar nova parte ao corpo da cobra
            Quadrado novaParte = new Quadrado(10, 10, Color.green);
            partesCobra.add(novaParte);

            // Aumenta a dificuldade ao diminuir o tempo de atualização
            tempoAtualizacao = Math.max(tempoAtualizacao - 1, 50);
        }
        if (ressurgeNaBorda) {
            if (cobra.x < 0) {
                cobra.x = larguraTabuleiro - 10;
            }
            if (cobra.x >= larguraTabuleiro) {
                cobra.x = 0;
            }
            if (cobra.y < 0) {
                cobra.y = alturaTabuleiro - 10;
            }
            if (cobra.y >= alturaTabuleiro) {
                cobra.y = 0;
            }
        } else {
            if (cobra.x < 0 || cobra.x >= larguraTabuleiro || cobra.y < 0 || cobra.y >= alturaTabuleiro) {
                JOptionPane.showMessageDialog(this, "Colisão com a borda! Jogo Encerrado", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }

        new Thread(() -> {
            while (true) {
                for (Quadrado parte : partesCobra) {
                    if (cobra.x == parte.x && cobra.y == parte.y) {
                        JOptionPane.showMessageDialog(this, "Colisão com o próprio corpo! Jogo Encerrado", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }

                try {
                    Thread.sleep(tempoAtualizacao);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                switch (direcao) {
                    case "esquerda":
                        cobra.x -= incremento;
                        break;
                    case "direita":
                        cobra.x += incremento;
                        break;
                    case "cima":
                        cobra.y -= incremento;
                        break;
                    case "baixo":
                        cobra.y += incremento;
                        break;

                }

                painel.repaint();

            }
        }).start();
    }

    private void Reiniciar() {
        // Adicione aqui a lÃ³gica para reiniciar o jogo               
        JOptionPane.showMessageDialog(this, "Jogo Reiniciado!", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Pausar() {
        //Interrompe o while(!reset) do mÃ©todo Iniciar() pausando o jogo.
        JOptionPane.showMessageDialog(this, "Jogo Pausado!", "Pause", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Tabuleiro();
    }
}
