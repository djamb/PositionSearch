package position.es.zonesearch;

import java.util.ArrayList;

public class Red {
	private String ssid; 
	private String seguridad; 
	private String bssid;
	private String frecuencia; 
	private String potencia;
	public Red(String ssid, String seg, String bssid, String frec, String potencia){ 
		this.ssid=ssid; 
		this.seguridad=seg; 
		this.bssid=bssid; 
		this.frecuencia=frec; 
		this.potencia=potencia; 
	}
	
	public APsInfo APdeRed(ArrayList<APsInfo> apLista){
		
		for (APsInfo ap:apLista){
			//Log.e("estoy comparando",""+getSSID()+" con "+ap.getSsid());
			if (getSSID().equals(ap.getSsid())){
				//Log.e("elijo"+ap.getSsid(),"elijo"+ap.getSsid());
				return ap;
			}
		}
		return null;
	}
	
	public String getSSID() {
		return ssid;
	}
	public void setSSID(String ssid) {
		this.ssid = ssid;
	}
	public String getSeguridad() {
		return seguridad;
	}
	public void setSeguridad(String seguridad) {
		this.seguridad = seguridad;
	}
	public String getBSSID() {
		return bssid;
	}
	public void setBSSID(String bssid) {
		this.bssid = bssid;
	}
	public String getFrecuencia() {
		return frecuencia;
	}
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}
	public String getPotencia() {
		return potencia;
	}
	public void setPotencia(String potencia) {
		this.potencia = potencia;
	}
	@Override
	public String toString() {
		return "Red [ssid=" + ssid + ", seguridad=" + seguridad + ", bssid="
				+ bssid + ", frecuencia=" + frecuencia + ", potencia="
				+ potencia + "]";
	}
	
}