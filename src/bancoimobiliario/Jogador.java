package bancoimobiliario;
import java.util.*;

public class Jogador {
    private String nome;
    private double saldo;
    private Casa posicao;
    private List<Imovel> propriedades;
    private boolean falido;
    private boolean preso;
    private int tentativasPrisao;

    public Jogador(String nome, double saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
        this.posicao = null;
        this.propriedades = new ArrayList<>();
        this.falido = false;
        this.preso = false;
        this.tentativasPrisao = 0;
    }

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
        for (int i = 0; i < jogadores.size(); i++) {
            if (!jogadores.get(i).equals(this) && !jogadores.get(i).isFalido())
                System.out.println((i + 1) + ". " + jogadores.get(i).getNome());
        }
        System.out.println("Escolha o número ou 0 para sair:");
        int escolha = Integer.parseInt(scanner.nextLine());
        if (escolha < 1 || escolha > jogadores.size() || jogadores.get(escolha - 1).equals(this)) return;
        Jogador alvo = jogadores.get(escolha - 1);
        // Aqui pode implementar lógica de negociação (oferecer/vender propriedades, dinheiro etc)
        System.out.println("Negociação entre " + nome + " e " + alvo.getNome() + " (funcionalidade em desenvolvimento)");
    }
}
