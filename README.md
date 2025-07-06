+++ b/README.md

+# Banco Imobiliário (console)
+
+Este projeto é um simulador em linha de comando inspirado no tradicional jogo de tabuleiro. O objetivo é administrar propriedades, coletar aluguel dos adversários e manter-se solvente até o final das rodadas.
+
+## Requisitos
+- **Java 17** ou superior
+
+## Compilação
+Crie um diretório para os binários e compile todos os arquivos do pacote:
+

+
+## Execução
+Execute a classe principal `Main` passando a pasta de binários no classpath:

+
+## Menu principal
+Ao iniciar, é exibido um menu com as seguintes opções:
+
+1. **Gerenciar Jogadores** – cadastro, listagem, remoção e edição dos participantes (mínimo de 2 e máximo de 6).
+2. **Gerenciar Imóveis** – permite criar, listar, atualizar e remover imóveis que compõem o tabuleiro (necessários pelo menos 10).
+3. **Definir Configurações da Partida** – ajuste do saldo inicial, salário por volta e número máximo de rodadas.
+4. **Iniciar Jogo** – começa a partida após configurados jogadores e imóveis.
+0. **Sair** – finaliza o programa.
+
+## Fluxo geral da partida
+Após iniciar a partida, cada rodada segue as ações abaixo:
+
+1. Cada jogador ativo realiza seu turno. Caso esteja na prisão, há um menu específico para tentar sair.
+2. No turno normal, o jogador pode lançar os dados e mover-se, consultar status, gerenciar suas propriedades, negociar com outros jogadores, ver o ranking ou desistir do jogo.
+3. Ao mover o peão, a casa correspondente executa sua ação: cobrança de aluguel, sorte/revés, prisão, imposto etc.
+4. A cada volta completa, o jogador recebe o salário definido nas configurações.
+5. O jogo termina quando atingir o número máximo de rodadas ou restar apenas um participante. O ranking final é exibido e o vencedor é aquele com maior patrimônio.
+6. Ele roda com uma Lista Circular onde o ultimo Nó sempre aponta para o inicio.
+7. Próximo passo é integrar ele com React pra deixar com uma interface amigável.
+8.Projeto feito por EdmoCunha, Jose-Edu e Bernardo.
