package bancoimobiliario;
import bancoimobiliario.Utils;
import bancoimobiliario.Jogador;
import bancoimobiliario.Imovel;
import bancoimobiliario.BancoImobiliario;

public class Main {

    public static void main(String[] args) {

        int opcao;
        BancoImobiliario game = new BancoImobiliario();
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Hospital", (double) 200000, (double) 2000);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Farmácia", (double) 300000, (double) 3000);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Escola", (double) 300000, (double) 3000);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Universidade", (double) 400000, (double) 4000);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Casa", (double) 0, (double) 0);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Bordel", (double) 0, (double) 0);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Hotel", (double) 0, (double) 0);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Restaurante", (double) 0, (double) 0);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Mercado", (double) 0, (double) 0);
        Imovel.cadastrarImovelMocado(game.getTabuleiro(), "Posto de gasolina", (double) 0, (double) 0);

        do {
            System.out.println("\n=== SIMULADOR DE JOGO DE TABULEIRO ===");
            System.out.println("1. Gerenciar Jogadores");
            System.out.println("2. Gerenciar Imóveis");
            System.out.println("3. Definir Configurações da Partida");
            System.out.println("4. Iniciar Jogo");
            System.out.println("0. Sair");
            System.out.print(">> Escolha uma opção: ");
            opcao = Utils.lerInt();
            switch (opcao) {
                case 1 -> Jogador.menuJogadores(game.getJogadores(), game.gameConst);
                case 2 -> Imovel.menuImoveis(game.getTabuleiro());
                case 3 -> game.menuConfiguracoes();
                case 4 -> game.iniciarJogo();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

    }

}
