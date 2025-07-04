package bancoimobiliario;
import bancoimobiliario.Utils;

import java.util.List;
import java.util.Scanner;

public class Imovel {
    private String nome;
    private double preco;
    private double aluguel;
    private Jogador dono;
    private boolean hipotecado;

    public Imovel(String nome, double preco, double aluguel) {
        this.nome = nome;
        this.preco = preco;
        this.aluguel = aluguel;
        this.dono = null;
        this.hipotecado = false;
    }

    public void setNome(String nome) { this.nome = nome; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public double getAluguel() { return aluguel; }
    public Jogador getDono() { return dono; }
    public boolean isHipotecado() { return hipotecado; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setAluguel(double aluguel) { this.aluguel = aluguel; }
    public void setDono(Jogador j) { this.dono = j; }

    public void acaoNaCasa(Jogador jogador, BancoImobiliario jogo) {
        if (dono == null) {
            System.out.println("Imóvel: "+this.nome);
            System.out.println("Este imóvel não tem proprietário.");
            jogador.mostrarStatus();
            System.out.println("Preço: R$ " + preco + " | Aluguel: R$ " + aluguel);
            System.out.println("1. Comprar | 2. Passar");
            int op = Utils.lerInt();
            if (op == 1 && jogador.getSaldo() >= preco) {
                jogador.pagar(preco);
                this.dono = jogador;
                jogador.adquirirImovel(this);
                System.out.println("Você comprou " + nome + "!");
            } else if (op == 1) {
                System.out.println("Saldo insuficiente!");
            }
        } else if (dono != jogador && !hipotecado) {
            System.out.println("Este imóvel pertence a " + dono.getNome() + ".");
            System.out.println("Você deve pagar aluguel de R$ " + aluguel + ".");
            jogador.pagar(aluguel);
            dono.receber(aluguel);
        } else if (hipotecado) {
            System.out.println("Imóvel hipotecado, não há cobrança de aluguel.");
        } else {
            System.out.println("Você caiu no seu próprio imóvel.");
        }
    }

    public double valorHipoteca() { return preco * 0.5; }
    public double valorQuitacao() { return preco * 0.55; }

    public void hipotecar() { hipotecado = true; }
    public void quitarHipoteca() { hipotecado = false; }

    public static void menuImoveis(Tabuleiro tabuleiro) {
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
            opcao = Utils.lerInt();
            switch (opcao) {
                case 1 -> cadastrarImovel(tabuleiro);
                case 2 -> tabuleiro.listarImoveis();
                case 3 -> atualizarImovel(tabuleiro);
                case 4 -> removerImovel(tabuleiro);
                case 5 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 5);
    }

    public static void cadastrarImovel(Tabuleiro tabuleiro) {
        if (tabuleiro.getQtdImoveis() >= 40) {
            System.out.println("Limite de imóveis atingido!");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nome do imóvel: ");
        String nome = scanner.nextLine();
        System.out.print("Preço de compra: ");
        double preco = Utils.lerDouble();
        System.out.print("Valor do aluguel: ");
        double aluguel = Utils.lerDouble();
        tabuleiro.criarImovel(nome, preco, aluguel);
        System.out.println("Imóvel cadastrado.");
    }

    public static void cadastrarImovelMocado(Tabuleiro tabuleiro, String nome, Double preco, double aluguel) {
        if (tabuleiro.getQtdImoveis() >= 40) {
            System.out.println("Limite de imóveis atingido!");
            return;
        }
        tabuleiro.criarImovel(nome, preco, aluguel);
    }

    public static void atualizarImovel(Tabuleiro tabuleiro) {
        tabuleiro.listarImoveis();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do imóvel para atualizar: ");
        String nome = scanner.nextLine();
        tabuleiro.atualizarImovel(nome, scanner);
    }

    public static void removerImovel(Tabuleiro tabuleiro) {
        tabuleiro.listarImoveis();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do imóvel para remover: ");
        String nome = scanner.nextLine();
        tabuleiro.removerImovel(nome);
    }


}
