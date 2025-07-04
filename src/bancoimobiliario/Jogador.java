package bancoimobiliario;
import java.util.*;
import bancoimobiliario.Utils;

public class Jogador {
    private String nome;
    private double saldo;
    private Casa posicao;
    private List<Imovel> propriedades;
    private boolean falido;
    private boolean preso;
    private int tentativasPrisao;
    private int passos;

    public Jogador(String nome, double saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
        this.posicao = null;
        this.propriedades = new ArrayList<>();
        this.falido = false;
        this.preso = false;
        this.tentativasPrisao = 0;
        this.passos = 0;
    }
    public void setNome(String nome) { this.nome = nome; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getNome() { return nome; }
    public double getSaldo() { return saldo; }
    public Casa getPosicao() { return posicao; }
    public void setPosicao(Casa pos) { this.posicao = pos; }
    public boolean isFalido() { return falido; }
    public void setFalido(boolean f) { this.falido = f; }
    public boolean isPreso() { return preso; }
    public int getTentativasPrisao() { return tentativasPrisao; }
    public void setTentativasPrisao(int t) { tentativasPrisao = t; }
    public void incrementarTentativasPrisao() { tentativasPrisao++; }
    public void sairPrisao() { preso = false; tentativasPrisao = 0; }
    public void entrarPrisao() { preso = true; tentativasPrisao = 0; }
    public void receber(double valor) { saldo += valor; }
    public void pagar(double valor) {
        saldo -= valor;
        if (saldo < 0) { saldo = 0; falido = true; }
    }
    public void adquirirImovel(Imovel im) { propriedades.add(im); }
    public void venderImovel(Imovel im) { propriedades.remove(im); }
    public List<Imovel> getPropriedades() { return propriedades; }

    public double getPatrimonio() {
        double soma = saldo;
        for (Imovel im : propriedades)
            if (!im.isHipotecado()) soma += im.getPreco();
        return soma;
    }

    public void mostrarStatus() {
        System.out.println("--- STATUS DE " + nome + " ---");
        System.out.printf("Saldo: R$ %.2f\n", saldo);
        System.out.printf("Patrimônio total: R$ %.2f\n", getPatrimonio());
        System.out.print("Propriedades: ");
        if (propriedades.isEmpty()) System.out.println("Nenhuma");
        else for (Imovel im : propriedades)
            System.out.print(im.getNome() + (im.isHipotecado() ? " (Hipotecada), " : ", "));
        System.out.println();
    }

    public void menuGerenciarPropriedades(Scanner scanner, Tabuleiro tabuleiro) {
        System.out.println("--- Minhas Propriedades ---");
        for (int i = 0; i < propriedades.size(); i++) {
            Imovel im = propriedades.get(i);
            System.out.printf("%d. %s - Hipotecada? %s\n", i + 1, im.getNome(), im.isHipotecado() ? "Sim" : "Não");
        }
        System.out.println("Escolha o número para hipotecar/quitar ou 0 para sair.");
        int escolha = Integer.parseInt(scanner.nextLine());
        if (escolha < 1 || escolha > propriedades.size()) return;
        Imovel im = propriedades.get(escolha - 1);
        if (!im.isHipotecado()) {
            System.out.println("Hipotecar por R$ " + im.valorHipoteca() + "? (1=Sim, 0=Não)");
            if (Integer.parseInt(scanner.nextLine()) == 1) {
                receber(im.valorHipoteca());
                im.hipotecar();
                System.out.println("Imóvel hipotecado.");
            }
        } else {
            System.out.println("Quitar hipoteca por R$ " + im.valorQuitacao() + "? (1=Sim, 0=Não)");
            if (Integer.parseInt(scanner.nextLine()) == 1 && saldo >= im.valorQuitacao()) {
                pagar(im.valorQuitacao());
                im.quitarHipoteca();
                System.out.println("Hipoteca quitada.");
            } else if (saldo < im.valorQuitacao()) {
                System.out.println("Saldo insuficiente.");
            }
        }
    }

    public void menuNegociacao(Scanner scanner, List<Jogador> jogadores, Tabuleiro tabuleiro) {
        System.out.println("--- Negociar com outro jogador ---");
        List<Jogador> disponiveis = new ArrayList<>();
        for (Jogador j : jogadores) {
            if (!j.equals(this) && !j.isFalido())
                disponiveis.add(j);
        }
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum jogador disponível para negociar.");
            return;
        }
        for (int i = 0; i < disponiveis.size(); i++)
            System.out.println((i + 1) + ". " + disponiveis.get(i).getNome());
        System.out.println("Escolha o número do jogador ou 0 para sair:");
        int escolha;
        try {
            escolha = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) { return; }
        if (escolha < 1 || escolha > disponiveis.size()) return;
        Jogador alvo = disponiveis.get(escolha - 1);

        // Oferta do proponente
        System.out.println("Sua oferta: ");
        double dinheiroOferta = 0;
        System.out.print("Quanto em dinheiro deseja oferecer? (0 para nenhum): ");
        try { dinheiroOferta = Double.parseDouble(scanner.nextLine()); } catch (Exception e) {}
        List<Imovel> imoveisOferta = selecionarImoveis(scanner, this);

        // Pedido ao alvo
        System.out.println("Pedido para " + alvo.getNome() + ":");
        double dinheiroPedido = 0;
        System.out.print("Quanto em dinheiro deseja pedir? (0 para nenhum): ");
        try { dinheiroPedido = Double.parseDouble(scanner.nextLine()); } catch (Exception e) {}
        List<Imovel> imoveisPedido = selecionarImoveis(scanner, alvo);

        // Mostra resumo da negociação
        System.out.println("Resumo da proposta:");
        System.out.println("Você oferece: R$" + dinheiroOferta + " e imóveis: " + nomesImoveis(imoveisOferta));
        System.out.println("Em troca de: R$" + dinheiroPedido + " e imóveis: " + nomesImoveis(imoveisPedido));
        System.out.println(alvo.getNome() + ", aceita a proposta? (1=Sim / 0=Não)");
        int aceitou = 0;
        try { aceitou = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
        if (aceitou != 1) {
            System.out.println("Negociação recusada.");
            return;
        }
        // Validação
        if (this.getSaldo() < dinheiroOferta) { System.out.println("Você não tem dinheiro suficiente."); return; }
        if (alvo.getSaldo() < dinheiroPedido) { System.out.println(alvo.getNome() + " não tem dinheiro suficiente."); return; }
        if (!this.getPropriedades().containsAll(imoveisOferta)) { System.out.println("Você não possui todos os imóveis ofertados."); return; }
        if (!alvo.getPropriedades().containsAll(imoveisPedido)) { System.out.println(alvo.getNome() + " não possui todos os imóveis pedidos."); return; }

        // Executa troca
        this.pagar(dinheiroOferta);
        alvo.receber(dinheiroOferta);
        alvo.pagar(dinheiroPedido);
        this.receber(dinheiroPedido);
        for (Imovel im : imoveisOferta) {
            this.venderImovel(im);
            alvo.adquirirImovel(im);
            im.setDono(alvo);
        }
        for (Imovel im : imoveisPedido) {
            alvo.venderImovel(im);
            this.adquirirImovel(im);
            im.setDono(this);
        }
        System.out.println("Negociação realizada com sucesso!");
    }
    private List<Imovel> selecionarImoveis(Scanner scanner, Jogador dono) {
        List<Imovel> todos = dono.getPropriedades();
        List<Imovel> selecionados = new ArrayList<>();
        if (todos.isEmpty()) return selecionados;
        System.out.println("Imóveis disponíveis:");
        for (int i = 0; i < todos.size(); i++)
            System.out.println((i + 1) + ". " + todos.get(i).getNome() + (todos.get(i).isHipotecado() ? " (Hipotecado)" : ""));
        System.out.println("Digite os números dos imóveis separados por vírgula (ou nada para nenhum): ");
        String linha = scanner.nextLine().trim();
        if (linha.isEmpty()) return selecionados;
        String[] nums = linha.split(",");
        for (String num : nums) {
            try {
                int idx = Integer.parseInt(num.trim()) - 1;
                if (idx >= 0 && idx < todos.size()) selecionados.add(todos.get(idx));
            } catch (Exception ignored) {}
        }
        return selecionados;
    }

    private String nomesImoveis(List<Imovel> lista) {
        if (lista == null || lista.isEmpty()) return "Nenhum";
        StringBuilder sb = new StringBuilder();
        for (Imovel im : lista) sb.append(im.getNome()).append(", ");
        return sb.substring(0, sb.length() - 2);
    }

    public void jogarDadosEMover(BancoImobiliario game) {
        int dado1 = Utils.rolarDado(), dado2 = Utils.rolarDado();
        int total = dado1 + dado2;
        System.out.printf("Você tirou %d e %d. Total: %d.\n", dado1, dado2, total);
        Casa novaCasa = game.getTabuleiro().avancar(this.getPosicao(), total);
        this.setPosicao(novaCasa);
        this.passos += total;
        novaCasa.acao(this, game);
        if (this.passos >= game.getTabuleiro().getTotalCasas()) {
            this.receber(game.gameConst.salarioPorVolta);
            System.out.println("Você recebeu salário por completar a volta!");
            this.passos = 0;
        }
    }

    public void resetPlayer(GameConst consts) {
        this.setFalido(false);
        this.sairPrisao();
        this.setSaldo(consts.saldoInicial);
        this.setPosicao(null);
        this.propriedades = new ArrayList<>();
        this.passos = 0;
    }

    public static void menuJogadores(List<Jogador> jogadores, GameConst gameConst) {
        int opcao;
        do {
            System.out.println("\n--- Menu de Jogadores ---");
            System.out.println("(Atualmente: " + jogadores.size() + "/6)");
            System.out.println("1. Cadastrar Novo Jogador");
            System.out.println("2. Listar Jogadores");
            System.out.println("3. Remover Jogador");
            System.out.println("4. Atualizar Jogador");
            System.out.println("5. Voltar");

            System.out.print(">> ");
            opcao = Utils.lerInt();
            switch (opcao) {
                case 1 -> cadastrarJogador(jogadores, gameConst);
                case 2 -> listarJogadores(jogadores);
                case 3 -> removerJogador(jogadores);
                case 4 -> atualizarJogador(jogadores);
                case 5 -> {
                    return;
                }

                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 4);
    }

    public static void cadastrarJogador(List<Jogador> jogadores, GameConst gameConst) {
        if (jogadores.size() >= 6) {
            System.out.println("Limite de jogadores atingido!");
            return;
        }
        System.out.print("Nome do novo jogador: ");
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine().trim();
        Jogador novo = new Jogador(nome, gameConst.saldoInicial);
        jogadores.add(novo);
        System.out.println("Jogador '" + nome + "' cadastrado.");
    }

    public static void listarJogadores(List<Jogador> jogadores) {
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

    public static void removerJogador(List<Jogador> jogadores) {
        listarJogadores(jogadores);
        if (jogadores.isEmpty()) return;
        System.out.print("Nome do jogador a remover: ");
        Scanner scanner = new Scanner(System.in);
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

    public static void atualizarJogador(List<Jogador> jogadores) {
        listarJogadores(jogadores);
        if (jogadores.isEmpty()) return;
        System.out.print("Nome do jogador para atualizar: ");
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine();
        Jogador j = null;
        for (Jogador jog : jogadores) if (jog.getNome().equalsIgnoreCase(nome)) j = jog;
        if (j == null) { System.out.println("Jogador não encontrado."); return; }
        System.out.print("Novo nome (enter para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) j.setNome(novoNome);
        System.out.print("Novo saldo (enter para manter): ");
        String novoSaldo = scanner.nextLine();
        if (!novoSaldo.isBlank()) j.setSaldo(Double.parseDouble(novoSaldo));
        System.out.println("Atualizado.");
    }

}
