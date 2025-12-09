/**
 * Classe con il main, che avvia l'app
 * che rappresenta il flusso dei clienti di un ufficio postale
 * messi in attesa da un totem elettronico che assegna
 * un numero progressivo e stampa il ticket.
 * Clienti gestiti da un solo sportello
 * @author frida
 * @version 1.0
 */

import java.time.Duration;
import java.time.LocalTime;

public class SimulatorePoste {

    public static void main(String[] args) {

        final int cost = 3;   // costo per cliente

        ListaClienti listaClienti = new ListaClienti();

        Thread arriviThread = new Thread(new GestoreArrivi(listaClienti));
        Thread sportelloThread1 = new Thread(new Sportello(listaClienti, "Marzia"));
        Thread sportelloThread2 = new Thread(new Sportello(listaClienti, "Cinzia"));

        // Ora di apertura
        LocalTime startTime = LocalTime.now();
        System.out.println("Posta aperta alle: " + startTime);

        // Avvio dei thread
        arriviThread.start();
        sportelloThread1.start();
        sportelloThread2.start();

        try {
            // Attendiamo la terminazione dei thread
            arriviThread.join();
            sportelloThread1.interrupt();
            sportelloThread2.interrupt();
            sportelloThread1.join();
            sportelloThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ora di chiusura
        LocalTime endTime = LocalTime.now();

        // Calcolo durata apertura
        Duration durata = Duration.between(startTime, endTime);
        long minuti = durata.toMinutes();
        long ore = minuti / 60;
        long minutiResidui = minuti % 60;

        int serviti = listaClienti.getUltimoServito();
        int ricavo = serviti * cost;

        System.out.println("          RISULTATI");
        System.out.println("Clienti serviti: " + serviti);
        System.out.println("Ricavo totale: " + ricavo + " €");
        System.out.println("Tempo di apertura: " + ore + "h " + minutiResidui + "m");
        System.out.println("Apertura: " + startTime);
        System.out.println("Chiusura: " + endTime);
    }
}
