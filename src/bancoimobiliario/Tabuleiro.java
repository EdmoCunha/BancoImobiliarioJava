package bancoimobiliario;
import java.util.*;

public class Tabuleiro {
    private Casa inicio;
    private int totalCasas;
    private List<Imovel> imoveis; // Para CRUD no menu

    public Tabuleiro() {
        this.inicio = null;
        this.totalCasas = 0;
        this.imoveis = new ArrayList<>();
    }

    public void montarTabuleiroPadrao() {
        // Limpa tabuleiro
        inicio = null;
        totalCasas = 0;
        // Exemplos de casas fixas (pode ajustar como quiser)
        adicionarCasa(new CasaInicio());
        adicionarCasa(new CasaImposto());
        adicionarCasa(new CasaSorteReves());
        adicionarCasa(new CasaPrisao());
        adicionarCasa(new CasaRestituicao());
        // Adiciona imóveis cadastrados pelo usuário
        for (Imovel im : imoveis) adicionarCasa(new CasaImovel(im));
        // Fecha lista circular
        fecharCircular();
    }

    public void adicionarCasa(Casa nova) {
        if (inicio == null) {
            inicio = nova;
            nova.proxima = inicio;
        } else {
            Casa temp = inicio;
            while (temp.proxima != inicio) temp = temp.proxima;
            temp.proxima = nova;
            nova.proxima = inicio;
        }
        totalCasas++;
    }

    private void fecharCircular() {
        if (inicio == null) return;
        Casa temp = inicio;
        while (temp.proxima != inicio) temp = temp.proxima;
        temp.proxima = inicio;
    }

    public Casa avancar(Casa atual, int passos) {
        Casa temp = atual;
        for (int i = 0; i < passos; i++) temp = temp.proxima;
        return temp;
    }

    public void criarImovel(String nome, double preco, double aluguel) {
        Imovel imovel = new Imovel(nome, preco, aluguel);
        imoveis.add(imovel);
    }

    public int getQtdImoveis() {
        return imoveis.size();
    }

    public void listarImoveis() {
        if (imoveis.isEmpty()) { System.out.println("Nenhum imóvel cadastrado."); return; }
        int i = 1;
        for (Imovel im : imoveis)
            System.out.printf("%d. %s - Preço: R$ %.2f - Aluguel: R$ %.2f\n", i++, im.getNome(), im.getPreco(), im.getAluguel());
    }

    public void atualizarImovel(String nome, Scanner scanner) {
        for (Imovel im : imoveis) {
            if (im.getNome().equalsIgnoreCase(nome)) {
                System.out.print("Novo preço de compra (atual: R$ " + im.getPreco() + "): ");
                im.setPreco(Double.parseDouble(scanner.nextLine()));
                System.out.print("Novo valor de aluguel (atual: R$ " + im.getAluguel() + "): ");
                im.setAluguel(Double.parseDouble(scanner.nextLine()));
                System.out.println("Imóvel atualizado.");
                return;
            }
        }
        System.out.println("Imóvel não encontrado.");
    }

    public void removerImovel(String nome) {
        Imovel remove = null;
        for (Imovel im : imoveis) if (im.getNome().equalsIgnoreCase(nome)) remove = im;
        if (remove != null) {
            imoveis.remove(remove);
            System.out.println("Removido.");
        } else {
            System.out.println("Não encontrado.");
        }
    }

    public Casa getCasaInicio() {
        return inicio;
    }
}
