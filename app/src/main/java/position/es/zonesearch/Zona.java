package position.es.zonesearch;

import android.os.Parcel;
import android.os.Parcelable;

public class Zona  implements Parcelable  {
    public String nombre;
    public APsInfo APsInfo1;
    public APsInfo APsInfo2;
    public APsInfo APsInfo3;
    public Zona(/*String nombre, APsInfo APsInfo1, APsInfo APsInfo2, APsInfo APsInfo3*/){
		/*this.nombre=nombre;
		this.APsInfo1=APsInfo1;
		this.APsInfo2=APsInfo2;
		this.APsInfo3=APsInfo3; */
    }
    public Zona(Parcel in){
        readFromParcel(in);
    }
    public int getCentroX(){
        return(APsInfo1.getEjeX()+APsInfo2.getEjeX()+APsInfo3.getEjeX())/3;
    }
    public int getCentroY(){
        return (APsInfo1.getEjeY()+APsInfo2.getEjeY()+APsInfo3.getEjeY())/3;
    }
    //Podemos comprobar que cualquier punto esta dentro de nuestro triangulo
    public boolean estaDentroDeZona(int Px, int Py){
        int orientacionTotal=(APsInfo1.getEjeX() - APsInfo3.getEjeX()) * (APsInfo2.getEjeY() - APsInfo3.getEjeY()) - (APsInfo1.getEjeY() - APsInfo3.getEjeY()) * (APsInfo2.getEjeX() - APsInfo3.getEjeX());
        int orientacionSinP3=(APsInfo1.getEjeX() - Px) * (APsInfo2.getEjeY() -  Py) - (APsInfo1.getEjeY() - Py) * (APsInfo2.getEjeX() - Px);
        int orientacionSinP1=(Px- APsInfo3.getEjeX()) * (APsInfo2.getEjeY() - APsInfo3.getEjeY()) - (Py - APsInfo3.getEjeY()) * (APsInfo2.getEjeX() - APsInfo3.getEjeX());
        int orientacionSinP2=(APsInfo1.getEjeX() - APsInfo3.getEjeX()) * (Py - APsInfo3.getEjeY()) - (APsInfo1.getEjeY() - APsInfo3.getEjeY()) * (Px - APsInfo3.getEjeX());
        if( orientacionTotal>=0){
            if (orientacionSinP3>=0 && orientacionSinP1>=0 && orientacionSinP2>=0){
                return true;
            }
        }else{
            if (orientacionSinP3<0 && orientacionSinP1<0 && orientacionSinP2<0){
                return true;
            }
        }
        return false;
    }

    public boolean esZonaCorrecta(APsInfo APsPrueba1, APsInfo APsPrueba2, APsInfo APsPrueba3){
        if (estaAp(APsPrueba1)&&estaAp(APsPrueba2) &&estaAp(APsPrueba3)){
            return true;
        }
        return false;

    }
    public boolean estaAp(APsInfo APsPrueba){
        if (APsPrueba.getSsid().equals(APsInfo1.getSsid())){
            return true;
        }
        if (APsPrueba.getSsid().equals(APsInfo2.getSsid())){
            return true;
        }
        if (APsPrueba.getSsid().equals(APsInfo3.getSsid())){
            return true;
        }
        return false;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public APsInfo getAPsInfo1() {
        return APsInfo1;
    }
    public void setAPsInfo1(APsInfo aPsInfo1) {
        APsInfo1 = aPsInfo1;
    }
    public APsInfo getAPsInfo2() {
        return APsInfo2;
    }
    public void setAPsInfo2(APsInfo aPsInfo2) {
        APsInfo2 = aPsInfo2;
    }
    public APsInfo getAPsInfo3() {
        return APsInfo3;
    }
    public void setAPsInfo3(APsInfo aPsInfo3) {
        APsInfo3 = aPsInfo3;
    }
    @Override
    public String toString() {
        return "Zona [nombre=" + nombre + ", APsInfo1=" + APsInfo1
                + ", APsInfo2=" + APsInfo2 + ", APsInfo3=" + APsInfo3 + "]";
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeValue(APsInfo1);
        dest.writeValue(APsInfo2);
        dest.writeValue(APsInfo3);
        // dest.writeBooleanArray(new boolean[]{esHijoUnico});
        //dest.writeFloatArray(notas);
        //dest.writeTypedList(amigos);

    }

    private void readFromParcel(Parcel in) {
        nombre = in.readString();
        APsInfo1=in.readParcelable(APsInfo.class.getClassLoader());
        APsInfo2=in.readParcelable(APsInfo.class.getClassLoader());
        APsInfo3=in.readParcelable(APsInfo.class.getClassLoader());

    }




}
