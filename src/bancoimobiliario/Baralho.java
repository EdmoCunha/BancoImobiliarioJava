package bancoimobiliario;
import java.util.*;

public class Baralho {
    private Stack<Carta> pilha;
    private List<Carta> cartasBase;

    public Baralho() {
        cartasBase = new ArrayList<>();
        pilha = new Stack<>();
        montarCartas();
        embaralhar();
    }

    public void montarCartas() {
        // TODO AQUI TEM QUE ADICIONAR MAIS CARTAS, ACHO QUE 16 MAS TO COM SONO E NÃO SEI JOGAR DIREITO
        cartasBase.add(new Carta("Avance até o início.", (j, jogo) -> {
            j.setPosicao(jogo.getTabuleiro().getCasaInicio());
            System.out.println("Você foi para o início!");
        }));
        cartasBase.add(new Carta("Ganhe R$ 5000.", (j, jogo) -> {
            j.receber(5000);
        }));
        cartasBase.add(new Carta("Pague R$ 2000.", (j, jogo) -> {
            j.pagar(2000);
        }));
        cartasBase.add(new Carta("Vá para a prisão.", (j, jogo) -> {
            j.entrarPrisao();
        }));

    }

    public void embaralhar() {
        pilha.clear();
        List<Carta> temp = new ArrayList<>(cartasBase);
        Collections.shuffle(temp);
        pilha.addAll(temp);
    }

    public Carta puxarCarta() {
        if (pilha.isEmpty()) embaralhar();
        return pilha.pop();
    }
}

class Carta {
    private String descricao;
    private CartaAcao acao;

    public Carta(String descricao, CartaAcao acao) {
        this.descricao = descricao;
        this.acao = acao;
    }
    public String getDescricao() { return descricao; }
    public void aplicar(Jogador jogador, BancoImobiliario jogo) { acao.executar(jogador, jogo); }
}

interface CartaAcao {
    void executar(Jogador jogador, BancoImobiliario jogo);
}

