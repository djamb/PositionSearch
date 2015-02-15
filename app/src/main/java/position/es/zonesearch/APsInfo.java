package position.es.zonesearch;

import android.os.Parcel;
import android.os.Parcelable;

public class APsInfo implements Parcelable{
	public String ssid; 
	 public int ejeX;
	 public int ejeY;
	 public int potencia;
	public APsInfo(/*String ssid, int ejeX, int ejeY*/){ 
		/*this.ssid=ssid; 
		this.ejeX=ejeX; 
		this.ejeY=ejeY; 
		this.potencia=0; */
	}
	public APsInfo(Parcel in){ 
		readFromParcel(in);
	}
	   @Override
	    public int describeContents() {
	        return 0;
	    }
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	    dest.writeInt(ejeX);
	    dest.writeInt(ejeY);
	    dest.writeString(ssid);
	    dest.writeInt(potencia);
	}
	private void readFromParcel(Parcel in) {
		ejeX = in.readInt();
		ejeY = in.readInt();
		potencia = in.readInt();
		ssid = in.readString();
	}
	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public int getEjeX() {
		return ejeX;
	}
	public void setEjeX(int ejeX) {
		this.ejeX = ejeX;
	}
	public int getEjeY() {
		return ejeY;
	}
	public void setEjeY(int ejeY) {
		this.ejeY = ejeY;
	}
	
}
