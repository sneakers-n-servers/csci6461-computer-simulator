package edu.gw.csci.simulator.cpu;

import edu.gw.csci.simulator.isa.instructions.LoadStore;
import edu.gw.csci.simulator.utils.FloatingPointsCalculate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PipeLine {
    private static final Logger LOGGER = LogManager.getLogger(PipeLine.class);
    private FloatingPointsCalculate[] floatingPointsCalculates;
    private int clock;
    public PipeLine(FloatingPointsCalculate[] floatingPointsCalculates,int clock){
        this.floatingPointsCalculates = floatingPointsCalculates;
        this.clock = clock;
    }
    public void pipeline(){
        for (int i = 0; i < clock ; i++) {
            System.out.println("————————————————————————————————");
            String mess = String.format("clock: %d",i+1);
            LOGGER.debug(mess);
            FloatingPointsCalculate.Match match = null;
            FloatingPointsCalculate.Calculate calculate = null;
            FloatingPointsCalculate.Normalize normalize = null;
            FloatingPointsCalculate.Result result = null;

            if(i<floatingPointsCalculates.length){
                match = floatingPointsCalculates[i].new Match();
                LOGGER.debug(match.getName());
            }

            if(i>=1 && i<floatingPointsCalculates.length+1){
                calculate = floatingPointsCalculates[i-1].new Calculate(match);
                LOGGER.debug(calculate.getName());
            }

            if(i>=2 && i<floatingPointsCalculates.length+2){
                normalize = floatingPointsCalculates[i-2].new Normalize(calculate);
                LOGGER.debug(normalize.getName());
            }

            if(i>=3 && i<floatingPointsCalculates.length+3){
                result = floatingPointsCalculates[i-3].new Result(normalize);
                LOGGER.debug(result.getName());
            }
        }

    }
}
