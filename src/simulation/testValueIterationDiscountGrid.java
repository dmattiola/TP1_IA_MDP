package simulation;

import javax.swing.SwingUtilities;

import vueGridworld.VueGridworldValue;
import agent.planningagent.ValueIterationAgent;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;

public class testValueIterationDiscountGrid {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                GridworldMDP gmdp = GridworldMDP.getDiscountGrid();
		GridworldEnvironnement g = new GridworldEnvironnement(gmdp);
                ValueIterationAgent a = new ValueIterationAgent(gmdp);
		VueGridworldValue vue = new VueGridworldValue(g,a);
		vue.setVisible(true);
            }
        });
    }
    // 1 : gamma = 0.9 // bruit = 0.2 // autre rec = -4
    // 2 : gamma = 0.9 // bruit = 0.0 // autre rec = 0.0
    // 3 : gamma = 0.35 // bruit = 0.2 // autre rec = 0.0
    // 4
    // 5 : gamma = 0.9 // bruit = 0.2 // autre rec > 1
    
}
