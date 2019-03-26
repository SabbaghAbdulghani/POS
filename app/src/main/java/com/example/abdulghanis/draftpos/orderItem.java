package com.example.abdulghanis.draftpos;

public class orderItem {
    public String Id ;
    public String product_id ;
    public String product_name;

    public String product_note ;
    public double Quntity ;
    public double Quntity_piece ;
    public double RatePartialQuntity ;
    public String Unit ;
    public String PartUnit;

    public double CurrencyEqual ;
    public double BasePrice ;
    public double price ;

    public double getTotalPrice() {
        return Quntity * price;
    }
    public String getPriceString()
    {
        if (Quntity_piece > 0 && RatePartialQuntity > 0 && PartUnit != "")
            return "#" + Quntity_piece + " " + PartUnit + "  *  " + (price / RatePartialQuntity);
        else
            return "#" + Quntity + " " + Unit + "  *  " + price ;
    }
}
