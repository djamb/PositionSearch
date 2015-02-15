package position.es.zonesearch;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import position.es.positionsearch.R;

public class SearchTask extends AsyncTask<String, Void, Boolean> {
    // Di�logo de progreso
    private ProgressDialog dialog;
    private Context context;
    // Gestor de Android de redes WIFI
    private WifiManager manWifi;
    // Lista donde guardamos las redes encontradas
    private List<ScanResult> wifiList;
    private View rootView;
    String ajusteModelo="";
    int ajusteRuido=0;
    int botonRojoOVerde=0;
    private Zona ZonaEscoger=null;
    private boolean dialogOn;
    // Constructor de la clase
    public SearchTask(Context c,View rootView, boolean dialogOn){
        this.context = c;
        dialog = new ProgressDialog(context);
        this.rootView=rootView;
       // ajusteRuido=MainActivity.ajusteRuido;
       // ajusteModelo=MainActivity.ajusteModelo;
        this.dialogOn=dialogOn;
    }
    // Antes de ejecutar la tarea verificamos si la interfaz WIFI est�
    // activa
    protected void onPreExecute() {

        // Buscamos el servicio WIFI
        this.manWifi = (WifiManager)
                this.context.getSystemService (Context.WIFI_SERVICE);
        // Si no esta activo mostramos un mensaje y no seguimos adelante
        if (!this.manWifi.isWifiEnabled()){
            Toast.makeText(context, "El dispositivo tiene desconectada la interfaz WIFI.", Toast.LENGTH_SHORT).show();
        } else{
            if (dialogOn==true) {
                this.dialog.setMessage("Localizando");
                this.dialog.setCancelable(false);
                this.dialog.show();
                this.dialog.setContentView(R.layout.custom_progressdialog);
            }
        }
    }
    // Al acabar ocultamos el dialogo de progreso y actualizamos el
    // adaptador de la Actividad principal
    @Override
    protected void onPostExecute ( final Boolean success) {
        if (dialog.isShowing()) {
            try{
                dialog.dismiss();
            }catch(Exception e){
                e.printStackTrace();

            }


        }
        if (ZonaEscoger==null){

            Toast aToast = Toast.makeText(context, "Se encuentra fuera de Nivel C.", Toast.LENGTH_SHORT);
            aToast.setGravity(Gravity.CENTER,0,0);
            ToastExpander.showFor(aToast, 3000);

        }
    }
    // Sentencias que se ejecutan en segundo plano
    protected Boolean doInBackground (final String... args) {
        // Si no est� activa la interfaz WIFI hemos acabado
        // incorrectamente

        if (!this.manWifi.isWifiEnabled())
            return false;

        // Recorremos todos los resultados de la b�squeda de redes y los
        // vamos pasando a la matriz del tipo Red
        Map<String, ArrayList<Red>> mapRedes = new HashMap<String, ArrayList<Red>>();
        for (int j = 0; j <10 ; j++){

            // Buscamos las redes WIFI
            this.manWifi.startScan();
            // Obtenemos los resultado de la b�squeda
            this.wifiList = this.manWifi.getScanResults();
            // Limpiamos la matriz de resultados de la Actividad principal
            for(int i = 0; i < wifiList. size () ; i++) {
                ScanResult scan_res = wifiList.get(i) ;
                String SSID = scan_res.SSID;
                String BSSID = scan_res.BSSID;

                String frec = Integer.valueOf(scan_res.frequency).toString();
                String pot = Integer.valueOf(scan_res.level).toString();
                //Log.e("nivel del wifi",""+pot);
                String seg = scan_res.toString().split(",")[1].split(":")[1];
                ArrayList<Red> lista;
                if (mapRedes.get(SSID)==null){
                    lista= new ArrayList<Red>();
                }
                else{
                    lista=mapRedes.get(SSID);

                }
                lista.add(new Red(SSID, seg, BSSID, frec, pot));
                mapRedes.put(SSID, lista);
                //redesLista.add(new Red(SSID, seg, BSSID, frec, pot));
                //MainActivity.redes.add(new Red(SSID, seg, BSSID, frec, pot));
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        List<Red> listaRedesMedia=new ArrayList<Red>();
        Iterator it = mapRedes.keySet().iterator();
        while(it.hasNext()){
            int media=0;
            String key = (String)it.next();
            ArrayList<Red> lista= mapRedes.get(key);
            int i=0;
            List<Integer> distanciaMedia=new ArrayList<Integer>();
            while (i<lista.size()){
                //Log.e("Clave: " + key , " -> Valor: "+lista.get(i).getPotencia());

                distanciaMedia.add(Integer.parseInt(lista.get(i).getPotencia()));
                i++;
            }
            Collections.sort(distanciaMedia);
            if (distanciaMedia.size()>=5){
                int j=2;
                while (j<distanciaMedia.size()-2){
                    media=media+distanciaMedia.get(j);
                    //Log.e("cvalores que cojo",""+distanciaMedia.get(i));
                    j++;
                }
                media=media/(distanciaMedia.size()-4);
            }
            else{
                int j=0;
                while (j<distanciaMedia.size()){
                    media=media+distanciaMedia.get(j);
                    j++;
                }
                media=media/distanciaMedia.size();
            }

            listaRedesMedia.add(new Red(lista.get(0).getSSID(), lista.get(0).getSeguridad(), lista.get(0).getBSSID(), lista.get(0).getFrecuencia(), Integer.toString(media)));
        }

        //listaRedesMedia.add(new Red("apC1", "asd", "asd", "asd","-20"));
        //listaRedesMedia.add(new Red("apC2", "asd", "asd", "asd","-10"));
        //listaRedesMedia.add(new Red("apB2", "asd", "asd", "asd","-30"));
        //Ordenar un arraylist de objetos por uno de sus campos
        Collections.sort( listaRedesMedia, new Comparator<Red>(){
            @Override
            public int compare(Red o1, Red o2) {
                return o1.getPotencia().compareTo(o2.getPotencia());
            }
        });





        int o=0;
        while (o<listaRedesMedia.size()){
            Log.e("redes con media",""+listaRedesMedia.get(o));
            o++;
        }
        //x=72y178A2   //128,178A3   //72,234B2    //128,234B3
        ArrayList<APsInfo> listaAPs = new ArrayList<APsInfo>();

        APsInfo pasillo1=CrearAp("EseePark0001",33,278);
        listaAPs.add(pasillo1);
        APsInfo pasillo2=CrearAp("Droiders-dev",33, 33);
        listaAPs.add(pasillo2);
        APsInfo pasillo3=CrearAp("EseePark0003",66, 33);

        listaAPs.add(pasillo3);
        APsInfo pasillo4=CrearAp("EseePark0004",84,278);
        listaAPs.add(pasillo4);
        APsInfo pasillo5=CrearAp("EseePark0005",101,278);
        listaAPs.add(pasillo5);
        APsInfo pasillo6=CrearAp("Droiders",33, 66);
        listaAPs.add(pasillo6);
        APsInfo pasillo7=CrearAp("EseePark0007",50,310);
        listaAPs.add(pasillo7);
        APsInfo pasillo8=CrearAp("EseePark0008",67,310);
        listaAPs.add(pasillo8);
        APsInfo pasillo9=CrearAp("EseePark0009",84,310);
        listaAPs.add(pasillo9);
        APsInfo pasillo10=CrearAp("EseePark0010",101,310);
        listaAPs.add(pasillo10);

        int i=0;
        List<APsInfo> elegidosRed = new ArrayList<APsInfo>();
        APsInfo pegadoAp=null;
        while (i< listaRedesMedia.size()){
            APsInfo apBueno=listaRedesMedia.get(i).APdeRed(listaAPs);
            if (apBueno!=null){
                apBueno.setPotencia(Integer.parseInt(listaRedesMedia.get(i).getPotencia()));

                if (Integer.parseInt(listaRedesMedia.get(i).getPotencia()) > (ajusteRuido) && pegadoAp == null) {
                    pegadoAp = apBueno;
                    Log.e("pillo ap unico", "ap unico" + apBueno.getSsid());
                }

                Log.e("ap elegidos","ap elegidos "+apBueno.getSsid());
                elegidosRed.add(apBueno);
            }

            i++;
        }
        for(APsInfo as:elegidosRed){
            Log.e("ap en lista","ap e lista "+as.getSsid());
        }

        ZonaEscoger=null;
        if (elegidosRed.size()>=3){
            if(ajusteModelo.compareTo("Modelo Rappaport")==0){
                ModeloRappaport rp=new ModeloRappaport(elegidosRed.get(0),elegidosRed.get(1),elegidosRed.get(2));
                ZonaEscoger=rp.puntoResultado();
            }else{
                //ZonaEscoger=new Zona("Vaya a Nivel C",elegidosRed.get(0),elegidosRed.get(1),elegidosRed.get(2));
                ZonaEscoger=CrearZona("Vaya a Nivel C.",elegidosRed.get(0),elegidosRed.get(1),elegidosRed.get(2));
            }
        }

        if (elegidosRed.size()==2){
            int xNew =(elegidosRed.get(0).getEjeX()+elegidosRed.get(1).getEjeX())/2;
            int yNew =(elegidosRed.get(0).getEjeY()+elegidosRed.get(1).getEjeY())/2;
            //Esto metod viejo muy valioso	aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
            //APsInfo apNew=new APsInfo("apNew",xNew,yNew);
            APsInfo apNew=CrearAp("apNew",xNew,yNew);
            //Zona zonaCoordenadasAp=new Zona("Vaya a Nivel C", apNew, apNew, apNew);
            Zona zonaCoordenadasAp=CrearZona("Vaya a Nivel C.", apNew, apNew, apNew);
            ZonaEscoger=zonaCoordenadasAp;
        }
        if (elegidosRed.size()==1 ){
            //Zona zonaCoordenadasAp=new Zona("Vaya a Nivel C", elegidosRed.get(0), elegidosRed.get(0), elegidosRed.get(0));
            Zona zonaCoordenadasAp=CrearZona("Vaya a Nivel C.", elegidosRed.get(0), elegidosRed.get(0), elegidosRed.get(0));
            ZonaEscoger=zonaCoordenadasAp;
        }
        if (pegadoAp!=null){
            //Zona zonaCoordenadasAp=new Zona("Vaya a Nivel C", pegadoAp, pegadoAp, pegadoAp);
            Zona zonaCoordenadasAp=CrearZona("Vaya a Nivel C.", pegadoAp, pegadoAp, pegadoAp);
            ZonaEscoger=zonaCoordenadasAp;
        }
        //prueba de rappaport
		/*EseePark0002.setPotencia(-60);
		EseePark0005.setPotencia(-61);
		EseePark0006.setPotencia(-61);
		ModeloRappaport rp=new ModeloRappaport(EseePark0002, EseePark0005, EseePark0006);
		ZonaEscoger=rp.puntoResultado();
		Log.e("X e Y son:",ZonaEscoger.getCentroX()+"  "+ZonaEscoger.getCentroY());
		*/
        //si esta fuera de rango borrar en la buena

            if (elegidosRed.size()>=1 ){
                if (elegidosRed.get(0).getPotencia()<-65){
                    elegidosRed.get(0).setEjeX(70);
                    elegidosRed.get(0).setEjeY(130);
                    //Zona zonaCoordenadasApas=new Zona("Uste",  elegidosRed.get(0),  elegidosRed.get(0),  elegidosRed.get(0));
                    Zona zonaCoordenadasApas=CrearZona("Uste",  elegidosRed.get(0),  elegidosRed.get(0),  elegidosRed.get(0));
                    ZonaEscoger=zonaCoordenadasApas;
                }

            }

        if (ZonaEscoger!=null){
         //   MainActivity.zonas.add(ZonaEscoger);
         //   MainActivity.seguir=1;
        }



        // Indicamos que la tarea ha acabado correctamente
        return true;
    }
    public APsInfo CrearAp( String ssid ,int ejeX ,int ejeY){
        APsInfo pasillo1=new APsInfo();
        pasillo1.ssid =ssid;
        pasillo1.ejeX=ejeX;
        pasillo1.ejeY=ejeY;
        return pasillo1;
    }

    public Zona CrearZona( String nombre, APsInfo APsInfo1, APsInfo APsInfo2, APsInfo APsInfo3){
        Zona zona=new Zona();
        zona.nombre =nombre;
        zona.APsInfo1=APsInfo1;
        zona.APsInfo2=APsInfo2;
        zona.APsInfo3=APsInfo3;
        return zona;
    }


    public void SavePreferences(){
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
            String json = new Gson().toJson(ZonaEscoger);
            editor.putString("sZonaCoche", json);
            editor.putInt("SdondeCocheX", ZonaEscoger.getCentroX());
            editor.putInt("SdondeCocheY", ZonaEscoger.getCentroY());
            editor.putString("SzonaNombre1", ZonaEscoger.getNombre());
            editor.putInt("SajusteRuido", -65);
            editor.putString("SajusteModelo", "Modelo Posicionamiento Fijo");

        editor.commit();   // I missed to save the data to preference here,.
    }
}




