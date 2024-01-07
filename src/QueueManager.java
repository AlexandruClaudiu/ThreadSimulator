import java.util.*;

public class QueueManager {
    List<Process> Q1, Q2, Q3, IOQ;
    List<Process> CPU, IO;
    int queueLimit1, queueLimit2, queueLimit3;



    public QueueManager(int queueLimit1, int queueLimit2, int queueLimit3) {
        this.queueLimit1 = queueLimit1;
        this.queueLimit2 = queueLimit2;
        this.queueLimit3 = queueLimit3;

        Q1 = new ArrayList<>();
        Q2 = new ArrayList<>();
        Q3 = new ArrayList<>();
        IOQ = new ArrayList<>();

        CPU = new ArrayList<>();
        IO = new ArrayList<>();
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
        return !IO.isEmpty();
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

    public String getInfo(){
        String cpu = "-";
        if(!CPU.isEmpty()){
            cpu = String.valueOf(CPU.get(0).alias);
        }
        String io = "-";
        if(!IO.isEmpty()){
            io = String.valueOf(IO.get(0).alias);
        }

        String q1 = "Q1: ";
        if(!Q1.isEmpty()){
            for(int i = 0; i < Q1.size(); i++){
                q1 += Q1.get(i).alias + " ";
            }
        } else {
            q1 += "-";
        }

        String q2 = "Q2: ";
        if(!Q2.isEmpty()){
            for(int i = 0; i < Q2.size(); i++){
                q2 += Q2.get(i).alias + " ";
            }
        } else {
            q2 += "-";
        }

        String q3 = "Q3: ";
        if(!Q3.isEmpty()){
            for(int i = 0; i < Q3.size(); i++){
                q3 += Q3.get(i).alias + " ";
            }
        } else {
            q3 += "-";
        }

        String qio = "QIO: ";
        if(!IOQ.isEmpty()){
            for(int i = 0; i < IOQ.size(); i++){
                qio += String.valueOf(IOQ.get(i).alias) + " ";
            }
        } else {
           qio += "-";
        }
        return cpu + " " + io + " " + q1 + " " + q2 + " " + q3 + " " + qio;
    }

}
