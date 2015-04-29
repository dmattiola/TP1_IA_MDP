package agent.planningagent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import environnement.gridworld.ActionGridworld;
import util.HashMapUtil;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	
    //*** VOTRE CODE
    private double gamma;
    private HashMapUtil v;

    public ValueIterationAgent(double gamma,MDP mdp) {
        super(mdp);
	//*** VOTRE CODE
        this.v = new HashMapUtil();
        for (Etat e : mdp.getEtatsAccessibles()) {
            this.v.put(e, 0.0);
        }
        this.gamma = gamma;
    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9,mdp);
    }
	
    /* Mise a jour de V: effectue UNE iteration de value iteration */
    @Override
    public void updateV(){
        this.delta=0.0;
	//*** VOTRE CODE
        HashMapUtil v2 = (HashMapUtil) this.v.clone();
	for(Etat s : mdp.getEtatsAccessibles()){
            double max = Integer.MIN_VALUE;
            for(Action a : mdp.getActionsPossibles(s)){
                double somme = 0.0;
                try {
                    Map<Etat, Double> m = mdp.getEtatTransitionProba(s, a);
                    for(Etat e : m.keySet()){
                        somme += m.get(e) * (mdp.getRecompense(s, a, e) + gamma * v2.get(e));
                    }
                } catch (Exception ex) {
                    System.out.println("Erreur : "+ex.getMessage());
                }
                if (somme > max){
                    max = somme;
                }
            }
            this.v.put(s, max);
        }
		
        // mise a jour vmax et vmin pour affichage
        double max = Integer.MIN_VALUE;
        double min = Integer.MAX_VALUE;
        for(Double val : this.v.values()){
            if(val < min){
                min = val;
            }
            if (val > max){
                max = val;
            }
        }
        super.vmax = max;
        super.vmin = min;    
        
        // MAJ delta
        double _max = Integer.MIN_VALUE;
        for (Etat e : this.v.keySet()) {
            if (Math.abs(this.v.get(e) - v2.get(e)) > _max){
                _max = Math.abs(this.v.get(e) - v2.get(e));
            }
        }
        super.delta = _max;

        //******************* a laisser a la fin de la methode
        this.notifyObs();
    }
	
    /* renvoi l'action donnee par la politique */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
	ArrayList<Action> listAction = getPolitique(e);
        if (listAction.isEmpty()){
            return ActionGridworld.NONE;
        } else if (listAction.size() == 1){
            return listAction.get(0);
        } else{
            int random = new Random().nextInt(listAction.size());
            return listAction.get(random);
        }
    }
        
    @Override
    public double getValeur(Etat _e) {
        //*** VOTRE CODE
        if (this.v.containsKey(_e)){
            return this.v.get(_e);
        } else {
            return 0.0;
        }
    }

    /* renvoi action(s) de plus forte(s) valeur(s) dans etat (plusieurs actions sont renvoyees si valeurs identiques, 
    liste vide si aucune action n'est possible) */
    @Override
    public ArrayList<Action> getPolitique(Etat _e) {
	ArrayList<Action> listAction = new ArrayList<>();
	//*** VOTRE CODE
        double max = Integer.MIN_VALUE;
        for(Action a : mdp.getActionsPossibles(_e)) {
            double somme = 0.0;
            try {
                Map<Etat, Double> m = mdp.getEtatTransitionProba(_e, a);
                for(Etat e : m.keySet()) {
                    somme += m.get(e) * (mdp.getRecompense(_e, a, e) + gamma * getValeur(e));
                }
            } catch (Exception ex) {
                System.out.println("Erreur : "+ex.getMessage());
            }
            if(somme > max){
                max = somme;
                listAction.clear();
                listAction.add(a);
            } else if(somme == max){
                listAction.add(a);
            }
        }
        return listAction;	
    }
	
    @Override
    public void reset() {
        super.reset();
	//*** VOTRE CODE
        this.gamma = 0.9;
        this.v = new HashMapUtil();
        for (Etat e : mdp.getEtatsAccessibles()){
            this.v.put(e,0.0);
        }
	/*-----------------*/
	this.notifyObs();
    }

    @Override
    public void setGamma(double gamma) {
        //*** VOTRE CODE
	this.gamma = gamma;
    }

}
