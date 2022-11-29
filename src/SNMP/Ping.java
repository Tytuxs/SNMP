package SNMP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Ping {
    public static int do_ping (String adresse) throws IOException
    {

        /* -1: erreur ; 0: pas de réponse ; 1: réponse reçue */

        System.out.println("Adresse testée = " + adresse);
        Process p = null;
        String commande = "ping -n 4 -w 1000 " + adresse;
        System.out.println("Commande testée = " + commande);
        BufferedReader bfIn = null;

        p=Runtime.getRuntime().exec(commande);
        if (p == null)
        {
            System.out.println("** Erreur d'exécution de la commande **");
            return -1;
        }
        bfIn = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String strLine;
        boolean pasDeReponse = false;
        while ((strLine = bfIn.readLine()) != null)
        {
            System.out.println(strLine); // pour trace
            if (Trouve100(strLine))
            {
                System.out.println("La machine " + adresse + " ne répond pas");
                return 0;
            }
        }
        bfIn.close();
        System.out.println("La machine " + adresse + " a répondu");
        return 1;
    }

    public static boolean Trouve100 (String s)
        /* true : on a trouvé 100% de paquets rejetés
         * false : certains paquets n'ont pas eté refusés */
    {
        boolean trouve = false;
        StringTokenizer scan = new StringTokenizer (s, " ");
        int cpt = 0;
        while (scan.hasMoreTokens())
        {
            String essai = scan.nextToken();
            int pp = essai.indexOf("%");
            if (pp != -1)
            {
                int p100 = essai.indexOf("100");
                trouve = (p100 != -1);
            }
            if (trouve) return true;
        }
        return false;
    }
}
