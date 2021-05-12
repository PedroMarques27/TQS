package hw.weatherbit.homework;

public class ApiRequest {
    public static int TIME_OUT = 120000;
    private WeatherData requestData;
    private long requestTime;


    public ApiRequest(WeatherData w){
        this.requestData = w;
        this.requestTime = System.currentTimeMillis();
    }

    public boolean isValid(){
        long dif = System.currentTimeMillis() - requestTime;
        if (dif>=TIME_OUT)
            return false;
        return true;
    }
    public WeatherData getData(){
        return this.requestData;
    }

    public void setTimeOut(int i){
        TIME_OUT = i;
    }



}