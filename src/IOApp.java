import java.awt.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class IOApp extends Frame {
    String path, dir;
    URL classFile;


    public static void main(String args[]) {
        new IOApp();
    }

    IOApp() {
        Dimension res = getToolkit().getScreenSize();
        setBackground(new Color(38, 104, 165));
        setForeground(new Color(255, 255, 0));
        setResizable(false);
        adaugaMenuBar();
        setTitle("Exemplu");
        resize(400, 400);
        move((int) ((res.width - 400) / 2), (int) ((res.height - 400) / 2));
        show();
    }

    void adaugaMenuBar() {
        MenuBar men = new MenuBar();
        Menu f = new Menu("File");
        f.add("Open");
        f.add("-");
        f.add("Exit");
        men.add(f);
        setMenuBar(men);
    }

    public boolean handleEvent(Event e) {
        if (e.id == Event.WINDOW_DESTROY) {
            System.exit(0);
        } else if (e.id == Event.ACTION_EVENT && e.target instanceof MenuItem) {
            if ("Exit".equals(e.arg)) {
                System.exit(0);
            } else if ("Open".equals(e.arg)) {
                loadFile();

                return true;
            }
        } else return false;
        return super.handleEvent(e);
    }


    void loadFile() {
        try {
            FileDialog fd = new FileDialog(this, "Open File", 0);
            if (dir != null) fd.setDirectory(dir);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                dir = fd.getDirectory();
                String fisier = fd.getFile();
                path = dir + fisier;
                try {
                    classFile = new URL("file:/" + path);
                } catch (MalformedURLException e) {
                }
                Simulator simulator = fileRead(classFile);
                simulator.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Simulator fileRead(URL classFile) {
        int q1 = 0, q2 = 0, q3 = 0, k = 0, r = 0;
        List<Process> processes = new ArrayList<>();
        try {
            URLConnection connection = classFile.openConnection();
            try {
                int aliasCode = 65;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                boolean readingProcesses = false;
                int processesCount = 0;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("SIM_CONDITIONS")) {
                        Pattern pattern = Pattern.compile("q1=(\\d+) q2=(\\d+) q3=(\\d+) k=(\\d+) r=(\\d+)");
                        Matcher matcher = pattern.matcher(line);

                        if (matcher.find()) {
                            q1 = Integer.parseInt(matcher.group(1));
                            q2 = Integer.parseInt(matcher.group(2));
                            q3 = Integer.parseInt(matcher.group(3));
                            k = Integer.parseInt(matcher.group(4));
                            r = Integer.parseInt(matcher.group(5));
                        }
                    } else if (line.startsWith("processes_count")) {
                        processesCount = Integer.parseInt(line.split("=")[1].trim());
                        readingProcesses = true;
                    } else if (readingProcesses && line.startsWith("Process #")) {
                        int processNumber = Integer.parseInt(line.split("#")[1].split("\n")[0].trim());
                        String name = br.readLine().split("=")[1].trim();
                        int startTime = Integer.parseInt(br.readLine().split("=")[1].trim());
                        int phasesCount = Integer.parseInt(br.readLine().split("=")[1].trim());

                        List<Phase> phases = new ArrayList<>();

                        for (int i = 0; i < phasesCount; i++) {
                            String phaseLine = br.readLine();
                            String[] phaseParts = phaseLine.split(" ");
                            int phaseNumber = Integer.parseInt(phaseParts[0].substring(6));
                            int cpu = Integer.parseInt(phaseParts[1].split("=")[1]);
                            int io = Integer.parseInt(phaseParts[2].split("=")[1]);
                            int repeat = Integer.parseInt(phaseParts[3].split("=")[1]);

                            phases.add(new Phase(cpu, io, phaseNumber, repeat));
                        }
                        processes.add(new Process(name, (char) aliasCode, startTime, (ArrayList<Phase>) phases, phasesCount));
                        aliasCode++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Simulator(new QueueManager(q1, q2, q3), (ArrayList<Process>) processes, k, r);
    }
}
