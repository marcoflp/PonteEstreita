package semaforo;

/**
 * Classe que representa o Carro (Thread).
 */

import java.util.Random;
/**
 *
 * @author 20231PF.CC0023
 */
public class Carro extends Thread {
    
    private int id;
    private Sentido sentido;
    private Ponte ponte;
    private static final Random random = new Random();

    public Carro(int id, Sentido sentido, Ponte ponte) {
        this.id = id;
        this.sentido = sentido;
        this.ponte = ponte;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(1000)); // Tempo até chegar
            
            System.out.println(this + " querendo entrar.");
            ponte.entrarNaPonte(this);
            
            Thread.sleep(1000 + random.nextInt(2000)); // Atravessando
            
            ponte.sairDaPonte(this);
            
        } catch (InterruptedException e) {
            System.out.println("Carro " + id + " interrompido.");
        }
    }

    public Sentido getSentido() { return sentido; }

    @Override
    public String toString() { return "Carro #" + id + " (" + sentido + ")"; }
}
