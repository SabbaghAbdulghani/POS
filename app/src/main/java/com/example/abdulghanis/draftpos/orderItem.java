package com.example.abdulghanis.draftpos;

public class orderItem {
    public String Id;
    public String product_id;
    public String product_name;

    public String product_note;
    public double Quntity;
    public double Quntity_piece;
    public double RatePartialQuntity;
    public String Unit;
    public String PartUnit;
    public String SupplierId;
    public double CurrencyEqual;
    public double BasePrice;
    public double price;
    public int ItemNO;
    public double getTotalPrice() {
        return Quntity * price;
    }

    public String getPriceString() {
        String str = String.valueOf(Quntity) + " " + Unit;

        if (PartUnit != null && PartUnit.equals("")) {
            product _product=general.getProductById(product_id);
            str += " ( " +String.valueOf(Quntity* _product.part_quntity)+ PartUnit + "  ) ";
        }

        return str + "  *  " + price;
    }
}
