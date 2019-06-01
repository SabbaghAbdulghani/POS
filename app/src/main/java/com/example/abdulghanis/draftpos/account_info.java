package com.example.abdulghanis.draftpos;

import java.util.Date;

public class account_info {
    public String account_id;
    public String account_code ;
    public String account_name ;
    public String account_parent ;
    public String parent_name;
    public String contact2;
    public String getlongName() {
        return account_code + "-" + account_name;
    }
    public byte account_type ;
    public byte net_type ;
    public double balance;
    public byte[] logo ;
}

/*

public class account_full_info
{
    public String account_id ;
    public String account_code ;
    public String account_name ;
    public String account_foreign_name ;
    public String account_parent ;
    public String parent_name ;
    public byte account_type ;
    public int close_with;
    public String close_with_dec ;
    public byte net_type ;
    public String net_type_desc ;
    public double account_limit ;
    public String contact1;
    public String contact2 ;
    public String EMAIL ;
    public String account_address ;
    public String NOTES ;
    public String company_name ;
    public String job_title;
    public String country ;
    public String city;
    public String website ;
    public boolean inactive ;
    public String create_user ;
    public Date create_date ;
    public String modify_user ;
    public Date last_modify_date;
    public Date last_check_date;
    public double last_check_in;
    public double last_check_out ;
    public double sum_in ;
    public double sum_out;
    public Date last_move_date;
    public byte[] logo ;
}
*/
