package com.moverbol.viewmodels.color;

import androidx.databinding.ObservableArrayList;

import com.moverbol.model.ColorPojo;
import com.moverbol.util.Util;

/**
 * Created by Admin on 27-09-2017.
 */

public class ColorListModel {

    public String[][] colorArray = new String[10][6];
    public ObservableArrayList<String> tempList;
    public ObservableArrayList<ColorPojo> colorList;

    public ColorListModel() {
        setColor();
        tempList = Util.twoDArrayToList(colorArray);
        colorList = new ObservableArrayList<>();
        for (String color : tempList) {
            colorList.add(new ColorPojo(color));
        }
    }

    public void setColor() {
        for (int i = 0; i < 10; i++) {
            colorArray[i] = new String[6];
        }
        //First Column!
        colorArray[0][0] = "254,46,46";
        colorArray[1][0] = "254,154,46";
        colorArray[2][0] = "247,254,46";
        colorArray[3][0] = "154,254,46";
        colorArray[4][0] = "46,254,46";
        colorArray[5][0] = "46,254,247";
        colorArray[6][0] = "46,154,254";
        colorArray[7][0] = "154,46,254";
        colorArray[8][0] = "254,46,247";
        colorArray[9][0] = "132,132,132";


        //Second Column!
        colorArray[0][1] = "250,88,88";
        colorArray[1][1] = "250,172,88";
        colorArray[2][1] = "244,250,88";
        colorArray[3][1] = "172,250,88";
        colorArray[4][1] = "88,250,88";
        colorArray[5][1] = "88,250,244";
        colorArray[6][1] = "88,172,250";
        colorArray[7][1] = "172,88,250";
        colorArray[8][1] = "250,88,244";
        colorArray[9][1] = "164,164,164";


        //Third Column!
        colorArray[0][2] = "247,129,129";
        colorArray[1][2] = "247,190,129";
        colorArray[2][2] = "243,247,129";
        colorArray[3][2] = "190,247,129";
        colorArray[4][2] = "129,247,129";
        colorArray[5][2] = "129,247,243";
        colorArray[6][2] = "129,190,247";
        colorArray[7][2] = "190,129,247";
        colorArray[8][2] = "247,129,243";
        colorArray[9][2] = "189,189,189";

        //Fourth Column!
        colorArray[0][3] = "245,169,169";
        colorArray[1][3] = "245,208,169";
        colorArray[2][3] = "242,245,169";
        colorArray[3][3] = "208,245,169";
        colorArray[4][3] = "169,245,169";
        colorArray[5][3] = "169,245,242";
        colorArray[6][3] = "169,208,245";
        colorArray[7][3] = "208,169,245";
        colorArray[8][3] = "245,169,242";
        colorArray[9][3] = "216,216,216";

        //Fifth Column !
        colorArray[0][4] = "246,206,206";
        colorArray[1][4] = "246,227,206";
        colorArray[2][4] = "245,246,206";
        colorArray[3][4] = "227,246,206";
        colorArray[4][4] = "206,246,206";
        colorArray[5][4] = "206,246,245";
        colorArray[6][4] = "206,227,246";
        colorArray[7][4] = "227,206,246";
        colorArray[8][4] = "246,206,245";
        colorArray[9][4] = "230,230,230";

        //Sixth Column!
        colorArray[0][5] = "248,224,224";
        colorArray[1][5] = "248,236,224";
        colorArray[2][5] = "247,248,224";
        colorArray[3][5] = "236,248,224";
        colorArray[4][5] = "224,248,224";
        colorArray[5][5] = "224,248,247";
        colorArray[6][5] = "224,236,248";
        colorArray[7][5] = "236,224,248";
        colorArray[8][5] = "248,224,247";
        colorArray[9][5] = "242,242,242";
    }
}
