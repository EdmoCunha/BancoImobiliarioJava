package bancoimobiliario;

public abstract class Casa {
    protected String nome;
    protected Casa proxima;

    public Casa(String nome) { this.nome = nome; this.proxima = null; }

    public String getNome() { return nome; }

    public abstract void acao(Jogador jogador, BancoImobiliario jogo);
}


class CasaInicio extends Casa {
    public CasaInicio() { super("Início"); }
    @Override
    public void acao(Jogador jogador, BancoImobiliario jogo) {
        System.out.println("Você passou pelo início!");
        // Salário já é pago no fluxo principal
    }
}


class CasaImposto extends Casa {
    public CasaImposto() { super("Imposto"); }
    @Override
    public void acao(Jogador jogador, BancoImobiliario jogo) {
        double taxa = jogador.getPatrimonio() * 0.05;
        jogador.pagar(taxa);
        System.out.println("Você pagou R$ " + String.format("%.2f", taxa) + " de imposto!");
    }
}


class CasaRestituicao extends Casa {
    public CasaRestituicao() { super("Restituição"); }
    @Override
    public void acao(Jogador jogador, BancoImobiliario jogo) {
        double restit = jogo.getSalarioPorVolta() * 0.10;
        jogador.receber(restit);
        System.out.println("Você recebeu R$ " + String.format("%.2f", restit) + " de restituição!");
    }
}


class CasaPrisao extends Casa {
    public CasaPrisao() { super("Prisão"); }
    @Override
    public void acao(Jogador jogador, BancoImobiliario jogo) {
        jogador.entrarPrisao();
        System.out.println("Você foi para a prisão!");
    }
}


class CasaSorteReves extends Casa {
    public CasaSorteReves() { super("Sorte/Revés"); }
    @Override
    public void acao(Jogador jogador, BancoImobiliario jogo) {
        Carta carta = jogo.getBaralho().puxarCarta();
        System.out.println("Carta sorte/revés: " + carta.getDescricao());
        carta.aplicar(jogador, jogo);
    }
}


class CasaImovel extends Casa {
    private Imovel imovel;
    public CasaImovel(Imovel imovel) {
        super(imovel.getNome());
        this.imovel = imovel;
    }
    @Override
    public void acao(Jogador jogador, BancoImobiliario jogo) {
        imovel.acaoNaCasa(jogador, jogo);
    }
    public Imovel getImovel() { return imovel; }
}
