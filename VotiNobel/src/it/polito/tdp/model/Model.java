package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.dao.EsameDAO;

public class Model {

	private List<Esame> esami;
	private EsameDAO edao;
	private List<Esame> soluzione;
	private double bestAvg;

	public Model() {
		edao = new EsameDAO();
		esami = edao.getTuttiEsami();
		// Controllo se ho popolato correttamente
		// for(Esame e : esami) {
		// System.out.println(e);
		// }
	}

	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		// inizializzazione
		soluzione = new ArrayList<Esame>();
		bestAvg = 0.0;

		int step = 0;
		List<Esame> parziale = new ArrayList<>();
		recursive(step, parziale, numeroCrediti);

		return soluzione;
	}

	private void recursive(int step, List<Esame> parziale, int numeroCrediti) {
		/*
		 * NB: la funzione ricorsiva di solito ritorna: - void: se vogliamo la migliore
		 * soluzione possibile (tra tutte) - boolean: se vogliamo una soluzione in
		 * particolare (la prima ad esempio)
		 */

		// Debug
//		for (Esame e : parziale) {
//			System.out.print(e.getCodins() + " ");
//		}
//		System.out.println(" ");

		// Condizione di terminazione
		if (totCrediti(parziale) > numeroCrediti) {
			return;
		}
		// Controllo se ho trovato una soluzione migliore della precedente
		if (totCrediti(parziale) == numeroCrediti) {
			if (avg(parziale) > bestAvg) {
				soluzione = new ArrayList<>(parziale); // deep copy! (fotografia)
				bestAvg = avg(parziale); // aggiorno il 'bestAvg'
			}
		}
		// Generazione di una nuova soluzione parziale
		for (Esame esame : esami) {
			if (!parziale.contains(esame)) { // NB: per usare contains devo aver definito hashCode e equals !!!
				parziale.add(esame);
				recursive(step + 1, parziale, numeroCrediti);
				parziale.remove(esame);
			}
		}

	}

	private double avg(List<Esame> parziale) {
		double avg = 0;
		for (Esame e : parziale) {
			avg += e.getCrediti() * e.getVoto();
		}
		avg /= totCrediti(parziale);
		return avg;
	}

	private int totCrediti(List<Esame> parziale) {
		int somma = 0;
		for (Esame e : parziale) {
			somma += e.getCrediti();
		}
		return somma;
	}

}
