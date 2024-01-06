import java.util.ArrayList;
import java.util.List;

public class Process {
    String name;
    String alias;

    long startTime;

    ArrayList<Phase> phases;
    int phaseCount;

    int currentQueueId;

    int penaltyPoints = 0;
    int awardPoints = 0;

    public Process(String name, String alias, long startTime, ArrayList<Phase> phases, int phaseCount) {
        this.name = name;
        this.alias = alias;
        this.startTime = startTime;
        this.phases = phases;
        this.phaseCount = phaseCount;
    }

    public void increasePenaltyPoints(){
        this.penaltyPoints++;
    }

    public void increaseAwardPoints(){
        this.awardPoints++;
    }

    public boolean isProcessDone(){
        for(int i = 0; i < this.phaseCount; i++){
            if(!phases.get(i).isDone()){
                return false;
            }
        }
        return true;
    }

    public void removeFirstPhase(){
        phases.remove(0);
    }

    public int getCurrentQueueId() {
        return currentQueueId;
    }

    public void setCurrentQueueId(int currentQueueId) {
        this.currentQueueId = currentQueueId;
    }
}
