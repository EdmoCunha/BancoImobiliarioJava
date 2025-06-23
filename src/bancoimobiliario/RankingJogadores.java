package bancoimobiliario;
import java.util.*;

public class RankingJogadores {
    private NoBST raiz;
    private List<Jogador> rankingOrdenado;

    public void reconstruir(List<Jogador> jogadores) {
        raiz = null;
        for (Jogador j : jogadores) {
            if (!j.isFalido()) inserir(j);
        }
        rankingOrdenado = new ArrayList<>();
        ordemInversa(raiz, rankingOrdenado);
    }

    public void inserir(Jogador jogador) {
        raiz = inserirRec(raiz, jogador);
    }

    private NoBST inserirRec(NoBST no, Jogador jogador) {
        if (no == null) return new NoBST(jogador);
        if (jogador.getPatrimonio() < no.jogador.getPatrimonio())
            no.esquerda = inserirRec(no.esquerda, jogador);
        else
            no.direita = inserirRec(no.direita, jogador);
        return no;
    }

    private void ordemInversa(NoBST no, List<Jogador> lista) {
        if (no == null) return;
        ordemInversa(no.direita, lista);
        lista.add(no.jogador);
        ordemInversa(no.esquerda, lista);
    }

    public void exibirRanking() {
        if (rankingOrdenado == null || rankingOrdenado.isEmpty()) {
            System.out.println("Ranking vazio.");
            return;
        }
        System.out.println("--- RANKING ATUAL ---");
        int pos = 1;
        for (Jogador j : rankingOrdenado) {
            System.out.printf("%d. %s - Patrimônio: R$ %.2f %s\n", pos++, j.getNome(), j.getPatrimonio(),
                    j.isFalido() ? "(Falido)" : "");
        }
    }

    public Jogador getVencedor() {
        if (rankingOrdenado == null || rankingOrdenado.isEmpty()) return null;
        return rankingOrdenado.get(0);
    }
}

class NoBST {
    Jogador jogador;
    NoBST esquerda, direita;
    public NoBST(Jogador jogador) { this.jogador = jogador; }
}
