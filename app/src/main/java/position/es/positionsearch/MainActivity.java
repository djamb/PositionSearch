package position.es.positionsearch;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import position.es.zonesearch.APsInfo;
import position.es.zonesearch.ModeloRappaport;
import position.es.zonesearch.Red;
import position.es.zonesearch.ToastExpander;
import position.es.zonesearch.Zona;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p/>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class MainActivity extends Activity {
private int salir=0;
    public static  ExampleCardScrollAdapter mAdapter;
    public static int hola=0;
    public  static ArrayList<Zona> zonas = new ArrayList<Zona>();
    public static Zona zonaEscogida=null;
    public String modo="";
    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private Zona ZonaEscoger=null;
    //private CardScrollView mCardScroller;

    private boolean mIsStopped = false;

    private CardScrollView mCardScrollView;
    private ArrayList<View> _cardsList;
private ArrayList<String> mTexts;

    private View mView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //setContentView(R.layout.mapeado_frame);
        createTexts();


        @SuppressWarnings("unused")
        AsyncTask<?, ?, ?> task2 = new ProgressTask(this, null,true).execute();
        mCardScrollView = new CardScrollView(this);
         mAdapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);

        mCardScrollView.activate();  // This should be moved in onResume.
        setContentView(mCardScrollView);
    }

    class ExampleCardScrollAdapter extends CardScrollAdapter {
        private WifiManager manWifi;
        private List<ScanResult> wifiList;
        @Override
        public int getPosition(Object item) {
            return mTexts.indexOf(item);
        }

        @Override
        public int getCount() {
            return mTexts.size();
        }


        @Override
        public Object getItem(int position) {
            return mTexts.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position){
            return CardBuilder.Layout.EMBED_INSIDE.ordinal();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

          // if (position==0){
            //    modo="mapa";
            //}
            View view = new CardBuilder(getApplicationContext(), CardBuilder.Layout.EMBED_INSIDE)
                    .setEmbeddedLayout(R.layout.mapeado_frame)
                    .setFootnote("Situacion: " + modo)
                    .getView(convertView, parent);
            ImageView imagenMapa= (ImageView)view.findViewById(R.id.map);
            imagenMapa.setImageResource(R.drawable.mapa2);
            ImageView imgPersona = (ImageView)view.findViewById(R.id.persona);
            imgPersona.setImageResource(R.drawable.pilotoaa);
            ImageView imgMapa = (ImageView)view.findViewById(R.id.map);
            imgMapa.setImageResource(R.drawable.mapa2);
            if (zonaEscogida != null) {
                Log.e("estoy en la zona", ""+ zonaEscogida.getNombre() + zonaEscogida.getCentroX() + zonaEscogida.getCentroY());
                  //  dibujarImg(44, 44, imgPersona);
                    //imgPersona.setPadding(30,30,0,0);
                    modo=zonaEscogida.getNombre();
                    dibujarImg(zonaEscogida.getCentroX(), zonaEscogida.getCentroY(), imgPersona);

            }
            @SuppressWarnings("unused")
            AsyncTask<?, ?, ?> task2 = new ProgressTask(getApplicationContext(), null,true).execute();



            return view;
        }

        public void dibujarImg(int x,int y, ImageView imV){
            //Transformo en dp el tamaño que viene en pixel para adaptarlo a todas las pantallas
            FrameLayout.LayoutParams absParams =
                    (FrameLayout.LayoutParams)imV.getLayoutParams();
            absParams.setMargins(x,y,0,0);
            imV.setLayoutParams(absParams);
        }



    }


    public class ProgressTask extends AsyncTask<String, Void, Boolean> {
        // Dislogo de progreso
       // private ProgressDialog dialog;
        private Context context;
        // Gestor de Android de redes WIFI
        private WifiManager manWifi;
        // Lista donde guardamos las redes encontradas
        private List<ScanResult> wifiList;
        private View rootView;
        String ajusteModelo = "";
        int ajusteRuido = 0;
        int botonRojoOVerde = 0;
        private Zona ZonaEscoger = null;
        private boolean dialogOn;

        // Constructor de la clase
        public ProgressTask(Context c, View rootView, boolean dialogOn) {
            this.context = c;
          //  dialog = new ProgressDialog(context);
            this.rootView = rootView;
            ajusteRuido = 55;
            ajusteModelo = "Modelo Rappaport";
            this.dialogOn = dialogOn;
        }

        // Antes de ejecutar la tarea verificamos si la interfaz WIFI est�
        // activa
        protected void onPreExecute() {

            // Buscamos el servicio WIFI
            this.manWifi = (WifiManager)
                    this.context.getSystemService(Context.WIFI_SERVICE);
            // Si no esta activo mostramos un mensaje y no seguimos adelante
            if (!this.manWifi.isWifiEnabled()) {
                Toast.makeText(context, "El dispositivo tiene desconectada la interfaz WIFI.", Toast.LENGTH_SHORT).show();
            }/* else {
                if (dialogOn == true) {
                    this.dialog.setMessage("Localizando");
                    this.dialog.setCancelable(false);
                    this.dialog.show();
                    this.dialog.setContentView(R.layout.custom_progressdialog);
                }
            }*/
        }

        // Al acabar ocultamos el dialogo de progreso y actualizamos el
        // adaptador de la Actividad principal
        @Override
        protected void onPostExecute(final Boolean success) {
            /*if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }*/
            if (ZonaEscoger == null) {
                Toast aToast = Toast.makeText(context, "Se encuentra fuera de Nivel C.", Toast.LENGTH_SHORT);
                aToast.setGravity(Gravity.CENTER, 0, 0);
                ToastExpander.showFor(aToast, 3000);
            }
            if (success) {
                mAdapter.notifyDataSetChanged();
            }
        }

        // Sentencias que se ejecutan en segundo plano
        protected Boolean doInBackground(final String... args) {
            // Si no esta activa la interfaz WIFI hemos acabado
            // incorrectamente

            if (!this.manWifi.isWifiEnabled())
                return false;

            // Recorremos todos los resultados de la b�squeda de redes y los
            // vamos pasando a la matriz del tipo Red
            Map<String, ArrayList<Red>> mapRedes = new HashMap<String, ArrayList<Red>>();
            for (int j = 0; j < 10; j++) {

                // Buscamos las redes WIFI
                this.manWifi.startScan();
                // Obtenemos los resultado de la busqueda
                this.wifiList = this.manWifi.getScanResults();
                // Limpiamos la matriz de resultados de la Actividad principal
                if (context instanceof MainActivity) {

                }
                for (int i = 0; i < wifiList.size(); i++) {
                    ScanResult scan_res = wifiList.get(i);
                    String SSID = scan_res.SSID;
                    String BSSID = scan_res.BSSID;

                    String frec = Integer.valueOf(scan_res.frequency).toString();
                    String pot = Integer.valueOf(scan_res.level).toString();
                    //Log.e("nivel del wifi",""+pot);
                    String seg = scan_res.toString().split(",")[1].split(":")[1];
                    ArrayList<Red> lista;
                    if (mapRedes.get(SSID) == null) {
                        lista = new ArrayList<Red>();
                    } else {
                        lista = mapRedes.get(SSID);

                    }
                    lista.add(new Red(SSID, seg, BSSID, frec, pot));
                    mapRedes.put(SSID, lista);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            List<Red> listaRedesMedia = new ArrayList<Red>();
            Iterator it = mapRedes.keySet().iterator();
            while (it.hasNext()) {
                int media = 0;
                String key = (String) it.next();
                ArrayList<Red> lista = mapRedes.get(key);
                int i = 0;
                List<Integer> distanciaMedia = new ArrayList<Integer>();
                while (i < lista.size()) {
                    distanciaMedia.add(Integer.parseInt(lista.get(i).getPotencia()));
                    i++;
                }
                Collections.sort(distanciaMedia);
                if (distanciaMedia.size() >= 5) {
                    int j = 2;
                    while (j < distanciaMedia.size() - 2) {
                        media = media + distanciaMedia.get(j);
                        j++;
                    }
                    media = media / (distanciaMedia.size() - 4);
                } else {
                    int j = 0;
                    while (j < distanciaMedia.size()) {
                        media = media + distanciaMedia.get(j);
                        j++;
                    }
                    media = media / distanciaMedia.size();
                }

                listaRedesMedia.add(new Red(lista.get(0).getSSID(), lista.get(0).getSeguridad(), lista.get(0).getBSSID(), lista.get(0).getFrecuencia(), Integer.toString(media)));
            }
            //Ordenar un arraylist de objetos por uno de sus campos
            Collections.sort(listaRedesMedia, new Comparator<Red>() {
                @Override
                public int compare(Red o1, Red o2) {
                    return o1.getPotencia().compareTo(o2.getPotencia());
                }
            });
            int o = 0;
            while (o < listaRedesMedia.size()) {
                Log.e("redes con media", "" + listaRedesMedia.get(o));
                o++;
            }
            //Situa los aps
            ArrayList<APsInfo> listaAPs=new PositionSite().getSite();
            int i = 0;
            List<APsInfo> elegidosRed = new ArrayList<APsInfo>();
            APsInfo pegadoAp = null;
            while (i < listaRedesMedia.size()) {
                APsInfo apBueno = listaRedesMedia.get(i).APdeRed(listaAPs);
                if (apBueno != null) {
                    apBueno.setPotencia(Integer.parseInt(listaRedesMedia.get(i).getPotencia()));
                    if (Integer.parseInt(listaRedesMedia.get(i).getPotencia()) > (ajusteRuido) && pegadoAp == null) {
                        pegadoAp = apBueno;
                        Log.e("pillo ap unico", "ap unico" + apBueno.getSsid());
                    }

                    Log.e("ap elegidos", "ap elegidos " + apBueno.getSsid());
                    elegidosRed.add(apBueno);
                }

                i++;
            }
            for (APsInfo as : elegidosRed) {
                Log.e("ap en lista", "ap e lista " + as.getSsid());
            }

            ZonaEscoger = null;
            if (elegidosRed.size() >= 3) {
                if (ajusteModelo.compareTo("Modelo Rappaport") == 0) {
                    ModeloRappaport rp = new ModeloRappaport(elegidosRed.get(0), elegidosRed.get(1), elegidosRed.get(2));
                    ZonaEscoger = rp.puntoResultado();
                } else {
                    ZonaEscoger = CrearZona("Vaya a Nivel C.", elegidosRed.get(0), elegidosRed.get(1), elegidosRed.get(2));
                }
            }

            if (elegidosRed.size() == 2) {
                int xNew = (elegidosRed.get(0).getEjeX() + elegidosRed.get(1).getEjeX()) / 2;
                int yNew = (elegidosRed.get(0).getEjeY() + elegidosRed.get(1).getEjeY()) / 2;
                APsInfo apNew = CrearAp("apNew", xNew, yNew);
                Zona zonaCoordenadasAp = CrearZona("Vaya a Nivel C.", apNew, apNew, apNew);
                ZonaEscoger = zonaCoordenadasAp;
            }
            if (elegidosRed.size() == 1) {
                Zona zonaCoordenadasAp = CrearZona("Vaya a Nivel C.", elegidosRed.get(0), elegidosRed.get(0), elegidosRed.get(0));
                ZonaEscoger = zonaCoordenadasAp;
            }
            if (pegadoAp != null) {
                Zona zonaCoordenadasAp = CrearZona("Vaya a Nivel C.", pegadoAp, pegadoAp, pegadoAp);
                ZonaEscoger = zonaCoordenadasAp;
            }
            if (elegidosRed.size() >= 1) {
              if (elegidosRed.get(0).getPotencia() < -65) {
                elegidosRed.get(0).setEjeX(70);
                elegidosRed.get(0).setEjeY(130);
                Zona zonaCoordenadasApas = CrearZona("Fuera de rango", elegidosRed.get(0), elegidosRed.get(0), elegidosRed.get(0));
                ZonaEscoger = zonaCoordenadasApas;
              }
            }

            //Para posicionamiento temporal
            String zonaEl = "";
            zonaEl = "Se encuentra fuera de Nivel C.";
            if (elegidosRed.size() >= 1) {
              if (elegidosRed.get(0).getPotencia() > -65) {
                if (elegidosRed.get(0).getSsid().compareTo("EseePark0001") == 0 || elegidosRed.get(0).getSsid().compareTo("Droiders-dev") == 0 || elegidosRed.get(0).getSsid().compareTo("EseePark0003") == 0 || elegidosRed.get(0).getSsid().compareTo("EseePark0004") == 0 || elegidosRed.get(0).getSsid().compareTo("EseePark0005") == 0) {
                  zonaEl = "Está en frutería "+elegidosRed.get(0).getSsid();
                }else{
                  zonaEl = "Está en carnicería "+elegidosRed.get(0).getSsid();
                }
              }
            }
            if (ZonaEscoger != null) {
              MainActivity.zonaEscogida=ZonaEscoger;
            }
            // Indicamos que la tarea ha acabado correctamente
            return true;
        }


      //Crea un ap nuevo
        public APsInfo CrearAp( String ssid ,int ejeX ,int ejeY){
            APsInfo pasillo1=new APsInfo();
            pasillo1.ssid =ssid;
            pasillo1.ejeX=ejeX;
            pasillo1.ejeY=ejeY;
            return pasillo1;
        }
        //Crea una zona para triangular
        public Zona CrearZona( String nombre, APsInfo APsInfo1, APsInfo APsInfo2, APsInfo APsInfo3){
            Zona zona=new Zona();
            zona.nombre =nombre;
            zona.APsInfo1=APsInfo1;
            zona.APsInfo2=APsInfo2;
            zona.APsInfo3=APsInfo3;
            return zona;
        }

    }

    private void createTexts(){
        mTexts = new ArrayList<String>();

        mTexts.add("Value for table 1");
       /* mTexts.add("Value for table 2");
        mTexts.add("Value for table 3");
        mTexts.add("Value for table 4");*/
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCardScrollView.activate();
    }

    @Override
    protected void onPause() {
        mCardScrollView.deactivate();
        super.onPause();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ALT_LEFT) {
            Log.e("slide izquierda","slide izquierda");
            salir=1;
        }

        // para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }


}
