package com.SpertaClient;

import java.io.IOException;
import java.util.Scanner;

public class SpertaClient {

    private static final int MAX_TIMER = 600;
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid arguments");
        }

        CommunicationClient comms = new CommunicationClient(args[0]);
        printHelpMenu();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            handleCommand(comms, sc.nextLine());
        }

        close(comms, sc);
    }

    private static void close(CommunicationClient comms, Scanner sc) {
         try {
            sc.close();
            comms.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleCommand(CommunicationClient comms, String ln) {
        String[] args = ln.split(" ");

        try {
            switch(args[0]) {
                case "CREATE":
                    handleCreate(comms, args);
                    break;
                case "ADD":
                    handleAdd(comms, args);
                    break;
                case "RD":
                    handleRD(comms, args);
                    break;
                case "EC":
                    handleEC(comms, args);
                    break;
                case "RT":
                    handleRT(comms, args);
                    break;
                case "RH":
                    handleRH(comms, args);
                    break;
                case "HELP":
                    printHelpMenu();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }

    private static void printHelpMenu() {
        System.out.println("CREATE <hm> ### Criar casa <hm> - utilizador é Owner");
        System.out.println("ADD <user1> <hm> <s> ### Adicionar utilizador <user1> à casa <hm>, seção <s>");
        System.out.println("RD <hm> <s> ### Registar um Dispositivo na casa <hm>, na seção <s>");
        System.out.println("EC <hm> <d> <int> ### Enviar valor <int> de estado/temporização, do dispositivo <d> da casa <hm>, para o servidor");
        System.out.println("RT <hm> ### Receber a informação sobre o último comando (estados/temporizações) enviado a cada dispositivo da casa <hm>, desde que o utilizador tenha permissões");
        System.out.println("RH <hm> <d> ### Receber o Histórico (ficheiro de log .csv) de comandos enviados ao dispositivo <d> da casa <hm>, desde que o utilizador tenha permissões");
        System.out.println("HELP ### Imprime novamente este menu");
    }

    private static void handleCreate(CommunicationClient comms, String[] args) throws IOException {
        int home = Integer.parseInt(args[1]);
        if (isValidHome(home)) {
            comms.sendCreate(home);
        } else {
            System.out.println("Invalid arguments: try CREATE <hm>");
        }
    }

    private static void handleAdd(CommunicationClient comms, String[] args) throws IOException {
        String user = args[1];
        int home = Integer.parseInt(args[2]);
        String section = args[3];

        if (isValidUser(user) && isValidHome(home) && isValidSection(section)) {
            comms.sendAdd(user, home, section);
        } else {
            System.out.println("Invalid arguments: try ADD <user1> <hm> <s>");
        }
    }

    private static void handleRD(CommunicationClient comms, String[] args) throws IOException {
        int home = Integer.parseInt(args[1]);
        String section = args[2];

        if (isValidHome(home) && isValidSection(section)) {
            comms.sendRD(home, section);
        } else {
            System.out.println("Invalid arguments: try RD <hm> <s>");
        }
    }

    private static void handleEC(CommunicationClient comms, String[] args) throws IOException {
        int home = Integer.parseInt(args[1]);
        String device = args[2];
        int command = Integer.parseInt(args[3]);

        if (isValidHome(home) && isValidDevice(device) && (command >= 0 && command <= MAX_TIMER)) {
            comms.sendEC(home, device, command);
        } else {
            System.out.println("Invalid arguments: try EC <hm> <d> <int>");
        }
    }

    private static void handleRT(CommunicationClient comms, String[] args) throws IOException {
        int home = Integer.parseInt(args[1]);

        if (isValidHome(home)) {
            comms.sendRT(home);
        } else {
            System.out.println("Invalid arguments: try RT <hm>");
        }
    }

    private static void handleRH(CommunicationClient comms, String[] args) throws IOException {
        int home = Integer.parseInt(args[1]);
        String device = args[2];

        if (isValidHome(home) && isValidDevice(device)) {
            comms.sendRH(home, device);
        } else {
            System.out.println("Invalid arguments: try RH <hm> <d>");
        }
    }

    private static boolean isValidHome(int home) {
        return home >= 0;
    }

    private static boolean isValidUser(String user) {
        return true;
    }

    private static boolean isValidSection(String s) {
        return s == "E" || s == "G" || s == "L" || s == "M" || s == "P" || s == "S";
    }

    private static boolean isValidDevice(String device) {
        String section = device.substring(0, 1);
        int deviceID = Integer.parseInt(device.substring(1, device.length()));
        return isValidSection(section) && deviceID >= 0;
    }
}
