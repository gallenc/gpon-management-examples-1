package org.opennms.test.application.datagram.syslog;

public class Main {

   public static void main(String[] args) {
      
      final String HELP= "Simple log tester. Either run as server to receive UDP syslogs or as a sender to send a single message\n"
               + "Server: java -jar syslog-tester-<VERSION>.jar server port\n"
               + "   example: java -jar syslog-tester-0.0.1-SNAPSHOT.jar server 514\n"
               + "Sender : java -jar syslog-tester-<VERSION>.jar sender port logmsg\n"
               + "   example: java -jar syslog-tester-0.0.1-SNAPSHOT.jar send 127.0.0.1 514 'Mar 12 09:01:09 LT1-she503-olt-502 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:624406,ENTITY_TYPE:interface,alarm-type-id:onu-dying-gasp,event-time:2024-03-12T09:01:09+00:00,perceived-severity:major,alarm-text:Serial-Number=ALCLFCA46748, Reg-ID=, CT-Name=LT1.she503-olt-502_pon3_CTERM_XGS' \n";
      
      String host;
      int port;
      String logMsg;
      SimpleLogServer server;
      
      try { 
         if("server".equals(args[0])) {
            port = Integer.parseInt(args[1]);
            server = new SimpleLogServer( port );
            server.start();

         }else if("send".equals(args[0])){
            host = args[1];
            port = Integer.parseInt(args[2]);
            logMsg = args[3];
            
            SimpleLogSender logSender = new SimpleLogSender(host, port);
            logSender.sendMessage(logMsg);
            
         } else {
            throw new IllegalArgumentException("unknown command "+args[0]);
         }
         
      } catch (Exception ex) {
         ex.printStackTrace();
         System.out.println(HELP);
      }
      
   }

}
