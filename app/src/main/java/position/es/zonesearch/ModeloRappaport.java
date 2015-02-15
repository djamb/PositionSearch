package position.es.zonesearch;

import android.util.Log;

public class ModeloRappaport {
	 //public static final double E = 2.7182818284590452354;
	//p1m Potencia a un metro
	private int p1m;
	//Establece la distancia entre receptor y transmisor en metros
	private int dTR;
	//Potencia en funci�n a la distancia.
	private APsInfo APsInfo1;
	private APsInfo APsInfo2;
	private APsInfo APsInfo3;

	//perdidas media ambiente
	private double pMA;

	private int XO;
	public ModeloRappaport(APsInfo APsInfo1, APsInfo APsInfo2, APsInfo APsInfo3){
		this.p1m=p1m;
		this.APsInfo1=APsInfo1; 
		this.APsInfo2=APsInfo2; 
		this.APsInfo3=APsInfo3; 
				
	}
	public double calculoDistanciaRappaport(APsInfo apInfo){
		//y = 0,7912x + 44,692
			//	R� = 0,4821

		//potencia=p1m+10*pMA*Math.log10(dTR);
		return dTR;
	}
	public double calculoLineaTendencia(APsInfo apInfo){
		return (apInfo.getPotencia()+52.45)/(-1.11);
	}
	//e = 2,718281
	public double calculoLogTendencia(APsInfo apInfo){		
		return Math.pow(Math.E,(apInfo.getPotencia()+42.33)/-10.64);
	}
	
	public Zona puntoResultado(){
		double x2=(APsInfo2.getEjeX()+APsInfo3.getEjeX())/2;
		double y2=(APsInfo2.getEjeY()+APsInfo3.getEjeY())/2;
		APsInfo ap1=tocaRectaCirc(APsInfo1,x2,y2);
		//double xAP2=ap1.getEjeX();
		//double yAP2=ap1.getEjeY();
		double xAP2=(APsInfo1.getEjeX()+APsInfo3.getEjeX())/2;
		double yAP2=(APsInfo1.getEjeY()+APsInfo3.getEjeY())/2;
		APsInfo ap2=tocaRectaCirc(APsInfo2,xAP2,yAP2);
		 xAP2=(APsInfo1.getEjeX()+APsInfo2.getEjeX())/2;
		yAP2=(APsInfo1.getEjeY()+APsInfo2.getEjeY())/2;
		APsInfo ap3=tocaRectaCirc(APsInfo3,xAP2,yAP2);
		Zona z=new Zona();
		z.nombre="zona";
		z.APsInfo1=ap1;
		z.APsInfo2=ap2;
		z.APsInfo3=ap3;
		return z;
		//return new Zona("zona",ap1,ap2,ap3);
		
		
		
		
	}
	
	//metodo para sacar el punto  donde una recta toca un a circunferencia
	//x2 es el punto x por donde pasa la recta
	//y2 es el punto y por donde pasa la recta
	//x1 es otro punto x por donde pasa la recta y el eje de coordenadas de la circunferencia
	//y1 es otro punto y  donde pasa la recta y el eje de coordenadas de la circunferencia
	public APsInfo tocaRectaCirc(APsInfo apInfoBueno,double x2,double y2){
		//Zona z=new Zona("zona",apInfoBueno,ap1,ap2);
		//int x=z.getCentroX();
		//int y=z.getCentroY();
		int xEcua=0;
		int yEcua=0;
		//punto medio de los dos aps que no se usan

		double x1=apInfoBueno.getEjeX();
		double y1=apInfoBueno.getEjeY();
		
		//double x2=(ap1.getEjeX()+ap2.getEjeX())/2;
		//double y2=(ap1.getEjeY()+ap2.getEjeY())/2;
		double a=apInfoBueno.getEjeX();
		double b=apInfoBueno.getEjeY();
		double radioMetros=calculoLogTendencia(apInfoBueno);
		double r= (radioMetros*62)/25;
		Log.e("metros de apinfo",""+radioMetros);
		
		
		
		/* x1=1;
		 x2=4;
		 y1=1;
		 y2=4;
		 a=1;
		 b=1;
		r=2;
		*/
		
		
		Log.e("x1, x2, y1, y2,r",+x1+"  "+x2+"  "+y1+"  "+y2+" "+r);
		//ecuacion de la recta para la interseccion
		//int ecuacionRecta=((xEcuaCir-getXmedioRecta)/(apInfoBueno.getEjeX()-getXmedioRecta))-((yEcuaCir-getYmedioRecta)/(apInfoBueno.getEjeY()-getYmedioRecta));
		//(xEcuaCir-getXmedioRecta)*(apInfoBueno.getEjeY()-getYmedioRecta)-(apInfoBueno.getEjeX()-getXmedioRecta)*(yEcuaCir-getYmedioRecta);
		//x-x1/x2-x1   y-y1/y2-y1
		//xEcuaCir*(apInfoBueno.getEjeY()-getYmedioRecta)-getXmedioRecta*(apInfoBueno.getEjeY()-getYmedioRecta)-
		 		
		//xEcuaCir=(((apInfoBueno.getEjeX()-getXmedioRecta)*(yEcuaCir-getYmedioRecta))+getXmedioRecta*(apInfoBueno.getEjeY()-getYmedioRecta))/(apInfoBueno.getEjeY()-getYmedioRecta);
		
		//ecuacion de la circunferencia
		//int ecuacionCir=(xEcuaCir-apInfoBueno.getEjeX())^2+(yEcuaCir-apInfoBueno.getEjeY())^2-apInfoBueno.getPotencia()^2;
		//x-a y-b
		double h=Math.pow((y2-y1),2);
		double i=1+((Math.pow((x1-x2),2))/h);
		Log.e("la solucion i es",""+i);
		//int i=1+(((x1-x2)^2)/h);
		/*int j1=((2*(x1*y2)*(-x1+x2))+(2*(-y1*x2)*(-x1+x2)))/h;
		int j2=-2*b+(2*a*(x1-x2))/(y2-y1);
		int j3=-(2*a*(x1*y2))/(y2-y1);
		int j4=(2*a*(y1*x2))/(y2-y1);
		int j=j1+j2+j3+j4;
		int z6=(2*(x1*y2)*(-y1*x2))/h;
		int z1=(-y1*x2)^2/h;
		int z4=-r^2+b^2+a^2;
		int z5=((x1*y2)^2)/h;
		int z=z1+z4+z5+z6;*/
		
		double j1=2*((x1*x2*y1)-(Math.pow(x2,2)*y1))/h;
		double j2=-2*((Math.pow(x1,2)*y2)-(x1*x2*y2))/h;
		double j3=(2*a*(x1-x2))/(y2-y1);
		double j4=-2*b;
		
		double j=j1+j2+j3+j4;
		Log.e ("Las jotas son", j1+"  "+j2+"  "+j3+"   "+j4+ "  "+h+" la solucion j es"+j);
		
		double z1=Math.pow(y1,2)*Math.pow(x2,2)/h;
		double z2=Math.pow(x1,2)*Math.pow(y2,2)/h;
		double z3=-2*x1*y2*y1*x2/h;
		double z4=-(2*a*(x1*y2))/(y2-y1);
		double z5=(2*a*(y1*x2))/(y2-y1);
		double z6=-Math.pow(r,2)+Math.pow(b,2)+Math.pow(a,2);
		double z=z1+z2+z3+z4+z5+z6;
		Log.e ("Las zetas son", z1+"  "+z2+"  "+z3+"   "+z4+ "  "+z5+ "  "+z6+" la solucion z es"+z+"la raiz es"+Math.sqrt(Math.pow(j,2)-(4*i*z)));
	//	Log.e("solucion de",""+i+" "+j+"   "+z+ "lo de dentro de la raiz"+ (j^2-(4*i*z)));
		double solucionYDouMas=(-j+Math.sqrt(Math.pow(j,2)-(4*i*z)))/(2*i);
		double solucionYDouMenos=(-j-Math.sqrt(Math.pow(j,2)-(4*i*z)))/(2*i);
		double solucionXMas=(((x2-x1)*(solucionYDouMas-y1))+x1*(y2-y1))/(y2-y1);
		double solucionXMenos=(((x2-x1)*(solucionYDouMenos-y1))+x1*(y2-y1))/(y2-y1);
		
		Double sumaMasY=solucionYDouMas-y2;
		Double sumaMasX=solucionXMas-x2;
		
		Double sumaMenosY=solucionYDouMenos-y2;
		Double sumaMenosX=solucionXMenos-x2;
		
		double totalMas=Math.abs(sumaMasY)+Math.abs(sumaMasX);		
		double totalMenos=Math.abs(sumaMenosY)+Math.abs(sumaMenosX);
		int solucionY;
		int solucionX;
		if (totalMas<totalMenos){
			solucionY=(int)solucionYDouMas;
			solucionX=(int)solucionXMas;
		}else{
			solucionY=(int)solucionYDouMenos;
			solucionX=(int)solucionXMenos;
		}
		
		
		//int solucionY=(int)solucionYDou;
		
		
		
		
		
		Log.e("solucio del ap",solucionX+"     "+solucionY);
		APsInfo ap=new APsInfo();
		ap.ssid="p1";
		ap.ejeX=(int)solucionX;
		ap.ejeY=solucionY;
		return ap;
		//return new APsInfo("p1",(int)solucionX,solucionY);
		
		
	}
	
	
}