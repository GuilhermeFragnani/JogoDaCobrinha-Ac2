#Snake Game Ac2

## Guilherme Fragnani Pereira - 237057  
## Daniel Carro de Andrade - 237010 

## Atributos:
painelJogo: JPanel onde o jogo será desenhado e exibido.
btnIniciar, btnReiniciar, btnPausar: Botões de controle do jogo para iniciar, reiniciar e pausar o jogo.
labelPlacar: JLabel que exibe o placar atual do jogo.
placar: Variável inteira que mantém o valor do placar do jogo.
LARGURA_JOGO, ALTURA_JOGO, TAMANHO_BLOCO: Constantes que definem as dimensões do jogo e o tamanho de cada bloco da cobra.
cobra: Lista de pontos (ArrayList<Point>) que representa a posição de cada segmento da cobra.
maca: Point que representa a posição da maçã no jogo.
direcao: String que indica a direção atual do movimento da cobra ("CIMA", "BAIXO", "ESQUERDA", "DIREITA").
rodando: Booleano que indica se o jogo está em execução.
timer: Timer que controla a atualização periódica do jogo.
## Métodos:
Tabuleiro(): Construtor da classe. Configura a janela do jogo, inicializa os componentes do painel de controle e de jogo, adiciona os listeners para os botões e teclas de direção, configura o timer do jogo e inicializa as variáveis do jogo.
resetJogo(): Reseta o estado do jogo. Inicializa a cobra com um ponto, define a direção inicial, zera o placar, atualiza a interface do placar e gera uma nova posição para a maçã.
iniciarJogo(): Inicia o jogo. Define rodando como true e inicia o timer para começar a atualizar o jogo periodicamente. Também garante que a janela capture as teclas de direção.
pausarJogo(): Pausa o jogo. Define rodando como false e para o timer, interrompendo as atualizações do jogo.
reiniciarJogo(): Reinicia o jogo. Chama resetJogo() para resetar o estado do jogo e reinicia o jogo chamando iniciarJogo().
atualizarPlacar(): Atualiza a interface do placar exibido no labelPlacar com o valor atual do placar.
gerarNovaMaca(): Gera uma nova posição aleatória para a maçã dentro dos limites do jogo. Usa a classe Random para calcular as coordenadas.
mudarDirecao(KeyEvent e): Muda a direção da cobra com base na tecla pressionada pelo usuário. As direções possíveis são "CIMA", "BAIXO", "ESQUERDA" e "DIREITA".
atualizarJogo(): Atualiza o estado do jogo. Move a cobra na direção atual, verifica colisões com as bordas ou com o próprio corpo, adiciona a nova posição da cabeça da cobra, verifica se a cobra comeu a maçã, e se sim, aumenta o placar e gera uma nova maçã, caso contrário, remove o último segmento da cobra. Revalida e redesenha o painel de jogo.
desenhar(Graphics g): Desenha a cobra e a maçã no painel de jogo. Desenha cada segmento da cobra e a maçã na posição atual. Também desenha uma borda em torno do tabuleiro.
main(String[] args): Método principal que inicia a aplicação. Cria uma instância da classe Tabuleiro e torna a janela visível.

Esses métodos e atributos juntos formam a estrutura do jogo da cobrinha, permitindo que ele seja configurado, controlado e atualizado dinamicamente com a interação do usuário.
