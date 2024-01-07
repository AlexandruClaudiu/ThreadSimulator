public class Phase {
    int fCpuTimeCount;
    int fInOutTimeCount;
    int phaseId;
    int cpuTimeCount;
    int inOutTimeCount;
    int repeatTimes;

    public Phase(int fCpuTimeCount, int fInOutTimeCount, int phaseId, int repeatTimes) {
        this.fCpuTimeCount = fCpuTimeCount;
        this.fInOutTimeCount = fInOutTimeCount;
        this.phaseId = phaseId;
        this.cpuTimeCount = this.fCpuTimeCount;
        this.inOutTimeCount = this.fInOutTimeCount;
        this.repeatTimes = repeatTimes;
    }

    public void resetPhase(){
        if(repeatTimes > 0){
            this.cpuTimeCount = this.fCpuTimeCount;
            this.inOutTimeCount = this.fInOutTimeCount;
            this.repeatTimes--;
        }
    }


    public void decreaseCpuTimeCount(){
        this.cpuTimeCount--;
    }

    public void decreaseInOutTimeCount(){
        this.inOutTimeCount--;
    }

    public boolean isDone(){
        return ((cpuTimeCount == 0) && (inOutTimeCount == 0) && (repeatTimes == 0));
    }
}
