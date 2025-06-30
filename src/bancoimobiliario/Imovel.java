package bancoimobiliario;

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
            System.out.println("Este imóvel não tem proprietário.");
            System.out.println("Preço: R$ " + preco + " | Aluguel: R$ " + aluguel);
            System.out.println("1. Comprar | 2. Passar");
            int op = jogo.lerInt();
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
}
