package org.example.task1;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Sensors {

    private static final int TEMP_LIMIT = 25;
    private static final int CO2_LIMIT = 70;
    private static final Random RANDOM_OBJ = new Random();

    public static void main(String[] args) {
        CompositeDisposable publishers = new CompositeDisposable();

        Observable<Integer> tempObs = Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> generateTemperature())
                .share();

        Observable<Integer> CO2Obs = Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> generateCO2())
                .share();

        publishers.add(
                Observable.combineLatest(
                        tempObs,
                        CO2Obs,
                        SensorData::new
                )
                        .subscribeOn(Schedulers.computation())
                        .subscribe(sensorData -> {
                            boolean isTempOverLimit = sensorData.temp > TEMP_LIMIT;
                            boolean isCO2OverLimit = sensorData.co2 > CO2_LIMIT;
                            if (isTempOverLimit && isCO2OverLimit) {
                                System.out.println("ALARM - temp: " + sensorData.temp + ", CO2: " + sensorData.co2);
                            }
                            else if (isTempOverLimit) {
                                System.out.println("Warning - temp is over the limit: " + sensorData.temp);
                            }
                            else if (isCO2OverLimit) {
                                System.out.println("Warning CO2 is over the limit: " + sensorData.co2);
                            }
                            else {
                                System.out.println("temp: " + sensorData.temp + ", CO2: " + sensorData.co2);
                            }
                        })
        );

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publishers.dispose();
    }
    private static int generateTemperature() {
        return 15 + RANDOM_OBJ.nextInt(16); // 15-30
    }

    private static int generateCO2() {
        return 30 + RANDOM_OBJ.nextInt(71); // 30-100
    }

    private static record SensorData(int temp, int co2) {
    }
}