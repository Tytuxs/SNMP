package SNMP;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SNMP_utility {
    public static ResponseEvent SNMP_GET_synchrone(String cible, String oid, String Community) throws IOException
    {
        TransportMapping transport;

        transport = new DefaultUdpTransportMapping();
        transport.listen();
        CommunityTarget target = new CommunityTarget();
        target.setVersion(SnmpConstants.version1);
        target.setCommunity(new OctetString(Community));


        //cible = X.X.X.X/PORT
        Address targetAddress = new UdpAddress(cible);
        // ou Address targetAddress = GenericAddress.parse("udp:127.0.0.1/161");
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);

        Snmp snmp = new Snmp(transport);
        System.out.println("Requete SNMP envoyée");
        return snmp.get(pdu, target);
    }

    public static ResponseEvent SNMP_SET_Synchrone(String cible, String Community, String oid, String nouvelleValeur) throws IOException {
        TransportMapping transport;

        transport = new DefaultUdpTransportMapping();
        transport.listen();
        CommunityTarget target = new CommunityTarget();
        target.setVersion(SnmpConstants.version1);
        target.setCommunity(new OctetString(Community));


        //cible = X.X.X.X/PORT
        Address targetAddress = new UdpAddress(cible);
        // ou Address targetAddress = GenericAddress.parse("udp:127.0.0.1/161");
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid),new OctetString(nouvelleValeur)));
        pdu.setType(PDU.SET);

        Snmp snmp = new Snmp(transport);
        System.out.println("Requete SNMP envoyée");

        return snmp.set(pdu, target);
    }
}
