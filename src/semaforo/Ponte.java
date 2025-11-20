package semaforo;

import java.util.concurrent.Semaphore;

public class Ponte {

    private final int CAPACIDADE_MAXIMA = 3;

    private int carrosNaPonte = 0;
    private Sentido sentidoAtual = Sentido.NENHUM;

    // Semáforos básicos
    private final Semaphore mutex = new Semaphore(1);       // Protege variáveis
    private final Semaphore filaNS = new Semaphore(0);      // Espera Norte→Sul
    private final Semaphore filaSN = new Semaphore(0);      // Espera Sul→Norte

    private int esperandoNS = 0;
    private int esperandoSN = 0;

    public void entrarNaPonte(Carro carro) throws InterruptedException {
        Sentido s = carro.getSentido();

        mutex.acquire();

        // Condições para NÃO entrar:
        // 1. Ponte cheia
        // 2. Sentido oposto
        while (carrosNaPonte == CAPACIDADE_MAXIMA ||
               (sentidoAtual != Sentido.NENHUM && sentidoAtual != s)) {

            // Carro precisa esperar
            if (s == Sentido.NORTE_SUL) {
                esperandoNS++;
                mutex.release();
                filaNS.acquire();  // Dorme aqui
                mutex.acquire();
                esperandoNS--;
            } else {
                esperandoSN++;
                mutex.release();
                filaSN.acquire();  // Dorme aqui
                mutex.acquire();
                esperandoSN--;
            }
        }

        // Entrou
        carrosNaPonte++;
        sentidoAtual = s;

        System.out.println(">>> [ENTRADA] " + carro + " | Na ponte: " + carrosNaPonte);

        mutex.release();
    }

    public void sairDaPonte(Carro carro) throws InterruptedException {
        mutex.acquire();

        carrosNaPonte--;
        System.out.println("<<< [SAÍDA]   " + carro + " | Restantes: " + carrosNaPonte);

        // Se esvaziou, o sentido pode trocar
        if (carrosNaPonte == 0) {
            sentidoAtual = Sentido.NENHUM;
            
            // Fairness simples:
            // Libera TODOS do lado oposto primeiro
            if (carro.getSentido() == Sentido.NORTE_SUL && esperandoSN > 0) {
                filaSN.release(Math.min(esperandoSN, CAPACIDADE_MAXIMA));
            } else if (carro.getSentido() == Sentido.SUL_NORTE && esperandoNS > 0) {
                filaNS.release(Math.min(esperandoNS, CAPACIDADE_MAXIMA));
            }
        } else {
            // Ponte ainda tem carros → libera mais do MESMO sentido
            if (sentidoAtual == Sentido.NORTE_SUL && esperandoNS > 0) {
                filaNS.release();
            } else if (sentidoAtual == Sentido.SUL_NORTE && esperandoSN > 0) {
                filaSN.release();
            }
        }

        mutex.release();
    }
}
