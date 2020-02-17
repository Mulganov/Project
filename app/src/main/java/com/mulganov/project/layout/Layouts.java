package com.mulganov.project.layout;

public class Layouts {

    public static int maps_number;
    public static int menu_number;
    public static int game_number;

    public static Layout Maps_Layout_MAIN;
    public static Layout Maps_Layout_MAIN_BAR;
    public static Layout Maps_Layout_MENU_BAR;
    public static Layout Maps_Layout_MAIN2;
    public static Layout Maps_Layout_ADD_BAR;

    public static int Maps_MAIN = 0;
    public static int Maps_MAIN2 = 1;
    public static int Maps_MAIN_BAR = 2;
    public static int Maps_MENU_BAR = 3;
    public static int Maps_ADD_BAR   = 4;


    public static Layout Menu_Layout_MAIN;

    public static int Menu_MAIN = 0;

    public static Layout Game_Layout_MAIN;

    public static int Game_MAIN = 0;

    public static void init(){
        maps_number = 0;
        menu_number = 0;
        game_number = 0;

        Maps_Layout_MAIN =      new Layout(0, "maps");
        Maps_Layout_MAIN2 =     new Layout(1, "maps");
        Maps_Layout_MAIN_BAR =  new Layout(2, "maps");
        Maps_Layout_MENU_BAR =  new Layout(3, "maps");
        Maps_Layout_ADD_BAR =   new Layout(4, "maps");


        Menu_Layout_MAIN =      new Layout(0, "menu");

        Game_Layout_MAIN =      new Layout(0, "game");
    }


    public static Layout getLayoutMaps(int n){
        switch (n){
            case 0:     return Maps_Layout_MAIN;

            case 1:     return Maps_Layout_MAIN2;

            case 2:     return Maps_Layout_MAIN_BAR;

            case 3:     return Maps_Layout_MENU_BAR;

            case 4:     return Maps_Layout_ADD_BAR;

            default:    return null;

        }
    }

    public static Layout getLayoutMenu(int n){
        switch (n){
            case 0:     return Menu_Layout_MAIN;

            default:    return null;

        }
    }

    public static Layout getLayoutGame(int n){
        switch (n){
            case 0:     return Game_Layout_MAIN;

            default:    return null;

        }
    }

}
