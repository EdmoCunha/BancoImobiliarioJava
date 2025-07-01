package bancoimobiliario;
import java.util.*;
import bancoimobiliario.Utils;

public class BancoImobiliario {
    private Tabuleiro tabuleiro;
    private List<Jogador> jogadores;
    private Baralho baralho;
    private RankingJogadores ranking;
    private Scanner scanner;
    public GameConst gameConst;


    public BancoImobiliario() {
        tabuleiro = new Tabuleiro();
        jogadores = new ArrayList<>();
        baralho = new Baralho();
        ranking = new RankingJogadores();
        scanner = new Scanner(System.in);
        gameConst = new GameConst();
        gameConst.saldoInicial = 25000;
        gameConst.salarioPorVolta = 2000;
        gameConst.maxRodadas = 20;
        gameConst.rodadaAtual = 1;
    }

    public void menuConfiguracoes() {
        int opcao;
        do {
            System.out.println("--- Configurações da Partida ---");
            System.out.println("1. Definir Saldo Inicial (Atual: R$ " + gameConst.saldoInicial + ")");
            System.out.println("2. Definir Salário por volta (Atual: R$ " + gameConst.salarioPorVolta + ")");
            System.out.println("3. Definir Nº Máximo de Rodadas (Atual: " + gameConst.maxRodadas + ")");
            System.out.println("4. Voltar");
            System.out.print(">> ");
            opcao = Utils.lerInt();
            switch (opcao) {
                case 1 -> { System.out.print("Novo saldo inicial: "); gameConst.saldoInicial = Utils.lerInt(); }
                case 2 -> { System.out.print("Novo salário por volta: "); gameConst.salarioPorVolta = Utils.lerInt(); }
                case 3 -> { System.out.print("Novo nº de rodadas: "); gameConst.maxRodadas = Utils.lerInt(); }
                case 4 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 4);
    }

    public void iniciarJogo() {
        if (tabuleiro.getQtdImoveis() < 10) {
            System.out.println("ERRO: Mínimo de 10 imóveis necessários.");
            return;
        }
        if (jogadores.size() < 2) {
            System.out.println("ERRO: Mínimo de 2 jogadores necessários.");
            return;
        }
        tabuleiro.montarTabuleiroPadrao();
        baralho.embaralhar();
        for (Jogador j : jogadores) j.setPosicao(tabuleiro.getCasaInicio());
        gameConst.rodadaAtual = 1;
        while (gameConst.rodadaAtual <= gameConst.maxRodadas && jogadores.stream().filter(j -> !j.isFalido()).count() > 1) {
            for (Jogador jogador : jogadores) {
                if (!jogador.isFalido()) {
                    executarTurno(jogador);
                }
            }
            ranking.reconstruir(jogadores);
            gameConst.rodadaAtual++;
        }
        encerrarJogo();
    }

    public void executarTurno(Jogador jogador) {
        System.out.println("\n=== RODADA " + gameConst.rodadaAtual + "/" + gameConst.maxRodadas + " - VEZ DE: " + jogador.getNome() + " ===");
        if (jogador.isPreso()) {
            menuPrisao(jogador);
            return;
        }
        int opcao;
        do {
            System.out.println("--- O que deseja fazer? ---");
            System.out.println("1. Lançar Dados e Mover");
            System.out.println("2. Ver Status");
            System.out.println("3. Gerenciar Propriedades");
            System.out.println("4. Propor Negociação");
            System.out.println("5. Ver Ranking");
            System.out.println("0. Desistir do Jogo");
            System.out.print(">> ");
            opcao = Utils.lerInt();
            switch (opcao) {
                case 1 -> {jogador.jogarDadosEMover(this); return; }
                case 2 -> jogador.mostrarStatus();
                case 3 -> jogador.menuGerenciarPropriedades(scanner, tabuleiro);
                case 4 -> jogador.menuNegociacao(scanner, jogadores, tabuleiro);
                case 5 -> ranking.exibirRanking();
                case 0 -> jogador.setFalido(true);
                default -> System.out.println("Opção inválida.");
            }
        } while (true);
    }

    public void menuPrisao(Jogador jogador) {
        System.out.println("Você está na prisão! (" + (jogador.getTentativasPrisao() + 1) + "ª tentativa)");
        System.out.println("1. Tentar a sorte (dados duplos)");
        System.out.println("2. Passar a vez");
        int opcao = Utils.lerInt();
        if (opcao == 1) {
            int d1 = Utils.rolarDado(), d2 = Utils.rolarDado();
            System.out.println("Você tirou: " + d1 + " e " + d2);
            if (d1 == d2) {
                System.out.println("Dados duplos! Você está livre.");
                jogador.sairPrisao();
                jogador.setTentativasPrisao(0);
                jogador.jogarDadosEMover(this);
            } else {
                jogador.incrementarTentativasPrisao();
                if (jogador.getTentativasPrisao() >= 3) {
                    System.out.println("3 tentativas sem sucesso. Você está livre mas perde o turno!");
                    jogador.sairPrisao();
                    jogador.setTentativasPrisao(0);
                } else {
                    System.out.println("Você permanece na prisão.");
                }
            }
        } else {
            jogador.incrementarTentativasPrisao();
            if (jogador.getTentativasPrisao() >= 3) {
                System.out.println("3 tentativas sem sucesso. Você está livre mas perde o turno!");
                jogador.sairPrisao();
                jogador.setTentativasPrisao(0);
            }
        }
    }

    public void encerrarJogo() {
        System.out.println("\n====== FIM DE JOGO ======");
        ranking.reconstruir(jogadores);
        ranking.exibirRanking();
        Jogador vencedor = ranking.getVencedor();
        System.out.println("VENCEDOR(A): " + vencedor.getNome() + " - Patrimônio: R$ " + vencedor.getPatrimonio());
    }

    // --- Getters para uso em outras classes ---
    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public Baralho getBaralho() { return baralho; }
    public int getSalarioPorVolta() { return gameConst.salarioPorVolta; }
    public List<Jogador> getJogadores() {
        return jogadores;
    }

}
