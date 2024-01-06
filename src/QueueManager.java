import java.util.*;

public class QueueManager {
    List<Process> Q1, Q2, Q3, IOQ;
    List<Process> CPU, IO;
    int queueLimit1, queueLimit2, queueLimit3;



    public QueueManager(int queueLimit1, int queueLimit2, int queueLimit3) {
        this.queueLimit1 = queueLimit1;
        this.queueLimit2 = queueLimit2;
        this.queueLimit3 = queueLimit3;

        Q1 = new LinkedList<>();
        Q2 = new LinkedList<>();
        Q3 = new LinkedList<>();
        IOQ = new LinkedList<>();

        CPU = new LinkedList<>();
        IO = new LinkedList<>();
    }

    public void addProcessToCpu(Process process){
        if(!cpuIsOccupied()){
            CPU.add(process);
        }
    }

    public void addProcessToIO(Process process){
        if(!ioIsOccupied()){
            IO.add(process);
        }
    }

    public void moveProcess(Process process, List<Process> source, List<Process> destination){
        source.remove(process);
        destination.add(process);
    }

    public void addProcessInSimulation(Process process){
        this.Q1.add(process);
    }


    public boolean cpuIsOccupied(){
        return !CPU.isEmpty();
    }

    public boolean ioIsOccupied(){
        return !CPU.isEmpty();
    }

    public List<Process> getQ1() {
        return Q1;
    }

    public List<Process> getQ2() {
        return Q2;
    }

    public List<Process> getQ3() {
        return Q3;
    }

    public List<Process> getIOQ() {
        return IOQ;
    }

    public List<Process> getCPU() {
        return CPU;
    }

    public List<Process> getIO() {
        return IO;
    }

}
