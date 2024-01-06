import java.util.LinkedList;

public class Simulator {
    long time = 0;
    QueueManager queueManager;

    LinkedList<Process> processes;

    int k, r;

    public Simulator(QueueManager cpuQueueManager) {
        this.queueManager = cpuQueueManager;
    }



    public void run(){
        while(!allProcessesDone()){
            for(int i = 0; i < processes.size(); i++){
                Process tmpProcess = processes.get(i);
                if(tmpProcess.startTime == time){
                    if(!queueManager.cpuIsOccupied()){
                        queueManager.addProcessToCpu(tmpProcess);
                    } else {
                        if(queueManager.getCPU().get(0).currentQueueId > 1){
                            returnProcessInItsOwnQueueAtStart(queueManager.getCPU().get(0));
                            queueManager.getCPU().remove(0);
                            queueManager.getCPU().add(tmpProcess);
                        } else{
                            queueManager.addProcessInSimulation(tmpProcess);
                        }
                    }
                    tmpProcess.setCurrentQueueId(1);
                }
            }
            if(queueManager.cpuIsOccupied() && queueManager.getCPU().get(0).isProcessDone()){
                queueManager.getCPU().remove(0);
            }
            else if(queueManager.cpuIsOccupied()){
                Process currentProcess = queueManager.getCPU().get(0);
                Phase currentPhase = currentProcess.phases.get(0);
                if(currentPhase.cpuTimeCount > 0){
                    if(currentPhase.cpuTimeAllowed > 0){
                        currentPhase.decreaseCpuTimeCount();
                        currentPhase.cpuTimeAllowed--;
                    } else{
                        queueManager.getCPU().remove(0);
                        currentProcess.penaltyPoints++;
                        if(currentProcess.penaltyPoints == k){
                            retrogradeProcess(currentProcess);
                            currentProcess.penaltyPoints = 0;
                        } else {
                            returnProcessInItsOwnQueue(currentProcess);
                        }
                    }

                }
                else if(currentPhase.cpuTimeCount == 0){
                    if(currentPhase.cpuTimeAllowed > 0){
                        currentProcess.increaseAwardPoints();
                        if(currentProcess.awardPoints == r){
                            advanceProcess(currentProcess);
                            currentProcess.awardPoints = 0;
                        }
                    }
                    queueManager.getCPU().remove(0);
                    if(!queueManager.ioIsOccupied()){
                        queueManager.getIO().add(currentProcess);
                    } else{
                        queueManager.getIOQ().add(currentProcess);
                    }
                }
            } else {
                if(!queueManager.Q1.isEmpty()){
                    queueManager.CPU.add(queueManager.Q1.get(0));
                    queueManager.Q1.remove(0);
                } else if(!queueManager.Q2.isEmpty()){
                    queueManager.CPU.add(queueManager.Q2.get(0));
                    queueManager.Q2.remove(0);
                } else if(!queueManager.Q3.isEmpty()){
                    queueManager.CPU.add(queueManager.Q3.get(0));
                    queueManager.Q3.remove(0);
                }
            }

            if(queueManager.ioIsOccupied()){
                Process currentProcess = queueManager.getIO().get(0);
                Phase currentPhase = currentProcess.phases.get(0);
                if(!currentPhase.isDone()){
                    if(currentPhase.inOutTimeCount > 0){
                        currentPhase.decreaseInOutTimeCount();
                    }else{
                        if(currentPhase.repeatTimes > 0){
                            currentPhase.resetPhase();
                            if(currentProcess.currentQueueId > queueManager.getCPU().get(0).getCurrentQueueId()){
                                returnProcessInItsOwnQueueAtStart(queueManager.getCPU().get(0));
                                queueManager.getCPU().remove(0);
                                queueManager.getCPU().add(currentProcess);
                            } else {
                                returnProcessInItsOwnQueue(currentProcess);
                            }
                        }
                        else{
                            if(currentProcess.currentQueueId > queueManager.getCPU().get(0).getCurrentQueueId()){
                                returnProcessInItsOwnQueueAtStart(queueManager.getCPU().get(0));
                                queueManager.getCPU().remove(0);
                                queueManager.getCPU().add(currentProcess);
                            } else {
                                returnProcessInItsOwnQueue(currentProcess);
                            }
                        }
                    }
                } else{
                    if(!queueManager.getIOQ().isEmpty()){
                        queueManager.getIOQ().add(queueManager.getIOQ().get(0));
                        queueManager.getIOQ().remove(0);
                    }
                }
            }
            time++;
        }
    }

    public boolean allProcessesDone(){
        for(int i = 0; i < processes.size(); i++){
            if(!processes.get(i).isProcessDone()){
                return false;
            }
        }
        return true;
    }

    public void returnProcessInItsOwnQueue(Process process){
        if(process.getCurrentQueueId() == 1){
            queueManager.Q1.add(process);
        }else if(process.getCurrentQueueId() == 2){
            queueManager.Q2.add(process);
        } else if(process.getCurrentQueueId() == 3){
            queueManager.Q3.add(process);
        }
    }

    public void returnProcessInItsOwnQueueAtStart(Process process){
        if(process.getCurrentQueueId() == 2){
            queueManager.Q2.add(0, process);
        } else if(process.getCurrentQueueId() == 3){
            queueManager.Q3.add(0, process);
        }
    }

    public void advanceProcess(Process process){
        if(process.getCurrentQueueId() == 3){
            queueManager.moveProcess(process, queueManager.Q3, queueManager.Q2);
            process.currentQueueId = 2;
        }else if(process.getCurrentQueueId() == 2){
            queueManager.moveProcess(process, queueManager.Q2, queueManager.Q1);
            process.currentQueueId = 1;
        }
    }

    public void retrogradeProcess(Process process){
        if(process.getCurrentQueueId() == 1){
            queueManager.moveProcess(process, queueManager.Q1, queueManager.Q2);
            process.currentQueueId = 2;
        }else if(process.getCurrentQueueId() == 2){
            queueManager.moveProcess(process, queueManager.Q2, queueManager.Q3);
            process.currentQueueId = 3;
        }
    }
}
