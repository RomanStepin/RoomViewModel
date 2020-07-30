package com.example.roomviewmodel;

import java.util.Random;

public class PlaceGenerator
{
   static Random random = new Random(50);

  static   private String getName()
    {
        String name = "";
        int length = random.nextInt(10) + 2; //97 122
        random = new Random();
        for (int i = 0; i < length; i++)
        {
            name = name + (char)(random.nextInt(25) + 97);
        }

        return name;
    }

   static private String getDescription()
    {

        String desc = "";
        random = new Random();
        for (int i = 0; i < random.nextInt(20); i++) {
            String name = "";
            int length = random.nextInt(10) + 2; //97 122
            random = new Random();
            for (int j = 0; j < length; j++) {
                name = name + (char) (random.nextInt(25) + 97);
            }
            desc += name + " ";
        }
        return desc;
    }

    static private double getCoordinate_x()
    {
        return (double) (random.nextInt(180)-90) + (double)(random.nextInt(100))/100;
    }

    static private double getCoordinate_y()
    {
        return (double) (random.nextInt(360)-180) + (double)(random.nextInt(100))/100;
    }

    public Place getPlace()
    {
        return new Place(getName(),getDescription(),getCoordinate_x(),getCoordinate_y());
    }
}
