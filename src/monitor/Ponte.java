package monitor;

public class Ponte {

    private final int CAPACIDADE_MAXIMA = 3;
    private int carrosNaPonte = 0;
    private Sentido sentidoAtual = Sentido.NENHUM;

    public synchronized void entrarNaPonte(Carro carro) throws InterruptedException {
        Sentido sentidoCarro = carro.getSentido();

        // Ponte vazia → define sentido
        if (carrosNaPonte == 0) {
            sentidoAtual = sentidoCarro;
        }

        // Enquanto NÃO posso entrar:
        // 1. ponte cheia
        // 2. sentido oposto
        while (carrosNaPonte == CAPACIDADE_MAXIMA || sentidoAtual != sentidoCarro) {
            wait();   // libera monitor e aguarda
        }

        // Entrou
        carrosNaPonte++;
        sentidoAtual = sentidoCarro;

        System.out.println(">>> [ENTRADA] " + carro + " | Na ponte: " + carrosNaPonte);
    }

    public synchronized void sairDaPonte(Carro carro) {
        carrosNaPonte--;

        System.out.println("<<< [SAÍDA]   " + carro + " | Restantes: " + carrosNaPonte);

        // Se o último saiu → ponte fica vazia → próximo carro define sentido
        if (carrosNaPonte == 0) {
            sentidoAtual = Sentido.NENHUM;
            System.out.println("--- PONTE VAZIA ---");
        }

        notifyAll(); // acorda todos (ambos sentidos vão reavaliar o while)
    }
}
