//package it.polimi.ingsw.is25am22new.Network;
//
//import it.polimi.ingsw.is25am22new.Network.RMI.Client.RmiClient;
//import it.polimi.ingsw.is25am22new.Network.Socket.Client.SocketClientSide;
//
//import java.util.Scanner;
//
//public class GalaxyTruckerClient {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("=== GALAXY TRUCKER GAME ===");
//        System.out.println("Select connection type:");
//        System.out.println("1. RMI (Remote Method Invocation)");
//        System.out.println("2. Socket");
//        System.out.print("Enter your choice (1 or 2): ");
//
//        int choice = 0;
//        while (choice != 1 && choice != 2) {
//            try {
//                choice = Integer.parseInt(scanner.nextLine());
//                if (choice != 1 && choice != 2) {
//                    System.out.print("Invalid choice. Please enter 1 or 2: ");
//                }
//            } catch (NumberFormatException e) {
//                System.out.print("Invalid input. Please enter a number (1 or 2): ");
//            }
//        }
//
//        String host = "localhost";
//        int port = 1234;
//
//        if (args.length >= 1) {
//            host = args[0];
//        }
//        if (args.length >= 2) {
//            try {
//                port = Integer.parseInt(args[1]);
//            } catch (NumberFormatException e) {
//                System.err.println("Invalid port number, using default: 1234");
//            }
//        }
//
//        try{
//            if(choice == 1) {
//                // RMI connection
//                //RmiClient.main(new String[]{host, String.valueOf(port)});
//            } else {
//                // Socket connection
//                SocketClientSide.main(new String[]{host, String.valueOf(port)});
//            }
//        } catch (Exception e){
//            System.err.println("Error: " + e.getMessage());
//        } finally {
//            scanner.close();
//        }
//    }
//}
