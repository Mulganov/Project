package com.mulganov.project.layout;

public class Layouts {

    public static int number;

    public static Layout Layout_MAIN;
    public static Layout Layout_MAIN_BAR;
    public static Layout Layout_MENU_BAR;
    public static Layout Layout_MAIN2;
    public static Layout Layout_ADD_BAR;

    public static int MAIN      = 0;
    public static int MAIN2     = 1;
    public static int MAIN_BAR  = 2;
    public static int MENU_BAR  = 3;
    public static int ADD_BAR   = 4;

    public static void init(){
        Layout_MAIN =      new Layout(0);
        Layout_MAIN2 =     new Layout(1);
        Layout_MAIN_BAR =  new Layout(2);
        Layout_MENU_BAR =  new Layout(3);
        Layout_ADD_BAR =   new Layout(4);
    }

    public static Layout getLayout(int n){
        switch (n){
            case 0:     return Layout_MAIN;

            case 1:     return Layout_MAIN2;

            case 2:     return Layout_MAIN_BAR;

            case 3:     return Layout_MENU_BAR;

            case 4:     return Layout_ADD_BAR;

            default:    return null;

        }
    }

}
