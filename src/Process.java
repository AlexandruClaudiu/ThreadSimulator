import java.util.ArrayList;
import java.util.List;

public class Process {
    String name;
    char alias;

    long startTime;

    ArrayList<Phase> phases;
    int phaseCount;

    int currentQueueId;
    int cpuTimeAllowed;

    int penaltyPoints = 0;
    int awardPoints = 0;

    public Process(String name, char alias, long startTime, ArrayList<Phase> phases, int phaseCount) {
        this.name = name;
        this.startTime = startTime;
        this.phases = phases;
        this.phaseCount = phaseCount;
        this.alias = alias;
    }

    public void increasePenaltyPoints(){
        this.penaltyPoints++;
    }

    public void increaseAwardPoints(){
        this.awardPoints++;
    }

    public boolean isProcessDone(){
        for(int i = 0; i < phases.size(); i++){
            if(!phases.get(i).isDone()){
                return false;
            }
        }
        return true;
    }

    public void setAlias(char alias){
        this.alias = alias;
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

    public void setCpuTimeAllowed(int timeAllowed){
        this.cpuTimeAllowed = timeAllowed;
    }
}
