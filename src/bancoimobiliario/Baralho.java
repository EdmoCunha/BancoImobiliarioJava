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
        cartasBase.add(new Carta("Avance até o início.", (j, jogo) -> {
            j.setPosicao(jogo.getTabuleiro().getCasaInicio());
            System.out.println("Você foi para o início!");
        }));
        cartasBase.add(new Carta("Ganhe R$ 5000.", (j, jogo) -> {
            j.receber(5000);
            System.out.println("Você ganhou R$ 5000!");
        }));
        cartasBase.add(new Carta("Pague R$ 2000.", (j, jogo) -> {
            j.pagar(2000);
            System.out.println("Você pagou R$ 2000!");
        }));
        cartasBase.add(new Carta("Vá para a prisão.", (j, jogo) -> {
            j.entrarPrisao();
            System.out.println("Você foi para a prisão!");
        }));
        cartasBase.add(new Carta("Ganhe R$ 1000 de cada jogador.", (j, jogo) -> {
            for (Jogador outro : jogo.getJogadores())
                if (!outro.equals(j) && !outro.isFalido()) {
                    outro.pagar(1000);
                    j.receber(1000);
                }
            System.out.println("Você ganhou R$ 1000 de cada jogador!");
        }));
        cartasBase.add(new Carta("Pague R$ 800 a cada jogador.", (j, jogo) -> {
            for (Jogador outro : jogo.getJogadores())
                if (!outro.equals(j) && !outro.isFalido()) {
                    outro.receber(800);
                    j.pagar(800);
                }
            System.out.println("Você pagou R$ 800 para cada jogador!");
        }));
        cartasBase.add(new Carta("Ganhe um imóvel grátis! Escolha pelo nome.", (j, jogo) -> {
            jogo.getTabuleiro().listarImoveis();
            System.out.print("Digite o nome do imóvel desejado: ");
            Scanner sc = new Scanner(System.in);
            String nome = sc.nextLine();
            for (Imovel im : jogo.getTabuleiro().getImoveis())
            {
                if (im.getDono() == null && im.getNome().equalsIgnoreCase(nome)) {
                    im.setDono(j);
                    j.adquirirImovel(im);
                    System.out.println("Você ganhou " + nome + "!");
                    return;
                }
            }
            System.out.println("Imóvel não encontrado ou já tem dono.");
        }));
        cartasBase.add(new Carta("Pague R$ 1500 por reformas.", (j, jogo) -> {
            j.pagar(1500);
            System.out.println("Você pagou R$ 1500 em reformas.");
        }));
        cartasBase.add(new Carta("Avance 4 casas.", (j, jogo) -> {
            Casa nova = jogo.getTabuleiro().avancar(j.getPosicao(), 4);
            j.setPosicao(nova);
            nova.acao(j, jogo);
        }));
        cartasBase.add(new Carta("Volte 3 casas.", (j, jogo) -> {
            Casa temp = j.getPosicao();
            for (int i = 0; i < 3; i++) {
                Casa prev = temp;
                while (prev.proxima != temp) prev = prev.proxima;
                temp = prev;
            }
            j.setPosicao(temp);
            temp.acao(j, jogo);
        }));
        cartasBase.add(new Carta("Ganhe R$ 1000.", (j, jogo) -> {
            j.receber(1000);
            System.out.println("Você ganhou R$ 1000!");
        }));
        cartasBase.add(new Carta("Pague R$ 700 ao banco.", (j, jogo) -> {
            j.pagar(700);
            System.out.println("Você pagou R$ 700 ao banco.");
        }));
        cartasBase.add(new Carta("Perdeu o celular, pague R$ 900.", (j, jogo) -> {
            j.pagar(900);
            System.out.println("Você pagou R$ 900.");
        }));
        cartasBase.add(new Carta("Receba restituição dobrada da próxima vez.", (j, jogo) -> {
            j.receber(jogo.getSalarioPorVolta() * 0.20);
            System.out.println("Você recebeu uma restituição dobrada!");
        }));
        cartasBase.add(new Carta("Sorte! Jogue novamente.", (j, jogo) -> {
            j.jogarDadosEMover(jogo);
        }));
        cartasBase.add(new Carta("Desconto! Na próxima compra, pague metade.", (j, jogo) -> {
            j.receber(0.5 * jogo.getSalarioPorVolta());
            System.out.println("Você recebeu desconto de metade do salário.");
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

