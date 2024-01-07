import java.util.ArrayList;

public class Simulator {
    long time = 0;
    QueueManager queueManager;

    ArrayList<Process> processes;

    int k, r;

    public Simulator(QueueManager cpuQueueManager, ArrayList<Process> processes, int k, int r) {
        this.queueManager = cpuQueueManager;
        this.processes = processes;
        this.k = k;
        this.r = r;
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
                    tmpProcess.setCpuTimeAllowed(queueManager.queueLimit1);

                }


            }

            if(queueManager.cpuIsOccupied()){
                if(queueManager.getCPU().get(0).isProcessDone()){
                    System.out.println("Process " + queueManager.getCPU().get(0).alias + " is done");
                    queueManager.getCPU().remove(0);
                }
                else {
                    Process currentProcess = queueManager.getCPU().get(0);
                    Phase currentPhase = currentProcess.phases.get(0);
                    if(currentPhase.cpuTimeCount > 0){
                        if(currentProcess.cpuTimeAllowed > 0){
                            currentPhase.cpuTimeCount--;
                            currentProcess.cpuTimeAllowed--;
                        } else{
                            currentProcess.penaltyPoints++;
                            if(currentProcess.penaltyPoints == k){
                                retrogradeProcess(currentProcess);
                                currentProcess.penaltyPoints = 0;
                            } else{
                                returnProcessInItsOwnQueue(currentProcess);
                            }
                            queueManager.getCPU().remove(0);
                        }
                    } else{
                        if(currentProcess.cpuTimeAllowed > 0){
                            currentProcess.awardPoints++;
                        }
                        if(!queueManager.ioIsOccupied()){
                            queueManager.IO.add(currentProcess);
                        } else{
                            queueManager.IOQ.add(currentProcess);
                        }
                        queueManager.getCPU().remove(0);
                    }
                }
            }

            if(!queueManager.cpuIsOccupied()){
                if(!queueManager.Q1.isEmpty()){
                    queueManager.CPU.add(queueManager.Q1.get(0));
                    if(!queueManager.Q1.get(0).isProcessDone()){
                        queueManager.Q1.get(0).cpuTimeAllowed--;
                        queueManager.Q1.get(0).phases.get(0).cpuTimeCount--;
                    }
                    queueManager.Q1.remove(0);
                } else if(!queueManager.Q2.isEmpty()){
                    queueManager.CPU.add(queueManager.Q2.get(0));
                    if(!queueManager.Q2.get(0).isProcessDone()){
                        queueManager.Q2.get(0).cpuTimeAllowed--;
                        queueManager.Q2.get(0).phases.get(0).cpuTimeCount--;
                    }
                    queueManager.Q2.remove(0);
                } else if(!queueManager.Q3.isEmpty()){
                    queueManager.CPU.add(queueManager.Q3.get(0));
                    if(!queueManager.Q3.get(0).isProcessDone()){
                        queueManager.Q3.get(0).cpuTimeAllowed--;
                        queueManager.Q3.get(0).phases.get(0).cpuTimeCount--;
                    }
                    queueManager.Q3.remove(0);
                }
            }

            if(queueManager.ioIsOccupied()){
                Process currentProcess = queueManager.getIO().get(0);
                Phase currentPhase = currentProcess.phases.get(0);
                if(currentPhase.inOutTimeCount > 0){
                    currentPhase.inOutTimeCount--;
                }
                else{
                    if(currentPhase.repeatTimes > 0){
                        currentPhase.resetPhase();
                    }
                    else{
                        if(currentProcess.phases.size() > 0){
                            System.out.println( "Phase " + currentPhase.phaseId +" is done!");
                            currentProcess.removeFirstPhase();
                        }
                    }
                    if(currentProcess.awardPoints == r){
                        advanceProcess(currentProcess);
                        if(queueManager.cpuIsOccupied()){
                            if(currentProcess.currentQueueId > queueManager.CPU.get(0).currentQueueId){
                                returnProcessInItsOwnQueueAtStart(queueManager.CPU.get(0));
                                queueManager.CPU.remove(0);
                                removeProcessFromItsOwnQueue(currentProcess);
                                queueManager.CPU.add(currentProcess);
                            }
                        }

                        currentProcess.awardPoints = 0;
                    }
                    else{
                        returnProcessInItsOwnQueue(currentProcess);
                    }
                    queueManager.IO.remove(0);
                }
            }
            if(!queueManager.ioIsOccupied()){
                if(!queueManager.IOQ.isEmpty()){
                    queueManager.IO.add(queueManager.IOQ.get(0));
                    queueManager.IOQ.get(0).phases.get(0).inOutTimeCount--;
                    queueManager.IOQ.remove(0);
                }
            }
            System.out.println(String.valueOf(time) + " " + queueManager.getInfo());
            time++;

            if(time == 1230){
                break;
            }
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

    public void removeProcessFromItsOwnQueue(Process process){
        if(process.currentQueueId == 1){
            queueManager.Q1.remove(process);
        } else if(process.currentQueueId == 2){
            queueManager.Q2.remove(process);
        } else if(process.currentQueueId == 3){
            queueManager.Q3.remove(process);
        }
    }

    public void returnProcessInItsOwnQueue(Process process){
        if(process.getCurrentQueueId() == 1){
            queueManager.Q1.add(process);
            process.setCpuTimeAllowed(queueManager.queueLimit1);
        }else if(process.getCurrentQueueId() == 2){
            queueManager.Q2.add(process);
            process.setCpuTimeAllowed(queueManager.queueLimit2);
        } else if(process.getCurrentQueueId() == 3){
            queueManager.Q3.add(process);
            process.setCpuTimeAllowed(queueManager.queueLimit3);
        }
    }

    public void returnProcessInItsOwnQueueAtStart(Process process){
        if(process.getCurrentQueueId() == 2){
            queueManager.Q2.add(0, process);
            process.setCpuTimeAllowed(queueManager.queueLimit2);
        } else if(process.getCurrentQueueId() == 3){
            queueManager.Q3.add(0, process);
            process.setCpuTimeAllowed(queueManager.queueLimit3);
        }
    }

    public void advanceProcess(Process process){
        if(process.getCurrentQueueId() == 3){
            queueManager.moveProcess(process, queueManager.Q3, queueManager.Q2);
            process.currentQueueId = 2;
            process.setCpuTimeAllowed(queueManager.queueLimit2);
        }else if(process.getCurrentQueueId() == 2){
            queueManager.moveProcess(process, queueManager.Q2, queueManager.Q1);
            process.currentQueueId = 1;
            process.setCpuTimeAllowed(queueManager.queueLimit1);
        }
    }

    public void retrogradeProcess(Process process){
        if(process.getCurrentQueueId() == 1){
            queueManager.moveProcess(process, queueManager.Q1, queueManager.Q2);
            process.currentQueueId = 2;
            process.setCpuTimeAllowed(queueManager.queueLimit2);
        }else if(process.getCurrentQueueId() == 2){
            queueManager.moveProcess(process, queueManager.Q2, queueManager.Q3);
            process.currentQueueId = 3;
            process.setCpuTimeAllowed(queueManager.queueLimit3);
        }
    }
}
