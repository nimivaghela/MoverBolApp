/*
package com.moverbol.viewmodels.jobsummary;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.moverbol.DataRepository;
import com.moverbol.model.ExtraStopsPojo;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.MaterialPojo;

import java.util.List;

*
 * Created by sumeet on 13/9/17.



public class PickupVM extends ViewModel{

//    public ObservableArrayList<ExtraStopsPojo> pickupList = new ObservableArrayList<>();

    public MutableLiveData<JobDetailPojo> jobDetailPojoLive = new MutableLiveData<>();

    public void setJobDetailLive(){
        jobDetailPojoLive = DataRepository.getInstance().getJobDetailsLive();
    }


    //For total materials
    public double getTotalMaterialCost(List<MaterialPojo> materialPojoList){
        double totalMaterialCost = 0;
        for (int i = 0; i < materialPojoList.size(); i++) {
            totalMaterialCost = totalMaterialCost + Double.parseDouble(materialPojoList.get(i).getRate());
        }
        return totalMaterialCost;
    }




public void loadData() {

 for (int i = 0; i < 10; i++) {
            pickupList.add(new ExtraStopsPojo("456 Demo Dr, Suite B, Fremont, California (CA) \nUnited States. 94555", "1st Floor No Stairs", "123", "100 m", "Yes", "Yes",
                    "123", "Jeff Smith", "444-444-4444", "Pickup Note ABC"));
        }
    }


}
*/
