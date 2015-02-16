package position.es.positionsearch;

import java.util.ArrayList;
import position.es.zonesearch.APsInfo;

/**
 * Created by djamb on 16/02/15.
 */
public class PositionSite {


  public ArrayList<APsInfo> getSite(){
    ArrayList<APsInfo> listaAPs = new ArrayList<APsInfo>();

    APsInfo pasillo1 = CrearAp("EseePark0001", 33, 278);
    listaAPs.add(pasillo1);
    APsInfo pasillo2 = CrearAp("Droiders-dev", 33, 33);
    listaAPs.add(pasillo2);
    APsInfo pasillo3 = CrearAp("EseePark0003", 66, 33);
    listaAPs.add(pasillo3);
    APsInfo pasillo4 = CrearAp("EseePark0004", 84, 278);
    listaAPs.add(pasillo4);
    APsInfo pasillo5 = CrearAp("EseePark0005", 101, 278);
    listaAPs.add(pasillo5);
    APsInfo pasillo6 = CrearAp("Droiders", 33, 66);
    listaAPs.add(pasillo6);
    APsInfo pasillo7 = CrearAp("EseePark0007", 50, 310);
    listaAPs.add(pasillo7);
    APsInfo pasillo8 = CrearAp("EseePark0008", 67, 310);
    listaAPs.add(pasillo8);
    APsInfo pasillo9 = CrearAp("EseePark0009", 84, 310);
    listaAPs.add(pasillo9);
    APsInfo pasillo10 = CrearAp("EseePark0010", 101, 310);
    listaAPs.add(pasillo10);
    return listaAPs;
  }

  public APsInfo CrearAp( String ssid ,int ejeX ,int ejeY){
    APsInfo pasillo1=new APsInfo();
    pasillo1.ssid =ssid;
    pasillo1.ejeX=ejeX;
    pasillo1.ejeY=ejeY;
    return pasillo1;
  }
}
