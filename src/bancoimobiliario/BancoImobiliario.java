package bancoimobiliario;
import java.util.*;

public class BancoImobiliario {
    private Tabuleiro tabuleiro;
    private List<Jogador> jogadores;
    private Baralho baralho;
    private RankingJogadores ranking;
    private Scanner scanner;
    private int saldoInicial;
    private int salarioPorVolta;
    private int maxRodadas;
    private int rodadaAtual;

    public BancoImobiliario() {
        tabuleiro = new Tabuleiro();
        jogadores = new ArrayList<>();
        baralho = new Baralho();
        ranking = new RankingJogadores();
        scanner = new Scanner(System.in);
        saldoInicial = 25000;
        salarioPorVolta = 2000;
        maxRodadas = 20;
        rodadaAtual = 1;
    }

    public static void main(String[] args) {
        new BancoImobiliario().menuPrincipal();
    }

    public void menuPrincipal() {
        int opcao;
        do {
            System.out.println("\n=== SIMULADOR DE JOGO DE TABULEIRO ===");
            System.out.println("1. Gerenciar Jogadores");
            System.out.println("2. Gerenciar Imóveis");
            System.out.println("3. Definir Configurações da Partida");
            System.out.println("4. Iniciar Jogo");
            System.out.println("0. Sair");
            System.out.print(">> Escolha uma opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> menuJogadores();
                case 2 -> menuImoveis();
                case 3 -> menuConfiguracoes();
                case 4 -> iniciarJogo();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // TODO  ----------------- MENU DE JOGADORES -----------------
    public void menuJogadores() {
        int opcao;
        do {
            System.out.println("\n--- Menu de Jogadores ---");
            System.out.println("(Atualmente: " + jogadores.size() + "/6)");
            System.out.println("1. Cadastrar Novo Jogador");
            System.out.println("2. Listar Jogadores");
            System.out.println("3. Remover Jogador");
            System.out.println("4. Voltar");
            System.out.print(">> ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrarJogador();
                case 2 -> listarJogadores();
                case 3 -> removerJogador();
                case 4 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 4);
    }

    public void cadastrarJogador() {
        if (jogadores.size() >= 6) {
            System.out.println("Limite de jogadores atingido!");
            return;
        }
        System.out.print("Nome do novo jogador: ");
        String nome = scanner.nextLine().trim();
        Jogador novo = new Jogador(nome, saldoInicial);
        jogadores.add(novo);
        System.out.println("Jogador '" + nome + "' cadastrado.");
    }

    public void listarJogadores() {
        if (jogadores.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
            return;
        }
        System.out.println("--- Jogadores ---");
        int i = 1;
        for (Jogador j : jogadores) {
            System.out.printf("%d. %s - Saldo: R$ %.2f\n", i++, j.getNome(), j.getSaldo());
        }
    }

    public void removerJogador() {
        listarJogadores();
        if (jogadores.isEmpty()) return;
        System.out.print("Nome do jogador a remover: ");
        String nome = scanner.nextLine();
        Jogador remove = null;
        for (Jogador j : jogadores) if (j.getNome().equalsIgnoreCase(nome)) remove = j;
        if (remove != null) {
            jogadores.remove(remove);
            System.out.println("Removido.");
        } else {
            System.out.println("Não encontrado.");
        }
    }

    // TODO ----------------- MENU DE IMÓVEIS -----------------
    public void menuImoveis() {
        int opcao;
        do {
            System.out.println("\n--- Menu de Imóveis ---");
            System.out.println("(Atualmente: " + tabuleiro.getQtdImoveis() + "/40)");
            System.out.println("1. Cadastrar Novo Imóvel");
            System.out.println("2. Listar Imóveis");
            System.out.println("3. Atualizar Imóvel");
            System.out.println("4. Remover Imóvel");
            System.out.println("5. Voltar");
            System.out.print(">> ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> cadastrarImovel();
                case 2 -> tabuleiro.listarImoveis();
                case 3 -> atualizarImovel();
                case 4 -> removerImovel();
                case 5 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 5);
    }

    public void cadastrarImovel() {
        if (tabuleiro.getQtdImoveis() >= 40) {
            System.out.println("Limite de imóveis atingido!");
            return;
        }
        System.out.print("Nome do imóvel: ");
        String nome = scanner.nextLine();
        System.out.print("Preço de compra: ");
        double preco = lerDouble();
        System.out.print("Valor do aluguel: ");
        double aluguel = lerDouble();
        tabuleiro.criarImovel(nome, preco, aluguel);
        System.out.println("Imóvel cadastrado.");
    }

    public void atualizarImovel() {
        tabuleiro.listarImoveis();
        System.out.print("Digite o nome do imóvel para atualizar: ");
        String nome = scanner.nextLine();
        tabuleiro.atualizarImovel(nome, scanner);
    }

    public void removerImovel() {
        tabuleiro.listarImoveis();
        System.out.print("Digite o nome do imóvel para remover: ");
        String nome = scanner.nextLine();
        tabuleiro.removerImovel(nome);
    }


    public void menuConfiguracoes() {
        int opcao;
        do {
            System.out.println("--- Configurações da Partida ---");
            System.out.println("1. Definir Saldo Inicial (Atual: R$ " + saldoInicial + ")");
            System.out.println("2. Definir Salário por volta (Atual: R$ " + salarioPorVolta + ")");
            System.out.println("3. Definir Nº Máximo de Rodadas (Atual: " + maxRodadas + ")");
            System.out.println("4. Voltar");
            System.out.print(">> ");
            opcao = lerInt();
            switch (opcao) {
                case 1 -> { System.out.print("Novo saldo inicial: "); saldoInicial = lerInt(); }
                case 2 -> { System.out.print("Novo salário por volta: "); salarioPorVolta = lerInt(); }
                case 3 -> { System.out.print("Novo nº de rodadas: "); maxRodadas = lerInt(); }
                case 4 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 4);
    }

    // TODO  ----------------- INICIAR JOGO -----------------
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
        rodadaAtual = 1;
        while (rodadaAtual <= maxRodadas && jogadores.stream().filter(j -> !j.isFalido()).count() > 1) {
            for (Jogador jogador : jogadores) {
                if (!jogador.isFalido()) {
                    executarTurno(jogador);
                }
            }
            ranking.reconstruir(jogadores);
            rodadaAtual++;
        }
        encerrarJogo();
    }

    public void executarTurno(Jogador jogador) {
        System.out.println("\n=== RODADA " + rodadaAtual + "/" + maxRodadas + " - VEZ DE: " + jogador.getNome() + " ===");
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
            opcao = lerInt();
            switch (opcao) {
                case 1 -> { jogarDadosEMover(jogador); return; }
                case 2 -> jogador.mostrarStatus();
                case 3 -> jogador.menuGerenciarPropriedades(scanner, tabuleiro);
                case 4 -> jogador.menuNegociacao(scanner, jogadores, tabuleiro);
                case 5 -> ranking.exibirRanking();
                case 0 -> jogador.setFalido(true);
                default -> System.out.println("Opção inválida.");
            }
        } while (true);
    }

    public void jogarDadosEMover(Jogador jogador) {
        int dado1 = rolarDado(), dado2 = rolarDado();
        int total = dado1 + dado2;
        System.out.printf("Você tirou %d e %d. Total: %d.\n", dado1, dado2, total);
        Casa novaCasa = tabuleiro.avancar(jogador.getPosicao(), total);
        jogador.setPosicao(novaCasa);
        novaCasa.acao(jogador, this);
        if (novaCasa instanceof CasaInicio) {
            jogador.receber(salarioPorVolta);
            System.out.println("Você recebeu salário por completar a volta!");
        }
    }

    public void menuPrisao(Jogador jogador) {
        System.out.println("Você está na prisão! (" + (jogador.getTentativasPrisao() + 1) + "ª tentativa)");
        System.out.println("1. Tentar a sorte (dados duplos)");
        System.out.println("2. Passar a vez");
        int opcao = lerInt();
        if (opcao == 1) {
            int d1 = rolarDado(), d2 = rolarDado();
            System.out.println("Você tirou: " + d1 + " e " + d2);
            if (d1 == d2) {
                System.out.println("Dados duplos! Você está livre.");
                jogador.sairPrisao();
                jogador.setTentativasPrisao(0);
                jogarDadosEMover(jogador);
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

    private int rolarDado() {
        return 1 + new Random().nextInt(6);
    }

    int lerInt() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (Exception e) { System.out.print("Inválido, tente de novo: "); }
        }
    }
    private double lerDouble() {
        while (true) {
            try { return Double.parseDouble(scanner.nextLine().replace(",", ".").trim()); }
            catch (Exception e) { System.out.print("Inválido, tente de novo: "); }
        }
    }
    // --- Getters para uso em outras classes ---
    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public Baralho getBaralho() { return baralho; }
    public int getSalarioPorVolta() { return salarioPorVolta; }

}
