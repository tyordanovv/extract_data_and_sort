package entities;

import java.util.ArrayList;

public class Ratings {
    private ArrayList<Double> ratings;
    private int numOfRatings;
    private Double sumRatings;

    public Ratings(){
        ratings = new ArrayList<>();
        numOfRatings = 0;
        sumRatings = 0.0;
    }

    public void addRating(Double rating){
        ratings.add(rating);
        numOfRatings++;
        sumRatings += rating;
    }

    public String getRatings(){
        String output = "";
        int counter = 1;
        for (Double rating : ratings){
            if (counter == numOfRatings){
                output += String.valueOf(rating);
            }else output += String.valueOf(rating) + ", ";

            counter++;
        }

        return output;
    }

    public Double getAverageRating(){
        return sumRatings / numOfRatings;
    }

    public int getNumOfRatings(){
        return numOfRatings;
    }
}

